package net.stuxcrystal.minehack.mineping.strategy.pooled;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.stuxcrystal.minehack.clparser.OptionBuilder;
import net.stuxcrystal.minehack.clparser.OptionParser;
import net.stuxcrystal.minehack.clparser.ParserResult;
import net.stuxcrystal.minehack.clparser.types.IntegerType;
import net.stuxcrystal.minehack.mineping.MinePing;
import net.stuxcrystal.minehack.mineping.MinePingInstances;
import net.stuxcrystal.minehack.mineping.api.Strategy;

public class PoolStrategy implements Strategy {

	private ExecutorService executor;

	private int maxQueue;

	private List<PingRunnable> running;

	public String getName() {
		return "pool";
	}

	public void execute() {
		running = new ArrayList<PingRunnable>(maxQueue);

		MinePing minePing = MinePingInstances.getMinePing();
		InetSocketAddress nextAddress = null;

		while (true) {
			nextAddress = minePing.getNextAddress();
			if (nextAddress == null)
				break;

			while (running.size() > maxQueue) sleep();

			PingRunnable pr = new PingRunnable(nextAddress, minePing);
			executor.execute(pr);
			running.add(pr);
		}

		executor.shutdown();

		while (!executor.isTerminated()) sleep();
	}

	public void signalStop(PingRunnable runnable) {
		synchronized (running) {
			running.remove(runnable);
		}
	}

	private void sleep() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void registerCommandLineArguments(OptionParser parser) {
		parser.addOption(new OptionBuilder("max-threads").hasArgument(true).setType(IntegerType.INSTANCE).setDefault(20).create());
		parser.addOption(new OptionBuilder("max-requests").hasArgument(true).setType(IntegerType.INSTANCE).setDefault(500).create());
	}

	public void parseCommandLine(ParserResult result) {

		int maxThreads = result.getValue("max-threads");

		if (maxThreads <= 0)
			executor = Executors.newCachedThreadPool();
		else
			executor = Executors.newFixedThreadPool(maxThreads);

		maxQueue = result.getValue("max-requests");

	}

}

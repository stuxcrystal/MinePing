package net.stuxcrystal.minehack.mineping.strategy.pooled;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.stuxcrystal.minehack.clparser.OptionBuilder;
import net.stuxcrystal.minehack.clparser.OptionParser;
import net.stuxcrystal.minehack.clparser.ParserResult;
import net.stuxcrystal.minehack.clparser.types.IntegerType;
import net.stuxcrystal.minehack.mineping.api.Strategy;

public class PoolStrategy implements Strategy {

	ExecutorService executor;

	int maxQueue;

	private int maxThreads;

	/**
	 * True, if the pool is executing its last requests.
	 */
	boolean inGlowOut = false;

	List<PingRunnable> running;

	public String getName() {
		return "pool";
	}

	public void signalStop(PingRunnable runnable) {
		synchronized (running) {
			running.remove(runnable);
		}
	}

	public void registerCommandLineArguments(OptionParser parser) {
		parser.addOption(new OptionBuilder("max-threads").hasArgument(true).setType(IntegerType.INSTANCE).setDefault(20).create());
		parser.addOption(new OptionBuilder("max-requests").hasArgument(true).setType(IntegerType.INSTANCE).setDefault(500).create());
	}

	public void parseCommandLine(ParserResult result) {
		maxThreads = result.getValue("max-threads");
		maxQueue = result.getValue("max-requests");
	}

	public void start() {
		running = new ArrayList<PingRunnable>(maxQueue);

		if (maxThreads <= 0)
			executor = Executors.newCachedThreadPool();
		else
			executor = Executors.newFixedThreadPool(maxThreads);

	}

	public boolean isRunning() {
		return !executor.isTerminated();
	}

	void gracefulInterrupt() {
		executor.shutdown();
		inGlowOut = true;
	}

	public void interrupt() {
		executor.shutdownNow();
	}

}

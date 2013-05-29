package net.stuxcrystal.minehack.mineping.strategy.staticthread;

import net.stuxcrystal.minehack.clparser.OptionBuilder;
import net.stuxcrystal.minehack.clparser.OptionParser;
import net.stuxcrystal.minehack.clparser.ParserResult;
import net.stuxcrystal.minehack.clparser.types.IntegerType;
import net.stuxcrystal.minehack.mineping.MinePingInstances;
import net.stuxcrystal.minehack.mineping.api.Strategy;

public class StaticStrategy implements Strategy {

	private int cThreads;

	private Thread[] threads;

	public String getName() {
		return "static";
	}

	public void registerCommandLineArguments(OptionParser parser) {
		parser.addOption(new OptionBuilder("threads").hasArgument(true).setType(IntegerType.INSTANCE).setDefault(10).create());
	}

	public void parseCommandLine(ParserResult result) {
		cThreads = result.getValue("threads");

	}

	public void start() {
		threads = new PingThread[cThreads];

		for (int i = 0; i<cThreads; i++) {
			threads[i] = new PingThread(MinePingInstances.getMinePing());
			threads[i].start();
		}
	}

	public boolean isRunning() {

		for (Thread thread : threads)
			if (thread.isAlive())
				return true;

		return false;
	}

	public void interrupt() {
		for (int i = 0; i<cThreads; i++)
			threads[i].interrupt();

		for (int i = 0; i<cThreads; i++) {
			if (threads[i].isAlive()) {
				try {
					threads[i].join();
				} catch (InterruptedException e) {
					break;
				}
			}
		}
	}
}

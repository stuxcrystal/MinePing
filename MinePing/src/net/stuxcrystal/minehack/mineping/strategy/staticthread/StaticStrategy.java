package net.stuxcrystal.minehack.mineping.strategy.staticthread;

import net.stuxcrystal.minehack.clparser.OptionBuilder;
import net.stuxcrystal.minehack.clparser.OptionParser;
import net.stuxcrystal.minehack.clparser.ParserResult;
import net.stuxcrystal.minehack.clparser.types.IntegerType;
import net.stuxcrystal.minehack.mineping.MinePingInstances;
import net.stuxcrystal.minehack.mineping.api.Strategy;

public class StaticStrategy implements Strategy {

	private int threads;

	public String getName() {
		return "static";
	}

	public void execute() {

		PingThread[] pingThreads = new PingThread[threads];

		for (int i = 0; i<threads; i++) {
			pingThreads[i] = new PingThread(MinePingInstances.getMinePing());
			pingThreads[i].start();
		}

		for (int i = 0; i<threads; i++) {
			if (pingThreads[i].isAlive()) {
				try {
					pingThreads[i].join();
				} catch (InterruptedException e) {
					break;
				}
			}
		}
	}

	public void registerCommandLineArguments(OptionParser parser) {
		parser.addOption(new OptionBuilder("threads").hasArgument(true).setType(IntegerType.INSTANCE).setDefault(10).create());
	}

	public void parseCommandLine(ParserResult result) {
		threads = result.getValue("threads");

	}
}

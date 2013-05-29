package net.stuxcrystal.minehack.mineping.api;

import net.stuxcrystal.minehack.clparser.OptionParser;
import net.stuxcrystal.minehack.clparser.ParserResult;

/**
 * The ping thread handling the way mineping is pinging the
 * threads.
 * @author StuxCrystal
 *
 */
public interface Strategy {

	/**
	 * The name of the strategy.
	 */
	public String getName();

	/**
	 * Start pinging.
	 */
	public void start();

	/**
	 * Checks if the ping-strategy is running.
	 * @return true if the software is pinging.
	 */
	public boolean isRunning();

	/**
	 * Interrupt the strategy.<p />
	 * All threads have to be stopped when the method returns.
	 */
	public void interrupt();

	/**
	 * Registers the arguments for this strategy.
	 * @param parser
	 */
	public void registerCommandLineArguments(OptionParser parser);

	/**
	 * Parses the command-line.
	 * @param result
	 */
	public void parseCommandLine(ParserResult result);
}

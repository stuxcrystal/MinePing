package net.stuxcrystal.minehack.mineping.api;

import net.stuxcrystal.minehack.clparser.OptionParser;
import net.stuxcrystal.minehack.clparser.ParserResult;

/**
 * Represents a resolver for ip-ranges.
 * @author StuxCrystal
 *
 */
public interface Resolver {

	/**
	 * @return The name of the resolver.
	 */
	public String getName();

	/**
	 * Returns the ranges to be pinged.
	 */
	public AddressIterator getRangeIterator(int[] ports);

	/**
	 * Registers the arguments for this pinger.
	 * @param parser
	 */
	public void registerCommandLineArguments(OptionParser parser);

	/**
	 * Parses the command-line.
	 * @param result
	 */
	public void parseCommandLine(ParserResult result);

}

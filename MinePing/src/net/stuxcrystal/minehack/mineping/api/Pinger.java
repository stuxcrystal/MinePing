package net.stuxcrystal.minehack.mineping.api;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;
import java.util.Map;

import net.stuxcrystal.minehack.clparser.OptionParser;
import net.stuxcrystal.minehack.clparser.ParserResult;

public interface Pinger {

	/**
	 * The name of the pinger.
	 * @return
	 */
	public String getName();

	/**
	 * Returns the column-lines.
	 * @return
	 */
	public List<String> getColumns();

	/**
	 * Prepare for start.
	 */
	public void prepare();

	/**
	 * Prepares the connector to be stopped.
	 */
	public void end();

	/**
	 * Connects to the server.
	 * @param socket
	 * @return
	 * @throws IOException
	 */
	public Map<String, String> executePing(InetAddress address, int port) throws IOException;

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

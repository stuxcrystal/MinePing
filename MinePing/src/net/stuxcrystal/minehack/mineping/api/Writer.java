package net.stuxcrystal.minehack.mineping.api;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import net.stuxcrystal.minehack.clparser.OptionParser;
import net.stuxcrystal.minehack.clparser.ParserResult;

public interface Writer {

	/**
	 * The name of the pinger. This represents the writer inside the commandline.
	 * @return
	 */
	public String getName();

	/**
	 * Prepare for start.
	 */
	public void onStart(OutputStream stream, List<String> header) throws IOException;

	/**
	 * Adds a ping.
	 * @param values
	 */
	public void add(Map<String, String> values) throws IOException;

	/**
	 * Flushes the stream.<p />
	 *
	 * Ignore this command if this writer cannot flush the current values.
	 * @param output
	 * @throws IOException
	 */
	public void flush() throws IOException;

	/**
	 * Close the output.
	 * @param stram
	 * @throws IOException
	 */
	public void close() throws IOException;

	/**
	 * Registers the arguments for this writer.
	 * @param parser
	 */
	public void registerCommandLineArguments(OptionParser parser);

	/**
	 * Parses the command-line.
	 * @param result
	 */
	public void parseCommandLine(ParserResult result);

}

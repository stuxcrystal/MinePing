package net.stuxcrystal.minehack.mineping.api;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SocketChannel;

import net.stuxcrystal.minehack.clparser.OptionParser;
import net.stuxcrystal.minehack.clparser.ParserResult;

/**
 * A connector manages how the sockets are connected.
 * @author StuxCrystal
 *
 */
public interface Connector {

	/**
	 * The name of the connector.
	 */
	public String getName();

	/**
	 * Starts the connector.<p />
	 * Shouldn't block.
	 */
	public void start();

	/**
	 * Stops the connector.<p />
	 * Shouldn't block.
	 */
	public void stop();

	/**
	 * Connects to the given port.<p />
	 * This method can block.
	 * @param address the endpoint to connect.
	 * @return a Socket or null if the connection failed..
	 */
	public Socket connectSocket(InetSocketAddress address);

	/**
	 * Creates a SocketChannel connected to the given SocketAddress.
	 * @param address
	 * @return a SocketChannel or null if the connection failed.
	 */
	public SocketChannel connectChannel(InetSocketAddress address);

	/**
	 * Registers the arguments for this connector.
	 * @param parser
	 */
	public void registerCommandLineArguments(OptionParser parser);

	/**
	 * Parses the command-line.
	 * @param result
	 */
	public void parseCommandLine(ParserResult result);

}

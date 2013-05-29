package net.stuxcrystal.minehack.mineping.api;

import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Eventhandler that can handle every event that can
 * occur in MinePing.
 * @author StuxCrystal
 *
 */
public interface MinePingEventHandler {

	/**
	 * Called before a thread gets a value assigned.
	 * @param address The address the thread will get.
	 * @return true if the ip should be assigned; false if not.
	 */
	public boolean preAssign(InetSocketAddress address);

	/**
	 * Called directly before a ip will connect.
	 * @param address The Endpoint the socket will connect to.
	 * @returns true if the ip should be connected; false if not.
	 */
	public boolean preConnect(InetSocketAddress address);

	/**
	 * Called after the connection was established.<p />
	 * Please note that it would result in exceptions if
	 *
	 * @param socket The socket object that establishes the connection.
	 */
	public void postConnect(Socket socket);

	/**
	 * Called when all parts of the software were started.
	 */
	public void start();

	/**
	 * Executes every tick as long the ping thread runs.
	 */
	public void tick();

	/**
	 * Called when the software is entirely stopped.
	 */
	public void stop();
}

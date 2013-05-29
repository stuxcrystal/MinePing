package net.stuxcrystal.minehack.mineping.strategy.pooled;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Map;
import java.util.logging.Level;

import net.stuxcrystal.minehack.mineping.MinePing;

/**
 * Implementation of a single ping.
 * @author StuxCrystal
 *
 */
public class PingRunnable implements Runnable {

	/**
	 * The address to be pinged.
	 */
	private InetSocketAddress address;

	/**
	 * The MinePing instance.
	 */
	private MinePing mp;

	public PingRunnable(InetSocketAddress address, MinePing mp) {
		this.address = address;
		this.mp = mp;
	}

	/**
	 * Executes a single ping.
	 */
	public void run() {

		Map<String, String> data = null;

		try {
			data = mp.pinger.executePing(address.getAddress(), address.getPort());
		} catch (SocketException e) {
			mp.logger.severe("Failed to create a connection" + e);
		} catch (IOException e) {
			mp.logger.warning("Failed to ping address (" + address + "): " + e.toString());
			return;
		} catch (Throwable e) {
			mp.logger.log(Level.SEVERE, "Exception occured [" + address + "].", e);
		}

		if (data != null)
			mp.saveData(data);
		else
			mp.logger.info("No Server under: " + address);

	}



}

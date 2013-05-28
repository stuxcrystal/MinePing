package net.stuxcrystal.minehack.mineping.core;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Map;
import java.util.logging.Level;

import net.stuxcrystal.minehack.mineping.MinePing;

public class PingThread extends Thread {

	/**
	 * The current thread id.
	 */
	private static int THREAD_ID = 0;

	/**
	 * The Threadgroup.
	 */
	private static ThreadGroup GROUP = new ThreadGroup("PingThread");

	MinePing mp;

	public PingThread(MinePing mineping) {
		super(GROUP, getThreadName());
		mp = mineping;
	}

	private static synchronized String getThreadName() {
		return "PingThread::"+(THREAD_ID++);
	}

	public void run() {
		InetSocketAddress address = null;
		Map<String, String> data = null;

		while((address = mp.getNextAddress()) != null) {
			mp.logger.fine("Ping address: " + address);

			try {
				data = mp.pinger.executePing(address.getAddress(), address.getPort());
			} catch (SocketException e) {
				mp.logger.severe("Failed to create a connection" + e);
			} catch (IOException e) {
				mp.logger.warning("Failed to ping address (" + address + "): " + e.toString());
				continue;
			} catch (Throwable e) {
				mp.logger.log(Level.SEVERE, "Exception occured [" + address + "].", e);
			}

			if (data != null)
				mp.saveData(data);
			else
				mp.logger.info("No Server under: " + address);

			// Free the Ram.
			System.gc();
		}
	}
}

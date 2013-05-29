package net.stuxcrystal.minehack.mineping.connector.pooled;

import java.net.InetSocketAddress;

/**
 * Repesents a request.
 * @author StuxCrystal
 *
 * @param <T>
 */
public abstract class Request<T> implements Runnable {

	/**
	 * The address to connect to.
	 */
	protected InetSocketAddress address;

	protected T result = null;

	Request(InetSocketAddress address) {
		this.address = address;
	}

	public void run() {

	}

	/**
	 * Directly connects to the given endpoint.
	 * @return
	 */
	public abstract T connect();

}

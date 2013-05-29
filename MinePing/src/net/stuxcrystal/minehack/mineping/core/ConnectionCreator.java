package net.stuxcrystal.minehack.mineping.core;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

import net.stuxcrystal.minehack.mineping.MinePing;

/**
 * Infrastructure to connect to other sockets.
 * @author StuxCrystal
 *
 */
public class ConnectionCreator implements Runnable {

	private class ConnectionRequest {

		private InetSocketAddress address;

		private Object socket = null;

		private boolean isChannel = false;

		public ConnectionRequest(InetSocketAddress address) {
			this.address = address;
		}
	}

	/**
	 * All requests to be created.
	 */
	private BlockingQueue<ConnectionRequest> sockets = new LinkedBlockingQueue<ConnectionRequest>();

	/**
	 * The timeout
	 */
	private int timeout;

	/**
	 * The logger that logs the data.
	 */
	private Logger logger;

	/**
	 * True if "--connections=0"
	 */
	private boolean noProducers = false;

	private static int THREAD_ID = 0;
	private static final Object lock = new Object();

	public ConnectionCreator(MinePing mp) {
		this.timeout = mp.timeout * 1000;
		this.logger  = mp.logger;
	}

	/**
	 * Waits for requests and handles these.
	 */
	public void run() {

		while (!Thread.currentThread().isInterrupted()) {
			ConnectionRequest request = null;

			try {
				request = sockets.take();
			} catch (InterruptedException e) {
				break;
			}

			if (request.isChannel) {
				connectAsChannel(request);
			} else {
				connectAsSocket(request);
			}

			synchronized(request) {
				request.notify();
			}
		}


	}

	/**
	 * Established the connection and uses a Socket for it.
	 * @param request
	 */
	private void connectAsSocket(ConnectionRequest request) {
		request.socket = new Socket();

		try {
			((Socket) request.socket).connect(request.address, timeout);
		} catch (IOException e) {
			logger.finer("Failed to connect to "+request.address+" :> " +e);
			request.socket = null;
		}
	}

	/**
	 * Establishes the connection and uses a SocketChannel for it.
	 * @param request
	 */
	private void connectAsChannel(ConnectionRequest request) {

		try {
			request.socket = SocketChannel.open(request.address);
		} catch (IOException e) {
			logger.finer("Failed to connect to "+request.address+" :> " +e);
			request.socket = null;
		}

	}

	/**
	 * Connects to a SocketAddress and returns a Socket.
	 * @param socket
	 * @return
	 */
	public Socket connect(InetSocketAddress socket) {
		ConnectionRequest request = new ConnectionRequest(socket);

		if (noProducers) {
			connectAsSocket(request);
			return (Socket) request.socket;
		}

		if (!waitForRequest(request))
			return null;

		return (Socket) request.socket;
	}

	/**
	 * Connects to the given SocketAddress and returns a SocketChannel.
	 * @param socket
	 * @return
	 */
	public SocketChannel connectChannel(InetSocketAddress socket) {
		ConnectionRequest request = new ConnectionRequest(socket);
		request.isChannel = true;

		if (noProducers) {
			connectAsChannel(request);
			return (SocketChannel) request.socket;
		}

		if (!waitForRequest(request))
			return null;

		return (SocketChannel) request.socket;
	}

	/**
	 * Waits for the request to be processed.
	 * @param request
	 * @return
	 */
	private boolean waitForRequest(ConnectionRequest request) {
		sockets.add(request);

		try {
			synchronized (request) {
				request.wait();
			}
		} catch (InterruptedException e) {
			return false;
		}

		return true;
	}

	/**
	 * Creats the given amount of threads.
	 * @param count
	 */
	public void createThreads(int count) {
		if (count <= 0) {
			// Connect inside the threads.
			this.noProducers = true;
		} else {
			// Create the producer-threads.
			for (int i = 0; i<count; i++)
				startNewThread();
		}
	}

	/**
	 * Starts a new thread.
	 * @return
	 */
	private Thread startNewThread() {
		Thread thread = new Thread(this);
		synchronized (lock) { thread.setName("ConnectThread::"+(THREAD_ID++)); };
		thread.setDaemon(true);
		thread.start();
		return thread;
	}



}

package net.stuxcrystal.minehack.mineping.connector.staticthread;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

import net.stuxcrystal.minehack.clparser.OptionBuilder;
import net.stuxcrystal.minehack.clparser.OptionParser;
import net.stuxcrystal.minehack.clparser.ParserResult;
import net.stuxcrystal.minehack.clparser.types.IntegerType;
import net.stuxcrystal.minehack.mineping.MinePing;
import net.stuxcrystal.minehack.mineping.api.Connector;

public class DefaultConnector implements Connector, Runnable {

	private class ConnectionRequest {

		private InetSocketAddress address;

		private Object socket = null;

		private boolean isChannel = false;

		public ConnectionRequest(InetSocketAddress address) {
			this.address = address;
		}
	}

	public String getName() {
		return "static";
	}

	/**
	 * All requests to be created.
	 */
	private BlockingQueue<ConnectionRequest> sockets = new LinkedBlockingQueue<ConnectionRequest>();

	/**
	 * All threads that are connecting sockets.
	 */
	private Thread[] threads;

	/**
	 * The timeout
	 */
	private int timeout;

	/**
	 * The logger that logs the data.
	 */
	private Logger logger;

	/**
	 * Count of threads that are connecting.
	 */
	private int cThreads;

	private static int THREAD_ID = 0;
	private static final Object lock = new Object();

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
				request.notifyAll();
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
	public Socket connectSocket(InetSocketAddress socket) {
		ConnectionRequest request = new ConnectionRequest(socket);

		if (cThreads == 0) {
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

		if (cThreads == 0) {
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
	 * Creates the given amount of threads.
	 * @param count
	 */
	public void start() {
		this.logger = MinePing.staticlogger;

		threads = new Thread[cThreads];
		for (int i = 0; i<cThreads; i++)
			threads[i] = startNewThread();
	}

	public void stop() {
		for (Thread thread : threads)
			thread.interrupt();
	}

	/**
	 * Starts a new thread.
	 * @return
	 */
	private Thread startNewThread() {
		Thread thread = new Thread(this);
		synchronized (lock) { thread.setName("ConnectThread::"+(THREAD_ID++)); };
		thread.start();
		return thread;
	}


	public void registerCommandLineArguments(OptionParser parser) {
		parser.addOption(new OptionBuilder("connections").setType(IntegerType.INSTANCE).setDefault(10).create());
		parser.addOption(new OptionBuilder("timeout").hasArgument(true).setType(IntegerType.INSTANCE).setDefault(1000).create());
	}

	public void parseCommandLine(ParserResult result) {
		cThreads = result.getValue("connections");
		timeout = result.getValue("timeout");
	}


}

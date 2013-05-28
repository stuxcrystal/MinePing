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

	private BlockingQueue<ConnectionRequest> sockets = new LinkedBlockingQueue<ConnectionRequest>();

	private int timeout;

	private Logger logger;

	private static int THREAD_ID = 0;
	private static final Object lock = new Object();

	public ConnectionCreator(MinePing mp) {
		this.timeout = mp.timeout * 1000;
		this.logger  = mp.logger;
	}

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

	private void connectAsSocket(ConnectionRequest request) {
		request.socket = new Socket();

		try {
			((Socket) request.socket).connect(request.address, timeout);
		} catch (IOException e) {
			logger.finer("Failed to connect to "+request.address+" :> " +e);
			request.socket = null;
		}
	}

	private void connectAsChannel(ConnectionRequest request) {

		try {
			request.socket = SocketChannel.open(request.address);
		} catch (IOException e) {
			logger.finer("Failed to connect to "+request.address+" :> " +e);
			request.socket = null;
		}

	}

	public Socket connect(InetSocketAddress socket) {
		ConnectionRequest request = new ConnectionRequest(socket);
		sockets.add(request);

		try {
			synchronized (request) {
				request.wait();
			}
		} catch (InterruptedException e) {

			return null;
		}

		return (Socket) request.socket;
	}

	public SocketChannel connectChannel(InetSocketAddress socket) {
		ConnectionRequest request = new ConnectionRequest(socket);
		request.isChannel = true;
		sockets.add(request);

		try {
			synchronized (request) {
				request.wait();
			}
		} catch (InterruptedException e) {

			return null;
		}

		return (SocketChannel) request.socket;
	}

	public Thread startNewThread() {
		Thread thread = new Thread(this);
		synchronized (lock) { thread.setName("ConnectThread::"+(THREAD_ID++)); };
		thread.setDaemon(true);
		thread.start();
		return thread;
	}



}

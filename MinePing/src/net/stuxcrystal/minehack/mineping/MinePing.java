package net.stuxcrystal.minehack.mineping;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

import net.stuxcrystal.minehack.mineping.api.AddressIterator;
import net.stuxcrystal.minehack.mineping.api.Pinger;
import net.stuxcrystal.minehack.mineping.api.Resolver;
import net.stuxcrystal.minehack.mineping.api.Writer;
import net.stuxcrystal.minehack.mineping.core.ConnectionCreator;
import net.stuxcrystal.minehack.mineping.core.PingThread;
import net.stuxcrystal.minehack.mineping.core.WriteThread;
import net.stuxcrystal.minehack.mineping.resolvers.defaultresolver.RangeResolver;

/**
 * Mine-Ping Base-Class.
 * @author StuxCrystal
 *
 */
public class MinePing {

	/**
	 * The Pinger pinging the ip-ranges.
	 */
	public Pinger pinger;

	/**
	 * The Writer writing the results.
	 */
	public Writer writer;

	/**
	 * The resolver provinding the ips for the threads.
	 */
	public Resolver resolver;

	/**
	 * The Output-Stream of the data.
	 */
	public OutputStream output;

	/**
	 * The Logger to log into.<p />
	 *
	 * To be used before MinePing is instantiated.
	 */
	public static Logger staticlogger;

	/**
	 * The Logger to log into.
	 */
	public Logger logger;

	/**
	 * The Port-Ranges.
	 */
	public int[] ports;

	/**
	 * The count of threads that are pinging a ip.
	 */
	public int threads;

	/**
	 * All ranges to be pinged.
	 */
	private AddressIterator iterator;

	/**
	 * All ping threads.
	 */
	private Thread[] pingThreads;

	/**
	 * The thread that is started when the file has to be flushed.
	 */
	private WriteThread writeThread;

	/**
	 * The thread that creates a connection.
	 */
	public ConnectionCreator connector;

	/**
	 * The timeout of the sockets.
	 */
	public int timeout;

	/**
	 * Connection threads.
	 */
	private int connections;

	/**
	 * The count of results before the data is flushed.
	 */
	private int flushAfter;

	/**
	 * Initializes Mine-Ping.
	 * @param pinger
	 * @param writer
	 * @param output
	 * @param logger
	 * @param ranges
	 * @param ports
	 */
	MinePing
	(
	  Pinger pinger, Writer writer, Resolver resolver, OutputStream output, Logger logger, String ports,
	  int threads, int timeout, int connections, int flushAfter
	) {
		this.pinger      = pinger;
		this.writer      = writer;
		this.resolver    = resolver;
		this.output      = output;
		this.logger      = logger;
		this.ports       = RangeResolver.parsePortRange(ports);
		this.threads     = threads;
		this.pingThreads = new Thread[this.threads];
		this.writeThread = new WriteThread(writer, logger, flushAfter);
		this.timeout     = timeout;
		this.connector   = new ConnectionCreator(this);
		this.connections = connections;
		this.flushAfter  = flushAfter;
	}

	/**
	 * Returns the next address to be pinged.
	 * @return An InetSocketAddress or null, if there is no additional address to be pinged.
	 */
	public synchronized InetSocketAddress getNextAddress() {
		if (!iterator.hasMoreElements())
			return null;

		try {
			return iterator.nextElement();
		} catch (NoSuchElementException e) {
			return null;
		}
	}

	/**
	 * Stores the data of a ping if there is one.
	 * @param values
	 */
	public void saveData(Map<String, String> values) {
		this.writeThread.add(values);
	}

	/**
	 * Starts pinging.
	 */
	public void startPing() {
		try {
			this.writer.onStart(this.output, this.pinger.getColumns());
		} catch (IOException e) {
			this.logger.severe("Initiation of the writer failed!");
			return;
		}

		this.pinger.prepare();
		iterator = this.resolver.getRangeIterator(ports);

		this.logger.info("MinePing 3 Resurrection");
		this.logger.info("Build: 10");

		this.writeThread.start();

		this.connector.createThreads(this.connections);

		for (int i = 0; i<threads; i++) {
			this.pingThreads[i] = new PingThread(this);
			this.pingThreads[i].start();
		}

		for (int i = 0; i<threads; i++) {
			if (this.pingThreads[i].isAlive())
				try {
					this.pingThreads[i].join();
				} catch (InterruptedException e) {
					break;
				}
		}

		this.writeThread.interrupt();

		try {
			this.writeThread.join();
		} catch (InterruptedException e) {
			logger.warning("Writer-Thread was interrupted.");
		}
	}
}

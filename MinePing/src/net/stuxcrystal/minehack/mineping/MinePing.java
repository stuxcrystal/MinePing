package net.stuxcrystal.minehack.mineping;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

import net.stuxcrystal.minehack.mineping.addons.ExtensionManager;
import net.stuxcrystal.minehack.mineping.api.AddressIterator;
import net.stuxcrystal.minehack.mineping.api.Connector;
import net.stuxcrystal.minehack.mineping.api.Extension;
import net.stuxcrystal.minehack.mineping.api.Pinger;
import net.stuxcrystal.minehack.mineping.api.Resolver;
import net.stuxcrystal.minehack.mineping.api.Strategy;
import net.stuxcrystal.minehack.mineping.api.Writer;
import net.stuxcrystal.minehack.mineping.events.ConnectorWrapper;
import net.stuxcrystal.minehack.mineping.events.EventManager;
import net.stuxcrystal.minehack.mineping.events.IteratorWrapper;
import net.stuxcrystal.minehack.mineping.events.PingerWrapper;
import net.stuxcrystal.minehack.mineping.events.WriterWrapper;
import net.stuxcrystal.minehack.mineping.resolvers.defaultresolver.RangeResolver;

/**
 * Mine-Ping Base-Class.
 * @author StuxCrystal
 *
 */
public class MinePing {

	//////////////////////////////////////////////////////////////////////////
	// API-Methods.                                                         //
	//////////////////////////////////////////////////////////////////////////

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
	 * The strategy how the connections are distributed.
	 */
	public Strategy strategy;

	/**
	 * The thread that creates a connection.
	 */
	public Connector connector;

	/**
	 * All ranges to be pinged.
	 */
	private AddressIterator iterator;

	//////////////////////////////////////////////////////////////////////////
	// Output                                                               //
	//////////////////////////////////////////////////////////////////////////

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

	//////////////////////////////////////////////////////////////////////////
	// Miscallanious stuff.                                                 //
	//////////////////////////////////////////////////////////////////////////

	/**
	 * The Port-Ranges.
	 */
	public int[] ports;

	/**
	 * A EventManager to manage all EventHandler.
	 */
	private EventManager events = new EventManager();

	/**
	 * The extension-manager that handles extensions.
	 */
	private ExtensionManager manager;

	/**
	 * The thread that is started when the file has to be flushed.
	 */
	private WriteThread writeThread;

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
	  ExtensionManager manager,
	  Pinger pinger, Writer writer, Resolver resolver,
	  Strategy strategy, Connector connector,
	  OutputStream output, Logger logger, String ports,
	  int flushAfter
	) {
		this.manager     = manager;

		this.pinger      = new PingerWrapper(events, pinger);
		this.writer      = new WriterWrapper(events, writer);
		this.connector   = new ConnectorWrapper(events, connector);
		this.strategy    = strategy;
		this.resolver    = resolver;

		this.output      = output;
		this.logger      = logger;
		this.ports       = RangeResolver.parsePortRange(ports);
		this.writeThread = new WriteThread(writer, logger, flushAfter);
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
	 * Returns the EventManager of the software.
	 * @return
	 */
	public EventManager getEventManager() {
		return this.events;
	}

	/**
	 * Starts pinging.
	 */
	public void startPing() {
		this.logger.info("MinePing 3 Resurrection");
		this.logger.info("Build: 13");

		// Information output
		this.logger.finest("Current writer:    " + writer.getName());
		this.logger.finest("Current pinger:    " + pinger.getName());
		this.logger.finest("Current resolver:  " + resolver.getName());
		this.logger.finest("Current strategy:  " + strategy.getName());
		this.logger.finest("Current connector: " + connector.getName());

		// Register events.
		for (Extension extension : this.manager.getExtensions())
			extension.registerEvents(events);

		// Preparation state.
		try {
			this.writer.onStart(this.output, this.pinger.getColumns());
		} catch (IOException e) {
			this.logger.severe("Initiation of the writer failed!");
			return;
		}
		this.pinger.prepare();
		iterator = new IteratorWrapper(events, this.resolver.getRangeIterator(ports));

		// Execution state.
		this.writeThread.start();
		this.connector.start();
		this.strategy.start();
		this.events.start();

		// Wait for execution
		while (!this.strategy.isRunning()) {

			this.events.tick();

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				break;
			}
		}

		this.strategy.interrupt();

		// Clingout state.
		this.connector.stop();
		this.pinger.end();
		this.events.stop();

		// Stop writer.
		this.writeThread.interrupt();
		try {
			this.writeThread.join();
		} catch (InterruptedException e) {
			logger.warning("Writer-Thread was interrupted.");
		}
	}
}

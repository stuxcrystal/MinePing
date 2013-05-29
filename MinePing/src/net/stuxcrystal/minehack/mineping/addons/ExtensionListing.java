package net.stuxcrystal.minehack.mineping.addons;

import java.util.LinkedList;
import java.util.List;

import net.stuxcrystal.minehack.mineping.api.Connector;
import net.stuxcrystal.minehack.mineping.api.Extension;
import net.stuxcrystal.minehack.mineping.api.Pinger;
import net.stuxcrystal.minehack.mineping.api.Resolver;
import net.stuxcrystal.minehack.mineping.api.Strategy;
import net.stuxcrystal.minehack.mineping.api.Writer;

/**
 * Lists all added Pinger, Resolver, Strategies, Writers and Connectors
 * und handles them.
 * @author StuxCrystal
 *
 */
public class ExtensionListing {

	/**
	 * All available pingers.
	 */
	public List<Pinger> pingers = new LinkedList<Pinger>();

	/**
	 * All available writers.
	 */
	public List<Writer> writers = new LinkedList<Writer>();

	/**
	 * A list of resolvers.
	 */
	public List<Resolver> resolvers = new LinkedList<Resolver>();

	/**
	 * A list of recognized strategies.
	 */
	public List<Strategy> strategies = new LinkedList<Strategy>();

	/**
	 * A list of known connectors.
	 */
	public List<Connector> connectors = new LinkedList<Connector>();

	/**
	 * Searches the pinger with the given name.
	 * @param name The name to search.
	 * @return The pinger.
	 * @throws RuntimeException if no pinger was found.
	 */
	public Pinger getPinger(String name) throws RuntimeException {
		for (Pinger pinger : pingers) {
			if (pinger.getName().equalsIgnoreCase(name))
				return pinger;
		}

		throw new RuntimeException("Pinger not found.");
	}

	/**
	 * Searches the resolver with the given name.
	 * @param name The name to search.
	 * @return The resolver.
	 * @throws RuntimeException if no resolver was found.
	 */
	public Resolver getResolver(String name) throws RuntimeException {
		for (Resolver resolver : resolvers) {
			if (resolver.getName().equalsIgnoreCase(name)) {
				return resolver;
			}
		}

		throw new RuntimeException("Resolver not found.");
	}

	/**
	 * Searches the writer with the given name.
	 * @param name The name to search.
	 * @return The writer.
	 * @throws RuntimeException if no writer was found.
	 */
	public Writer getWriter(String name) throws RuntimeException {
		for (Writer writer : writers) {
			if (writer.getName().equalsIgnoreCase(name))
				return writer;
		}

		throw new RuntimeException("Writer not found.");
	}

	/**
	 * Searches the strategy with the given name.
	 * @param name The name to search.
	 * @return The strategy.
	 * @throws RuntimeException if no strategy was found.
	 */
	public Strategy getStrategy(String name) {
		for (Strategy strategy : strategies) {
			if (strategy.getName().equalsIgnoreCase(name))
				return strategy;
		}

		throw new RuntimeException("Strategy not recognized.");
	}

	/**
	 * Searches the connector with the given name.
	 * @param name The name to search.
	 * @return The pinger.
	 * @throws RuntimeException if no connector was found.
	 */
	public Connector getConnector(String name) {
		for (Connector connector : connectors) {
			if (connector.getName().equalsIgnoreCase(name))
				return connector;
		}

		throw new RuntimeException("Connector unknown.");
	}

	/**
	 * Registers a extension to the system.
	 * @param extension
	 */
	public void loadExtension(Extension extension) {
		List<Pinger> p    = extension.getPingers();
		List<Writer> w    = extension.getWriters();
		List<Resolver> r  = extension.getResolvers();
		List<Strategy> s  = extension.getStrategies();
		List<Connector> c = extension.getConnectors();

		if (p!=null) pingers.addAll(p);
		if (w!=null) writers.addAll(w);
		if (r!=null) resolvers.addAll(r);
		if (s!=null) strategies.addAll(s);
		if (c!=null) connectors.addAll(c);
	}

}

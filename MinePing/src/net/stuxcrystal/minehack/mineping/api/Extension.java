package net.stuxcrystal.minehack.mineping.api;

import java.util.List;

/**
 * Implements an extension.
 * @author StuxCrystal
 *
 */
public interface Extension {

	/**
	 * Returns the implemented pingers.
	 */
	public List<Pinger> getPingers();

	/**
	 * Returns all implemented writers.
	 */
	public List<Writer> getWriters();

	/**
	 * Returns all resolvers.
	 */
	public List<Resolver> getResolvers();

	/**
	 * Returns a list of implemented strategy.
	 */
	public List<Strategy> getStrategies();

	/**
	 * Returns a list of connectors implemented by this extension.
	 */
	public List<Connector> getConnectors();
}

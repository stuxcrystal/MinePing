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
	 * All supported strategies will be returned here.
	 */
	public List<Strategy> getStrategies();
}

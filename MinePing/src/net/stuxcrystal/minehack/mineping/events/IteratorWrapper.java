package net.stuxcrystal.minehack.mineping.events;

import java.net.InetSocketAddress;

import net.stuxcrystal.minehack.mineping.api.AddressIterator;

/**
 * Wraps an AddressIterator to use the EventSystem of MinePing.
 * @author StuxCrystal
 *
 */
public class IteratorWrapper implements AddressIterator {

	EventManager manager;

	AddressIterator backend;

	public IteratorWrapper(EventManager manager, AddressIterator iterator) {
		this.manager = manager;
		this.backend = iterator;
	}

	public boolean hasMoreElements() {
		return backend.hasMoreElements();
	}

	public boolean hasNext() {
		return this.hasMoreElements();
	}

	/**
	 * Retrieves the next ip as long as all Handlers allow the ip to be
	 * assigned.
	 */
	public InetSocketAddress next() {
		InetSocketAddress address = backend.next();

		while (!manager.preAssign(address))
			address = backend.next();

		return address;
	}

	public InetSocketAddress nextElement() {
		return this.next();
	}

	public void remove() {
		backend.remove();
	}

}

package net.stuxcrystal.minehack.mineping.resolvers.defaultresolver;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.NoSuchElementException;

import net.stuxcrystal.minehack.mineping.api.AddressIterator;

/**
 * An iterator for ranges.
 * @author StuxCrystal
 *
 */
public class RangeIterator implements AddressIterator {

	/**
	 * Iterator for ranges.
	 */
	private byte[][][] ranges = null;

	/**
	 * The count of ports to be tested.
	 */
	private int[] ports = null;

	/**
	 * Current range to be pinged.
	 */
	private int range_id = 0;

	/**
	 * Current port to be pinged.
	 */
	private int port_id = 0;

	/**
	 * The current subrange counter.
	 */
	private int[] subranges = new int[] {0,0,0,0};

	/**
	 * True if the first object was returned by an object.
	 */
	boolean first_returned = false;

	public RangeIterator(byte[][][] ranges, int[] ports) {
		this.ranges = ranges;
		this.ports  = ports;
	}

	public boolean hasMoreElements()  {
		synchronized (this) {

			if (port_id < ports.length-1)
				return true;

			if (range_id < ranges.length-1)
				return true;

			for (int i = 0; i<subranges.length; i++) {
				if (subranges[i] < ranges[range_id][i].length-1)
					return true;
			}

			return false;
		}
	}

	public InetSocketAddress nextElement() throws NoSuchElementException{
		synchronized (this) {

			// If this method wasn't executed before, return the first element.
			if (!first_returned) {
				first_returned = true;
				return createAddress();
			}

			// Calculated the port.
			port_id = (port_id+1)%ports.length;

			// If the port_id is zero try the next id.
			if (port_id == 0) {
				// Was the last id reset?
				boolean reset = true;

				for (int i = subranges.length-1; i>=0; i--) {
					if (reset) {

						// Increment the number. If the number is equal to length of the range,
						// set it to zero.
						subranges[i] = (subranges[i]+1)%ranges[range_id][i].length;

						// If the subrange was not reset, stop the iteration.
						if (subranges[i] != 0) {
							reset = false;
							break;
						}
					}
				}

				// If the A range is reset, the next range will be used if the range exists.
				if (reset) {
					if (range_id < ranges.length-1) {
						// If the current range is not the last range, move to the next range.
						range_id += 1;
					} else {
						// Fail, if this is the last range.
						throw new NoSuchElementException();
					}
				}
			}

			return createAddress();
		}
	}

	public boolean hasNext() {
		return this.hasMoreElements();
	}

	public InetSocketAddress next() {
		return this.nextElement();
	}

	public void remove() {
		throw new UnsupportedOperationException("Underlying datastructure is not supported.");
	}

	private InetSocketAddress createAddress() {
		// Compose the ranges.
		byte[] result = new byte[ranges[range_id].length];
		for (int i = 0; i<subranges.length; i++) {
			result[i] = ranges[range_id][i][subranges[i]];
		}
		try {
			return new InetSocketAddress(InetAddress.getByAddress(result), ports[port_id]);
		} catch (UnknownHostException e) {
			throw new NoSuchElementException("Illegal Address Length.");
		}
	}

}

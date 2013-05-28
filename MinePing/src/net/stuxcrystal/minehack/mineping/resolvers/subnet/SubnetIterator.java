package net.stuxcrystal.minehack.mineping.resolvers.subnet;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.NoSuchElementException;

import net.stuxcrystal.minehack.mineping.api.AddressIterator;

public class SubnetIterator implements AddressIterator {

	/**
	 * All subnets.
	 */
	private IPv4[] subnets;

	/**
	 * Count of ports.
	 */
	private int[] ports;

	/**
	 * Current Subnet Index.
	 */
	private int index = 0;

	/**
	 * The current index of the subnet.
	 */
	private int subnet_index = 0;

	/**
	 * The current index of ports.
	 */
	private int port_index = 0;

	/**
	 * True if the ip-pool is depleted.
	 */
	private boolean completed = false;

	private static byte[] int2byte(int number) {
		byte[] data = new byte[4];

		// int -> byte[]
		for (int i = 0; i < 4; ++i) {
		    int shift = i << 3; // i * 8
		    data[3-i] = (byte)((number & (0xff << shift)) >>> shift);
		}

		return data;
	}

	public SubnetIterator(int[] ports, IPv4... subnets) {
		this.subnets = subnets;
		this.ports   = ports;
	}


	public boolean hasMoreElements() {
		synchronized (this) {
			return !completed;
		}

	}

	public InetSocketAddress nextElement() throws NoSuchElementException {
		synchronized (this) {
			if (completed)
				throw new NoSuchElementException("All ips given.");

			int port = ports[port_index];

			IPv4 info = subnets[index];
			int address = info.baseIPnumeric + subnet_index;
			InetAddress inetAddress = null;
			try {
				 inetAddress = InetAddress.getByAddress(int2byte(address));
			} catch (UnknownHostException e) {
				throw new NoSuchElementException("Invalid subnet.");
			}

			// Set the next address.
			port_index = (port_index + 1)%ports.length;
			if (port_index == 0) {
				subnet_index = (int) ((subnet_index + 1)%info.getNumberOfHosts());

				if (subnet_index == 0) {
					index = (index + 1)%subnets.length;

					if (index == 0) {
						completed = true;
						throw new NoSuchElementException("All ips given.");
					}
				}
			}

			InetSocketAddress result = new InetSocketAddress(inetAddress, port);
			assert info.contains(result.getAddress().getHostAddress());
			return result;
		}
	}

	public boolean hasNext() {
		return hasMoreElements();
	}

	public InetSocketAddress next() {
		return nextElement();
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}



}

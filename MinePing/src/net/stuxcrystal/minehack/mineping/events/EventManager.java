package net.stuxcrystal.minehack.mineping.events;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.List;

import net.stuxcrystal.minehack.mineping.api.MinePingEventHandler;

/**
 * Manages events.
 * @author StuxCrystal
 *
 */
public class EventManager/* implements MinePingEventHandler*/ {

	/**
	 * All EventHandlers.
	 */
	private List<MinePingEventHandler> handlers = new LinkedList<MinePingEventHandler>();

	/**
	 * Add a handler.
	 * @param handler
	 */
	public void addHandler(MinePingEventHandler handler) {
		handlers.add(handler);
	}

	/**
	 * Removes a handler.
	 * @param handler
	 */
	public void removeHandler(MinePingEventHandler handler) {
		handlers.remove(handler);
	}

	public boolean preAssign(InetSocketAddress address) {
		for (MinePingEventHandler handler : handlers) {
			if (!handler.preAssign(address))
				return false;;
		}

		return true;
	}

	public boolean preConnect(InetSocketAddress address) {
		for (MinePingEventHandler handler : handlers) {
			if (!handler.preConnect(address))
				return false;
		}

		return true;
	}

	void postConnect(SocketChannel channel) {
		postConnect(channel.socket());
	}

	public void postConnect(Socket socket) {
		for (MinePingEventHandler handler : handlers) {
			handler.postConnect(socket);
		}
	}

	public void start() {
		for (MinePingEventHandler handler : handlers) {
			handler.start();
		}
	}

	public void tick() {
		for (MinePingEventHandler handler : handlers) {
			handler.tick();
		}
	}

	public void stop() {
		for (MinePingEventHandler handler : handlers) {
			handler.stop();
		}
	}


}

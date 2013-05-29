package net.stuxcrystal.minehack.mineping.api;

import java.net.InetSocketAddress;
import java.net.Socket;

public class AbstractPingEventHandler implements MinePingEventHandler {

	public AbstractPingEventHandler() { }

	public boolean preAssign(InetSocketAddress address) {
		return true;
	}

	public boolean preConnect(InetSocketAddress address) {
		return true;
	}

	public void postConnect(Socket socket) { }

	public void start() { }

	public void tick() { }

	public void stop() { }

}

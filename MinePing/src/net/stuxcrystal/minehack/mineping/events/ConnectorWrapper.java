package net.stuxcrystal.minehack.mineping.events;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SocketChannel;

import net.stuxcrystal.minehack.clparser.OptionParser;
import net.stuxcrystal.minehack.clparser.ParserResult;
import net.stuxcrystal.minehack.mineping.api.Connector;

public class ConnectorWrapper implements Connector {

	EventManager manager;

	Connector backend;

	public ConnectorWrapper(EventManager manager, Connector backend) {
		this.manager = manager;
		this.backend = backend;
	}

	public String getName() {
		return backend.getName();
	}

	public void start() {
		backend.start();
	}

	public void stop() {
		backend.stop();
	}

	public Socket connectSocket(InetSocketAddress address) {
		if (!manager.preConnect(address))
			return null;

		Socket result = backend.connectSocket(address);
		manager.postConnect(result);
		return result;
	}

	public SocketChannel connectChannel(InetSocketAddress address) {
		if (!manager.preConnect(address))
			return null;

		SocketChannel result = backend.connectChannel(address);
		manager.postConnect(result);
		return result;
	}

	public void registerCommandLineArguments(OptionParser parser) {
		backend.registerCommandLineArguments(parser);
	}

	public void parseCommandLine(ParserResult result) {
		backend.parseCommandLine(result);
	}

}

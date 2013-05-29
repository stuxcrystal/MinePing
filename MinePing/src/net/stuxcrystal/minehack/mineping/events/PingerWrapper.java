package net.stuxcrystal.minehack.mineping.events;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;
import java.util.Map;

import net.stuxcrystal.minehack.clparser.OptionParser;
import net.stuxcrystal.minehack.clparser.ParserResult;
import net.stuxcrystal.minehack.mineping.api.Pinger;

public class PingerWrapper implements Pinger {

	EventManager manager;

	Pinger backend;

	public PingerWrapper(EventManager manager, Pinger backend) {
		this.manager = manager;
		this.backend = backend;
	}

	public String getName() {
		return backend.getName();
	}

	public List<String> getColumns() {
		return backend.getColumns();
	}

	public void prepare() {
		backend.prepare();
	}

	public void end() {
		backend.end();
	}

	public Map<String, String> executePing(InetAddress address, int port)
			throws IOException {
		return backend.executePing(address, port);
	}

	public void registerCommandLineArguments(OptionParser parser) {
		backend.registerCommandLineArguments(parser);
	}

	public void parseCommandLine(ParserResult result) {
		backend.parseCommandLine(result);
	}
}

package net.stuxcrystal.minehack.mineping.events;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import net.stuxcrystal.minehack.clparser.OptionParser;
import net.stuxcrystal.minehack.clparser.ParserResult;
import net.stuxcrystal.minehack.mineping.api.Writer;

public class WriterWrapper implements Writer {

	EventManager manager;

	Writer backend;

	public WriterWrapper(EventManager manager, Writer backend) {
		this.manager = manager;
		this.backend = backend;
	}

	public String getName() {
		return backend.getName();
	}

	public void onStart(OutputStream stream, List<String> header) throws IOException {
		backend.onStart(stream, header);
	}

	public void add(Map<String, String> values) throws IOException {
		backend.add(values);
	}

	public void flush() throws IOException {
		backend.flush();
	}

	public void close() throws IOException {
		backend.close();
	}

	public void registerCommandLineArguments(OptionParser parser) {
		backend.registerCommandLineArguments(parser);
	}

	public void parseCommandLine(ParserResult result) {
		backend.parseCommandLine(result);
	}

}

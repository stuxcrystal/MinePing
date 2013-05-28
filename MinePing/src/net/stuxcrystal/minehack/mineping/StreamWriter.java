package net.stuxcrystal.minehack.mineping;

import java.io.OutputStream;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;

public class StreamWriter extends StreamHandler {
	public StreamWriter(OutputStream out, Formatter formatter) {
		super(out, formatter);
	}

	public void publish(LogRecord record) {
		super.publish(record);
		super.flush();
	}
}

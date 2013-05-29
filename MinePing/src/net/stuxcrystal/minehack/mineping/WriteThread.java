package net.stuxcrystal.minehack.mineping;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

import net.stuxcrystal.minehack.mineping.api.Writer;

public class WriteThread extends Thread {

	/**
	 * The writer the data has to be written to.
	 */
	private Writer writer;

	/**
	 * The logger of the software.
	 */
	private Logger logger;

	/**
	 * Flush after.
	 */
	private int flushAfter;

	/**
	 * The current count of stored results.
	 */
	private int currentCount = 0;

	/**
	 * The output-queue.
	 */
	private BlockingQueue<Map<String, String>> outputQueue = new LinkedBlockingQueue<Map<String, String>>();

	public WriteThread(Writer writer, Logger logger, int flushAfter) {
		super();
		this.setName("Writer Thread");
		this.writer = writer;
		this.logger = logger;
		this.flushAfter = flushAfter;
	}

	public void run() {

		while (!this.isInterrupted()) {
			Map<String, String> data;
			try {
				data = outputQueue.take();
			} catch (InterruptedException e) {
				break;
			}

			addToWriter(data);

			if ((++currentCount)%flushAfter == 0)
				flush();
		}

		Map<String, String> data;
		while((data = outputQueue.poll()) != null) {
			addToWriter(data);
		}

		logger.fine("Saving data...");
		flush();
	}

	private void addToWriter(Map<String, String> data) {
		try {
			this.writer.add(data);
		} catch (IOException e) {
			this.logger.warning("Failed to add result: "+e);
		}
	}

	public void add(Map<String, String> data) {
		this.outputQueue.add(data);
	}

	private void flush() {
		try {
			this.writer.flush();
		} catch (IOException e) {
			this.logger.warning("Failed to save data: " + e);
		}
	}

}

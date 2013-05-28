package net.stuxcrystal.minehack.mineping.writers;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.csvreader.CsvWriter;

import net.stuxcrystal.minehack.clparser.OptionBuilder;
import net.stuxcrystal.minehack.clparser.OptionParser;
import net.stuxcrystal.minehack.clparser.ParserResult;
import net.stuxcrystal.minehack.mineping.api.Writer;

public class CSVFileWriter implements Writer {

	public List<String> columns = null;

	public CsvWriter writer;

	public char sep = ',';
	public boolean addHeaders = true;

	public String getName() {
		return "CSV";
	}

	/**
	 * Writes the record into the file.<p />
	 * Thread-Safe.
	 * @param data
	 * @throws IOException
	 */
	private synchronized void writeRecord(List<String> data) throws IOException {
		writer.writeRecord(data.toArray(new String[0]));
	}

	public void onStart(OutputStream stream, List<String> header) throws IOException {
		columns = header;

		writer = new CsvWriter((java.io.Writer) new OutputStreamWriter(stream), sep);
		if (!addHeaders) writeRecord(header);
	}

	public void add(Map<String, String> values) throws IOException {
		List<String> line = new ArrayList<String>();

		for (String key : columns) {
			String value = values.get(key);
			line.add(value == null?"":value);
		}

		writeRecord(line);
	}

	public void flush() throws IOException {
		writer.flush();
	}

	public void close() throws IOException {
		writer.flush();
		writer.close();
	}

	public void registerCommandLineArguments(OptionParser parser) {
		parser.addOption(new OptionBuilder("separator").setShortName("s").hasArgument(true).setDefault(",").create());
		parser.addOption(new OptionBuilder("no-header").setShortName("a").create());
	}

	public void parseCommandLine(ParserResult result) {
		sep = ((String)result.getValue("separator")).charAt(0);
		addHeaders = !result.isGiven("no-header");
	}

}

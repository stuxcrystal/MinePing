package net.stuxcrystal.anime.rawscan;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
//import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.stuxcrystal.minehack.clparser.OptionBuilder;
import net.stuxcrystal.minehack.clparser.OptionParser;
import net.stuxcrystal.minehack.clparser.ParserResult;
import net.stuxcrystal.minehack.mineping.MinePingInstances;
import net.stuxcrystal.minehack.mineping.api.Pinger;

public class EpgRecScanner implements Pinger {

	/**
	 * The supported options that are parsed by the writer.
	 */
	private static final Map<String, String> OPTIONS = new HashMap<String, String>();

	/**
	 * Filename of systemSetting.php
	 */
	private static final String SYSTEM_SETTING_PATH = "systemSetting.php";

	/**
	 * Path to "epgrec" to be checked.
	 */
	private String path = "/epgrec/";

	/**
	 * Adds the "systemSetting.php"-Path.
	 */
	private boolean addFilename = true;

	/**
	 * Path to "systemSetting.php"
	 */
	private String fullPath;

	static {
		for (String name : new String[] {
				"host", "port",
				"db_host", "db_user", "db_pass", "db_name",
				"tbl_prefix", "install_url",
				"spool",
				"ffmpeg", "thumbs",
				"temp_data", "temp_xml",
				"epgdump", "at", "atrm", "sleep"}
		) {
			OPTIONS.put(name, "input");

		}

		OPTIONS.put("use_thumbs", "select");
	}

	@Override
	public String getName() {
		return "epgrec";
	}

	@Override
	public List<String> getColumns() {
		//List<String> result = new ArrayList<String>();
		//result.addAll(OPTIONS.keySet());
		//return result;
		return Arrays.asList("host", "port", "path");
	}

	@Override
	public void prepare() {
		fullPath = getPath();
	}

	@Override
	public Map<String, String> executePing(InetAddress address, int port) throws IOException {
		SocketChannel socket = MinePingInstances.getMinePing().connector.connectChannel(
				new InetSocketAddress(address, port)
		);

		if (socket == null)
			return null;

		String data = RawHttpConnect.fetchWebsite(socket, fullPath);

		if (!data.contains("id=\"system_setting-submit\" />"))
			return null;

		Map<String, String> result = new HashMap<String, String>();
		result.put("host", address.getHostAddress());
		result.put("port", "" + port);
		result.put("path", fullPath);
		return result;
	}

	/**
	 * Calculates the path to epgrecs systemSetting.php
	 * @return The string to the path.
	 */
	private String getPath() {
		StringBuilder sb = new StringBuilder();

		if (!path.startsWith("/"))
			sb.append("/");

		sb.append(path);

		if (addFilename) {
			if (!path.endsWith("/")) {
				sb.append("/");
			}

			sb.append(SYSTEM_SETTING_PATH);
		}

		return sb.toString();
	}

	@Override
	public void registerCommandLineArguments(OptionParser parser) {
		parser.addOption(new OptionBuilder("no-filename").create());
		parser.addOption(new OptionBuilder("path").hasArgument(true).setDefault("/epgrec/").create());
	}

	@Override
	public void parseCommandLine(ParserResult result) {
		addFilename = !result.isGiven("no-filename");
		path = result.getValue("path");
	}

}

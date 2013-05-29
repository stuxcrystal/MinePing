package net.stuxcrystal.minehack.MinecraftPinger;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.stuxcrystal.minehack.clparser.OptionParser;
import net.stuxcrystal.minehack.clparser.ParserResult;
import net.stuxcrystal.minehack.mineping.MinePingInstances;
import net.stuxcrystal.minehack.mineping.api.Pinger;

public class MinecraftPinger implements Pinger {

	public final class MinecraftServer {
		private String address = "localhost";
		private int port = 25565;

		private int timeout = 1500;

		private int pingVersion = -1;
		private int protocolVersion = -1;
		private String gameVersion = "<1.3";
		private String motd;
		private int playersOnline = -1;
		private int maxPlayers = -1;

		public MinecraftServer() {

		}

		public MinecraftServer(String address) {
			this();

			this.setAddress(address);
		}

		public MinecraftServer(String address,int port) {
			this(address);

			this.setPort(port);
		}

		public MinecraftServer(String address,int port,int timeout) {
			this(address,port);

			this.setTimeout(timeout*1000);
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public String getAddress() {
			return this.address;
		}

		public void setPort(int port) {
			this.port = port;
		}

		public int getPort() {
			return this.port;
		}

		public void setTimeout(int timeout) {
			this.timeout = timeout;
		}

		public int getTimeout() {
			return this.timeout;
		}

		private void setPingVersion(int pingVersion) {
			this.pingVersion = pingVersion;
		}

		public int getPingVersion() {
			return this.pingVersion;
		}

		private void setProtocolVersion(int protocolVersion) {
			this.protocolVersion = protocolVersion;
		}

		public int getProtocolVersion() {
			return this.protocolVersion;
		}

		private void setGameVersion(String gameVersion) {
			this.gameVersion = gameVersion;
		}

		public String getGameVersion() {
			return this.gameVersion;
		}

		private void setMotd(String motd) {
			this.motd = motd;
		}

		public String getMotd() {
			return this.motd;
		}

		private void setPlayersOnline(int playersOnline) {
			this.playersOnline = playersOnline;
		}

		public int getPlayersOnline() {
			return this.playersOnline;
		}

		private void setMaxPlayers(int maxPlayers) {
			this.maxPlayers = maxPlayers;
		}

		public int getMaxPlayers() {
			return this.maxPlayers;
		}

		public boolean fetchData() throws IOException {
			Socket socket = MinePingInstances.getMinePing().connector.connectSocket(
					new InetSocketAddress(this.getAddress(), this.getPort())
			);

			if (socket == null)
				return false;

			if (!socket.isConnected()) {
				socket.close();
				throw new IOException("Socket was not connected: " + socket.toString());
			}

			OutputStream outputStream = null;
			DataOutputStream dataOutputStream = null;

			InputStream inputStream = null;
			InputStreamReader inputStreamReader = null;

			try {
				outputStream = socket.getOutputStream();
				dataOutputStream = new DataOutputStream(outputStream);

				inputStream = socket.getInputStream();
				inputStreamReader = new InputStreamReader(inputStream,Charset.forName("UTF-16BE"));

				dataOutputStream.write(new byte[]{
					(byte) 0xFE,
					(byte) 0x01
				});

				dataOutputStream.flush();

				int packetId = inputStream.read();

				if (packetId == -1) {
					throw new IOException("Premature end of stream.");
				}

				if (packetId != 0xFF) {
					throw new IOException("Invalid packet ID (" + packetId + ").");
				}

				int length = inputStreamReader.read();

				if (length == -1) {
					throw new IOException("Premature end of stream.");
				}

				if (length == 0) {
					throw new IOException("Invalid string length.");
				}

				char[] chars = new char[length];

				if (inputStreamReader.read(chars,0,length) != length) {
					throw new IOException("Premature end of stream.");
				}

				String string = new String(chars);

				if (string.startsWith("§")) {
					String[] data = string.split("\0");

					this.setPingVersion(Integer.parseInt(data[0].substring(1)));
					this.setProtocolVersion(Integer.parseInt(data[1]));
					this.setGameVersion(data[2]);
					this.setMotd(data[3]);
					this.setPlayersOnline(Integer.parseInt(data[4]));
					this.setMaxPlayers(Integer.parseInt(data[5]));
				} else {
					String[] data = string.split("§");

					this.setMotd(data[0]);
					this.setPlayersOnline(Integer.parseInt(data[1]));
					this.setMaxPlayers(Integer.parseInt(data[2]));
				}



			} finally {
				try { dataOutputStream.close(); } catch (IOException | NullPointerException e) {}
				try {outputStream.close(); } catch (IOException | NullPointerException e) {}

				try {inputStreamReader.close(); } catch (IOException | NullPointerException e) {}
				try {inputStream.close(); } catch (IOException | NullPointerException e) {}

				try {socket.close(); } catch (IOException e) {}
			}

			return true;
		}
	}

	public String getName() {
		return "MC-Ping";
	}

	public List<String> getColumns() {
		return Arrays.asList("ip", "port", "pingVersion", "protocolVersion", "gameVersion", "motd", "playersOnline", "maxPlayers");
	}

	public void prepare() {
	}

	public Map<String, String> executePing(InetAddress address, int port) throws IOException {

		MinecraftServer server = new MinecraftServer(address.getHostAddress(), port);

		try {
			if (!server.fetchData())
				return null;
		} catch (RuntimeException e) {
			return null;
		}

		Map<String, String> result = new HashMap<String, String>();
		result.put("ip", address.getHostAddress());
		result.put("port", "" + port);
		result.put("pingVersion", "" + server.getPingVersion());
		result.put("protocolVersion", "" + server.getProtocolVersion());
		result.put("gameVersion", server.getGameVersion());
		result.put("motd", server.getMotd());
		result.put("playersOnline", "" + server.getPlayersOnline());
		result.put("maxPlayers", "" + server.getMaxPlayers());

		return result;

	}

	public void registerCommandLineArguments(OptionParser parser) {
	}

	public void parseCommandLine(ParserResult result) {
	}

	@Override
	public void end() {/* Does nothing. */}

}

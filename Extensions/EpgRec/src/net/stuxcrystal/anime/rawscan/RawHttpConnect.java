package net.stuxcrystal.anime.rawscan;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

public class RawHttpConnect {

	public static String fetchWebsite(SocketChannel channel, String file) throws IOException {

	    try {
	      Charset charset = Charset.forName("UTF-8");
	      CharsetDecoder decoder = charset.newDecoder();
	      CharsetEncoder encoder = charset.newEncoder();

	      ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
	      CharBuffer charBuffer = CharBuffer.allocate(1024);

	      String request = "GET " + file + " \r\n\r\n";
	      channel.write(encoder.encode(CharBuffer.wrap(request)));

	      StringBuilder sb = new StringBuilder();

	      while ((channel.read(buffer)) != -1) {
	        buffer.flip();
	        decoder.decode(buffer, charBuffer, false);
	        charBuffer.flip();
	        sb.append(charBuffer);
	        buffer.clear();
	        charBuffer.clear();
	      }
	      String str = sb.toString().replace("\r\n", "\n");
	      int index = str.indexOf("\n\n");
	      if (index == -1)
	    	  return "";

	      return str.substring(index);

	    } finally {
	      if (channel != null) {
	        try {
	          channel.close();
	        } catch (IOException ignored) {
	        }
	      }
	    }
	}
}

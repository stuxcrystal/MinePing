package net.stuxcrystal.minehack.clparser.types;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;

import net.stuxcrystal.minehack.clparser.Type;
import net.stuxcrystal.minehack.clparser.types.helpers.BlackHoleStream;

public class OutputStreamType implements Type {

	final boolean allowConsole;

	private OutputStreamType(boolean allowConsole) {
		this.allowConsole = allowConsole;
	}

	public Object parseValue(String argument) throws IllegalArgumentException {

		if (argument.equalsIgnoreCase("*stdout") || argument.equalsIgnoreCase("*stderr") || argument.equalsIgnoreCase("*blackhole")) {
			if (allowConsole) {
				if (argument.equalsIgnoreCase("*stdout"))
					return new FileOutputStream(FileDescriptor.out);

				if (argument.equalsIgnoreCase("*stderr"))
					return new FileOutputStream(FileDescriptor.out);

				if (argument.equalsIgnoreCase("*blackhole"))
					return BlackHoleStream.NEXT_BLACKHOLE;
			} else {
				throw new IllegalArgumentException("Thie given file cannot be a console object.");
			}
		} else {
			try {
				return new FileOutputStream(argument);
			} catch (IOException e) {
				throw new IllegalArgumentException(e.toString());
			}
		}

		return null;  // Should not be happening.

	}

	public static final OutputStreamType ALL = new OutputStreamType(true);
	public static final OutputStreamType NOCONSOLE = new OutputStreamType(false);

}

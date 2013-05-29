package net.stuxcrystal.minehack.clparser.types;

import java.io.File;

import net.stuxcrystal.minehack.clparser.Type;

public class FileType implements Type {

	final boolean forceExist;

	private FileType(boolean hasToExist) {
		forceExist = hasToExist;
	}

	public Object parseValue(String argument) throws IllegalArgumentException {
		File result = new File(argument);

		if (!result.exists() && forceExist)
			throw new IllegalArgumentException(argument + " does not exist.");

		return result;
	}

	public static final FileType EXISTING = new FileType(true);
	public static final FileType ALL = new FileType(false);

}

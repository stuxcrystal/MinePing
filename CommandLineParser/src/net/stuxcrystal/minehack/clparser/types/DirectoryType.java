package net.stuxcrystal.minehack.clparser.types;

import java.io.File;

import net.stuxcrystal.minehack.clparser.Type;

public class DirectoryType implements Type {

	final boolean forceExist;

	private DirectoryType(boolean hasToExist) {
		forceExist = hasToExist;
	}

	public Object parseValue(String argument) throws IllegalArgumentException {
		File result = new File(argument);

		if (!result.exists() && forceExist)
			throw new IllegalArgumentException(argument + " does not exist.");

		if (result.exists() && !result.isDirectory())
			throw new IllegalArgumentException(argument + " is not a directory.");

		return result;
	}

	public static final DirectoryType EXISTING = new DirectoryType(true);
	public static final DirectoryType ALL = new DirectoryType(false);

}

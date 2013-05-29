package net.stuxcrystal.minehack.clparser.types;

import net.stuxcrystal.minehack.clparser.Type;

public class IntegerType implements Type {

	private IntegerType() {}

	public Object parseValue(String argument) throws IllegalArgumentException {
		try {
			return Integer.parseInt(argument);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(e.toString());
		}
	}

	public static final IntegerType INSTANCE = new IntegerType();

}

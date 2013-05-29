package net.stuxcrystal.minehack.clparser.types;

import net.stuxcrystal.minehack.clparser.Type;

public class FloatType implements Type {

	private FloatType() {}

	public Object parseValue(String argument) throws IllegalArgumentException {
		try {
			return Float.parseFloat(argument);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(e.toString());
		}
	}

	public static final FloatType INSTANCE = new FloatType();
}

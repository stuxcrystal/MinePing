package net.stuxcrystal.minehack.clparser.types;

import net.stuxcrystal.minehack.clparser.Type;

public class ClassType implements Type {

	private ClassType() {}

	public Object parseValue(String argument) throws IllegalArgumentException {
		try {
			return ClassType.class.getClassLoader().loadClass(argument);
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException(e.getLocalizedMessage());
		}
	}

	public static final ClassType INSTANCE = new ClassType();

}

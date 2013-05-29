package net.stuxcrystal.minehack.clparser;

public interface Type {

	public Object parseValue(String argument) throws IllegalArgumentException;

}

package net.stuxcrystal.minehack.clparser.types.helpers;

import java.io.OutputStream;

/**
 * Acts like a black-hole.<p />
 *
 * Nothing can go out of it!
 * @author StuxCrystal
 *
 */
public class BlackHoleStream extends OutputStream {

	/**
	 * This Stream moves the content written into it directly to the next blackhole.
	 */
	public static final BlackHoleStream NEXT_BLACKHOLE = new BlackHoleStream();

	/**
	 * Prevent a new Blackhole to be created.
	 */
	private BlackHoleStream() { super();  };

	/**
	 * Just redirect the output into the next blackhole.<p />
	 *
	 * It's faster than any other method you could find in Java. (Except for methods doing nothing.)<p />
	 *
	 * It cannot fail, too, so have fun with it.
	 */
	@Override
	public void write(int b) {}

}

package net.stuxcrystal.minehack.mineping;

/**
 * Handler for Singletons.
 * @author StuxCrystal
 *
 */
public class MinePingInstances {

	public static final MinePing getMinePing() {
		return MINEPING;
	}

	static final void setMinePing(MinePing minePing) {
		if (MINEPING != null)
			throw new IllegalStateException("Mineping already set.");

		MINEPING = minePing;
	}

	public static final Starter getStarter() {
		return STARTER;
	}

	static final void setStarter(Starter starter) {
		if (STARTER != null)
			throw new IllegalStateException("Starter already set.");

		STARTER = starter;
	}

	private static MinePing MINEPING;

	private static Starter STARTER;
}

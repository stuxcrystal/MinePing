package net.stuxcrystal.minehack.MinecraftPinger;

import java.util.Arrays;
import java.util.List;

import net.stuxcrystal.minehack.mineping.api.Connector;
import net.stuxcrystal.minehack.mineping.api.Extension;
import net.stuxcrystal.minehack.mineping.api.Pinger;
import net.stuxcrystal.minehack.mineping.api.Resolver;
import net.stuxcrystal.minehack.mineping.api.Strategy;
import net.stuxcrystal.minehack.mineping.api.Writer;

public class EntryPoint implements Extension {

	@Override
	public List<Pinger> getPingers() {
		return Arrays.asList((Pinger) new MinecraftPinger());
	}

	@Override
	public List<Writer> getWriters() {
		return null;
	}

	@Override
	public List<Resolver> getResolvers() {
		return null;
	}

	@Override
	public List<Strategy> getStrategies() {
		return null;
	}

	@Override
	public List<Connector> getConnectors() {
		// TODO Auto-generated method stub
		return null;
	}

}

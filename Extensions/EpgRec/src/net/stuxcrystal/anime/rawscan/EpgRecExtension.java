package net.stuxcrystal.anime.rawscan;

import java.util.Arrays;
import java.util.List;

import net.stuxcrystal.minehack.mineping.api.Extension;
import net.stuxcrystal.minehack.mineping.api.Pinger;
import net.stuxcrystal.minehack.mineping.api.Resolver;
import net.stuxcrystal.minehack.mineping.api.Writer;

public class EpgRecExtension implements Extension {
	@Override
	public List<Pinger> getPingers() {
		return Arrays.asList((Pinger) new EpgRecScanner());
	}

	@Override
	public List<Writer> getWriters() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Resolver> getResolvers() {
		// TODO Auto-generated method stub
		return null;
	}

}

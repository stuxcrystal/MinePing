package net.stuxcrystal.minehack.mineping;

import java.util.Arrays;
import java.util.List;

import net.stuxcrystal.minehack.mineping.api.Extension;
import net.stuxcrystal.minehack.mineping.api.Pinger;
import net.stuxcrystal.minehack.mineping.api.Resolver;
import net.stuxcrystal.minehack.mineping.api.Writer;
import net.stuxcrystal.minehack.mineping.resolvers.defaultresolver.DefaultResolver;
import net.stuxcrystal.minehack.mineping.resolvers.subnet.SubnetFile;
import net.stuxcrystal.minehack.mineping.resolvers.subnet.SubnetRanges;
import net.stuxcrystal.minehack.mineping.writers.CSVFileWriter;

/**
 * The Extension installing the base-extension.
 * @author StuxCrystal
 *
 */
public class CoreExtension implements Extension {

	public List<Pinger> getPingers() {
		return null;
	}

	public List<Writer> getWriters() {
		return Arrays.asList((Writer) new CSVFileWriter());
	}

	public List<Resolver> getResolvers() {
		return Arrays.asList(
			(Resolver) new DefaultResolver(),
			(Resolver) new SubnetRanges(), (Resolver) new SubnetFile()
		);
	}

}

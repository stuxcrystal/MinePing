package net.stuxcrystal.minehack.mineping.resolvers.defaultresolver;

import java.util.Arrays;

import net.stuxcrystal.minehack.clparser.OptionBuilder;
import net.stuxcrystal.minehack.clparser.OptionParser;
import net.stuxcrystal.minehack.clparser.ParserResult;
import net.stuxcrystal.minehack.mineping.api.AddressIterator;
import net.stuxcrystal.minehack.mineping.api.Resolver;

public class DefaultResolver implements Resolver {

	private byte[][][] ranges = null;

	public String getName() {
		return "default";
	}

	public void registerCommandLineArguments(OptionParser parser) {
		parser.addOption(new OptionBuilder("range").setShortName("n").hasArgument(true).required(true).setMulti(true).create());
	}

	public void parseCommandLine(ParserResult result) {
		ranges = RangeResolver.parseRanges(castObjectArray((Object[]) result.getValue("range")));
	}

	public static String[] castObjectArray(Object array) {
		return Arrays.copyOf((Object[]) array, ((Object[])array).length, String[].class);
	}

	public AddressIterator getRangeIterator(int[] ports) {
		return new RangeIterator(ranges, ports);
	}
}

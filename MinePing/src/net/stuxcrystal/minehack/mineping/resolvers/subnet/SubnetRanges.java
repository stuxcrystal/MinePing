package net.stuxcrystal.minehack.mineping.resolvers.subnet;

import java.util.Arrays;

import net.stuxcrystal.minehack.clparser.OptionBuilder;
import net.stuxcrystal.minehack.clparser.OptionParser;
import net.stuxcrystal.minehack.clparser.ParserResult;
import net.stuxcrystal.minehack.mineping.api.AddressIterator;
import net.stuxcrystal.minehack.mineping.api.Resolver;

public class SubnetRanges implements Resolver {

	IPv4[] subnets;

	public String getName() {
		return "subnet";
	}

	public AddressIterator getRangeIterator(int[] ranges) {
		return new SubnetIterator(ranges, subnets);
	}

	public void registerCommandLineArguments(OptionParser parser) {
		parser.addOption(new OptionBuilder("range").setShortName("n").hasArgument(true).required(true).setMulti(true).create());
	}

	public void parseCommandLine(ParserResult result) {
		subnets = toSubnets(castObjectArray((Object[]) result.getValue("range")));
	}

	public static IPv4[] toSubnets(String[] raw) {
		IPv4[] subnets = new IPv4[raw.length];

		for (int i = 0; i<raw.length; i++) {
			subnets[i] = new IPv4(raw[i]);
		}

		return subnets;
	}

	public static String[] castObjectArray(Object array) {
		return Arrays.copyOf((Object[]) array, ((Object[])array).length, String[].class);
	}

}

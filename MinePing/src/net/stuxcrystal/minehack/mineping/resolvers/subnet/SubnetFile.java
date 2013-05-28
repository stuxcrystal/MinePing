package net.stuxcrystal.minehack.mineping.resolvers.subnet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.stuxcrystal.minehack.clparser.OptionBuilder;
import net.stuxcrystal.minehack.clparser.OptionParser;
import net.stuxcrystal.minehack.clparser.ParserResult;
import net.stuxcrystal.minehack.clparser.types.FileType;
import net.stuxcrystal.minehack.mineping.api.AddressIterator;
import net.stuxcrystal.minehack.mineping.api.Resolver;

public class SubnetFile implements Resolver {

	IPv4[] subnets;

	public String getName() {
		return "subnet:file";
	}


	public AddressIterator getRangeIterator(int[] ranges) {
		return new SubnetIterator(ranges, subnets);
	}

	public void registerCommandLineArguments(OptionParser parser) {
		parser.addOption(new OptionBuilder("range-file").hasArgument(true).setType(FileType.EXISTING).required(true).create());
	}

	public void parseCommandLine(ParserResult result) {
		subnets = toSubnets((File) result.getValue("range-file"));
	}

	public static IPv4[] toSubnets(File raw) {
		BufferedReader bw;
		try {
			bw = new BufferedReader(new FileReader(raw));
		} catch (FileNotFoundException e) {
			return new IPv4[0];
		}

		List<IPv4> lists = new ArrayList<IPv4>();
		String line = null;

		while (true) {
			try {
				line = bw.readLine();
			} catch (IOException e) {
				continue;
			}

			if (line == null)
				break;

			lists.add(new IPv4(line));
		}

		try {
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return lists.toArray(new IPv4[0]);
	}

	public static String[] castObjectArray(Object array) {
		return Arrays.copyOf((Object[]) array, ((Object[])array).length, String[].class);
	}
}

package net.stuxcrystal.minehack.mineping;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.stuxcrystal.minehack.clparser.OptionBuilder;
import net.stuxcrystal.minehack.clparser.OptionParser;
import net.stuxcrystal.minehack.clparser.ParserResult;
import net.stuxcrystal.minehack.clparser.types.ClassType;
import net.stuxcrystal.minehack.clparser.types.DirectoryType;
import net.stuxcrystal.minehack.clparser.types.IntegerType;
import net.stuxcrystal.minehack.clparser.types.OutputStreamType;
import net.stuxcrystal.minehack.mineping.addons.ExtensionListing;
import net.stuxcrystal.minehack.mineping.addons.ExtensionManager;
import net.stuxcrystal.minehack.mineping.api.Connector;
import net.stuxcrystal.minehack.mineping.api.Extension;
import net.stuxcrystal.minehack.mineping.api.Pinger;
import net.stuxcrystal.minehack.mineping.api.Resolver;
import net.stuxcrystal.minehack.mineping.api.Strategy;
import net.stuxcrystal.minehack.mineping.api.Writer;

/**
 * Hello world!
 *
 */
public class Starter
{

	/**
	 * The arguments given in the main method.
	 */
	public String[] arguments;

	/**
	 * The folder for the extensions.
	 */
	public File extensionFolder;

	/**
	 * Lists extensions.
	 */
	public ExtensionListing listing = new ExtensionListing();

	Starter(File folder, String[] args) {
		this.arguments = args;
		this.extensionFolder = folder;
	}

	private void loadExtensions(Extension[] extensions) {
		listing.loadExtension(new CoreExtension());

		for (Extension extension : extensions) {
			listing.loadExtension(extension);
		}
	}

	public MinePing parseArguments() throws RuntimeException {

		// Parse basic results.
		OptionParser parser = new OptionParser();

		// Register options to parsed here.
		parser.addOption(new OptionBuilder("pinger").setShortName("p").hasArgument(true).required(true).create());
		parser.addOption(new OptionBuilder("writer").setShortName("w").hasArgument(true).setDefault("CSV").create());
		parser.addOption(new OptionBuilder("resolver").setShortName("r").hasArgument(true).setDefault("subnet").create());
		parser.addOption(new OptionBuilder("strategy").setShortName("s").hasArgument(true).setDefault("static").create());
		parser.addOption(new OptionBuilder("connector").setShortName("c").hasArgument(true).setDefault("static").create());
		parser.addOption(new OptionBuilder("output").setShortName("o").hasArgument(true).setType(OutputStreamType.ALL).setDefault(System.out).create());
		parser.addOption(new OptionBuilder("log").setShortName("l").hasArgument(true).setType(OutputStreamType.ALL).setDefault(System.err).create());
		parser.addOption(new OptionBuilder("port").setShortName("t").hasArgument(true).required(true).create());
		parser.addOption(new OptionBuilder("log-level").hasArgument(true).setDefault("INFO").create());
		parser.addOption(new OptionBuilder("flush-results").hasArgument(true).setType(IntegerType.INSTANCE).setDefault(50).create());
		parser.addOption(new OptionBuilder("extension").hasArgument(true).setMulti(true).setType(ClassType.INSTANCE).create());

		ParserResult baseOptions = parser.parseArguments(arguments, true);
    	if (baseOptions.message != null) {
    		System.err.println(baseOptions.message);
    		return null;
    	}

    	// Prepare the logger to be started.
    	MinePing.staticlogger = getLogger((OutputStream) baseOptions.getValue("log"), (String) baseOptions.getValue("log-level"));

    	// Load Extensions
    	ExtensionManager mgr = new ExtensionManager();
    	mgr.loadExtensions(this.extensionFolder);
    	mgr.loadExtensions(castObjectArray2Class(baseOptions.getValue("extension")));
    	loadExtensions(mgr.getExtensions());

    	// Parse the Pinger.
		Pinger pinger =       listing.getPinger((String) baseOptions.getValue("pinger"));
		Writer writer =       listing.getWriter((String) baseOptions.getValue("writer"));
		Resolver resolver =   listing.getResolver((String) baseOptions.getValue("resolver"));
		Strategy strategy =   listing.getStrategy((String) baseOptions.getValue("strategy"));
		Connector connector = listing.getConnector((String) baseOptions.getValue("connector"));

		// Register options already parsed in the first pass.
    	parser.addOption(new OptionBuilder("extension").hasArgument(true).setType(ClassType.INSTANCE).setMulti(true).setShortName("ext").create());
    	parser.addOption(new OptionBuilder("help").setShortName("h").create());

    	// Register options of pinger and writer
    	pinger.registerCommandLineArguments(parser);
    	writer.registerCommandLineArguments(parser);
    	resolver.registerCommandLineArguments(parser);
    	strategy.registerCommandLineArguments(parser);
    	connector.registerCommandLineArguments(parser);

    	// Parse Results
    	ParserResult result = parser.parseArguments(arguments, false);

    	if (result.message != null) {
    		System.err.println(result.message);
    		return null;
    	}

    	try {
	    	pinger.parseCommandLine(result);
	    	writer.parseCommandLine(result);
	    	resolver.parseCommandLine(result);
	    	strategy.parseCommandLine(result);
	    	connector.parseCommandLine(result);
    	} catch (IllegalArgumentException e) {
    		System.err.println(e.getMessage());
    		return null;
    	}



		// Loading Ping-Pinger.
		MinePing system = new MinePing(
				pinger,
				writer,
				resolver,
				strategy,
				connector,
				(OutputStream) baseOptions.getValue("output"),
				MinePing.staticlogger,
				(String) baseOptions.getValue("port"),
				(Integer) baseOptions.getValue("flush-results")
		);

		MinePingInstances.setMinePing(system);
		MinePingInstances.setStarter(this);

    	return system;
	}

	public static String[] castObjectArray(Object array) {
		return Arrays.copyOf((Object[]) array, ((Object[])array).length, String[].class);
	}

	private static Logger getLogger(OutputStream stream, String level) {

		Handler handler = new StreamWriter(stream, new LogFormatter());
		handler.setLevel(Level.parse(level));

		Logger result = Logger.getLogger("MinePing");
		result.setUseParentHandlers(false);
		result.addHandler(handler);
		result.setLevel(Level.ALL);

		return result;

	}


    public static void main( String[] args )
    {
    	OptionParser parser = new OptionParser();
    	parser.addOption(new OptionBuilder("extensions").hasArgument(true).setType(DirectoryType.EXISTING).setDefault(new File(".", "extensions")).setMulti(true).setShortName("ext").create());
    	parser.addOption(new OptionBuilder("help").setShortName("h").create());

    	ParserResult extensionLoader = parser.parseArguments(args, true);

    	if (extensionLoader.isGiven("help")) {
    		showHelp(extensionLoader);
    		return;
    	}

    	Starter app = new Starter((File) extensionLoader.getValue("extensions"), args);

    	MinePing mp = app.parseArguments();

    	if (mp == null) {
    		showHelp(extensionLoader);
    		return;
    	}

    	mp.startPing();
    }

    private static void showHelp(ParserResult result) {
    	printFile("arguments.txt");
    }

    private static void printFile(String string) {
    	InputStream stream = Starter.class.getClassLoader().getResourceAsStream(string);

    	int b;
    	try {
			while ((b = stream.read()) != -1) {
				System.out.print((char) b);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static List<Extension> parseExtensions(Class<?>[] cls) {

    	if (cls == null)
    		return new LinkedList<Extension>();

    	List<Extension> result = new ArrayList<Extension>();

    	for (Class<?> clazz : cls) {

    		if (!clazz.isAssignableFrom(Extension.class))
    			throw new IllegalArgumentException("The class is not an extension.");

    		Extension extension = null;
    		try {
				extension = (Extension) clazz.newInstance();
			} catch (InstantiationException e) {
				throw new IllegalArgumentException(e.toString());
			} catch (IllegalAccessException e) {
				throw new IllegalArgumentException(e.toString());
			}
    		result.add(extension);
    	}

    	return result;
    }

	public static Class<?>[] castObjectArray2Class(Object array) {
		if (array == null)
			return new Class<?>[0];

		return Arrays.copyOf((Object[]) array, ((Object[])array).length, Class[].class);
	}
}

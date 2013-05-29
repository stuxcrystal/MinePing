package net.stuxcrystal.minehack.clparser;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;

/**
 * This class parses the options.
 * @author Stux
 *
 */
public class OptionParser {

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//                                                  Variables                                                     //
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * The registered options.
	 */
	private List<Option> options = new LinkedList<Option>();

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//                                               Constructor                                                      //
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Do nothing
	 */
	public OptionParser() {

	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//                                                 Adders                                                         //
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Adds an option.
	 * @param options
	 */
	public void addOption(Option... options) {
		for (Option option : options) {
			this.options.add(option);
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//                                                Functions                                                       //
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Returns the Option with the given short name.
	 *
	 * @param string
	 * @return
	 */
	public Option getOptionByShortName(String string) {
		for (Option option : this.options) {
			if (option.getShortName() == null)
				continue;

			if (option.isCaseSensitive() && option.getShortName().equals(string))
				return option;
			else if (!option.isCaseSensitive() && option.getShortName().equalsIgnoreCase(string))
				return option;
		}

		return null;
	}

	/**
	 * Returns the Option with the given long name.
	 * @param string
	 * @return
	 */
	public Option getOptionByLongName(String string) {
		for (Option option : this.options) {
			if (option.isLongNameCaseSensitive() && option.getLongName().equals(string))
				return option;
			else if (!option.isLongNameCaseSensitive() && option.getLongName().equalsIgnoreCase(string))
				return option;
		}

		return null;
	}

	/**
	 * Parse the Arguments passed in System.args
	 * @param args
	 */
	public ParserResult parseArguments(String[] args, boolean allowUnknown) {

		Map<Option, Object> result = new HashMap<Option, Object>();

		for (String arg : args) {
			Option option;

			// Search the option by its long name if the argument starts with two "-".
			if (arg.startsWith("--")) {
				option = getOptionByLongName(arg.substring(2).split("=", 2)[0]);

			// Search the option by its short name if the argument starts with one "-".
			} else if (arg.startsWith("-")) {
				option = getOptionByShortName(arg.substring(1).split("=", 2)[0]);

			// Fail if an argument does not start with a "-".
			} else {
				return new ParserResult(options, null, "Cannot parse option.");
			}

			// Ignore an option that is not supported.
			if (allowUnknown && option == null)
				continue;
			// Fail if no unknown options are allowed.
			else if (!allowUnknown && option == null)
				return new ParserResult(options, null, "Unknwon option: " + arg.split("=", 2)[0]);

			// Fail if a option cannot be parsed.
			String parse = parseOption(result, option, arg);
			if (parse != null)
				return new ParserResult(options, null, parse);
		}

		// Check if all required options are given.
		for (Option option : options) {
			if (option.isRequired() && !result.containsKey(option))
				return new ParserResult(options, null, "Missing option: --" + option.getLongName());
		}


		return new ParserResult(options, result, null);
	}

	/**
	 * Parse one option.
	 * @param result
	 * @param option
	 * @param arg
	 * @return
	 */
	private String parseOption(Map<Option, Object> result, Option option, String arg) {

		// Fail if the option requires an argument.
		if (option.hasArgument() && !arg.contains("="))
			return "The option --" + option.getLongName() + " has an argument.";

		if (option.hasArgument()) {

			// Split the argument
			String value = arg.substring(2).split("=", 2)[1];

			// Cache the result value.
			Object resultValue = null;

			// Just set the result value to the raw string.
			if (option.getType() == null) {
				resultValue = value;

			// Convert the value.
			} else {
				try {
					resultValue = option.getType().parseValue(value);
				} catch (IllegalArgumentException e) {
					return e.getLocalizedMessage();
				}
			}

			// Create an array if an option can occur multiple times.
			if (option.isMulti()) {

				// Add a value to the array if the option was already added.
				if (result.containsKey(option))
					result.put(option, ArrayUtils.add((Object[]) result.get(option), resultValue));

				// Create a new array if this is the first occurence.
				else
					result.put(option, new Object[] {resultValue});
			} else {

				// Fail if this option is already used.
				if (result.containsKey(option))
					return "Option --" + option.getLongName() + " is already given.";

				// Add the value.
				result.put(option, resultValue);
			}

		} else {
			result.put(option, true);
		}

		return null;
	}
}

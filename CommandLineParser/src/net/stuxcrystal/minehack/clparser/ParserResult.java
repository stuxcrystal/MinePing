package net.stuxcrystal.minehack.clparser;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ParserResult {

	public final List<Option> options;

	public final Map<Option, Object> arguments;

	public final String message;

	public ParserResult(List<Option> options, Map<Option, Object> arguments, String message) {
		this.options = Collections.unmodifiableList(options);
		this.arguments = arguments != null ? Collections.unmodifiableMap(arguments) : null;
		this.message = message;
	}

	/**
	 * Returns the value.<p />
	 *
	 * This function is threadsafe.
	 * @param option
	 */
	@SuppressWarnings("unchecked")
	public <T> T getValue(Option option) {
		if (option == null)
			return null;

		synchronized(this) {
			if (arguments.containsKey(option))
				return (T) arguments.get(option);
			return (T) option.getDefaultValue();
		}
	}

	/**
	 * Returns the value by its long name.<p />
	 *
	 * This function is threadsafe.
	 * @param longName
	 * @return
	 */
	public <T> T getValue(String longName) {
		return getValue(getOptionByLongName(longName));
	}

	/**
	 * Returns the value as an boolean value.<p />
	 *
	 * This function thread-safe.
	 * @param option
	 * @return
	 */
	public boolean getBooleanValue(Option option) {
		Object o = getValue(option);

		if (!(o instanceof Boolean))
			return false;

		return o == null ? false : ((o instanceof Boolean) ? (Boolean) o : true);
	}

	/**
	 * Returns the value by its long name as a boolean.<p />
	 *
	 * This function is threadsafe.
	 * @param longName
	 * @return
	 */
	public boolean getBooleanValue(String longName) {
		return getBooleanValue(getOptionByLongName(longName));
	}

	/**
	 * Returns true if the argument was given.
	 * @param longName
	 * @return
	 */
	public boolean isGiven(String longName) {
		return arguments.containsKey(getOptionByLongName(longName));
	}

	/**
	 * Returns the Option with the given short name.
	 *
	 * @param string
	 * @return
	 */
	public Option getOptionByShortName(String string) {
		for (Option option : this.options) {
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
}

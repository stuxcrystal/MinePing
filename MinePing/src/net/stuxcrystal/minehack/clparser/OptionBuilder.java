package net.stuxcrystal.minehack.clparser;

public class OptionBuilder {

	private static Option option = null;

	public OptionBuilder(String longName) {
		option = new Option();
		option.setLongName(longName);
	}

	public OptionBuilder setShortName(String longName) {
		option.setShortName(longName);
		return this;
	}

	public OptionBuilder hasArgument(boolean has) {
		option.setHasArgument(has);
		return this;
	}

	public OptionBuilder setType(Type type) {
		this.hasArgument(true);
		option.setType(type);
		return this;
	}

	public OptionBuilder setMulti(boolean multi) {
		option.setMulti(multi);
		return this;
	}

	public OptionBuilder setDefault(Object defaultValue) {
		option.setDefaultValue(defaultValue);
		return this;
	}

	public OptionBuilder required(boolean required) {
		option.setRequired(required);
		return this;
	}

	public OptionBuilder caseSensitive(boolean longName, boolean shortName) {
		option.setCaseSensitive(shortName);
		option.setLongNameCaseSensitive(longName);
		return this;
	}

	public Option create() {
		return option;
	}

}

package net.stuxcrystal.minehack.clparser;

/**
 * All Options are represented here.
 * @author Stux
 *
 */
public class Option {

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//                                                  Variables                                                     //
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * The long name.<p />
	 * --&lt;longName&gt;
	 */
	private String longName = null;

	/**
	 * The short character.<p />
	 * -&lt;shortName&gt;
	 */
	private String shortName = null;

	/**
	 * Has the argument a value?.<p />
	 * &lt;Argument&gt;=&lt;Value&gt;
	 */
	private boolean hasArgument = false;

	/**
	 * The Output-Type.
	 */
	private Type type = null;

	/**
	 * Stores the default value.
	 */
	private Object defaultValue = null;

	/**
	 * Can this option appear multiple times?
	 */
	private boolean multi = false;

	/**
	 * Is this option required?
	 */
	private boolean required = false;

	/**
	 * Is the short name case-sensitive?
	 */
	private boolean caseSensitive = false;

	/**
	 * Is the long name case-sensitive?
	 */
	private boolean longNameCaseSensitive = false;

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//                                               Constructor                                                      //
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Constructor for OptionBuilder.
	 */
	Option() {

	}

	public Option(String longName) {
		this(longName, null, false, null);
	}

	public Option(String longName, String shortName) {
		this(longName, shortName, false, null);
	}

	public Option(String longName, boolean hasArgument) {
		this(longName, null, hasArgument, null);
	}

	public Option(String longName, String shortName, boolean hasArgument) {
		this(longName, shortName, hasArgument, null);
	}

	public Option(String longName, boolean hasArgument, Type type) {
		this(longName, null, hasArgument, type);
	}

	public Option(String longName, String shortName, boolean hasArgument, Type type) {
		this.longName    = longName;
		this.shortName   = shortName;
		this.hasArgument = hasArgument;
		this.type        = type;

		if (!this.hasArgument)
			this.defaultValue = false;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//                                            Getters and Setters                                                 //
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public String getLongName() {
		return longName;
	}

	public void setLongName(String longName) {
		this.longName = longName;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public boolean hasArgument() {
		return hasArgument;
	}

	public void setHasArgument(boolean hasArgument) {
		this.hasArgument = hasArgument;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Object getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}

	public boolean isMulti() {
		return multi;
	}

	public void setMulti(boolean multi) {
		this.multi = multi;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public boolean isCaseSensitive() {
		return caseSensitive;
	}

	public void setCaseSensitive(boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
	}

	public boolean isLongNameCaseSensitive() {
		return longNameCaseSensitive;
	}

	public void setLongNameCaseSensitive(boolean longNameCaseSensitive) {
		this.longNameCaseSensitive = longNameCaseSensitive;
	}
}

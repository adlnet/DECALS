package com.eduworks.resolver.enumeration;

@Deprecated
public enum ResolverMatchOption implements ResolverEnum
{
	ALL("all"),
	ANY("any"),
	IN_ARRAY("inArray"),
	IN_ITEM("inItem"),
	IN_OBJECT("inObject"),
	;

	/* SHARED MEMBERS */

	public final static String DEFAULT_KEY = "match";

	public static ResolverMatchOption optionForKeyValue(String matchKey)
	{
		for (ResolverMatchOption matchVal : values())
			if (matchVal.value.equalsIgnoreCase(matchKey))
				return matchVal;

		return null;
	}

	/* INDIVIDUAL MEMBERS */

	private final String key;
	private final String value;

	private ResolverMatchOption(String value)
	{
		this.key = DEFAULT_KEY;
		this.value = value;
	}

	@Override
	public String getKey()
	{
		return key;
	}

	@Override
	public String getValue()
	{
		return value;
	}
}

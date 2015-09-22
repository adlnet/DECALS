package com.eduworks.resolver.enumeration;

@Deprecated
public enum ResolverStringOp implements ResolverEnum
{
	LENGTH("length"),
	CAPITALIZE("caps"),
	FORMAT("format"),
	REPLACE("replace"),
	REVERSE("reverse"),
	SPLIT("split"),
	SUBSTR("substr"),
	TRIM("trim"),
	TO_LOWER("lower"),
	TO_TITLE("title"),
	TO_UPPER("upper")
	;

	public final static String DEFAULT_KEY = "str";

	public static ResolverStringOp operationForKeyValue(String opKey)
	{
		for (ResolverStringOp opVal : values())
		{
			if (opVal.value.equalsIgnoreCase(opKey))
				return opVal;

			// TODO: "tolower" key is deprecated, remove in next version
			else if (opVal == TO_LOWER && "tolower".equalsIgnoreCase(opKey))
				return TO_LOWER;
		}

		return ResolverStringOp.FORMAT; // Default operation
	}

	/* INDIVIDUAL MEMBERS */

	private final String key;
	private final String value;

	private ResolverStringOp(String value)
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
		return this.value;
	}
}

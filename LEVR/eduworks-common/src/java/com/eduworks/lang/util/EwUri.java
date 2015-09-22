package com.eduworks.lang.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class EwUri
{
	public static final String UTF_8 = "UTF-8";

	public static String encodeValue(String value)
	{
		try
		{
			return URLEncoder.encode(decodeValue(value), UTF_8);
		}
		catch (UnsupportedEncodingException uee)
		{
		}

		return value;
	}

	public static String decodeValue(String value)
	{
		if (value == null) return null;
		try
		{
			String decode = URLDecoder.decode(value, UTF_8);
			if (decode == null) return value;
			return decode.replace("\\n", "\n");
		}
		catch (UnsupportedEncodingException uee)
		{
		}
		catch (IllegalArgumentException iae)
		{
		}

		return value;
	}

}

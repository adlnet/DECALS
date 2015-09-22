package com.eduworks.resolver.string;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Resolver;
import com.eduworks.resolver.enumeration.ResolverStringOp;


public class ResolverString extends Resolver
{
	public static final String OP_KEY = "op";
	public static final String REGEX_KEY = "regex";

	@Override
	public Object resolve(Context c, Map<String,String[]> parameters, Map<String,InputStream> dataStreams) throws JSONException
	{
		resolveAllChildren(c, parameters, dataStreams);

		final String strKey = ResolverStringOp.DEFAULT_KEY;
		String string = getAsString(strKey, parameters);
		if (string == null || string.isEmpty())
			string = getAsString("obj",parameters);
		final ResolverStringOp operation = ResolverStringOp.operationForKeyValue(getAsString(OP_KEY, parameters));

		switch (operation)
		{
			case CAPITALIZE:
				return capitalize(string);

			case FORMAT:
				return format(string, parameters, strKey);

			case REPLACE:
				return string.replaceAll(
						decodeValue(optAsString(REGEX_KEY, parameters)),
						optAsString("with", parameters)
					);

			case REVERSE:
				return new StringBuilder(string).reverse().toString();

			case SPLIT:
				return Arrays.asList(string.split(optAsString(REGEX_KEY, parameters)));

			case SUBSTR:
				int optInt = optInt("begin");
				int optInt2 = optInt("end", string.length());
				return string.substring(Math.min(optInt,string.length()), Math.min(optInt2,string.length()));

			case TRIM:
				return trim(string, optAsString("chars", parameters), optAsBoolean("trimQuotes", false, parameters));

			case TO_LOWER:
				return string.toLowerCase();

			case TO_TITLE:
				return toTitleCase(string, optAsBoolean("lowerFirst", false, parameters));

			case TO_UPPER:
				return string.toUpperCase();
				
			case LENGTH:
				return string.length();
		}

		return string;
	}

	/**
	 * Capitalizes a single word, or all the words in a string
	 * (does not handle camel-case, acronyms, or special chars).
	 * @see #toTitleCase(String, boolean)
	 * */
	protected String capitalize(String string)
	{
		if (string == null || string.isEmpty()) return string;

		final StringBuilder capitalized = new StringBuilder(string.length());
		final String[] words = string.split("\\s");

		for (int i = 0;;)
		{
			final String word = words[i];
			final char first = word.charAt(0);

			if (Character.isLowerCase(first))
			{
				capitalized.append(Character.toUpperCase(first));
				capitalized.append(word.substring(1));
			}
			else
				capitalized.append(word);

			if (++i < words.length)
				capitalized.append(' ');

			else break;
		}

		return capitalized.toString();
	}

	/**
	 * Replaces $(key) in the format string with the corresponding value from this JSONObject,
	 * or the attached parameters. Setting keys and any keys specified in the ignore array may
	 * not be referenced in the format string, or an exception is thrown.
	 */
	protected String format(String format, Map<String,String[]> parameters, String ... ignore)
	{
		// Approximate the result length: format string + 16 character args
	    final StringBuilder sb = new StringBuilder(format.length() + (parameters.size()*16));

	    final char escChar = '\\';
	    final char derefChar = '$';
	    final char openDelim = '(';
	    final char closeDelim = ')';

	    int cur = 0;
	    int len = format.length();
	    int open;
	    int close;

	    WHILE:
	    while (cur < len)
	    {
	        switch (open = format.indexOf(openDelim, cur))
	        {
	        	case -1:
	        		// No open paren: just append the string as is
	        		sb.append(format.substring(cur, len));
	        		break WHILE;

	        	default:
	        		// Found open paren: append everything leading up to it
		            sb.append(format.substring(cur, open));

		            switch (close = format.indexOf(closeDelim, open))
		            {
		            	case -1:
			        		// No close paren: append the rest of the string
		            		sb.append(format.substring(open));
			        		break WHILE;

	            		default:
	            			// Does a dollar sign precede the open paren?
	            			if (open > 0 && format.charAt(open-1) == derefChar)
	            			{
	            				// Is the dollar escaped?
	            				if (open > 1 && format.charAt(open-2) == escChar)
	            				{
            						// Remove escape and dollar sign
            						sb.setLength(sb.length() - 2);

	            					// Append escaped dollar and open, and continue from there
	            					sb.append(derefChar).append(openDelim);

	            					cur = (open + 1);

	            					continue;
	            				}
	            				else
	            				{
	            					// Parse the paren delimited key: "$(key)"
	            					final String fmtKey = format.substring(open + 1, close);

	            					try
	            					{
	            						if (isSetting(fmtKey)) throw new Exception("Key is Setting.");

	            						for (int i = 0; i < ignore.length; i++)
	            							if (fmtKey.equals(ignore[i]))
	            								throw new Exception("Key is Ignored.");

	            						// Remove preceding dollar sign
	            						sb.setLength(sb.length() - 1);

	            						// Append the corresponding param value
	            						sb.append(getAsString(fmtKey, parameters));
	            					}
	            					catch (Exception e)
	            					{
	            						// Append the dollar sign, the parens and the original delimited value
	            						sb.append(derefChar).append(openDelim).append(fmtKey).append(closeDelim);
	            					}
	            				}

	            				cur = close + 1; // Continue after the closing paren
	            			}
	            			else
	            			{
	            				/* No dollar sign before open: find next one and continue from there */

	            				final int nextOpen = format.indexOf(openDelim, open + 1);

	            				if (nextOpen != -1 && nextOpen < close)
									cur = nextOpen;		// Continue at next open paren before close
								else
									cur = (close + 1);	// No open before close: continue after close

	            				sb.append(format.substring(open, cur));
	            			}
		            }
	        }
	    }

	    return sb.toString();
	}

	/**
	 * Title case sentence, or capitalize a word: if lowerFirst is false,
	 * camel-cased words are parsed, and acronyms such as "JSONObject" are separated:
	 * "JSON Object". In either case, underscores '_' and spaces delimit words,
	 * whereas words with dashes '-' and single quotes are considered single words.
	 */
	public String toTitleCase(String string, boolean lowerFirst)
	{
		if (string == null || string.isEmpty()) return string;

		final char nullChar = '\0';

		char[] chars = (lowerFirst)
			? string.toLowerCase().toCharArray()
			: string.toCharArray();

		int length = chars.length;

		boolean capNext = true;
		boolean skipNext = false;
		boolean spaceBefore = false;
		boolean spaceAfter = false;

		char last;
		char next;

		StringBuilder title = new StringBuilder(length * 2);

		for (int i = 0, n = 1; i < length; i++, n++)
		{
			last = (i > 0) ? chars[i-1] : nullChar;
			next = (n < length) ? chars[n] : nullChar;

			switch (chars[i])
			{
				case '_':
					chars[i] = ' '; // Fall through as space

				case '\t':
				case ' ':
					title.append(chars[i]);
					capNext = true;
					skipNext = true;
					break;

				default:
					if (isWordChar(chars[i]))
					{
						if (capNext)
						{
							/* Capitalize only words with more than one letter, excepting 'i' */
							if (i == 0 || i == length - 1 || isWordChar(next) || chars[i] == 'i')
							{
								chars[i] = Character.toUpperCase(chars[i]);
								capNext = false;
							}
						}
						else if (!skipNext)
						{
							/* Consider previous and following letter case when inserting spaces */
							if (Character.isUpperCase(chars[i]))
							{
								if (Character.isLowerCase(last) || Character.isLowerCase(next))
									spaceBefore = true;
							}
							else if (Character.isUpperCase(next))
								spaceAfter = true;

							skipNext = (spaceBefore || spaceAfter);
						}
						else skipNext = false;

						/* Append character and spaces before and/or after */

						if (spaceBefore) title.append(' ');

						title.append(chars[i]);

						if (spaceAfter) title.append(' ');
					}
					else // Not a word character
					{
						title.append(chars[i]);

						/* Insert space after if not a word char and next is a letter */
						if (Character.isLetter(next)) title.append(' ');

						capNext = true;
					}

					spaceBefore = false;
					spaceAfter = false;
					break;
			}
		}

		return title.toString();
	}

    /** @return true if ch is a character that may appear in a single word. */
	private static boolean isWordChar(char ch)
	{
		switch (ch)
		{
			case '-':
			case '\'':
				return true;
			default:
				return Character.isLetter(ch);
		}
	}

	protected String trim(String string, String chars, boolean trimQuotes)
	{
		if (string == null) return null;

		if(trimQuotes){
			chars = chars.concat("\"");
		}
		
		if (chars == null || chars.isEmpty()) return string.trim();		
		
		int beginIndex = 0;
		int endIndex = string.length();
		int length = string.length();

		for (int i = 0; i < length; i++)
		{
			if (chars.indexOf(string.charAt(i)) == -1)
				break;
			else beginIndex++;
		}

		for (int j = length-1; j >= beginIndex ; j--)
		{
			if (chars.indexOf(string.charAt(j)) == -1)
				break;
			else endIndex--;
		}

		return string.substring(beginIndex, endIndex);
	}

	@Override
	public String getDescription()
	{
		return "Performs string operations. Input is 'str', operation depends on 'op'." +

	"\ncaps - Capitalize all words." +
	"\nformat - Formats the words according to standard $1...$n formatting seen in C and C++" +
	"\nreplace - Replaces a regex 'regex' string with 'with'." +
	"\nreverse - Reverses the string." +
	"\nsplit - Splits the string based on 'regex'" +
	"\nsubstr - Gets a substring of the string based on 'begin' and 'end'" +
	"\ntrim - Trims the string." +
	"\nlower - Lowercases the string." +
	"\ntitle - Converts the string to title case." +
	"\nupper - Converts the string to upper case.";
	}

	@Override
	public String getReturn()
	{
		return "String";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("str","String","op","String","?<any>","String|Number");
	}
}

package com.eduworks.lang;


public class EwStringUtil
{

	public static String join(String[] strings, String delim)
	{
		StringBuilder result = new StringBuilder();
		for (int i = 0;i < strings.length;i++)
		{
			if (strings[i] == null) continue;
			if (i > 0)
				result.append(delim);
			result.append(strings[i]);
		}
		return result.toString();
	}
	public static String tabs(int tablevel)
	{
		StringBuilder sb = new StringBuilder(tablevel);
		for (;tablevel > 0;tablevel--)
			sb.append('\t');
		return sb.toString();
	}
	public static String spaces(int tablevel)
	{
		StringBuilder sb = new StringBuilder(tablevel);
		for (;tablevel > 0;tablevel--)
			sb.append(' ');
		return sb.toString();
	}
	public static String stringToHTMLString(String string) {
	    StringBuffer sb = new StringBuffer(string.length());
	    // true if last char was blank
	    boolean lastWasBlankChar = false;
	    int len = string.length();
	    char c;

	    for (int i = 0; i < len; i++)
	        {
	        c = string.charAt(i);
	        if (c == ' ') {
	            // blank gets extra work,
	            // this solves the problem you get if you replace all
	            // blanks with &nbsp;, if you do that you loss 
	            // word breaking
	            if (lastWasBlankChar) {
	                lastWasBlankChar = false;
	                sb.append("&nbsp;");
	                }
	            else {
	                lastWasBlankChar = true;
	                sb.append(' ');
	                }
	            }
	        else {
	            lastWasBlankChar = false;
	            //
	            // HTML Special Chars
	            if (c == '"')
	                sb.append("&quot;");
	            else if (c == '&')
	                sb.append("&amp;");
	            else if (c == '<')
	                sb.append("&lt;");
	            else if (c == '>')
	                sb.append("&gt;");
	            else if (c == '\n')
	                // Handle Newline
	                sb.append("&lt;br/&gt;");
	            else {
	                int ci = 0xffff & c;
	                if (ci < 160 )
	                    // nothing special only 7 Bit
	                    sb.append(c);
	                else {
	                    // Not 7 Bit use the unicode system
	                    sb.append("&#");
	                    sb.append(new Integer(ci).toString());
	                    sb.append(';');
	                    }
	                }
	            }
	        }
	    return sb.toString();
	}
}

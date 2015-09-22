package com.eduworks.resolver.io;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.interfaces.EwJsonSerializable;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Resolver;

public class ResolverDisplayXml extends Resolver
{
	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		resolveAllChildren(c, parameters, dataStreams);
		String name = "result";

		for (String key : keySet())
		{
			if (isSetting(key) || !has(key))
			{
				remove(key);
				continue;
			}
			if (key.equals("enclosingTagName"))
			{
				name = getAsString("enclosingTagName", parameters);
				remove("enclosingTagName");
				continue;
			}

			Object value = get(key, parameters);
			if (value instanceof EwJsonSerializable)
				put(key, ((EwJsonSerializable) value).toJsonObject());
			else
				put(key,value);
		}
		return "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" + toString(this, name);
	}

	/**
	 * Replace special characters with XML escapes:
	 * 
	 * <pre>
	 * &amp; <small>(ampersand)</small> is replaced by &amp;amp;
	 * &lt; <small>(less than)</small> is replaced by &amp;lt;
	 * &gt; <small>(greater than)</small> is replaced by &amp;gt;
	 * &quot; <small>(double quote)</small> is replaced by &amp;quot;
	 * </pre>
	 * 
	 * @param string
	 *            The string to be escaped.
	 * @return The escaped string.
	 */
	public static String escape(String string)
	{
		StringBuffer sb = new StringBuffer();
		for (int i = 0, length = string.length(); i < length; i++)
		{
			char c = string.charAt(i);
			switch (c)
			{
				case '&':
					sb.append("&amp;");
					break;
				case '<':
					sb.append("&lt;");
					break;
				case '>':
					sb.append("&gt;");
					break;
				case '"':
					sb.append("&quot;");
					break;
				case '\'':
					sb.append("&apos;");
					break;
				default:
					sb.append(c);
			}
		}
		return sb.toString();
	}

	/**
	 * Convert a JSONObject into a well-formed, element-normal XML string.
	 * 
	 * @param object
	 *            A JSONObject.
	 * @return A string.
	 * @throws JSONException
	 */
	public static String toString(Object object) throws JSONException
	{
		return toString(object, null);
	}

	/**
	 * Convert a JSONObject into a well-formed, element-normal XML string.
	 * 
	 * @param object
	 *            A JSONObject.
	 * @param tagName
	 *            The optional name of the enclosing tag.
	 * @return A string.
	 * @throws JSONException
	 */
	public static String toString(Object object, String tagName) throws JSONException
	{
		StringBuffer sb = new StringBuffer();
		int i;
		JSONArray ja;
		JSONObject jo;
		String key;
		Iterator keys;
		int length;
		String string;
		StringBuilder header = new StringBuilder();
		Object value;
		if (object instanceof JSONObject)
		{

			// Emit <tagName>

			if (tagName != null)
			{
				header.append('<');
				header.append(tagName);
			}

			// Loop thru the keys.

			jo = (JSONObject) object;
			keys = jo.keys();
			while (keys.hasNext())
			{
				key = keys.next().toString();
				value = jo.opt(key);
				if (value == null)
				{
					value = "";
				}
				if (value instanceof String)
				{
					string = (String) value;
				}
				else
				{
					string = null;
				}

				if (key.startsWith("attr"))
				{
					header.append(" "+key.substring("attr".length()) + "=\""+value+"\"");
				}
				else if (value instanceof JSONArray)
				{
					ja = (JSONArray) value;
					length = ja.length();
					for (i = 0; i < length; i += 1)
					{
						value = ja.get(i);
						if (value instanceof JSONArray)
						{
							sb.append('<');
							sb.append(key);
							sb.append('>');
							sb.append(toString(value));
							sb.append("</");
							sb.append(key);
							sb.append('>');
						}
						else
						{
							sb.append(toString(value, key));
						}
					}
				}
				else if ("".equals(value))
				{
					sb.append('<');
					sb.append(key);
					sb.append("/>");

					// Emit a new tag <k>

				}
				else
				{
					sb.append(toString(value, key));
				}
			}
			if (tagName != null)
			{

				// Emit the </tagname> close tag

				sb.append("</");
				sb.append(tagName);
				sb.append('>');
			}
			header.append('>');
			return header.toString()+sb.toString();

			// XML does not have good support for arrays. If an array appears in
			// a place
			// where XML is lacking, synthesize an <array> element.

		}
		else
		{
			if (object.getClass().isArray())
			{
				object = new JSONArray(object);
			}
			if (object instanceof JSONArray)
			{
				ja = (JSONArray) object;
				length = ja.length();
				for (i = 0; i < length; i += 1)
				{
					sb.append(toString(ja.opt(i), tagName == null ? "array" : tagName));
				}
				return sb.toString();
			}
			else
			{
				string = (object == null) ? "null" : escape(object.toString());
				return (tagName == null) ? "\"" + string + "\"" : (string.length() == 0) ? "<" + tagName + "/>" : "<" + tagName + ">" + string + "</" + tagName
						+ ">";
			}
		}
	}

	@Override
	public String getDescription()
	{
		return "Returns the resultant data as JSON converted to XML." + "enclosingTagName = The tag to wrap the XML object in, instead of 'obj'";
	}

	@Override
	public String getReturn()
	{
		return "String";
	}

	@Override
	public String getAttribution()
	{
		return "ATTRIB_NONE";
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("<any>", "Object|Array|String|Number|Boolean");
	}

}

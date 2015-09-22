package com.eduworks.lang.json.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.eduworks.lang.EwList;
import com.eduworks.lang.json.EwJsonCollection;
import com.eduworks.lang.util.EwJson;
import com.eduworks.lang.util.EwUri;

/**
 * A class to extend {@link JSONObject} and implement {@link EwJsonCollection}.
 * Provides the following features and functionality:
 * <ul>
 * <li>enhances all JSONObject behavior even when JSONObject methods are called</li>
 * <li>processes complex array keys "key[0].key[next]" against nested JSON
 * objects</li>
 * <li>wraps incoming/outgoing {@link JSONArray}s as EwJsonArrays</li>
 * <li>wraps incoming/outgoing {@link JSONObject}s as {@link EwJsonObject}s</li>
 * </ul>
 * <p>
 * This list can and should grow as Eduworks applications have need of new JSON
 * array functionality.
 * </p>
 * 
 * @author dharvey
 * @since September, 2011
 */
@SuppressWarnings("rawtypes")
public class EwJsonObject extends JSONObject implements EwJsonCollection
{
	/* STATIC MEMBERS */
	public static EwJsonObject convert(JSONObject object)
	{
		if (object == null)
			return null;

		if (object instanceof EwJsonObject)
			return (EwJsonObject) object;

		try
		{
			return new EwJsonObject(object);
		}
		catch (JSONException je)
		{
		}

		return null;
	}

	/**
	 * Convert String or {@link JSONObject} to EwJsonObject, and put and return
	 * it if it is valid.
	 * 
	 * @throws JSONException
	 *             if key is not found or value cannot be converted to
	 *             {@link JSONObject}
	 */
	public static EwJsonObject getJSONObject(EwJsonCollection json, Object ref) throws JSONException
	{
		final Object object = json.get(ref);

		if (object instanceof EwJsonObject)
			return (EwJsonObject) object;

		else if (object instanceof JSONObject)
			return convert((JSONObject) object);

		else if (object instanceof Collection)
			return new EwJsonObject(object);

		// Convert String to EwJsonArray and put it before returning
		else if (object instanceof String)
		{
			final EwJsonObject jsonObject = convert(EwJson.getJsonObject((String) object));

			if (jsonObject != null)
			{
				json.put(ref, jsonObject);
				return jsonObject;
			}
		}

		throw buildGetterException(ref, "is not a JSONObject");
	}

	/**
	 * Merges values from an {@link EwJsonCollection} to a {@link EwJsonObject}.
	 * If "ref" is not null, merge "from" with any existing json value at "ref"
	 * -- a non-json value at "ref" will be overwritten. If "from" is an
	 * {@link EwJsonArray}, its values will be merged using the indices as keys.
	 */
	public static EwJsonObject merge(EwJsonObject into, EwJsonCollection from, Object ref) throws JSONException
	{
		if (into == null && from == null)
			return null;

		else if (into == null)
			return (from instanceof EwJsonObject) ? (EwJsonObject) from : null;

		else if (from == null)
			return into;

		if (ref != null)
		{
			final Object existing = into.opt(ref);

			if (EwJson.isJson(existing))
				EwJson.tryMerge(existing, from, null);
			else
				into.put(ref, from);
		}
		else if (from instanceof JSONArray)
		{
			for (int i = 0; i < from.length(); i++)
				into.put(new Integer(i).toString(), EwJson.wrap(from.get(i)));
		}
		else if (from instanceof JSONObject)
		{
			EwJson.merge(into, (JSONObject) from);
		}

		return into;
	}

	/**
	 * @see #mergeFromString(JSONObject, String, Object)
	 * @return a new {@link EwJsonObject} parsed from source string
	 */
	public static EwJsonObject parse(String source) throws JSONException
	{
		return mergeFromString(null, source, null);
	}

	/**
	 * Parse contents of json object String, and insert the new object at the
	 * specified key (ref). If object is null, an {@link EwJsonObject} is
	 * instantiated from source and returned. If source is null the object is
	 * converted to an {@link EwJsonObject} and returned. If ref is null the new
	 * object is merged with the the one passed in; otherwise the new object is
	 * merged with anything existing at the key slot specified by ref.
	 */
	public static EwJsonObject mergeFromString(JSONObject object, String source, Object ref) throws JSONException
	{
		final EwJsonObject ewObject = (object == null || ref != null) ? new EwJsonObject() // Parse
																							// a
																							// new
																							// object
																							// from
																							// source
				: convert(object); // Merge into provided object

		if (EwJson.isNull(source))
			return ewObject;

		final JSONTokener tokener = new JSONTokener(source);

		if (tokener.nextClean() != '{')
			throw tokener.syntaxError("EwJsonObject text must begin with '{'");

		/* Arguments processed; begin parsing */

		parsing: while (true)
		{
			final String key;

			switch (tokener.nextClean())
			{
				case 0:
					throw tokener.syntaxError("EwJsonObject text must end with '}'");

				case '}':
					break parsing;

				default:
					tokener.back();
					key = tokener.nextValue().toString();
			}

			/* A key must be followed by ':', '=' or '=>' */

			switch (tokener.nextClean())
			{
				case ':':
					break;

				case '=':
					if (tokener.next() != '>')
						tokener.back();
					break;

				default:
					throw tokener.syntaxError("Expected a ':', '=' or '=>' after a key");
			}

			final Object next = tokener.nextValue();
			ewObject.putOnce(key, next);

			/* Pairs are separated by ',' or ';' */

			switch (tokener.nextClean())
			{
				case '}':
					break parsing;

				case ';':
				case ',':
					if (tokener.nextClean() == '}')
						break parsing;
					else
						tokener.back();
					break;

				default:
					throw tokener.syntaxError("Expected a ',' or ';' delimiting a pair");
			}
		}

		final boolean putParsed = (ref != null);

		if (object == null && putParsed)
			return (new EwJsonObject()).put(ref, ewObject);

		else if (putParsed)
			return convert(object).put(ref, ewObject);

		else
			return ewObject;
	}

	/**
	 * If "from" is parsable as JSON, merge it with "into"; otherwise if ref and
	 * from are not null, put or merge with "from". Finally, return "into" as an
	 * EwJsonObject.
	 * 
	 * @see EwJson#tryParseJson(Object, boolean)
	 * @see EwJsonObject#merge(EwJsonObject, EwJsonCollection, Object)
	 */
	public static EwJsonCollection tryMergeAny(JSONObject into, Object from, Object ref) throws JSONException
	{
		if (into == null)
			return null;

		final EwJsonObject converted = convert(into);

		if (!EwJson.isNull(from))
		{
			final Object wrapped = EwJson.wrap(from, true);

			if (EwJson.isJson(wrapped))
				return EwJsonObject.merge(converted, EwJson.tryConvert(wrapped), ref);

			else if (wrapped != null && ref instanceof String)
				return converted.put((String) ref, wrapped);
		}

		return converted;
	}

	private LinkedHashSet<String> orderedKeys = new LinkedHashSet<String>();

	/* CONSTRUCTORS */

	public EwJsonObject()
	{
		super();
	}

	public EwJsonObject(String source) throws JSONException
	{
		this();

		EwJsonObject.mergeFromString(this, source, null);
	}

	public EwJsonObject(Map map)
	{
		this();

		if (map == null)
			return;

		try
		{
			final Iterator keys = map.entrySet().iterator();
			while (keys.hasNext())
			{
				final Map.Entry entry = (Map.Entry) keys.next();
				final Object value = entry.getValue();

				if (value != null)
					this.put(entry.getKey(), EwJson.wrap(value));
			}
		}
		catch (JSONException je)
		{
		}
	}

	public EwJsonObject(EwJsonCollection collection) throws JSONException
	{
		this();

		EwJsonObject.merge(this, collection, null);
	}

	/** Equivalent to JSONObject(JSONObject, String[]) */
	public EwJsonObject(JSONObject object, String[] names) throws JSONException
	{
		super(object, names);
	}

	/** Equivalent to JSONObject(Object, String[]) */
	public EwJsonObject(Object object, String[] names) throws JSONException
	{
		super(object, names);
	}

	/**
	 * Attempt to merge any object with this.
	 * 
	 * @see #merge(Object)
	 */
	public EwJsonObject(Object source) throws JSONException
	{
		this();

		if (EwJson.isNull(source))
			return;

		else if (source instanceof JSONObject)
		{
			EwJson.merge(this, (JSONObject) source);
		}
		else if (source instanceof String)
		{
			EwJsonObject.mergeFromString(this, (String) source, null);
		}
		else
		{
			EwJsonObject.tryMergeAny(this, source, null);
		}
	}

	/* OVERRIDDEN MEMBERS */

	/** Overridden to ensure {@link EwJsonArray}s are used for accumulation. */
	@Override
	public EwJsonObject accumulate(String key, Object value) throws JSONException
	{
		return (EwJsonObject) this.accumulate((Object) key, value);
	}

	@Override
	public EwJsonCollection accumulate(Object ref, Object value) throws JSONException
	{
		if (ref == null)
			throw new JSONException("Null key.");

		return EwJson.accumulate(this, ref, value);
	}

	@Override
	public boolean contains(Object element)
	{
		return EwJson.contains((JSONObject) this, element);
	}

	@Override
	public Object get(Object ref) throws JSONException
	{
		if (ref == null)
			throw new JSONException("Null key.");

		final String key = (ref instanceof String) ? (String) ref : ref.toString();

		return super.get(key);
	}

	@Override
	public boolean getBoolean(Object ref) throws JSONException
	{
		try
		{
			return EwJson.parseBoolean(this.get(ref));
		}
		catch (JSONException je)
		{
			throw buildGetterException(ref, "is not a boolean");
		}
	}

	@Override
	public double getDouble(Object ref) throws JSONException
	{
		try
		{
			return EwJson.parseDouble(this.get(ref));
		}
		catch (JSONException je)
		{
			throw buildGetterException(ref, "is not a double");
		}
	}

	@Override
	public int getInt(Object ref) throws JSONException
	{
		try
		{
			return EwJson.parseInt(this.get(ref));
		}
		catch (JSONException je)
		{
			throw buildGetterException(ref, "is not an integer");
		}
	}

	/**
	 * Overridden to convert Strings or {@link JSONArray}s to
	 * {@link EwJsonArray}s
	 */
	@Override
	public EwJsonArray getJSONArray(String key) throws JSONException
	{
		return this.getJSONArray((Object) key);
	}

	@Override
	public EwJsonArray getJSONArray(Object ref) throws JSONException
	{
		try
		{
			return EwJsonArray.getJSONArray(this, ref);
		}
		catch (JSONException je)
		{
			throw buildGetterException(ref, "is not a JSONArray", je.getMessage());
		}
	}

	/**
	 * Overridden to convert Strings or {@link JSONObject}s to
	 * {@link EwJsonObject}s
	 */
	@Override
	public EwJsonObject getJSONObject(String key) throws JSONException
	{
		return EwJsonObject.getJSONObject(this, key);
	}

	@Override
	public EwJsonObject getJSONObject(Object ref) throws JSONException
	{
		return EwJsonObject.getJSONObject(this, ref);
	}

	@Override
	public EwJsonCollection getJSONCollection(Object ref) throws JSONException
	{
		final EwJsonCollection collection = EwJson.tryConvert(this.opt(ref));

		if (collection != null)
			return collection;
		else
			throw buildGetterException(ref, "is not an EwJsonCollection");
	}

	@Override
	public long getLong(Object ref) throws JSONException
	{
		try
		{
			return EwJson.parseLong(this.get(ref));
		}
		catch (JSONException je)
		{
			throw buildGetterException(ref, "is not a long");
		}
	}

	@Override
	public String getString(Object ref) throws JSONException
	{
		try
		{
			return EwJson.parseString(this.get(ref));
		}
		catch (JSONException je)
		{
			throw buildGetterException(ref, "is not a string");
		}
	}

	@Override
	public EwJsonObject emptyInstance()
	{
		return new EwJsonObject();
	}

	/** Checks for presence of ref as a simple or complex key in the map. */
	@Override
	public boolean hasComplex(Object ref)
	{
		if (hasSimple(ref))
			return true;

		try
		{
			return EwJson.hasComplexKey(this, (String) ref);
		}
		catch (JSONException e)
		{
		}

		return false;
	}

	/** Ensures the key is a String and exists in the map. */
	@Override
	public boolean hasSimple(Object ref)
	{
		if (ref instanceof String)
			return super.has((String) ref);

		return false;
	}

	@Override
	public boolean isEmpty()
	{
		return (super.length() < 1);
	}

	/** Overridden for a more thorough null check. */
	@Override
	public boolean isNull(String key)
	{
		return super.isNull(key) || this.isNull((Object) key);
	}

	@Override
	public boolean isNull(Object ref)
	{
		return (ref == null || EwJson.isNull(this.opt(ref)));
	}

	/**
	 * Iterates over sorted keys and inserts separators between corresponding
	 * values
	 */
	@Override
	public String join(String separator) throws JSONException
	{
		final StringBuilder joined = new StringBuilder();
		final Iterator keys = sortedKeys();

		if (keys.hasNext())
		{
			joinNextKeyAndValue(this, keys, joined);

			while (keys.hasNext())
				joinNextKeyAndValue(this, keys, joined.append(separator));
		}

		return joined.toString();
	}

	public Iterator sortedKeys()
	{
		List l = new ArrayList(keySet());
		Collections.sort(l);
		return l.iterator();
	}
	@SuppressWarnings("unchecked")
	public EwList<String> keySetUnsorted()
	{
		EwList<String> results = new EwList<String>();
		Iterator<String> i = orderedKeys.iterator();

		while (i.hasNext())
			results.add(i.next());

		return results;
	}

	@Override
	public EwJsonObject merge(EwJsonCollection value) throws JSONException
	{
		return EwJsonObject.merge(this, value, null);
	}

	@Override
	public EwJsonObject merge(Object value) throws JSONException
	{
		return (EwJsonObject) EwJsonObject.tryMergeAny(this, value, null);
	}

	/**
	 * Overridden to ensure complex keys are dereferenced by all getters. This
	 * method is what enables all gets and opts to parse complex keys.
	 */
	@Override
	public Object opt(String key)
	{
		return opt(key, null);
	}

	/**
	 * Referenced by overridden {@link #opt(String)}, which is called throughout
	 * parent code.
	 */
	@Override
	public Object opt(Object ref)
	{
		return this.opt(ref, null);
	}

	/** If "ref" is not a String, the default value is returned regardless. */
	@Override
	public Object opt(Object ref, Object defaultValue)
	{
		if (ref instanceof String)
		{
			// Try it the way the parent would do it first
			final Object object = super.opt((String) ref);

			// If that fails, try parsing ref as an array key
			if (object == null)
				try
				{
					if (EwJson.isComplexKey(ref))
						return EwJson.derefComplexKey(this, (String) ref);
				}
				catch (JSONException e)
				{
				}

			return object;
		}

		return defaultValue;
	}

	/**
	 * @return the boolean value corresponding to "ref", or false if key does not
	 *         exist or is not a String
	 */
	@Override
	public boolean optBoolean(Object ref)
	{
		return super.optBoolean((String) ref, EwJson.DEFAULT_BOOLEAN);
	}

	@Override
	public boolean optBoolean(Object ref, boolean defaultValue)
	{
		if (ref instanceof String)
			return super.optBoolean((String) ref, defaultValue);

		return defaultValue;
	}

	/**
	 * @return the value corresponding to "ref", or {@link Double#NaN} if key
	 *         does not exist or is not a String
	 */
	@Override
	public double optDouble(Object ref)
	{
		return super.optDouble((String) ref, EwJson.DEFAULT_DOUBLE);
	}

	@Override
	public double optDouble(Object ref, double defaultValue)
	{
		if (ref instanceof String)
			return super.optDouble((String) ref, defaultValue);

		return defaultValue;
	}

	/**
	 * @return the value corresponding to "ref", or zero if key does not exist or
	 *         is not a String
	 */
	@Override
	public int optInt(Object ref)
	{
		return super.optInt((String) ref, EwJson.DEFAULT_INT);
	}

	@Override
	public int optInt(Object ref, int defaultValue)
	{
		if (ref instanceof String)
			return super.optInt((String) ref, defaultValue);

		return defaultValue;
	}

	/**
	 * Overridden to convert Strings or {@link JSONArray}s to
	 * {@link EwJsonArray}s
	 */
	@Override
	public EwJsonArray optJSONArray(String key)
	{
		return this.optJSONArray((Object) key);
	}

	/**
	 * @return the EwJsonArray corresponding to "ref", or null if key does not
	 *         exist or is not a String
	 */
	@Override
	public EwJsonArray optJSONArray(Object ref)
	{
		return this.optJSONArray(ref, (JSONArray) EwJson.DEFAULT_VALUE);
	}

	@Override
	public EwJsonArray optJSONArray(Object ref, JSONArray defaultValue)
	{
		try
		{
			return EwJsonArray.getJSONArray(this, ref);
		}
		catch (JSONException je)
		{
		}

		return EwJsonArray.convert(defaultValue);
	}

	/**
	 * Overridden to convert Strings or {@link JSONObject}s to
	 * {@link EwJsonObject}s
	 */
	@Override
	public EwJsonObject optJSONObject(String key)
	{
		return this.optJSONObject((Object) key);
	}

	/**
	 * @return the EwJsonObject corresponding to "ref", or null if key does not
	 *         exist or is not a String
	 */
	@Override
	public EwJsonObject optJSONObject(Object ref)
	{
		return this.optJSONObject(ref, (JSONObject) EwJson.DEFAULT_VALUE);
	}

	@Override
	public EwJsonObject optJSONObject(Object ref, JSONObject defaultValue)
	{
		try
		{
			return EwJsonObject.getJSONObject(this, ref);
		}
		catch (JSONException je)
		{
		}

		return EwJsonObject.convert(defaultValue);
	}

	@Override
	public EwJsonCollection optJSONCollection(Object ref)
	{
		return this.optJSONCollection(ref, (EwJsonCollection) EwJson.DEFAULT_VALUE);
	}

	@Override
	public EwJsonCollection optJSONCollection(Object ref, EwJsonCollection defaultValue)
	{
		return EwJson.tryConvert(this.opt(ref));
	}

	/**
	 * @return the value corresponding to "ref", or zero if key does not exist or
	 *         is not a String
	 */
	@Override
	public long optLong(Object ref)
	{
		return super.optLong((String) ref, EwJson.DEFAULT_LONG);
	}

	@Override
	public long optLong(Object ref, long defaultValue)
	{
		if (ref instanceof String)
			return super.optLong((String) ref, defaultValue);

		return defaultValue;
	}

	/**
	 * @return the value corresponding to "ref", or the empty string if key does
	 *         not exist or is not a String
	 */
	@Override
	public String optString(Object ref)
	{
		return super.optString((String) ref, EwJson.DEFAULT_STRING);
	}

	@Override
	public String optString(Object ref, String defaultValue)
	{
		if (ref instanceof String)
			return super.optString((String) ref, defaultValue);

		return defaultValue;
	}

	@Override
	public EwJsonObject put(Object ref, boolean value) throws JSONException
	{
		return this.put(ref, new Boolean(value));
	}

	/**
	 * Overridden to avoid conversion of {@link Collection}s to
	 * {@link JSONArray}s
	 */
	@Override
	public EwJsonObject put(String key, Collection value) throws JSONException
	{
		return this.put((Object) key, (Object) value);
	}

	/**
	 * Inserts a {@link JSONArray} created from the {@link Collection} at the
	 * specified key.
	 */
	@Override
	public EwJsonObject put(Object ref, Collection value) throws JSONException
	{
		return this.put(ref, (Object) value);
	}

	@Override
	public EwJsonObject put(Object ref, double value) throws JSONException
	{
		return this.put(ref, new Double(value));
	}

	@Override
	public EwJsonObject put(Object ref, int value) throws JSONException
	{
		return this.put(ref, new Integer(value));
	}

	@Override
	public EwJsonObject put(Object ref, long value) throws JSONException
	{
		return this.put(ref, new Long(value));
	}

	/** Overridden to avoid conversion of {@link Map}s to {@link JSONObject}s */
	@Override
	public EwJsonObject put(String key, Map value) throws JSONException
	{
		return this.put((Object) key, (Object) value);
	}

	@Override
	public EwJsonObject put(Object ref, Map value) throws JSONException
	{
		return this.put(ref, (Object) value);
	}

	/**
	 * Overridden to first try parsing value as an EwJsonCollection. All puts
	 * executed in the parent call this method. Null values result in the
	 * removal of any existing value at key.
	 */
	@Override
	public synchronized EwJsonObject put(String key, Object value) throws JSONException
	{
		Object wrapped = EwJson.wrap(value);

		orderedKeys.add(key);

		if (EwJson.isNull(wrapped))
			super.put(key, (Object) null);
		else
			super.put(key, wrapped);

		return this;
	}

	/** Keys must be non-null strings or a {@link JSONException} is thrown. */
	@Override
	public synchronized EwJsonObject put(Object ref, Object value) throws JSONException
	{
		if (ref instanceof String)
			return this.put((String) ref, value);

		throw buildGetterException(ref, "is null or is not a string");
	}

	/** Overridden to first wrap the value for consistency. */
	@Override
	public EwJsonObject putOnce(String key, Object value) throws JSONException
	{
		return this.put((Object) key, value);
	}

	@Override
	public EwJsonObject putOnce(Object ref, Object value) throws JSONException
	{
		final Object existing = this.opt(ref);

		if (!EwJson.isNull(existing))
			throw new JSONException("Duplicate key \"" + (String) ref + "\"");

		return this.put(ref, value);
	}

	@Override
	public EwJsonObject putOpt(Object ref, Object value)
	{
		try
		{
			this.put(ref, value);
		}
		catch (JSONException e)
		{
		}

		return this;
	}

	@Override
	public Object reduce()
	{
		return EwJson.reduce(this);
	}

	@Override
	public String urlDecode(Object ref)
	{
		return EwUri.decodeValue(optString(ref));
	}

	@Override
	public String urlEncode(Object ref)
	{
		return EwUri.encodeValue(optString(ref));
	}

	/* OVERRIDDEN (Object) */

	/**
	 * Compares this with object, after it has been wrapped by
	 * {@link EwJson#wrap(Object)}.
	 */
	@Override
	public boolean equals(Object object)
	{
		if (super.equals(object))
			return true;

		final Object wrapped = EwJson.wrap(object);

		if (this == wrapped)
			return true;

		if (EwJson.isNull(wrapped))
			return false;

		if (wrapped instanceof EwJsonCollection)
			return EwJson.equals(this, (EwJsonCollection) wrapped);

		return false;
	}

	/* HELPERS */

	private static JSONException buildGetterException(Object ref, String issue)
	{
		return buildGetterException(ref, issue, null);
	}

	private static JSONException buildGetterException(Object ref, String issue, String description)
	{
		if (ref == null)
			ref = "null";

		if (issue == null)
			issue = "null";

		if (description == null)
			description = "";

		StringBuilder message = new StringBuilder(32 + issue.length() + description.length());

		message.append("EwJsonObject.").append(ref).append(' ').append(issue);

		if (!description.isEmpty())
			message.append(": ").append(description);

		return new JSONException(message.append('.').toString());
	}

	/**
	 * Joins next key and value, and returns the key -- does not check nulls or
	 * if keys has next
	 */
	private static String joinNextKeyAndValue(EwJsonObject json, Iterator keys, StringBuilder joined) throws JSONException
	{
		final String key = keys.next().toString();

		joined.append('"').append(key).append("\":");
		joined.append(EwJson.valueToString(json.opt(key)));

		return key;
	}

	@Override
	public Iterator keys()
	{
		return ((Collection) orderedKeys.clone()).iterator();
	}

	@Override
	public Object remove(String arg0)
	{
		orderedKeys.remove(arg0);
		return super.remove(arg0);
	}

	@Override
	public Set<String> keySet()
	{
		Set<String> r = new LinkedHashSet<String>();
		Iterator<String> sortedKeys = orderedKeys.iterator();
		while (sortedKeys.hasNext())
			r.add(sortedKeys.next());
		return r;
	}

}

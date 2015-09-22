package com.eduworks.lang.json.impl;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.eduworks.lang.json.EwJsonCollection;
import com.eduworks.lang.util.EwJson;
import com.eduworks.lang.util.EwUri;

/**
 * A class to extend {@link JSONArray} and implement {@link EwJsonCollection}.
 * Provides the following features and functionality:
 * <ul>
 * <li>enhances all JSONArray behavior even when JSONArray methods are called</li>
 * <li>processes complex array keys "key[0][1]" against nested JSON objects</li>
 * <li>wraps incoming/outgoing {@link JSONArray}s as EwJsonArrays</li>
 * <li>wraps incoming/outgoing {@link JSONObject}s as {@link EwJsonObject}s</li>
 * </ul>
 * <p>
 * This list can and should grow as Eduworks applications have need of new JSON
 * array functionality.
 * </p>
 * @author dharvey
 * @since September, 2011
 */
@SuppressWarnings("rawtypes")
public class EwJsonArray extends JSONArray implements EwJsonCollection
{
	/* STATIC METHODS */

	public static EwJsonArray convert(JSONArray array)
	{
		if (array == null) return null;

    	if (array instanceof EwJsonArray)
			return (EwJsonArray) array;

    	try {
			return new EwJsonArray(array);
		} catch (JSONException je) {
		}

		return null;
	}

	/**
	 * Convert String or {@link JSONArray} to EwJsonArray, and put and return it if it is valid.
	 * @throws JSONException if key is not found or value cannot be converted to {@link JSONArray}
	 */
	public static EwJsonArray getJSONArray(EwJsonCollection json, Object ref) throws JSONException
	{
		final Object object = json.get(ref);

		if (object instanceof EwJsonArray)
			return (EwJsonArray) object;

		else if (object instanceof JSONArray)
            return convert((JSONArray)object);

		else if (object instanceof Collection)
			return new EwJsonArray((Collection)object);

		// Convert String to EwJsonArray and put it before returning
		else if (object instanceof String)
        {
        	final EwJsonArray jsonArray =
        		convert(EwJson.getJsonArray((String)object));

        	if (jsonArray != null)
        	{
        		json.put(ref, jsonArray);
        		return jsonArray;
        	}
        }

        throw buildGetterException(ref, "is not a JSONArray. Is " + object.getClass().getName());
	}

	/**
	 * Merges values from an EwJsonCollection starting at the key/index specified by ref if not null.
	 * If ref is null or non-numeric, all possible values in "from" are appended to the end of the array.
	 */
 	public static EwJsonArray merge(EwJsonArray into, EwJsonCollection from, Object ref) throws JSONException
	{
		if (into == null && from == null)
			return null;

		else if (into == null)
			return (from instanceof EwJsonArray) ? (EwJsonArray) from : null;

		else if (from == null)
			return into;

		final int index = EwJson.keyToIndex(ref);
		final int putDex = (index >= 0) ? index : into.length();

		if (from instanceof JSONArray)
			EwJson.merge(into, (JSONArray)from, 0, putDex, from.length());

		else if (!EwJson.isJson(from) || EwJson.getElements(from) > 0)
			into.put(putDex, from);

		return into;
	}

	/**
	 * @see #mergeFromString(JSONArray, String, Integer)
	 * @return a new {@link EwJsonArray} parsed from source string
	 */
	public static EwJsonArray parse(String source) throws JSONException
	{
		return mergeFromString(null, source, null);
	}

	/**
	 * Parse contents of json array String, and merge them with the array starting at index.
	 * If array is null, an {@link EwJsonArray} is instantiated from source and returned. If
	 * source is null the array is converted to an {@link EwJsonArray} and returned. If index
	 * is null or negative, the new array elements are appended to the end of the one passed in.
	 */
	public static EwJsonArray mergeFromString(JSONArray array, String source, Integer index)
		throws JSONException
	{
		final EwJsonArray ewArray = (array == null)
			? new EwJsonArray()	// Parse a new array from source
			: convert(array);	// Merge into provided array

		if (EwJson.isNull(source)) return ewArray;

		final JSONTokener tokener = new JSONTokener(source);

		if (tokener.nextClean() != '[')
			throw tokener.syntaxError("EwJsonArray text must start with '['");

		int mergeIndex = (index == null || index.intValue() < 0)
			? ewArray.length()
			: index.intValue();

		/* Arguments processed; begin parsing */

		parsing:
		if (tokener.nextClean() != ']')
		{
			tokener.back();

			while (true)
			{
				// Must execute nextClean() before back()
				if (tokener.nextClean() == ',')
				{
					tokener.back();
					ewArray.put(mergeIndex++, (Object) null);
				}
				else
				{
					tokener.back();
					ewArray.put(mergeIndex++, tokener.nextValue());
				}

				switch (tokener.nextClean())
				{
					case ']':
						break parsing;

					case ';':
					case ',':
						if (tokener.nextClean() == ']')
							break parsing;
						else
							tokener.back();
						break;

					default:
						throw tokener.syntaxError("Expected a ',' or ']'");
				}
			}
		}

		if (tokener.more()) throw tokener.syntaxError("More unknown characters.");
		return ewArray;
	}

    /**
     * If "from" is parsable as JSON, merge it with "into"; otherwise if ref and from
     * are not null, put "from". Finally, return "into" as an EwJsonArray.
     * @see EwJson#tryParseJson(Object, boolean)
     * @see #merge(EwJsonArray, EwJsonCollection, Object)
     */
    public static EwJsonCollection tryMergeAny(JSONArray into, Object from, Object ref) throws JSONException
    {
    	if (into == null) return null;

    	final EwJsonArray converted = convert(into);

    	if (!EwJson.isNull(from))
    	{
    		final Object wrapped = EwJson.wrap(from, true);

    		if (wrapped instanceof EwJsonCollection)
    			return EwJsonArray.merge(converted, (EwJsonCollection) wrapped, ref);

    		else if (EwJson.isValidIndex(ref))
    			return converted.putOpt(ref, wrapped);

    		else if (wrapped != null)
    			converted.put(wrapped);
    	}

    	return converted;
    }


    /* CONSTRUCTORS */

	public EwJsonArray()
	{
		super();
	}

	/** Initializes an array of the specified size containing null values */
	public EwJsonArray(int size)
	{
		this();

        while (size > length())
            super.put((Object) null);
	}

	public EwJsonArray(String source) throws JSONException
	{
		this();

		EwJsonArray.mergeFromString(this, source, new Integer(0));
	}

	public EwJsonArray(Collection collection)
	{
		this();

		if (collection != null)
		{
			final Iterator iter = collection.iterator();
			while (iter.hasNext())
				this.put(EwJson.wrap(iter.next()));
		}
	}

	/**
	 * Initializes an array with the collections' values if it is a {@link EwJsonArray}
	 * or with the {@link EwJsonCollection} as the first element in the array.
	 */
	public EwJsonArray(EwJsonCollection collection) throws JSONException
	{
		this();

		EwJsonArray.merge(this, collection, new Integer(0));
	}

	/**
	 * If source is JSONArray or array, merge; if JSONObject, convert to EwJsonObject and put;
	 * if String, try to parse and put; otherwise insert source as first element in array.
	 */
	public EwJsonArray(Object source) throws JSONException
	{
		this();

		if (EwJson.isNull(source)) return;

		else if (source.getClass().isArray())
		{
			final int length = Array.getLength(source);

			for (int i = 0; i < length; i++)
				this.put(EwJson.wrap(Array.get(source, i)));
		}
        else if (source instanceof JSONArray)
        {
        	EwJson.merge(this, (JSONArray)source);
        }
		else if (source instanceof String)
		{
			EwJsonArray.mergeFromString(this, (String)source, new Integer(0));
		}
		else
		{
			EwJsonArray.tryMergeAny(this, source, null);
		}
	}


	/* OVERRIDDEN (JSONArray) */

	@Override
	public EwJsonCollection accumulate(Object ref, Object value) throws JSONException
	{
		if (!EwJson.isValidIndex(ref))
			throw buildGetterException(ref, "not found");

		return EwJson.accumulate(this, ref, value);
	}

	@Override
	public boolean contains(Object element)
	{
		return EwJson.contains((JSONArray)this, element);
	}

	/** Calls {@link #opt(Object)} to parse any array keys. */
	@Override
	public Object get(Object ref) throws JSONException
	{
		Object object;

		try {
			int keyToIndex = EwJson.keyToIndex(ref);
			if (keyToIndex == -1) 
				return null;
			return super.get(keyToIndex);
		} catch (JSONException je) {
			if ((object = this.opt(ref)) == null) throw je;
		}

		return object;
	}

	@Override
	public boolean getBoolean(Object ref) throws JSONException
	{
		try {
			return EwJson.parseBoolean(this.get(ref));
		} catch (JSONException je) {
			throw buildGetterException(ref, "is not a boolean");
		}
	}

	@Override
	public double getDouble(Object ref) throws JSONException
	{
		try {
			return EwJson.parseDouble(this.get(ref));
		} catch (JSONException je) {
			throw buildGetterException(ref, "is not a double");
		}
	}

	@Override
	public int getInt(Object ref) throws JSONException
	{
		try {
			return EwJson.parseInt(this.get(ref));
		} catch (JSONException je) {
			throw buildGetterException(ref, "is not an integer");
		}
	}

	/** Overridden to convert Strings or {@link JSONArray}s to {@link EwJsonArray}s */
	@Override
	public EwJsonArray getJSONArray(int index) throws JSONException
	{
		return EwJsonArray.getJSONArray(this, new Integer(index));
	}

	@Override
	public EwJsonArray getJSONArray(Object ref) throws JSONException
	{
		return EwJsonArray.getJSONArray(this, ref);
	}

	/** Overridden to convert Strings or {@link JSONObject}s to {@link EwJsonObject}s */
	@Override
	public EwJsonObject getJSONObject(int index) throws JSONException
	{
		return this.getJSONObject(new Integer(index));
	}

	@Override
	public EwJsonObject getJSONObject(Object ref) throws JSONException
	{
		try {
			return EwJsonObject.getJSONObject(this, ref);
		} catch (JSONException je) {
			throw buildGetterException(ref, "is not a JSONObject", je.getMessage());
		}
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
		try {
			return EwJson.parseLong(this.get(ref));
		} catch (JSONException je) {
			throw buildGetterException(ref, "is not a long");
		}
	}

	@Override
	public String getString(Object ref) throws JSONException
	{
		try {
			return EwJson.parseString(this.get(ref));
		} catch (JSONException je) {
			throw buildGetterException(ref, "is not a string");
		}
	}

	@Override
	public EwJsonArray emptyInstance()
	{
		return new EwJsonArray();
	}

	@Override
	public boolean hasComplex(Object ref)
	{
		if (hasSimple(ref)) return true;

		return !isNull(ref);
	}

	@Override
	public boolean hasSimple(Object ref)
	{
		if (EwJson.isValidIndex(ref))
			return !isNull(ref);

		return false;
	}

	@Override
	public boolean isEmpty()
	{
		return (super.length() < 1);
	}

	/** Overridden for a more thorough null check. */
	@Override
	public boolean isNull(int index)
	{
		return super.isNull(index) || EwJson.isNull(this.opt(index));
	}

	@Override
	public boolean isNull(Object ref)
	{
		return (ref == null || EwJson.isNull(this.opt(ref)));
	}

	/** Append everything from the incoming collection to this one. */
	@Override
	public EwJsonArray merge(EwJsonCollection value) throws JSONException
	{
		return EwJsonArray.merge(this, value, null);
	}

	/**
	 * Parse string value as json and append values.
	 * @see EwJson#wrap(Object)
	 */
	@Override
	public EwJsonArray merge(Object value) throws JSONException
	{
		return (EwJsonArray) EwJsonArray.tryMergeAny(this, value, null);
	}

	/**
	 * Overridden to ensure complex keys are dereferenced by all getters.
	 * This method is what enables all gets and opts to parse array keys.
	 */
	@Override
	public Object opt(int index)
	{
		return this.opt(new Integer(index), null);
	}

	/** Referenced by overridden {@link #opt(int)}, which is called throughout parent code. */
	@Override
	public Object opt(Object ref)
	{
		return this.opt(ref, null);
	}

	@Override
	public Object opt(Object ref, Object defaultValue)
	{
		// Try it the way the parent would do it first
		final Object object = super.opt(EwJson.keyToIndex(ref));

		// If that fails, try parsing ref as a composite key
		if (object == null && ref instanceof String)
			try {
				if (EwJson.isComplexKey(ref))
					return EwJson.derefComplexKey(this, (String)ref);
			} catch (JSONException e) {
			}

		return (object == null) ? defaultValue : object;
	}

	/** @return the value corresponding to "ref", or false if key/index does not exist */
	@Override
	public boolean optBoolean(Object ref)
	{
		return this.optBoolean(ref, EwJson.DEFAULT_BOOLEAN);
	}

	@Override
	public boolean optBoolean(Object ref, boolean defaultValue)
	{
		try {
			return this.getBoolean(ref);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	/** @return the value corresponding to "ref", or {@link Double#NaN} if key/index does not exist */
	@Override
	public double optDouble(Object ref)
	{
		return this.optDouble(ref, EwJson.DEFAULT_DOUBLE);
	}

	@Override
	public double optDouble(Object ref, double defaultValue)
	{
		try {
			return this.getDouble(ref);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	/** @return the value corresponding to "ref", or false if key/index does not exist */
	@Override
	public int optInt(Object ref)
	{
		return this.optInt(ref, EwJson.DEFAULT_INT);
	}

	@Override
	public int optInt(Object ref, int defaultValue)
	{
		try {
			return this.getInt(ref);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	/** Overridden to convert Strings or {@link JSONArray}s to {@link EwJsonArray}s */
	@Override
	public EwJsonArray optJSONArray(int index)
	{
		return this.optJSONArray(new Integer(index));
	}

	/** @return the EwJsonArray corresponding to "ref", or null if key is not valid */
	@Override
	public EwJsonArray optJSONArray(Object ref)
	{
		return this.optJSONArray(ref, (JSONArray)EwJson.DEFAULT_VALUE);
	}

	@Override
	public EwJsonArray optJSONArray(Object ref, JSONArray defaultValue)
	{
		try {
			return EwJsonArray.getJSONArray(this, ref);
		} catch (JSONException je) {
		}

		return convert(defaultValue);
	}

	/** Overridden to convert Strings or {@link JSONObject}s to {@link EwJsonObject}s */
	@Override
	public EwJsonObject optJSONObject(int index)
	{
		return this.optJSONObject(new Integer(index));
	}

	/** @return the EwJsonObject corresponding to "ref", or null if key is not valid */
	@Override
	public EwJsonObject optJSONObject(Object ref)
	{
		return this.optJSONObject(ref, (JSONObject)EwJson.DEFAULT_VALUE);
	}

	@Override
	public EwJsonObject optJSONObject(Object ref, JSONObject defaultValue)
	{
		try {
			return EwJsonObject.getJSONObject(this, ref);
		} catch (JSONException je) {
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

	@Override
	public long optLong(Object ref)
	{
		return this.optLong(ref, EwJson.DEFAULT_LONG);
	}

	@Override
	public long optLong(Object ref, long defaultValue)
	{
		try {
			return this.getLong(ref);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	@Override
	public String optString(Object ref)
	{
		return this.optString(ref, EwJson.DEFAULT_STRING);
	}

	@Override
	public String optString(Object ref, String defaultValue)
	{
		try {
			return this.getString(ref);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	@Override
	public EwJsonArray put(boolean value)
	{
		return this.put(new Boolean(value));
	}

	@Override
	public EwJsonArray put(Object ref, boolean value) throws JSONException
	{
		return this.put(ref, new Boolean(value));
	}

	/** Overridden to avoid conversion of {@link Collection}s to {@link JSONArray}s */
	@Override
	public EwJsonArray put(Collection value)
	{
		return this.put((Object)value);
	}

	@Override
	public EwJsonArray put(Object ref, Collection value) throws JSONException
	{
		return this.put(ref, (Object)value);
	}

	@Override
	public EwJsonArray put(double value)
	{
		return this.put(new Double(value));
	}

	@Override
	public EwJsonArray put(Object ref, double value) throws JSONException
	{
		return this.put(ref, new Double(value));
	}

	@Override
	public EwJsonArray put(int value)
	{
		return this.put(new Integer(value));
	}

	@Override
	public EwJsonArray put(Object ref, int value) throws JSONException
	{
		return this.put(ref, new Integer(value));
	}

	@Override
	public EwJsonArray put(long value)
	{
		return this.put(new Long(value));
	}

	@Override
	public EwJsonArray put(Object ref, long value) throws JSONException
	{
		return this.put(ref, new Long(value));
	}

	/** Overridden to avoid conversion of {@link Map}s to {@link JSONObject}s */
	@Override
	public EwJsonArray put(Map value)
	{
		return this.put((Object)value);
	}

	@Override
	public EwJsonArray put(Object ref, Map value) throws JSONException
	{
		return this.put(ref, (Object)value);
	}

	/**
	 * Insert a value at index (if ref can be converted to one) even if the array has
	 * to be extended (padded by null values).
	 * @param ref an object to be converted to an array index
	 * @param value the value to be inserted
	 * @see #put(int, Object)
	 * @return this object after insertion has been attempted
	 */
	@Override
	public EwJsonArray put(Object ref, Object value) throws JSONException
	{
		return this.put(EwJson.keyToIndex(ref), value);
	}

	/** Overridden to first wrap the value for consistency. */
	@Override
	public EwJsonArray put(Object value)
	{
		super.put(EwJson.wrap(value));

		return this;
	}

	/**
	 * Overridden to first try wrapping as a valid JSON value. All
	 * index-based puts in parent call this method. All index-based puts
	 * also pad the underlying {@link ArrayList} with null values when
	 * index is beyond the current range of the array.
	 */
	@Override
	public EwJsonArray put(int index, Object value) throws JSONException
	{
		Object wrapped = EwJson.wrap(value);

		if (EwJson.isNull(wrapped))
			super.put(index, (Object) null);
		else
			super.put(index, wrapped);

		return this;
	}

	/**
	 * Insert a value at index (if ref can be converted to one) even if the array has
	 * to be extended (padded by null values). If index is less than zero
	 * or value is null, nothing is done.
	 * @param ref an object to be converted to an array index
	 * @param value a non-null value to be inserted
	 * @see #put(int, Object)
	 * @return this object after insertion has been attempted
	 */
	@Override
	public EwJsonArray putOpt(Object ref, Object value)
	{
		return this.putOpt(EwJson.keyToIndex(ref), value);
	}

	/**
	 * Insert a value at index (if ref can be converted to one) even if the array has
	 * to be extended (padded by null values). If the index already has a
	 * value, a {@link JSONException} is thrown. If index is less than zero or value
	 * is null, nothing is done.
	 * @param ref an object to be converted to an array index
	 * @param value a non-null, non-duplicate value to be inserted
	 * @see #put(int, Object)
	 * @see #put(Object, Object)
	 * @return this object after insertion has been attempted
	 */
	@Override
	public EwJsonArray putOnce(Object ref, Object value) throws JSONException
	{
       return this.putOnce(EwJson.keyToIndex(ref), value);
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

	/** Compares this with object, after it has been wrapped by {@link EwJson#wrap(Object)}. */
	@Override
	public boolean equals(Object object)
	{
		if (super.equals(object)) return true;

		final Object wrapped = EwJson.wrap(object);

		if (this == wrapped) return true;

		if (EwJson.isNull(wrapped)) return false;

		if (wrapped instanceof EwJsonCollection)
			return EwJson.equals(this, (EwJsonCollection) wrapped);

		return false;
	}


	/* CUSTOM METHODS */

	public EwJsonArray putOpt(Object value)
	{
		return this.putOpt(length(), value);
	}

	public EwJsonArray putOpt(int index, Object value)
	{
		try {
			this.put(index, value);
		} catch (Exception e) {
		}

		return this;
	}

	public EwJsonArray putOnce(int index, Object value) throws JSONException
	{
		final Object existing = super.opt(index);

		if (!EwJson.isNull(existing))
			throw new JSONException("Duplicate index \"" + index + "\"");

		return this.put(index, value);
	}


	/* HELPERS */

	private static JSONException buildGetterException(Object ref, String issue)
	{
		return buildGetterException(ref, issue, null);
	}

	private static JSONException buildGetterException(Object ref, String issue, String description)
	{
		if (ref == null) ref = "null";

		if (issue == null) issue = "null";

		if (description == null) description = "";

		StringBuilder message = new StringBuilder(32 + issue.length() + description.length());

		message.append("EwJsonArray[").append(ref).append("] ").append(issue);

		if (!description.isEmpty())
			message.append(": ").append(description);

		return new JSONException(message.append('.').toString());
	}

	@Override
	public Set<String> keySet()
	{
		Set<String> r = new LinkedHashSet<String>();
		for (int i = 0;i < length();i++)
			try
			{
				r.add(this.getString(i));
			}
			catch (JSONException e)
			{
			}
		return r;
	}

	public Iterator keys()
	{
		return keySet().iterator();
	}
}

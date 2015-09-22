package com.eduworks.resolver;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;

import com.eduworks.lang.json.EwJsonCollection;
import com.eduworks.lang.json.impl.EwJsonArray;
import com.eduworks.lang.json.impl.EwJsonObject;
import com.eduworks.lang.util.EwJson;

/**
 * A class to retrieve non-setting keys from an element in this {@link Resolver},
 * and put them into either an {@link EwJsonObject} or {@link EwJsonArray}. Also
 * provides options for result {@link EwJsonCollection#reduce() reduction}, and
 * for to-json value conversion for the following types:
 * <ul>
 * <li>{@link Map Maps}</li>
 * <li>{@link List Lists}</li>
 * <li>{@link String Strings}</li>
 * </ul>
 * @author dharvey
 * @since 11/2011
 */
@Deprecated
public abstract class ResolverGetter extends Resolver
{
	protected Object getItem(Map<String, String[]> parameters, boolean convert, boolean reduce) throws JSONException
	{
		final Object item = (reduce)
			? EwJson.tryReduce(opt("item", parameters), false)
			: opt("item", parameters);

		return (convert) ? tryParseJson(item) : item;
	}

	/**
	 * Derives an EwJsonCollection from the value in this Resolver at the array key "array".
	 * @see #getCollection(Object, boolean)
	 */
	protected EwJsonArray getArray(Map<String, String[]> parameters, boolean convert, boolean reduce) throws JSONException
	{
		return getArray("array", parameters, convert, reduce);
	}

	/**
	 * Derives an EwJsonCollection from the value in this Resolver at the specified key.
	 * @see #getCollection(Object, boolean)
	 */
	protected EwJsonArray getArray(String key, Map<String, String[]> parameters, boolean convert, boolean reduce) throws JSONException
	{
		final EwJsonCollection collection = getCollection(get(key, parameters), convert);

		return (collection instanceof EwJsonArray)
			? (EwJsonArray) collection
			: EwJsonArray.merge(new EwJsonArray(), collection, null);
	}

	/**
	 * Attempts to convert the value at the default array key to an EwJsonArray. This includes
	 * parsing a string with json content.
	 */
	protected EwJsonArray getDefaultArray(Map<String, String[]> parameters) throws JSONException
	{
		final Object array = opt("array", parameters);
		final JSONArray wrapped = EwJson.wrapAsArray(array);
		final boolean notWrapped = (wrapped == null || wrapped.length() == 0);

		if (notWrapped && array != null)
			return (EwJsonArray) EwJson.wrapAsArray(getCollection(array, true));

		return (EwJsonArray) EwJson.tryConvert(wrapped);
	}

	/**
	 * Derives an EwJsonCollection from the value in this Resolver at the default object key "obj".
	 * @see #getCollection(Object, boolean)
	 */
	protected EwJsonObject getObject(Map<String, String[]> parameters, boolean convert, boolean reduce) throws JSONException
	{
		return getObject("obj", parameters, convert, reduce);
	}

	/**
	 * Derives an EwJsonCollection from the value in this Resolver at the specified key.
	 * @see #getCollection(Object, boolean)
	 */
	protected EwJsonObject getObject(String key, Map<String, String[]> parameters, boolean convert, boolean reduce) throws JSONException
	{
		final EwJsonCollection collection = getCollection(get(key, parameters), convert);

		return (collection instanceof EwJsonObject)
			? (EwJsonObject) collection
			: EwJsonObject.merge(new EwJsonObject(), collection, null);
	}

	/**
	 * Gets an EwJsonCollection from this Resolver. Lists are converted to EwJsonArrays,
	 * and empty arrays are reduced.
	 * @see EwJson#reduce(JSONArray)
	 */
	@SuppressWarnings("unchecked")
	private <T extends EwJsonCollection> T getCollection(Object gotten, boolean convert)
			throws JSONException
	{
		Object obj = tryParseJson(gotten);

		if (convert && obj instanceof List)
			obj = new EwJsonArray((List<?>)obj);

		else if (convert && obj instanceof Map<?,?>)
			obj = new EwJsonObject((Map<?,?>)obj);

		else if (convert && obj instanceof String)
			obj = tryParseJson(obj);

		return (T) EwJson.tryConvert(obj);
	}

	/**
	 * Iterates over non-setting, numeric keys "[0]", and inserts any non-null values into an EwJsonArray.
	 * @see EwJsonCollection#reduce()
	 */
	protected Object getInArray(EwJsonCollection source, Double scale, boolean reduce) throws JSONException
	{
		return (source == null) ? source : getIn(new EwJsonArray(), source, scale, reduce);
	}

	/**
	 * Iterates over non-setting keys, and inserts any non-null object values into an EwJsonObject.
	 * @see EwJsonCollection#reduce()
	 */
	protected Object getInObject(EwJsonCollection source, Double scale, boolean reduce) throws JSONException
	{
		return (source == null) ? source : getIn(new EwJsonObject(), source, scale, reduce);
	}

	private <T extends EwJsonCollection> Object getIn(T result, EwJsonCollection source, Double scale, boolean reduce)
			throws JSONException
	{
		final boolean asArray = (result instanceof EwJsonArray);

		for (String key : this.keySet())
			if (!skipKey(key))
			{
				final Object value = source.opt(key);

				if (value != null)
					if (asArray)
						result.put(result.length(), tryScaleNumber(value, scale));
					else
						result.put(key, tryScaleNumber(value, scale));
			}

		return (reduce) ? result.reduce() : result;
	}

	private boolean skipKey(String key)
	{
		return (
				key == null ||
				isSetting(key) ||
				key.equals("array") ||
				key.equals("obj")
			);
	}

	private Object tryScaleNumber(Object value, Number scale)
	{
		if (scale != null && value instanceof Number)
			return ((Number)value).doubleValue() * scale.doubleValue();

		return value;
	}
}

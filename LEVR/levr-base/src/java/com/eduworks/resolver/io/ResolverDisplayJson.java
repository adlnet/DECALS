package com.eduworks.resolver.io;

import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.interfaces.EwDisplayable;
import com.eduworks.lang.json.EwJsonCollection;
import com.eduworks.lang.util.EwJson;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Resolver;

public class ResolverDisplayJson extends Resolver
{
	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		resolveAllChildren(c, parameters, dataStreams);

		final boolean collapse = optAsBoolean("collapse", false, parameters);
		final boolean asArray = optAsBoolean("asArray", false, parameters);
		final Integer spaces = optAsInteger("spaces", 5, parameters);

		for (String key : keySet())
		{
			if (isSetting(key) || !has(key))
			{
				remove(key);
				continue;
			}

			Object value = get(key, parameters);
			put(key, value);
		}

		dismantleInfiniteLoops(this);

		Object result; // Clones this from string value
		if (collapse && length() == 1)
			result = get(keySet().iterator().next());
		else
		{
			if (spaces > 0)
				result = toString(spaces);
			else
				result = toString();
		}

		final boolean isJsonArray = (result instanceof JSONArray);
		final boolean isJsonObject = (result instanceof JSONObject);

		if (asArray && isJsonObject)
		{
			if (spaces > 0)
				return EwJson.copyFromObject((JSONObject) result).toString(spaces);
			else
				return EwJson.copyFromObject((JSONObject) result).toString();
		}
		else if (isJsonObject)
		{
			if (spaces > 0)
				result = ((JSONObject) result).toString(spaces);
			else
				result = ((JSONObject) result).toString();
		}
		else if (isJsonArray)
		{
			if (spaces > 0)
				result = ((JSONArray) result).toString(spaces);
			else
				result = ((JSONArray) result).toString();
		}

		return result;
	}

	public static Object dismantleInfiniteLoops(Object o) throws JSONException
	{
		if (o instanceof JSONObject)
		{
			JSONObject jo = (JSONObject) o;

			for (String key : EwJson.getKeys(jo))
			{
				Object object = jo.get(key);
				Object repl = dismantleInfiniteLoops(object);
				if (repl != null)
					jo.put(key, repl);
			}
		}
		else if (o instanceof JSONArray)
		{
			JSONArray jo = (JSONArray) o;

			for (int i = 0; i < jo.length(); i++)
			{
				if (jo.isNull(i)) continue;
				Object object = jo.get(i);
				Object repl = dismantleInfiniteLoops(object);
				if (repl != null)
					jo.put(i, repl);
			}
		}
		else if (o instanceof EwDisplayable)
			return ((EwDisplayable) o).toDisplayString();
		else if (o instanceof Collection)
		{
			Iterator<?> iterator = ((Collection<?>) o).iterator();
			while (iterator.hasNext())
			{
				Object obj = iterator.next();
				Object repl = dismantleInfiniteLoops(obj);
				if (repl != null)
					throw new RuntimeException("Failed to dismantle infinite loops.");
			}
		}
		else if (o instanceof EwJsonCollection)
		{
			EwJsonCollection col = (EwJsonCollection) o;
			for (String key : col.keySet())
			{
				Object repl = dismantleInfiniteLoops(col.get(key));
				if (repl != null)
					col.put(key, repl);
			}
		}
		return null;
	}

	@Override
	public String getDescription()
	{
		return "Displays the resultant object, array, or any other renderable as JSON."
				+ "\nCollapse will have the resultant object not wrapped (if one object/array/etc)" + "\nAsArray will place the parameters into an array.";
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
		return jo("<any>", "Object|Array|String|Number|Boolean", "?collapse", "Boolean", "?asArray", "Boolean","?spaces","Integer");
	}
}

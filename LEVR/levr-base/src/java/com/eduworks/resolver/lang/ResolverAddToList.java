package com.eduworks.resolver.lang;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.lang.json.impl.EwJsonArray;
import com.eduworks.lang.util.EwJson;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.ResolverMatcher;
import com.eduworks.resolver.ResolverMerger;

public class ResolverAddToList extends ResolverMatcher implements ResolverMerger
{
	public static final String AT_FRONT_KEY = "atFront";

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		resolveAllChildren(c, parameters, dataStreams);

		// Set criteria for call to matches()
		setMatchCriteria(parameters, true);

		final boolean atFront = optAsBoolean(AT_FRONT_KEY, false, parameters);
		final boolean unique = optAsBoolean("unique", false, parameters);

		final Object item = getItem(parameters, false, false);
		final EwJsonArray array = getDefaultArray(parameters);

		remove(AT_FRONT_KEY);
		remove("unique");
		remove("array");
		remove("item");

		if (item != null)
		{
			// Item is not null: put single item at front or back
			if (!(unique && matches(array, item)))
				if (!atFront)
					array.put(item);
				else
					return EwJson.insert(array, 0, item);
		}
		else
		{
			// Merge/insert keyed values from numeric keys in RSL
			merge(array, null, null, parameters);

			if (unique)
			{
			    final EwJsonArray distinct = new EwJsonArray();
			    final EwJsonArray result = new EwJsonArray();

				for (int i = 0; i < array.length(); i++)
				{
					final Object next = array.get(i);

					if (!matches(distinct, next))
					{
						distinct.put(next);
						result.put(next);
					}
				}

				return result;
			}
		}

		return array;
	}

	@Override
	public Object merge(Object mergeTo, String srcKey, String destKey, Map<String,String[]> parameters)
			throws JSONException
	{
		final boolean merge = optAsBoolean("merge", true, parameters);

		remove("merge");

		for (String key : keySet())
			EwJsonArray.tryMergeAny((JSONArray)mergeTo, get(key, parameters), (merge) ? key : null);

		return mergeTo;
	}
	@Override
	public String getDescription()
	{
		return "Adds an item to a list, or creates a list if it does not exist.";
	}
	@Override
	public String[] getResolverNames()
	{
		return new String[]{getResolverName(),"listAdd"};
	}
	@Override
	public String getReturn()
	{
		return "JSONArray";
	}
	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}
	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("?array","JSONArray|List","item","Object");
	}
}

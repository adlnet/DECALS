
package com.eduworks.resolver.lang;
import java.io.InputStream;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.lang.json.EwJsonCollection;
import com.eduworks.lang.json.impl.EwJsonArray;
import com.eduworks.lang.util.EwJson;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.ResolverMatcher;
import com.eduworks.resolver.enumeration.ResolverMatchOption;

public class ResolverRemoveFromList extends ResolverMatcher
{
	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		resolveAllChildren(c, parameters, dataStreams);

		final EwJsonArray array = getDefaultArray(parameters);
		final Object item = getItem(parameters, true, false);

		final EwJsonCollection results;

		if (item != null)
		{
			setMatchCriteria(parameters, ResolverMatchOption.IN_ITEM);

			results = removeFrom(array, item);
		}
		else
		{
			results = new EwJsonArray();

			// Leave out any numeric keys specified in RSL
			for (int i = 0; i < array.length(); i++)
				if (!has(EwJson.indexToKey(i)))
					results.put(results.length(), array.get(i));
		}

		if (optAsBoolean("required", false, parameters) && results.length() == array.length())
			throw new RuntimeException("Failed to remove an item, and removal is required.");

		return results;
	}
	@Override
	public String getDescription()
	{
		return "Removes one or more objects from an array of objects if they match the criteria defined by this object.";
	}
	@Override
	public String[] getResolverNames()
	{
		return new String[]{getResolverName(),"listRemove"};
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
		return jo("array","JSONArray","?item","JSONObject","<any>","Object");
	}

}

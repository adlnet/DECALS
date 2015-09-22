package com.eduworks.resolver.lang;

import java.io.InputStream;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.lang.json.impl.EwJsonObject;
import com.eduworks.lang.util.EwJson;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Resolver;
import com.eduworks.resolver.ResolverMerger;

public class ResolverObject extends Resolver implements ResolverMerger
{
	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		resolveAllChildren(c, parameters, dataStreams);

		boolean optAsBoolean = optAsBoolean("_soft", false, parameters);
		removeAllSettings();

		final String dest = optAsString("dest", parameters);
		final String destKey = (dest == null || dest.isEmpty()) ? null : dest;

		final boolean merge = optAsBoolean("merge", false, parameters);

		remove("dest");
		remove("merge");

		final Set<String> keySet = keySet();

		int count = 0;
		for (String key : keySet)
		{
			final Object value = get(key, parameters);

			if (value == null)
				continue;

			else if (merge)
			{
				count++;
				merge(this, key, destKey, parameters);
			}

			else if (!value.equals(get(key)))
			{
				count++;
				put(key, value);
			}
		}

		// Unwrap (reduce) a single non-json value at dest
		if (destKey != null) 
			put(destKey, EwJson.tryReduce(get(destKey, parameters), false));

		if (optAsBoolean)
			if (count == 0)
				return null;
		
		return this;
	}

	@Override
	public Object merge(Object mergeTo, String srcKey, String destKey, Map<String, String[]> parameters) throws JSONException
	{
		final Object value = get(srcKey, parameters);

		remove(srcKey);

		// Determine if value can be merged as json
		final boolean isJsonValue = (value instanceof String)
			? EwJson.isJson((String) value)
			: EwJson.isJson(value);

		// Wrap non-json in an object for accumulation at dest
		final Object mergeValue = (!isJsonValue)
			? new EwJsonObject().put(srcKey, value)
			: tryParseJson(value);

		return EwJson.tryMerge(mergeTo, mergeValue, destKey);
	}

	@Override
	public String getDescription()
	{
		return "Creates a JSON Object, executes resolvers in alphabetical order.";
	}

	@Override
	public String getReturn()
	{
		return "JSONObject";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("<any>","Object");
	}

}

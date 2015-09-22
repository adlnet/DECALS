package com.eduworks.resolver.manip;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.lang.json.impl.EwJsonArray;
import com.eduworks.lang.util.EwJson;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Resolver;

public class ResolverFilterObjects extends Resolver
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		resolveAllChildren(c, parameters, dataStreams);

		final boolean requireAll = optAsBoolean("requireAll", false, parameters);
		JSONArray ja = optAsArray("array", parameters);
		if (ja == null || ja.length() == 0)
			ja = optAsArray("obj",parameters);
		if (ja == null)
			return null;

		final EwJsonArray results = new EwJsonArray();

		for (int i = 0; i < ja.length(); i++)
		{
			final Object element = tryParseJson(ja.get(i));
			if (element instanceof JSONObject)
			{
				final JSONObject jsonElement = (JSONObject) element;
				boolean allOk = true;
				for (String key : EwJson.getKeys(this))
				{
					if (isSetting(key))
						continue;
					if (key.equals("obj")) continue;
					if (jsonElement.has(key) && jsonElement.get(key).equals(this.get(key, parameters)))
					{
						if (!requireAll)
						{
							results.put(element);
							break;
						}
						//else
							//results.put(element);
					}
					else
					{
						allOk = false;
					}
				}
				if (requireAll && allOk)
					results.put(element);
			}
		}

		// COEUS was relying on some way to reduce.
		Object reduced = results;
		if (optAsBoolean("reduce", false, parameters))
			reduced = results.reduce();

		return (reduced == null) ? null : reduced;

		// return (results.length() == 0) ? null : results;
	}

	@Override
	public String getDescription()
	{
		return "Keep only objects in this array that have a parameter and value defined by parameters in this resolver.\n" +
				"(Optional) requireAll -- Require all the parameters, not just one.";
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
		return jo("obj","JSONArray","<any>","Object","?_requireAll","Boolean");
	}

}

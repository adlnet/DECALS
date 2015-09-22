package com.eduworks.resolver.io;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONML;
import org.json.JSONObject;

import com.eduworks.interfaces.EwJsonSerializable;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Resolver;

public class ResolverDisplayJsonML extends Resolver
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		resolveAllChildren(c, parameters, dataStreams);

		for (String key : keySet())
		{
			if (isSetting(key) || !has(key))
			{
				remove(key);
				continue;
			}
			Object value = get(key, parameters);
			if (value instanceof EwJsonSerializable)
				put(key, ((EwJsonSerializable) value).toJsonObject());
		}
		return "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" + JSONML.toString(this);
	}

	@Override
	public String getDescription()
	{
		return "Returns the resultant objects as JSONML (an amalgum of JSON and XML)";
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
		return jo("<any>","Object|Array|String|Number|Boolean");
	}

}

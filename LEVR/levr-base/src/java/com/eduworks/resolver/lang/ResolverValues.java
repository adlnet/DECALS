package com.eduworks.resolver.lang;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.lang.EwSet;
import com.eduworks.lang.json.impl.EwJsonArray;
import com.eduworks.lang.json.impl.EwJsonObject;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.ResolverGetter;

public class ResolverValues extends ResolverGetter
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		resolveAllChildren(c, parameters, dataStreams);
		EwJsonObject object = getObject(parameters, true, false);
		EwSet<String> matches = new EwSet<String>(getAsStrings("matches", parameters));
		EwJsonArray out = new EwJsonArray();
		for (String key : object.keySetUnsorted())
		{
			if (isSetting(key)) continue;
			if (matches.contains(object.get(key).toString()))
				out.put(key);
		}
		return out;
	}

	@Override
	public String getDescription() {
		return "Gets the values of a JSON Object as an array.";
	}

	@Override
	public String getReturn() {
		return "JSONArray";
	}

	@Override
	public String getAttribution() {
		return ATTRIB_NONE;
	}

	@Override
	public JSONObject getParameters() throws JSONException {
		return jo("obj","JSONObject");
	}

}

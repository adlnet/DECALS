package com.eduworks.cruncher.manip;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherRemove extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		JSONArray ja = getObjAsJsonArray(c, parameters, dataStreams);
		JSONArray result = new JSONArray();
		String asString = getAsString("item", c, parameters, dataStreams);
		String index = getAsString("index", c, parameters, dataStreams);
		if (asString!=null)
			for (int i = 0;i < ja.length();i++) {
				if (!ja.getString(i).equals(asString))
					result.put(ja.get(i));
			}
		else if (index!=null) {
			int nIndex = Integer.parseInt(index);
			for (int i = 0;i < ja.length();i++) {
				if (i!=nIndex)
					result.put(ja.get(i));
			}
		}
		return result;
	}

	@Override
	public String getDescription()
	{
		return "Removes all instances of 'item' from the array provided by obj.";
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
		return jo("obj","JSONArray","item","String");
	}

}

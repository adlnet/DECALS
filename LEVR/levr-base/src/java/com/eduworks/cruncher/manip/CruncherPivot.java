package com.eduworks.cruncher.manip;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

//Takes a JSON Object of Arrays, and creates a pivot table, where the elements of the array are the new keys of the JSON Objects, and the original keys are in the array.
public class CruncherPivot extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		JSONObject obj = getObjAsJsonObject(c, parameters, dataStreams);
		if (obj == null)
			return null;
		JSONObject result = new JSONObject();

		Iterator<String> keys = obj.keys();
		while (keys.hasNext())
		{
			String key = keys.next();
			JSONArray array = null;
			if (obj.get(key) instanceof JSONArray)
				array = obj.getJSONArray(key);
			else
				array = new JSONArray(new Object[] { obj.get(key) });
			for (int i = 0; i < array.length(); i++)
			{
				String element = array.get(i).toString();
				result.append(element, key);
			}
		}
		if (result.length() != 0)
			return result;
		return null;
	}

	@Override
	public String getDescription()
	{
		return "Pivots an JSONObject where all values are JSONArrays (or single items) such that the keys of the JSONObject become the array members, and the array members become the keys.";
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
		return jo("obj", "JSONObject");
	}

}

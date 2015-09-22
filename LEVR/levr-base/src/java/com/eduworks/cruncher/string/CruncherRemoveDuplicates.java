package com.eduworks.cruncher.string;

import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherRemoveDuplicates extends Cruncher
{
	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		JSONArray ja = getObjAsJsonArray(c,parameters, dataStreams);
		if (ja == null) return null;
		HashMap<String,Object> hs = new LinkedHashMap<String,Object>();
		
		for (int i = 0;i < ja.length();i++)
			hs.put(ja.get(i).toString(),ja.get(i));
//			for (int j = i+1;j < ja.length();j++)
//			{
//				if (ja.opt(i) == null)
//				    continue;
//			    if (ja.opt(j) == null)
//			    	continue;
//				if (ja.get(i).equals(ja.get(j)))
//				{
//					ja.put(j,(Object)null);
//				}
//			}
		return new JSONArray(hs.values());
//		JSONArray jaNew = new JSONArray();
//		for (int i = 0;i < ja.length();i++)
//		{
//			if (ja.opt(i) == null)
//			    continue;
//			Object test = ja.get(i);
//			if (test != null)
//				jaNew.put(test);
//		}
//		return jaNew;
	}

	@Override
	public String getDescription()
	{
		return "Removes duplicates (compared by toString())";
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
		return jo("obj","JSONArray");
	}
}

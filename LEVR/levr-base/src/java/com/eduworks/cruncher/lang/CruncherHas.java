package com.eduworks.cruncher.lang;

import java.io.InputStream;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.lang.EwList;
import com.eduworks.lang.EwSet;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherHas extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		Object obj = getObj(c, parameters, dataStreams);
		if (obj == null)
			return false;
		if (obj instanceof String)
		{
			String asString = getAsString("has", c, parameters, dataStreams);
			if (optAsBoolean("regex", false, c, parameters, dataStreams))
			{
				Pattern p = null;
				if (c.containsKey("regex_" + asString))
				{
					p = (Pattern) c.get("regex_" + asString);
				}
				else
				{
					p = Pattern.compile(asString);
					p.matcher("");
					c.put("regex_" + asString, p);
				}
				
				return p.matcher(obj.toString()).matches();
			}
			else
			{
				return obj.toString().indexOf(asString) != -1;
			}
		}
		if (obj instanceof JSONObject)
		{
			JSONObject obje = (JSONObject) obj;
			String has = getAsString("has", c, parameters, dataStreams);
			if (obje.has(has))
				return true;
			return false;
		}
		{
			JSONArray obje = (JSONArray) obj;
			if (has("hasArray"))
			{
				JSONObject jo = new JSONObject();
				Set<String> js = new EwSet(new EwList<String>(getAsJsonArray("hasArray", c, parameters, dataStreams)));
				for (int i = 0; i < obje.length(); i++)
				{
					String o = obje.get(i).toString();
					if (js.contains(o))
						jo.put(o, true);
					else
						jo.put(o, false);
				}
				return jo;
			}
			String has = getAsString("has", c, parameters, dataStreams);
			for (int i = 0; i < obje.length(); i++)
			{
				if (obje.get(i).equals(has))
					return true;
			}
			return false;
		}
	}

	@Override
	public String getDescription()
	{
		return "Determines if a JSONObject has a key, or a JSONArray has an item at a particular index designated by 'has'";
	}

	@Override
	public String getReturn()
	{
		return "Boolean";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("obj", "JSONObject|JSONArray", "has", "String|Number");
	}

}

package com.eduworks.cruncher.lang;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherDebug extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		Object obj = getObj(c, parameters, dataStreams);
		try
		{
			String optAsString = optAsString("prefix", "", c, parameters, dataStreams);
			if (obj == null)
				log.debug(optAsString+"NULL");
			else if (obj instanceof JSONArray)
				log.debug(optAsString+((JSONArray) obj).toString(5) + " ("+ obj.getClass().getSimpleName() +")");
			else if (obj instanceof JSONObject)
				log.debug(optAsString+((JSONObject) obj).toString(5) + " ("+ obj.getClass().getSimpleName() +")");
			else
				log.debug(optAsString+obj.toString() + " ("+obj.getClass().getSimpleName() +")");
		}
		catch (Throwable t)
		{

		}
		return obj;
	}

	@Override
	public String getDescription()
	{
		return "Prints out the object and then returns it. Inject to see things happening in the console log.";
	}

	@Override
	public String getReturn()
	{
		return "Object";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("obj", "Object");
	}

}

package com.eduworks.cruncher.lang;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.lang.EwMap;
import com.eduworks.lang.threading.EwThreading;
import com.eduworks.lang.threading.EwThreading.MyRunnable;
import com.eduworks.lang.util.EwCache;
import com.eduworks.lang.util.EwJson;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherCall extends Cruncher
{
	@Override
	public Object resolve(final Context c, Map<String, String[]> parameters, final Map<String, InputStream> dataStreams) throws JSONException
	{
		final EwMap<String, String[]> newParams = new EwMap<String, String[]>(parameters);
		boolean background = optAsBoolean("background",false,c,parameters,dataStreams);
		ArrayList<String> valuesToRemove = new ArrayList<String>();
		for (String key : keySet())
		{
			if (key.equals("obj"))
				continue;
			if (key.equals("background"))
				continue;
			if (isSetting(key))
				continue;
			Object value;
				value = get(key,c,parameters, dataStreams);
			if (value != null)
			{
				String valueString = value.toString();
				c.put(valueString, value);
				valuesToRemove.add(valueString);
				newParams.put(key, new String[] { valueString });
			}
		}
		JSONObject paramsObject = getAsJsonObject("_params", c, parameters, dataStreams);
		if (paramsObject != null)
		for (String key : EwJson.getKeys(paramsObject))
		{
			Object value;
			value = paramsObject.get(key);
			if (value != null)
			{
				String valueString = value.toString();
				c.put(valueString, value);
				valuesToRemove.add(valueString);
				newParams.put(key, new String[] { valueString });
			}
		}
		Object result=null;
		if (background)
			EwThreading.fork(new MyRunnable()
			{
				
				@Override
				public void run()
				{
					try
					{
						Object result = resolveAChild("obj", c,newParams, dataStreams);
					}
					catch (JSONException e)
					{
						e.printStackTrace();
					}
				}
			});
		else
		result=resolveAChild("obj", c,newParams, dataStreams);
		for (String s : valuesToRemove)
			c.remove(s);
		return result;
	}

	@Override
	public String getDescription()
	{
		return "Moves parameters of this function into the @ parameters";
	}

	@Override
	public String getReturn()
	{
		return "Object";
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("obj","Resolvable","<any>","Object","background","Boolean");
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}
}

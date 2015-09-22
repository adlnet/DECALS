package com.eduworks.cruncher.refl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.lang.EwMap;
import com.eduworks.lang.util.EwJson;
import com.eduworks.levr.servlet.impl.LevrResolverServlet;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;
import com.eduworks.util.io.InMemoryFile;

public class CruncherExecute extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		String ws = getAsString("service", c, parameters, dataStreams);
		boolean soft = optAsBoolean("soft",true,c,parameters, dataStreams);

		Map<String, String[]> parameterMap = new EwMap<String, String[]>(parameters);

		for (String key : keySet())
		{
			if (isSetting(key))
				continue;
			if (key.equals("service"))
				continue;
			if (key.equals("obj"))
				continue;
			if (key.equals("soft"))
				continue;
			if (key.equals("paramObj")){
				JSONObject paramObj = getAsJsonObject(key, c, parameters, dataStreams);
				
				for(String paramName : EwJson.getKeys(paramObj)){
					
					if(!parameterMap.containsKey(paramName)){
							parameterMap.put(paramName, new String[] { paramObj.get(paramName).toString() });
					}
				}
			}
			Object object = get(key, c, parameters, dataStreams);
			if (object!=null)
				parameterMap.put(key, new String[] { object.toString() });
		}

		Map<String, InputStream> dataStreamMap = new EwMap<String, InputStream>(dataStreams);
		if (has("obj"))
		{
			Object o = get("obj", c, parameters, dataStreams);
			try
			{
				accmDataStream(dataStreamMap, o);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			parameterMap.put("obj", new String[]{o.toString()});
		}

		try
		{
			return LevrResolverServlet.execute(log, true, ws, c, parameterMap,dataStreams, false);
		}
		catch (RuntimeException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			if (soft)
				log.error(e);
			else
				throw new RuntimeException(e);
			return null;
		}
	}

	private void accmDataStream(Map<String, InputStream> dataStreamMap, Object o) throws IOException, JSONException
	{
		if (o instanceof File)
			o = new InMemoryFile((File) o);
		if (o instanceof InMemoryFile)
		{
			InMemoryFile imf = (InMemoryFile) o;
			dataStreamMap.put(imf.name, imf.getInputStream());
		}
		else if (o instanceof JSONArray)
		{
			JSONArray ja = (JSONArray) o;
			for (int i = 0; i < ja.length(); i++)
				accmDataStream(dataStreamMap, ja.get(i));
		}
		else if (o instanceof JSONObject)
		{
			JSONObject jo = (JSONObject) o;
			for (String key : EwJson.getKeys(jo))
				accmDataStream(dataStreamMap, jo.get(key));
		}
	}

	@Override
	public String getDescription()
	{
		return "Executes a web service on the same machine, described by 'service', passes in url parameters and parameters defined, and datastreams passed in, and datastreams from obj. All Key-Values in paramObj are converted to @-params as well.";
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
		return jo("service","String","<any>","String","obj","File|InMemoryFile|JSONArray|JSONObject","paramObj","JSONObject");
	}

}

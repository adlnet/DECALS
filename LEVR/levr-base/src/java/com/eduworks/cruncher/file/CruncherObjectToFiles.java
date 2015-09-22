package com.eduworks.cruncher.file;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.lang.util.EwJson;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherObjectToFiles extends Cruncher
{
	Map<String, String>	documents	= new HashMap<String, String>();

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		JSONObject jo = getObjAsJsonObject(c, parameters, dataStreams);
		for (String key : EwJson.getKeys(jo))
		{
			documents.put(key,jo.get(key).toString());
		}
		return documents;
	}

	@Override
	public String getDescription()
	{
		return "Converts a JSON Object to a format that will be converted to HTML files (or other text files) when used with filesToZip";
	}

	@Override
	public String getReturn()
	{
		return "Map<String,String>";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_TRADEM1;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("obj","JSONObject");
	}

}

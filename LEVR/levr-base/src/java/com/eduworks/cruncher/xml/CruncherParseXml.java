package com.eduworks.cruncher.xml;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONML;
import org.json.JSONObject;
import org.json.XML;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherParseXml extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		String xml = getObj(c, parameters, dataStreams).toString();
		JSONObject jsonObject = JSONML.toJSONObject(xml);
		if (optAsString("simple","true", c, parameters, dataStreams).equals("true"))
			return XML.toJSONObject(xml);
		
		return jsonObject;
	}

	@Override
	public String getDescription()
	{
		return "Converts an XML object to a JSON object using one of two methods (determined by 'simple')";
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
		return jo("obj","String","?simple","Boolean");
	}

}

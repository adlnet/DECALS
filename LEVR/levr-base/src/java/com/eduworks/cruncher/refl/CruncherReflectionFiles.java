package com.eduworks.cruncher.refl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.levr.servlet.impl.LevrResolverServlet;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherReflectionFiles extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		JSONObject jo = new JSONObject();
		try
		{
			for (File f : LevrResolverServlet.codeFiles)
			{
				if (!f.getName().endsWith("rs2"))
					continue;
				jo.put(f.getName(), FileUtils.readFileToString(f));
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return jo;
	}

	@Override
	public String getDescription()
	{
		return "Retreives all the LEVR RS2 config files.";
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
		return jo();
	}

}

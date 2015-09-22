package com.eduworks.cruncher.string;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherReplace extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		String obj = getObj(c, parameters, dataStreams).toString();
		String replace = getAsString("replace", c, parameters, dataStreams);
		String with = getAsString("with",c,parameters, dataStreams);
		Boolean simple = optAsBoolean("simple", false, c, parameters, dataStreams);
		if (obj == null || replace == null) return obj;
		if (simple)
			return obj.replace(replace, with);
		return obj.replaceAll(replace, with);
	}

	@Override
	public String getDescription() {
		return "Replaces a substring (detected by regex) with another string." +
				"\n(Optional)simple - Don't use regex to detect the string.";
	}

	@Override
	public String getReturn() {
		return "String";
	}

	@Override
	public String getAttribution() {
		return ATTRIB_NONE;
	}

	@Override
	public JSONObject getParameters() throws JSONException {
		return jo("obj","String","replace","String","with","String","?simple","Boolean");
	}
}

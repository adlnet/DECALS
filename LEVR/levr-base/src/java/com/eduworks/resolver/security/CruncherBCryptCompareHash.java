package com.eduworks.resolver.security;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherBCryptCompareHash extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		String password = getAsString("password", c, parameters, dataStreams);
		String passwordHash = getAsString("passwordHash", c, parameters, dataStreams);
		
		return BCrypt.checkpw(password, passwordHash);
	}

	@Override
	public String getDescription()
	{
		return "Compares the password hash to a the supplied password and returns if they are the same.";
	}

	@Override
	public String getReturn()
	{
		return "Boolean";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_DECALS;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("password","string","passwordHash","string");
	}

}

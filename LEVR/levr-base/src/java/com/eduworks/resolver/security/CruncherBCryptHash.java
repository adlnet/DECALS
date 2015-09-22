package com.eduworks.resolver.security;

import java.io.InputStream;
import java.security.SecureRandom;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherBCryptHash extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		String password = getAsString("password", c, parameters, dataStreams);
		int rounds = Integer.parseInt(optAsString("rounds", "10", c, parameters, dataStreams));
		
		return BCrypt.hashpw(password, BCrypt.gensalt(rounds, new SecureRandom()));
	}

	@Override
	public String getDescription()
	{
		return "Generates password hash from supplied password given the number of supplied rounds, if nothing is supplied then the default 10 rounds.";
	}

	@Override
	public String getReturn()
	{
		return "String";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_DECALS;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("password","string","rounds","int");
	}
}

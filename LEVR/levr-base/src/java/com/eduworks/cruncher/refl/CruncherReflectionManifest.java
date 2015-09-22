package com.eduworks.cruncher.refl;

import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;
import com.eduworks.resolver.Resolvable;
import com.eduworks.resolver.Resolver;
import com.eduworks.resolver.ResolverFactory;

public class CruncherReflectionManifest extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		JSONObject results = new JSONObject();
		try
		{
			for (Entry<String, Class<? extends Cruncher>> cruncher : ResolverFactory.cruncherSpecs.entrySet())
				if (!cruncher.getValue().getName().startsWith("Command"))
					results.put(cruncher.getKey(), getSpecs(cruncher.getValue().newInstance()));
			for (Entry<String, Class<? extends Resolver>> cruncher : ResolverFactory.factorySpecs.entrySet())
				if (!cruncher.getValue().getName().startsWith("Command"))
					results.put(cruncher.getKey(), getSpecs(cruncher.getValue().newInstance()));
		}
		catch (InstantiationException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		return results;
	}

	private JSONObject getSpecs(Resolvable n) throws JSONException
	{
		JSONObject jo = new JSONObject();
		jo.put("description",n.getDescription());
		jo.put("variables",n.getParameters());
		jo.put("attribution",n.getAttribution());
		jo.put("namespace",n.getClass().getPackage().getName().replace("com.eduworks.", ""));
		jo.put("return",n.getReturn());
		return jo;
	}

	@Override
	public String getDescription()
	{
		return "Requests that LEVR self-describe all resolvers that are available.";
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

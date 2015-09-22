package com.eduworks.resolver.net;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.lang.EwList;
import com.eduworks.lang.threading.EwThreading;
import com.eduworks.lang.threading.EwThreading.MyRunnable;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Resolver;
import com.eduworks.util.io.EwFileSystem;

public class ResolverGetFileFromUrl extends Resolver
{
	public static File operate(Context c,String url, int timeout) throws IOException
	{
		String cacheUrl = ResolverGetFileFromUrl.class.getName() + ":" + url;
		Object cacheResult = getCache(c,cacheUrl);
		if (cacheResult != null)
		{
			return (File) cacheResult;
		}
		try
		{
			File f = EwFileSystem.downloadFile(url, timeout);
			putCache(c,cacheUrl, f);
			return f;
		}
		finally
		{
		}
	}

	@Override
	public Object resolve(final Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		resolveAllChildren(c, parameters, dataStreams);
		final int timeout = optAsInteger("timeout",30000, parameters);
		final EwList<Object> results = new EwList<Object>();
		EwThreading.foreach(getAsStrings("url", parameters), new MyRunnable()
		{
			@Override
			public void run()
			{
				try
				{
					File operate = operate(c,o.toString(),timeout);
					if (operate != null)
						results.add(operate);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		});
		return results;
	}

	@Override
	public String getDescription()
	{
		return "Retreives a file from a target url using an HTTP GET";
	}

	@Override
	public String getReturn()
	{
		return "File";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("url","String","?timeout","Number");
	}
}

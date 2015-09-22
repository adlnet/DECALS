package com.eduworks.cruncher.lang;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.lang.threading.EwThreading;
import com.eduworks.lang.threading.EwThreading.MyRunnable;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherAbortable extends Cruncher
{

	@Override
	public Object resolve(final Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		EwThreading.fork(new MyRunnable()
		{
			@Override
			public void run()
			{
				while (c.response != null)
				{
					EwThreading.sleep(1000);
					try
					{
						c.pw.print(' ');
						c.response.flushBuffer();
					}
					catch (Exception ex)
					{
						c.abort = true;
						return;
					}
				}
			}
		});
		for (String key : keySet())
		{
			if (key.equals("obj"))
				continue;
			if (key.equals("Content-Type"))
				c.response.setContentType(getAsString(key, c, parameters, dataStreams));
			else
				c.response.setHeader(key, getAsString(key,c,parameters,dataStreams));
		}
		return getObj(c, parameters, dataStreams);
	}

	@Override
	public String getDescription()
	{
		return "Allows the operation to abort itself. As Java Servlets do not have connection closed detection, we trickle space characters out and wait for a connection reset."
				+ "Headers must be set before data goes out, so all key/value pairs here are placed into headers. Note that all crunchers (that do not explicitly check for abort clauses) will only be aborted at the beginning of their execution.";
	}

	@Override
	public String getReturn()
	{
		return "Any";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("obj", "Any","Content-Type","String");
	}

}

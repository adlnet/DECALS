package com.eduworks.cruncher.db.couchbase;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.couchbase.client.CouchbaseClient;
import com.couchbase.client.protocol.views.Paginator;
import com.couchbase.client.protocol.views.Query;
import com.couchbase.client.protocol.views.View;
import com.couchbase.client.protocol.views.ViewResponse;
import com.couchbase.client.protocol.views.ViewRow;
import com.eduworks.interfaces.EwJsonSerializable;
import com.eduworks.lang.EwMap;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;
import com.eduworks.resolver.exception.SoftException;

public class CruncherForEachInTable extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		final String paramName = optAsString("paramName", "eachId", c, parameters, dataStreams);
		final String prevParamName = optAsString("prevParamName", null, c, parameters, dataStreams);

		CouchbaseClient client = CouchBaseClientFactory.get(this, c, parameters, dataStreams);

		View view = client.getView("dev_all", "all");
		final JSONObject output = new JSONObject();
		Paginator paginatedQuery = client.paginatedQuery(view, new Query(), 100);
		while (paginatedQuery.hasNext())
		{
			ViewResponse next = paginatedQuery.next();
			ViewRow me = null;
			for (Iterator<ViewRow> it = next.iterator(); it.hasNext();)
			{ 
				me = it.next();
				final EwMap<String, String[]> newParams = new EwMap<String, String[]>(parameters);
				String key = me.getKey();
				newParams.put(paramName, new String[] { key });
				int tryCount = 0;
				boolean keepTrying = true;
				Object result = null;
				while (keepTrying)
					try
					{
						keepTrying = false;
						result = resolveAChild("op", c,newParams, dataStreams);
					}
					catch (SoftException ex)
					{
						tryCount++;
						if (tryCount < 30)
							keepTrying = true;
					}
				if (result instanceof EwJsonSerializable)
					result = ((EwJsonSerializable) result).toJsonObject();
				synchronized (output)
				{
					if (optAsString("countInstances", "false", c, parameters, dataStreams).equals("true"))
					{
						if (result instanceof JSONObject)
						{
							int count = 0;
							if (output.has(key))
								count = output.getJSONObject(key).optInt("count", 0);
							((JSONObject) result).put("count", ++count);
						}
					}
					output.put(key, result);
				}
			}
		}
		return output;
	}

	@Override
	public String getDescription()
	{
		return "Iterates over a NoSQL table in a database called Couchbase.\n" +
				"Host is determined by the following parameters: _serverUri, _serverUsername, _serverPassword\n" +
				"Table is determined by _databaseName.\n" +
				"Will take the object names and iterate over them, attaching each to a @parameter defined by paramName (and the last one being retained in prevParamName).\n" +
				"Will execute op for each one, with appropriate parameters.\n" +
				"Places all results into a JSONObject where the key is retained, and the result of op is placed in the value.";
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
		return jo("_serverUri","String","_serverUsername","String","_serverPassword","String","_databaseName","String","op","Resolvable","paramName","String","?prevParamName","String");
	}

}

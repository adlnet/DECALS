package com.eduworks.cruncher.db.couchbase;

import java.io.InputStream;
import java.util.Map;

import net.spy.memcached.CASResponse;
import net.spy.memcached.CASValue;

import org.json.JSONException;
import org.json.JSONObject;

import com.couchbase.client.CouchbaseClient;
import com.eduworks.lang.threading.EwThreading;
import com.eduworks.lang.util.EwCache;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;
import com.eduworks.resolver.exception.SoftException;

public class CruncherUpdateDocument extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		String id = getAsString("_id", c, parameters, dataStreams);
		String error = "";
		String cbCache = CouchBaseClientFactory.getCacheValue(this, c, parameters, dataStreams) + id;
		CouchbaseClient client = CouchBaseClientFactory.get(this, c, parameters, dataStreams);

		try
		{
			CASValue<Object> result = (CASValue<Object>) EwCache.getCache("cbDocument").get(cbCache);
			boolean cached = result != null;
			if (result == null)
				synchronized (client)
				{
					result = client.gets(id);
				}
			if (result == null)
			{
				synchronized (client)
				{
					client.add(id, 0, "{}");
					result = client.gets(id);
				}
			}

			JSONObject jo = new JSONObject(result.getValue().toString());

			for (String key : keySet())
			{
				if (isSetting(key))
					continue;
				Object object = get(key, c, parameters, dataStreams);
				if (object != null)
				jo.put(key, object);
			}
			String paramName = getAsString("_paramName", c, parameters, dataStreams);
			if (paramName != null)
			{
				jo.put(paramName, get("_paramValue", c, parameters, dataStreams).toString());
			}
			CASResponse cas = null;
			synchronized (client)
			{
				cas = client.cas(id, result.getCas(), jo.toString());
			}
			if (cas == CASResponse.OK)
			{
				EwCache.getCache("cbDocument").remove(cbCache);
				return jo;
			}
			EwCache.getCache("cbDocument").remove(cbCache);
			error = cas.name();
		}
		catch (NullPointerException ex)
		{
			throw ex;
		}
		catch (RuntimeException ex)
		{
			throw ex;
		}
		catch (Exception ex)
		{
			EwCache.getCache("cbDocument").remove(cbCache);
			CouchBaseClientFactory.removeFromCache(client);
			EwThreading.sleep(5000);
			client.shutdown();
			ex.printStackTrace();
			throw new SoftException(ex.getMessage());
		}
		throw new SoftException(error);
	}

	@Override
	public String getDescription()
	{
		return "Places an object into a JSON NoSQL table in a database called Couchbase.\n" +
				"Host is determined by the following parameters: _serverUri, _serverUsername, _serverPassword\n" +
				"Table is determined by _databaseName. The name of the object is determined by _id.\n" +
				"The fields saved are based on the additional parameters in the object. Will only save the fields that exist as empty parameters!\n" +
				"Returns the new object (with appropriate _id if necessary)";
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
		return jo("_serverUri","String","_serverUsername","String","_serverPassword","String","_databaseName","String","_id","String","obj","JSONObject","<any>","String");
	}

}

package com.eduworks.cruncher.db.couchbase;

import java.io.InputStream;
import java.util.Map;

import net.spy.memcached.CASValue;

import org.json.JSONException;
import org.json.JSONObject;

import com.couchbase.client.CouchbaseClient;
import com.eduworks.lang.util.EwCache;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherGetDocument extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		String key = getAsString("_id", c, parameters, dataStreams);
		String cbCache = CouchBaseClientFactory.getCacheValue(this, c, parameters, dataStreams) + key;
		CASValue<Object> result = (CASValue<Object>) EwCache.getCache("cbDocument").get(cbCache);
		if (result != null)
			return new JSONObject(result.getValue().toString());
		CouchbaseClient client = CouchBaseClientFactory.get(this, c, parameters, dataStreams);
		result = client.gets(key);
		if (result == null)
			return null;
		if (result.getValue() == null)
			return null;
		JSONObject jo = new JSONObject(result.getValue().toString());

		EwCache.getCache("cbDocument").put(cbCache, result);
		return jo;
	}

	@Override
	public String getDescription()
	{
		return "Retreives an object from a JSON NoSQL table in a database called Couchbase.\n" +
				"Host is determined by the following parameters: _serverUri, _serverUsername, _serverPassword\n" +
				"Table is determined by _databaseName. The name of the object retreived is determined by _id";
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
		return jo("_serverUri","String","_serverUsername","String","_serverPassword","String","_databaseName","String","_id","String");
	}

}

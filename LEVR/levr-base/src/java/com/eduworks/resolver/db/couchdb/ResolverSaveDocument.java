package com.eduworks.resolver.db.couchdb;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.ace.product.levr.adapter.DocumentDbInterface;
import com.eduworks.interfaces.EwJsonSerializable;
import com.eduworks.lang.EwList;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Resolver;
import com.fourspaces.couchdb.Document;

public class ResolverSaveDocument extends Resolver
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		resolveAllChildren(c, parameters, dataStreams);
		try
		{
			for (String key : keySet())
			{
				put(key, serializeTree(get(key, parameters)));
				put(key, getAsString(key, parameters));
			}
			Document d = new Document((JSONObject) this.clone());
			DocumentDbInterface.stripDatabaseSettings(d);
			DocumentDbInterface.saveDocument(this, d, parameters);
		}
		catch (Exception e)
		{
			this.removeAll();
			this.put("error", e.toString());
			e.printStackTrace();
		}
		DocumentDbInterface.stripDatabaseSettings(this);
		return this;
	}

	public static Object serializeTree(Object o) throws JSONException
	{
		if (o instanceof EwList)
		{
			EwList ja = (EwList) o;
			for (int i = 0; i < ja.size(); i++)
				ja.set(i,serializeTree(ja.get(i)));
		}
		if (o instanceof JSONArray)
		{
			JSONArray ja = (JSONArray) o;
			for (int i = 0; i < ja.length(); i++)
				ja.put(i,serializeTree(ja.get(i)));
		}
		else if (o instanceof JSONObject)
		{
			JSONObject ja = (JSONObject) o;
			Iterator<String> keys = ja.keys();
			while (keys.hasNext())
			{
				String key = keys.next();
				ja.put(key, serializeTree(ja.get(key)));
			}
		}
		else if (o instanceof EwJsonSerializable)
		{
			return ((EwJsonSerializable) o).toJsonObject();
		}
		return o;
	}

	@Override
	public String getDescription()
	{
		return "Creates a document in a NoSQL CouchDb table.\n" +
				"The endpoint is defined by _serverHostname, _serverPort, _serverLogin, _serverPassword, _databasePrefix and _databaseName\n" +
				"The NoSQL Document is defined by _id"+
				"The set variables in the NoSQL Document are defined by any other parameters.";
	}

	@Override
	public String getReturn()
	{
		return "null";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_UCASTER;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("_serverHostname","String","_serverPort","Number","_serverLogin","String","_serverPassword","String","?_databasePrefix","String","_databaseName","String","_id","String","<any>","Object");
	}
}

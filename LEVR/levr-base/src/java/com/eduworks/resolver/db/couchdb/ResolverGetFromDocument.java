package com.eduworks.resolver.db.couchdb;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.eclipse.jetty.util.URIUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.ace.product.levr.adapter.DocumentDbInterface;
import com.eduworks.lang.json.impl.EwJsonObject;
import com.eduworks.lang.util.EwJson;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.exception.SoftException;
import com.fourspaces.couchdb.Document;

public class ResolverGetFromDocument extends ResolverDocument
{
	public String encodeValue2(String value)
	{
		return URIUtil.encodePath(value);
	}
	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		resolveAllChildren(c,parameters, dataStreams);

		final boolean doNotShorten = optAsBoolean("_doNotShorten", false, parameters);
		final boolean nullify = optAsBoolean("_canNull", false, parameters);
		final boolean soft = optAsBoolean("_soft", false, parameters);
		final String id = getAsString(ID, parameters);

		Document d = null;
		try
		{
			d = DocumentDbInterface.getDocument(this, id, parameters);
			if (d == null && decodeValue(id).equals(id) == false)
				d = DocumentDbInterface.getDocument(this, decodeValue(id), parameters);
			if (d == null && encodeValue(id).equals(id) == false)
				d = DocumentDbInterface.getDocument(this, encodeValue(id), parameters);
			if (d == null && encodeValue2(id).equals(id) == false)
				d = DocumentDbInterface.getDocument(this, encodeValue2(id), parameters);
		}
		catch (IOException e)
		{
		}
		catch (IllegalArgumentException e)
		{
			throw new SoftException(e.getMessage());
		}

		if (d == null)
		{
			if (soft) return null;
			throw new RuntimeException("Document not found: " + id);
		}

		security(d, parameters);

		for (String key : keySet())
		{
			final EwJsonObject jsonObject = (EwJsonObject) d.getJSONObject();
			final String existing = getAsString(key, parameters);

			if (isSetting(key))
				remove(key);
			else if (isId(key))
				put(key, EwJson.getId(jsonObject));
			else if (jsonObject.hasComplex(key))
			{
				Object complex = jsonObject.get(key);
				if (complex instanceof String)
					complex = decodeValue((String) complex);
				if (existing.isEmpty())
					put(key, complex);
				else if (existing.equals(complex))
					put(key, complex);
				else
					return null;
			}
			else if (!existing.isEmpty())
				return null;
			else
				remove(key);
		}

		removeAllSettings();

		DocumentDbInterface.stripDatabaseSettings(this);
		if (!doNotShorten && length() == 1)
		{
			Object object = tryParseJson(get(names().getString(0)));

			if (object instanceof JSONArray)
				object = EwJson.reduce((JSONArray)object);

			debug(id + " == " + object, parameters);
			return object;
		}

		debug(id + " == " + toString(), parameters);
		return (nullify && length() == 0) ? null : this;
	}
	@Override
	public String getDescription()
	{
		return "Retreives a document from a NoSQL CouchDb table.\n" +
				"The endpoint is defined by _serverHostname, _serverPort, _serverLogin, _serverPassword, _databasePrefix and _databaseName\n" +
				"The NoSQL Document is defined by _id\n" +
				"The retreived variables from the NoSQL Document are defined by any other parameters.";
	}
	@Override
	public String getReturn()
	{
		return "JSONObject";
	}
	@Override
	public String getAttribution()
	{
		return ATTRIB_UCASTER;
	}
	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("_serverHostname","String","_serverPort","Number","_serverLogin","String","_serverPassword","String","?_databasePrefix","String","_databaseName","String","_id","String","<any>","Empty");
	}

}

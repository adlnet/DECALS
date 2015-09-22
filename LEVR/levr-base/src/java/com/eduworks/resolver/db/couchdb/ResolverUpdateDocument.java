package com.eduworks.resolver.db.couchdb;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.ace.product.levr.adapter.DocumentDbInterface;
import com.eduworks.interfaces.EwJsonSerializable;
import com.eduworks.lang.EwList;
import com.eduworks.lang.json.impl.EwJsonArray;
import com.eduworks.lang.util.EwJson;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.exception.SoftException;
import com.fourspaces.couchdb.Document;

public class ResolverUpdateDocument extends ResolverDocument
{
	private boolean arrayTooLarge(Document d, String key, int maxArraySize) throws JSONException
	{
		return (maxArraySize != Integer.MAX_VALUE && d.optJSONArray(key) != null && d.optJSONArray(key).length() > maxArraySize);
	}

	private int getMaximumArraySize(String key, Map<String, String[]> parameters) throws JSONException
	{
		return optAsInteger("_" + key + "Max", Integer.MAX_VALUE, parameters);
	}

	private boolean hasDuplicate(Document d, String key, String value) throws JSONException,
			UnsupportedEncodingException
	{
		if (d.optJSONArray(key) != null
				&& new EwList<String>(d.getJSONArray(key).join("\t").replace("\"", "").split("\t"))
						.containsIgnoreCase(value))
			return true;
		else if (d.optString(key).equals(value))
			return true;
		return false;
	}

	private void resizeArray(Document d, String key, int maxArraySize) throws JSONException
	{
		JSONArray optJSONArray = d.getJSONArray(key);
		EwJsonArray newArray = new EwJsonArray();
		for (int i = 0; i < maxArraySize; i++)
			newArray.put(optJSONArray.get(optJSONArray.length() - maxArraySize + i));
		d.put(key, newArray);
	}

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		resolveAllChildren(c, parameters, dataStreams);

		final String id = getAsString("id", parameters);
		final String databaseName = getAsString("databaseName", parameters);
		final String lockName = databaseName + id;

		final boolean mustExist = optAsBoolean("mustAlreadyExist", false, parameters);
		final boolean literal = optAsBoolean("literal",false,parameters);

		lockDocument(lockName);

		try
		{
			// Lazy create
			boolean exists = (id != null && !id.isEmpty());
			Document d = (exists) ? DocumentDbInterface.getDocument(this, id, parameters) : new Document();

			if (d == null)
			{
				String decodeValue = decodeValue(id);
				if (!id.equals(decodeValue))
					d = DocumentDbInterface.getDocument(this, decodeValue, parameters);
			}
			if (d == null)
			{
				String encodeValue = encodeValue(id);
				if (!id.equals(encodeValue))
				d = DocumentDbInterface.getDocument(this, encodeValue, parameters);
			}
			if (d == null)
			{
				if (mustExist)
					throw new RuntimeException("Document must already exist.");

				d = new Document((JSONObject) this.clone());
				d.setId(encodeValue(id));

				for (Object key : EwJson.toArray(d.names()))
					if (d.optString(key.toString()).startsWith("@"))
						d.remove(key.toString());
			}

			security(d, parameters);

			if (d.has("_id") && d.has(ID))
				d.remove("_id");

			for (String key : keySet())
			{
				updatePasswordIfNecessary(d, key, parameters);

				// Ignore any settings
				if (key.equals("_id"))
				{
					remove(ID);
					continue;
				}
				if (isSetting(key))
					continue;
				if (isId(key))
				{
					remove("_id");
					continue;
				}

				 if (get(key, parameters) instanceof EwJsonSerializable)
					d.put(key, ((EwJsonSerializable) get(key, parameters)).toJsonObject());
				else if (get(key, parameters) == null)
				{
					continue;
				}
				else if (Integer.valueOf(0).equals(get(key, parameters)))
				{
					d.remove(key);
					continue;
				}
				else if (shouldAccumulate(key, parameters))
				{
					accumulate(parameters, d, key);
				}
				else
				{
					d.remove(key);
					if (literal)
						d.put(key,ResolverSaveDocument.serializeTree(get(key)));
					else
						accumulate(parameters, d, key);
				}

			}

			DocumentDbInterface.stripDatabaseSettings(d);
			stripSettings(d);

			if (optAsBoolean("needsKey", false, parameters))
				if (d.getId() == null || d.getId().isEmpty())
					return d.getJSONObject();

			DocumentDbInterface.saveDocument(this, d, parameters);
			stripSettings(d);
			
			//fray Fixes issue where an ID is put into the object via 'id' parameter, but does not come out.
			if (contains("id"))
				d.put("id", d.getId());
			return d.getJSONObject();
		}
		catch (SecurityException e)
		{
			throw e;
		}
		catch (SocketException e)
		{
			this.removeAll();
			this.put("error", e.toString());
			throw new RuntimeException(e);
		}
		catch (SoftException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			this.removeAll();
			this.put("error", e.toString());
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		finally
		{
			unlockDocument(lockName);
		}
	}

	private void accumulate(Map<String, String[]> parameters, Document d, String key) throws JSONException,
			UnsupportedEncodingException
	{
//		put(key, ResolverSaveDocument.serializeTree(get(key)));
		for (String value : getAsStrings(key, parameters))
		{
			int maxArraySize = getMaximumArraySize(key, parameters);

			if (value.isEmpty() || value.startsWith("@"))
				continue;
			if (value.trim().startsWith("{"))
				accumulateAsObject(d, key, value);
			else if (value.trim().startsWith("["))
				accumulateAsArray(d, key, value);
			else
				accumulateAsString(d, key, value);

			if (arrayTooLarge(d, key, maxArraySize))
				resizeArray(d, key, maxArraySize);
		}
	}

	private void accumulateAsObject(Document d, String key, String value) throws UnsupportedEncodingException,
			JSONException
	{
		try
		{
			d.accumulate(key, new JSONObject(value));
		}
		catch (JSONException ex)
		{
			accumulateAsString(d, key, value);
		}
	}

	private void accumulateAsArray(Document d, String key, String value) throws UnsupportedEncodingException,
			JSONException
	{
		try
		{
			d.accumulate(key, new JSONArray(value));
		}
		catch (JSONException ex)
		{
			accumulateAsString(d, key, value);
		}
	}

	private void accumulateAsString(Document d, String key, String value) throws UnsupportedEncodingException,
			JSONException
	{
		final String encodedValue = encodeValue(value);
		if (hasDuplicate(d, key, encodedValue))
			return;
		d.accumulate(key, encodedValue);
	}

	private boolean shouldAccumulate(String key, Map<String, String[]> parameters) throws JSONException
	{
		if (optAsBoolean("_" + key + "Append", false, parameters))
			return true;
		return optAsBoolean("_" + key + "Accm", false, parameters);
	}

	@Override
	public String getDescription()
	{
		return "Updates/creates a document in a NoSQL CouchDb table.\n" +
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

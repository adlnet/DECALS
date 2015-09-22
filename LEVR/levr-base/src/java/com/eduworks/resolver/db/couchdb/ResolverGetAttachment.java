package com.eduworks.resolver.db.couchdb;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.ace.product.levr.adapter.DocumentDbInterface;
import com.eduworks.resolver.Context;
import com.eduworks.util.io.InMemoryFile;
import com.fourspaces.couchdb.Document;

public class ResolverGetAttachment extends ResolverDocument
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		resolveAllChildren(c,parameters, dataStreams);

		final String id = getAsString("id", parameters);
		final String databaseName = getAsString("databaseName", parameters);
		final String attachmentId = getAsString("name", parameters);
		final String attachmentName = getAsString("fileName",parameters);
		final String lockName = databaseName + id;
		lockDocument(lockName);
		try
		{
			Document d = DocumentDbInterface.getDocument(this, id, parameters);

			if (d == null)
				d = DocumentDbInterface.getDocument(this, decodeValue(id), parameters);
			if (d == null)
				d = DocumentDbInterface.getDocument(this, encodeValue(id), parameters);
			if (d == null)
				return null;

			security(d, parameters);
			InMemoryFile f = new InMemoryFile();
			f.data = DocumentDbInterface.getAttachment(this, id, attachmentId, parameters);
			f.mime = "application/octet-stream";
			f.name = attachmentName;
			return f;
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
		finally
		{
			unlockDocument(lockName);
		}
	}
	@Override
	public String getDescription()
	{
		return "Gets a file from a document in a NoSQL CouchDb table.\n" +
				"The endpoint is defined by _serverHostname, _serverPort, _serverLogin, _serverPassword, _databasePrefix and _databaseName\n" +
				"The NoSQL Document is defined by _id"+
				"The document variable is defined by 'name' and the resultant filename is defined by 'filename'";
	}

	@Override
	public String[] getResolverNames()
	{
		return new String[]{getResolverName(),"attachmentGet"};
	}
	@Override
	public String getReturn()
	{
		return "null";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("_serverHostname","String","_serverPort","Number","_serverLogin","String","_serverPassword","String","?_databasePrefix","String","_databaseName","String","_id","String","name","String","filename","String");
	}
}

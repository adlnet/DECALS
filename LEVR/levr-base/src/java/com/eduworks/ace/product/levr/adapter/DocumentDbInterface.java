package com.eduworks.ace.product.levr.adapter;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Resolver;
import com.eduworks.resolver.exception.SoftException;
import com.eduworks.util.Tuple;
import com.fourspaces.couchdb.Database;
import com.fourspaces.couchdb.Document;
import com.fourspaces.couchdb.Session;
import com.fourspaces.couchdb.ViewResults;

public class DocumentDbInterface
{
	static HashMap<String, Session>		sessions	= new HashMap<String, Session>();
	static HashMap<String, Database>	dbs			= new HashMap<String, Database>();

	protected static Session getSession(String serverHostname, short serverPort, String serverLogin,
			String serverPassword)
	{
		String key = serverHostname + "," + serverPort + "," + serverLogin + "," + serverPassword;
		Session s = sessions.get(key);
		if (s != null)
			return s;

		try
		{
			s = new Session(serverHostname, serverPort, serverLogin, serverPassword, true, false);
			sessions.put(key, s);
			return s;
		}
		finally
		{
		}
	}

	protected static Database getDatabase(String serverHostname, short serverPort, String serverLogin,
			String serverPassword, String databaseName) throws JSONException
	{
		String key = serverHostname + "," + serverPort + "," + serverLogin + "," + serverPassword + "," + databaseName;
		Database db = dbs.get(key);
		if (db != null)
			return db;

		try
		{
			Session s = getSession(serverHostname, serverPort, serverLogin, serverPassword);
			try
			{
				db = s.getDatabase(databaseName.toLowerCase(Locale.ENGLISH));
			}
			catch (JSONException ex)
			{
			}
			if (db == null)
				db = s.createDatabase(databaseName);
			dbs.put(key, db);
			return db;
		}
		finally
		{
		}
	}

	public static Document getDocument(String serverHostname, short serverPort, String serverLogin,
			String serverPassword, String databaseName, String documentKey) throws IOException, JSONException
	{
		String cacheName = serverHostname + "\t" + serverPort + "\t" + serverLogin + "\t" + serverPassword + "\t"
				+ databaseName + "\t" + documentKey;
		Document document = (Document) Resolver.getThreadCache(cacheName);
		if (document != null)
		{
			return document;
		}
		Database db = getDatabase(serverHostname, serverPort, serverLogin, serverPassword, databaseName);
		try
		{
			document = db.getDocument(documentKey);
			Resolver.putThreadCache(cacheName, document);
			return document;
		}
		finally
		{
		}
	}

	public static boolean deleteDocument(String serverHostname, short serverPort, String serverLogin,
			String serverPassword, String databaseName, Document document) throws IOException, JSONException
	{
		String cacheName = serverHostname + "\t" + serverPort + "\t" + serverLogin + "\t" + serverPassword + "\t"
				+ databaseName + "\t" + document.getId();
		Database db = getDatabase(serverHostname, serverPort, serverLogin, serverPassword, databaseName);
		try
		{
			Resolver.putThreadCache(cacheName, null);
			boolean success = db.deleteDocument(document);
			return success;
		}
		finally
		{
		}
	}

	public static byte[] getAttachment(Resolver settingsObject, String documentKey, String attachmentId,
			Map<String, String[]> parameters) throws JSONException, IOException
	{
		String serverHostname = settingsObject.getAsString("serverHostname", parameters);
		short serverPort = settingsObject.getAsInteger("serverPort", parameters).shortValue();
		String serverLogin = settingsObject.getAsString("serverLogin", parameters);
		String serverPassword = settingsObject.getAsString("serverPassword", parameters);
		String databaseName = settingsObject.optString("databasePrefix", "")
				+ settingsObject.getAsString("databaseName", parameters);
		return getAttachment(serverHostname, serverPort, serverLogin, serverPassword, databaseName, documentKey,
				attachmentId);
	}

	public static byte[] getAttachment(String serverHostname, short serverPort, String serverLogin,
			String serverPassword, String databaseName, String documentId, String attachmentId) throws IOException,
			JSONException
	{
		Database db = getDatabase(serverHostname, serverPort, serverLogin, serverPassword, databaseName);
		try
		{
			return db.getAttachment(documentId, attachmentId);
		}
		finally
		{
		}
	}

	public static String saveAttachment(Resolver settingsObject, String documentKey, String attachmentId,
			String revision, InputStream stream, Map<String, String[]> parameters) throws JSONException, IOException
	{
		String serverHostname = settingsObject.getAsString("serverHostname", parameters);
		short serverPort = settingsObject.getAsInteger("serverPort", parameters).shortValue();
		String serverLogin = settingsObject.getAsString("serverLogin", parameters);
		String serverPassword = settingsObject.getAsString("serverPassword", parameters);
		String databaseName = settingsObject.optString("databasePrefix", "")
				+ settingsObject.getAsString("databaseName", parameters);
		return saveAttachment(serverHostname, serverPort, serverLogin, serverPassword, databaseName, documentKey,
				attachmentId, revision, stream);
	}

	public static String saveAttachment(String serverHostname, short serverPort, String serverLogin,
			String serverPassword, String databaseName, String documentId, String attachmentId, String revision,
			InputStream stream) throws IOException, JSONException
	{
		Database db = getDatabase(serverHostname, serverPort, serverLogin, serverPassword, databaseName);
		try
		{
			stream.reset();
			String cacheName = serverHostname + "\t" + serverPort + "\t" + serverLogin + "\t" + serverPassword + "\t"
					+ databaseName + "\t" + documentId;
			Resolver.putThreadCache(cacheName, null);
			return db.putAttachment(documentId, attachmentId + "?rev=" + revision, "application/octet-stream", stream);
		}
		finally
		{
		}
	}

	public static Document getDocument(JSONObject settingsObject, String documentKey) throws JSONException, IOException
	{
		String serverHostname = settingsObject.getString("serverHostname");
		short serverPort = (short) settingsObject.getInt("serverPort");
		String serverLogin = settingsObject.getString("serverLogin");
		String serverPassword = settingsObject.getString("serverPassword");
		String databaseName = settingsObject.optString("databasePrefix", "") + settingsObject.getString("databaseName");
		return getDocument(serverHostname, serverPort, serverLogin, serverPassword, databaseName, documentKey);
	}

	public static Document getDocument(Resolver settingsObject, String documentKey, Map<String, String[]> parameters)
			throws JSONException, IOException
	{
		String serverHostname = settingsObject.getAsString("serverHostname", parameters);
		short serverPort = settingsObject.getAsInteger("serverPort", parameters).shortValue();
		String serverLogin = settingsObject.getAsString("serverLogin", parameters);
		String serverPassword = settingsObject.getAsString("serverPassword", parameters);
		String databaseName = settingsObject.optString("databasePrefix", "")
				+ settingsObject.getAsString("databaseName", parameters);
		return getDocument(serverHostname, serverPort, serverLogin, serverPassword, databaseName, documentKey);
	}

	public static boolean deleteDocument(JSONObject settingsObject, Document document) throws JSONException,
			IOException
	{
		String serverHostname = settingsObject.getString("serverHostname");
		short serverPort = (short) settingsObject.getInt("serverPort");
		String serverLogin = settingsObject.getString("serverLogin");
		String serverPassword = settingsObject.getString("serverPassword");
		String databaseName = settingsObject.optString("databasePrefix", "") + settingsObject.getString("databaseName");
		return deleteDocument(serverHostname, serverPort, serverLogin, serverPassword, databaseName, document);
	}

	public static boolean deleteDocument(Resolver settingsObject, Document document, Map<String, String[]> parameters)
			throws JSONException, IOException
	{
		String serverHostname = settingsObject.getAsString("serverHostname", parameters);
		short serverPort = settingsObject.getAsInteger("serverPort", parameters).shortValue();
		String serverLogin = settingsObject.getAsString("serverLogin", parameters);
		String serverPassword = settingsObject.getAsString("serverPassword", parameters);
		String databaseName = settingsObject.optString("databasePrefix", "")
				+ settingsObject.getAsString("databaseName", parameters);
		return deleteDocument(serverHostname, serverPort, serverLogin, serverPassword, databaseName, document);
	}

	public static void saveDocument(Resolver settingsObject, Document document, Map<String, String[]> parameters)
			throws JSONException, IOException
	{
		String serverHostname = settingsObject.getAsString("serverHostname", parameters);
		short serverPort = Short.parseShort(settingsObject.getAsString("serverPort", parameters));
		String serverLogin = settingsObject.getAsString("serverLogin", parameters);
		String serverPassword = settingsObject.getAsString("serverPassword", parameters);
		String databaseName = settingsObject.optString("databasePrefix", "")
				+ settingsObject.getAsString("databaseName", parameters);
		boolean overwrite = Boolean.parseBoolean(settingsObject.getAsString("overwrite", parameters));
		saveDocument(serverHostname, serverPort, serverLogin, serverPassword, databaseName, document, overwrite);
	}

	public static void saveDocument(JSONObject settingsObject, Document document) throws JSONException, IOException
	{
		String serverHostname = settingsObject.getString("serverHostname");
		short serverPort = (short) settingsObject.getInt("serverPort");
		String serverLogin = settingsObject.getString("serverLogin");
		String serverPassword = settingsObject.getString("serverPassword");
		String databaseName = settingsObject.optString("databasePrefix", "") + settingsObject.getString("databaseName");
		boolean overwrite = settingsObject.getBoolean("overwrite");
		saveDocument(serverHostname, serverPort, serverLogin, serverPassword, databaseName, document, overwrite);
	}

	public static void saveDocument(JSONObject settingsObject, Document document, boolean overwrite)
			throws JSONException, IOException
	{
		String serverHostname = settingsObject.getString("serverHostname");
		short serverPort = (short) settingsObject.getInt("serverPort");
		String serverLogin = settingsObject.getString("serverLogin");
		String serverPassword = settingsObject.getString("serverPassword");
		String databaseName = settingsObject.optString("databasePrefix", "") + settingsObject.getString("databaseName");
		saveDocument(serverHostname, serverPort, serverLogin, serverPassword, databaseName, document, overwrite);
	}

	public static void stripDatabaseSettings(JSONObject settingsObject)
	{
		settingsObject.remove("serverHostname");
		settingsObject.remove("serverPort");
		settingsObject.remove("serverLogin");
		settingsObject.remove("serverPassword");
		settingsObject.remove("databaseName");
		settingsObject.remove("overwrite");
		settingsObject.remove("_serverHostname");
		settingsObject.remove("_serverPort");
		settingsObject.remove("_serverLogin");
		settingsObject.remove("_serverPassword");
		settingsObject.remove("_databaseName");
		settingsObject.remove("_overwrite");
		settingsObject.remove("databasePrefix");
		settingsObject.remove("_databasePrefix");
	}

	public static void saveDocument(String serverHostname, short serverPort, String serverLogin, String serverPassword,
			String databaseName, Document document, boolean overwrite) throws JSONException, IOException
	{
		Database db = getDatabase(serverHostname, serverPort, serverLogin, serverPassword, databaseName);
		try
		{
			if (db.saveDocument(document))
			{
				return;
			}
			if (!overwrite || !document.getRev().isEmpty())
			{
				throw new SoftException("Document Conflict.");
			}
			if (document.getRev() == null)
			{
				Document revision = db.getDocument(document.getId());
				if (revision != null)
					document.setRev(revision.getRev());
			}
			if (!db.saveDocument(document))
			{
				throw new IOException("Could not save document.");
			}
		}
		catch (SocketException c)
		{
			System.err.println(c.toString());
		}
		finally
		{
		}
	}

	public static void stripDatabaseSettings(Document settingsObject) throws JSONException
	{
		settingsObject.remove("serverHostname");
		settingsObject.remove("serverPort");
		settingsObject.remove("serverLogin");
		settingsObject.remove("serverPassword");
		settingsObject.remove("databaseName");
		settingsObject.remove("overwrite");
		settingsObject.remove("_serverHostname");
		settingsObject.remove("_serverPort");
		settingsObject.remove("_serverLogin");
		settingsObject.remove("_serverPassword");
		settingsObject.remove("_databaseName");
		settingsObject.remove("_overwrite");
		settingsObject.remove("databasePrefix");
		settingsObject.remove("_databasePrefix");
	}

	public static boolean compactTable(JSONObject settingsObject) throws JSONException
	{
		String serverHostname = settingsObject.getString("serverHostname");
		short serverPort = (short) settingsObject.getInt("serverPort");
		String serverLogin = settingsObject.getString("serverLogin");
		String serverPassword = settingsObject.getString("serverPassword");
		String databaseName = settingsObject.optString("databasePrefix", "") + settingsObject.getString("databaseName");
		try
		{
			Database db = getDatabase(serverHostname, serverPort, serverLogin, serverPassword, databaseName);
			boolean success = db.compact();
			return success;
		}
		finally
		{
		}
	}

	public static Tuple<Integer, List<String>> getAllDocumentIds(Resolver settingsObject, int start, int count,
			Map<String, String[]> parameters) throws JSONException
	{
		String serverHostname = settingsObject.getAsString("serverHostname", parameters);
		short serverPort = settingsObject.getAsInteger("serverPort", parameters).shortValue();
		String serverLogin = settingsObject.getAsString("serverLogin", parameters);
		String serverPassword = settingsObject.getAsString("serverPassword", parameters);
		String databaseName = settingsObject.optString("databasePrefix", "")
				+ settingsObject.getAsString("databaseName", parameters);

		Database db = getDatabase(serverHostname, serverPort, serverLogin, serverPassword, databaseName);
		ViewResults documents = db.getAllDocumentsWithCount(start, count);
		List<String> results = new ArrayList<String>();
		for (final Document d : documents.getResults())
			results.add(d.getId());
		return new Tuple<Integer, List<String>>(documents.getInt("total_rows"), results);
	}

	public static Tuple<Integer, List<String>> getAllDocumentIds(Resolver settingsObject, String startId, int count,
			Map<String, String[]> parameters) throws JSONException
	{
		String serverHostname = settingsObject.getAsString("serverHostname", parameters);
		short serverPort = settingsObject.getAsInteger("serverPort", parameters).shortValue();
		String serverLogin = settingsObject.getAsString("serverLogin", parameters);
		String serverPassword = settingsObject.getAsString("serverPassword", parameters);
		String databaseName = settingsObject.optString("databasePrefix", "")
				+ settingsObject.getAsString("databaseName", parameters);

		try
		{
			Database db = getDatabase(serverHostname, serverPort, serverLogin, serverPassword, databaseName);
			ViewResults documents = null;
			while (documents == null) documents = db.getAllDocumentsWithCount(startId, count);
			List<String> results = new ArrayList<String>();
			for (final Document d : documents.getResults())
				results.add(d.getId());
			Tuple<Integer, List<String>> result = new Tuple<Integer, List<String>>(documents.getInt("total_rows"),
					results);
			return result;
		}
		finally
		{
		}
	}

	public static Tuple<Integer, List<String>> getAllDocumentIds(Resolver settingsObject, String startId, String endId,
			int count, Map<String, String[]> parameters) throws JSONException
	{
		String serverHostname = settingsObject.getAsString("serverHostname", parameters);
		short serverPort = settingsObject.getAsInteger("serverPort", parameters).shortValue();
		String serverLogin = settingsObject.getAsString("serverLogin", parameters);
		String serverPassword = settingsObject.getAsString("serverPassword", parameters);
		String databaseName = settingsObject.optString("databasePrefix", "")
				+ settingsObject.getAsString("databaseName", parameters);

		try
		{
			Database db = getDatabase(serverHostname, serverPort, serverLogin, serverPassword, databaseName);
			ViewResults documents = db.getAllDocumentsWithCount(startId, endId, count);
			List<String> results = new ArrayList<String>();
			for (final Document d : documents.getResults())
				results.add(d.getId());
			Tuple<Integer, List<String>> result = new Tuple<Integer, List<String>>(documents.getInt("total_rows"),
					results);
			return result;
		}
		finally
		{
		}
	}

	public static Tuple<Integer, List<Document>> getAllDocuments(JSONObject settingsObject, int start, int count)
			throws JSONException
	{
		String serverHostname = settingsObject.getString("serverHostname");
		short serverPort = (short) settingsObject.getInt("serverPort");
		String serverLogin = settingsObject.getString("serverLogin");
		String serverPassword = settingsObject.getString("serverPassword");
		String databaseName = settingsObject.optString("databasePrefix", "") + settingsObject.getString("databaseName");

		try
		{
			Database db = getDatabase(serverHostname, serverPort, serverLogin, serverPassword, databaseName);
			ViewResults documents = db.getAllDocumentsWithCountWithDocument(start, count);
			Tuple<Integer, List<Document>> result = new Tuple<Integer, List<Document>>(documents.getInt("total_rows"),
					documents.getResults());
			return result;
		}
		finally
		{
		}
	}

	public static Tuple<Integer, List<String>> getViewIds(JSONObject settingsObject, String viewName, int start,
			int count) throws JSONException
	{
		String serverHostname = settingsObject.getString("serverHostname");
		short serverPort = (short) settingsObject.getInt("serverPort");
		String serverLogin = settingsObject.getString("serverLogin");
		String serverPassword = settingsObject.getString("serverPassword");
		String databaseName = settingsObject.optString("databasePrefix", "") + settingsObject.getString("databaseName");

		try
		{
			Database db = getDatabase(serverHostname, serverPort, serverLogin, serverPassword, databaseName);
			ViewResults documents = db.view(viewName + "/" + viewName, false, start, count);
			if (documents == null)
				return null;
			List<String> results = new ArrayList<String>();
			for (final Document d : documents.getResults())
				results.add(d.getId());
			Tuple<Integer, List<String>> result = new Tuple<Integer, List<String>>(documents.getInt("total_rows"),
					results);
			return result;
		}
		finally
		{
		}
	}

	public static boolean verifyConnection(JSONObject settingsObject) throws JSONException
	{

		String serverHostname = settingsObject.getString("serverHostname");
		short serverPort = (short) settingsObject.getInt("serverPort");
		String serverLogin = settingsObject.getString("serverLogin");
		String serverPassword = settingsObject.getString("serverPassword");
		String databaseName = settingsObject.optString("databasePrefix", "") + settingsObject.getString("databaseName");

		Session s = getSession(serverHostname, serverPort, serverLogin, serverPassword);
		return s.getSoft(databaseName) != null;
	}
	
	public static void verifyView(JSONObject settingsObject, String viewName, String viewCode) throws JSONException
	{
		String serverHostname = settingsObject.getString("serverHostname");
		short serverPort = (short) settingsObject.getInt("serverPort");
		String serverLogin = settingsObject.getString("serverLogin");
		String serverPassword = settingsObject.getString("serverPassword");
		String databaseName = settingsObject.optString("databasePrefix", "") + settingsObject.getString("databaseName");

		Database db = getDatabase(serverHostname, serverPort, serverLogin, serverPassword, databaseName);
		boolean success = db.saveView(viewName, viewCode);
	}

	public static Tuple<Integer, List<String>> getAllDocumentIds(JSONObject settingsObject, int startId, int count)
			throws JSONException
	{
		String serverHostname = settingsObject.getString("serverHostname");
		short serverPort = (short) settingsObject.getInt("serverPort");
		String serverLogin = settingsObject.getString("serverLogin");
		String serverPassword = settingsObject.getString("serverPassword");
		String databaseName = settingsObject.optString("databasePrefix", "") + settingsObject.getString("databaseName");

		Database db = getDatabase(serverHostname, serverPort, serverLogin, serverPassword, databaseName);
		ViewResults documents = db.getAllDocumentsWithCount(startId, count);
		List<String> results = new ArrayList<String>();
		for (final Document d : documents.getResults())
			results.add(d.getId());
		Tuple<Integer, List<String>> result = new Tuple<Integer, List<String>>(documents.getInt("total_rows"), results);
		return result;
	}

}

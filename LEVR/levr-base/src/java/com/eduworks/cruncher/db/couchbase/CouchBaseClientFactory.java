package com.eduworks.cruncher.db.couchbase;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.json.JSONException;

import com.couchbase.client.CouchbaseClient;
import com.eduworks.lang.util.EwCache;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CouchBaseClientFactory
{
	static long				accesses	= 0;
	public static Logger	log			= Logger.getLogger(CouchBaseClientFactory.class);

	public static void removeFromCache(CouchbaseClient client)
	{
		EwCache<Object, Object> theCache = EwCache.getCache("CouchbaseClient");
		Object keyIwant = null;
		for (Entry<Object, Object> e : theCache.getAll())
			if (e.getValue() == client)
				keyIwant = e.getKey();
		try
		{
			theCache.remove(keyIwant);
		}
		catch (Exception ex)
		{
			log.debug("Key already removed." + ex);
		}
	}

	public static CouchbaseClient get(Cruncher cruncherGetDocument, Context c,
			Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		// Set the URIs and get a client
		List<URI> uris = new ArrayList<URI>();

		// Connect to localhost or to the appropriate URI(s)
		for (String s : cruncherGetDocument.getAsString("_serverUri", c, parameters, dataStreams).split(","))
			uris.add(URI.create(s));
		String databaseName = cruncherGetDocument.getAsString("_databaseName", c, parameters, dataStreams);
		String username = cruncherGetDocument.getAsString("_serverUsername", c, parameters, dataStreams);
		String password = cruncherGetDocument.getAsString("_serverPassword", c, parameters, dataStreams);

		String cbCache = getCacheValue(cruncherGetDocument, c, parameters, dataStreams);

		EwCache<Object, Object> theCache = EwCache.getCache("CouchbaseClient");
		CouchbaseClient cache = (CouchbaseClient) theCache.get(cbCache);
		accesses++;
		if (accesses % 50000 == 0)
		{
			log.debug("Pausing to ensure queue is keeping up.");
			cache.waitForQueues(60, TimeUnit.SECONDS);
			log.debug("Resuming.");
		}
		if (cache != null)
			return cache;
		synchronized (CouchBaseClientFactory.class)
		{
			cache = (CouchbaseClient) theCache.get(cbCache);
			if (cache != null)
				return cache;
			try
			{
				CouchbaseClient cc = new CouchbaseClient(uris, databaseName, username, password);
				theCache.put(cbCache, cc);
				return cc;
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		return null;
	}

	public static String getCacheValue(Cruncher cruncherGetDocument, Context c,
			Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		// Set the URIs and get a client
		List<URI> uris = new ArrayList<URI>();

		// Connect to localhost or to the appropriate URI(s)
		for (String s : cruncherGetDocument.getAsString("_serverUri", c, parameters, dataStreams).split(","))
			uris.add(URI.create(s));
		String databaseName = cruncherGetDocument.getAsString("_databaseName", c, parameters, dataStreams);
		String username = cruncherGetDocument.getAsString("_serverUsername", c, parameters, dataStreams);
		String password = cruncherGetDocument.getAsString("_serverPassword", c, parameters, dataStreams);
		return uris.toString() + databaseName + username + password;
	}

}

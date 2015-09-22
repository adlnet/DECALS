/*
   Copyright 2007 Fourspaces Consulting, LLC

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package com.fourspaces.couchdb;

import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.lang.json.impl.EwJsonObject;
import com.fourspaces.couchdb.util.JSONUtils;

/**
 * Everything in CouchDB is a Document. In this case, the document is an object
 * backed by a JSONObject. The Document is also aware of the database that it is
 * connected to. This allows the Document to reload it's properties when needed.
 * The only special fields are "_id", "_rev", "_revisions", and "_view_*".
 * <p>
 * All document have an _id and a _rev. If this is a new document those fields
 * are populated when they are saved to the CouchDB server.
 * <p>
 * _revisions is only populated if the document has been retrieved via
 * database.getDocumentWithRevisions(); So, if this document wasn't, then when
 * you call document.getRevisions(), it will go back to the server to reload
 * itself via database.getDocumentWithRevisions().
 * <p>
 * The Document can be treated like a JSONObject, eventhough it doesn't extend
 * JSONObject (it's final).
 * <p>
 * You can also get/set values by calling document.get(key),
 * document.put(key,value), just like a Map.
 * <p>
 * You can get a handle on the backing JSONObject by calling
 * document.getJSONObject(); If this hasn't been loaded yet, it will load the
 * data itself using the given database connection.
 * <p>
 * If you got this Document from a view, you are likely missing elements. To
 * load them you can call document.load().
 *
 * @author mbreese
 *
 */
@SuppressWarnings({ "rawtypes", "serial" })
public class Document implements Serializable
{
	Log							log						= LogFactory.getLog(Document.class);

	public static final String	REVISION_HISTORY_PROP	= "_revisions";

	protected Database			database				= null;
	protected EwJsonObject		object;

	boolean						loaded					= false;

	/**
	 * Create a new Document
	 *
	 */
	public Document()
	{
		this.object = new EwJsonObject();
	}

	/**
	 * Create a new Document from a JSONObject
	 *
	 * @param obj
	 */
	public Document(JSONObject obj)
	{
		this.object = EwJsonObject.convert(obj);
		loaded = true;
	}

	/**
	 * Load data into this document from a differing JSONObject
	 * <p>
	 * This is mainly for reloading data for an object that was retrieved from a
	 * view. This version doesn't overwrite any unsaved data that is currently
	 * present in this object.
	 *
	 * @param object2
	 * @throws JSONException
	 */
	protected void load(JSONObject object2) throws JSONException
	{
		if (!loaded)
		{
			Iterator i = object2.keys();
			while (i.hasNext())
			{
				String key = i.next().toString();
				object.put(key, object2.get(key));
			}
			loaded = true;
		}
	}

	/**
	 * This document's id (if saved)
	 *
	 * @return
	 */
	public String getId()
	{
		if (StringUtils.isNotBlank(object.optString("_id")))
		{
			return object.optString("_id");
		}
		else
		{
			return object.optString("id");
		}
	}

	public void setId(String id) throws JSONException
	{
		object.put("_id", id);
	}

	/**
	 * This strips _design from the document id
	 */
	public String getViewDocumentId()
	{
		String id = getId();
		int pos = id.lastIndexOf("/");
		if (pos == -1)
		{
			return id;
		}
		else
		{
			return id.substring(pos + 1);
		}
	}

	/**
	 * This document's Revision (if saved)
	 *
	 * @return
	 */
	public String getRev()
	{
		if (StringUtils.isNotBlank(object.optString("_rev")))
		{
			return object.optString("_rev");
		}
		else
		{
			return object.optString("rev");
		}
	}

	public void setRev(String rev) throws JSONException
	{
		if (rev == null || rev.isEmpty())
			object.remove("_rev");
		else
			object.put("_rev", rev);
	}

	/**
	 * A list of the revision numbers that this document has. If this hasn't
	 * been populated with a "full=true" query, then the database will be
	 * re-queried
	 *
	 * @return
	 * @throws JSONException
	 */
	public String[] getRevisions() throws IOException, JSONException
	{
		String[] revs = null;
		if (!object.has("_revs"))
		{
			populateRevisions();
		}
		// System.out.println(object);
		JSONArray ar = object.getJSONObject(REVISION_HISTORY_PROP).getJSONArray("ids");
		if (ar != null)
		{
			revs = new String[ar.length()];
			for (int i = 0; i < ar.length(); i++)
			{
				revs[i] = ar.getString(i);
			}
		}
		return revs;
	}

	/**
	 * Get a named view that is stored in the document.
	 *
	 * @param name
	 * @return
	 * @throws JSONException
	 */
	public View getView(String name) throws JSONException
	{
		if (object.has("views"))
		{
			JSONObject views = object.getJSONObject("views");
			if (views.has(name))
			{
				return new View(this, name);
			}
		}
		return null;
	}

	/**
	 * Add a view to this document. If a view function already exists with the
	 * given viewName it is overwritten.
	 * <p>
	 * This isn't persisted until the document is saved.
	 *
	 * @param designDoc
	 *            document name
	 * @param viewName
	 * @param function
	 * @return
	 * @throws JSONException
	 */
	public View addView(String designDoc, String viewName, String function) throws JSONException
	{
		object.put("_id", "_design/" + designDoc); // Not sure if _id or id
													// should be used
		object.put("language", "javascript"); // FIXME specify language

		EwJsonObject funcs = new EwJsonObject();
		// System.err.println("JSON String: " +
		// JSONUtils.stringSerializedFunction(function));
		// funcs.put("map", JSONUtils.stringSerializedFunction(function));
		funcs.accumulate("map", JSONUtils.stringSerializedFunction(function));

		System.err.println("FUNCS: " + funcs.toString());

		EwJsonObject viewMap = new EwJsonObject();
		viewMap.put(viewName, funcs);

		object.put("views", viewMap);

		return new View(this, viewName, function);

	}

	/**
	 * Removes a view from this document.
	 * <p>
	 * This isn't persisted until the document is saved.
	 *
	 * @param viewName
	 */
	public void deleteView(String viewName)
	{
		object.remove("_design/" + viewName);
	}

	void setDatabase(Database database)
	{
		this.database = database;
	}

	/**
	 * Loads data from the server for this document. Actually requests a new
	 * copy of data from the server and uses that to populate this document.
	 * This doesn't overwrite any unsaved data.
	 *
	 * @throws JSONException
	 */
	public void refresh() throws IOException, JSONException
	{
		if (database != null)
		{
			Document doc = database.getDocument(getId());
			if (doc == null)
				return;
			// log.info("Loading: "+doc.getJSONObject());
			load(doc.getJSONObject());
		}
	}

	protected void populateRevisions() throws IOException, JSONException
	{
		if (database != null)
		{
			Document doc = database.getDocumentWithRevisions(getId());
			log.info("Loading: " + doc.getJSONObject());
			load(doc.getJSONObject());
		}
	}

	/**
	 * Retrieves the backing JSONObject
	 *
	 * @return
	 * @throws JSONException
	 */
	public JSONObject getJSONObject() throws JSONException
	{
		if (!loaded && database != null && getId() != null && !getId().equals(""))
		{
			try
			{
				refresh();
			}
			catch (IOException e)
			{
				throw new RuntimeException("error in refreshing Document", e);
			}
		}
		return object;
	}

	@Override
	public String toString()
	{
		return object.toString();
	}

	/*
	 * Delegate methods to the JSON Object.
	 */
	public JSONObject accumulate(String arg0, boolean arg1) throws JSONException
	{
		return getJSONObject().accumulate(arg0, arg1);
	}

	public JSONObject accumulate(String arg0, double arg1) throws JSONException
	{
		return getJSONObject().accumulate(arg0, arg1);
	}

	public JSONObject accumulate(String arg0, int arg1) throws JSONException
	{
		return getJSONObject().accumulate(arg0, arg1);
	}

	public JSONObject accumulate(String arg0, long arg1) throws JSONException
	{
		return getJSONObject().accumulate(arg0, arg1);
	}

	public JSONObject accumulate(String arg0, Object arg1) throws JSONException
	{
		return getJSONObject().accumulate(arg0, arg1);
	}

	public boolean containsKey(Object arg0)
	{
		try
		{
			return getJSONObject().has(arg0.toString());
		}
		catch (JSONException e)
		{
			e.printStackTrace();
			return false;
		}
	}

	public boolean containsValue(Object arg0)
	{
		try
		{
			return getJSONObject().has(arg0.toString());
		}
		catch (JSONException e)
		{
			e.printStackTrace();
			return false;
		}
	}

	// public JSONObject element(String arg0, boolean arg1) throws JSONException
	// {
	// return getJSONObject().element(arg0, arg1);
	// }
	// public JSONObject element(String arg0, Collection arg1) throws
	// JSONException {
	// return getJSONObject().element(arg0, arg1);
	// }
	// public JSONObject element(String arg0, double arg1) throws JSONException
	// {
	// return getJSONObject().element(arg0, arg1);
	// }
	// public JSONObject element(String arg0, int arg1) throws JSONException {
	// return getJSONObject().element(arg0, arg1);
	// }
	// public JSONObject element(String arg0, long arg1) throws JSONException {
	// return getJSONObject().element(arg0, arg1);
	// }
	// public JSONObject element(String arg0, Map arg1) throws JSONException {
	// return getJSONObject().element(arg0, arg1);
	// }
	// public JSONObject element(String arg0, Object arg1) throws JSONException
	// {
	// return getJSONObject().element(arg0, arg1);
	// }
	// public JSONObject elementOpt(String arg0, Object arg1) throws
	// JSONException {
	// return getJSONObject().elementOpt(arg0, arg1);
	// }

	public Object get(Object arg0)
	{
		try
		{
			return getJSONObject().get(arg0.toString());
		}
		catch (JSONException e)
		{
			return null;
		}
	}

	/** Identical to {@link #get(String)} since complex keys are basic to {@link EwJsonObject}. */
	public Object getComplex(String path) throws JSONException
	{
		return get(path);
	}

	public boolean hasComplex(String path) throws JSONException
	{
		return object.hasComplex(path);
	}

	public Object get(String arg0) throws JSONException
	{
		return getJSONObject().get(arg0);
	}

	public boolean getBoolean(String arg0) throws JSONException
	{
		return getJSONObject().getBoolean(arg0);
	}

	public double getDouble(String arg0) throws JSONException
	{
		return getJSONObject().getDouble(arg0);
	}

	public int getInt(String arg0) throws JSONException
	{
		return getJSONObject().getInt(arg0);
	}

	public JSONArray getJSONArray(String arg0) throws JSONException
	{
		return getJSONObject().getJSONArray(arg0);
	}

	public JSONObject getJSONObject(String arg0) throws JSONException
	{
		return getJSONObject().getJSONObject(arg0);
	}

	public long getLong(String arg0) throws JSONException
	{
		return getJSONObject().getLong(arg0);
	}

	public String getString(String arg0) throws JSONException
	{
		return getJSONObject().getString(arg0);
	}

	public boolean has(String arg0) throws JSONException
	{
		return getJSONObject().has(arg0);
	}

	public Iterator keys() throws JSONException
	{
		return getJSONObject().keys();
	}

	public JSONArray names() throws JSONException
	{
		return getJSONObject().names();
	}

	public Object opt(String arg0) throws JSONException
	{
		return getJSONObject().opt(arg0);
	}

	public boolean optBoolean(String arg0, boolean arg1) throws JSONException
	{
		return getJSONObject().optBoolean(arg0, arg1);
	}

	public boolean optBoolean(String arg0) throws JSONException
	{
		return getJSONObject().optBoolean(arg0);
	}

	public double optDouble(String arg0, double arg1) throws JSONException
	{
		return getJSONObject().optDouble(arg0, arg1);
	}

	public double optDouble(String arg0) throws JSONException
	{
		return getJSONObject().optDouble(arg0);
	}

	public int optInt(String arg0, int arg1) throws JSONException
	{
		return getJSONObject().optInt(arg0, arg1);
	}

	public int optInt(String arg0) throws JSONException
	{
		return getJSONObject().optInt(arg0);
	}

	public JSONArray optJSONArray(String arg0) throws JSONException
	{
		return getJSONObject().optJSONArray(arg0);
	}

	public JSONObject optJSONObject(String arg0) throws JSONException
	{
		return getJSONObject().optJSONObject(arg0);
	}

	public long optLong(String arg0, long arg1) throws JSONException
	{
		return getJSONObject().optLong(arg0, arg1);
	}

	public long optLong(String arg0) throws JSONException
	{
		return getJSONObject().optLong(arg0);
	}

	public String optString(String arg0, String arg1) throws JSONException
	{
		return getJSONObject().optString(arg0, arg1);
	}

	public String optString(String arg0) throws JSONException
	{
		return getJSONObject().optString(arg0);
	}

	public Object put(Object arg0, Object arg1) throws JSONException
	{
		return getJSONObject().put(arg0.toString(), arg1);
	}

	public void putAll(Map arg0) throws JSONException
	{
		for (Object o : arg0.keySet())
			getJSONObject().put(o.toString(), arg0.get(o));
	}

	public Object remove(Object arg0) throws JSONException
	{
		return getJSONObject().remove(arg0.toString());
	}

	public Object remove(String arg0) throws JSONException
	{
		return getJSONObject().remove(arg0);
	}

	public int size() throws JSONException
	{
		return getJSONObject().length();
	}

	public boolean isEmpty() throws JSONException
	{
		return (object == null)
			? true
			: getJSONObject().length() == 0;
	}
}

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

import static com.fourspaces.couchdb.util.JSONUtils.urlEncodePath;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.lang.json.impl.EwJsonArray;
import com.eduworks.lang.json.impl.EwJsonObject;

/**
 * This represents a particular database on the CouchDB server
 * <p/>
 * Using this object, you can get/create/update/delete documents. You can also
 * call views (named and adhoc) to query the underlying database.
 * 
 * @author mbreese
 */
public class Database
{
	Log							log		= LogFactory.getLog(Database.class);
	private final String		name;
	private final int			documentCount;
	private final int			updateSeq;

	private final Session		session;

	private static final String	VIEW	= "/_view/";
	private static final String	DESIGN	= "_design/";

	/**
	 * C-tor only used by the Session object. You'd never call this directly.
	 * 
	 * @param json
	 * @param session
	 * @throws JSONException
	 */
	Database(JSONObject json, Session session) throws JSONException
	{
		name = json.getString("db_name");
		documentCount = json.getInt("doc_count");
		updateSeq = json.getInt("update_seq");

		this.session = session;
	}

	/**
	 * The name of the database
	 * 
	 * @return
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * The number of documents in the database <b>at the time that it was
	 * retrieved from the session</b> This number probably isn't accurate after
	 * the initial load... so if you want an accurate assessment, call
	 * Session.getDatabase() again to reload a new database object.
	 * 
	 * @return
	 */
	public int getDocumentCount()
	{
		return documentCount;
	}

	/**
	 * The update seq from the initial database load. The update sequence is the
	 * 'revision id' of an entire database. Useful for getting all documents in
	 * a database since a certain revision
	 * 
	 * @return
	 * @see getAllDocuments()
	 */
	public int getUpdateSeq()
	{
		return updateSeq;
	}

	/**
	 * Runs the standard "_all_docs" view on this database
	 * 
	 * @return ViewResults - the results of the view... this can be iterated
	 *         over to get each document.
	 * @throws JSONException
	 */
	public ViewResults getAllDocuments() throws JSONException
	{
		return view(new View("_all_docs"), false);
	}

	/**
	 * Gets all design documents
	 * 
	 * @return ViewResults - all design docs
	 * @throws JSONException
	 */
	public ViewResults getAllDesignDocuments() throws JSONException
	{
		View v = new View("_all_docs");
		v.startKey = "%22_design%2F%22";
		v.endKey = "%22_design0%22";
		v.includeDocs = Boolean.TRUE;
		return view(v, false);
	}

	/**
	 * Runs the standard "_all_docs" view on this database, with count
	 * 
	 * @return ViewResults - the results of the view... this can be iterated
	 *         over to get each document.
	 * @throws JSONException
	 */
	public ViewResults getAllDocumentsWithCount(int count) throws JSONException
	{
		View v = new View("_all_docs");
		v.setLimit(count);
		return view(v, false);
	}

	public ViewResults getAllDocumentsWithCount(int start, int count) throws JSONException
	{
		View v = new View("_all_docs");
		v.setLimit(count);
		v.setSkip(Integer.toString(start));
		return view(v, false);
	}

	public ViewResults getAllDocumentsWithCountWithDocument(int start, int count) throws JSONException
	{
		View v = new View("_all_docs");
		v.setLimit(count);
		v.setSkip(Integer.toString(start));
		v.setWithDocs(true);
		return view(v, false);
	}

	public ViewResults getAllDocumentsWithCountWithDocument(String startKey, int count) throws JSONException
	{
		View v = new View("_all_docs");
		v.setLimit(count);
		v.setStartKey(startKey);
		v.setWithDocs(true);
		return view(v, false);
	}

	public ViewResults getAllDocumentsWithCount(String startKey, int count) throws JSONException
	{
		View v = new View("_all_docs");
		v.setLimit(count);
		v.setStartKey(startKey);
		return view(v, false);
	}

	public ViewResults getAllDocumentsWithCount(String startKey, String endKey, int count) throws JSONException
	{
		View v = new View("_all_docs");
		v.setLimit(count);
		v.setStartKey(startKey);
		v.setEndKey(endKey);
		return view(v, false);
	}

	/**
	 * Runs "_all_docs_by_update_seq?startkey=revision" view on this database
	 * 
	 * @return ViewResults - the results of the view... this can be iterated
	 *         over to get each document.
	 * @throws JSONException
	 */
	public ViewResults getAllDocuments(int revision) throws JSONException
	{
		return view(new View("_all_docs_by_seq?startkey=" + revision), false);
	}

	/**
	 * Runs a named view on the database This will run a view and apply any
	 * filtering that is requested (reverse, startkey, etc).
	 * 
	 * @param view
	 * @return
	 * @throws JSONException
	 */
	public ViewResults view(View view) throws JSONException
	{
		return view(view, true);
	}

	/**
	 * Runs a view, appending "_view" to the request if isPermanentView is true.
	 * *
	 * 
	 * @param view
	 * @param isPermanentView
	 * @return
	 * @throws JSONException
	 */
	private ViewResults view(final View view, final boolean isPermanentView) throws JSONException
	{
		String url = null;
		if (isPermanentView)
		{
			String[] elements = view.getFullName().split("/");
			url = this.name + "/" + ((elements.length < 2) ? elements[0] : DESIGN + elements[0] + VIEW + elements[1]);
		}
		else
		{
			url = this.name + "/" + view.getFullName();
		}

		CouchResponse resp = session.get(url, view.getQueryString());
		if (resp == null)
			return null;
		if (resp.isOk())
		{
			ViewResults results = new ViewResults(view, resp.getBodyAsJSONObject());
			results.setDatabase(this);
			return results;
		}
		return null;

	}

	/**
	 * Runs a named view <i>Not currently working in CouchDB code</i>
	 * 
	 * @param fullname
	 *            - the fullname (including the document name) ex:
	 *            foodoc:viewname
	 * @return
	 * @throws JSONException
	 */

	public ViewResults view(String fullname, boolean stale) throws JSONException
	{
		View view2 = new View(fullname);
		view2.setStale(stale);
		return view(view2, true);
	}

	public ViewResults view(String fullname, boolean stale, int limit) throws JSONException
	{
		View view2 = new View(fullname);
		view2.setLimit(limit);
		view2.setDescending(true);
		view2.setStale(stale);
		return view(view2, true);
	}

	public ViewResults view(String fullname, boolean stale, int start, int limit) throws JSONException
	{
		View view2 = new View(fullname);
		view2.setLimit(limit);
		view2.setStale(stale);
		view2.setDescending(true);
		view2.setSkip(Integer.toString(start));
		return view(view2, true);
	}

	public ViewResults viewWithDocs(String fullname, boolean stale, int start, int limit) throws JSONException
	{
		View view2 = new View(fullname);
		view2.setLimit(limit);
		view2.setStale(stale);
		view2.setWithDocs(true);
		view2.setDescending(true);
		view2.setSkip(Integer.toString(start));
		return view(view2, true);
	}

	public ViewResults view(String fullname) throws JSONException
	{
		return view(new View(fullname), true);
	}

	/**
	 * Runs an ad-hoc view from a string
	 * 
	 * @param function
	 *            - the Javascript function to use as the filter.
	 * @return results
	 */
	// public ViewResults adhoc(String function)
	// {
	// return adhoc(new AdHocView(function));
	// }

	/**
	 * Runs an ad-hoc view from an AdHocView object. You probably won't use this
	 * much, unless you want to add filtering to the view (reverse, startkey,
	 * etc...)
	 * 
	 * @param view
	 * @return
	 */
	/*
	 * public ViewResults adhoc(final AdHocView view) {
	 * 
	 * String adHocBody = new JSONStringer().object().key("map").value(
	 * JSONUtils
	 * .stringSerializedFunction(view.getFunction())).endObject().toString();
	 * 
	 * // Bugfix - include query string for adhoc views to support // additional
	 * view options (setLimit, etc) CouchResponse resp = session.post(name +
	 * "/_temp_view", adHocBody, view.getQueryString()); if (resp.isOk()) {
	 * ViewResults results = new ViewResults(view, resp.getBodyAsJSONObject());
	 * results.setDatabase(this); return results; } else {
	 * log.warn("Error executing view - " + resp.getErrorId() + " " +
	 * resp.getErrorReason()); } return null; }
	 */

	/**
	 * Save a document at the given _id
	 * <p/>
	 * if the docId is null or empty, then this performs a POST to the database
	 * and retrieves a new _id.
	 * <p/>
	 * Otherwise, a PUT is called.
	 * <p/>
	 * Either way, a new _id and _rev are retrieved and updated in the Document
	 * object
	 * 
	 * @param doc
	 * @param docId
	 * @throws JSONException
	 */
	public boolean saveDocument(Document doc, String docId) throws IOException, JSONException
	{
		return saveDocument(doc, docId, false);
	}

	public boolean saveDocument(Document doc, String docId, boolean batch) throws IOException, JSONException
	{
		CouchResponse resp;
		if (docId == null || docId.equals(""))
		{
			resp = session.post(name + (batch ? "?batch=ok" : ""), doc.getJSONObject().toString());
		}
		else
		{
			resp = session.put(name + "/" + urlEncodePath(docId) + (batch ? "?batch=ok" : ""), doc.getJSONObject()
					.toString());
		}

		if (resp == null)
		{
			return saveDocument(doc, docId, batch);
		}
		if (resp.isOk())
		{
			try
			{
				JSONObject bodyAsJSONObject = resp.getBodyAsJSONObject();
				if (doc.getId() == null || doc.getId().equals(""))
				{
					doc.setId(bodyAsJSONObject.getString("id"));
				}
				doc.setRev(bodyAsJSONObject.optString("rev", bodyAsJSONObject.optString("_rev")));
			}
			catch (JSONException e)
			{
				System.out.println(resp);
				e.printStackTrace();
			}
			doc.setDatabase(this);
			return true;
		}
		else
		{
			// log.warn("Error adding document - " + resp.getErrorId() + " " +
			// resp.getErrorReason());
			// System.err.println("RESP: " + resp);
			return false;
		}
	}

	/**
	 * Save a document w/o specifying an id (can be null)
	 * 
	 * @param doc
	 * @throws JSONException
	 */
	public boolean saveDocument(Document doc) throws IOException, JSONException
	{
		return saveDocument(doc, doc.getId(), false);
	}

	public boolean saveDocument(Document doc, boolean batch) throws IOException, JSONException
	{
		return saveDocument(doc, doc.getId(), batch);
	}

	public List<Document> getBulkDocument(JSONArray ary) throws JSONException
	{
		ArrayList<Document> results = new ArrayList<Document>();

		EwJsonObject getObj = new EwJsonObject();
		getObj.put("keys", ary);

		CouchResponse resp = null;

		resp = session.post(name + "/_all_docs?include_docs=true", getObj.toString());

		if (resp.isOk())
		{
			JSONObject o = resp.getBodyAsJSONObject();
			JSONArray docs = o.getJSONArray("rows");

			for (int i = 0; i < docs.length(); i++)
			{
				JSONObject jsonObject = docs.getJSONObject(i);
				if (jsonObject.opt("error") != null)
					continue;
				Document document = new Document(jsonObject.getJSONObject("doc"));
				results.add(document);
			}
		}
		else
		{
			log.warn("Error getting bulk docs - " + resp.getErrorId() + " " + resp.getErrorReason());
		}
		return results;
	}

	static public HashMap<String, Integer>	errors	= new HashMap<String, Integer>();

	public List<String> bulkSaveDocuments(Document[] documents) throws IOException, JSONException
	{
		CouchResponse resp = null;

		EwJsonObject jsonObject = new EwJsonObject();
		EwJsonArray array = new EwJsonArray();
		for (Document d : documents)
			array.put(d.getJSONObject());
		jsonObject.put("docs", array);
		String string = jsonObject.toString();
		resp = session.post(name + "/_bulk_docs", string);

		List<String> failedDocuments = new ArrayList<String>();

		if (resp != null && resp.isOk())
		{
			// TODO set Ids and revs and name (db)
			final JSONArray respJsonArray = resp.getBodyAsJSONArray();
			JSONObject respObj = null;
			String id = null;
			String rev = null;
			Map<String, Document> lookup = new HashMap<String, Document>();
			for (Document d : documents)
				lookup.put(d.getId(), d);
			for (int j = 0; j < respJsonArray.length(); j++)
			{
				respObj = respJsonArray.getJSONObject(j);
				id = respObj.getString("id");
				Document document = lookup.get(id);
				String error = respObj.optString("error", "");
				if (error.equals("") == false)
				{
					failedDocuments.add(id);
					lookup.remove(id);
					synchronized (errors)
					{
						if (!errors.containsKey(error))
							errors.put(error, 1);
						else
							errors.put(error, errors.get(error) + 1);
					}
					continue;
				}
				synchronized (errors)
				{
					if (!errors.containsKey("success"))
						errors.put("success", 1);
					else
						errors.put("success", errors.get("success") + 1);
				}
				rev = respObj.getString("rev");
				if (StringUtils.isBlank(document.getId()))
				{
					document.setId(id);
					document.setRev(rev);
				}
				else if (StringUtils.isNotBlank(document.getId()) && document.getId().equals(id))
				{
					document.setRev(rev);
				}

				document.setDatabase(this);
				lookup.remove(id);
			}
			if (lookup.size() > 0)
				System.out.println("Couldn't account for all documents.");
			return failedDocuments;
		}
		else
		{
			if (resp != null)
				log.warn("Error bulk saving documents - " + resp.getErrorId() + " " + resp.getErrorReason());
			for (Document d : documents)
				failedDocuments.add(d.getId());
			return failedDocuments;
		}
	}

	/**
	 * Retrieves a document from the CouchDB database
	 * 
	 * @param id
	 * @return
	 * @throws JSONException
	 */
	public Document getDocument(String id) throws IOException, JSONException
	{
		return getDocument(id, null, false);
	}

	/**
	 * Retrieves a document from the database and asks for a list of it's
	 * revisions. The list of revision keys can be retrieved from
	 * Document.getRevisions();
	 * 
	 * @param id
	 * @return
	 * @throws JSONException
	 */
	public Document getDocumentWithRevisions(String id) throws IOException, JSONException
	{
		return getDocument(id, null, true);
	}

	/**
	 * Retrieves a specific document revision
	 * 
	 * @param id
	 * @param revision
	 * @return
	 * @throws JSONException
	 */
	public Document getDocument(String id, String revision) throws IOException, JSONException
	{
		return getDocument(id, revision, false);
	}

	/**
	 * Retrieves a specific document revision and (optionally) asks for a list
	 * of all revisions
	 * 
	 * @param id
	 * @param revision
	 * @param showRevisions
	 * @return the document
	 * @throws JSONException
	 */
	public Document getDocument(String id, String revision, boolean showRevisions) throws IOException, JSONException
	{
		CouchResponse resp;
		Document doc = null;
		if (revision != null && showRevisions)
		{
			resp = session.get(name + "/" + urlEncodePath(id), "rev=" + revision + "&full=true");
		}
		else if (revision != null && !showRevisions)
		{
			resp = session.get(name + "/" + urlEncodePath(id), "rev=" + revision);
		}
		else if (revision == null && showRevisions)
		{
			resp = session.get(name + "/" + urlEncodePath(id), "revs=true");
		}
		else
		{
			resp = session.get(name + "/" + urlEncodePath(id));
		}
		if (resp != null && resp.isOk())
		{
			doc = new Document(resp.getBodyAsJSONObject());
			doc.setDatabase(this);
		}
		else
		{
			try
			{
				resp = session.get(name + "/" + id);
				if (resp != null && resp.isOk())
				{
					doc = new Document(resp.getBodyAsJSONObject());
					doc.setDatabase(this);
				}
				else
				{
					// log.warn("Error getting document - " + resp.getErrorId()
					// +
					// " " + resp.getErrorReason());
				}
			}
			catch (IllegalArgumentException ex)
			{
				// log.warn("Error getting document - " + resp.getErrorId() +
				// " " + resp.getErrorReason());
			}
		}
		return doc;
	}

	/**
	 * Deletes a document
	 * 
	 * @param d
	 * @return was the delete successful?
	 * @throws JSONException
	 * @throws IllegalArgumentException
	 *             for blank document id
	 */
	public boolean deleteDocument(Document d) throws IOException, JSONException
	{
		return deleteDocument(d, false);
	}

	public boolean deleteDocument(Document d, boolean batch) throws IOException, JSONException
	{

		if (StringUtils.isBlank(d.getId()))
		{
			throw new IllegalArgumentException("cannot delete document, doc id is empty");
		}

		CouchResponse resp = session.delete(name + "/" + urlEncodePath(d.getId()) + "?rev=" + d.getRev()
				+ (batch ? "&batch=ok" : ""));

		if (resp.isOk())
		{
			return true;
		}
		else
		{
			log.warn("Error deleting document - " + resp.getErrorId() + " " + resp.getErrorReason());
			return false;
		}

	}

	/**
	 * Gets attachment
	 * 
	 * @param id
	 * @param attachment
	 *            attachment body
	 * @return attachment body
	 * @throws JSONException
	 */
	public byte[] getAttachment(String id, String attachment) throws IOException, JSONException
	{
		CouchResponse resp = session.get(name + "/" + urlEncodePath(id) + "/" + attachment);
		return resp.getBodyBytes();
	}

	/**
	 * Puts attachment to the doc
	 * 
	 * @param id
	 * @param fname
	 *            attachment name
	 * @param ctype
	 *            content type
	 * @param attachment
	 *            attachment body
	 * @return was the delete successful?
	 * @throws JSONException
	 */
	public String putAttachment(String id, String fname, String ctype, InputStream attachment) throws IOException,
			JSONException
	{
		CouchResponse resp = session.put(name + "/" + urlEncodePath(id) + "/" + fname, ctype, attachment);
		return resp.getBody();
	}

	public boolean compact() throws JSONException
	{
		CouchResponse resp = session.post(name + "/_compact", "");
		return resp.isOk();
	}

	public boolean saveView(String string, String string2) throws JSONException
	{
		string2 = string2.replace("\"", "\\\"");
		CouchResponse resp = session
				.put(name + "/" + "_design/" + string, "{ \"_id\": \"_design/" + string
						+ "\", \"language\": \"javascript\",\"views\": {\"" + string + "\": {\"map\": \"" + string2
						+ "\"  }}}");
		return resp.isOk();
	}

}

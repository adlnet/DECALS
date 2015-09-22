package com.eduworks.cruncher.solr;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.lang.json.impl.EwJsonArray;
import com.eduworks.lang.util.EwJson;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;
import com.eduworks.resolver.Resolver;

public class CruncherSolrAddDocument extends Cruncher
{
	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		String solrURL = Resolver.decodeValue(optAsString("solrURL", "http%3A%2F%2Flocalhost%3A8983%2Fsolr%2F", c, parameters, dataStreams));
		HttpSolrServer solrServer;
		if (!SolrServer.serverMap.containsKey(solrURL)) {
			solrServer = new HttpSolrServer(solrURL);
			SolrServer.serverMap.put(solrURL, solrServer);
		} else 
			solrServer = SolrServer.serverMap.get(solrURL);
		
		JSONArray rawDocuments = getAsJsonArray("documents", c, parameters, dataStreams);
		
		Collection<SolrInputDocument> documentSet = new ArrayList<SolrInputDocument>();
		SolrInputDocument document;
		Object docValue;
		for (int rawDocumentIndex = 0; rawDocumentIndex < rawDocuments.length(); rawDocumentIndex++) {
			document = new SolrInputDocument();
			JSONObject currentRawDocument = (JSONObject) EwJson.tryParseJson(rawDocuments.get(rawDocumentIndex), false);
			for (Iterator<String> rawDocumentKeys = currentRawDocument.keys(); rawDocumentKeys.hasNext();) {
				String rawDocumentKey = rawDocumentKeys.next();
				docValue = currentRawDocument.get(rawDocumentKey);
				//T. Buskirk: added check here for mutlivalued fields...otherwise arrays are just getting inserted as strings.  2/6/2015
				if (docValue instanceof EwJsonArray) setMutlivaluedField(document,rawDocumentKey,(EwJsonArray)docValue);
				else document.setField(rawDocumentKey,docValue);
			}
			documentSet.add(document);
		}
		
		try {
			if (documentSet.size()!=0) {
				solrServer.add(documentSet);
				solrServer.commit();
			}
		} catch (SolrServerException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		return documentSet.size();
	}
	
	private void setMutlivaluedField(SolrInputDocument document, String docKey, EwJsonArray ja) throws JSONException {
	   if (ja.length() <= 0) return;
	   document.setField(docKey,ja.get(0));
	   for (int i=1;i<ja.length();i++) document.addField(docKey,ja.get(i));
	}
	
	@Override
	public String getDescription()
	{
		return "Takes an array of json objects that represents documents to add to the index. Each key with the json object translates to a field with the same value."
				+ "*_i = integer, *_b = boolean, *_l = long, *_f = float, *_s = string(use for exact representation), *_t = text(parsed by solr and normalized, _txt is multiple values), "
				+ "adding s allows for fields to be multivalued (*_is). Returns the number of documents added";
	}

	@Override
	public String getReturn()
	{
		return "int";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("documents", "JSONArray", "solrURL", "String");
	}
}

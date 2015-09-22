package com.eduworks.cruncher.solr;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.ConcurrentUpdateSolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.lang.json.impl.EwJsonArray;
import com.eduworks.lang.threading.EwThreading;
import com.eduworks.lang.util.EwJson;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;
import com.eduworks.resolver.Resolver;
import com.sun.corba.se.spi.orbutil.threadpool.ThreadPool;

public class CruncherSolrUpdate extends Cruncher
{
	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		String solrURL = Resolver.decodeValue(optAsString("solrURL", "http%3A%2F%2Flocalhost%3A8983%2Fsolr%2F", c, parameters, dataStreams));
		
		ConcurrentUpdateSolrServer solrServer;
		if (!SolrServer.updateServerMap.containsKey(solrURL)) {
			solrServer = new ConcurrentUpdateSolrServer(solrURL, 100, EwThreading.threads);
			SolrServer.updateServerMap.put(solrURL, solrServer);
		} else 
			solrServer = SolrServer.updateServerMap.get(solrURL);
		
		SolrInputDocument document = new SolrInputDocument();
		
		for(String fieldName : keySet()){
			
			if (!(fieldName.equals("obj")||fieldName.equals("solrURL")||fieldName.startsWith("?"))) {
				if(fieldName.equals("documentId")) {
					document.addField("id", getAsString(fieldName, c, parameters, dataStreams));
				} else {
					Map<String, Object> fieldMod = new HashMap<String, Object>();
					Object fieldObj = resolveAChild(fieldName, c, parameters, dataStreams);
					
					JSONObject operation;
					try{
						operation = (JSONObject)fieldObj;
						
						String op = operation.getString("op");
						Object val = operation.get("val");
						
						fieldMod.put(op, val);
						document.addField(fieldName, fieldMod);
					} catch(RuntimeException e) {
						
					}
				}
			}
		}
		
		UpdateResponse response;
		try {
			response = solrServer.add(document);
			solrServer.commit();
		} catch (SolrServerException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		return response;
	}
	
	@Override
	public String getDescription()
	{
		return "Accepts a solr query with rows and pages. Returns response in json format";
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
		return jo("solrURL", "String", "q", "String", "?pages", "Integer", "?rows", "Integer", "?fields", "JSONArray", 
		          "?returnFields", "JSONArray", "?idSort", "boolean", "?useCursor", "boolean", "?useMustMatchAll", "boolean",
		          "?start","Integer");
	}
}

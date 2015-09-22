package com.eduworks.cruncher.solr;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;
import com.eduworks.resolver.Resolver;

public class CruncherSolrClearIndex extends Cruncher
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
		
		
		UpdateResponse responseResult;
		try {
			responseResult = solrServer.deleteByQuery("*:*");
			solrServer.commit();
		} catch (SolrServerException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		return responseResult.getStatus();
	}

	@Override
	public String getDescription()
	{
		return "Wipes out all records stored in the solr index, returns status code 0 is no errors";
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
		return jo("solrURL", "String");
	}
}

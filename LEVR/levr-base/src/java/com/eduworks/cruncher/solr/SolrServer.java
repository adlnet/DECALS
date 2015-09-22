package com.eduworks.cruncher.solr;

import java.util.HashMap;

import org.apache.solr.client.solrj.impl.ConcurrentUpdateSolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;

public class SolrServer {
	public static HashMap<String, HttpSolrServer> serverMap = new HashMap<String, HttpSolrServer>();
	public static HashMap<String, ConcurrentUpdateSolrServer> updateServerMap = new HashMap<String, ConcurrentUpdateSolrServer>();
}

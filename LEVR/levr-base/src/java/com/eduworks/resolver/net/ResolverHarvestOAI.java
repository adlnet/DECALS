package com.eduworks.resolver.net;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import com.eduworks.lang.EwMap;
import com.eduworks.lang.util.EwJson;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Resolvable;
import com.eduworks.resolver.Resolver;
import com.eduworks.util.io.EwFileSystem;
import com.sun.org.apache.xerces.internal.util.URI;

public class ResolverHarvestOAI extends Resolver
{

	private static String	metadataPrefix	= "oai_dc";

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		metadataPrefix = optAsString("metadataPrefix",parameters);
		if (metadataPrefix == null || metadataPrefix.isEmpty())
			metadataPrefix = "oai_dc";
			
		String baseUrl = getAsString("baseUrl", parameters);
		String set = optAsString("set", parameters);
		String url = baseUrl + constructPhrase(set);
		//String all = "";
		if (!optAsString("resumptionToken",parameters).isEmpty())
		{
			url = baseUrl + "?verb=ListRecords"+
			(optAsBoolean("registry",false,parameters)?"&metadataPrefix=" + metadataPrefix:"")+
			"&resumptionToken=" + optAsString("resumptionToken",parameters);
			System.out.println(url);
		}
		File f;
		while (url != null)
		{
			try
			{
				f = EwFileSystem.downloadFile(url);
				url = null;
				SAXReader sr = new SAXReader();
				Document xml = sr.read(f);
				FileReader input = new FileReader(f);
				//all += IOUtils.toString(input);
				input.close();
				for (Object n : xPath(xml, "//oai:record").selectNodes(xml))
					resolveRecord((Node) n, c,parameters, dataStreams);
				for (Object n : xPath(xml, "//record").selectNodes(xml))
					resolveRecord((Node) n, c,parameters, dataStreams);
				url = createResumptionToken(xPath(xml, "//oai:resumptionToken").selectSingleNode(xml), baseUrl,parameters);
				
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			catch (DocumentException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for (String key : keySet())
			if (isSetting(key))
				remove(key);
		remove("op");
		return this;
	}

	private String constructPhrase(String set)
	{
		if (set == null || set.isEmpty())
			return "?verb=ListRecords&metadataPrefix=" + metadataPrefix;
		return "?verb=ListRecords&metadataPrefix=" + metadataPrefix + "&set=" + set;
	}

	private XPath xPath(Document xml, String query)
	{
		XPath xp = xml.createXPath(query);
		EwMap<String, String> namespaces = new EwMap<String, String>();
		namespaces.put("oai", "http://www.openarchives.org/OAI/2.0/");
		xp.setNamespaceURIs(namespaces);
		return xp;
	}

	private String createResumptionToken(Node selectSingleNode, String baseUrl, Map<String, String[]> parameters) throws JSONException
	{
		if (selectSingleNode == null)
			return null;
		String url = baseUrl + "?verb=ListRecords"+
		(optAsBoolean("registry",false,parameters)?"&metadataPrefix=" + metadataPrefix:"")+
		"&resumptionToken=" + selectSingleNode.getText();
		System.out.println(url);
		return url;
	}

	private void resolveRecord(Node item, Context c,Map<String, String[]> parameters, Map<String, InputStream> dataStreams)
	{
		resolveRecord(item.asXML(), c,parameters, dataStreams);
	}

	int	i	= 0;
	int	max	= 0;

	private void resolveRecord(String record, Context c,Map<String, String[]> parameters, Map<String, InputStream> dataStreams)
	{
		try
		{
			i++;
			if (i % 1000 == 0)
				System.out.println("ForEach: On " + i + "/" + max);
			JSONObject jsonObject = XML.toJSONObject(record);
			jsonObject = new JSONObject(jsonObject.toString().replaceAll("oai:", ""));
			jsonObject = new JSONObject(jsonObject.toString().replaceAll("dc:", ""));
			JSONObject recordObj = jsonObject.getJSONObject("record");
			if (!recordObj.has("metadata"))
				return;
			JSONObject header = recordObj.getJSONObject("header");
			if (header.has("setSpec"))
				if (!optAsString("setSpec", parameters).isEmpty())
					if (!getAsString("setSpec", parameters).equals(header.getString("setSpec")))
						if (!EwJson.contains(header.getJSONArray("setSpec"),getAsString("setSpec",parameters)))
							return;
			JSONObject dc = recordObj.getJSONObject("metadata");
			if (dc.has(metadataPrefix))dc = dc.getJSONObject(metadataPrefix);
			if (dc.has("nsdl_nsdl_dc"))dc = dc.getJSONObject("nsdl_nsdl_dc");
			JSONObject about = recordObj.optJSONObject("about");
			if (dc == null)
				return;
			String id = null;
			if (dc.has("identifier"))
				if (dc.get("identifier") instanceof JSONObject)
					id = dc.getJSONObject("identifier").getString("content");
				else if (dc.get("identifier") instanceof JSONArray)
					if (dc.getJSONArray("identifier").get(0) instanceof JSONObject)
						id = dc.getJSONArray("identifier").getJSONObject(0).getString("content");
					else
						id = dc.getJSONArray("identifier").getString(0);
				else if (dc.get("identifier") instanceof String)
					id = dc.getString("identifier");
			if (about != null)
				if (about.has("searchInfo"))
					if (about.get("searchInfo") instanceof JSONObject)
						if (about.getJSONObject("searchInfo").has("fullRecordLink"))
							if (about.getJSONObject("searchInfo").get("fullRecordLink") instanceof JSONObject)
								id = about.getJSONObject("searchInfo").getJSONObject("fullRecordLink")
										.getString("content");
							else if (about.getJSONObject("searchInfo").get("fullRecordLink") instanceof JSONArray)
								id = about.getJSONObject("searchInfo").getJSONArray("fullRecordLink").getString(0);
							else if (about.getJSONObject("searchInfo").get("fullRecordLink") instanceof String)
								id = about.getJSONObject("searchInfo").getString("fullRecordLink");
			if (optAsString("urlPrefix",parameters).isEmpty() == false && URI.isWellFormedAddress(id) == false)
				id = getAsString("urlPrefix",parameters) + id;
			if (header.optString("identifier") != null)
			{
				try
				{
					new URI(id);
				}
				catch(Exception e)
				{
					id = header.getString("identifier");
				}
			}
			id = healUrl(id);
			if (id == null)
				return;
			Resolvable operation = (Resolvable) get("op");
			
			try
			{
				final Resolvable thing = (Resolvable) operation.clone();
				try
				{
					final EwMap<String, String[]> newParams = new EwMap<String, String[]>(parameters);
					newParams.put("dc", new String[] { dc.toString() });
					newParams.put("documentId", new String[] { id });
					newParams.put("header", new String[] { header.toString() });
					if (recordObj.has("about"))
						newParams.put("about", new String[] { recordObj.get("about").toString() });
					try
					{
						Object o = thing.resolve(c, newParams, dataStreams);
						if (!optAsBoolean("memorySaver", true, parameters))
							put(id,o);
					}
					catch (JSONException e)
					{
						e.printStackTrace();
					}
				}
				finally
				{
				}
			}
			catch (CloneNotSupportedException e)
			{
				e.printStackTrace();
			}
		}
		catch (JSONException ex)
		{
			ex.printStackTrace();
		}
	}
	private String healUrl(String id)
	{
		try
		{
			URL url = new URL(id);
			return id;
		}
		catch (MalformedURLException ex)
		{
			String[] split = id.split(":");
			if (split.length != 2)
				return null;
			if (split[0].equals("pubmedcentral.nih.gov"))
				return "http://www."+split[0]+"/articlerender.fcgi?artid="+split[1];
		}
		return null;
	}
	@Override
	public String getDescription()
	{
		return "Harvests a OAI-PMH enabled repository and performs some operation with each record contained in the OAI" +
				"\nbaseUrl = URL of the OAI Endpoint." +
				"\nmetadataPrefix = Metadata format used by the repository" +
				"\n(Optional) set = Logical grouping of the results to retreive" +
				"\n(Optional) resumptionToken = Used to resume a query in progress" +
				"\n(Optional) urlPrefix = Used to prepend the identifier with a url, in case the registry assumes location independence" +
				"\n(Optional) memorySaver = Do not return results, and save memory in processing. (Defaults to true)" +
				"\nop = Operation to perform on each record. Parameters can be found in " +
				"\n\t@dc = Metadata record" +
				"\n\t@documentId = Identifier of the document (hopefully URL)" +
				"\n\t@header = Header";
	}
	@Override
	public String[] getResolverNames()
	{
		return new String[]{getResolverName(),"harvestOai"};
	}
	@Override
	public String getReturn()
	{
		return "JSONObject";
	}
	@Override
	public String getAttribution()
	{
		return ATTRIB_UCASTER;
	}
	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("baseUrl","String","metadataPrefix","String","op","Resolvable","?set","String","?resumptionToken","String","?urlPrefix","String","?memorySaver","Boolean");
	}

}

package com.eduworks.cruncher.parse;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities.EscapeMode;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherStripTags extends Cruncher
{
	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		String htmlText = (String)getObj(c, parameters, dataStreams);
		
		// Customize the whitelist
		Whitelist wl = Whitelist.simpleText();
		JSONArray allowTags = getAsJsonArray("allowTags", c, parameters, dataStreams);
		String[] tagList = new String[allowTags.length()+2];
		tagList[0] = "sub";
		tagList[1] = "sup";
		if (allowTags != null) {
			for (int i=0; i<allowTags.length(); i++) {
				tagList[i+2] = allowTags.getString(i);
				// For now, check for MathML tag and allow xmlns attribute on that tag if present
				if (tagList[i+2].equalsIgnoreCase("math"))
					wl = wl.addAttributes("math", "xmlns");
			}
			//log.debug("JSoup allowed tag list:" + wl.toString());
		}
		wl = wl.addTags(tagList);
		
		// Clean the text using the whitelist, and allow escaped characters
		Document doc = Jsoup.parse(htmlText);
		log.debug("JSoup parsed: " + doc.body().html());
		doc.outputSettings().charset("ISO-8859-1");
		doc.outputSettings().escapeMode(EscapeMode.xhtml);
		htmlText = Jsoup.clean(doc.body().html(), wl);
		log.debug("JSoup cleaned: " + htmlText);
		htmlText = StringEscapeUtils.unescapeHtml(htmlText);
		return htmlText;
	}

	@Override
	public String getDescription()
	{
		return "Strips any non-simple-text-manipulating HTML Tags from the input string.";
	}

	@Override
	public String getReturn()
	{
		return "String";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("obj","String", "allowTags", "JSONArray");
	}

}

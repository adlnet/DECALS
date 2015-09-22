package com.eduworks.cruncher.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;
import com.eduworks.util.io.InMemoryFile;

public class CruncherDisplayXML extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		InMemoryFile f = new InMemoryFile();
		
		try {
			docBuilder = factory.newDocumentBuilder();
			
			String str = getAsString("obj", c, parameters, dataStreams);
			
			StringReader sr = new StringReader(str);
			
			InputSource src = new InputSource(sr);
			
			Document doc = docBuilder.parse(src);
			
			OutputFormat format = new OutputFormat(doc); //document is an instance of org.w3c.dom.Document
			format.setLineWidth(65);
			format.setIndenting(true);
			format.setIndent(2);
			StringWriter out = new StringWriter();
			XMLSerializer serializer = new XMLSerializer(out, format);
			serializer.serialize(doc);

			String formattedXML = out.toString();
			
			f.data = out.toString().getBytes();
		
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		f.mime = "text/xml";
		f.name = getAsString("name", c,parameters, dataStreams);
		return f;
	}

	@Override
	public String getDescription()
	{
		return "Returns a file of type text/plain using 'obj' as the contents and 'name' as the filename.";
	}

	@Override
	public String getReturn()
	{
		return "ResolverFile";
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("obj","String","name","String");
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}

}

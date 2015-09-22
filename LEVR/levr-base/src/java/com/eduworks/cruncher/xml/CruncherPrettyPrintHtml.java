package com.eduworks.cruncher.xml;

import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;

import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherPrettyPrintHtml extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		try
		{
			SAXParserFactory factory = SAXParserFactory.newInstance();
			factory.setNamespaceAware(false);
			factory.setValidating(false);
			XMLReader reader = factory.newSAXParser().getXMLReader();
			String asString = getAsString("obj", c,
					parameters, dataStreams);
			Source xmlInput = new SAXSource(reader, new InputSource(new StringReader(asString)));

			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			StringWriter stringWriter = new StringWriter();
			StreamResult xmlPretty = new StreamResult(stringWriter);
			transformer.transform(xmlInput, xmlPretty);
			return xmlPretty.getWriter().toString();
		}
		catch (SAXException e)
		{
			throw new RuntimeException(e);
		}
		catch (TransformerException e)
		{
			throw new RuntimeException(e);
		}
		catch (ParserConfigurationException e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	public String getDescription() {
		return "Ingests HTML and pretty-prints it for output.";
	}

	@Override
	public String getReturn() {
		return "String";
	}

	@Override
	public String getAttribution() {
		return ATTRIB_NONE;
	}
	@Override
	public JSONObject getParameters() throws JSONException {
		return jo("obj","String");
	}

}

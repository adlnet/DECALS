package com.eduworks.cruncher.ontology;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.ontology.Ontology;
import com.eduworks.ontology.OntologyProperty;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Resolver;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.ReadWrite;
import com.hp.hpl.jena.shared.ClosedException;

public class CruncherOntologyUpdateProperty extends CruncherOntology
{

	@SuppressWarnings("unused")
	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{

		String directory = Resolver.decodeValue(optAsString("directory", "", c, parameters, dataStreams));

		String ontologyId = Resolver.decodeValue(optAsString("ontologyId", "", c, parameters, dataStreams));

		String propertyId = Resolver.decodeValue(optAsString("propertyId", "", c, parameters, dataStreams));

		JSONObject vals = new JSONObject(Resolver.decodeValue(optAsString("vals", "{}", c, parameters, dataStreams)));

		Ontology o = null;
		OntologyProperty prop = null;
		JSONObject jsonRepresentation = null;
		Dataset tdbDataset = getDataSet(directory,ReadWrite.WRITE,c);
	
		try
		{
			try
			{
				o = getOntology(ontologyId, tdbDataset, c);
			}
			catch (ClosedException e)
			{
				clearContextData(c);
				return resolve(c, parameters, dataStreams);
			}

			prop = o.getProperty(propertyId);

			prop.update(vals);

			if (prop != null)
			{
				jsonRepresentation = prop.getJSONRepresentation();
			}

		}
		catch (RuntimeException e)
		{

			throw e;
		}
		finally
		{
			if (o != null)
				o.close(false);
		}

		return jsonRepresentation;
	}

	@Override
	public String getDescription()
	{
		return "Updates the property specified by propertyId with the values passed in the vals parameter. "
				+ "Vals could contain domain, range, subclassof, or property flags such as reflexive, tranistive etc.";
	}

	@Override
	public String getReturn()
	{
		return "Object";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("ontologyId", "string", "directory", "path string", "propertyId", "string", "vals", "object");
	}

}

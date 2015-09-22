package com.eduworks.cruncher.ontology;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.ontology.Ontology;
import com.eduworks.ontology.OntologyInstance;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Resolver;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.ReadWrite;
import com.hp.hpl.jena.shared.ClosedException;

public class CruncherOntologyCreateInstance extends CruncherOntology
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{

		String ontologyId = Resolver.decodeValue(optAsString("ontologyId", "", c, parameters, dataStreams));

		String classId = Resolver.decodeValue(optAsString("classId", "", c, parameters, dataStreams));

		String directory = Resolver.decodeValue(optAsString("directory", "", c, parameters, dataStreams));

		JSONObject vals = new JSONObject(Resolver.decodeValue(optAsString("vals", "{}", c, parameters, dataStreams)));

		JSONObject ret = null;
		Ontology o = null;
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
				return resolve(c,parameters, dataStreams);
			}

			OntologyInstance instance = o.createInstance(classId, vals);

			ret = new JSONObject();

			ret.put(instance.getId(), instance.getJSONRepresentation());

		}
		catch (RuntimeException e)
		{

			throw e;
		}
		finally
		{
			tdbDataset.commit();
			
			if (o != null)
				o.close(false);
		}
		
		return ret;
	}

	@Override
	public String getDescription()
	{
		return "Creates a new instance of the class specified by classId, vals is a map from propertyIds to property values for the new instance";
	}

	@Override
	public String getReturn()
	{
		return "Object representing the new instance";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("ontologyId", "string", "directory", "path string", "classId", "string", "vals", "Object");
	}

}

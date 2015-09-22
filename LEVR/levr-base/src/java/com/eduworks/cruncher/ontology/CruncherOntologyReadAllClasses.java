package com.eduworks.cruncher.ontology;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.ontology.Ontology;
import com.eduworks.ontology.OntologyClass;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Resolver;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.ReadWrite;
import com.hp.hpl.jena.shared.ClosedException;

public class CruncherOntologyReadAllClasses extends CruncherOntology
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{

		String directory = Resolver.decodeValue(optAsString("directory", "", c, parameters, dataStreams));

		String ontologyId = Resolver.decodeValue(optAsString("ontologyId", "", c, parameters, dataStreams));

		Ontology o = null;
		JSONObject allClassObj = new JSONObject();
		Dataset tdbDataset = getDataSet(directory, ReadWrite.READ, c);

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

			Map<String, OntologyClass> allClasses = o.getAllClasses();

			for (String k : allClasses.keySet())
			{
				allClassObj.put(k, allClasses.get(k).getJSONRepresentation());
			}
		}
		finally
		{
			if (o != null)
				o.close(true);

		}

		return allClassObj;
	}

	@Override
	public String getDescription()
	{
		return "Returns an map from classId to class representation object for every class in the ontology specified";
	}

	@Override
	public String getReturn()
	{
		return "object";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("ontologyId", "string", "directory", "path string");
	}

}

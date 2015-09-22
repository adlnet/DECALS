package com.eduworks.cruncher.ontology;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.ontology.Ontology;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Resolver;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.ReadWrite;
import com.hp.hpl.jena.shared.ClosedException;

public class CruncherOntologyQuery extends CruncherOntology
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{

		String ontologyId = Resolver.decodeValue(optAsString("ontologyId", "", c, parameters, dataStreams));

		String directory = Resolver.decodeValue(optAsString("directory", "", c, parameters, dataStreams));

		String query = Resolver.decodeValue(optAsString("query", "", c, parameters, dataStreams));

		boolean local = optAsBoolean("local", false, c, parameters, dataStreams);

		Ontology o = null;
		JSONArray results = new JSONArray();
		Dataset tdbDataset = getDataSet(directory,ReadWrite.READ,c);

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

			JSONObject queryReturn = o.query(query, local);

			if (queryReturn.has("result"))
			{
				results = queryReturn.getJSONArray("result");
			}
		}
		finally
		{
			if (o != null)
				o.close(true);

		}
		
		return results;

	}

	@Override
	public String getDescription()
	{
		return "Runs the SPARQL query given on the ontology specified";
	}

	@Override
	public String getReturn()
	{
		return "Array of objects, each a result to the query with values that match the variables in the query";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("ontologyId", "string", "directory", "path string", "query", "sparql query string");
	}

}

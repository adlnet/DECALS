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

public class CruncherOntologyReadInstance extends CruncherOntology
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{

		String ontologyId = Resolver.decodeValue(optAsString("ontologyId", "", c, parameters, dataStreams));

		String instanceId = Resolver.decodeValue(optAsString("instanceId", "", c, parameters, dataStreams));

		String directory = Resolver.decodeValue(optAsString("directory", "", c, parameters, dataStreams));

		Ontology o = null;
		JSONObject result = new JSONObject();
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

			if (instanceId.startsWith("[") && instanceId.endsWith("]"))
			{
				JSONArray ids = new JSONArray(instanceId);

				for (int i = 0; i < ids.length(); i++)
				{
					String id = ids.optString(i);
					result.put(id, o.getInstance(id).getJSONRepresentation());
				}
			}
			else
			{
				result.put(instanceId, o.getInstance(instanceId).getJSONRepresentation());
			}
		}
		finally
		{
			if (o != null)
				o.close(true);

		}

		return result;
	}

	@Override
	public String getDescription()
	{
		return "Returns the representing object for the instance specified by instanceId";
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
		return jo("ontologyId", "string", "directory", "path string", "instanceId", "string");
	}

}

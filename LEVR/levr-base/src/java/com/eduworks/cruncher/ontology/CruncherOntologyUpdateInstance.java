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

public class CruncherOntologyUpdateInstance extends CruncherOntology
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{

		String ontologyId = Resolver.decodeValue(optAsString("ontologyId", "", c, parameters, dataStreams));

		String instanceId = Resolver.decodeValue(optAsString("instanceId", "", c, parameters, dataStreams));

		String directory = Resolver.decodeValue(optAsString("directory", "", c, parameters, dataStreams));

		JSONObject vals = new JSONObject(Resolver.decodeValue(optAsString("vals", "{}", c, parameters, dataStreams)));

		Ontology o = null;
		Dataset tdbDataset = getDataSet(directory, ReadWrite.WRITE, c);

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

			OntologyInstance ins = o.getInstance(instanceId, true);

			ins.update(vals);

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

		return true;
	}

	@Override
	public String getDescription()
	{
		return "Updates the instance specified by instanceId with the values passed in the vals parameter. Vals is a map from propertyId to property values for this instance";
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
		return jo("ontologyId", "string", "directory", "path string", "instanceId", "string", "vals", "object");
	}

}

package com.eduworks.cruncher.ontology;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.ontology.Ontology;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Resolver;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.ReadWrite;
import com.hp.hpl.jena.shared.ClosedException;

public class CruncherOntologyCreate extends CruncherOntology
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{

		String ontologyId = Resolver.decodeValue(optAsString("ontologyId", "", c, parameters, dataStreams));

		String directory = Resolver.decodeValue(optAsString("directory", "", c, parameters, dataStreams));

		Ontology o = null;
		JSONObject ret = null;
		Dataset tdbDataset = getDataSet(directory,ReadWrite.WRITE,c);
		
		try
		{
			try
			{
				o = Ontology.createOntology(tdbDataset,ontologyId);
			}
			catch (ClosedException e)
			{
				clearContextData(c);
				return resolve(c,parameters, dataStreams);
			}

			ret = new JSONObject();

			ret.put("uri", o.getId());
			ret.put("directory", directory);
			ret.put("ontologyId", ontologyId);

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
		
		return ret;
	}

	@Override
	public String getDescription()
	{
		return "creates a new ontology in the local directory specified with the id specified";
	}

	@Override
	public String getReturn()
	{
		return "Object {uri, directory, ontologyId, filename}";
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

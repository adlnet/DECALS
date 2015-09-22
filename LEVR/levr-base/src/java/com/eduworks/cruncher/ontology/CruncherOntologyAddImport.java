package com.eduworks.cruncher.ontology;

import java.io.InputStream;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.ontology.Ontology;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Resolver;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.ReadWrite;
import com.hp.hpl.jena.shared.ClosedException;

public class CruncherOntologyAddImport extends CruncherOntology
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{

		String ontologyId = Resolver.decodeValue(optAsString("ontologyId", "", c, parameters, dataStreams));

		String directory = Resolver.decodeValue(optAsString("directory", "", c, parameters, dataStreams));

		String importId = Resolver.decodeValue(optAsString("importId", "", c, parameters, dataStreams));

		Ontology o = null;

		JSONArray importedIds = new JSONArray();
		Dataset tdbDataset = getDataSet(directory, ReadWrite.WRITE,c);
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
			o.addOntology(tdbDataset, importId);

			o.save(tdbDataset);

			Set<String> ids = o.listImportedOntologies();

			for (String id : ids)
			{
				importedIds.put(id);
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

		if (importedIds.length() > 0)
		{
			return importedIds;
		}
		else
		{
			return false;
		}
	}

	@Override
	public String getDescription()
	{
		return "adds an import statement to the ontology specified by ontologyId to import the ontology specified with importId";
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
		return jo("ontologyId", "string", "directory", "path string", "importId", "string");
	}

}

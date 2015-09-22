package com.eduworks.cruncher.ontology;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.ontology.Ontology;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Resolver;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.ReadWrite;

public class CruncherOntologyTDBLoader extends CruncherOntology
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		String inputDirectory = Resolver.decodeValue(optAsString("input", "", c, parameters, dataStreams));

		String extension = Resolver.decodeValue(optAsString("extension", "", c, parameters, dataStreams));

		String tdbDirectory = Resolver.decodeValue(optAsString("tdbDirectory", "", c, parameters, dataStreams));

		String identifier = Resolver.decodeValue(optAsString("identifier", "", c, parameters, dataStreams));

		Dataset tdbDataset = getDataSet(tdbDirectory,ReadWrite.WRITE,c);

		try
		{
			File input = new File(inputDirectory);

			if (input.isDirectory())
			{
				File[] files = input.listFiles();

				for (File file : files)
				{
					if (file.getName().endsWith(extension))
					{
						identifier = file.getName().substring(0, file.getName().length() - extension.length());

						Ontology.importToTDB(tdbDataset, file.getPath().replace(File.separatorChar, '/'), identifier);
					}
				}
			}
			else
			{
				if (!identifier.isEmpty())
				{
					Ontology.importToTDB(tdbDataset, input.getPath().replace(File.separatorChar, '/'), identifier);
				}
				else
				{
					Ontology.importToTDB(tdbDataset, input.getPath().replace(File.separatorChar, '/'),
							input.getName().substring(0, input.getName().indexOf(".")));
				}
			}
		}
		finally
		{
		}

		return true;
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

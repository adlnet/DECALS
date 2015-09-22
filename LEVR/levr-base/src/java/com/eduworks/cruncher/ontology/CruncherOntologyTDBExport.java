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

public class CruncherOntologyTDBExport extends CruncherOntology
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		String exportDirectory = Resolver.decodeValue(optAsString("exportDirectory", "", c, parameters, dataStreams));

		String tdbDirectory = Resolver.decodeValue(optAsString("tdbDirectory", "", c, parameters, dataStreams));

		String identifier = Resolver.decodeValue(optAsString("identifier", "", c, parameters, dataStreams));
		
		String extension = Resolver.decodeValue(optAsString("extension", "", c, parameters, dataStreams));

		Dataset tdbDataset = getDataSet(tdbDirectory,ReadWrite.READ,c);

		try{
			Ontology.exportFromTDB(tdbDataset,exportDirectory, identifier, extension);
		}finally{
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

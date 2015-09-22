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


public class CruncherOntologyGetShortestPath extends CruncherOntology {

	@Override
	public Object resolve(Context c,
			Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException {
		
		String directory = Resolver.decodeValue(optAsString("directory","", c,parameters, dataStreams));
		
		String ontologyId = Resolver.decodeValue(optAsString("ontologyId","", c,parameters, dataStreams));
		
		String start = Resolver.decodeValue(optAsString("startId","", c,parameters, dataStreams));
		
		String end = Resolver.decodeValue(optAsString("endId","", c,parameters, dataStreams));
		
		JSONArray pathRelationships = getAsJsonArray("path", c, parameters, dataStreams);
		
		Ontology o = null;
		JSONArray pathRepresentation = null;
		Dataset tdbDataset = getDataSet(directory,ReadWrite.READ,c);
		
		tdbDataset.begin(ReadWrite.READ);
		try
		{
			try{
				o = getOntology(ontologyId, tdbDataset, c);
			}catch(ClosedException e){
				clearContextData(c);
				return resolve(c,parameters, dataStreams);
			}
			
			pathRepresentation = o.findShortestPath(start, end, pathRelationships);
		}
		finally
		{
			if (o != null)
				o.close(true);
			
		}
			
		return pathRepresentation;
	}

	@Override
	public String getDescription() {
		return "Returns the object representation of the class specified";
	}

	@Override
	public String getReturn() {
		return "object";
	}

	@Override
	public String getAttribution() {
		return ATTRIB_NONE;
	}

	@Override
	public JSONObject getParameters() throws JSONException {
		return jo("ontologyId", "string", "directory", "path string", "classId", "string");
	}

}

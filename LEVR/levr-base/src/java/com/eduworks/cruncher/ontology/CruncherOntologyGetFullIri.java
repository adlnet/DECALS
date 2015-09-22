package com.eduworks.cruncher.ontology;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.ontology.Ontology;
import com.eduworks.ontology.OntologyWrapper;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Resolver;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.ReadWrite;
import com.hp.hpl.jena.shared.ClosedException;


public class CruncherOntologyGetFullIri extends CruncherOntology {

	// TODO: This probably needs to be reviewed before being used... not sure if it has the correct catches etc..
	
	@Override
	public Object resolve(Context c,
			Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException {
		
		String directory = Resolver.decodeValue(optAsString("directory","", c,parameters, dataStreams));
		
		String ontologyId = Resolver.decodeValue(optAsString("ontologyId","", c,parameters, dataStreams));
		
		String id = Resolver.decodeValue(optAsString("id","", c,parameters, dataStreams));

		Dataset tdbDataset = getDataSet(directory,ReadWrite.READ,c);
			
		Ontology o;
		try{
			o = getOntology(ontologyId, tdbDataset, c);
		}catch(ClosedException e){
			clearContextData(c);
			return resolve(c,parameters, dataStreams);
		}
		
		OntologyWrapper wrapper;
		
		try{
			wrapper = o.getProperty(id);
			
			String fullId = wrapper.getFullId();		
			
			o.close(true);

			return fullId;
		}catch(RuntimeException e){
			
		}
		
		try{
			wrapper = o.getClass(id);
			
			String fullId = wrapper.getFullId();		
			
			o.close(true);

			return fullId;
		}catch(RuntimeException e){
			
		}
		
		try{
			wrapper = o.getInstance(id);
			
			String fullId = wrapper.getFullId();		
			
			o.close(true);
			
			return fullId;
		}catch(RuntimeException e){
			
		}
		
		throw new RuntimeException("Could not Find Property, Class or Instance with the Id ("+id+") in the ontology: "+ontologyId);
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

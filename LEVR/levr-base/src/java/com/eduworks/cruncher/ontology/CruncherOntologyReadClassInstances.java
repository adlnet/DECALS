package com.eduworks.cruncher.ontology;

import java.io.InputStream;
import java.util.ConcurrentModificationException;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.ontology.Ontology;
import com.eduworks.ontology.OntologyClass;
import com.eduworks.ontology.OntologyInstance;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Resolver;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.ReadWrite;
import com.hp.hpl.jena.shared.ClosedException;

public class CruncherOntologyReadClassInstances extends CruncherOntology {

	@Override
	public Object resolve(Context c,
			Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException {
		
		String ontologyId = Resolver.decodeValue(optAsString("ontologyId","", c,parameters, dataStreams));
		
		String classId = Resolver.decodeValue(optAsString("classId","", c,parameters, dataStreams));
		
		String directory = Resolver.decodeValue(optAsString("directory","", c,parameters, dataStreams));
		
		boolean local = optAsBoolean("local", false, c, parameters, dataStreams);
		
		Ontology o = null;
		JSONObject all = new JSONObject();
		Dataset tdbDataset = getDataSet(directory,ReadWrite.READ,c);

		try
		{	
			
			try{
				o = getOntology(ontologyId, tdbDataset, c);
			}catch(ClosedException e){
				clearContextData(c);
				return resolve(c,parameters, dataStreams);
			}
			
			OntologyClass cls = o.getClass(classId);
			
			Map<String, OntologyInstance> instances = cls.getAllInstances(local);
			for(String k : instances.keySet()){
				
				if(local){
					if(o.getJenaModel().isInBaseModel(instances.get(k).getJenaIndividual())){
						all.put(k, instances.get(k).getJSONRepresentation());
					}
				}else{
					all.put(k, instances.get(k).getJSONRepresentation());
				}
			}
		}
		catch(ConcurrentModificationException e){
			e.printStackTrace();
			return resolve(c, parameters, dataStreams);
		}
		finally
		{
			if (o != null)
				o.close(true);
			
		}
		
		return all;
	}

	@Override
	public String getDescription() {
		return "Returns a map from instanceId to representing object for every instance that is in the specified class";
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

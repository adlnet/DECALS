package com.eduworks.cruncher.ontology;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.ontology.Ontology;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;
import com.eduworks.resolver.Resolver;


public class CruncherOntologyId extends Cruncher {

	@Override
	public Object resolve(Context c,
			Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException {
		
		String id = Resolver.decodeValue(optAsString("id","", c,parameters, dataStreams));
		
		if(!id.startsWith(Ontology.idCharacter)){
			return Ontology.idCharacter+id;
		}else{
			return id;
		}
	
	}

	@Override
	public String getDescription() {
		return "Converts the Id passed in to an ontologyId (@-sign at beginning)";
	}

	@Override
	public String getReturn() {
		return "string";
	}

	@Override
	public String getAttribution() {
		return ATTRIB_NONE;
	}

	@Override
	public JSONObject getParameters() throws JSONException {
		return jo("id", "string");
	}

}

package com.eduworks.ontology;

import org.json.JSONObject;

public abstract class OntologyWrapper {
	
	public static String idCharacter = ":";
	
	public abstract JSONObject getJSONRepresentation();
	
	/**
	 * Returns the Id of the OntologyObject (the part after the # in the IRI), should convert it to the @-format that can be sent across 
	 * web services and still be recognized as an ID
	 * @return 
	 */
	public abstract String getId();
	
	/**
	 * Returns the Full IRI of the OntologyObject
	 * @return
	 */
	public abstract String getFullId();
	
	protected static String getIdentifier(String fullId){
		if(fullId.contains("#")){
			return fullId.substring(fullId.indexOf('#')).replace("#", idCharacter);
		}else{ 
			return fullId;
		}
	}
	
	protected String getIdentifierString(String fullId){
		return fullId.substring(fullId.indexOf('#') + 1);
	}

}

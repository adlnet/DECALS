package com.eduworks.ontology.test;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

public class OntologyTestHarness {

	protected boolean compareObjects(JSONObject rep1, JSONObject rep2){
		
		if(rep1 != null && rep2 != null){
			Iterator<String> keys = rep1.keys();
			while(keys.hasNext()){
				String key = keys.next();
				
				Object o1, o2;
				
				if( (o1 = rep1.opt(key)) != null){
					if(o1 instanceof JSONArray){
						if((o2 = rep2.opt(key)) != null){
							if(o2 instanceof JSONArray){
								if(compareArrays((JSONArray)o1, (JSONArray)o2) == false){
									return false;
								}
							}
						}
					}else if(o1 instanceof JSONObject){
						if((o2 = rep2.opt(key)) != null){
							if(o2 instanceof JSONObject){
								if(compareObjects((JSONObject)o1, (JSONObject)o2) == false){
									return false;
								}
							}
						}
					}else{
						if( (o2 = rep2.opt(key)) != null){
							if(!o1.toString().equals(o2.toString())){
								return false;
							}
						}
					}
				}
				
				rep2.remove(key);
			}
			
			if(rep2.length() > 0){
				return false;
			}
		}
		
		return true;		
	}
	
	protected boolean compareArrays(JSONArray arr1, JSONArray arr2){
		
		if(arr1.length() == arr2.length()){
			for(int i = 0; i < arr1.length(); i++){
				String val = arr1.optString(i);
				
				if(val == null){
					return false;
				}
				
				if(!arr2.toString().contains(val)){
					return false;
				}
			}
		}
		
		return true;
	}
}

package com.eduworks.decals.ui.client.model;

import com.google.gwt.json.client.JSONObject;

public class ResourceParadata {
	
	public static final String RESOURCE_URL_KEY = "resourceUrl";
	public static final String VIEW_COUNT_KEY = "viewCount";
	public static final String COLLECTION_COUNT_KEY = "collectionCount";
	
	private String resourceUrl;
	
	private String viewCount;
	private String collectionCount;
	
	public ResourceParadata(JSONObject paradata) {
		if (paradata.containsKey(RESOURCE_URL_KEY)) resourceUrl = paradata.get(RESOURCE_URL_KEY).isString().stringValue();
     	
		if (paradata.containsKey(VIEW_COUNT_KEY)){	
			if(paradata.get(VIEW_COUNT_KEY).isString() != null){
				viewCount = paradata.get(VIEW_COUNT_KEY).isString().stringValue();
			}else if(paradata.get(VIEW_COUNT_KEY).isNumber() != null){
				viewCount = paradata.get(VIEW_COUNT_KEY).isNumber().toString();
			}else{
				viewCount = "0";
			}
		}
		
		if (paradata.containsKey(COLLECTION_COUNT_KEY)){	
			if(paradata.get(COLLECTION_COUNT_KEY).isString() != null){
				collectionCount = paradata.get(COLLECTION_COUNT_KEY).isString().stringValue();
			}else if(paradata.get(COLLECTION_COUNT_KEY).isNumber() != null){
				collectionCount = paradata.get(COLLECTION_COUNT_KEY).isNumber().toString();
			}else{
				collectionCount = "0";
			}
		}
		
	}
	
	public String getResourceUrl(){
		return resourceUrl;
	}
	
	public String getViewCount(){
		return viewCount;
	}
	
	public String getCollectionCount(){
		return collectionCount;
	}
}

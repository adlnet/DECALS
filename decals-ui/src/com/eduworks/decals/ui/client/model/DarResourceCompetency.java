package com.eduworks.decals.ui.client.model;

import java.util.ArrayList;

import org.json.JSONException;

import com.eduworks.decals.ui.client.util.DsUtil;
import com.eduworks.lang.json.impl.EwJsonArray;
import com.eduworks.lang.json.impl.EwJsonObject;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;


public class DarResourceCompetency {

	private String competencyUri;
	private String title;
	private String description;
	
	public DarResourceCompetency(String uri, String title, String description) {
		if(uri == null || uri.isEmpty()){
			throw new NullPointerException("Competency URI cannot be Empty");
		}else{
			competencyUri = uri;
		}
		
		if(title == null || title.isEmpty()){
			throw new NullPointerException("Competency Title cannot be Empty");
		}else{
			this.title = title;
		}
		
		this.description = description;
	}
	
	public String getCompetencyUri(){
		return competencyUri;
	}
	
	public String getTitle(){
		return title;
	}
	
	public String getDescription(){
		return description;
	}

	public static JSONArray buildJsonArrayFromCompetencyList(ArrayList<DarResourceCompetency> competencyList) {
		JSONArray ret = new JSONArray();
		
		if(competencyList != null && competencyList.size() > 0){
			for(int i = 0; i < competencyList.size(); i++){
				JSONObject obj = new JSONObject();
				
				obj.put("title", new JSONString(competencyList.get(i).getTitle()));
				obj.put("description", new JSONString(competencyList.get(i).getDescription()));
				obj.put("uri", new JSONString(competencyList.get(i).getCompetencyUri()));
				
				ret.set(i, obj);
			}
		}
		
		return ret;
	}

	public static ArrayList<DarResourceCompetency> buildCompetencyListSolrReturn(JSONArray array) {
		ArrayList<DarResourceCompetency> ret = new ArrayList<DarResourceCompetency>();
		
		if(array != null && array.size() > 0){
			for(int i = 0; i < array.size(); i++){
				JSONObject obj = DsUtil.parseJson(array.get(i).isString().stringValue());
				ret.add(new DarResourceCompetency(obj.get("uri").isString().stringValue(), obj.get("title").isString().stringValue(), obj.get("description").isString().stringValue()));
			}
		}
		
		return ret;
	}

}

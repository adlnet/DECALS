package com.eduworks.decals.ui.client;

import java.util.ArrayList;

import com.eduworks.decals.ui.client.api.DsESBApi;
import com.eduworks.decals.ui.client.pagebuilder.screen.enums.GRADE_LEVEL;
import com.eduworks.decals.ui.client.pagebuilder.screen.enums.LANGUAGE;
import com.eduworks.decals.ui.client.pagebuilder.screen.enums.RESOURCE_TYPE;
import com.eduworks.gwt.client.net.callback.ESBCallback;
import com.eduworks.gwt.client.net.callback.EventCallback;
import com.eduworks.gwt.client.net.packet.ESBPacket;
import com.google.gwt.core.client.Callback;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;

public class DsUserPreferences {
	public static String RESOURCE_TYPES_KEY = "resourceTypes";
	public static String GRADE_LEVELS_KEY = "gradeLevel";
	public static String LEARNING_OBJECTIVES_KEY = "learningObjectives";
	public static String LANGUAGE_KEY = "language";
	
	public ArrayList<RESOURCE_TYPE> resourceTypes = new ArrayList<RESOURCE_TYPE>();
	public ArrayList<GRADE_LEVEL> gradeLevels = new ArrayList<GRADE_LEVEL>();
	public ArrayList<LANGUAGE> languages = new ArrayList<LANGUAGE>();

	public ArrayList<String> learningObjectives = new ArrayList<String>();
	
	
	public static DsUserPreferences INSTANCE = new DsUserPreferences();
	
	public static DsUserPreferences getInstance(){
		return INSTANCE;
	}
	
	public boolean prefsAreLocal = false;
	
	public void pullDownPreferences(final Callback<ESBPacket, Throwable> callback){
		DsESBApi.decalsUserPreferences(new ESBCallback<ESBPacket>() {
			@Override
			public void onSuccess(ESBPacket esbPacket) {
				parsePreferences(esbPacket);
				callback.onSuccess(esbPacket);
			}
			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}
		});
	}
	
	public void parsePreferences(ESBPacket packet){
		JSONObject thing = null;
		
		if (packet.containsKey("contentStream")) 
			thing = JSONParser.parseStrict(packet.getContentString()).isObject();
		else
			thing = packet.isObject();
		
		if(thing == null){
			return;
		}else{
			if(thing.containsKey(RESOURCE_TYPES_KEY)){
				JSONArray types = thing.get(RESOURCE_TYPES_KEY).isArray();
				
				if(types != null){
					for(int i = 0; i < types.size(); i++){
						String type = types.get(i).isString().stringValue();
						DsUserPreferences.getInstance().resourceTypes.add(RESOURCE_TYPE.findSymbolType(type));
					}
				}
			}
			
			if(thing.containsKey(GRADE_LEVELS_KEY)){
				JSONArray grades = thing.get(GRADE_LEVELS_KEY).isArray();
				
				if(grades != null){
					for(int i = 0; i < grades.size(); i++){
						String grade = grades.get(i).isString().stringValue();
						DsUserPreferences.getInstance().gradeLevels.add(GRADE_LEVEL.findSymbolGrade(grade));
					}
				}
			}
			
			if(thing.containsKey(LEARNING_OBJECTIVES_KEY)){
				JSONArray objectives = thing.get(LEARNING_OBJECTIVES_KEY).isArray();
			
				if(objectives != null){
					for(int i = 0; i < objectives.size(); i++){
						DsUserPreferences.getInstance().learningObjectives.add(objectives.get(i).isString().stringValue());
					}
				}
			}
			
			if(thing.containsKey(LANGUAGE_KEY)){
				JSONArray langs = thing.get(LANGUAGE_KEY).isArray();
				
				if(langs != null){
					for(int i = 0; i < langs.size(); i++){
						DsUserPreferences.getInstance().languages.add(LANGUAGE.findSymbolLanguage(langs.get(i).isString().stringValue()));
					}
				}
			}
			
			DsUserPreferences.getInstance().prefsAreLocal = true;
		}
	}
	
	public void clearResourceTypes(){
		resourceTypes.clear();
	}
	
	public void clearLanguages(){
		languages.clear();
	}
	
	public void clearGradeLevels(){
		gradeLevels.clear();
	}
	
	public void savePreferencesToServer(ESBCallback<ESBPacket> preferencesSavedCallback){
		DsESBApi.decalsUpdateUserPreferences(resourceTypes, languages, gradeLevels, learningObjectives, preferencesSavedCallback);
		DsSession.getInstance().setCachedLrSearchHandler(null);
	}
	
	public void addDesiredCompetenciesToServer(ArrayList<String> newDesiredCompetencyIds, ESBCallback<ESBPacket> competenciesSavedCallback){
		DsESBApi.decalsAddDesiredCompetencies(newDesiredCompetencyIds, competenciesSavedCallback);
	}
	
}

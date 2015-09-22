package com.eduworks.decals.ui.client.pagebuilder.screen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import org.json.JSONException;

import com.google.gwt.user.client.Element;
import com.eduworks.gwt.client.net.callback.ESBCallback;
import com.eduworks.gwt.client.net.callback.EventCallback;
import com.eduworks.gwt.client.net.packet.ESBPacket;
import com.eduworks.gwt.client.pagebuilder.PageAssembler;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HTML; 
import com.google.gwt.user.client.ui.ListBox;
import com.eduworks.decals.ui.client.DsScreenDispatch;
import com.eduworks.decals.ui.client.DsSession;
import com.eduworks.decals.ui.client.DsUserPreferences;
import com.eduworks.decals.ui.client.api.DsESBApi;
import com.eduworks.decals.ui.client.pagebuilder.screen.enums.GRADE_LEVEL;
import com.eduworks.decals.ui.client.pagebuilder.screen.enums.LANGUAGE;
import com.eduworks.decals.ui.client.pagebuilder.screen.enums.RESOURCE_TYPE;

public class DsUserPreferencesScreen extends DecalsWithGroupMgmtScreen {

	
	
	private static final String RESOURCE_TYPE_SELECT_ID = "prefResourceType";
	private static final String LANGUAGE_SELECT_ID = "prefLang";
	private static final String GRADE_LEVEL_SELECT_ID = "prefGrade";
	
	private static final String OBJECTIVE_LIST_ID = "userObjectivesList";
	private static final String NO_OBJECTIVE_ITEM_ID = "noObjectivesItem";
	
	private static final String ADD_OBJECTIVE_INPUT_ID = "prefAddObjectiveInput";
	private static final String ADD_OBJECTIVE_BTN_ID = "addObjectiveBtn";
	
	private static final String COMPETENCY_LINK_ID = "prefCompetencyLink";
	
	private static final String HELD_COMPETENCY_LIST_ID = "heldCompetenciesList";
	private static final String NO_HELD_COMPETENCY_ITEM_ID = "noHeldCompetenciesItem";
	
	private static final String DESIRED_COMPETENCY_LIST_ID = "desiredCompetenciesList";
	private static final String NO_DESIRED_COMPETENCY_ITEM_ID = "noDesiredCompetenciesItem";
	
	private static final String COMPETENCY_ID_INPUT = "prefAddCompetencyId";
	private static final String ADD_COMPETENCY_INPUT_ID = "prefAddCompetencyInput";
	private static final String ADD_COMPETENCY_BTN_ID = "addCompetencyBtn";
	
	private static final String SEARCH_COMPETENCY_INPUT_ID = "searchCompetencyInput";
	private static final String SEARCH_COMPETENCY_BTN_ID = "searchCompetencyBtn";
	
	private static final String SEARCH_COMPETENCY_RESULTS_ID = "competencyResultsList";
	
	private static final String SAVE_PREFERENCES_BTN_ID = "savePreferencesBtn";
	private static final String PREFS_CHANGED_ALERT_ID = "prefChangedAlert";
	
	private static final String CONFIRM_SAVE_PREFERENCES_BTN_ID = "confirmSavePreferences";
	private static final String CANCEL_SAVE_PREFERENCES_BTN_ID = "confirmCancelPreferences";
	
	private static final String CONFIRM_CHANGES_MODAL = "modalConfirmCancel";
	private static final String COMPETENCY_SEARCH_MODAL = "modalCompetencySearch";
	
	private static final String CLEAR_RESOURCE_TYPE_BTN_ID = "clearResourceType";
	private static final String CLEAR_LANGUAGE_BTN_ID = "clearLanguage";
	private static final String CLEAR_GRADE_LEVEL_BTN_ID = "clearGrade";
	
	public static ArrayList<String> competencyIds = new ArrayList<String>();
	
	public static ArrayList<String> newDesiredCompetencyIds = new ArrayList<String>();
	
	private static HashMap<String, JSONObject> competencyCache = new HashMap<String, JSONObject>();
	
	public DsUserPreferencesScreen(ESBPacket packet) {
		DsUserPreferences.getInstance().resourceTypes = new ArrayList<RESOURCE_TYPE>();
		DsUserPreferences.getInstance().gradeLevels = new ArrayList<GRADE_LEVEL>();
		DsUserPreferences.getInstance().languages = new ArrayList<LANGUAGE>();
		DsUserPreferences.getInstance().learningObjectives = new ArrayList<String>();
		newDesiredCompetencyIds = new ArrayList<String>();
		
		prefsChanged = false;
		
		DsUserPreferences.getInstance().parsePreferences(packet);
		
		DsESBApi.decalsUserCompetencies(setupCompetenciesCallback);
		DsESBApi.decalsLearningObjectives(setupLearningObjectivesCallback);
	}
	
	@Override
	public void display() {
		
		validateSession();
		
		PageAssembler.ready(new HTML(getTemplates().getUserPreferencesPanel().getText()));
		PageAssembler.buildContents();
		
		UI.setupPreferences();
		setupPageHandlers(); 
		DOM.getElementById(COMPETENCY_LINK_ID + "1").setAttribute("href", DsSession.getInstance().getCompetencyManagerUrl() + "/home?competencySessionId="+DsSession.getUser().getCompetencySessionId());
		DOM.getElementById(COMPETENCY_LINK_ID + "2").setAttribute("href", DsSession.getInstance().getCompetencyManagerUrl() + "/home?competencySessionId="+DsSession.getUser().getCompetencySessionId());
	}

	private void setupPageHandlers(){
		PageAssembler.attachHandler(DOM.getElementById(RESOURCE_TYPE_SELECT_ID), Event.ONCHANGE, enableSaveCallback);
		PageAssembler.attachHandler(DOM.getElementById(LANGUAGE_SELECT_ID), Event.ONCHANGE, enableSaveCallback);
		PageAssembler.attachHandler(DOM.getElementById(GRADE_LEVEL_SELECT_ID), Event.ONCHANGE, enableSaveCallback);
		
		PageAssembler.attachHandler(DOM.getElementById(ADD_OBJECTIVE_INPUT_ID), Event.ONKEYPRESS, typingObjectiveCallback);
		PageAssembler.attachHandler(DOM.getElementById(ADD_OBJECTIVE_BTN_ID), Event.ONCLICK, addLearningObjectiveCallback);
		
		PageAssembler.attachHandler(DOM.getElementById(ADD_COMPETENCY_INPUT_ID), Event.ONCLICK | Event.ONFOCUS, openCompetencyModalCallback);
		PageAssembler.attachHandler(DOM.getElementById(ADD_COMPETENCY_BTN_ID), Event.ONCLICK, openCompetencyModalCallback);
		
		PageAssembler.attachHandler(DOM.getElementById(SEARCH_COMPETENCY_INPUT_ID), Event.ONKEYPRESS, keypressSearchCompetenciesCallback);
		PageAssembler.attachHandler(DOM.getElementById(SEARCH_COMPETENCY_BTN_ID), Event.ONCLICK, searchCompetenciesCallback);
		
		PageAssembler.attachHandler(DOM.getElementById(CLEAR_RESOURCE_TYPE_BTN_ID), Event.ONCLICK, clearResourceTypeCallback);
		PageAssembler.attachHandler(DOM.getElementById(CLEAR_LANGUAGE_BTN_ID), Event.ONCLICK, clearLanguageCallback);
		PageAssembler.attachHandler(DOM.getElementById(CLEAR_GRADE_LEVEL_BTN_ID), Event.ONCLICK, clearGradeLevelCallback);
		
	}
	
	private ESBCallback<ESBPacket> setupCompetenciesCallback = new ESBCallback<ESBPacket>() {
		@Override
		public void onFailure(Throwable caught) {}

		@Override
		public void onSuccess(ESBPacket packet) {
			JSONObject thing = null;
			
			if (packet.containsKey("contentStream")) 
				thing = JSONParser.parseStrict(packet.getContentString()).isObject();
			else
				thing = packet.isObject();
			
			if(thing == null){
				return;
			}else{
				UI.setupCompetencyLists(thing);
			}
		}
	};
	
	private ESBCallback<ESBPacket> setupLearningObjectivesCallback = new ESBCallback<ESBPacket>() {
		@Override
		public void onSuccess(ESBPacket esbPacket) {
			Element e = DOM.getElementById(ADD_OBJECTIVE_INPUT_ID);
			JSONArray objectives = esbPacket.getArray("obj");
			
			setupTypeahead(e, objectives);
		}
		
		@Override
		public void onFailure(Throwable caught) {}
	};
	
	private static boolean prefsChanged = false;
	
	private static void informChangesMade(){
		prefsChanged = true;
		
		UI.showSavePreferencesButton();
		
		PageAssembler.attachHandler(DOM.getElementById(SAVE_PREFERENCES_BTN_ID), Event.ONCLICK, savePreferencesCallback);
		
		PageAssembler.attachHandler(DOM.getElementById(CONFIRM_SAVE_PREFERENCES_BTN_ID), Event.ONCLICK, confirmSavePreferencesCallback);
		PageAssembler.attachHandler(DOM.getElementById(CANCEL_SAVE_PREFERENCES_BTN_ID), Event.ONCLICK, confirmCancelPreferencesCallback);
	}
	
	@Override
	public void lostFocus() {
		if(prefsChanged){
			PageAssembler.openPopup(CONFIRM_CHANGES_MODAL);
		}
	}
	
	private static class UI{
		
		/** Setup Preferences on Page Load **/
		
		public static void setupPreferences(){
			DsESBApi.decalsPreferenceTypes(new ESBCallback<ESBPacket>(){

				@Override
				public void onFailure(Throwable caught) {
					setupBasicPreferences();
				}
		
				@Override
				public void onSuccess(ESBPacket esbPacket) {
					setupOnlinePreferences(esbPacket);
				}
			   
			});
			
			for(String objective : DsUserPreferences.getInstance().learningObjectives){
				addLearningObjectiveToList(objective);
			}
		}
		
		public static void setupBasicPreferences(){
			
			
			setupBasicResourceTypeSelect();
			setupBasicGradeLevelSelect();
			
			String objectives = "";
			for(String obj : DsUserPreferences.getInstance().learningObjectives){
				objectives+="<li>"+obj+" <i class='fa fa-times'/></li>";
			}
			if(!objectives.isEmpty())
				DOM.getElementById("userObjectives").setInnerHTML(objectives);
		}
		
		public static void setupBasicResourceTypeSelect(){
			String resourceOptions = "";
			for(RESOURCE_TYPE type : RESOURCE_TYPE.values()){		
				if(DsUserPreferences.getInstance().resourceTypes.contains(type)){
					resourceOptions+="<option value='"+type.toSymbol()+"' selected='selected'>"+type.toString()+"</option>";
				}else{
					resourceOptions+="<option value='"+type.toSymbol()+"'>"+type.toString()+"</option>";
				}
			}
			DOM.getElementById("prefResourceType").setInnerHTML(resourceOptions);
		}
		
		public static void setupBasicGradeLevelSelect(){
			String gradeOptions = "";
			for(GRADE_LEVEL grade : GRADE_LEVEL.values()){
				if(DsUserPreferences.getInstance().gradeLevels.contains(grade)){
					gradeOptions+="<option value='"+grade.toSymbol()+"' selected='selected'>"+grade.toString()+"</option>";
				}else{
					gradeOptions+="<option value='"+grade.toSymbol()+"'>"+grade.toString()+"</option>";
				}
			}
			DOM.getElementById("prefGrade").setInnerHTML(gradeOptions);
		}
		
		public static void setupBasicLanguageSelect(){
			String languageOptions = "";
			for(LANGUAGE language : LANGUAGE.values()){
				if(language.equals(LANGUAGE.OTHER)){
					if(DsUserPreferences.getInstance().languages.contains(language)){
						languageOptions+="<option value='"+language.toSymbol()+"' selected='selected'>"+language.toString()+"</option>";
					}else{
						languageOptions+="<option value='"+language.toSymbol()+"'>"+language.toString()+"</option>";
					}
				}else{
					if(DsUserPreferences.getInstance().languages.contains(language)){
						languageOptions+="<option value='"+language.toSymbol()+"' selected='selected'>"+language.toString()+" ("+language.toSymbol()+")</option>";
					}else{
						languageOptions+="<option value='"+language.toSymbol()+"'>"+language.toString()+" ("+language.toSymbol()+")</option>";
					}
				}
			}
			DOM.getElementById("prefLang").setInnerHTML(languageOptions);
		}
		
		public static void setupOnlinePreferences(ESBPacket packet){
			String str = packet.getContentString();
			JSONObject thing = null;
			
			if (packet.containsKey("contentStream")) 
				thing = JSONParser.parseStrict(packet.getContentString()).isObject();
			else
				thing = packet.isObject();
			
			if(thing == null){
				setupPreferences();
			}else{
				if(thing.containsKey(DsUserPreferences.RESOURCE_TYPES_KEY)){
					JSONObject types = thing.get(DsUserPreferences.RESOURCE_TYPES_KEY).isObject();
					if(types != null){
						String resourceOptions = "";
						for(String key : types.keySet()){
							if(DsUserPreferences.getInstance().resourceTypes.contains(RESOURCE_TYPE.findSymbolType(key))){
								resourceOptions+="<option value='"+key+"' selected='selected'>"+types.get(key).isString().stringValue()+"</option>";
							}else{
								resourceOptions+="<option value='"+key+"'>"+types.get(key).isString().stringValue()+"</option>";
							}
							
							DOM.getElementById("prefResourceType").setInnerHTML(resourceOptions);
						}
					}else{
						setupBasicResourceTypeSelect();
					}
				}
				
				if(thing.containsKey(DsUserPreferences.GRADE_LEVELS_KEY)){
					JSONObject grades = thing.get(DsUserPreferences.GRADE_LEVELS_KEY).isObject();
					
					if(grades != null){
						String gradeOptions = "";
						for(String key : grades.keySet()){
							if(DsUserPreferences.getInstance().gradeLevels.contains(GRADE_LEVEL.findSymbolGrade(key))){
								gradeOptions+="<option value='"+key+"' selected='selected'>"+grades.get(key).isString().stringValue()+"</option>";
							}else{
								gradeOptions+="<option value='"+key+"'>"+grades.get(key).isString().stringValue()+"</option>";
							}
						}
						
						DOM.getElementById("prefGrade").setInnerHTML(gradeOptions);
					}else{
						setupBasicGradeLevelSelect();
					}
				}
				
				if(thing.containsKey(DsUserPreferences.LANGUAGE_KEY)){
					JSONArray possibleLanguages = thing.get(DsUserPreferences.LANGUAGE_KEY).isArray();
					
					if(possibleLanguages != null){
						boolean hasOther = false;
						
						String languageOptions = "";
						for(int i = 0; i < possibleLanguages.size(); i++){
							String key = possibleLanguages.get(i).isString().stringValue();
							LANGUAGE lang = LANGUAGE.findSymbolLanguage(key);
							
							if(lang != null){
								if(DsUserPreferences.getInstance().languages.contains(lang)){
									languageOptions+="<option value='"+key+"' selected='selected'>"+lang.toString()+" ("+lang.toSymbol()+")</option>";
								}else{
									languageOptions+="<option value='"+key+"'>"+lang.toString()+" ("+lang.toSymbol()+")</option>";
								}
							}
							
							if(lang.equals(LANGUAGE.OTHER))
								hasOther = true;
						}
						
						if(!hasOther){
							if(DsUserPreferences.getInstance().languages.contains(LANGUAGE.OTHER.toSymbol())){
								languageOptions += "<option value='"+LANGUAGE.OTHER.toSymbol()+"' selected='selected'>"+LANGUAGE.OTHER.toString()+"</option>";
							}else{
								languageOptions += "<option value='"+LANGUAGE.OTHER.toSymbol()+"' >"+LANGUAGE.OTHER.toString()+"</option>";
							}
						}
						
						DOM.getElementById("prefLang").setInnerHTML(languageOptions);
					}else{
						setupBasicLanguageSelect();
					}
				}
			}
		}
		
		public static void setupCompetencyLists(JSONObject competencyObj){
			if(competencyObj != null){
				DOM.getElementById("heldCompetenciesList").setInnerHTML("<li id='noHeldCompetenciesItem'><em>No Competencies Held</em></li>");
				DOM.getElementById("desiredCompetenciesList").setInnerHTML("<li id='noDesiredCompetenciesItem'><em>No Competencies Desired</em></li>");
				
				for(String id : competencyObj.keySet()){
					JSONObject competency = competencyObj.get(id).isObject();
					
					if(competency != null){
						String levelId = competency.get(":recordLevel").isArray().get(0).isString().stringValue();
						
						JSONObject levels = competency.get("modelLevels").isObject();
						if(levels != null){
							int maxRank = -1;
							String maxId = "";
							
							for(String key : levels.keySet()){
								try{
									int levelRank = Integer.parseInt(levels.get(key).isObject().get(":competencyLevelRank").isArray().get(0).isString().stringValue());
									if(levelRank > maxRank){
										maxRank = levelRank;
										maxId = key;
									}
								}catch(NumberFormatException e){	}
							}
	
							String competencyTitle = competency.get("competencyDetails").isObject().get(":competencyTitle").isArray().get(0).isString().stringValue();
							String competencyId = competency.get(":recordCompetency").isArray().get(0).isString().stringValue();
							
							if(levelId.equals(maxId)){
								addCompetencyToHeldList(competencyId, competencyTitle);
							}else{
								addCompetencyToDesiredList(competencyId, competencyTitle);
							}
							
							competencyCache.put(competencyId, competency.get("competencyDetails").isObject());
						}
					}
				}
				
				DOM.getElementById("findingCompetencies").addClassName("hidden");
				DOM.getElementById("competencyLists").removeClassName("hidden");
			}
		}
		
		/** Resource Type Methods **/
		
		public static void clearResourceTypeSelect(){
			SelectElement input = SelectElement.as(DOM.getElementById(RESOURCE_TYPE_SELECT_ID));
			
			input.setValue(null);
		}
		
		/** Language Methods **/
		
		public static void clearLanguageSelect(){
			SelectElement input = SelectElement.as(DOM.getElementById(LANGUAGE_SELECT_ID));
			
			input.setValue(null);
		}
		
		/** Grade Level Methods **/
		
		public static void clearGradeLevelSelect(){
			SelectElement input = SelectElement.as(DOM.getElementById(GRADE_LEVEL_SELECT_ID));
			
			input.setValue(null);
		}
		
		/** Competency Methods **/
		
		public static void addCompetencyToHeldList(String competencyId, String competencyTitle){
			
			competencyIds.add(competencyId);
			
			Element listItem = DOM.createElement("li");
			listItem.setInnerText(competencyTitle);
			
			DOM.getElementById(HELD_COMPETENCY_LIST_ID).appendChild(listItem);
			
			DOM.getElementById(NO_HELD_COMPETENCY_ITEM_ID).setClassName("hidden");
		}
		
		public static void addCompetencyToDesiredList(String competencyId, String competencyTitle){
			competencyIds.add(competencyId);
			
			Element listItem = DOM.createElement("li");
			listItem.setInnerText(competencyTitle);
			
			DOM.getElementById(DESIRED_COMPETENCY_LIST_ID).appendChild(listItem);
			
			DOM.getElementById(NO_DESIRED_COMPETENCY_ITEM_ID).setClassName("hidden");
		}
		

		/** Learning Objective Methods **/
		
		public static void addLearningObjectiveToList(String objective){
			Element listItem = DOM.createElement("li");
			Element span = DOM.createElement("span");
			span.setInnerText(objective);
			
			listItem.appendChild(span);
			
			Element removeBtn = DOM.createElement("i");
			removeBtn.addClassName("fa");
			removeBtn.addClassName("fa-times");
			listItem.appendChild(removeBtn);
			
			PageAssembler.attachHandler(removeBtn, Event.ONCLICK, removeLearningObjectiveCallback);
			
			DOM.getElementById(OBJECTIVE_LIST_ID).appendChild(listItem);
			
			DOM.getElementById(NO_OBJECTIVE_ITEM_ID).addClassName("hidden");	
		}
		
		public static void removeLearningObjectiveFromList(Element e){
			e.removeFromParent();
			
			if(DsUserPreferences.getInstance().learningObjectives.size() == 0)
				DOM.getElementById(NO_OBJECTIVE_ITEM_ID).removeClassName("hidden");	
		}
		
		
		
		public static void showSavePreferencesButton(){
			DOM.getElementById(SAVE_PREFERENCES_BTN_ID).removeClassName("hidden");
			DOM.getElementById(PREFS_CHANGED_ALERT_ID).removeClassName("hidden");
		}

		public static void hideSavePreferencesButton(){
			DOM.getElementById(SAVE_PREFERENCES_BTN_ID).addClassName("hidden");
			DOM.getElementById(PREFS_CHANGED_ALERT_ID).addClassName("hidden");
		}
		
		public static void closeSavePreferencesModal(){
			PageAssembler.closePopup(CONFIRM_CHANGES_MODAL);
		}
		
		
		
		public static void openCompetencyModal() {
			PageAssembler.openPopup(COMPETENCY_SEARCH_MODAL);
			InputElement.as(DOM.getElementById(ADD_COMPETENCY_INPUT_ID)).blur();
			PageAssembler.attachHandler(DOM.getElementById("addSelectedCompetency"), Event.ONCLICK, addCompetencyCallback);
			PageAssembler.attachHandler(DOM.getElementById("cancelAddCompetency"), Event.ONCLICK, closeCompetencyModalCallback);
		}
		
		public static void closeCompetencyModal(){
			PageAssembler.closePopup(COMPETENCY_SEARCH_MODAL);
		}
		
		public static void displayCompetencyResults(JSONObject thing) {
			JSONObject allModelInfo = thing.get("modelInfo").isObject();
			
			JSONObject results = thing.get("results").isObject();
			for(String modelId : results.keySet()){
				JSONObject competencies = results.get(modelId).isObject();
				JSONObject modelInfo = allModelInfo.get(modelId).isObject();
				
				DOM.getElementById(SEARCH_COMPETENCY_RESULTS_ID).setInnerHTML("");
				for(String competencyId : competencies.keySet()){
					JSONObject competency = competencies.get(competencyId).isObject();
					addCompetencySearchResult(competencyId, competency, modelInfo);
				}
			}
		}
		
		public static void addCompetencySearchResult(String competencyId, JSONObject competency, JSONObject modelInfo){
			competencyCache.put(competencyId, competency);
			
			String competencyTitle = competency.get(":competencyTitle").isArray().get(0).isString().stringValue();
			String modelTitle = modelInfo.get("name").isString().stringValue();
			String modelId = modelInfo.get("ontologyId").isString().stringValue();
			
			Element resultRow = DOM.createElement("div");	
			resultRow.addClassName("row");
			Element result = DOM.createElement("div");
			result.addClassName("large-12");
			result.addClassName("competencyResult");
			result.setInnerText(competencyTitle);
			result.setId(modelId + " " + competencyId);
			
			Element modelName = DOM.createElement("span");
			modelName.addClassName("modelInfo");
			modelName.setInnerText(modelTitle);
			result.appendChild(modelName);
			
			Element checkWrapper = DOM.createElement("span");
			checkWrapper.addClassName("checkWrapper");
			
			Element emptyCheck = DOM.createElement("i");
			emptyCheck.addClassName("fa");
			emptyCheck.addClassName("fa-square-o");
			checkWrapper.appendChild(emptyCheck);
			
			Element fullCheck = DOM.createElement("i");
			fullCheck.addClassName("fa");
			fullCheck.addClassName("fa-check-square-o");
			checkWrapper.appendChild(fullCheck);
			result.appendChild(checkWrapper);
			
			resultRow.appendChild(result);
			
			if(competencyIds.contains(competencyId)){
				result.addClassName("selected");
			}else{
				PageAssembler.attachHandler(resultRow, Event.ONCLICK, toggleSelectCallback);
			}
			
			
			Element resultsContainer = DOM.getElementById(SEARCH_COMPETENCY_RESULTS_ID);
			
			resultsContainer.appendChild(resultRow);
		}
	}
	
	/** Clear Resource Callbacks **/
	
	private EventCallback clearResourceTypeCallback = new EventCallback() {
		@Override
		public void onEvent(Event event) {
			informChangesMade();
			
			DsUserPreferences.getInstance().clearResourceTypes();
			
			UI.clearResourceTypeSelect();
			
			//UI.hideResourceTypeClearBtn();
		}
	};
	
	private EventCallback clearLanguageCallback = new EventCallback() {
		@Override
		public void onEvent(Event event) {
			informChangesMade();
			
			DsUserPreferences.getInstance().clearLanguages();
			
			UI.clearLanguageSelect();
			
			//UI.hideLanguageClearBtn();
		}
	};
	
	private EventCallback clearGradeLevelCallback = new EventCallback() {
		@Override
		public void onEvent(Event event) {
			informChangesMade();
			
			DsUserPreferences.getInstance().clearGradeLevels();
			
			UI.clearGradeLevelSelect();
			
			//UI.hideGradeLevelClearBtn();
		}
	};

	/** Save Preference Callbacks **/
	
	private EventCallback enableSaveCallback = new EventCallback(){
		@Override
		public void onEvent(com.google.gwt.user.client.Event event) {
			informChangesMade();
			
			ListBox resourceTypeSelect = ListBox.wrap(DOM.getElementById(RESOURCE_TYPE_SELECT_ID));
			ListBox gradeSelect = ListBox.wrap(DOM.getElementById(GRADE_LEVEL_SELECT_ID));
			ListBox langSelect = ListBox.wrap(DOM.getElementById(LANGUAGE_SELECT_ID));
			
			
			DsUserPreferences.getInstance().resourceTypes = new ArrayList<RESOURCE_TYPE>();
			for(int i = 0; i < resourceTypeSelect.getItemCount(); i++){
				if(resourceTypeSelect.isItemSelected(i)){
					DsUserPreferences.getInstance().resourceTypes.add(RESOURCE_TYPE.findSymbolType(resourceTypeSelect.getValue(i)));
				}
			}
			
			DsUserPreferences.getInstance().languages = new ArrayList<LANGUAGE>();
			for(int i = 0; i < langSelect.getItemCount(); i++){
				if(langSelect.isItemSelected(i)){
					DsUserPreferences.getInstance().languages.add(LANGUAGE.findSymbolLanguage(langSelect.getValue(i)));
				}
			}
			
			DsUserPreferences.getInstance().gradeLevels = new ArrayList<GRADE_LEVEL>();
			for(int i = 0; i < gradeSelect.getItemCount(); i++){
				if(gradeSelect.isItemSelected(i)){
					DsUserPreferences.getInstance().gradeLevels.add(GRADE_LEVEL.findSymbolGrade(gradeSelect.getValue(i)));
				}
			}
			
			PageAssembler.attachHandler(DOM.getElementById(RESOURCE_TYPE_SELECT_ID), Event.ONCHANGE, enableSaveCallback);
			PageAssembler.attachHandler(DOM.getElementById(LANGUAGE_SELECT_ID), Event.ONCHANGE, enableSaveCallback);
			PageAssembler.attachHandler(DOM.getElementById(GRADE_LEVEL_SELECT_ID), Event.ONCHANGE, enableSaveCallback);
			PageAssembler.attachHandler(DOM.getElementById(ADD_OBJECTIVE_BTN_ID), Event.ONCLICK, enableSaveCallback);
			PageAssembler.attachHandler(DOM.getElementById(ADD_COMPETENCY_BTN_ID), Event.ONCLICK, enableSaveCallback);
		}
	};
	
	private static EventCallback savePreferencesCallback = new EventCallback() {
		@Override
		public void onEvent(com.google.gwt.user.client.Event event) {
			DsUserPreferences.getInstance().savePreferencesToServer(preferencesSavedCallback);
			DsUserPreferences.getInstance().addDesiredCompetenciesToServer(newDesiredCompetencyIds, competenciesSavedCallback);
		
			prefsChanged = false;
			
			UI.hideSavePreferencesButton();
		}
	};
	
	public static boolean prefsSaved = false;
	public static boolean competenciesSaved = false;
	
	private static ESBCallback<ESBPacket> preferencesSavedCallback = new ESBCallback<ESBPacket>() {
		@Override
		public void onFailure(Throwable caught) {
			
		}

		@Override
		public void onSuccess(ESBPacket esbPacket) {
			if(competenciesSaved){
				UI.hideSavePreferencesButton();
				competenciesSaved = false;
			}else{
				prefsSaved = true;
			}
		}
	};
	
	private static ESBCallback<ESBPacket> competenciesSavedCallback = new ESBCallback<ESBPacket>() {
		@Override
		public void onFailure(Throwable caught) {

		}

		@Override
		public void onSuccess(ESBPacket esbPacket) {
			if(competenciesSaved){
				UI.hideSavePreferencesButton();
				competenciesSaved = false;
			}else{
				prefsSaved = true;
			}
		}
	};
	
	
	/** Save Preferences Modal Callbacks **/
	
	private static EventCallback confirmSavePreferencesCallback = new EventCallback() {
		@Override
		public void onEvent(com.google.gwt.user.client.Event event) {
			DsESBApi.decalsUpdateUserPreferences(DsUserPreferences.getInstance().resourceTypes, DsUserPreferences.getInstance().languages, DsUserPreferences.getInstance().gradeLevels, DsUserPreferences.getInstance().learningObjectives, preferencesSavedCallback);
			DsESBApi.decalsAddDesiredCompetencies(newDesiredCompetencyIds, competenciesSavedCallback);
			
			UI.closeSavePreferencesModal();
		}
	};
	
	private static EventCallback confirmCancelPreferencesCallback = new EventCallback() {
		@Override
		public void onEvent(com.google.gwt.user.client.Event event) {
			UI.closeSavePreferencesModal();
		}
	};
	
	/** Learning Objective Callbacks **/
	
	private EventCallback addLearningObjectiveCallback = new EventCallback() {
		@Override
		public void onEvent(Event event) {
			InputElement input = InputElement.as(DOM.getElementById(ADD_OBJECTIVE_INPUT_ID));
			String learningObjective = input.getValue();
			
			if(!learningObjective.isEmpty()){
				informChangesMade();
				
				DsUserPreferences.getInstance().learningObjectives.add(learningObjective);
				
				UI.addLearningObjectiveToList(learningObjective);
			}
			
			input.setValue("");
		}
	};
	
	private static EventCallback removeLearningObjectiveCallback = new EventCallback() {
		@Override
		public void onEvent(Event event) {
			informChangesMade();
			
			Element removeBtn = (Element) event.getTarget();
			Element e = removeBtn;
			
			while(!e.getTagName().equalsIgnoreCase("li")){
				e = (Element) e.getParentElement();
			}
			
			String oldObjective = e.getInnerText();
			
			DsUserPreferences.getInstance().learningObjectives.remove(oldObjective);
			
			UI.removeLearningObjectiveFromList(e);
			
			
		}
	};
	
	private EventCallback typingObjectiveCallback = new EventCallback() {
		@Override
		public void onEvent(Event event) {
			if(event.getKeyCode() == 13){
				informChangesMade();
				
				String learningObjective = InputElement.as(DOM.getElementById(ADD_OBJECTIVE_INPUT_ID)).getValue();
				
				DsUserPreferences.getInstance().learningObjectives.add(learningObjective);
				
				UI.addLearningObjectiveToList(learningObjective);
				
				clearTypeaheadValue();
			}else{
				scrollToBottom();
			}
		}
	};
	
	/** Competency Modal Callbacks **/
	
	private static ArrayList<String> selectedCompetencies = new ArrayList<String>();
	
	private EventCallback openCompetencyModalCallback = new EventCallback() {
		@Override
		public void onEvent(Event event) {
			UI.openCompetencyModal();
			DOM.getElementById("searchCompetencyInput").focus();
		}
	};
	
	private EventCallback searchCompetenciesCallback = new EventCallback(){
		@Override
		public void onEvent(Event event) {
			searchCompetencies();
		}
		
	};
	
	private EventCallback keypressSearchCompetenciesCallback = new EventCallback(){
		@Override
		public void onEvent(Event event) {
			if(event.getKeyCode() == 13){
				searchCompetencies();
			}
		}
	}; 
	
	private static EventCallback toggleSelectCallback = new EventCallback(){
		@Override
		public void onEvent(Event event) {
			com.google.gwt.dom.client.Element e = event.getTarget();
			while(!e.getClassName().contains("competencyResult")){
				e = e.getParentElement();
			}
			
			e.toggleClassName("selected");
			
			String competencyId = e.getId();
			selectedCompetencies.add(competencyId);
		}
	}; 
	
	private static EventCallback addCompetencyCallback = new EventCallback() {
		@Override
		public void onEvent(Event event) {			
			for(String ids : selectedCompetencies){
				String[] idArray = ids.split(" ");
				String modelId = idArray[0];
				String competencyId = idArray[1];
				
				JSONObject x = competencyCache.get(competencyId);
				
				UI.addCompetencyToDesiredList(competencyId, x.get(":competencyTitle").isArray().get(0).isString().stringValue());
				DsESBApi.decalsAddDesiredCompetency(competencyId, modelId, competencyAddedCallback);
			}
			
			selectedCompetencies = new ArrayList<String>();
			UI.closeCompetencyModal();
		}
	};
	
	private static EventCallback closeCompetencyModalCallback = new EventCallback() {
		@Override
		public void onEvent(Event event) {
			selectedCompetencies = new ArrayList<String>();
			UI.closeCompetencyModal();
		}
	};
	
	private static ESBCallback<ESBPacket> competencyAddedCallback = new ESBCallback<ESBPacket>() {
		@Override
		public void onSuccess(ESBPacket esbPacket) {
			
		}
		
		@Override
		public void onFailure(Throwable caught) {}
	};
	
	private void searchCompetencies(){
		InputElement input = InputElement.as(DOM.getElementById(SEARCH_COMPETENCY_INPUT_ID));
		
		String query = input.getValue();
		
		DsESBApi.decalsSearchCompetencies(query, competencyResultsCallback);
	}
	
	private ESBCallback<ESBPacket> competencyResultsCallback = new ESBCallback<ESBPacket>() {
		@Override
		public void onFailure(Throwable caught) {}

		@Override
		public void onSuccess(ESBPacket packet) {
			JSONObject thing = null;
			
			if (packet.containsKey("contentStream")) 
				thing = JSONParser.parseStrict(packet.getContentString()).isObject();
			else
				thing = packet.isObject();
			
			if(thing == null){
				return;
			}else{
				UI.displayCompetencyResults(thing);
			}
		}
	};
	
	
	
	/** Native Javascript Functions **/
	
	public static final native void setupTypeahead(Element e, JSONArray objectives) /*-{
		var substringMatcher = function(strs) {
		  return function findMatches(q, cb) {
		    var matches, substringRegex;
		 
		    // an array that will be populated with substring matches
		    matches = [];
		 
		    // regex used to determine if a string contains the substring `q`
		    substrRegex = new RegExp(q, 'i');
		 
		    // iterate through the pool of strings and for any string that
		    // contains the substring `q`, add it to the `matches` array
		    for(var idx in strs["jsArray"]){
		      var str = strs["jsArray"][idx];
		      if (substrRegex.test(str)) {
		        matches.push(str);
		      }
		    }
		 
		    cb(matches);
		  };
		};
		
		$wnd.$(e).typeahead({
				hint: true,
				highlight: true,
				minLength: 1
		},
		{
			name: "Objectives",
			source: substringMatcher(objectives)
		});
	}-*/;
	
	public static final native void clearTypeaheadValue() /*-{
		$wnd.$("#prefAddObjectiveInput").typeahead("val", "");
	}-*/;
	
	public static final native void scrollToBottom() /*-{
		$wnd.scroll(0,$doc.body.scrollHeight+100);
	}-*/;

}

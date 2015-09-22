package com.eduworks.decals.ui.client.handler;

import java.util.ArrayList;

import com.eduworks.decals.ui.client.DsSession;
import com.eduworks.decals.ui.client.DsUserPreferences;
import com.eduworks.decals.ui.client.api.DsESBApi;
import com.eduworks.decals.ui.client.handler.InteractiveSearchHandler.CommentHandlerType;
import com.eduworks.decals.ui.client.handler.InteractiveSearchHandler.MoreOptsMode;
import com.eduworks.decals.ui.client.handler.InteractiveSearchHandler.NavMode;
import com.eduworks.decals.ui.client.handler.InteractiveSearchHandler.RatingHandlerType;
import com.eduworks.decals.ui.client.handler.InteractiveSearchHandler.SearchHistoryItem;
import com.eduworks.decals.ui.client.model.InteractiveSearchResultSetReturn;
import com.eduworks.decals.ui.client.model.SearchHandlerParamPacket;
import com.eduworks.decals.ui.client.model.WikiInfoItem;
import com.eduworks.decals.ui.client.model.WordOntologyDefinition;
import com.eduworks.decals.ui.client.model.WordOntologyItem;
import com.eduworks.decals.ui.client.pagebuilder.screen.enums.GRADE_LEVEL;
import com.eduworks.decals.ui.client.util.DsUtil;
import com.eduworks.decals.ui.client.util.OntologyHelper;
import com.eduworks.decals.ui.client.util.SolrResultsResponseParser;
import com.eduworks.decals.ui.client.util.WikiInfoHelper;
import com.eduworks.gwt.client.net.api.ESBApi;
import com.eduworks.gwt.client.net.callback.ESBCallback;
import com.eduworks.gwt.client.net.callback.EventCallback;
import com.eduworks.gwt.client.net.packet.ESBPacket;
import com.eduworks.gwt.client.pagebuilder.PageAssembler;
import com.google.gwt.core.client.Callback;
import com.google.gwt.dom.client.Element;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

public class LearnerFocusedSearchHandler extends InteractiveSearchHandler {
	
	private String APPLIED_GRADE_LEVELS_DESC = "Applied Grade Levels (via Preferences):";
	
	protected boolean ignoreGradeLevelPrefs = false;
	protected boolean ignoreLanguagePrefs = false;
	protected boolean ignoreResourceTypePrefs = false;
	protected boolean ignoreLearningObjectivePrefs = false;
	protected boolean ignoreCompetencyPrefs = false;
	
	protected String GRADE_LEVEL_FIELD_NAME = "grade_levels";
	
	public void performInteractiveSearch(String searchTerm, String widgetText, SearchHandlerParamPacket paramPacket,
	         RatingHandlerType ratingHandlerType, CommentHandlerType commentHandlerType, boolean buildAddToCollectionWidgets){
		DsUtil.sendTrackingMessage("Initiated interactive search for \"" + searchTerm + "\"");
	    
		this.searchQuery = DsUtil.cleanString(searchTerm.replace("+", " "));
	    this.widgetText = widgetText;
	    
	    parseParamPacket(paramPacket);
	    showMoreResultsOuterDivId = null;
	    showMoreResultsButtonId = null;  
	    showMoreResultsBusyId = null;     
	    
	    this.ratingHandlerType = ratingHandlerType;
	    this.commentHandlerType = commentHandlerType;
	    this.buildAddToCollectionWidgets = buildAddToCollectionWidgets;
	    actionHandler = new RegistryResourceActionHandler(this,paramPacket);
	   
	    syncId++; 
	    clearSearch();
	    
	    executeLearnerFocusedSearch();
	}
	
	protected void executeLearnerFocusedSearch(){
		final long currentSyncId = syncId;
	    try {
	    	DsUtil.removeAllWidgetsFromRootPanel(RootPanel.get(resultsContainerId));
	        setCounterContainerDisplay(false);
	        setSearchBusyDisplay(true);
	        DsUtil.hideLabel(filterNavContaierId);         
	        displayedSearchTerm = searchQuery;
	        setupInitialLearnerFocusedNavigation(currentSyncId);
	    }
	    catch (Exception e) {
	    	if (currentSyncId == syncId) showSearchError("Interactive search failed: " + e.getMessage());
	    }  
	}
	
	protected void setupInitialLearnerFocusedNavigation(final long currentSyncId){
		DsUtil.showLabel(filterNavContaierId);
	    DsUtil.setLabelText(filterNavQuestionId,TEMP_NAV_QUESTION);      
	    hideAllNavigationLineItems();
	    addLearnerFocusedAppliedGradeLevelDisplay();      
	    findAndHandleLearnerFocusedWordOntology(searchQuery, currentSyncId);
	    setUpLearnerFocusedAdvancedOptions(searchQuery);
	}
	
	//Show the applied grade level on the page
	protected void addLearnerFocusedAppliedGradeLevelDisplay() {
		if(DsUserPreferences.getInstance().prefsAreLocal){
			displayLearnerFocusedGradeLevelDisplay();
		}else{
			DsUserPreferences.getInstance().pullDownPreferences(new Callback<ESBPacket, Throwable>(){
				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub	
				}

				@Override
				public void onSuccess(ESBPacket result) {
					displayLearnerFocusedGradeLevelDisplay();
				}
			});
		}
	}
	
	private void displayLearnerFocusedGradeLevelDisplay(){
		DsUtil.showLabel(appliedGradeLevelsContainerId);
		DsUtil.removeAllWidgetsFromRootPanel(RootPanel.get(appliedGradeLevelsContainerId));
		StringBuffer sb = new StringBuffer();
		
		ArrayList<GRADE_LEVEL> prefGradeLevels = DsUserPreferences.getInstance().gradeLevels;
		if(!ignoreGradeLevelPrefs){
			if(prefGradeLevels.contains(GRADE_LEVEL.KINDERGARTEN)) kgGlApplied = true;
			else kgGlApplied = false;
			if(prefGradeLevels.contains(GRADE_LEVEL.ELEMENTARY)) esGlApplied = true;
			else esGlApplied = false;
			if(prefGradeLevels.contains(GRADE_LEVEL.MIDDLE_SCHOOL)) msGlApplied = true;
			else msGlApplied = false;
			if(prefGradeLevels.contains(GRADE_LEVEL.HIGH_SCHOOL)) hsGlApplied = true;
			else hsGlApplied = false;
			if(prefGradeLevels.contains(GRADE_LEVEL.HIGHER_ED)) cuGlApplied = true;
			else cuGlApplied = false;
			if(prefGradeLevels.contains(GRADE_LEVEL.VOCATIONAL)) vtpGlApplied = true;
			else vtpGlApplied = false;
		
			if(!kgGlApplied && !esGlApplied && !msGlApplied && !hsGlApplied && !cuGlApplied && !vtpGlApplied){
				kgGlApplied = true;
				 esGlApplied = true;
				 msGlApplied = true;
				 hsGlApplied = true;
				 cuGlApplied = true;
				 vtpGlApplied = true;
				 
				 ignoreGradeLevelPrefs = true;
			}
		}
		
		if(ignoreGradeLevelPrefs){
			sb.append("<p class=\"" + InteractiveSearchHandler.APPLIED_GRADE_LEVELS_CLASS + "\">");
			sb.append("<b>" + InteractiveSearchHandler.APPLIED_GRADE_LEVELS_DESC + " </b>");
		}else{
			sb.append("<p class=\"" + APPLIED_GRADE_LEVELS_CLASS + "\">");
			sb.append("<b>" + APPLIED_GRADE_LEVELS_DESC + " </b>");
		}
		
		if (kgGlApplied && esGlApplied && msGlApplied && hsGlApplied && cuGlApplied && vtpGlApplied) sb.append(GRADE_LEVEL_ALL_DESC);
		else if (!kgGlApplied && !esGlApplied && !msGlApplied && !hsGlApplied && !cuGlApplied && !vtpGlApplied) sb.append(GRADE_LEVEL_NONE_DESC);
		else {
			if (kgGlApplied) sb.append(GRADE_LEVEL_KG_DESC + ", ");      
			if (esGlApplied) sb.append(GRADE_LEVEL_ES_DESC + ", ");
			if (msGlApplied) sb.append(GRADE_LEVEL_MS_DESC + ", ");
			if (hsGlApplied) sb.append(GRADE_LEVEL_HS_DESC + ", ");
			if (cuGlApplied) sb.append(GRADE_LEVEL_CU_DESC + ", ");
			if (vtpGlApplied) sb.append(GRADE_LEVEL_VTP_DESC + ", ");
			sb.setLength(sb.length() - 2);
		}
		
		sb.append(" <span id='openOptionsLink'>(Click to Change)</span>");
		sb.append("</p>");
		RootPanel.get(appliedGradeLevelsContainerId).add(new HTML(sb.toString()));
		
		PageAssembler.attachHandler(DOM.getElementById("openOptionsLink"), Event.ONCLICK, quickGradeSelectedListener);
	}
	
	protected void buildCurrentDefRelatedResultCountsAndPopulateNymNavigation(final long currentSyncId) {
	      if (!ontologyHelper.currentDefinitionHasRelations()) findAndHandleWikiInfo(ontologyHelper.getCurrentDefinition().getWikiLookupValue(),currentSyncId,false);
	      else {
	         DsESBApi.decalsSolrRegistryQueryCounts(getCurrentDefinitionRelatedWordsList(),true,new ESBCallback<ESBPacket>() {
	            @Override
	            public void onSuccess(ESBPacket result) {
	               try {                  
	                  if (currentSyncId == syncId) {
	                     parseCurrentDefRelatedWordsResultCountsResults(result.getObject(ESBApi.ESBAPI_RETURN_OBJ));
	                     if (!ontologyHelper.currentDefinitionHasAnyRelationResults()) {
	                        findAndHandleWikiInfo(ontologyHelper.getCurrentDefinition().getWikiLookupValue(),currentSyncId,false);
	                     }
	                     else {
	                        ontologyHelper.applyCurrentDefinitionWordRelationsOrder();
	                        addNextNymNavigation();
	                     }                     
	                  }                  
	               }
	               catch (Exception e2) {if (currentSyncId == syncId) showSearchError("Error processing interactive search results (buildCurrentDefRelatedResultCountsAndPopulateNymNavigation): " + e2.getMessage());}
	            }
	            @Override
	            public void onFailure(Throwable caught) {if (currentSyncId == syncId) showSearchError("Error contacting search server (buildCurrentDefRelatedResultCountsAndPopulateNymNavigation)");}
	         });
	      }
	   }
	
	protected void findAndHandleLearnerFocusedWordOntology(final String word, final long currentSyncId){
		lastWordForOntology = word;
		DsESBApi.decalsDefineWord(word,DEFINE_WORD_LEVELS,new ESBCallback<ESBPacket>() {
			@Override
			public void onSuccess(ESBPacket result) {
				try {        
					if (currentSyncId == syncId) handleLearnerFocusedOntologyResults(word, result);
				}
				catch (Exception e2) {if (currentSyncId == syncId) showSearchError("Error processing interactive search results (define word): " + e2.getMessage());}
			}
			@Override
			public void onFailure(Throwable caught) {if (currentSyncId == syncId) showSearchError("Error contacting search server (define word)");}
		});         
	}
	
	//Handle the results from the word ontology lookup
	private void handleLearnerFocusedOntologyResults(String word, ESBPacket result) {      
		final long currentSyncId = syncId;
		ArrayList<String> nymList;
		if (result.containsKey(ESBApi.ESBAPI_RETURN_OBJ) && result.get(ESBApi.ESBAPI_RETURN_OBJ) instanceof JSONArray && result.getArray(ESBApi.ESBAPI_RETURN_OBJ).size() > 0) {
			if (ontologyHelper == null) {
	            ontologyHelper = new OntologyHelper();
	            ontologyHelper.registerUsedWord(searchQuery.toLowerCase().trim());
			}
			ontologyHelper.initFromDefineWordReturn(result.getArray(ESBApi.ESBAPI_RETURN_OBJ));
			
			if (ontologyHelper.getNumberOfDefinitions() == 0) {
				performLearnerFocusedSolrSearch(searchQuery,RESULTS_PER_PAGE,0,currentSyncId);
				findAndHandleWikiInfo(word,currentSyncId,false);                
			} 
	        else if (ontologyHelper.getNumberOfDefinitions() == 1) {
	            lastNavAnswerIndex = -1;
	            nymList = ontologyHelper.getRelatedSingleWordsForCurrentDefinition(MAX_NUM_DEF_TERMS);
	            if (nymList.size() > 0) searchQuery = word + " " + getStringFromList(nymList);       
	            performLearnerFocusedSolrSearch(searchQuery,RESULTS_PER_PAGE,0,currentSyncId);
	            buildCurrentDefRelatedResultCountsAndPopulateNymNavigation(currentSyncId);
	        } 
	        else if (ontologyHelper.getNumberOfDefinitions() > 1) {
	            lastNavAnswerIndex = -1;
	            nymList = ontologyHelper.getRelatedSingleWordsForAllDefinitions(MAX_NUM_DEF_TERMS);
	            if (nymList.size() > 0) searchQuery = word + WORD_BOOST + " " + getStringFromList(nymList);       
	            performLearnerFocusedSolrSearch(searchQuery,RESULTS_PER_PAGE,0,currentSyncId);
	            addNextMultiDefinitionNavigation(word);
	        }
		}
		else {
			performLearnerFocusedSolrSearch(searchQuery,RESULTS_PER_PAGE,0,currentSyncId);
			findAndHandleWikiInfo(word,currentSyncId,false);
		}
	}
	
	//Performs a solr search with an update to the search history
	private void performLearnerFocusedSolrSearch(final String query, int numberOfRecords, final int start, final long currentSyncId) {
		performLearnerFocusedSolrSearch(query,numberOfRecords,start,currentSyncId,true);
	}
	   
	//Performs a solr search
	private void performLearnerFocusedSolrSearch(final String query, int numberOfRecords, final int start, final long currentSyncId, final boolean updateHistory) {
		DsUtil.setLabelText("realSearchQuery", "Search Query: " + query + getAppliedGradeLevelsQueryString());
		
		ArrayList<String> ignorePrefsArray = new ArrayList<String>();
		if(ignoreGradeLevelPrefs) ignorePrefsArray.add(GRADE_LEVEL_FIELD_NAME);
		
		DsESBApi.decalsSolrLearnerFocusedSearch(query,
												String.valueOf(numberOfRecords),
												SOLR_QUERY_FIELDS,
												false,
												start,
												ignorePrefsArray,
												getAppliedGradeLevelsQueryString(),
												new ESBCallback<ESBPacket>() {
			@Override
			public void onSuccess(ESBPacket result) {
				try {        
					if (currentSyncId == syncId) {  
						if (intSearchResultSet == null) intSearchResultSet = new InteractiveSearchResultSetReturn();
						if (start == 0) intSearchResultSet.clearSearchResults(); 
						if (updateHistory) updateSearchHistory(displayedSearchTerm,query);
						SolrResultsResponseParser.parseSolrRegistryResponse(result.getObject(ESBApi.ESBAPI_RETURN_OBJ), intSearchResultSet, DsSession.getInstance().getInteractiveSearchThumbnailRootUrl());                                    
						populateInteractiveResults(start,currentSyncId);
					}
				}
				catch (Exception e2) {if (currentSyncId == syncId) showSearchError("Error processing interactive search results: " + e2.getMessage());}
			}
			@Override
			public void onFailure(Throwable caught) {if (currentSyncId == syncId) showSearchError("Error contacting search server");}
		});         
	}
	
	//Build the wiki info lookup data
	private void findAndHandleWikiInfo(final String title, final long currentSyncId, final boolean goToTopics) {
		//DsESBApi.decalsWikiInfo(title,new ESBCallback<ESBPacket>() {
		//for some reason wiki api doesn't always like caps (ex. Small Hive Beetle vs. small hive beetle)
		DsESBApi.decalsWikiInfo(title.toLowerCase(),new ESBCallback<ESBPacket>() { 
			@Override
			public void onSuccess(ESBPacket result) {
				try {        
					if (currentSyncId == syncId) handleWikiInfoResults(title, result, goToTopics);
				}
				catch (Exception e2) {if (currentSyncId == syncId) showSearchError("Error processing interactive search results (wiki info): " + e2.getMessage());}
			}
			@Override
			public void onFailure(Throwable caught) {if (currentSyncId == syncId) showSearchError("Error contacting search server (wiki info)");}
		});         
	}
	
	//Handle the results of the wiki info lookup
	private void handleWikiInfoResults(String title, ESBPacket result, boolean goToTopics) {
		DsUtil.setLabelText(filterNavQuestionId,TEMP_NAV_QUESTION);
		final long currentSyncId = syncId;     
		if (result.containsKey(ESBApi.ESBAPI_RETURN_OBJ)) {         
			if (wikiInfoHelper == null) wikiInfoHelper = new WikiInfoHelper();
			
			wikiInfoHelper.initFromWikiInfoReturn(title,result.get(ESBApi.ESBAPI_RETURN_OBJ).isObject(),true,true);
			
			if (wikiInfoHelper.getHasGoodInfo()) {
				addWordDefinition(title,wikiInfoHelper.getDescriptionExtract());
	            //addWordDefinition(title,wikiInfoHelper.getDescriptionExtract() + "<br><br>" + wikiInfoHelper.getNonMarkedDescriptionExtract());
	            registerExtractMarkupLinks();            
	            if (wikiInfoHelper.getNumberOfGoodCategories() > 0 && !goToTopics) {
	            	lastNavAnswerIndex = -1;
	            	addNextCategoryNavigation(); 
	            }
	            else if (wikiInfoHelper.getNumberOfGoodTopics() > 0) {
	            	lastNavAnswerIndex = -1;
	            	buildTopicResultCountsAndPopulateTopicNavigation(currentSyncId);
	            }
	            else showGradeLevelNavigation();
			}
			else showGradeLevelNavigation();
		}
		else showGradeLevelNavigation();
	}
	
	//Add category navigation answer
	private void addCategoryNavigationAnswer(String navAnswerLiId, int navAnswerIndex) {
		WikiInfoItem item = wikiInfoHelper.getRelatedCategories().get(navAnswerIndex);
		String answerId = genNavAnswerId(CATEGORY_MODE_ANSWER_PREFIX);
		addNavAnswer(navAnswerLiId,answerId,item.getName());
		PageAssembler.attachHandler(answerId,Event.ONCLICK,categorySelectedListener);
	}
	
	private void handleCategorySelection(int idx, final long currentSyncId) {
		WikiInfoItem wii = wikiInfoHelper.getRelatedCategories().get(idx);
		wii.setWikiLookupValue(wii.getName());
		wii.setSearchTerm(wii.getName());
		DsUtil.removeAllWidgetsFromRootPanel(RootPanel.get(resultsContainerId));
		setCounterContainerDisplay(false);
		setSearchBusyDisplay(true);
		DsUtil.setLabelText(filterNavQuestionId,TEMP_NAV_QUESTION);      
		hideAllNavigationLineItems();
		displayedSearchTerm = wii.getName();
		searchQuery = wii.getSearchTerm();
		clearWordDefinition();
		findAndHandleLearnerFocusedWordOntology(wii.getName(), currentSyncId);
		setUpLearnerFocusedAdvancedOptions(wii.getName());
		setSearchTermBoxText(wii.getName());
	}
	
	protected EventCallback categorySelectedListener = new EventCallback() {
		@Override
		public void onEvent(Event event) {
			syncId++;
			try {
				String id;
				Element e = Element.as(event.getEventTarget());
				if (e.getId() == null || e.getId().trim().isEmpty()) id = e.getParentElement().getId();
				else id = e.getId();            
				int itemIdx = getIndexFromElementId(id,CATEGORY_MODE_ANSWER_PREFIX);
				handleCategorySelection(itemIdx,syncId);        
			}
			catch (Exception e) {}
		}
	};
	
	//Add the next set of category navigation if there are any.  If not add topic navigation
	protected void addNextCategoryNavigation() {
		currentNavMode = NavMode.CATEGORY;
		hideAllNavigationLineItems();
		buildCategoryNavigationQuestion();      
		lastNavAnswerIndex++;
		if (wikiInfoHelper.getNumberOfGoodCategories() >= lastNavAnswerIndex + 1) {
			addCategoryNavigationAnswer(filterNavAnswer1LiId,lastNavAnswerIndex);
			lastNavAnswerIndex++;
			if (wikiInfoHelper.getNumberOfGoodCategories() >= lastNavAnswerIndex + 1) {
				addCategoryNavigationAnswer(filterNavAnswer2LiId,lastNavAnswerIndex);
				lastNavAnswerIndex++;
				if (wikiInfoHelper.getNumberOfGoodCategories() >= lastNavAnswerIndex + 1) {
					addCategoryNavigationAnswer(filterNavAnswer3LiId,lastNavAnswerIndex);
				}
			}
			DsUtil.showLabel(filterNavLinksId);
			PageAssembler.attachHandler(filterNavMoreAnswersLinkId,Event.ONCLICK,showMoreAnswersListener);
			PageAssembler.attachHandler(filterNavMoreOptionsLinkId,Event.ONCLICK,showMoreOptionsListener);         
		}
		else {
			lastNavAnswerIndex = -1;
			buildTopicResultCountsAndPopulateTopicNavigation(syncId);
		}
	}
	
	/* Advanced Options */
	
	protected void setUpLearnerFocusedAdvancedOptions(String word) {
		DsUtil.hideLabel(filterNavMoreOptDisambigLiId);
		DsUtil.hideLabel(filterNavMoreOptCatLiId);
		DsUtil.hideLabel(filterNavMoreOptTopicLiId);      
		long currentSyncId = syncId;        
		setUpLearnerFocusedAdvancedOptionsStep1(word,currentSyncId);
	}
	
	private void setUpLearnerFocusedAdvancedOptionsStep1(final String word, final long currentSyncId) {
		DsESBApi.decalsDefineWord(word,DEFINE_WORD_LEVELS,new ESBCallback<ESBPacket>() {
	         @Override
	         public void onSuccess(ESBPacket result) {
	            try {        
	               if (currentSyncId == syncId) {
	                  if (result.containsKey(ESBApi.ESBAPI_RETURN_OBJ) && result.get(ESBApi.ESBAPI_RETURN_OBJ) instanceof JSONArray && result.getArray(ESBApi.ESBAPI_RETURN_OBJ).size() > 0) {         
	                     OntologyHelper aooh = new OntologyHelper();
	                     aooh.initFromDefineWordReturn(result.getArray(ESBApi.ESBAPI_RETURN_OBJ));
	                     if (aooh.getNumberOfDefinitions() > 0) DsUtil.showLabel(filterNavMoreOptDisambigLiId); 
	                  }
	                  setUpLearnerFocusedAdvancedOptionsStep2(word,currentSyncId);
	               }
	            }
	            catch (Exception e2) {if (currentSyncId == syncId) showSearchError("Error processing interactive search results (setUpAdvancedOptionsStep1): " + e2.getMessage());}
	         }
	         @Override
	         public void onFailure(Throwable caught) {if (currentSyncId == syncId) showSearchError("Error contacting search server (setUpAdvancedOptionsStep1)");}
	      });  
	}
	
	private void setUpLearnerFocusedAdvancedOptionsStep2(final String title, final long currentSyncId) {   
		DsESBApi.decalsWikiInfo(title.toLowerCase(),new ESBCallback<ESBPacket>() { 
			@Override
			public void onSuccess(ESBPacket result) {
				try {        
					if (currentSyncId == syncId) {
						if (result.containsKey(ESBApi.ESBAPI_RETURN_OBJ)) {         
							WikiInfoHelper aowih = new WikiInfoHelper();
							aowih.initFromWikiInfoReturn(title,result.get(ESBApi.ESBAPI_RETURN_OBJ).isObject(),false,false);
							if (aowih.getNumberOfGoodCategories() > 0) DsUtil.showLabel(filterNavMoreOptCatLiId);
							if (aowih.getNumberOfGoodTopics() > 0) DsUtil.showLabel(filterNavMoreOptTopicLiId); 
						}
						//	DsUtil.showLabel(filterNavMoreOptGrdLvlLiId);
					}
				}
				catch (Exception e2) {if (currentSyncId == syncId) showSearchError("Error processing interactive search results (setUpAdvancedOptionsStep2): " + e2.getMessage());}
			}
			@Override
			public void onFailure(Throwable caught) {if (currentSyncId == syncId) showSearchError("Error contacting search server (setUpAdvancedOptionsStep2)");}
		}); 
	}
	
	/* Search History Methods */
	
	public void rebuildFromLastSearchHistory() {
		currentMoreOptsMode = MoreOptsMode.LESS;
		int idx = searchHistory.size() - 1;
		handleLearnerFocusedSearchHistorySelection(idx,syncId);             
	}
	
	//Handle topic selection when passed the topic index   
	private void handleLearnerFocusedSearchHistorySelection(int idx, final long currentSyncId) {
		SearchHistoryItem shi = searchHistory.get(idx);
		searchHistory = getSearchHistorySubList(searchHistory,idx + 1);
		DsUtil.removeAllWidgetsFromRootPanel(RootPanel.get(resultsContainerId));
		setCounterContainerDisplay(false);
		setSearchBusyDisplay(true);
		DsUtil.setLabelText(filterNavQuestionId,TEMP_NAV_QUESTION);      
		hideAllNavigationLineItems();
		displayedSearchTerm = shi.getDisplayedSearchTerm();
		searchQuery = shi.getSearchQuery ();
		clearWordDefinition();      
		addLearnerFocusedAppliedGradeLevelDisplay();      
		findAndHandleLearnerFocusedWordOntology(displayedSearchTerm, currentSyncId);
		setUpLearnerFocusedAdvancedOptions(displayedSearchTerm);
		setSearchTermBoxText(displayedSearchTerm);      
	}
	
	
	/* Grade Level Select Handlers */
	
	private EventCallback quickGradeSelectedListener = new EventCallback(){
		@Override
		public void onEvent(Event event) {
	        showGradeLevelNavigation();
	        PageAssembler.attachHandler(filterNavGrdLvlApplyLinkId,Event.ONCLICK,applyGradeLevelListener);
		}
	};
	
	protected EventCallback applyGradeLevelListener = new EventCallback() {
		@Override
		public void onEvent(Event event) {
			syncId++;
			try {
				handleLearnerFocusApplyGradeLevel(syncId);
			}
			catch (Exception e) {}
		}
	};

	//Handle apply grade level selection
	private void handleLearnerFocusApplyGradeLevel(final long currentSyncId) {  
		updateAppliedGradeLevels();      
		addAppliedGradeLevelDisplay();
		ignoreGradeLevelPrefs = true;
		performLearnerFocusedSolrSearch(searchQuery+getAppliedGradeLevelsQueryString(),RESULTS_PER_PAGE,0,currentSyncId,false);
	}
	
	/* Word Disambiguation Handlers */
	
	// to use to perform the search query.
	protected void handleDefinitionSelection(int idx, final long currentSyncId) {
		DsUtil.setLabelText(filterNavQuestionId,TEMP_NAV_QUESTION);      
		hideAllNavigationLineItems();
		ontologyHelper.applyAsCurrentDefintion(idx);      
		if (ontologyHelper.getCurrentDefinition().hasDomain()) {
			if (ontologyHelper.getCurrentDefinition().getDomain().equalsIgnoreCase(ontologyHelper.getCurrentDefinition().getWord())) {
				ontologyHelper.getCurrentDefinition().setWikiLookupValue(ontologyHelper.getCurrentDefinition().getWord());
				ontologyHelper.getCurrentDefinition().setSearchValue(ontologyHelper.getCurrentDefinition().getWord());
			}
			else {
				ontologyHelper.getCurrentDefinition().setWikiLookupValue(ontologyHelper.getCurrentDefinition().getWord() + " " + ontologyHelper.getCurrentDefinition().getDomain());
				ontologyHelper.getCurrentDefinition().setSearchValue(ontologyHelper.getCurrentDefinition().getWord() + " " + ontologyHelper.getCurrentDefinition().getDomain());
			}
			finishDefinitionSelectedActions(currentSyncId);
		}
		else buildSearchValuesForNonDomainDefintion(currentSyncId);
	}
	
	//After deciding how to perform the definition search, perform it and update the appropriate page elements
	protected void finishDefinitionSelectedActions(final long currentSyncId) {
		addWordDefinition(ontologyHelper.getCurrentDefinition());
		DsUtil.removeAllWidgetsFromRootPanel(RootPanel.get(resultsContainerId));
		setCounterContainerDisplay(false);
		setSearchBusyDisplay(true);
		displayedSearchTerm = buildDisplayTermForCurrentDefinition();
		ArrayList<String >nymList = ontologyHelper.getRelatedSingleWordsForCurrentDefinition(MAX_NUM_DEF_TERMS);
		if (nymList.size() > 0) searchQuery = ontologyHelper.getCurrentDefinition().getWord() + " " + getStringFromList(nymList);
		else searchQuery = ontologyHelper.getCurrentDefinition().getSearchValue();
		performLearnerFocusedSolrSearch(searchQuery,RESULTS_PER_PAGE,0,currentSyncId);
		lastNavAnswerIndex = -1;
		lastWordForOntology = ontologyHelper.getCurrentDefinition().getWikiLookupValue();
		buildCurrentDefRelatedResultCountsAndPopulateNymNavigation(currentSyncId);
		setUpLearnerFocusedAdvancedOptions(ontologyHelper.getCurrentDefinition().getWikiLookupValue());
		setSearchTermBoxText(ontologyHelper.getCurrentDefinition().getWord());      
	}
	
	//Add definition navigation answer
	protected void addDefinitionNavigationAnswer(String navAnswerLiId, int navAnswerIndex) {
		WordOntologyDefinition def = ontologyHelper.getDefinitions().get(navAnswerIndex);
		String answerId = genNavAnswerId(DEFINITION_MODE_ANSWER_PREFIX);
		addNavAnswer(navAnswerLiId,answerId,def.getDefinition());
		PageAssembler.attachHandler(answerId,Event.ONCLICK,learnerFocusedDefinitionSelectedListener);
	}
	
	private EventCallback learnerFocusedDefinitionSelectedListener = new EventCallback() {
		@Override
		public void onEvent(Event event) {
			syncId++;
			try {
				String id;
				Element e = Element.as(event.getEventTarget());
				if (e.getId() == null || e.getId().trim().isEmpty()) id = e.getParentElement().getId();
				else id = e.getId();            
				int itemIdx = getIndexFromElementId(id,DEFINITION_MODE_ANSWER_PREFIX);
				handleDefinitionSelection(itemIdx,syncId);
			}
			catch (Exception e) {}
		}
	};
	
	//Add the next set of definition questions if there are any.  If not, add wiki info questions
	protected void addNextMultiDefinitionNavigation(String word) {      
		lastWordForOntology = word;
		currentNavMode = NavMode.DEFINITION;
		clearWordDefinition();
		hideAllNavigationLineItems();
		buildDefinitionNavigationQuestion(word);      
		lastNavAnswerIndex++;
		if (ontologyHelper.getNumberOfDefinitions() >= lastNavAnswerIndex + 1) {
			addDefinitionNavigationAnswer(filterNavAnswer1LiId,lastNavAnswerIndex);
			lastNavAnswerIndex++;
			if (ontologyHelper.getNumberOfDefinitions() >= lastNavAnswerIndex + 1) {
				addDefinitionNavigationAnswer(filterNavAnswer2LiId,lastNavAnswerIndex);
				lastNavAnswerIndex++;
				if (ontologyHelper.getNumberOfDefinitions() >= lastNavAnswerIndex + 1) {
					addDefinitionNavigationAnswer(filterNavAnswer3LiId,lastNavAnswerIndex);
				}
			}
			DsUtil.showLabel(filterNavLinksId);
			PageAssembler.attachHandler(filterNavMoreAnswersLinkId,Event.ONCLICK,showMoreAnswersListener);
			PageAssembler.attachHandler(filterNavMoreOptionsLinkId,Event.ONCLICK,showMoreOptionsListener);         
		}
		else findAndHandleWikiInfo(word,syncId,false);
	}
	
	
	//Add related word answer
	private void addNymNavigationAnswer(String navAnswerLiId, int navAnswerIndex) {
		WordOntologyItem item = ontologyHelper.getCurrentDefinition().getRelatedWords().get(navAnswerIndex);
		String answerId = genNavAnswerId(NYM_MODE_ANSWER_PREFIX);
		addNavAnswer(navAnswerLiId,answerId,"<b>" + item.getWord() + "</b>" + ": " + item.getDefinition());
		PageAssembler.attachHandler(answerId,Event.ONCLICK,relatedWordSelectedListener);
	}
	
	//Add the next set of related word navigation questions if any exist.  If not, add wiki info
	private void addNextNymNavigation() {      
		currentNavMode = NavMode.NYM;
		hideAllNavigationLineItems();
		buildNymNavigationQuestion();
		addWordDefinition(ontologyHelper.getCurrentDefinition());
		lastNavAnswerIndex++;
		if (ontologyHelper.getCurrentDefinition().getNumberOfRelatedWords() >= lastNavAnswerIndex + 1) {
			addNymNavigationAnswer(filterNavAnswer1LiId,lastNavAnswerIndex);
			lastNavAnswerIndex++;
			if (ontologyHelper.getCurrentDefinition().getNumberOfRelatedWords() >= lastNavAnswerIndex + 1) {
				addNymNavigationAnswer(filterNavAnswer2LiId,lastNavAnswerIndex);
				lastNavAnswerIndex++;
				if (ontologyHelper.getCurrentDefinition().getNumberOfRelatedWords() >= lastNavAnswerIndex + 1) {
					addNymNavigationAnswer(filterNavAnswer3LiId,lastNavAnswerIndex);
				}
			}
			DsUtil.showLabel(filterNavLinksId);
			PageAssembler.attachHandler(filterNavMoreAnswersLinkId,Event.ONCLICK,showMoreAnswersListener);
			PageAssembler.attachHandler(filterNavMoreOptionsLinkId,Event.ONCLICK,showMoreOptionsListener);         
		}
		else findAndHandleWikiInfo(ontologyHelper.getCurrentDefinition().getWikiLookupValue(),syncId,false);   
	}
	
	//Handle related words selection
	private void handleRelatedWordSelection(int idx, final long currentSyncId) {
		WordOntologyItem woi = ontologyHelper.getCurrentDefinition().getRelatedWords().get(idx);
		woi.setWikiLookupValue(woi.getWord());
		if (woi.getWord().toLowerCase().indexOf(ontologyHelper.getCurrentDefinition().getWord().toLowerCase()) >= 0) {
			woi.setSearchValue(woi.getWord());
		}
		else {
			woi.setSearchValue(ontologyHelper.getCurrentDefinition().getWord() + " " + woi.getWord());
		}
		DsUtil.removeAllWidgetsFromRootPanel(RootPanel.get(resultsContainerId));
		setCounterContainerDisplay(false);
		setSearchBusyDisplay(true);
		DsUtil.setLabelText(filterNavQuestionId,TEMP_NAV_QUESTION);      
		hideAllNavigationLineItems();
		displayedSearchTerm = woi.getWord();
		searchQuery = woi.getSearchValue();
		clearWordDefinition();
		findAndHandleLearnerFocusedWordOntology(woi.getWord(), currentSyncId);
		setUpLearnerFocusedAdvancedOptions(woi.getWord());
		setSearchTermBoxText(woi.getWord());
	}
	
	protected EventCallback relatedWordSelectedListener = new EventCallback() {
		@Override
		public void onEvent(Event event) {
			syncId++;
			try {
				String id;
				Element e = Element.as(event.getEventTarget());
				if (e.getId() == null || e.getId().trim().isEmpty()) id = e.getParentElement().getId();
				else id = e.getId();            
				int itemIdx = getIndexFromElementId(id,NYM_MODE_ANSWER_PREFIX);
				handleRelatedWordSelection(itemIdx,syncId);        
			}
			catch (Exception e) {}
		}
	};
	
	protected EventCallback showMoreAnswersListener = new EventCallback() {
		@Override
		public void onEvent(Event event) {
			syncId++;
			if (currentNavMode.equals(NavMode.DEFINITION)) addNextMultiDefinitionNavigation(lastWordForOntology);
			else if (currentNavMode.equals(NavMode.NYM)) addNextNymNavigation();
			else if (currentNavMode.equals(NavMode.TOPIC)) addNextTopicNavigation();
			else if (currentNavMode.equals(NavMode.CATEGORY)) addNextCategoryNavigation();
			//else if (currentNavMode.equals(NavMode.GRADE)) findAndHandleWordOntology(searchQuery, syncId);
		}
	};
	
	//Add the topic navigation answer
	private void addTopicNavigationAnswer(String navAnswerLiId, int navAnswerIndex) {
		WikiInfoItem item = wikiInfoHelper.getRelatedTopics().get(navAnswerIndex);
		String answerId = genNavAnswerId(TOPIC_MODE_ANSWER_PREFIX);
		addNavAnswer(navAnswerLiId,answerId,item.getName());
		PageAssembler.attachHandler(answerId,Event.ONCLICK,topicSelectedListener);
	}
	
	
	//Display grade level navigation.
	protected void showGradeLevelNavigation() {
      super.showGradeLevelNavigation();
      PageAssembler.attachHandler(filterNavGrdLvlApplyLinkId,Event.ONCLICK,applyGradeLevelListener);
      super.synchGradeLevelCheckBoxes();
   }
	
	//Add the next set of topic navigation if there are any.  If not, show grade level navigation
	private void addNextTopicNavigation() {
		currentNavMode = NavMode.TOPIC;
		hideAllNavigationLineItems();
		buildTopicNavigationQuestion();      
		lastNavAnswerIndex++;
		if (wikiInfoHelper.getNumberOfGoodTopics() >= lastNavAnswerIndex + 1) {
			addTopicNavigationAnswer(filterNavAnswer1LiId,lastNavAnswerIndex);
			lastNavAnswerIndex++;
			if (wikiInfoHelper.getNumberOfGoodTopics() >= lastNavAnswerIndex + 1) {
				addTopicNavigationAnswer(filterNavAnswer2LiId,lastNavAnswerIndex);
				lastNavAnswerIndex++;
				if (wikiInfoHelper.getNumberOfGoodTopics() >= lastNavAnswerIndex + 1) {
					addTopicNavigationAnswer(filterNavAnswer3LiId,lastNavAnswerIndex);
				}
			}
			DsUtil.showLabel(filterNavLinksId);
			PageAssembler.attachHandler(filterNavMoreAnswersLinkId,Event.ONCLICK,showMoreAnswersListener);
			PageAssembler.attachHandler(filterNavMoreOptionsLinkId,Event.ONCLICK,showMoreOptionsListener);         
		}
		else showGradeLevelNavigation();
	}
	
	protected EventCallback showMoreOptionsListener = new EventCallback() {
		 @Override
		 public void onEvent(Event event) {
			 syncId++;
			 handleShowMoreOptionsSelect(syncId);
		 }
	};
	
	//Handle show more options selected
	private void handleShowMoreOptionsSelect(final long currentSyncId) {
		if (currentMoreOptsMode.equals(MoreOptsMode.LESS)) {
			DsUtil.showLabel(filterNavMoreOptContainerId);
			currentMoreOptsMode = MoreOptsMode.MORE;
			DsUtil.setLabelText(filterNavMoreOptLinkTxtId,MORE_OPTS_LESS_TEXT);
			PageAssembler.attachHandler(filterNavMoreOptDisambigId,Event.ONCLICK,aoDisambigSelectedListener);
			PageAssembler.attachHandler(filterNavMoreOptCatId,Event.ONCLICK,aoCatSelectedListener);
			PageAssembler.attachHandler(filterNavMoreOptTopicId,Event.ONCLICK,aoTopicSelectedListener);
			PageAssembler.attachHandler(filterNavMoreOptGrdLvlId,Event.ONCLICK,aoGrdLvlSelectedListener);
		}
		else {
			DsUtil.hideLabel(filterNavMoreOptContainerId);
			currentMoreOptsMode = MoreOptsMode.LESS;
			DsUtil.setLabelText(filterNavMoreOptLinkTxtId,MORE_OPTS_MORE_TEXT);
		}
	}
	
	protected EventCallback aoDisambigSelectedListener = new EventCallback() {
		@Override
		public void onEvent(Event event) {
			syncId++;
			try {
				hideAllNavigationLineItems();
				DsUtil.setLabelText(filterNavQuestionId,TEMP_NAV_QUESTION);
				findAndHandleLearnerFocusedWordOntology(lastWordForOntology, syncId);
			}
			catch (Exception e) {}
		}
	};

	protected EventCallback aoCatSelectedListener = new EventCallback() {
		@Override
		public void onEvent(Event event) {
			syncId++;
			try {
				hideAllNavigationLineItems();
				DsUtil.setLabelText(filterNavQuestionId,TEMP_NAV_QUESTION);
				findAndHandleWikiInfo(lastWordForOntology,syncId,false);
			}
			catch (Exception e) {}
		}
	};
	
	protected EventCallback aoTopicSelectedListener = new EventCallback() {
		@Override
		public void onEvent(Event event) {
			syncId++;
			try {
				hideAllNavigationLineItems();
				DsUtil.setLabelText(filterNavQuestionId,TEMP_NAV_QUESTION);
				findAndHandleWikiInfo(lastWordForOntology,syncId,true);
			}
			catch (Exception e) {}
		}
	};
	   
	protected EventCallback aoGrdLvlSelectedListener = new EventCallback() {
		@Override
		public void onEvent(Event event) {
			syncId++;
			try {
				showGradeLevelNavigation();
			}
			catch (Exception e) {}
		}
	}; 
	
	/* Search History Methods */
	
	//Rebuild the search history display
	private void refreshSearchHistoryDisplay() {
		DsUtil.showLabel(searchHistoryElementId);      
		DsUtil.removeAllWidgetsFromRootPanel(RootPanel.get(searchHistoryElementId));
		StringBuffer sb = new StringBuffer();
		sb.append("<p class=\"" + APPLIED_GRADE_LEVELS_CLASS + "\">");
		sb.append("<b>" + SEARCH_HISTORY_DESC  + " </b>");
		for (int i=0; i < searchHistory.size();i++) {
			sb.append(buildSearchHistoryLink(i,searchHistory.get(i).getDisplayedSearchTerm()) + ", ");
		}
		if (searchHistory.size() >= 1) sb.setLength(sb.length() - 2);
		sb.append("</p>");
		RootPanel.get(searchHistoryElementId).add(new HTML(sb.toString()));
		buildSearchHistoryEventHandlers();
	}
	
	//Adds a term to the search history and refreshes the search history display
	void updateSearchHistory(String displayedSearchTerm, String searchQuery) {
		if (searchHistory.size() >= 1) {
			SearchHistoryItem shi = searchHistory.get(searchHistory.size() - 1);
			if (shi.getDisplayedSearchTerm().equalsIgnoreCase(displayedSearchTerm) && shi.getSearchQuery().equalsIgnoreCase(searchQuery)) {
				refreshSearchHistoryDisplay();
				return;
			}
		}     
		searchHistory.add(new SearchHistoryItem(displayedSearchTerm, searchQuery));
		refreshSearchHistoryDisplay();
	}
	
	//Build search history event handlers
	private void buildSearchHistoryEventHandlers() {
		for (int i=0; i < searchHistory.size();i++) {
			PageAssembler.attachHandler(SEARCH_HISTORY_ID_PREFIX + i,Event.ONCLICK,searchHistoryListener);
		}
	}
	   
	
	
	//Handle topic selection when passed the topic index   
	private void handleSearchHistorySelection(int idx, final long currentSyncId) {
		SearchHistoryItem shi = searchHistory.get(idx);
		searchHistory = getSearchHistorySubList(searchHistory,idx + 1);
		DsUtil.removeAllWidgetsFromRootPanel(RootPanel.get(resultsContainerId));
		setCounterContainerDisplay(false);
		setSearchBusyDisplay(true);
		DsUtil.setLabelText(filterNavQuestionId,TEMP_NAV_QUESTION);      
		hideAllNavigationLineItems();
		displayedSearchTerm = shi.getDisplayedSearchTerm();
		searchQuery = shi.getSearchQuery ();
		clearWordDefinition();      
		findAndHandleLearnerFocusedWordOntology(displayedSearchTerm, currentSyncId);
		setUpLearnerFocusedAdvancedOptions(displayedSearchTerm);
		setSearchTermBoxText(displayedSearchTerm);      
	}
	
	protected EventCallback searchHistoryListener = new EventCallback() {
		@Override
		public void onEvent(Event event) {
			syncId++;
			try {
				String id;
				Element e = Element.as(event.getEventTarget());
				if (e.getId() == null || e.getId().trim().isEmpty()) id = e.getParentElement().getId();
				else id = e.getId();            
				int itemIdx = getIndexFromElementId(id,SEARCH_HISTORY_ID_PREFIX);
				handleSearchHistorySelection(itemIdx,syncId);        
			}
			catch (Exception e) {}                        
		}
	};
	
	
	public void populateFindSimilarResults(ESBPacket result){
		int currentSyncId = 5;
		syncId = currentSyncId;
		
		int start = 0;
		
		if (intSearchResultSet == null) intSearchResultSet = new InteractiveSearchResultSetReturn();
		if (start == 0) intSearchResultSet.clearSearchResults(); 
		//if (updateHistory) updateSearchHistory(displayedSearchTerm,query);
		SolrResultsResponseParser.parseSolrRegistryResponse(result.getObject(ESBApi.ESBAPI_RETURN_OBJ), intSearchResultSet, DsSession.getInstance().getInteractiveSearchThumbnailRootUrl());                                    
		populateInteractiveResults(start,currentSyncId);
	}
}

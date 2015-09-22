package com.eduworks.decals.ui.client.handler;

import java.util.ArrayList;

import com.eduworks.decals.ui.client.api.DsESBApi;
import com.eduworks.decals.ui.client.model.BasicSearchResult;
import com.eduworks.decals.ui.client.util.DsUtil;
import com.eduworks.decals.ui.client.util.RegistrySearchResultWidgetGenerator;
import com.eduworks.gwt.client.net.api.ESBApi;
import com.eduworks.gwt.client.net.callback.ESBCallback;
import com.eduworks.gwt.client.net.callback.EventCallback;
import com.eduworks.gwt.client.net.packet.ESBPacket;
import com.eduworks.gwt.client.pagebuilder.PageAssembler;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Performs basic search against free.ed.gov.
 * 
 * @author Eduworks Corporation
 *
 */
public class BasicSearchHandler extends SearchHandler {
   
   private static final String TOTAL_RESULTS_KEY = "total";
   private static final String RESULTS_KEY = "hits";
   private static final int RESULTS_PER_PAGE = 25;
   
   private static final String SHOW_MORE_RESULTS_ID_SUFFIX = "-bsh";
   
   protected int currentPage;
   
   @Override
   protected String getShowMoreResultsIdSuffix() {return SHOW_MORE_RESULTS_ID_SUFFIX;}
      
   protected EventCallback showMoreResultsListener = new EventCallback() {
      @Override
      public void onEvent(Event event) {
         syncId++;
         currentPage++;         
         hideShowMoreResultsButton();
         showShowMoreResultsBusyImage();
         fetchBasicSearchResults(false);         
      }
   };
   
   @Override
   protected void addShowMoreResultsHandler() {
      PageAssembler.attachHandler(showMoreResultsButtonId,Event.ONCLICK,showMoreResultsListener);
   }
   
   //build the search counter statement
   private String getSearchCounterStatement(JSONObject ro) {
      long totalNumberOfResults = (long) ro.get(RESULTS_KEY).isObject().get(TOTAL_RESULTS_KEY).isNumber().doubleValue();
      return "\"" + searchQuery +  "\" - " + counterFormat.format(totalNumberOfResults) + " results found";
   }
   
   
   //parses the results array into a list of basic search results
   private ArrayList<BasicSearchResult> parseResultsArray(JSONArray resultsArray) {
      ArrayList<BasicSearchResult> resultsList = new ArrayList<BasicSearchResult>();
      for (int i=0;i<resultsArray.size();i++) resultsList.add(new BasicSearchResult((JSONObject)resultsArray.get(i)));
      return resultsList;
   }
   
   //populate the search results
   private void populateBasicResults(JSONObject ro, boolean isNewSearch) {      
      hideShowMoreResultsDiv();
      if (isNewSearch) DsUtil.removeAllWidgetsFromRootPanel(RootPanel.get(resultsContainerId));
      setCounterContainerDisplay(true);
      setSearchBusyDisplay(false);      
      DsUtil.setLabelText(counterElementId, getSearchCounterStatement(ro));      
      JSONArray resultsArray = ro.get(RESULTS_KEY).isObject().get(RESULTS_KEY).isArray();     
      RegistrySearchResultWidgetGenerator srwg = new RegistrySearchResultWidgetGenerator();      
      srwg.addBasicSearchResultWidgets(parseResultsArray(resultsArray),resultsContainerId,widgetText);            
      if (resultsArray.size() >= RESULTS_PER_PAGE) addShowMoreResultsButton();
   }
   
   //populateBasicResults(DsUtil.parseJson(response.getText()),isNewSearch);
   
   //attempt to fetch results from free.ed.gov
   private void fetchBasicSearchResults(final boolean isNewSearch) {      
      final long currentSyncId = syncId;
      if (isNewSearch) {
         DsUtil.removeAllWidgetsFromRootPanel(RootPanel.get(resultsContainerId));
         setCounterContainerDisplay(false);
         setSearchBusyDisplay(true);       
      } 
      DsESBApi.decalsBasicSearch(URL.encode(searchQuery), RESULTS_PER_PAGE, currentPage, new ESBCallback<ESBPacket>() {
         @Override
         public void onSuccess(ESBPacket result) {
            if (currentSyncId == syncId) populateBasicResults(result.getObject(ESBApi.ESBAPI_RETURN_OBJ),isNewSearch);                           
         }
         @Override
         public void onFailure(Throwable caught) {showSearchError("Could not connect to the search server.");}
      });                    
   } 
   
   /**
    * Performs a basic search against free.ed.gov and populates the given element with the results.
    * 
    * @param page The starting search page.
    * @param searchTerm The non URL encoded search term. (URL encoding is done internally).
    * @param resultsContainerId The element ID to attach the results.
    * @param widgetText The widget text to use for the results.
    * @param counterElementId The element ID of the search counter.
    * @param counterContainerElementId The element ID of the search counter container.
    * @param searchBusyElementId The element ID of the busy indicator.
    */
   public void performBasicSearch(int page, String searchTerm, String resultsContainerId, String widgetText, String counterElementId, 
                                  String counterContainerElementId, String searchBusyElementId) {
      DsUtil.sendTrackingMessage("Performed basic search for \"" + searchTerm + "\"");
      this.resultsContainerId = resultsContainerId;
      this.widgetText = widgetText;   
      this.counterElementId = counterElementId;
      this.searchQuery = searchTerm;
      this.counterContainerElementId = counterContainerElementId;
      this.searchBusyElementId = searchBusyElementId;
      this.currentPage = page;
      showMoreResultsOuterDivId = null;
      showMoreResultsButtonId = null;  
      showMoreResultsBusyId = null;
      syncId++;
      fetchBasicSearchResults(true);     
   }

}
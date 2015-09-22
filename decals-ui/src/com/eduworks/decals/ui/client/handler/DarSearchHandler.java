package com.eduworks.decals.ui.client.handler;

import com.eduworks.decals.ui.client.api.DsESBApi;
import com.eduworks.decals.ui.client.model.DarSearchResultSetReturn;
import com.eduworks.decals.ui.client.model.DecalsApplicationRepositoryRecord;
import com.eduworks.decals.ui.client.model.SearchHandlerParamPacket;
import com.eduworks.decals.ui.client.util.DarSearchResultWidgetGenerator;
import com.eduworks.decals.ui.client.util.DsUtil;
import com.eduworks.decals.ui.client.util.SolrResultsResponseParser;
import com.eduworks.gwt.client.net.api.ESBApi;
import com.eduworks.gwt.client.net.callback.ESBCallback;
import com.eduworks.gwt.client.net.callback.EventCallback;
import com.eduworks.gwt.client.net.packet.ESBPacket;
import com.eduworks.gwt.client.pagebuilder.PageAssembler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * DECALS application repository item search handler.
 * 
 * @author Eduworks Corporation
 *
 */
public class DarSearchHandler extends SearchHandler {
   
   private static final int RESULTS_PER_PAGE = 10;
   
   private static final String SHOW_MORE_RESULTS_ID_SUFFIX = "-dsh";
   
   private static final String SOLR_DESC_ORDER = "desc";
   //private static final String SOLR_UPLOAD_DATE_FIELD = "uploadDate_l";
   private static final String SOLR_UPDATE_DATE_FIELD = "updatedDate_l";
   
   private String userId;
   private DarSearchResultSetReturn darSearchResultSet;   
   private int lastResultStart;
   private String emptyContributionMessageId;
   
   private DarResourceActionHandler actionHandler;
   
   @Override
   protected void addShowMoreResultsHandler() {
      PageAssembler.attachHandler(showMoreResultsButtonId,Event.ONCLICK,showMoreResultsListener);      
   }

   @Override
   protected String getShowMoreResultsIdSuffix() {return SHOW_MORE_RESULTS_ID_SUFFIX;}
   
   /**
    * Listener for show more results click.
    * 
    * This may need to change once we have multiple search types...
    */
   protected EventCallback showMoreResultsListener = new EventCallback() {
      @Override
      public void onEvent(Event event) {
         syncId++;
         int newStartResult = lastResultStart + RESULTS_PER_PAGE;      
         hideShowMoreResultsButton();
         showShowMoreResultsBusyImage();
         performDarSolrSearchByUser(SOLR_UPDATE_DATE_FIELD,SOLR_DESC_ORDER,RESULTS_PER_PAGE,newStartResult,syncId);                                    
      }
   };
   
   //Build the search counter statement
   private String getSearchCounterStatement() {
      long numShown = 0;
      if ((lastResultStart + RESULTS_PER_PAGE) > darSearchResultSet.getNumTotalResultsFound()) numShown = darSearchResultSet.getNumTotalResultsFound();
      else numShown = lastResultStart + RESULTS_PER_PAGE;
      return "Showing " + counterFormat.format(numShown) + " of " +  counterFormat.format(darSearchResultSet.getNumTotalResultsFound()) + " of your contributions";      
   }
   
   //Displays the empty contributions message
   private void showEmptyContributionsMessage() {
      setCounterContainerDisplay(false);
      setSearchBusyDisplay(false);
      DsUtil.showLabel(emptyContributionMessageId);
   }
   
   //Populate the page with the interactive search results
   private void populateDarResults(int start, final long currentSyncId) {
      if (currentSyncId == syncId) {
         if (start == 0) DsUtil.removeAllWidgetsFromRootPanel(RootPanel.get(resultsContainerId));
         if (darSearchResultSet.getNumTotalResultsFound() == 0) showEmptyContributionsMessage();
         else {
            DsUtil.hideLabel(emptyContributionMessageId);
            lastResultStart = start;      
            hideShowMoreResultsDiv();        
            setCounterContainerDisplay(true);
            setSearchBusyDisplay(false);  
            DsUtil.setLabelText(counterElementId, getSearchCounterStatement());      
            DarSearchResultWidgetGenerator dsrwg = new DarSearchResultWidgetGenerator(actionHandler);
            dsrwg.addDarSearchResultWidgets(darSearchResultSet.getSearchResults(start,RESULTS_PER_PAGE),resultsContainerId,widgetText);
            if (darSearchResultSet.getNumTotalResultsFound() > (lastResultStart + RESULTS_PER_PAGE)) addShowMoreResultsButton();            
         }
      }
   }
   
   //Performs a solr search
   private void performDarSolrSearchByUser(String sortField, String sortOrder, int numberOfRecords, final int start, final long currentSyncId) {
      DsESBApi.decalsDarSolrSearchByUser(userId, String.valueOf(RESULTS_PER_PAGE), sortField, sortOrder, true, start, new ESBCallback<ESBPacket>() {
         @Override
         public void onSuccess(ESBPacket result) {
            try {        
               if (currentSyncId == syncId) {  
                  if (darSearchResultSet == null) darSearchResultSet = new DarSearchResultSetReturn();
                  if (start == 0) darSearchResultSet.clearSearchResults(); 
                  SolrResultsResponseParser.parseSolrDarResponse(result.getObject(ESBApi.ESBAPI_RETURN_OBJ), darSearchResultSet);
                  populateDarResults(start,currentSyncId);
               }
            }
            catch (Exception e2) {if (currentSyncId == syncId) showSearchError("Error processing DAR search results: " + e2.getMessage());}
         }
         @Override
         public void onFailure(Throwable caught) {if (currentSyncId == syncId) showSearchError("Error contacting search server");}
         });         
   }
   
   //Execute a new DAR search
   private void executeNewUserUpdateDateSearch() {
      final long currentSyncId = syncId;
      try {
         DsUtil.removeAllWidgetsFromRootPanel(RootPanel.get(resultsContainerId));         
         setCounterContainerDisplay(false);
         setSearchBusyDisplay(true);
         performDarSolrSearchByUser(SOLR_UPDATE_DATE_FIELD,SOLR_DESC_ORDER,RESULTS_PER_PAGE,0,syncId);
      }
      catch (Exception e) {if (currentSyncId == syncId) showSearchError("DAR search failed: " + e.getMessage());}      
   }
   
   //Grabs the needed element IDs out of the parameter packet
   private void parseParamPacket(SearchHandlerParamPacket paramPacket) {
      this.resultsContainerId = paramPacket.getResultsContainerId();
      this.counterContainerElementId = paramPacket.getCounterContainerElementId();
      this.counterElementId = paramPacket.getCounterElementId();
      this.searchBusyElementId = paramPacket.getSearchBusyElementId();
      this.emptyContributionMessageId = paramPacket.getEmptyContributionMessageId();
   }
   
   /**
    * Performs a DECALS application repository (DAR) and populates the results in the element with resultsContainerId
    * 
    * @param userId The user ID to use for the search
    * @param widgetText The search result widget text
    * @param paramPacket The packet containing relevant element IDs  
    */
   public void performDarSearchByUserDate(String userId, String widgetText, SearchHandlerParamPacket paramPacket) {
      this.widgetText = widgetText;
      this.userId = userId;
      parseParamPacket(paramPacket);
      if(actionHandler == null)
    	  actionHandler = new DarResourceActionHandler(this,paramPacket);
      executeNewUserUpdateDateSearch();
   }  
   
   /**
    * Refreshes the search results with the current search criteria
    */
   public void refreshCurrentSearch(long syncId) {
      if (this.syncId == syncId) executeNewUserUpdateDateSearch();
   }
   
   /**
    * Returns the current sync ID.
    * 
    * @return  Returns the current sync ID.
    */
   public long getCurrentSyncId() {return syncId;}
   
   /**
    * Returns the DarSearchResult with the given ID.  Returns null if not found.
    * 
    * @param resourceId The ID of the result to find.
    * @return  Returns the DarSearchResult with the given ID.  Returns null if not found.
    */
   public DecalsApplicationRepositoryRecord getDarResource(String resourceId) {
      if (darSearchResultSet == null) return null;
      else return darSearchResultSet.getSearchResult(resourceId);
   }
   
}

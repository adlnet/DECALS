package com.eduworks.decals.ui.client.handler;

import com.eduworks.decals.ui.client.util.DsUtil;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Base class for search handlers.
 * 
 * @author Eduworks Corporation
 *
 */
public abstract class SearchHandler {
   
   protected static final String SHOW_MORE_RESULTS_TEXT = "Show More Results";
   protected static final String SHOW_MORE_RESULTS_BUSY_IMAGE = "images/status-busy.gif";
   
   protected NumberFormat counterFormat = NumberFormat.getFormat("#,##0;(#,##0)");   
   protected String resultsContainerId;
   protected String widgetText;
   protected String counterElementId;
   protected String counterContainerElementId;
   protected String searchBusyElementId;
   protected String searchQuery;   
   protected long syncId = 0;
   protected String showMoreResultsOuterDivId;
   protected String showMoreResultsButtonId;   
   protected String showMoreResultsBusyId;
   
   protected abstract void addShowMoreResultsHandler();
   
   protected abstract String getShowMoreResultsIdSuffix();
   
   //adds a show more results button
   protected void addShowMoreResultsButton() {      
      showMoreResultsButtonId = syncId + "-bs-showMoreResults" + getShowMoreResultsIdSuffix(); 
      showMoreResultsBusyId = showMoreResultsButtonId + "-busyImg";
      showMoreResultsOuterDivId = showMoreResultsButtonId + "-outerDiv";
      StringBuffer sb = new StringBuffer();
      sb.append("<div id=\"" + showMoreResultsOuterDivId + "\" class=\"row\">");
      sb.append("<div class=\"medium-12\" style=\"text-align:center\">");
      sb.append("<button class=\"showMoreResultsButton\" id=\"" + showMoreResultsButtonId + "\">" + SHOW_MORE_RESULTS_TEXT + "</button>");
      sb.append("<img id=\"" + showMoreResultsBusyId + "\" src=\"" + SHOW_MORE_RESULTS_BUSY_IMAGE + "\" style=\"display:none\"/>");
      sb.append("</div>");
      sb.append("</div>");
      RootPanel.get(resultsContainerId).add(new HTML(sb.toString())); 
      //PageAssembler.attachHandler(showMoreResultsButtonId,Event.ONCLICK,showMoreResultsListener);
      addShowMoreResultsHandler();
   }
   
   //hides the current show more results button if one exists
   protected void hideShowMoreResultsButton() {
      if (RootPanel.get(showMoreResultsButtonId) != null) DsUtil.hideLabel(showMoreResultsButtonId);      
   }
   
   //hides the current show more results div if one exists
   protected void hideShowMoreResultsDiv() {
      if (RootPanel.get(showMoreResultsOuterDivId) != null) DsUtil.hideLabel(showMoreResultsOuterDivId);
   }
   
   //toggle the counter container display
   protected void setCounterContainerDisplay(boolean display) {
      if (display) DsUtil.showLabel(counterContainerElementId);
      else DsUtil.hideLabel(counterContainerElementId);
   }
   
   //shows the current show more results busy image if one exists
   protected void showShowMoreResultsBusyImage() {
      if (RootPanel.get(showMoreResultsBusyId) != null) DsUtil.setImageStyle(showMoreResultsBusyId,"display:inline");
   }
   
   //toggle the search busy display
   protected void setSearchBusyDisplay(boolean display) {
      if (display) DsUtil.setImageStyle(searchBusyElementId,"display:block;text-align:center");
      else DsUtil.hideImage(searchBusyElementId);
   }
   
   //show a search error
   protected void showSearchError(String errorMessage) {
      setCounterContainerDisplay(false);
      setSearchBusyDisplay(false);
      hideShowMoreResultsDiv();
      DsUtil.showSimpleErrorMessage(resultsContainerId,"Search Term: \"" + searchQuery + "\" : " + errorMessage, false);      
   }   

}

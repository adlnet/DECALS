package com.eduworks.decals.ui.client.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Vector;






import com.eduworks.decals.ui.client.DsSession;
import com.eduworks.decals.ui.client.api.DsESBApi;
import com.eduworks.decals.ui.client.handler.RegistryResourceActionHandler;
import com.eduworks.decals.ui.client.handler.InteractiveSearchHandler.CommentHandlerType;
import com.eduworks.decals.ui.client.handler.InteractiveSearchHandler.RatingHandlerType;
import com.eduworks.decals.ui.client.model.BasicSearchResult;
import com.eduworks.decals.ui.client.model.InteractiveSearchResult;
import com.eduworks.decals.ui.client.model.ParadataPublicationInfo;
import com.eduworks.decals.ui.client.model.RegistrySearchResult;
import com.eduworks.decals.ui.client.model.ResourceCommentSummaryInfo;
import com.eduworks.decals.ui.client.model.ResourceParadata;
import com.eduworks.decals.ui.client.model.ResourceRatingSummaryInfo;
import com.eduworks.gwt.client.net.api.ESBApi;
import com.eduworks.gwt.client.net.callback.ESBCallback;
import com.eduworks.gwt.client.net.callback.EventCallback;
import com.eduworks.gwt.client.net.packet.ESBPacket;
import com.eduworks.gwt.client.pagebuilder.PageAssembler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HTML;
import com.ibm.icu.util.BytesTrie.Iterator;

/**
 * Generates registry search results widgets.
 * 
 * @author Eduworks Corporation
 *
 */
public class RegistrySearchResultWidgetGenerator {

   private static final String DEFAULT_WIDGET_ID_TOKEN = "x";

   private static final String THUMBNAIL_ANCHOR_SUFFIX = "-srThumbnailAnchor";
   private static final String THUMBNAIL_IMAGE_SUFFIX = "-srThumbnailImage";
   private static final String DETAILS_ANCHOR_SUFFIX = "-srDetailsAnchor";
   private static final String DETAILS_TITLE_SUFFIX = "-srDetailsTitle";
   private static final String DETAILS_LINK_SUFFIX = "-srDetailsLink";
   private static final String DETAILS_DESC_SUFFIX = "-srDetailsDesc";
   private static final String DETAILS_SOURCE_SUFFIX = "-srDetailsSource";
   private static final String DETAILS_REDIRECT_WARNING_SUFFIX = "-srRedirectWarning";
   private static final String DETAILS_DESC_TOGGLE_SUFFIX = "-srDetailsToggle";
   private static final String DETAILS_DESC_TOGGLE_STATUS = "-srDetailsToggleStatus";
   private static final String DETAILS_RATING_FILL_SUFFIX = "-srDetailsRatingFill";
   private static final String DETAILS_RATING_INFO_SUFFIX = "-srDetailsRatingInfo";
   private static final String DETAILS_RATING_1_SUFFIX = "-srDetailsRating-1";
   private static final String DETAILS_RATING_2_SUFFIX = "-srDetailsRating-2";
   private static final String DETAILS_RATING_3_SUFFIX = "-srDetailsRating-3";
   private static final String DETAILS_RATING_4_SUFFIX = "-srDetailsRating-4";
   private static final String DETAILS_RATING_5_SUFFIX = "-srDetailsRating-5";  
   private static final String DETAILS_COMMENTS_INFO_SUFFIX = "-srDetailsCommentsInfo";
   private static final String DETAILS_COMMENTS_LINK_SUFFIX = "-srDetailsCommentsLink";
   private static final String DETAILS_SCORE_SUFFIX = "-srDetailsScore";
   private static final String DETAILS_CREATE_DATE_SUFFIX = "-srDetailsCreateDate";   
   private static final String DETAILS_ADD_TO_COL_CONTAINER_SUFFIX = "-srFlipBoxContainer";
   
   private static final String DETAILS_VIEW_COUNT_SUFFIX = "-srDetailsViewCount";
   private static final String DETAILS_COLLECTION_COUNT_SUFFIX = "-srDetailsCollectionCount";
   
   private static final String FLIP_ADD_TO_COL_SUFFIX = "-srFlipAddToCollection";
   private static final String ADD_TO_COL_LINK_SUFFIX = "-srAddToCollectionLink";
   
   private static final String FLIP_NEW_RESOURCE_SUFFIX = "-srFlipNewResource";
   private static final String NEW_RESOURCE_LINK_SUFFIX = "-srNewResourceLink";
   
   private static final String FLIP_FIND_SIMILAR_SUFFIX = "-srFlipFindSimilar";
   private static final String FIND_SIMILAR_LINK_SUFFIX = "-srFindSimilarLink";

   private static final String DESC_FULL_STATUS = "full";
   private static final String DESC_SHORT_STATUS = "short";

   private static final String SHOW_MORE_DESC = "More...";
   private static final String SHOW_LESS_DESC = "Less...";

   private static final String DEFAULT_THUMBNAIL_URL = "images/thumbnail-search-result.jpg";
   private static final String DEFAULT_BOOKSHARE_THUMBNAIL_URL = "images/bookShare-default-tn.png";

   private static final String BOOKSHARE_URL = "www.bookshare.org";
   
   private static final String ACTIVE_TOGGLE_STYLE = "display:block;font-size:12px;font-weight:bold";
   
   private static final String NO_RATINGS_MODIFY = "Be the first to rate this resource!";
   private static final String NO_RATINGS_VIEW = "No ratings available for this resource";
   
   private static final String NO_COMMENTS_MODIFY = "Be the first to comment on this resource!";
   private static final String NO_COMMENTS_VIEW = "No comments available for this resource";

   private HashMap<String,String> shortDescMap = new HashMap<String,String>();
   private HashMap<String,String> fullDescMap = new HashMap<String,String>();
   
   private RatingHandlerType ratingHandlerType = RatingHandlerType.NONE;
   private CommentHandlerType commentHandlerType = CommentHandlerType.NONE;
   
   private static final String HOVER_EVENT = "onmouseover";
   private static final String LEAVE_EVENT = "onmouseleave";
   private static final String TITLE_ATTR = "title";
   private static final String FLIPPED_CLASS = "flip-container flip";
   private static final String NON_FLIPPED_CLASS = "flip-container";
   
   private RegistryResourceActionHandler actionHandler;

   private final String viewCountProxyUrl = DsESBApi.getESBActionURL("decalsIncrementViewsAndGo")+"?resourceUrl=";
   
   private String wrapResourceUrl(String url){
	   return viewCountProxyUrl+url;
   }
   
   //Determines if the given URL is from bookshare.org   
   private boolean isBookshareResource(String url) {
      if (url == null ||  url.trim().isEmpty()) return false;
      else return url.indexOf(BOOKSHARE_URL) > -1;
   }

   //Listener for description toggle
   protected EventCallback descToggleListener = new EventCallback() {
      @Override
      public void onEvent(Event event) {
         Element e = Element.as(event.getEventTarget());
         String token = DsUtil.getTokenFromWidgetId(e.getId(),DETAILS_DESC_TOGGLE_SUFFIX);
         if (DESC_SHORT_STATUS.equals(DsUtil.getLabelText(token + DETAILS_DESC_TOGGLE_STATUS))) {
            DsUtil.setLabelText(token + DETAILS_DESC_SUFFIX,fullDescMap.get(token + DETAILS_DESC_SUFFIX));
            DsUtil.setLabelText(token + DETAILS_DESC_TOGGLE_STATUS,fullDescMap.get(DESC_FULL_STATUS));
            DsUtil.setAnchorText(token + DETAILS_DESC_TOGGLE_SUFFIX, SHOW_LESS_DESC);            
         }
         else {
            DsUtil.setLabelText(token + DETAILS_DESC_SUFFIX, shortDescMap.get(token + DETAILS_DESC_SUFFIX));
            DsUtil.setLabelText(token + DETAILS_DESC_TOGGLE_STATUS, DESC_SHORT_STATUS);
            DsUtil.setAnchorText(token + DETAILS_DESC_TOGGLE_SUFFIX, SHOW_MORE_DESC);            
         }
      }
   };

   //Listens for errors retrieving thumbnail images
   protected EventCallback thumbnailErrorListener = new EventCallback() {
      @Override
      public void onEvent(Event event) {
         Element e = Element.as(event.getEventTarget());
         DsUtil.setImageUrl(e.getId(),DEFAULT_THUMBNAIL_URL);         
      }
   };

   //Tries to replace apostrophe special characters with ASCII apostrophes
   //This needs to be moved out of here and into a separate LR cleanup process
   private String cleanApostropheString(String str) {
      String temp = str;
      temp = temp.replaceAll("&#39;", "'");
      temp = temp.replaceAll("&#039;", "'");
      temp = temp.replaceAll("&#0039;", "'");
      temp = temp.replaceAll("&#34;", "\"");
      temp = temp.replaceAll("&#034;", "\"");
      temp = temp.replaceAll("&#0034;", "\"");
      return temp;
   }

   //Adds the appropriate thumbnail image values
   private void addThumbnailImage(String token, RegistrySearchResult sr) {
      String thumbnailUrl = null;
      if (sr.getHasScreenshot()) thumbnailUrl = sr.getThumbnailImageUrl();
      if (thumbnailUrl == null)  {
         if (isBookshareResource(sr.getResourceUrl())) thumbnailUrl = DEFAULT_BOOKSHARE_THUMBNAIL_URL;
         else thumbnailUrl = DEFAULT_THUMBNAIL_URL; 
      }
      PageAssembler.attachHandler(token + THUMBNAIL_IMAGE_SUFFIX,Event.ONERROR,thumbnailErrorListener);
      DsUtil.setImageUrl(token + THUMBNAIL_IMAGE_SUFFIX,thumbnailUrl);      
   }
   
   //Adds the appropriate source field values
   private void addSourceFields(String token, RegistrySearchResult sr) {
      if (sr.getPublisher() != null && !sr.getPublisher().trim().isEmpty()) DsUtil.setLabelText(token + DETAILS_SOURCE_SUFFIX, sr.getPublisher());
      else if (sr.getAuthor() != null && !sr.getAuthor().trim().isEmpty()) DsUtil.setLabelText(token + DETAILS_SOURCE_SUFFIX, sr.getAuthor());            
      else DsUtil.setLabelText(token + DETAILS_SOURCE_SUFFIX, "N/A");
   }
   
   //Sets up the description field
   private void setUpDescriptionFields(String token, RegistrySearchResult sr) {
      shortDescMap.put(token + DETAILS_DESC_SUFFIX, cleanApostropheString(sr.getShortDescription()));
      fullDescMap.put(token + DETAILS_DESC_SUFFIX, cleanApostropheString(sr.getDescription()));
      DsUtil.setLabelText(token + DETAILS_DESC_SUFFIX, cleanApostropheString(sr.getShortDescription()));
      if (sr.hasLongDescription()) {
         DsUtil.setAnchorStyle(token + DETAILS_DESC_TOGGLE_SUFFIX,ACTIVE_TOGGLE_STYLE);
         PageAssembler.attachHandler(token + DETAILS_DESC_TOGGLE_SUFFIX,Event.ONCLICK,descToggleListener);
         DsUtil.setAnchorText(token + DETAILS_DESC_TOGGLE_SUFFIX,SHOW_MORE_DESC);
      }
      DsUtil.setLabelText(token + DETAILS_DESC_TOGGLE_STATUS,DESC_SHORT_STATUS);      
   }
   
   //Populates all widget elements with the given token with the appropriate values from the given search result.
   private void assignSearchResultWidgetValues(String token, RegistrySearchResult sr) {
      DsUtil.setAnchorTarget(token + THUMBNAIL_ANCHOR_SUFFIX, sr.getTitle());
      DsUtil.setAnchorTarget(token + DETAILS_ANCHOR_SUFFIX, sr.getTitle());
      
      String proxiedResourceUrl = wrapResourceUrl(sr.getResourceUrl());
      
      DsUtil.setAnchorHref(token + THUMBNAIL_ANCHOR_SUFFIX, proxiedResourceUrl);
      DsUtil.setAnchorHref(token + DETAILS_ANCHOR_SUFFIX, proxiedResourceUrl);  
      addThumbnailImage(token,sr);
      DsUtil.setLabelText(token + DETAILS_TITLE_SUFFIX, cleanApostropheString(sr.getTitle()));
      DsUtil.setLabelText(token + DETAILS_LINK_SUFFIX, sr.getTruncatedResourceUrl());
      addSourceFields(token,sr);
      setUpDescriptionFields(token,sr);
   }

   //Assigns interactive specific search result values
   private void assignInteractiveSearchResultWidgetValues(String token, InteractiveSearchResult sr) {
      DsUtil.setLabelText(token + DETAILS_SCORE_SUFFIX, sr.getScore());
      DsUtil.setLabelText(token + DETAILS_CREATE_DATE_SUFFIX, sr.getCreatedDateStr());
      if (sr.getUrlStatus().startsWith("3")) DsUtil.showLabel(token + DETAILS_REDIRECT_WARNING_SUFFIX);
      else DsUtil.hideLabel(token + DETAILS_REDIRECT_WARNING_SUFFIX);      
   }
   
   //build rating info string
   private static String buildRatingInfoString(ResourceRatingSummaryInfo ratingSummary,RatingHandlerType ratingHandlerType) {
      if (ratingSummary.getNumberOfRatings() == 0) {
         if (RatingHandlerType.MODIFY.equals(ratingHandlerType)) return NO_RATINGS_MODIFY;
         else return NO_RATINGS_VIEW;
      }
      return "Current rating: " + ratingSummary.getAverageRating() + " (" + ratingSummary.getNumberOfRatings() + " votes)";
   }
   
   //determine fill width
   public static int getRatingsFillWidth(ResourceRatingSummaryInfo ratingSummary) {
      if (ratingSummary.getNumberOfRatings() == 0) return 0;
      int ret = (int) (((double) ratingSummary.getAverageRating() / 5) * 100);
      return ret;
   }
   
   /**
    * Updates a results rating info with the values of ratingInfo.
    * widgetId is used to determine the proper token value.
    * 
    * @param widgetId A widget ID containing the result token
    * @param ratingInfo The rating information
    */
   public static void updateRatingInfo(String widgetId, ResourceRatingSummaryInfo ratingInfo, RatingHandlerType ratingHandlerType) {
      updateRatingInfoHaveToken(DsUtil.getTokenFromWidgetId(widgetId), ratingInfo, ratingHandlerType);
   }
   
   //update rating info
   private static void updateRatingInfoHaveToken(String token, ResourceRatingSummaryInfo ratingInfo, RatingHandlerType ratingHandlerType) {
      DsUtil.setLabelText(token + DETAILS_RATING_INFO_SUFFIX,buildRatingInfoString(ratingInfo, ratingHandlerType));
      PageAssembler.setWidth(DOM.getElementById(token + DETAILS_RATING_FILL_SUFFIX), getRatingsFillWidth(ratingInfo) + "%");
   }   
   
   //add rating info
   private void addRatingInfo(HashMap<String,String> urlTokenMap, ArrayList<ResourceRatingSummaryInfo> ratingInfoList) {
      for (ResourceRatingSummaryInfo ratingInfo:ratingInfoList) {         
         updateRatingInfoHaveToken(urlTokenMap.get(ratingInfo.getResourceUrl()),ratingInfo,ratingHandlerType);
      }
   }
   
   //fetch rating info
   private void fetchRatingInfo(final HashMap<String,String> urlTokenMap) {
      if (RatingHandlerType.VIEW.equals(ratingHandlerType) || RatingHandlerType.MODIFY.equals(ratingHandlerType)) {
         DsESBApi.decalsGetRatingInfoForUrls(DsUtil.getKeyList(urlTokenMap), new ESBCallback<ESBPacket>() {
            @Override
            public void onSuccess(ESBPacket result) {
               addRatingInfo(urlTokenMap,ResourceRatingParser.parseRatingSummaryList(result.getObject(ESBApi.ESBAPI_RETURN_OBJ)));
            }
            @Override
            public void onFailure(Throwable caught) {}
         });
      }
   }
   
   /**
    * Updates a results comment info with the values of commentInfo.
    * widgetId is used to determine the proper token value.
    * 
    * @param widgetId A widget ID containing the result token
    * @param commentInfo The comment information
    */
   public static void updateCommentInfo(String widgetId, ResourceCommentSummaryInfo commentInfo, CommentHandlerType commentHandlerType) {
      updateCommentInfoHaveToken(DsUtil.getTokenFromWidgetId(widgetId), commentInfo, commentHandlerType);
   }
   
   //build comment info string
   private static String buildCommentInfoString(ResourceCommentSummaryInfo commentInfo, CommentHandlerType commentHandlerType) {
      if (commentInfo.getNumberOfComments() == 0) {
         if (CommentHandlerType.MODIFY.equals(commentHandlerType)) return NO_COMMENTS_MODIFY;
         else return NO_COMMENTS_VIEW;
      }
      else {
         if (CommentHandlerType.MODIFY.equals(commentHandlerType)) return commentInfo.getNumberOfComments() + " comments available for this resource (Add your own!)";
         else return commentInfo.getNumberOfComments() + " comments available for this resource";
      }      
   }
   
   //update comment info
   private static void updateCommentInfoHaveToken(String token, ResourceCommentSummaryInfo commentInfo, CommentHandlerType commentHandlerType) {
      DsUtil.setLabelText(token + DETAILS_COMMENTS_INFO_SUFFIX,buildCommentInfoString(commentInfo, commentHandlerType));
    }
   
   //add comment info
   private void addCommentInfo(HashMap<String,String> urlTokenMap, ArrayList<ResourceCommentSummaryInfo> commentInfoList) {
      for (ResourceCommentSummaryInfo commentInfo:commentInfoList) {         
         updateCommentInfoHaveToken(urlTokenMap.get(commentInfo.getResourceUrl()),commentInfo,commentHandlerType);
      }
   }
   
   //fetch comment info
   private void fetchCommentInfo(final HashMap<String,String> urlTokenMap) {
      if (CommentHandlerType.VIEW.equals(commentHandlerType) || CommentHandlerType.MODIFY.equals(commentHandlerType)) {
         DsESBApi.decalsGetCommentInfoForUrls(DsUtil.getKeyList(urlTokenMap), new ESBCallback<ESBPacket>() {
            @Override
            public void onSuccess(ESBPacket result) {
               addCommentInfo(urlTokenMap,ResourceCommentParser.parseCommentSummaryList(result.getObject(ESBApi.ESBAPI_RETURN_OBJ)));
            }
            @Override
            public void onFailure(Throwable caught) {}
         });
      }
   }
   	
   	private static void updateParadataInfoHaveToken(String token, ResourceParadata paradata) {
   		DsUtil.setLabelText(token + DETAILS_VIEW_COUNT_SUFFIX, paradata.getViewCount());
   		DsUtil.setLabelText(token + DETAILS_COLLECTION_COUNT_SUFFIX, paradata.getCollectionCount());
	}
   
   	private void addParadataInfo(HashMap<String,String> urlTokenMap, ArrayList<ResourceParadata> paradataList){
	   for (ResourceParadata paradata : paradataList) {         
	         updateParadataInfoHaveToken(urlTokenMap.get(paradata.getResourceUrl()), paradata);
	      }
   	}
   
   	private void fetchParadata(final HashMap<String, String> urlTokenMap){
   		DsESBApi.decalsGetParadataForUrls(DsUtil.getKeyList(urlTokenMap), new ESBCallback<ESBPacket>(){

		@Override
		public void onFailure(Throwable caught) {}

		@Override
		public void onSuccess(ESBPacket esbPacket) {
			addParadataInfo(urlTokenMap, parseParadataList(esbPacket.getObject(ESBApi.ESBAPI_RETURN_OBJ)));
		}
		   
   		});
   	}
   
   private ArrayList<ResourceParadata> parseParadataList(JSONObject responseObj){
	   Set<String> keys = responseObj.keySet();
	   ArrayList<ResourceParadata> paradata = new ArrayList<ResourceParadata>();
	   for(String key: keys){
		   JSONValue val = responseObj.get(key);
		   
		   paradata.add(new ResourceParadata(val.isObject()));
	   }
	   
	   return paradata;
   }
   
   //add rating event handlers
   private void addRatingAndCommentHandlers(String token, InteractiveSearchResult sr) {
      if (RatingHandlerType.MODIFY.equals(ratingHandlerType)) {
         actionHandler.addRatingClickListener(token + DETAILS_RATING_1_SUFFIX,sr.getResourceUrl(),1);
         actionHandler.addRatingClickListener(token + DETAILS_RATING_2_SUFFIX,sr.getResourceUrl(),2);
         actionHandler.addRatingClickListener(token + DETAILS_RATING_3_SUFFIX,sr.getResourceUrl(),3);
         actionHandler.addRatingClickListener(token + DETAILS_RATING_4_SUFFIX,sr.getResourceUrl(),4);
         actionHandler.addRatingClickListener(token + DETAILS_RATING_5_SUFFIX,sr.getResourceUrl(),5);
      }      
      if (CommentHandlerType.MODIFY.equals(commentHandlerType) || CommentHandlerType.VIEW.equals(commentHandlerType) ) {
         actionHandler.addCommentClickListener(token + DETAILS_COMMENTS_LINK_SUFFIX, sr.getResourceUrl());
      }
   }
   
  // build the on add to collection on hover action
  private String getAddToColOnHoverAction(String token) {
     return "document.getElementById('" + token + FLIP_ADD_TO_COL_SUFFIX + "').className='" + FLIPPED_CLASS + "';";
  }
  
//build the on add to collection on leave action
  private String getAddToColOnLeaveAction(String token) {
     return "document.getElementById('" + token + FLIP_ADD_TO_COL_SUFFIX + "').className='" + NON_FLIPPED_CLASS + "';";
  }
   
   //build the add to collection widgets
   private void buildAddToCollectionWidgets(String token, boolean build, InteractiveSearchResult isr) {
      if (!build) return;
      if (!DsSession.userHasModifiableCollections()){ 
    	 DsUtil.hideLabel(token + token + FLIP_ADD_TO_COL_SUFFIX); 
    	 DsUtil.hideLabel(token + ADD_TO_COL_LINK_SUFFIX);
      }else {
         DOM.getElementById(token + ADD_TO_COL_LINK_SUFFIX).setAttribute(TITLE_ATTR,"Add '" + isr.getTitle() + "' to a collection");
         DOM.getElementById(token + FLIP_ADD_TO_COL_SUFFIX).setAttribute(HOVER_EVENT,getAddToColOnHoverAction(token));      
         DOM.getElementById(token + FLIP_ADD_TO_COL_SUFFIX).setAttribute(LEAVE_EVENT,getAddToColOnLeaveAction(token));
         actionHandler.addAddToCollectionClickListener(token + ADD_TO_COL_LINK_SUFFIX,isr);
      }
   }
   
   // build the on add to collection on hover action
  private String getNewResourceOnHoverAction(String token) {
     return "document.getElementById('" + token + FLIP_NEW_RESOURCE_SUFFIX + "').className='" + FLIPPED_CLASS + "';";
  }
  
  //build the on add to collection on leave action
  private String getNewResourceOnLeaveAction(String token) {
     return "document.getElementById('" + token + FLIP_NEW_RESOURCE_SUFFIX + "').className='" + NON_FLIPPED_CLASS + "';";
  }
   
   
   //build the add to collection widgets
   private void buildNewResourceWidgets(String token, boolean build, InteractiveSearchResult isr) {
      if (!build) return;
      DOM.getElementById(token + NEW_RESOURCE_LINK_SUFFIX).setAttribute(TITLE_ATTR,"Add Similar/Duplicate Resource");
      DOM.getElementById(token + FLIP_NEW_RESOURCE_SUFFIX).setAttribute(HOVER_EVENT, getNewResourceOnHoverAction(token));      
      DOM.getElementById(token + FLIP_NEW_RESOURCE_SUFFIX).setAttribute(LEAVE_EVENT, getNewResourceOnLeaveAction(token));
      actionHandler.addNewResourceClickListener(token + NEW_RESOURCE_LINK_SUFFIX,isr);
   }
   
   // build the on add to collection on hover action
   private String getFindSimilarOnHoverAction(String token) {
      return "document.getElementById('" + token + FIND_SIMILAR_LINK_SUFFIX + "').className='" + FLIPPED_CLASS + "';";
   }
   
   //build the on add to collection on leave action
   private String getFindSimilarOnLeaveAction(String token) {
      return "document.getElementById('" + token + FIND_SIMILAR_LINK_SUFFIX + "').className='" + NON_FLIPPED_CLASS + "';";
   }
   
   private void buildFindSimilarWidgets(String token, boolean build, InteractiveSearchResult isr) {
      if (!build) return;
      DOM.getElementById(token + FIND_SIMILAR_LINK_SUFFIX).setAttribute(TITLE_ATTR,"Find Similar Resources");
      DOM.getElementById(token + FLIP_FIND_SIMILAR_SUFFIX).setAttribute(HOVER_EVENT, getFindSimilarOnHoverAction(token));      
      DOM.getElementById(token + FLIP_FIND_SIMILAR_SUFFIX).setAttribute(LEAVE_EVENT, getFindSimilarOnLeaveAction(token));
      actionHandler.addFindSimilarClickListener(token + FIND_SIMILAR_LINK_SUFFIX,isr);
   }
   
   
   /**
    * Builds a set of Basic Search result widgets and adds them to the given results container
    * 
    * @param resultList The result list
    * @param resultsContainerId The id of the results container element
    * @param widgetText The widget text
    */
   public void addBasicSearchResultWidgets(ArrayList<BasicSearchResult> resultList, String resultsContainerId, String widgetText) {
      Vector<String> widgetIds;
      ArrayList<String> tokens;
      for (BasicSearchResult bsr:resultList) {
         widgetIds = PageAssembler.inject(resultsContainerId, DEFAULT_WIDGET_ID_TOKEN, new HTML(widgetText), false);
         tokens = DsUtil.getWidgetElementTokenList(widgetIds);
         for (String token:tokens) assignSearchResultWidgetValues(token,bsr);
      }
   }

   /**
    * Builds a set of Interactive Search result widgets and adds them to the given results container
    * 
    * @param resultList The result list
    * @param resultsContainerId The id of the results container element
    * @param widgetText The widget text
    * @param ratingHandlerType How to handle ratings
    * @param commentHandlerType How to handle comments
    * @param actionHandler The resource action handler
    */
   public void addInteractiveSearchResultWidgets(ArrayList<InteractiveSearchResult> resultList, String resultsContainerId, String widgetText,
         RatingHandlerType ratingHandlerType, CommentHandlerType commentHandlerType, RegistryResourceActionHandler actionHandler, boolean buildAddToCollectionWidgets) {
      Vector<String> widgetIds;
      ArrayList<String> tokens;
      this.ratingHandlerType = ratingHandlerType;
      this.commentHandlerType = commentHandlerType;
      this.actionHandler = actionHandler;
      HashMap<String,String> urlTokenMap = new HashMap<String,String>();
      for (InteractiveSearchResult isr:resultList) {
         widgetIds = PageAssembler.inject(resultsContainerId, DEFAULT_WIDGET_ID_TOKEN, new HTML(widgetText), false);
         tokens = DsUtil.getWidgetElementTokenList(widgetIds);
         for (String token:tokens) {
            urlTokenMap.put(isr.getResourceUrl(),token);
            assignSearchResultWidgetValues(token,isr);
            assignInteractiveSearchResultWidgetValues(token,isr);  
            addRatingAndCommentHandlers(token,isr);
            buildFindSimilarWidgets(token, buildAddToCollectionWidgets, isr);
            buildNewResourceWidgets(token, buildAddToCollectionWidgets, isr);
            buildAddToCollectionWidgets(token, buildAddToCollectionWidgets,isr);
         }
      }
      fetchRatingInfo(urlTokenMap);
      fetchCommentInfo(urlTokenMap);
      fetchParadata(urlTokenMap);
   }

}

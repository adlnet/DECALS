package com.eduworks.decals.ui.client.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import com.eduworks.decals.ui.client.DsSession;
import com.eduworks.decals.ui.client.handler.DarResourceActionHandler;
import com.eduworks.decals.ui.client.model.DecalsApplicationRepositoryRecord;
import com.eduworks.gwt.client.net.callback.EventCallback;
import com.eduworks.gwt.client.pagebuilder.PageAssembler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HTML;

/**
 * Generates DECALS application repository (DAR) search results widgets.
 * 
 * @author Eduworks Corporation
 *
 */
public class DarSearchResultWidgetGenerator {
   
private static final String DEFAULT_WIDGET_ID_TOKEN = "x";
   
   private static final String THUMBNAIL_ANCHOR1_SUFFIX = "-srThumbnailAnchor1";
   private static final String THUMBNAIL_ANCHOR2_SUFFIX = "-srThumbnailAnchor2";
   private static final String THUMBNAIL_IMAGE_SUFFIX = "-srThumbnailImage";
   private static final String DETAILS_ANCHOR_SUFFIX = "-srDetailsAnchor";
   private static final String TITLE_SUFFIX = "-srDetailsTitle";
   private static final String LINK_SUFFIX = "-srDetailsLink";
   private static final String DESC_SUFFIX = "-srDetailsDesc";
   private static final String EMPTY_DESC_SUFFIX = "-srDetailsEmptyDesc";   
   private static final String DESC_TOGGLE_SUFFIX = "-srDetailsToggle";
   private static final String DESC_TOGGLE_STATUS = "-srDetailsToggleStatus";
   private static final String UPLOAD_DATE_SUFFIX = "-srDetailsUploadDate";
   private static final String UPDATE_DATE_SUFFIX = "-srDetailsUpdateDate";
   private static final String TYPE_SUFFIX = "-srDetailsType";
   private static final String MIMETYPE_SUFFIX = "-srDetailsMimeType";
   private static final String FILESIZE_SUFFIX = "-srDetailsFileSize";
   private static final String FILENAME_SUFFIX = "-srDetailsFileName";   
   //private static final String LINK_DIV_SUFFIX = "-srDetailsLinkDiv";
   private static final String MIMETYPE_DIV_SUFFIX = "-srDetailsMimeTypeDiv";
   private static final String FILESIZE_DIV_SUFFIX = "-srDetailsFileSizeDiv";
   private static final String FILENAME_DIV_SUFFIX = "-srDetailsFileNameDiv";
   private static final String UPLOAD_DATE_DIV_SUFFIX = "-srDetailsUploadDateDiv";
   private static final String UPDATE_DATE_DIV_SUFFIX = "-srDetailsUpdateDateDiv";
   private static final String ADD_TO_COLLECTION_SUFFIX = "-srAddToCollection";
   private static final String EDIT_SUFFIX = "-srDetailsEdit";
   private static final String PUBLISH_SUFFIX = "-srDetailsPublish";
   private static final String DELETE_SUFFIX = "-srDetailsDelete";
   private static final String GEN_MD_SUFFIX = "-srDetailsGenerateMd";   
   private static final String ROW_RESULT_SUFFIX = "-srRowResult";
   private static final String ROW_TOOLS_SUFFIX = "-resultTools";
   private static final String FLIP_THUMBNAIL_SUFFIX = "-srFlipThumbnail";
   private static final String LR_ID_DIV_SUFFIX = "-srDetailsLrIdDiv";
   private static final String LR_ID_SUFFIX = "-srDetailsLrId";
   private static final String LR_ANCHOR_SUFFIX = "-srDetailsLrAnchor";
   private static final String LR_PUBLISH_DATE_DIV_SUFFIX = "-srDetailsLrPublishDateDiv";   
   private static final String LR_PUBLISH_DATE_SUFFIX = "-srDetailsLrPublishDate";
   
      
   private static final String DEFAULT_THUMBNAIL = "images/mimeTypes/default-icon.png";
   private static final String WEBPAGE_THUMBNAIL = "images/mimeTypes/webpage-icon.png";
   private static final String TEXTPLAIN_THUMBNAIL = "images/mimeTypes/text-icon.png";
   private static final String HTML_THUMBNAIL = "images/mimeTypes/html-icon.png";
   private static final String AUDIO_THUMBNAIL = "images/mimeTypes/audio-icon.png";  
   private static final String IMAGE_THUMBNAIL = "images/mimeTypes/image-icon.png";
   private static final String VIDEO_THUMBNAIL = "images/mimeTypes/video-icon.png";   
   private static final String RTF_THUMBNAIL = "images/mimeTypes/rtf-icon.png";
   private static final String XML_THUMBNAIL = "images/mimeTypes/xml-icon.png";
   private static final String BINARY_THUMBNAIL = "images/mimeTypes/binary-icon.png";
   private static final String PDF_THUMBNAIL = "images/mimeTypes/pdf-icon.png";
   private static final String FLASH_THUMBNAIL = "images/mimeTypes/flash-icon.png";
   private static final String ZIP_THUMBNAIL = "images/mimeTypes/zip-icon.png";
   private static final String JAVASCRIPT_THUMBNAIL = "images/mimeTypes/javascript-icon.png";
   private static final String MSOFFICE_THUMBNAIL = "images/mimeTypes/msword-icon.png";
   private static final String MSWORD_THUMBNAIL = "images/mimeTypes/msword-icon.png";
   private static final String MSPP_THUMBNAIL = "images/mimeTypes/mspowerpoint-icon.png";
   private static final String MSEXCEL_THUMBNAIL = "images/mimeTypes/msexcel-icon.png";
   private static final String JAVA_THUMBNAIL = "images/mimeTypes/java-icon.png";
   
   private static final String DESC_TOGGLE_ACTIVE_STYLE = "display:block;font-size:12px;font-weight:bold";
      
   private static final String HOVER_EVENT = "onmouseover";
   private static final String LEAVE_EVENT = "onmouseleave";
   private static final String HOVER_COLOR = "#CEE3F6";
   private static final String LEAVE_COLOR = "#FFF";
   private static final String FLIPPED_CLASS = "flip-container flip";
   private static final String NON_FLIPPED_CLASS = "flip-container";
   private static final String HOVER_BORDER = "1px solid #BDBDBD";
   
   private static final String DESC_FULL_STATUS = "full";
   private static final String DESC_SHORT_STATUS = "short";   
   private static final String SHOW_MORE_DESC = "More...";
   private static final String SHOW_LESS_DESC = "Less...";
   
   //private static final String TITLE_ATTR = "title";
   
   private HashMap<String,String> shortDescMap = new HashMap<String,String>();
   private HashMap<String,String> fullDescMap = new HashMap<String,String>();
   
   private DarResourceActionHandler actionHandler;
   
   public DarSearchResultWidgetGenerator(DarResourceActionHandler actionHandler) {
      this.actionHandler = actionHandler;
   }
   
   //Determine which thumbnail image to use
   private String getThumbnailForSearchResult(DecalsApplicationRepositoryRecord sr) {
      String tn = null;
      switch(sr.getBasicMimeType()) {
         case WEBPAGE: tn = WEBPAGE_THUMBNAIL; break;
         case TEXTPLAIN: tn = TEXTPLAIN_THUMBNAIL; break;
         case HTML: tn = HTML_THUMBNAIL; break;
         case AUDIO: tn = AUDIO_THUMBNAIL; break;
         case IMAGE: tn = IMAGE_THUMBNAIL; break;
         case VIDEO: tn = VIDEO_THUMBNAIL; break;
         case RTF: tn = RTF_THUMBNAIL; break;
         case XML: tn = XML_THUMBNAIL; break;
         case BINARY: tn = BINARY_THUMBNAIL; break;
         case PDF: tn = PDF_THUMBNAIL; break;
         case FLASH: tn = FLASH_THUMBNAIL; break;
         case ZIP: tn = ZIP_THUMBNAIL; break;
         case JAVASCRIPT: tn = JAVASCRIPT_THUMBNAIL; break;
         case MSOFFICE: tn = MSOFFICE_THUMBNAIL; break;
         case MSWORD: tn = MSWORD_THUMBNAIL; break;
         case MSPP: tn = MSPP_THUMBNAIL; break;
         case MSEXCEL: tn = MSEXCEL_THUMBNAIL; break;
         case JAVA: tn = JAVA_THUMBNAIL; break;
         default: tn = DEFAULT_THUMBNAIL; break;
      }
      return tn;        
   }
   
   //Retrieve the current token from the element ID
   private String getTokenFromElementId(String elementId, String suffix) {
      return(elementId.substring(0,elementId.indexOf(suffix)));
   }   
   
   //Listener for description toggle
   protected EventCallback descToggleListener = new EventCallback() {
      @Override
      public void onEvent(Event event) {
         Element e = Element.as(event.getEventTarget());
         String token = getTokenFromElementId(e.getId(),DESC_TOGGLE_SUFFIX);
         if (DESC_SHORT_STATUS.equals(DsUtil.getLabelText(token + DESC_TOGGLE_STATUS))) {
            DsUtil.setLabelText(token + DESC_SUFFIX,fullDescMap.get(token + DESC_SUFFIX));
            DsUtil.setLabelText(token + DESC_TOGGLE_STATUS,fullDescMap.get(DESC_FULL_STATUS));
            DsUtil.setAnchorText(token + DESC_TOGGLE_SUFFIX, SHOW_LESS_DESC);            
         }
         else {
            DsUtil.setLabelText(token + DESC_SUFFIX, shortDescMap.get(token + DESC_SUFFIX));
            DsUtil.setLabelText(token + DESC_TOGGLE_STATUS, DESC_SHORT_STATUS);
            DsUtil.setAnchorText(token + DESC_TOGGLE_SUFFIX, SHOW_MORE_DESC);            
         }
      }
   };
   
   //Generates the action string to perform on result hover
   private String getResultOnHoverAction(String token, DecalsApplicationRepositoryRecord sr) {
      StringBuffer sb = new StringBuffer();
      sb.append("this.style.background='" + HOVER_COLOR + "';");
      sb.append("this.style.border='" + HOVER_BORDER + "';");
      sb.append("document.getElementById('" + token + FLIP_THUMBNAIL_SUFFIX + "').className='" + FLIPPED_CLASS + "';");
      sb.append("document.getElementById('" + token + ROW_TOOLS_SUFFIX + "').style.display='block';");
      sb.append("document.getElementById('" + token + UPLOAD_DATE_DIV_SUFFIX + "').style.display='block';");
      sb.append("document.getElementById('" + token + UPDATE_DATE_DIV_SUFFIX + "').style.display='block';");
      sb.append("document.getElementById('" + token + MIMETYPE_DIV_SUFFIX + "').style.display='block';");
      if (sr.isFileType()) {         
         sb.append("document.getElementById('" + token + FILESIZE_DIV_SUFFIX + "').style.display='block';");
         sb.append("document.getElementById('" + token + FILENAME_DIV_SUFFIX + "').style.display='block';");
      }
      if (sr.getLrDocId() != null && !sr.getLrDocId().trim().isEmpty()) {
         sb.append("document.getElementById('" + token + LR_ID_DIV_SUFFIX + "').style.display='block';");
         sb.append("document.getElementById('" + token + LR_PUBLISH_DATE_DIV_SUFFIX + "').style.display='block';");
      }
      return sb.toString();
   }
   
   //Generates the action string to perform on result leave
   private String getResultOnLeaveAction(String token, DecalsApplicationRepositoryRecord sr) {
      StringBuffer sb = new StringBuffer();
      sb.append("this.style.background='" + LEAVE_COLOR + "';");
      sb.append("this.style.border='';");
      sb.append("document.getElementById('" + token + FLIP_THUMBNAIL_SUFFIX + "').className='" + NON_FLIPPED_CLASS + "';");
      sb.append("document.getElementById('" + token + ROW_TOOLS_SUFFIX + "').style.display='none';");
      sb.append("document.getElementById('" + token + UPLOAD_DATE_DIV_SUFFIX + "').style.display='none';");
      sb.append("document.getElementById('" + token + UPDATE_DATE_DIV_SUFFIX + "').style.display='none';");
      sb.append("document.getElementById('" + token + MIMETYPE_DIV_SUFFIX + "').style.display='none';");
      sb.append("document.getElementById('" + token + FILESIZE_DIV_SUFFIX + "').style.display='none';");
      sb.append("document.getElementById('" + token + FILENAME_DIV_SUFFIX + "').style.display='none';");
      sb.append("document.getElementById('" + token + LR_ID_DIV_SUFFIX + "').style.display='none';");
      sb.append("document.getElementById('" + token + LR_PUBLISH_DATE_DIV_SUFFIX + "').style.display='none';");      
      return sb.toString();
   }
   
   //Adds the result hover components
   private void addResultHoverActions(String widgetElementId, DecalsApplicationRepositoryRecord sr) {
      String token = getTokenFromElementId(widgetElementId,ROW_RESULT_SUFFIX);      
      DOM.getElementById(widgetElementId).setAttribute(HOVER_EVENT,getResultOnHoverAction(token,sr));      
      DOM.getElementById(widgetElementId).setAttribute(LEAVE_EVENT,getResultOnLeaveAction(token,sr));      
   }
   
   //Determines if the search result has a description
   private boolean searchResultHasDescription(DecalsApplicationRepositoryRecord sr) {
      if (sr.getDescription() == null || sr.getDescription().trim().isEmpty()) return false;
      return true;
   }
   
   //Sets the result anchor values
   private void setUpResultAnchors(String token, DecalsApplicationRepositoryRecord sr) {
      DsUtil.setAnchorTarget(token + THUMBNAIL_ANCHOR1_SUFFIX, sr.getTitle());
      DsUtil.setAnchorTarget(token + THUMBNAIL_ANCHOR2_SUFFIX, sr.getTitle());
      DsUtil.setAnchorTarget(token + DETAILS_ANCHOR_SUFFIX, sr.getTitle());
      DsUtil.setAnchorHref(token + THUMBNAIL_ANCHOR1_SUFFIX, sr.getUrl());
      DsUtil.setAnchorHref(token + THUMBNAIL_ANCHOR2_SUFFIX, sr.getUrl());
      DsUtil.setAnchorHref(token + DETAILS_ANCHOR_SUFFIX, sr.getUrl());
//      if (sr.isUrlType()) {
//         DsUtil.setAnchorHref(token + THUMBNAIL_ANCHOR1_SUFFIX, sr.getUrl());
//         DsUtil.setAnchorHref(token + THUMBNAIL_ANCHOR2_SUFFIX, sr.getUrl());
//         DsUtil.setAnchorHref(token + DETAILS_ANCHOR_SUFFIX, sr.getUrl());         
//      }
//      else {         
//         DsUtil.setAnchorHref(token + THUMBNAIL_ANCHOR1_SUFFIX,"#");
//         DsUtil.setAnchorHref(token + THUMBNAIL_ANCHOR2_SUFFIX,"#");
//         DsUtil.setAnchorHref(token + DETAILS_ANCHOR_SUFFIX,"#");
//      }       
   }
   
   //Sets up the result description
   private void setUpDescriptionFields(String token, DecalsApplicationRepositoryRecord sr) {
      if (searchResultHasDescription(sr)) {
         DsUtil.hideLabel(token + EMPTY_DESC_SUFFIX);
         shortDescMap.put(token + DESC_SUFFIX, sr.getShortDescription());
         fullDescMap.put(token + DESC_SUFFIX, sr.getDescription());
         DsUtil.setLabelText(token + DESC_SUFFIX, sr.getShortDescription());
         if (sr.hasLongDescription()) {
            DsUtil.setAnchorStyle(token + DESC_TOGGLE_SUFFIX,DESC_TOGGLE_ACTIVE_STYLE);         
            PageAssembler.attachHandler(token + DESC_TOGGLE_SUFFIX,Event.ONCLICK,descToggleListener);
            DsUtil.setAnchorText(token + DESC_TOGGLE_SUFFIX, SHOW_MORE_DESC);            
         }      
         DsUtil.setLabelText(token + DESC_TOGGLE_STATUS, DESC_SHORT_STATUS);
      }      
      else DsUtil.showLabel(token + EMPTY_DESC_SUFFIX);
   }
   
   //Sets up the appropriate learning registry published data
   private void setUpLearningRegistryData(String token, DecalsApplicationRepositoryRecord sr) {
      if (sr.getLrDocId() == null || sr.getLrDocId().trim().isEmpty()) return;
      DsUtil.setLabelText(token + LR_ID_SUFFIX,sr.getLrDocId());
      DsUtil.setLabelText(token + LR_PUBLISH_DATE_SUFFIX,sr.getLrPublishDateStr());
      DsUtil.setAnchorHref(token + LR_ANCHOR_SUFFIX, DsUtil.buildLearningRegistryLink(sr.getLrDocId()));
      DsUtil.setAnchorTarget(token + LR_ANCHOR_SUFFIX,sr.getTitle() + " at the Learning Registry");      
   }
   
   private void setupResutTools(String token, DecalsApplicationRepositoryRecord sr) {
      actionHandler.addEditClickHandler(token + EDIT_SUFFIX, sr.getId());            
      actionHandler.addDeleteClickHandler(token + DELETE_SUFFIX, sr.getId());
      if (sr.canPublishToLr()) actionHandler.addPublishClickHandler(token + PUBLISH_SUFFIX, sr.getId());
      else DsUtil.hideAnchor(token + PUBLISH_SUFFIX);
      if (sr.canAutoGenerateMetadata()) actionHandler.addGenerateMetadataClickHandler(token + GEN_MD_SUFFIX, sr.getId());
      else DsUtil.hideAnchor(token + GEN_MD_SUFFIX);      
      if (DsSession.userHasModifiableCollections()) actionHandler.addAddToCollectionClickListener(token + ADD_TO_COLLECTION_SUFFIX, sr);
      else DsUtil.hideAnchor(token + ADD_TO_COLLECTION_SUFFIX); 
   }
   
   //Populates all widget elements with the given token with the appropriate values from the given search result.
   private void assignDarSearchResultWidgetValues(String token, DecalsApplicationRepositoryRecord sr) {
      setUpResultAnchors(token,sr);
      DsUtil.setImageUrl(token + THUMBNAIL_IMAGE_SUFFIX,getThumbnailForSearchResult(sr));
      DsUtil.setLabelText(token + TITLE_SUFFIX, sr.getTitle());
      DsUtil.setLabelText(token + LINK_SUFFIX, sr.getTruncatedUrl());
      setUpDescriptionFields(token,sr);      
      DsUtil.setLabelText(token + TYPE_SUFFIX, sr.getType().toString());
      DsUtil.setLabelText(token + MIMETYPE_SUFFIX,sr.getMimeType());
      DsUtil.setLabelText(token + FILENAME_SUFFIX,sr.getFileName());
      DsUtil.setLabelText(token + FILESIZE_SUFFIX,DsUtil.getNiceFileSizeString(sr.getFileSizeBytes())); 
      DsUtil.setLabelText(token + UPLOAD_DATE_SUFFIX,sr.getUploadDateStr());
      DsUtil.setLabelText(token + UPDATE_DATE_SUFFIX,sr.getUpdateDateStr());
      setUpLearningRegistryData(token,sr);      
      setupResutTools(token,sr);
      addResultHoverActions(token + ROW_RESULT_SUFFIX,sr);      
   }
   
   /**
    * Builds a set of DAR search result widgets and adds them to the given results container 
    * 
    * @param resultList The result list
    * @param resultsContainerId The id of the results container element
    * @param widgetText The widget text
    */
   public void addDarSearchResultWidgets(ArrayList<DecalsApplicationRepositoryRecord> resultList, String resultsContainerId, String widgetText) {
      Vector<String> widgetIds;      
      ArrayList<String> tokens; 
      for (DecalsApplicationRepositoryRecord dsr:resultList) {
         widgetIds = PageAssembler.inject(resultsContainerId, DEFAULT_WIDGET_ID_TOKEN, new HTML(widgetText), false);     
         tokens = DsUtil.getWidgetElementTokenList(widgetIds);         
         for (String token:tokens) assignDarSearchResultWidgetValues(token,dsr);
      }      
   }

}

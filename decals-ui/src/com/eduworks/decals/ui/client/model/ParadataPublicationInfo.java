package com.eduworks.decals.ui.client.model;

import com.eduworks.decals.ui.client.util.DsUtil;
import com.google.gwt.json.client.JSONObject;

/**
 * 
 * Represents a paradata publication information object
 * 
 * @author Eduworks Corporation
 *
 */
public class ParadataPublicationInfo implements Comparable<ParadataPublicationInfo>{
   
   public static final String LAST_PUBLISH_DATE_KEY = "lastPublishDate";
   public static final String LR_DOC_ID_KEY = "paradataDocId";
   public static final String RESOURCE_TITLE_KEY = "resourceTitle";
   public static final String RESOURCE_URL_KEY = "resourceUrl";
   
   public static final String NEVER_PUBLISHED_STRING = "never";
   
   private String lastPublishDateStr;
   private String lrDocId;
   private String resourceTitle;
   private String resourceUrl;
   
   public ParadataPublicationInfo() {}
   
   /**
    * Constructor that parses out a /decalsGetAllParaPubInfo formatted JSON object 
    * 
    * @param paraPubInfo
    */
   public ParadataPublicationInfo(JSONObject paraPubInfo) {
      if (paraPubInfo.containsKey(RESOURCE_TITLE_KEY)) resourceTitle = paraPubInfo.get(RESOURCE_TITLE_KEY).isString().stringValue();
      if (paraPubInfo.containsKey(RESOURCE_URL_KEY)) resourceUrl = paraPubInfo.get(RESOURCE_URL_KEY).isString().stringValue();
      if (paraPubInfo.containsKey(LR_DOC_ID_KEY)) lrDocId = paraPubInfo.get(LR_DOC_ID_KEY).isString().stringValue();
      parseLastPublishDate(paraPubInfo);
   }
   
   //parses the last publish date into an appropriate value
   private void parseLastPublishDate(JSONObject paraPubInfo) {
      if (paraPubInfo.containsKey(LAST_PUBLISH_DATE_KEY)) {
         if (Long.parseLong(paraPubInfo.get(LAST_PUBLISH_DATE_KEY).isString().stringValue()) == 0) {
            lastPublishDateStr = NEVER_PUBLISHED_STRING;
         }
         else {
            try {
               lastPublishDateStr = DsUtil.getDateFormatLongDate(Long.parseLong(paraPubInfo.get(LAST_PUBLISH_DATE_KEY).isString().stringValue()));
            }
            catch (Exception e) {
               lastPublishDateStr = NEVER_PUBLISHED_STRING;
            }
         }                           
      }
      else {
         lastPublishDateStr = NEVER_PUBLISHED_STRING;
      }
   }
   
   /**
    * {@link ParadataPublicationInfo#lastPublishDateStr}
    */
   public String getLastPublishDateStr() {return lastPublishDateStr;}
   public void setLastPublishDateStr(String lastPublishDateStr) {this.lastPublishDateStr = lastPublishDateStr;}
   
   /**
    * {@link ParadataPublicationInfo#lrDocId}
    */
   public String getLrDocId() {return lrDocId;}
   public void setLrDocId(String lrDocId) {this.lrDocId = lrDocId;}
   
   /**
    * {@link ParadataPublicationInfo#resourceTitle}
    */
   public String getResourceTitle() {return resourceTitle;}
   public void setResourceTitle(String resourceTitle) {this.resourceTitle = resourceTitle;}
   
   /**
    * {@link ParadataPublicationInfo#resourceUrl}
    */
   public String getResourceUrl() {return resourceUrl;}
   public void setResourceUrl(String resourceUrl) {this.resourceUrl = resourceUrl;}
   
   /**
    * Returns a truncated string of the resource URL.
    * 
    * @return Returns a truncated string of the resource URL. 
    */
   public String getTruncatedResourceUrl() {
      if (getResourceUrl() == null || getResourceUrl().trim().isEmpty()) return "";
      if (getResourceUrl().length() <= InteractiveSearchResult.MAX_URL_DISPLAY_LENGTH) return getResourceUrl();
      return getResourceUrl().substring(0,InteractiveSearchResult.MAX_URL_DISPLAY_LENGTH) + "...";
   }

   @Override
   public int compareTo(ParadataPublicationInfo o) {return this.getResourceTitle().compareTo(o.getResourceTitle());}

}

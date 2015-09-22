package com.eduworks.decals.ui.client.model;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

/**
 * Represents a collection item
 * 
 * @author Eduworks Corporation
 *
 */
public class CollectionItem implements Comparable<CollectionItem>{

   public static final String ITEM_INDEX_KEY = "itemIndex";
   public static final String RESOURCE_TITLE_KEY = "resourceTitle";
   public static final String RESOURCE_URL_KEY = "resourceUrl";
   public static final String RESOURCE_DESC_KEY = "resourceDescription";
   
   private static final int TRUNCATED_TITLE_LENGTH = 35;
   private static final int TRUNCATED_URL_LENGTH = 50;
   
   private String resourceUrl;
   private String resourceTitle;
   private String resourceDescription;
   private int itemIndex;
   private String collectionId;
   private String locatorKey;
   
   public CollectionItem() {}
   
   /**
    * Constructor that parses out a collection item formatted JSON object 
    * 
    * @param itemInfo JSON item info
    * @param collectionId The parent collection ID
    * @param locatorKey A temporary unique ID...used for the item sorting HTML
    */
   public CollectionItem(JSONObject itemInfo, String collectionId, String locatorKey) {
      this.collectionId = collectionId;
      this.locatorKey = locatorKey;
      if (itemInfo.containsKey(RESOURCE_TITLE_KEY)) resourceTitle = itemInfo.get(RESOURCE_TITLE_KEY).isString().stringValue();
      if (itemInfo.containsKey(RESOURCE_URL_KEY)) resourceUrl = itemInfo.get(RESOURCE_URL_KEY).isString().stringValue();
      if (itemInfo.containsKey(RESOURCE_DESC_KEY)) resourceDescription = itemInfo.get(RESOURCE_DESC_KEY).isString().stringValue();
      try {
         if (itemInfo.containsKey(ITEM_INDEX_KEY)) itemIndex = Integer.parseInt(itemInfo.get(ITEM_INDEX_KEY).isString().stringValue());
      }         
      catch (Exception e) {itemIndex = Integer.MAX_VALUE;}
   }
   
   /**
    * Builds and returns a JSON representation of the collection item.
    * 
    * @return Returns a JSON representation of the collection item.
    */
   public JSONObject toJson() {
      JSONObject jo = new JSONObject();
      jo.put(RESOURCE_TITLE_KEY,new JSONString(resourceTitle.trim()));
      jo.put(RESOURCE_URL_KEY,new JSONString(resourceUrl.trim()));
      jo.put(RESOURCE_DESC_KEY,new JSONString(resourceDescription.trim()));
      jo.put(ITEM_INDEX_KEY,new JSONString(String.valueOf(itemIndex)));
      return jo;
   }
   
   /**
    * Returns a truncated string of the resource URL.
    * 
    * @return Returns a truncated string of the resource URL. 
    */   
   public String getTruncatedResourceUrl() {
      if (resourceUrl == null || resourceUrl.trim().isEmpty()) return "";
      if (resourceUrl.length() <= TRUNCATED_URL_LENGTH) return resourceUrl;
      return resourceUrl.substring(0,TRUNCATED_URL_LENGTH) + "...";
   }
   
   /**
    * Returns a truncated string of the resource title.
    * 
    * @return Returns a truncated string of the resource title. 
    */   
   public String getTruncatedResourceTitle() {
      if (resourceTitle == null || resourceTitle.trim().isEmpty()) return "";
      if (resourceTitle.length() <= TRUNCATED_TITLE_LENGTH) return resourceTitle;
      return resourceTitle.substring(0,TRUNCATED_TITLE_LENGTH) + "...";
   }
   
   /**
    * {@link CollectionItem#resourceUrl}
    */
   public String getResourceUrl() {return resourceUrl;}
   public void setResourceUrl(String resourceUrl) {this.resourceUrl = resourceUrl;}
   
   /**
    * {@link CollectionItem#resourceTitle}
    */
   public String getResourceTitle() {return resourceTitle;}
   public void setResourceTitle(String resourceTitle) {this.resourceTitle = resourceTitle;}
   
   /**
    * {@link CollectionItem#resourceDescription}
    */
   public String getResourceDescription() {return resourceDescription;}
   public void setResourceDescription(String resourceDescription) {this.resourceDescription = resourceDescription;}
   
   /**
    * {@link CollectionItem#itemIndex}
    */
   public int getItemIndex() {return itemIndex;}
   public void setItemIndex(int itemIndex) {this.itemIndex = itemIndex;}
   
   /**
    * {@link CollectionItem#collectionId}
    */
   public String getCollectionId() {return collectionId;}
   public void setCollectionId(String collectionId) {this.collectionId = collectionId;}
   
   /**
    * {@link CollectionItem#locatorKey}
    */
   public String getLocatorKey() {return locatorKey;}
   public void setLocatorKey(String locatorKey) {this.locatorKey = locatorKey;}
   
   @Override
   public int compareTo(CollectionItem o) {
      if (this.itemIndex == o.getItemIndex()) return 0;
      else if (this.itemIndex < o.getItemIndex()) return -1;
      else return 1;
   }
   
}

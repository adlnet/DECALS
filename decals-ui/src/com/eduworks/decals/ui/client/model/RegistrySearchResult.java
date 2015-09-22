package com.eduworks.decals.ui.client.model;

/**
 * 
 * Base class for search results (both basic and interactive).
 *  
 * @author Eduworks Corporation
 *
 */
public interface RegistrySearchResult {
   
   public final static int MAX_URL_DISPLAY_LENGTH = 100;
   
   public abstract String getTitle();
   public abstract String getResourceUrl();
   public abstract String getTruncatedResourceUrl();
   public abstract boolean getHasScreenshot();
   public abstract String getThumbnailImageUrl();
   public abstract String getDescription();
   public abstract String getShortDescription();
   public abstract String getPublisher();
   public abstract String getAuthor();
   public abstract boolean hasLongDescription();
   
}

package com.eduworks.decals.ui.client.model;

import com.google.gwt.json.client.JSONObject;

/**
 * Represents a result from the basic search
 * 
 * @author Eduworks Corporation
 *
 */
public class BasicSearchResult implements RegistrySearchResult{
	
   private static final String THUMBNAIL_SOURCE_PATTERN_PREFIX = "http://search.learningregistry.net/webcap/";
   private static final String THUMBNAIL_SOURCE_PATTERN_SUFFIX = "/145/screencap.jpg";
   
   private static final int SHORT_DESC_LENGTH = 215;
   
   private String title;
	private String description;
	private String publisher;
	private String resourceUrl;
	private String repositoryId;	
	private boolean hasScreenshot;
	
	public static final String TITLE_KEY = "title";
	public static final String DESCRIPTION_KEY = "description";
	public static final String PUBLISHER_KEY = "publisher";
	public static final String RESOURCE_URL_KEY = "url";
	public static final String REPOSITORY_ID_KEY = "_id";
	public static final String HAS_SCREENSHOT_KEY = "hasScreenshot";
	public static final String SOURCE_KEY = "_source";
	
	/**
	 * Empty BasicSearchResult constructor.
	 */
	public BasicSearchResult() {}
	
	/**
	 * BasicSearchResult constructor.
	 * 
	 * @param title The search result title.
	 * @param description The search result description.
	 * @param publisher The search result publisher.
	 * @param resourceUrl The search result resource URL.
	 * @param repositoryId The search result repository ID.
	 * @param hasScreenshot The search result has screenshot flag.
	 */
	public BasicSearchResult(String title, String description, String publisher, String resourceUrl, String repositoryId, boolean hasScreenshot) {
	   this.title = title;
	   this.description = description;
	   this.publisher = publisher;
	   this.resourceUrl = resourceUrl;
	   this.repositoryId = repositoryId;
	   this.hasScreenshot = hasScreenshot;
	}
	
	/**
    * BasicSearchResult constructor
    * 
    * @param sro JSONObject that contains a basic search result.
    */
   public BasicSearchResult(JSONObject sro) {
      JSONObject sroSource = sro.get(SOURCE_KEY).isObject();
      title = sroSource.get(TITLE_KEY).isString().stringValue();
      description = sroSource.get(DESCRIPTION_KEY).isString().stringValue();
      publisher = sroSource.get(PUBLISHER_KEY).isString().stringValue();
      resourceUrl = sroSource.get(RESOURCE_URL_KEY).isString().stringValue();
      repositoryId = sro.get(REPOSITORY_ID_KEY).isString().stringValue();
//      if (sro.get(HAS_SCREENSHOT_KEY) != null && (sro.get(HAS_SCREENSHOT_KEY) instanceof JSONBoolean)) {       
//         hasScreenshot = sro.get(HAS_SCREENSHOT_KEY).isBoolean().booleanValue();
//      }
//      else hasScreenshot = false;
      this.hasScreenshot = true;
   }
   
	/**
    * {@link BasicSearchResult#title}
    */
	@Override
   public String getTitle() {return title;}
   public void setTitle(String title) {this.title = title;}
   
   /**
    * {@link BasicSearchResult#description}
    */
   @Override
   public String getDescription() {return description;}
   public void setDescription(String description) {this.description = description;}
   
   /**
    * {@link BasicSearchResult#publisher}
    */
   @Override
   public String getPublisher() {return publisher;}
   public void setPublisher(String publisher) {this.publisher = publisher;}
   
   /**
    * {@link BasicSearchResult#resourceUrl}
    */
   @Override
   public String getResourceUrl() {return resourceUrl;}
   public void setResourceUrl(String resourceUrl) {this.resourceUrl = resourceUrl;}
   
   /**
    * {@link BasicSearchResult#repositoryId}
    */
   public String getRepositoryId() {return repositoryId;}
   public void setRepositoryId(String repositoryId) {this.repositoryId = repositoryId;}
   
   /**
    * {@link BasicSearchResult#hasScreenshot}
    */
   @Override
   public boolean getHasScreenshot() {return hasScreenshot;}
   public void setHasScreenshot(boolean hasScreenshot) {this.hasScreenshot = hasScreenshot;}
	
	/**
	 * Returns a string containing the result set information (debugging info).
	 * 
	 * @return Returns a string containing the result set information (debugging info).
	 */
	public String getContents() {
	   StringBuffer sb = new StringBuffer();
	   sb.append("title: " + title + "\n");
	   sb.append("description: " + description + "\n");
	   sb.append("publisher: " + publisher + "\n");
	   sb.append("resourceUrl: " + resourceUrl + "\n");
	   sb.append("repositoryId: " + repositoryId + "\n");
	   sb.append("hasScreenshot: " + hasScreenshot + "\n");
	   return sb.toString();
	}
	
	/**
    * Return the generated thumbnail URL for basic search results.
    */
   @Override
   public String getThumbnailImageUrl() {
      //if (!hasScreenshot) return null;
      return THUMBNAIL_SOURCE_PATTERN_PREFIX + getRepositoryId() + THUMBNAIL_SOURCE_PATTERN_SUFFIX;
   }
	
	/**
    * Returns a truncated version of the description.
    * 
    * @return Returns a truncated version of the description.
    */
	@Override
	public String getShortDescription() {
      if (description.length() > SHORT_DESC_LENGTH) return description.substring(0, SHORT_DESC_LENGTH) + "...";
      else return description;
   }
   
	/**
    * Returns true if the result has a long description.  Returns false otherwise.
    * 
    * @return  Returns true if the result has a long description.  Returns false otherwise.
    */
	@Override
   public boolean hasLongDescription() {return (description.length() > SHORT_DESC_LENGTH)?true:false;}
   
	/**
    * Returns empty string for basic search result/author. (Basic search results don't provide author information.)
    * 
    * @return Returns empty string for basic search result/author. (Basic search results don't provide author information.)
    */
   @Override
   public String getAuthor() {return "";}

   /**
    * Returns a truncated string of the resource URL.
    * 
    * @return Returns a truncated string of the resource URL. 
    */
   @Override
   public String getTruncatedResourceUrl() {
      if (getResourceUrl() == null || getResourceUrl().trim().isEmpty()) return "";
      if (getResourceUrl().length() <= MAX_URL_DISPLAY_LENGTH) return getResourceUrl();
      return getResourceUrl().substring(0,MAX_URL_DISPLAY_LENGTH) + "...";  
   }   

}

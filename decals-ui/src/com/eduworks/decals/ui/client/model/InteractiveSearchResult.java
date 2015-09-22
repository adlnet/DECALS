package com.eduworks.decals.ui.client.model;

import java.util.ArrayList;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

/**
 * Represents a result from the interactive search
 * 
 * @author Eduworks Corporation
 *
 */
public class InteractiveSearchResult implements RegistrySearchResult {
   
   private static final int SHORT_DESC_LENGTH = 195;
   
   private String solrId;
   private String createdDateStr;
   private String lastModifiedDateStr;
   private String title;
   private ArrayList<String> keywords;
   private String source;
   private String description;
   private String contentType;
   private String author;
   private String publisher;
   private String resourceUrl;
   private String score;
   private boolean hasScreenshot = false;
   private String thumbnailImageUrl;
   private String urlStatus;
   
   private ArrayList<String> gradeLevels;
   private String language;
   private String mimetype;
   
   private static final String ID_KEY = "id";
   
   private static final String REG_URL_KEY = "url";
   private static final String REG_TITLE_KEY = "title";   
   private static final String REG_DESC_KEY = "description";
   private static final String REG_AUTHOR_KEY = "author";
   private static final String REG_LAST_MODIFIED_KEY = "last_modified";
   private static final String REG_PUBLISHER_KEY = "publisher";
   private static final String REG_SOURCE_KEY = "source";
   private static final String REG_CREATED_DATE_KEY = "create_date";
   private static final String REG_KEYWORDS_KEY = "keywords";  
   private static final String REG_CONTENT_TYPE_KEY = "content_type";
   private static final String REG_SCORE_KEY = "score";
   private static final String REG_THUMBNAIL_KEY = "thumbnail";
   private static final String REG_URL_STATUS_KEY = "url_status";
   
   public static final String GRADE_LEVELS_KEY = "grade_levels";
   public static final String LANGUAGE_KEY = "language_t";
   public static final String MIME_TYPE_KEY = "mimetype_t";
   
   public JSONObject toJson(){
	   JSONObject jo = new JSONObject();
	   
	   if(solrId != null) jo.put(ID_KEY, new JSONString(solrId));
	   if(resourceUrl != null) jo.put(REG_URL_KEY, new JSONString(resourceUrl));
	   if(title != null) jo.put(REG_TITLE_KEY, new JSONString(title));
	   if(description != null) jo.put(REG_DESC_KEY, new JSONString(description));
	   if(author != null) jo.put(REG_AUTHOR_KEY, new JSONString(author));
	   if(lastModifiedDateStr != null) jo.put(REG_LAST_MODIFIED_KEY, new JSONString(lastModifiedDateStr));
	   if(publisher != null) jo.put(REG_PUBLISHER_KEY, new JSONString(publisher));
	   if(source != null) jo.put(REG_SOURCE_KEY, new JSONString(source));
	   if(createdDateStr != null) jo.put(REG_CREATED_DATE_KEY, new JSONString(createdDateStr));
	   
	   if(keywords != null && keywords.size() > 0){
		   JSONArray words = new JSONArray();
		   for(int i = 0; i < keywords.size(); i++){
			   String word = keywords.get(i);
			   words.set(i, new JSONString(word));
		   }
		   jo.put(REG_KEYWORDS_KEY, words);
	   }
	   
	   if(contentType != null) jo.put(REG_CONTENT_TYPE_KEY, new JSONString(contentType));
	   if(score != null) jo.put(REG_SCORE_KEY, new JSONString(score));
	   if(thumbnailImageUrl != null) jo.put(REG_THUMBNAIL_KEY, new JSONString(thumbnailImageUrl));
	   if(urlStatus != null) jo.put(REG_URL_STATUS_KEY, new JSONString(urlStatus));
	   
	   if(gradeLevels != null && gradeLevels.size() > 0){
		   JSONArray grades = new JSONArray();
		   for(int i = 0 ; i < gradeLevels.size(); i++){
			   String level = gradeLevels.get(i);
			   grades.set(i, new JSONString(level));
		   }
		   jo.put(GRADE_LEVELS_KEY, grades);
	   }
	   
	   if(language != null) jo.put(LANGUAGE_KEY, new JSONString(language));
	   if(mimetype != null) jo.put(MIME_TYPE_KEY, new JSONString(mimetype));
	   
	   return jo;
   }
   
   
   /**
    * InteractiveSearchResult constructor.
    */
   public InteractiveSearchResult() {}
   
   /**
    * {@link InteractiveSearchResult#solrId}
    */
   public String getSolrId() {return solrId;}
   public void setSolrId(String solrId) {this.solrId = solrId;}
   
   /**
    * {@link InteractiveSearchResult#createdDateStr}
    */
   public String getCreatedDateStr() {return createdDateStr;}
   public void setCreatedDateStr(String createdDateStr) {this.createdDateStr = createdDateStr;}
   
   /**
    * {@link InteractiveSearchResult#lastModifiedDateStr}
    */
   public String getLastModifiedDateStr() {return lastModifiedDateStr;}
   public void setLastModifiedDateStr(String lastModifiedDateStr) {this.lastModifiedDateStr = lastModifiedDateStr;}
   
   /**
    * {@link InteractiveSearchResult#title}
    */
   @Override
   public String getTitle() {return title;}
   public void setTitle(String title) {this.title = title;}
   
   /**
    * {@link InteractiveSearchResult#keywords}
    */
   public ArrayList<String> getKeywords() {return keywords;}
   public void setKeywords(ArrayList<String> keywords) {this.keywords = keywords;}
   
   /**
    * {@link InteractiveSearchResult#source}
    */
   public String getSource() {return source;}
   public void setSource(String source) {this.source = source;}
   
   /**
    * {@link InteractiveSearchResult#description}
    */
   @Override
   public String getDescription() {return description;}
   public void setDescription(String description) {this.description = description;}
   
   /**
    * {@link InteractiveSearchResult#contentType}
    */
   public String getContentType() {return contentType;}
   public void setContentType(String contentType) {this.contentType = contentType;}
   
   /**
    * {@link InteractiveSearchResult#author}
    */
   @Override
   public String getAuthor() {return author;}
   public void setAuthor(String author) {this.author = author;}
   
   /**
    * {@link InteractiveSearchResult#publisher}
    */
   @Override
   public String getPublisher() {return publisher;}
   public void setPublisher(String publisher) {this.publisher = publisher;}
   
   /**
    * {@link InteractiveSearchResult#resourceUrl}
    */
   @Override
   public String getResourceUrl() {return resourceUrl;}
   public void setResourceUrl(String resourceUrl) {this.resourceUrl = resourceUrl;}
   
   /**
    * {@link InteractiveSearchResult#urlStatus}
    */
   public String getUrlStatus() {return urlStatus;}
   public void setUrlStatus(String urlStatus) {this.urlStatus = urlStatus;}
   
   /**
    * {@link InteractiveSearchResult#score}
    */
   public String getScore() {return score;}
   public void setScore(String score) {this.score = score;}
   
   /**
    * {@link InteractiveSearchResult#hasScreenshot}
    */
   @Override   
   public boolean getHasScreenshot() {return hasScreenshot;}
   public void setHasScreenshot(boolean hasScreenshot) {this.hasScreenshot = hasScreenshot;}
   
   /**
    * {@link InteractiveSearchResult#thumbnailImageUrl}
    */
   @Override
   public String getThumbnailImageUrl() {return thumbnailImageUrl;}   
   public void setThumbnailImageUrl(String thumbnailImageUrl) {this.thumbnailImageUrl = thumbnailImageUrl;}

   
   public ArrayList<String> getGradeLevels() { return gradeLevels;}
   public void setGradeLevels(ArrayList<String> levels) { this.gradeLevels = levels; }
   
   public String getLanguage() { return language; }
   public void setLanguage(String lang) { this.language = lang; }
   
   public String getMimeType(){ return mimetype; }
   public void setMimeType(String mime) { this.mimetype = mime; }
   
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
   
   /**
    * Returns a short description of the result.
    * 
    * @return  Returns a short description of the result. 
    */
   @Override
   public String toString() {
      return getTitle() + " : " + getDescription() + " : " +  getCreatedDateStr();
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
   
}

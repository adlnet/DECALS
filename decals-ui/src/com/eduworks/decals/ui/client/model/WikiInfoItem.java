package com.eduworks.decals.ui.client.model;

/**
 * Represents wiki info item.
 * 
 * @author Eduworks Corporation
 *
 */
public class WikiInfoItem implements Comparable<WikiInfoItem> {
   
   private String name;
   private long numResults;
   private String searchTerm;
   private String wikiLookupValue;
   
   /**
    * Empty WikiInfoItem constructor
    */
   public WikiInfoItem() {}
   
   /**
    * WikiInfoItem constructor
    * 
    * @param name The name of the wiki info item
    */
   public WikiInfoItem(String name) {
      this.name = name;
   }
   
   /**
    * {@link WikiInfoItem#name}
    */
   public String getName() {return name;}
   public void setName(String name) {this.name = name;}
   
   /**
    * {@link WikiInfoItem#numResults}
    */
   public long getNumResults() {return numResults;}
   public void setNumResults(long numResults) {this.numResults = numResults;}
   
   /**
    * {@link WikiInfoItem#searchTerm}
    */
   public String getSearchTerm() {return searchTerm;}
   public void setSearchTerm(String searchTerm) {this.searchTerm = searchTerm;}
   
   /**
    * Returns the derived {@link WikiInfoItem#wikiLookupValue} if available.  Returns {@link WikiInfoItem#name} otherwise.
    * @return Returns the derived {@link WikiInfoItem#wikiLookupValue} if available.  Returns {@link WikiInfoItem#name} otherwise.
    */
   public String getWikiLookupValue() {
      if (wikiLookupValue == null || wikiLookupValue.trim().isEmpty()) return name;
      else return wikiLookupValue;
   }
   
   /**
    * {@link WikiInfoItem#wikiLookupValue}
    */
   public void setWikiLookupValue(String wikiLookupValue) {this.wikiLookupValue = wikiLookupValue;}
   
   /**
    * Sort by {@link WikiInfoItem#numResults} descending.
    */
   @Override
   public int compareTo(WikiInfoItem otherItem) {
      if (this.getNumResults() == otherItem.getNumResults()) return 0;
      else if (this.getNumResults() < otherItem.getNumResults()) return 1;
      else return -1;
   }
   
}

package com.eduworks.decals.ui.client.model;

import java.util.ArrayList;

/**
 * Helper class for managing a list of {@link InteractiveSearchResult}
 * 
 * @author Eduworks Corporation
 *
 */
public class InteractiveSearchResultSetReturn {
   
   private long numTotalResultsFound;
   private ArrayList<InteractiveSearchResult> searchResults = new ArrayList<InteractiveSearchResult>();
   
   /**
    * {@link InteractiveSearchResultSetReturn#numTotalResultsFound}
    */
   public long getNumTotalResultsFound() {return numTotalResultsFound;}
   public void setNumTotalResultsFound(long numTotalResultsFound) {this.numTotalResultsFound = numTotalResultsFound;}
   
   /**
    * {@link InteractiveSearchResultSetReturn#searchResults}
    */
   public ArrayList<InteractiveSearchResult> getSearchResults() {return searchResults;}
   public void setSearchResults(ArrayList<InteractiveSearchResult> searchResults) {this.searchResults = searchResults;}
   
   /**
    * Returns a subset of {@link InteractiveSearchResultSetReturn#searchResults} based on the given start and count.
    * 
    * @param start The index of the {@link InteractiveSearchResultSetReturn#searchResults} to start the subset. 
    * @param count The number of items to return.
    * @return  Returns a subset of {@link InteractiveSearchResultSetReturn#searchResults} based on the given start and count.
    */
   public ArrayList<InteractiveSearchResult> getSearchResults(int start, int count) {
      try {
         ArrayList<InteractiveSearchResult> retList = new ArrayList<InteractiveSearchResult>();
         int currentIndex = start;
         for (int i=0;i<count;i++){
            currentIndex = start + i;
            if ((searchResults.size() - 1) >= currentIndex) {
               retList.add(searchResults.get(currentIndex));
            }
            else break;
         } 
         return retList;
      }
      catch (Exception e) {
         return new ArrayList<InteractiveSearchResult>();
      }
   }
   
   /**
    * Returns the InteractiveSearchResult with the given URL.  Returns null if not found.
    * 
    * @param resourceUrl The resource URL of the result to find.
    * @return  Returns the InteractiveSearchResult with the given ID.  Returns null if not found.
    */
   public InteractiveSearchResult getSearchResult(String resourceUrl) {
      if (searchResults == null || searchResults.size() == 0) return null;
      for (InteractiveSearchResult sr:searchResults) {
         if (sr.getResourceUrl().equals(resourceUrl)) return sr;
      }
      return null;
   }
   
   /**
    * Clears the list of search results.
    */
   public void clearSearchResults() {if (searchResults != null) searchResults.clear();}
      
}

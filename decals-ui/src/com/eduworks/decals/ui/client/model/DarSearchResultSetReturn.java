package com.eduworks.decals.ui.client.model;

import java.util.ArrayList;

/**
 * Helper class for managing a list of {@link DecalsApplicationRepositoryRecord}
 * 
 * @author Eduworks Corporation
 *
 */
public class DarSearchResultSetReturn {
   
   private long numTotalResultsFound;
   private ArrayList<DecalsApplicationRepositoryRecord> searchResults = new ArrayList<DecalsApplicationRepositoryRecord>();
   
   /**
    * {@link DarSearchResultSetReturn#numTotalResultsFound}
    */
   public long getNumTotalResultsFound() {return numTotalResultsFound;}
   public void setNumTotalResultsFound(long numTotalResultsFound) {this.numTotalResultsFound = numTotalResultsFound;}
   
   /**
    * {@link DarSearchResultSetReturn#searchResults}
    */
   public ArrayList<DecalsApplicationRepositoryRecord> getSearchResults() {return searchResults;}
   public void setSearchResults(ArrayList<DecalsApplicationRepositoryRecord> searchResults) {this.searchResults = searchResults;}
   
   /**
    * Returns a subset of {@link DarSearchResultSetReturn#searchResults} based on the given start and count.
    * 
    * @param start The index of the {@link DarSearchResultSetReturn#searchResults} to start the subset. 
    * @param count The number of items to return.
    * @return  Returns a subset of {@link DarSearchResultSetReturn#searchResults} based on the given start and count.
    */
   public ArrayList<DecalsApplicationRepositoryRecord> getSearchResults(int start, int count) {
      try {
         ArrayList<DecalsApplicationRepositoryRecord> retList = new ArrayList<DecalsApplicationRepositoryRecord>();
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
         return new ArrayList<DecalsApplicationRepositoryRecord>();
      }
   }
   
   /**
    * Returns the DarSearchResult with the given ID.  Returns null if not found.
    * 
    * @param resultId The ID of the result to find.
    * @return  Returns the DarSearchResult with the given ID.  Returns null if not found.
    */
   public DecalsApplicationRepositoryRecord getSearchResult(String resultId) {
      if (searchResults == null || searchResults.size() == 0) return null;
      for (DecalsApplicationRepositoryRecord sr:searchResults) {
         if (sr.getId().equals(resultId)) return sr;
      }
      return null;
   }
   
   /**
    * Clears the list of search results.
    */
   public void clearSearchResults() {if (searchResults != null) searchResults.clear();}

}

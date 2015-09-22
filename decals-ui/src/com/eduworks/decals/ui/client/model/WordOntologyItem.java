package com.eduworks.decals.ui.client.model;

/**
 * Represents an item in a word ontology
 * 
 * @author Eduworks Corporation
 *
 */
public class WordOntologyItem implements Comparable<WordOntologyItem> {
   
   protected String word;
   protected String type;
   protected String definition;
   protected long numResults = 0;
   protected int level;
   protected String searchValue;
   protected String wikiLookupValue;
   
   /**
    * WordOntologyItem constructor
    * 
    * @param word  The word form of the word
    * @param type The item type (definition, hypernym, etc.)
    * @param definition The definition of the word
    * @param level The level of the word hierarchy 
    */
   public WordOntologyItem(String word, String type, String definition, int level) {
      this.word = word;
      this.type = type;
      this.definition = definition;
      this.level = level;
      this.searchValue = word;
   }
   
   /**
    * {@link WordOntologyItem#level}
    */
   public int getLevel() {return level;}
   public void setLevel(int level) {this.level = level;}
   
   /**
    * {@link WordOntologyItem#numResults}
    */
   public long getNumResults() {return numResults;}
   public void setNumResults(long numResults) {this.numResults = numResults;}
   
   /**
    * {@link WordOntologyItem#word}
    */
   public String getWord() {return word;}
   public void setWord(String word) {this.word = word;}
   
   /**
    * {@link WordOntologyItem#type}
    */
   public String getType() {return type;}
   public void setType(String type) {this.type = type;}
   
   /**
    * {@link WordOntologyItem#definition}
    */
   public String getDefinition() {return definition;}
   public void setDefinition(String definition) {this.definition = definition;}
   
   /**
    * {@link WordOntologyItem#searchValue}
    */
   public String getSearchValue() {return searchValue;}
   public void setSearchValue(String searchValue) {this.searchValue = searchValue;}
   
   /**
    * Returns the derived {@link WordOntologyItem#wikiLookupValue} if available.  Returns {@link WordOntologyItem#word} otherwise.
    * @return Returns the derived {@link WordOntologyItem#wikiLookupValue} if available.  Returns {@link WordOntologyItem#word} otherwise.
    */
   public String getWikiLookupValue() {
      if (wikiLookupValue == null || wikiLookupValue.trim().isEmpty()) return word;
      else return wikiLookupValue;
   }

   /**
    * {@link WordOntologyItem#wikiLookupValue}
    */
   public void setWikiLookupValue(String wikiLookupValue) {this.wikiLookupValue = wikiLookupValue;}
      
   /**
    * Sort by {@link WordOntologyItem#numResults} descending.
    */
   @Override
   public int compareTo(WordOntologyItem otherItem) {
      if (this.getNumResults() == otherItem.getNumResults()) return 0;
      else if (this.getNumResults() < otherItem.getNumResults()) return 1;
      else return -1;
   }
   
}

package com.eduworks.decals.ui.client.model;

import java.util.ArrayList;

/**
 * Represents an definition type in a word ontology
 * 
 * @author Eduworks Corporation
 *
 */
public class WordOntologyDefinition extends WordOntologyItem {
   
   private String domain;
   
   private ArrayList<WordOntologyItem> relatedWords = new ArrayList<WordOntologyItem>();

   /**
    * WordOntologyDefinition constructor
    * 
    * @param word  The word form of the word
    * @param type The item type (definition, hypernym, etc.)
    * @param definition The definition of the word
    * @param level The level of the word hierarchy 
    */
   public WordOntologyDefinition(String word, String type, String definition, int level) {super(word,type,definition,level);}
   
   /**
    * {@link WordOntologyDefinition#relatedWords}
    */
   public ArrayList<WordOntologyItem> getRelatedWords() {return relatedWords;}
   public void setRelatedWords(ArrayList<WordOntologyItem> relatedWords) {this.relatedWords = relatedWords;}
   
   /**
    * {@link WordOntologyDefinition#domain}
    */
   public String getDomain() {return domain;}
   public void setDomain(String domain) {this.domain = domain;}
   
   /**
    * Returns the number of related words.
    * 
    * @return Returns the number of related words.
    */
   public int getNumberOfRelatedWords() {return relatedWords.size();}
   
   /**
    * Returns true if the definition has a domain.  Returns false otherwise.
    * 
    * @return  Returns true if the definition has a domain.  Returns false otherwise.
    */
   public boolean hasDomain() {
      if (domain == null ||domain.trim().isEmpty()) return false;
      return true;
   }
   
   /**
    * Sort by number of related words descending.
    */
   @Override
   public int compareTo(WordOntologyItem otherItem) {
      WordOntologyDefinition otherDef = (WordOntologyDefinition) otherItem; 
      if (this.getRelatedWords().size() == otherDef.getRelatedWords().size()) return 0;
      else if (this.getRelatedWords().size() < otherDef.getRelatedWords().size()) return 1;
      else return -1;
   }
   
}

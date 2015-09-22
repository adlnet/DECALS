package com.eduworks.decals.ui.client.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.eduworks.decals.ui.client.model.WordOntologyDefinition;
import com.eduworks.decals.ui.client.model.WordOntologyItem;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;

/**
 * Class used to help manage data retrieved from the decalsDefineWord web service.
 * 
 * Note:  Recently moved this class from a different project into DECALS.  
 * We should be able to clean up the initialization and have this class do the levr calls.  Will
 * 
 * @author Eduworks Corporation
 *
 */
//TODO move levr calls to this class.
public class OntologyHelper {
   
   private static final String DW_DEF_KEY = "definition";
   private static final String DW_LEVEL_KEY = "level";
   private static final String DW_RW_KEY = "relatedWords";
   private static final String DW_TYPE_KEY = "type";
   private static final String DW_DOMAIN_KEY = "domain";
   private static final String DW_WORDFORM_KEY = "wordForm";
   
   public static final String DEFINITION_TYPE = "definition";

   private HashMap<String,String> usedWordForms = new HashMap<String,String>();
   
   private ArrayList<WordOntologyDefinition> definitions = new ArrayList<WordOntologyDefinition>();  
   private WordOntologyDefinition currentDefinition;
   
   private String currentDomain;
   
   /**
    * Returns the list of definitions.
    * 
    * @return Returns the list of definitions.
    */
   public ArrayList<WordOntologyDefinition> getDefinitions() {return definitions;}
   
   
   /**
    * Returns the current definition.
    * 
    * @return Returns the current definition.
    */
   public WordOntologyDefinition getCurrentDefinition() {return currentDefinition;}
   
   /**
    * Registers the given word as having already been 'used'.
    * Note:  Not used that much in the current iteration of interactive search.  Could probably be removed.
    * 
    * @param word The word to register as used.
    */
   public void registerUsedWord(String word) {usedWordForms.put(word.toLowerCase(),"");}
   
   /**
    * Returns the map of used words.
    * Note:  Not used that much in the current iteration of interactive search.  Could probably be removed.
    * 
    * @return Returns the map of used words.
    */
   public HashMap<String,String>getUsedWordForms() {return usedWordForms;}
   
   /**
    * Returns the current word 'domain'.
    * 
    * @return Returns the current word 'domain'.
    */
   public String getCurrentDomain() {return currentDomain;}
   
   /**
    * Sets the current word 'domain'.
    * 
    * @param currentDomain The domain to set to current.
    */
   public void setCurrentDomain(String currentDomain) {this.currentDomain = currentDomain;}
   
//   public void applyCurrentDefinitionWordAsDomain() {
//      if (currentDefinition != null && currentDomain == null) currentDomain = currentDefinition.getWord().toLowerCase();
//   }
   
   /**
    * Applies the definition at the given index as the current definition.
    * 
    * @param idx  The index of the definition of which to set as current.
    */
   public void applyAsCurrentDefintion(int idx) {currentDefinition = getDefinitions().get(idx);}
   
   /**
    * Initializes the ontology helper with the given return data from decalsDefineWord.
    * 
    * @param defArray The decalsDefineWord return data to use for initialization.
    */
   public void initFromDefineWordReturn(JSONArray defArray) {
      parseDefineWordReturn(defArray);
   }
   
   /**
    * Returns true if there are any definitions.  Returns false otherwise.
    * 
    * @return Returns true if there are any definitions.  Returns false otherwise.
    */
   public boolean haveAnyDefinitions() {return definitions.size() > 0;}
   
   /**
    * Returns true if there are any results for any of the definitions.  Returns false otherwise.
    * 
    * @return Returns true if there are any results for any of the definitions.  Returns false otherwise.
    */
   public boolean haveAnyDefinitionResults() {
      int count = 0;
      for (WordOntologyDefinition def:definitions) {
         count += def.getNumResults();
      }
      return count > 0;
   }
      
   /**
    * Returns true if there are any results for any of the related words of the current definition.  Returns false otherwise.
    * 
    * @return Returns true if there are any results for any of the related words of the current definition.  Returns false otherwise.
    */
   public boolean currentDefinitionHasAnyRelationResults() {
      int count = 0;
      for (WordOntologyItem item:currentDefinition.getRelatedWords()) {
         count += item.getNumResults();
      }
      return count > 0;
   }
   
   /**
    * Returns the number of definitions.
    * 
    * @return  Returns the number of definitions.
    */
   public int getNumberOfDefinitions() {return definitions.size();}
   
   /**
    * Returns true if the current definition has any related words.  Returns false otherwise.
    * 
    * @return  Returns true if the current definition has any related words.  Returns false otherwise.
    */
   public boolean currentDefinitionHasRelations() {
      if (currentDefinition == null) return false;
      return currentDefinition.getRelatedWords().size() > 0;
   }
   
   //If there is a domain set, move any definitions that are in that domain to the top of the list.
   private void applyDomainToDefinitionList() {      
      if (currentDomain == null || currentDomain.trim().isEmpty()) return;
      for (WordOntologyDefinition wod:definitions) {
         if (currentDomain.equalsIgnoreCase(wod.getDomain()))  {
            definitions.remove(wod);
            definitions.add(0,wod);
         }
      }
   }
   
   /**
    * Sorts the definition list by number of results then moves any definitions with the current domain to the top if a domain is set.
    */
   public void applyDefinitionOrder() {
      if (definitions != null && definitions.size() > 0) {
         Collections.sort(definitions);
         applyDomainToDefinitionList();         
      }
   }
   
   /**
    * Sorts the current definition's related word list by number of results.
    */
   public void applyCurrentDefinitionWordRelationsOrder() {
      if (currentDefinition != null && currentDefinition.getRelatedWords().size() > 0) {
         Collections.sort(currentDefinition.getRelatedWords());
      }
   }
   
   //Creates a WordOntologyDefinition object from the given JSON data.
   private WordOntologyDefinition getDefinitionFromJSON(JSONObject jo) {
      WordOntologyDefinition ret = new WordOntologyDefinition(jo.get(DW_WORDFORM_KEY).isString().stringValue(), jo.get(DW_TYPE_KEY).isString().stringValue(),
            jo.get(DW_DEF_KEY).isString().stringValue(), (int)jo.get(DW_LEVEL_KEY).isNumber().doubleValue());
      if (jo.containsKey(DW_DOMAIN_KEY)) ret.setDomain(jo.get(DW_DOMAIN_KEY).isString().stringValue());      
      return ret;
   }
   
   //Creates a WordOntologyItem object from the given JSON data.
   private WordOntologyItem getItemFromJSON(JSONObject jo) {
      WordOntologyItem ret = new WordOntologyItem(jo.get(DW_WORDFORM_KEY).isString().stringValue(), jo.get(DW_TYPE_KEY).isString().stringValue(),
            jo.get(DW_DEF_KEY).isString().stringValue(), (int)jo.get(DW_LEVEL_KEY).isNumber().doubleValue());
      return ret;
   }
   
   //Parses the given JSON array into WordOntologyItems and adds them to the given WordOntologyDefinition
   private void addRelatedWordsToDefinition(WordOntologyDefinition def, JSONArray items) {
      JSONObject jo;
      WordOntologyItem item;
      for (int i=0;i<items.size();i++) {
         jo = items.get(i).isObject();
         item = getItemFromJSON(jo);
         if (!hasWordFormBeenUsed(item.getWord())) def.getRelatedWords().add(getItemFromJSON(jo));
      }
   }
   
   //Parses the given JSON array into WordOntologyDefinitions
   private void parseDefineWordReturn(JSONArray defArray) {
      currentDefinition = null;
      definitions.clear();
      JSONObject jo;
      WordOntologyDefinition def;
      for (int i=0;i<defArray.size();i++) {
         jo = defArray.get(i).isObject();
         def = getDefinitionFromJSON(jo);
         if (jo.containsKey(DW_RW_KEY)) addRelatedWordsToDefinition(def,jo.get(DW_RW_KEY).isArray());
         definitions.add(def);         
      }
      if (definitions.size() == 1) currentDefinition = definitions.get(0);
      applyDefinitionOrder();
   }
   
   private ArrayList<String> getSubList(ArrayList<String> list, int size) {
      ArrayList<String> ret = new ArrayList<String>();
      for (int i=0;i<size;i++) {
         if (i < list.size()) ret.add(list.get(i));
      }
      return ret;
   }
   
   //Searches the usedWordForm map for the given word
   private boolean hasWordFormBeenUsed(String word) {return usedWordForms.containsKey(word.toLowerCase());}
   
   //Tries to determine if the given string is a single word 
   private boolean isSingleWord(String word) {
      String temp = word.replaceAll("[^A-Za-z]", " ");
      String[] wa = temp.split(" ");
      return wa.length == 1;
   }
   
   //Returns a list of at most maxNumber of single word related words for the given definition
   private ArrayList<String> getRelatedSingleWordsForDef(WordOntologyDefinition def, int maxNumber) {
      ArrayList<String> ret = new ArrayList<String>();
      String word;
      for (WordOntologyItem woi:def.getRelatedWords()) {
         word = woi.getWord();
         if (isSingleWord(word)) ret.add(word);
      }
      if (ret.size() > maxNumber) return getSubList(ret,maxNumber);
      else return ret;
   }
   
   /**
    * Returns a list of at most maxNumber of single word related words for all definitions.
    * 
    * @param maxNumber The max number of single word related words to return.
    * @return Returns a list of at most maxNumber of single word related words for all definitions.
    */
   public ArrayList<String> getRelatedSingleWordsForAllDefinitions(int maxNumber) {
      ArrayList<String> ret = new ArrayList<String>();
      for (WordOntologyDefinition def:definitions) {
         ret.addAll(getRelatedSingleWordsForDef(def, maxNumber));
      }
      if (ret.size() > maxNumber) return getSubList(ret,maxNumber);
      else return ret;
   }
   
   /**
    * Returns a list of at most maxNumber of single word related words for the current definitions.
    * 
    * @param maxNumber The max number of single word related words to return.
    * @return Returns a list of at most maxNumber of single word related words for the current definitions.
    */
   public ArrayList<String> getRelatedSingleWordsForCurrentDefinition(int maxNumber) {
      return getRelatedSingleWordsForDef(currentDefinition, maxNumber);
   }

}

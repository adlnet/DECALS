package com.eduworks.decals.ui.client.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

import com.eduworks.decals.ui.client.model.WikiInfoItem;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;

/**
 * Class used to help manage data retrieved from the decalsWikiInfo web service.
 * 
 * Note:  Recently moved this class from a different project into DECALS.  
 * We should be able to clean up the initialization and have this class do the levr calls.  Will
 * 
 * Note:  Combined topics and categories into topics (TB 12/10/2014)
 * 
 * @author Eduworks Corporation
 *
 */
//TODO move levr calls to this class.
public class WikiInfoHelper {
   
   /**
    * Helps sort WikiInfoItems into descending name order. 
    */
   public class ReverseWikiItemComparator implements Comparator<WikiInfoItem> {
      @Override
      public int compare(WikiInfoItem a, WikiInfoItem b) {
          return b.getName().compareToIgnoreCase(a.getName());
      }
   }
   
   private static final String WIKI_INFO_RETURN_CATEGORIES_KEY = "categories";
   private static final String WIKI_INFO_RETURN_TOPICS_KEY = "links";
   private static final String WIKI_INFO_RETURN_EXTRACT_KEY = "extract";
   private static final String WIKI_INFO_RETURN_ERROR_KEY = "error";
   
   private static final String WIKI_DISAMBIGUATION_CATEGORY_FLAG = "disambig";
   
   private static final int GOOD_VALUE_SIZE_LOWER_LIMIT = 4;
   private static final int GOOD_VALUE_WORDS_UPPER_LIMIT = 3;
   
   private static final String MARKUP_TITLE_KEY_PREFIX = "mkp_ttl_key_";
   
   private static final String MARKUP_EXTRACT_ANCHOR_PREFIX = "exmkp_";
   
   public static final String CATEGORY_MARKUP_TYPE = "cat_";
   public static final String TOPIC_MARKUP_TYPE = "topic_";
  
   private ArrayList<WikiInfoItem> relatedCategories = new ArrayList<WikiInfoItem>();
   private ArrayList<WikiInfoItem> relatedTopics = new ArrayList<WikiInfoItem>();
   private String descriptionExtract;
   private String nonMarkedDescriptionExtract;
   private boolean hasGoodInfo  = false;
   private String title;
   private HashMap<String,String> extractMarkupMap = new HashMap<String,String>();
   private HashMap<String,String> extractLinkTitleMap = new HashMap<String,String>();
   
   public ArrayList<WikiInfoItem> getRelatedCategories() {return relatedCategories;}
   public ArrayList<WikiInfoItem> getRelatedTopics() {return relatedTopics;}
   public String getDescriptionExtract() {return descriptionExtract;}
   public boolean getHasGoodInfo() {return hasGoodInfo;}
   public HashMap<String,String> getExtractMarkupMap() {return extractMarkupMap;}
   public String getNonMarkedDescriptionExtract() {return nonMarkedDescriptionExtract;}
      
   /**
    * Initializes the WikiInfoHelper from the return of a decalsWikiInfo call.
    * 
    * @param title The title used in the decalsWikiInfo call
    * @param jo The decalsWikiInfo return data
    * @param markupExtract If true, the extract section is marked up with corresponding related category and topic hyperlinks.
    * @param htmlEncodeExtractSpecialCharacters If true, special characters in the extract are HTML encoded (helps with unicode and foreign language).
    */
   public void initFromWikiInfoReturn(String title, JSONObject jo, boolean markupExtract, boolean htmlEncodeExtractSpecialCharacters) {
      this.title = title;
      parseWikiInfoReturn(jo,htmlEncodeExtractSpecialCharacters);
      if (markupExtract) markupExtract();
   }
   
   /**
    * Returns the number of 'good' topics found in the data.
    * 
    * @return Returns the number of 'good' topics found in the data.
    */
   public long getNumberOfGoodTopics() {return relatedTopics.size();}
   
   /**
    * Returns the number of 'good' categories found in the data.
    * 
    * @return  Returns the number of 'good' categories found in the data.
    */
   public long getNumberOfGoodCategories() {return relatedCategories.size();}
   
   
   /**
    * Returns the list index of the given topic.  Returns -1 if topic is not found.
    * 
    * @param topic The topic in which to find the index.
    * @return Returns the list index of the given topic.  Returns -1 if topic is not found.
    */
   public int getRelatedTopicIndex(String topic) {
      if (topic == null || topic.trim().isEmpty()) return -1;
      int count = 0;
      for (WikiInfoItem i:getRelatedTopics()) {
         if (i.getName().equalsIgnoreCase(topic)) return count;
         count++;  
      }
      return -1;
   }
   
   /**
    * Returns the list index of the given category.  Returns -1 if category is not found.
    * 
    * @param category The category in which to find the index.
    * @return Returns the list index of the given category.  Returns -1 if category is not found.
    */
   public int getRelatedCategoryIndex(String category) {
      if (category == null || category.trim().isEmpty()) return -1;
      int count = 0;
      for (WikiInfoItem i:getRelatedCategories()) {
         if (i.getName().equalsIgnoreCase(category)) return count;
         count++;  
      }
      return -1;      
   }
   
   //Looks for 'good' info inside of the return data and populates hasGoodInfo accordingly
   private void checkForGoodInfo(JSONObject jo) {
      if (jo.containsKey(WIKI_INFO_RETURN_ERROR_KEY)) {
         hasGoodInfo = false;
         return;
      }
      if (jo.containsKey(WIKI_INFO_RETURN_CATEGORIES_KEY)) {
         JSONArray ja = jo.get(WIKI_INFO_RETURN_CATEGORIES_KEY).isArray();
         if ((ja.size() == 1) && (ja.get(0).isString().stringValue().toLowerCase().indexOf(WIKI_DISAMBIGUATION_CATEGORY_FLAG) > -1)) {
            hasGoodInfo = false;
            return;
         }
      }
      if (jo.containsKey(WIKI_INFO_RETURN_EXTRACT_KEY)) {
         if (jo.get(WIKI_INFO_RETURN_EXTRACT_KEY).isString().stringValue().trim().endsWith(":")) {
            hasGoodInfo = false;
            return;
         }
      }
      if (jo.containsKey(WIKI_INFO_RETURN_CATEGORIES_KEY) && jo.containsKey(WIKI_INFO_RETURN_EXTRACT_KEY) && 
          jo.containsKey(WIKI_INFO_RETURN_TOPICS_KEY)) {
         hasGoodInfo = true;
         return;
      }
      hasGoodInfo = false;
   }
   
   /**
    * Applies a number of results sort to the related topics list.
    */
   public void applyTopicOrder() {if (relatedTopics != null && relatedTopics.size() > 0) Collections.sort(relatedTopics); }
   
   //Attempts to HTML encode the given string if it is out of the ASCII character range.
   private String encodeSpecialCharacters(String s) {
      StringBuilder buf = new StringBuilder();
      int len = (s == null) ? -1 : s.length();
      for (int i = 0; i < len; i++) {
          char c = s.charAt(i);
          if ((int)c <= 127) buf.append(c);
          else buf.append( "&#" + (int)c + ";" );
      }
      return buf.toString();
   }
   
   //Combine topics and categories (ensure unique items)
   private void combineTopicsAndCategories() {
      HashMap <String,String> uniqueMap = new HashMap<String,String>();
      for (WikiInfoItem wii: relatedTopics) uniqueMap.put(wii.getName(),wii.getName());      
      for (WikiInfoItem wii: relatedCategories) {
         if (uniqueMap.get(wii.getName()) == null) relatedTopics.add(wii);
      }
   }
   
   //Parses the given decalsWikiInfo return data
   private void parseWikiInfoReturn(JSONObject jo, boolean encodeExtract) {
      checkForGoodInfo(jo);
      if (hasGoodInfo) {
         descriptionExtract = jo.get(WIKI_INFO_RETURN_EXTRACT_KEY).isString().stringValue();               
         if (encodeExtract) descriptionExtract = encodeSpecialCharacters(descriptionExtract);
         nonMarkedDescriptionExtract = descriptionExtract;
         relatedCategories = parseGoodValues(jo.get(WIKI_INFO_RETURN_CATEGORIES_KEY).isArray(),false);
         relatedTopics = parseGoodValues(jo.get(WIKI_INFO_RETURN_TOPICS_KEY).isArray(),true);
         
         //combine categories and topics
         combineTopicsAndCategories();
         relatedCategories.clear();
      }
   }
   
   //Builds and returns a map of phrase case variations in the given text.
   private HashMap<String,Long> getPhraseVariations(String text, String phrase) {
      HashMap<String,Long> map = new HashMap<String,Long>();
      int idx = text.toLowerCase().indexOf(phrase.toLowerCase());
      String key;
      while (idx >= 0){
         key = text.substring(idx,idx + phrase.length());
         if (map.containsKey(key)) map.put(key, map.get(key) + 1);
         else map.put(key,new Long(1));         
         idx = text.toLowerCase().indexOf(phrase.toLowerCase(),idx + phrase.length());
      }
      return map;
   }
   
   //Builds and returns an HTML ID attribute based on the given ID string 
   private String getIdString (String id) {return "id=\"" + id + "\"";}
   
   //Builds and returns a key based on the given phrase index so that title attributes can be properly built for markup links.
   private String getTitleKeyString(int phraseIndex) {return MARKUP_TITLE_KEY_PREFIX + phraseIndex;}
   
   //Builds and returns an HTML anchor tag based on the given string value, element ID, and phraseIndex
   private String getAnchorText(String value, String id, int phraseIndex) {      
      String titleKey = getTitleKeyString(phraseIndex);      
      extractLinkTitleMap.put(titleKey, value);
      StringBuffer sb = new StringBuffer();
      sb.append("<a " + getIdString(id) + "  title=\"Search for '" + titleKey + "'\" >");
      sb.append(value);
      sb.append("</a>");
      return sb.toString();
   }
   
   //Attempts to attach hyperlinks to the extract section for all matches to the given phrase.
   private void markupExtractWithPhrase(String phrase, int phraseIndex, String markupType) {
      HashMap<String,Long> map = getPhraseVariations(descriptionExtract,phrase);
      Iterator<String> it = map.keySet().iterator();
      String key;
      int phraseVariationNumber = 0;
      String anchorId;
      String newAnchorId;
      Long variationCount;
      while (it.hasNext()) {
         phraseVariationNumber++;
         anchorId = MARKUP_EXTRACT_ANCHOR_PREFIX + phraseIndex + "_" + phraseVariationNumber + "_" + markupType + phraseIndex;
         key = it.next();
         variationCount = map.get(key);
         descriptionExtract = descriptionExtract.replaceAll(key,getAnchorText(key,anchorId,phraseIndex));
         if (variationCount > 1) {
            for (int i=0;i<variationCount;i++) {
               newAnchorId = anchorId + "_" + i + "_" + markupType + phraseIndex;
               descriptionExtract = descriptionExtract.replaceFirst(getIdString(anchorId), getIdString(newAnchorId));
               extractMarkupMap.put(newAnchorId,key);
            }
         }
         else extractMarkupMap.put(anchorId,key);         
      }      
   }
   
   //Checks to see if any part of the potential markup phrase has already been done.  Do this to avoid nested anchor tags.
   private boolean hasMarkupBeenUsed(String potentialMarkup, ArrayList<String> usedMarkups) {
      for (String usedMarkup:usedMarkups) {
         if (potentialMarkup.toLowerCase().indexOf(usedMarkup.toLowerCase()) != -1) return true;
         if (usedMarkup.toLowerCase().indexOf(potentialMarkup.toLowerCase()) != -1) return true;
      }
      return false;
   }
   
   //Replace all 'Search for ...' strings with lower case versions of the proper phrase.  Helps alleviate multiple phrase case variations.
   private void replaceLinkTitles() {
      Iterator<String> it = extractLinkTitleMap.keySet().iterator();
      String titleKey;
      while (it.hasNext()) {
         titleKey = it.next();
         descriptionExtract = descriptionExtract.replaceAll(titleKey,extractLinkTitleMap.get(titleKey).toLowerCase());
      }
   }
   
   //Attempts to markup the extract section with hyperlinks to related categories and topics.  Hyperlink IDs are stored in
   //the extractMarkupMap which can be used to generate event handlers.
   private void markupExtract() {
      ArrayList<String> usedMarkups = new ArrayList<String>();
      extractMarkupMap.clear();
      extractLinkTitleMap.clear();
      String item;
      ArrayList<WikiInfoItem> tempCatList = relatedCategories;
      Collections.sort(tempCatList,new ReverseWikiItemComparator());
      ArrayList<WikiInfoItem> tempTopicList = relatedTopics;
      Collections.sort(tempTopicList,new ReverseWikiItemComparator());
      for (int i=0;i<tempCatList.size();i++) {
         item = tempCatList.get(i).getName().toLowerCase();
         if (descriptionExtract.toLowerCase().indexOf(item) > -1 && !hasMarkupBeenUsed(item,usedMarkups)) markupExtractWithPhrase(item,i,CATEGORY_MARKUP_TYPE);
      }
      for (int i=0;i<tempTopicList.size();i++) {
         item = tempTopicList.get(i).getName().toLowerCase();
         if (descriptionExtract.toLowerCase().indexOf(item) > -1 && !hasMarkupBeenUsed(item,usedMarkups)) markupExtractWithPhrase(item,i,TOPIC_MARKUP_TYPE);
      }
      replaceLinkTitles();
   }
   
   //Tries to determine if the given value is a 'good' value.
   private boolean isGoodValue(String val, boolean doSizeChecks) {
      if (val == null || val.trim().isEmpty()) return false;
      if (val.toLowerCase().indexOf(title.toLowerCase()) != -1) return false;
      if (title.toLowerCase().indexOf(val.toLowerCase()) != -1) return false;
      if (val.toLowerCase().indexOf("stub") >= 0) return false;          
      if (doSizeChecks) {
         if (val.trim().length() < GOOD_VALUE_SIZE_LOWER_LIMIT) return false;
         if (val.split(" ").length > GOOD_VALUE_WORDS_UPPER_LIMIT) return false;
      }
      return true;
   }
   
   //Removes a bunch of 'odd' values from a string.
   private String cleanValueString(String val) {return val.replaceAll("[^A-Za-z0-9 ().]", "");}
   
   //Attempts to parse 'good' values from the given array.
   private ArrayList<WikiInfoItem> parseGoodValues(JSONArray sourceArray, boolean doSizeChecks) {
      ArrayList<WikiInfoItem> valueList = new ArrayList<WikiInfoItem>();
      String s;
      for (int i=0;i<sourceArray.size();i++) {
         s = cleanValueString(sourceArray.get(i).isString().stringValue().trim());
         if (isGoodValue(s,doSizeChecks)) valueList.add(new WikiInfoItem(s));         
      }  
      return valueList;
   }
   
}

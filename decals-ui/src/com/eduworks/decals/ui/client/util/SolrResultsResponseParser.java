package com.eduworks.decals.ui.client.util;

import java.util.ArrayList;

import com.eduworks.decals.ui.client.model.DarResourceCompetency;
import com.eduworks.decals.ui.client.model.DarResourceMetadata;
import com.eduworks.decals.ui.client.model.DarResourceObjective;
import com.eduworks.decals.ui.client.model.DarSearchResultSetReturn;
import com.eduworks.decals.ui.client.model.DecalsApplicationRepositoryRecord;
import com.eduworks.decals.ui.client.model.InteractiveSearchResult;
import com.eduworks.decals.ui.client.model.InteractiveSearchResultSetReturn;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;

/**
 * Parses return data from the decalsSolrSearch web service.
 * 
 * @author Eduworks Corporation
 *
 */
public class SolrResultsResponseParser {
   
   private static final String SC_NUM_FOUND_KEY = "total";   
   private static final String SC_DOCS_KEY = "items";
   
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
   
   //Transforms a JSON array of strings into an array list of strings.
   private static ArrayList<String> parseJSONStringArray(JSONArray jsa) {
      ArrayList<String> retList = new ArrayList<String>();
      for (int i=0;i<jsa.size();i++) {
         retList.add(jsa.get(i).isString().stringValue());         
      }
      return retList;
   }   
   
   //Try to remove JSON formatted data from the given title string.  Need to add some additions.
   private static String checkTitleStringForJsonContent(String title) {
      if (title != null && title.toLowerCase().startsWith("{\"content\"")) {
         try {
            JSONObject jo = JSONParser.parseStrict(title).isObject();            
            if (jo != null) return jo.get("content").toString();
            else return title;
         }
         catch (Exception e) {return title;}
      }
      else return title;
   }
   
   //Builds an InteractiveSearchResult object from the given JSON document
   private static InteractiveSearchResult parseSolrRegistryDoc(JSONObject doc, String thumbnailRootUrl) throws Exception {
      InteractiveSearchResult sr = new InteractiveSearchResult();      
      if (doc.containsKey(ID_KEY)) sr.setSolrId(doc.get(ID_KEY).isString().stringValue());
      if (doc.containsKey(REG_TITLE_KEY)) sr.setTitle(checkTitleStringForJsonContent(doc.get(REG_TITLE_KEY).isArray().get(0).isString().stringValue()));
      if (doc.containsKey(REG_SOURCE_KEY)) sr.setSource(doc.get(REG_SOURCE_KEY).isString().stringValue());
      if (doc.containsKey(REG_DESC_KEY)) sr.setDescription(doc.get(REG_DESC_KEY).isString().stringValue().replace("<br>", "; "));
      if (doc.containsKey(REG_CONTENT_TYPE_KEY)) sr.setContentType(doc.get(REG_CONTENT_TYPE_KEY).isString().stringValue());
      if (doc.containsKey(REG_AUTHOR_KEY)) sr.setAuthor(doc.get(REG_AUTHOR_KEY).isString().stringValue());
      if (doc.containsKey(REG_PUBLISHER_KEY)) sr.setPublisher(doc.get(REG_PUBLISHER_KEY).isString().stringValue());
      if (doc.containsKey(REG_URL_KEY)) sr.setResourceUrl(doc.get(REG_URL_KEY).isString().stringValue());
      if (doc.containsKey(REG_KEYWORDS_KEY)) sr.setKeywords(parseJSONStringArray(doc.get(REG_KEYWORDS_KEY).isArray()));
      if (doc.containsKey(REG_CREATED_DATE_KEY)) sr.setCreatedDateStr(doc.get(REG_CREATED_DATE_KEY).isString().stringValue());
      if (doc.containsKey(REG_LAST_MODIFIED_KEY)) sr.setLastModifiedDateStr(doc.get(REG_LAST_MODIFIED_KEY).isString().stringValue());   
      if (doc.containsKey(REG_SCORE_KEY)) sr.setScore(String.valueOf(doc.get(REG_SCORE_KEY).isNumber().doubleValue()));
      if (doc.containsKey(REG_URL_STATUS_KEY)) sr.setUrlStatus(doc.get(REG_URL_STATUS_KEY).isString().stringValue());
      
      if (doc.containsKey(InteractiveSearchResult.GRADE_LEVELS_KEY)) sr.setGradeLevels(parseJSONStringArray(doc.get(InteractiveSearchResult.GRADE_LEVELS_KEY).isArray()));
      if (doc.containsKey(InteractiveSearchResult.LANGUAGE_KEY)) sr.setLanguage(doc.get(InteractiveSearchResult.LANGUAGE_KEY).isString().stringValue());
      if (doc.containsKey(InteractiveSearchResult.MIME_TYPE_KEY)) sr.setMimeType(doc.get(InteractiveSearchResult.MIME_TYPE_KEY).isString().stringValue());
      
      if (doc.containsKey(REG_THUMBNAIL_KEY)) {
         sr.setHasScreenshot(true);
         sr.setThumbnailImageUrl(thumbnailRootUrl + doc.get(REG_THUMBNAIL_KEY).isString().stringValue());
      }
      else sr.setHasScreenshot(false);
      return sr;
   }
   
   
   /**
    * Builds and populates an InteractiveSearchResultSetReturn based on the given JSON data and thumbnailRootUrl.
    * 
    * @param responseObject  The response from the decalsSolrSearch web service.
    * @param thumbnailRootUrl  The root URL to use for thumbnail images.
    * @return Returns an InteractiveSearchResultSetReturn built from the given JSON data
    */
//   public static InteractiveSearchResultSetReturn parseSolrCruncherResponse(JSONObject responseObject, String thumbnailRootUrl) {      
//      InteractiveSearchResultSetReturn ret =  new InteractiveSearchResultSetReturn();
//      parseSolrCruncherResponse(responseObject,ret,thumbnailRootUrl);
//      return ret;
//   }
   
   /**
    * Parses the given JSON data and appends the results to the given InteractiveSearchResultSetReturn.
    * To be used for registry search responses
    * 
    * @param responseObject The response from the decalsSolrSearch web service.
    * @param resultSet The result set in which to append new results
    * @param thumbnailRootUrl The root URL to use for thumbnail images.
    */
   public static void parseSolrRegistryResponse(JSONObject responseObject, InteractiveSearchResultSetReturn resultSet, String thumbnailRootUrl) {      
      resultSet.setNumTotalResultsFound((long)responseObject.get(SC_NUM_FOUND_KEY).isNumber().doubleValue());      
      JSONArray ra = responseObject.get(SC_DOCS_KEY).isArray();
      for (int i=0;i<ra.size();i++) {
         try {
            resultSet.getSearchResults().add(parseSolrRegistryDoc(ra.get(i).isObject(),thumbnailRootUrl));            
         }
         catch (Exception e) {
            //Window.alert("Error parsing item: " + i + " : " + e.toString());
         }  
      }      
   }
   
   //Builds an DarSearchResult object from the given JSON document
   private static DecalsApplicationRepositoryRecord parseSolrDarDoc(JSONObject doc) throws Exception {
      DecalsApplicationRepositoryRecord sr = new DecalsApplicationRepositoryRecord();      
      if (doc.containsKey(DarResourceMetadata.ID_KEY)) sr.setId(doc.get(DarResourceMetadata.ID_KEY).isString().stringValue());
      if (doc.containsKey(DecalsApplicationRepositoryRecord.UPLOADED_BY_KEY)) sr.setUploadedBy(doc.get(DecalsApplicationRepositoryRecord.UPLOADED_BY_KEY).isString().stringValue());
      if (doc.containsKey(DecalsApplicationRepositoryRecord.FILESIZE_KEY)) sr.setFileSizeBytes(Long.parseLong(doc.get(DecalsApplicationRepositoryRecord.FILESIZE_KEY).isNumber().toString()));
      if (doc.containsKey(DecalsApplicationRepositoryRecord.UPLOAD_DT_KEY)) {
         sr.setUploadDateStr(DsUtil.getDateFormatLongDate(Long.parseLong(doc.get(DecalsApplicationRepositoryRecord.UPLOAD_DT_KEY).isNumber().toString())));
         sr.setPureUploadDate(Long.parseLong(doc.get(DecalsApplicationRepositoryRecord.UPLOAD_DT_KEY).isNumber().toString()));
      }
      if (doc.containsKey(DecalsApplicationRepositoryRecord.UPDATED_DT_KEY)) {
         sr.setUpdateDateStr(DsUtil.getDateFormatLongDate(Long.parseLong(doc.get(DecalsApplicationRepositoryRecord.UPDATED_DT_KEY).isNumber().toString())));
         sr.setPureUpdateDate(Long.parseLong(doc.get(DecalsApplicationRepositoryRecord.UPDATED_DT_KEY).isNumber().toString()));
      }
      if (doc.containsKey(DecalsApplicationRepositoryRecord.LR_PUBLISH_DATE)) {
         sr.setLrPublishDateStr(DsUtil.getDateFormatLongDate(Long.parseLong(doc.get(DecalsApplicationRepositoryRecord.LR_PUBLISH_DATE).isNumber().toString())));
         sr.setPureLrPublishDate(Long.parseLong(doc.get(DecalsApplicationRepositoryRecord.LR_PUBLISH_DATE).isNumber().toString()));
      }
      if (doc.containsKey(DecalsApplicationRepositoryRecord.FILENAME_KEY)) sr.setFileName(doc.get(DecalsApplicationRepositoryRecord.FILENAME_KEY).isString().stringValue());      
      if (doc.containsKey(DecalsApplicationRepositoryRecord.MIME_TYPE_KEY)) sr.setMimeType(doc.get(DecalsApplicationRepositoryRecord.MIME_TYPE_KEY).isString().stringValue());      
      if (doc.containsKey(DecalsApplicationRepositoryRecord.LR_DOC_ID)) sr.setLrDocId(doc.get(DecalsApplicationRepositoryRecord.LR_DOC_ID).isString().stringValue());      
      if (doc.containsKey(DecalsApplicationRepositoryRecord.LR_PARADATA_ID)) sr.setLrParadataId(doc.get(DecalsApplicationRepositoryRecord.LR_PARADATA_ID).isString().stringValue());
      if (doc.containsKey(DecalsApplicationRepositoryRecord.TYPE_KEY)) {
         if (doc.get(DecalsApplicationRepositoryRecord.TYPE_KEY).isString().stringValue().equalsIgnoreCase("file")) sr.setType(DecalsApplicationRepositoryRecord.ResourceType.FILE);
         else sr.setType(DecalsApplicationRepositoryRecord.ResourceType.URL);
      }      
      if (doc.containsKey(DarResourceMetadata.TITLE_KEY)) sr.setTitle(doc.get(DarResourceMetadata.TITLE_KEY).isString().stringValue());
      if (doc.containsKey(DarResourceMetadata.DESC_KEY)) sr.setDescription(doc.get(DarResourceMetadata.DESC_KEY).isString().stringValue());
      if (doc.containsKey(DarResourceMetadata.CLASSIFICATION_KEY)) sr.setClassification(doc.get(DarResourceMetadata.CLASSIFICATION_KEY).isString().stringValue());
      if (doc.containsKey(DarResourceMetadata.SEC_LEVEL_KEY)) sr.setSecurityLevel(doc.get(DarResourceMetadata.SEC_LEVEL_KEY).isString().stringValue());
      if (doc.containsKey(DarResourceMetadata.DISTR_KEY)) sr.setDistribution(doc.get(DarResourceMetadata.DISTR_KEY).isString().stringValue());
      if (doc.containsKey(DarResourceMetadata.PUBLISHER_KEY)) sr.setPublisher(doc.get(DarResourceMetadata.PUBLISHER_KEY).isString().stringValue());
      if (doc.containsKey(DarResourceMetadata.OWNER_KEY)) sr.setOwner(doc.get(DarResourceMetadata.OWNER_KEY).isString().stringValue());
      if (doc.containsKey(DarResourceMetadata.COVERAGE_KEY)) sr.setCoverage(doc.get(DarResourceMetadata.COVERAGE_KEY).isString().stringValue());
      if (doc.containsKey(DarResourceMetadata.INTERACT_KEY)) sr.setInteractivity(doc.get(DarResourceMetadata.INTERACT_KEY).isString().stringValue());
      if (doc.containsKey(DarResourceMetadata.ENVIRON_KEY)) sr.setEnvironment(doc.get(DarResourceMetadata.ENVIRON_KEY).isString().stringValue());
      if (doc.containsKey(DarResourceMetadata.SKILL_LEVEL_KEY)) sr.setSkillLevel(doc.get(DarResourceMetadata.SKILL_LEVEL_KEY).isString().stringValue());
      if (doc.containsKey(DarResourceMetadata.LANGUAGE_KEY)) sr.setLanguage(doc.get(DarResourceMetadata.LANGUAGE_KEY).isString().stringValue());
      if (doc.containsKey(DarResourceMetadata.DURATION_KEY)) sr.setDuration((long)doc.get(DarResourceMetadata.DURATION_KEY).isNumber().doubleValue());
      if (doc.containsKey(DarResourceMetadata.TECH_REQS_KEY)) sr.setTechRequirements(doc.get(DarResourceMetadata.TECH_REQS_KEY).isString().stringValue());
      if (doc.containsKey(DarResourceMetadata.IS_PART_OF_KEY)) sr.setIsPartOf(doc.get(DarResourceMetadata.IS_PART_OF_KEY).isString().stringValue());
      if (doc.containsKey(DarResourceMetadata.REQUIRES_KEY)) sr.setRequires(doc.get(DarResourceMetadata.REQUIRES_KEY).isString().stringValue());     
      if (doc.containsKey(DecalsApplicationRepositoryRecord.URL_KEY)) sr.setUrl(doc.get(DecalsApplicationRepositoryRecord.URL_KEY).isString().stringValue());
      if (doc.containsKey(DarResourceMetadata.KEYWORDS_KEY)) 
         sr.setKeywords(DsUtil.buildStringListFromJsonArray(doc.get(DarResourceMetadata.KEYWORDS_KEY).isArray()));
      if (doc.containsKey(DarResourceMetadata.OBJECTIVES_KEY)) 
         sr.setObjectives(DarResourceObjective.buildObjectiveListSolrReturn(doc.get(DarResourceMetadata.OBJECTIVES_KEY).isArray()));
      
      if (doc.containsKey(DecalsApplicationRepositoryRecord.PUBLISHED_KEY)) sr.setPublished(doc.get(DecalsApplicationRepositoryRecord.PUBLISHED_KEY).isString().stringValue());

      if (doc.containsKey(DecalsApplicationRepositoryRecord.OLD_TITLE_KEY)) sr.setOldTitle(doc.get(DecalsApplicationRepositoryRecord.OLD_TITLE_KEY).isString().stringValue());
      
      if(doc.containsKey(DecalsApplicationRepositoryRecord.COMPETENCY_LIST_KEY)) sr.setCompetencyList(DarResourceCompetency.buildCompetencyListSolrReturn(doc.get(DecalsApplicationRepositoryRecord.COMPETENCY_LIST_KEY).isArray()));
      
      return sr;
   }
   
   /**
    * Parses the given JSON data and appends the results to the given DarSearchResultSetReturn.
    * To be used for DECALS application repository (DAR) search responses
    * 
    * @param responseObject The response from the decalsGetFilesByUser web service.
    * @param resultSet The result set in which to append new results
    */
   public static void parseSolrDarResponse(JSONObject responseObject, DarSearchResultSetReturn resultSet) {      
      resultSet.setNumTotalResultsFound((long)responseObject.get(SC_NUM_FOUND_KEY).isNumber().doubleValue());      
      JSONArray ra = responseObject.get(SC_DOCS_KEY).isArray();
      for (int i=0;i<ra.size();i++) {
         try {
            resultSet.getSearchResults().add(parseSolrDarDoc(ra.get(i).isObject()));            
         }
         catch (Exception e) {
            //Window.alert("Error parsing item: " + i + " : " + e.toString());
         }  
      } 
      //Window.alert("parseSolrDarResponse size: " + resultSet.getSearchResults().size());
   }


}

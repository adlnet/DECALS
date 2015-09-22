package com.eduworks.decals.ui.client.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import com.eduworks.decals.ui.client.model.ResourceComment;
import com.eduworks.decals.ui.client.model.ResourceCommentSummaryInfo;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;

/**
 * Parses return data from the decalsGetCommentInfoForUrl, decalsGetCommentInfoForUrlGroup, decalsGetCommentsForUrl, 
 * decalsAddComment, and decalsRemoveComment web services.
 * 
 * @author Eduworks Corporation
 *
 */
public class ResourceCommentParser {
   
   private static final String COMMENT_KEY = "comment";
   private static final String COMMENT_ID_KEY = "commentId";
   private static final String CREATE_DATE_KEY = "createDate";
   private static final String USER_ID_KEY = "userId";
   private static final String NUMBER_OF_COMMENTS_KEY = "numberOfComments";
   private static final String RESOURCE_URL_KEY = "resourceUrl";
   
   /**
    * Parses a comment summary info object
    * 
    * @param commentSummaryObject The object to parse
    * @return Returns {@link com.eduworks.decals.ui.client.model.ResourceCommentSummaryInfo}  
    */
   public static ResourceCommentSummaryInfo parseCommentSummary(JSONObject commentSummaryObject) {
      ResourceCommentSummaryInfo cs = new ResourceCommentSummaryInfo();
      if (commentSummaryObject.containsKey(NUMBER_OF_COMMENTS_KEY)) cs.setNumberOfComments((long)commentSummaryObject.get(NUMBER_OF_COMMENTS_KEY).isNumber().doubleValue());
      if (commentSummaryObject.containsKey(RESOURCE_URL_KEY)) cs.setResourceUrl(commentSummaryObject.get(RESOURCE_URL_KEY).isString().stringValue());
      return cs;
   }
   
   /**
    * Parses a group of comment summary info objects
    * 
    * @param responseObject The group of comment summaries
    * @return Returns the parsed information as an array list of {@link com.eduworks.decals.ui.client.model.ResourceCommentSummaryInfo}
    */
   public static ArrayList<ResourceCommentSummaryInfo> parseCommentSummaryList(JSONObject responseObject) {
      ArrayList<ResourceCommentSummaryInfo> ret = new ArrayList<ResourceCommentSummaryInfo>();
      Iterator<String> it = responseObject.keySet().iterator();
      while (it.hasNext()) {
         ret.add(parseCommentSummary(responseObject.get(it.next()).isObject()));
      }
      return ret;
   }
   
   /**
    * Parses a comment object
    * 
    * @param commentObject The object to parse
    * @return Returns {@link com.eduworks.decals.ui.client.model.ResourceComment}  
    */
   public static ResourceComment parseComment(JSONObject commentObject) {
      ResourceComment rc = new ResourceComment();
      if (commentObject.containsKey(COMMENT_KEY)) rc.setComment(commentObject.get(COMMENT_KEY).isString().stringValue());
      if (commentObject.containsKey(COMMENT_ID_KEY)) rc.setCommentId(commentObject.get(COMMENT_ID_KEY).isString().stringValue());
      if (commentObject.containsKey(USER_ID_KEY)) rc.setUserId(commentObject.get(USER_ID_KEY).isString().stringValue());
      if (commentObject.containsKey(CREATE_DATE_KEY)) {
         rc.setPureCreateDate(Long.parseLong(commentObject.get(CREATE_DATE_KEY).isString().stringValue()));
         rc.setCreateDateStr(DsUtil.getDateFormatLongDate(Long.parseLong(commentObject.get(CREATE_DATE_KEY).isString().stringValue())));
      }      
      return rc;
   }
   
   /**
    * Parses a group of comment objects
    * 
    * @param responseObject The group of comment summaries
    * @return Returns the parsed information as an array list of {@link com.eduworks.decals.ui.client.model.ResourceComment}
    */
   public static ArrayList<ResourceComment> parseCommentList(JSONArray responseArray) {
      ArrayList<ResourceComment> ret = new ArrayList<ResourceComment>();      
      for (int i=0;i<responseArray.size();i++) {
         //Window.alert("responseArray: " + responseArray.get(i).toString());
         ret.add(parseComment(responseArray.get(i).isObject()));
      }
      Collections.sort(ret);
      return ret;
   }

}

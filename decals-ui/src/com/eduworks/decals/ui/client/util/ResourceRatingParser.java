package com.eduworks.decals.ui.client.util;

import java.util.ArrayList;
import java.util.Iterator;

import com.eduworks.decals.ui.client.model.ResourceRatingSummaryInfo;
import com.google.gwt.json.client.JSONObject;

/**
 * Parses return data from the decalsGetRatingInfoForUrl, decalsAddRating, and decalsGetRatingInfoForUrlGroup web services.
 * 
 * @author Eduworks Corporation
 *
 */
public class ResourceRatingParser {
   
   private static final String AVERAGE_RATING_KEY = "averageRating";
   private static final String NUMBER_OF_RATINGS_KEY = "numberOfRatings";
   private static final String RESOURCE_URL_KEY = "resourceUrl";
   

   /**
    * Parses an rating summary info object
    * 
    * @param ratingSummaryObject The object to parse
    * @return Returns {@link com.eduworks.decals.ui.client.model.ResourceRatingSummaryInfo}  
    */
   public static ResourceRatingSummaryInfo parseRatingSummary(JSONObject ratingSummaryObject) {
      ResourceRatingSummaryInfo rs = new ResourceRatingSummaryInfo();
      if (ratingSummaryObject.containsKey(RESOURCE_URL_KEY)) rs.setResourceUrl(ratingSummaryObject.get(RESOURCE_URL_KEY).isString().stringValue());
      if (ratingSummaryObject.containsKey(AVERAGE_RATING_KEY)) rs.setAverageRating(ratingSummaryObject.get(AVERAGE_RATING_KEY).isNumber().doubleValue());
      if (ratingSummaryObject.containsKey(NUMBER_OF_RATINGS_KEY)) rs.setNumberOfRatings((int)ratingSummaryObject.get(NUMBER_OF_RATINGS_KEY).isNumber().doubleValue());
      return rs;
   }
   
   /**
    * Parses a group of rating summary info objects
    * 
    * @param responseObject The group of rating summaries.
    * @return Returns the parsed information as an array list of {@link com.eduworks.decals.ui.client.model.ResourceRatingSummaryInfo}
    */
   public static ArrayList<ResourceRatingSummaryInfo> parseRatingSummaryList(JSONObject responseObject) {
      ArrayList<ResourceRatingSummaryInfo> ret = new ArrayList<ResourceRatingSummaryInfo>();
      Iterator<String> it = responseObject.keySet().iterator();
      while (it.hasNext()) {
         ret.add(parseRatingSummary(responseObject.get(it.next()).isObject()));
      }
      return ret;
   }
   
}

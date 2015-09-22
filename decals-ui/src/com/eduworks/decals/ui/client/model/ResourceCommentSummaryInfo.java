package com.eduworks.decals.ui.client.model;

/**
 * Represents the summary comment information of a resource
 * 
 * @author Eduworks Corporation
 *
 */
public class ResourceCommentSummaryInfo {
   
   private long numberOfComments;
   private String resourceUrl;

   public long getNumberOfComments() {return numberOfComments;}
   public void setNumberOfComments(long numberOfComments) {this.numberOfComments = numberOfComments;}
   
   public String getResourceUrl() {return resourceUrl;}
   public void setResourceUrl(String resourceUrl) {this.resourceUrl = resourceUrl;}

}

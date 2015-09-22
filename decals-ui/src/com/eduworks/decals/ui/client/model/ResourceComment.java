package com.eduworks.decals.ui.client.model;

/**
 * Represents a resource comment 
 * 
 * @author Eduworks Corporation
 *
 */
public class ResourceComment implements Comparable<ResourceComment>{

   private String comment;
   private String commentId;
   private String createDateStr;
   private long pureCreateDate;
   private String userId;
   
   public String getComment() {return comment;}
   public void setComment(String comment) {this.comment = comment;}
   
   public String getCommentId() {return commentId;}
   public void setCommentId(String commentId) {this.commentId = commentId;}
   
   public String getCreateDateStr() {return createDateStr;}
   public void setCreateDateStr(String createDateStr) {this.createDateStr = createDateStr;}
   
   public long getPureCreateDate() {return pureCreateDate;}
   public void setPureCreateDate(long pureCreateDate) {this.pureCreateDate = pureCreateDate;}
   
   public String getUserId() {return userId;}
   public void setUserId(String userId) {this.userId = userId;}
   
   @Override
   public int compareTo(ResourceComment o) {
      //reverse sort...newest at the top of the list
      if (pureCreateDate > o.getPureCreateDate()) return -1;
      if (pureCreateDate < o.getPureCreateDate()) return 1;
      return 0;
   }
   
}

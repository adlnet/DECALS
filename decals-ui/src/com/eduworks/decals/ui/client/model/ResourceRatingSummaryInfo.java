package com.eduworks.decals.ui.client.model;

/**
 * Represents the summary rating information of a resource
 * 
 * @author Eduworks Corporation
 *
 */
public class ResourceRatingSummaryInfo {
   
   private double averageRating;
   private int numberOfRatings;
   private String resourceUrl;
   
   public double getAverageRating() {return averageRating;}
   public void setAverageRating(double averageRating) {this.averageRating = averageRating;}
   
   public int getNumberOfRatings() {return numberOfRatings;}
   public void setNumberOfRatings(int numberOfRatings) {this.numberOfRatings = numberOfRatings;}
   
   public String getResourceUrl() {return resourceUrl;}
   public void setResourceUrl(String resourceUrl) {this.resourceUrl = resourceUrl;}

}

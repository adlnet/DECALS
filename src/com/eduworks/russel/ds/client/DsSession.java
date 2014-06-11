package com.eduworks.russel.ds.client;

/**
 * Singleton class to help carry information around.
 * 
 * @author Tom
 *
 */
public class DsSession {
   
   private String firstName;
   private String lastName;
   
   private static final DsSession INSTANCE = new DsSession();
   
   private DsSession() {}

   public static DsSession getInstance() {return INSTANCE;}

   public String getFirstName() {return firstName;}
   public void setFirstName(String firstName) {this.firstName = firstName;}

   public String getLastName() {return lastName;}
   public void setLastName(String lastName) {this.lastName = lastName;}
   
   public String getFullName() {return firstName + " " + lastName;}

}

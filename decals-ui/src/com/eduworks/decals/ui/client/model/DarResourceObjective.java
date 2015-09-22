package com.eduworks.decals.ui.client.model;

import java.util.ArrayList;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

/**
 * Represents a DECALS application repository resource objective.
 * 
 * @author Eduworks Corporation
 *
 */
public class DarResourceObjective {
   
   public static final String TITLE_KEY = "objTitle";
   public static final String DESC_KEY = "objDesc";

   private String title;
   private String description;
   
   public DarResourceObjective(String title, String description) {
      this.title = title;
      this.description = description;
   }
   
   /**
    * {@link DarResourceObjective#title}
    */
   public String getTitle() {return title;}
   public void setTitle(String title) {this.title = title;}
   
   /**
    * {@link DarResourceObjective#description}
    */
   public String getDescription() {return description;}
   public void setDescription(String description) {this.description = description;}
   
   /**
    * Builds and returns a JSON array string from the given objective list.
    * 
    * @param objList The objective list
    * @return Returns a JSON array string from the given objective list.
    */
   public static String buildJsonArrayStringFromObjectiveList(ArrayList<DarResourceObjective> objList) {
      JSONArray ja = new JSONArray();
      if (objList == null || objList.size() == 0) ja.toString();      
      JSONObject jo;
      for (int i=0;i<objList.size();i++) {
         jo = new JSONObject();
         jo.put(TITLE_KEY, new JSONString(objList.get(i).getTitle()));
         jo.put(DESC_KEY, new JSONString(objList.get(i).getDescription()));
         ja.set(i,jo);
      }
      return ja.toString();
   }
   
   /**
    * Builds and returns a JSON array from the given objective list.
    * 
    * @param objList The objective list
    * @return Returns a JSON array string from the given objective list.
    */
   public static JSONArray buildJsonArrayFromObjectiveList(ArrayList<DarResourceObjective> objList) {
      JSONArray ja = new JSONArray();
      if (objList == null || objList.size() == 0) ja.toString();      
      JSONObject jo;
      for (int i=0;i<objList.size();i++) {
         jo = new JSONObject();
         jo.put(TITLE_KEY, new JSONString(objList.get(i).getTitle()));
         jo.put(DESC_KEY, new JSONString(objList.get(i).getDescription()));
         ja.set(i,jo);
      }
      return ja;
   }
   
   /**
    * Builds and returns an objective list from the given JSON array as solr return...
    * 
    * @param ja The JSON array
    * @return Returns an objective list from the given JSON array.
    */
   public static ArrayList<DarResourceObjective> buildObjectiveListSolrReturn(JSONArray ja) {
      ArrayList<DarResourceObjective> objList = new ArrayList<DarResourceObjective>();
      JSONObject jo;
      DarResourceObjective dro;
      for (int i=0;i<ja.size();i++) {
         jo = (JSONObject) JSONParser.parseStrict(ja.get(i).isString().stringValue());
         dro = new DarResourceObjective(jo.get(TITLE_KEY).isString().stringValue(),jo.get(DESC_KEY).isString().stringValue());
         objList.add(dro);
      }
      return objList;      
   }
   
}

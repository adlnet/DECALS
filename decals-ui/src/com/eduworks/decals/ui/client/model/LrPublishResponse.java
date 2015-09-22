package com.eduworks.decals.ui.client.model;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;

/**
 * Represents the response from an LR publish
 * 
 * @author Eduworks Corporation
 *
 */
public class LrPublishResponse {
   
   private static final String OK_KEY = "OK";
   private static final String DOC_RESULTS_KEY = "document_results";
   private static final String DOC_ID_KEY = "doc_ID";
   private static final String ERROR_KEY = "error";
   
   private boolean ok = false;
   private String errorMessage;
   private String docId;
   
   private void setAsInvalidReturn() {
      ok = false;
      errorMessage = "Invalid LR Publish Return Value";
   }
   
   public LrPublishResponse(JSONObject result) {
      try {
         if (result.containsKey(OK_KEY)) {
            ok = result.get(OK_KEY).isBoolean().booleanValue();
            if (ok) {
               if (result.containsKey(DOC_RESULTS_KEY)) {
                  JSONArray docResults = result.get(DOC_RESULTS_KEY).isArray();
                  docId = docResults.get(0).isObject().get(DOC_ID_KEY).isString().stringValue();
               }
               else setAsInvalidReturn();
            }
            else {
               if (result.containsKey(ERROR_KEY)) {
                  errorMessage = result.get(ERROR_KEY).isString().stringValue();
               }
               else setAsInvalidReturn();
            }
         }
         else setAsInvalidReturn();
      }
      catch (Exception e) {setAsInvalidReturn();}
   }

   public boolean isOk() {return ok;}
   
   public String getErrorMessage() {return errorMessage;}

   public String getDocId() {return docId;}

}

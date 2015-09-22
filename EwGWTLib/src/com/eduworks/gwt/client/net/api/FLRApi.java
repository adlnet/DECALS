/*
Copyright 2012-2013 Eduworks Corporation

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.eduworks.gwt.client.net.api;

import java.util.ArrayList;

import com.eduworks.gwt.client.model.FLRRecord;
import com.eduworks.gwt.client.net.CommunicationHub;
import com.eduworks.gwt.client.net.MultipartPost;
import com.eduworks.gwt.client.net.callback.ESBCallback;
import com.eduworks.gwt.client.net.packet.AjaxPacket;
import com.eduworks.gwt.client.net.packet.ESBPacket;
import com.eduworks.gwt.client.net.packet.FLRPacket;
import com.google.gwt.core.client.JsDate;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;


public class FLRApi {
	public static final String FLR_RUSSEL_MIME_TYPE = "russel/flr";
	public static final String FLR_SUCCESS = "success";
	public static final String FLR_FAILURE = "failure";
	public static final String FLR_ACTIVITY_RATINGS = "ratings";
	public static final String FLR_ACTIVITY_COMMENTS = "comments";
	public static final String FLR_ACTIVITY_ISD = "isd";
	
	public static String FLR_REPOSITORY_SETTING = "FLR-repository";
	public static String FLR_IMPORT_SETTING = "FLR-import";
	public static String FLR_PUBLISH_SETTING = "FLR-publish";
	public static String FLR_ACTIVITY_SETTING = "FLR-activity";
	
	public static final String FLR_NOT_IN_USE = "None";
    public static final String FLR_SAND_BOX = "FLR-Sandbox";
    public static final String FLR_CUSTOM_URL = "FLR-Custom";
    public static String FLR_REPOSITORY_MODE = FLR_SAND_BOX;

	public static final String FLR_IMPORT_DISABLED = "FLR-NoImport";
	public static final String FLR_IMPORT_ENABLED = "FLR-Import";
	public static String FLR_IMPORT_MODE = FLR_IMPORT_ENABLED;
	
	public static final String FLR_PUBLISH_ACTIONS_NONE = "FLR-NoPublish";
	public static final String FLR_PUBLISH_ACTIONS_GENERAL = "FLR-GeneralPublish";
	public static final String FLR_PUBLISH_ACTIONS_ISD = "FLR-IsdPublish";
	public static final String FLR_PUBLISH_ACTIONS_ALL = "FLR-AllPublish";
	public static String FLR_PUBLISH_MODE = FLR_PUBLISH_ACTIONS_ALL;
	
	public static final String FLR_ACTIVITY_ACTIONS_NONE = "FLR-NoActivity";
	public static final String FLR_ACTIVITY_ACTIONS_FEEDBACK = "FLR-FeedbackActivity";
	public static final String FLR_ACTIVITY_ACTIONS_ISD = "FLR-IsdActivity";
	public static final String FLR_ACTIVITY_ACTIONS_ALL = "FLR-AllActivity";
	public static String FLR_ACTIVITY_MODE = FLR_ACTIVITY_ACTIONS_ALL;
	
	public static String currentDirectoryId = "";
	public static String ticket;

	// FLR API private methods
	private final native static String getISOdate0(JsDate date) /*-{
		return date.toISOString();
	}-*/;
	
	private static String buildNsdlDcRecord0(FLRRecord ap) {
		String nsdl = null;
		String valueTest = null;
		
		nsdl = "<nsdl_dc:nsdl_dc xmlns:xsi=\'http://www.w3.org/2001/XMLSchema-instance\'  " +
		       "                 xmlns:dc=\'http://purl.org/dc/elements/1.1/\'  "+
		       "                 xmlns:dct=\'http://purl.org/dc/terms/\'       "+
		       "                 xmlns:ieee=\'http://www.ieee.org/xsd/LOMv1p0\' "+
		       "                 xmlns:nsdl_dc=\'http://ns.nsdl.org/nsdl_dc_v1.02/\'  schemaVersion=\'1.02.020\'  "+
		       "                 xsi:schemaLocation=\'http://ns.nsdl.org/nsdl_dc_v1.02/ http://ns.nsdl.org/schemas/nsdl_dc/nsdl_dc_v1.02.xsd\'> ";
		nsdl += "       <dc:identifier xsi:type=\'dct:URI\'>"+ CommunicationHub.siteURL+"?id="+ ap.getGuid() + "</dc:identifier>";
		if ((valueTest = ap.getTitle()) != "") {
			nsdl += "       <dc:title>" + valueTest + "</dc:title>";
		}
		if ((valueTest = ap.getDescription()) != "") {
			nsdl += "       <dc:description>" + valueTest + "</dc:description>";
		}
		if ((valueTest = ap.getPublisher()) != "") {
			nsdl += "       <dc:creator>" + valueTest + "</dc:creator>";
		}
		if ((valueTest = ap.getLanguage()) != "") {		
			nsdl += "       <dc:language>" + valueTest + "</dc:language> ";
		}
		if ((valueTest = ap.getSkill()) != "") {		
			nsdl += "       <dct:educationLevel xsi:type=\'nsdl_dc:NSDLEdLevel\'>" + valueTest + "</dct:educationLevel>";
		}
		if ((valueTest = ap.getMimeType()) != "") {		
			nsdl += "       <dc:format>" + valueTest + "</dc:format>";
		}
//		if ((valueTest = ap.getUploadDate()) != "") {		
//			nsdl += "       <dc:date>" + valueTest + "</dc:date>";			
//		}
		nsdl += "</nsdl_dc:nsdl_dc>";

		return nsdl;
	}

	private static FLRPacket buildActivityProperties0(FLRRecord ap, ESBPacket feedback, String type) {
		JsDate date = JsDate.create();
		String dateStr = getISOdate0(date);
		FLRPacket fp = new FLRPacket();
		
		// Describe the actor
		FLRPacket actor = new FLRPacket();
		actor.put("objectType", "community");
		JSONArray ja = new JSONArray();
		ja.set(0, new JSONString("ADL RUSSEL user community"));
		actor.put("description", ja);
		fp.put("actor", actor);

		// Describe the verb (activity)
		//TODO fix FLR
//		if (type == FLR_ACTIVITY_RATINGS) {
//			FLRPacket payload = new FLRPacket();
//			payload.put("avg", feedback.getAverageRating());
//			payload.put("scale min", "1");
//			payload.put("scale max", "5");
//			payload.put("sample size", feedback.getRatingCount());
//			FLRPacket value = new FLRPacket();
//			value.put("measureType", "star average");
//			value.put("value", payload);
//			FLRPacket measure = new FLRPacket();
//			measure.put("action", "rated");
//			measure.put("measure", value);
//			measure.put("context", "repository");		
//			measure.put("date", ap.getCreateDate() + "/" + dateStr.substring(0, dateStr.indexOf('T')));	
//			fp.put("verb", measure);
//			fp.put("content", feedback.getRatingCount() + " member(s) of the ADL RUSSEL user community gave '"+ap.getTitle()+"' a rating of "+ feedback.getAverageRating() + " out of 5 stars");
//		}
//		else if (type == FLR_ACTIVITY_COMMENTS) {
//			FLRPacket payload = new FLRPacket();
//			payload.put("measureType", "count");
//			payload.put("value", feedback.getCommentCount());
//			FLRPacket measure = new FLRPacket();
//			measure.put("action", "commented");
//			measure.put("measure", payload);		
//			measure.put("date", ap.getCreateDate() + "/" + dateStr.substring(0, dateStr.indexOf('T')));	
//			measure.put("context", "ADL RUSSEL repository");
//			fp.put("verb", measure);
//			fp.put("content", feedback.getCommentCount() + " member(s) of the ADL RUSSEL user community commented on '"+ap.getTitle()+"'.");
//		}
//		else if (type == FLR_ACTIVITY_ISD) {
//			FLRPacket payload = new FLRPacket();
//			payload.put("measureType", "count");
//			payload.put("value", feedback.getString("count"));
//			FLRPacket measure = new FLRPacket();
//			measure.put("action", "aligned");
//			measure.put("measure", payload);		
//			measure.put("date", ap.getCreateDate() + "/" + dateStr.substring(0, dateStr.indexOf('T')));	
//			measure.put("context", feedback.getString("template")+" instructional strategy");
//			fp.put("verb", measure);
//			FLRPacket object = new FLRPacket();
//			object.put("objectType", "Instructional Strategy");
//			object.put("description", feedback.getString("strategy"));
//			ja = new JSONArray();
//			ja.set(0, object);
//			fp.put("related", ja);
//			fp.put("content", "'"+ap.getTitle() + "' has been aligned with the '"+feedback.getString("strategy")+"' part of the '"+ feedback.getString("template") + "' template "+feedback.getString("count")+" time(s).");
//		}
//		
//		// Describe the object
//		FLRPacket object = new FLRPacket();
//		object.put("objectType", "resource");
//		if (ap.getRusselValue("russel:FLRid") != null) {
//			object.put("id", ap.getRusselValue("russel:FLRtag"));	
//		} else {
//			object.put("id", CommunicationHub.siteURL+"?id="+ap.getNodeId());	
//		}
//		fp.put("object", object);
//
//		if (ap.getRusselValue("russel:FLRid") != null) {
//			object = new FLRPacket();
//			object.put("objectType", "comment");
//			object.put("id", CommunicationHub.siteURL+"?id="+ap.getNodeId());
//			ja = new JSONArray();
//			ja.set(0, object);
//			fp.put("related", ja);
//		}

		return fp;
	}
	
	private static FLRPacket buildActivityRecord0(FLRRecord ap, ESBPacket feedback, String type) { // ISD or RATING
		FLRPacket activity = new FLRPacket();
		activity.put("activity", buildActivityProperties0(ap, feedback, type));
		return activity;
	}
	
	// FLRAPI public methods
	public static String buildFLRResourceDataDescription(ESBPacket ap) {
		JsDate date = JsDate.create();
		//TODO fix FLR
//		FLRPacket fpRdd = new FLRPacket();
//		fpRdd.put("doc_type", "resource_data");
//		fpRdd.put("resource_data_type", "metadata");		
//		fpRdd.put("node_timestamp", getISOdate0(date));	
//		fpRdd.put("TOS", FLRPacket.makePacketTOS());	
//		fpRdd.put("payload_placement", "inline");
//		JSONArray ja = new JSONArray();
//		ja.set(0, new JSONString("NSDL DC 1.02.020"));
//		fpRdd.put("payload_schema", ja);	
//		fpRdd.put("payload_schema_locator", "http://ns.nsdl.org/schemas/nsdl_dc/nsdl_dc_v1.02.xsd");
//		fpRdd.put("active", true);	
//		fpRdd.put("doc_version", "0.23.0");	
//		fpRdd.put("resource_locator", CommunicationHub.siteURL+"?id="+ap.getNodeId());	
//		fpRdd.put("publishing_node", "RUSSEL");
//		fpRdd.put("identity", FLRPacket.makePacketIdentity(ap.getPublisher()));
//		fpRdd.put("resource_data", buildNsdlDcRecord0(ap));  
		
		//return fpRdd.toString();
		return null;
	}
	
	public static String buildFLRResourceDataActivity(FLRRecord ap, ESBPacket feedback, String type) { 
		JsDate date = JsDate.create();

		//TODO fix FLR
		FLRPacket fpRdd = new FLRPacket();
		fpRdd.put("doc_type", "resource_data");
		fpRdd.put("resource_data_type", "paradata");		// assertion?
		fpRdd.put("active", true);	
		fpRdd.put("node_timestamp", getISOdate0(date));	
		fpRdd.put("create_timestamp", getISOdate0(date));	
		fpRdd.put("TOS", FLRPacket.makePacketTOS());	
		fpRdd.put("payload_placement", "inline");
		JSONArray ja = new JSONArray();
		ja.set(0, new JSONString("LR Paradata 1.0"));
		fpRdd.put("payload_schema", ja);
		fpRdd.put("doc_version", "0.23.0");	
		fpRdd.put("resource_locator", CommunicationHub.siteURL+"?id="+ap.getGuid());	
		fpRdd.put("publishing_node", "RUSSEL");
		fpRdd.put("identity", FLRPacket.makePacketIdentity(ap.getPublisher()));
		fpRdd.put("resource_data", buildActivityRecord0(ap, feedback, type));  

		return fpRdd.toString();
	}
	
	public static String buildFLRDocuments(ArrayList<String> docs) {
		FLRPacket fpDocs = new FLRPacket();
		String docAcc = "";
		for (int docIndex=0;docIndex<docs.size();docIndex++)
			docAcc += "," + docs.get(docIndex);
		if (docAcc!="")
			docAcc = docAcc.substring(1);
		
		JSONArray ja = new JSONArray();
		ja.set(0, new JSONObject(AjaxPacket.parseJSON(docAcc)));
		fpDocs.put("documents", ja);

		return fpDocs.toString();
	}

	public static ESBPacket parseFLRResponse(String op, ESBPacket response, ESBPacket ap) {
		ESBPacket status = new ESBPacket();
		
		//TODO fix FLR
//		if (op.equals(FLR_PUBLISH_SETTING)) {
//			if (response.getResponseStatus().equals("true")) {
//				JSONArray results = response.getResponseDocResults();
//				for (int i=0; i<results.size(); i++) {  //NOTE: right now we are only publishing one node at a time
//					FLRPacket doc = new FLRPacket(results.get(i).isObject());
//					if (doc.getResponseStatus().equals("true")) {
//						status.put("status", FLR_SUCCESS);
//						status.put("flr_ID", doc.getResponseDocID());
//						status.put("russel_ID", ap.getNodeId());
//					}
//					else {
//						status.put("status", FLR_FAILURE);
//						status.put("error", doc.getResponseError());
//					}
//				}
//			}
//			else {
//				status.put("status", FLR_FAILURE);
//				status.put("error", response.getResponseError());
//			}
//		}
//		else if (op.equals(FLR_ACTIVITY_SETTING)) {
//			if (response.getResponseStatus().equals("true")) {
//				status.put("status", FLR_SUCCESS);
//				JSONArray results = response.getResponseDocResults();
//				JSONArray ja = new JSONArray();
//				ja.set(0, new JSONString(results.toString()));
//				status.put("list", ja);
//			}
//			else {
//				status.put("status", FLR_FAILURE);
//				status.put("error", response.getResponseError());
//			}
//		}

		return status;
	}
	
	public static void saveFLRsetting(String setting, String value) {
		// TODO: Need to save these settings to a file or Alfresco node -- they are currently only maintained within a session.
		if (setting.equalsIgnoreCase(FLR_REPOSITORY_SETTING))  {
		    FLR_REPOSITORY_MODE = value;
		}
		else if ((setting.equalsIgnoreCase(FLR_IMPORT_SETTING)) && 
			    (value.equalsIgnoreCase(FLR_IMPORT_ENABLED)||value.equalsIgnoreCase(FLR_IMPORT_DISABLED))) {
			FLR_IMPORT_MODE = value;
		}
		else if ((setting.equalsIgnoreCase(FLR_PUBLISH_SETTING)) && 
		    (value.equalsIgnoreCase(FLR_PUBLISH_ACTIONS_NONE)||value.equalsIgnoreCase(FLR_PUBLISH_ACTIONS_GENERAL)||value.equalsIgnoreCase(FLR_PUBLISH_ACTIONS_ISD)||value.equalsIgnoreCase(FLR_PUBLISH_ACTIONS_ALL))) {
			FLR_PUBLISH_MODE = value;
		}
		else if ((setting.equalsIgnoreCase(FLR_ACTIVITY_SETTING)) && 
			(value.equalsIgnoreCase(FLR_ACTIVITY_ACTIONS_NONE)||value.equalsIgnoreCase(FLR_ACTIVITY_ACTIONS_FEEDBACK)||value.equalsIgnoreCase(FLR_ACTIVITY_ACTIONS_ISD)||value.equalsIgnoreCase(FLR_ACTIVITY_ACTIONS_ALL))) {
			FLR_ACTIVITY_MODE = value;
		}		
	}

	public static String getFLRsetting(String setting) {
		String response = "Unknown FLR setting: "+setting; 
		
		if (setting.equalsIgnoreCase(FLR_REPOSITORY_SETTING))  {
		    response = FLR_REPOSITORY_MODE;
		}
		else if (setting.equalsIgnoreCase(FLR_IMPORT_SETTING)) {
			response = FLR_IMPORT_MODE;
		}
		else if (setting.equalsIgnoreCase(FLR_PUBLISH_SETTING)) {
			response = FLR_PUBLISH_MODE;
		}
		else if (setting.equalsIgnoreCase(FLR_ACTIVITY_SETTING)) {
			response = FLR_ACTIVITY_MODE;
		}
		return response;	
	}
	
	// TODO: This initializeFLRconfig function is not being called until we are properly setting the value. It will need to be called after successful login.
	public static void initializeFLRconfig() {
		// TODO: Retrieve the file or node where configuration is saved, then pull the values from there.
		String value = ""; // TODO: replace this with each relevant value
		
		saveFLRsetting(FLR_REPOSITORY_SETTING, value);
		saveFLRsetting(FLR_IMPORT_SETTING, value);
		saveFLRsetting(FLR_PUBLISH_SETTING, value);
		saveFLRsetting(FLR_ACTIVITY_SETTING, value);
	}
	
	public static void getFLRdata(ESBCallback<FLRPacket> callback) {
		if (FLR_IMPORT_MODE.equals(FLR_IMPORT_ENABLED)) {
			CommunicationHub.sendHTTP(CommunicationHub.GET,
									  ESBApi.getAlfrescoFlrImportURL(),  
									  null,
									  false, 
									  callback);
		} 
		else {
			callback.onFailure(new Throwable("FLR import is disabled.  See Repository Settings to change the configuration."));
		}
	}
	
	public static void putFLRdata(MultipartPost postData, ESBCallback<FLRPacket> callback) {
		if (!FLR_PUBLISH_MODE.equals(FLR_PUBLISH_ACTIONS_NONE)) {
			ESBPacket jo = new ESBPacket();
			jo.put("username", ESBApi.username);
			jo.put("sessionId", ESBApi.sessionId);
			postData.appendMultipartFormData("session", jo);
			CommunicationHub.sendMultipartPost(ESBApi.getFlrPostURL(), 
											   postData, 
											   false, 
											   callback);
		}
		else {
			callback.onFailure(new Throwable("FLR publish is disabled.  See Repository Settings to change the configuration."));
		}
	}
	
	public static void putFLRactivity(String activityString, ESBCallback<FLRPacket> callback) {
		if (!FLR_ACTIVITY_MODE.equals(FLR_ACTIVITY_ACTIONS_NONE)) {			
//			CommunicationHub.sendMultipartPost(ESBApi.getAlfrescoFlrDispatchURL(), 
//											   postData, 
//											   false, 
//											   callback);
		}
		else {
			callback.onFailure(new Throwable("FLR activity stream publish is disabled.  See Repository Settings to change the configuration."));
		}
	}
	
	

}

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

import com.eduworks.gwt.client.net.CommunicationHub;
import com.eduworks.gwt.client.net.callback.ESBCallback;
import com.eduworks.gwt.client.net.packet.ESBPacket;


public class Adl3DRApi {
	public static final String ADL3DR_RUSSEL_MIME_TYPE = "russel/3dr";
	public static final String ADL3DR_RUSSEL_SUBMITTER = "Russel.project@adlnet.gov";
	public static final String ADL3DR_SUCCESS = "success";
	public static final String ADL3DR_FAILURE = "failure";
	public static final String ADL3DR_ACTIVITY_RATINGS = "ratings";
	public static final String ADL3DR_ACTIVITY_COMMENTS = "comments";
	public static final String ADL3DR_ACTIVITY_ISD = "isd";
	
	public static String ADL3DR_OPTION_SETTING = "ADL-3DR-option";
	public static String ADL3DR_ACTIVITY_SETTING = "ADL-3DR-activity";
	
	public static final String ADL3DR_DISABLED = "ADL-3DR-disabled";
    public static final String ADL3DR_ENABLED = "ADL-3DR-enabled";
	public static String ADL3DR_OPTION_MODE = ADL3DR_ENABLED;
	
	public static final String ADL3DR_ACTIVITY_ACTIONS_NONE = "ADL-3DR-NoActivity";
	public static final String ADL3DR_ACTIVITY_ACTIONS_FEEDBACK = "ADL-3DR-FeedbackActivity";
	public static final String ADL3DR_ACTIVITY_ACTIONS_ISD = "ADL-3DR-IsdActivity";
	public static final String ADL3DR_ACTIVITY_ACTIONS_ALL = "ADL-3DR-AllActivity";
	public static String ADL3DR_ACTIVITY_MODE = ADL3DR_ACTIVITY_ACTIONS_ALL;
	
	public static String currentDirectoryId = "";
	public static String ticket;
	
	public static void saveAdl3DRsetting(String setting, String value) {
		// TODO: Need to save these settings to a file or Alfresco node -- they are currently only maintained within a session.
		if (setting.equalsIgnoreCase(ADL3DR_OPTION_SETTING))  {
		    ADL3DR_OPTION_MODE = value;
		}
		else if ((setting.equalsIgnoreCase(ADL3DR_ACTIVITY_SETTING)) && 
			(value.equalsIgnoreCase(ADL3DR_ACTIVITY_ACTIONS_NONE)||value.equalsIgnoreCase(ADL3DR_ACTIVITY_ACTIONS_FEEDBACK)||value.equalsIgnoreCase(ADL3DR_ACTIVITY_ACTIONS_ISD)||value.equalsIgnoreCase(ADL3DR_ACTIVITY_ACTIONS_ALL))) {
			ADL3DR_ACTIVITY_MODE = value;
		}		
	}

	public static String getADL3DRsetting(String setting) {
		String response = ""; 
		
		if (setting.equalsIgnoreCase(ADL3DR_OPTION_SETTING))  {
		    response = ADL3DR_OPTION_MODE;
		}
		else if (setting.equalsIgnoreCase(ADL3DR_ACTIVITY_SETTING)) {
			response = ADL3DR_ACTIVITY_MODE;
		}
		else response = "Unknown ADL3DR setting: "+setting;
		return response;	
	}
	
	// TODO: This initializeADL3DRconfig function is not being called until we are properly setting the value. It will need to be called after successful login.
	public static void initializeADL3DRconfig() {
		// TODO: Retrieve the file or node where configuration is saved, then pull the values from there.
		String value = ""; // TODO: replace this with each relevant value
		saveAdl3DRsetting(ADL3DR_OPTION_SETTING, value);
		saveAdl3DRsetting(ADL3DR_ACTIVITY_SETTING, value);
	}
		
	public static void searchADL3DR(String queryString, ESBCallback<ESBPacket> callback) {
		if (ADL3DR_OPTION_MODE.equals(ADL3DR_ENABLED)) {			
			CommunicationHub.sendHTTP(CommunicationHub.POST,
									  Adl3DRApi.getAlfresco3drDispatchSearchURL(queryString), 
									  null,
									  false, 
									  callback);
		}
		else {
			callback.onFailure(new Throwable("ADL3DR search is disabled.  See Repository Settings to change the configuration."));
		}
	}

	private static String getAlfresco3drDispatchSearchURL(String queryString) {
		// TODO Auto-generated method stub
		return null;
	}

	public static void getADL3DRobject(String id, ESBCallback<ESBPacket> callback) {
		if (ADL3DR_OPTION_MODE.equals(ADL3DR_ENABLED)) {			
			CommunicationHub.sendHTTP(CommunicationHub.POST,
									  Adl3DRApi.getAlfresco3drDispatchIdURL("metadata",id), 
									  null,
									  false, 
									  callback);
		}
		else {
			callback.onFailure(new Throwable("ADL3DR metadata is disabled.  See Repository Settings to change the configuration."));
		}
	}

	public static void getADL3DRobjectReview(String id, ESBCallback<ESBPacket> callback) {
		if (ADL3DR_OPTION_MODE.equals(ADL3DR_ENABLED)) {			
			CommunicationHub.sendHTTP(CommunicationHub.POST,
									  Adl3DRApi.getAlfresco3drDispatchIdURL("reviews",id), 
									  null,
									  false, 
									  callback);
		}
		else {
			callback.onFailure(new Throwable("ADL3DR reviews is disabled.  See Repository Settings to change the configuration."));
		}
	}

	private static String getAlfresco3drDispatchIdURL(String string, String id) {
		// TODO Auto-generated method stub
		return null;
	}

	public static void putADL3DRactivity(String id, String activityString, ESBCallback<ESBPacket> callback) {
		if (ADL3DR_OPTION_MODE.equals(ADL3DR_ENABLED)) {			
			CommunicationHub.sendHTTP(CommunicationHub.POST,
									  Adl3DRApi.getAlfresco3drDispatchReviewURL(id), 
									  activityString,
									  false, 
									  callback);
		}
		else {
			callback.onFailure(new Throwable("ADL3DR uploadReviews is disabled.  See Repository Settings to change the configuration."));
		}
	}

	private static String getAlfresco3drDispatchReviewURL(String id) {
		// TODO Auto-generated method stub
		return null;
	}
	
}

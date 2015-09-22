///*
//Copyright 2012-2013 Eduworks Corporation
//
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.
//*/
package com.eduworks.gwt.client.net.api;

import org.vectomatic.file.Blob;

import com.eduworks.gwt.client.net.CommunicationHub;
import com.eduworks.gwt.client.net.MultipartPost;
import com.eduworks.gwt.client.net.callback.ESBCallback;
import com.eduworks.gwt.client.net.packet.ESBPacket;

public class ESBApi {
    public static final String ESBAPI_RETURN_OBJ = "obj";
    
	public static String username;
	public static String sessionId = null;
	public static String esbURL = "api/custom/";
	
	public static void setSessionId(String id){
		sessionId = id;
		storeSessionId(sessionId);
	}
	
	public static native void storeSessionId(String id) /*-{
		$wnd.localStorage.setItem("decalsSessionId",id);
	}-*/;
	
	public static native String getStoredSessionId() /*-{
		return $wnd.localStorage.getItem("decalsSessionId");
	}-*/;
	
	
	
	public static String getESBActionURL(String action) {
		return CommunicationHub.esbURL + action;
	}
	
	public static String getAlfrescoFlrImportURL() {
		// TODO Auto-generated method stub
		return "";
	}

	public static String getFlrPostURL() {
		return CommunicationHub.esbURL + "publishToFlr";
	}

	public static String getAlfresco3drDispatchReviewURL(String id) {
		// TODO Auto-generated method stub
		return "";
	}

	public static String getAlfresco3drDispatchIdURL(String string, String id) {
		// TODO Auto-generated method stub
		return "";
	}

	public static String getAlfresco3drDispatchSearchURL(String queryString) {
		// TODO Auto-generated method stub
		return "";
	}
    
	//------------------------------------------------------
	
	public static String login(String username, String password, ESBCallback<ESBPacket> callback) {
		MultipartPost mp = new MultipartPost();
		ESBPacket jo = new ESBPacket();
		jo.put("username", username);
		jo.put("password", password);
		mp.appendMultipartFormData("session", jo);
		return CommunicationHub.sendMultipartPost(getESBActionURL("login"),
								  		   mp, 
								  		   false, 
								  		   callback);
	}
	
	
	public static String getUser(String username, ESBCallback<ESBPacket> callback) {
      MultipartPost mp = new MultipartPost();
      ESBPacket jo = new ESBPacket();
      jo.put("username", username);
      jo.put("sessionId", sessionId);
      mp.appendMultipartFormData("session", jo);
      return CommunicationHub.sendMultipartPost(getESBActionURL("getUserByUsername"),
                                 mp, 
                                 false, 
                                 callback);
   }
   
	//I know this still has userId instead of username, but there in the libUser, it is still userId.  
	//This needs to be customized across both russel2.rs2 ant libUser.rs2 -- T. Buskirk. 6/6/2014
	//TODO Make userId/username consistent in both russel2.rs2 and libUser.rs2
   public static String updateUserAtCreate(String firstName, String lastName, String email, ESBCallback<ESBPacket> callback) {
      MultipartPost mp = new MultipartPost();
      ESBPacket jo = new ESBPacket();
      jo.put("userId", email);
      jo.put("firstName", firstName);
      jo.put("lastName", lastName);
      jo.put("email", email);      
      mp.appendMultipartFormData("userMetadata", jo);
      return CommunicationHub.sendMultipartPost(getESBActionURL("updateUserAtCreate"),
                                 mp, 
                                 false, 
                                 callback);
   }
	
	public static String validateSession(ESBCallback<ESBPacket> callback) {
		MultipartPost mp = new MultipartPost();
		ESBPacket jo = new ESBPacket();
		jo.put("username", username);
		jo.put("sessionId", sessionId);
		mp.appendMultipartFormData("session", jo);
		return CommunicationHub.sendMultipartPost(getESBActionURL("validateSession"), 
														   mp, 
														   false, 
														   callback);
	}

	public static String logout(ESBCallback<ESBPacket> callback) {
		MultipartPost mp = new MultipartPost();
		ESBPacket jo = new ESBPacket();
		jo.put("username", username);
		jo.put("sessionId", sessionId);
		mp.appendMultipartFormData("session", jo);
		return CommunicationHub.sendMultipartPost(getESBActionURL("logout"), 
										   mp, 
										   false, 
										   callback);
	}

	//------------------------------------------------------
	
	public static String userHasPermission(String permissionId, ESBCallback<ESBPacket> callback) {
      MultipartPost mp = new MultipartPost();
      ESBPacket jo = new ESBPacket();
      jo.put("username", username);
      jo.put("permissionId", permissionId);
      jo.put("sessionId", sessionId);
      mp.appendMultipartFormData("session", jo);
      return CommunicationHub.sendMultipartPost(getESBActionURL("checkUserPermission"), 
                                 mp, 
                                 false, 
                                 callback);
   }

	//------------------------------------------------------

	
	public static String updateResourceMetadata(ESBPacket metadata, ESBCallback<ESBPacket> callback) {
		MultipartPost mp = new MultipartPost();
		ESBPacket jo = new ESBPacket();
		jo.put("sessionId", sessionId);
		mp.appendMultipartFormData("session", jo);
		mp.appendMultipartFormData("fileMetadata", metadata);
		return CommunicationHub.sendMultipartPost(getESBActionURL("fileUpdateMetadata"),
					                       mp, 
					                       false, 
					                       callback);
	}

	public static String getResourceMetadata(String nodeId, ESBCallback<ESBPacket> callback) {
		MultipartPost mp = new MultipartPost();
		ESBPacket jo = new ESBPacket();
		jo.put("sessionId", sessionId);
		jo.put("id", nodeId);
		mp.appendMultipartFormData("session", jo);
		return CommunicationHub.sendMultipartPost(getESBActionURL(nodeId.startsWith("r-")?"resourceGetMetadata":"fileGetMetadata"),
					                       mp, 
					                       false, 
					                       callback);
	}
	
	//------------------------------------------------------
	
	public static String getResource(String nodeId, Boolean binary, ESBCallback<ESBPacket> callback) {
		MultipartPost mp = new MultipartPost();
		return CommunicationHub.sendMultipartPost(getESBActionURL((nodeId.startsWith("r-")?"resourceGet?id=":"fileGet?id=") + nodeId + "&sessionId=" + sessionId),
					                       mp, 
					                       binary, 
					                       callback);
	}

	public static String deleteResource(String nodeId, ESBCallback<ESBPacket> callback) {
		MultipartPost mp = new MultipartPost();
		ESBPacket jo = new ESBPacket();
		jo.put("sessionId", sessionId);
		jo.put("id", nodeId);
		mp.appendMultipartFormData("session", jo);
		return CommunicationHub.sendMultipartPost(getESBActionURL(nodeId.startsWith("r-")?"resourceDeleteResource":"fileDeleteFile"),
					                       mp, 
					                       false, 
					                       callback);
	}

	public static String updateResource(Blob fileData, String fileTitle, String nodeId, ESBCallback<ESBPacket> callback) {
		MultipartPost mp = new MultipartPost();
		ESBPacket jo = new ESBPacket();
		if (nodeId.startsWith("r-")) throw new IllegalArgumentException("Cannot update file of reference-based resource.");
		jo.put("sessionId", sessionId);
		jo.put("username", username);
		jo.put("id", nodeId);
		mp.appendMultipartFileData(fileTitle, fileData);
		mp.appendMultipartFormData("session", jo);
		return CommunicationHub.sendMultipartPost(getESBActionURL("fileUpdate"),
					                       mp, 
					                       false, 
					                       callback);		
	}
		
	public static String updateResource(String nodeId, String filename, String fileString, String mimeType, ESBCallback<ESBPacket> callback) {
		MultipartPost mp = new MultipartPost();
		ESBPacket jo = new ESBPacket();
		if (nodeId.startsWith("r-")) throw new IllegalArgumentException("Cannot update file of reference-based resource.");
		jo.put("sessionId", sessionId);
		jo.put("username", username);
		jo.put("id", nodeId);
		jo.put("mime", mimeType);
		mp.appendMultipartFormData(filename, fileString);
		mp.appendMultipartFormData("session", jo);
		return CommunicationHub.sendMultipartPost(getESBActionURL("fileUpdate"),
					                       mp, 
					                       false, 
					                       callback);	
	}
	
	public static String uploadResource(final Blob file, final String filename, final ESBCallback<ESBPacket> callback) {
		final MultipartPost mp = new MultipartPost();
		ESBPacket jo = new ESBPacket();
		jo.put("sessionId", sessionId);
		jo.put("username", username);
		mp.appendMultipartFileData(filename, file);
		mp.appendMultipartFormData("session", jo);
		return CommunicationHub.sendMultipartPost(getESBActionURL("fileUpload"),
										   mp, 
										   false, 
										   callback);
	}

	public static String uploadResource(String filename, String filedata, String mimeType, ESBCallback<ESBPacket> callback) {
		final MultipartPost mp = new MultipartPost();
		ESBPacket jo = new ESBPacket();
		jo.put("sessionId", sessionId);
		jo.put("username", username);
		jo.put("mime", mimeType);
		mp.appendMultipartFormData(filename, filedata);
		mp.appendMultipartFormData("session", jo);
		return CommunicationHub.sendMultipartPost(getESBActionURL("fileUpload"),
										   mp, 
										   false, 
										   callback);
	}
	
	public static String compactCMS(ESBCallback<ESBPacket> callback) {
		MultipartPost mp = new MultipartPost();
	    ESBPacket jo = new ESBPacket();
        jo.put("sessionId", sessionId);
	    mp.appendMultipartFormData("session", jo);
	    return CommunicationHub.sendMultipartPost(getESBActionURL("russel/compact"), 
				                                  mp, 
				                                  false, 
				                                  callback);
	}
	
	//------------------------------------------------------
	
	
	public static String rateObject(String nodeId, Integer rating, ESBCallback<ESBPacket> callback) {
		final MultipartPost mp = new MultipartPost();
		ESBPacket jo = new ESBPacket();
		jo.put("sessionId", sessionId);
		mp.appendMultipartFormData("session", jo);
		jo = new ESBPacket();
		jo.put("fileGuid", nodeId);
		jo.put("rating", rating);
		jo.put("username", username);
		mp.appendMultipartFormData("inRating", jo);
		return CommunicationHub.sendMultipartPost(getESBActionURL(nodeId.startsWith("r-")?"resourceAddRating":"fileAddRating"),
										   mp, 
										   false, 
										   callback);
	}

	public static String getRatings(String nodeId, ESBCallback<ESBPacket> callback) {
		final MultipartPost mp = new MultipartPost();
		ESBPacket jo = new ESBPacket();
		jo.put("sessionId", sessionId);
		jo.put("id", nodeId);
		mp.appendMultipartFormData("session", jo);
		return CommunicationHub.sendMultipartPost(getESBActionURL(nodeId.startsWith("r-")?"resourceGetRatingsByFile":"fileGetRatingsByFile"),
										   mp, 
										   false, 
										   callback);
	}
	
	//------------------------------------------------------

	public static String deleteComment(String nodeId, ESBCallback<ESBPacket> callback) {
		final MultipartPost mp = new MultipartPost();
		ESBPacket jo = new ESBPacket();
		jo.put("sessionId", sessionId);
		jo.put("id", nodeId);
		mp.appendMultipartFormData("session", jo);
		return CommunicationHub.sendMultipartPost(getESBActionURL(nodeId.startsWith("r-")?"resourceDeleteCommentByKey":"fileDeleteCommentByKey"),
										   mp, 
										   false, 
										   callback);
	}

	
	public static String addComment(String nodeId, String comment, ESBCallback<ESBPacket> callback) {
		final MultipartPost mp = new MultipartPost();
		ESBPacket jo = new ESBPacket();
		jo.put("sessionId", sessionId);
		mp.appendMultipartFormData("session", jo);
		jo = new ESBPacket();
		jo.put("fileGuid", nodeId);
		jo.put("comment", comment);
		jo.put("username", username);
		mp.appendMultipartFormData("inComment", jo);
		return CommunicationHub.sendMultipartPost(getESBActionURL(nodeId.startsWith("r-")?"resourceAddComment":"fileAddComment"),
										   mp, 
										   false, 
										   callback);
	}

	public static String getComments(String nodeId, ESBCallback<ESBPacket> callback) {
		final MultipartPost mp = new MultipartPost();
		ESBPacket jo = new ESBPacket();
		jo.put("sessionId", sessionId);
		jo.put("id", nodeId);
		mp.appendMultipartFormData("session", jo);		
		return CommunicationHub.sendMultipartPost(getESBActionURL(nodeId.startsWith("r-")?"resourceGetCommentsByFile":"fileGetCommentsByFile"),
										   mp, 
										   false, 
										   callback);
	}
	
	//------------------------------------------------------
	
	public static String downloadContentUrl(String nodeId, String filename) {
		if (nodeId.startsWith("r-")) throw new IllegalArgumentException("Cannot download file of reference-based resource.");
		return getESBActionURL("fileGet?id=" + nodeId + "&sessionId=" + sessionId);
	}

	//------------------------------------------------------
	
	public static String importZipPackage(String nodeId, ESBPacket fileFilter, ESBCallback<ESBPacket> callback) {
		if (nodeId.startsWith("r-")) throw new IllegalArgumentException("Cannot update file of reference-based resource.");
		final MultipartPost mp = new MultipartPost();
		ESBPacket jo = new ESBPacket();
		jo.put("sessionId", sessionId);
		jo.put("id", nodeId);
		jo.put("filters", fileFilter);
		mp.appendMultipartFormData("session", jo);		
		return CommunicationHub.sendMultipartPost(getESBActionURL("importFromZip"),
										   mp, 
										   true, 
										   callback);
	}
	
	//------------------------------------------------------

	public static String getThumbnail(String nodeId, ESBCallback<ESBPacket> callback) {
		if (nodeId.startsWith("r-")) throw new IllegalArgumentException("Cannot get thumbnail of reference-based resource. Use I-frame.");
		callback.onSuccess(new ESBPacket());
		return "";
	}

	public static String search(ESBPacket ap, ESBCallback<ESBPacket> callback) {
		MultipartPost mp = new MultipartPost();
		ap.put("sessionId", sessionId);
		mp.appendMultipartFormData("session", ap);
		return CommunicationHub.sendMultipartPost(getESBActionURL("solrQuery"), 
										   mp, 
										   false, 
										   callback);
	}
	
	public static String publishToFlr(ESBPacket ap, ESBCallback<ESBPacket> callback) {
	   MultipartPost mp = new MultipartPost();
	   ESBPacket jo = new ESBPacket();
      jo.put("sessionId", sessionId);
      mp.appendMultipartFormData("session", jo);
      mp.appendMultipartFormData("flrData", ap);
      return CommunicationHub.sendMultipartPost(getESBActionURL("publishToFlr"), 
                                 mp, 
                                 false, 
                                 callback);
	}
	
	public static String getUserListing(ESBCallback<ESBPacket> callback) {
		MultipartPost mp = new MultipartPost();
	    ESBPacket jo = new ESBPacket();
        jo.put("sessionId", sessionId);
	    mp.appendMultipartFormData("session", jo);
	    return CommunicationHub.sendMultipartPost(getESBActionURL("userListing"), 
				                                  mp, 
				                                  false, 
				                                  callback);
	}
	
	public static String createPermission(String permissionId, ESBCallback<ESBPacket> callback) {
		MultipartPost mp = new MultipartPost();
	    ESBPacket jo = new ESBPacket();
        jo.put("sessionId", sessionId);
		jo.put("permissionId", permissionId);
	    mp.appendMultipartFormData("session", jo);
	    return CommunicationHub.sendMultipartPost(getESBActionURL("createPermission"), 
				                                  mp, 
				                                  false, 
				                                  callback);
	}
	
	public static String deletePermission(String permissionId, ESBCallback<ESBPacket> callback) {
		MultipartPost mp = new MultipartPost();
	    ESBPacket jo = new ESBPacket();
        jo.put("sessionId", sessionId);
		jo.put("permissionId", permissionId);
	    mp.appendMultipartFormData("session", jo);
	    return CommunicationHub.sendMultipartPost(getESBActionURL("deletePermission"), 
				                                  mp, 
				                                  false, 
				                                  callback);
	}

	public static String getAllPermissions(ESBCallback<ESBPacket> callback) {
		MultipartPost mp = new MultipartPost();
	    ESBPacket jo = new ESBPacket();
        jo.put("sessionId", sessionId);
	    mp.appendMultipartFormData("session", jo);
	    return CommunicationHub.sendMultipartPost(getESBActionURL("getAllPermissions"), 
				                                  mp, 
				                                  false, 
				                                  callback);
	}

	public static String addUserPermissions(String username, String permissionId, ESBCallback<ESBPacket> callback) {
		MultipartPost mp = new MultipartPost();
	    ESBPacket jo = new ESBPacket();
        jo.put("sessionId", sessionId);
		jo.put("username", username);
		jo.put("permissionId", permissionId);
	    mp.appendMultipartFormData("session", jo);
	    return CommunicationHub.sendMultipartPost(getESBActionURL("addUserPermission"), 
				                                  mp, 
				                                  false, 
				                                  callback);
	}

	public static String removeUserPermissions(String username, String permissionId, ESBCallback<ESBPacket> callback) {
		MultipartPost mp = new MultipartPost();
	    ESBPacket jo = new ESBPacket();
        jo.put("sessionId", sessionId);
		jo.put("username", username);
		jo.put("permissionId", permissionId);
	    mp.appendMultipartFormData("session", jo);
	    return CommunicationHub.sendMultipartPost(getESBActionURL("removeUserPermission"), 
				                                  mp, 
				                                  false, 
				                                  callback);
	}

	public static String resetUserPassword(String username, String password, ESBCallback<ESBPacket> callback) {
		MultipartPost mp = new MultipartPost();
	    ESBPacket jo = new ESBPacket();
        jo.put("sessionId", sessionId);
		jo.put("username", username);
		jo.put("password", password);
	    mp.appendMultipartFormData("session", jo);
	    return CommunicationHub.sendMultipartPost(getESBActionURL("userPasswordReset"), 
				                                  mp, 
				                                  false, 
				                                  callback);
	}

	public static String setUserEula(String username, ESBCallback<ESBPacket> callback) {
		MultipartPost mp = new MultipartPost();
	    ESBPacket jo = new ESBPacket();
        jo.put("sessionId", sessionId);
		jo.put("username", username);
	    mp.appendMultipartFormData("session", jo);
	    return CommunicationHub.sendMultipartPost(getESBActionURL("userEulaSet"), 
				                                  mp, 
				                                  false, 
				                                  callback);
	}
	
	public static String createUser(String username, String password, ESBCallback<ESBPacket> callback) {
		MultipartPost mp = new MultipartPost();
	    ESBPacket jo = new ESBPacket();
        jo.put("sessionId", sessionId);
		jo.put("username", username);
		jo.put("password", password);
	    mp.appendMultipartFormData("session", jo);
	    return CommunicationHub.sendMultipartPost(getESBActionURL("createUser"), 
				                                  mp, 
				                                  false, 
				                                  callback);
	}

	public static String deleteUser(String username, ESBCallback<ESBPacket> callback) {
		MultipartPost mp = new MultipartPost();
	    ESBPacket jo = new ESBPacket();
        jo.put("sessionId", sessionId);
		jo.put("username", username);
	    mp.appendMultipartFormData("session", jo);
	    return CommunicationHub.sendMultipartPost(getESBActionURL("deleteUser"), 
				                                  mp, 
				                                  false, 
				                                  callback);
	}
	
	
	 public static String getGroup(String groupname, ESBCallback<ESBPacket> callback) {
      MultipartPost mp = new MultipartPost();
      ESBPacket jo = new ESBPacket();
      jo.put("groupId", groupname);
      jo.put("sessionId", sessionId);
      mp.appendMultipartFormData("session", jo);
      return CommunicationHub.sendMultipartPost(getESBActionURL("getGroupById"),
                                 mp, 
                                 false, 
                                 callback);
    }
	
	public static String getGroupListing(ESBCallback<ESBPacket> callback) {
		MultipartPost mp = new MultipartPost();
	    ESBPacket jo = new ESBPacket();
        jo.put("sessionId", sessionId);
	    mp.appendMultipartFormData("session", jo);
	    return CommunicationHub.sendMultipartPost(getESBActionURL("getAllGroups"), 
				                                  mp, 
				                                  false, 
				                                  callback);
	}
	
	public static String createGroup(String groupname, String groupdescription, String groupusers, ESBCallback<ESBPacket> callback) {
		MultipartPost mp = new MultipartPost();
	    ESBPacket jo = new ESBPacket();
        jo.put("sessionId", sessionId);
		jo.put("groupId", groupname);
		jo.put("description", groupdescription);
		jo.put("users", groupusers);
	    mp.appendMultipartFormData("session", jo);
	    return CommunicationHub.sendMultipartPost(getESBActionURL("createGroup"), 
				                                  mp, 
				                                  false, 
				                                  callback);
	}
	
	public static String deleteGroup(String groupname, ESBCallback<ESBPacket> callback) {
		MultipartPost mp = new MultipartPost();
	    ESBPacket jo = new ESBPacket();
        jo.put("sessionId", sessionId);
		jo.put("groupId", groupname);
	    mp.appendMultipartFormData("session", jo);
	    return CommunicationHub.sendMultipartPost(getESBActionURL("deleteGroup"), 
				                                  mp, 
				                                  false, 
				                                  callback);
	}
	
	public static String updateGroup(String groupId, String newName, String newDescription, ESBCallback<ESBPacket> callback) {
		MultipartPost mp = new MultipartPost();
	    ESBPacket jo = new ESBPacket();
        jo.put("sessionId", sessionId);
		jo.put("groupId", groupId);
		jo.put("name", newName);
		jo.put("description", newDescription);
	    mp.appendMultipartFormData("session", jo);
	    return CommunicationHub.sendMultipartPost(getESBActionURL("updateGroup"), 
				                                  mp, 
				                                  false, 
				                                  callback);
	}

	public static String addUserToGroup(String groupname, String username, ESBCallback<ESBPacket> callback) {
		MultipartPost mp = new MultipartPost();
	    ESBPacket jo = new ESBPacket();
        jo.put("sessionId", sessionId);
		jo.put("groupId", groupname);
		jo.put("username", username);
	    mp.appendMultipartFormData("session", jo);
	    return CommunicationHub.sendMultipartPost(getESBActionURL("addUserToGroup"), 
				                                  mp, 
				                                  false, 
				                                  callback);
	}
	
	public static String removeUserFromGroup(String groupname, String username, ESBCallback<ESBPacket> callback) {
		MultipartPost mp = new MultipartPost();
	    ESBPacket jo = new ESBPacket();
        jo.put("sessionId", sessionId);
		jo.put("groupId", groupname);
		jo.put("username", username);
	    mp.appendMultipartFormData("session", jo);
	    return CommunicationHub.sendMultipartPost(getESBActionURL("removeUserFromGroup"), 
				                                  mp, 
				                                  false, 
				                                  callback);
	}
}

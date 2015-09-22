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

package com.eduworks.gwt.client.net;

import java.util.HashMap;
import java.util.Map;

import com.eduworks.gwt.client.net.callback.AjaxCallback;
import com.eduworks.gwt.client.net.callback.EventCallback;
import com.eduworks.gwt.client.net.packet.AjaxPacket;
import com.eduworks.gwt.client.util.Logger;
import com.eduworks.gwt.client.util.MathUtil;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.RemoteService;

public class CommunicationHub implements RemoteService  {
	/* STATIC METHODS */

//	public static final String ATOM_XML_PREAMBLE = "atomxml";
	public static String rootURL = "";
	public static String esbURL = "";
	public static String baseURL = rootURL+"alfresco/";
	public static String siteURL = "";
	private static Map<String, JavaScriptObject> outgoingRequestHistory = new HashMap<String, JavaScriptObject>();
	private static Map<String, EventCallback> onProgressHandlers = new HashMap<String, EventCallback>();
	public final static String POST = "POST";
	public final static String GET = "GET";
	public final static String DELETE = "DELETE";
	public final static String Put = "PUT";
	public final static String PROGRESS_HANDLE = "progress";
	
	public static String randomString() {
		String acc = "";
		for (int x=0;x<20;x++) 
			acc += Character.toString((char)(Math.random() * 25 + 97));
		return acc;
	}

	
	/*
	 * Aborts an call for by guid, if the guid exists
	 */
	public static void cancelOutgoingRequest(String guid) {
		if (outgoingRequestHistory.containsKey(guid))
			cancelRequest(outgoingRequestHistory.get(guid));
	}
	
	public static String putInRequestHistory(JavaScriptObject request){
		String guid = MathUtil.generateUUID();
		
		outgoingRequestHistory.put(guid, request);
		
		return guid;
	}
	
	/*
	 * Takes a GUID from a sent response and adds an event callback to progress updates.
	 * Since this handler is added after the call has been made it might never update for short calls.
	 */
	public static void addProgressHandler(String guid, EventCallback eventCallback) {
		onProgressHandlers.put(guid, eventCallback);
	}
	
	private static void runProgressHandler(String guid, Event event) {
		if (onProgressHandlers.containsKey(guid))
			((EventCallback)onProgressHandlers.get(guid)).onEvent(event);
	}
	
	private static native void cancelRequest(JavaScriptObject outgoing) /*-{
		outgoing.abort();
	}-*/;

	public static String sendMultipartPost(String url, MultipartPost payload, final boolean binaryReturn, final AjaxCallback<? extends AjaxPacket> callback) {
		String guid = MathUtil.generateUUID();
		outgoingRequestHistory.put(guid, sendPostMultipart(url, payload, guid, binaryReturn, callback));
		return guid;
	}
	
	public static String sendHTTP(String httpMethod, String url, String outgoingPost, final boolean binaryData, final AjaxCallback<? extends AjaxPacket> callback) {
		String guid = MathUtil.generateUUID();
		outgoingRequestHistory.put(guid, httpSend(httpMethod, url, outgoingPost, guid, binaryData, callback));
		return guid;
	}
	
	public static String sendMultipartPost(String url, MultipartPost payload, final boolean binaryReturn, String guid, final AjaxCallback<? extends AjaxPacket> callback) {
		outgoingRequestHistory.put(guid, sendPostMultipart(url, payload, guid, binaryReturn, callback));
		return guid;
	}
	
	public static String sendHTTP(String httpMethod, String url, String outgoingPost, final boolean binaryData, String guid, final AjaxCallback<? extends AjaxPacket> callback) {
		outgoingRequestHistory.put(guid, httpSend(httpMethod, url, outgoingPost, guid, binaryData, callback));
		return guid;
	}

	/**
	 * Written in JSNI for IE 7/8 compatibility, GWT 2.6 drop IE Support for less than 9
	 * @param url
	 * @param payload
	 * @param guid
	 * @param binaryReturn
	 * @param callback
	 * @return XHR object that represents the outgoing request
	 */
	private static native JavaScriptObject sendPostMultipart(String url, MultipartPost payload, String guid, final boolean binaryReturn, final AjaxCallback<? extends AjaxPacket> callback) /*-{
		var xhr;
    	if (window.XMLHttpRequest) 
		  	xhr=new XMLHttpRequest();
		else 
			xhr=new ActiveXObject("Microsoft.XMLHTTP");
		
		xhr.open("POST", url, true);
	    if (binaryReturn&&$wnd.isIE===undefined)
	    	xhr.responseType = "arraybuffer";
		if (!($wnd.isIE===undefined)) {
			xhr.setRequestHeader("Content-Type", payload.@com.eduworks.gwt.client.net.MultipartPost::getContentTypeValue()());
			xhr.setRequestHeader("Content-Length", payload.@com.eduworks.gwt.client.net.MultipartPost::toString()().length);
		}
		xhr.onprogress = function (e) {
			@com.eduworks.gwt.client.net.CommunicationHub::runProgressHandler(Ljava/lang/String;Lcom/google/gwt/user/client/Event;)(guid, e);
		};
	    xhr.onreadystatechange = function() {
	        if (xhr.readyState == 4 && (xhr.status >= 200 && xhr.status <= 206)) { 
	        	if (!binaryReturn||!($wnd.isIE===undefined))
	        		callback.@com.eduworks.gwt.client.net.callback.AjaxCallback::onSuccess(Ljava/lang/String;)(xhr.responseText);
	        	else 
	        		callback.@com.eduworks.gwt.client.net.callback.AjaxCallback::onFileSuccess(Ljava/lang/String;Ljava/lang/Object;)(xhr.getResponseHeader('Content-Type'), xhr.response);
	        } else if (xhr.readyState == 4) {
	        	if (!binaryReturn)
	        		callback.@com.eduworks.gwt.client.net.callback.AjaxCallback::onFailure(Ljava/lang/Throwable;)(new @java.lang.Throwable::new(Ljava/lang/String;)(xhr.responseText));
	        	else 
	        		callback.@com.eduworks.gwt.client.net.callback.AjaxCallback::onFailure(Ljava/lang/Throwable;)(new @java.lang.Throwable::new(Ljava/lang/String;)(""));
	    	}
	    };
	    if ($wnd.isIE===undefined)
			xhr.send(payload.@com.eduworks.gwt.client.net.MultipartPost::toObject()());
		else
			xhr.send(payload.@com.eduworks.gwt.client.net.MultipartPost::toString()());
		
		return xhr;
	}-*/;
	
	/**
	 * Written in JSNI for IE 7/8 compatibility, GWT 2.6 drop IE Support for less than 9
	 * @param httpMethod
	 * @param url
	 * @param outgoingPost
	 * @param guid
	 * @param binaryData
	 * @param callback
	 * @return XHR object that represents the outgoing request
	 */
	private static native JavaScriptObject httpSend(String httpMethod, String url, String outgoingPost, String guid, final boolean binaryData, final AjaxCallback<? extends AjaxPacket> callback) /*-{
	    var xhr;
    	if (window.XMLHttpRequest) 
		  	xhr=new XMLHttpRequest();
		else 
			xhr=new ActiveXObject("Microsoft.XMLHTTP");
	
	    xhr.open(httpMethod, url, true);
	    if (binaryData&&$wnd.isIE===undefined)
	    	xhr.responseType = "arraybuffer";
	    if (outgoingPost!=null&&outgoingPost.charAt(0)=="{")
	    	xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
		xhr.onprogress = function (e) {
			@com.eduworks.gwt.client.net.CommunicationHub::runProgressHandler(Ljava/lang/String;Lcom/google/gwt/user/client/Event;)(guid, e);
		};
	    xhr.onreadystatechange = function() {
	        if (xhr.readyState == 4 && (xhr.status >= 200 && xhr.status <= 206)) 
	        	if (!binaryData||!($wnd.isIE===undefined))
	        		callback.@com.eduworks.gwt.client.net.callback.AjaxCallback::onSuccess(Ljava/lang/String;)(xhr.responseText);
	        	else 
	        		callback.@com.eduworks.gwt.client.net.callback.AjaxCallback::onFileSuccess(Ljava/lang/String;Ljava/lang/Object;)(xhr.getResponseHeader('Content-Type'), xhr.response);
	        else if (xhr.readyState == 4) 
	        	if (!binaryData)
	        		callback.@com.eduworks.gwt.client.net.callback.AjaxCallback::onFailure(Ljava/lang/Throwable;)(new @java.lang.Throwable::new(Ljava/lang/String;)(xhr.responseText));
	        	else 
	        		callback.@com.eduworks.gwt.client.net.callback.AjaxCallback::onFailure(Ljava/lang/Throwable;)(new @java.lang.Throwable::new(Ljava/lang/String;)(""));
	    };
	    // Initiate a multipart/form-data upload
	    xhr.send(outgoingPost);
	    
	    return xhr;
	}-*/;

}

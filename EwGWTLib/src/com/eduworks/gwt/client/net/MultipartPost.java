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

import org.vectomatic.file.Blob;

import com.eduworks.gwt.client.util.Browser;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.json.client.JSONValue;

public class MultipartPost
{
	/* STATIC MEMBERS */

	private static final String COMMENT	= "--";
	private static final String NEWLINE	= "\r\n";
	private static final String BOUNDARY	= "----fritterboyfgfd4545645";

	// HTTP headers
	public static final String CONTENT_DISP	= "Content-Disposition";
	public static final String CONTENT_LEN	= "Content-Length";
	public static final String CONTENT_TYPE	= "Content-Type";

	public static final String FILE_FORMAT	= "form-data; name=\"$(0)\"; filename=\"$(0)\"";
	public static final String FORM_FORMAT	= "form-data; name=\"$(0)\"";
	public static final String MIME_JSON	= "application/json";
	public static final String MULTI_VALUE	= "multipart/form-data; boundary=" + BOUNDARY;
	public static final String TEXT_VALUE	= "text/html";
	
	//private String payload;
	private String payload;
	private JavaScriptObject payloadHandle;
	
	/* INSTANCE MEMBERS */
	public MultipartPost()
	{
		if (Browser.isBadIE()) {
			payload = "";
		} else {
			payloadHandle = getFormData();
		}
	}
	
	public static native JavaScriptObject getFormData() /*-{
		return new FormData(); 
	}-*/;
	
	public native void appendMultipartFileData(final String fileName, final Blob data) /*-{
		//this.@com.eduworks.gwt.client.net.MultipartPost::payloadHandle.append(fileName, data, fileName);
		this.@com.eduworks.gwt.client.net.MultipartPost::payloadHandle.append(fileName, data);
	}-*/;
	
	private native void appendMultipartFormDataHTML5(final String fieldName, final JSONValue data) /*-{
		//this.@com.eduworks.gwt.client.net.MultipartPost::payloadHandle.append(fieldName, @com.eduworks.gwt.client.util.BlobUtils::buildBlob(Ljava/lang/String;Ljava/lang/String;)("", data), fieldName);
		this.@com.eduworks.gwt.client.net.MultipartPost::payloadHandle.append(fieldName, data);
	}-*/;
	
	private native void appendMultipartFormDataHTML5(final String fieldName, final String data) /*-{
		//this.@com.eduworks.gwt.client.net.MultipartPost::payloadHandle.append(fieldName, @com.eduworks.gwt.client.util.BlobUtils::buildBlob(Ljava/lang/String;Ljava/lang/String;)("", data), fieldName);
		this.@com.eduworks.gwt.client.net.MultipartPost::payloadHandle.append(fieldName, data);
	}-*/;
	
	/**
	 * Appends data to the body of the request and sets the multi-part header.
	 * @param fieldName the name of the file for the data
	 * @param data request data to be appended
	 * @param done true if this is the last one to append
	 */
	public void appendMultipartFormData(final String fieldName, final JSONValue data)
	{
		if (Browser.isBadIE()) {
			final StringBuilder body = new StringBuilder();
	
			if (payload != null && payload != "") body.append(payload);
	
			body.append(COMMENT).append(BOUNDARY).append(NEWLINE);
	
			String field = FORM_FORMAT.replace("$(0)", fieldName);
			body.append("Content-Disposition: ").append(field).append(NEWLINE);
			//body.append("Content-Type: " + mimeType).append(NEWLINE).append(NEWLINE);
			
			body.append(NEWLINE).append(NEWLINE);
			body.append(data).append(NEWLINE);
	
			payload = body.toString();
		} else
			appendMultipartFormDataHTML5(fieldName, data);
	}
	
	/**
	 * Appends data to the body of the request and sets the multi-part header.
	 * @param file the name of the file for the data
	 * @param data request data to be appended
	 * @param done true if this is the last one to append
	 */
	public void appendMultipartFormData(final String fieldName, final String data)
	{
		if (Browser.isBadIE()) {
			final StringBuilder body = new StringBuilder();
	
			if (payload != null && payload != "") body.append(payload);
	
			body.append(COMMENT).append(BOUNDARY).append(NEWLINE);
	
			String field = FORM_FORMAT.replace("$(0)", fieldName);
			body.append("Content-Disposition: ").append(field).append(NEWLINE);
			//body.append("Content-Type: " + mimeType).append(NEWLINE).append(NEWLINE);
			
			body.append(NEWLINE).append(NEWLINE);
			body.append(data).append(NEWLINE);
	
			payload = body.toString();
		} else
			appendMultipartFormDataHTML5(fieldName, data);
	}
	
	public JavaScriptObject toObject() {
		return payloadHandle;
	}
	
	public String toString() {
		StringBuilder body = new StringBuilder();
		body.append(payload);
		body.append(COMMENT).append(BOUNDARY).append(COMMENT).append(NEWLINE);
		return body.toString();
	}

	public String getContentTypeValue() {
		return MULTI_VALUE;
	}
}

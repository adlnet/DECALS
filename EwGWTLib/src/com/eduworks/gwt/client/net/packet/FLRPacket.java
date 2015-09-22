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

package com.eduworks.gwt.client.net.packet;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;

public class FLRPacket extends AjaxPacket{
	public FLRPacket() {
		super();
	};
	public FLRPacket(JavaScriptObject o) {
		super(o);
	};
	public FLRPacket(JSONObject o) {
		super(o.getJavaScriptObject());
	};
	
	public static final FLRPacket makePacketTOS() {
		FLRPacket fp = new FLRPacket();
		fp.put("submission_TOS", "http://www.learningregistry.org/tos/cc0/v0-5/");
		return fp;
	};
	
	public static final FLRPacket makePacketIdentity(String owner) {
		FLRPacket fp = new FLRPacket();
		fp.put("curator", "ADL RUSSEL");
		fp.put("owner", owner);
		fp.put("submitter", "ADL RUSSEL");
		fp.put("submitter_type", "agent");
		return fp;
	};
	
	public final Boolean getResponseStatus() {
		if (this.getBoolean("OK") != null && this.getBoolean("OK") != false) 
			return true;
		else 
			return false;
	}
	
	public final String getResponseError() {
		return this.getString("error");
	}

	public final JSONArray getResponseDocResults() {
		return this.getArray("document_results");
	}

	public final String getResponseDocID() {
		if (this.containsKey("document_results")) {
			JSONArray ja = this.getArray("document_results");
			if (ja.size()>0)
				return ((JSONObject)ja.get(0)).get("doc_ID").isString().stringValue();
			else
				return null;
		} else
			return this.getString("doc_ID");
	}
}
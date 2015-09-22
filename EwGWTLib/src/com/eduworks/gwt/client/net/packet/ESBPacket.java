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

import org.vectomatic.arrays.ArrayBuffer;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

public class ESBPacket extends AjaxPacket{
	
	public static final String OBJECT = "obj";
	public static final String USAGE_DELIMITER = "|";
	public static final String USAGE_STRATEGY_DELIMITER = "^";
	public static final String USAGE_COUNT_DELIMITER = "#";
	
	public ESBPacket() {};
	public ESBPacket(String json) {
		super(parseJSON(stripExtra(json)));
	}
	public ESBPacket(JavaScriptObject o) {
		super(o);
	};
	public ESBPacket(JSONObject o) {
		super(o.getJavaScriptObject());
	};
	
	public ESBPacket(String mimeType, ArrayBuffer f) {
		super();
		this.setContent(f);
		this.put("mimeType", mimeType);
	}
	
	public ESBPacket(String mimeType, String f) {
		super();
		this.setContent(f);
		this.put("mimeType", mimeType);
	}
		
	public final String getPayloadString() {
		return this.getString("obj");
	}
	
	public final String getErrorString() {
		return this.getString("error");
	}
	
	public final native void setContent(ArrayBuffer f) /*-{
		this.contentStream = f;
	}-*/;
	
	public final void setContent(String f) {
		this.put("contentStream", new JSONString(f));
	};
	
	public final native ArrayBuffer getContents() /*-{ 
		if (this.b64Encoded)
			return $wnd.Base64.decode(this.contentStream);
		else
			return this.contentStream;
	}-*/;
	
	public final native void downloadAsFile(String filename, String mimetype)/*-{
		var binary = '';
		var buffer = this.contentStream;
		var bytes = new Uint8Array(buffer);
		
		for (var i = 0, l = bytes.byteLength; i < l; i++) {
		 binary += String.fromCharCode(bytes[i]);
		}
		
		uriContent = "data:"+mimetype+";base64," + window.btoa(binary);
		
		var a = document.createElement('a');
		a.setAttribute('download',filename);
		a.href=uriContent;
		a.style.display='none';
		document.body.appendChild(a);
		a.click();
		document.body.removeChild(a);
		
	}-*/;
	
	public final native String getFileDownloadUri(String filename, String mimetype)/*-{
      var binary = '';
      var buffer = this.contentStream;
      var bytes = new Uint8Array(buffer);
      
      for (var i = 0, l = bytes.byteLength; i < l; i++) {
       binary += String.fromCharCode(bytes[i]);
      }
      
      return "data:"+mimetype+";filename="+filename+";base64," + window.btoa(binary);      
   }-*/;
	
	public final String getContentString() { 
		return this.getString("contentStream");
	}
	
	@Override
	public ESBPacket getObject(String key) {
		JSONValue jv;
		try {
			jv = super.get(key);
		} catch (NullPointerException e) {
			return null;
		}
		if (jv!=null&&jv.isObject()!=null)
			return new ESBPacket(jv.isObject());
		return null;
	}
	
	private static String stripExtra(String str){
		if(!str.startsWith("{")){
			str = str.substring(str.indexOf('{'), str.lastIndexOf('}')+1);
		}
		
		return str;
	}
//	public final ArrayList<ESBPacket> parseIsdUsage() {
//		ArrayList<ESBPacket> parsedIsd = new ArrayList<ESBPacket>();
//		ESBPacket use;
//		StringTokenizer useList, tempList; 
//		String useStr = "";
//		String templateStr="";
//		String strategyStr=""; 
//		int count=0;
//		//TODO fix isd usage code
////		String nodeUsage = this.getRusselValue("russel:epssStrategy");
////		
////		if ((nodeUsage != "") && (nodeUsage != null))  {
////			useList = new StringTokenizer(nodeUsage, USAGE_DELIMITER);
////			while (useList.hasMoreTokens()) {
////				useStr = useList.nextToken();
////				use = new ESBPacket();
////
////				tempList = new StringTokenizer(useStr, USAGE_STRATEGY_DELIMITER); 
////				if (tempList.countTokens() == 2) {
////					templateStr = tempList.nextToken();
////					use.put("template", new JSONString(templateStr));
////					tempList = new StringTokenizer(tempList.nextToken(), USAGE_COUNT_DELIMITER);
////					if (tempList.countTokens() == 2) {
////						strategyStr = tempList.nextToken();
////						use.put("strategy", new JSONString(strategyStr));
////						count = (int)Integer.parseInt(tempList.nextToken());
////						use.put("count", new JSONNumber(count));
////					}
////				}
////				parsedIsd.add(use);
////			}
////		}
//		return parsedIsd;
//	}
}
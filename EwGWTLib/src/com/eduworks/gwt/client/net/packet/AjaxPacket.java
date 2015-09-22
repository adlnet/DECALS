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

import java.util.Iterator;
import java.util.Set;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONException;
import com.google.gwt.json.client.JSONNull;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.rpc.AsyncCallback;


public abstract class AjaxPacket extends JSONObject {
	//Required protected constructor
	public AjaxPacket() {
		super();
	};
	public AjaxPacket(JavaScriptObject o) {
		super(o);
	};
	public AjaxPacket(JSONObject o) {
		super(o.getJavaScriptObject());
	};
	
	public void put(String key, Object jsonValue) {
		if (jsonValue instanceof String)
			super.put(key, new JSONString((String)jsonValue));
		else if (jsonValue instanceof Number) 
			super.put(key, new JSONNumber(Double.parseDouble(String.valueOf(jsonValue))));
		else if (jsonValue instanceof JSONArray)
			super.put(key, (JSONArray)jsonValue);
		else if (jsonValue instanceof JSONObject)
			super.put(key, (JSONObject)jsonValue);
		else if (jsonValue instanceof Boolean)
			super.put(key, JSONBoolean.getInstance((Boolean)jsonValue));
		else if (jsonValue==null)
			super.put(key, (JSONNull)jsonValue);
		else
			throw new JSONException();
	}
	
	public String getString(String key) {
		JSONValue jv;
		try {
			jv = super.get(key);
		} catch (NullPointerException e) {
			return null;
		}
		if (jv!=null&&jv.isString()!=null)
			return jv.isString().stringValue();
		return null;
	}
	
	public Integer getInteger(String key) {
		JSONValue jv;
		try {
			jv = super.get(key);
		} catch (NullPointerException e) {
			return null;
		}
		if (jv!=null&&jv.isNumber()!=null)
			return Integer.parseInt(String.valueOf(jv.isNumber().doubleValue()));
		return null;
	}
	
	public Double getDouble(String key) {
		JSONValue jv;
		try {
			jv = super.get(key);
		} catch (NullPointerException e) {
			return null;
		}
		if (jv!=null&&jv.isNumber()!=null)
			return (Double)jv.isNumber().doubleValue();
		return null;
	}
	
	public JSONArray getArray(String key) {
		JSONValue jv;
		try {
			jv = super.get(key);
		} catch (NullPointerException e) {
			return null;
		}
		if (jv!=null&&jv.isArray()!=null)
			return jv.isArray();
		return null;
	}
	
	public JSONObject getObject(String key) {
		JSONValue jv;
		try {
			jv = super.get(key);
		} catch (NullPointerException e) {
			return null;
		}
		if (jv!=null&&jv.isObject()!=null)
			return jv.isObject();
		return null;
	}
	
	public Boolean getBoolean(String key) {
		JSONValue jv;
		try {
			jv = super.get(key);
		} catch (NullPointerException e) {
			return null;
		}
		if (jv!=null&&jv.isBoolean()!=null)
			return (Boolean)jv.isBoolean().booleanValue();
		return null;
	}
	
	public native void remove(String key) /*-{
		if (this['jsObject']!=null)
			delete this['jsObject'][key];
		else
			delete this[key];
	}-*/;
	
	public final native static JavaScriptObject parseJSON(String x) /*-{ 
		var e = (x!=null)? x.replace(/[\r\n\t]/g," "): "{}";
		return eval('(' + ((e==""||e==null)? '{}' : e) + ')'); 
	}-*/;

	public final AjaxPacket mergePackets(AjaxPacket ap) {
		Set<String> keys = ap.keySet();
		for (Iterator<String> keyPointer=keys.iterator();keyPointer.hasNext();) {
			String key = keyPointer.next();
			this.put(key, ap.get(key));
		}
		return this;
	}
}
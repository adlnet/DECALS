package com.eduworks.interfaces;

import org.json.JSONException;
import org.json.JSONObject;

/***
 * Allow an object to serialize into a JSON Object.
 * 
 * @author Fritz
 * 
 */
public interface EwJsonSerializable
{
	JSONObject toJsonObject() throws JSONException;
}

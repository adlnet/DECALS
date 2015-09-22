package com.eduworks.lang.json;

import java.io.Writer;

import org.json.JSONException;

/**
 * Defines a basic, Eduworks JSON entity. Provides an API for methods that
 * all JSON based entities tend to have even though they are not related.
 * @author dharvey
 * @since September, 2011
 */
public interface EwJsonEntity
{
	/** @return pretty-printed JSON text of this JSON. */
	public String toString(int indentFactor) throws JSONException;


	/** Write the contents of the JSON as text to a writer. */
	public Writer write(Writer writer) throws JSONException;

}

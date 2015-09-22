package com.eduworks.resolver;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public interface Resolvable extends Cloneable
{
	static final String	ATTRIB_NONE	    = "Open Source/Trivial";
	static final String	ATTRIB_PROPRIETARY	    = "Eduworks Proprietary";
	static final String	ATTRIB_ASPOA1	= "NSF 1214187";
	static final String	ATTRIB_ASPOA2	= "NSF 1353200";
	static final String	ATTRIB_UCASTER	= "NSF 1044161";
	static final String	ATTRIB_RUSSEL	= "W31P4Q-12-C-0119";
	static final String	ATTRIB_TRADEM1	= "W911-QX-12-C-0055";
	static final String	ATTRIB_TRADEM2	= "W911-QX-13-C-0063";
	static final String	ATTRIB_DECALS	= "W911-QY-13-C-0087";
	static final String	ATTRIB_PULSAR	= "W911-QY-13-C-0030 Subcontract 13-0030-8975-01";

	Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams)
			throws JSONException;
	void build(String key, Object value) throws JSONException;
	Object clone() throws CloneNotSupportedException;
	String toOriginalJson() throws JSONException;
	String toJson() throws JSONException;
	String getDescription();
	String getReturn();
	String getAttribution();
	JSONObject getParameters() throws JSONException;
}

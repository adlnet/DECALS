/*
   Copyright 2007 Fourspaces Consulting, LLC

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

package com.fourspaces.couchdb;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * The CouchResponse parses the HTTP response returned by the CouchDB server.
 * This is almost never called directly by the user, but indirectly through the
 * Session and Database objects.
 * <p>
 * Given a CouchDB response, it will determine if the request was successful
 * (status 200,201,202), or was an error. If there was an error, it parses the
 * returned json error message.
 * 
 * @author mbreese
 * 
 */
public class CouchResponse
{
	Log					log	= LogFactory.getLog(CouchResponse.class);

	private String		body;
	private byte[] bodybytes;
	private String		path;
	private Header[]	headers;
	private int			statusCode;
	private String		methodName;
	boolean				ok	= false;

	private String		error_id;
	private String		error_reason;

	/**
	 * C-tor parses the method results to build the CouchResponse object. First,
	 * it reads the body (hence the IOException) from the method Next, it checks
	 * the status codes to determine if the request was successful. If there was
	 * an error, it parses the error codes.
	 * 
	 * @param method
	 * @throws IOException
	 * @throws JSONException 
	 */
	CouchResponse(HttpRequestBase req, HttpResponse response) throws IOException, JSONException
	{
		headers = response.getAllHeaders();

		HttpEntity entity = response.getEntity();
		
		bodybytes = IOUtils.toByteArray(entity.getContent());
		ByteArrayInputStream bais = new ByteArrayInputStream(bodybytes);
		body = toString(entity,null,bais);

		path = req.getURI().getPath();

		statusCode = response.getStatusLine().getStatusCode();

		boolean isGet = (req instanceof HttpGet);

		boolean isPut = (req instanceof HttpPut);

		boolean isPost = (req instanceof HttpPost);

		boolean isDelete = (req instanceof HttpDelete);

		if ((isGet && statusCode == 404) ||(statusCode == 401) || (isPut && statusCode == 409) || (isPost && statusCode == 404)
				|| (isDelete && statusCode == 404))
		{
			JSONObject jbody = new JSONObject(body);
			error_id = jbody.getString("error");
			error_reason = jbody.getString("reason");
		}
		else if ((isPut && 199 < statusCode && statusCode < 300) || (isPost && statusCode == 201) ||(isPost && statusCode == 202) || (isDelete && statusCode == 202)
				|| (isDelete && statusCode == 200))
		{

			if (path.endsWith("_bulk_docs"))
			{ // Handle bulk doc update differently
				ok = new JSONArray(body).length() > 0;
			}
			else
			{
				ok = new JSONObject(body).getBoolean("ok");
			}

		}
		else if ((req instanceof HttpGet) || ((req instanceof HttpPost) && statusCode == 200))
		{
			ok = true;
		}
//		log.debug(toString());
	}

    public static String toString(
            final HttpEntity entity, final String defaultCharset,final InputStream bais) throws IOException, ParseException {
        if (entity == null) {
            throw new IllegalArgumentException("HTTP entity may not be null");
        }
        InputStream instream = bais == null ? entity.getContent() : bais;
        if (instream == null) {
            return "";
        }
        if (entity.getContentLength() > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("HTTP entity too large to be buffered in memory");
        }
        int i = (int)entity.getContentLength();
        if (i < 0) {
            i = 4096;
        }
        String charset = EntityUtils.getContentCharSet(entity);
        if (charset == null) {
            charset = defaultCharset;
        }
        if (charset == null) {
            charset = HTTP.DEFAULT_CONTENT_CHARSET;
        }
        Reader reader = new InputStreamReader(instream, charset);
        CharArrayBuffer buffer = new CharArrayBuffer(i); 
        try {
            char[] tmp = new char[1024];
            int l;
            while((l = reader.read(tmp)) != -1) {
                buffer.append(tmp, 0, l);
            }
        } finally {
            reader.close();
        }
        return buffer.toString();
    }
	@Override
	/**
	 * A better toString for this object... can be very verbose though.
	 */
	public String toString()
	{
		return "[" + methodName + "] " + path + " [" + statusCode + "] " + " => " + body;
	}

	/**
	 * Retrieves the body of the request as a JSONArray object. (such as listing
	 * database names)
	 * 
	 * @return
	 * @throws JSONException 
	 */
	public JSONArray getBodyAsJSONArray() throws JSONException
	{
		if (body == null)
			return null;
		return new JSONArray(body);
	}

	/**
	 * Was the request successful?
	 * 
	 * @return
	 */
	public boolean isOk()
	{
		return ok;
	}

	/**
	 * What was the error id?
	 * 
	 * @return
	 */
	public String getErrorId()
	{
		if (error_id != null)
		{
			return error_id;
		}
		return null;
	}

	/**
	 * what was the error reason given?
	 * 
	 * @return
	 */
	public String getErrorReason()
	{
		if (error_reason != null)
		{
			return error_reason;
		}
		return null;
	}

	/**
	 * Returns the body of the response as a JSON Object (such as for a
	 * document)
	 * 
	 * @return
	 * @throws JSONException 
	 */
	public JSONObject getBodyAsJSONObject() throws JSONException
	{
		if (body == null)
		{
			return null;
		}
		return new JSONObject(body);
	}

	/**
	 * Retrieves a specific header from the response (not really used anymore)
	 * 
	 * @param key
	 * @return
	 */
	public String getHeader(String key)
	{
		for (Header h : headers)
		{
			if (h.getName().equals(key))
			{
				return h.getValue();
			}
		}
		return null;
	}

	public String getBody()
	{
		return body;
	}

	public byte[] getBodyBytes()
	{
		return bodybytes;
	}
}

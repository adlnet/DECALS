package com.eduworks.resolver.service;

import java.io.InputStream;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Resolver;

public class ResolverValidatePaypal extends Resolver
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		StringBuilder paramString = new StringBuilder();
		boolean first = true;
		for (String key : parameters.keySet())
			for (String value : parameters.get(key))
			{
				if (!first)
					paramString.append("&");
				first = false;
				paramString.append(key + "=" + value);
			}
		boolean valid = validatePaypal(paramString.toString(),parameters);
		valid = valid & parameters.get("payment_status")[0].equals("Completed");
		System.out.println("Payment valid: " + valid);
		if (valid)
			resolveAChild(c,parameters, dataStreams, "success");
		else
			resolveAChild(c,parameters, dataStreams, "failure");
		
		return null;
	}

	protected boolean validatePaypal(String content, Map<String, String[]> parameters) throws JSONException
	{
		HttpPost post = null;
		if (optAsBoolean("test",false,parameters))
			 post = new HttpPost("https://www.sandbox.paypal.com/cgi-bin/webscr");
		else
			 post = new HttpPost("https://www.paypal.com/cgi-bin/webscr");
			
		String result = null;
		try
		{
			StringEntity requestEntity = new StringEntity(content + "&cmd=_notify-validate");
			// requestEntity.setContentType();
			post.setEntity(requestEntity);
			HttpResponse response = getClient().execute(post);
			HttpEntity responseEntity = response.getEntity();
			if (responseEntity != null)
			{
				InputStream instream = responseEntity.getContent();
				result = IOUtils.toString(instream);
				instream.close();
			}
		}
		catch (Exception e)
		{
		}

		if (result != null && result.contains("VERIFIED"))
			return true;
		return false;
	}

	private static HttpClient	httpClient	= null;

	public synchronized static HttpClient getClient()
	{
		if (httpClient == null)
		{
			DefaultHttpClient dhc = new DefaultHttpClient();
			BasicHttpParams params = new BasicHttpParams();
			httpClient = new DefaultHttpClient(new ThreadSafeClientConnManager(params, dhc.getConnectionManager()
					.getSchemeRegistry()), params);
			dhc.getConnectionManager().shutdown();
		}
		return httpClient;
	}

	@Override
	public String getDescription()
	{
		return "Vaidates a paypal receipt. You must know what parameters to put in. (see the Paypal API)";
	}

	@Override
	public String getReturn()
	{
		return "Boolean";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("<any>","String|Number");
	}

}

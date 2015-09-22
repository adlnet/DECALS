package com.eduworks.resolver.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Resolver;

public class ResolverOauth extends Resolver
{
	@Override
	public String getDescription()
	{
		return "Authenticates an oauth token. Will throw an exception if does not authenticate.";
	}

	@Override
	public String getReturn()
	{
		return "String";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("appId","String","oauthToken","String","oauthSecret","String","serverUrl","String","consumerKey","String","consumerSecret","String","federatedIdentity","String");
	}


	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		final String APP_ID = getAsString("appId",parameters);
		final String TOKEN = getAsString("oauthToken",parameters);
		final String TOKEN_SECRET = getAsString("oauthSecret",parameters);
		final String SERVER_URL = getAsString("serverUrl",parameters);
		final String CONSUMER_KEY = getAsString("consumerKey",parameters);
		final String CONSUMER_SECRET = getAsString("consumerSecret",parameters);
		final String FEDERATED_IDENTITY = getAsString("federatedIdentity",parameters);
		OAuthConsumer oauth = new DefaultOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
		oauth.setTokenWithSecret(TOKEN, TOKEN_SECRET);
		URLConnection http;
		try
		{
			URL url = new URL(String.format(SERVER_URL, APP_ID,
					URLEncoder.encode(FEDERATED_IDENTITY, "UTF-8")));
			http = url.openConnection();
			oauth.sign(http);
			http.connect();
			String response = IOUtils.toString(http.getInputStream());
			JSONObject responseObj = new JSONObject(response);
			if (responseObj.getString("result").equals("YES"))
				return responseObj;
			else
				throw new SecurityException("Failed to authenticate.");
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
			throw new SecurityException("Failed to authenticate. Malformed Oauth URL.");
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
			throw new SecurityException("Failed to authenticate. Unsupported Encoding.");
		}
		catch (IOException e)
		{
			e.printStackTrace();
			throw new SecurityException("Failed to authenticate. IO Exception.");
		}
		catch (OAuthMessageSignerException e)
		{
			e.printStackTrace();
			throw new SecurityException("Failed to authenticate. Error in signing.");
		}
		catch (OAuthExpectationFailedException e)
		{
			e.printStackTrace();
			throw new SecurityException("Failed to authenticate. Failed Expectation.");
		}
		catch (OAuthCommunicationException e)
		{
			e.printStackTrace();
			throw new SecurityException("Failed to authenticate. Communication Failure.");
		}
	}

}

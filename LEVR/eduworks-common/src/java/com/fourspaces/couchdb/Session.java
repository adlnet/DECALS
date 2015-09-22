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

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.ProtocolException;
import java.net.SocketException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.AllClientPNames;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.params.ConnManagerPNames;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * The Session is the main connection to the CouchDB instance. However, you'll
 * only use the Session to obtain a reference to a CouchDB Database. All of the
 * main work happens at the Database level.
 * <p>
 * It uses the Apache's HttpClient library for all communication with the
 * server. This is a little more robust than the standard URLConnection.
 * <p>
 * Ex usage: <br>
 * Session session = new Session(host,port); Database db =
 * session.getDatabase("dbname");
 *
 * @author mbreese
 * @author brennanjubb - HTTP-Auth username/pass
 */
public class Session
{
	private static final String	DEFAULT_CHARSET	= "UTF-8";

	private static final String	MIME_TYPE_JSON	= "application/json";

	protected Log				log				= LogFactory.getLog(Session.class);
	protected final String		host;
	protected final int			port;
	protected final String		user;
	protected final String		pass;
	protected final boolean		secure;
	protected final boolean		usesAuth;

	protected CouchResponse		lastResponse;

	BasicHttpContext			localContext	= new BasicHttpContext();

	BasicScheme					basicAuth		= new BasicScheme();

	protected HttpClient		httpClient;
	protected HttpParams		httpParams;

	/**
	 * Constructor for obtaining a Session with an HTTP-AUTH username/password
	 * and (optionally) a secure connection This isn't supported by CouchDB -
	 * you need a proxy in front to use this
	 *
	 * @param host
	 *            - hostname
	 * @param port
	 *            - port to use
	 * @param user
	 *            - username
	 * @param pass
	 *            - password
	 * @param secure
	 *            - use an SSL connection?
	 */
	static class PreemptiveAuthInterceptor implements HttpRequestInterceptor
	{

		public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException
		{
			AuthState authState = (AuthState) context.getAttribute(ClientContext.TARGET_AUTH_STATE);

			// If no auth scheme avaialble yet, try to initialize it
			// preemptively
			if (authState.getAuthScheme() == null)
			{
				AuthScheme authScheme = (AuthScheme) context.getAttribute("preemptive-auth");
				CredentialsProvider credsProvider = (CredentialsProvider) context
						.getAttribute(ClientContext.CREDS_PROVIDER);
				HttpHost targetHost = (HttpHost) context.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
				if (authScheme != null)
				{
					Credentials creds = credsProvider.getCredentials(new AuthScope(targetHost.getHostName(), targetHost
							.getPort()));
					if (creds == null)
					{
						throw new HttpException("No credentials for preemptive authentication");
					}
					authState.setAuthScheme(authScheme);
					authState.setCredentials(creds);
				}
			}

		}

	}

	public Session(String host, int port, String user, String pass, boolean usesAuth, boolean secure)
	{
		this.host = host;
		this.port = port;
		this.user = user;
		this.pass = pass;
		this.usesAuth = usesAuth;
		this.secure = secure;

		localContext.setAttribute("preemptive-auth", basicAuth);
		httpParams = new BasicHttpParams();
		httpParams.setIntParameter(ConnManagerPNames.MAX_TOTAL_CONNECTIONS, 200);
		httpParams.setParameter(CoreConnectionPNames.TCP_NODELAY, true);
		httpParams.setParameter(ConnManagerPNames.MAX_CONNECTIONS_PER_ROUTE,
				new org.apache.http.conn.params.ConnPerRoute()
				{

					@Override
					public int getMaxForRoute(HttpRoute arg0)
					{
						return 1000;
					}
				});
		SchemeRegistry schemeRegistry = new SchemeRegistry();

		schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		schemeRegistry.register(new Scheme("https", org.apache.http.conn.ssl.SSLSocketFactory.getSocketFactory(), 443));

		ThreadSafeClientConnManager connManager = new ThreadSafeClientConnManager(httpParams, schemeRegistry);

		DefaultHttpClient defaultClient = new DefaultHttpClient(connManager, httpParams);
		if (user != null)
		{
			defaultClient.addRequestInterceptor(new PreemptiveAuthInterceptor(), 0);
			defaultClient.getCredentialsProvider().setCredentials(AuthScope.ANY,
					new UsernamePasswordCredentials(user, pass));
		}

		this.httpClient = defaultClient;

		((AbstractHttpClient) httpClient).addRequestInterceptor(new PreemptiveAuthInterceptor(), 0);

		setUserAgent("couchdb4j");
		setSocketTimeout((60 * 1000));
		setConnectionTimeout((60 * 1000));

	}

	/**
	 * Constructor for obtaining a Session with an HTTP-AUTH username/password
	 * This isn't supported by CouchDB - you need a proxy in front to use this
	 *
	 * @param host
	 * @param port
	 * @param user
	 *            - username
	 * @param pass
	 *            - password
	 */
	public Session(String host, int port, String user, String pass)
	{
		this(host, port, user, pass, false, false);
	}

	/**
	 * Main constructor for obtaining a Session.
	 *
	 * @param host
	 * @param port
	 */
	public Session(String host, int port)
	{
		this(host, port, null, null, false, false);
	}

	/**
	 * Optional constructor that indicates an HTTPS connection should be used.
	 * This isn't supported by CouchDB - you need a proxy in front to use this
	 *
	 * @param host
	 * @param port
	 * @param secure
	 */
	public Session(String host, int port, boolean secure)
	{
		this(host, port, null, null, false, secure);
	}

	/**
	 * Read-only
	 *
	 * @return the host name
	 */
	public String getHost()
	{
		return host;
	}

	/**
	 * read-only
	 *
	 * @return the port
	 */
	public int getPort()
	{
		return port;
	}

	/**
	 * Is this a secured connection (set in constructor)
	 *
	 * @return
	 */
	public boolean isSecure()
	{
		return secure;
	}

	/**
	 * Retrieves a list of all database names from the server
	 *
	 * @return
	 * @throws JSONException
	 */
	public List<String> getDatabaseNames() throws JSONException
	{
		CouchResponse resp = get("_all_dbs");
		JSONArray ar = resp.getBodyAsJSONArray();

		List<String> dbs = new ArrayList<String>(ar.length());
		for (int i = 0; i < ar.length(); i++)
		{
			dbs.add(ar.getString(i));
		}
		return dbs;
	}

	/**
	 * Loads a database instance from the server
	 *
	 * @param name
	 * @return the database (or null if it doesn't exist)
	 * @throws JSONException
	 */
	public Database getDatabase(String name) throws JSONException
	{
		CouchResponse resp = get(name);
		if (resp.isOk())
		{
			return new Database(resp.getBodyAsJSONObject(), this);
		}
		else
		{
			log.warn("Error getting database: " + name);
		}
		return null;
	}

	/**
	 * Creates a new database (if the name doesn't already exist)
	 *
	 * @param name
	 * @return the new database (or null if there was an error)
	 * @throws JSONException
	 */
	public Database createDatabase(String name) throws JSONException
	{
		String dbname = name.toLowerCase().replaceAll("[^a-z0-9_$()+\\-/]", "_");
		if (!dbname.endsWith("/"))
		{
			dbname += "/";
		}
		CouchResponse resp = put(dbname);
		if (resp.isOk())
		{
			return getDatabase(dbname);
		}
		else
		{
			log.warn("Error creating database: " + name);
			return null;
		}
	}

	/**
	 * Deletes a database (by name) from the CouchDB server.
	 *
	 * @param name
	 * @return true = successful, false = an error occurred (likely the database
	 *         named didn't exist)
	 * @throws JSONException
	 */
	public boolean deleteDatabase(String name) throws JSONException
	{
		return delete(name).isOk();
	}

	/**
	 * Deletes a database from the CouchDB server
	 *
	 * @param db
	 * @return was successful
	 * @throws JSONException
	 */
	public boolean deleteDatabase(Database db) throws JSONException
	{
		return deleteDatabase(db.getName());
	}

	/**
	 * For a given url (such as /_all_dbs/), build the database connection url
	 *
	 * @param url
	 * @return the absolute URL (hostname/port/etc)
	 */
	protected String buildUrl(String url)
	{
		return ((secure) ? "https" : "http") + "://" + host + ":" + port + "/" + url;
	}

	protected String buildUrl(String url, String queryString)
	{
		return (queryString != null) ? buildUrl(url) + "?" + queryString : buildUrl(url);
	}

	protected String buildUrl(String url, NameValuePair[] params)
	{

		url = ((secure) ? "https" : "http") + "://" + host + ":" + port + "/" + url;

		if (params.length > 0)
		{
			url += "?";
		}
		for (NameValuePair param : params)
		{
			url += param.getName() + "=" + param.getValue();
		}

		return url;

	}

	/**
	 * Package level access to send a DELETE request to the given URL
	 *
	 * @param url
	 * @return
	 * @throws JSONException
	 */
	CouchResponse delete(String url) throws JSONException
	{
		HttpDelete del = new HttpDelete(buildUrl(url));
		return http(del);
	}

	/**
	 * Send a POST with no body / parameters
	 *
	 * @param url
	 * @return
	 * @throws JSONException
	 */
	CouchResponse post(String url) throws JSONException
	{
		return post(url, null, null);
	}

	/**
	 * Send a POST with body
	 *
	 * @param url
	 * @param content
	 * @return
	 * @throws JSONException
	 */
	CouchResponse post(String url, String content) throws JSONException
	{
		return post(url, content, null);
	}

	/**
	 * Send a POST with a body and query string
	 *
	 * @param url
	 * @param content
	 * @param queryString
	 * @return
	 * @throws JSONException
	 */
	CouchResponse post(String url, String content, String queryString) throws JSONException
	{
		HttpPost post = new HttpPost(buildUrl(url, queryString));
		if (content != null)
		{
			HttpEntity entity;
			try
			{
				entity = new StringEntity(content, DEFAULT_CHARSET);
				post.setEntity(entity);
				post.setHeader(new BasicHeader("Content-Type", MIME_TYPE_JSON));
			}
			catch (UnsupportedEncodingException e)
			{
				log.error(ExceptionUtils.getStackTrace(e));
			}
		}

		return http(post);
	}

	/**
	 * Send a PUT (for creating databases)
	 *
	 * @param url
	 * @return
	 * @throws JSONException
	 */
	CouchResponse put(String url) throws JSONException
	{
		return put(url, null);
	}

	/**
	 * Send a PUT with a body (for creating documents)
	 *
	 * @param url
	 * @param content
	 * @return
	 * @throws JSONException
	 */
	CouchResponse put(String url, String content) throws JSONException
	{
		HttpPut put = new HttpPut(buildUrl(url));
		if (content != null)
		{
			HttpEntity entity;
			try
			{
				entity = new StringEntity(content, DEFAULT_CHARSET);
				put.setEntity(entity);
				put.setHeader(new BasicHeader("Content-Type", MIME_TYPE_JSON));
			}
			catch (UnsupportedEncodingException e)
			{
				log.error(ExceptionUtils.getStackTrace(e));
			}
		}
		return http(put);
	}

	/**
	 * Overloaded Put using by attachments
	 *
	 * @throws JSONException
	 */
	CouchResponse put(String url, String ctype, InputStream attachment) throws JSONException
	{
		HttpPut put = new HttpPut(buildUrl(url));
		if (attachment != null)
		{
			HttpEntity entity;
			try
			{
				entity = new ByteArrayEntity(IOUtils.toByteArray(attachment));
				put.setEntity(entity);
				put.setHeader(new BasicHeader("Content-Type", ctype));
			}
			catch (UnsupportedEncodingException e)
			{
				log.error(ExceptionUtils.getStackTrace(e));
			}
			catch (IOException e)
			{
				log.error(ExceptionUtils.getStackTrace(e));
			}
		}
		return http(put);
	}

	/**
	 * Send a GET request
	 *
	 * @param url
	 * @return
	 * @throws JSONException
	 */
	CouchResponse get(String url) throws JSONException
	{
		HttpGet get = new HttpGet(buildUrl(url));
		return http(get);
	}

	public CouchResponse getSoft(String url) throws JSONException
	{
		HttpGet get = new HttpGet(buildUrl(url));
		return http(get,30);
	}

	/**
	 * Send a GET request with a number of name/value pairs as a query string
	 *
	 * @param url
	 * @param queryParams
	 * @return
	 * @throws JSONException
	 */
	CouchResponse get(String url, NameValuePair[] queryParams) throws JSONException
	{
		HttpGet get = new HttpGet(buildUrl(url, queryParams));
		return http(get);
	}

	/**
	 * Send a GET request with a queryString (?foo=bar)
	 *
	 * @param url
	 * @param queryString
	 * @return
	 * @throws JSONException
	 */
	CouchResponse get(String url, String queryString) throws JSONException
	{
		HttpGet get = new HttpGet(buildUrl(url, queryString));
		return http(get);
	}

	/**
	 * Method that actually performs the GET/PUT/POST/DELETE calls. Executes the
	 * given HttpMethod on the HttpClient object (one HttpClient per Session).
	 * <p>
	 * This returns a CouchResponse, which can be used to get the status of the
	 * call (isOk), and any headers / body that was sent back.
	 *
	 * @param req
	 * @return the CouchResponse (status / error / json document)
	 * @throws JSONException
	 */
	protected CouchResponse http(HttpRequestBase req) throws JSONException
	{
		return http(req, 0);
	}

	protected CouchResponse http(HttpRequestBase req, int attempt) throws JSONException
	{
		HttpResponse httpResponse = null;
		HttpEntity entity = null;
		CouchResponse response = null;
		try
		{
			if (usesAuth)
			{
				req.getParams().setBooleanParameter(ClientPNames.HANDLE_AUTHENTICATION, true);
			}
			httpResponse = httpClient.execute(req, localContext);
			entity = httpResponse.getEntity();
			response = new CouchResponse(req, httpResponse);
		}
		catch (ProtocolException e)
		{
			log.error(ExceptionUtils.getStackTrace(e));
			return null;
		}
		catch (IllegalStateException e)
		{
			log.error(ExceptionUtils.getStackTrace(e));
			if (attempt < 30)
				http(req,attempt + 1);
		}
		catch (IllegalArgumentException e)
		{
			if (attempt < 30)
				http(req,attempt + 1);
		}
		catch (SocketException e)
		{
			if (attempt < 30)
				http(req,attempt + 1);
			else
				log.error(e.getMessage());
		}
		catch (IOException e)
		{
			log.error(ExceptionUtils.getStackTrace(e));
			if (attempt < 30)
				http(req,attempt + 1);
		}
		finally
		{
			if (entity != null)
			{
				try
				{
					entity.consumeContent();
				}
				catch (IOException e)
				{
					// throw new RuntimeException(e);
				}
			}
		}
		if (response != null)
		if ("unauthorized".equals(response.getErrorId()))
			return http(req);
		return response;
	}

	public void setUserAgent(String ua)
	{
		httpParams.setParameter(AllClientPNames.USER_AGENT, ua);
	}

	public void setConnectionTimeout(int milliseconds)
	{
		httpParams.setIntParameter(AllClientPNames.CONNECTION_TIMEOUT, milliseconds);
	}

	public void setSocketTimeout(int milliseconds)
	{
		httpParams.setIntParameter(AllClientPNames.SO_TIMEOUT, milliseconds);
	}

	protected String encodeParameter(String paramValue)
	{
		try
		{
			return URLEncoder.encode(paramValue, DEFAULT_CHARSET);
		}
		catch (UnsupportedEncodingException e)
		{
			throw new RuntimeException(e);
		}
	}
}
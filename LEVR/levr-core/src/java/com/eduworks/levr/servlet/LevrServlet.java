package com.eduworks.levr.servlet;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Request;

import com.eduworks.levr.servlet.impl.LevrResolverServlet;

public abstract class LevrServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	protected static Logger log = Logger.getLogger(LevrResolverServlet.class);

	/* ABSTRACT METHODS */

	public abstract void go(boolean isPost, HttpServletRequest request, HttpServletResponse response, ServletOutputStream outputStream) throws IOException;

	public abstract String getServletPath();

	public abstract String getServletUsage();

	/* OVERRIDDEN METHODS */

	/**
	 * Both GET and POST execute in the same fashion. The only difference is
	 * that a GET is guaranteed not to have datastreams (files) attached to it.
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		ServletOutputStream outputStream = response.getOutputStream();
		try
		{
			if (Boolean.parseBoolean(getStringFromParameter(request, "usage", "false")))
			{
				outputStream.print(this.getServletUsage());
				return;
			}
			go(false, request, response, outputStream);
		}
		catch (Throwable e)
		{
			handleException(response, outputStream, e);
		}
		finally
		{
			closeOutputStream(request, outputStream);
		}
	}

	/**
	 * Both GET and POST execute in the same fashion. The only difference is
	 * that a GET is guaranteed not to have datastreams (files) attached to it.
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		ServletOutputStream outputStream = response.getOutputStream();
		try
		{
			if (Boolean.parseBoolean(getStringFromParameter(request, "usage", "false")))
			{
				outputStream.print(this.getServletUsage());
				return;
			}
			go(true, request, response, outputStream);
		}
		catch (Throwable e)
		{
			handleException(response, outputStream, e);
		}
		finally
		{
			closeOutputStream(request, outputStream);
		}
	}

	/**
	 * Both GET and POST execute in the same fashion. The only difference is
	 * that a GET is guaranteed not to have datastreams (files) attached to it.
	 */
	@Override
	public void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		ServletOutputStream outputStream = response.getOutputStream();
		try
		{
			if (Boolean.parseBoolean(getStringFromParameter(request, "usage", "false")))
			{
				outputStream.print(this.getServletUsage());
				return;
			}
			
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.setHeader("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE, OPTIONS");
			response.setHeader("Access-Control-Allow-Headers", "If-Modified-Since, Content-Type, Content-Range, Content-Disposition, Content-Description");

		}
		catch (Throwable e)
		{
			handleException(response, outputStream, e);
		}
		finally
		{
			closeOutputStream(request, outputStream);
		}
	}

	/* PROTECTED/PUBLIC METHODS */

	/**
	 * Appropriately wraps a JSONP call if the request has a callback parameter.
	 * This is required by cross-domain ajax service calls. See: <a
	 * href="http://bob.pythonmac.org/archives/2005/12/05/remote-json-jsonp/"
	 * >this link</a>
	 * 
	 * @param request
	 *            HTTP Request
	 * @param resultsAsString
	 *            The JSONP object to return.
	 * @return If appropriate, a JSONP object.
	 */
	protected String crossDomainFix(HttpServletRequest request, String resultsAsString)
	{
		String callback = getStringFromParameter(request, "callback", "");

		return (callback.isEmpty()) ? resultsAsString : callback + "(" + resultsAsString + ")";
	}

	/** @see {@link #crossDomainFix(HttpServletRequest, String)} */
	protected void crossDomainFixStart(HttpServletRequest request, PrintStream pw)
	{
		String callback = getStringFromParameter(request, "callback", "");
		if (!callback.isEmpty())
			pw.append(callback + "(");
	}

	/** @see {@link #crossDomainFix(HttpServletRequest, String)} */
	protected void crossDomainFixEnd(HttpServletRequest request, PrintStream pw)
	{
		String callback = getStringFromParameter(request, "callback", "");
		if (!callback.isEmpty())
			pw.append(")");
	}

	/**
	 * Get an integer value from the request object, defaulting to a known value
	 * if it is not present.
	 * 
	 * @param request
	 *            HTTP request
	 * @param key
	 *            HTTP request parameter name
	 * @param defValue
	 *            the default value if key does not exist
	 * @return the value corresponding to key parsed as an int, or the default
	 *         value
	 */
	protected int getIntFromParameter(HttpServletRequest request, String key, int defValue)
	{
		String param = getStringFromParameter(request, key, null);
		return (param == null) ? defValue : Integer.parseInt(param);
	}

	/**
	 * Get value from the request object, defaulting to a known value if it is
	 * not present.
	 * 
	 * @param request
	 *            HTTP request
	 * @param key
	 *            HTTP request parameter name
	 * @param defValue
	 *            the default value if key does not exist
	 * @return the value corresponding to key, or the default value
	 */
	protected static String getStringFromParameter(HttpServletRequest request, String key, String defValue)
	{
		String param;
		if ((param = request.getParameter(key)) != null)
			return param;
		else
			return defValue;
	}

	/**
	 * Get an string value from the request object, defaulting to a known value
	 * if it is not present.
	 * 
	 * @param request
	 *            HTTP request
	 * @param key
	 *            HTTP request parameter name
	 * @param defValue
	 *            the value to return if the key doesn't exist
	 * @return the param, attrib, or header value corresponding to the key, or
	 *         defValue
	 */
	protected String getStringFromRequest(HttpServletRequest request, String key, String defValue)
	{
		String param;
		if ((param = (String) request.getAttribute(key)) != null)
			return param;
		else if ((param = request.getParameter(key)) != null)
			return param;
		else if ((param = request.getHeader(key)) != null)
			return param;
		else
			return defValue;
	}

	private void closeOutputStream(HttpServletRequest request, ServletOutputStream outputStream) throws IOException
	{
		// If not yet handled, the OutputStream should not be closed
		if (request instanceof Request && !((Request) request).isHandled())
			return;

		outputStream.close();
	}

	private void handleException(HttpServletResponse response, ServletOutputStream outputStream, Throwable e) throws IOException
	{
		response.setStatus(500);
		e.printStackTrace();
		if (outputStream != null)
		{
			outputStream.println(e.getMessage());
			outputStream.flush();
		}
	}

	@Override
	public void log(String msg)
	{
		if (getServletContext() != null)
			super.log(msg);
		else
			System.out.println(msg);
	}

	protected Map<String, String[]> getParams(HttpServletRequest request)
	{
		return request.getParameterMap();
	}

	public String getServletPathExample()
	{
		return getServletPath();
	}

}

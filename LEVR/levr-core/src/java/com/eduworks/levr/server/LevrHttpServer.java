package com.eduworks.levr.server;

import org.eclipse.jetty.io.EndPoint;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.ServletHandler;
import org.mortbay.jetty.cert.JettyResourceHandler;

import com.eduworks.levr.servlet.LevrServlet;
import com.eduworks.levr.servlet.impl.LevrResolverServlet;

/**
 * This class is meant for developers to be able to run an instance of the LEVR
 * server without issue. It executes using JETTY, and servlets are available for
 * various tasks.
 * 
 * The root can be found at http://localhost:9722 Servlets are /api/custom (
 * {@link LevrResolverServlet}), /api/help ({@link LevrHelpServlet}), /api/test
 * ({@link LevrTestServlet}), /api/counters ({@link LevrCounterServlet}).
 * 
 * @author Fritz Ray
 * 
 */
public class LevrHttpServer extends Server
{
	/** Thread-safe lazy initialization for the singleton instance */
	private static class LevrHttpServerLoader
	{
		private static LevrHttpServer	INSTANCE	= new LevrHttpServer();
	}

	/*
	 * This holds the servlets that are available to the JETTY server. Note that
	 * TOMCAT servlets are recorded in the web/WEB-INF/web.xml.
	 */
	private final static LevrServlet[]	DEF_SERVLETS		= new LevrServlet[] {
			 new LevrResolverServlet()};

	protected final static String		SOAP_BIND_ADDR		= "http://localhost:9723/web/ws";
	protected final static String		DEF_RESOURCE_BASE	= "web";
	protected final static String		DEF_WELCOME_PAGE	= "index.html";

	public static final int				DEFAULT_PORT		= 9722;

	/**
	 * Activate the server on a specified port (9722 by default).
	 * 
	 * @param args
	 *            if present, the first is used as the port
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception
	{
		int port = (args.length > 0) ? Integer.parseInt(args[0]) : DEFAULT_PORT;
		LevrHttpServer.getInstance().setupMetadataServer(port);
		LevrHttpServer.getInstance().startMetadataServer();
	}

	/**
	 * Supports servlet classes extending {@link LevrServlet} for lazy
	 * initialization
	 */
	public static void attach(ServletHandler sh, Class<? extends LevrServlet> servlet)
	{
		try
		{
			LevrHttpServer.attach(sh, servlet.newInstance());
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * Multiple paths may be specified in {@link LevrServlet#getServletPath()}
	 * by separating them with whitespace.
	 */
	public static void attach(ServletHandler sh, LevrServlet servlet)
	{
		for (String servletPath : servlet.getServletPath().split("\\s+"))
			if (servletPath != null && servletPath.length() > 0)
				sh.addServletWithMapping(servlet.getClass(), servletPath);
	}

	/**
	 * Supports Class&lt;{@link LevrServlet}&gt;[] of servlets for lazy
	 * initialization
	 */
	public static void attachAll(ServletHandler sh, Class<? extends LevrServlet>[] array)
	{
		if (array != null)
			for (Class<? extends LevrServlet> servlet : array)
				LevrHttpServer.attach(sh, servlet);
	}

	/**
	 * Supports Class&lt;{@link LevrServlet}&gt;[] of servlets for lazy
	 * initialization
	 */
	public static void attachAll(ServletHandler sh, LevrServlet[] array)
	{
		if (array != null)
			for (LevrServlet servlet : array)
				LevrHttpServer.attach(sh, servlet);
	}

	/**
	 * Singleton Instance Retreival
	 */
	public static LevrHttpServer getInstance()
	{
		return LevrHttpServerLoader.INSTANCE;
	}

	public static LevrServlet[] getServlets()
	{
		return LevrHttpServer.DEF_SERVLETS.clone();
	}

	/* INSTANCE MEMBERS */

	/** Protected constructor as part of singleton implementation */
	protected LevrHttpServer()
	{
		// This always fails if called implicitly by child constructor
		if (this.getClass() == LevrHttpServer.class && LevrHttpServerLoader.INSTANCE != null)
			throw new IllegalStateException("Already instantiated");
	}

	/**
	 * Start up the server on the given port.
	 * 
	 * @param port
	 *            port to start server on, typically "9722".
	 * @param servletClasses
	 *            an array of LevrServlet classes to attach to the
	 *            {@link ServletHandler}
	 * @throws Exception
	 */
	public void setupMetadataServer(int port, Class<? extends LevrServlet>... servletClasses)
	{
		setupMetadataServer(port, SOAP_BIND_ADDR, DEF_RESOURCE_BASE, new String[] { DEF_WELCOME_PAGE }, servletClasses);
	}

	/**
	 * Start up the server on the given port, publish address, resource base
	 * folder, and welcome page(s).
	 * 
	 * @param port
	 *            the port to set on the HTTP {@link Connector}
	 * @param soapAddress
	 *            the {@link EndPoint} address to which to publish
	 * @param resourceBase
	 *            the base folder to set on the {@link JettyResourceHandler}
	 * @param welcomePages
	 *            the web pages to set as default for the
	 *            {@link JettyResourceHandler}
	 * @param servletClasses
	 *            an array of LevrServlet classes to attach to the
	 *            {@link ServletHandler}
	 * @throws Exception
	 */
	protected void setupMetadataServer(int port, String soapAddress, String resourceBase, String[] welcomePages,
			Class<? extends LevrServlet>... servletClasses)
	{
		ServerConnector connector = new ServerConnector(this);
		connector.setPort(port);
//		connector.setIdleTimeout(Long.MAX_VALUE);
		setConnectors(new Connector[] { connector });

		ServletHandler sh = new ServletHandler();
		LevrHttpServer.attachAll(sh, LevrHttpServer.getServlets());
		LevrHttpServer.attachAll(sh, servletClasses);

		JettyResourceHandler rh = new JettyResourceHandler();
		rh.setResourceBase(resourceBase);
		rh.setWelcomeFiles(welcomePages);

		HandlerCollection handlers = new HandlerCollection();
		handlers.setHandlers(new Handler[] { sh, rh, new ContextHandlerCollection(), new DefaultHandler() });
		setHandler(handlers);
	}

	public void startMetadataServer() throws Exception
	{
		if (this.isStopped())
		{
			this.start();
			join();
			System.out.println("Server started. Thread count: " + getThreadPool().getThreads());
		}
		else
		{
			throw new IllegalStateException("Server already started");
		}
	}
}

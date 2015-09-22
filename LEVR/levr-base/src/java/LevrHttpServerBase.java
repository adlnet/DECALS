import org.apache.log4j.Logger;

import com.eduworks.levr.server.LevrHttpServer;
import com.eduworks.levr.servlet.impl.LevrResolverServlet;

public class LevrHttpServerBase
{

	/**
	 * Activate the server on a specified port (9722 by default).
	 * 
	 * @param args
	 *            if present, the first is used as the port
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception
	{
		Logger logger = Logger.getLogger(LevrHttpServerBase.class);
		System.out.println("Info Log: " + logger.isInfoEnabled());
		System.out.println("Debug Log: " + logger.isDebugEnabled());
		System.out.println("Trace Log: " + logger.isTraceEnabled());
		int port = (args.length > 0) ? Integer.parseInt(args[0]) : LevrHttpServer.DEFAULT_PORT;
		LevrHttpServer.getInstance().setupMetadataServer(port);
		LevrHttpServer.getInstance().startMetadataServer();
		LevrResolverServlet.codeFiles.size();
	}
}

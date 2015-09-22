package com.eduworks.resolver.lang;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.lang.output.resolverLexer;
import com.eduworks.resolver.lang.output.resolverParser;

public class LevrResolverParser
{
	public static Logger	log	= Logger.getLogger(LevrResolverParser.class);

	public static Map<String, JSONObject> decodeStreams(File fileToDecode) throws JSONException
	{
		FileInputStream input = null;
		try
		{
			input = new FileInputStream(fileToDecode);
			resolverLexer rl = new resolverLexer(new ANTLRStringStream(IOUtils.toString(input)));
			CommonTokenStream tokens = new CommonTokenStream(rl);
			resolverParser rp = new resolverParser(tokens);
			try
			{
				rp.parse();
			}
			catch (RecognitionException e)
			{
				e.printStackTrace();
			}
			log.info("Reloaded Resolver Code File. " + rp.servlets.size() + " servlets detected.");
			log.debug(rp.servlets.keySet());
			System.out.println(rp.servlets.keySet());
			return rp.servlets;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
		finally
		{
			IOUtils.closeQuietly(input);
		}
	}
}

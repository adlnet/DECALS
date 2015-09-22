package com.eduworks.scripter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import rcaller.RCaller;
import rcaller.RCode;

import com.eduworks.resolver.Cruncher;

public class CruncherR extends Cruncher
{
	@Override
	public Object resolve(com.eduworks.resolver.Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		RCaller caller = new RCaller();
		String rPath = getAsString("path", c, parameters, dataStreams);
		String rCode = getObj(c, parameters, dataStreams).toString();
		String returns = getAsString("return", c, parameters, dataStreams);
		caller.setRscriptExecutable(rPath);
		RCode code = new RCode();
		// Passing Java objects to R
		code.addRCode(rCode);
		for (String key : keySet())
		{
			if (key.equals("obj")) continue;
			if (key.equals("return")) continue;
			if (key.equals("path")) continue;
			Object value = get(key, c, parameters, dataStreams);
			interpretAndSet(code, key, value);
		}
		caller.setRCode(code);
//		ByteArrayOutputStream o = new ByteArrayOutputStream();
//		caller.redirectROutputToStream(o);
//		caller.redirectROutputToConsole();
		// Performing Calculations
//		Process exec = null;
//		try
//		{
//			exec = Runtime.getRuntime().exec(rPath);
//			exec.wait();
//		}
//		catch (IOException e1)
//		{
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		catch (IllegalMonitorStateException e1)
//		{
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		catch (InterruptedException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		try
//		{
//			System.out.println(IOUtils.toString(exec.getInputStream()));
//			System.out.println(IOUtils.toString(exec.getErrorStream()));
//		}
//		catch (IOException e1)
//		{
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		exec.destroy();
		try
		{
			caller.redirectROutputToConsole();
			caller.runAndReturnResult(returns);
		}
		finally
		{
			caller.stopStreamConsumers();
		}
		try
		{
			return XML.toJSONObject(caller.getParser().getXMLFileAsString());
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	private void interpretAndSet(RCode code, String key,Object value) throws JSONException
	{
		if (value instanceof JSONArray)
		{
			JSONArray ary = (JSONArray) value;
			if (ary.get(0) instanceof JSONArray)
			{
				JSONArray ary2= ary.getJSONArray(0);
				double[][] matrix = new double[ary.length()][ary2.length()];
				for (int i = 0;i < ary.length();i++)
				{
					JSONArray aryx = ary.getJSONArray(i);
					for (int j = 0;j < aryx.length();j++)
					{
						matrix[i][j]=aryx.optDouble(j, Double.parseDouble(aryx.optString(j)));
					}
				}
				code.addDoubleMatrix(key, matrix);
			}
			else
			{
				try{
					double[] matrix = new double[ary.length()];
					for (int i = 0;i < ary.length();i++)
					{
						double val = Double.parseDouble(ary.get(i).toString());
						matrix[i] = val;
					}
					code.addDoubleArray(key, matrix);
				}
				catch (NumberFormatException p)
				{
					String[] matrix = new String[ary.length()];
					for (int i = 0;i < ary.length();i++)
					{
						String val = ary.get(i).toString();
						matrix[i] = val;
					}
					code.addStringArray(key, matrix);				
				}
			}
		}
	}

	@Override
	public String getDescription()
	{
		return "Allows LEVR to run javascript and interact with crunchers and resolvers.";
	}

	@Override
	public String getReturn()
	{
		return "The variable result in the javascript will return its value to the client when the code has finished being executed.";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("<expression>", "Required:An expression to be executed that is a valid javascript program", "<any variable arguments>",
				"Optional:Any variables that are not bound during the execution can be bound to the standard LEVR query paramters and dataStreams.");
	}
}

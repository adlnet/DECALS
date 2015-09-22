package com.eduworks.scripter;

import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;
import org.python.util.PythonInterpreter;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Scripter;

public class ScripterPython extends Scripter
{
	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		String expression = (String)get("expression");
		PythonInterpreter interpreter = new PythonInterpreter();
		interpreter.set("script", this);
		Set<Entry<String, String[]>> keys = parameters.entrySet();
		for (Entry<String, String[]> key : keys)
			interpreter.set(key.getKey(), key.getValue()[0]);
		interpreter.set("dataStreams", dataStreams);
		try {
			interpreter.exec(expression);
		} catch (Exception e) {
			e.printStackTrace();
			return e.toString();
		}
		Object result = interpreter.get("result");
		return (result==null)? "null" : result.toString();
	}

	@Override
	public String getDescription()
	{
		return "Allows LEVR to run python and interact with crunchers and resolvers.";
	}

	@Override
	public String getReturn()
	{
		return "The variable result in the python will return its value to the client when the code has finished being executed.";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("<expression>", "Required:An expression to be executed that is a valid python program", "<any variable arguments>", "Optional:Any variables that are not bound during the execution can be bound to the standard LEVR query paramters and dataStreams.");
	}
}

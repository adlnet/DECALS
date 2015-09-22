package com.eduworks.scripter;

import java.io.File;
import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ScriptableObject;

import com.eduworks.resolver.Scripter;
import com.eduworks.util.io.InMemoryFile;

public class ScripterJavascript extends Scripter
{	
	@Override
	public Object resolve(com.eduworks.resolver.Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		String expression = (String)get("expression");
		Context jsContext = Context.enter();
		ScriptableObject scope = jsContext.initStandardObjects();
		scope.defineProperty("script", this, ScriptableObject.DONTENUM);
		Set<Entry<String, String[]>> keys = parameters.entrySet();
		for (Entry<String, String[]> key : keys)
			scope.defineProperty(key.getKey(), key.getValue()[0], ScriptableObject.EMPTY);
		for (String key : keySet())
			scope.defineProperty(key, get(key), ScriptableObject.EMPTY);
		scope.defineProperty("dataStreams", dataStreams, ScriptableObject.DONTENUM);
		jsContext.evaluateString(scope, 
								 expression, 
								 "DynamicJS", 
								 1, 
								 null);
		Context.exit();

		if (scope.get("result")==null)
			return null;
		else if (scope.get("result") instanceof File || scope.get("result") instanceof InMemoryFile)
			return scope.get("result");
		else
			return (scope.get("result") instanceof String ? scope.get("result") : scope.get("result").toString()); 
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
		return jo("<expression>", "Required:An expression to be executed that is a valid javascript program", "<any variable arguments>", "Optional:Any variables that are not bound during the execution can be bound to the standard LEVR query paramters and dataStreams.");
	}
}

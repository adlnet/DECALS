package com.eduworks.cruncher.lang;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.lang.util.EwJson;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;
import com.eduworks.resolver.Resolvable;

public class CruncherBuildArguments extends Cruncher
{
	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		Resolvable resolvable = (Resolvable) get("obj");
		JSONObject argumentList = new JSONObject();
		
		boolean hard = optAsBoolean("hard", false, c, parameters, dataStreams);
		
		JSONObject arguments = getAsJsonObject("arguments", c, parameters, dataStreams);
		if (arguments==null) {
			Object o = get("arguments");
			if (o instanceof String)
				arguments = EwJson.getJsonObject((String)o);
			else
				arguments = (JSONObject)o;
		}
		int keyIndex = 0;
		String keyString = "key";
		String valueString = "value";
		Object value = get(valueString, c, parameters, dataStreams);
		String key = getAsString(keyString, c, parameters, dataStreams);
		while (key!=null) {
			argumentList.put(key, value);
			keyIndex++;
			keyString = "key" + keyIndex;
			valueString = "value" + keyIndex;
			value = get(valueString, c, parameters, dataStreams);
			key = getAsString(keyString, c, parameters, dataStreams);
		}
		
		String argumentsKey = "arguments";
		int argumentsKeyIndex = 0;
		while (arguments!=null) {				
			JSONArray argumentKeys = arguments.names();
			for (int argumentIndex = 0; argumentIndex < argumentKeys.length(); argumentIndex++)
				argumentList.put(argumentKeys.getString(argumentIndex), arguments.get(argumentKeys.getString(argumentIndex)));
			argumentsKeyIndex++;
			argumentsKey = "arguments" + argumentsKeyIndex;
			arguments = getAsJsonObject(argumentsKey, c, parameters, dataStreams);
			if (arguments==null) {
				Object o = get(argumentsKey);
				if (o instanceof String)
					arguments = EwJson.getJsonObject((String)o);
				else
					arguments = (JSONObject)o;
			}
		}
		
		Set<String> parameterKeys = parameters.keySet();
		for (Iterator<String> parameterKeyPointer = parameterKeys.iterator(); parameterKeyPointer.hasNext();) {
			String parameterKey = parameterKeyPointer.next();
			if (!parameterKey.equals("threadId")&&!parameterKey.equals("ip")&&!parameterKey.equals("urlRemainder"))
				argumentList.put(parameterKey, parameters.get(parameterKey)[0]);
		}
		
		String removeKey = "remove";
		int removeIndex = 0;
		String remove = getAsString(removeKey, c, parameters, dataStreams);
		while (remove!=null) {
			argumentList.remove(remove);
			
			if(hard){
				parameters.remove(remove);
			}
			removeIndex++;
			remove = getAsString(removeKey + removeIndex, c, parameters, dataStreams);
		}
		
		JSONArray argumentKeys = argumentList.names();
		for (int argumentIndex = 0; argumentIndex < argumentKeys.length(); argumentIndex++) {
			resolvable.build(argumentKeys.getString(argumentIndex), argumentList.get(argumentKeys.getString(argumentIndex)));
		}
		
		return resolvable.resolve(c, parameters, dataStreams);
	}

	@Override
	public String getDescription()
	{
		return "Takes an arguments, arguments1 JSONObjects and/or a key value (key=\"\", value=\"\", key1=\"\", value1=\"\") pairs and moves the parameters into arguments positions then adds all arguments to the obj then resolves the obj.";
	}

	@Override
	public String getReturn()
	{
		return "Object";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("obj", "Object", "?arguments", "JSONObject", "?key", "String", "?value", "Object");
	}
}

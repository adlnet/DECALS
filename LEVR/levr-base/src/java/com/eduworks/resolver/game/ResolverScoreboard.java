package com.eduworks.resolver.game;

import java.io.InputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.lang.EwList;
import com.eduworks.lang.json.impl.EwJsonObject;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Resolver;

public class ResolverScoreboard extends Resolver
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		resolveAllChildren(c, parameters, dataStreams);
		Object o = get("array", parameters);

		JSONArray ja = null;
		if (o instanceof JSONArray)
			ja = (JSONArray) o;
		if (ja == null)
			ja = new JSONArray();
		if (o instanceof EwJsonObject)
			ja.put(o);
		List<JSONObject> wses = new EwList<JSONObject>();
		for (int i = 0; i < ja.length(); i++)
		{
			wses.add(ja.getJSONObject(i));
		}
		JSONObject representativeWS = null;

		if (optAsBoolean("lookup", false, parameters))
		{
			int startPoint;
			if (has("key"))
			{
				for (JSONObject jo : wses)
					if (jo.getString("key").equals(getAsString("key", parameters)))
						representativeWS = jo;
				int focusPoint;
				if (representativeWS == null)
					focusPoint = 0;
				else
					focusPoint = wses.indexOf(representativeWS);
				startPoint = Math.max(0, focusPoint - optAsInteger("aboveCount", 9, parameters));
				int maxPoint = Math.min(wses.size(), startPoint + optAsInteger("belowCount", 10, parameters));
				wses = (List<JSONObject>) wses.subList(startPoint, maxPoint);
			}
			else
			{
				startPoint = 0;
				wses = wses.subList(0, optAsInteger("belowCount", 10, parameters));
			}
			for (int i = 0;i < 10 && i < wses.size();i++)
				wses.get(i).put("name","#"+(startPoint+i+1)+": "+wses.get(i).get("name"));
			for (int i = 0;i < wses.size();i++)
			{
				wses.get(i).remove("key");
			}
		}
		else
		{
			if (has("key"))
			{
				for (JSONObject jo : wses)
					if (jo.has("key"))
					if (jo.getString("key").equals(getAsString("key", parameters)))
						representativeWS = jo;
				if (representativeWS == null)
				{
					representativeWS = new JSONObject();
					representativeWS.put("key",getAsString("key", parameters));
					wses.add(representativeWS);
				}
				if (has("name"))
				{
					representativeWS.put("name",getAsString("name", parameters));
				}
				if (has("score"))
				{
					representativeWS.put("score",getAsDouble("score", parameters));
				}
			}
			Collections.sort(wses,new Comparator<JSONObject>(){

				@Override
				public int compare(JSONObject arg0, JSONObject arg1)
				{
					try
					{
						if (!arg1.has("score"))
							arg1.put("score", 0);
						if (!arg0.has("score"))
							arg0.put("score", 0);
						
						return (int) ((arg1.getDouble("score")-arg0.getDouble("score"))*100);
					}
					catch (JSONException e)
					{
						return 0;
					}
				}});
		}
		return wses;
	}

	@Override
	public String getDescription()
	{
		return "Manages a scoreboard represented as a JSONArray.\n" +
				"Fetch the scoreboard as a JSONArray and place it into array." +
				"When lookup is true, will find the score of people aboveCount and belowCount of the player represented by key.\n" +
				"When lookup is false, will insert or update the player represented by key = score, if name is provided, that will be used as the display name. The result will be the updated scoreboard.";
	}

	@Override
	public String getReturn()
	{
		return "JSONArray";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("array","JSONArray","key","String","name","String","score","Number","?lookup","Boolean","?aboveCount","Number","?belowCount","Number");
	}

}

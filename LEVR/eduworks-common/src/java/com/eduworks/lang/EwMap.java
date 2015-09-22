package com.eduworks.lang;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class EwMap<E,T> extends LinkedHashMap<E,T> implements Serializable
{
	public EwMap(int initialCapacity)
	{
		super(initialCapacity);
	}

	public EwMap()
	{
	}
	
	public EwMap(Map<E, T> parameters)
	{
		if (parameters != null)
		for (Map.Entry<E,T> e: parameters.entrySet())
			put(e.getKey(),e.getValue());
	}

	public EwMap(JSONObject parameters) throws JSONException
	{
		if (parameters != null)
		{
			Iterator<String> keys = parameters.keys();
			while (keys.hasNext())
			{
				String next = keys.next();
				put((E)next,(T)parameters.get(keys.next()));
			}
		}
	}

	public static final long serialVersionUID = 1L;
}

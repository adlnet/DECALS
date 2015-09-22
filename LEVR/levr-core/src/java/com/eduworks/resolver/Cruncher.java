package com.eduworks.resolver;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.lang.EwList;
import com.eduworks.lang.json.impl.EwJsonObject;
import com.eduworks.resolver.exception.EditableRuntimeException;

/*
 * High performance zero flexibility implementation of Resolver
 * 
 * Rules of implementation:
 * Crunchers are NOT JSON OBJECTS.
 * Abstract nothing. Make it as lean as possible.
 * Crunchers are immutable. This is important for things like memory allocation and loops.
 * One use case per cruncher. No special considerations, multi-purpose or anything like that.
 * Define cruncher's activity at top of cruncher.
 */
public abstract class Cruncher implements Resolvable, Cloneable
{
	public Map<String, Object> data = null;
	public boolean resolverCompatibilityReplaceMode = true;
	public static Logger log = Logger.getLogger(Cruncher.class);

	protected JSONObject jo(Object... strings) throws JSONException
	{
		JSONObject jo = new JSONObject();
		for (int i = 0; i < strings.length; i += 2)
		{
			jo.put(strings[i].toString(), strings[i + 1]);
		}
		return jo;
	}

	protected boolean isObj(String key)
	{
		return key.equals("obj");
	}

	public static void clearThreadCache()
	{
		Resolver.threadCache.remove(Thread.currentThread().getName());
	}

	public String toJson() throws JSONException
	{
		JSONObject jo = new JSONObject();
		for (String key : data.keySet())
		{
			Object object = data.get(key);
			if (object instanceof Resolvable)
				jo.put(key, ((Resolvable) object).toJson());
			else
				jo.put(key, object);
		}

		return jo.toString();
	}

	public String toOriginalJson() throws JSONException
	{
		JSONObject jo = new JSONObject();
		for (String key : data.keySet())
		{
			Object object = data.get(key);
			if (object instanceof Resolvable)
				jo.put(key, ((Resolvable) object).toOriginalJson());
			else
				jo.put(key, object);
		}

		String func = getClass().getSimpleName().replace("Resolver", "").replace("Cruncher", "");
		func = "c" + Character.toLowerCase(func.charAt(0)) + func.substring(1);

		jo.put("function", func);
		return jo.toString();
	}

	protected boolean has(String string)
	{
		return data.containsKey(string);
	}

	public Iterator<String> sortedKeys()
	{
		EwList<String> results = new EwList<String>(keySet());
		results.sort(results);
		return results.iterator();
	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		try
		{
			return ResolverFactory.create(this);
		}
		catch (JSONException e)
		{
			try
			{
				return new EwJsonObject(toString());
			}
			catch (JSONException e1)
			{
				throw new RuntimeException(e1);
			}
		}
	}

	public Set<String> keySet()
	{
		if (data == null)
			return new HashSet<String>();
		return data.keySet();
	}

	protected Object resolveAChild(String key, Context c,Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		if (c.shouldAbort()) return null;
		try
		{
			Object o = get(key);
			if (o instanceof Resolver)
			{
				Resolver resolver = (Resolver) o;
				if (resolverCompatibilityReplaceMode)
					resolver = (Resolver) resolver.clone();
				return Resolver.resolveAChild(c,parameters, dataStreams, key, resolver);
			}
			if (o instanceof Cruncher)
			{
				Cruncher cruncher = (Cruncher) o;
				return Resolver.resolveAChild(c,parameters, dataStreams, key, cruncher);
			}
			if (o instanceof Scripter)
			{
				Scripter scripter = (Scripter) o;
				return Resolver.resolveAChild(c,parameters, dataStreams, key, scripter);
			}
			return o;
		}
		catch (EditableRuntimeException ex)
		{
			ex.append("in " + getKeys(this));
			throw ex;
		}
		catch (CloneNotSupportedException e)
		{
			e.printStackTrace();
			throw new EditableRuntimeException("Failed to clone Resolver.");
		}
	}

	private Set<String> getKeys(Cruncher cruncher)
	{
		return data.keySet();
	}

	public Object get(String key)
	{
		if (data == null)
			return null;
		return data.get(key);
	}

	protected boolean hasParam(String key)
	{
		if (data.get(key) != null)
			if (data.get(key) instanceof String)
				if (data.get(key).toString().length()>1)
				return ((String) data.get(key)).startsWith("@");
		return false;
	}

	protected String optAsString(String key, String defaultx, Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		String result = getAsString(key, c, parameters, dataStreams);
		if (result == null)
			return defaultx;
		return result;
	}

	protected Double getAsDouble(String key, Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		Object obj = get(key, c, parameters, dataStreams);
		return objectToDouble(obj);
	}

	public Double objectToDouble(Object obj)
	{
		if (obj == null)
			return null;
		if (obj instanceof Integer)
			obj = new Double((Integer) obj);
		if (obj instanceof Long)
			obj = new Double((Long) obj);
		if (!(obj instanceof Double) && !(obj instanceof String))
			throw new EditableRuntimeException("Expected String or Double, got " + obj.getClass().getSimpleName());
		if (obj instanceof Double)
			return (Double) obj;
		else
			return Double.parseDouble((String) obj);
	}

	public Double optAsDouble(String key, double defaultx, Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		if (has(key))
			try
			{
				return getAsDouble(key, c, parameters, dataStreams);
			}
			catch (NumberFormatException ex)
			{
				return defaultx;
			}
		else
			return defaultx;
	}

	public String getAsString(String key, Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		Object obj = get(key, c, parameters, dataStreams);
		return objectToString(obj);
	}

	public String objectToString(Object obj)
	{
		if (obj == null)
			return null;
		if (obj instanceof Integer)
			return Integer.toString((Integer) obj);
		if (obj instanceof Double)
			if (((Double) obj) == Math.round(((Double) obj)))
				return Integer.toString(((Double) obj).intValue());
			else
				return Double.toString((Double) obj);
		if (!(obj instanceof String))
			throw new EditableRuntimeException("Expected String, got " + obj.getClass().getSimpleName());
		return (String) obj;
	}

	protected boolean optAsBoolean(String key, boolean defaultx, Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		String string = getAsString(key, c, parameters, dataStreams);
		if (string == null)
			return defaultx;
		return Boolean.parseBoolean(string);
	}

	protected int optAsInteger(String key, int defaultx, Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		String string = getAsString(key, c, parameters, dataStreams);
		if (string == null)
			return defaultx;
		return Integer.parseInt(string);
	}

	protected JSONObject getObjAsJsonObject(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		String key = "obj";
		return getAsJsonObject(key, c, parameters, dataStreams);
	}

	protected Object getObj(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		String key = "obj";
		if (hasParam(key))
			return Resolver.getParameter(data.get(key).toString().substring(1), parameters);
		return get(key, c, parameters, dataStreams);
	}

	protected JSONArray getObjAsJsonArray(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		String key = "obj";
		return getAsJsonArray(key, c, parameters, dataStreams);
	}

	public JSONObject getAsJsonObject(String key, Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		Object obj = get(key, c, parameters, dataStreams);
		if (obj == null)
			return null;
		if (!(obj instanceof JSONObject))
			throw new EditableRuntimeException("Expected JSONObject, got " + obj.getClass().getSimpleName());
		return (JSONObject) obj;
	}

	public JSONArray getAsJsonArray(String key, Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		Object obj = get(key, c, parameters, dataStreams);
		if (obj == null)
			return null;
		if (obj instanceof EwList)
			obj = new JSONArray((List) obj);
		if (!(obj instanceof JSONArray))
			throw new EditableRuntimeException("Expected JSONArray, got " + obj.getClass().getSimpleName());
		return (JSONArray) obj;
	}

	public static boolean isSetting(String key)
	{
		return Resolver.isSetting(key);
	}

	public void build(String key, Object value)
	{
		if (data == null)
			data = new LinkedHashMap<String, Object>();
		data.put(key, value);
	}

	protected Object get(String key, Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		if (hasParam(key))
			return (String) Resolver.getParameter(data.get(key).toString().substring(1), parameters);

		Object obj = resolveAChild(key, c,parameters, dataStreams);
		return obj;
	}

	public String getResolverName()
	{
		return getClass().getSimpleName().replace("Cruncher", "").toLowerCase().substring(0, 1)
				+ getClass().getSimpleName().replace("Cruncher", "").substring(1);
	}

	public String[] getResolverNames()
	{
		return new String[] { "c" + getResolverName(), getResolverName() };
	}
}

package com.eduworks.resolver;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.lang.EwCacheMap;
import com.eduworks.lang.EwList;
import com.eduworks.lang.EwMap;
import com.eduworks.lang.json.impl.EwJsonArray;
import com.eduworks.lang.json.impl.EwJsonObject;
import com.eduworks.lang.util.EwCache;
import com.eduworks.lang.util.EwJson;
import com.eduworks.lang.util.EwUri;
import com.fourspaces.couchdb.Document;
import com.fourspaces.couchdb.util.JSONUtils;

public abstract class Resolver extends EwJsonObject implements Cloneable, Resolvable
{
	/* STATIC MEMBERS */

	public static final String ID = "id";
	public static final String CACHE = "Resolver";

	static EwCacheMap<String, EwMap<String, Object>> threadCache = new EwCacheMap<String, EwMap<String, Object>>();

	public static void clearCache()
	{
		threadCache.clear();
		EwCache.getCache(CACHE).clear();
	}

	protected JSONObject jo(Object... strings) throws JSONException
	{
		JSONObject jo = new JSONObject();
		for (int i = 0; i < strings.length; i += 2)
		{
			jo.put(strings[i].toString(), strings[i + 1]);
		}
		return jo;
	}

	@Override
	public boolean equals(Object object)
	{
		if (object instanceof JSONObject)
			return ((JSONObject) object).toString().equals(this.toString());
		return super.equals(object);
	}

	@Override
	public String toOriginalJson() throws JSONException
	{
		JSONObject jo = new JSONObject();
		for (String key : keySet())
		{
			Object object = get(key);
			if (object instanceof Resolvable)
				jo.put(key, ((Resolvable) object).toOriginalJson());
			else
				jo.put(key, object);
		}

		String func = getClass().getSimpleName().replace("Resolver", "").replace("Cruncher", "c");
		func = (Character.toLowerCase(func.charAt(0))) + func.substring(1);

		jo.put("function", func);
		return jo.toString();
	}

	@Override
	public String toJson() throws JSONException
	{
		JSONObject jo = new JSONObject();
		for (String key : keySet())
		{
			Object object = get(key);
			if (object instanceof Resolvable)
				jo.put(key, ((Resolvable) object).toJson());
			else
				jo.put(key, object);
		}

		return jo.toString();
	}

	@Override
	public void build(String key, Object value) throws JSONException
	{
		put(key, value);
	}

	public static void clearThreadCache()
	{
		threadCache.remove(Thread.currentThread().getName());
	}

	public String getResolverName()
	{
		return getClass().getSimpleName().replace("Resolver", "").toLowerCase().substring(0, 1)
				+ getClass().getSimpleName().replace("Resolver", "").substring(1);
	}

	public String[] getResolverNames()
	{
		return new String[] { getResolverName() };
	}

	public static synchronized Object getCache(Context c, String cacheEntry)
	{
		return c.get(cacheEntry);
	}

	public static synchronized void putCache(Context c, String cacheEntry, Object o)
	{
		if (o == null)
			EwCache.getCache(CACHE).remove(cacheEntry);
		else
			EwCache.getCache(CACHE).put(cacheEntry, o);
	}

	public static Object getThreadCache(String cacheName)
	{
		String threadName = Thread.currentThread().getName();
		EwMap<String, Object> hashMap = threadCache.get(threadName);
		if (hashMap == null)
			return null;
		return hashMap.get(cacheName);
	}

	public static void putThreadCache(String cacheName, Object obj)
	{
		String threadName = Thread.currentThread().getName();
		EwMap<String, Object> hashMap = threadCache.get(threadName);
		if (hashMap == null)
		{
			hashMap = new EwMap<String, Object>();
			threadCache.put(threadName, hashMap);
		}
		if (obj == null)
			hashMap.remove(cacheName);
		else
			hashMap.put(cacheName, obj);
	}

	/** Equivalent to {@link EwUri#encodeValue(String)}, but throws exception. */
	protected static String encodeValue(String value) throws UnsupportedEncodingException
	{
		return URLEncoder.encode(decodeValue(value), EwUri.UTF_8);
	}

	public static String decodeValue(String value)
	{
		return EwUri.decodeValue(value);
	}

	protected static boolean isId(String key)
	{
		return ID.equals(key);
	}

	protected static boolean isSetting(String key)
	{
		return key != null && !key.isEmpty() && key.charAt(0) == '_';
	}

	protected static String getSettingKey(String key)
	{
		return (isSetting(key)) ? key : ('_' + key);
	}

	protected static void removeAllSettings(EwJsonObject object) throws JSONException
	{
		for (Object o : EwJson.toArray(object.names()))
		{
			final String key = o.toString();
			if (isSetting(key) && !key.equals(JSONUtils.COUCH_ID))
				object.remove(key);
		}
	}

	protected static void stripSettings(Document d) throws JSONException
	{
		for (Object o : EwJson.toArray(d.names()))
		{
			final String key = o.toString();
			if (isSetting(key) && !JSONUtils.isCouchSetting(key))
				if (!key.equals("_attachments"))
					d.remove(key);
		}
	}

	/**
	 * @return the object parsed or cast as a JSON object if possible, or the
	 *         object itself
	 */
	public static Object tryParseJson(Object object) throws JSONException
	{
		return EwJson.tryParseJson(object, false);
	}

	/* INSTANCE MEMBERS */

	public boolean debugAllowed = true;

	protected void debug(String debug, Map<String, String[]> parameters)
	{
		final String debugKey = "debug";

		if (!debugAllowed)
			return;
		if (parameters.containsKey(debugKey))
			if (parameters.get(debugKey)[0].equals(Boolean.TRUE.toString()))
			{
				System.out.println(debug);
			}
	}

	@Override
	public Object remove(String key)
	{
		Object result = super.remove(key);

		if (result == null && !isSetting(key))
			result = super.remove(getSettingKey(key));

		return result;
	}

	protected void removeAll()
	{
		for (String key : keySet())
			remove(key);
	}

	public void removeAllSettings() throws JSONException
	{
		removeAllSettings(this);
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

	@SuppressWarnings("unchecked")
	protected EwList<Object> values() throws JSONException
	{
		EwList<Object> values = new EwList<Object>();
		Iterator<String> i = keys();
		while (i.hasNext())
		{
			String key = i.next();
			values.add(super.get(key));
		}
		return values;
	}

	protected boolean hasParam(String key)
	{
		if (opt(key) != null)
			if (opt(key) instanceof String)
				return super.optString(key, "").startsWith("@");

		return false;
	}

	public Object get(String key, Map<String, String[]> parameters) throws JSONException
	{
		if (hasParam(key))
			return getParameter(super.get(key).toString().substring(1), parameters);

		if (super.has(key))
			return super.get(key);

		if (isSetting(key))
			return null;

		return get(getSettingKey(key), parameters);
	}

	public Object opt(String key, Map<String, String[]> parameters) throws JSONException
	{
		Object opt = super.opt(key);

		if (opt == null)
			return (isSetting(key)) ? null : opt(getSettingKey(key), parameters);

		if (hasParam(key))
			return getParameter(opt.toString().substring(1), parameters);

		return opt;
	}

	public static Object getParameter(String key, Map<String, String[]> parameters)
	{
		if (parameters == null)
			return null;

		String[] params = parameters.get(key);
		if (params == null)
			return null;
		else if (params.length == 1)
			return params[0];

		return params;
	}

	@SuppressWarnings("rawtypes")
	public JSONArray optAsArray(String key, Map<String, String[]> parameters) throws JSONException
	{
		Object o = opt(key, parameters);
		if (o == null)
			return new EwJsonArray();

		JSONArray ja = EwJson.getInstanceOfJsonArray(o);
		if (ja != null)
			return ja;

		JSONObject jo = EwJson.getInstanceOfJsonObject(o);
		if (o instanceof String || (jo != null && jo.length() > 0))
			return new EwJsonArray().put((jo == null) ? o : jo);

		if (o instanceof List)
			return new EwJsonArray((List) o);

		return null;
	}

	@SuppressWarnings("unchecked")
	protected List<String> getAsListOfStrings(String key, Map<String, String[]> parameters) throws JSONException
	{
		final Object param = get(key, parameters);

		if (param instanceof List)
			return (List<String>) get(key, parameters);

		JSONArray ja = EwJson.getInstanceOfJsonArray(param);
		List<String> results = new EwList<String>();

		if (ja != null)
			for (int i = 0; i < ja.length(); i++)
				results.add(ja.getString(i));
		else
			results.add(getAsString(key, parameters));

		return results;
	}

	public String getAsString(String key, Map<String, String[]> parameters) throws JSONException
	{
		return listAsString(getAsStrings(key, parameters));
	}

	public String optAsString(String key, Map<String, String[]> parameters) throws JSONException
	{
		return listAsString(optAsStrings(key, parameters));
	}

	protected String listAsString(List<String> results) throws JSONException
	{
		if (results.isEmpty())
			return "";
		else if (results.size() > 1)
			return new EwJsonArray(results).toString();
		else
			return results.get(0);
	}

	public boolean getAsBoolean(String key, Map<String, String[]> parameters) throws JSONException
	{
		if (get(key, parameters) == null)
			return false;
		return Boolean.parseBoolean(getAsString(key, parameters));
	}

	public boolean optAsBoolean(String key, boolean defaultx, Map<String, String[]> parameters) throws JSONException
	{
		if (opt(key, parameters) == null)
			return defaultx;
		return Boolean.parseBoolean(optAsString(key, parameters));
	}

	@SuppressWarnings("rawtypes")
	public Double getAsDouble(String key, Map<String, String[]> parameters) throws JSONException
	{
		Object o = get(key, parameters);
		if (o == null)
			return (Double) o;
		else if (o instanceof List)
		{
			List l = (List) o;
			if (l.size() == 0)
				return 0.0;
			if (l.size() != 1)
				return (double) l.size();
			return Double.parseDouble(l.get(0).toString());
		}
		else if (o instanceof String)
			return Double.parseDouble(o.toString());
		else if (o instanceof Resolver)
		{
			Resolver obj = (Resolver) o;
			if (obj.names() != null && obj.names().length() > 0)
				return obj.getAsDouble(obj.names().get(0).toString(), parameters);
		}
		else if (o instanceof Double)
			return (Double) o;
		else if (o instanceof Double[])
		{
			Double[] l = (Double[]) o;
			if (l.length == 0)
				return 0.0;
			if (l.length != 1)
				return (double) l.length;
			return l[0];
		}
		else if (o instanceof String[])
		{
			String[] l = (String[]) o;
			if (l.length == 0)
				return 0.0;
			if (l.length != 1)
				return (double) l.length;
			return Double.parseDouble(l[0].toString());
		}
		else if (o instanceof Object[])
		{
			Object[] l = (Object[]) o;
			if (l.length == 0)
				return 0.0;
			if (l.length != 1)
				return (double) l.length;
			return Double.parseDouble(l[0].toString());
		}
		else if (o instanceof Integer)
			return ((Integer) o).doubleValue();
		throw new NumberFormatException("Attempted to parse double, do not know what it is: " + o.getClass().getSimpleName());
	}

	public Double optAsDouble(String key, Double defaultx, Map<String, String[]> parameters) throws JSONException
	{
		if (opt(key, parameters) == null)
			return defaultx;
		try
		{
			return Double.parseDouble(optAsString(key, parameters));
		}
		catch (NumberFormatException ex)
		{
			return defaultx;
		}
	}

	public Integer getAsInteger(String key, Map<String, String[]> parameters) throws JSONException
	{
		return getAsDouble(key, parameters).intValue();
	}

	public Integer optAsInteger(String key, Integer defaultx, Map<String, String[]> parameters) throws JSONException
	{
		if (opt(key, parameters) == null)
			return defaultx;
		try
		{
			return Integer.parseInt(optAsString(key, parameters));
		}
		catch (NumberFormatException ex)
		{
			return defaultx;
		}
	}

	public EwList<String> getAsStrings(String key, Map<String, String[]> parameters) throws JSONException
	{
		return objectAsStrings(get(key, parameters));
	}

	public EwList<String> optAsStrings(String key, Map<String, String[]> parameters) throws JSONException
	{
		return objectAsStrings(opt(key, parameters));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected EwList<String> objectAsStrings(Object o) throws JSONException
	{
		if (o == null)
			return new EwList<String>();

		JSONArray ja = (JSONArray) (o instanceof JSONArray ? o : null);
		if (ja != null)
			return (EwJson.toArray(ja)).toStringsEwList();

		JSONObject jo = (JSONObject) (o instanceof JSONObject ? o : null);
		if (jo != null)
		{
			final EwList<Object> results = new EwList<Object>();
			final Iterator<String> iter = jo.keys();

			while (iter.hasNext())
			{
				Object inner = jo.get(iter.next());

				if (inner instanceof JSONArray)
					results.addAll(EwJson.toArray((JSONArray) inner));
				else if (inner instanceof JSONObject)
					results.addAll(EwJson.flatten((JSONObject) inner));
				else
					results.add(inner);
			}
			return results.toStringsEwList();
		}
		else if (o instanceof EwList)
			return ((EwList) o).toStringsEwList();
		else if (o instanceof List)
			return new EwList((List) o).toStringsEwList();
		else if (o instanceof String)
			return new EwList(new String[] { (String) o });
		else if (o instanceof Double)
			return new EwList(new String[] { Double.toString((Double) o) });
		else if (o instanceof Object[])
			return new EwList((Object[]) o).toStringsEwList();
		else
			return new EwList(new String[] { o.toString() });
	}

	public Object getComplex(String path, JSONObject object) throws JSONException
	{
		Object complex = EwJson.derefComplexKey(object, path);

		if (complex instanceof String)
			complex = decodeValue((String) complex);

		return complex;
	}

	protected boolean has(Map<String, String[]> parameters, String key)
	{
		return parameters.containsKey(key) && parameters.get(key) != null && parameters.get(key).length != 0;
	}

	protected boolean has(Map<String, String[]> parameters, String key, String value)
	{
		return has(parameters, key) && Arrays.asList(parameters.get(key)).contains(value);
	}

	public void resolveAllChildren(Context c,Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		resolveAllChildren(c,this, parameters, dataStreams);
	}

	public static void resolveAllChildren(Context c,JSONObject settings, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		@SuppressWarnings("unchecked")
		final Iterator<String> keys = settings.sortedKeys();

		while (keys.hasNext())
			resolveAChild(c,settings, parameters, dataStreams, keys.next());
	}

	protected static void resolveAChild(Context c,JSONObject settings, Map<String, String[]> parameters, Map<String, InputStream> dataStreams, String key)
			throws JSONException
	{
		Object o = null;
		if (settings.has(key))
			o = settings.get(key);
		else
			o = settings.get("_" + key);
		if (o instanceof Resolvable)
		{
			Resolvable resolver = (Resolvable) o;
			settings.put(key, resolveAChild(c,parameters, dataStreams, key, resolver));
		}
	}

	protected void resolveAChild(Context c,Map<String, String[]> parameters, Map<String, InputStream> dataStreams, String key) throws JSONException
	{
		resolveAChild(c,this, parameters, dataStreams, key);
	}

	protected static Object resolveAChild(Context c,Map<String, String[]> parameters, Map<String, InputStream> dataStreams, String key, Resolvable thing)
			throws JSONException
	{
		if (c.shouldAbort()) return null;
		if (thing instanceof Resolver)
			return resolveAChildR(c,parameters, dataStreams, key, (Resolver) thing);
		if (thing instanceof Cruncher)
			return resolveAChildC(c,parameters, dataStreams, key, (Cruncher) thing);
		if (thing instanceof Scripter)
			return resolveAChildS(c,parameters, dataStreams, key, (Scripter) thing);
		throw new RuntimeException("Don't understand how to resolve " + thing);
	}

	protected static Object resolveAChildR(Context c,Map<String, String[]> parameters, Map<String, InputStream> dataStreams, String key, Resolver resolver)
			throws JSONException
	{
		String shortName = resolver.getClass().getSimpleName().replace("Resolver", "");
		try
		{
			Object resolved = resolver.resolve(c, parameters, dataStreams);
			return resolved;
		}
		catch (JSONException e)
		{
			throw e;
		}
		catch (RuntimeException e)
		{
			throw e;
		}
		finally
		{
		}
	}

	protected static Object resolveAChildC(Context c,Map<String, String[]> parameters, Map<String, InputStream> dataStreams, String key, Cruncher resolver)
			throws JSONException
	{
		try
		{
			Object resolved = resolver.resolve(c, parameters, dataStreams);
			return resolved;
		}
		catch (JSONException e)
		{
			throw e;
		}
	}

	protected static Object resolveAChildS(Context c,Map<String, String[]> parameters, Map<String, InputStream> dataStreams, String key, Scripter resolver)
			throws JSONException
	{
		try
		{
			Object resolved = resolver.resolve(c, parameters, dataStreams);
			return resolved;
		}
		catch (JSONException e)
		{
			throw e;
		}
	}
}

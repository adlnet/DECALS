package com.eduworks.resolver;

import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.lang.EwList;
import com.eduworks.lang.json.impl.EwJsonObject;
import com.eduworks.resolver.exception.EditableRuntimeException;

public abstract class Scripter implements Resolvable, Cloneable {
	Map<String, Object> data = null;
	public boolean resolverCompatibilityReplaceMode = true;
	public static Logger log = Logger.getLogger(Scripter.class);

	public boolean isObj(String key) {
		return key.equals("obj");
	}

	protected JSONObject jo(Object...strings) throws JSONException
	{
		JSONObject jo = new JSONObject();
		for (int i = 0;i < strings.length;i += 2)
		{
			jo.put(strings[i].toString(),strings[i+1]);
		}
		return jo;
	}
	
	public String toJson() throws JSONException {
		JSONObject jo = new JSONObject();
		for (String key : data.keySet()) {
			Object object = data.get(key);
			if (object instanceof Resolvable)
				jo.put(key, ((Resolvable) object).toJson());
			else
				jo.put(key, object);
		}

		return jo.toString();
	}

	public String toOriginalJson() throws JSONException {
		JSONObject jo = new JSONObject();
		for (String key : data.keySet()) {
			Object object = data.get(key);
			if (object instanceof Resolvable)
				jo.put(key, ((Resolvable) object).toOriginalJson());
			else
				jo.put(key, object);
		}

		String func = getClass().getSimpleName().replace("Resolver", "")
				.replace("Cruncher", "").replace("Scripter", "");
		func = "s" + Character.toLowerCase(func.charAt(0)) + func.substring(1);

		jo.put("function", func);
		return jo.toString();
	}

	public boolean has(String string) {
		return data.containsKey(string);
	}

	public Iterator<String> sortedKeys() {
		EwList<String> results = new EwList<String>(data.keySet());
		results.sort(results);
		return results.iterator();
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		try {
			return ResolverFactory.create(this);
		} catch (JSONException e) {
			try {
				return new EwJsonObject(toString());
			} catch (JSONException e1) {
				throw new RuntimeException(e1);
			}
		}
	}
	
	public Object jsonObject(Object o) {
		
		if (o instanceof JSONObject) {
			JSONObject jo = (JSONObject)o;
			try {
				JSONArray keys = jo.names();
				for (int keyIndex=0; keyIndex<keys.length(); keyIndex++) {
					Object element = jo.get(keys.getString(keyIndex));
					if ((element instanceof JSONObject)||(element instanceof JSONArray))
						jo.put(keys.getString(keyIndex), jsonObject(element));
				}
				jo.put("function", "object");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else if (o instanceof JSONArray) {
			try {
				JSONArray array = (JSONArray) o;
				for (int keyIndex=0; keyIndex<array.length(); keyIndex++) {
					Object element = array.get(keyIndex);
					if ((element instanceof JSONObject)||(element instanceof JSONArray))
						array.put(keyIndex, jsonObject(element));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return o;
	}

	public Set<String> keySet() {
		return data.keySet();
	}

	public Object resolve(String key, Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException {
		try {
			JSONObject functionPack = new JSONObject();
			functionPack.put("function", key);
			Set<Entry<String, String[]>> parameterSet = parameters.entrySet();
			for (Entry<String, String[]> parameter : parameterSet)
				functionPack.put(parameter.getKey(), parameter.getValue());
			Object o = ResolverFactory.create(functionPack);
			if (o instanceof Resolver) {
				Resolver resolver = (Resolver) o;
				if (resolverCompatibilityReplaceMode)
					resolver = (Resolver) resolver.clone();
				return Resolver.resolveAChild(c,parameters, dataStreams, key,
						resolver);
			}
			if (o instanceof Cruncher) {
				Cruncher cruncher = (Cruncher) o;
				return Resolver.resolveAChild(c,parameters, dataStreams, key,
						cruncher);
			}
			if (o instanceof Scripter) {
				Scripter cruncher = (Scripter) o;
				return Resolver.resolveAChild(c,parameters, dataStreams, key,
						cruncher);
			}
			return o;
		} catch (EditableRuntimeException ex) {
			ex.append("in " + getKeys(this));
			throw ex;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			throw new EditableRuntimeException("Failed to clone Resolver.");
		}
	}
	
	private Set<String> getKeys(Scripter cruncher) {
		return data.keySet();
	}

	public Object get(String key) {
		if (data == null)
			return null;
		return data.get(key);
	}

	public boolean hasParam(String key) {
		if (data.get(key) != null)
			if (data.get(key) instanceof String)
				return ((String) data.get(key)).startsWith("@");
		return false;
	}

	public static boolean isSetting(String key) {
		return Resolver.isSetting(key);
	}

	public void build(String key, Object value) {
		if (data == null)
			data = new LinkedHashMap<String, Object>();
		data.put(key, value);
	}

	public String getResolverName() {
		return getClass().getSimpleName().replace("Scripter", "").toLowerCase()
				.substring(0, 1)
				+ getClass().getSimpleName().replace("Scripter", "")
						.substring(1);
	}

	public String[] getResolverNames() {
		return new String[] { "s" + getResolverName(), getResolverName() };
	}
}

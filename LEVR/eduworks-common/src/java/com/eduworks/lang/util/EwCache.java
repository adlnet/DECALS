package com.eduworks.lang.util;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class EwCache<K, V>
{
	public static boolean cache = true;
	public static EwCache<String, SoftReference<EwCache<Object, Object>>>	caches	= new EwCache<String, SoftReference<EwCache<Object, Object>>>(20);
	public static synchronized EwCache<Object, Object> getCache(String name)
	{
		return getCache(name,2000);
	}
	public static synchronized EwCache<Object, Object> getCache(String name,int count)
	{
		if (!cache)
			return null;
		SoftReference<EwCache<Object, Object>> cache = caches.get(name);
		EwCache<Object, Object> cacheActual;
		if (cache != null)
			if ((cacheActual = cache.get()) != null)
				return cacheActual;
		System.out.println("Creating cache: " + name);
		caches.put(name, new SoftReference<EwCache<Object, Object>>(cacheActual = new EwCache<Object, Object>(count)));
		return cacheActual;
	}

	private static final float	hashTableLoadFactor	= 0.75f;

	private LinkedHashMap<K, V>	map;
	private int					cacheSize;

	public EwCache(int cacheSize)
	{
		this.cacheSize = cacheSize;
		int hashTableCapacity = (int) Math.ceil(cacheSize / hashTableLoadFactor) + 1;
		map = new LinkedHashMap<K, V>(hashTableCapacity, hashTableLoadFactor, true)
		{
			private static final long	serialVersionUID	= 1;

			@Override
			protected boolean removeEldestEntry(Map.Entry<K, V> eldest)
			{
				return size() > EwCache.this.cacheSize;
			}
		};
	}

	public synchronized V get(K key)
	{
		return map.get(key);
	}

	public synchronized void put(K key, V value)
	{
		if (cache)
		map.put(key, value);
	}

	public synchronized void clear()
	{
		map.clear();
	}

	public synchronized int usedEntries()
	{
		return map.size();
	}

	public synchronized Collection<Map.Entry<K, V>> getAll()
	{
		return new ArrayList<Map.Entry<K, V>>(map.entrySet());
	}

	public void remove(K name)
	{
		map.remove(name);
	}

	public static void clearAll()
	{
		caches.clear();
	}
	public Set<Entry<K,V>> entrySet()
	{
		return map.entrySet();
	}
	public int size()
	{
		return map.size();
	}

}

package com.eduworks.lang;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Map;
import java.util.Set;

import sun.misc.GC;

/***
 * A map to be used to cache objects. Objects may vanish at any point based on
 * memory available and garbage collection.
 * 
 * @author Fritz
 * 
 * @param <E>
 *            Key type
 * @param <T>
 *            Value type
 */
public class EwCacheMap<E, T> implements Map<E, T>
{
	private static final long serialVersionUID = 1L;

	Map<E, SoftReference<T>> map = Collections.synchronizedMap(new EwMap<E, SoftReference<T>>());

	long lastCleaned = System.currentTimeMillis();

	public void clear()
	{
		map.clear();
	}

	/***
	 * Removes all dangling soft references.
	 */
	private void cull()
	{
		long current = System.currentTimeMillis() - GC.maxObjectInspectionAge();
		if (current == lastCleaned)
			return;
		if (current < lastCleaned + 5 * 1000 * 60)
			return;

		lastCleaned = current;
		try
		{
			EwList<E> removeThese = new EwList<E>();
			for (Map.Entry<E, SoftReference<T>> e : map.entrySet())
				if (e.getValue().get() == null)
					removeThese.add(e.getKey());
			while (removeThese.isEmpty() == false)
				remove(removeThese.remove(0));
		}
		catch (ConcurrentModificationException ex)
		{
		}

	}

	public boolean containsKey(Object arg0)
	{
		return map.get(arg0) != null;
	}

	public boolean containsValue(Object arg0)
	{
		return map.containsValue(new WeakReference<T>((T) arg0));
	}

	public Set<java.util.Map.Entry<E, T>> entrySet()
	{
		EwHashMap<E, T> set = new EwHashMap<E, T>();
		for (Entry<E, SoftReference<T>> r : map.entrySet())
		{
			T value = r.getValue().get();
			if (value == null)
				continue;
			set.put(r.getKey(), value);
		}
		return set.entrySet();
	}

	public T get(Object arg0)
	{
		cull();
		return getInner(arg0);
	}

	/***
	 * Returns the inner object of a soft reference, if it exists and is not
	 * null.
	 * 
	 * @param arg0
	 *            The key object
	 * @return The value object
	 */
	private T getInner(Object arg0)
	{
		SoftReference<T> weakReference = map.get(arg0);
		if (weakReference != null)
			return weakReference.get();
		return null;
	}

	public boolean isEmpty()
	{
		for (SoftReference<T> t : map.values())
			if (t.get() != null)
				return false;
		return true;
	}

	public Set<E> keySet()
	{
		return map.keySet();
	}

	public T put(E arg0, T arg1)
	{
		cull();
		map.put(arg0, new SoftReference<T>(arg1));
		return arg1;
	}

	public void putAll(Map<? extends E, ? extends T> arg0)
	{
		for (E e : arg0.keySet())
			put(e, arg0.get(e));
	}

	public T remove(Object arg0)
	{
		SoftReference<T> remove = map.remove(arg0);
		if (remove == null)
			return null;
		return remove.get();
	}

	public int size()
	{
		int i = 0;
		for (SoftReference<T> t : map.values())
			if (t.get() != null)
				i++;
		return i;
	}

	public Collection<T> values()
	{
		EwList<T> results = new EwList<T>();
		for (SoftReference<T> t : map.values())
		{
			T t2 = t.get();
			if (t2 != null)
				results.add(t2);
		}
		return results;
	}

}

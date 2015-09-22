package com.eduworks.lang;

import java.util.Iterator;


public class EwSet<T> extends EwHashSet<T>
{

	public EwSet(EwList<T> asStrings)
	{
		addAll(asStrings);
	}

	public EwSet()
	{
	}

	public void addAll(T[] array)
	{
		for (T t : array)
			add(t);
	}

	public String join(String delimiter)
	{
		StringBuffer buffer = new StringBuffer();
		Iterator<T> iter = iterator();
		while (iter.hasNext())
		{
			buffer.append(iter.next().toString());
			if (iter.hasNext())
			{
				buffer.append(delimiter);
			}
		}
		return buffer.toString();
	}

}

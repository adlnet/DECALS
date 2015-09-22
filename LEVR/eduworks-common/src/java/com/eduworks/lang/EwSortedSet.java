package com.eduworks.lang;

import java.util.AbstractCollection;
import java.util.TreeSet;

public class EwSortedSet<E> extends TreeSet<E>
{
	private static final long	serialVersionUID	= 1L;

	public EwSortedSet(EwSortedSet<E> hs)
	{
		super(hs);
	}

	public EwSortedSet()
	{
	}
	
	public boolean containsAny(AbstractCollection<E> coll)
	{
		for (E e : coll)
			for (E r : this)
				if (e.equals(r))
					return true;
		return false;
	}
}

package com.eduworks.lang;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.TreeSet;

public class EwTreeSet<E> extends TreeSet<E>
{
	private static final long	serialVersionUID	= 1L;

	public EwTreeSet(Collection<E> hs)
	{
		super(hs);
	}

	public EwTreeSet()
	{
	}
	
	public boolean containsAny(AbstractCollection<E> coll)
	{
		for (E e : coll)
			if (this.contains(e))
				return true;
		return false;
	}
}

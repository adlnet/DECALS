package com.eduworks.util;

import java.io.Serializable;

/**
 * Utility class designed to hold a pair of typed objects. Useful for creating
 * lists of pairs or for returning two values from a function.
 * 
 * Note that this is a TUple, not a more general N-Tuple (though you can use an
 * array of Object to get pretty close). ThreeTuple, FourTuple, and such can be
 * written as needed.
 * 
 * This class is technically comparable, but this works only if the first
 * element is of a comparable type. If you try otherwise it returns a
 * UnsupportedOperationException. You can test if it's comparable using:
 * 
 * myTuple.getKey() instanceof Comparable
 * 
 * @author Tom Wrensch
 * 
 * @param <T>
 *            Type of the first object
 * @param <S>
 *            Type of the second object
 */
public class Tuple<T, S> implements Comparable<Tuple<T, S>>, Serializable
{
	T first;
	S second;

	/**
	 * Constructor. Note that this object is not mutable, so the two values MUST
	 * be set in the constructor.
	 * 
	 * @param first
	 *            first object
	 * @param second
	 *            second object
	 */
	public Tuple(T first, S second)
	{
		this.first = first;
		this.second = second;
	}

	/**
	 * @return the first object in the tuple
	 */
	public T getFirst()
	{
		return first;
	}

	/**
	 * @return the second object in the tuple
	 */
	public S getSecond()
	{
		return second;
	}

	/**
	 * Returns the hash code. Has to match the way equals() works.
	 */
	public int hashCode()
	{
		return (first == null ? 0 : first.hashCode()) + (second == null ? 0 : second.hashCode());
	}

	/**
	 * Return true if the given object represents the same information as
	 * myself. True only if it is a tuple and the first and second items are
	 * equal to the first and second items in the original.
	 */
	public boolean equals(Object obj)
	{
		if (obj instanceof Tuple)
		{
			Tuple<?, ?> t = (Tuple<?, ?>) obj;
			if (first == null && second == null)
				return t.getFirst() == null && t.getSecond() == null;
			else if (first == null)
				return second.equals(t.getSecond());
			else if (second == null)
				return first.equals(t.getFirst());
			else
				return first.equals(t.getFirst()) && second.equals(t.getSecond());
		}
		else
			return false;
	}

	/**
	 * Return a humnan-readable representation of myself.
	 */
	public String toString()
	{
		return "Tuple[" + getFirst() + "," + getSecond() + "]";
	}

	/**
	 * Compare with another tuple. This works only if the first (T) type is a
	 * comparable, otherwise it throws an exception.
	 */
	public int compareTo(Tuple<T, S> tup)
	{
		if (first == null)
			return tup.getFirst() == null ? 0 : -1;
		else if (first instanceof Comparable)
			return ((Comparable<T>) first).compareTo(tup.getFirst());
		else
			throw new UnsupportedOperationException("Key type must be comparable");
	}

	public void setFirst(T bestValue)
	{
		this.first = bestValue;
	}

	public void setSecond(S second)
	{
		this.second = second;
	}
}

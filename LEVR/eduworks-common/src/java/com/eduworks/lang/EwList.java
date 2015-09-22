package com.eduworks.lang;

import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;

/***
 * Helper methods attached on top of ArrayList.
 * 
 * @author Fritz
 * 
 * @param <E>
 *            List type
 */
public class EwList<E> extends ArrayList<E> implements Serializable
{
	private static final long serialVersionUID = 1L;

	public EwList()
	{
	}

	public EwList(int size)
	{
		super(size);
	}

	public EwList(E[] that)
	{
		this.addAll(that);
	}

	public EwList(Collection<? extends E> that)
	{
		if (that != null)
			this.addAll(that);
	}

	@SuppressWarnings("unchecked")
	public EwList(List<Object> that)
	{
		for (Object o : that)
			add((E) o);
	}

	public EwList(AbstractCollection<E> that)
	{
		addAll(that);
	}

	public EwList(JSONArray ja)
	{
		if (ja != null)
		for (int i = 0; i < ja.length(); i++)
			if (!ja.isNull(i))
			try
			{
				add((E) ja.get(i));
			}
			catch (JSONException e)
			{
				try
				{
					throw new RuntimeException("Types differ: " + ja.get(i).toString());
				}
				catch (JSONException e1)
				{
					e1.printStackTrace();
				}
			}
	}

	public EwList(Object object)
	{
		if (object instanceof AbstractCollection)
		{
			addAll((AbstractCollection<E>) object);
		}
		else if (object instanceof JSONArray)
		{
			JSONArray ja = (JSONArray) object;
			for (int i = 0; i < ja.length(); i++)
				if (!ja.isNull(i))
				try
				{
					add((E) ja.get(i));
				}
				catch (JSONException e)
				{
					try
					{
						throw new RuntimeException("Types differ: " + ja.get(i));
					}
					catch (JSONException e1)
					{
						e1.printStackTrace();
					}
				}
		}
		else
			add((E) object);
	}

	/***
	 * Returns true IFF this list contains an element in c, uses
	 * {@link #contains(Object)}
	 * 
	 * @param c
	 * @return
	 */
	public boolean containsAny(Collection<?> c)
	{
		Iterator<?> e = c.iterator();
		while (e.hasNext())
			if (contains(e.next()))
				return true;
		return false;
	}

	/***
	 * Returns true IFF this list contains an element in c, uses
	 * {@link #contains(Object, Comparator)}
	 * 
	 * @param c
	 * @param comparator
	 * @return
	 */
	public boolean containsAny(Collection<?> c, Comparator<E> comparator)
	{
		Iterator<?> e = c.iterator();
		while (e.hasNext())
			if (contains((E) e.next(), comparator))
				return true;
		return false;
	}

	/***
	 * Returns true IFF this list contains an element in c, uses
	 * {@link #contains(Object)}
	 * 
	 * @param c
	 * @return
	 */
	public boolean containsAny(E[] c)
	{
		for (int i = 0; i < c.length; i++)
			if (contains(c[i]))
				return true;
		return false;
	}

	/***
	 * Returns true IFF this list contains an element who's toString() is
	 * String.equals(str)
	 * 
	 * @param word
	 * @return
	 */
	public boolean containsByToString(String str)
	{
		for (E e : this)
			if (e.toString().equals(str))
				return true;
		return false;
	}

	public String join(String delimiter)
	{
		StringBuffer buffer = new StringBuffer();
		Iterator<E> iter = iterator();
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

	public int indexOfByObject(E e)
	{
		int i = 0;
		for (E e2 : this)
		{
			if (e == e2)
				return i;
			i++;
		}
		return -1;
	}

	@Override
	public boolean equals(Object o)
	{
		try
		{
			return super.equals(o);
		}
		catch (StackOverflowError e)
		{
			e.printStackTrace();
			return false;
		}
	}

	public EwList<E> trimEmpty()
	{
		EwList<E> list = new EwList<E>();
		for (E e : this)
			if (e != null && e.toString() != "")
				list.add(e);
		return list;
	}

	public String combine(String delim)
	{
		StringBuilder sb = new StringBuilder();
		for (E e : this)
		{
			if (e != this.get(0))
				sb.append(delim);
			sb.append(e.toString());
		}
		return sb.toString();
	}

	public AbstractCollection<String> toStrings()
	{
		EwList<String> list = new EwList<String>();
		for (E e : this)
			if (e != null)
				list.add(e.toString());
		return list;
	}

	public String[] toStringArray()
	{
		String[] list = new String[size()];
		int i = 0;
		for (E e : this)
			list[i++] = e.toString();
		return list;
	}

	public EwList<String> toStringsEwList()
	{
		return (EwList<String>) toStrings();
	}

	public Set<String> toStringsSet()
	{
		Set<String> list = new HashSet<String>();
		for (E e : this)
			list.add(e.toString());
		return list;
	}

	public void addAll(AbstractCollection<E> objects)
	{
		for (E e : objects)
			add(e);
	}

	public void addAll(E[] that)
	{
		for (E e : that)
			add(e);
	}

	public void addAllNoDuplicate(AbstractCollection<E> objects)
	{
		for (E e : objects)
			if (!contains(e))
				add(e);
	}

	public void addAllNoDuplicate(List<E> objects)
	{
		for (E e : objects)
			if (!contains(e))
				add(e);
	}

	public void addAllNoDuplicate(AbstractCollection<E> objects, Comparator<E> comp)
	{
		for (E e : objects)
			if (!contains(e, comp))
				add(e);
	}

	public void addAllNoDuplicate(List<E> objects, Comparator<E> comp)
	{
		for (E e : objects)
			if (!contains(e, comp))
				add(e);
	}

	public boolean contains(E e, Comparator<E> comp)
	{
		for (E ee : this)
			if (comp.compare(e, ee) == 0)
				return true;
		return false;
	}

	public void addAllNoDuplicateByToString(EwList<E> clone)
	{
		for (E e : clone)
		{
			AbstractCollection<String> strings = this.toStrings();
			if (!strings.contains(e.toString()))
				add(e);
		}
	}

	public void addAllNoDuplicateByToString(E[] clone)
	{
		for (E e : clone)
		{
			AbstractCollection<String> strings = this.toStrings();
			if (!strings.contains(e.toString()))
				add(e);
		}
	}

	public void addAllNoDuplicateByObjectReference(EwList<E> clone)
	{
		for (E e : clone)
			if (!this.containsObjectReference(e))
				add(e);
	}

	public void addAllNoDuplicateByObjectReferenceFast(EwList<E> clone)
	{
		LinkedHashSet<E> hs = new LinkedHashSet<E>(this);
		for (E e : clone)
			if (!hs.contains(e))
			{
				hs.add(e);
				add(e);
			}
	}

	public boolean containsObjectReference(E e)
	{
		for (E e2 : this)
			if (e == e2)
				return true;
		return false;
	}

	public EwList<E> intersect(EwList<E> organizations, Comparator<E> comparator)
	{
		EwList<E> list = new EwList<E>();
		for (E e : this)
			if (organizations.contains(e, comparator))
				list.add(e);
		return list;
	}

	public EwList<E> intersect(AbstractCollection<?> organizations)
	{
		EwList<E> list = new EwList<E>();
		for (E e : this)
			if (organizations.contains(e))
				list.add(e);
		return list;
	}

	public EwList<E> intersect(JSONArray organizations) throws JSONException
	{
		EwList<E> list = new EwList<E>();
		for (E e : this)
			for (int i = 0; i < organizations.length(); i++)
				if (organizations.get(i).equals(e))
					list.add(e);
		return list;
	}

	public EwList<E> union(List<E> that)
	{
		EwList<E> list = new EwList<E>();
		list.addAll(this);
		if (that != null)
			list.addAllNoDuplicate(that);
		return list;
	}

	@SuppressWarnings("unchecked")
	public EwList<E> removeDuplicates()
	{
		List<E> clone = (List<E>) this.clone();
		this.clear();
		this.addAllNoDuplicate(clone);
		return this;
	}

	@SuppressWarnings("unchecked")
	public EwList<E> removeDuplicates(Comparator<E> comp)
	{
		List<E> clone = (List<E>) this.clone();
		this.clear();
		this.addAllNoDuplicate(clone, comp);
		return this;
	}

	@SuppressWarnings("unchecked")
	public EwList<E> removeDuplicatesCompareByToString()
	{
		EwList<E> clone = new EwList<E>((List<E>) this.clone());
		this.clear();
		this.addAllNoDuplicateByToString(clone);
		return this;
	}

	@SuppressWarnings("unchecked")
	public EwList<E> removeDuplicatesByObjectReference()
	{
		for (int i = 0;i < size();i++)
			for (int j = i;j < size();j++)
			{
				if (i == j) continue;
				if (get(i) == get(j))
				{
					remove(j);
					j = i;
				}
			}
		return this;
	}

	public void printAll()
	{
		for (E e : this)
			System.out.println(e);
	}

	public int indexOf(E e, int pos)
	{
		for (int i = pos; i < size(); i++)
			if (e.equals(get(i)))
				return i;
		return -1;
	}

	public EwList<E> removeWhiteSpace()
	{
		EwList<E> el = new EwList<E>();
		for (E e : this)
			if (!e.toString().trim().isEmpty())
				el.add(e);
		return el;
	}

	public EwList<E> removeEmpty()
	{
		EwList<E> el = new EwList<E>();
		for (E e : this)
			if (!e.toString().equals(""))
				el.add(e);
		return el;
	}

	public EwList<E> removeEmptyOnThis()
	{
		List<E> e = new EwList<E>();
		for (int i = 0; i < size(); i++)
			if (get(i).toString().equals(""))
				e.add(get(i));
		for (E x : e)
			remove(x);
		return this;
	}

	public List<E> toNormalList()
	{
		return new ArrayList<E>(this);
	}

	public boolean containsType(Class<? extends E> class1)
	{
		for (E e : this)
			if (class1.isInstance(e))
				return true;
		return false;
	}

	public E get(Class<? extends E> class1)
	{
		for (E e : this)
			if (class1.isInstance(e))
				return e;
		return null;
	}

	public static <E extends Comparable<? super E>> List<E> sort(List<E> list)
	{
		Collections.sort(list);
		return list;
	}

	public static <E> List<E> sort(List<E> list, Comparator<E> comparator)
	{
		Collections.sort(list, comparator);
		return list;
	}

	public E last()
	{
		if (size() == 0)
			return null;
		return get(size() - 1);
	}

	public E first()
	{
		if (size() == 0)
			return null;
		return get(0);
	}

	public EwList<E> first(int count)
	{
		EwList<E> results = new EwList<E>();
		for (int i = 0; i < count && i < size(); i++)
			results.add(get(i));
		return results;
	}

	public boolean containsIgnoreCase(E e)
	{
		for (E e2 : this)
			if (e.toString().equalsIgnoreCase(e2.toString()))
				return true;
		return false;
	}

	public int indexOfIgnoreCase(E e)
	{
		int i = 0;
		for (E e2 : this)
		{
			if (e.toString().equalsIgnoreCase(e2.toString()))
				return i;
			i++;
		}
		return -1;
	}

	public boolean containsIgnoreCaseByToString(String e)
	{
		for (E e2 : this)
			if (e.toString().equalsIgnoreCase(e2.toString()))
				return true;
		return false;
	}

	public boolean containsAnyIgnoreCase(AbstractCollection<String> organizationFragments)
	{
		for (E e2 : this)
			for (String e : organizationFragments)
				if (e.toString().equalsIgnoreCase(e2.toString()))
					return true;
		return false;
	}

	public EwList<E> reverse()
	{
		Collections.reverse(this);
		return this;
	}

	public static void ensureSize(ArrayList<?> list, int size) {
	    // Prevent excessive copying while we're adding
	    list.ensureCapacity(size);
	    while (list.size() < size) {
	        list.add(null);
	    }
	}

}

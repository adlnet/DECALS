package com.eduworks.lang;

import java.util.HashMap;
import java.util.Map;

/***
 * Helper methods appended to a HashMap.
 * 
 * @author Fritz
 * 
 * @param <E>
 *            Key type
 * @param <T>
 *            Value type
 */
public class EwHashMap<E, T> extends HashMap<E, T>
{
	private static final long serialVersionUID = 1L;

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();

		sb.append("{\n");
		for (Map.Entry<E, T> entry : entrySet())
			sb.append("\t" + entry.getKey().toString() + " -> " + entry.getValue().toString() + "\n");
		sb.append("}");
		return sb.toString();
	}
}

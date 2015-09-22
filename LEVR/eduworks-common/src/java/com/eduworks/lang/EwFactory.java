package com.eduworks.lang;

/***
 * Simple factory class.
 * 
 * @author Fritz
 * 
 * @param <W>
 *            Type to create.
 */
public abstract class EwFactory<W>
{
	/***
	 * Create a new object and return it.
	 * 
	 * @param string
	 *            The name of the object.
	 * @return The new object.
	 */
	public abstract W get(Object name);
}

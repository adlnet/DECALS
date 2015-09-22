package com.eduworks.resolver.enumeration;

/**
 * An API for Resolver enumerations that define a set of keys or values for an operation.
 * @author dharvey
 * @since 11/2011
 */
@Deprecated
public interface ResolverEnum
{
	/** Specifies a key for a specific value (or default key if the enumeration represents values) */
	public String getKey();

	/** Specifies the value for a specific key (or default value if the enumeration represents keys) */
	public String getValue();
}

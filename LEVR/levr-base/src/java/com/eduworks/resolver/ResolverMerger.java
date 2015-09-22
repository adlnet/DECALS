package com.eduworks.resolver;

import java.util.Map;

import org.json.JSONException;

/**
 * An api for Resolvers that process JSON merges with common options (key values).
 * @author dharvey
 * @since 11/2011
 */
@Deprecated
public interface ResolverMerger
{
	public Object merge(Object mergeTo, String srcKey, String destKey, Map<String, String[]> parameters) throws JSONException;

}

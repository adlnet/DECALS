/*
Copyright (c) 2007, Ilya Sterin
All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted provided that the following conditions are met:

 * Redistributions of source code must retain the above copyright notice, this
   list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice,
   this list of conditions and the following disclaimer in the documentation
   and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package com.fourspaces.couchdb.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.eduworks.lang.util.EwJson;

/**
 * Couch specific JSON utility. For more general JSON utility see {@link EwJson}
 * @author Ilya Sterin
 * @version 1.0
 */
public class JSONUtils
{
	public static final String COUCH_ID = "_id";
	public static final String COUCH_REV = "_rev";

	private JSONUtils() {}

	public static boolean isCouchSetting(String key)
	{
		return COUCH_ID.equals(key) || COUCH_REV.equals(key);
	}

	public static String stringSerializedFunction(final String func)
	{
		return (new StringBuilder(func.length()+2))
			.append('"')
			.append(func)
			.append('"').toString();
	}

	public static String urlEncodePath(String path) throws UnsupportedEncodingException
	{
		return URLEncoder.encode(path, "utf-8").replaceAll("%2F", "/").replace("%13", "%E2%80%93");
	}

}

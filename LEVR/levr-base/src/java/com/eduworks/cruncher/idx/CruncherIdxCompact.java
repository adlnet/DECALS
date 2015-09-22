package com.eduworks.cruncher.idx;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;
import com.eduworks.resolver.Resolver;
import com.eduworks.util.io.EwDB;

public class CruncherIdxCompact extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		String _databasePath = Resolver.decodeValue(getAsString("indexDir", c, parameters, dataStreams));
		String _databaseName = Resolver.decodeValue(getAsString("databaseName", c, parameters, dataStreams));

		EwDB ewDB = null;
		try
		{
			ewDB  = EwDB.get(_databasePath, _databaseName);
			ewDB.compact();
		}
		finally
		{
			ewDB.db.commit();
			if (ewDB != null)
				ewDB.close();
		}
		return null;
	}

	@Override
	public String getDescription()
	{
		return "Compacts an IDX Database.";
	}

	@Override
	public String getReturn()
	{
		return "null";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("indexDir","String","databaseName","String");
	}

}

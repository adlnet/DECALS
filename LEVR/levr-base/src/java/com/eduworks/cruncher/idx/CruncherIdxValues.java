package com.eduworks.cruncher.idx;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mapdb.HTreeMap;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;
import com.eduworks.resolver.Resolver;
import com.eduworks.util.io.EwDB;

public class CruncherIdxValues extends Cruncher
{

	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		String _databasePath = Resolver.decodeValue(getAsString("indexDir", c, parameters, dataStreams));
		String _databaseName = Resolver.decodeValue(getAsString("databaseName", c, parameters, dataStreams));
		String index = Resolver.decodeValue(getAsString("index", c, parameters, dataStreams));
		Integer mod = Integer.parseInt(optAsString("mod","-1", c, parameters, dataStreams));
		boolean optCommit = optAsBoolean("_commit", true, c, parameters, dataStreams);
		EwDB ewDB = null;
		long l = 0;
		try
		{
			ewDB = EwDB.get(_databasePath, _databaseName);
			JSONArray ja = new JSONArray();
			if (optCommit)
				ewDB.db.commit();
			HTreeMap<Object, Object> hashMap = ewDB.db.getHashMap(index);
			for (Object s : hashMap.keySet())
				try
				{
					if (mod != -1)
						if (l++ % mod != 0)
							continue;
					ja.put(hashMap.get(s));
				}
				catch (InternalError e)
				{

				}
			return ja;
		}
		finally
		{
			if (ewDB != null)
				ewDB.close();
		}
	}

	@Override
	public String getDescription()
	{
		return "Returns all values of a string only on-disk multimap defined by indexDir+databaseName->index->key += value.";
	}

	@Override
	public String getReturn()
	{
		return "JSONArray";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("indexDir", "LocalPathString", "databaseName", "String", "index", "String");
	}

}

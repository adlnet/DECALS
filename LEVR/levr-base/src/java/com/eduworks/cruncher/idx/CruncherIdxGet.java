package com.eduworks.cruncher.idx;

import java.io.InputStream;
import java.util.Map;
import java.util.NavigableSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mapdb.Fun;

import com.eduworks.lang.util.EwJson;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;
import com.eduworks.resolver.Resolver;
import com.eduworks.util.io.EwDB;

public class CruncherIdxGet extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		String _databasePath = Resolver.decodeValue(getAsString("indexDir", c, parameters, dataStreams));
		String _databaseName = Resolver.decodeValue(getAsString("databaseName", c, parameters, dataStreams));
		boolean optCommit = optAsBoolean("_commit", true, c, parameters, dataStreams);
		String index = Resolver.decodeValue(getAsString("index", c, parameters, dataStreams));
		String key = getAsString("key", c, parameters, dataStreams);
		if (key == null)
		{
			Object obj = getObj(c, parameters, dataStreams);
			if (obj != null)
				key = obj.toString();
		}
		
		EwDB ewDB = null;
		try
		{
			ewDB = EwDB.get(_databasePath, _databaseName);

			if (optCommit)
				ewDB.db.commit();
			if (optAsString("multi", "false", c, parameters, dataStreams).equals("false"))
			{
				Object object = ewDB.db.getHashMap(index).get(key);
				return object;
			}
			else
			{
				NavigableSet<Fun.Tuple2<String, Object>> multiMap = ewDB.db.getTreeSet(index);
				JSONArray ja = new JSONArray();
				for (Object l : Fun.filter(multiMap, key))
				{
					ja.put(EwJson.tryParseJson(l,false));
				}
				if (ja.length() > 0)
					return ja;
				return null;
			}
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
		return "Gets from a string only on-disk multimap defined by indexDir+databaseName->index->key += value.\n" +
				"If #idxAdd was used, multi must be set to true, and will return array.";
	}

	@Override
	public String getReturn()
	{
		return "String|JSONArray";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("indexDir","LocalPathString","databaseName","String","index","String","key","String","multi","Boolean");
	}

}

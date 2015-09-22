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

public class CruncherIdxDelete extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		String _databasePath = Resolver.decodeValue(getAsString("indexDir", c, parameters, dataStreams));
		String _databaseName = Resolver.decodeValue(getAsString("databaseName", c, parameters, dataStreams));
		boolean optCommit = optAsBoolean("_commit", true, c, parameters, dataStreams);
		String index = Resolver.decodeValue(getAsString("index", c, parameters, dataStreams));
		boolean deleteIndex = optAsBoolean("removeIndex", false, c, parameters, dataStreams);
		String key = getAsString("key", c, parameters, dataStreams);
		Object value = get("value", c, parameters, dataStreams);
		EwDB ewDB = null;
		try
		{
			if (optCommit)
				ewDB = EwDB.get(_databasePath, _databaseName);
			else
				ewDB = EwDB.getNoTransaction(_databasePath, _databaseName);

			ewDB.compact = true;
			if (deleteIndex) {
				ewDB.db.delete(index);
				return null;
			} else {
				if (optAsString("multi", "false", c, parameters, dataStreams).equals("false")){
					return ewDB.db.getHashMap(index).remove(key);
				}else{
					NavigableSet<Fun.Tuple2<String, Object>> multiMap = ewDB.db.getTreeSet(index);
					
					if(multiMap.remove(Fun.t2(key, value.toString())))
						ewDB.writeCount.decrementAndGet();
					
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
		}
		finally
		{
//			final EwDB ewDBX = ewDB;
			if (optCommit && ewDB.handles.get() < 5)
				ewDB.db.commit();
			if (ewDB != null)
				ewDB.close();
		}
	}

	@Override
	public String getDescription()
	{
		// TODO Auto-generated method stub
		return "Removed from a string only on-disk multimap defined by indexDir+databaseName->index->key += value. Returns value (as String).";
	}

	@Override
	public String getReturn()
	{
		return "Object";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("indexDir","LocalPathString","databaseName","String","index","String","key","String");
	}
}

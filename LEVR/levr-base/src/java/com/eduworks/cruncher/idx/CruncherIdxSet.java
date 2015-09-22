package com.eduworks.cruncher.idx;

import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;
import org.mapdb.DB.HTreeMapMaker;
import org.mapdb.HTreeMap;

import com.eduworks.lang.EwRandom;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;
import com.eduworks.resolver.Resolver;
import com.eduworks.util.io.EwDB;

public class CruncherIdxSet extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		String _databasePath = Resolver.decodeValue(getAsString("indexDir", c, parameters, dataStreams));
		String _databaseName = Resolver.decodeValue(getAsString("databaseName", c, parameters, dataStreams));
		String index = Resolver.decodeValue(getAsString("index", c, parameters, dataStreams));
		boolean optCommit = optAsBoolean("_commit", true, c, parameters, dataStreams);
		String key = getAsString("key", c, parameters, dataStreams);
		int ttlInSecondsAccess = optAsInteger("ttlAccessSeconds", -1, c, parameters, dataStreams);
		int ttlInSecondsModify = optAsInteger("ttlModifySeconds", -1, c, parameters, dataStreams);
		Object value = get("value", c, parameters, dataStreams);
		if (value == null)
			value = getObj(c, parameters, dataStreams);
		EwDB ewDB = null;
		try
		{
			if (optCommit)
				ewDB = EwDB.get(_databasePath, _databaseName);
			else
				ewDB = EwDB.getNoTransaction(_databasePath, _databaseName);

			HTreeMapMaker maker = ewDB.db.createHashMap(index);
			
			if (ttlInSecondsModify!=-1)
				 maker.expireAfterWrite(ttlInSecondsModify, TimeUnit.SECONDS);
			
			if (ttlInSecondsAccess!=-1)
				 maker.expireAfterAccess(ttlInSecondsAccess, TimeUnit.SECONDS);
			
			HTreeMap<Object, Object> hashMap = maker.makeOrGet();
			ewDB.writeCount.incrementAndGet();
			if (value == null)
				hashMap.remove(key);
			else
				hashMap.put(key, value.toString());
			
		}
		finally
		{
			if (optCommit && ewDB.handles.get() < 5)
				ewDB.db.commit();
			if (ewDB != null)
				ewDB.close();
		}
		return value;
	}

	@Override
	public String getDescription()
	{
		return "Moves obj (as a string) into a string only on-disk multimap defined by indexDir+databaseName->index->key = value\nWill convert into a string if not already.";
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
		return jo("obj","Object","indexDir","LocalPathString","databaseName","String","index","String","key","String");
	}

}

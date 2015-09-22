package com.eduworks.lang.util;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import com.eduworks.lang.json.impl.EwJsonArray;
import com.eduworks.lang.json.impl.EwJsonObject;

public class EwJsonTest
{
	final static String json = "[4,5,6.0,7.1,[\"test\"],{\"id\":\"testfriends\",\"name\":\"testfriends\",\"array\": [10,11,{\"val1\": \"v1\",\"val2\": \"v2\",\"arr2\": [\"A\",\"B\",\"C\"]},[\"X\",\"Y\",\"Z\"]],\"bool\":true},true,false]";

	@Test
	public void testEwJsonCollections() throws JSONException
	{
		/* TEST CONSTRUCTORS */

		HashMap<String, String> testMap = new HashMap<String, String>();
		testMap.put("a", "bc");
		testMap.put("null", null);
		testMap.put("x", "yz");
		EwJsonObject map = new EwJsonObject(testMap);

		if (!map.contains("bc") || map.contains(null) || !map.contains("yz"))
			throw new JSONException("EwJsonObject.contains(Object) failed");

		ArrayList<String> testList = new ArrayList<String>();
		testList.add("123");
		testList.add("456");
		testList.add("789");
		EwJsonArray list = new EwJsonArray(testList);

		if (!list.contains("123") || !list.contains("789"))
			throw new JSONException("EwJsonArray.contains(Object) failed");

		JSONObject jo = new JSONObject(testMap);
		JSONArray ja = new JSONArray(testList);

		System.out.println(jo.toString());
		System.out.println(map.toString());

		System.out.println(ja.toString());
		System.out.println(list.toString());

		System.out.println(new EwJsonArray(jo).toString());
		System.out.println(new EwJsonArray(ja).toString());

		System.out.println(new EwJsonObject(jo).toString());
		System.out.println(new EwJsonObject(ja).toString());

		System.out.println(new EwJsonObject(map).toString());
		System.out.println(new EwJsonObject(list).toString());

		System.out.println(new EwJsonArray(map).toString());
		System.out.println(new EwJsonArray(list).toString());

		EwJsonArray arr = (EwJsonArray) EwJson.tryParseJson(json, true, true);

		System.out.println(arr.toString());
		System.out.println(new EwJsonArray(json).toString());

		EwJsonObject bean = new EwJsonObject(
				new Object()
				{
					private final String id = "beanid";
					private final String name = "beanname";

					public String getId()
					{
						return id;
					}

					public String getName()
					{
						return name;
					}

					@Override
					public String toString()
					{
						return getId() + ":" + getName();
					}
				}
		);
		System.out.println(bean.toString());


		/* TEST OBJECT METHODS*/

		HashMap<String, String> beanMap = new HashMap<String, String>();
		beanMap.put("id", "beanid");
		beanMap.put("name", "beanname");

		String beanString = "{\"id\":\"beanid\",\"name\":\"beanname\"}";

		EwJsonObject beanFromMap = new EwJsonObject(beanMap);
		if (bean.equals(beanFromMap))
			System.out.println(beanFromMap.toString());
		else throw new JSONException("Bean <> BeanMap");

		EwJsonObject beanFromString = new EwJsonObject(beanString);
		if (bean.equals(beanFromString))
			System.out.println(beanFromString.toString());
		else throw new JSONException("Bean <> BeanString");


		/* TEST JSON METHODS */

		System.out.println(map.accumulate("accm",new EwJsonObject(map)));
		System.out.println(map.accumulate("accl",new EwJsonArray(list)));
		System.out.println(list.accumulate(4,new EwJsonArray(list)));
		System.out.println(list.accumulate(4,new EwJsonObject(map)));

		System.out.println(map.join("|"));
		System.out.println(list.join("|"));

		System.out.println(arr.toString());

		Integer i = (Integer) arr.get(0);
		System.out.println(i.toString());
		System.out.println(arr.get(1).toString());
		System.out.println(arr.get("[0]").toString());

		Double f = (Double) arr.get(2);
		System.out.println(f.toString());
		System.out.println(arr.optLong("2"));
		System.out.println(arr.optLong("[2]"));

		Double d = (Double) arr.get(3);
		System.out.println(d.toString());
		System.out.println(arr.optDouble("3"));
		System.out.println(arr.optDouble("[3]"));

		System.out.println(arr.getBoolean(new Integer(6)));
		System.out.println(arr.getBoolean(new Integer(7)));

		EwJsonArray iarr = (EwJsonArray) arr.get(4);
		System.out.println(iarr.get(0).toString());

		if (arr.hasComplex("4"))
			System.out.println(arr.get("4").toString());
		else throw new JSONException("Missing complex index '4'");

		if (arr.hasSimple("4"))
			System.out.println(arr.get("[4]").toString());
		else throw new JSONException("Missing simple index '4'");

		System.out.println(arr.getJSONArray("[4]").toString());
		System.out.println(arr.optJSONArray("[4]").toString());

		if (arr.hasComplex("[4][0]"))
			System.out.println(arr.get("[4][0]").toString());
		else throw new JSONException("Missing complex index '[4][0]'");

		if (arr.hasComplex("[5].array[2].arr2[1]"))
			System.out.println(arr.get("[5].array[2][arr2][1]").toString());
		else throw new JSONException("Missing complex index '[5].array[2].arr2[1]'");

		EwJsonObject obj = (EwJsonObject) arr.get(5);
		System.out.println(obj.toString());
		System.out.println(obj.get("array").toString());
		System.out.println(obj.get("array[2]").toString());

		if (obj.hasComplex("array[2].arr2[1]"))
			System.out.println(obj.get("array[2].arr2[2]").toString());
		else throw new JSONException("Missing complex key 'array[2].arr2[1]'");

		if (obj.hasComplex("bool"))
			System.out.println(obj.getBoolean("bool"));
		else throw new JSONException("Missing complex key 'bool'");

		if (obj.hasSimple("name"))
			System.out.println(obj.getString("name"));
		else throw new JSONException("Missing simple key 'name'");

	}
}

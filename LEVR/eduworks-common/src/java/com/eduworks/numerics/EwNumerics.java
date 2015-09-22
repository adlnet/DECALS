package com.eduworks.numerics;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.lang.EwList;
import com.eduworks.util.Tuple;

public class EwNumerics
{
	public static double[] normalize(double[] list) {
		double max = 0.0;
		for (double ws : list)
			max = Math.max(max,ws);
		double[] results = new double[list.length];
		for (int i = 0;i < list.length;i++)
		{
			results[i] = list[i]/max;
		}
		return results;
	}

	public static double rootMeanSquared(List<Double> value)
	{
		Double result = 0.0;
		for (Double d : value)
			result += d*d;
		return Math.sqrt(result/value.size());
	}
	public static double sum(List<Double> value)
	{
		Double result = 0.0;
		for (Double d : value)
			result += d;
		return result;
	}
	public static double rootMeanSquared(JSONArray value) throws JSONException
	{
		Double result = 0.0;
		for (int i = 0;i < value.length();i++)
			result += value.getDouble(i)*value.getDouble(i);
		return Math.sqrt(result/value.length());
	}
	public static double average(List<Double> value)
	{
		Double result = 0.0;
		for (Double d : value)
			result += d;
		return result/value.size();
	}
	public static double average(JSONArray value) throws JSONException
	{
		Double result = 0.0;
		for (int i = 0;i < value.length();i++)
			result += value.getDouble(i);
		return result/value.length();
	}

	public static Double max(JSONArray objAsJsonArray) throws JSONException
	{
		Double result = -Double.MAX_VALUE;
		for (int i = 0;i < objAsJsonArray.length();i++)
			result = Math.max(result, objAsJsonArray.getDouble(i));
		return result;
	}

	public static Double min(JSONArray objAsJsonArray) throws JSONException
	{
		Double result = Double.MAX_VALUE;
		for (int i = 0;i < objAsJsonArray.length();i++)
			result = Math.min(result, objAsJsonArray.getDouble(i));
		return result;
	}

	public static double[] calculateCentroid(Collection<double[]> values)
	{
		double[] centroid = new double[values.iterator().next().length];
		
		for (double[] value : values)
		{
			for (int i = 0;i < value.length;i++)
				centroid[i] += value[i];
		}
		for (int i = 0;i < centroid.length;i++)
			centroid[i] /= values.size();
		return centroid;
	}

	public static double distance(double[] centroid, double[] vertex)
	{
		double result = 0;
		for (int i = 0;i < centroid.length;i++)
		{
			double sub = vertex[i]-centroid[i];
			result += sub*sub;
		}
		return Math.sqrt(result);
	}

	public static double vectorDistance(JSONObject o1, JSONObject o2)
	{
		String key = "";
		double result = 0;
		for (java.util.Iterator<String> it = o1.keys();it.hasNext();key = it.next())
		{
			if (!o2.has(key)) continue;
			double d1 = o1.optDouble(key);
			if (Double.isNaN(d1)) continue;
			double d2 = o2.optDouble(key);
			if (Double.isNaN(d2)) continue;
			result += (d2-d1)*(d2-d1);
		}
		return Math.sqrt(result);
	}
	public static double vectorDistanceAmplified(JSONObject o1, JSONObject o2)
	{
		String key = "";
		double result = 0;
		double count = 0;
		for (java.util.Iterator<String> it = o1.keys();it.hasNext();key = it.next())
		{
			if (!o2.has(key)) continue;
			double d1 = o1.optDouble(key);
			if (Double.isNaN(d1)) continue;
			double d2 = o2.optDouble(key);
			if (Double.isNaN(d2)) continue;
			result += (d2-d1)*(d2-d1);
			count++;
		}
		return Math.sqrt(result)/(count*count);
	}

	public static Double max(Collection<Double> values)
	{
		Double d = Double.MIN_VALUE;
		for (Double dd : values)
			d = Math.max(d,dd);
		return d;
	}

	public static Tuple<Double, Double> maxGap(Collection<Double> values)
	{
		EwList<Double> intermediate = new EwList<Double>(values);
		EwList.sort(intermediate);
		Map<Double,Tuple<Double,Double>> gapRegistry = new HashMap<Double,Tuple<Double,Double>>();
		Double maxGap = new Double(Integer.MIN_VALUE);
		Double oldD = null;
		for (Double d : intermediate)
		{
			if (oldD != null)
			{
				Double gap = d-oldD;
				maxGap = Math.max(maxGap,gap);
				gapRegistry.put(gap,new Tuple<Double,Double>(oldD,d));
			}
			oldD = d;
		}
		return gapRegistry.get(maxGap);
	}
	public static Tuple<Double, Double> bestBisect(Collection<Double> values)
	{
		EwList<Double> intermediate = new EwList<Double>(values);
		EwList.sort(intermediate);
		Double sumsqMin = Double.MAX_VALUE;
		int iMin = 0;
		for (int i = 0;i < values.size();i++)
		{
			List<Double> l1 = intermediate.subList(0,i);
			List<Double> l2 = intermediate.subList(i, intermediate.size());
			if (l1.size() == 0) continue;
			if (l2.size() == 0) continue;

			Double avg1 = 0.0;
			for (Double d : l1) avg1 += d;
			avg1 /= l1.size();
			
			Double avg2 = 0.0;
			for (Double d : l2) avg2 += d;
			avg2 /= l2.size();
			
			Double sumsq1 = 0.0;
			for (Double d : l1) sumsq1 += (avg1-d)*(avg1-d);
			Double sumsq2 = 0.0;
			for (Double d : l2) sumsq2 += (avg2-d)*(avg2-d);
			if (sumsqMin > sumsq1*2+sumsq2)
			{	
				sumsqMin = sumsq1*2+sumsq2;
				iMin=i;
			}
		}

		List<Double> l1 = intermediate.subList(0,iMin);
		List<Double> l2 = intermediate.subList(iMin, intermediate.size());
		return new Tuple<Double,Double>(l1.get(l1.size()-1),l2.get(0));
	}

}

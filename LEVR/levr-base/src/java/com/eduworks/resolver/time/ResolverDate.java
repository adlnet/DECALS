package com.eduworks.resolver.time;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Resolver;

public class ResolverDate extends Resolver
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		resolveAllChildren(c, parameters, dataStreams);
		Calendar d = Calendar.getInstance();
		String dateFormat = getAsString("dateFormat",parameters);
		if (opt("input") != null)
			try
			{
				if (dateFormat == null || dateFormat.isEmpty())
					d.setTimeInMillis(DateTime.parse(getAsString("input", parameters)).getMillis());
				else
					d.setTimeInMillis(new SimpleDateFormat(dateFormat).parse(getAsString("input", parameters)).getTime());
			}
			catch (ParseException e)
			{
			}
		else if (opt("inputMillis") != null)
			d.setTimeInMillis(Long.parseLong(getAsString("inputMillis", parameters)));
		
		d.add(Calendar.SECOND, optAsInteger("addSeconds",0,parameters));
		d.add(Calendar.MINUTE, optAsInteger("addMinutes",0,parameters));
		d.add(Calendar.HOUR, optAsInteger("addHours",0,parameters));
		d.add(Calendar.DAY_OF_YEAR, optAsInteger("addDays",0,parameters));
		d.add(Calendar.MONTH, optAsInteger("addMonths",0,parameters));
		d.add(Calendar.YEAR, optAsInteger("addYears",0,parameters));
		
		if (optAsBoolean("raw", false, parameters))
			return Long.toString(d.getTimeInMillis());
		else if (optAsBoolean("dayOfWeek", false, parameters))
			return d.get(Calendar.DAY_OF_WEEK);
		else if (optAsBoolean("timeOfDay", false, parameters))
			return Integer.parseInt(String.valueOf(((d.get(Calendar.HOUR_OF_DAY)==0)?12:d.get(Calendar.HOUR_OF_DAY)) + String.valueOf(d.get(Calendar.MINUTE))));
		return new SimpleDateFormat(dateFormat).format(d.getTime());
	}

	@Override
	public String getDescription()
	{
		return "Retreives a date, possibly offset by some amount." +
				"\n(Optional) input - The date to use instead of Now." +
				"\n(Optional) dateFormat - The Java style date format to format the date by." +
				"\n(Optional) raw - Return the date as a long" +
				"\n(Optional) dayOfWeek - Return the day of the week the date falls on" +
				"\n(Optional) timeOfDay - Return the hour, minute, and second of the day" +
				"\n(Optional) addSeconds - The number of seconds to add to the date" +
				"\n(Optional) addMinutes - The number of minutes to add to the date" +
				"\n(Optional) addHours - The number of hours to add to the date" +
				"\n(Optional) addDays - The number of days to add to the date" +
				"\n(Optional) addMonths - The number of months to add to the date" +
				"\n(Optional) addYears - The number of years to add to the date";
	}

	@Override
	public String getReturn()
	{
		return "Number|String";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("?input","String","?dateFormat","String","?raw","Boolean","?dayOfWeek","Boolean","?timeOfDay","Boolean","?addSeconds","Number","?addMinutes","Number","?addHours","Number","?addDays","Number","?addMonths","Number","?addYears","Number");
	}

}

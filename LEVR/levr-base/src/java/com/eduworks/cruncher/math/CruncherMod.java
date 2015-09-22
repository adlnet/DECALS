package com.eduworks.cruncher.math;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherMod extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		Object obj = getObj(c,parameters, dataStreams);
		
		Double d = null;
		if (obj instanceof Double)
			d = (Double) obj;
		else if (obj instanceof Integer)
			d = ((Integer)obj).doubleValue();
		if (has("operator"))
		{
			Double operator = Double.parseDouble(getAsString("operator",c,parameters, dataStreams));
			return operator%d;
		}
		else if (has("operand"))
		{
			Double operand = Double.parseDouble(getAsString("operand",c,parameters, dataStreams));
			return d%operand;
		}
		else
			throw new JSONException("Could not find operator/operand");
	}

	@Override
	public String getDescription()
	{
		return "Performs Modulo operation on two numbers. If operator is defined, performs modulo operation on operator using obj as divisor. If operand is defined, performs modulo operation on obj using operand as divisor.";
	}

	@Override
	public String getReturn()
	{
		return "Number";
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("obj","Number","operator","Number","operand","Number");
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}

}

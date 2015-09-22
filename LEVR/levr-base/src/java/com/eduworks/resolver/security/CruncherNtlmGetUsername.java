package com.eduworks.resolver.security;

import java.io.IOException;
import java.io.InputStream;
import org.apache.xerces.impl.dv.util.Base64;
import java.util.Map;

import jcifs.ntlmssp.Type1Message;
import jcifs.ntlmssp.Type3Message;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;

public class CruncherNtlmGetUsername extends Cruncher
{
	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		String token = getObj(c, parameters, dataStreams).toString();

		byte[] src = Base64.decode(token);
		try
		{
			String msg = new String(src, "ASCII");
			if (msg != null && msg.startsWith("NTLM"))
			{
				if (src[8] == 1)
				{
					Type1Message type1 = new Type1Message(src);
					// Type2Message type2 = new Type2Message(type1, challenge,
					// null);
				}
				else if (src[8] == 3)
				{
					Type3Message type3 = new Type3Message(src);
					return type3.getUser();
				}
			}
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getDescription()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getReturn()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAttribution()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		// TODO Auto-generated method stub
		return null;
	}

}

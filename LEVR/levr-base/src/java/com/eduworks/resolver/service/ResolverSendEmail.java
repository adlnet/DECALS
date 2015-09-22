package com.eduworks.resolver.service;

import java.io.InputStream;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.lang.json.impl.EwJsonObject;
import com.eduworks.net.mail.EwMail;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.string.ResolverString;

public class ResolverSendEmail extends ResolverString
{

	/**
	 * Sends an email using the parameters passed in. Expected parameters:
	 * <list> <li><b>_to</b>: destination email</li> <li><b>_from</b>: source
	 * email</li> <li><b>_subject</b>: email subject</li> <li><b>_template</b>:
	 * email body</li> </list> <br/>
	 * <br/>
	 * The template value searches for "${from}" Email sending has no return
	 * value, so this method just returns the body of the email sent.
	 *
	 * @return the body of the email sent
	 */
	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		resolveAllChildren(c, parameters, dataStreams);

		/*
		 * TODO: This loop is a work-around -- there may be an underlying issue:
		 *
		 * 	The method Resolver.get(key, params) is called in ResolverString.format()
		 * 	Even when parameters contains key, null is returned because !this.has(key)
		 * 	Previously, this resolver considered both json and url values, so for now
		 * 	I am inserting the url parameter key/values into the current object.
		 */

		for (String key : parameters.keySet())
			if (!has(key) && has(parameters, key))
				put(key, parameters.get(key)[0]);

		final String fromEmail = getAsString("_from", parameters);
		final String toEmail = getAsString("_to", parameters);
		final String subject = format(getAsString("_subject", parameters), parameters);
		final String template = format(getAsString("_template", parameters), parameters);
		final String smtpHost = getAsString("_smtpHost",parameters);
		final String smtpPort = getAsString("_smtpPort",parameters);
		final String smtpUser = getAsString("_smtpUser",parameters);
		final String smtpPass = getAsString("_smtpPass",parameters);

		if (optAsBoolean("html", false, parameters))
			try
			{
				EwMail.sendHtmlEmail(smtpHost,smtpPort,smtpUser,smtpPass,fromEmail, toEmail, subject, template);
			}
			catch (AddressException e)
			{
				throw new RuntimeException(e);
			}
			catch (MessagingException e)
			{
				throw new RuntimeException(e);
			}
		else
			try
			{
				EwMail.sendEmail(smtpHost,smtpPort,smtpUser,smtpPass,fromEmail, toEmail, subject, template);
			}
			catch (AddressException e)
			{
				throw new RuntimeException(e);
			}
			catch (MessagingException e)
			{
				throw new RuntimeException(e);
			}

		final EwJsonObject obj = new EwJsonObject();

		obj.put("from", fromEmail);
		obj.put("to", toEmail);
		obj.put("subject", subject);
		obj.put("body", template);

		return obj;
	}

	@Override
	public String getDescription()
	{
		return "Sends an email (TODO: This)";
	}

	@Override
	public String getReturn()
	{
		return "JSONObject";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo();
	}
}

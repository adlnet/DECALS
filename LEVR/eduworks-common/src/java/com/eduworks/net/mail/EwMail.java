package com.eduworks.net.mail;

//# Configuration file for javax.mail
//# If a value for an item is not provided, then
//# system defaults will be used. These items can
//# also be set in code.
//
//# Host whose mail services will be used
//# (Default value : localhost)
//mail.host=mail.blah.com
//
//# Return address to appear on emails
//# (Default value : username@host)
//mail.from=webmaster@blah.net
//
//# Other possible items include:
//# mail.user=
//# mail.store.protocol=
//# mail.transport.protocol=
//# mail.smtp.host=
//# mail.smtp.user=
//# mail.debug=
//
//A class which uses this file to send an email :

import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


/**
 * Simple demonstration of using the javax.mail API.
 *
 */
public final class EwMail
{

	private static class Authenticator extends javax.mail.Authenticator
	{
		private final PasswordAuthentication authentication;

		public Authenticator(String smtpUser, String smtpPass)
		{
			authentication = new PasswordAuthentication(smtpUser, smtpPass);
		}

		@Override
		protected PasswordAuthentication getPasswordAuthentication()
		{
			return authentication;
		}
	}

	private static Session getSession(String smtpHost,String smtpPort,String smtpUser,String smtpPass)
	{
		Authenticator authenticator = new Authenticator(smtpUser,smtpPass);

		Properties properties = new Properties();
		properties.setProperty("mail.smtp.submitter", authenticator.getPasswordAuthentication().getUserName());
		properties.setProperty("mail.smtp.auth", Boolean.TRUE.toString());

		properties.setProperty("mail.smtp.host", smtpHost);
		properties.setProperty("mail.smtp.port", smtpPort);
		properties.setProperty("mail.smtp.user", smtpUser);
		properties.setProperty("mail.smtp.timeout", "20000");
		properties.setProperty("mail.smtp.starttls.enable", Boolean.TRUE.toString());

		return Session.getInstance(properties, authenticator);
	}

	/**
	 * Send a single email.
	 * @throws MessagingException
	 * @throws AddressException
	 */
	public static void sendEmail(String smtpHost,String smtpPort,String smtpUser,String smtpPass,String aFromEmailAddr, String aToEmailAddr, String aSubject, String aBody) throws AddressException, MessagingException
	{
		// Here, no Authenticator argument is used (it is null).
		// Authenticators are used to prompt the user for user
		// name and password.
		Session session = getSession(smtpHost,smtpPort,smtpUser,smtpPass);
		MimeMessage message = new MimeMessage(session);

		message.setFrom(new InternetAddress(aFromEmailAddr));
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(aToEmailAddr));
		message.setSubject(aSubject);
		message.setText(aBody);

		Transport.send(message);

	}

	/**
	 * Send a single email.
	 * @throws MessagingException
	 * @throws AddressException
	 */
	public static void sendHtmlEmail(String smtpHost,String smtpPort,String smtpUser,String smtpPass,String aFromEmailAddr, String aToEmailAddr, String aSubject, String aBody) throws AddressException, MessagingException
	{
		// Here, no Authenticator argument is used (it is null).
		// Authenticators are used to prompt the user for user
		// name and password.
		Session session = getSession(smtpHost,smtpPort,smtpUser,smtpPass);
		MimeMessage message = new MimeMessage(session);

		message.setFrom(new InternetAddress(aFromEmailAddr));
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(aToEmailAddr));
		message.setSubject(aSubject);
		MimeMultipart mp = new MimeMultipart("related");
		BodyPart bp = new MimeBodyPart();
		bp.setContent(aBody,"text/html");
		mp.addBodyPart(bp);

		message.setContent(mp);
		Transport.send(message);

	}
}
package com.eduworks.cruncher.xmpp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.XHTMLManager;
import org.jivesoftware.smackx.XHTMLText;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.lang.threading.EwThreading;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;
import com.eduworks.util.io.InMemoryFile;

public class CruncherXmppSend extends Cruncher
{

	@Override
	public Object resolve(Context c, Map<String, String[]> parameters, Map<String, InputStream> dataStreams) throws JSONException
	{
		String server = getAsString("serverHostname", c, parameters, dataStreams);
		String loginHostname = getAsString("loginHostname", c, parameters, dataStreams);
		String port = getAsString("port", c, parameters, dataStreams);
		String username = getAsString("username", c, parameters, dataStreams);
		String password = getAsString("password", c, parameters, dataStreams);
		String recipient = getAsString("recipient", c, parameters, dataStreams);
		String message = getObj(c, parameters, dataStreams).toString();
		String alt = optAsString("alt","",c, parameters, dataStreams).toString();

		XMPPConnection connection = XmppManager.get(server, port, loginHostname, username, password);
		try
		{
			Chat chat = XmppManager.getChat(connection, recipient);
			Message chatMessage = new Message(recipient,Message.Type.chat);
			chatMessage.setBody(message);
			if (alt != null && alt.isEmpty() == false)
				chatMessage.setProperty("alt", alt);
			chat.sendMessage(chatMessage);
			long msDelay = message.split(" ").length * 100+1000;
			log.debug(recipient + " <-- " + message);
			Object file = get("file", c, parameters, dataStreams);
			if (file != null)
			{
				InMemoryFile imf = null;
				if (file instanceof InMemoryFile)
					imf = (InMemoryFile) file;
				else if (file instanceof File)
					imf = new InMemoryFile((File) file);
				if (imf != null)
				{
					FileTransferManager manager = XmppManager.getFtm(server, port, loginHostname, username, password);
					OutgoingFileTransfer transfer = manager.createOutgoingFileTransfer(recipient);
					transfer.sendStream(imf.getInputStream(), imf.name, imf.data.length,
							optAsString("description", "", c, parameters, dataStreams));
					while (transfer.isDone() == false)
						EwThreading.sleep(100);
					if (transfer.getException().getMessage().equals("service-unavailable(503)"))
					{
						String altUrl = optAsString("fileUrl", "", c, parameters, dataStreams);
						if (altUrl != null && altUrl.isEmpty() == false && XHTMLManager.isServiceEnabled(connection, chat.getParticipant()))
						{
							Message msg = new Message();

							msg.setType(Message.Type.chat);
							XHTMLText xhtmlText = new XHTMLText(null, null);
							xhtmlText.append("<img src=\""+altUrl+"\">");
	
							XHTMLManager.addBody(msg, xhtmlText.toString());
							chat.sendMessage(msg);
						}
						else
							chat.sendMessage(
								optAsString("fileFailed", "File send failed.", c, parameters, dataStreams));
					}
				}
			}
			//EwThreading.sleep(Math.max(250, msDelay));
			if (optAsBoolean("fast",false,c,parameters, dataStreams))
				EwThreading.sleep(100);
			else
				EwThreading.sleep(Math.max(250, msDelay));
				
		}
		catch (XMPPException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getDescription()
	{
		return "Sends a message to a client using XMPP.";
	}

	@Override
	public String getReturn()
	{
		return "null";
	}

	@Override
	public String getAttribution()
	{
		return ATTRIB_NONE;
	}

	@Override
	public JSONObject getParameters() throws JSONException
	{
		return jo("serverHostname", "String", "username", "String", "password", "String");
	}

}

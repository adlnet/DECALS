package com.eduworks.cruncher.xmpp;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.levr.servlet.impl.LevrResolverServlet;
import com.eduworks.resolver.Context;
import com.eduworks.resolver.Cruncher;
import com.eduworks.resolver.Resolvable;

public class CruncherXmppListen extends Cruncher
{

	private final class MessageListenerImplementation implements MessageListener
	{
		protected Resolvable op;
		private final Map<String, String[]> parameters;
		private final Map<String, InputStream> dataStreams;

		private MessageListenerImplementation(Resolvable op, Map<String, String[]> parameters, Map<String, InputStream> dataStreams)
		{
			this.op = op;
			this.parameters = parameters;
			this.dataStreams = dataStreams;
		}

		@Override
		public void processMessage(Chat chat2, Message arg1)
		{
			Context c = new Context();
			try
			{
				if (arg1.getBody() == null) 
					return;
				Map<String, String[]> newParameters = new HashMap<String,String[]>(parameters);
				newParameters.put("message", new String[]{arg1.getBody()});
				newParameters.put("sender", new String[]{chat2.getParticipant().split("/")[0]});
				log.debug(chat2.getParticipant() + " --> " + arg1.getBody());
				LevrResolverServlet.initConfig(System.out);
				((Resolvable)op.clone()).resolve(c, newParameters, dataStreams);
				c.success();
			}
			catch (Throwable e)
			{
				c.failure();
				if (!(e instanceof RuntimeException))
					e.printStackTrace();
				else if (e.getMessage() != null && !e.getMessage().isEmpty())
					System.out.println(e.getMessage());
			}
			finally
			{
				c.finish();
			}
		}
	}

	@Override
	public Object resolve(Context c, final Map<String, String[]> parameters, final Map<String, InputStream> dataStreams) throws JSONException
	{
		String server = getAsString("serverHostname", c, parameters, dataStreams);
		String loginHostname = getAsString("loginHostname", c, parameters, dataStreams);
		String port = getAsString("port", c, parameters, dataStreams);
		String username = getAsString("username", c, parameters, dataStreams);
		String password = getAsString("password", c, parameters, dataStreams);
		XMPPConnection connection = XmppManager.get(server,port,loginHostname, username, password);
		
		final Resolvable op = (Resolvable) get("messageReceived");
		log.debug("Unregistering chat listeners.");
		for (ChatManagerListener cml : connection.getChatManager().getChatListeners())
			connection.getChatManager().removeChatListener(cml);
		for (Chat ch : XmppManager.chats.values())
		{
			for (MessageListener l : ch.getListeners())
			{
				((MessageListenerImplementation)l).op = op;
			}
		}
		log.debug("Registering new chat listener.");
		connection.getChatManager().addChatListener(new ChatManagerListener()
		{
			@Override
			public void chatCreated(Chat chat, boolean createdLocally)
			{
				chat.addMessageListener(getMessageListener(parameters, dataStreams, op));
			}
		});
		return null;
	}

	public MessageListener getMessageListener(final Map<String, String[]> parameters,
			final Map<String, InputStream> dataStreams, final Resolvable op)
	{
		return new MessageListenerImplementation(op, parameters, dataStreams);
	}
	@Override
	public String getDescription()
	{
		return "Begins listening for conversations on a XMPP client port.";
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
		return jo("serverHostname","String","username","String","password","String");
	}

}

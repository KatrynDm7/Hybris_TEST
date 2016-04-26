package de.hybris.platform.cuppy.web.components;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;


public class CuppyChatController
{
	private final static Logger LOG = Logger.getLogger(CuppyChatController.class);
	private final LinkedList<ChatMessage> messages = new LinkedList<ChatMessage>(); //NOPMD
	private boolean consoleLogEnabled = false;
	private int maxMessageSize = 100;

	public static class ChatMessage
	{
		private final String userID;
		private final String message;
		private final Date creationTime;

		public ChatMessage(final String userID, final String message)
		{
			this.userID = userID;
			this.message = message;
			this.creationTime = new Date();
		}

		public String getUserID()
		{
			return userID;
		}

		public String getMessage()
		{
			return message;
		}

		public Date getCreationTime()
		{
			return creationTime;
		}

		@Override
		public String toString()
		{
			return userID + "(" + creationTime + "): " + message;
		}
	}


	public void addMessage(final ChatMessage message)
	{
		synchronized (this)
		{
			if (isConsoleLogEnabled())
			{
				LOG.info("New chatline: " + message);
			}
			messages.addFirst(message);
			if (messages.size() > getMaxMessageSize())
			{
				messages.removeLast();
			}
		}
	}


	public List<ChatMessage> getMessagesSince(final Date after)
	{
		final LinkedList<ChatMessage> ret = new LinkedList<ChatMessage>();
		if (after != null)
		{
			synchronized (this)
			{
				for (final ChatMessage msg : messages)
				{
					if (msg.getCreationTime().compareTo(after) > 0)
					{
						ret.addFirst(msg);
					}
				}
			}
		}

		if (consoleLogEnabled)
		{
			LOG.info("Message request, returning " + ret.size() + " messages.");
		}

		return ret;
	}


	public void setConsoleLogEnabled(final boolean consoleLogEnabled)
	{
		this.consoleLogEnabled = consoleLogEnabled;
	}


	public boolean isConsoleLogEnabled()
	{
		return consoleLogEnabled;
	}


	public void setMaxMessageSize(final int maxMessageSize)
	{
		this.maxMessageSize = maxMessageSize;
	}


	public int getMaxMessageSize()
	{
		return maxMessageSize;
	}
}

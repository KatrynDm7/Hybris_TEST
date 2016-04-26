package de.hybris.platform.cuppy.web.components;

import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cuppy.web.components.CuppyChatController.ChatMessage;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;


public class CuppyChatClientController
{
	private CuppyChatController chatController;
	private final LinkedList<ChatMessage> messages = new LinkedList<ChatMessage>(); //NOPMD
	private Date firstFetch = null;


	public void addMessage(final String message)
	{
		if (StringUtils.isNotBlank(message))
		{
			chatController.addMessage(new ChatMessage(UISessionUtils.getCurrentSession().getUser().getUid(), message));
		}
	}

	public List<ChatMessage> fetchNewMessages()
	{
		final List<ChatMessage> newMessages = chatController.getMessagesSince(getLastLocalMessageDate());
		messages.addAll(newMessages);
		if (messages.size() > chatController.getMaxMessageSize())
		{
			messages.removeFirst();
		}
		return newMessages;
	}

	private Date getLastLocalMessageDate()
	{
		return messages.isEmpty() ? (firstFetch == null ? firstFetch = new Date() : firstFetch) : messages.getLast()
				.getCreationTime();
	}

	public int getNewMessageCount()
	{
		return chatController.getMessagesSince(getLastLocalMessageDate()).size();
	}

	public List<ChatMessage> getAllMessages()
	{
		return Collections.unmodifiableList(messages);
	}

	@Required
	public void setChatController(final CuppyChatController chatController)
	{
		this.chatController = chatController;
	}
}

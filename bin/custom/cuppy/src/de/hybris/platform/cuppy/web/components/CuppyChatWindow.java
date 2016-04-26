package de.hybris.platform.cuppy.web.components;

import de.hybris.platform.cuppy.web.components.CuppyChatController.ChatMessage;
import de.hybris.platform.cuppy.web.facades.PlayerFacade;

import java.util.List;

import org.zkoss.spring.SpringUtil;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Timer;
import org.zkoss.zul.Window;


public class CuppyChatWindow extends Window
{
	private final Div outputBox;
	private CuppyChatClientController chatClientController;
	private final StatusBarComponent statusbar;
	private final Div chatBtn;

	public CuppyChatWindow(final int updateInterval, final StatusBarComponent frontendController, final Div chatBtn)
	{
		super();
		this.statusbar = frontendController;
		this.chatBtn = chatBtn;

		this.setSclass("cuppyChatWindow");
		this.setSizable(true);
		this.setClosable(true);


		outputBox = new Div();
		outputBox.setSclass("chatOutput");
		this.appendChild(outputBox);

		final Textbox inputBox = new Textbox();
		inputBox.setSclass("chatInput");
		this.appendChild(inputBox);

		inputBox.addEventListener(Events.ON_OK, new EventListener()
		{
			@Override
			public void onEvent(final Event event) throws Exception //NOPMD
			{
				getChatClientController().addMessage(inputBox.getText());
				updateOutput();
				inputBox.setText("");
				inputBox.focus();
			}
		});

		final Timer timer = new Timer(updateInterval);
		timer.setRunning(true);
		timer.setRepeats(true);
		this.appendChild(timer);
		timer.addEventListener(Events.ON_TIMER, new EventListener()
		{
			@Override
			public void onEvent(final Event event) throws Exception //NOPMD
			{
				updateOutput();
			}
		});

		this.addEventListener(Events.ON_CREATE, new EventListener()
		{
			@Override
			public void onEvent(final Event event) throws Exception //NOPMD
			{
				inputBox.focus();
			}
		});

		reloadOutput();
	}

	private void updateOutput()
	{
		final List<ChatMessage> newMessages = getChatClientController().fetchNewMessages();
		for (final ChatMessage msg : newMessages)
		{
			addToOutput(msg.getUserID(), msg.getMessage());
		}
		statusbar.refreshChatStatus(chatBtn);
	}

	private void reloadOutput()
	{
		outputBox.getChildren().clear();
		for (final ChatMessage msg : getChatClientController().getAllMessages())
		{
			addToOutput(msg.getUserID(), msg.getMessage());
		}
	}


	private void addToOutput(final String playerID, final String message)
	{
		final Div outputEntry = new Div();
		outputEntry.setSclass("chatOutputEntry");

		final Label label = new Label(getPlayerFacade().getProfile(playerID).getName() + ": ");
		label.setSclass(getPlayerFacade().getCurrentPlayer().getId().equals(playerID) ? "currentUserChat" : "otherUserChat");
		outputEntry.appendChild(label);
		outputEntry.appendChild(new Label(message));

		this.outputBox.appendChild(outputEntry);
		Clients.scrollIntoView(outputEntry);
	}


	private CuppyChatClientController getChatClientController()
	{
		if (chatClientController == null)
		{
			chatClientController = (CuppyChatClientController) SpringUtil.getBean("chatClientController");
		}
		return chatClientController;
	}

	private PlayerFacade getPlayerFacade()
	{
		return (PlayerFacade) SpringUtil.getBean("playerFacade");
	}
}

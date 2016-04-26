/**
 * 
 */
package de.hybris.platform.cuppy.web.components;

import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.cuppy.web.facades.PlayerFacade;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;


/**
 * @author andreas.thaler
 * 
 */
public class ForgetWindow extends Window
{
	public ForgetWindow() throws InterruptedException
	{
		super();

		this.setWidth("450px");
		this.setPosition("center");
		this.setMode("overlapped");
		this.setShadow(false);
		this.setBorder("none");
		this.setClosable(false);
		this.setSizable(false);

		final Groupbox box = new Groupbox();
		box.setMold("3d");
		box.setClosable(false);
		this.appendChild(box);

		box.appendChild(new Caption(Labels.getLabel("forget.title")));

		final Div formDiv = new Div();
		formDiv.setWidth("100%");
		box.appendChild(formDiv);

		final Grid grid = new Grid();
		formDiv.appendChild(grid);

		final Rows rows = new Rows();
		grid.appendChild(rows);

		final Row mailRow = new Row();
		mailRow.setSclass("registerRow");
		mailRow.appendChild(new Label(Labels.getLabel("forget.param.email")));
		final Textbox mailBox = new Textbox();
		mailBox.setConstraint("/.+@.+\\.[a-z]+/: " + Labels.getLabel("forget.error.noemail"));
		mailRow.appendChild(mailBox);
		rows.appendChild(mailRow);

		final Hbox hBox = new Hbox();
		hBox.setPack("center");
		hBox.setWidth("100%");
		final Button submitButton = new Button(Labels.getLabel("forget.reset"));
		hBox.appendChild(submitButton);
		final Button backButton = new Button(Labels.getLabel("forget.backtologin"));
		backButton.setHref("/");
		hBox.appendChild(backButton);
		box.appendChild(hBox);

		final EventListener listener = new EventListener()
		{
			@Override
			public void onEvent(final Event event) throws InterruptedException
			{
				doForget(mailBox);
			}
		};

		UITools.addBusyListener(submitButton, Events.ON_CLICK, listener, null, null);
		UITools.addBusyListener(this, Events.ON_OK, listener, null, null);
	}

	private PlayerFacade getPlayerFacade()
	{
		return (PlayerFacade) SpringUtil.getBean("playerFacade");
	}

	private void doForget(final Textbox mailBox) throws InterruptedException
	{
		mailBox.addEventListener("onMessageLater", new EventListener()
		{
			@Override
			public void onEvent(final Event event) throws Exception //NOPMD
			{
				if ("success".equals(event.getData()))
				{
					Messagebox.show(Labels.getLabel("forget.success"));
					Executions.sendRedirect("/");
				}
				else if ("unknownMail".equals(event.getData()))
				{
					Messagebox.show(Labels.getLabel("forget.error.unknownmail", new Object[]
					{ mailBox.getValue() }));
				}
				else
				{
					Messagebox.show((String) event.getData());
				}
			}
		});

		try
		{
			getPlayerFacade().forgotPassword(mailBox.getValue());
			Events.echoEvent("onMessageLater", mailBox, "success");
		}
		catch (final UnknownIdentifierException e)
		{
			Events.echoEvent("onMessageLater", mailBox, "unknownMail");
		}
		catch (final Exception e)
		{
			Events.echoEvent("onMessageLater", mailBox, e.getMessage());
		}
	}
}

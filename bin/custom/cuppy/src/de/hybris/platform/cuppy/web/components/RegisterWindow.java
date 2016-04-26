/**
 * 
 */
package de.hybris.platform.cuppy.web.components;

import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.cuppy.web.data.PlayerProfileData;
import de.hybris.platform.cuppy.web.facades.PlayerFacade;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;

import java.util.Locale;

import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.SimpleConstraint;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;


/**
 * @author andreas.thaler
 * 
 */
public class RegisterWindow extends Window
{
	public RegisterWindow() throws InterruptedException
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

		box.appendChild(new Caption(Labels.getLabel("register.title")));

		final Div formDiv = new Div();
		formDiv.setWidth("100%");
		box.appendChild(formDiv);

		final Grid grid = new Grid();
		formDiv.appendChild(grid);

		final Rows rows = new Rows();
		grid.appendChild(rows);

		final Row idRow = new Row();
		idRow.setSclass("registerRow");
		idRow.appendChild(new Label(Labels.getLabel("register.param.id")));
		final Textbox idBox = new Textbox();
		idBox.setWidth("200px");
		idBox.setConstraint(new SimpleConstraint(SimpleConstraint.NO_EMPTY, Labels.getLabel("register.error.noid")));
		idRow.appendChild(idBox);
		rows.appendChild(idRow);

		final Row nameRow = new Row();
		nameRow.setSclass("registerRow");
		nameRow.appendChild(new Label(Labels.getLabel("register.param.name")));
		final Textbox nameBox = new Textbox();
		nameBox.setWidth("200px");
		nameBox.setConstraint(new SimpleConstraint(SimpleConstraint.NO_EMPTY, Labels.getLabel("register.error.noname")));
		nameRow.appendChild(nameBox);
		rows.appendChild(nameRow);

		final Row mailRow = new Row();
		mailRow.setSclass("registerRow");
		mailRow.appendChild(new Label(Labels.getLabel("register.param.email")));
		final Textbox mailBox = new Textbox();
		mailBox.setConstraint("/.+@.+\\.[a-z]+/: " + Labels.getLabel("register.error.noemail"));
		mailRow.appendChild(mailBox);
		rows.appendChild(mailRow);

		final Row pwdRow = new Row();
		pwdRow.setSclass("registerRow");
		pwdRow.appendChild(new Label(Labels.getLabel("register.param.password")));
		final Textbox pwdBox = new Textbox();
		pwdBox.setConstraint(new SimpleConstraint(SimpleConstraint.NO_EMPTY, Labels.getLabel("register.error.nopassword")));
		pwdBox.setType("password");
		pwdRow.appendChild(pwdBox);
		rows.appendChild(pwdRow);

		final Row pwd2Row = new Row();
		pwd2Row.setSclass("registerRow");
		pwd2Row.appendChild(new Label(Labels.getLabel("register.param.password2")));
		final Textbox pwd2Box = new Textbox();
		pwd2Box.setConstraint(new Constraint()
		{
			@Override
			public void validate(final Component comp, final Object value) throws WrongValueException
			{
				if (!(pwdBox.getValue().equals(value)))
				{
					throw new WrongValueException(comp, Labels.getLabel("register.error.unequalpassword"));
				}
			}
		});
		pwd2Box.setType("password");
		pwd2Row.appendChild(pwd2Box);
		rows.appendChild(pwd2Row);

		final Row countryRow = new Row();
		countryRow.setSclass("registerRow");
		countryRow.appendChild(new Label(Labels.getLabel("register.param.country")));
		final Combobox countryBox = new Combobox();
		countryBox.setConstraint(new SimpleConstraint(SimpleConstraint.NO_EMPTY, Labels.getLabel("register.error.nocountry")));
		fillCombo(countryBox);
		countryBox.setAutodrop(true);
		countryBox.setReadonly(true);
		countryRow.appendChild(countryBox);
		rows.appendChild(countryRow);

		final Hbox hBox = new Hbox();
		hBox.setPack("center");
		hBox.setWidth("100%");
		final Button submitButton = new Button(Labels.getLabel("register.register"));
		hBox.appendChild(submitButton);
		final Button backButton = new Button(Labels.getLabel("register.backtologin"));
		backButton.setHref("/");
		hBox.appendChild(backButton);
		box.appendChild(hBox);

		final EventListener listener = new EventListener()
		{
			@Override
			public void onEvent(final Event event) throws InterruptedException
			{
				doRegister(idBox, nameBox, mailBox, pwdBox, pwd2Box, countryBox);
			}
		};

		UITools.addBusyListener(submitButton, Events.ON_CLICK, listener, null, null);
		UITools.addBusyListener(this, Events.ON_OK, listener, null, null);
	}

	private void fillCombo(final Combobox combo)
	{
		if (combo.getItemCount() == 0)
		{
			final Locale currentLocale = UISessionUtils.getCurrentSession().getLocale();
			for (final Locale locale : getPlayerFacade().getAllCountries())
			{
				final Comboitem item = new Comboitem(locale.getDisplayCountry(currentLocale));
				item.setValue(locale);
				combo.appendChild(item);
			}
		}
	}

	private PlayerFacade getPlayerFacade()
	{
		return (PlayerFacade) SpringUtil.getBean("playerFacade");
	}

	private void doRegister(final Textbox idBox, final Textbox nameBox, final Textbox mailBox, final Textbox pwdBox,
			final Textbox pwd2Box, final Combobox countryBox) throws InterruptedException
	{
		final PlayerProfileData registration = new PlayerProfileData();

		registration.setName(nameBox.getValue());
		registration.setEMail(mailBox.getValue());
		registration.setId(idBox.getValue());
		registration.setPassword(pwdBox.getValue());
		pwd2Box.getValue();
		countryBox.getValue(); //calls validation
		if (countryBox.getSelectedItem() == null)
		{
			// TODO:
			registration.setLocale(Locale.UK);
		}
		else
		{
			registration.setLocale((Locale) countryBox.getSelectedItem().getValue());
		}

		idBox.addEventListener("onMessageLater", new EventListener()
		{
			@Override
			public void onEvent(final Event event) throws Exception //NOPMD
			{
				if ("success".equals(event.getData()))
				{
					Messagebox.show(Labels.getLabel("register.success"));
					Executions.sendRedirect("/");
				}
				else if ("duplicateID".equals(event.getData()))
				{
					Messagebox.show(Labels.getLabel("register.error.duplicateplayer", new Object[]
					{ registration.getId() }));
				}
				else
				{
					Messagebox.show((String) event.getData());
				}
			}
		});

		try
		{
			getPlayerFacade().registerPlayer(registration);
			Events.echoEvent("onMessageLater", idBox, "success");
		}
		catch (final AmbiguousIdentifierException e)
		{
			Events.echoEvent("onMessageLater", idBox, "duplicateID");
		}
		catch (final Exception e)
		{
			Events.echoEvent("onMessageLater", idBox, e.getMessage());
		}
	}
}

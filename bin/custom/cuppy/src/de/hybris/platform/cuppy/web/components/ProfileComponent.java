package de.hybris.platform.cuppy.web.components;

import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cuppy.web.data.CompetitionData;
import de.hybris.platform.cuppy.web.data.PlayerProfileData;
import de.hybris.platform.cuppy.web.facades.PlayerFacade;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.zkoss.image.Image;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zhtml.Img;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Div;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.SimpleConstraint;
import org.zkoss.zul.Textbox;


public class ProfileComponent extends Div
{
	private static final Logger LOG = Logger.getLogger(ProfileComponent.class);

	public ProfileComponent()
	{
		super();
		final PlayerProfileData player = getPlayerFacade().getCurrentPlayer();

		final Grid profileGrid = new Grid();
		profileGrid.setSclass("profileGrid");

		final Rows profileRows = new Rows();
		profileGrid.appendChild(profileRows);

		final Row idRow = new Row();
		idRow.setSclass("profileRow");
		idRow.appendChild(new Label(Labels.getLabel("profile.id")));
		idRow.appendChild(new Label(player.getId()));
		profileRows.appendChild(idRow);

		final Row nameRow = new Row();
		nameRow.setSclass("profileRow");
		nameRow.appendChild(new Label(Labels.getLabel("profile.name")));
		final Textbox nameBox = new Textbox(player.getName());
		nameBox.setConstraint(new SimpleConstraint(SimpleConstraint.NO_EMPTY, Labels.getLabel("register.error.noname")));
		nameRow.appendChild(nameBox);
		profileRows.appendChild(nameRow);

		final Row mailRow = new Row();
		mailRow.setSclass("profileRow");
		mailRow.appendChild(new Label(Labels.getLabel("profile.email")));
		final Textbox mailBox = new Textbox(player.getEMail());
		mailBox.setConstraint("/.+@.+\\.[a-z]+/: " + Labels.getLabel("register.error.noemail"));
		mailRow.appendChild(mailBox);
		profileRows.appendChild(mailRow);

		final Row pwdRow = new Row();
		pwdRow.setSclass("profileRow");
		pwdRow.appendChild(new Label(Labels.getLabel("profile.password")));
		final Textbox pwdBox = new Textbox(player.getPassword());
		//pwdBox.setConstraint(new SimpleConstraint(SimpleConstraint.NO_EMPTY, Labels.getLabel("register.error.nopassword")));
		pwdBox.setType("password");
		pwdRow.appendChild(pwdBox);
		profileRows.appendChild(pwdRow);

		final Row pwd2Row = new Row();
		pwd2Row.setSclass("profileRow");
		pwd2Row.appendChild(new Label(Labels.getLabel("profile.passwordagain")));
		final Textbox pwd2Box = new Textbox(player.getPassword());
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
		profileRows.appendChild(pwd2Row);

		final Row countryRow = new Row();
		countryRow.setSclass("profileRow");
		countryRow.appendChild(new Label(Labels.getLabel("profile.country")));
		final Combobox countryBox = new Combobox();
		countryBox.setConstraint(new SimpleConstraint(SimpleConstraint.NO_EMPTY, Labels.getLabel("register.error.nocountry")));
		countryBox.setAutodrop(true);
		countryBox.setReadonly(true);
		fillCombo(countryBox, player.getLocale());
		countryRow.appendChild(countryBox);
		profileRows.appendChild(countryRow);


		final Row avCompetitionsRow = new Row();
		avCompetitionsRow.setSclass("competitionsRow");
		avCompetitionsRow.appendChild(new Label(Labels.getLabel("profile.availableCompetitions")));
		final Listbox listbox = new Listbox();
		listbox.setSclass("competitionsListbox");
		avCompetitionsRow.appendChild(listbox);
		listbox.setCheckmark(true);
		listbox.setMultiple(true);

		final List<CompetitionData> comps = getPlayerFacade().getAllCompetitions();
		for (final CompetitionData cmpData : comps)
		{
			final Listitem listItem = new Listitem(cmpData.getName());
			listItem.setValue(cmpData);
			listItem.setSelected(cmpData.isActive());
			listItem.setDisabled(!cmpData.isDeactivatable() || (cmpData.isCurrentCompetition() && comps.size() != 1));
			listbox.appendChild(listItem);
		}

		profileRows.appendChild(avCompetitionsRow);

		final Row buttonRow = new Row();
		buttonRow.setSclass("profileRow");
		final Button button = new Button(Labels.getLabel("profile.submit"));
		button.addEventListener(Events.ON_CLICK, new EventListener()
		{
			@Override
			public void onEvent(final Event event)
			{
				player.setEMail(mailBox.getValue());
				player.setName(nameBox.getValue());

				if (StringUtils.isNotBlank(pwdBox.getValue()) && pwdBox.getValue().equals(pwd2Box.getValue()))
				{
					player.setPassword(pwdBox.getValue());
				}
				player.setLocale((Locale) countryBox.getSelectedItem().getValue());

				final List<CompetitionData> cmps = new ArrayList<CompetitionData>();
				for (final Object listItem : listbox.getSelectedItems())
				{
					if (listItem instanceof Listitem)
					{
						final Object value = ((Listitem) listItem).getValue();
						if (value instanceof CompetitionData)
						{
							cmps.add((CompetitionData) value);
						}
					}
				}

				getPlayerFacade().setActiveCompetitions(cmps);

				getPlayerFacade().updatePlayer(player);

				try
				{
					Messagebox.show(Labels.getLabel("profile.update.success"));
					// TODO proper update mechanism
					Executions.sendRedirect("/");
				}
				catch (final InterruptedException e)
				{
					LOG.warn("Error while showing messagebox: ", e);
				}
			}
		});
		buttonRow.appendChild(button);
		profileRows.appendChild(buttonRow);

		this.appendChild(profileGrid);

		final Div detailsDiv = new Div();
		detailsDiv.setSclass("rankingUserDetails");

		final Img img = new Img();
		final Div imgCnt = new Div();
		imgCnt.appendChild(img);
		imgCnt.setSclass("rankingUserDetailsImg");
		img.setDynamicProperty("src", player.getPictureUrl());
		detailsDiv.appendChild(imgCnt);

		final Button uploadButton = new Button(Labels.getLabel("profile.upload"));
		uploadButton.setSclass("btngreen profileUserDetailsUpload");
		detailsDiv.appendChild(uploadButton);

		uploadButton.addEventListener(Events.ON_CLICK, new EventListener()
		{

			@Override
			public void onEvent(final Event event) throws InterruptedException
			{
				final Object media = Fileupload.get();
				if (media instanceof Image)
				{
					final Image image = (Image) media;
					player.setPictureUrl(getPlayerFacade().uploadProfilePicture(image.getByteData(), image.getName()));
					img.setDynamicProperty("src", player.getPictureUrl());
				}
				else if (media != null)
				{
					Messagebox.show("Not an image: " + media, "Error", Messagebox.OK, Messagebox.ERROR);
				}
			}
		});
		this.appendChild(detailsDiv);
	}

	private void fillCombo(final Combobox combo, final Locale curLocale)
	{
		if (combo.getItemCount() == 0)
		{
			final Locale currentLocale = UISessionUtils.getCurrentSession().getLocale();
			for (final Locale locale : getPlayerFacade().getAllCountries())
			{
				final Comboitem item = new Comboitem(locale.getDisplayCountry(currentLocale));
				item.setValue(locale);
				combo.appendChild(item);
				if (curLocale.getCountry().equals(locale.getCountry()))
				{
					combo.setSelectedItem(item);
				}
			}
		}
	}

	private PlayerFacade getPlayerFacade()
	{
		return (PlayerFacade) SpringUtil.getBean("playerFacade");
	}
}

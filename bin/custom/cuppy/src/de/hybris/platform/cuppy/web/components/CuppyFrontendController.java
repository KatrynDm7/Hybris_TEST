package de.hybris.platform.cuppy.web.components;

import de.hybris.platform.cockpit.session.UICockpitPerspective;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.cuppy.services.impl.NoCompetitionAvailableException;
import de.hybris.platform.cuppy.web.data.CompetitionData;
import de.hybris.platform.cuppy.web.facades.PlayerFacade;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zhtml.Div;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;


public class CuppyFrontendController
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(CuppyFrontendController.class);
	private static final String CUPPY_ADMIN_PERSPECTIVE = "CuppyAdminPerspective";
	private List<CuppyMainPage> mainPages;
	private PlayerFacade playerFacade;

	private CuppyMainPage lastOpenedMainPage = null;

	public void setMainPages(final List<CuppyMainPage> mainPages)
	{
		this.mainPages = mainPages;
	}

	public List<CuppyMainPage> getMainPages()
	{
		return mainPages == null ? Collections.EMPTY_LIST : mainPages;
	}

	public void initialize(final Component mainDiv, final Component toolbar)
	{
		if (!getMainPages().isEmpty())
		{
			showPage(lastOpenedMainPage == null ? getMainPages().iterator().next() : lastOpenedMainPage, mainDiv, toolbar);
		}
	}

	protected void showPage(final CuppyMainPage page, final Component mainDiv, final Component toolbar)
	{
		if (page.isOwnWindow())
		{
			final Window window = new Window();
			window.setSclass("betsWindow");
			window.setClosable(true);
			window.setTitle(Labels.getLabel(page.getLabel()));
			final Map<String, Object> args = page.getArgs();
			Executions.createComponents(page.getViewURI(), window, args);

			window.doHighlighted();
			window.addEventListener(Events.ON_CLOSE, new EventListener()
			{
				@Override
				public void onEvent(final Event event) throws Exception //NOPMD
				{
					window.detach();
				}
			});
			mainDiv.appendChild(window);
		}
		else
		{
			// clear page
			mainDiv.getChildren().clear();
			Executions.createComponents(page.getViewURI(), mainDiv, page.getArgs());
			createToolbar(mainDiv, toolbar, page);
			lastOpenedMainPage = page;
		}
	}


	protected void createToolbar(final Component mainDiv, final Component toolbar, final CuppyMainPage selectedPage)
	{
		toolbar.getChildren().clear();

		CompetitionData currentCompetition;
		try
		{
			currentCompetition = playerFacade.getCurrentCompetition();
		}
		catch (final NoCompetitionAvailableException e)
		{
			currentCompetition = null;
		}
		for (final CuppyMainPage page : getMainPages())
		{
			if (page.isOnlyTournament() && currentCompetition != null && !currentCompetition.isTournament())
			{
				continue;
			}
			final Div containerDiv = new Div();
			containerDiv.setSclass("toolbar_btn");

			final Toolbarbutton btn = new Toolbarbutton();
			btn.setLabel(Labels.getLabel(page.getLabel(), page.getLabel()));
			btn.addEventListener(Events.ON_CLICK, new EventListener()
			{
				@Override
				public void onEvent(final Event event) throws Exception // NOPMD
				{
					showPage(page, mainDiv, toolbar);
				}
			});

			containerDiv.appendChild(btn);
			toolbar.appendChild(containerDiv);

			if (page.equals(selectedPage))
			{
				UITools.modifySClass(btn, "tb_selected", true);
			}
		}

		// YTODO : ADD SELECT BOX
	}

	public void openAdminPerspective()
	{
		final UICockpitPerspective perspective = (UICockpitPerspective) SpringUtil.getBean(CUPPY_ADMIN_PERSPECTIVE);
		UISessionUtils.getCurrentSession().setCurrentPerspective(perspective);
	}

	public boolean isAdminVisible()
	{
		return playerFacade.isCurrentPlayerAdmin();
	}

	public void setPlayerFacade(final PlayerFacade playerFacade)
	{
		this.playerFacade = playerFacade;
	}

	public boolean isCompetitionsVisible()
	{
		return this.playerFacade.getActiveCompetitions().size() > 1;
	}

	public void changeCompetition(final String code)
	{
		if (!StringUtils.equals(playerFacade.getCurrentCompetition().getCode(), code))
		{
			playerFacade.setCurrentCompetition(code);

			// TODO proper update mechanism
			Executions.sendRedirect("/");
		}
	}
}

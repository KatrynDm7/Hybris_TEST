/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2011 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package de.hybris.platform.cuppy.web.cockpit.events;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.UICockpitPerspective;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.AbstractRequestEventHandler;
import de.hybris.platform.core.PK;
import de.hybris.platform.cuppy.model.CompetitionModel;
import de.hybris.platform.cuppy.web.components.CuppyFrontendController;

import java.util.Map;

import org.apache.log4j.Logger;
import org.zkoss.spring.SpringUtil;


/**
 * Handles events for setting a competition using jump-in URL.
 */
public class CompetitionSelectionEventHandler extends AbstractRequestEventHandler
{
	private final static Logger LOG = Logger.getLogger(CompetitionSelectionEventHandler.class);

	/**
	 * parameter for specifying the PK of the component to be selected
	 */
	public static final String COMPETITION_KEY = "item";

	@Override
	public void handleEvent(final UICockpitPerspective perspective, final Map<String, String[]> params)
	{
		final String itemParam = this.getParameter(params, COMPETITION_KEY);

		CompetitionModel competition = null;
		try
		{
			final TypedObject item = UISessionUtils.getCurrentSession().getTypeService().wrapItem(PK.parse(itemParam));
			if (item != null)
			{
				competition = (CompetitionModel) item.getObject();
			}
		}
		catch (final Exception e)
		{
			LOG.warn("competition specified in jumpin URL not forund: " + itemParam, e);
		}

		if (competition != null)
		{
			getFrontendController().changeCompetition(competition.getCode());
		}
	}

	private CuppyFrontendController getFrontendController()
	{
		return (CuppyFrontendController) SpringUtil.getBean("frontendController");
	}
}

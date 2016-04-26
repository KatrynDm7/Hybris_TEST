/*
 *
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2014 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 */
package de.hybris.platform.financialservices.jalo.components;

import de.hybris.platform.cms2.jalo.contents.components.SimpleCMSComponent;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;

import java.util.List;

import org.apache.log4j.Logger;


public class CMSMultiComparisonTabContainer extends GeneratedCMSMultiComparisonTabContainer
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(CMSMultiComparisonTabContainer.class.getName());

	@Override
	protected Item createItem(final SessionContext ctx, final ComposedType type, final ItemAttributeMap allAttributes)
			throws JaloBusinessException
	{
		// business code placed here will be executed before the item is created
		// then create the item
		final Item item = super.createItem(ctx, type, allAttributes);
		// business code placed here will be executed after the item was created
		// and return the item
		return item;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.cms2.jalo.contents.containers.GeneratedAbstractCMSComponentContainer#getCurrentCMSComponents
	 * (de.hybris.platform.jalo.SessionContext)
	 */
	@Override
	public List<SimpleCMSComponent> getCurrentCMSComponents(final SessionContext sessionContext)
	{
		return getSimpleCMSComponents(sessionContext);
	}
}

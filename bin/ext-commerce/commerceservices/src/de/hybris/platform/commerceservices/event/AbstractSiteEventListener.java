/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *  
 */
package de.hybris.platform.commerceservices.event;

import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;

public abstract class AbstractSiteEventListener<T extends AbstractEvent> extends AbstractEventListener<T>
{
	protected abstract void onSiteEvent(final T event);

	protected abstract boolean shouldHandleEvent(final T event);

	@Override
	protected void onEvent(final T event)
	{
		if (shouldHandleEvent(event))
		{
			onSiteEvent(event);
		}
	}
}

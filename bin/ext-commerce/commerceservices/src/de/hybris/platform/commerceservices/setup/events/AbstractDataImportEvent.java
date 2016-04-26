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
package de.hybris.platform.commerceservices.setup.events;

import de.hybris.platform.commerceservices.setup.data.ImportData;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.servicelayer.event.SynchronousEvent;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;

import java.util.List;


public class AbstractDataImportEvent extends AbstractEvent implements SynchronousEvent
{
	private final List<ImportData> importData;
	private final SystemSetupContext context;

	public AbstractDataImportEvent(final SystemSetupContext context, final List<ImportData> importData)
	{
		this.context = context;
		this.importData = importData;
	}

	public SystemSetupContext getContext()
	{
		return context;
	}

	public List<ImportData> getImportData()
	{
		return importData;
	}
}

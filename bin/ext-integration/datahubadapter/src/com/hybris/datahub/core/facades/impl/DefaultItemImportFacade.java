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
 */

package com.hybris.datahub.core.facades.impl;

import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.impex.ImpExResource;
import de.hybris.platform.servicelayer.impex.ImportConfig;
import de.hybris.platform.servicelayer.impex.ImportConfig.ValidationMode;
import de.hybris.platform.servicelayer.impex.ImportResult;
import de.hybris.platform.servicelayer.impex.ImportService;

import com.hybris.datahub.core.dto.ItemImportTaskData;
import com.hybris.datahub.core.event.DatahubAdapterImportEvent;
import com.hybris.datahub.core.facades.ItemImportFacade;
import com.hybris.datahub.core.facades.ItemImportResult;
import com.hybris.datahub.core.services.ImpExResourceFactory;
import com.hybris.datahub.core.services.impl.DataHubFacade;

import java.io.IOException;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default facade for the ImpEx import process.
 */
public class DefaultItemImportFacade implements ItemImportFacade
{
	private ImportService importService;
	private ImpExResourceFactory resourceFactory;
	private ImportResultConverter resultConverter;
	private DataHubFacade dataHubFacade;
	private EventService eventService;

	@Override
	public ItemImportResult importItems(final ItemImportTaskData ctx) throws IOException
	{
		final ItemImportResult result = runImportAndHandleErrors(ctx);
		addHeaderErrorsToResult(ctx, result);

		fireDatahubAdapterImportEvent(ctx.getPoolName(), result.getStatus());
		callbackToDataHub(ctx.getResultCallbackUrl(), result);

		return result;
	}

	private void addHeaderErrorsToResult(final ItemImportTaskData ctx, final ItemImportResult result)
	{
		if (ctx != null && !CollectionUtils.isEmpty(ctx.getHeaderErrors()))
		{
			result.addErrors(ctx.getHeaderErrors());
		}
	}

	private ItemImportResult runImportAndHandleErrors(final ItemImportTaskData ctx) throws IOException
	{
		try
		{
			return runImport(ctx);
		}
		catch (final ImpExException | RuntimeException e)
		{
			return new ItemImportResult(e);
		}
	}

	private ItemImportResult runImport(final ItemImportTaskData ctx) throws ImpExException
	{
		final ImpExResource resource = resourceFactory.createResource(ctx);
		final ImportConfig config = createImportConfig(resource);
		final ImportResult result = importService.importData(config);
		return resultConverter.convert(result);
	}

	private void fireDatahubAdapterImportEvent(final String poolName, final ItemImportResult.DatahubAdapterEventStatus status)
	{
		eventService.publishEvent(new DatahubAdapterImportEvent(poolName, status));
	}

	protected ImportConfig createImportConfig(final ImpExResource resource)
	{
		final ImportConfig cfg = new ImportConfig();
		cfg.setScript(resource);
		cfg.setValidationMode(ValidationMode.STRICT);
		cfg.setLegacyMode(false);
		return cfg;
	}

	private void callbackToDataHub(final String resultCallbackUrl, final ItemImportResult result)
	{
		dataHubFacade.returnImportResult(resultCallbackUrl, result);
	}

	/**
	 * Injects <code>ImportService</code> implementation to be used by this facade.
	 *
	 * @param impl implementation of the <code>ImportService</code> to use.
	 */
	@Required
	public void setImportService(final ImportService impl)
	{
		importService = impl;
	}

	/**
	 * Injects <code>ImpExResourceFactory</code> implementation to be used by this facade.
	 *
	 * @param impl an implementation of the <code>ImpExResourceFactory</code>.
	 */
	@Required
	public void setResourceFactory(final ImpExResourceFactory impl)
	{
		resourceFactory = impl;
	}

	/**
	 * Injects implementation of the <code>ImportResultConverter</code>
	 *
	 * @param converter a converter to use for changing import service result to this facade's result.
	 */
	@Required
	public void setResultConverter(final ImportResultConverter converter)
	{
		resultConverter = converter;
	}

	/**
	 * Injects implemenation of <code>DataHubFacade</code>
	 *
	 * @param dataHubFacade
	 */
	@Required
	public void setDataHubFacade(final DataHubFacade dataHubFacade)
	{
		this.dataHubFacade = dataHubFacade;
	}

	/**
	 * Injects event service used by the system.
	 *
	 * @param eventService event service implementation to use.
	 */
	@Required
	public final void setEventService(final EventService eventService)
	{
		this.eventService = eventService;
	}
}

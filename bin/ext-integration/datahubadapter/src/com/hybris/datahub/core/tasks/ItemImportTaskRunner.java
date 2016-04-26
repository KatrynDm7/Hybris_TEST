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

package com.hybris.datahub.core.tasks;

import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.TaskRunner;
import de.hybris.platform.task.TaskService;

import com.hybris.datahub.core.dto.ItemImportTaskData;
import com.hybris.datahub.core.facades.ItemImportFacade;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;


/**
 * A TaskRunner that will trigger the ImpexImport
 */
public class ItemImportTaskRunner implements TaskRunner<TaskModel>
{
	private ItemImportFacade importFacade;
	private SessionService sessionService;

	@Override
	public void run(final TaskService taskService, final TaskModel task) throws RetryLaterException
	{
		final ItemImportTaskData importTaskData = (ItemImportTaskData) task.getContext();

		initSession(importTaskData.getSessionAttrs());

		try
		{
			importFacade.importItems(importTaskData);
		}
		catch (final IOException e)
		{
			throw new RuntimeException("Could not close input stream", e);
		}

		sessionService.closeCurrentSession();
	}

	public void handleError(final TaskService taskService, final TaskModel task, final Throwable error)
	{
		logger().error("ItemImportTaskRunner.handleError()", error);
	}

	void initSession(final java.util.Map<java.lang.String, Object> sessionAttrs)
	{
		sessionService.setAttribute("user", sessionAttrs.get("user"));
		sessionService.setAttribute("language", sessionAttrs.get("language"));
	}

	private Logger logger()
	{
		return LoggerFactory.getLogger(getClass());
	}

	/**
	 * Injects import facade to be used by this task runner.
	 *
	 * @param facade import facade implementation to use.
	 */
	@Required
	public void setImportFacade(final ItemImportFacade facade)
	{
		importFacade = facade;
	}

	/**
	 * Injects session service used by the system.
	 *
	 * @param service a user session service implementation to be used by this task runner.
	 */
	@Required
	public void setSessionService(final SessionService service)
	{
		sessionService = service;
	}
}

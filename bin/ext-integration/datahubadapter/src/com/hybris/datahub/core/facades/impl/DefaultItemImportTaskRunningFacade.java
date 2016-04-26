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

import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.TaskService;

import com.hybris.datahub.core.dto.ItemImportTaskData;
import com.hybris.datahub.core.facades.ItemImportTaskRunningFacade;

import java.util.Date;

import org.springframework.beans.factory.annotation.Required;

import com.google.common.base.Preconditions;

/**
 * Facade for scheduling the ImpexImport via the TaskService
 */
public class DefaultItemImportTaskRunningFacade implements ItemImportTaskRunningFacade
{
	private ModelService modelService;
	private TaskService taskService;

	/**
	 * @param importTaskData the dto containing details that will be used by the ImpexImport
	 */
	@Override
	public void scheduleImportTask(final ItemImportTaskData importTaskData)
	{
		Preconditions.checkArgument(importTaskData != null, "importTaskData cannot be null");

		final TaskModel task = modelService.create(TaskModel.class);

		task.setRunnerBean("itemImportTaskRunner");
		task.setExecutionTimeMillis(new Date().getTime());
		task.setContext(importTaskData);

		taskService.scheduleTask(task);
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	@Required
	public void setTaskService(final TaskService taskService)
	{
		this.taskService = taskService;
	}
}

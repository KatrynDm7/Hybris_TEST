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
 */
package de.hybris.platform.xyformsservices.interceptors;

import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.xyformsservices.enums.YFormDataTypeEnum;
import de.hybris.platform.xyformsservices.model.YFormDataHistoryModel;
import de.hybris.platform.xyformsservices.model.YFormDataModel;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;


/**
 * PrepareInterceptor to create YFormDataHistory entry when YFormData gets saved
 */
public class YFormDataPrepareInterceptor implements PrepareInterceptor<YFormDataModel>
{
	private ModelService modelService;

	@Override
	public void onPrepare(final YFormDataModel yFormData, final InterceptorContext paramInterceptorContext)
			throws InterceptorException
	{
		// if trying to update a draft document, no history is created
		if (YFormDataTypeEnum.DRAFT.equals(yFormData.getType()))
		{
			return;
		}

		final YFormDataHistoryModel history = getModelService().create(YFormDataHistoryModel.class);
		history.setFormDataId(yFormData.getId());
		history.setContent(yFormData.getContent());
		getModelService().save(history);

		final List<YFormDataHistoryModel> items = new ArrayList<YFormDataHistoryModel>();

		final List<YFormDataHistoryModel> historyItems = yFormData.getHistory();

		if (historyItems != null)
		{
			items.addAll(historyItems);
		}

		items.add(history);

		yFormData.setHistory(items);
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}
}

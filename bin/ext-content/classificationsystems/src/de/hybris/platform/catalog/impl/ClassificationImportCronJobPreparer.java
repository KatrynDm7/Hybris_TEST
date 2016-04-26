/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 * 
 *  
 */
package de.hybris.platform.catalog.impl;

import de.hybris.platform.catalog.jalo.classification.ClassificationImportJob;
import de.hybris.platform.catalog.model.classification.ClassificationImportCronJobModel;
import de.hybris.platform.catalog.model.classification.ClassificationImportJobModel;
import de.hybris.platform.classificationsystems.jalo.ClassificationsystemsManager;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;


/**
 * PrepareInterceptor for the {@link ClassificationImportCronJobModel}. Sets the default classification import job for
 * this cronjob model.
 */
public class ClassificationImportCronJobPreparer implements PrepareInterceptor
{

	@Override
	public void onPrepare(final Object model, final InterceptorContext ctx) throws InterceptorException
	{
		if (model instanceof ClassificationImportCronJobModel && ctx.isNew(model))
		{
			final ClassificationImportCronJobModel modelImpl = (ClassificationImportCronJobModel) model;

			if (modelImpl.getJob() == null)
			{
				final ClassificationImportJob jobJalo = ClassificationsystemsManager.getInstance()
						.getOrCreateDefaultClassificationImportJob();

				if (jobJalo == null)
				{
					throw new InterceptorException("Can't get or create default classification import job");
				}

				final ClassificationImportJobModel jobModel = ctx.getModelService().toModelLayer(jobJalo);
				modelImpl.setJob(jobModel);
			}
		}
	}
}

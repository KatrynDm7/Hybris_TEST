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
package de.hybris.platform.timedaccesspromotionsservices.interceptors;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


public class PromotionCodeMatcherPrepareInterceptor implements PrepareInterceptor
{
	private static final Logger LOG = Logger.getLogger(PromotionCodeMatcherPrepareInterceptor.class.getName());
	private KeyGenerator keyGenerator;

	@Override
	public void onPrepare(final Object model, final InterceptorContext ctx) throws InterceptorException
	{
		if (model instanceof AbstractOrderEntryModel)
		{
			final AbstractOrderEntryModel abstractOrderEntryModel = (AbstractOrderEntryModel) model;
			if (abstractOrderEntryModel.getPromotionMatcher() == null)
			{
				abstractOrderEntryModel.setPromotionMatcher((String) keyGenerator.generate());
			}
		}
	}

	@Required
	public void setKeyGenerator(final KeyGenerator keyGenerator)
	{
		this.keyGenerator = keyGenerator;
	}

}

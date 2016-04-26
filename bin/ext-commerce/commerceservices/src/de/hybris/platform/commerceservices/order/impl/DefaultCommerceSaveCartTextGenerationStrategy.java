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
package de.hybris.platform.commerceservices.order.impl;

import de.hybris.platform.commerceservices.order.CommerceSaveCartTextGenerationStrategy;
import de.hybris.platform.core.model.order.CartModel;


/**
 * Default implementation of the {@link CommerceSaveCartTextGenerationStrategy}. It generates only simple texts for saved cart's
 * name and description and should be replaced by a more complex strategy in case more sophisticated texts are needed.
 */
public class DefaultCommerceSaveCartTextGenerationStrategy implements CommerceSaveCartTextGenerationStrategy
{

	@Override
	public String generateSaveCartName(final CartModel cartToBeSaved)
	{
		return cartToBeSaved.getCode();
	}

	@Override
	public String generateSaveCartDescription(final CartModel cartToBeSaved)
	{
		return "-";
	}

}

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
package de.hybris.platform.commerceservices.order;

import de.hybris.platform.core.model.order.CartModel;


/**
 * Exposes methods to generate texts (such as a name or description) for a cart to be saved. These methods are only
 * called if these texts are not provided.
 */
public interface CommerceSaveCartTextGenerationStrategy
{
	/**
	 * Generates a name for the cart to be saved
	 *
	 * @param cartToBeSaved
	 *           {@link CartModel}
	 * @return the generated name
	 */
	String generateSaveCartName(CartModel cartToBeSaved);

	/**
	 * Generates a description for the cart to be saved
	 *
	 * @param cartToBeSaved
	 *           {@link CartModel}
	 * @return the generated description
	 */
	String generateSaveCartDescription(CartModel cartToBeSaved);
}

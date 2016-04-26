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
package de.hybris.platform.b2ctelcoservices.order.impl;

import de.hybris.platform.commerceservices.order.CommerceCartMergingException;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.impl.AbstractCommerceCartStrategy;
import de.hybris.platform.commerceservices.order.impl.CommerceCartMergingStrategy;
import de.hybris.platform.core.model.order.CartModel;

import java.util.List;


/**
 * B2ctelcoCommerceCartMergingStrategy, an implementation of CommerceCartMergingStrategy that doesn't merge.
 */
public class B2ctelcoCommerceCartMergingStrategy extends AbstractCommerceCartStrategy implements CommerceCartMergingStrategy

{

	@Override
	public void mergeCarts(final CartModel cartmodel, final CartModel cartmodel1, final List<CommerceCartModification> list)
			throws CommerceCartMergingException
	{
		//Intentionally do nothing for b2ctelco

	}

}

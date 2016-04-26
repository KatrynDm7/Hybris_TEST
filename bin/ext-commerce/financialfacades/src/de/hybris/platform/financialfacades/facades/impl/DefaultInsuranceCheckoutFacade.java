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
package de.hybris.platform.financialfacades.facades.impl;

import de.hybris.platform.commercefacades.order.CheckoutFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.financialfacades.facades.InsuranceCheckoutFacade;
import de.hybris.platform.xyformsfacades.data.YFormDataData;
import de.hybris.platform.xyformsservices.enums.YFormDataTypeEnum;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


public class DefaultInsuranceCheckoutFacade implements InsuranceCheckoutFacade
{

	private static final Logger LOG = Logger.getLogger(DefaultInsuranceCheckoutFacade.class);

	private CheckoutFacade checkoutFacade;

	/**
	 * Checks whether the orderEntryData has valid Form Data or not
	 *
	 * @return boolean
	 */
	@Override
	public boolean orderEntryHasValidFormData()
	{
		final CartData cartData = getCheckoutFacade().getCheckoutCart();

		if (cartData == null)
		{
			return false;
		}

		if (CollectionUtils.isEmpty(cartData.getEntries()))
		{
			return false;
		}

		if (!hasFormData(cartData.getEntries()))
		{
			return false;
		}

		for (final OrderEntryData orderEntryData : cartData.getEntries())
		{
			if (CollectionUtils.isNotEmpty(orderEntryData.getFormDataData()))
			{
				for (final YFormDataData yFormDataData : orderEntryData.getFormDataData())
				{
					if (StringUtils.isEmpty(yFormDataData.getContent()) || !YFormDataTypeEnum.DATA.equals(yFormDataData.getType()))
					{
						return false;
					}
				}
			}
		}

		return true;
	}

	/**
	 * Helper method to find whether the orderEntry has any YForm associated with it.
	 *
	 * @param orderEntryList
	 * @return true if the first orderEntry(first entry is the main product) has formData otherwise false.
	 */
	protected boolean hasFormData(final List<OrderEntryData> orderEntryList)
	{
		boolean hasFormData = false;

		if (CollectionUtils.isNotEmpty(orderEntryList) && CollectionUtils.isNotEmpty(orderEntryList.get(0).getFormDataData()))
		{
			hasFormData = true;
		}

		return hasFormData;
	}

	/**
	 * Checks whether the cart or session have saved form data.
	 *
	 * @return boolean
	 */
	@Override
	public boolean hasSavedFormData()
	{
		final CartData sessionCart = getCheckoutFacade().getCheckoutCart();

		if (sessionCart == null)
		{
			return false;
		}

		if (CollectionUtils.isNotEmpty(sessionCart.getEntries()))
		{
			for (final OrderEntryData entryData : sessionCart.getEntries())
			{
				if (CollectionUtils.isNotEmpty(entryData.getFormDataData()))
				{
					for (final YFormDataData yFormDataData : entryData.getFormDataData())
					{
						if (StringUtils.isNotEmpty(yFormDataData.getContent()))
						{
							return true;
						}
					}

				}
			}
		}

		return Boolean.TRUE.equals(sessionCart.getHasSessionFormData());
	}

	protected CheckoutFacade getCheckoutFacade()
	{
		return checkoutFacade;
	}

	@Required
	public void setCheckoutFacade(final CheckoutFacade checkoutFacade)
	{
		this.checkoutFacade = checkoutFacade;
	}
}

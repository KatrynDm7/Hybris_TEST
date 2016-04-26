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
package de.hybris.platform.financialfacades.populators;

import de.hybris.platform.commercefacades.insurance.data.InsuranceQuoteReviewData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;

import org.springframework.util.Assert;


/**
 * The class of InsuranceQuoteReviewDetailsPopulator.
 */
public class InsuranceQuoteReviewDetailsPopulator extends InsurancePolicyDetailsPopulator
{

	/**
	 * Populate the target instance with values from the source instance.
	 * 
	 * @param orderEntryData
	 *           the source object
	 * @param insuranceQuoteReviewData
	 *           the target to fill
	 */
	public void populate(final OrderEntryData orderEntryData, final InsuranceQuoteReviewData insuranceQuoteReviewData)
	{
		Assert.notNull(orderEntryData, "orderEntryData cannot be null");
        Assert.notNull(insuranceQuoteReviewData, "insuranceQuoteReviewData cannot be null");

		super.populate(orderEntryData, insuranceQuoteReviewData);
	}
}

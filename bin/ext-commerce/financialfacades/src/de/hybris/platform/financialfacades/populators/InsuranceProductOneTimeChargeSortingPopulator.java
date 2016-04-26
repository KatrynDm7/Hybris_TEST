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

import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.subscriptionfacades.data.OneTimeChargeEntryData;
import de.hybris.platform.subscriptionfacades.data.SubscriptionPricePlanData;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;


/**
 * The class of InsuranceProductOneTimeChargeSortingPopulator.
 */
public class InsuranceProductOneTimeChargeSortingPopulator<SOURCE extends ProductModel, TARGET extends ProductData> implements
		Populator<SOURCE, TARGET>
{
	private static final Logger LOG = Logger.getLogger(InsuranceProductOneTimeChargeSortingPopulator.class);

	private Comparator<OneTimeChargeEntryData> oneTimeChargeEntryDataComparator;

	@Override
	public void populate(final SOURCE source, final TARGET target) throws ConversionException
	{
		Assert.notNull(target, "productData parameter cannot be null");

		if (target.getPrice() != null)
		{
			if (target.getPrice() instanceof SubscriptionPricePlanData)
			{
				final SubscriptionPricePlanData pricePlanData = (SubscriptionPricePlanData) target.getPrice();

				final List<OneTimeChargeEntryData> oneTimeChargeEntryDataCollection = pricePlanData.getOneTimeChargeEntries();

				Collections.sort(oneTimeChargeEntryDataCollection, getOneTimeChargeEntryDataComparator());

				if (LOG.isDebugEnabled())
				{
					for (final OneTimeChargeEntryData oneTimeChargeEntry : oneTimeChargeEntryDataCollection)
					{
						LOG.debug(oneTimeChargeEntry.getName() + "[" + oneTimeChargeEntry.getBillingTime().getCode() + ":"
								+ oneTimeChargeEntry.getBillingTime().getName() + ":" + oneTimeChargeEntry.getPrice().getFormattedValue()
								+ "]");
					}
				}
			}
		}

	}

	protected Comparator<OneTimeChargeEntryData> getOneTimeChargeEntryDataComparator()
	{
		return oneTimeChargeEntryDataComparator;
	}

	@Required
	public void setOneTimeChargeEntryDataComparator(final Comparator<OneTimeChargeEntryData> oneTimeChargeEntryDataComparator)
	{
		this.oneTimeChargeEntryDataComparator = oneTimeChargeEntryDataComparator;
	}

}

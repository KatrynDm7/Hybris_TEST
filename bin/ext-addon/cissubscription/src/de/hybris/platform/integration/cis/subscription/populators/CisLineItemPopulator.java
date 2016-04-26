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
package de.hybris.platform.integration.cis.subscription.populators;

import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.subscriptionfacades.data.OrderEntryPriceData;

import java.math.BigDecimal;

import org.springframework.util.Assert;

import com.hybris.cis.api.model.CisLineItem;


/**
 * Populate the CisLineItem with the OrderEntryData information
 */
public class CisLineItemPopulator implements Populator<OrderEntryData, CisLineItem>
{
	@Override
	public void populate(final OrderEntryData source, final CisLineItem target) throws ConversionException
	{
		Assert.notNull(target, "Parameter target cannot be null.");

		if (source == null)
		{
			return;
		}

		target.setId(source.getEntryNumber());
		target.setItemCode(source.getProduct().getCode());
		target.setProductDescription(getProductDescription(source));
		target.setQuantity(getQuantity(source));
		target.setTaxCode(null);
		target.setUnitPrice(getPayNowPrice(source));
		target.setVendorParameters(null);
	}

	@SuppressWarnings("unused")
	protected Integer getQuantity(final OrderEntryData source)
	{
		// always return a quantity of 1 for this implementation
		return Integer.valueOf(1);
	}

	protected String getProductDescription(final OrderEntryData source)
	{
		final int quantity = source.getQuantity().intValue();

		String description = source.getProduct().getName();
		if (quantity > 1)
		{
			description = String.format("%dx %s", Integer.valueOf(quantity), description);
		}
		return description;
	}

	protected BigDecimal getPayNowPrice(final OrderEntryData entry)
	{
		BigDecimal price = BigDecimal.valueOf(0.00);
		for (final OrderEntryPriceData entryPrice : entry.getOrderEntryPrices())
		{
			if (entryPrice.getBillingTime() != null && "paynow".equalsIgnoreCase(entryPrice.getBillingTime().getCode()))
			{
				price = entryPrice.getTotalPrice().getValue();
				break;
			}
		}
		return price;
	}

}

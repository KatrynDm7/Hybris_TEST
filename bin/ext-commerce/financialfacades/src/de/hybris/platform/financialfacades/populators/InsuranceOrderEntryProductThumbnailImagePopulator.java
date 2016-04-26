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

import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;


/**
 * The class of InsuranceOrderEntryPopulator.
 */
public class InsuranceOrderEntryProductThumbnailImagePopulator implements Populator<AbstractOrderEntryModel, OrderEntryData>
{

	private InsuranceProductThumbnailImagePopulator productThumbnailImagePopulator;

	/**
	 * Populate the target instance with values from the source instance.
	 *
	 * @param orderEntryModel
	 *           the source object
	 * @param orderEntryData
	 *           the target to fill
	 */
	@Override
	public void populate(AbstractOrderEntryModel orderEntryModel, OrderEntryData orderEntryData)
	{
		Assert.notNull(orderEntryModel, "orderEntryModel cannot be null");
		Assert.notNull(orderEntryData, "orderEntryData cannot be null");

		if (orderEntryModel.getProduct() != null && orderEntryData.getProduct() != null)
		{
			getProductThumbnailImagePopulator().populate(orderEntryModel.getProduct(), orderEntryData.getProduct());
		}
	}

	protected InsuranceProductThumbnailImagePopulator getProductThumbnailImagePopulator()
	{
		return productThumbnailImagePopulator;
	}

	@Required
	public void setProductThumbnailImagePopulator(InsuranceProductThumbnailImagePopulator productThumbnailImagePopulator)
	{
		this.productThumbnailImagePopulator = productThumbnailImagePopulator;
	}
}

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

import de.hybris.platform.commercefacades.insurance.data.InsurancePolicyData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.xyformsfacades.data.YFormDataData;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;


/**
 * The class of InsurancePolicyDetailsPopulator.
 */
public class InsurancePolicyDetailsPopulator implements Populator<OrderEntryData, InsurancePolicyData>
{
	private Map<String, List<Populator<YFormDataData, InsurancePolicyData>>> detailsPopulatorsMap;

	/**
	 * Populate the target instance with values from the source instance.
	 *
	 * @param orderEntryData
	 *           the source object
	 * @param insurancePolicyData
	 *           the target to fill
	 */
	@Override
	public void populate(final OrderEntryData orderEntryData, final InsurancePolicyData insurancePolicyData)
	{
		Assert.notNull(orderEntryData, "orderEntryData cannot be null");
		Assert.notNull(insurancePolicyData, "insurancePolicyData cannot be null");

		if (orderEntryData.getProduct() != null && orderEntryData.getProduct().getDefaultCategory() != null)
		{
			final String defaultCategoryCode = orderEntryData.getProduct().getDefaultCategory().getCode();

			if (detailsPopulatorsMap.containsKey(defaultCategoryCode))
			{
				final List<Populator<YFormDataData, InsurancePolicyData>> populators = getDetailsPopulatorsMap().get(
						defaultCategoryCode);
				if (CollectionUtils.isNotEmpty(populators))
				{
					for (final Populator<YFormDataData, InsurancePolicyData> populator : populators)
					{
						if (CollectionUtils.isNotEmpty(orderEntryData.getFormDataData()))
						{
							for (final YFormDataData yFormDataData : orderEntryData.getFormDataData())
							{
								populator.populate(yFormDataData, insurancePolicyData);
							}
						}
					}

				}
			}
		}
	}

	protected Map<String, List<Populator<YFormDataData, InsurancePolicyData>>> getDetailsPopulatorsMap()
	{
		return detailsPopulatorsMap;
	}

	@Required
	public void setDetailsPopulatorsMap(final Map<String, List<Populator<YFormDataData, InsurancePolicyData>>> detailsPopulatorsMap)
	{
		this.detailsPopulatorsMap = detailsPopulatorsMap;
	}
}

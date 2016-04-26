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
package de.hybris.platform.configurablebundlefacades.converters.populator;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.configurablebundleservices.model.BundleSelectionCriteriaModel;
import de.hybris.platform.configurablebundleservices.model.BundleTemplateModel;
import de.hybris.platform.configurablebundleservices.model.PickExactlyNBundleSelectionCriteriaModel;
import de.hybris.platform.configurablebundleservices.model.PickNToMBundleSelectionCriteriaModel;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.configurablebundlefacades.data.BundleTemplateData;
import org.apache.commons.collections.CollectionUtils;


/**
 * 
 * Populator implementation for {@link BundleTemplateModel} as source and {@link BundleTemplateData} as target type.
 */

public class BundleTemplatePopulator<SOURCE extends BundleTemplateModel, TARGET extends BundleTemplateData> implements
		Populator<SOURCE, TARGET>
{

	@Override
	public void populate(final SOURCE source, final TARGET target)
	{
		validateParameterNotNullStandardMessage("target",target);
		validateParameterNotNullStandardMessage("source",source);

		target.setId(source.getId());
		target.setName(source.getName());
		target.setVersion(source.getVersion());

		if (CollectionUtils.isNotEmpty(source.getProducts()))
		{
			target.setType(source.getProducts().iterator().next().getClass().getSimpleName());
		}

		//maximum product selection for the given BundleTemplate.
		target.setMaxItemsAllowed(getMaxNoOfProductsAllowed(source));
	}

	/**
	 * Helper method to find maximum possible product selections for the given BundleTemplate based on Selection
	 * criteria.
	 * 
	 * @param bundleTemplate
	 * @return Number of products allowed to the BundleTemplate
	 */
	protected int getMaxNoOfProductsAllowed(final BundleTemplateModel bundleTemplate)
	{
		int maxItemsAllowed = 0;

		final BundleSelectionCriteriaModel selectionCriteria = bundleTemplate.getBundleSelectionCriteria();

		if (selectionCriteria instanceof PickNToMBundleSelectionCriteriaModel)
		{
			maxItemsAllowed = ((PickNToMBundleSelectionCriteriaModel) selectionCriteria).getM();
		}
		else if (selectionCriteria instanceof PickExactlyNBundleSelectionCriteriaModel)
		{
			maxItemsAllowed = ((PickExactlyNBundleSelectionCriteriaModel) selectionCriteria).getN();
		}

		return maxItemsAllowed;
	}

}

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
package de.hybris.platform.subscriptionfacades.converters.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.subscriptionfacades.data.BillingCycleTypeData;
import de.hybris.platform.subscriptionservices.enums.BillingCycleType;

import org.springframework.beans.factory.annotation.Required;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;


/**
 * Populator implementation for {@link BillingCycleType} as source and {@link BillingCycleTypeData} as target type.
 */
public class BillingCycleTypePopulator implements Populator<BillingCycleType, BillingCycleTypeData>
{
	private TypeService typeService;

	@Override
	public void populate(final BillingCycleType source, final BillingCycleTypeData target) throws ConversionException
	{
		validateParameterNotNullStandardMessage("source", source);
		validateParameterNotNullStandardMessage("target", target);

		target.setCode(source.getCode());
		target.setName(getTypeService().getEnumerationValue(source).getName());
	}

	protected TypeService getTypeService()
	{
		return typeService;
	}

	@Required
	public void setTypeService(final TypeService typeService)
	{
		this.typeService = typeService;
	}

}

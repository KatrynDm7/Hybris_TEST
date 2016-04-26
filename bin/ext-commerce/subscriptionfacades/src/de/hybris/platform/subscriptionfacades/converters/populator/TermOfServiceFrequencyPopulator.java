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

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.subscriptionfacades.data.TermOfServiceFrequencyData;
import de.hybris.platform.subscriptionservices.enums.TermOfServiceFrequency;

import org.springframework.beans.factory.annotation.Required;


/**
 * Populator implementation for {@link TermOfServiceFrequency} as source and {@link TermOfServiceFrequencyData} as
 * target type.
 */
public class TermOfServiceFrequencyPopulator implements Populator<TermOfServiceFrequency, TermOfServiceFrequencyData>
{
	private TypeService typeService;

	@Override
	public void populate(final TermOfServiceFrequency source, final TermOfServiceFrequencyData target)
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

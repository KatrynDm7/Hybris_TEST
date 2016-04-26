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
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.subscriptionfacades.data.TermOfServiceRenewalData;
import de.hybris.platform.subscriptionservices.enums.TermOfServiceRenewal;

import org.springframework.beans.factory.annotation.Required;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

/**
 * Converter implementation for {@link TermOfServiceRenewal} as source and {@link TermOfServiceRenewalData} as target
 * type.
 */
public class TermOfServiceRenewalPopulator implements Populator<TermOfServiceRenewal, TermOfServiceRenewalData>
{
	private TypeService typeService;

	@Override
	public void populate(final TermOfServiceRenewal source, final TermOfServiceRenewalData target)
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

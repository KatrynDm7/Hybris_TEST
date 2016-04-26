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

import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import org.springframework.util.Assert;

import com.hybris.cis.api.model.CisAddress;


/**
 * Populate the CisAddress with the AddressData information
 */
public class CisAddressPopulator implements Populator<AddressData, CisAddress>
{
	@Override
	public void populate(final AddressData source, final CisAddress target) throws ConversionException
	{
		Assert.notNull(target, "Parameter target cannot be null.");

		if (source == null)
		{
			return;
		}

		target.setCompany(source.getCompanyName());
		if (source.getCountry() != null)
		{
			target.setCountry(source.getCountry().getIsocode() == null ? source.getCountry().getName() : source.getCountry()
					.getIsocode());
		}
		target.setEmail(source.getEmail());
		target.setFirstName(source.getFirstName());
		target.setLastName(source.getLastName());
		target.setAddressLine1(source.getLine1());
		target.setAddressLine2(source.getLine2());
		target.setPhone(source.getPhone());
		target.setZipCode(source.getPostalCode());
		if (source.getRegion() != null)
		{
			target.setState(source.getRegion().getName());
		}
		target.setCity(source.getTown());
	}
}

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
package de.hybris.platform.b2ctelcofacades.user.converters.populator;

import de.hybris.platform.commercefacades.user.converters.populator.AddressReversePopulator;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;


/**
 * An extension to the standard AddressReversePopulator.
 *
 * Allows addresses with countries that have no regions (not allowed in default B2C).
 */
public class B2ctelcoAddressReversePopulator extends AddressReversePopulator
{

	/**
	 * After populating the address data (using the superclass method), this extension checks if the region is null, and
	 * sets it as such on the AddressModel.
	 */
	@Override
	public void populate(final AddressData addressData, final AddressModel addressModel) throws ConversionException
	{
		super.populate(addressData, addressModel);
		if (addressData.getRegion() == null)
		{
			addressModel.setRegion(null);
		}

	}

}

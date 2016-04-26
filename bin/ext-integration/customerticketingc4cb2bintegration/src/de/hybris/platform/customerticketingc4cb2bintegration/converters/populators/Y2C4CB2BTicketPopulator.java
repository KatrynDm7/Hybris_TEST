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
package de.hybris.platform.customerticketingc4cb2bintegration.converters.populators;

import de.hybris.platform.b2bacceleratorfacades.company.B2BCommerceUserFacade;
import de.hybris.platform.b2bacceleratorfacades.order.data.B2BUnitData;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.customerticketingc4cintegration.SitePropsHolder;
import de.hybris.platform.customerticketingc4cintegration.data.ServiceRequestData;
import de.hybris.platform.customerticketingfacades.data.TicketData;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import javax.annotation.Resource;


/**
 *
 * Populator for B2B external customer id
 *
 */
public class Y2C4CB2BTicketPopulator<SOURCE extends TicketData, TARGET extends ServiceRequestData> implements
		Populator<SOURCE, TARGET>
{
	@Resource(name = "b2bCommerceUserFacade")
	protected B2BCommerceUserFacade b2bCommerceUserFacade;

	@Resource(name = "customerFacade")
	private CustomerFacade customerFacade;

	@Resource
	private SitePropsHolder sitePropsHolder;

	@Override
	public void populate(final TicketData source, final ServiceRequestData target) throws ConversionException
	{
		if (!sitePropsHolder.isB2C() && (source.getStatus() == null || source.getStatus().getId() == null)) // creating
		{
			final B2BUnitData businessUnit = b2bCommerceUserFacade.getParentUnitForCustomer(customerFacade.getCurrentCustomerUid());
			target.setExternalContactID(source.getCustomerId());

			//if the length of the BU id is 10 or less then just use it as the business unit it
			//else just get the first 10 digits
			String businessUnitId = businessUnit.getUid();
			if (businessUnitId.length() <= 10)
			{
				target.setExternalCustomerID(businessUnitId);
			}
			else
			{
				businessUnitId = businessUnitId.substring(0, 10);
				target.setExternalCustomerID(businessUnitId);
			}
		}
	}
}
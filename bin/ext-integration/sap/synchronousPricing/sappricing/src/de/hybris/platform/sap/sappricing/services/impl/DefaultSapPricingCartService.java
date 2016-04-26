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
package de.hybris.platform.sap.sappricing.services.impl;

import org.springframework.beans.factory.annotation.Required;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.sap.sappricing.services.SapPartnerService;
import de.hybris.platform.sap.sappricing.services.SapPricingBolFactory;
import de.hybris.platform.sap.sappricing.services.SapPricingCartService;
import de.hybris.platform.sap.sappricingbol.converter.ConversionService;

public class DefaultSapPricingCartService implements SapPricingCartService
{
	private SapPricingBolFactory bolFactory;
	private SapPartnerService sapPartnerService;
	private ConversionService conversionService;

	public ConversionService getConversionService()
	{
		return conversionService;
	}

	@Required
	public void setConversionService(ConversionService conversionService)
	{
		this.conversionService = conversionService;
	}
	public SapPartnerService getSapPartnerService() {
		return sapPartnerService;
	}

	@Required
	public void setSapPartnerService(SapPartnerService sapPartnerService) {
		this.sapPartnerService = sapPartnerService;
	}

	public SapPricingBolFactory getBolFactory()
	{
		return bolFactory;
	}

	@Required
	public void setBolFactory(SapPricingBolFactory bolFactory)
	{
		this.bolFactory = bolFactory;
	}

	@Override
	public void getPriceInformationForCart(AbstractOrderModel order)
	{
		
		getBolFactory().getSapPricing().getPriceInformationForCart(order, getSapPartnerService().getPartnerFunction(), this.getConversionService());
	}

}

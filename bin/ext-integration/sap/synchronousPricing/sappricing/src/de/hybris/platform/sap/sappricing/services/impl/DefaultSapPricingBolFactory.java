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

import de.hybris.platform.sap.core.common.util.GenericFactory;
import de.hybris.platform.sap.sappricing.services.SapPricingBolFactory;
import de.hybris.platform.sap.sappricingbol.businessobject.interf.SapPricing;
import de.hybris.platform.sap.sappricingbol.constants.SappricingbolConstants;


public class DefaultSapPricingBolFactory implements SapPricingBolFactory
{


	private GenericFactory genericFactory;

	/**
	 * 
	 * @return the genericFactory
	 */
	public GenericFactory getGenericFactory()
	{
		return genericFactory;
	}

	/**
	 * 
	 * @param genericFactory
	 */
	public void setGenericFactory(GenericFactory genericFactory)
	{
		this.genericFactory = genericFactory;
	}

	@Override
	public SapPricing getSapPricing() {
		return (SapPricing) genericFactory.getBean(SappricingbolConstants.SAP_PRICING_BO);
	}
}

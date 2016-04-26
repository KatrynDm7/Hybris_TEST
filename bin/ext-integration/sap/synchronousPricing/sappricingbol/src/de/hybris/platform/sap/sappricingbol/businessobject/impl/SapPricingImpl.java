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
package de.hybris.platform.sap.sappricingbol.businessobject.impl;


import java.util.List;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.jalo.order.price.PriceInformation;
import de.hybris.platform.sap.core.jco.exceptions.BackendException;
import de.hybris.platform.sap.core.bol.businessobject.BackendInterface;
import de.hybris.platform.sap.core.bol.businessobject.BusinessObjectBase;
import de.hybris.platform.sap.core.bol.businessobject.CommunicationException;
import de.hybris.platform.sap.sappricingbol.backend.interf.SapPricingBackend;
import de.hybris.platform.sap.sappricingbol.businessobject.interf.SapPricing;
import de.hybris.platform.sap.sappricingbol.businessobject.interf.SapPricingPartnerFunction;
import de.hybris.platform.sap.sappricingbol.converter.ConversionService;
import de.hybris.platform.sap.sappricingbol.exceptions.SapPricingRuntimeException;


/**
 * 
 */
@BackendInterface(SapPricingBackend.class)
public class SapPricingImpl extends BusinessObjectBase implements SapPricing
{

	/**
	 * @return the SAPPricingBackend
	 * @throws BackendException
	 */
	public SapPricingBackend getSapPricingBackend() throws BackendException
	{
		return (SapPricingBackend) getBackendBusinessObject();
	}

	@Override
	public void getPriceInformationForCart(AbstractOrderModel order,
			SapPricingPartnerFunction partnerFunction, ConversionService conversionService) {

		try {
			getSapPricingBackend().readPricesForCart(order, partnerFunction, conversionService);
		} catch (final CommunicationException e) {
			throw new SapPricingRuntimeException(e);

		} catch (final BackendException e) {
			throw new SapPricingRuntimeException(e);
		}

	}

	@Override
	public List<PriceInformation> getPriceInformationForProducts(List<ProductModel> productModels,
			SapPricingPartnerFunction partnerFunction, ConversionService conversionService)
	{
		
		try {
			return getSapPricingBackend().readPriceInformationForProducts(productModels, partnerFunction, conversionService);
		}

		catch (final BackendException e) {
			throw new SapPricingRuntimeException(e);
		}
	}

}

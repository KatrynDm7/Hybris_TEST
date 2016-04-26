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
package de.hybris.platform.sap.sappricingbol.businessobject.interf;



import java.util.List;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.jalo.order.price.PriceInformation;
import de.hybris.platform.sap.sappricingbol.converter.ConversionService;


/**
 * 
 */
public interface SapPricing
{

	/**
	 * 
	 * @param order
	 * @param partnerFunction
	 * @param conversionService TODO
	 */
	public void getPriceInformationForCart(AbstractOrderModel order, SapPricingPartnerFunction partnerFunction, ConversionService conversionService);

	/**
	 * @param partnerFunction
	 * @param conversionService TODO
	 * @param productModel
	 * @return List<PriceInformation>
	 */
	public List<PriceInformation> getPriceInformationForProducts(final List<ProductModel> productModels, final SapPricingPartnerFunction partnerFunction, ConversionService conversionService);

}

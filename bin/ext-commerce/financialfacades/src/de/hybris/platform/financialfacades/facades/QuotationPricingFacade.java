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
package de.hybris.platform.financialfacades.facades;

import de.hybris.platform.commercefacades.quotation.QuotationRequestData;
import de.hybris.platform.commercefacades.quotation.QuotationResponseData;


/**
 * The class of QuotationFacade.
 */
public interface QuotationPricingFacade
{
	/**
	 * Get quote by quotationRequestData.
	 *
	 * @param quotationRequestData
	 *           the quotation request data
	 * @return the quotation response data
	 */
	QuotationResponseData getQuote(final QuotationRequestData quotationRequestData);
}

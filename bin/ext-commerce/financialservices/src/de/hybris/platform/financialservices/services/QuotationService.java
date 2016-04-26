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
package de.hybris.platform.financialservices.services;

import de.hybris.platform.commercefacades.quotation.QuotationRequestData;
import de.hybris.platform.commercefacades.quotation.QuotationResponseData;
import de.hybris.platform.financialservices.model.InsuranceQuoteModel;


/**
 * The interface for the Quotation Service
 */
public interface QuotationService
{
	/**
	 * Creates the Quotation Request Data object based on the supplied information
	 *
	 * @param providerName
	 *           the name of the quotation provider
	 * @param productIds
	 *           a number of product Ids
	 * @param quote
	 *           the <InsuranceQuoteModel> object
	 * @return the QuotationRequestData object
	 */
	public QuotationRequestData createQuotationRequestData(final String providerName, final String productIds,
			final InsuranceQuoteModel quote);

	/**
	 * Determines the price for a given response, item and whether or not that item has a 'pay now' flag set
	 *
	 * @param responseData
	 *           the QuotationResponseData bean
	 * @param itemId
	 *           the id of the quote item
	 * @param isPaynow
	 *           whether or not the item is flagged 'paynow'
	 * @return the price for the item
	 */
	public Double getPrice(final QuotationResponseData responseData, final String itemId, final boolean isPaynow);

}

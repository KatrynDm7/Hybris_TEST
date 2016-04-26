/*
 *
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
 */
package de.hybris.platform.financialfacades.services;

import de.hybris.platform.financialservices.enums.QuoteBindingState;
import de.hybris.platform.financialservices.model.InsuranceQuoteModel;


public interface InsuranceQuoteService
{
	/**
	 * Update quote on the new binding state.
	 */
	InsuranceQuoteModel updateQuoteByBindingState(final InsuranceQuoteModel quoteModel, final QuoteBindingState state);

}

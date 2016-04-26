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
package de.hybris.platform.acceleratorwebservicesaddon.payment.service;


import de.hybris.platform.acceleratorwebservicesaddon.model.payment.PaymentSubscriptionResultModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;


/**
 * Service for managing payment subscription result
 *
 * @spring.bean paymentSubscriptionResultService
 *
 */
public interface PaymentSubscriptionResultService
{
	/**
	 * Returns payment subscription result by given cart id
	 *
	 * @param cartId
	 *           - cart identifier
	 * @return payment subscription result
	 * @throws IllegalArgumentException
	 *            when 'cartId' parameter is null
	 * @throws UnknownIdentifierException
	 *            when there is no result related to cart with given id
	 */
	PaymentSubscriptionResultModel findPaymentSubscriptionResultByCart(String cartId);

	/**
	 * Remove payment subscription result related to cart with given cartId
	 *
	 * @param cartId
	 *           - cart identifier (code or guid)
	 * @throws IllegalArgumentException
	 *            when 'cartId' parameter is null
	 */
	void removePaymentSubscriptionResultForCart(String cartId);

	/**
	 * Remove payment subscription result related to cart with given cartCode or cart guid
	 *
	 * @param cartCode
	 *           Cart code
	 * @param cartGuid
	 *           Cart guid
	 * @throws IllegalArgumentException
	 *            when 'cartCode' or 'cartGuid' parameter is null
	 */
	void removePaymentSubscriptionResultForCart(String cartCode, String cartGuid);

	/**
	 * Save payment subscription result model
	 *
	 * @param paymentSubscriptionResultModel
	 *           - object to save
	 * @throws IllegalArgumentException
	 *            when 'paymentSubscriptionResultModel' parameter is null
	 */
	void savePaymentSubscriptionResult(PaymentSubscriptionResultModel paymentSubscriptionResultModel);
}

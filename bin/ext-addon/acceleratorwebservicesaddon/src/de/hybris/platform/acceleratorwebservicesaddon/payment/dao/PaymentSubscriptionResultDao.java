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
package de.hybris.platform.acceleratorwebservicesaddon.payment.dao;


import de.hybris.platform.acceleratorwebservicesaddon.model.payment.PaymentSubscriptionResultModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.internal.dao.Dao;

import java.util.Date;
import java.util.List;


/**
 * Dao for {@link PaymentSubscriptionResultModel} access.
 *
 * @spring.bean paymentSubscriptionResultDao
 *
 */
public interface PaymentSubscriptionResultDao extends Dao
{
	/**
	 * Find payment subscription result for given cart
	 *
	 * @param cartId
	 *           - cart identifier (code or guid)
	 * @return payment subscription result
	 * @throws ModelNotFoundException
	 *            if payment subscription result for given cart doesn't exist
	 */
	PaymentSubscriptionResultModel findPaymentSubscriptionResultByCart(String cartId);

	/**
	 * Find payment subscription result for cart
	 *
	 * @param cartCode
	 *           Cart code
	 * @param cartGuid
	 *           Cart guid
	 * @return list with found objects
	 */
	List<PaymentSubscriptionResultModel> findPaymentSubscriptionResultByCart(String cartCode, String cartGuid);

	/**
	 * Find payment subscription result modified before given date
	 *
	 * @param modifiedBefore
	 *           Date before which subscription result should be modified
	 * @return list with found objects
	 */
	List<PaymentSubscriptionResultModel> findOldPaymentSubscriptionResult(Date modifiedBefore);
}

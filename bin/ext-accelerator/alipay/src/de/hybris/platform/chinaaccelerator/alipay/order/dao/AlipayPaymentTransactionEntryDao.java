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
package de.hybris.platform.chinaaccelerator.alipay.order.dao;

import de.hybris.platform.payment.model.AlipayPaymentTransactionEntryModel;

public interface AlipayPaymentTransactionEntryDao {
	public AlipayPaymentTransactionEntryModel findPaymentTransactionEntryByRequestId(String batch_no);
	public AlipayPaymentTransactionEntryModel findPaymentTransactionEntryByNotifyId(final String notify_id);
}

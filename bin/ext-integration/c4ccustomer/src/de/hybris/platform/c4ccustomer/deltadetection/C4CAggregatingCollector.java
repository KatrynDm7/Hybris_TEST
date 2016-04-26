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
package de.hybris.platform.c4ccustomer.deltadetection;

import de.hybris.deltadetection.ChangesCollector;
import de.hybris.y2ysync.deltadetection.collector.BatchingCollector;

import javax.annotation.Nonnull;


/**
 * Specification of {@link ChangesCollector}, used during synchronization of customers and addresses.
 */
public interface C4CAggregatingCollector extends ChangesCollector
{
	/**
	 * @param customerCollector Collector that will take care of customers.
	 */
	void setCustomerCollector(@Nonnull BatchingCollector customerCollector);

	/**
	 * @param addressCollector Collector that will take care of addresses.
	 */
	void setAddressCollector(@Nonnull BatchingCollector addressCollector);

	/**
	 * @param id stream configuration id for customers.
	 */
	void setCustomerConfigurationId(@Nonnull String id);

	/**
	 * @param id stream configuration id for addresses.
	 */
	void setAddressConfigurationId(@Nonnull String id);
}

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
package de.hybris.platform.commerceservices.delivery.dao;

import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;

import java.util.Collection;


/**
 * DAO to find zone delivery modes.
 */
public interface CountryZoneDeliveryModeDao extends Dao
{
	/**
	 * Find the delivery modes for the delivery country, currency and net flag.
	 * 
	 * @param deliveryCountry
	 *           the delivery country
	 * @param currency
	 *           the cart currency
	 * @param net
	 *           the net flag
	 * @return the matching delivery modes
	 * 
	 * @deprecated use findDeliveryModes(AbstractOrderModel abstractOrder) instead.
	 */
	@Deprecated
	Collection<DeliveryModeModel> findDeliveryModesByCountryAndCurrency(CountryModel deliveryCountry, CurrencyModel currency,
			Boolean net);

	/**
	 * Find the delivery modes for the delivery country, currency, net flag and store.
	 * 
	 * @param order
	 *           the order with search parameters.
	 * @return the matching delivery modes
	 */
	Collection<DeliveryModeModel> findDeliveryModes(AbstractOrderModel order);

}

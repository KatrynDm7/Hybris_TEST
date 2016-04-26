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
package de.hybris.platform.commerceservices.delivery;

import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.deliveryzone.model.ZoneDeliveryModeModel;
import de.hybris.platform.deliveryzone.model.ZoneDeliveryModeValueModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.util.PriceValue;

import java.util.Collection;
import java.util.List;


/**
 * The Delivery Service provides functionality around available and supported delivery rules that are scoped to a single
 * store front rather than an entire platform deployment which may include multiple store fronts.
 */
public interface DeliveryService
{
	/**
	 * Get the supported delivery countries. Given {@link AbstractOrderModel} might be taken into account to return
	 * appropriate result.
	 * 
	 * 
	 * @return list of supported delivery countries.
	 */
	List<CountryModel> getDeliveryCountriesForOrder(AbstractOrderModel abstractOrder);

	/**
	 * Get the list of supported delivery addresses.
	 * 
	 * @param visibleAddressesOnly
	 *           include only the visible addresses
	 * @return the supported delivery addresses
	 */
	List<AddressModel> getSupportedDeliveryAddressesForOrder(AbstractOrderModel abstractOrder, boolean visibleAddressesOnly);

	/**
	 * Get the supported delivery modes for the abstract order. Deprecated. Use
	 * {@link DeliveryService#getSupportedDeliveryModeListForOrder(AbstractOrderModel)}
	 * 
	 * @param abstractOrder
	 *           the abstract order
	 * @return the collection of supported delivery modes
	 */
	@Deprecated
	Collection<DeliveryModeModel> getSupportedDeliveryModesForOrder(AbstractOrderModel abstractOrder);

	/**
	 * Get the supported delivery modes for the abstract order.
	 * 
	 * @param abstractOrder
	 * @return the list of supported delivery modes, by default sorted by cost
	 */
	List<DeliveryModeModel> getSupportedDeliveryModeListForOrder(AbstractOrderModel abstractOrder);

	/**
	 * Find delivery mode given its code
	 * 
	 * @param code
	 *           the code
	 * @return the delivery mode
	 */
	DeliveryModeModel getDeliveryModeForCode(String code);

	/**
	 * Get the country given its iso code. Deprecated. Use {@link CommonI18NService#getCountry(String)}.
	 * 
	 * @param countryIso
	 *           the country iso code
	 * @return the country for the code specified
	 */
	@Deprecated
	CountryModel getCountryForCode(String countryIso);

	/**
	 * Deprecated. Get delivery mode value (delivery cost) of a delivery mode for a given cart or order. This method
	 * determines the delivery mode value based on the currency and delivery address associated with the cart/order.
	 * 
	 * @param deliveryMode
	 *           the delivery mode
	 * @param abstractOrder
	 *           the abstract order
	 * @return the delivery mode value
	 */
	@Deprecated
	ZoneDeliveryModeValueModel getZoneDeliveryModeValueForAbstractOrder(ZoneDeliveryModeModel deliveryMode,
			AbstractOrderModel abstractOrder);

	/**
	 * Get the delivery cost of the given delivery mode for the given cart or order.
	 * 
	 * @param deliveryMode
	 *           the delivery mode
	 * @param abstractOrder
	 *           the abstract order
	 * @return the delivery cost
	 */
	PriceValue getDeliveryCostForDeliveryModeAndAbstractOrder(DeliveryModeModel deliveryMode, AbstractOrderModel abstractOrder);

}

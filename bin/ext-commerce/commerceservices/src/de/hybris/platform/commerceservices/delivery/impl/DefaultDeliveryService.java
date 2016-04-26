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
package de.hybris.platform.commerceservices.delivery.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.commerceservices.delivery.DeliveryService;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.commerceservices.strategies.DeliveryAddressesLookupStrategy;
import de.hybris.platform.commerceservices.strategies.DeliveryModeLookupStrategy;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.deliveryzone.model.ZoneDeliveryModeModel;
import de.hybris.platform.deliveryzone.model.ZoneDeliveryModeValueModel;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.delivery.DeliveryMode;
import de.hybris.platform.jalo.order.delivery.JaloDeliveryModeException;
import de.hybris.platform.order.daos.DeliveryModeDao;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.PriceValue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of the {@link DeliveryService}.
 */
public class DefaultDeliveryService implements DeliveryService
{
	private CommerceCommonI18NService commerceCommonI18NService;
	private CommonI18NService commonI18NService;
	private DeliveryModeDao deliveryModeDao;
	private DeliveryAddressesLookupStrategy deliveryAddressesLookupStrategy;
	private DeliveryModeLookupStrategy deliveryModeLookupStrategy;
	private ModelService modelService;

	protected DeliveryModeLookupStrategy getDeliveryModeLookupStrategy()
	{
		return deliveryModeLookupStrategy;
	}

	@Required
	public void setDeliveryModeLookupStrategy(final DeliveryModeLookupStrategy deliveryModeLookupStrategy)
	{
		this.deliveryModeLookupStrategy = deliveryModeLookupStrategy;
	}

	protected CommerceCommonI18NService getCommerceCommonI18NService()
	{
		return commerceCommonI18NService;
	}

	@Required
	public void setCommerceCommonI18NService(final CommerceCommonI18NService commerceCommonI18NService)
	{
		this.commerceCommonI18NService = commerceCommonI18NService;
	}

	protected CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	@Required
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}

	protected DeliveryModeDao getDeliveryModeDao()
	{
		return deliveryModeDao;
	}

	@Required
	public void setDeliveryModeDao(final DeliveryModeDao deliveryModeDao)
	{
		this.deliveryModeDao = deliveryModeDao;
	}

	protected DeliveryAddressesLookupStrategy getDeliveryAddressesLookupStrategy()
	{
		return deliveryAddressesLookupStrategy;
	}

	@Required
	public void setDeliveryAddressesLookupStrategy(final DeliveryAddressesLookupStrategy deliveryAddressesLookupStrategy)
	{
		this.deliveryAddressesLookupStrategy = deliveryAddressesLookupStrategy;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	/**
	 * Default implementation ignores a given {@link AbstractOrderModel} informations.
	 */
	@Override
	public List<CountryModel> getDeliveryCountriesForOrder(final AbstractOrderModel abstractOrder)
	{
		Collection<CountryModel> countries = getCommerceCommonI18NService().getAllCountries();
		if (countries == null || countries.isEmpty())
		{
			countries = getCommonI18NService().getAllCountries();
		}

		if (countries != null && !countries.isEmpty())
		{
			return new ArrayList<CountryModel>(countries);
		}
		return Collections.emptyList();
	}

	@Override
	@Deprecated
	public CountryModel getCountryForCode(final String countryIso)
	{
		return getCommonI18NService().getCountry(countryIso);
	}

	@Override
	public List<AddressModel> getSupportedDeliveryAddressesForOrder(final AbstractOrderModel abstractOrder,
			final boolean visibleAddressesOnly)
	{
		final List<AddressModel> addresses = getDeliveryAddressesLookupStrategy().getDeliveryAddressesForOrder(abstractOrder,
				visibleAddressesOnly);
		if (addresses != null && !addresses.isEmpty())
		{
			final List<CountryModel> deliveryCountries = getDeliveryCountriesForOrder(abstractOrder);

			final List<AddressModel> result = new ArrayList<AddressModel>();

			// Filter for delivery addresses
			for (final AddressModel address : addresses)
			{
				if (address.getCountry() != null)
				{
					// Filter out invalid addresses for the site
					final boolean validForSite = deliveryCountries != null && deliveryCountries.contains(address.getCountry());
					if (validForSite)
					{
						result.add(address);
					}
				}
			}

			return result;
		}
		return Collections.emptyList();
	}

	@Override
	public List<DeliveryModeModel> getSupportedDeliveryModeListForOrder(final AbstractOrderModel abstractOrder)
	{
		validateParameterNotNull(abstractOrder, "abstractOrder model cannot be null");
		final List<DeliveryModeModel> deliveryModes = getDeliveryModeLookupStrategy().getSelectableDeliveryModesForOrder(
				abstractOrder);
		sortDeliveryModes(deliveryModes, abstractOrder);
		return deliveryModes;
	}

	@Override
	@Deprecated
	public Collection<DeliveryModeModel> getSupportedDeliveryModesForOrder(final AbstractOrderModel abstractOrder)
	{
		return getSupportedDeliveryModeListForOrder(abstractOrder);
	}

	protected void sortDeliveryModes(final List<DeliveryModeModel> deliveryModeModels, final AbstractOrderModel abstractOrder)
	{
		Collections.sort(deliveryModeModels, new DeliveryModeCostComparator(this, abstractOrder));
	}

	@Override
	public DeliveryModeModel getDeliveryModeForCode(final String code)
	{
		validateParameterNotNull(code, "Parameter code cannot be null");

		final List<DeliveryModeModel> deliveryModes = getDeliveryModeDao().findDeliveryModesByCode(code);

		return (deliveryModes != null && !deliveryModes.isEmpty()) ? deliveryModes.get(0) : null;
	}

	@Override
	@Deprecated
	public ZoneDeliveryModeValueModel getZoneDeliveryModeValueForAbstractOrder(final ZoneDeliveryModeModel deliveryMode,
			final AbstractOrderModel abstractOrder)
	{
		validateParameterNotNull(deliveryMode, "deliveryMode model cannot be null");
		validateParameterNotNull(abstractOrder, "abstractOrder model cannot be null");

		ZoneDeliveryModeValueModel result = null;
		final AddressModel deliveryAddress = abstractOrder.getDeliveryAddress();
		final CurrencyModel currency = abstractOrder.getCurrency();
		if (deliveryAddress != null && currency != null)
		{
			for (final ZoneDeliveryModeValueModel deliveryModeValue : deliveryMode.getValues())
			{
				if (deliveryModeValue.getCurrency().equals(currency)
						&& deliveryModeValue.getZone().getCountries().contains(deliveryAddress.getCountry()))
				{
					result = deliveryModeValue;
					break;
				}
			}
		}

		return result;
	}

	@Override
	public PriceValue getDeliveryCostForDeliveryModeAndAbstractOrder(final DeliveryModeModel deliveryMode,
			final AbstractOrderModel abstractOrder)
	{
		validateParameterNotNull(deliveryMode, "deliveryMode model cannot be null");
		validateParameterNotNull(abstractOrder, "abstractOrder model cannot be null");

		final DeliveryMode deliveryModeSource = getModelService().getSource(deliveryMode);
		try
		{
			final AbstractOrder abstractOrderSource = getModelService().getSource(abstractOrder);
			return deliveryModeSource.getCost(abstractOrderSource);
		}
		catch (final JaloDeliveryModeException e)
		{
			return null;
		}
	}

	public static class DeliveryModeCostComparator implements Comparator<DeliveryModeModel>
	{
		private final DeliveryService deliveryService;
		private final AbstractOrderModel order;

		public DeliveryModeCostComparator(final DeliveryService deliveryService, final AbstractOrderModel order)
		{
			this.deliveryService = deliveryService;
			this.order = order;
		}

		@Override
		public int compare(final DeliveryModeModel deliveryMode1, final DeliveryModeModel deliveryMode2)
		{
			final PriceValue priceValue1 = deliveryService.getDeliveryCostForDeliveryModeAndAbstractOrder(deliveryMode1, order);
			final PriceValue priceValue2 = deliveryService.getDeliveryCostForDeliveryModeAndAbstractOrder(deliveryMode2, order);

			final BigDecimal value1 = priceValue1 != null ? BigDecimal.valueOf(priceValue1.getValue()) : null;
			final BigDecimal value2 = priceValue2 != null ? BigDecimal.valueOf(priceValue2.getValue()) : null;

			if (value1 == null && value2 == null)
			{
				return deliveryMode1.getCode().compareTo(deliveryMode2.getCode());
			}
			else if (value1 == null)
			{
				return -1;
			}
			else if (value2 == null)
			{
				return 1;
			}

			final int result = value1.compareTo(value2);
			return result == 0 ? deliveryMode1.getCode().compareTo(deliveryMode2.getCode()) : result;
		}
	}

}

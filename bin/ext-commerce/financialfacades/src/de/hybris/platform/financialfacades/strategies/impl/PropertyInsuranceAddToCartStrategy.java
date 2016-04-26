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
package de.hybris.platform.financialfacades.strategies.impl;

import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.financialfacades.constants.FinancialfacadesConstants;
import de.hybris.platform.financialservices.model.InsuranceQuoteModel;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.session.Session;
import de.hybris.platform.servicelayer.user.AddressService;
import de.hybris.platform.xyformsservices.exception.YFormServiceException;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Required;

import com.google.common.collect.Maps;


/**
 * Temporary strategy to add the postcode for property insurance (both kinds) into the delivery address on the cart.
 *
 */
public class PropertyInsuranceAddToCartStrategy extends AbstractInsuranceAddToCartStrategy
{
	private static final Logger LOG = Logger.getLogger(TravelInsuranceAddToCartStrategy.class);

	private AddressService addressService;

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.financialacceleratorstorefront.strategies.impl.AbstractInsuranceAddToCartStrategy#addToCartInternal
	 * (java.util.Map)
	 */
	@Override
	protected void addToCartInternal(final Map<String, Object> properties)
	{
		final Session currentSession = getSessionService().getCurrentSession();
		final String sessionPostcode = (String) currentSession.getAttribute(FinancialfacadesConstants.PROPERTY_POSTCODE);

		if (StringUtils.isNotEmpty(sessionPostcode))
		{
			final CartModel cartModel = getCartService().getSessionCart();
			AddressModel deliveryAddress = cartModel.getDeliveryAddress();

			if (cartModel.getDeliveryAddress() == null)
			{
				deliveryAddress = getAddressService().createAddressForOwner(cartModel);
				cartModel.setDeliveryAddress(deliveryAddress);
			}

			try
			{
				deliveryAddress.setPostalcode(sessionPostcode);
				getModelService().saveAll(cartModel, deliveryAddress);
			}
			catch (final ModelSavingException modelSavingException)
			{
				LOG.error("Unable to save changes to address for cart : " + cartModel.getCode());
			}
		}
		if (properties.containsKey(PROPERTY_PRODUCT_CODE) && properties.containsKey(PROPERTY_BUNDLE_NO))
		{
			persistInsuranceInformation();
		}

	}


	@Override
	protected void populateInsuranceDetailsInformation(final InsuranceQuoteModel quoteModel) throws YFormServiceException
	{

		final Map<String, Object> infoMap = Maps.newHashMap();

		final DateTimeFormatter formatter = DateTimeFormat.forPattern(FinancialfacadesConstants.INSURANCE_GENERIC_DATE_FORMAT);

		final String addressLineOne = getSessionService().getAttribute(FinancialfacadesConstants.PROPERTY_ADDRESS1);
		final String coverRequired = getSessionService().getAttribute(FinancialfacadesConstants.PROPERTY_DETAILS_COVER_REQUIRED);
		final String startDate = getSessionService().getAttribute(FinancialfacadesConstants.PROPERTY_DETAILS_START_DATE);
		final String propertyType = getSessionService().getAttribute(FinancialfacadesConstants.PROPERTY_DETAILS_TYPE);
		final String propertyValue = getSessionService().getAttribute(FinancialfacadesConstants.PROPERTY_DETAILS_VALUE);
		final String propertyIsStandard50000ContentCover = getSessionService().getAttribute(
				FinancialfacadesConstants.PROPERTY_DETAILS_IS_STANDARD_50000_CONTENT_COVER);
		final String propertyRebuildCost = getSessionService()
				.getAttribute(FinancialfacadesConstants.PROPERTY_DETAILS_REBUILD_COST);
		final String propertyMultipleOf10000ContentCover = getSessionService().getAttribute(
				FinancialfacadesConstants.PROPERTY_DETAILS_CONTENT_COVER_MULTIPLE_OF_10000);
		if (StringUtils.isNotEmpty(addressLineOne))
		{
			infoMap.put(FinancialfacadesConstants.PROPERTY_ADDRESS1, addressLineOne);
		}
		if (StringUtils.isNotEmpty(coverRequired))
		{
			infoMap.put(FinancialfacadesConstants.PROPERTY_DETAILS_COVER_REQUIRED, coverRequired);
		}
		if (StringUtils.isNotEmpty(startDate))
		{
			infoMap.put(FinancialfacadesConstants.PROPERTY_DETAILS_START_DATE, formatter.parseDateTime(startDate).toDate());
		}
		if (StringUtils.isNotEmpty(propertyType))
		{
			infoMap.put(FinancialfacadesConstants.PROPERTY_DETAILS_TYPE, propertyType);
		}
		if (StringUtils.isNotEmpty(propertyValue))
		{
			infoMap.put(FinancialfacadesConstants.PROPERTY_DETAILS_VALUE, propertyValue);
		}
		if (StringUtils.isNotEmpty(propertyIsStandard50000ContentCover))
		{
			infoMap.put(FinancialfacadesConstants.PROPERTY_DETAILS_IS_STANDARD_50000_CONTENT_COVER,
					propertyIsStandard50000ContentCover);
		}
		if (StringUtils.isNotEmpty(propertyRebuildCost))
		{
			infoMap.put(FinancialfacadesConstants.PROPERTY_DETAILS_REBUILD_COST, propertyRebuildCost);
		}
		if (StringUtils.isNotEmpty(propertyMultipleOf10000ContentCover))
		{
			infoMap.put(FinancialfacadesConstants.PROPERTY_DETAILS_CONTENT_COVER_MULTIPLE_OF_10000,
					propertyMultipleOf10000ContentCover);
		}

		quoteModel.setProperties(infoMap);
	}

	protected AddressService getAddressService()
	{
		return addressService;
	}

	@Required
	public void setAddressService(final AddressService addressService)
	{
		this.addressService = addressService;
	}

}

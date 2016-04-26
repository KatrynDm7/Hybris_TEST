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
package de.hybris.platform.configurablebundlefacades.converters.populator;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.subscriptionservices.model.SubscriptionProductModel;

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * SOLR Populator for {@link SubscriptionProductModel}
 */
public class SearchResultBundleProductPopulator<SOURCE extends SearchResultValueData, TARGET extends ProductData> implements
		Populator<SOURCE, TARGET>
{
	private static final Logger LOG = Logger.getLogger(SearchResultBundleProductPopulator.class);

	private PriceDataFactory priceDataFactory;
	private CommonI18NService commonI18NService;

	@Override
	public void populate(final SOURCE source, final TARGET target)
	{
		validateParameterNotNullStandardMessage("source", source);
		validateParameterNotNullStandardMessage("target", target);
		try
		{
			final Boolean soldIndividually = this.getValue(source, ProductModel.SOLDINDIVIDUALLY);
			target.setSoldIndividually(soldIndividually == null || soldIndividually);

			final Double lowestBundlePriceValue = this.getValue(source, "lowestBundlePriceValue");
			target.setLowestBundlePrice(lowestBundlePriceValue == null ? null : getPriceDataFactory().create(PriceDataType.BUY,
					BigDecimal.valueOf(lowestBundlePriceValue), getCommonI18NService().getCurrentCurrency().getIsocode()));
		}
		catch (ClassCastException e)
		{
			LOG.warn(e);
		}
	}

	protected <T> T getValue(final SOURCE source, final String propertyName)
	{
		if (source.getValues() == null)
		{
			return null;
		}

		// DO NOT REMOVE the cast (T) below, while it should be unnecessary it is required by the javac compiler
		return (T) source.getValues().get(propertyName);
	}

	protected PriceDataFactory getPriceDataFactory()
	{
		return priceDataFactory;
	}

	@Required
	public void setPriceDataFactory(final PriceDataFactory priceDataFactory)
	{
		this.priceDataFactory = priceDataFactory;
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
}

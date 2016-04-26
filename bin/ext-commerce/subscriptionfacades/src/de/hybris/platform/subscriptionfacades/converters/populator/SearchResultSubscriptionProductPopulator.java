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
package de.hybris.platform.subscriptionfacades.converters.populator;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.subscriptionfacades.data.BillingPlanData;
import de.hybris.platform.subscriptionfacades.data.BillingTimeData;
import de.hybris.platform.subscriptionfacades.data.SubscriptionTermData;
import de.hybris.platform.subscriptionfacades.data.TermOfServiceFrequencyData;
import de.hybris.platform.subscriptionfacades.data.TermOfServiceRenewalData;
import de.hybris.platform.subscriptionservices.enums.TermOfServiceRenewal;
import de.hybris.platform.subscriptionservices.model.SubscriptionProductModel;

import org.springframework.beans.factory.annotation.Required;

/**
 * SOLR Populator for {@link SubscriptionProductModel}.
 *
 * @param <SOURCE> source class
 * @param <TARGET> target class
 */
public class SearchResultSubscriptionProductPopulator<SOURCE extends SearchResultValueData, TARGET extends ProductData>
		implements Populator<SOURCE, TARGET>
{

	private ProductService productService;
	private Populator<ProductModel, ProductData> subscriptionPricePlanPopulator;
	private Converter<TermOfServiceRenewal, TermOfServiceRenewalData> termOfServiceRenewalConverter;

	@Override
	public void populate(final SOURCE source, final TARGET target)
	{
		validateParameterNotNullStandardMessage("source", source);
		validateParameterNotNullStandardMessage("target", target);

		final String billingTimeAsString = this.getValue(source, "billingTime");
		final BillingTimeData billingTime = new BillingTimeData();
		billingTime.setName(billingTimeAsString);

		if (target.getSubscriptionTerm() == null)
		{
			target.setSubscriptionTerm(new SubscriptionTermData());
			target.getSubscriptionTerm().setBillingPlan(new BillingPlanData());
		}

		if (target.getSubscriptionTerm().getBillingPlan() == null)
		{
			target.getSubscriptionTerm().setBillingPlan(new BillingPlanData());
		}

		target.getSubscriptionTerm().getBillingPlan().setBillingTime(billingTime);

		final String termOfServiceFrequencyAsString = this.getValue(source, "termLimit");
		final TermOfServiceFrequencyData termOfServiceFrequencyData = new TermOfServiceFrequencyData();
		termOfServiceFrequencyData.setName(termOfServiceFrequencyAsString);
		target.getSubscriptionTerm().setTermOfServiceFrequency(termOfServiceFrequencyData);

		final ProductModel productModel = getProductService().getProductForCode(target.getCode());

		if (productModel instanceof SubscriptionProductModel)
		{
			final PriceData oldPrice = target.getPrice();
			getSubscriptionPricePlanPopulator().populate(productModel, target);

			final TermOfServiceRenewal termOfServiceRenewal = ((SubscriptionProductModel) productModel).getSubscriptionTerm()
					.getTermOfServiceRenewal();
			target.getSubscriptionTerm().setTermOfServiceRenewal(getTermOfServiceRenewalConverter().convert(termOfServiceRenewal));
			// restore values from old price as they may have been overridden by the SubscriptionPricePlanPopulator
			if (oldPrice != null)
			{
				target.getPrice().setValue(oldPrice.getValue());
				target.getPrice().setCurrencyIso(oldPrice.getCurrencyIso());
				target.getPrice().setFormattedValue(oldPrice.getFormattedValue());
				target.getPrice().setMaxQuantity(oldPrice.getMaxQuantity());
				target.getPrice().setMinQuantity(oldPrice.getMinQuantity());
				target.getPrice().setPriceType(oldPrice.getPriceType());
			}
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

	protected ProductService getProductService()
	{
		return productService;
	}

	@Required
	public void setProductService(final ProductService productService)
	{
		this.productService = productService;
	}

	protected Populator<ProductModel, ProductData> getSubscriptionPricePlanPopulator()
	{
		return subscriptionPricePlanPopulator;
	}

	@Required
	public void setSubscriptionPricePlanPopulator(final Populator<ProductModel, ProductData> subscriptionPricePlanPopulator)
	{
		this.subscriptionPricePlanPopulator = subscriptionPricePlanPopulator;
	}

	protected Converter<TermOfServiceRenewal, TermOfServiceRenewalData> getTermOfServiceRenewalConverter()
	{
		return termOfServiceRenewalConverter;
	}

	@Required
	public void setTermOfServiceRenewalConverter(
			final Converter<TermOfServiceRenewal, TermOfServiceRenewalData> termOfServiceRenewalConverter)
	{
		this.termOfServiceRenewalConverter = termOfServiceRenewalConverter;
	}
}

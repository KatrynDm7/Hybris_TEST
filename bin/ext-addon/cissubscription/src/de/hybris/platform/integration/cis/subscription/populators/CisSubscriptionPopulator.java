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
package de.hybris.platform.integration.cis.subscription.populators;

import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.subscriptionfacades.SubscriptionFacade;
import de.hybris.platform.subscriptionfacades.converters.SubscriptionXStreamAliasConverter;
import de.hybris.platform.subscriptionfacades.data.SubscriptionData;

import java.util.Arrays;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import com.hybris.cis.api.subscription.model.CisSubscriptionData;


/**
 * Populate the {@link SubscriptionData} with the {@link CisSubscriptionData} data
 */

public class CisSubscriptionPopulator implements Populator<CisSubscriptionData, SubscriptionData>
{
	private static final Logger LOG = Logger.getLogger(CisSubscriptionPopulator.class);

	private ProductFacade productFacade;
	private SubscriptionFacade subscriptionFacade;
	private SubscriptionXStreamAliasConverter subscriptionXStreamAliasConverter;

	@Override
	public void populate(final CisSubscriptionData source, final SubscriptionData target) throws ConversionException
	{
		Assert.notNull(target, "Parameter target cannot be null.");
		Assert.notNull(source, "Parameter source cannot be null.");

		final ProductData productData = getProductForSubscription(source);

		target.setBillingFrequency(source.getBillingFrequency());
		target.setBillingsystemId(source.getBillingSystemId());
		target.setCancellable(source.getCancellationPossible());
		target.setCancelledDate(source.getCancelDate());
		target.setContractDuration(getContractDuration(source, productData));
		target.setContractFrequency(getContractFrequency(source, productData));
		target.setCustomerId(source.getMerchantAccountId());
		target.setDescription(source.getSubscriptionDescription());
		target.setId(source.getSubscriptionId());
		target.setName(source.getSubscriptionName());

		if (StringUtils.isBlank(source.getSubscriptionOrderEntryId()))
		{
			LOG.warn("Source subscription does not have an order entry id");
		}
		else
		{
			target.setOrderEntryNumber(Integer.valueOf(source.getSubscriptionOrderEntryId()));
		}

		target.setOrderNumber(source.getSubscriptionOrderId());
		target.setPlacedOn(source.getOrderDate());
		target.setProductCode(source.getSubscriptionProductId());
		target.setRenewalType(getSubscriptionRenewalType(source, productData));
		target.setStartDate(source.getSubscriptionStartDate());
		target.setEndDate(getSubscriptionEndDate(source, productData));
		target.setSubscriptionStatus(source.getSubscriptionStatus());

		if (source.getPaymentMethod() == null)
		{
			LOG.warn("Source subscription does not have a payment method");
		}
		else
		{
			target.setPaymentMethodId(source.getPaymentMethod().getMerchantPaymentMethodId());
		}

		if (productData == null)
		{
			LOG.warn("Product may not be null. Cannot set product URL");
		}
		else
		{
			target.setProductUrl(productData.getUrl());
		}
	}

	/**
	 * Tries to find {@link ProductData} in hybris for the subscription product in the given cis subscription
	 * 
	 * @param cisSubscription
	 * @return {@link ProductData} or null
	 */
	protected ProductData getProductForSubscription(final CisSubscriptionData cisSubscription)
	{
		ProductData productData = null;

		if (StringUtils.isNotEmpty(cisSubscription.getSubscriptionOrderId())
				&& cisSubscription.getSubscriptionOrderEntryId() != null)
		{
			final AbstractOrderEntryModel orderEntry = getSubscriptionFacade().getOrderEntryForOrderCodeAndEntryNumber(
					cisSubscription.getSubscriptionOrderId(),
					Integer.valueOf(cisSubscription.getSubscriptionOrderEntryId()));

			if (orderEntry != null)
			{
				try {
					productData = getSubscriptionXStreamAliasConverter()
							.getSubscriptionProductDataFromXml(orderEntry.getXmlProduct());
				} catch (ClassCastException e) {
					LOG.error("Broken stored product: incorrect object class in XML");
				}
			}
		}

		if (productData == null && StringUtils.isNotEmpty(cisSubscription.getSubscriptionProductId()))
		{
			try
			{
				productData = getProductFacade().getProductForCodeAndOptions(cisSubscription.getSubscriptionProductId(),
						Arrays.asList(ProductOption.BASIC, ProductOption.PRICE));
			}
			catch (final UnknownIdentifierException e)
			{
				LOG.warn(String.format("Product with code '%s' not found.", cisSubscription.getSubscriptionProductId()), e);
			}
			catch (final AmbiguousIdentifierException e)
			{
				LOG.info("Ambiguous product code '" + cisSubscription.getSubscriptionProductId()
						+ "'. It's ok if called by cscockpit.");
			}
		}

		if (productData == null)
		{
			LOG.warn(String
					.format(
							"Cannot find product data for subscription '%s' as either order id|order entry id|product id are empty or this data does not exist (any more) in the hybris database",
							cisSubscription.getSubscriptionId()));
		}

		return productData;
	}

	protected Date getSubscriptionEndDate(final CisSubscriptionData source, final ProductData productData)
	{
		if (source.getSubscriptionEndDate() == null && productData != null)
		{
			return getSubscriptionFacade().getSubscriptionEndDate(productData, source.getSubscriptionStartDate());
		}
		return source.getSubscriptionEndDate();
	}

	protected String getSubscriptionRenewalType(final CisSubscriptionData source, final ProductData productData)
	{
		if (source.getAutoRenewal() == null && productData != null && productData.getSubscriptionTerm() != null
				&& productData.getSubscriptionTerm().getTermOfServiceRenewal() != null)
		{
			return productData.getSubscriptionTerm().getTermOfServiceRenewal().getName();
		}
		return source.getAutoRenewal() == null ? StringUtils.EMPTY : source.getAutoRenewal().toString();
	}

	protected Integer getContractDuration(final CisSubscriptionData source, final ProductData productData)
	{
		if (StringUtils.isEmpty(source.getContractDuration()) && productData != null && productData.getSubscriptionTerm() != null)
		{
			return Integer.valueOf(productData.getSubscriptionTerm().getTermOfServiceNumber());
		}

		try
		{
			final Scanner in = new Scanner(source.getContractDuration()).useDelimiter("[^0-9]+");
			final int contractDuration = in.nextInt();
			in.close();
			return Integer.valueOf(contractDuration);
		}
		catch (final InputMismatchException e)
		{
			LOG.error(String.format("Cannot convert contract duration '%s' to an integer", source.getContractDuration()));
		}
		return null;
	}

	protected String getContractFrequency(final CisSubscriptionData source, final ProductData productData)
	{
		if (productData != null && productData.getSubscriptionTerm() != null
				&& productData.getSubscriptionTerm().getTermOfServiceFrequency() != null)
		{
			return productData.getSubscriptionTerm().getTermOfServiceFrequency().getName();
		}
		return source.getSubscriptionDescription();
	}

	protected ProductFacade getProductFacade()
	{
		return productFacade;
	}

	@Required
	public void setProductFacade(final ProductFacade productFacade)
	{
		this.productFacade = productFacade;
	}

	protected SubscriptionFacade getSubscriptionFacade()
	{
		return subscriptionFacade;
	}

	@Required
	public void setSubscriptionFacade(final SubscriptionFacade subscriptionFacade)
	{
		this.subscriptionFacade = subscriptionFacade;
	}

	protected SubscriptionXStreamAliasConverter getSubscriptionXStreamAliasConverter()
	{
		return subscriptionXStreamAliasConverter;
	}

	@Required
	public void setSubscriptionXStreamAliasConverter(final SubscriptionXStreamAliasConverter subscriptionXStreamAliasConverter)
	{
		this.subscriptionXStreamAliasConverter = subscriptionXStreamAliasConverter;
	}
}

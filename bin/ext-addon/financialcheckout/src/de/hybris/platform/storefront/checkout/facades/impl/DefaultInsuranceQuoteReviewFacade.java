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
package de.hybris.platform.storefront.checkout.facades.impl;

import de.hybris.platform.commercefacades.insurance.data.InsuranceBenefitData;
import de.hybris.platform.commercefacades.insurance.data.InsuranceCoverageData;
import de.hybris.platform.commercefacades.insurance.data.InsuranceQuoteReviewData;
import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.financialfacades.facades.InsuranceQuoteFacade;
import de.hybris.platform.financialfacades.populators.InsuranceQuoteReviewDetailsPopulator;
import de.hybris.platform.storefront.checkout.facades.InsuranceQuoteReviewFacade;
import de.hybris.platform.subscriptionfacades.data.OneTimeChargeEntryData;
import de.hybris.platform.subscriptionfacades.data.SubscriptionPricePlanData;
import de.hybris.platform.xyformsfacades.data.YFormDataData;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Required;

import com.google.common.collect.Lists;


/**
 * The class of DefaultInsuranceQuoteReviewFacade.
 */
public class DefaultInsuranceQuoteReviewFacade implements InsuranceQuoteReviewFacade
{

	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(DefaultInsuranceQuoteReviewFacade.class);

	private String datetimeFormat = "dd-MM-yyyy";

	private CartFacade cartFacade;
	private InsuranceQuoteFacade insuranceQuoteFacade;

	private InsuranceQuoteReviewDetailsPopulator insuranceQuoteReviewDetailsPopulator;

	protected static final String BILLING_EVENT_PAY_NOW = "paynow";

	@Override
	public List<InsuranceQuoteReviewData> getInsuranceQuoteReviews()
	{
		final List<InsuranceQuoteReviewData> quoteReviewDataList = Lists.newArrayList();
		if (getCartFacade().hasSessionCart() && getCartFacade().hasEntries())
		{
			final CartData sessionCart = getCartFacade().getSessionCart();

			if (sessionCart.getEntries().get(0) != null)
			{
				final OrderEntryData entryData = sessionCart.getEntries().get(0);

				final InsuranceQuoteReviewData quoteReviewData = new InsuranceQuoteReviewData();

				final ProductData product = entryData.getProduct();

				quoteReviewData.setWorkFlowType(getInsuranceQuoteFacade().getQuoteWorkflowType());

				final InsuranceCoverageData mainCoverageData = new InsuranceCoverageData();
				mainCoverageData.setCoverageProduct(product);

				final List<InsuranceCoverageData> optionalCoverages = Lists.newLinkedList();

				quoteReviewData.setMainProduct(mainCoverageData);
				quoteReviewData.setOptionalProducts(optionalCoverages);
				quoteReviewData.setQuoteId(getMockQuoteId(sessionCart));
				quoteReviewData.setQuoteExpires(getMockQuoteExpires());

				final List<YFormDataData> formDataData = (List<YFormDataData>) entryData.getFormDataData();
				if (CollectionUtils.isNotEmpty(formDataData))
				{
					getInsuranceQuoteReviewDetailsPopulator().populate(entryData, quoteReviewData);
				}
				quoteReviewDataList.add(quoteReviewData);

				populateMainCoverages(product, mainCoverageData);
				populateExtraCoverages(sessionCart, optionalCoverages);
			}
		}
		return quoteReviewDataList;
	}

	/**
	 * Populate the extra coverages for the quote.
	 *
	 * @param cart
	 *           cartData
	 * @param optionalCoverages
	 *           optional coverages
	 */
	protected void populateExtraCoverages(final CartData cart, final List<InsuranceCoverageData> optionalCoverages)
	{
		if (cart.getEntries().size() > 1)
		{
			boolean skipFirst = true;
			for (final OrderEntryData oeData : cart.getEntries())
			{
				if (skipFirst)
				{
					skipFirst = false;
				}
				else
				{
					/*
					 * Note : the cart contains : - the base product - all options so we need to identify which of the option
					 * we have selected. We do this by checking the 'removable' status flag for the entry item.
					 * 
					 * Note 2 : Do not sort this list as it needs to come out in product order (sorting removed for
					 * INSA-906/907)
					 */
					if (oeData.isRemoveable())
					{
						final InsuranceCoverageData extraCoverageData = new InsuranceCoverageData();
						extraCoverageData.setCoverageProduct(oeData.getProduct());
						optionalCoverages.add(extraCoverageData);
					}
				}
			}
		}
	}

	/**
	 * Populate main product coverages.
	 *
	 * @param product
	 *           productData
	 * @param mainCoverageData
	 *           main coverage data
	 */
	protected void populateMainCoverages(final ProductData product, final InsuranceCoverageData mainCoverageData)
	{
		final PriceData priceData = product.getPrice();
		if (priceData instanceof SubscriptionPricePlanData)
		{
			final SubscriptionPricePlanData pricePlanData = (SubscriptionPricePlanData) priceData;
			final List<OneTimeChargeEntryData> oneTimeChargeEntries = pricePlanData.getOneTimeChargeEntries();

			if (oneTimeChargeEntries != null && oneTimeChargeEntries.size() > 0)
			{
				final List<InsuranceBenefitData> benefits = Lists.newArrayList();

				for (final OneTimeChargeEntryData entry : oneTimeChargeEntries)
				{
					final InsuranceBenefitData benefitData = new InsuranceBenefitData();

					if (!BILLING_EVENT_PAY_NOW.equals(entry.getBillingTime().getCode()))
					{
						benefitData.setName(entry.getBillingTime().getName());
						benefits.add(benefitData);
					}

				}
				Collections.sort(benefits, new Comparator<InsuranceBenefitData>()
				{
					@Override
					public int compare(final InsuranceBenefitData o1, final InsuranceBenefitData o2)
					{
						if (o1.getName() == null || o2.getName() == null)
						{
							return 0;
						}

						return o1.getName().compareTo(o2.getName());
					}
				});
				mainCoverageData.setBenefits(benefits);
			}
		}
	}

	protected String getMockQuoteId(final CartData sessionCart)
	{
		return sessionCart.getCode();
	}

	protected String getMockQuoteExpires()
	{
		final DateTime expires = DateTime.now().plus(Days.days(30));

		final SimpleDateFormat sdf = new SimpleDateFormat(getDatetimeFormat());
		return sdf.format(expires.toDate());
	}

	protected CartFacade getCartFacade()
	{
		return cartFacade;
	}

	@Required
	public void setCartFacade(final CartFacade cartFacade)
	{
		this.cartFacade = cartFacade;
	}

	protected String getDatetimeFormat()
	{
		return datetimeFormat;
	}

	public void setDatetimeFormat(final String datetimeFormat)
	{
		this.datetimeFormat = datetimeFormat;
	}

	protected InsuranceQuoteReviewDetailsPopulator getInsuranceQuoteReviewDetailsPopulator()
	{
		return insuranceQuoteReviewDetailsPopulator;
	}

	@Required
	public void setInsuranceQuoteReviewDetailsPopulator(
			final InsuranceQuoteReviewDetailsPopulator insuranceQuoteReviewDetailsPopulator)
	{
		this.insuranceQuoteReviewDetailsPopulator = insuranceQuoteReviewDetailsPopulator;
	}

	public InsuranceQuoteFacade getInsuranceQuoteFacade()
	{
		return insuranceQuoteFacade;
	}

	@Required
	public void setInsuranceQuoteFacade(final InsuranceQuoteFacade insuranceQuoteFacade)
	{
		this.insuranceQuoteFacade = insuranceQuoteFacade;
	}
}

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
package de.hybris.platform.financialfacades.populators;

import de.hybris.platform.commercefacades.insurance.data.InsuranceBenefitData;
import de.hybris.platform.commercefacades.insurance.data.InsuranceCoverageData;
import de.hybris.platform.commercefacades.insurance.data.InsurancePolicyData;
import de.hybris.platform.commercefacades.order.data.AbstractOrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.quotation.InsuranceQuoteData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.subscriptionfacades.data.BillingTimeData;
import de.hybris.platform.subscriptionfacades.data.OneTimeChargeEntryData;
import de.hybris.platform.subscriptionfacades.data.SubscriptionPricePlanData;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;


/**
 * The class of InsuranceOrderPolicyPopulator.
 */
public class InsuranceOrderPolicyPopulator implements Populator<OrderModel, AbstractOrderData>
{
	protected final String PAYNOW = "paynow";

	private InsurancePolicyDetailsPopulator insurancePolicyDetailsPopulator;

	/**
	 * Populate the target instance with values from the source instance.
	 *
	 * @param orderModel
	 *           the source object
	 * @param orderData
	 *           the target to fill
	 */
	@Override
	public void populate(final OrderModel orderModel, final AbstractOrderData orderData)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("orderModel", orderModel);
		ServicesUtil.validateParameterNotNullStandardMessage("orderData", orderData);

		final List<InsurancePolicyData> insurancePolicyDataList = Lists.newArrayList();

		final Map<Integer, List<OrderEntryData>> groupedOrderEntriesMap = groupOrderEntryDataByBundle(orderData);

		if (MapUtils.isNotEmpty(groupedOrderEntriesMap))
		{
			for (final Map.Entry<Integer, List<OrderEntryData>> orderEntries : groupedOrderEntriesMap.entrySet())
			{
				final InsurancePolicyData policyData = new InsurancePolicyData();

				addBasicInfo(orderData, policyData);
				addWhatsIncluded(orderEntries.getValue(), policyData);
				addOptionalExtras(orderEntries.getValue(), policyData);
				addPolicyDetails(orderEntries.getValue(), policyData);
				addDefaultCategoryDetails(policyData);

				insurancePolicyDataList.add(policyData);
			}

		}

		orderData.setInsurancePolicy(insurancePolicyDataList);
	}

	protected void addBasicInfo(final AbstractOrderData orderData, final InsurancePolicyData policyData)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("orderData", orderData);
		policyData.setPolicyNumber(orderData.getCode());

		final InsuranceQuoteData insuranceQuoteData = orderData.getInsuranceQuote();

		if (insuranceQuoteData != null)
		{
			if (insuranceQuoteData.getTripStartDate() != null)
			{
				policyData.setPolicyStartDate(insuranceQuoteData.getTripStartDate());
			}
			else if (insuranceQuoteData.getPropertyStartDate() != null)
			{
				policyData.setPolicyStartDate(insuranceQuoteData.getPropertyStartDate());
			}
			else if (insuranceQuoteData.getAutoDetail() != null && insuranceQuoteData.getAutoDetail().getAutoCoverStart() != null)
			{
				policyData.setPolicyStartDate(insuranceQuoteData.getAutoDetail().getAutoFormattedCoverStart());
			}
			policyData.setPolicyEndDate(insuranceQuoteData.getTripEndDate());
			policyData.setAddressLine1(insuranceQuoteData.getPropertyAddressLine1());
			policyData.setPropertyCoverRequired(insuranceQuoteData.getPropertyCoverRequired());
			policyData.setPropertyType(insuranceQuoteData.getPropertyType());
			policyData.setPropertyValue(insuranceQuoteData.getPropertyValue());
			policyData.setAutoDetail(insuranceQuoteData.getAutoDetail());
			policyData.setLifeDetail(insuranceQuoteData.getLifeDetail());
		}
		policyData.setOneTimePrice(orderData.getTotalPrice());

	}

	protected void addPolicyDetails(final List<OrderEntryData> orderEntries, final InsurancePolicyData policyData)
	{
		for (final OrderEntryData orderEntry : orderEntries)
		{
			getInsurancePolicyDetailsPopulator().populate(orderEntry, policyData);
		}
	}

	protected void addDefaultCategoryDetails(final InsurancePolicyData policyData)
	{
		final ProductData coverageProduct = policyData.getMainProduct() == null ? null : policyData.getMainProduct()
				.getCoverageProduct();
		if (coverageProduct != null)
		{
			policyData.setCategoryData(coverageProduct.getDefaultCategory());
		}
	}

	protected void addOptionalExtras(final List<OrderEntryData> entries, final InsurancePolicyData policyData)
	{
		if (CollectionUtils.isNotEmpty(entries) && policyData != null)
		{
			final List<InsuranceCoverageData> optionalCoverages = Lists.newLinkedList();
			policyData.setOptionalProducts(optionalCoverages);

			final int size = entries.size();
			for (int i = 1; i < size; i++)
			{
				final OrderEntryData entryData = entries.get(i);
				if (entryData != null && entryData.getProduct() != null)
				{
					final ProductData productData = entryData.getProduct();
					if (StringUtils.isNotEmpty(productData.getName()))
					{
						final InsuranceCoverageData optionalCoverage = new InsuranceCoverageData();
						optionalCoverage.setCoverageProduct(productData);
						optionalCoverages.add(optionalCoverage);
					}
				}
			}

			Collections.sort(optionalCoverages, new Comparator<InsuranceCoverageData>()
			{
				@Override
				public int compare(final InsuranceCoverageData o1, final InsuranceCoverageData o2)
				{
					if (o1.getCoverageProduct() == null || o1.getCoverageProduct().getName() == null
							|| o2.getCoverageProduct() == null || o2.getCoverageProduct().getName() == null)
					{
						return 0;
					}
					return o1.getCoverageProduct().getName().compareTo(o2.getCoverageProduct().getName());
				}
			});
		}
	}

	protected void addWhatsIncluded(final List<OrderEntryData> entries, final InsurancePolicyData policyData)
	{
		if (CollectionUtils.isNotEmpty(entries) && policyData != null)
		{
			final InsuranceCoverageData coverageData = new InsuranceCoverageData();
			policyData.setMainProduct(coverageData);

			final ProductData productData = entries.get(0).getProduct();
			if (productData != null)
			{
				coverageData.setCoverageProduct(productData);
				final List<InsuranceBenefitData> benefits = Lists.newLinkedList();

				if (productData.getPrice() instanceof SubscriptionPricePlanData)
				{
					final SubscriptionPricePlanData pricePlanData = (SubscriptionPricePlanData) productData.getPrice();
					final List<OneTimeChargeEntryData> oneTimeChargeEntries = pricePlanData.getOneTimeChargeEntries();

					if (oneTimeChargeEntries != null && oneTimeChargeEntries.size() > 0)
					{
						for (final OneTimeChargeEntryData entry : oneTimeChargeEntries)
						{
							final BillingTimeData billingTime = entry.getBillingTime();
							if (billingTime != null && !PAYNOW.contains(billingTime.getCode()))
							{
								if (StringUtils.isNotEmpty(billingTime.getName()))
								{
									final InsuranceBenefitData benefit = new InsuranceBenefitData();
									benefit.setName(billingTime.getName());
									benefit.setDescription(billingTime.getDescription());
									if (entry.getPrice() != null)
									{
										benefit.setCoverageValue(entry.getPrice().getFormattedValue());
									}
									benefits.add(benefit);
								}
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
						coverageData.setBenefits(benefits);
					}
				}
			}
		}
	}

	protected Map<Integer, List<OrderEntryData>> groupOrderEntryDataByBundle(final AbstractOrderData orderData)
	{
		final Map<Integer, List<OrderEntryData>> groupedOrderEntries = Maps.newHashMap();

		if (CollectionUtils.isNotEmpty(orderData.getEntries()))
		{
			for (final OrderEntryData entryData : orderData.getEntries())
			{
				final int bundleNo = entryData.getBundleNo();
				if (groupedOrderEntries.containsKey(bundleNo))
				{
					groupedOrderEntries.get(bundleNo).add(entryData);
				}
				else
				{
					final LinkedList<OrderEntryData> entries = Lists.newLinkedList();
					entries.add(entryData);
					groupedOrderEntries.put(bundleNo, entries);
				}
			}
		}

		return groupedOrderEntries;
	}

	protected InsurancePolicyDetailsPopulator getInsurancePolicyDetailsPopulator()
	{
		return insurancePolicyDetailsPopulator;
	}

	@Required
	public void setInsurancePolicyDetailsPopulator(final InsurancePolicyDetailsPopulator insurancePolicyDetailsPopulator)
	{
		this.insurancePolicyDetailsPopulator = insurancePolicyDetailsPopulator;
	}
}

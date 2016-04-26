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
package de.hybris.platform.financialacceleratorstorefront.comparison;

import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.configurablebundlefacades.data.BundleTemplateData;
import de.hybris.platform.subscriptionfacades.data.BillingTimeData;
import de.hybris.platform.subscriptionfacades.data.OneTimeChargeEntryData;
import de.hybris.platform.subscriptionfacades.data.RecurringChargeEntryData;
import de.hybris.platform.subscriptionfacades.data.SubscriptionPricePlanData;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

import com.google.common.collect.Lists;


/**
 * The class of InsuranceComparisonTableFactory. It creates a coverage item (OneTimePriceEntry) comparison table based
 * on search result Product Data
 */
public class InsuranceComparisonTableFactory implements ComparisonTableFactory
{

	private Comparator<BillingTimeData> billingTimeDataComparator;
	private Set<String> excludableBillingEvents;
	public static final String LABEL_COLUMN = "label";
	private final Map<String, ProductData> fullProductData = new LinkedHashMap<String, ProductData>();
	protected static final String RECURRING_CHARGE_ANNUAL_PRICE = "recurringAnnualPrice";
	protected static final String ANNUAL_SAVING_PRICE = "annualSavingPrice";
	protected static final String DEFAULT_CURRENT_ISO = "USD";
	protected static final int NUM_OF_MONTH = 12;
	private PriceDataFactory priceFactory;


	/**
	 * Creates a Product comparison table based on the searchPageData
	 *
	 * @param searchPageData
	 */
	@Override
	public ComparisonTable createTable(final SearchPageData<ProductData> searchPageData)
	{
		final ComparisonTable comparisonTable = new ComparisonTable();

		final Set<BillingTimeData> referenceDataSet = new ConcurrentSkipListSet<BillingTimeData>(getBillingTimeDataComparator());

		final Map<ProductData, Map<String, OneTimeChargeEntryData>> oneTimeChargeEntyrMap = new HashMap<ProductData, Map<String, OneTimeChargeEntryData>>();

		final List<ProductData> results = getMainProducts(searchPageData);

		setFullProductDataMap(searchPageData);

		collectBillingEvents(referenceDataSet, oneTimeChargeEntyrMap, results);

		setUpLabelColumn(comparisonTable, referenceDataSet);

		createCoverageItemColumns(comparisonTable, referenceDataSet, oneTimeChargeEntyrMap, results);

		return comparisonTable;
	}

	/**
	 * Creates comparison columns for each products
	 *
	 * @param table
	 * @param billingTimeDataSet
	 * @param dataMap
	 * @param results
	 */
	protected void createCoverageItemColumns(final ComparisonTable table, final Set<BillingTimeData> billingTimeDataSet,
			final Map<ProductData, Map<String, OneTimeChargeEntryData>> dataMap, final List<ProductData> results)
	{
		boolean calculateOtherprice = false;

		for (final ProductData productData : results)
		{
			final ComparisonTableColumn column = new ComparisonTableColumn();

			// If the product has recurringCharge then toggle the flag calculateOtherprice to calculate the price difference
			if (isRecurringPrice((SubscriptionPricePlanData) productData.getPrice()))
			{
				calculateOtherprice = true;
			}

			final Map<String, OneTimeChargeEntryData> map = dataMap.get(productData);

			for (final BillingTimeData aBillingTimeDataSet : billingTimeDataSet)
			{
				final OneTimeChargeEntryData oneTimeChargeEntryData = map.get(aBillingTimeDataSet.getCode());

				column.addItem(oneTimeChargeEntryData);
			}

			table.addColumn(getFullProductData(productData.getCode()), column);

		}

		if (calculateOtherprice)
		{
			calculateOtherPrice(table);
		}

	}


	/**
	 * Helper method to find whether the {@link SubscriptionPricePlanData} has any RecurringCharges
	 *
	 * @param priceData
	 * @return true if it has RecurringCharges other wise false
	 */
	protected boolean isRecurringPrice(final SubscriptionPricePlanData priceData)
	{

		boolean isRecurringPrice = false;

		if (CollectionUtils.isNotEmpty(priceData.getRecurringChargeEntries()))
		{
			isRecurringPrice = true;
		}

		return isRecurringPrice;
	}

	/**
	 * Helper method to calculate the price difference between pay-monthly vs pay-annual (paynow)
	 *
	 * @param table
	 */
	protected void calculateOtherPrice(final ComparisonTable table)
	{
		Double annualRecurringPrice = 0d;
		Double annualPrice = 0d;

		String currencyIso = null;

		for (final Entry<Object, ComparisonTableColumn> data : table.getColumns().entrySet())
		{
			if (data.getKey() instanceof ProductData)
			{
				final ProductData annualProduct = (ProductData) data.getKey();
				final SubscriptionPricePlanData subscriptionPrice = (SubscriptionPricePlanData) annualProduct.getPrice();

				if (CollectionUtils.isNotEmpty(subscriptionPrice.getOneTimeChargeEntries()))
				{
					final OneTimeChargeEntryData oneTimeCharge = subscriptionPrice.getOneTimeChargeEntries().get(0);
					annualPrice = oneTimeCharge.getPrice().getValue().doubleValue();
				}

				if (CollectionUtils.isNotEmpty(subscriptionPrice.getRecurringChargeEntries()))
				{
					final RecurringChargeEntryData recurringCharge = subscriptionPrice.getRecurringChargeEntries().get(0);
					annualRecurringPrice = recurringCharge.getPrice().getValue().doubleValue() * NUM_OF_MONTH;
				}

				if (StringUtils.isEmpty(currencyIso))
				{
					currencyIso = subscriptionPrice.getCurrencyIso() == null ? DEFAULT_CURRENT_ISO : subscriptionPrice
							.getCurrencyIso();
				}
			}
		}

		if (annualRecurringPrice > 0)
		{
			bindPriceDifference(annualPrice, annualRecurringPrice, table, currencyIso);
		}

	}

	/**
	 * Helper method to bind the annualRecurringPrice and the price difference between annual vs paymonthly
	 *
	 * @param annualPrice
	 * @param annualRecurringPrice
	 * @param table
	 * @param currencyIso
	 */
	protected void bindPriceDifference(final Double annualPrice, final Double annualRecurringPrice, final ComparisonTable table,
			final String currencyIso)
	{
		final ComparisonTableColumn annualRecurringPriceDataColumn = new ComparisonTableColumn();
		final PriceData annualRecurringPriceData = getPriceFactory().create(PriceDataType.BUY,
				BigDecimal.valueOf(annualRecurringPrice), currencyIso);
		annualRecurringPriceDataColumn.addItem(annualRecurringPriceData.getFormattedValue());
		table.addColumn(RECURRING_CHARGE_ANNUAL_PRICE, annualRecurringPriceDataColumn);

		final ComparisonTableColumn savingPriceDataColumn = new ComparisonTableColumn();
		final PriceData savingPriceData = getPriceFactory().create(PriceDataType.BUY,
				BigDecimal.valueOf(annualRecurringPrice - annualPrice), currencyIso);

		savingPriceDataColumn.addItem(savingPriceData.getFormattedValue());
		table.addColumn(ANNUAL_SAVING_PRICE, savingPriceDataColumn);

	}

	/**
	 * Creates a label column from the BillingTimeData names.
	 *
	 * @param table
	 * @param billingTimeDataSet
	 */
	protected void setUpLabelColumn(final ComparisonTable table, final Set<BillingTimeData> billingTimeDataSet)
	{
		final ComparisonTableColumn labelColumn = new ComparisonTableColumn();

		final Iterator<BillingTimeData> labelIterator = billingTimeDataSet.iterator();

		while (labelIterator.hasNext())
		{
			labelColumn.addItem(labelIterator.next());
		}

		table.addColumn(LABEL_COLUMN, labelColumn);
	}

	/**
	 * Collects all the BillingEvents from the results ProductData list
	 *
	 * @param billingTimeDataSet
	 * @param dataMap
	 * @param results
	 */
	protected void collectBillingEvents(final Set<BillingTimeData> billingTimeDataSet,
			final Map<ProductData, Map<String, OneTimeChargeEntryData>> dataMap, final List<ProductData> results)
	{
		for (final ProductData productData : results)
		{

			final Map<String, OneTimeChargeEntryData> map = new HashMap<String, OneTimeChargeEntryData>();


			for (final OneTimeChargeEntryData oneTimeChargeEntryData : ((SubscriptionPricePlanData) productData.getPrice())
					.getOneTimeChargeEntries())
			{
				final BillingTimeData billingTime = oneTimeChargeEntryData.getBillingTime();
				final String code = billingTime.getCode();

				if (validBillingEvent(code))
				{

					billingTimeDataSet.add(billingTime);

					map.put(code, oneTimeChargeEntryData);
				}
			}

			dataMap.put(productData, map);
		}
	}

	protected boolean validBillingEvent(final String billingEventCode)
	{
		final Set<String> excludableBillingEvents = getExcludableBillingEvents();

		return excludableBillingEvents == null || !excludableBillingEvents.contains(billingEventCode);
	}

	protected List<ProductData> getMainProducts(final SearchPageData<ProductData> searchPageData)
	{
		List<ProductData> productDataList = Lists.newArrayList();
		if (searchPageData != null && CollectionUtils.isNotEmpty(searchPageData.getResults())
				&& searchPageData.getResults().get(0) != null
				&& CollectionUtils.isNotEmpty(searchPageData.getResults().get(0).getBundleTemplates())
				&& searchPageData.getResults().get(0).getBundleTemplates().get(0) != null
				&& CollectionUtils.isNotEmpty(searchPageData.getResults().get(0).getBundleTemplates().get(0).getProducts()))
		{

			productDataList = getMainProductsFromDifferentBundles(searchPageData.getResults());

			CollectionUtils.filter(productDataList, new Predicate()
			{
				@Override
				public boolean evaluate(final Object o)
				{
					if (o instanceof ProductData)
					{
						for (final ProductData searchedProduct : searchPageData.getResults())
						{
							if (searchedProduct.getCode().equals(((ProductData) o).getCode()))
							{
								return true;
							}
						}
					}
					return false;
				}
			});

		}

		return productDataList;
	}

	/**
	 * Method used to extract only the main products and eliminates the optional products for the given list of
	 * ProductData
	 *
	 * @param searchData
	 * @return list of ProductData contains main products
	 */
	protected List<ProductData> getMainProductsFromDifferentBundles(final List<ProductData> searchData)
	{
		String bundleId = StringUtils.EMPTY;

		final List<ProductData> productDataList = Lists.newArrayList();

		for (final ProductData productData : searchData)
		{
			if (CollectionUtils.isNotEmpty(productData.getBundleTemplates()))
			{
				final BundleTemplateData bundleData = productData.getBundleTemplates().get(0);
				if (!bundleId.equals(bundleData.getId()))
				{
					bundleId = bundleData.getId();
					productDataList.addAll(bundleData.getProducts());

				}
			}

		}

		return productDataList;

	}

	protected void setFullProductDataMap(final SearchPageData<ProductData> searchPageData)
	{
		for (final ProductData data : searchPageData.getResults())
		{
			fullProductData.put(data.getCode(), data);
		}
	}

	protected ProductData getFullProductData(final String productCode)
	{
		return fullProductData.get(productCode);
	}

	protected Comparator<BillingTimeData> getBillingTimeDataComparator()
	{
		return billingTimeDataComparator;
	}

	@Required
	public void setBillingTimeDataComparator(final Comparator<BillingTimeData> billingTimeDataComparator)
	{
		this.billingTimeDataComparator = billingTimeDataComparator;
	}

	protected Set<String> getExcludableBillingEvents()
	{
		return excludableBillingEvents;
	}

	public void setExcludableBillingEvents(final Set<String> excludableBillingEvents)
	{
		this.excludableBillingEvents = excludableBillingEvents;
	}

	protected PriceDataFactory getPriceFactory()
	{
		return priceFactory;
	}

	@Required
	public void setPriceFactory(final PriceDataFactory priceFactory)
	{
		this.priceFactory = priceFactory;
	}
}

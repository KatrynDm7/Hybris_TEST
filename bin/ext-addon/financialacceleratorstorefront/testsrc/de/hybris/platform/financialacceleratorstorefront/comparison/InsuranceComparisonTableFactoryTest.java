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

import com.google.common.collect.Lists;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.configurablebundlefacades.data.BundleTemplateData;
import de.hybris.platform.financialacceleratorstorefront.comparison.ComparisonTable;
import de.hybris.platform.financialacceleratorstorefront.comparison.InsuranceComparisonTableFactory;
import de.hybris.platform.subscriptionfacades.data.BillingTimeData;
import de.hybris.platform.subscriptionfacades.data.OneTimeChargeEntryData;
import de.hybris.platform.subscriptionfacades.data.SubscriptionPricePlanData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@UnitTest
public class InsuranceComparisonTableFactoryTest
{

	@Mock
	private Comparator<BillingTimeData> billingTimeDataComparator;

	@InjectMocks
	private InsuranceComparisonTableFactory factory;

	@Before
	public void setUp()
	{

		factory = new InsuranceComparisonTableFactory();

		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testCreateTable()
	{

		Mockito.doAnswer(new Answer()
		{

			@Override
			public Object answer(final InvocationOnMock invocation) throws Throwable
			{
				final BillingTimeData billingTimeData1 = (BillingTimeData) invocation.getArguments()[0];
				final BillingTimeData billingTimeData2 = (BillingTimeData) invocation.getArguments()[1];
				return Integer.valueOf(billingTimeData1.getCode().compareTo(billingTimeData2.getCode()));
			}
		}).when(billingTimeDataComparator).compare(Mockito.any(BillingTimeData.class), Mockito.any(BillingTimeData.class));

		final Set<String> excludableBillingEvents = new HashSet<String>();
		excludableBillingEvents.add("code6");

		factory.setExcludableBillingEvents(excludableBillingEvents);

		final BundleTemplateData bundleTemplateData = new BundleTemplateData();
		final ProductData comparableProduct1 = new ProductData();
		final ProductData comparableProduct2 = new ProductData();


		//Preparing comparableProduct1
		final SubscriptionPricePlanData comparableProduct1PlanData = new SubscriptionPricePlanData();
		final List<OneTimeChargeEntryData> comparableProduct1Charges = Lists.newArrayList();
		comparableProduct1Charges.add(createOneTimeChargeEntryData("product1Charge1", "code1"));
		comparableProduct1Charges.add(createOneTimeChargeEntryData("product1Charge2", "code2"));
		comparableProduct1Charges.add(createOneTimeChargeEntryData("product1Charge4", "code4"));
		comparableProduct1Charges.add(createOneTimeChargeEntryData("product1Charge6", "code6"));

		comparableProduct1PlanData.setOneTimeChargeEntries(comparableProduct1Charges);

		comparableProduct1.setPrice(comparableProduct1PlanData);

		//Preparing comparableProduct2
		final SubscriptionPricePlanData comparableProduct2PlanData = new SubscriptionPricePlanData();
		final List<OneTimeChargeEntryData> comparableProduct2Charges = Lists.newArrayList();
		comparableProduct2Charges.add(createOneTimeChargeEntryData("product2Charge1", "code1"));
		comparableProduct2Charges.add(createOneTimeChargeEntryData("product2Charge2", "code2"));
		comparableProduct2Charges.add(createOneTimeChargeEntryData("product2Charge5", "code5"));
		comparableProduct2Charges.add(createOneTimeChargeEntryData("product2Charge3", "code3"));
		comparableProduct2Charges.add(createOneTimeChargeEntryData("product2Charge6", "code6"));

		comparableProduct2PlanData.setOneTimeChargeEntries(comparableProduct2Charges);

		comparableProduct2.setPrice(comparableProduct2PlanData);

		final String product1_code = "productData1";
		final String product2_code = "productData2";
		comparableProduct1.setCode(product1_code);
		comparableProduct2.setCode(product2_code);
		bundleTemplateData.setProducts(Lists.newArrayList(comparableProduct1, comparableProduct2));

		final ProductData productData1 = new ProductData();
		final ProductData productData2 = new ProductData();

		productData1.setBundleTemplates(Lists.newArrayList(bundleTemplateData));

		final SearchPageData<ProductData> searchPageData = new SearchPageData<ProductData>();
		searchPageData.setResults(Lists.newArrayList(productData1, productData2));
		productData1.setCode(product1_code);
		productData2.setCode(product2_code);
		final ComparisonTable comparisonTable = factory.createTable(searchPageData);
		final List<BillingTimeData> labelColumnItems = comparisonTable.getColumns()
				.get(InsuranceComparisonTableFactory.LABEL_COLUMN).getItems();
		final List<OneTimeChargeEntryData> product1ColumnItems = comparisonTable.getColumns().get(productData1).getItems();
		final List<OneTimeChargeEntryData> product2ColumnItems = comparisonTable.getColumns().get(productData2).getItems();

		Assert.assertEquals(3, comparisonTable.getColumns().size());
		Assert.assertEquals(5, labelColumnItems.size());
		Assert.assertEquals(5, product1ColumnItems.size());
		Assert.assertEquals(5, product2ColumnItems.size());

		Assert.assertEquals("code1Name", labelColumnItems.get(0).getName());
		Assert.assertEquals("code2Name", labelColumnItems.get(1).getName());
		Assert.assertEquals("code3Name", labelColumnItems.get(2).getName());
		Assert.assertEquals("code4Name", labelColumnItems.get(3).getName());
		Assert.assertEquals("code5Name", labelColumnItems.get(4).getName());

		Assert.assertEquals("product1Charge1", product1ColumnItems.get(0).getName());
		Assert.assertEquals("product1Charge2", product1ColumnItems.get(1).getName());
		Assert.assertNull(product1ColumnItems.get(2));
		Assert.assertEquals("product1Charge4", product1ColumnItems.get(3).getName());
		Assert.assertNull(product1ColumnItems.get(4));

		Assert.assertEquals("product2Charge1", product2ColumnItems.get(0).getName());
		Assert.assertEquals("product2Charge2", product2ColumnItems.get(1).getName());
		Assert.assertEquals("product2Charge3", product2ColumnItems.get(2).getName());
		Assert.assertNull(product2ColumnItems.get(3));
		Assert.assertEquals("product2Charge5", product2ColumnItems.get(4).getName());

	}

	private OneTimeChargeEntryData createOneTimeChargeEntryData(final String name, final String billingEventCode)
	{

		final OneTimeChargeEntryData oneTimeChargeEntryData = new OneTimeChargeEntryData();
		oneTimeChargeEntryData.setName(name);

		final BillingTimeData billingTimeData = new BillingTimeData();
		billingTimeData.setName(billingEventCode + "Name");
		billingTimeData.setCode(billingEventCode);

		oneTimeChargeEntryData.setBillingTime(billingTimeData);

		return oneTimeChargeEntryData;
	}
}

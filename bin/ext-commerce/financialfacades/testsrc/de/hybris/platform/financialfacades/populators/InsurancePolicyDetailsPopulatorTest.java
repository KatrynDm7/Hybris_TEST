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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.hybris.platform.commercefacades.insurance.data.InsurancePolicyData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.financialfacades.populators.InsuranceQuoteReviewDetailsPopulator;
import de.hybris.platform.xyformsfacades.data.YFormDataData;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * The class of InsuranceQuoteReviewDetailsPopulatorTest.
 */
public class InsurancePolicyDetailsPopulatorTest
{

	private InsuranceQuoteReviewDetailsPopulator insuranceQuoteReviewDetailsPopulator;

	private Populator<YFormDataData, InsurancePolicyData> mockPopulatorA;

	private Populator<YFormDataData, InsurancePolicyData> mockPopulatorB;

	private List<Populator<YFormDataData, InsurancePolicyData>> populatorListA;
	private List<Populator<YFormDataData, InsurancePolicyData>> populatorListB;

	private Map<String, List<Populator<YFormDataData, InsurancePolicyData>>> populatorMap;

	private final String keyA = "KEY_A";
	private final String keyB = "KEY_B";

	@Before
	public void setup()
	{
		insuranceQuoteReviewDetailsPopulator = new InsuranceQuoteReviewDetailsPopulator();
		mockPopulatorA = Mockito.mock(Populator.class);
		mockPopulatorB = Mockito.mock(Populator.class);
		populatorListA = Lists.newArrayList();
		populatorListB = Lists.newArrayList();
		populatorListA.add(mockPopulatorA);
		populatorListB.add(mockPopulatorB);

		populatorMap = Maps.newHashMap();
		populatorMap.put(keyA, populatorListA);
		populatorMap.put(keyB, populatorListB);
		insuranceQuoteReviewDetailsPopulator.setDetailsPopulatorsMap(populatorMap);
	}

	@Test
	public void shouldRunPopulatorA()
	{
		OrderEntryData orderEntryData = new OrderEntryData();
		ProductData product = new ProductData();
		CategoryData defaultCategoryData = new CategoryData();
		defaultCategoryData.setCode(keyA);
		product.setDefaultCategory(defaultCategoryData);
		orderEntryData.setProduct(product);
		orderEntryData.setFormDataData(Arrays.asList(new YFormDataData()));

		insuranceQuoteReviewDetailsPopulator.populate(orderEntryData, new InsurancePolicyData());

		Mockito.verify(mockPopulatorA)
				.populate(Mockito.any(YFormDataData.class), Mockito.any(InsurancePolicyData.class));
		Mockito.verify(mockPopulatorB, Mockito.never()).populate(Mockito.any(YFormDataData.class),
				Mockito.any(InsurancePolicyData.class));
	}

	@Test
	public void shouldNotRunPopulators()
	{
		OrderEntryData orderEntryData = new OrderEntryData();
		ProductData product = new ProductData();
		CategoryData defaultCategoryData = new CategoryData();
		defaultCategoryData.setCode("any other key");
		product.setDefaultCategory(defaultCategoryData);
		orderEntryData.setProduct(product);
		orderEntryData.setFormDataData(Arrays.asList(new YFormDataData()));

		insuranceQuoteReviewDetailsPopulator.populate(orderEntryData, new InsurancePolicyData());

		Mockito.verify(mockPopulatorA, Mockito.never()).populate(Mockito.any(YFormDataData.class),
				Mockito.any(InsurancePolicyData.class));
		Mockito.verify(mockPopulatorB, Mockito.never()).populate(Mockito.any(YFormDataData.class),
				Mockito.any(InsurancePolicyData.class));

	}

	@Test
	public void shouldNotRunPopulatorIfNoYFormData()
	{
		OrderEntryData orderEntryData = new OrderEntryData();
		ProductData product = new ProductData();
		CategoryData defaultCategoryData = new CategoryData();
		defaultCategoryData.setCode(keyA);
		product.setDefaultCategory(defaultCategoryData);
		orderEntryData.setProduct(product);
		orderEntryData.setFormDataData(null);

		insuranceQuoteReviewDetailsPopulator.populate(orderEntryData, new InsurancePolicyData());

		Mockito.verify(mockPopulatorA, Mockito.never()).populate(Mockito.any(YFormDataData.class),
				Mockito.any(InsurancePolicyData.class));
		Mockito.verify(mockPopulatorB, Mockito.never()).populate(Mockito.any(YFormDataData.class),
				Mockito.any(InsurancePolicyData.class));
	}
}

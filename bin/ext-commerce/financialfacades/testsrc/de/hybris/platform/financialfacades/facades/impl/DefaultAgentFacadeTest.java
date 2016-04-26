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
package de.hybris.platform.financialfacades.facades.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.financialfacades.findagent.data.AgentData;
import de.hybris.platform.financialfacades.facades.impl.DefaultAgentFacade;
import de.hybris.platform.financialservices.model.AgentModel;
import de.hybris.platform.financialservices.services.AgentService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.given;

/**
 * Tests for the {@link DefaultAgentFacade}.
 */
@UnitTest
public class DefaultAgentFacadeTest
{

	private static final String UNEXISTING_CATEGORY_CODE = "testCategory";
	private static final String AGENT_UID = "testAgent";

	private static final String CODE_OF_CATEGORY_1 = "insurance_category_1";
	private static final String CODE_OF_CATEGORY_11 = "insurance_category_2";
	private static final String CODE_OF_CATEGORY_111 = "insurance_category_3";
	private static final String CODE_OF_CATEGORY_112 = "insurance_category_4";
	private static final String CODE_OF_CATEGORY_12 = "insurance_category_5";
	private static final String CODE_OF_CATEGORY_121 = "insurance_category_6";

	private static final String NULL_AGENT_ID_MESSAGE = "Agent id must not be null or empty";
	private static final String NULL_CATEGORY_CODE_MESSAGE = "Category code must not be null or empty";

	@Mock
	private CategoryModel insuranceCategory1;
	@Mock
	private CategoryModel insuranceCategory11;
	@Mock
	private CategoryModel insuranceCategory111;
	@Mock
	private CategoryModel insuranceCategory112;
	@Mock
	private CategoryModel insuranceCategory12;
	@Mock
	private CategoryModel insuranceCategory121;

	@Mock
	private CategoryData categoryData1;
	@Mock
	private CategoryData categoryData11;
	@Mock
	private CategoryData categoryData111;
	@Mock
	private CategoryData categoryData112;
	@Mock
	private CategoryData categoryData12;
	@Mock
	private CategoryData categoryData121;

	@Mock
	private AgentModel agentModel1;
	@Mock
	private AgentData agentData1;

	private DefaultAgentFacade agentFacade;

	private List<CategoryModel> subcategoriesOfCategory1;
	private List<CategoryModel> subcategoriesOfCategory11;
	private List<CategoryModel> subcategoriesOfCategory121;

	private List<AgentData> agentDataList;
	private List<AgentModel> agentModelList;
	private List<CategoryModel> agentCategoryModelList;
	private List<CategoryData> agentCategoryDataList;

	@Mock
	private CategoryService categoryService;
	@Mock
	private AgentService agentService;
	@Mock
	private Converter<CategoryModel, CategoryData> categoryConverter;
	@Mock
	private Converter<AgentModel, AgentData> agentConverter;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		agentFacade = new DefaultAgentFacade();
		agentFacade.setCategoryService(categoryService);
		agentFacade.setAgentService(agentService);
		agentFacade.setCategoryConverter(categoryConverter);
		agentFacade.setAgentConverter(agentConverter);

		given(insuranceCategory1.getCode()).willReturn(CODE_OF_CATEGORY_1);
		given(insuranceCategory11.getCode()).willReturn(CODE_OF_CATEGORY_11);
		given(insuranceCategory111.getCode()).willReturn(CODE_OF_CATEGORY_111);
		given(insuranceCategory112.getCode()).willReturn(CODE_OF_CATEGORY_112);
		given(insuranceCategory12.getCode()).willReturn(CODE_OF_CATEGORY_12);
		given(insuranceCategory121.getCode()).willReturn(CODE_OF_CATEGORY_121);

		subcategoriesOfCategory1 = Arrays.asList(insuranceCategory11, insuranceCategory12);
		subcategoriesOfCategory11 = Arrays.asList(insuranceCategory111, insuranceCategory112);
		subcategoriesOfCategory121 = Arrays.asList(insuranceCategory121);

		given(insuranceCategory1.getCategories()).willReturn(subcategoriesOfCategory1);
		given(insuranceCategory11.getCategories()).willReturn(subcategoriesOfCategory11);
		given(insuranceCategory111.getCategories()).willReturn(Collections.EMPTY_LIST);
		given(insuranceCategory112.getCategories()).willReturn(Collections.EMPTY_LIST);
		given(insuranceCategory12.getCategories()).willReturn(subcategoriesOfCategory121);
		given(insuranceCategory121.getCategories()).willReturn(Collections.EMPTY_LIST);

		given(categoryService.getCategoryForCode(CODE_OF_CATEGORY_1)).willReturn(insuranceCategory1);
		given(categoryService.getCategoryForCode(CODE_OF_CATEGORY_11)).willReturn(insuranceCategory11);
		given(categoryService.getCategoryForCode(CODE_OF_CATEGORY_111)).willReturn(insuranceCategory111);
		given(categoryService.getCategoryForCode(CODE_OF_CATEGORY_112)).willReturn(insuranceCategory112);
		given(categoryService.getCategoryForCode(CODE_OF_CATEGORY_12)).willReturn(insuranceCategory12);
		given(categoryService.getCategoryForCode(CODE_OF_CATEGORY_121)).willReturn(insuranceCategory121);

		given(categoryData1.getCode()).willReturn(CODE_OF_CATEGORY_1);
		given(categoryData11.getCode()).willReturn(CODE_OF_CATEGORY_11);
		given(categoryData111.getCode()).willReturn(CODE_OF_CATEGORY_111);
		given(categoryData112.getCode()).willReturn(CODE_OF_CATEGORY_112);
		given(categoryData12.getCode()).willReturn(CODE_OF_CATEGORY_12);
		given(categoryData121.getCode()).willReturn(CODE_OF_CATEGORY_121);

		given(categoryConverter.convert(insuranceCategory1)).willReturn(categoryData1);
		given(categoryConverter.convert(insuranceCategory11)).willReturn(categoryData11);
		given(categoryConverter.convert(insuranceCategory111)).willReturn(categoryData111);
		given(categoryConverter.convert(insuranceCategory112)).willReturn(categoryData112);
		given(categoryConverter.convert(insuranceCategory12)).willReturn(categoryData12);
		given(categoryConverter.convert(insuranceCategory121)).willReturn(categoryData121);

		agentCategoryModelList = Arrays.asList(insuranceCategory1, insuranceCategory11, insuranceCategory111, insuranceCategory112);
		agentCategoryDataList = Arrays.asList(categoryData1, categoryData11, categoryData111, categoryData112);

		given(agentData1.getUid()).willReturn(AGENT_UID);
		given(agentData1.getCategories()).willReturn(agentCategoryDataList);

		given(agentModel1.getUid()).willReturn(AGENT_UID);
		given(agentModel1.getCategories()).willReturn(agentCategoryModelList);

		agentModelList = Arrays.asList(agentModel1);
		agentDataList = Arrays.asList(agentData1);

		given(agentService.getAgentForCode(AGENT_UID)).willReturn(agentModel1);
		given(agentService.getAgentsByCategory(CODE_OF_CATEGORY_1)).willReturn(agentModelList);
		given(agentService.getAgentsByCategory(CODE_OF_CATEGORY_11)).willReturn(agentModelList);
		given(agentService.getAgentsByCategory(CODE_OF_CATEGORY_111)).willReturn(agentModelList);
		given(agentService.getAgentsByCategory(CODE_OF_CATEGORY_112)).willReturn(agentModelList);

		given(agentConverter.convert(agentModel1)).willReturn(agentData1);
	}

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void testGetAgentByNullUid()
	{
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage(NULL_AGENT_ID_MESSAGE);
		final AgentData agent = agentFacade.getAgentByUid(null);
	}

	@Test
	public void testGetAgentByEmptyUid()
	{
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage(NULL_AGENT_ID_MESSAGE);
		final AgentData agent = agentFacade.getAgentByUid("");
	}

	@Test
	public void testGetAgentByUid()
	{
		final AgentData agent = agentFacade.getAgentByUid(AGENT_UID);

		Assert.assertNotNull(agent);
		Assert.assertNotNull(agent.getUid());
		Assert.assertEquals("Wrong agent uid!", AGENT_UID, agent.getUid());
	}

	@Test
	public void testGetAgentsByCategory()
	{
		final Collection<AgentData> agents = agentFacade.getAgentsByCategory(CODE_OF_CATEGORY_1);

		Assert.assertNotNull(agents);
		Assert.assertTrue("Agents' list must not be empty!", !agents.isEmpty());
		Assert.assertEquals("Wrong agents count!", agentDataList.size(), agents.size());
		for(final AgentData currentAgent: agents)
		{
			Assert.assertNotNull(currentAgent);

			Assert.assertNotNull(currentAgent.getUid());
			Assert.assertNotNull(currentAgent.getCategories());
			Assert.assertTrue("Wrong categories count!",!currentAgent.getCategories().isEmpty());

			boolean isCategoryFound = false;
			for(final CategoryData currentCategory: currentAgent.getCategories())
			{
				Assert.assertNotNull(currentCategory);
				Assert.assertNotNull(currentCategory.getCode());
				if(CODE_OF_CATEGORY_1.equals(currentCategory.getCode()))
				{
					isCategoryFound = true;
				}
			}
			Assert.assertTrue("No given category!", isCategoryFound);
		}
	}

	@Test
	public void testGetAgentsByNullCategory()
	{
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage(NULL_CATEGORY_CODE_MESSAGE);
		agentFacade.getAgentsByCategory(null);
	}

	@Test
	public void testGetAgentsByEmptyCategory()
	{
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage(NULL_CATEGORY_CODE_MESSAGE);
		agentFacade.getAgentsByCategory("");
	}

	@Test
	public void testGetCategoriesByWrongRootCode()
	{
		given(categoryService.getCategoryForCode(UNEXISTING_CATEGORY_CODE)).willThrow(
				new UnknownIdentifierException("Category with code '" + UNEXISTING_CATEGORY_CODE + "' not found! "));

		thrown.expect(UnknownIdentifierException.class);
		thrown.expectMessage("Category with code '" + UNEXISTING_CATEGORY_CODE + "' not found! ");
		agentFacade.getCategories(UNEXISTING_CATEGORY_CODE);
	}

	@Test
	public void testGetCategoriesByEmptyRootCode()
	{
		given(categoryService.getCategoryForCode("")).willThrow(
				new UnknownIdentifierException("Category with code '' not found! "));

		thrown.expect(UnknownIdentifierException.class);
		thrown.expectMessage("Category with code '' not found! ");
		agentFacade.getCategories("");
	}

	@Test
	public void testGetCategoriesByNullRootCode()
	{
		given(categoryService.getCategoryForCode(null)).willThrow(
				new IllegalArgumentException(NULL_CATEGORY_CODE_MESSAGE));

		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage(NULL_CATEGORY_CODE_MESSAGE);
		agentFacade.getCategories(null);
	}

	@Test
	public void shouldReturnCategories()
	{
		final List<CategoryData> categories = agentFacade.getCategories(CODE_OF_CATEGORY_1);
		Assert.assertNotNull(categories);
		Assert.assertFalse(categories.isEmpty());
	}

	@Test
	public void shouldReturnEmptyListForLeafCategory()
	{
		final List<CategoryData> categories = agentFacade.getCategories(CODE_OF_CATEGORY_12);
		Assert.assertNotNull(categories);
		Assert.assertTrue(categories.isEmpty());
	}

	@Test
	public void shouldFilterOutEmptyCategories()
	{
		final List<CategoryData> categories = agentFacade.getCategories(CODE_OF_CATEGORY_1);
		Assert.assertTrue(categories.contains(categoryData11));
		Assert.assertFalse(categories.contains(categoryData12));
	}

	@Test
	public void shouldReturnOnlyDirectDescenders()
	{
		final List<CategoryData> categories = agentFacade.getCategories(CODE_OF_CATEGORY_1);
		Assert.assertFalse(categories.contains(categoryData111));
		Assert.assertFalse(categories.contains(categoryData112));
		Assert.assertFalse(categories.contains(categoryData121));
	}

	@Test
	public void shouldKeepCategoryOrder()
	{
		final List<CategoryData> categories = agentFacade.getCategories(CODE_OF_CATEGORY_11);
		Assert.assertEquals(categoryData111, categories.get(0));
		Assert.assertEquals(categoryData112, categories.get(1));
	}
}
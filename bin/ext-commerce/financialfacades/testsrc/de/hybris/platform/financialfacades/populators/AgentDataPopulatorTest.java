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
 */
package de.hybris.platform.financialfacades.populators;


import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.converters.populator.CategoryPopulator;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.user.converters.populator.AddressPopulator;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commerceservices.url.UrlResolver;
import de.hybris.platform.commerceservices.util.ConverterFactory;
import de.hybris.platform.converters.impl.AbstractPopulatingConverter;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.financialfacades.findagent.data.AgentData;
import de.hybris.platform.financialfacades.populators.AgentDataPopulator;
import de.hybris.platform.financialservices.model.AgentModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Collections;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Assert;

/**
 * Test suite for {@link AgentDataPopulator}.
 */
@UnitTest
public class AgentDataPopulatorTest
{
	private static final String CATEGORY_CODE = "catCode";

	private static final String CATEGORY_NAME = "catName";

	private AbstractPopulatingConverter<AgentModel, AgentData> agentConverter;

	private AgentDataPopulator<AgentModel, AgentData> agentDataPopulator = new AgentDataPopulator<>();

	private Converter<CategoryModel, CategoryData> categoryConverter;

	private Converter<AddressModel, AddressData> addressConverter;
	@Mock
	private ModelService modelService;
	@Mock
	private CatalogVersionModel catVersion;
	@Mock
	private CategoryService categoryService;

	@Mock
	private Map<String, Converter<AddressModel, StringBuilder>> addressFormatConverterMap;
	@Mock
	private Converter<AddressModel, StringBuilder> defaultAddressFormatConverter;

	private final AddressPopulator addressPopulator = new AddressPopulator();

	private final CategoryPopulator categoryPopulator = new CategoryPopulator();

	@Mock
	private UrlResolver<CategoryModel> categoryModelUrlResolver;


	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);

		categoryPopulator.setCategoryModelUrlResolver(categoryModelUrlResolver);

		categoryConverter = new ConverterFactory<CategoryModel, CategoryData, CategoryPopulator>().create(CategoryData.class,
				categoryPopulator);

		agentDataPopulator.setCategoryConverter(categoryConverter);

		addressPopulator.setAddressFormatConverterMap(addressFormatConverterMap);
		addressPopulator.setDefaultAddressFormatConverter(defaultAddressFormatConverter);


		addressConverter = new ConverterFactory<AddressModel, AddressData, AddressPopulator>().create(AddressData.class,
				addressPopulator);

		agentDataPopulator.setAddressConverter(addressConverter);

		agentConverter = new ConverterFactory<AgentModel, AgentData, AgentDataPopulator>().create(AgentData.class,
				agentDataPopulator);
	}

	@Test
	public void testPopulate()
	{
		final AgentModel source = new AgentModel();
		final AddressModel addressModel = mock(AddressModel.class);
		addressModel.setPhone1("(415) 512-2100");
		addressModel.setEmail("google@google.com");
		addressModel.setStreetname("1 Dr Carlton B Goodlett Pl");
		addressModel.setStreetnumber("21");
		addressModel.setPostalcode("644020");
		addressModel.setTown("Weimar");
		addressModel.setFirstname("John");
		addressModel.setLastname("Zoidberg");

		final CategoryModel category = mock(CategoryModel.class);
		categoryService.setAllowedPrincipalsForCategory(category, Collections.<PrincipalModel>emptyList());

		source.setCategories(Lists.<CategoryModel>newArrayList(category));
		source.setEnquiry(addressModel);
		source.getEnquiry().getPhone1();
		MediaModel mediaModel = mock(MediaModel.class);
		when(mediaModel.getURL()).thenReturn("image.png");
		source.setThumbnail(mediaModel);
		given(defaultAddressFormatConverter.convert(Mockito.any(AddressModel.class))).willReturn(
				new StringBuilder("singleLineAddress"));
		given(category.getCode()).willReturn(CATEGORY_CODE);
		given(category.getName()).willReturn(CATEGORY_NAME);
		final AgentData result = new AgentData();
		agentDataPopulator.populate(source, result);

		Assert.assertEquals(source.getEnquiry().getPhone1(), result.getEnquiryData().getPhone());
		Assert.assertEquals(source.getEnquiry().getEmail(), result.getEnquiryData().getEmail());
		Assert.assertEquals(source.getEnquiry().getStreetname(), result.getEnquiryData().getLine1());
		Assert.assertEquals(source.getEnquiry().getPostalcode(), result.getEnquiryData().getPostalCode());
		Assert.assertEquals(source.getEnquiry().getTown(), result.getEnquiryData().getTown());
		Assert.assertEquals(source.getEnquiry().getFirstname(), result.getEnquiryData().getFirstName());
		Assert.assertEquals(source.getEnquiry().getLastname(), result.getEnquiryData().getLastName());

		final CategoryData resultCategory = result.<CategoryData>getCategories().get(0);
		final CategoryModel sourceCategory = source.<CategoryModel>getCategories().iterator().next();
		Assert.assertNotNull(sourceCategory.getCode());
		Assert.assertNotNull(resultCategory.getCode());
		Assert.assertEquals(sourceCategory.getCode(), resultCategory.getCode());
		Assert.assertEquals(sourceCategory.getName(), resultCategory.getName());
		Assert.assertNotNull(result.getThumbnail());
		Assert.assertEquals(source.getThumbnail().getURL(), result.getThumbnail().getUrl());


	}

	@Test(expected = IllegalArgumentException.class)
	public void testConvertNull()
	{
		agentConverter.convert(null);
	}

	@Test
	public void testPopulateEmptyAddress()
	{
		final AgentModel source = new AgentModel();
		final AddressModel addressModel = mock(AddressModel.class);
		final CategoryModel category = mock(CategoryModel.class);
		categoryService.setAllowedPrincipalsForCategory(category, Collections.<PrincipalModel>emptyList());

		source.setCategories(Lists.<CategoryModel>newArrayList(category));
		source.setEnquiry(addressModel);
		source.getEnquiry().getPhone1();
		given(defaultAddressFormatConverter.convert(Mockito.any(AddressModel.class))).willReturn(
				new StringBuilder("singleLineAddress"));
		given(category.getCode()).willReturn(CATEGORY_CODE);
		given(category.getName()).willReturn(CATEGORY_NAME);
		final AgentData result = new AgentData();
		agentDataPopulator.populate(source, result);

		final CategoryData resultCategory = result.<CategoryData>getCategories().get(0);
		final CategoryModel sourceCategory = source.<CategoryModel>getCategories().iterator().next();
		Assert.assertNotNull(sourceCategory.getCode());
		Assert.assertNotNull(resultCategory.getCode());
		Assert.assertEquals(sourceCategory.getCode(), resultCategory.getCode());
		Assert.assertEquals(sourceCategory.getName(), resultCategory.getName());
	}
}

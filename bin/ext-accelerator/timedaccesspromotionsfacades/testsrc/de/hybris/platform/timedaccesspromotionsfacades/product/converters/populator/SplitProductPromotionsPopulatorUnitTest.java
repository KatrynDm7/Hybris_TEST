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
package de.hybris.platform.timedaccesspromotionsfacades.product.converters.populator;

import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.PromotionData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.promotions.PromotionsService;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.promotions.model.ProductPromotionModel;
import de.hybris.platform.promotions.model.PromotionGroupModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.site.BaseSiteService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 * Test suite for {@link SplitProductPromotionsPopulator}
 */
@UnitTest
public class SplitProductPromotionsPopulatorUnitTest
{
	@Mock
	private PromotionsService promotionsService;
	@Mock
	private Converter<AbstractPromotionModel, PromotionData> promotionsConverter;
	@Mock
	private TimeService timeService;
	@Mock
	private ModelService modelService;
	@Mock
	private BaseSiteService baseSiteService;

	private ProductModel source;

	private PromotionGroupModel defaultPromotionGroup;

	private ProductPromotionModel productPromotionModel, specialPromotionModel;

	private PromotionData promotionData, specialPromotionData;

	private BaseSiteModel baseSiteModel;

	private Date currentDate;

	private SplitProductPromotionsPopulator splitProductPromotionsPopulator;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		splitProductPromotionsPopulator = new SplitProductPromotionsPopulator();
		splitProductPromotionsPopulator.setBaseSiteService(baseSiteService);
		splitProductPromotionsPopulator.setModelService(modelService);
		splitProductPromotionsPopulator.setPromotionsConverter(promotionsConverter);
		splitProductPromotionsPopulator.setPromotionsService(promotionsService);
		splitProductPromotionsPopulator.setTimeService(timeService);

		source = new ProductModel();
		defaultPromotionGroup = new PromotionGroupModel();
		currentDate = DateUtils.round(new Date(), Calendar.MINUTE);
		productPromotionModel = new ProductPromotionModel();
		specialPromotionModel = new ProductPromotionModel();
		promotionData = new PromotionData();
		specialPromotionData = new PromotionData();
		baseSiteModel = new BaseSiteModel();

		given(timeService.getCurrentTime()).willReturn(currentDate);
		given(baseSiteService.getCurrentBaseSite()).willReturn(baseSiteModel);
		baseSiteModel.setDefaultPromotionGroup(defaultPromotionGroup);
		specialPromotionModel.setSpecialDiscount('Y');
		given(promotionsConverter.convert(productPromotionModel)).willReturn(promotionData);
		given(promotionsConverter.convert(specialPromotionModel)).willReturn(specialPromotionData);
	}

	@Test
	public void testPopulate()
	{
		//mock two kinds of product promotion
		final List<ProductPromotionModel> promotions = new ArrayList<ProductPromotionModel>();
		//normal product promotion
		promotions.add(productPromotionModel);
		//special product promotion
		promotions.add(specialPromotionModel);

		given(promotionsService.getProductPromotions(Collections.singletonList(defaultPromotionGroup), source, true, currentDate))
				.willReturn(promotions);

		final ProductData result = new ProductData();
		splitProductPromotionsPopulator.populate(source, result);

		Assert.assertEquals(1, result.getPotentialPromotions().size());
		Assert.assertEquals(promotionData, result.getPotentialPromotions().iterator().next());

		Assert.assertEquals(1, result.getSpecialPromotions().size());
		Assert.assertEquals(specialPromotionData, result.getSpecialPromotions().iterator().next());
	}

	@Test
	public void tesPopulatetOnlySpecialPromotion()
	{
		given(promotionsService.getProductPromotions(Collections.singletonList(defaultPromotionGroup), source, true, currentDate))
				.willReturn(Collections.singletonList(specialPromotionModel));

		final ProductData result = new ProductData();
		splitProductPromotionsPopulator.populate(source, result);

		Assert.assertEquals(1, result.getSpecialPromotions().size());
		Assert.assertEquals(specialPromotionData, result.getSpecialPromotions().iterator().next());

		Assert.assertEquals(0, result.getPotentialPromotions().size());
	}

	@Test
	public void testPopulateOnlyNonSpecialPromotion()
	{
		given(promotionsService.getProductPromotions(Collections.singletonList(defaultPromotionGroup), source, true, currentDate))
				.willReturn(Collections.singletonList(productPromotionModel));

		final ProductData result = new ProductData();
		splitProductPromotionsPopulator.populate(source, result);

		Assert.assertEquals(1, result.getPotentialPromotions().size());
		Assert.assertEquals(promotionData, result.getPotentialPromotions().iterator().next());

		Assert.assertEquals(0, result.getSpecialPromotions().size());
	}

	@Test
	public void testPopulateNoPromotion()
	{
		defaultPromotionGroup = null;

		final ProductData result = new ProductData();
		splitProductPromotionsPopulator.populate(source, result);

		Assert.assertEquals(0, result.getSpecialPromotions().size());
		Assert.assertEquals(0, result.getPotentialPromotions().size());
	}

}

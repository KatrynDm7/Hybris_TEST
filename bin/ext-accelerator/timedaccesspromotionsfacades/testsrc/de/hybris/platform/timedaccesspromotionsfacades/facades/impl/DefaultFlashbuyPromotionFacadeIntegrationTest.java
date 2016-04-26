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
package de.hybris.platform.timedaccesspromotionsfacades.facades.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.enums.ArticleApprovalStatus;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.jalo.order.price.PriceFactory;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.order.TestPriceFactory;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.timedaccesspromotionsfacades.facades.FlashbuyPromotionFacade;
import de.hybris.platform.timedaccesspromotionsservices.exception.MultipleEnqueueException;
import de.hybris.platform.timedaccesspromotionsservices.model.FlashbuyPromotionModel;
import de.hybris.platform.timedaccesspromotionsservices.service.FlashbuyPromotionService;
import de.hybris.platform.util.PriceValue;

import java.util.Collections;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * Integration test suite for {@link DefaultFlashbuyPromotionFacade}
 */
@IntegrationTest
public class DefaultFlashbuyPromotionFacadeIntegrationTest extends ServicelayerTransactionalTest
{
	private static final String PROMOTION_CODE = "promotionCode";
	private static final String PRODUCT_CODE = "productCode";
	private static final String CUSTOMER_UID = "customerUID";
	private static final String CUSTOMER_2 = "customer2";
	private static final String CATALOG_ID = "catalogModelId";
	private static final String VERSION = "version";
	private static final String UNIT = "myUnit";
	private static final String CURRENCY_ISOCODE = "CNY";

	@Resource
	private FlashbuyPromotionService flashbuyPromotionService;
	@Resource
	private FlashbuyPromotionFacade flashbuyPromotionFacade;
	@Resource
	private ModelService modelService;
	@Resource
	private CatalogVersionService catalogVersionService;

	private PriceFactory prevPriceFactory;
	private TestPriceFactory priceFactory;

	@Before
	public void setUp() throws Exception
	{
		final CatalogModel catalogModel = modelService.create(CatalogModel.class);
		catalogModel.setId(CATALOG_ID);
		modelService.save(catalogModel);

		final CatalogVersionModel version = modelService.create(CatalogVersionModel.class);
		version.setVersion(VERSION);
		version.setCatalog(catalogModel);
		modelService.save(version);

		catalogVersionService.setSessionCatalogVersion(CATALOG_ID, VERSION);

		final UnitModel unit = modelService.create(UnitModel.class);
		unit.setCode(UNIT);
		unit.setUnitType(UNIT);
		modelService.save(unit);

		final ProductModel productModel = modelService.create(ProductModel.class);
		productModel.setCode(PRODUCT_CODE);
		productModel.setCatalogVersion(version);
		productModel.setApprovalStatus(ArticleApprovalStatus.APPROVED);
		productModel.setUnit(unit);
		modelService.save(productModel);

		final FlashbuyPromotionModel flashbuyPromotionModel = modelService.create(FlashbuyPromotionModel.class);
		flashbuyPromotionModel.setCode(PROMOTION_CODE);
		flashbuyPromotionModel.setAvailableUnitsPerProduct(new Long(4));
		flashbuyPromotionModel.setAvailableUnitsPerUserAndProduct(new Long(4));
		flashbuyPromotionModel.setStartBuyDate(flashbuyPromotionModel.getStartDate());
		flashbuyPromotionModel.setProducts(Collections.singletonList(productModel));
		modelService.save(flashbuyPromotionModel);

		prevPriceFactory = jaloSession.getSessionContext().getPriceFactory();
		priceFactory = new TestPriceFactory();
		jaloSession.getSessionContext().setPriceFactory(priceFactory);

		//make the test price factory return 15CNY for the test product
		priceFactory.setBasePrice((Product) modelService.getSource(productModel), new PriceValue(CURRENCY_ISOCODE, 15, false));

	}

	@Test
	public void testHasProductAvailable()
	{
		final boolean hasProductAvailable = flashbuyPromotionFacade.hasProductAvailable(PRODUCT_CODE, PROMOTION_CODE);
		Assert.assertEquals(true, hasProductAvailable);
	}

	@Test
	public void testNoProductAvailable() throws MultipleEnqueueException
	{
		flashbuyPromotionService.enqueue(PROMOTION_CODE, PRODUCT_CODE, CUSTOMER_UID, 4);
		final boolean hasProductAvailable = flashbuyPromotionFacade.hasProductAvailable(PRODUCT_CODE, PROMOTION_CODE);
		Assert.assertEquals(false, hasProductAvailable);

	}

	@Test
	public void testEnqueueSuccess() throws CommerceCartModificationException, MultipleEnqueueException
	{
		final boolean enqueueResult = flashbuyPromotionFacade.enqueue(PRODUCT_CODE, PROMOTION_CODE, CUSTOMER_UID, 2);
		Assert.assertEquals(true, enqueueResult);
	}

	@Test
	public void testEnqueueFailedNoProductAvailable() throws CommerceCartModificationException, MultipleEnqueueException
	{
		flashbuyPromotionService.enqueue(PROMOTION_CODE, PRODUCT_CODE, CUSTOMER_2, 4);
		final boolean enqueueResult = flashbuyPromotionFacade.enqueue(PRODUCT_CODE, PROMOTION_CODE, CUSTOMER_UID, 1);
		Assert.assertEquals(false, enqueueResult);
	}

	@Test(expected = MultipleEnqueueException.class)
	public void testEnqueueFailedForMultipleEnqueue() throws CommerceCartModificationException, MultipleEnqueueException
	{
		flashbuyPromotionService.enqueue(PROMOTION_CODE, PRODUCT_CODE, CUSTOMER_UID, 1);
		final boolean enqueueResult = flashbuyPromotionFacade.enqueue(PRODUCT_CODE, PROMOTION_CODE, CUSTOMER_UID, 1);
	}

	@Test
	public void testEnqueueFailedForIllegalQuantity() throws CommerceCartModificationException, MultipleEnqueueException
	{
		final boolean enqueueResult = flashbuyPromotionFacade.enqueue(PRODUCT_CODE, PROMOTION_CODE, CUSTOMER_UID, 0);
		Assert.assertEquals(false, enqueueResult);
	}

	@After
	public void tearDown()
	{
		jaloSession.getSessionContext().setPriceFactory(prevPriceFactory);
	}
}

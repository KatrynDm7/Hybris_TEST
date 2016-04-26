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
package de.hybris.platform.timedaccesspromotionsservices.service.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.timedaccesspromotionsservices.dao.FlashbuyPromotionDao;
import de.hybris.platform.timedaccesspromotionsservices.exception.MultipleEnqueueException;
import de.hybris.platform.timedaccesspromotionsservices.jalo.FlashbuyPromotion;
import de.hybris.platform.timedaccesspromotionsservices.model.FlashbuyPromotionModel;
import de.hybris.platform.timedaccesspromotionsservices.model.PromotionAllocationModel;
import de.hybris.platform.timedaccesspromotionsservices.model.PromotionEnqueueModel;
import de.hybris.platform.timedaccesspromotionsservices.model.PromotionReservationModel;
import de.hybris.platform.timedaccesspromotionsservices.service.FlashbuyPromotionService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;


/**
 * Integration test suite for {@link DefaultFlashbuyService}
 */
@IntegrationTest
public class DefaultFlashbuyPromotionServiceIntegrationTest extends ServicelayerTransactionalTest
{
	private static final String PROMOTION_1 = "promotionCode_1";
	private static final String PRODUCT_1_1 = "productCode_1_1";
	private static final String PRODUCT_1_2 = "productCode_1_2";
	private static final String CUSTOMER_1 = "customerUID_1";
	private static final String PROMOTION_MATCHER_C1_PM_1_PD_1 = "customer1_promotion1_product1_cartEntry";
	private static final String PROMOTION_MATCHER_C1_PM_1_PD_2 = "customer1_promotion1_product2_cartEntry";

	private static final String PROMOTION_2 = "promotionCode_2";
	private static final String PRODUCT_2 = "productCode_2";
	private static final String CUSTOMER_2 = "customerUID_2";
	private static final String PROMOTION_MATCHER_C2_PM_1_PD_1 = "customer2_promotion1_product1_cartEntry";
	private static final String PROMOTION_MATCHER_C2_PM_2_PD_2 = "customer2_promotion2_product2_cartEntry";

	private static final String CATALOG_ID = "catalogModelId";
	private static final String VERSION = "version";

	@Resource(name = "defaultFlashbuyPromotionService")
	FlashbuyPromotionService flashbuyPromotionService;

	@Resource
	ModelService modelService;

	@Resource
	FlashbuyPromotionDao flashbuyPromotionDao;

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

		final ProductModel product1_1 = modelService.create(ProductModel.class);
		product1_1.setCode(PRODUCT_1_1);
		product1_1.setCatalogVersion(version);
		modelService.save(product1_1);

		final ProductModel product1_2 = modelService.create(ProductModel.class);
		product1_2.setCode(PRODUCT_1_2);
		product1_2.setCatalogVersion(version);
		modelService.save(product1_2);

		final ProductModel product2 = modelService.create(ProductModel.class);
		product2.setCode(PRODUCT_2);
		product2.setCatalogVersion(version);
		modelService.save(product2);

		// create flashbuyPromotion 1, which has two products (product1_1 and product1_2)
		final FlashbuyPromotionModel flashbuyPromotionModel_1 = modelService.create(FlashbuyPromotionModel.class);
		flashbuyPromotionModel_1.setCode(PROMOTION_1);
		flashbuyPromotionModel_1.setAvailableUnitsPerProduct(new Long(10));
		flashbuyPromotionModel_1.setAvailableUnitsPerUserAndProduct(new Long(4));
		flashbuyPromotionModel_1.setStartBuyDate(flashbuyPromotionModel_1.getStartDate());
		final List<ProductModel> products = new ArrayList<ProductModel>();
		products.add(product1_1);
		products.add(product1_2);
		flashbuyPromotionModel_1.setProducts(products);
		modelService.save(flashbuyPromotionModel_1);

		// create flashbuyPromotion 2, which has one product (product2)
		final FlashbuyPromotionModel flashbuyPromotionModel_2 = modelService.create(FlashbuyPromotionModel.class);
		flashbuyPromotionModel_2.setCode(PROMOTION_2);
		flashbuyPromotionModel_2.setAvailableUnitsPerProduct(new Long(10));
		flashbuyPromotionModel_2.setAvailableUnitsPerUserAndProduct(new Long(3));
		flashbuyPromotionModel_2.setStartBuyDate(flashbuyPromotionModel_2.getStartDate());
		flashbuyPromotionModel_2.setProducts(Collections.singletonList(product2));
		modelService.save(flashbuyPromotionModel_2);
	}

	/**
	 * Test flows for a customer successfully applies {@link FlashbuyPromotion} completely.
	 *
	 * @throws MultipleEnqueueException
	 */
	@Test
	public void testFlashbuyEnqueue() throws MultipleEnqueueException
	{
		//case1: customer 1 requests for promotion 1 product1_1 with quantity 3
		final boolean enqueueCase1 = flashbuyPromotionService.enqueue(PROMOTION_1, PRODUCT_1_1, CUSTOMER_1, 3);
		long product1_1_remainingQty = flashbuyPromotionDao.getRemainingQuantity(PROMOTION_1, PRODUCT_1_1);
		Assert.assertEquals(true, enqueueCase1);
		Assert.assertEquals(7, product1_1_remainingQty);
		final List<PromotionEnqueueModel> enqueueModels1 = flashbuyPromotionDao.getEnqueueforRemoval(PRODUCT_1_1, PROMOTION_1,
				CUSTOMER_1);
		Assert.assertEquals(3, enqueueModels1.size());
		Assert.assertEquals(CUSTOMER_1, enqueueModels1.get(0).getCustomerUID());
		Assert.assertEquals(PROMOTION_1, enqueueModels1.get(0).getPromotionCode());
		Assert.assertEquals(PRODUCT_1_1, enqueueModels1.get(0).getProductCode());

		//case2: customer 1 requests for promotion 1 product1_2 with quantity 2
		final boolean enqueueCase2 = flashbuyPromotionService.enqueue(PROMOTION_1, PRODUCT_1_2, CUSTOMER_1, 2);
		final long product1_2_remainingQty = flashbuyPromotionDao.getRemainingQuantity(PROMOTION_1, PRODUCT_1_2);
		Assert.assertEquals(true, enqueueCase2);
		Assert.assertEquals(7, product1_1_remainingQty);
		Assert.assertEquals(8, product1_2_remainingQty);
		final List<PromotionEnqueueModel> enqueueModels2 = flashbuyPromotionDao.getEnqueueforRemoval(PRODUCT_1_2, PROMOTION_1,
				CUSTOMER_1);
		Assert.assertEquals(2, enqueueModels2.size());
		Assert.assertEquals(CUSTOMER_1, enqueueModels2.get(0).getCustomerUID());
		Assert.assertEquals(PROMOTION_1, enqueueModels2.get(0).getPromotionCode());
		Assert.assertEquals(PRODUCT_1_2, enqueueModels2.get(0).getProductCode());

		//case3: customer 2 requests for promotion 1 product1_1 with quantity 2
		final boolean enqueueCase3 = flashbuyPromotionService.enqueue(PROMOTION_1, PRODUCT_1_1, CUSTOMER_2, 2);
		product1_1_remainingQty = flashbuyPromotionDao.getRemainingQuantity(PROMOTION_1, PRODUCT_1_1);
		Assert.assertEquals(true, enqueueCase3);
		Assert.assertEquals(5, product1_1_remainingQty);
		final List<PromotionEnqueueModel> enqueueModels3 = flashbuyPromotionDao.getEnqueueforRemoval(PRODUCT_1_1, PROMOTION_1,
				CUSTOMER_2);
		Assert.assertEquals(2, enqueueModels3.size());
		Assert.assertEquals(CUSTOMER_2, enqueueModels3.get(0).getCustomerUID());
		Assert.assertEquals(PROMOTION_1, enqueueModels3.get(0).getPromotionCode());
		Assert.assertEquals(PRODUCT_1_1, enqueueModels3.get(0).getProductCode());

		//case4: customer 2 requests for promotion 2 product 2 with quantity 3
		final boolean enqueueCase4 = flashbuyPromotionService.enqueue(PROMOTION_2, PRODUCT_2, CUSTOMER_2, 3);
		final long product2_remainingQty = flashbuyPromotionDao.getRemainingQuantity(PROMOTION_2, PRODUCT_2);
		Assert.assertEquals(true, enqueueCase4);
		Assert.assertEquals(7, product2_remainingQty);
		final List<PromotionEnqueueModel> enqueueModels4 = flashbuyPromotionDao.getEnqueueforRemoval(PRODUCT_2, PROMOTION_2,
				CUSTOMER_2);
		Assert.assertEquals(3, enqueueModels4.size());
		Assert.assertEquals(CUSTOMER_2, enqueueModels4.get(0).getCustomerUID());
		Assert.assertEquals(PROMOTION_2, enqueueModels4.get(0).getPromotionCode());
		Assert.assertEquals(PRODUCT_2, enqueueModels4.get(0).getProductCode());
	}

	@Test
	public void testFlashbuyReserve() throws MultipleEnqueueException
	{
		//prepare enqueue data for reserve
		flashbuyEnqueue();
		final boolean reserveCase1 = flashbuyPromotionService.reserve(PROMOTION_1, PRODUCT_1_1, CUSTOMER_1,
				PROMOTION_MATCHER_C1_PM_1_PD_1);
		Assert.assertEquals(true, reserveCase1);
		final long remainingQty = flashbuyPromotionDao.getRemainingQuantity(PROMOTION_1, PRODUCT_1_1);
		Assert.assertEquals(5, remainingQty);

		final long reservationQty = flashbuyPromotionDao.getReserverdQuantity(PROMOTION_1, PRODUCT_1_1,
				PROMOTION_MATCHER_C1_PM_1_PD_1);
		Assert.assertEquals(3, reservationQty);

		final List<PromotionReservationModel> reservationModels = flashbuyPromotionDao.getReservationforRemoval(PRODUCT_1_1,
				PROMOTION_1, PROMOTION_MATCHER_C1_PM_1_PD_1);
		Assert.assertEquals(3, reservationModels.size());
		Assert.assertEquals(PROMOTION_1, reservationModels.get(0).getPromotionCode());
		Assert.assertEquals(PRODUCT_1_1, reservationModels.get(0).getProductCode());
		Assert.assertEquals(PROMOTION_MATCHER_C1_PM_1_PD_1, reservationModels.get(0).getPromotionMatcher());
	}

	@Test
	public void testflashbuyAllocate() throws MultipleEnqueueException
	{
		//prepare reserve data for allocate
		flashbuyReserve();

		final long reservationQty_beforeAllocate = flashbuyPromotionDao.getReserverdQuantity(PROMOTION_1, PRODUCT_1_1,
				PROMOTION_MATCHER_C1_PM_1_PD_1);
		Assert.assertEquals(3, reservationQty_beforeAllocate);

		final boolean allocateCase1 = flashbuyPromotionService.allocate(PROMOTION_1, PRODUCT_1_1, PROMOTION_MATCHER_C1_PM_1_PD_1);
		Assert.assertEquals(true, allocateCase1);

		final long remainingQty = flashbuyPromotionDao.getRemainingQuantity(PROMOTION_1, PRODUCT_1_1);
		Assert.assertEquals(5, remainingQty);

		final long reservationQty_afterAllocate = flashbuyPromotionDao.getReserverdQuantity(PROMOTION_1, PRODUCT_1_1,
				PROMOTION_MATCHER_C1_PM_1_PD_1);
		Assert.assertEquals(0, reservationQty_afterAllocate);

		final List<PromotionAllocationModel> allocationModels = flashbuyPromotionDao.getAllocationforRemoval(PRODUCT_1_1,
				PROMOTION_1, PROMOTION_MATCHER_C1_PM_1_PD_1);
		Assert.assertEquals(3, allocationModels.size());
		Assert.assertEquals(PROMOTION_1, allocationModels.get(0).getPromotionCode());
		Assert.assertEquals(PRODUCT_1_1, allocationModels.get(0).getProductCode());
		Assert.assertEquals(PROMOTION_MATCHER_C1_PM_1_PD_1, allocationModels.get(0).getPromotionMatcher());
	}

	@Test
	public void testEnqueueAmountOverAvailable() throws MultipleEnqueueException
	{
		final boolean enqueueResult = flashbuyPromotionService.enqueue(PROMOTION_2, PRODUCT_2, CUSTOMER_2, 11);
		final long remainingQty = flashbuyPromotionService.getRemainingQuantity(PROMOTION_2, PRODUCT_2);
		final long enqueueQty = flashbuyPromotionDao.getEnqueueforRemoval(PRODUCT_2, PROMOTION_2, CUSTOMER_2).size();
		Assert.assertEquals(7, remainingQty);
		Assert.assertEquals(3, enqueueQty);
		Assert.assertEquals(true, enqueueResult);
	}

	@Test(expected = MultipleEnqueueException.class)
	public void testEnqueueTwiceFailed() throws MultipleEnqueueException
	{
		flashbuyPromotionService.enqueue(PROMOTION_2, PRODUCT_2, CUSTOMER_1, 1);
		final boolean enqueueResult = flashbuyPromotionService.enqueue(PROMOTION_2, PRODUCT_2, CUSTOMER_1, 2);
	}

	@Test
	public void testReserveNoCustomerFoundFailed() throws MultipleEnqueueException
	{
		flashbuyPromotionService.enqueue(PROMOTION_2, PRODUCT_2, CUSTOMER_1, 1);
		final boolean reserveResult = flashbuyPromotionService.reserve(PROMOTION_2, PRODUCT_2, CUSTOMER_2,
				PROMOTION_MATCHER_C2_PM_2_PD_2);
		Assert.assertEquals(false, reserveResult);
		Assert.assertEquals(0,
				flashbuyPromotionService.getReserverdQuantity(PROMOTION_2, PRODUCT_2, PROMOTION_MATCHER_C2_PM_2_PD_2));
	}

	@Test
	public void testReserveNoEnqueueRecordFailed()
	{
		final boolean reserveResult = flashbuyPromotionService.reserve(PROMOTION_1, PRODUCT_2, CUSTOMER_1,
				PROMOTION_MATCHER_C1_PM_1_PD_2);
		Assert.assertEquals(false, reserveResult);
	}

	@Test
	public void testAllocateNoRecordFailed() throws MultipleEnqueueException
	{
		flashbuyPromotionService.enqueue(PROMOTION_2, PRODUCT_2, CUSTOMER_2, 2);
		final boolean allocateResult = flashbuyPromotionService.allocate(PROMOTION_2, PRODUCT_2, PROMOTION_MATCHER_C2_PM_2_PD_2);
		Assert.assertEquals(false, allocateResult);
	}

	/**
	 * Data preparation. Create 4 different enqueue cases
	 *
	 * @throws MultipleEnqueueException
	 */
	protected void flashbuyEnqueue() throws MultipleEnqueueException
	{
		//case1: customer 1 requests for promotion 1 product1_1 with quantity 3
		flashbuyPromotionService.enqueue(PROMOTION_1, PRODUCT_1_1, CUSTOMER_1, 3);
		//case2: customer 1 requests for promotion 1 product1_2 with quantity 2
		flashbuyPromotionService.enqueue(PROMOTION_1, PRODUCT_1_2, CUSTOMER_1, 2);
		//case3: customer 2 requests for promotion 1 product1_1 with quantity 2
		flashbuyPromotionService.enqueue(PROMOTION_1, PRODUCT_1_1, CUSTOMER_2, 2);
		//case4: customer 2 requests for promotion 2 product 2 with quantity 3
		flashbuyPromotionService.enqueue(PROMOTION_2, PRODUCT_2, CUSTOMER_2, 3);
	}

	/**
	 * Data preparation. Create 4 enqueue cases and 4 corresponding reserve cases
	 *
	 * @throws MultipleEnqueueException
	 */
	protected void flashbuyReserve() throws MultipleEnqueueException
	{
		flashbuyEnqueue();
		flashbuyPromotionService.reserve(PROMOTION_1, PRODUCT_1_1, CUSTOMER_1, PROMOTION_MATCHER_C1_PM_1_PD_1);
		flashbuyPromotionService.reserve(PROMOTION_1, PRODUCT_1_2, CUSTOMER_1, PROMOTION_MATCHER_C1_PM_1_PD_2);
		flashbuyPromotionService.reserve(PROMOTION_1, PRODUCT_1_1, CUSTOMER_2, PROMOTION_MATCHER_C2_PM_1_PD_1);
		flashbuyPromotionService.reserve(PROMOTION_2, PRODUCT_2, CUSTOMER_2, PROMOTION_MATCHER_C2_PM_2_PD_2);
	}
}

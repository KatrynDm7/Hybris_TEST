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

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.promotions.model.ProductPercentageDiscountPromotionModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.timedaccesspromotionsservices.dao.impl.DefaultFlashbuyPromotionDao;
import de.hybris.platform.timedaccesspromotionsservices.exception.MultipleEnqueueException;
import de.hybris.platform.timedaccesspromotionsservices.model.FlashbuyPromotionModel;
import de.hybris.platform.timedaccesspromotionsservices.model.PromotionAllocationModel;
import de.hybris.platform.timedaccesspromotionsservices.model.PromotionEnqueueModel;
import de.hybris.platform.timedaccesspromotionsservices.model.PromotionReservationModel;

import java.util.ArrayList;
import java.util.Collections;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 * JUnit test suite for {@link DefaultFlashbuyPromotionService}
 */
@UnitTest
public class DefaultFlashbuyPromotionServiceTest
{
	private static final String PROMOTION_CODE = "promotionCode";
	private static final String PRODUCT_CODE = "productCode";
	private static final String CUSTOMER_UID = "customerUID";
	private static final String PROMOTION_MATCHER = "promotionMatcher";
	private static final String NOT_FLAHSBUY_PROMOTION = "notFlashbuyPromotionCode";

	private DefaultFlashbuyPromotionService flashbuyService;

	@Mock
	private DefaultFlashbuyPromotionDao flashbuyPromotionDao;
	@Mock
	private ModelService modelService;


	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		flashbuyService = new DefaultFlashbuyPromotionService();
		flashbuyService.setFlashbuyPromotionDao(flashbuyPromotionDao);
		flashbuyService.setModelService(modelService);
		given(flashbuyPromotionDao.getRemainingQuantity(PROMOTION_CODE, PRODUCT_CODE)).willReturn(Long.valueOf(10));
		given(flashbuyPromotionDao.getReserverdQuantity(PROMOTION_CODE, PRODUCT_CODE, PROMOTION_MATCHER)).willReturn(
				Long.valueOf(2));

	}

	@Test
	public void testGetReamainingQty()
	{
		final long remainingQty = flashbuyService.getRemainingQuantity(PROMOTION_CODE, PRODUCT_CODE);
		Assert.assertEquals(10, remainingQty);
	}

	@Test
	public void testGetReservedQty()
	{
		final long reservedQty = flashbuyService.getReserverdQuantity(PROMOTION_CODE, PRODUCT_CODE, PROMOTION_MATCHER);
		Assert.assertEquals(2, reservedQty);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEnqueueWhenCustomerIsNull() throws MultipleEnqueueException
	{
		flashbuyService.enqueue(PROMOTION_CODE, PRODUCT_CODE, null, 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEnqueueWhenPromotionIsNull() throws MultipleEnqueueException
	{
		flashbuyService.enqueue(null, PRODUCT_CODE, CUSTOMER_UID, 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEnqueueWhenProductIsNull() throws MultipleEnqueueException
	{
		flashbuyService.enqueue(PROMOTION_CODE, null, CUSTOMER_UID, 1);
	}

	@Test(expected = MultipleEnqueueException.class)
	public void testEnqueueFailedTwice() throws MultipleEnqueueException
	{
		final PromotionEnqueueModel enqueueModel = new PromotionEnqueueModel();
		final FlashbuyPromotionModel flashbuyPromotion = new FlashbuyPromotionModel();
		flashbuyPromotion.setAvailableUnitsPerUserAndProduct(Long.valueOf(3));
		given(modelService.create(PromotionEnqueueModel.class)).willReturn(enqueueModel);
		given(flashbuyPromotionDao.getPromotionForCode(PROMOTION_CODE)).willReturn(flashbuyPromotion);
		given(flashbuyPromotionDao.getEnqueueforRemoval(PRODUCT_CODE, PROMOTION_CODE, CUSTOMER_UID)).willReturn(
				Collections.singletonList(new PromotionEnqueueModel()));
		final boolean enqueueResult = flashbuyService.enqueue(PROMOTION_CODE, PRODUCT_CODE, CUSTOMER_UID, 2);

		Assert.assertEquals(null, enqueueModel.getCustomerUID());
		Assert.assertEquals(null, enqueueModel.getProductCode());
		Assert.assertEquals(null, enqueueModel.getPromotionCode());
		verify(modelService, times(0)).save(enqueueModel);
		Assert.assertEquals(false, enqueueResult);
	}

	@Test(expected = ClassCastException.class)
	public void testEnqueueFailedWhenPromotionNotFlashbuy() throws MultipleEnqueueException
	{
		final PromotionEnqueueModel enqueueModel = new PromotionEnqueueModel();
		final ProductPercentageDiscountPromotionModel otherPromotion = new ProductPercentageDiscountPromotionModel();
		given(modelService.create(PromotionEnqueueModel.class)).willReturn(enqueueModel);
		given(flashbuyPromotionDao.getPromotionForCode(NOT_FLAHSBUY_PROMOTION)).willReturn(otherPromotion);

		final boolean enqueueResult = flashbuyService.enqueue(NOT_FLAHSBUY_PROMOTION, PRODUCT_CODE, CUSTOMER_UID, 2);

		Assert.assertEquals(false, enqueueResult);
	}

	@Test
	public void testEnqueueFailedWhenPromotionNotExists() throws MultipleEnqueueException
	{
		final PromotionEnqueueModel enqueueModel = new PromotionEnqueueModel();
		given(modelService.create(PromotionEnqueueModel.class)).willReturn(enqueueModel);
		given(flashbuyPromotionDao.getPromotionForCode(NOT_FLAHSBUY_PROMOTION)).willReturn(null);

		final boolean enqueueResult = flashbuyService.enqueue(NOT_FLAHSBUY_PROMOTION, PRODUCT_CODE, CUSTOMER_UID, 2);

		Assert.assertEquals(false, enqueueResult);
	}

	@Test
	public void testEnqueue() throws MultipleEnqueueException
	{
		final PromotionEnqueueModel enqueueModel = new PromotionEnqueueModel();
		final FlashbuyPromotionModel flashbuyPromotion = new FlashbuyPromotionModel();
		flashbuyPromotion.setAvailableUnitsPerUserAndProduct(Long.valueOf(3));
		given(modelService.create(PromotionEnqueueModel.class)).willReturn(enqueueModel);
		given(flashbuyPromotionDao.getPromotionForCode(PROMOTION_CODE)).willReturn(flashbuyPromotion);

		flashbuyService.enqueue(PROMOTION_CODE, PRODUCT_CODE, CUSTOMER_UID, 2);

		Assert.assertEquals(CUSTOMER_UID, enqueueModel.getCustomerUID());
		Assert.assertEquals(PRODUCT_CODE, enqueueModel.getProductCode());
		Assert.assertEquals(PROMOTION_CODE, enqueueModel.getPromotionCode());
		verify(modelService, times(2)).save(enqueueModel);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testReserveWhenCustomerIsNull()
	{
		flashbuyService.reserve(PROMOTION_CODE, PRODUCT_CODE, null, PROMOTION_MATCHER);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testReserveWhenPromotionIsNull()
	{
		flashbuyService.reserve(null, PRODUCT_CODE, CUSTOMER_UID, PROMOTION_MATCHER);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testReserveWhenProductIsNull()
	{
		flashbuyService.reserve(PROMOTION_CODE, null, CUSTOMER_UID, PROMOTION_MATCHER);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testReserveWhenCartEntryIsNull()
	{
		flashbuyService.reserve(PROMOTION_CODE, PRODUCT_CODE, CUSTOMER_UID, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAllocateWhenPromotionIsNull()
	{
		flashbuyService.allocate(null, PRODUCT_CODE, PROMOTION_MATCHER);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAllocateWhenProductIsNull()
	{
		flashbuyService.allocate(PROMOTION_CODE, null, PROMOTION_MATCHER);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAllocateWhenCartEntryIsNull()
	{
		flashbuyService.allocate(PROMOTION_CODE, PRODUCT_CODE, null);
	}

	@Test
	public void testReserveNoEnqueueFound()
	{
		final PromotionReservationModel promotionReservationModel = new PromotionReservationModel();
		given(flashbuyPromotionDao.getEnqueueforRemoval(PRODUCT_CODE, PROMOTION_CODE, CUSTOMER_UID)).willReturn(null);

		final boolean reserveResult = flashbuyService.reserve(PROMOTION_CODE, PRODUCT_CODE, CUSTOMER_UID, PROMOTION_MATCHER);

		Assert.assertEquals(false, reserveResult);
	}

	@Test
	public void testReserveSuccess()
	{
		final PromotionEnqueueModel promotionEnqueueModel = new PromotionEnqueueModel();
		final ArrayList<PromotionEnqueueModel> enqueueModels = new ArrayList<PromotionEnqueueModel>();
		enqueueModels.add(promotionEnqueueModel);
		enqueueModels.add(promotionEnqueueModel);
		final PromotionReservationModel promotionReservationModel = new PromotionReservationModel();
		given(flashbuyPromotionDao.getEnqueueforRemoval(PRODUCT_CODE, PROMOTION_CODE, CUSTOMER_UID)).willReturn(enqueueModels);
		given(modelService.create(PromotionReservationModel.class)).willReturn(promotionReservationModel);

		final boolean reserveResult = flashbuyService.reserve(PROMOTION_CODE, PRODUCT_CODE, CUSTOMER_UID, PROMOTION_MATCHER);
		Assert.assertEquals(PRODUCT_CODE, promotionReservationModel.getProductCode());
		Assert.assertEquals(PROMOTION_CODE, promotionReservationModel.getPromotionCode());
		Assert.assertEquals(PROMOTION_MATCHER, promotionReservationModel.getPromotionMatcher());
		Assert.assertEquals(true, reserveResult);
		verify(modelService, times(2)).remove(promotionEnqueueModel);
		verify(modelService, times(2)).save(promotionReservationModel);
	}

	@Test
	public void testAllocateNoReserveFound()
	{
		final PromotionReservationModel promotionReservationModel = new PromotionReservationModel();
		given(flashbuyPromotionDao.getReservationforRemoval(PRODUCT_CODE, PROMOTION_CODE, PROMOTION_MATCHER)).willReturn(null);

		final boolean reserveResult = flashbuyService.allocate(PROMOTION_CODE, PRODUCT_CODE, PROMOTION_MATCHER);

		Assert.assertEquals(false, reserveResult);
	}

	@Test
	public void testAllocateSuccess()
	{
		final PromotionReservationModel promotionReservationModel = new PromotionReservationModel();
		final ArrayList<PromotionReservationModel> reservationModels = new ArrayList<PromotionReservationModel>();
		reservationModels.add(promotionReservationModel);
		reservationModels.add(promotionReservationModel);
		final PromotionAllocationModel promotionAllocationModel = new PromotionAllocationModel();
		given(flashbuyPromotionDao.getReservationforRemoval(PRODUCT_CODE, PROMOTION_CODE, PROMOTION_MATCHER)).willReturn(
				reservationModels);
		given(modelService.create(PromotionAllocationModel.class)).willReturn(promotionAllocationModel);

		final boolean allocateResult = flashbuyService.allocate(PROMOTION_CODE, PRODUCT_CODE, PROMOTION_MATCHER);

		Assert.assertEquals(PRODUCT_CODE, promotionAllocationModel.getProductCode());
		Assert.assertEquals(PROMOTION_CODE, promotionAllocationModel.getPromotionCode());
		Assert.assertEquals(PROMOTION_MATCHER, promotionAllocationModel.getPromotionMatcher());
		Assert.assertEquals(true, allocateResult);
		verify(modelService, times(2)).remove(promotionReservationModel);
		verify(modelService, times(2)).save(promotionAllocationModel);
	}

}

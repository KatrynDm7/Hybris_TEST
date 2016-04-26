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
package de.hybris.platform.timedaccesspromotionsservices.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.timedaccesspromotionsservices.dao.FlashbuyPromotionDao;
import de.hybris.platform.timedaccesspromotionsservices.model.FlashbuyPromotionModel;
import de.hybris.platform.timedaccesspromotionsservices.model.PromotionAllocationModel;
import de.hybris.platform.timedaccesspromotionsservices.model.PromotionEnqueueModel;
import de.hybris.platform.timedaccesspromotionsservices.model.PromotionReservationModel;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;


/**
 * Tests current implementation of {@link FlashbuyPromotionDao}.
 */
@IntegrationTest
public class FlashbuyPromotionDaoTest extends ServicelayerTransactionalTest
{

	private final static Logger LOG = Logger.getLogger(FlashbuyPromotionDaoTest.class);

	@Resource
	private FlashbuyPromotionDao flashbuyPromotionDao;

	@Resource
	private ModelService modelService;

	private static final String PROMOTION_CODE = "flashbuy";
	private static final String PROMOTION_CODE1 = "flashbuy1";
	private static final String PRODUCT_CODE = "1934793";
	private static final String PRODUCT_CODE1 = "1934794";
	private static final String CUSTOMER_UID = "test@sap.com";
	private static final String CUSTOMER_UID1 = "test1@sap.com";
	private static final String PROMOTION_MATCHER = "111111111";
	private static final String PROMOTION_MATCHER1 = "111111112";

	@Before
	public void before()
	{
		final Date startBuyDate = mock(Date.class);
		final FlashbuyPromotionModel flashbuyPromotionModel = modelService.create(FlashbuyPromotionModel.class);
		flashbuyPromotionModel.setAvailableUnitsPerProduct(new Long(10));
		flashbuyPromotionModel.setCode(PROMOTION_CODE);
		flashbuyPromotionModel.setStartBuyDate(startBuyDate);
		modelService.save(flashbuyPromotionModel);
	}

	@Test
	public void testGetRemainingQuantityOfPromotion()
	{
		assetRemainingQuantity(PROMOTION_CODE1, PRODUCT_CODE, 0);
		assetRemainingQuantity(PROMOTION_CODE, PRODUCT_CODE, 10);
	}


	@Test
	public void testGetRemainingQuantityAfterEnqueue()
	{
		createEnqueue(PROMOTION_CODE, PRODUCT_CODE, CUSTOMER_UID);
		createEnqueue(PROMOTION_CODE, PRODUCT_CODE, CUSTOMER_UID1);

		assetRemainingQuantity(PROMOTION_CODE, PRODUCT_CODE, 8);
	}

	@Test
	public void testGetRemainingQuantityAfterReservation()
	{
		createEnqueue(PROMOTION_CODE, PRODUCT_CODE, CUSTOMER_UID);

		createReservation(PROMOTION_CODE, PRODUCT_CODE, PROMOTION_MATCHER);
		createReservation(PROMOTION_CODE, PRODUCT_CODE, PROMOTION_MATCHER1);

		assetRemainingQuantity(PROMOTION_CODE, PRODUCT_CODE, 7);
	}

	@Test
	public void testGetRemainingQuantityAfterAllocation()
	{
		createEnqueue(PROMOTION_CODE, PRODUCT_CODE, CUSTOMER_UID);

		createReservation(PROMOTION_CODE, PRODUCT_CODE, PROMOTION_MATCHER);
		createReservation(PROMOTION_CODE, PRODUCT_CODE, PROMOTION_MATCHER1);

		for (int i = 0; i < 10; i++)
		{
			createAllocation(PROMOTION_CODE, PRODUCT_CODE, PROMOTION_MATCHER + i);

			final int leftQty = 6 - i > 0 ? 6 - i : 0;
			assetRemainingQuantity(PROMOTION_CODE, PRODUCT_CODE, leftQty);
		}

	}

	@Test
	public void testGetRemainingQuantityAfterIncorrectEnqueue()
	{
		//It's a different promotion code, so will not be count in
		createEnqueue(PROMOTION_CODE1, PRODUCT_CODE, CUSTOMER_UID);

		//It's a different promotion code, so will not be count in
		createEnqueue(PROMOTION_CODE, PRODUCT_CODE1, CUSTOMER_UID);

		assetRemainingQuantity(PROMOTION_CODE, PRODUCT_CODE, 10);

	}

	@Test
	public void testGetRemainingQuantityAfterIncorrectReservation()
	{
		//It's a different promotion code, so will not be count in
		createReservation(PROMOTION_CODE1, PRODUCT_CODE, PROMOTION_MATCHER);

		//It's a different product code, so will not be count in
		createReservation(PROMOTION_CODE, PRODUCT_CODE1, PROMOTION_MATCHER);

		assetRemainingQuantity(PROMOTION_CODE, PRODUCT_CODE, 10);

	}


	@Test
	public void testGetRemainingQuantityAfterIncorrectAllocation()
	{
		//It's a different promotion code, so will not be count in
		createAllocation(PROMOTION_CODE1, PRODUCT_CODE, PROMOTION_MATCHER);

		//It's a different promotion code, so will not be count in
		createAllocation(PROMOTION_CODE, PRODUCT_CODE1, PROMOTION_MATCHER);

		assetRemainingQuantity(PROMOTION_CODE, PRODUCT_CODE, 10);
	}


	@Test
	public void testGetReserverdQuantity()
	{
		long reservedQty = flashbuyPromotionDao.getReserverdQuantity(PROMOTION_CODE, PRODUCT_CODE, PROMOTION_MATCHER);
		assertEquals(reservedQty, 0);

		for (int i = 0; i < 3; i++)
		{
			createReservation(PROMOTION_CODE, PRODUCT_CODE, PROMOTION_MATCHER);

			reservedQty = flashbuyPromotionDao.getReserverdQuantity(PROMOTION_CODE, PRODUCT_CODE, PROMOTION_MATCHER);
			assertEquals(reservedQty, i + 1);
		}
	}

	@Test
	public void testGetReserverdQuantityAfterIncorrectReservation()
	{

		//It's a different promotion code, so will not be count in
		createReservation(PROMOTION_CODE1, PRODUCT_CODE, PROMOTION_MATCHER);

		//It's a different product code, so will not be count in
		createReservation(PROMOTION_CODE1, PRODUCT_CODE1, PROMOTION_MATCHER);


		final long reservedQty = flashbuyPromotionDao.getReserverdQuantity(PROMOTION_CODE, PRODUCT_CODE, PROMOTION_MATCHER);
		assertEquals(reservedQty, 0);

	}

	@Test
	public void testGetEnqueueforRemovalSuccessful()
	{
		createEnqueue(PROMOTION_CODE, PRODUCT_CODE, CUSTOMER_UID);
		createEnqueue(PROMOTION_CODE, PRODUCT_CODE, CUSTOMER_UID);

		//We try to get the PromotionEnqueueModel with the same PROMOTION_CODE, PRODUCT_CODE and CUSTOMER_UID, so it can select the models we created.
		final List<PromotionEnqueueModel> retrieveResult = flashbuyPromotionDao.getEnqueueforRemoval(PRODUCT_CODE, PROMOTION_CODE,
				CUSTOMER_UID);

		assertEquals(2, retrieveResult.size());
		assertEquals(PROMOTION_CODE, retrieveResult.get(0).getPromotionCode());
		assertEquals(PRODUCT_CODE, retrieveResult.get(0).getProductCode());
		assertEquals(CUSTOMER_UID, retrieveResult.get(0).getCustomerUID());
		assertEquals(PROMOTION_CODE, retrieveResult.get(1).getPromotionCode());
		assertEquals(PRODUCT_CODE, retrieveResult.get(1).getProductCode());
		assertEquals(CUSTOMER_UID, retrieveResult.get(1).getCustomerUID());
	}

	@Test
	public void testGetEnqueueforRemovalWiththeDifferentParameters()
	{
		createEnqueue(PROMOTION_CODE, PRODUCT_CODE, CUSTOMER_UID);

		//We try to get the PromotionEnqueueModel with the different PROMOTION_CODE, so it could not select the model we created.
		List<PromotionEnqueueModel> retrieveResult = flashbuyPromotionDao.getEnqueueforRemoval(PRODUCT_CODE1, PROMOTION_CODE,
				CUSTOMER_UID);
		assertEquals(0, retrieveResult.size());

		//We try to get the PromotionEnqueueModel with the different PRODUCT_CODE, so it could not select the model we created.
		retrieveResult = flashbuyPromotionDao.getEnqueueforRemoval(PRODUCT_CODE, PROMOTION_CODE1, CUSTOMER_UID);
		assertEquals(0, retrieveResult.size());

		//We try to get the PromotionEnqueueModel with the different CUSTOMER_UID, so it could not select the model we created
		retrieveResult = flashbuyPromotionDao.getEnqueueforRemoval(PRODUCT_CODE, PROMOTION_CODE, CUSTOMER_UID1);
		assertEquals(0, retrieveResult.size());
	}


	@Test
	public void testGetReservationforRemovalSuccessful()
	{
		createReservation(PROMOTION_CODE, PRODUCT_CODE, PROMOTION_MATCHER);
		createReservation(PROMOTION_CODE, PRODUCT_CODE, PROMOTION_MATCHER);

		//We try to get the PromotionReservationModel with the same PROMOTION_CODE, PRODUCT_CODE and PROMOTION_MATCHER, so it can select the models we created.
		final List<PromotionReservationModel> retrieveResult = flashbuyPromotionDao.getReservationforRemoval(PRODUCT_CODE,
				PROMOTION_CODE, PROMOTION_MATCHER);

		assertEquals(2, retrieveResult.size());
		assertEquals(PROMOTION_CODE, retrieveResult.get(0).getPromotionCode());
		assertEquals(PRODUCT_CODE, retrieveResult.get(0).getProductCode());
		assertEquals(PROMOTION_MATCHER, retrieveResult.get(0).getPromotionMatcher());
		assertEquals(PROMOTION_CODE, retrieveResult.get(1).getPromotionCode());
		assertEquals(PRODUCT_CODE, retrieveResult.get(1).getProductCode());
		assertEquals(PROMOTION_MATCHER, retrieveResult.get(1).getPromotionMatcher());


	}

	@Test
	public void testGetReservationForRemovalWiththeDifferentParameters()
	{
		createReservation(PROMOTION_CODE, PRODUCT_CODE, PROMOTION_MATCHER);

		//We try to get the PromotionReservationModel with the different PROMOTION_CODE, so it could not select the model we created.
		List<PromotionReservationModel> retrieveResult = flashbuyPromotionDao.getReservationforRemoval(PRODUCT_CODE,
				PROMOTION_CODE1, PROMOTION_MATCHER);
		assertEquals(0, retrieveResult.size());

		//We try to get the PromotionReservationModel with the different PRODUCT_CODE, so it could not select the model we created.
		retrieveResult = flashbuyPromotionDao.getReservationforRemoval(PRODUCT_CODE1, PROMOTION_CODE, PROMOTION_MATCHER);
		assertEquals(0, retrieveResult.size());

		//We try to get the PromassertEquals(0, retrieveResult.size());otionReservationModel with the different PROMOTION_MATCHER, so it could not select the model we created.
		retrieveResult = flashbuyPromotionDao.getReservationforRemoval(PRODUCT_CODE1, PROMOTION_CODE, PROMOTION_MATCHER1);
		assertEquals(0, retrieveResult.size());
	}

	@Test
	public void testGetAllocationforRemovalSuccessful()
	{
		createAllocation(PROMOTION_CODE, PRODUCT_CODE, PROMOTION_MATCHER);
		createAllocation(PROMOTION_CODE, PRODUCT_CODE, PROMOTION_MATCHER);

		//We try to get the PromotionAllocationModel with the same PROMOTION_CODE, PRODUCT_CODE and PROMOTION_MATCHER, so it can select the models we created.
		final List<PromotionAllocationModel> retrieveResult = flashbuyPromotionDao.getAllocationforRemoval(PRODUCT_CODE,
				PROMOTION_CODE, PROMOTION_MATCHER);

		assertEquals(2, retrieveResult.size());
		assertEquals(PROMOTION_CODE, retrieveResult.get(0).getPromotionCode());
		assertEquals(PRODUCT_CODE, retrieveResult.get(0).getProductCode());
		assertEquals(PROMOTION_MATCHER, retrieveResult.get(0).getPromotionMatcher());
		assertEquals(PROMOTION_CODE, retrieveResult.get(1).getPromotionCode());
		assertEquals(PRODUCT_CODE, retrieveResult.get(1).getProductCode());
		assertEquals(PROMOTION_MATCHER, retrieveResult.get(1).getPromotionMatcher());
	}

	@Test
	public void testGetAllocationforReservationWiththeDifferentParameters()
	{
		createAllocation(PROMOTION_CODE, PRODUCT_CODE, PROMOTION_MATCHER);

		//We try to get the PromotionAllocationModel with the different PROMOTION_CODE, so it could not select the model we created.
		List<PromotionAllocationModel> retrieveResult = flashbuyPromotionDao.getAllocationforRemoval(PRODUCT_CODE, PROMOTION_CODE1,
				PROMOTION_MATCHER);
		assertEquals(0, retrieveResult.size());

		//We try to get the PromotionAllocationModel with the different PRODUCT_CODE, so it could not select the model we created.
		retrieveResult = flashbuyPromotionDao.getAllocationforRemoval(PRODUCT_CODE1, PROMOTION_CODE, PROMOTION_MATCHER);
		assertEquals(0, retrieveResult.size());

		//We try to get the PromotionAllocationModel with the different PROMOTION_MATCHER, so it could not select the model we created.
		retrieveResult = flashbuyPromotionDao.getAllocationforRemoval(PRODUCT_CODE1, PROMOTION_CODE, PROMOTION_MATCHER1);
		assertEquals(0, retrieveResult.size());
	}

	protected void createEnqueue(final String promotionCode, final String productCode, final String customerUID)
	{
		final PromotionEnqueueModel enqueue1 = modelService.create(PromotionEnqueueModel.class);
		enqueue1.setProductCode(productCode);
		enqueue1.setPromotionCode(promotionCode);
		enqueue1.setCustomerUID(customerUID);
		modelService.save(enqueue1);
	}


	protected void createReservation(final String promotionCode, final String productCode, final String promotionMatcher)
	{
		final PromotionReservationModel reservaion = modelService.create(PromotionReservationModel.class);
		reservaion.setProductCode(productCode);
		reservaion.setPromotionCode(promotionCode);
		reservaion.setPromotionMatcher(promotionMatcher);
		modelService.save(reservaion);
	}

	protected void createAllocation(final String promotionCode, final String productCode, final String promotionMatcher)
	{
		final PromotionAllocationModel allocation = modelService.create(PromotionAllocationModel.class);
		allocation.setProductCode(productCode);
		allocation.setPromotionCode(promotionCode);
		allocation.setPromotionMatcher(promotionMatcher);
		modelService.save(allocation);
	}

	protected void assetRemainingQuantity(final String promotionCode, final String productCode, final long quantity)
	{
		final long remainingQuantity = flashbuyPromotionDao.getRemainingQuantity(promotionCode, productCode);
		assertEquals(remainingQuantity, quantity);

	}

}

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

import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.timedaccesspromotionsfacades.order.ExtendedCartFacade;
import de.hybris.platform.timedaccesspromotionsservices.exception.MultipleEnqueueException;
import de.hybris.platform.timedaccesspromotionsservices.service.FlashbuyPromotionService;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 * Junit test suite for {@link DefaultFlashbuyPromotionFacade}
 */
@UnitTest
public class DefaultFlashbuyPromotionFacadeTest
{
	private static final String PROMOTION_CODE = "promotionCode";
	private static final String PRODUCT_CODE = "productCode";
	private static final String CUSTOMER_UID = "customerUID";
	private static final long QUANTITY = Long.valueOf(10);

	@Mock
	FlashbuyPromotionService flashbuyPromotionService;
	@Mock
	ExtendedCartFacade extendedCartFacade;

	DefaultFlashbuyPromotionFacade flashbuyPromotionFacade;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);

		flashbuyPromotionFacade = new DefaultFlashbuyPromotionFacade();
		flashbuyPromotionFacade.setFlashbuyPromotionService(flashbuyPromotionService);
		flashbuyPromotionFacade.setExtendedCartFacade(extendedCartFacade);
	}

	@Test
	public final void testHasProductAvailable()
	{
		given(flashbuyPromotionService.getRemainingQuantity(PROMOTION_CODE, PRODUCT_CODE)).willReturn(QUANTITY);
		Assert.assertEquals(true, flashbuyPromotionFacade.hasProductAvailable(PRODUCT_CODE, PROMOTION_CODE));
	}

	@Test
	public final void testHasNoProductAvailable()
	{
		given(flashbuyPromotionService.getRemainingQuantity(PROMOTION_CODE, PRODUCT_CODE)).willReturn(Long.valueOf(0));
		Assert.assertEquals(false, flashbuyPromotionFacade.hasProductAvailable(PRODUCT_CODE, PROMOTION_CODE));
	}

	@Test
	public final void testEnqueueSuccess() throws CommerceCartModificationException, MultipleEnqueueException
	{
		given(flashbuyPromotionService.getRemainingQuantity(PROMOTION_CODE, PRODUCT_CODE)).willReturn(QUANTITY);
		given(flashbuyPromotionService.enqueue(PROMOTION_CODE, PRODUCT_CODE, CUSTOMER_UID, QUANTITY)).willReturn(true);
		final boolean enqueueResult = flashbuyPromotionFacade.enqueue(PRODUCT_CODE, PROMOTION_CODE, CUSTOMER_UID, QUANTITY);
		Assert.assertEquals(true, enqueueResult);
	}


	@Test
	public final void testEnqueueFailedByServiceFail() throws CommerceCartModificationException, MultipleEnqueueException
	{
		given(flashbuyPromotionService.enqueue(PROMOTION_CODE, PRODUCT_CODE, CUSTOMER_UID, QUANTITY)).willReturn(false);
		final boolean enqueueResult = flashbuyPromotionFacade.enqueue(PRODUCT_CODE, PROMOTION_CODE, CUSTOMER_UID, QUANTITY);
		Assert.assertEquals(false, enqueueResult);
	}

	@Test
	public final void testEnqueueFailedByNoRemaining() throws CommerceCartModificationException, MultipleEnqueueException
	{
		given(flashbuyPromotionService.getRemainingQuantity(PROMOTION_CODE, PRODUCT_CODE)).willReturn(Long.valueOf(0));
		final boolean enqueueResult = flashbuyPromotionFacade.enqueue(PRODUCT_CODE, PROMOTION_CODE, CUSTOMER_UID, QUANTITY);
		Assert.assertEquals(false, enqueueResult);
	}

}

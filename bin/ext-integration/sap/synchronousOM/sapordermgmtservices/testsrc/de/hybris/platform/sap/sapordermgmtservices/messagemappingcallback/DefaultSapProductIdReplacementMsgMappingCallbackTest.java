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
package de.hybris.platform.sap.sapordermgmtservices.messagemappingcallback;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.sap.core.common.util.LocaleUtil;
import de.hybris.platform.sap.sapordermgmtbol.transaction.salesdocument.backend.impl.messagemapping.BackendMessage;

import java.util.Locale;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;


@SuppressWarnings("javadoc")
@UnitTest
public class DefaultSapProductIdReplacementMsgMappingCallbackTest
{

	private DefaultSapProductIdReplacementMsgMappingCallback classUnderTest;

	@Before
	public void setUp()
	{

		classUnderTest = new DefaultSapProductIdReplacementMsgMappingCallback();

	}


	@Test
	public void testProcess()
	{
		LocaleUtil.setLocale(new Locale("en"));

		final ProductService productServiceMock = EasyMock.createNiceMock(ProductService.class);
		final ProductModel productModelMock = EasyMock.createNiceMock(ProductModel.class);
		EasyMock.expect(productServiceMock.getProductForCode("HT-1010")).andReturn(productModelMock);
		EasyMock.expect(productModelMock.getName(LocaleUtil.getLocale())).andReturn("Product Description");
		EasyMock.replay(productModelMock);
		EasyMock.replay(productServiceMock);
		classUnderTest.setProductService(productServiceMock);

		final BackendMessage message = new BackendMessage("E", "ID1", "123", "HT-1010", null, null, null);

		classUnderTest.process(message);

		Assert.assertEquals("Product Description", message.getVars()[0]);
	}

	@Test
	public void testGetId()
	{

		Assert.assertEquals(DefaultSapProductIdReplacementMsgMappingCallback.SAP_PRODUCTID_REPLACEMENT_CALLBACK_ID,
				classUnderTest.getId());

	}

}

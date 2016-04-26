/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2014 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 * 
 *  
 */
package de.hybris.platform.sap.orderexchange.inbound.events;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.sap.orderexchange.constants.DataHubInboundConstants;
import de.hybris.platform.sap.orderexchange.datahub.inbound.DataHubInboundDeliveryHelper;
import de.hybris.platform.sap.orderexchange.datahub.inbound.impl.DefaultDataHubInboundDeliveryHelper;
import de.hybris.platform.sap.orderexchange.inbound.events.DataHubGoodsIssueTranslator;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;


@SuppressWarnings("javadoc")
@UnitTest
public class DataHubGoodsIssueTranslatorTest
{

	@InjectMocks
	private DataHubGoodsIssueTranslator classUnderTest;
	@Mock
	private Item processedItem;

	@Mock
	private DataHubInboundDeliveryHelper orderHubService;

	@Before
	public void setUp()
	{
		classUnderTest = new DataHubGoodsIssueTranslator();
		processedItem = org.mockito.Mockito.mock(Item.class);
		orderHubService = org.mockito.Mockito.mock(DefaultDataHubInboundDeliveryHelper.class);
		classUnderTest.setInboundHelper(orderHubService);
	}

	@Test
	public void testPerformGoodsIssueNegativeNullCheck() throws ImpExException, JaloInvalidParameterException,
			JaloSecurityException
	{
		Mockito.when(processedItem.getAttribute(DataHubInboundConstants.CODE)).thenReturn("0815");
		Mockito.when(orderHubService.determineGoodsIssueDate("19900101")).thenReturn("19900101");
		classUnderTest.performImport(null, processedItem);
		org.mockito.Mockito.verifyZeroInteractions(orderHubService);
	}

	@Test
	public void testPerformGoodsIssuePositiveGIwithWarehouseNullCheck() throws ImpExException, JaloInvalidParameterException,
			JaloSecurityException
	{
		Mockito.when(processedItem.getAttribute(DataHubInboundConstants.CODE)).thenReturn("0815");
		Mockito.when(orderHubService.determineWarehouseId("xyz")).thenReturn(null);
		Mockito.when(orderHubService.determineGoodsIssueDate("xyz")).thenReturn("19900101");
		classUnderTest.performImport("xyz", processedItem);
		org.mockito.Mockito.verify(orderHubService).processDeliveryAndGoodsIssue("0815", null, "19900101");

	}

	@Test
	public void testPerformGoodsIssuePositiveGIwithWarehouseCheck() throws ImpExException, JaloInvalidParameterException,
			JaloSecurityException
	{
		Mockito.when(processedItem.getAttribute(DataHubInboundConstants.CODE)).thenReturn("0815");
		Mockito.when(orderHubService.determineWarehouseId("xyz")).thenReturn("4711");
		Mockito.when(orderHubService.determineGoodsIssueDate("xyz")).thenReturn("19900101");
		classUnderTest.performImport("xyz", processedItem);
		org.mockito.Mockito.verify(orderHubService).processDeliveryAndGoodsIssue("0815", "4711", "19900101");

	}

}

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
/**
 * 
 */
package de.hybris.platform.sap.sapcreditcheck.integrationtest;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.order.data.AbstractOrderData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.sap.core.bol.test.SapcorebolSpringJUnitTest;
import de.hybris.platform.sap.core.jco.exceptions.BackendException;
import de.hybris.platform.sap.core.jco.rec.JCoRecMode;
import de.hybris.platform.sap.core.jco.rec.JCoRecording;
import de.hybris.platform.sap.sapcreditcheck.backend.impl.SapCreditCheckBackendERP;


/**
 * Test credit check back-end
 */
@SuppressWarnings("javadoc")
@UnitTest
@JCoRecording(mode = JCoRecMode.PLAYBACK, recordingExtensionName = "sapcreditcheck")
@ContextConfiguration(locations = { "classpath:test/sapcreditcheck-test-spring.xml","classpath:test/sapcorejco-test-spring.xml"})
public class DefaultSapCreditCheckBackendTest extends SapcorebolSpringJUnitTest
{
	@Resource(name = "sapCreditCheckBackendERP")
	private SapCreditCheckBackendERP sapCreditCheckBackendERP;

	@Test
	public void testBlockedOrderCreditStatus() throws BackendException
	{
		Boolean orderBlocked = sapCreditCheckBackendERP.checkOrderCreditBlocked("6201042");
			Assert.assertTrue(orderBlocked);			
	}
	
	@Test
	public void testUnblockedOrderCreditStatus() throws BackendException
	{
		Boolean orderBlocked = sapCreditCheckBackendERP.checkOrderCreditBlocked("6201028");
			Assert.assertFalse(orderBlocked);			
	}
		
	@Test
	public void testCreditLimitExceeded() throws BackendException
	{
		AbstractOrderData orderData = new OrderData();
		
		PriceData totalPrice = new PriceData();	
		totalPrice.setCurrencyIso("EUR");
		totalPrice.setValue(BigDecimal.valueOf(5000));
		
		PriceData totalTax = new PriceData();
		totalTax.setValue(BigDecimal.valueOf(1000));
				
		orderData.setTotalPrice(totalPrice);
		orderData.setTotalTax(totalTax);
		
		Boolean creditExceeded = sapCreditCheckBackendERP.checkCreditLimitExceeded(orderData, "1000");        
		Assert.assertTrue(creditExceeded);	
				
	}
	
	@Test
	public void testCreditLimitNotExceeded() throws BackendException
	{
		AbstractOrderData orderData = new OrderData();
		
		PriceData totalPrice = new PriceData();	
		totalPrice.setCurrencyIso("USD");
		totalPrice.setValue(BigDecimal.valueOf(4000));
		
		PriceData totalTax = new PriceData();
		totalTax.setValue(BigDecimal.valueOf(1000));
		
		
		orderData.setTotalPrice(totalPrice);
		orderData.setTotalTax(totalTax);
		
		Boolean creditExceeded = sapCreditCheckBackendERP.checkCreditLimitExceeded(orderData, "1000");        
		Assert.assertFalse(creditExceeded);	
				
	}
	
}

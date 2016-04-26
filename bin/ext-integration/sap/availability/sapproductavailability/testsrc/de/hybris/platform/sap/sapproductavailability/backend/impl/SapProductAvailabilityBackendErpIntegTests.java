/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package de.hybris.platform.sap.sapproductavailability.backend.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
//import de.hybris.platform.sap.core.backend.sp.jco.test.JcoRecAttributes;
import de.hybris.platform.sap.core.bol.businessobject.BusinessObjectException;
import de.hybris.platform.sap.core.jco.exceptions.BackendException;
import de.hybris.platform.sap.core.jco.rec.JCoRecException;
import de.hybris.platform.sap.core.jco.rec.impl.JCoRecManagedConnectionFactory;
import de.hybris.platform.sap.sapproductavailability.backend.SapProductAvailabilityBackend;
import de.hybris.platform.sap.sapproductavailability.businessobject.SapProductAvailability;
import de.hybris.platform.sap.sapproductavailability.unittests.base.SapProductAvailabilityBolSpringJunitTest;

import javax.annotation.Resource;

import org.junit.Test;


/**
 *
 */
@UnitTest
public class SapProductAvailabilityBackendErpIntegTests extends SapProductAvailabilityBolSpringJunitTest
{


	private SapProductAvailabilityBackend sapProductAvailabilityBO = null;

	@Resource(name = "sapCoreJCoManagedConnectionFactory")
	private JCoRecManagedConnectionFactory factory;



	@Test
	public void testReadPlantForMaterial() throws BusinessObjectException, BackendException, JCoRecException

	{
		sapProductAvailabilityBO = (SapProductAvailabilityBackend) genericFactory.getBean("sapProductAvailabilityBackendERP");

		assertNotNull(sapProductAvailabilityBO);

		final String material = "EPHBR01";

		final String plant = sapProductAvailabilityBO.readPlantForMaterial(material);

		assertEquals(plant, "1000");

	}

	@Test
	public void testReadPlantForCustomerMaterial() throws BusinessObjectException, BackendException
	{

		sapProductAvailabilityBO = (SapProductAvailabilityBackend) genericFactory.getBean("sapProductAvailabilityBackendERP");

		assertNotNull(sapProductAvailabilityBO);

		final String material = "MATDEMO_05";
		final String customerId = "JV02";

		final String plant = sapProductAvailabilityBO.readPlantForCustomerMaterial(material, customerId);

		assertEquals("2500", plant);

	}


	@Test
	public void testReadPlant() throws BusinessObjectException, BackendException
	{

		sapProductAvailabilityBO = (SapProductAvailabilityBackend) genericFactory.getBean("sapProductAvailabilityBackendERP");

		assertNotNull(sapProductAvailabilityBO);

		final String customerId = "JV02";

		final UnitModel unit = new UnitModel();
		unit.setCode("PC");

		final ProductModel productModel = new ProductModel();
		productModel.setUnit(unit);
		productModel.setCode("EPHBR01");


		final String plant = sapProductAvailabilityBO.readPlant(productModel, customerId);

		assertEquals("1000", plant);

	}

	@Test
	public void testReadProductAvailability() throws BackendException
	{

		sapProductAvailabilityBO = (SapProductAvailabilityBackend) genericFactory.getBean("sapProductAvailabilityBackendERP");

		assertNotNull(sapProductAvailabilityBO);

		final String customerId = "JV02";

		final UnitModel unit = new UnitModel();
		unit.setCode("PC");

		final ProductModel productModel = new ProductModel();
		productModel.setUnit(unit);
		productModel.setCode("EPHBR01");

		final String plant = "1000";

		final Long requestedQuantity = Long.valueOf(100);

		final SapProductAvailability availability = sapProductAvailabilityBO.readProductAvailability(productModel, customerId,
				plant, requestedQuantity);

		assertNotNull(availability);

		assertEquals(800L, availability.getCurrentStockLevel().longValue());


	}



}

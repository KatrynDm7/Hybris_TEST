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
package de.hybris.platform.sap.core.bol.businessobject.test;

import de.hybris.platform.sap.core.bol.businessobject.BackendInterface;
import de.hybris.platform.sap.core.bol.businessobject.BusinessObjectBase;
import de.hybris.platform.sap.core.bol.businessobject.test.be.TestBackendInterfaceBESingleImplementation;


/**
 * Test BusinessObjectBase implementation - for single backend implementation test..
 */
@BackendInterface(TestBackendInterfaceBESingleImplementation.class)
public class TestBusinessObjectBaseBESingleImplementationImpl extends BusinessObjectBase
{
	// only for testing
}

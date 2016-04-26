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
package de.hybris.platform.sap.core.jco.test;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.sap.core.jco.monitor.jaxb.JCoMonitorJAXBHandlerTest;
import de.hybris.platform.sap.core.jco.monitor.provider.JCoConnectionMonitorClusterProviderTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


/**
 * Spring testsuite for extension sapcorejco.
 */
@UnitTest
@RunWith(Suite.class)
@SuiteClasses(
{ JCoMonitorJAXBHandlerTest.class, JCoConnectionMonitorClusterProviderTest.class, RFCDirectoryDestinationDataProviderTest.class })
public class SapcorejcoSpringTestSuite
{

}

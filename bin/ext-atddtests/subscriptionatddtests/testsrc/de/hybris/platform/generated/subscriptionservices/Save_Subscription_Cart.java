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
package de.hybris.platform.generated.subscriptionservices;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Registry;
import de.hybris.platform.atddengine.framework.RobotSettings;
import de.hybris.platform.atddengine.framework.RobotTest;
import de.hybris.platform.atddengine.framework.RobotTestResult;
import de.hybris.platform.atddengine.framework.RobotTestSuite;
import de.hybris.platform.atddengine.framework.RobotTestSuiteFactory;
import de.hybris.platform.atddengine.framework.impl.DefaultPythonProvider;
import de.hybris.platform.atddengine.framework.impl.PythonAware;
import de.hybris.platform.atddengine.framework.impl.PythonRobotTestSuiteFactory;
import de.hybris.platform.atddengine.keywords.ImpExAdaptorAware;
import de.hybris.platform.atddengine.keywords.KeywordLibraryContext;
import de.hybris.platform.atddengine.keywords.KeywordLibraryContextHolder;
import de.hybris.platform.atddengine.keywords.RobotTestContext;
import de.hybris.platform.atddengine.keywords.RobotTestContextAware;
import de.hybris.platform.atddengine.keywords.impl.DefaultImpExAdaptor;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.Config;

import java.io.File;
import java.io.IOException;

import javax.annotation.Resource;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;


/*
 * This is a generated class. Do not edit, changes will be overriden.
 */
@SuppressWarnings("PMD")
@IntegrationTest
public class Save_Subscription_Cart extends ServicelayerTest implements RobotTestContext
{
	public static RobotTestSuite robotTestSuite;
	
	private ModelService modelService;

	public static void startSuite() throws IOException
	{
		if (robotTestSuite == null)
		{
			final PythonAware pythonAware = new DefaultPythonProvider(Config.getParameter("atddengine.libraries-path"));

			final RobotSettings robotSettings = new RobotSettings();
			
			robotSettings.setOutputDir(new File(Config.getParameter("atddengine.report-path"), "subscriptionservices"));
			robotSettings.setLogName("Save_Subscription_Cart-log");
			robotSettings.setOutputName("Save_Subscription_Cart-output");
			robotSettings.setReportName("Save_Subscription_Cart-report");
			
			final RobotTestSuiteFactory robotTestSuiteFactory = new PythonRobotTestSuiteFactory(pythonAware);

			robotTestSuite = robotTestSuiteFactory.parseTestSuite(robotSettings, new File(
					"/opt/bamboo-agent/xml-data/build-dir/MAINTNG-TD570-PCS/bin/ext-atddtests/subscriptionatddtests/genresources/robottests/Save_Subscription_Cart.txt"));
		}

		if (!robotTestSuite.isStarted())
		{
			robotTestSuite.start();
		}

	}

	@AfterClass
	public static void tearDownSuite()
	{
		robotTestSuite.close();
	}

	private RobotTest currentRobotTest;

	@Resource(name = "impExAdaptorHolder")
	private ImpExAdaptorAware impExAdaptorHolder;

	@Resource(name = "keywordLibraryContext")
	private KeywordLibraryContext keywordLibraryContext;

	@Resource(name = "robotTestContextHolder")
	private RobotTestContextAware robotTestContextHolder;

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.atddengine.keywords.RobotTestContext#getCurrentRobotTest()
	 */
	@Override
	public RobotTest getCurrentRobotTest()
	{
		return currentRobotTest;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.atddengine.keywords.RobotTestContext#getProjectName()
	 */
	@Override
	public String getProjectName()
	{
		return "subscriptionservices";
	}

	@Before
	public void setUp() throws IOException
	{
		impExAdaptorHolder.setImpExAdaptor(new DefaultImpExAdaptor());
		
		robotTestContextHolder.setRobotTestContext(this);

		KeywordLibraryContextHolder.setKeywordLibraryContext(keywordLibraryContext);
		
		modelService = (ModelService)Registry.getApplicationContext().getBean("modelService");

		startSuite();
	}
	
	@Test
	public void Test_Save_Subscription_Cart_SaveMasterCart_OneTimeCharge_Oncancellation_NoChildCart()
	{
		modelService.detachAll();
		
		final String robotTestName = "Test_Save_Subscription_Cart_SaveMasterCart_OneTimeCharge_Oncancellation_NoChildCart";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void Test_Save_Subscription_Cart_SaveMasterCart_OneTimeCharge_Paynow_NoChildCart()
	{
		modelService.detachAll();
		
		final String robotTestName = "Test_Save_Subscription_Cart_SaveMasterCart_OneTimeCharge_Paynow_NoChildCart";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void Test_Save_Subscription_Cart_SaveMasterCart_OneTimeCharge_Onfirstbill_1xChildCart()
	{
		modelService.detachAll();
		
		final String robotTestName = "Test_Save_Subscription_Cart_SaveMasterCart_OneTimeCharge_Onfirstbill_1xChildCart";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void Test_Save_Subscription_Cart_SaveMasterCart_RecurringCharge_1xChildCart()
	{
		modelService.detachAll();
		
		final String robotTestName = "Test_Save_Subscription_Cart_SaveMasterCart_RecurringCharge_1xChildCart";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void Test_Save_Subscription_Cart_SaveMasterCart_OneTimeCharge_Onfirstbill_And_RecurringCharge_2xChildCart()
	{
		modelService.detachAll();
		
		final String robotTestName = "Test_Save_Subscription_Cart_SaveMasterCart_OneTimeCharge_Onfirstbill_And_RecurringCharge_2xChildCart";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void Test_Save_Subscription_Cart_SaveMasterCart_ByCartCode()
	{
		modelService.detachAll();
		
		final String robotTestName = "Test_Save_Subscription_Cart_SaveMasterCart_ByCartCode";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void Test_Save_Subscription_Cart_SaveChildCart_OneTimeCharge_Onfirstbill_Exception()
	{
		modelService.detachAll();
		
		final String robotTestName = "Test_Save_Subscription_Cart_SaveChildCart_OneTimeCharge_Onfirstbill_Exception";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void Test_Save_Subscription_Cart_SaveChildCart_RecurringCharge_Exception()
	{
		modelService.detachAll();
		
		final String robotTestName = "Test_Save_Subscription_Cart_SaveChildCart_RecurringCharge_Exception";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	

}

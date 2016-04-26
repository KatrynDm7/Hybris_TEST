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
package de.hybris.platform.customerticketingc4cintegration.facade;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.customerticketingc4cintegration.SitePropsHolder;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.site.BaseSiteService;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class SiteResolverTest extends ServicelayerTest
{
	private static final Logger LOGGER = Logger.getLogger(SiteResolverTest.class);

	@Resource
	private BaseSiteService baseSiteService;

	@Resource
	private SitePropsHolder sitePropsHolder;

	@Before
	public void setUp() throws ImpExException
	{
		importCsv("/customerticketingc4cintegration/test/testCustomerTicketing.impex", "UTF-8");

		final BaseSiteModel baseSite = baseSiteService.getBaseSiteForUID("testSite");
		LOGGER.info("BS:" + baseSite);
		baseSiteService.setCurrentBaseSite(baseSite, true);
	}

	@Test
	public void testResolver()
	{
		LOGGER.info(sitePropsHolder.getSiteId());
		LOGGER.info(Boolean.valueOf(sitePropsHolder.isB2C()));
		Assert.assertTrue(sitePropsHolder.isB2C());

		final BaseSiteModel baseSite = baseSiteService.getBaseSiteForUID("testb2bSite");
		baseSiteService.setCurrentBaseSite(baseSite, true);
		Assert.assertFalse(sitePropsHolder.isB2C());
	}
}

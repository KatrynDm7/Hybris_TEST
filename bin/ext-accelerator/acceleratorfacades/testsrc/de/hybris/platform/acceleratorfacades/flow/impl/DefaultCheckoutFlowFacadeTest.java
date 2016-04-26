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
package de.hybris.platform.acceleratorfacades.flow.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.acceleratorfacades.flow.CheckoutFlowFacade;
import de.hybris.platform.acceleratorservices.enums.CheckoutFlowEnum;
import de.hybris.platform.basecommerce.util.BaseCommerceBaseTest;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.store.BaseStoreModel;

import java.util.Collections;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;


@IntegrationTest
public class DefaultCheckoutFlowFacadeTest extends BaseCommerceBaseTest
{
	@Resource
	private CheckoutFlowFacade checkoutFlowFacade;

	@Resource
	private CMSSiteService cmsSiteService;

	@Mock
	private HttpServletRequest request;

	@Resource
	private UserService userService;

	@Before
	public void prepareRequest() throws ImpExException
	{
		importCsv("/acceleratorfacades/test/testDefaultFlow.impex", "utf-8");
		BDDMockito.given(request.getHeader("User-Agent")).willReturn(
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.7 (KHTML, like Gecko) Chrome/16.0.912.77 Safari/535.7");
	}

	/**
	 * fallback to default
	 */
	@Test
	public void testGetDefaultFlowForUndefinedSiteUid()
	{
		new CMSSiteAwareTestExecutor("dummy", cmsSiteService, userService)
		{
			@Override
			protected void performTest()
			{
				Assert.assertEquals(CheckoutFlowEnum.MULTISTEP, checkoutFlowFacade.getCheckoutFlow());
			}
		}.run();
	}

	protected abstract static class CMSSiteAwareTestExecutor implements Runnable
	{
		private CMSSiteModel siteBefore;
		private UserModel userBefore;
		private final CMSSiteModel dummySite;
		private final CMSSiteService cmsSiteService;
		private final UserService userService;
		private final BaseStoreModel dummyStore;

		CMSSiteAwareTestExecutor(final String siteUid, final CMSSiteService cmsSiteService, final UserService userService)
		{
			this.cmsSiteService = cmsSiteService;
			this.userService = userService;
			this.dummySite = new CMSSiteModel();
			this.dummyStore = new BaseStoreModel();
			this.dummySite.setUid(siteUid);
			this.dummyStore.setPaymentProvider("Mockup");
			this.dummyStore.setUid(siteUid);
			this.dummySite.setStores(Collections.singletonList(dummyStore));
		}

		private void prepare()
		{
			siteBefore = cmsSiteService.getCurrentSite();
			cmsSiteService.setCurrentSite(dummySite);

			userBefore = userService.getCurrentUser();
			final CustomerModel customerModel = (CustomerModel) userService.getUserForUID("dummyuser@dummy.com");
			userService.setCurrentUser(customerModel);
		}

		public void unPrepare()
		{
			if (siteBefore != null)
			{
				cmsSiteService.setCurrentSite(siteBefore);
			}
			userService.setCurrentUser(userBefore);
		}

		@Override
		public void run()
		{
			try
			{
				prepare();
				performTest();
			}
			finally
			{
				unPrepare();
			}

		}

		protected abstract void performTest();
	}
}

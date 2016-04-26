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
package de.hybris.platform.b2bacceleratoraddon.order.impl;


import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.b2bacceleratoraddon.base.ProcessAwareBaseTest;
import de.hybris.platform.b2bacceleratorservices.order.impl.B2BAcceleratorCartToOrderJob;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.enums.SiteChannel;
import de.hybris.platform.commerceservices.order.impl.DefaultCommerceCheckoutService;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.cronjob.enums.DayOfWeek;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.TriggerModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.orderscheduling.model.CartToOrderCronJobModel;
import de.hybris.platform.payment.commands.factory.CommandFactoryRegistry;
import de.hybris.platform.payment.impl.DefaultPaymentServiceImpl;
import de.hybris.platform.payment.methods.impl.DefaultCardPaymentServiceImpl;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.datasetup.ServiceLayerDataSetup;
import de.hybris.platform.servicelayer.internal.model.ServicelayerJobModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;


@ContextConfiguration(locations =
{ "classpath:/payment-spring-test.xml" })
@IntegrationTest
public class B2BAcceleratorCartToOrderJobTest extends ProcessAwareBaseTest
{
	@Resource
	private CronJobService cronJobService;
	@Resource
	private ModelService modelService;
	@Resource
	private CartService cartService;
	@Resource
	private FlexibleSearchService flexibleSearchService;
	@Resource
	private UserService userService;
	@Resource
	private ServiceLayerDataSetup serviceLayerDataSetup;
	@Resource
	private B2BAcceleratorCartToOrderJob b2bAcceleratorCartToOrderJob;
	@Resource
	private BusinessProcessService businessProcessService;
	@Resource
	private CommandFactoryRegistry mockupCommandFactoryRegistry;
	@Resource
	private DefaultCardPaymentServiceImpl cardPaymentService;
	@Resource
	private DefaultPaymentServiceImpl paymentService;
	@Resource
	private DefaultCommerceCheckoutService commerceCheckoutService;
	@Resource
	private BaseSiteService baseSiteService;
	@Resource
	private BaseStoreService baseStoreService;

	TriggerModel triggerModel = null;
	CartToOrderCronJobModel cartToOrderCronJob = null;

	@Before
	public void beforeTest() throws Exception
	{
		// inject a mock payment provider
		cardPaymentService.setCommandFactoryRegistry(mockupCommandFactoryRegistry);
		paymentService.setCardPaymentService(cardPaymentService);
		commerceCheckoutService.setPaymentService(paymentService);

		createCoreData();
		createDefaultCatalog();

		importCsv("/b2bacceleratoraddon/test/testOrganizations.csv", "utf-8");
		importCsv("/b2bacceleratoraddon/test/testB2BCommerceCart.csv", "utf-8");

		final BaseSiteModel site = baseSiteService.getBaseSiteForUID("b2bstoretemplate");
		site.setChannel(SiteChannel.B2C);
		Assert.assertNotNull("no site found for id 'b2bstoretemplate'", site);
		baseSiteService.setCurrentBaseSite(site, false);
		Assert.assertNotNull("Base Store is null", baseStoreService.getCurrentBaseStore());

		final CartModel modelByExample = new CartModel();
		modelByExample.setCode("dc_shhCart_b2bas");
		final CartModel cart = flexibleSearchService.getModelByExample(modelByExample);
		Assert.assertNotNull(cart);
		cart.setSite(site);
		modelService.save(cart);
		cartService.setSessionCart(cart);
		userService.setCurrentUser(cart.getUser());


		final Date startDate = new Date();
		final Integer day = Integer.valueOf(5);
		final Integer week = Integer.valueOf(2);
		final List<DayOfWeek> days = new ArrayList<DayOfWeek>();
		days.add(DayOfWeek.TUESDAY);
		days.add(DayOfWeek.FRIDAY);
		triggerModel = modelService.create(TriggerModel.class);
		triggerModel.setRelative(Boolean.TRUE);
		triggerModel.setActivationTime(startDate);
		triggerModel.setDay(day);
		triggerModel.setWeekInterval(week);
		triggerModel.setDaysOfWeek(days);

		if (flexibleSearchService
				.search(
						"SELECT {" + ServicelayerJobModel.PK + "} FROM {" + ServicelayerJobModel._TYPECODE + "} WHERE " + "{"
								+ ServicelayerJobModel.SPRINGID + "}=?springid",
						Collections.singletonMap("springid", "b2bAcceleratorCartToOrderJob")).getResult().isEmpty())
		{
			final ServicelayerJobModel servicelayerJobModel = modelService.create(ServicelayerJobModel.class);
			servicelayerJobModel.setCode("b2bAcceleratorCartToOrderJob");
			servicelayerJobModel.setSpringId("b2bAcceleratorCartToOrderJob");
			modelService.save(servicelayerJobModel);
		}
		cartToOrderCronJob = modelService.create(CartToOrderCronJobModel.class);
		cartToOrderCronJob.setCart(cartService.getSessionCart());
		cartToOrderCronJob.setDeliveryAddress(userService.getCurrentUser().getDefaultShipmentAddress());
		cartToOrderCronJob.setPaymentAddress(userService.getCurrentUser().getDefaultPaymentAddress());
		cartToOrderCronJob.setPaymentInfo(cartService.getSessionCart().getPaymentInfo());
		setCronJobToTrigger(cartToOrderCronJob, Collections.singletonList(triggerModel));
		cartToOrderCronJob.setJob(cronJobService.getJob("b2bAcceleratorCartToOrderJob"));
		modelService.save(cartToOrderCronJob);

	}

	@Test
	public void testPerformCartToOrderJob() throws Exception
	{
		Assert.assertNotNull("cart not null", cartService.getSessionCart());
		Assert.assertNotNull("user not null", cartService.getSessionCart().getUser());
		Assert.assertEquals("DC S No", cartService.getSessionCart().getUser().getUid());
		cronJobService.performCronJob(cartToOrderCronJob, true);
		waitForProcessToBeCreated("order-process", 5000);
		this.modelService.refresh(cartToOrderCronJob);
		Assert.assertTrue(cronJobService.isSuccessful(cartToOrderCronJob));
		waitForProcessToEnd("order-process", 15000);
	}

	protected void setCronJobToTrigger(final CronJobModel cronJob, final List<TriggerModel> triggers)
	{
		for (final TriggerModel trigger : triggers)
		{
			trigger.setCronJob(cronJob);
		}
		cronJob.setTriggers(triggers);
	}
}

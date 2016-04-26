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
package de.hybris.platform.b2bacceleratoraddon.btg;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.b2bacceleratoraddon.model.btg.OrganizationOrdersReportingCronJobModel;
import de.hybris.platform.basecommerce.util.BaseCommerceBaseTest;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.internal.model.ServicelayerJobModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Collections;
import java.util.Date;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;


@IntegrationTest
@ContextConfiguration(locations =
{ "classpath:/b2bacceleratoraddon-spring-test.xml" })
public class OrganizationOrdersReportingJobTest extends BaseCommerceBaseTest
{
	@Resource
	OrganizationOrdersReportingJob organizationOrdersReportingTestJob;
	@Resource
	UserService userService;
	@Resource
	ModelService modelService;
	@Resource
	CommonI18NService commonI18NService;
	@Resource
	CronJobService cronJobService;
	@Resource
	FlexibleSearchService flexibleSearchService;

	final String jobBeanId = "organizationOrdersReportingTestJob";

	@Before
	public void setUp() throws Exception
	{
		if (flexibleSearchService
				.search(
						"SELECT {" + ServicelayerJobModel.PK + "} FROM {" + ServicelayerJobModel._TYPECODE + "} WHERE " + "{"
								+ ServicelayerJobModel.SPRINGID + "}=?springid", Collections.singletonMap("springid", jobBeanId))
				.getResult().isEmpty())
		{
			final ServicelayerJobModel servicelayerJobModel = modelService.create(ServicelayerJobModel.class);
			servicelayerJobModel.setCode(jobBeanId);
			servicelayerJobModel.setSpringId(jobBeanId);
			modelService.save(servicelayerJobModel);
		}

		createCoreData();
		createDefaultCatalog();

		importCsv("/b2bacceleratoraddon/test/testOrganizations.csv", "utf-8");
		importCsv("/b2bacceleratoraddon/test/testB2BCommerceCart.csv", "utf-8");

		final UserModel user = userService.getCurrentUser();
		final OrderModel testOrder = modelService.create(OrderModel.class);

		testOrder.setCode("organizationOrdersReportingJob test" + System.currentTimeMillis());
		testOrder.setUser(user);
		testOrder.setCurrency(commonI18NService.getCurrency("USD"));
		testOrder.setDate(new Date());
		testOrder.setNet(Boolean.FALSE);
		testOrder.setTotalPrice(Double.valueOf(1000D));
		modelService.save(testOrder);
	}


	@Test
	public void testOrganizationOrdersReportingJob() throws Exception
	{
		final OrganizationOrdersReportingCronJobModel cronjob = modelService.create(OrganizationOrdersReportingCronJobModel.class);
		cronjob.setJob(cronJobService.getJob(jobBeanId));
		final PerformResult performResult = organizationOrdersReportingTestJob.perform(cronjob);

		Assert.assertEquals(CronJobResult.SUCCESS, performResult.getResult());
		Assert.assertEquals(CronJobStatus.FINISHED, performResult.getStatus());
	}
}

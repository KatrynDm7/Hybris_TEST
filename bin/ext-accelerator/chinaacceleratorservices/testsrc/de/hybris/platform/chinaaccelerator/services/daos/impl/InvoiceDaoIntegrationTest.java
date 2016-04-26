/*
 *
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
 */
package de.hybris.platform.chinaaccelerator.services.daos.impl;

import static org.junit.Assert.assertEquals;

import de.hybris.platform.chinaaccelerator.services.enums.InvoiceCategory;
import de.hybris.platform.chinaaccelerator.services.enums.InvoiceTitle;
import de.hybris.platform.chinaaccelerator.services.model.invoice.InvoiceModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import javax.annotation.Resource;

import org.junit.Test;


public class InvoiceDaoIntegrationTest extends ServicelayerTransactionalTest
{
	@Resource
	private ModelService modelService;

	@Resource
	private FlexibleSearchService flexibleSearchService;



	@Test
	public void extendAttributesTest()
	{
		final int OriginCount = flexibleSearchService.search("select {pk} from {Invoice} ").getCount();


		final InvoiceModel pos1 = new InvoiceModel();
		pos1.setCategory(InvoiceCategory.FOOD);
		pos1.setInvoicedName("TEST-MSG");
		pos1.setTitle(InvoiceTitle.CORPS);
		modelService.save(pos1);
		final int currentCount = flexibleSearchService.search("select {pk} from {Invoice} ").getCount();

		assertEquals(OriginCount + 1, currentCount);
	}


}

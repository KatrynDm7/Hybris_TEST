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
package de.hybris.platform.financialfacades.process.xslfo.context;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.insurance.data.InsurancePolicyData;
import de.hybris.platform.commercefacades.insurance.data.PolicyHolderDetailData;
import de.hybris.platform.commercefacades.product.data.CategoryData;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;


/**
 * Event Policy Context for populating Travel templates
 */
public class EventPolicyContext extends AbstractPolicyContext
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(EventPolicyContext.class);

	@Override
	public void init(final BaseSiteModel baseSite, final InsurancePolicyData policyData)
	{
		super.init(baseSite, policyData);

		final PolicyHolderDetailData policyHolderDetail = policyData.getPolicyHolderDetail();
		if (policyHolderDetail != null)
		{
			if (policyHolderDetail.getTitle() != null)
			{
				put("title", StringEscapeUtils.escapeXml(policyHolderDetail.getTitle()));
			}
			put("firstName", StringEscapeUtils.escapeXml(policyHolderDetail.getFirstName()));
			put("lastName", StringEscapeUtils.escapeXml(policyHolderDetail.getLastName()));
		}

		final CategoryData categoryData = policyData.getCategoryData();
		if (categoryData != null)
		{
			put("categoryName", StringEscapeUtils.escapeXml(categoryData.getName()));
			put("categoryCode", StringEscapeUtils.escapeXml(categoryData.getCode()));
		}

		put("policyNumber", StringEscapeUtils.escapeXml(policyData.getPolicyNumber()));
		if (policyData.getRecurringPrice() != null)
		{
			put("recurringPrice", StringEscapeUtils.escapeXml(policyData.getRecurringPrice().getFormattedValue()));
		}
		if (policyData.getOneTimePrice() != null)
		{
			put("oneTimePrice", StringEscapeUtils.escapeXml(policyData.getOneTimePrice().getFormattedValue()));
		}
		put("periodRetrievedFrom", StringEscapeUtils.escapeXml(policyData.getPeriodRetrievedFrom()));
		put("durationRetrievedFrom", StringEscapeUtils.escapeXml(policyData.getDurationRetrievedFrom()));
		put("benefits", policyData.getMainProduct().getBenefits());
		put("mainProduct", policyData.getMainProduct());
		put("optionalProducts", policyData.getOptionalProducts());
	}
}

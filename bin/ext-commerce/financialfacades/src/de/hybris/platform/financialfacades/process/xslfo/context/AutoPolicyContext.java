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
import de.hybris.platform.commercefacades.insurance.data.AutoDetailData;
import de.hybris.platform.commercefacades.insurance.data.InsurancePolicyData;
import de.hybris.platform.commercefacades.insurance.data.PolicyHolderDetailData;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;


/**
 * Event Policy Context for populating Travel templates
 */
public class AutoPolicyContext extends AbstractPolicyContext
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(AutoPolicyContext.class);

	@Override
	public void init(final BaseSiteModel baseSite, final InsurancePolicyData policyData)
	{
		super.init(baseSite, policyData);

		final PolicyHolderDetailData policyHolderDetail = policyData.getPolicyHolderDetail();

		final AutoDetailData autoDetail = policyData.getAutoDetail();

		put("autoRegistration", StringEscapeUtils.escapeXml(autoDetail.getAutoLicense()));
		put("autoYear", StringEscapeUtils.escapeXml(autoDetail.getAutoYear()));
		put("autoMake", StringEscapeUtils.escapeXml(autoDetail.getAutoMake()));
		put("autoModel", StringEscapeUtils.escapeXml(autoDetail.getAutoModel()));
		put("autoPrice", StringEscapeUtils.escapeXml(autoDetail.getAutoPrice()));
		put("autoState", StringEscapeUtils.escapeXml(autoDetail.getAutoState()));
		put("autoFormattedDateOfBirth", StringEscapeUtils.escapeXml(autoDetail.getAutoFormattedDateOfBirth()));
	}
}

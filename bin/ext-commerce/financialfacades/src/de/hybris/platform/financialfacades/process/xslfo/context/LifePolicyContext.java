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
import de.hybris.platform.commercefacades.insurance.data.LifeDetailData;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;


/**
 * Life Policy Context for populating life insurance templates
 */
public class LifePolicyContext extends AbstractPolicyContext
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(LifePolicyContext.class);

	@Override
	public void init(final BaseSiteModel baseSite, final InsurancePolicyData policyData)
	{
		super.init(baseSite, policyData);

		final LifeDetailData lifeDetailData = policyData.getLifeDetail();

		put("coverageAmount", StringEscapeUtils.escapeXml(lifeDetailData.getLifeCoverageAmount()));
		put("lifeCoverageStartDate", StringEscapeUtils.escapeXml(lifeDetailData.getLifeCoverStartDate()));
		put("coverageLength", StringEscapeUtils.escapeXml(lifeDetailData.getLifeCoverageLast()));

		put("mainApplicantDateOfBirth", StringEscapeUtils.escapeXml(lifeDetailData.getLifeMainDob()));
		put("mainApplicantSmoker", StringEscapeUtils.escapeXml(lifeDetailData.getLifeMainSmoke()));

		put("secondApplicantDateOfBirth", StringEscapeUtils.escapeXml(lifeDetailData.getLifeSecondDob()));
		put("secondApplicantSmoker", StringEscapeUtils.escapeXml(lifeDetailData.getLifeSecondSmoke()));
	}
}

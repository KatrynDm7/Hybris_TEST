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

import de.hybris.platform.acceleratorservices.urlencoder.UrlEncoderService;
import de.hybris.platform.acceleratorservices.urlresolver.SiteBaseUrlResolutionService;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.insurance.data.InsurancePolicyData;
import de.hybris.platform.commercefacades.insurance.data.PolicyHolderDetailData;
import de.hybris.platform.commercefacades.product.data.CategoryData;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.velocity.VelocityContext;


/**
 * Abstract Policy Context
 */
public abstract class AbstractPolicyContext extends VelocityContext
{
	public static final String BASE_URL = "baseUrl";
	public static final String SECURE_BASE_URL = "secureBaseUrl";
	public static final String MEDIA_BASE_URL = "mediaBaseUrl";
	public static final String MEDIA_SECURE_BASE_URL = "mediaSecureBaseUrl";
	public static final String THEME = "theme";
	public static final String BASE_SITE = "baseSite";
	public static final String BASE_THEME_URL = "baseThemeUrl";
	public static final String URL_RESOLUTION_SERVICE = "urlResolutionService";
	private String urlEncodingAttributes;

	private SiteBaseUrlResolutionService siteBaseUrlResolutionService;
	private UrlEncoderService urlEncoderService;


	public void init(final BaseSiteModel baseSite, final InsurancePolicyData policyData)
	{
		put(BASE_SITE, baseSite);
		setUrlEncodingAttributes(getUrlEncoderService().getUrlEncodingPattern());
		final SiteBaseUrlResolutionService siteBaseUrlResolutionService = getSiteBaseUrlResolutionService();
		// Lookup the site specific URLs
		put(BASE_URL, siteBaseUrlResolutionService.getWebsiteUrlForSite(baseSite, getUrlEncodingAttributes(), false, ""));
		put(BASE_THEME_URL, siteBaseUrlResolutionService.getWebsiteUrlForSite(baseSite, "", false, ""));
		put(SECURE_BASE_URL, siteBaseUrlResolutionService.getWebsiteUrlForSite(baseSite, getUrlEncodingAttributes(), true, ""));
		put(MEDIA_BASE_URL, siteBaseUrlResolutionService.getMediaUrlForSite(baseSite, false));
		put(MEDIA_SECURE_BASE_URL, siteBaseUrlResolutionService.getMediaUrlForSite(baseSite, true));
		put(THEME, baseSite.getTheme() != null ? baseSite.getTheme().getCode() : null);
		//insert services for usage at jsp/vm page
		put(URL_RESOLUTION_SERVICE, getSiteBaseUrlResolutionService());


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

		put("startDate", StringEscapeUtils.escapeXml(policyData.getPolicyStartDate()));

		put("policyNumber", StringEscapeUtils.escapeXml(policyData.getPolicyNumber()));

		put("benefits", policyData.getMainProduct().getBenefits());
		put("mainProduct", policyData.getMainProduct());
		put("optionalProducts", policyData.getOptionalProducts());

		if (policyData.getRecurringPrice() != null)
		{
			put("recurringPrice", StringEscapeUtils.escapeXml(policyData.getRecurringPrice().getFormattedValue()));
		}
		if (policyData.getOneTimePrice() != null)
		{
			put("oneTimePrice", StringEscapeUtils.escapeXml(policyData.getOneTimePrice().getFormattedValue()));
		}
	}

	public BaseSiteModel getBaseSite()
	{
		return (BaseSiteModel) get(BASE_SITE);
	}

	protected SiteBaseUrlResolutionService getSiteBaseUrlResolutionService()
	{
		return siteBaseUrlResolutionService;
	}

	public void setSiteBaseUrlResolutionService(final SiteBaseUrlResolutionService siteBaseUrlResolutionService)
	{
		this.siteBaseUrlResolutionService = siteBaseUrlResolutionService;
	}

	protected String getUrlEncodingAttributes()
	{
		return urlEncodingAttributes;
	}

	public void setUrlEncodingAttributes(final String urlEncodingAttributes)
	{
		this.urlEncodingAttributes = urlEncodingAttributes;
	}

	protected UrlEncoderService getUrlEncoderService()
	{
		return urlEncoderService;
	}

	public void setUrlEncoderService(final UrlEncoderService urlEncoderService)
	{
		this.urlEncoderService = urlEncoderService;
	}
}

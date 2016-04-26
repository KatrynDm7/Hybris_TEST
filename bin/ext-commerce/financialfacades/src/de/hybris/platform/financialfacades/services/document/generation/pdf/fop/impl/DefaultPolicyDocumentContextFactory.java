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
package de.hybris.platform.financialfacades.services.document.generation.pdf.fop.impl;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.insurance.data.InsurancePolicyData;
import de.hybris.platform.commons.model.renderer.RendererTemplateModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.financialfacades.process.xslfo.context.AbstractPolicyContext;
import de.hybris.platform.financialfacades.services.document.generation.pdf.fop.PolicyDocumentContextFactory;
import de.hybris.platform.site.BaseSiteService;

import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;


/**
 * Default Policy Document Context Factory used to create velocity context for rendering policy documents
 */
public class DefaultPolicyDocumentContextFactory implements PolicyDocumentContextFactory
{
	private static final Logger LOG = Logger.getLogger(DefaultPolicyDocumentContextFactory.class);
	BaseSiteService baseSiteService;

	/**
	 * Create a velocity context for the policy documents with given policy model.
	 */
	@Override
	public VelocityContext create(final InsurancePolicyData policyData, final RendererTemplateModel renderTemplate)
	{
		final BaseSiteModel baseSite = baseSiteService.getCurrentBaseSite();

		final AbstractPolicyContext context = resolveContext(renderTemplate);
		context.init(baseSite, policyData);
		return context;
	}

	protected <T extends VelocityContext> T resolveContext(final RendererTemplateModel renderTemplate) throws RuntimeException
	{
		try
		{
			final Class<T> contextClass = (Class<T>) Class.forName(renderTemplate.getContextClass());
			final Map<String, T> context = getApplicationContext().getBeansOfType(contextClass);
			if (MapUtils.isNotEmpty(context))
			{
				return context.entrySet().iterator().next().getValue();
			}
			else
			{
				throw new RuntimeException("Cannot find bean in application context for context class [" + contextClass + "]");
			}
		}
		catch (final ClassNotFoundException e)
		{
			LOG.error("failed to create policy context", e);
			throw new RuntimeException("Cannot find policy context class", e);
		}
	}

	protected ApplicationContext getApplicationContext()
	{
		return Registry.getApplicationContext();
	}

	protected BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	@Required
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}
}

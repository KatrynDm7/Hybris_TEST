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
package de.hybris.platform.commerceservices.search.solrfacetsearch.populators;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;

import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;


public class CommerceIndexConfigPopulator implements Populator<SolrFacetSearchConfigModel, IndexConfig>
{
	private BaseSiteService baseSiteService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final SolrFacetSearchConfigModel source, final IndexConfig target) throws ConversionException
	{
		target.setBaseSite(findBaseSiteForConfig(source));
	}

	protected BaseSiteModel findBaseSiteForConfig(final SolrFacetSearchConfigModel configModel)
	{
		final Collection<BaseSiteModel> allBaseSites = getBaseSiteService().getAllBaseSites();
		if (CollectionUtils.isNotEmpty(allBaseSites))
		{
			for (final BaseSiteModel site : allBaseSites)
			{
				if (configModel.equals(site.getSolrFacetSearchConfiguration()))
				{
					return site;
				}
			}
		}
		return null;
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

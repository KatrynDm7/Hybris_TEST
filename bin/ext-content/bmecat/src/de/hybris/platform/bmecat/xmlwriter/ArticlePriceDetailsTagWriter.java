/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 * 
 *  
 */
package de.hybris.platform.bmecat.xmlwriter;

import de.hybris.platform.bmecat.constants.BMECatConstants;
import de.hybris.platform.europe1.constants.Europe1Constants;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.enumeration.EnumerationValue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.znerd.xmlenc.XMLOutputter;


/**
 * Writes the &lt;ArticlePriceDetails&gt; tag
 * 
 * 
 */
@SuppressWarnings("deprecation")
public class ArticlePriceDetailsTagWriter extends XMLTagWriter
{
	/**
	 * @param parent
	 */
	public ArticlePriceDetailsTagWriter(final ArticleTagWriter parent)
	{
		super(parent, true);
		final Map typeMap = new HashMap();
		typeMap.put("net_list", new ArticlePriceTagWriter(this, "net_list"));
		typeMap.put("gros_list", new ArticlePriceTagWriter(this, "gros_list"));
		typeMap.put("net_customer", new ArticlePriceTagWriter(this, "net_customer"));
		typeMap.put("nrp", new ArticlePriceTagWriter(this, "nrp"));
		typeMap.put("net_customer_exp", new ArticlePriceTagWriter(this, "net_customer_exp"));
		for (final Iterator it = EnumerationManager.getInstance().getEnumerationType(Europe1Constants.TYPES.PRICE_USER_GROUP)
				.getValues().iterator(); it.hasNext();)
		{
			final EnumerationValue userPriceGroup = (EnumerationValue) it.next();
			final String qualifier = "udp_" + userPriceGroup.getCode();
			typeMap.put(qualifier, new ArticlePriceTagWriter(this, qualifier));
		}
		addSubTagWriter(BMECatConstants.XML.TAG.ARTICLE_PRICE, typeMap);
	}

	/**
	 * @see de.hybris.platform.bmecat.xmlwriter.XMLTagWriter#getTagName()
	 */
	@Override
	protected String getTagName()
	{
		return BMECatConstants.XML.TAG.ARTICLE_PRICE_DETAILS;
	}

	/**
	 * @see de.hybris.platform.bmecat.xmlwriter.XMLTagWriter#writeContent(org.znerd.xmlenc.XMLOutputter,
	 *      java.lang.Object)
	 */
	@Override
	protected void writeContent(final XMLOutputter xmlOut, final Object object) throws IOException
	{
		final ArticlePriceDetailsContext ctx = (ArticlePriceDetailsContext) object;
		for (final Iterator it = ctx.getArticlePriceContexts().iterator(); it.hasNext();)
		{
			final ArticlePriceTagWriter.ArticlePriceContext aCtx = (ArticlePriceTagWriter.ArticlePriceContext) it.next();
			final ArticlePriceTagWriter tagWriter = (ArticlePriceTagWriter) getSubTagWriter(BMECatConstants.XML.TAG.ARTICLE_PRICE,
					aCtx.getType());
			tagWriter.write(xmlOut, aCtx);
		}
	}

	public static class ArticlePriceDetailsContext
	{
		private final Collection articlePriceContexts = new ArrayList();
		private final Date validStartDate;
		private final Date validEndDate;

		public ArticlePriceDetailsContext()
		{
			this(null, null);
		}

		public ArticlePriceDetailsContext(final Date validStartDate, final Date validEndDate)
		{
			this.validStartDate = validStartDate;
			this.validEndDate = validEndDate;
		}

		/**
		 * @return Returns the articlePriceContexts.
		 */
		public Collection getArticlePriceContexts()
		{
			return articlePriceContexts;
		}

		public void addArticlePriceContext(final ArticlePriceTagWriter.ArticlePriceContext ctx)
		{
			getArticlePriceContexts().add(ctx);
		}

		/**
		 * @return Returns the validEndDate.
		 */
		public Date getValidEndDate()
		{
			return validEndDate;
		}

		/**
		 * @return Returns the validStartDate.
		 */
		public Date getValidStartDate()
		{
			return validStartDate;
		}
	}

}

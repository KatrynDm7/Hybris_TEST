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

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.znerd.xmlenc.XMLOutputter;


/**
 * Writes the &lt;Variants&gt; tag
 * 
 * 
 */
@SuppressWarnings("deprecation")
public class VariantsTagWriter extends XMLTagWriter
{
	/**
	 * @param parent
	 */
	public VariantsTagWriter(final XMLTagWriter parent)
	{
		super(parent);
		addSubTagWriter(new NumberTagWriter(this, BMECatConstants.XML.TAG.VORDER, true));
		addSubTagWriter(new VariantTagWriter(this));
	}

	/**
	 * @see de.hybris.platform.bmecat.xmlwriter.XMLTagWriter#getTagName()
	 */
	@Override
	protected String getTagName()
	{
		return BMECatConstants.XML.TAG.VARIANTS;
	}

	/**
	 * @see de.hybris.platform.bmecat.xmlwriter.XMLTagWriter#writeContent(org.znerd.xmlenc.XMLOutputter,
	 *      java.lang.Object)
	 */
	@Override
	protected void writeContent(final XMLOutputter xmlOut, final Object object) throws IOException
	{
		final VariantsContext vCtx = (VariantsContext) object;

		final Collection variantContexts = vCtx.getVariantContexts();
		for (final Iterator it = variantContexts.iterator(); it.hasNext();)
		{
			getSubTagWriter(BMECatConstants.XML.TAG.VARIANT).write(xmlOut, it.next());
		}
		getSubTagWriter(BMECatConstants.XML.TAG.VORDER).write(xmlOut, Integer.valueOf(vCtx.getVOrder()));
	}

	/**
	 * 
	 */
	public static class VariantsContext
	{
		private final Collection variantContexts;
		private final int vOrder;

		/**
		 * @param variantContexts
		 * @param vOrder
		 */
		public VariantsContext(final Collection variantContexts, final int vOrder)
		{
			this.variantContexts = variantContexts;
			this.vOrder = vOrder;
		}

		/**
		 * @return Returns the variantContexts.
		 */
		public Collection getVariantContexts()
		{
			return variantContexts;
		}

		/**
		 * @return Returns the vOrder.
		 */
		public int getVOrder()
		{
			return vOrder;
		}
	}

}

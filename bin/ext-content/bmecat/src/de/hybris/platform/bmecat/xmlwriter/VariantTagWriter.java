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

import org.znerd.xmlenc.XMLOutputter;


/**
 * Writes the &lt;Variant&gt; tag
 * 
 * 
 */
@SuppressWarnings("deprecation")
public class VariantTagWriter extends XMLTagWriter
{
	/**
	 * @param parent
	 */
	public VariantTagWriter(final XMLTagWriter parent)
	{
		super(parent, true);
		addSubTagWriter(new SimpleTagWriter(this, BMECatConstants.XML.TAG.FVALUE, true));
		addSubTagWriter(new SimpleTagWriter(this, BMECatConstants.XML.TAG.SUPPLIER_AID_SUPPLEMENT, true));
	}

	/**
	 * @see de.hybris.platform.bmecat.xmlwriter.XMLTagWriter#getTagName()
	 */
	@Override
	protected String getTagName()
	{
		return BMECatConstants.XML.TAG.VARIANT;
	}

	/**
	 * @see de.hybris.platform.bmecat.xmlwriter.XMLTagWriter#writeContent(org.znerd.xmlenc.XMLOutputter,
	 *      java.lang.Object)
	 */
	@Override
	protected void writeContent(final XMLOutputter xmlOut, final Object object) throws IOException
	{
		final VariantContext vCtx = (VariantContext) object;
		getSubTagWriter(BMECatConstants.XML.TAG.FVALUE).write(xmlOut, vCtx.getFValue());
		getSubTagWriter(BMECatConstants.XML.TAG.SUPPLIER_AID_SUPPLEMENT).write(xmlOut, vCtx.getSupplierIdSupplement());
	}

	/**
	 * 
	 */
	public static class VariantContext
	{
		private final String fValue;
		private final String supplierIdSupplement;

		/**
		 * @param fValue
		 * @param supplierIdSupplement
		 */
		public VariantContext(final String fValue, final String supplierIdSupplement)
		{
			this.fValue = fValue;
			this.supplierIdSupplement = supplierIdSupplement;
		}

		/**
		 * @return Returns the fValue.
		 */
		public String getFValue()
		{
			return fValue;
		}

		/**
		 * @return Returns the supplierIdSupplement.
		 */
		public String getSupplierIdSupplement()
		{
			return supplierIdSupplement;
		}
	}

}

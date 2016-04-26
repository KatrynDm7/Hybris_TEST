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
package de.hybris.platform.bmecat.parser.taglistener;

import de.hybris.bootstrap.xml.TagListener;
import de.hybris.platform.bmecat.constants.BMECatConstants;
import de.hybris.platform.bmecat.parser.BMECatObjectProcessor;
import de.hybris.platform.bmecat.parser.Variant;

import java.util.Arrays;
import java.util.Collection;


/**
 * Parses the &lt;Variant&gt; tag
 * 
 * 
 * 
 */
public class VariantTagListener extends DefaultBMECatTagListener
{

	/**
	 * @param parent
	 */
	public VariantTagListener(final TagListener parent)
	{
		super(parent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.bmecat.parser.taglistener.DefaultBMECatTagListener#processEndElement(de.hybris.platform.bmecat
	 * .parser.BMECatObjectProcessor)
	 */
	@Override
	protected Object processEndElement(final BMECatObjectProcessor processor)
	{
		final Variant variant = new Variant();
		variant.setFvalue((String) getSubTagValue(BMECatConstants.XML.TAG.FVALUE));
		variant.setSupplierAidSupplement((String) getSubTagValue(BMECatConstants.XML.TAG.SUPPLIER_AID_SUPPLEMENT));
		return variant;
	}

	@Override
	protected Collection createSubTagListeners()
	{
		return Arrays.asList(new TagListener[]
		{ new StringValueTagListener(this, BMECatConstants.XML.TAG.FVALUE),
				new StringValueTagListener(this, BMECatConstants.XML.TAG.SUPPLIER_AID_SUPPLEMENT) });
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.bmecat.parser.taglistener.TagListener#getTagName()
	 */
	public String getTagName()
	{
		return BMECatConstants.XML.TAG.VARIANT;
	}

}

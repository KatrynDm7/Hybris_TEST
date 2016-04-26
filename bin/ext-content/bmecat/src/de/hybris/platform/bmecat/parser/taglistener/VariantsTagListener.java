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
import de.hybris.platform.bmecat.parser.Variants;

import java.util.Arrays;
import java.util.Collection;


/**
 * Parses the &lt;Variants&gt; tag
 * 
 * 
 * 
 */
public class VariantsTagListener extends DefaultBMECatTagListener
{

	/**
	 * @param parent
	 */
	public VariantsTagListener(final TagListener parent)
	{
		super(parent);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Collection createSubTagListeners()
	{
		return Arrays.asList(new TagListener[]
		{ new VariantTagListener(this), new IntegerValueTagListener(this, BMECatConstants.XML.TAG.VORDER) });
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
		final Variants variants = new Variants();
		variants.setVariants(getSubTagValueCollection(BMECatConstants.XML.TAG.VARIANT));
		variants.setVorder((Integer) getSubTagValue(BMECatConstants.XML.TAG.VORDER));
		return variants;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.bmecat.parser.taglistener.TagListener#getTagName()
	 */
	public String getTagName()
	{
		return BMECatConstants.XML.TAG.VARIANTS;
	}

}

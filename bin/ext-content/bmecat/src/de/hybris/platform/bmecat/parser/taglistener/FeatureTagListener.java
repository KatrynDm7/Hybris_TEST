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
import de.hybris.platform.bmecat.parser.Feature;
import de.hybris.platform.bmecat.parser.Variants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;


/**
 * Parses the &lt;Feature&gt; tag
 * 
 * 
 * 
 */
public class FeatureTagListener extends DefaultBMECatTagListener
{

	/**
	 * @param parent
	 */
	public FeatureTagListener(final TagListener parent)
	{
		super(parent);
	}

	@Override
	protected Collection createSubTagListeners()
	{
		return Arrays.asList(new TagListener[]
		{ new StringValueTagListener(this, BMECatConstants.XML.TAG.FNAME),
				new StringValueTagListener(this, BMECatConstants.XML.TAG.FVALUE),
				new StringValueTagListener(this, BMECatConstants.XML.TAG.FUNIT),
				new StringValueTagListener(this, BMECatConstants.XML.TAG.FDESCR),
				new IntegerValueTagListener(this, BMECatConstants.XML.TAG.FORDER),
				new StringValueTagListener(this, BMECatConstants.XML.TAG.FVALUE_DETAILS), new VariantsTagListener(this) });
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
		final Feature feature = new Feature();
		feature.setFname((String) getSubTagValue(BMECatConstants.XML.TAG.FNAME));
		feature.setFunit((String) getSubTagValue(BMECatConstants.XML.TAG.FUNIT));

		final Collection old_values = getSubTagValueCollection(BMECatConstants.XML.TAG.FVALUE);
		final Collection values = new ArrayList();
		for (final Iterator iter = old_values.iterator(); iter.hasNext();)
		{
			final String element = (String) iter.next();
			if (element.length() > 0 && !element.equalsIgnoreCase("-"))
			{
				values.add(element);
			}

		}
		feature.setVariants((Variants) getSubTagValue(BMECatConstants.XML.TAG.VARIANTS));
		if (values.isEmpty() && feature.getVariants() == null)
		{
			return null; //TODO XXX add log entry if feature is removed		
		}

		//old
		//Collection values=getSubTagValueCollection(BMECatConstants.XML.TAG.FVALUE);
		feature.setFvalues((String[]) values.toArray(new String[values.size()]));
		feature.setDescription((String) getSubTagValue(BMECatConstants.XML.TAG.FDESCR));
		feature.setValueDetails((String) getSubTagValue(BMECatConstants.XML.TAG.FVALUE_DETAILS));
		feature.setForder((Integer) getSubTagValue(BMECatConstants.XML.TAG.FORDER));
		return feature;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.bmecat.parser.taglistener.TagListener#getTagName()
	 */
	public String getTagName()
	{
		return BMECatConstants.XML.TAG.FEATURE;
	}

}

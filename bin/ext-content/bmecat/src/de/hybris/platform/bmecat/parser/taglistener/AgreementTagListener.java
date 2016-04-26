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
import de.hybris.platform.bmecat.parser.Agreement;
import de.hybris.platform.bmecat.parser.BMECatObjectProcessor;
import de.hybris.platform.bmecat.parser.DateTime;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;


/**
 * Parses the &lt;Agreement&gt; tag
 * 
 * 
 * 
 */
public class AgreementTagListener extends DefaultBMECatTagListener
{

	public AgreementTagListener(final TagListener parent)
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
	protected Collection createSubTagListeners()
	{
		return Arrays.asList(new TagListener[]
		{ new StringValueTagListener(this, BMECatConstants.XML.TAG.AGREEMENT_ID), new DateTimeValueTagListener(this), });
	}

	@Override
	public Object processEndElement(final BMECatObjectProcessor processor)
	{
		final Agreement agr = new Agreement();
		final Collection dates = getSubTagValueCollection(BMECatConstants.XML.TAG.DATETIME);
		for (final Iterator it = dates.iterator(); it.hasNext();)
		{
			final DateTime dateTime = (DateTime) it.next();
			final String type = dateTime.getType();
			if (type.equals(DateTime.TYPE_AGREEMENT_START_DATE))
			{
				agr.setStartDate(dateTime);
			}
			else if (type.equals(DateTime.TYPE_AGREEMENT_END_DATE))
			{
				agr.setEndDate(dateTime);
			}
		}
		if (agr.getEndDate() == null)
		{
			throw new IllegalStateException("Missing 'agreement_end_date' definition !");
		}


		agr.setID((String) getSubTagValue(BMECatConstants.XML.TAG.AGREEMENT_ID));
		return agr;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.bmecat.parser.taglistener.TagListener#getTagName()
	 */
	public String getTagName()
	{
		return BMECatConstants.XML.TAG.AGREEMENT;
	}

}

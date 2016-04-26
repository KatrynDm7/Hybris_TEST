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
import de.hybris.platform.catalog.jalo.Agreement;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.collections.map.LinkedMap;
import org.znerd.xmlenc.XMLOutputter;


/**
 * Writes the &lt;Agreement&gt; tag
 * 
 * 
 */
@SuppressWarnings("deprecation")
public class AgreementTagWriter extends XMLTagWriter
{

	/**
	 * @param parent
	 */
	public AgreementTagWriter(final XMLTagWriter parent)
	{
		super(parent);

		addSubTagWriter(new SimpleTagWriter(this, BMECatConstants.XML.TAG.AGREEMENT_ID, true));

		final Map dateTimeTagWriterMap = new LinkedMap();
		dateTimeTagWriterMap.put("agreement_start_date", new DateTimeTagWriter(this, "agreement_start_date"));
		dateTimeTagWriterMap.put("agreement_end_date", new DateTimeTagWriter(this, "agreement_end_date", true));

		addSubTagWriter(BMECatConstants.XML.TAG.DATETIME, dateTimeTagWriterMap);

	}

	@Override
	protected void writeContent(final XMLOutputter xmlOut, final Object object) throws IOException
	{
		final Agreement agreement = (Agreement) object;
		getSubTagWriter(BMECatConstants.XML.TAG.AGREEMENT_ID).write(xmlOut, agreement.getId());
		getSubTagWriter(BMECatConstants.XML.TAG.DATETIME, "agreement_start_date").write(xmlOut, agreement.getStartdate());
		getSubTagWriter(BMECatConstants.XML.TAG.DATETIME, "agreement_end_date").write(xmlOut, agreement.getEnddate());
	}

	/**
	 * @see de.hybris.platform.bmecat.xmlwriter.XMLTagWriter#getTagName()
	 */
	@Override
	protected String getTagName()
	{
		return BMECatConstants.XML.TAG.AGREEMENT;
	}

}

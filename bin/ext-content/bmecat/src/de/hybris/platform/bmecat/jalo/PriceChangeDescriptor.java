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
package de.hybris.platform.bmecat.jalo;

import org.apache.log4j.Logger;


/**
 * PriceChangeDescriptor
 */
public class PriceChangeDescriptor extends GeneratedPriceChangeDescriptor
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(PriceChangeDescriptor.class.getName());

	@Override
	public String toString()
	{
		if (getImplementation() == null)
		{
			return super.toString();
		}
		final StringBuilder out = new StringBuilder("PriceChangeDescriptor");
		out.append(" type: ").append(getChangeType());
		out.append(" pricerow:").append(getPriceRow());
		out.append(" priceCopy:").append(getPriceCopy());
		out.append(" desc:").append(getDescription());
		return out.toString();
	}
}

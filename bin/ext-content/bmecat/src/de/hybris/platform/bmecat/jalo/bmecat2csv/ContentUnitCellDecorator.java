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
package de.hybris.platform.bmecat.jalo.bmecat2csv;

import de.hybris.platform.util.CSVCellDecorator;

import java.util.Map;


/**
 * The ContentUnitCellDecorator changes the given PUNIT value to the Unit value defined in the hybris system. It is in
 * fact a kind of mapping. For example, "C62" is a valid value for content unit, and it is changed to "pieces" which has
 * the real meaning.
 * 
 * 
 */
public class ContentUnitCellDecorator implements CSVCellDecorator
{

	//values are not complete, "C62" is just one of them
	//new values can be added if necessary
	public String decorate(final int position, final Map srcLine)
	{
		final String csvCell = (String) srcLine.get(Integer.valueOf(position));
		if (csvCell == null || csvCell.length() == 0)
		{
			return csvCell;
		}
		else
		{
			if ("C62".equals(csvCell))
			{
				return "pieces";
			}
			else
			{
				return csvCell;
			}
		}
	}
}

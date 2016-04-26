/*
 *  
 * [y] hybris Platform
 *  
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *  
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *  
 */
package de.hybris.platform.cuppy.enums;

import de.hybris.platform.core.HybrisEnumValue;

/**
 * Generated enum CompetitionType declared at extension cuppy.
 * <p/>
 * Sets the type of competition on which the frontend will behave slightly different.
 */
@SuppressWarnings("PMD")
public enum CompetitionType implements HybrisEnumValue
{
	/**
	 * Generated enum value for CompetitionType.league declared at extension cuppy.
	 * <p/>
	 * Competition is a league, so no tournament view will be available.
	 */
	LEAGUE("league"),
	/**
	 * Generated enum value for CompetitionType.tournament declared at extension cuppy.
	 * <p/>
	 * Competition is a tournament, so the tournament view will be available.
	 */
	TOURNAMENT("tournament");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "CompetitionType";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "CompetitionType";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private CompetitionType(final String code)
	{
		this.code = code.intern();
	}
	
	
	/**
	 * Gets the code of this enum value.
	 *  
	 * @return code of value
	 */
	@Override
	public String getCode()
	{
		return this.code;
	}
	
	/**
	 * Gets the type this enum value belongs to.
	 *  
	 * @return code of type
	 */
	@Override
	public String getType()
	{
		return SIMPLE_CLASSNAME;
	}
	
}

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
package de.hybris.platform.cuppytrail.enums;

import de.hybris.platform.core.HybrisEnumValue;

/**
 * Generated enum StadiumType declared at extension cuppytrail.
 */
@SuppressWarnings("PMD")
public enum StadiumType implements HybrisEnumValue
{
	/**
	 * Generated enum value for StadiumType.openair declared at extension cuppytrail.
	 */
	OPENAIR("openair"),
	/**
	 * Generated enum value for StadiumType.enclosed declared at extension cuppytrail.
	 */
	ENCLOSED("enclosed");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "StadiumType";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "StadiumType";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private StadiumType(final String code)
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

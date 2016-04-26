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
package de.hybris.platform.mediaconversion.enums;

import de.hybris.platform.core.HybrisEnumValue;

/**
 * Generated enum ConversionStatus declared at extension mediaconversion.
 */
@SuppressWarnings("PMD")
public enum ConversionStatus implements HybrisEnumValue
{
	/**
	 * Generated enum value for ConversionStatus.EMPTY declared at extension mediaconversion.
	 */
	EMPTY("EMPTY"),
	/**
	 * Generated enum value for ConversionStatus.UNCONVERTED declared at extension mediaconversion.
	 */
	UNCONVERTED("UNCONVERTED"),
	/**
	 * Generated enum value for ConversionStatus.PARTIALLY_CONVERTED declared at extension mediaconversion.
	 */
	PARTIALLY_CONVERTED("PARTIALLY_CONVERTED"),
	/**
	 * Generated enum value for ConversionStatus.CONVERTED declared at extension mediaconversion.
	 */
	CONVERTED("CONVERTED");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "ConversionStatus";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "ConversionStatus";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private ConversionStatus(final String code)
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

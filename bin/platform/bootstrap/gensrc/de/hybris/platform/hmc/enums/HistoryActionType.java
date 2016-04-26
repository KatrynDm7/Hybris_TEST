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
package de.hybris.platform.hmc.enums;

import de.hybris.platform.core.HybrisEnumValue;

/**
 * Generated enum HistoryActionType declared at extension hmc.
 */
@SuppressWarnings("PMD")
public enum HistoryActionType implements HybrisEnumValue
{
	/**
	 * Generated enum value for HistoryActionType.created declared at extension hmc.
	 */
	CREATED("created"),
	/**
	 * Generated enum value for HistoryActionType.modified declared at extension hmc.
	 */
	MODIFIED("modified"),
	/**
	 * Generated enum value for HistoryActionType.removed declared at extension hmc.
	 */
	REMOVED("removed"),
	/**
	 * Generated enum value for HistoryActionType.viewed declared at extension hmc.
	 */
	VIEWED("viewed"),
	/**
	 * Generated enum value for HistoryActionType.unspecified declared at extension hmc.
	 */
	UNSPECIFIED("unspecified");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "HistoryActionType";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "HistoryActionType";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private HistoryActionType(final String code)
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

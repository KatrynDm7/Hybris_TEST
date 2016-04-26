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
package de.hybris.platform.ldap.enums;

import de.hybris.platform.core.HybrisEnumValue;

/**
 * Generated enum SearchOperationEnum declared at extension ldap.
 */
@SuppressWarnings("PMD")
public enum SearchOperationEnum implements HybrisEnumValue
{
	/**
	 * Generated enum value for SearchOperationEnum.equals declared at extension ldap.
	 */
	EQUALS("equals"),
	/**
	 * Generated enum value for SearchOperationEnum.exists declared at extension ldap.
	 */
	EXISTS("exists"),
	/**
	 * Generated enum value for SearchOperationEnum.similar declared at extension ldap.
	 */
	SIMILAR("similar"),
	/**
	 * Generated enum value for SearchOperationEnum.greater declared at extension ldap.
	 */
	GREATER("greater"),
	/**
	 * Generated enum value for SearchOperationEnum.lesser declared at extension ldap.
	 */
	LESSER("lesser");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "SearchOperationEnum";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "SearchOperationEnum";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private SearchOperationEnum(final String code)
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

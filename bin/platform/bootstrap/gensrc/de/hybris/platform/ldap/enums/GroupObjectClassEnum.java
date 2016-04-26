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
 * Generated enum GroupObjectClassEnum declared at extension ldap.
 */
@SuppressWarnings("PMD")
public enum GroupObjectClassEnum implements HybrisEnumValue
{
	/**
	 * Generated enum value for GroupObjectClassEnum.posixGroup declared at extension ldap.
	 */
	POSIXGROUP("posixGroup"),
	/**
	 * Generated enum value for GroupObjectClassEnum.organizationalUnit declared at extension ldap.
	 */
	ORGANIZATIONALUNIT("organizationalUnit");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "GroupObjectClassEnum";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "GroupObjectClassEnum";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private GroupObjectClassEnum(final String code)
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

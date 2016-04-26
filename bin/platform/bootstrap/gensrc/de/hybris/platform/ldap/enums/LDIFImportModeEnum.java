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
 * Generated enum LDIFImportModeEnum declared at extension ldap.
 */
@SuppressWarnings("PMD")
public enum LDIFImportModeEnum implements HybrisEnumValue
{
	/**
	 * Generated enum value for LDIFImportModeEnum.filebased declared at extension ldap.
	 */
	FILEBASED("filebased"),
	/**
	 * Generated enum value for LDIFImportModeEnum.querybased declared at extension ldap.
	 */
	QUERYBASED("querybased");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "LDIFImportModeEnum";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "LDIFImportModeEnum";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private LDIFImportModeEnum(final String code)
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

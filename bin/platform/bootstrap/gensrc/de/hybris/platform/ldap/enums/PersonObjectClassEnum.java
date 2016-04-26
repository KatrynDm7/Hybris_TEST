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
 * Generated enum PersonObjectClassEnum declared at extension ldap.
 */
@SuppressWarnings("PMD")
public enum PersonObjectClassEnum implements HybrisEnumValue
{
	/**
	 * Generated enum value for PersonObjectClassEnum.person declared at extension ldap.
	 */
	PERSON("person"),
	/**
	 * Generated enum value for PersonObjectClassEnum.posixAccount declared at extension ldap.
	 */
	POSIXACCOUNT("posixAccount"),
	/**
	 * Generated enum value for PersonObjectClassEnum.organizationalPerson declared at extension ldap.
	 */
	ORGANIZATIONALPERSON("organizationalPerson"),
	/**
	 * Generated enum value for PersonObjectClassEnum.residentialPerson declared at extension ldap.
	 */
	RESIDENTIALPERSON("residentialPerson"),
	/**
	 * Generated enum value for PersonObjectClassEnum.inetOrgPerson declared at extension ldap.
	 */
	INETORGPERSON("inetOrgPerson"),
	/**
	 * Generated enum value for PersonObjectClassEnum.eduPerson declared at extension ldap.
	 */
	EDUPERSON("eduPerson");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "PersonObjectClassEnum";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "PersonObjectClassEnum";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private PersonObjectClassEnum(final String code)
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

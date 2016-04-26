/*
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
 *
 */
package de.hybris.platform.financialservices.substitute.impl;

import de.hybris.platform.financialservices.substitute.ExtensionSubstitutionService;

import java.util.Map;


public class DefaultExtensionSubsitutionService implements ExtensionSubstitutionService
{
	private Map<String, String> extensionSubstitutionMap;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.financialservices.substitute.ExtensionSubstitutionService#getSubstitutedExtension(java.lang
	 * .String)
	 */
	@Override
	public String getSubstitutedExtension(final String extension)
	{
		final String sub = extensionSubstitutionMap.get(extension);

		if (sub == null || sub.isEmpty())
		{
			return extension;
		}

		return sub;
	}

	/**
	 * @return the extensionSubstitutionMap
	 */
	public Map<String, String> getExtensionSubstitutionMap()
	{
		return extensionSubstitutionMap;
	}

	/**
	 * @param extensionSubstitutionMap
	 *           the extensionSubstitutionMap to set
	 */
	public void setExtensionSubstitutionMap(final Map<String, String> extensionSubstitutionMap)
	{
		this.extensionSubstitutionMap = extensionSubstitutionMap;
	}

}

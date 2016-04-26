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
package de.hybris.platform.b2bacceleratorfacades.company.refactoring.impl;

import de.hybris.platform.b2bacceleratorfacades.order.data.B2BSelectionData;


public final class B2BCompanyUtils
{
	public static B2BSelectionData createB2BSelectionData(final String code, final boolean selected, final boolean active)
	{
		final B2BSelectionData b2BSelectionData = new B2BSelectionData();
		b2BSelectionData.setId(code);
		b2BSelectionData.setNormalizedCode(code == null ? null : code.replaceAll("\\W", "_"));
		b2BSelectionData.setSelected(selected);
		b2BSelectionData.setActive(active);
		return b2BSelectionData;
	}
}

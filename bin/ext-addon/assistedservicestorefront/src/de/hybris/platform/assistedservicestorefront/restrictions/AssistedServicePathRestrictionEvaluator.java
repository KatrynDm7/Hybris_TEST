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
package de.hybris.platform.assistedservicestorefront.restrictions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Required;


/**
 * Evaluates list of provided path restrictions.
 */
public class AssistedServicePathRestrictionEvaluator
{
	private List<AssistedServicePathRestriction> restrictions;

	public boolean evaluate(final HttpServletRequest httpservletrequest, final HttpServletResponse httpservletresponse)
	{
		for (final AssistedServicePathRestriction restriction : getRestrictions())
		{
			if (!restriction.evaluate(httpservletrequest, httpservletresponse))
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * @return the restrictions
	 */
	protected List<AssistedServicePathRestriction> getRestrictions()
	{
		return restrictions;
	}

	/**
	 * @param restrictions
	 *           the restrictions to set
	 */
	@Required
	public void setRestrictions(final List<AssistedServicePathRestriction> restrictions)
	{
		this.restrictions = restrictions;
	}

}
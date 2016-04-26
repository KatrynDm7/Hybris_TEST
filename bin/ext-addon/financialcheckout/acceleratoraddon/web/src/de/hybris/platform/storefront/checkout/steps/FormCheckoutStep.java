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
package de.hybris.platform.storefront.checkout.steps;

import de.hybris.platform.financialacceleratorstorefront.checkout.step.InsuranceCheckoutStep;
import org.apache.log4j.Logger;


public class FormCheckoutStep extends InsuranceCheckoutStep
{
	protected static final Logger LOG = Logger.getLogger(FormCheckoutStep.class);
	private String formPageId;

	public String getFormPageId()
	{
		return formPageId;
	}

	public void setFormPageId(String formPageId)
	{
		this.formPageId = formPageId;
	}
}

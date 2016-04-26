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
package de.hybris.platform.b2ctelcoservices.validation;



import java.util.Locale;

import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;


/**
 * Customized MessageInterpolator (referenced in {@link CustomValidationService}) that resolves the issues that only the
 * very first {@link de.hybris.platform.b2ctelcoservices.jalo.ClassificationNotBlankConstraint}
 * violation is displayed in the product cockpit's inspector
 * coverage. The standard ResourceBundleMessageInterpolator uses a hash value that is not unique for
 * {@link de.hybris.platform.b2ctelcoservices.jalo.ClassificationNotBlankConstraint}s,
 * therefore several {@link de.hybris.platform.b2ctelcoservices.jalo.ClassificationNotBlankConstraint} violations look
 * the same and only the first one appears in the coverage.
 **/
public class CustomMessageInterpolator extends ResourceBundleMessageInterpolator
{

	@Override
	public String interpolate(final String message, final Context context)
	{
		return Integer.toString(System.identityHashCode(context.getConstraintDescriptor()));
	}

	@Override
	public String interpolate(final String message, final Context context, final Locale locale)
	{
		return Integer.toString(System.identityHashCode(context.getConstraintDescriptor()));
	}
}

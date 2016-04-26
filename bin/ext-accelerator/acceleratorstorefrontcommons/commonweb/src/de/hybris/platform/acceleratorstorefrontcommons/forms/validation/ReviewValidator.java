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
package de.hybris.platform.acceleratorstorefrontcommons.forms.validation;

import de.hybris.platform.acceleratorstorefrontcommons.forms.ReviewForm;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Validates review forms.
 */
@Component("reviewValidator")
public class ReviewValidator implements Validator
{
	@Override
	public boolean supports(final Class<?> aClass)
	{
		return ReviewForm.class.equals(aClass);
	}

	@Override
	public void validate(final Object object, final Errors errors)
	{
		final ReviewForm reviewForm = (ReviewForm) object;
		final String headLine = reviewForm.getHeadline();
		final String comment = reviewForm.getComment();
		final Double rating = reviewForm.getRating();

		if (StringUtils.isEmpty(headLine) || StringUtils.length(headLine) > 255)
		{
			errors.rejectValue("headline", "review.headline.invalid");
		}

		if (StringUtils.isEmpty(comment) || StringUtils.length(comment) > 4000)
		{
			errors.rejectValue("comment", "review.comment.invalid");
		}

		if (rating == null || rating.doubleValue() < 1 || rating.doubleValue() > 5)
		{
			errors.rejectValue("rating", "review.rating.invalid");
		}
	}
}

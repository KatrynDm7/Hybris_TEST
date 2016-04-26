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
package de.hybris.platform.b2bacceleratoraddon.forms.validation;

import de.hybris.platform.b2bacceleratoraddon.forms.PaymentTypeForm;
import de.hybris.platform.b2bacceleratorservices.enums.CheckoutPaymentType;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/**
 * Validator for {@link PaymentTypeForm}.
 */
@Component("paymentTypeFormValidator")
@Scope("tenant")
public class PaymentTypeFormValidator implements Validator
{

	@Override
	public boolean supports(final Class<?> clazz)
	{
		return PaymentTypeForm.class.equals(clazz);
	}

	@Override
	public void validate(final Object object, final Errors errors)
	{
		if (object instanceof PaymentTypeForm)
		{
			final PaymentTypeForm paymentTypeForm = (PaymentTypeForm) object;

			if (CheckoutPaymentType.ACCOUNT.getCode().equals(paymentTypeForm.getPaymentType())
					&& StringUtils.isBlank(paymentTypeForm.getCostCenterId()))
			{
				errors.rejectValue("costCenterId", "general.required");
			}
		}
	}

}

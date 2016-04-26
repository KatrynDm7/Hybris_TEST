/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 * 
 *  
 */
package de.hybris.platform.hyend2.ui.validation;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.hyend2.model.Hyend2PrecedenceRuleModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;

import org.junit.Test;


/**
 * @author michal.flasinski
 * 
 */
public class Hyend2PrecedenceRuleValidatorTest
{
	Hyend2PrecedenceRuleValidator validator = new Hyend2PrecedenceRuleValidator();
	final Hyend2PrecedenceRuleModel modelToValidate = mock(Hyend2PrecedenceRuleModel.class);

	@Test(expected = HyendValidationException.class)
	public void testValidationIfNoValuesSpecified() throws InterceptorException
	{
		//given
		given(modelToValidate.getDimensionValueItem()).willReturn(null);
		given(modelToValidate.getDimensionValueString()).willReturn(null);

		//when
		validator.onValidate(modelToValidate, null);
	}

	@Test(expected = HyendValidationException.class)
	public void testValidationIfTwoValuesSpecified() throws InterceptorException
	{
		//given
		final ItemModel mockItem = mock(ItemModel.class);
		given(modelToValidate.getDimensionValueItem()).willReturn(mockItem);
		given(modelToValidate.getDimensionValueString()).willReturn("testValue");

		//when
		validator.onValidate(modelToValidate, null);
	}

	@Test
	public void testValidationIfWrongType() throws InterceptorException
	{
		//given
		final ItemModel mockItem = mock(ItemModel.class);

		//when
		validator.onValidate(mockItem, null);

		//then nothing happens
	}

	@Test
	public void testValidationIfIsOk() throws InterceptorException
	{
		//given
		given(modelToValidate.getDimensionValueItem()).willReturn(null);
		given(modelToValidate.getDimensionValueString()).willReturn("testValue");

		//when
		validator.onValidate(modelToValidate, null);

		//then nothing happens
	}
}

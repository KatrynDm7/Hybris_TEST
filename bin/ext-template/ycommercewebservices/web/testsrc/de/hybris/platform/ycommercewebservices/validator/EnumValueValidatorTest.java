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
package de.hybris.platform.ycommercewebservices.validator;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.text.MessageFormat;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;


@UnitTest
public class EnumValueValidatorTest
{
	private EnumerationService enumerationService;
	private EnumValueValidator enumValueValidator;

	@Before
	public void prepare()
	{
		enumerationService = Mockito.mock(EnumerationService.class);
		Mockito.when(enumerationService.getEnumerationValue(TestEnum.TYPE, TestEnum.VALUE1.getCode())).thenReturn(TestEnum.VALUE1);
		Mockito.when(enumerationService.getEnumerationValue(TestEnum.TYPE, TestEnum.VALUE2.getCode())).thenReturn(TestEnum.VALUE2);
		Mockito.when(enumerationService.getEnumerationValue(TestEnum.TYPE, TestEnum.VALUE3.getCode())).thenReturn(null);
		Mockito.when(enumerationService.getEnumerationValue(TestEnum.TYPE, TestEnum.VALUE4.getCode())).thenThrow(
				UnknownIdentifierException.class);

		enumValueValidator = new EnumValueValidator(TestEnum.TYPE);
		enumValueValidator.setEnumerationService(enumerationService);
	}

	@Test
	public void ValidSingleTest()
	{
		//given
		final String[] params = new String[]
		{ TestEnum.VALUE1.getCode() };
		final BindException errors = new BindException("", "");

		//when
		enumValueValidator.validate(params, errors);

		//then
		Assert.assertFalse(errors.hasErrors());
	}

	@Test
	public void ValidMultipleTest()
	{
		//given
		final String[] params = new String[]
		{ TestEnum.VALUE1.getCode(), TestEnum.VALUE2.getCode() };
		final BindException errors = new BindException("", "");

		//when
		enumValueValidator.validate(params, errors);

		//then
		Assert.assertFalse(errors.hasErrors());
	}

	@Test
	public void InvalidSingleNullTest()
	{
		//given
		final String param = TestEnum.VALUE3.getCode();
		final String[] params =
		{ param };
		final BindException errors = new BindException("", "");
		final String expected = getExpectedMessage(param);
		//when
		enumValueValidator.validate(params, errors);

		//then
		Assert.assertTrue(errors.hasErrors());
		Assert.assertEquals(errors.getErrorCount(), 1);

		final String actual = formatMessage(errors);
		Assert.assertEquals(actual, expected);
	}

	@Test
	public void InvalidSingleExceptionTest()
	{
		//given
		final String param = TestEnum.VALUE4.getCode();
		final String[] params =
		{ param };
		final BindException errors = new BindException("", "");
		final String expected = getExpectedMessage(param);

		//when
		enumValueValidator.validate(params, errors);

		//then
		Assert.assertTrue(errors.hasErrors());
		Assert.assertEquals(errors.getErrorCount(), 1);

		final String actual = formatMessage(errors);
		Assert.assertEquals(actual, expected);
	}

	@Test
	public void InvalidMultipleTest()
	{
		//given
		final String param1 = TestEnum.VALUE3.getCode();
		final String param2 = TestEnum.VALUE4.getCode();
		final String[] params =
		{ param1, param2 };
		final BindException errors = new BindException("", "");
		final String expected = getExpectedMessage(param1) + getExpectedMessage(param2);

		//when
		enumValueValidator.validate(params, errors);

		//then
		Assert.assertTrue(errors.hasErrors());
		Assert.assertEquals(errors.getErrorCount(), 2);

		final String actual = formatMessage(errors);
		Assert.assertEquals(actual, expected);
	}

	@Test
	public void MixedMultipleTest()
	{
		//given
		final String[] params = new String[]
		{ TestEnum.VALUE1.getCode(), TestEnum.VALUE4.getCode() };
		final BindException errors = new BindException("", "");
		final String expected = getExpectedMessage(TestEnum.VALUE4.getCode());

		//when
		enumValueValidator.validate(params, errors);

		//then
		Assert.assertTrue(errors.hasErrors());
		Assert.assertEquals(errors.getErrorCount(), 1);
		final String actual = formatMessage(errors);
		Assert.assertEquals(actual, expected);
	}

	private String formatMessage(final Errors errors)
	{
		final StringBuilder sb = new StringBuilder();
		for (final ObjectError error : errors.getAllErrors())
		{
			sb.append(MessageFormat.format(error.getDefaultMessage(), error.getArguments()));
		}
		return sb.toString();
	}

	private String getExpectedMessage(final String param)
	{
		return MessageFormat.format(EnumValueValidator.DEFAULT_MESSAGE, param, TestEnum.TYPE);
	}

	private static class TestEnum implements HybrisEnumValue
	{
		public static final String TYPE = "testEnum";

		public static final TestEnum VALUE1 = new TestEnum("value1");

		public static final TestEnum VALUE2 = new TestEnum("value2");

		public static final TestEnum VALUE3 = new TestEnum("value3");

		public static final TestEnum VALUE4 = new TestEnum("value4");

		private final String code;

		private TestEnum(final String code)
		{
			this.code = code;
		}

		@Override
		public String getCode()
		{
			return code;
		}

		@Override
		public String getType()
		{
			return TYPE;
		}

	}
}

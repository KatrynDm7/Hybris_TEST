/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2014 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package de.hybris.platform.util;

import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.Fail.fail;

import de.hybris.bootstrap.annotations.UnitTest;

import org.junit.Test;


@UnitTest
public class ThreadUtilitiesTest
{
	@Test
	public void shouldReturnConstantValue()
	{
		final int value = ThreadUtilities.getNumberOfThreadsFromExpression("7", 2);

		assertThat(value).isEqualTo(7);
	}

	@Test
	public void shouldEvaluateToNumberOfCores()
	{
		final int value = ThreadUtilities.getNumberOfThreadsFromExpression("#cores", Integer.MAX_VALUE);

		assertThat(value).isGreaterThan(0).isLessThan(Integer.MAX_VALUE).isEqualTo(Runtime.getRuntime().availableProcessors());
	}

	@Test
	public void shouldEvaluateArithmeticExpression()
	{
		final int value = ThreadUtilities.getNumberOfThreadsFromExpression("2 * 2 + 7 / 3", Integer.MAX_VALUE);

		assertThat(value).isEqualTo(6);
	}

	@Test
	public void shouldEvaluateArithmeticExpressionWithVariable()
	{
		final int value = ThreadUtilities.getNumberOfThreadsFromExpression("2 * 2 + #cores", Integer.MAX_VALUE);

		assertThat(value).isEqualTo(4 + Runtime.getRuntime().availableProcessors());
	}

	@Test
	public void shouldReturnDefaultValueWhenExpressionWithArithmeticExceptionHasBeenPassed()
	{
		final int value = ThreadUtilities.getNumberOfThreadsFromExpression("#cores / 0", 17);

		assertThat(value).isEqualTo(17);
	}

	@Test
	public void shouldReturnDefaultValueWhenInvalidExpressionHasBeenPassed()
	{
		final int value = ThreadUtilities.getNumberOfThreadsFromExpression("1 + #invalid", 17);

		assertThat(value).isEqualTo(17);
	}

	@Test
	public void shouldReturnDefaultValueWhenExpressionEvaluatesToString()
	{
		final int value = ThreadUtilities.getNumberOfThreadsFromExpression("1 + 'test'", 17);

		assertThat(value).isEqualTo(17);
	}

	@Test
	public void shouldReturnDefaultValueWhenExpressionEvaluatesToZero()
	{
		final int value = ThreadUtilities.getNumberOfThreadsFromExpression("#cores - #cores", 17);

		assertThat(value).isEqualTo(17);
	}

	@Test
	public void shouldReturnDefaultValueWhenExpressionEvaluatesToNegativeValue()
	{
		final int value = ThreadUtilities.getNumberOfThreadsFromExpression("#cores - #cores * 2", 17);

		assertThat(value).isEqualTo(17);
	}

	@Test
	public void shouldReturnDefaultValueWhenExpressionEvaluatesToNull()
	{
		final int value = ThreadUtilities.getNumberOfThreadsFromExpression("null", 17);

		assertThat(value).isEqualTo(17);
	}

	@Test
	public void shouldReturnRoundedValueOfExpressionWhetItEvaluatesToDouble()
	{
		final int value = ThreadUtilities.getNumberOfThreadsFromExpression("1 + 3.14159", 17);

		assertThat(value).isEqualTo(4);
	}

	@Test
	public void shouldThrowAnExceptionWhenNegativeDefaultValueHasBeenPassed()
	{
		try
		{
			ThreadUtilities.getNumberOfThreadsFromExpression("7", -1);
		}
		catch (final IllegalArgumentException expected)
		{
			assertThat(expected).isNotNull().hasMessage("defaultValue must be greater than 0");
			return;
		}

		fail("IllegalArgumentException was expected");
	}

	@Test
	public void shouldThrowAnExceptionWhenZeroDefaultValueHasBeenPassed()
	{
		try
		{
			ThreadUtilities.getNumberOfThreadsFromExpression("7", 0);
		}
		catch (final IllegalArgumentException expected)
		{
			assertThat(expected).isNotNull().hasMessage("defaultValue must be greater than 0");
			return;
		}

		fail("IllegalArgumentException was expected");
	}

	@Test
	public void shouldReturnDefaultValueWhenNullExpressionHasBeenPassed()
	{
		final int value = ThreadUtilities.getNumberOfThreadsFromExpression(null, 2);

		assertThat(value).isEqualTo(2);
	}

	@Test
	public void shouldReturnDefaultValueWhenEmptyExpressionHasBeenPassed()
	{
		final int value = ThreadUtilities.getNumberOfThreadsFromExpression("", 2);

		assertThat(value).isEqualTo(2);
	}

	@Test
	public void shouldReturnDefaultValueWhenOnlyWhiteSpacesHasBeenPassedAsAnExpression()
	{
		final int value = ThreadUtilities.getNumberOfThreadsFromExpression(" ", 2);

		assertThat(value).isEqualTo(2);
	}

}

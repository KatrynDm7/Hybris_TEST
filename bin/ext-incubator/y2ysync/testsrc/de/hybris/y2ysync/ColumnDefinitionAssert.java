/*
 *
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
package de.hybris.y2ysync;

import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.y2ysync.model.Y2YColumnDefinitionModel;

import org.fest.assertions.Assertions;
import org.fest.assertions.GenericAssert;


public class ColumnDefinitionAssert extends GenericAssert<ColumnDefinitionAssert, Y2YColumnDefinitionModel>
{

	protected ColumnDefinitionAssert(final Y2YColumnDefinitionModel actual)
	{
		super(ColumnDefinitionAssert.class, actual);
	}

	public static ColumnDefinitionAssert assertThat(final Y2YColumnDefinitionModel actual)
	{
		return new ColumnDefinitionAssert(actual);
	}

	public ColumnDefinitionAssert withImpexHeader(final String impexHeader)
	{
		Assertions.assertThat(actual.getImpexHeader()).isEqualTo(impexHeader);
		return this;
	}

	public ColumnDefinitionAssert withColumnName(final String columnName)
	{
		Assertions.assertThat(actual.getColumnName()).isEqualTo(columnName);
		return this;
	}


	public ColumnDefinitionAssert withLanguage(final LanguageModel languageModel)
	{
		Assertions.assertThat(actual.getLanguage()).isNotNull();
		Assertions.assertThat(actual.getLanguage().getIsocode()).isEqualTo(languageModel.getIsocode());
		return this;
	}

	public ColumnDefinitionAssert withoutLanguage()
	{
		Assertions.assertThat(actual.getLanguage()).isNull();
		return this;
	}

}

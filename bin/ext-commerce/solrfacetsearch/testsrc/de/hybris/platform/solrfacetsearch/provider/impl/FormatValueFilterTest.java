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
package de.hybris.platform.solrfacetsearch.provider.impl;

import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.BeanFactory;


@UnitTest
public class FormatValueFilterTest
{
	protected static final String INDEXED_PROPERTY_NAME = "indexedProperty";

	@Mock
	private BeanFactory beanFactory;

	@Mock
	private IndexerBatchContext batchContext;

	private FormatValueFilter formatValueFilter;

	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);

		formatValueFilter = new FormatValueFilter();
		formatValueFilter.setBeanFactory(beanFactory);
	}

	protected IndexedProperty getIndexProperty()
	{
		final IndexedProperty indexedProperty = new IndexedProperty();
		indexedProperty.setName(INDEXED_PROPERTY_NAME);
		indexedProperty.setValueProviderParameters(new HashMap<String, String>());
		indexedProperty.setLocalized(false);

		return indexedProperty;
	}

	@Test
	public void formatSingleDateValue()
	{
		//given
		final IndexedProperty indexedProperty = getIndexProperty();

		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
		final Date value = new Date();
		final String formatterBeanName = "dateFormatter";

		indexedProperty.getValueProviderParameters().put(FormatValueFilter.FORMAT_PARAM, formatterBeanName);

		when(beanFactory.getBean(formatterBeanName, Format.class)).thenReturn(simpleDateFormat);

		final String targetedFormattedValue = simpleDateFormat.format(value);

		//when
		final Object formattedValue = formatValueFilter.doFilter(batchContext, indexedProperty, value);

		//then
		Assert.assertEquals(formattedValue, targetedFormattedValue);
	}

	@Test
	public void formatCollectionOfDateValues()
	{
		//given
		final IndexedProperty indexedProperty = getIndexProperty();

		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

		final List<Date> values = new ArrayList<>();
		final Calendar calendar = Calendar.getInstance();
		values.add(calendar.getTime());
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		values.add(calendar.getTime());
		calendar.add(Calendar.MONTH, 1);
		values.add(calendar.getTime());

		final String formatterBeanName = "dateFormatter";

		indexedProperty.getValueProviderParameters().put(FormatValueFilter.FORMAT_PARAM, formatterBeanName);

		when(beanFactory.getBean(formatterBeanName, Format.class)).thenReturn(simpleDateFormat);


		//when
		final Object formattedValue = formatValueFilter.doFilter(batchContext, indexedProperty, values);

		//then
		Assert.assertTrue(formattedValue instanceof Collection);

		final List<Date> formattedDates = (List<Date>) formattedValue;
		for (int i = 0; i < formattedDates.size(); i++)
		{
			Assert.assertEquals(formattedDates.get(i), simpleDateFormat.format(values.get(i)));
		}

	}

	@Test
	public void formatDateValueWithDateObjectAsNull()
	{
		//given
		final IndexedProperty indexedProperty = getIndexProperty();

		final Date value = null;

		final String targetedFormattedValue = null;

		//when
		final Object formattedValue = formatValueFilter.doFilter(batchContext, indexedProperty, value);

		//then
		Assert.assertEquals(formattedValue, targetedFormattedValue);
	}

	@Test
	public void formatDateValueWithBeanNameNull()
	{
		//given
		final IndexedProperty indexedProperty = getIndexProperty();

		final Date value = new Date();
		final String formatterBeanName = null;

		indexedProperty.getValueProviderParameters().put(FormatValueFilter.FORMAT_PARAM, formatterBeanName);

		final Object targetedFormattedValue = value;

		//when
		final Object formattedValue = formatValueFilter.doFilter(batchContext, indexedProperty, value);

		//then
		Assert.assertEquals(formattedValue, targetedFormattedValue);
	}
}

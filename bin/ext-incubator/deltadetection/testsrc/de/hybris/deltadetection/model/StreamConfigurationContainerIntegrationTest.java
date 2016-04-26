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
package de.hybris.deltadetection.model;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;

import com.google.common.collect.Lists;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.DuplicateKeyException;


public class StreamConfigurationContainerIntegrationTest extends ServicelayerTransactionalBaseTest
{

	@Resource
	private ModelService modelService;
	@Resource
	private TypeService typeService;
	private StreamConfigurationContainerModel container1, container2;

	@Before
	public void setUp() throws Exception
	{
		container1 = modelService.create(StreamConfigurationContainerModel.class);
		container1.setId("testStreams1");
		container2 = modelService.create(StreamConfigurationContainerModel.class);
		container2.setId("testStreams2");
		modelService.saveAll(container1, container2);
	}

	@Test
	public void shouldSaveStreamConfigurationCorrectlyWithProperWhereClause() throws Exception
	{
		// given
		final StreamConfigurationModel config = prepareStreamConfigurationFor("testStream", ProductModel.class, "{code}=?");
		config.setContainer(container1);

		// when
		modelService.save(config);

		// then
		assertThat(modelService.isNew(config)).isFalse();
		assertThat(config.getStreamId()).isEqualTo("testStream");
		assertThat(config.getWhereClause()).isEqualTo("{code}=?");
	}

	@Test
	public void shouldThrowModelSavingExceptionWhenTryingToSaveStreamConfigurationWithTheSameStreamIDWithinSameContainer()
			throws Exception
	{
		// given
		final StreamConfigurationModel config1 = prepareStreamConfigurationFor("testStream", ProductModel.class, "{code}=?");
		final StreamConfigurationModel config2 = prepareStreamConfigurationFor("testStream", ProductModel.class, "{code}=?");
		config1.setContainer(container1);
		config2.setContainer(container1);

		try
		{
			// when
			modelService.saveAll(config1, config2);
			fail("should throw ModelSavingException");
		}
		catch (final ModelSavingException e)
		{
			// then OK
		}
	}

	@Test
	public void shouldFailValidationOfStreamConfigurationWhenWhereClauseContains_WHERE_keywordAtTheBeginning() throws Exception
	{
		shouldFailValidationOfStreamConfigurationWhenWhereClauseContains_WHERE_keywordAtTheBeginning("WHERE {code} IS TRUE");
	}

	@Test
	public void shouldFailValidationOfStreamConfigurationWhenWhereClauseContains_where_keywordAtTheBeginning() throws Exception
	{
		shouldFailValidationOfStreamConfigurationWhenWhereClauseContains_WHERE_keywordAtTheBeginning("where {code} IS TRUE");
	}

	@Test
	public void shouldFailValidationOfStreamConfigurationWhenWhereClauseContains__where_keywordAtTheBeginning() throws Exception
	{
		shouldFailValidationOfStreamConfigurationWhenWhereClauseContains_WHERE_keywordAtTheBeginning(" WHERE {code} IS TRUE");
	}

	private void shouldFailValidationOfStreamConfigurationWhenWhereClauseContains_WHERE_keywordAtTheBeginning(
			final String whereClause)
	{
		// given
		final StreamConfigurationModel config = prepareStreamConfigurationFor("testStream", ProductModel.class, whereClause);
		config.setWhereClause(whereClause);

		try
		{
			// when
			modelService.save(config);
			fail("Should throw ModelSavingException");
		}
		catch (final ModelSavingException e)
		{
			// then
			assertThat(e.getCause()).isInstanceOf(InterceptorException.class);
		}
	}

	private StreamConfigurationModel prepareStreamConfigurationFor(final String streamId, final Class itemType,
			final String whereClause)
	{
		final StreamConfigurationModel config = modelService.create(StreamConfigurationModel.class);
		config.setStreamId(streamId);
		config.setItemTypeForStream(typeService.getComposedTypeForClass(itemType));
		config.setWhereClause(whereClause);

		return config;
	}
}

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
package de.hybris.platform.c4ccustomer.datasetup;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.scripting.enums.ScriptType;
import de.hybris.platform.scripting.model.ScriptModel;
import de.hybris.platform.servicelayer.model.ModelService;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

/**
 * Essential data setup tests.
 */
@UnitTest
public class DataSetupTest
{
	private DataSetup dataSetup;
	private ModelService modelService;

	@Before
	public void setup()
	{
		modelService = mock(ModelService.class);
		doReturn(new ScriptModel()).when(modelService).create(ScriptModel.class);
		dataSetup = new DataSetup();
		dataSetup.setModelService(modelService);
	}

	/**
	 * Check if all scripts are available.
	 */
	@Test
	public void shouldFindResources()
	{
		doAnswer(invocationOnMock ->
		{
			final Object arg = invocationOnMock.getArguments()[0];
			assertThat("Wrong argument of ModelService#save", arg, instanceOf(ScriptModel.class));
			final ScriptModel script = (ScriptModel) arg;
			assertThat("Incorrect type of script", script.getScriptType(), is(ScriptType.GROOVY));
			assertThat("Unexpected script content", script.getContent(), containsString("flexibleSearchService.search"));
			// save is a void-returning method, so it doesn't matter what to return
			return null;
		}).when(modelService).save(any(ScriptModel.class));
		dataSetup.createEssentialData();
	}
}

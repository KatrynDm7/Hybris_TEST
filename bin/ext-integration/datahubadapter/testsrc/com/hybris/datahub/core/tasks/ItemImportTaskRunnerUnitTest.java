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
 */

package com.hybris.datahub.core.tasks;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.TaskService;

import com.hybris.datahub.core.dto.ItemImportTaskData;
import com.hybris.datahub.core.facades.ItemImportFacade;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;


@SuppressWarnings("javadoc")
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class ItemImportTaskRunnerUnitTest
{
	private static final String POOL_NAME = "testpool";
	private static final Long PUBLICATION_ID = 1l;
	private static final String CALLBACK_URL = "http://localhost/callback";
	private static final byte[] IMPEX_CONTENT = "INSERT_UPDATE value, value, value".getBytes();
	private static final String USER = "user name";
	private static final String LANGUAGE = "ja";
	private final ItemImportTaskRunner taskRunner = new ItemImportTaskRunner();
	@Mock
	private ItemImportFacade importFacade;
	@Mock
	private SessionService sessionService;
	@Mock
	private TaskModel taskModel;
	@Mock
	private TaskService taskService;

	@Before
	public void setup()
	{
		final Map<String, Object> sessionAttrs = new HashMap<>();
		sessionAttrs.put("user", USER);
		sessionAttrs.put("language", LANGUAGE);

		final ItemImportTaskData taskData = new ItemImportTaskData(POOL_NAME, PUBLICATION_ID, CALLBACK_URL, IMPEX_CONTENT,
				sessionAttrs);
		Mockito.doReturn(taskData).when(taskModel).getContext();

		taskRunner.setImportFacade(importFacade);
		taskRunner.setSessionService(sessionService);
	}

	@Test
	public void testRunInitializesSessionUserBeforeTheItemImport() throws Exception
	{
		final InOrder callSeq = Mockito.inOrder(sessionService, importFacade);

		taskRunner.run(taskService, taskModel);

		callSeq.verify(sessionService).setAttribute("user", USER);
		callSeq.verify(importFacade).importItems((ItemImportTaskData) taskModel.getContext());
	}

	@Test
	public void testRunInitializesSessionLanguageBeforeTheItemImport() throws Exception
	{
		final InOrder callSeq = Mockito.inOrder(sessionService, importFacade);

		taskRunner.run(taskService, taskModel);

		callSeq.verify(sessionService).setAttribute("language", LANGUAGE);
		callSeq.verify(importFacade).importItems((ItemImportTaskData) taskModel.getContext());
	}

	@Test
	public void testRunClosesSessionAfterTheItemImport() throws Exception
	{
		final InOrder callSeq = Mockito.inOrder(sessionService, importFacade);

		taskRunner.run(taskService, taskModel);

		callSeq.verify(importFacade).importItems((ItemImportTaskData) taskModel.getContext());
		callSeq.verify(sessionService).closeCurrentSession();
	}

	@Test
	public void testRunHandlesImportItemsException() throws Exception
	{
		final IOException ioEx = new IOException();
		Mockito.doThrow(ioEx).when(importFacade).importItems(any(ItemImportTaskData.class));

		try
		{
			taskRunner.run(taskService, taskModel);
			fail("Exception should have been thrown");
		}
		catch (final RuntimeException e)
		{
			assertSame(ioEx, e.getCause());
		}
	}
}

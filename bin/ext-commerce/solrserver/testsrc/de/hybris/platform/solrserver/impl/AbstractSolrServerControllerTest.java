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
package de.hybris.platform.solrserver.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.solrserver.SolrInstance;
import de.hybris.platform.solrserver.SolrServerException;
import de.hybris.platform.solrserver.impl.AbstractSolrServerController.CommandResult;
import de.hybris.platform.solrserver.impl.AbstractSolrServerController.ServerStatus;
import de.hybris.platform.solrserver.impl.AbstractSolrServerController.ServerStatus.Status;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;


@UnitTest
public class AbstractSolrServerControllerTest
{
	public static final String INSTANCE_NAME = "testInst1";
	public static final String INSTANCE_PORT = "9991";
	public static final String INSTANCE_AUTOSTART = Boolean.TRUE.toString();
	public static final String INSTANCE_CONFIG_DIR = "/Users/112233/hybris/search-and-nav/solrserver/resources/solr/server/solr1/";
	public static final String INSTANCE_DATA_DIR = "";
	public static final String INSTANCE_LOG_DIR = "";
	public static final String INSTANCE_MEMORY = "512m";
	public static final String INSTANCE_JAVA_OPTIONS = "";

	public static final String SOLT_MULTIPLE_SERVERS_STDOUT_FILE = "/test/solr_multiple_servers_stdout.txt";
	public static final String SOLT_NO_JSON_STDOUT_FILE = "/test/solr_no_json_stdout.txt";
	public static final String SOLT_NO_SERVER_STDOUT_FILE = "/test/solr_no_server_stdout.txt";

	@Rule
	public ExpectedException expectedException = ExpectedException.none(); //NOPMD

	private AbstractSolrServerController solrServerController;
	private SolrInstance solrInstance;
	private ServerStatus stoppedServerStatus;
	private ServerStatus unknownServerStatus;
	private ServerStatus startedServerStatus;

	@Before
	public void setUp() throws Exception
	{
		solrServerController = Mockito.spy(new AbstractSolrServerControllerSpy());

		solrInstance = new SolrInstance(INSTANCE_NAME);

		final Map<String, String> configuration = solrInstance.getConfiguration();
		configuration.put(SolrInstance.PORT_PROPERTY, INSTANCE_PORT);
		configuration.put(SolrInstance.AUTOSTART_PROPERTY, INSTANCE_AUTOSTART);
		configuration.put(SolrInstance.CONFIG_DIR_PROPERTY, INSTANCE_CONFIG_DIR);
		configuration.put(SolrInstance.DATA_DIR_PROPERTY, INSTANCE_DATA_DIR);
		configuration.put(SolrInstance.LOG_DIR_PROPERTY, INSTANCE_LOG_DIR);
		configuration.put(SolrInstance.MEMORY_PROPERTY, INSTANCE_MEMORY);
		configuration.put(SolrInstance.JAVA_OPTIONS_PROPERTY, INSTANCE_JAVA_OPTIONS);

		stoppedServerStatus = new ServerStatus();
		stoppedServerStatus.setPort(solrInstance.getPort());
		stoppedServerStatus.setStatus(Status.STOPPED);

		unknownServerStatus = new ServerStatus();
		unknownServerStatus.setPort(solrInstance.getPort());
		unknownServerStatus.setStatus(Status.UNKNOWN);

		startedServerStatus = new ServerStatus();
		startedServerStatus.setPort(solrInstance.getPort());
		startedServerStatus.setSolrHome(solrInstance.getConfigDir());
		startedServerStatus.setStatus(Status.STARTED);
	}

	@Test
	public void startServer() throws Exception
	{
		// given
		final CommandResult commandResult = new CommandResult();
		commandResult.setExitValue(0);

		doReturn(commandResult).when(solrServerController).callSolrCommand(solrInstance, AbstractSolrServerController.START_COMMAND,
				true);
		doReturn(stoppedServerStatus).doReturn(startedServerStatus).when(solrServerController).getSolrServerStatus(solrInstance);

		// when
		solrServerController.start(solrInstance);

		// then
		verify(solrServerController).ensureToStartSolr(solrInstance);
	}

	@Test
	public void startServerWithStatusRetry() throws Exception
	{
		// given
		final CommandResult commandResult = new CommandResult();
		commandResult.setExitValue(0);

		doReturn(commandResult).when(solrServerController).callSolrCommand(solrInstance, AbstractSolrServerController.START_COMMAND,
				true);
		doReturn(stoppedServerStatus).doReturn(unknownServerStatus).doReturn(startedServerStatus).when(solrServerController)
				.getSolrServerStatus(solrInstance);

		// when
		solrServerController.start(solrInstance);

		// then
		verify(solrServerController).ensureToStartSolr(solrInstance);
	}

	@Test
	public void restartRunningServer() throws Exception
	{
		// given
		final CommandResult commandResult = new CommandResult();
		commandResult.setExitValue(0);

		doReturn(commandResult).when(solrServerController).callSolrCommand(solrInstance, AbstractSolrServerController.START_COMMAND,
				true);
		doReturn(commandResult).when(solrServerController).callSolrCommand(solrInstance, AbstractSolrServerController.STOP_COMMAND,
				true);
		doReturn(startedServerStatus).doReturn(stoppedServerStatus).doReturn(startedServerStatus).when(solrServerController)
				.getSolrServerStatus(solrInstance);

		// when
		solrServerController.start(solrInstance);

		// then
		verify(solrServerController).ensureToStopSolr(solrInstance);
		verify(solrServerController).ensureToStartSolr(solrInstance);
	}

	@Test
	public void thrownExceptionBecauseWrongServerIsRunning() throws Exception
	{
		// given
		startedServerStatus.setSolrHome(solrInstance.getConfigDir() + "_notfound");
		doReturn(startedServerStatus).when(solrServerController).getSolrServerStatus(solrInstance);

		// expect
		expectedException.expect(SolrServerException.class);

		// when
		solrServerController.start(solrInstance);
	}

	@Test
	public void serverDoesNotStart() throws Exception
	{
		// given
		final CommandResult commandResult = new CommandResult();
		commandResult.setExitValue(0);

		doReturn(commandResult).when(solrServerController).callSolrCommand(solrInstance, AbstractSolrServerController.START_COMMAND,
				true);
		doReturn(stoppedServerStatus).when(solrServerController).getSolrServerStatus(solrInstance);

		// expect
		expectedException.expect(SolrServerException.class);

		// when
		solrServerController.start(solrInstance);
	}

	@Test
	public void stopServer() throws Exception
	{
		// given
		final CommandResult commandResult = new CommandResult();
		commandResult.setExitValue(0);

		doReturn(commandResult).when(solrServerController).callSolrCommand(solrInstance, AbstractSolrServerController.STOP_COMMAND,
				true);
		doReturn(startedServerStatus).doReturn(stoppedServerStatus).when(solrServerController).getSolrServerStatus(solrInstance);

		// when
		solrServerController.stop(solrInstance);

		// then
		verify(solrServerController).ensureToStopSolr(solrInstance);
	}

	@Test
	public void statusShouldBeStopped() throws Exception
	{
		// given
		final String output = readFile(SOLT_NO_SERVER_STDOUT_FILE);
		final CommandResult commandResult = new CommandResult();
		commandResult.setExitValue(0);
		commandResult.setOutput(output);

		doReturn(commandResult).when(solrServerController).callSolrCommand(solrInstance,
				AbstractSolrServerController.STATUS_COMMAND, false);

		// when
		final ServerStatus serverStatus = solrServerController.getSolrServerStatus(solrInstance);

		// then
		assertEquals(ServerStatus.Status.STOPPED, serverStatus.getStatus());
	}

	@Test
	public void statusShouldBeStarted() throws Exception
	{
		// given
		final String output = readFile(SOLT_MULTIPLE_SERVERS_STDOUT_FILE);
		final CommandResult commandResult = new CommandResult();
		commandResult.setExitValue(0);
		commandResult.setOutput(output);

		doReturn(commandResult).when(solrServerController).callSolrCommand(solrInstance,
				AbstractSolrServerController.STATUS_COMMAND, false);

		// when
		final ServerStatus serverStatus = solrServerController.getSolrServerStatus(solrInstance);

		// then
		assertEquals(ServerStatus.Status.STARTED, serverStatus.getStatus());
	}

	@Test
	public void statusShouldBeUnknown() throws Exception
	{
		// given
		final String output = readFile(SOLT_NO_JSON_STDOUT_FILE);
		final CommandResult commandResult = new CommandResult();
		commandResult.setExitValue(0);
		commandResult.setOutput(output);

		doReturn(commandResult).when(solrServerController).callSolrCommand(solrInstance,
				AbstractSolrServerController.STATUS_COMMAND, false);

		// when
		final ServerStatus serverStatus = solrServerController.getSolrServerStatus(solrInstance);

		// then
		assertEquals(ServerStatus.Status.UNKNOWN, serverStatus.getStatus());
	}

	protected String readFile(final String file) throws IOException
	{
		final InputStream inputStream = AbstractSolrServerControllerTest.class.getResourceAsStream(file);
		if (inputStream == null)
		{
			throw new FileNotFoundException("file [" + file + "] cannot be found");
		}

		return IOUtils.toString(inputStream);
	}

	protected class AbstractSolrServerControllerSpy extends AbstractSolrServerController
	{
		@Override
		protected void configureSolrCommandInvocation(final SolrInstance solrInstance, final ProcessBuilder processBuilder,
				final String command)
		{
			// NOOP
		}

	}
}

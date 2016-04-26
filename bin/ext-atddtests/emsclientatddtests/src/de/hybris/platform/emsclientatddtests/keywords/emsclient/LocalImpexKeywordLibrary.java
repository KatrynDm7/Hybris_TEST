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
package de.hybris.platform.emsclientatddtests.keywords.emsclient;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import de.hybris.platform.atddengine.framework.RobotTest;
import de.hybris.platform.atddengine.keywords.AbstractKeywordLibrary;
import de.hybris.platform.atddengine.keywords.ImpExAdaptor;
import de.hybris.platform.atddengine.keywords.ImpExAdaptorAware;
import de.hybris.platform.atddengine.keywords.RobotTestContext;
import de.hybris.platform.atddengine.keywords.RobotTestContextAware;
import de.hybris.platform.atddengine.templates.TemplateProcessor;
import de.hybris.platform.atddengine.templates.TemplateProcessorFactory;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.Config;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.TeeInputStream;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;


public class LocalImpexKeywordLibrary extends AbstractKeywordLibrary implements ImpExAdaptorAware, RobotTestContextAware
{
	private static final String DEFAULT_ENCODING = "UTF-8";

	private static Logger LOG = Logger.getLogger(LocalImpexKeywordLibrary.class);

	private static final String PROP_REPORT_PATH = "atddengine.report-path";

	private ImpExAdaptor impExAdaptor;

	private OutputStream impExLogOutputStream;

	private TemplateProcessor impexTemplateProcessor;

	private RobotTestContext robotTestContext;

	private static final String IMPEX_LOG_LINE_SEPERATOR = System.getProperty("line.separator");

	private static final String IMPEX_LOG_RESOURCE_BANNER = String.format("# %s %s", StringUtils.repeat("#", 80),
			IMPEX_LOG_LINE_SEPERATOR);

	@Autowired
	private TemplateProcessorFactory templateProcessorFactory;

	@Autowired
	private ModelService modelService;

	private String legacyModeMarker;

	private OutputStream getImpExLogOutputStream(final String resourceName) throws IOException
	{
		if (impExLogOutputStream == null)
		{
			final RobotTest robotTest = robotTestContext.getCurrentRobotTest();

			if (robotTest == null)
			{
				throw new IllegalStateException("KeywordMethods must only be called within a valid RobotTestContext");
			}
			else
			{
				final String testSuiteName = robotTest.getTestSuite().getName().replaceAll(Pattern.quote(" "), "_");

				final String impExLogPath = String.format("%s/%s/%s-data.impex", robotTestContext.getProjectName(), testSuiteName,
						robotTest.getName());
				final File impExLogFile = new File(Config.getParameter(PROP_REPORT_PATH), impExLogPath);

				impExLogOutputStream = FileUtils.openOutputStream(impExLogFile);
			}
		}
		else
		{
			IOUtils.write(IMPEX_LOG_LINE_SEPERATOR, impExLogOutputStream, DEFAULT_ENCODING);
		}

		IOUtils.write(IMPEX_LOG_RESOURCE_BANNER, impExLogOutputStream, DEFAULT_ENCODING);
		IOUtils.write(String.format("# Import from %s %s", resourceName, IMPEX_LOG_LINE_SEPERATOR), impExLogOutputStream,
				DEFAULT_ENCODING);
		IOUtils.write(IMPEX_LOG_RESOURCE_BANNER, impExLogOutputStream, DEFAULT_ENCODING);

		impExLogOutputStream.flush();

		return impExLogOutputStream;
	}

	protected TemplateProcessor getImpexTemplateProcessor() throws IOException
	{
		if (impexTemplateProcessor == null)
		{
			impexTemplateProcessor = templateProcessorFactory.createTemplateProcessor();
		}

		return impexTemplateProcessor;
	}

	private void importImpExFromInputStream(final String resourceName, InputStream inputStream) throws ImpExException, IOException
	{
		if (LOG.isInfoEnabled())
		{
			LOG.info("Importing [" + resourceName + "]");

			final ByteArrayOutputStream impexData = new ByteArrayOutputStream();
			final TeeInputStream consoleTeeInputStream = new TeeInputStream(inputStream, impexData);

			LOG.info("ImpEx contents are:\n" + IOUtils.toString(consoleTeeInputStream, DEFAULT_ENCODING));

			inputStream = new ByteArrayInputStream(impexData.toByteArray());
		}

		final OutputStream logOutputStream = getImpExLogOutputStream(resourceName);
		final InputStream fileFeeInputStream = new TeeInputStream(inputStream, logOutputStream);

		impExAdaptor.importStream(fileFeeInputStream, DEFAULT_ENCODING, resourceName);

		logOutputStream.flush();

		modelService.detachAll();
	}

	private void importImpExFromInputStream(final String resourceName, final InputStream inputStream,
			final String expectedException) throws IOException
	{
		Throwable exception = null;

		try
		{
			importImpExFromInputStream(resourceName, inputStream);
		}
		catch (final Exception e)
		{
			exception = e;
			while (exception.getCause() != null)
			{
				exception = exception.getCause();
			}
			if (exception.getClass().getSimpleName().equals(expectedException))
			{
				LOG.info("Expected [" + expectedException + "] caught: " + exception.getMessage());
			}
			else
			{
				LOG.error(exception.getMessage(), exception);
			}
		}
		finally
		{
			IOUtils.closeQuietly(inputStream);
		}

		if (expectedException == null)
		{
			assertNull("Import of resource " + resourceName + " failed: " + (exception == null ? "" : exception.getMessage()),
					exception);
		}
		else
		{
			assertNotNull("Expected [" + expectedException + "] not thrown", exception);
			assertEquals("Type of thrown exception does not match type of expected exception", expectedException, exception
					.getClass().getSimpleName());
		}
	}

	/**
	 * Imports an ImpEx from a classpath resource
	 *
	 * @param resourceName
	 *           name of the classpath resource which contains the ImpEx
	 * @throws ImpExException
	 * @throws IOException
	 */
	public void importImpExFromResourceWithoutFailingIfResourceDoesNotExist(final String resourceName) throws ImpExException,
			IOException
	{
		InputStream resourceAsStream = null;
		try
		{
			resourceAsStream = new ClassPathResource(resourceName).getInputStream();
		}
		catch (final FileNotFoundException e)
		{
			LOG.info(resourceName + " does not exist! not importing file");
			return;
		}
		importImpExFromInputStream(resourceName, resourceAsStream, null);
	}

	@Override
	public void setImpExAdaptor(final ImpExAdaptor impexAdaptor)
	{
		this.impExAdaptor = impexAdaptor;
	}

	@Override
	public void setRobotTestContext(final RobotTestContext robotTestContext)
	{
		this.robotTestContext = robotTestContext;
	}
}

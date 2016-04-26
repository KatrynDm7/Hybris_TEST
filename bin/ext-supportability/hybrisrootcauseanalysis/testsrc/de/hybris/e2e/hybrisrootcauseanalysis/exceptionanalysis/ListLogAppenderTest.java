package de.hybris.e2e.hybrisrootcauseanalysis.exceptionanalysis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import de.hybris.e2e.hybrisrootcauseanalysis.exceptionanalysis.constants.ListLogAppenderConstants;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;


/**
 * Stand alone tests for the appender class.
 *
 * Note: Platform is not instantiated for these tests.
 *
 * @author I842210
 *
 */
public class ListLogAppenderTest
{

	private static Logger LOG = Logger.getLogger(ListLogAppenderTest.class.getName());

	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();

	private static File currentLogFile = null;
	private static File currentTraceFile = null;
	private static final String NEEDLE = "03df2bc152bf47a3b95bc17db333533a";
	private ListLogAppender SUT;


	@BeforeClass
	public static void setupBeforeClass()
	{
		Logger.getRootLogger().getLoggerRepository().resetConfiguration();
		Logger.getRootLogger().removeAllAppenders();
		Logger.getRootLogger().setLevel(Level.INFO);
	}



	@Before
	public void setUp()
	{
		// Logger for this class to be set to DEBUG
		LOG.setLevel(Level.DEBUG);

		// Temporary logging files
		currentLogFile = new File(tempFolder.getRoot().getAbsolutePath() + File.separator + "_log.log");
		currentTraceFile = new File(tempFolder.getRoot().getAbsolutePath() + File.separator + "_trace.log");

		// Setup the ListLogAppender
		SUT = new ListLogAppender();
		SUT.setRotationCount(0);
		SUT.setRotationSize(0);
		SUT.setLogPath(currentLogFile.getAbsolutePath());
		SUT.setTracePath(currentTraceFile.getAbsolutePath());
		SUT.setLogSeverity("INFO");
		SUT.setTraceSeverity("ERROR");
		SUT.setEnableTracing(false);
		SUT.init();
	}

	@Test
	public void testAppenderSetup()
	{
		assertNotNull(Logger.getRootLogger().getAppender(ListLogAppenderConstants.APPENDER_NAME));
	}

	@Test
	public void testLogFileCreation()
	{
		SUT.setLogSeverity("INFO");
		LOG.info(NEEDLE);
		assertTrue(currentLogFile.exists() && !currentLogFile.isDirectory());
	}


	@Test
	public void testDynamicTraceToggling() throws IOException
	{
		SUT.setTraceSeverity("ERROR");
		SUT.setEnableTracing(false);

		// when tracing is disabled, the file should not be created
		LOG.error(NEEDLE);
		LOG.error(NEEDLE);
		LOG.error(NEEDLE);
		assertFalse(currentTraceFile.exists());

		// tracing enabled should create the file and insert the log
		SUT.setEnableTracing(true);
		LOG.error(NEEDLE);
		assertTrue(currentTraceFile.exists() && !currentTraceFile.isDirectory());
		String fileContent = FileUtils.readFileToString(currentTraceFile);
		int matchCount = StringUtils.countMatches(fileContent, NEEDLE);
		assertEquals(1, matchCount);

		// disabling the tracing again, the file should still exist but the trace should not be written
		SUT.setEnableTracing(false);
		LOG.error(NEEDLE);
		assertTrue(currentTraceFile.exists() && !currentTraceFile.isDirectory());
		fileContent = FileUtils.readFileToString(currentTraceFile);
		matchCount = StringUtils.countMatches(fileContent, NEEDLE);
		assertEquals(1, matchCount); // should still be 1
	}


	@Test
	public void testLogDebugSeverity() throws IOException
	{
		SUT.setLogSeverity("DEBUG");
		LOG.debug(NEEDLE);
		LOG.info(NEEDLE);
		LOG.warn(NEEDLE);
		LOG.error(NEEDLE);
		LOG.fatal(NEEDLE);

		final String fileContent = FileUtils.readFileToString(currentLogFile);
		final int matchCount = StringUtils.countMatches(fileContent, NEEDLE);
		assertEquals(5, matchCount);
	}


	@Test
	public void testLogWarnSeverity() throws IOException
	{
		SUT.setLogSeverity("WARNING");

		LOG.debug(NEEDLE);
		LOG.info(NEEDLE);
		LOG.warn(NEEDLE);
		LOG.error(NEEDLE);
		LOG.fatal(NEEDLE);

		final String fileContent = FileUtils.readFileToString(currentLogFile);
		final int matchCount = StringUtils.countMatches(fileContent, NEEDLE);
		assertEquals(3, matchCount);
	}




	@Test
	public void testNoneSeverityForLogAndTrace()
	{
		SUT.setLogSeverity("NONE");
		SUT.setTraceSeverity("NONE");

		LOG.debug(NEEDLE);
		LOG.info(NEEDLE);
		LOG.warn(NEEDLE);
		LOG.error(NEEDLE);
		LOG.fatal(NEEDLE);

		assertFalse(currentLogFile.exists());
		assertFalse(currentTraceFile.exists());
	}



	@Test
	public void testDynamicLoggerLevelChanges() throws IOException
	{
		// no log/trace when the LOGGER LEVEL = OFF
		LOG.setLevel(Level.OFF);
		SUT.setLogSeverity("DEBUG");
		SUT.setTraceSeverity("DEBUG");
		SUT.setEnableTracing(true);
		LOG.debug(NEEDLE);
		LOG.error(NEEDLE);
		assertFalse(currentLogFile.exists());
		assertFalse(currentTraceFile.exists());

		// no log/trace when LOGGER LEVEL = WARN and LOG.info() used
		LOG.setLevel(Level.WARN);
		SUT.setLogSeverity("DEBUG");
		SUT.setTraceSeverity("DEBUG");
		SUT.setEnableTracing(true);
		LOG.info(NEEDLE);
		assertFalse(currentLogFile.exists());
		assertFalse(currentTraceFile.exists());

	}

	@After
	public void tearDown()
	{
		Logger.getRootLogger().removeAllAppenders();
		currentLogFile.delete();
		currentTraceFile.delete();
		currentLogFile = currentTraceFile = null;
	}







}

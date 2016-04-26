/**
 *
 */
package de.hybris.e2e.hybrisrootcauseanalysis.exceptionanalysis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
 * @author I842210
 *
 */
public class ListLogConfigChangeListenerTest
{

	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();


	private File currentLogFile = null;
	private File currentTraceFile = null;


	private static ListLogAppender appender;
	private static ListLogConfigChangeListener listener;


	@BeforeClass
	public static void setupBeforeClass()
	{

		// Setup logger
		Logger.getRootLogger().getLoggerRepository().resetConfiguration();
		Logger.getRootLogger().setLevel(Level.ALL);
		Logger.getRootLogger().removeAllAppenders();
	}



	@Before
	public void setUp()
	{
		// create temporary files for logging
		createTemporaryLogFiles();

		// Setup appender
		appender = new ListLogAppender();
		appender.setRotationCount(0);
		appender.setRotationSize(0);
		appender.setLogPath(currentTraceFile.getAbsolutePath());
		appender.setTracePath(currentTraceFile.getAbsolutePath());
		appender.setLogSeverity("DEBUG");
		appender.setTraceSeverity("DEBUG");
		appender.setEnableTracing(false);
		appender.init();

		// create the listener that we will be testing
		listener = new ListLogConfigChangeListener();
		listener.setListLogAppender(appender);
	}


	private void createTemporaryLogFiles()
	{
		currentLogFile = new File(tempFolder.getRoot().getAbsolutePath() + File.separator + ".log");
		currentTraceFile = new File(tempFolder.getRoot().getAbsolutePath() + File.separator + ".log");
	}





	@Test
	public void testAllowedRuntimeConfigurationChanges()
	{

		final String newLogSeverity = "FATAL";
		final String newTraceSeverity = "ERROR";
		final String newEnableTracing = "TRUE";

		listener.configChanged(ListLogAppenderConstants.PROP_PREFIX + "logseverity", newLogSeverity);
		listener.configChanged(ListLogAppenderConstants.PROP_PREFIX + "traceseverity", newTraceSeverity);
		listener.configChanged(ListLogAppenderConstants.PROP_PREFIX + "enabletracing", newEnableTracing);

		assertEquals(appender.getLogSeverity(), newLogSeverity);
		assertEquals(appender.getTraceSeverity(), newTraceSeverity);
		assertTrue(appender.isEnableTracing());

	}




	@Test
	public void testProhibitedRuntimeConfigurationChanges() throws IOException
	{

		final String newLogPath = "someweridpath";
		final String newTracePath = "someevenweirderpath";
		final String newRotationCount = "352365";
		final String newRotationSize = "34";

		listener.configChanged(ListLogAppenderConstants.PROP_PREFIX + "logpath", newLogPath);
		listener.configChanged(ListLogAppenderConstants.PROP_PREFIX + "tracepath", newTracePath);
		listener.configChanged(ListLogAppenderConstants.PROP_PREFIX + "rotationcount", newRotationCount);
		listener.configChanged(ListLogAppenderConstants.PROP_PREFIX + "rotationsize", newRotationSize);

		assertFalse(appender.getLogPath() == newLogPath);
		assertFalse(appender.getTracePath() == newTracePath);
		assertFalse(appender.getRotationCount() == Integer.parseInt(newRotationCount));
		assertFalse(appender.getRotationSize() == Integer.parseInt(newRotationSize));

		final String fileContent = FileUtils.readFileToString(currentLogFile);
		final int matchCount = StringUtils.countMatches(fileContent,
				"can not be changed once the appender is initialized and integrated");
		assertEquals(4, matchCount);

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

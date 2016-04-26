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
package de.hybris.platform.catalog.synchronization;

import de.hybris.platform.cronjob.enums.ErrorMode;
import de.hybris.platform.cronjob.enums.JobLogLevel;


/**
 * Config object for the catalog synchronization.
 */
public class SyncConfig
{
	private Boolean logToFile;
	private Boolean logToDatabase;
	private Boolean forceUpdate;
	private Boolean keepCronJob;
	private JobLogLevel logLevelFile;
	private JobLogLevel logLevelDatabase;
	private Boolean createSavedValues;
	private ErrorMode errorMode;
	private Boolean synchronous;
	private Boolean fullSync;

	public Boolean getLogToFile()
	{
		return logToFile;
	}

	public void setLogToFile(final Boolean logToFile)
	{
		this.logToFile = logToFile;
	}

	public Boolean getLogToDatabase()
	{
		return logToDatabase;
	}

	public void setLogToDatabase(final Boolean logToDatabase)
	{
		this.logToDatabase = logToDatabase;
	}

	public Boolean getForceUpdate()
	{
		return forceUpdate;
	}

	public void setForceUpdate(final Boolean forceUpdate)
	{
		this.forceUpdate = forceUpdate;
	}

	public Boolean getKeepCronJob()
	{
		return keepCronJob;
	}

	public void setKeepCronJob(final Boolean keepCronJob)
	{
		this.keepCronJob = keepCronJob;
	}

	public JobLogLevel getLogLevelFile()
	{
		return logLevelFile;
	}

	public void setLogLevelFile(final JobLogLevel logLevelFile)
	{
		this.logLevelFile = logLevelFile;
	}

	public JobLogLevel getLogLevelDatabase()
	{
		return logLevelDatabase;
	}

	public void setLogLevelDatabase(final JobLogLevel logLevelDatabase)
	{
		this.logLevelDatabase = logLevelDatabase;
	}

	public Boolean getCreateSavedValues()
	{
		return createSavedValues;
	}

	public void setCreateSavedValues(final Boolean createSavedValues)
	{
		this.createSavedValues = createSavedValues;
	}

	public ErrorMode getErrorMode()
	{
		return errorMode;
	}

	public void setErrorMode(final ErrorMode errorMode)
	{
		this.errorMode = errorMode;
	}

	public Boolean getSynchronous()
	{
		return synchronous;
	}

	public void setSynchronous(final Boolean synchronous)
	{
		this.synchronous = synchronous;
	}

	public Boolean getFullSync()
	{
		return fullSync;
	}

	public void setFullSync(final Boolean fullSync)
	{
		this.fullSync = fullSync;
	}
}

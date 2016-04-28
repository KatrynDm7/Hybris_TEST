/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 28.04.2016 16:51:49                         ---
 * ----------------------------------------------------------------
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
 */
package de.hybris.platform.cronjob.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobMediaModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type MediaProcessCronJob first defined at extension processing.
 */
@SuppressWarnings("all")
public class MediaProcessCronJobModel extends CronJobModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "MediaProcessCronJob";
	
	/** <i>Generated constant</i> - Attribute key of <code>MediaProcessCronJob.jobMedia</code> attribute defined at extension <code>processing</code>. */
	public static final String JOBMEDIA = "jobMedia";
	
	/** <i>Generated constant</i> - Attribute key of <code>MediaProcessCronJob.currentLine</code> attribute defined at extension <code>processing</code>. */
	public static final String CURRENTLINE = "currentLine";
	
	/** <i>Generated constant</i> - Attribute key of <code>MediaProcessCronJob.lastSuccessfulLine</code> attribute defined at extension <code>processing</code>. */
	public static final String LASTSUCCESSFULLINE = "lastSuccessfulLine";
	
	
	/** <i>Generated variable</i> - Variable of <code>MediaProcessCronJob.jobMedia</code> attribute defined at extension <code>processing</code>. */
	private JobMediaModel _jobMedia;
	
	/** <i>Generated variable</i> - Variable of <code>MediaProcessCronJob.currentLine</code> attribute defined at extension <code>processing</code>. */
	private Integer _currentLine;
	
	/** <i>Generated variable</i> - Variable of <code>MediaProcessCronJob.lastSuccessfulLine</code> attribute defined at extension <code>processing</code>. */
	private Integer _lastSuccessfulLine;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public MediaProcessCronJobModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public MediaProcessCronJobModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _job initial attribute declared by type <code>CronJob</code> at extension <code>processing</code>
	 */
	@Deprecated
	public MediaProcessCronJobModel(final JobModel _job)
	{
		super();
		setJob(_job);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _job initial attribute declared by type <code>CronJob</code> at extension <code>processing</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public MediaProcessCronJobModel(final JobModel _job, final ItemModel _owner)
	{
		super();
		setJob(_job);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MediaProcessCronJob.currentLine</code> attribute defined at extension <code>processing</code>. 
	 * @return the currentLine
	 */
	@Accessor(qualifier = "currentLine", type = Accessor.Type.GETTER)
	public Integer getCurrentLine()
	{
		if (this._currentLine!=null)
		{
			return _currentLine;
		}
		return _currentLine = getPersistenceContext().getValue(CURRENTLINE, _currentLine);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MediaProcessCronJob.jobMedia</code> attribute defined at extension <code>processing</code>. 
	 * @return the jobMedia
	 */
	@Accessor(qualifier = "jobMedia", type = Accessor.Type.GETTER)
	public JobMediaModel getJobMedia()
	{
		if (this._jobMedia!=null)
		{
			return _jobMedia;
		}
		return _jobMedia = getPersistenceContext().getValue(JOBMEDIA, _jobMedia);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MediaProcessCronJob.lastSuccessfulLine</code> attribute defined at extension <code>processing</code>. 
	 * @return the lastSuccessfulLine
	 */
	@Accessor(qualifier = "lastSuccessfulLine", type = Accessor.Type.GETTER)
	public Integer getLastSuccessfulLine()
	{
		if (this._lastSuccessfulLine!=null)
		{
			return _lastSuccessfulLine;
		}
		return _lastSuccessfulLine = getPersistenceContext().getValue(LASTSUCCESSFULLINE, _lastSuccessfulLine);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MediaProcessCronJob.currentLine</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the currentLine
	 */
	@Accessor(qualifier = "currentLine", type = Accessor.Type.SETTER)
	public void setCurrentLine(final Integer value)
	{
		_currentLine = getPersistenceContext().setValue(CURRENTLINE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MediaProcessCronJob.jobMedia</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the jobMedia
	 */
	@Accessor(qualifier = "jobMedia", type = Accessor.Type.SETTER)
	public void setJobMedia(final JobMediaModel value)
	{
		_jobMedia = getPersistenceContext().setValue(JOBMEDIA, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MediaProcessCronJob.lastSuccessfulLine</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the lastSuccessfulLine
	 */
	@Accessor(qualifier = "lastSuccessfulLine", type = Accessor.Type.SETTER)
	public void setLastSuccessfulLine(final Integer value)
	{
		_lastSuccessfulLine = getPersistenceContext().setValue(LASTSUCCESSFULLINE, value);
	}
	
}

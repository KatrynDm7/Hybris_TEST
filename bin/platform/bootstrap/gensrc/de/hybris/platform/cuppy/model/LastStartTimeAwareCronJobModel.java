/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 18.04.2016 18:26:54                         ---
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
package de.hybris.platform.cuppy.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;

/**
 * Generated model class for type LastStartTimeAwareCronJob first defined at extension cuppy.
 * <p>
 * A CronJob holding the start time of last execution.
 */
@SuppressWarnings("all")
public class LastStartTimeAwareCronJobModel extends CronJobModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "LastStartTimeAwareCronJob";
	
	/** <i>Generated constant</i> - Attribute key of <code>LastStartTimeAwareCronJob.lastStartTime</code> attribute defined at extension <code>cuppy</code>. */
	public static final String LASTSTARTTIME = "lastStartTime";
	
	
	/** <i>Generated variable</i> - Variable of <code>LastStartTimeAwareCronJob.lastStartTime</code> attribute defined at extension <code>cuppy</code>. */
	private Date _lastStartTime;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public LastStartTimeAwareCronJobModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public LastStartTimeAwareCronJobModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _job initial attribute declared by type <code>CronJob</code> at extension <code>processing</code>
	 */
	@Deprecated
	public LastStartTimeAwareCronJobModel(final JobModel _job)
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
	public LastStartTimeAwareCronJobModel(final JobModel _job, final ItemModel _owner)
	{
		super();
		setJob(_job);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LastStartTimeAwareCronJob.lastStartTime</code> attribute defined at extension <code>cuppy</code>. 
	 * @return the lastStartTime - Start time of last execution.
	 */
	@Accessor(qualifier = "lastStartTime", type = Accessor.Type.GETTER)
	public Date getLastStartTime()
	{
		if (this._lastStartTime!=null)
		{
			return _lastStartTime;
		}
		return _lastStartTime = getPersistenceContext().getValue(LASTSTARTTIME, _lastStartTime);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>LastStartTimeAwareCronJob.lastStartTime</code> attribute defined at extension <code>cuppy</code>. 
	 *  
	 * @param value the lastStartTime - Start time of last execution.
	 */
	@Accessor(qualifier = "lastStartTime", type = Accessor.Type.SETTER)
	public void setLastStartTime(final Date value)
	{
		_lastStartTime = getPersistenceContext().setValue(LASTSTARTTIME, value);
	}
	
}

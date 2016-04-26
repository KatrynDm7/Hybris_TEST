/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 07.04.2016 16:34:24                         ---
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
package de.hybris.platform.cuppy.jalo;

import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.cuppy.constants.CuppyConstants;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.cuppy.jalo.LastStartTimeAwareCronJob LastStartTimeAwareCronJob}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedLastStartTimeAwareCronJob extends CronJob
{
	/** Qualifier of the <code>LastStartTimeAwareCronJob.lastStartTime</code> attribute **/
	public static final String LASTSTARTTIME = "lastStartTime";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(CronJob.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(LASTSTARTTIME, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LastStartTimeAwareCronJob.lastStartTime</code> attribute.
	 * @return the lastStartTime - Start time of last execution.
	 */
	public Date getLastStartTime(final SessionContext ctx)
	{
		return (Date)getProperty( ctx, LASTSTARTTIME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LastStartTimeAwareCronJob.lastStartTime</code> attribute.
	 * @return the lastStartTime - Start time of last execution.
	 */
	public Date getLastStartTime()
	{
		return getLastStartTime( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>LastStartTimeAwareCronJob.lastStartTime</code> attribute. 
	 * @param value the lastStartTime - Start time of last execution.
	 */
	public void setLastStartTime(final SessionContext ctx, final Date value)
	{
		setProperty(ctx, LASTSTARTTIME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>LastStartTimeAwareCronJob.lastStartTime</code> attribute. 
	 * @param value the lastStartTime - Start time of last execution.
	 */
	public void setLastStartTime(final Date value)
	{
		setLastStartTime( getSession().getSessionContext(), value );
	}
	
}

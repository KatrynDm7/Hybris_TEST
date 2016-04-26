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

import de.hybris.platform.cuppy.constants.CuppyConstants;
import de.hybris.platform.cuppy.jalo.Competition;
import de.hybris.platform.cuppy.jalo.LastStartTimeAwareCronJob;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.cuppy.jalo.UpdateCompetitionCronJob UpdateCompetitionCronJob}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedUpdateCompetitionCronJob extends LastStartTimeAwareCronJob
{
	/** Qualifier of the <code>UpdateCompetitionCronJob.competition</code> attribute **/
	public static final String COMPETITION = "competition";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(LastStartTimeAwareCronJob.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(COMPETITION, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UpdateCompetitionCronJob.competition</code> attribute.
	 * @return the competition - Competition this CronJob will update.
	 */
	public Competition getCompetition(final SessionContext ctx)
	{
		return (Competition)getProperty( ctx, COMPETITION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UpdateCompetitionCronJob.competition</code> attribute.
	 * @return the competition - Competition this CronJob will update.
	 */
	public Competition getCompetition()
	{
		return getCompetition( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>UpdateCompetitionCronJob.competition</code> attribute. 
	 * @param value the competition - Competition this CronJob will update.
	 */
	public void setCompetition(final SessionContext ctx, final Competition value)
	{
		setProperty(ctx, COMPETITION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>UpdateCompetitionCronJob.competition</code> attribute. 
	 * @param value the competition - Competition this CronJob will update.
	 */
	public void setCompetition(final Competition value)
	{
		setCompetition( getSession().getSessionContext(), value );
	}
	
}

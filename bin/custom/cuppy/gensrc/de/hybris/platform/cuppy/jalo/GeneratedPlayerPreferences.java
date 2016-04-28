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
package de.hybris.platform.cuppy.jalo;

import de.hybris.platform.cuppy.constants.CuppyConstants;
import de.hybris.platform.cuppy.jalo.Competition;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.cuppy.jalo.PlayerPreferences PlayerPreferences}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedPlayerPreferences extends GenericItem
{
	/** Qualifier of the <code>PlayerPreferences.currentCompetition</code> attribute **/
	public static final String CURRENTCOMPETITION = "currentCompetition";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(CURRENTCOMPETITION, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PlayerPreferences.currentCompetition</code> attribute.
	 * @return the currentCompetition - Competition the player was viewing last at frontend.
	 */
	public Competition getCurrentCompetition(final SessionContext ctx)
	{
		return (Competition)getProperty( ctx, CURRENTCOMPETITION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PlayerPreferences.currentCompetition</code> attribute.
	 * @return the currentCompetition - Competition the player was viewing last at frontend.
	 */
	public Competition getCurrentCompetition()
	{
		return getCurrentCompetition( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PlayerPreferences.currentCompetition</code> attribute. 
	 * @param value the currentCompetition - Competition the player was viewing last at frontend.
	 */
	public void setCurrentCompetition(final SessionContext ctx, final Competition value)
	{
		setProperty(ctx, CURRENTCOMPETITION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PlayerPreferences.currentCompetition</code> attribute. 
	 * @param value the currentCompetition - Competition the player was viewing last at frontend.
	 */
	public void setCurrentCompetition(final Competition value)
	{
		setCurrentCompetition( getSession().getSessionContext(), value );
	}
	
}

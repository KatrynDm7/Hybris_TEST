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
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.cuppy.jalo.OverallStatistic OverallStatistic}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedOverallStatistic extends GenericItem
{
	/** Qualifier of the <code>OverallStatistic.playersOnlineMaxCount</code> attribute **/
	public static final String PLAYERSONLINEMAXCOUNT = "playersOnlineMaxCount";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(PLAYERSONLINEMAXCOUNT, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OverallStatistic.playersOnlineMaxCount</code> attribute.
	 * @return the playersOnlineMaxCount - Maximal amount of players online at same time.
	 */
	public Integer getPlayersOnlineMaxCount(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, PLAYERSONLINEMAXCOUNT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OverallStatistic.playersOnlineMaxCount</code> attribute.
	 * @return the playersOnlineMaxCount - Maximal amount of players online at same time.
	 */
	public Integer getPlayersOnlineMaxCount()
	{
		return getPlayersOnlineMaxCount( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OverallStatistic.playersOnlineMaxCount</code> attribute. 
	 * @return the playersOnlineMaxCount - Maximal amount of players online at same time.
	 */
	public int getPlayersOnlineMaxCountAsPrimitive(final SessionContext ctx)
	{
		Integer value = getPlayersOnlineMaxCount( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OverallStatistic.playersOnlineMaxCount</code> attribute. 
	 * @return the playersOnlineMaxCount - Maximal amount of players online at same time.
	 */
	public int getPlayersOnlineMaxCountAsPrimitive()
	{
		return getPlayersOnlineMaxCountAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>OverallStatistic.playersOnlineMaxCount</code> attribute. 
	 * @param value the playersOnlineMaxCount - Maximal amount of players online at same time.
	 */
	public void setPlayersOnlineMaxCount(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, PLAYERSONLINEMAXCOUNT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>OverallStatistic.playersOnlineMaxCount</code> attribute. 
	 * @param value the playersOnlineMaxCount - Maximal amount of players online at same time.
	 */
	public void setPlayersOnlineMaxCount(final Integer value)
	{
		setPlayersOnlineMaxCount( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>OverallStatistic.playersOnlineMaxCount</code> attribute. 
	 * @param value the playersOnlineMaxCount - Maximal amount of players online at same time.
	 */
	public void setPlayersOnlineMaxCount(final SessionContext ctx, final int value)
	{
		setPlayersOnlineMaxCount( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>OverallStatistic.playersOnlineMaxCount</code> attribute. 
	 * @param value the playersOnlineMaxCount - Maximal amount of players online at same time.
	 */
	public void setPlayersOnlineMaxCount(final int value)
	{
		setPlayersOnlineMaxCount( getSession().getSessionContext(), value );
	}
	
}

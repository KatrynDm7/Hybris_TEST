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
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.cuppy.jalo.TimePointStatistic TimePointStatistic}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedTimePointStatistic extends GenericItem
{
	/** Qualifier of the <code>TimePointStatistic.playersOnlineCount</code> attribute **/
	public static final String PLAYERSONLINECOUNT = "playersOnlineCount";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(PLAYERSONLINECOUNT, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TimePointStatistic.playersOnlineCount</code> attribute.
	 * @return the playersOnlineCount - Amount of players online (logged in) at creation time of this item.
	 */
	public Integer getPlayersOnlineCount(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, PLAYERSONLINECOUNT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TimePointStatistic.playersOnlineCount</code> attribute.
	 * @return the playersOnlineCount - Amount of players online (logged in) at creation time of this item.
	 */
	public Integer getPlayersOnlineCount()
	{
		return getPlayersOnlineCount( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TimePointStatistic.playersOnlineCount</code> attribute. 
	 * @return the playersOnlineCount - Amount of players online (logged in) at creation time of this item.
	 */
	public int getPlayersOnlineCountAsPrimitive(final SessionContext ctx)
	{
		Integer value = getPlayersOnlineCount( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TimePointStatistic.playersOnlineCount</code> attribute. 
	 * @return the playersOnlineCount - Amount of players online (logged in) at creation time of this item.
	 */
	public int getPlayersOnlineCountAsPrimitive()
	{
		return getPlayersOnlineCountAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TimePointStatistic.playersOnlineCount</code> attribute. 
	 * @param value the playersOnlineCount - Amount of players online (logged in) at creation time of this item.
	 */
	public void setPlayersOnlineCount(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, PLAYERSONLINECOUNT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TimePointStatistic.playersOnlineCount</code> attribute. 
	 * @param value the playersOnlineCount - Amount of players online (logged in) at creation time of this item.
	 */
	public void setPlayersOnlineCount(final Integer value)
	{
		setPlayersOnlineCount( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TimePointStatistic.playersOnlineCount</code> attribute. 
	 * @param value the playersOnlineCount - Amount of players online (logged in) at creation time of this item.
	 */
	public void setPlayersOnlineCount(final SessionContext ctx, final int value)
	{
		setPlayersOnlineCount( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>TimePointStatistic.playersOnlineCount</code> attribute. 
	 * @param value the playersOnlineCount - Amount of players online (logged in) at creation time of this item.
	 */
	public void setPlayersOnlineCount(final int value)
	{
		setPlayersOnlineCount( getSession().getSessionContext(), value );
	}
	
}

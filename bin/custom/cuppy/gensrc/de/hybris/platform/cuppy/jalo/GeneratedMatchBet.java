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
import de.hybris.platform.cuppy.jalo.Match;
import de.hybris.platform.cuppy.jalo.Player;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.cuppy.jalo.MatchBet MatchBet}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedMatchBet extends GenericItem
{
	/** Qualifier of the <code>MatchBet.guestGoals</code> attribute **/
	public static final String GUESTGOALS = "guestGoals";
	/** Qualifier of the <code>MatchBet.homeGoals</code> attribute **/
	public static final String HOMEGOALS = "homeGoals";
	/** Qualifier of the <code>MatchBet.player</code> attribute **/
	public static final String PLAYER = "player";
	/** Qualifier of the <code>MatchBet.match</code> attribute **/
	public static final String MATCH = "match";
	/**
	* {@link BidirectionalOneToManyHandler} for handling 1:n PLAYER's relation attributes from 'one' side.
	**/
	protected static final BidirectionalOneToManyHandler<GeneratedMatchBet> PLAYERHANDLER = new BidirectionalOneToManyHandler<GeneratedMatchBet>(
	CuppyConstants.TC.MATCHBET,
	false,
	"player",
	null,
	false,
	true,
	CollectionType.COLLECTION
	);
	/**
	* {@link BidirectionalOneToManyHandler} for handling 1:n MATCH's relation attributes from 'one' side.
	**/
	protected static final BidirectionalOneToManyHandler<GeneratedMatchBet> MATCHHANDLER = new BidirectionalOneToManyHandler<GeneratedMatchBet>(
	CuppyConstants.TC.MATCHBET,
	false,
	"match",
	null,
	false,
	true,
	CollectionType.COLLECTION
	);
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(GUESTGOALS, AttributeMode.INITIAL);
		tmp.put(HOMEGOALS, AttributeMode.INITIAL);
		tmp.put(PLAYER, AttributeMode.INITIAL);
		tmp.put(MATCH, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	@Override
	protected Item createItem(final SessionContext ctx, final ComposedType type, final ItemAttributeMap allAttributes) throws JaloBusinessException
	{
		PLAYERHANDLER.newInstance(ctx, allAttributes);
		MATCHHANDLER.newInstance(ctx, allAttributes);
		return super.createItem( ctx, type, allAttributes );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MatchBet.guestGoals</code> attribute.
	 * @return the guestGoals - Amount of goals for guest team at end of match.
	 */
	public Integer getGuestGoals(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, GUESTGOALS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MatchBet.guestGoals</code> attribute.
	 * @return the guestGoals - Amount of goals for guest team at end of match.
	 */
	public Integer getGuestGoals()
	{
		return getGuestGoals( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MatchBet.guestGoals</code> attribute. 
	 * @return the guestGoals - Amount of goals for guest team at end of match.
	 */
	public int getGuestGoalsAsPrimitive(final SessionContext ctx)
	{
		Integer value = getGuestGoals( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MatchBet.guestGoals</code> attribute. 
	 * @return the guestGoals - Amount of goals for guest team at end of match.
	 */
	public int getGuestGoalsAsPrimitive()
	{
		return getGuestGoalsAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MatchBet.guestGoals</code> attribute. 
	 * @param value the guestGoals - Amount of goals for guest team at end of match.
	 */
	public void setGuestGoals(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, GUESTGOALS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MatchBet.guestGoals</code> attribute. 
	 * @param value the guestGoals - Amount of goals for guest team at end of match.
	 */
	public void setGuestGoals(final Integer value)
	{
		setGuestGoals( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MatchBet.guestGoals</code> attribute. 
	 * @param value the guestGoals - Amount of goals for guest team at end of match.
	 */
	public void setGuestGoals(final SessionContext ctx, final int value)
	{
		setGuestGoals( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MatchBet.guestGoals</code> attribute. 
	 * @param value the guestGoals - Amount of goals for guest team at end of match.
	 */
	public void setGuestGoals(final int value)
	{
		setGuestGoals( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MatchBet.homeGoals</code> attribute.
	 * @return the homeGoals - Amount of goals for home team at end of match.
	 */
	public Integer getHomeGoals(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, HOMEGOALS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MatchBet.homeGoals</code> attribute.
	 * @return the homeGoals - Amount of goals for home team at end of match.
	 */
	public Integer getHomeGoals()
	{
		return getHomeGoals( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MatchBet.homeGoals</code> attribute. 
	 * @return the homeGoals - Amount of goals for home team at end of match.
	 */
	public int getHomeGoalsAsPrimitive(final SessionContext ctx)
	{
		Integer value = getHomeGoals( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MatchBet.homeGoals</code> attribute. 
	 * @return the homeGoals - Amount of goals for home team at end of match.
	 */
	public int getHomeGoalsAsPrimitive()
	{
		return getHomeGoalsAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MatchBet.homeGoals</code> attribute. 
	 * @param value the homeGoals - Amount of goals for home team at end of match.
	 */
	public void setHomeGoals(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, HOMEGOALS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MatchBet.homeGoals</code> attribute. 
	 * @param value the homeGoals - Amount of goals for home team at end of match.
	 */
	public void setHomeGoals(final Integer value)
	{
		setHomeGoals( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MatchBet.homeGoals</code> attribute. 
	 * @param value the homeGoals - Amount of goals for home team at end of match.
	 */
	public void setHomeGoals(final SessionContext ctx, final int value)
	{
		setHomeGoals( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MatchBet.homeGoals</code> attribute. 
	 * @param value the homeGoals - Amount of goals for home team at end of match.
	 */
	public void setHomeGoals(final int value)
	{
		setHomeGoals( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MatchBet.match</code> attribute.
	 * @return the match - The match this bet belongs to.
	 */
	public Match getMatch(final SessionContext ctx)
	{
		return (Match)getProperty( ctx, MATCH);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MatchBet.match</code> attribute.
	 * @return the match - The match this bet belongs to.
	 */
	public Match getMatch()
	{
		return getMatch( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MatchBet.match</code> attribute. 
	 * @param value the match - The match this bet belongs to.
	 */
	public void setMatch(final SessionContext ctx, final Match value)
	{
		MATCHHANDLER.addValue( ctx, value, this  );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MatchBet.match</code> attribute. 
	 * @param value the match - The match this bet belongs to.
	 */
	public void setMatch(final Match value)
	{
		setMatch( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MatchBet.player</code> attribute.
	 * @return the player - The player this bet belongs to.
	 */
	public Player getPlayer(final SessionContext ctx)
	{
		return (Player)getProperty( ctx, PLAYER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MatchBet.player</code> attribute.
	 * @return the player - The player this bet belongs to.
	 */
	public Player getPlayer()
	{
		return getPlayer( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MatchBet.player</code> attribute. 
	 * @param value the player - The player this bet belongs to.
	 */
	public void setPlayer(final SessionContext ctx, final Player value)
	{
		PLAYERHANDLER.addValue( ctx, value, this  );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MatchBet.player</code> attribute. 
	 * @param value the player - The player this bet belongs to.
	 */
	public void setPlayer(final Player value)
	{
		setPlayer( getSession().getSessionContext(), value );
	}
	
}

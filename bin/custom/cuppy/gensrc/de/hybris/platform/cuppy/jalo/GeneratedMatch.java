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
import de.hybris.platform.cuppy.jalo.Group;
import de.hybris.platform.cuppy.jalo.MatchBet;
import de.hybris.platform.cuppy.jalo.Team;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.cuppy.jalo.Match Match}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedMatch extends GenericItem
{
	/** Qualifier of the <code>Match.id</code> attribute **/
	public static final String ID = "id";
	/** Qualifier of the <code>Match.guestGoals</code> attribute **/
	public static final String GUESTGOALS = "guestGoals";
	/** Qualifier of the <code>Match.homeGoals</code> attribute **/
	public static final String HOMEGOALS = "homeGoals";
	/** Qualifier of the <code>Match.location</code> attribute **/
	public static final String LOCATION = "location";
	/** Qualifier of the <code>Match.date</code> attribute **/
	public static final String DATE = "date";
	/** Qualifier of the <code>Match.matchday</code> attribute **/
	public static final String MATCHDAY = "matchday";
	/** Qualifier of the <code>Match.matchBets</code> attribute **/
	public static final String MATCHBETS = "matchBets";
	/** Qualifier of the <code>Match.group</code> attribute **/
	public static final String GROUP = "group";
	/** Qualifier of the <code>Match.guestTeam</code> attribute **/
	public static final String GUESTTEAM = "guestTeam";
	/** Qualifier of the <code>Match.homeTeam</code> attribute **/
	public static final String HOMETEAM = "homeTeam";
	/**
	* {@link OneToManyHandler} for handling 1:n MATCHBETS's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<MatchBet> MATCHBETSHANDLER = new OneToManyHandler<MatchBet>(
	CuppyConstants.TC.MATCHBET,
	true,
	"match",
	null,
	false,
	true,
	CollectionType.COLLECTION
	);
	/**
	* {@link BidirectionalOneToManyHandler} for handling 1:n GROUP's relation attributes from 'one' side.
	**/
	protected static final BidirectionalOneToManyHandler<GeneratedMatch> GROUPHANDLER = new BidirectionalOneToManyHandler<GeneratedMatch>(
	CuppyConstants.TC.MATCH,
	false,
	"group",
	null,
	false,
	true,
	CollectionType.COLLECTION
	);
	/**
	* {@link BidirectionalOneToManyHandler} for handling 1:n GUESTTEAM's relation attributes from 'one' side.
	**/
	protected static final BidirectionalOneToManyHandler<GeneratedMatch> GUESTTEAMHANDLER = new BidirectionalOneToManyHandler<GeneratedMatch>(
	CuppyConstants.TC.MATCH,
	false,
	"guestTeam",
	null,
	false,
	true,
	CollectionType.COLLECTION
	);
	/**
	* {@link BidirectionalOneToManyHandler} for handling 1:n HOMETEAM's relation attributes from 'one' side.
	**/
	protected static final BidirectionalOneToManyHandler<GeneratedMatch> HOMETEAMHANDLER = new BidirectionalOneToManyHandler<GeneratedMatch>(
	CuppyConstants.TC.MATCH,
	false,
	"homeTeam",
	null,
	false,
	true,
	CollectionType.COLLECTION
	);
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(ID, AttributeMode.INITIAL);
		tmp.put(GUESTGOALS, AttributeMode.INITIAL);
		tmp.put(HOMEGOALS, AttributeMode.INITIAL);
		tmp.put(LOCATION, AttributeMode.INITIAL);
		tmp.put(DATE, AttributeMode.INITIAL);
		tmp.put(MATCHDAY, AttributeMode.INITIAL);
		tmp.put(GROUP, AttributeMode.INITIAL);
		tmp.put(GUESTTEAM, AttributeMode.INITIAL);
		tmp.put(HOMETEAM, AttributeMode.INITIAL);
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
		GROUPHANDLER.newInstance(ctx, allAttributes);
		GUESTTEAMHANDLER.newInstance(ctx, allAttributes);
		HOMETEAMHANDLER.newInstance(ctx, allAttributes);
		return super.createItem( ctx, type, allAttributes );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Match.date</code> attribute.
	 * @return the date - The date of the match (start date/time).
	 */
	public Date getDate(final SessionContext ctx)
	{
		return (Date)getProperty( ctx, DATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Match.date</code> attribute.
	 * @return the date - The date of the match (start date/time).
	 */
	public Date getDate()
	{
		return getDate( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Match.date</code> attribute. 
	 * @param value the date - The date of the match (start date/time).
	 */
	public void setDate(final SessionContext ctx, final Date value)
	{
		setProperty(ctx, DATE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Match.date</code> attribute. 
	 * @param value the date - The date of the match (start date/time).
	 */
	public void setDate(final Date value)
	{
		setDate( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Match.group</code> attribute.
	 * @return the group - The group the match belongs to.
	 */
	public Group getGroup(final SessionContext ctx)
	{
		return (Group)getProperty( ctx, GROUP);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Match.group</code> attribute.
	 * @return the group - The group the match belongs to.
	 */
	public Group getGroup()
	{
		return getGroup( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Match.group</code> attribute. 
	 * @param value the group - The group the match belongs to.
	 */
	public void setGroup(final SessionContext ctx, final Group value)
	{
		GROUPHANDLER.addValue( ctx, value, this  );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Match.group</code> attribute. 
	 * @param value the group - The group the match belongs to.
	 */
	public void setGroup(final Group value)
	{
		setGroup( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Match.guestGoals</code> attribute.
	 * @return the guestGoals - Amount of goals the guest team had at end of match.
	 */
	public Integer getGuestGoals(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, GUESTGOALS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Match.guestGoals</code> attribute.
	 * @return the guestGoals - Amount of goals the guest team had at end of match.
	 */
	public Integer getGuestGoals()
	{
		return getGuestGoals( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Match.guestGoals</code> attribute. 
	 * @return the guestGoals - Amount of goals the guest team had at end of match.
	 */
	public int getGuestGoalsAsPrimitive(final SessionContext ctx)
	{
		Integer value = getGuestGoals( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Match.guestGoals</code> attribute. 
	 * @return the guestGoals - Amount of goals the guest team had at end of match.
	 */
	public int getGuestGoalsAsPrimitive()
	{
		return getGuestGoalsAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Match.guestGoals</code> attribute. 
	 * @param value the guestGoals - Amount of goals the guest team had at end of match.
	 */
	public void setGuestGoals(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, GUESTGOALS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Match.guestGoals</code> attribute. 
	 * @param value the guestGoals - Amount of goals the guest team had at end of match.
	 */
	public void setGuestGoals(final Integer value)
	{
		setGuestGoals( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Match.guestGoals</code> attribute. 
	 * @param value the guestGoals - Amount of goals the guest team had at end of match.
	 */
	public void setGuestGoals(final SessionContext ctx, final int value)
	{
		setGuestGoals( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Match.guestGoals</code> attribute. 
	 * @param value the guestGoals - Amount of goals the guest team had at end of match.
	 */
	public void setGuestGoals(final int value)
	{
		setGuestGoals( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Match.guestTeam</code> attribute.
	 * @return the guestTeam - The guest team of the match.
	 */
	public Team getGuestTeam(final SessionContext ctx)
	{
		return (Team)getProperty( ctx, GUESTTEAM);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Match.guestTeam</code> attribute.
	 * @return the guestTeam - The guest team of the match.
	 */
	public Team getGuestTeam()
	{
		return getGuestTeam( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Match.guestTeam</code> attribute. 
	 * @param value the guestTeam - The guest team of the match.
	 */
	public void setGuestTeam(final SessionContext ctx, final Team value)
	{
		GUESTTEAMHANDLER.addValue( ctx, value, this  );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Match.guestTeam</code> attribute. 
	 * @param value the guestTeam - The guest team of the match.
	 */
	public void setGuestTeam(final Team value)
	{
		setGuestTeam( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Match.homeGoals</code> attribute.
	 * @return the homeGoals - Amount of goals the home team had at end of match.
	 */
	public Integer getHomeGoals(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, HOMEGOALS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Match.homeGoals</code> attribute.
	 * @return the homeGoals - Amount of goals the home team had at end of match.
	 */
	public Integer getHomeGoals()
	{
		return getHomeGoals( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Match.homeGoals</code> attribute. 
	 * @return the homeGoals - Amount of goals the home team had at end of match.
	 */
	public int getHomeGoalsAsPrimitive(final SessionContext ctx)
	{
		Integer value = getHomeGoals( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Match.homeGoals</code> attribute. 
	 * @return the homeGoals - Amount of goals the home team had at end of match.
	 */
	public int getHomeGoalsAsPrimitive()
	{
		return getHomeGoalsAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Match.homeGoals</code> attribute. 
	 * @param value the homeGoals - Amount of goals the home team had at end of match.
	 */
	public void setHomeGoals(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, HOMEGOALS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Match.homeGoals</code> attribute. 
	 * @param value the homeGoals - Amount of goals the home team had at end of match.
	 */
	public void setHomeGoals(final Integer value)
	{
		setHomeGoals( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Match.homeGoals</code> attribute. 
	 * @param value the homeGoals - Amount of goals the home team had at end of match.
	 */
	public void setHomeGoals(final SessionContext ctx, final int value)
	{
		setHomeGoals( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Match.homeGoals</code> attribute. 
	 * @param value the homeGoals - Amount of goals the home team had at end of match.
	 */
	public void setHomeGoals(final int value)
	{
		setHomeGoals( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Match.homeTeam</code> attribute.
	 * @return the homeTeam - The home team of the match.
	 */
	public Team getHomeTeam(final SessionContext ctx)
	{
		return (Team)getProperty( ctx, HOMETEAM);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Match.homeTeam</code> attribute.
	 * @return the homeTeam - The home team of the match.
	 */
	public Team getHomeTeam()
	{
		return getHomeTeam( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Match.homeTeam</code> attribute. 
	 * @param value the homeTeam - The home team of the match.
	 */
	public void setHomeTeam(final SessionContext ctx, final Team value)
	{
		HOMETEAMHANDLER.addValue( ctx, value, this  );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Match.homeTeam</code> attribute. 
	 * @param value the homeTeam - The home team of the match.
	 */
	public void setHomeTeam(final Team value)
	{
		setHomeTeam( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Match.id</code> attribute.
	 * @return the id - Unique identifier of match within a competition.
	 */
	public Integer getId(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, ID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Match.id</code> attribute.
	 * @return the id - Unique identifier of match within a competition.
	 */
	public Integer getId()
	{
		return getId( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Match.id</code> attribute. 
	 * @return the id - Unique identifier of match within a competition.
	 */
	public int getIdAsPrimitive(final SessionContext ctx)
	{
		Integer value = getId( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Match.id</code> attribute. 
	 * @return the id - Unique identifier of match within a competition.
	 */
	public int getIdAsPrimitive()
	{
		return getIdAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Match.id</code> attribute. 
	 * @param value the id - Unique identifier of match within a competition.
	 */
	public void setId(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, ID,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Match.id</code> attribute. 
	 * @param value the id - Unique identifier of match within a competition.
	 */
	public void setId(final Integer value)
	{
		setId( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Match.id</code> attribute. 
	 * @param value the id - Unique identifier of match within a competition.
	 */
	public void setId(final SessionContext ctx, final int value)
	{
		setId( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Match.id</code> attribute. 
	 * @param value the id - Unique identifier of match within a competition.
	 */
	public void setId(final int value)
	{
		setId( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Match.location</code> attribute.
	 * @return the location - Description of place where match take/took place.
	 */
	public String getLocation(final SessionContext ctx)
	{
		return (String)getProperty( ctx, LOCATION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Match.location</code> attribute.
	 * @return the location - Description of place where match take/took place.
	 */
	public String getLocation()
	{
		return getLocation( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Match.location</code> attribute. 
	 * @param value the location - Description of place where match take/took place.
	 */
	public void setLocation(final SessionContext ctx, final String value)
	{
		setProperty(ctx, LOCATION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Match.location</code> attribute. 
	 * @param value the location - Description of place where match take/took place.
	 */
	public void setLocation(final String value)
	{
		setLocation( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Match.matchBets</code> attribute.
	 * @return the matchBets - The bets placed on this match.
	 */
	public Collection<MatchBet> getMatchBets(final SessionContext ctx)
	{
		return MATCHBETSHANDLER.getValues( ctx, this );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Match.matchBets</code> attribute.
	 * @return the matchBets - The bets placed on this match.
	 */
	public Collection<MatchBet> getMatchBets()
	{
		return getMatchBets( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Match.matchBets</code> attribute. 
	 * @param value the matchBets - The bets placed on this match.
	 */
	public void setMatchBets(final SessionContext ctx, final Collection<MatchBet> value)
	{
		MATCHBETSHANDLER.setValues( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Match.matchBets</code> attribute. 
	 * @param value the matchBets - The bets placed on this match.
	 */
	public void setMatchBets(final Collection<MatchBet> value)
	{
		setMatchBets( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to matchBets. 
	 * @param value the item to add to matchBets - The bets placed on this match.
	 */
	public void addToMatchBets(final SessionContext ctx, final MatchBet value)
	{
		MATCHBETSHANDLER.addValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to matchBets. 
	 * @param value the item to add to matchBets - The bets placed on this match.
	 */
	public void addToMatchBets(final MatchBet value)
	{
		addToMatchBets( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from matchBets. 
	 * @param value the item to remove from matchBets - The bets placed on this match.
	 */
	public void removeFromMatchBets(final SessionContext ctx, final MatchBet value)
	{
		MATCHBETSHANDLER.removeValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from matchBets. 
	 * @param value the item to remove from matchBets - The bets placed on this match.
	 */
	public void removeFromMatchBets(final MatchBet value)
	{
		removeFromMatchBets( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Match.matchday</code> attribute.
	 * @return the matchday - Absolute number of day of match within competition. Used to order matches by day.
	 * 					For league games it is similar to the matchday.
	 */
	public Integer getMatchday(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, MATCHDAY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Match.matchday</code> attribute.
	 * @return the matchday - Absolute number of day of match within competition. Used to order matches by day.
	 * 					For league games it is similar to the matchday.
	 */
	public Integer getMatchday()
	{
		return getMatchday( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Match.matchday</code> attribute. 
	 * @return the matchday - Absolute number of day of match within competition. Used to order matches by day.
	 * 					For league games it is similar to the matchday.
	 */
	public int getMatchdayAsPrimitive(final SessionContext ctx)
	{
		Integer value = getMatchday( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Match.matchday</code> attribute. 
	 * @return the matchday - Absolute number of day of match within competition. Used to order matches by day.
	 * 					For league games it is similar to the matchday.
	 */
	public int getMatchdayAsPrimitive()
	{
		return getMatchdayAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Match.matchday</code> attribute. 
	 * @param value the matchday - Absolute number of day of match within competition. Used to order matches by day.
	 * 					For league games it is similar to the matchday.
	 */
	public void setMatchday(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, MATCHDAY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Match.matchday</code> attribute. 
	 * @param value the matchday - Absolute number of day of match within competition. Used to order matches by day.
	 * 					For league games it is similar to the matchday.
	 */
	public void setMatchday(final Integer value)
	{
		setMatchday( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Match.matchday</code> attribute. 
	 * @param value the matchday - Absolute number of day of match within competition. Used to order matches by day.
	 * 					For league games it is similar to the matchday.
	 */
	public void setMatchday(final SessionContext ctx, final int value)
	{
		setMatchday( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Match.matchday</code> attribute. 
	 * @param value the matchday - Absolute number of day of match within competition. Used to order matches by day.
	 * 					For league games it is similar to the matchday.
	 */
	public void setMatchday(final int value)
	{
		setMatchday( getSession().getSessionContext(), value );
	}
	
}

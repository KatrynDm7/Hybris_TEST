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
import de.hybris.platform.cuppy.model.GroupModel;
import de.hybris.platform.cuppy.model.MatchBetModel;
import de.hybris.platform.cuppy.model.TeamModel;
import de.hybris.platform.cuppytrail.model.StadiumModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Date;

/**
 * Generated model class for type Match first defined at extension cuppy.
 * <p>
 * A match is part of a competition and played by a home and guest team.
 */
@SuppressWarnings("all")
public class MatchModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "Match";
	
	/**<i>Generated relation code constant for relation <code>MatchBetMatchRelation</code> defining source attribute <code>matchBets</code> in extension <code>cuppy</code>.</i>*/
	public final static String _MATCHBETMATCHRELATION = "MatchBetMatchRelation";
	
	/**<i>Generated relation code constant for relation <code>StadiumMatchRelation</code> defining source attribute <code>stadium</code> in extension <code>cuppytrail</code>.</i>*/
	public final static String _STADIUMMATCHRELATION = "StadiumMatchRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>Match.id</code> attribute defined at extension <code>cuppy</code>. */
	public static final String ID = "id";
	
	/** <i>Generated constant</i> - Attribute key of <code>Match.guestGoals</code> attribute defined at extension <code>cuppy</code>. */
	public static final String GUESTGOALS = "guestGoals";
	
	/** <i>Generated constant</i> - Attribute key of <code>Match.homeGoals</code> attribute defined at extension <code>cuppy</code>. */
	public static final String HOMEGOALS = "homeGoals";
	
	/** <i>Generated constant</i> - Attribute key of <code>Match.location</code> attribute defined at extension <code>cuppy</code>. */
	public static final String LOCATION = "location";
	
	/** <i>Generated constant</i> - Attribute key of <code>Match.date</code> attribute defined at extension <code>cuppy</code>. */
	public static final String DATE = "date";
	
	/** <i>Generated constant</i> - Attribute key of <code>Match.matchday</code> attribute defined at extension <code>cuppy</code>. */
	public static final String MATCHDAY = "matchday";
	
	/** <i>Generated constant</i> - Attribute key of <code>Match.matchBets</code> attribute defined at extension <code>cuppy</code>. */
	public static final String MATCHBETS = "matchBets";
	
	/** <i>Generated constant</i> - Attribute key of <code>Match.group</code> attribute defined at extension <code>cuppy</code>. */
	public static final String GROUP = "group";
	
	/** <i>Generated constant</i> - Attribute key of <code>Match.guestTeam</code> attribute defined at extension <code>cuppy</code>. */
	public static final String GUESTTEAM = "guestTeam";
	
	/** <i>Generated constant</i> - Attribute key of <code>Match.homeTeam</code> attribute defined at extension <code>cuppy</code>. */
	public static final String HOMETEAM = "homeTeam";
	
	/** <i>Generated constant</i> - Attribute key of <code>Match.stadium</code> attribute defined at extension <code>cuppytrail</code>. */
	public static final String STADIUM = "stadium";
	
	
	/** <i>Generated variable</i> - Variable of <code>Match.id</code> attribute defined at extension <code>cuppy</code>. */
	private Integer _id;
	
	/** <i>Generated variable</i> - Variable of <code>Match.guestGoals</code> attribute defined at extension <code>cuppy</code>. */
	private Integer _guestGoals;
	
	/** <i>Generated variable</i> - Variable of <code>Match.homeGoals</code> attribute defined at extension <code>cuppy</code>. */
	private Integer _homeGoals;
	
	/** <i>Generated variable</i> - Variable of <code>Match.location</code> attribute defined at extension <code>cuppy</code>. */
	private String _location;
	
	/** <i>Generated variable</i> - Variable of <code>Match.date</code> attribute defined at extension <code>cuppy</code>. */
	private Date _date;
	
	/** <i>Generated variable</i> - Variable of <code>Match.matchday</code> attribute defined at extension <code>cuppy</code>. */
	private Integer _matchday;
	
	/** <i>Generated variable</i> - Variable of <code>Match.matchBets</code> attribute defined at extension <code>cuppy</code>. */
	private Collection<MatchBetModel> _matchBets;
	
	/** <i>Generated variable</i> - Variable of <code>Match.group</code> attribute defined at extension <code>cuppy</code>. */
	private GroupModel _group;
	
	/** <i>Generated variable</i> - Variable of <code>Match.guestTeam</code> attribute defined at extension <code>cuppy</code>. */
	private TeamModel _guestTeam;
	
	/** <i>Generated variable</i> - Variable of <code>Match.homeTeam</code> attribute defined at extension <code>cuppy</code>. */
	private TeamModel _homeTeam;
	
	/** <i>Generated variable</i> - Variable of <code>Match.stadium</code> attribute defined at extension <code>cuppytrail</code>. */
	private StadiumModel _stadium;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public MatchModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public MatchModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _date initial attribute declared by type <code>Match</code> at extension <code>cuppy</code>
	 * @param _group initial attribute declared by type <code>Match</code> at extension <code>cuppy</code>
	 * @param _guestTeam initial attribute declared by type <code>Match</code> at extension <code>cuppy</code>
	 * @param _homeTeam initial attribute declared by type <code>Match</code> at extension <code>cuppy</code>
	 * @param _id initial attribute declared by type <code>Match</code> at extension <code>cuppy</code>
	 * @param _matchday initial attribute declared by type <code>Match</code> at extension <code>cuppy</code>
	 */
	@Deprecated
	public MatchModel(final Date _date, final GroupModel _group, final TeamModel _guestTeam, final TeamModel _homeTeam, final int _id, final int _matchday)
	{
		super();
		setDate(_date);
		setGroup(_group);
		setGuestTeam(_guestTeam);
		setHomeTeam(_homeTeam);
		setId(_id);
		setMatchday(_matchday);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _date initial attribute declared by type <code>Match</code> at extension <code>cuppy</code>
	 * @param _group initial attribute declared by type <code>Match</code> at extension <code>cuppy</code>
	 * @param _guestTeam initial attribute declared by type <code>Match</code> at extension <code>cuppy</code>
	 * @param _homeTeam initial attribute declared by type <code>Match</code> at extension <code>cuppy</code>
	 * @param _id initial attribute declared by type <code>Match</code> at extension <code>cuppy</code>
	 * @param _matchday initial attribute declared by type <code>Match</code> at extension <code>cuppy</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public MatchModel(final Date _date, final GroupModel _group, final TeamModel _guestTeam, final TeamModel _homeTeam, final int _id, final int _matchday, final ItemModel _owner)
	{
		super();
		setDate(_date);
		setGroup(_group);
		setGuestTeam(_guestTeam);
		setHomeTeam(_homeTeam);
		setId(_id);
		setMatchday(_matchday);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Match.date</code> attribute defined at extension <code>cuppy</code>. 
	 * @return the date - The date of the match (start date/time).
	 */
	@Accessor(qualifier = "date", type = Accessor.Type.GETTER)
	public Date getDate()
	{
		if (this._date!=null)
		{
			return _date;
		}
		return _date = getPersistenceContext().getValue(DATE, _date);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Match.group</code> attribute defined at extension <code>cuppy</code>. 
	 * @return the group - The group the match belongs to.
	 */
	@Accessor(qualifier = "group", type = Accessor.Type.GETTER)
	public GroupModel getGroup()
	{
		if (this._group!=null)
		{
			return _group;
		}
		return _group = getPersistenceContext().getValue(GROUP, _group);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Match.guestGoals</code> attribute defined at extension <code>cuppy</code>. 
	 * @return the guestGoals - Amount of goals the guest team had at end of match.
	 */
	@Accessor(qualifier = "guestGoals", type = Accessor.Type.GETTER)
	public Integer getGuestGoals()
	{
		if (this._guestGoals!=null)
		{
			return _guestGoals;
		}
		return _guestGoals = getPersistenceContext().getValue(GUESTGOALS, _guestGoals);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Match.guestTeam</code> attribute defined at extension <code>cuppy</code>. 
	 * @return the guestTeam - The guest team of the match.
	 */
	@Accessor(qualifier = "guestTeam", type = Accessor.Type.GETTER)
	public TeamModel getGuestTeam()
	{
		if (this._guestTeam!=null)
		{
			return _guestTeam;
		}
		return _guestTeam = getPersistenceContext().getValue(GUESTTEAM, _guestTeam);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Match.homeGoals</code> attribute defined at extension <code>cuppy</code>. 
	 * @return the homeGoals - Amount of goals the home team had at end of match.
	 */
	@Accessor(qualifier = "homeGoals", type = Accessor.Type.GETTER)
	public Integer getHomeGoals()
	{
		if (this._homeGoals!=null)
		{
			return _homeGoals;
		}
		return _homeGoals = getPersistenceContext().getValue(HOMEGOALS, _homeGoals);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Match.homeTeam</code> attribute defined at extension <code>cuppy</code>. 
	 * @return the homeTeam - The home team of the match.
	 */
	@Accessor(qualifier = "homeTeam", type = Accessor.Type.GETTER)
	public TeamModel getHomeTeam()
	{
		if (this._homeTeam!=null)
		{
			return _homeTeam;
		}
		return _homeTeam = getPersistenceContext().getValue(HOMETEAM, _homeTeam);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Match.id</code> attribute defined at extension <code>cuppy</code>. 
	 * @return the id - Unique identifier of match within a competition.
	 */
	@Accessor(qualifier = "id", type = Accessor.Type.GETTER)
	public int getId()
	{
		return toPrimitive( _id = getPersistenceContext().getValue(ID, _id));
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Match.location</code> attribute defined at extension <code>cuppy</code>. 
	 * @return the location - Description of place where match take/took place.
	 */
	@Accessor(qualifier = "location", type = Accessor.Type.GETTER)
	public String getLocation()
	{
		if (this._location!=null)
		{
			return _location;
		}
		return _location = getPersistenceContext().getValue(LOCATION, _location);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Match.matchBets</code> attribute defined at extension <code>cuppy</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the matchBets - The bets placed on this match.
	 */
	@Accessor(qualifier = "matchBets", type = Accessor.Type.GETTER)
	public Collection<MatchBetModel> getMatchBets()
	{
		if (this._matchBets!=null)
		{
			return _matchBets;
		}
		return _matchBets = getPersistenceContext().getValue(MATCHBETS, _matchBets);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Match.matchday</code> attribute defined at extension <code>cuppy</code>. 
	 * @return the matchday - Absolute number of day of match within competition. Used to order matches by day.
	 * 					For league games it is similar to the matchday.
	 */
	@Accessor(qualifier = "matchday", type = Accessor.Type.GETTER)
	public int getMatchday()
	{
		return toPrimitive( _matchday = getPersistenceContext().getValue(MATCHDAY, _matchday));
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Match.stadium</code> attribute defined at extension <code>cuppytrail</code>. 
	 * @return the stadium
	 */
	@Accessor(qualifier = "stadium", type = Accessor.Type.GETTER)
	public StadiumModel getStadium()
	{
		if (this._stadium!=null)
		{
			return _stadium;
		}
		return _stadium = getPersistenceContext().getValue(STADIUM, _stadium);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Match.date</code> attribute defined at extension <code>cuppy</code>. 
	 *  
	 * @param value the date - The date of the match (start date/time).
	 */
	@Accessor(qualifier = "date", type = Accessor.Type.SETTER)
	public void setDate(final Date value)
	{
		_date = getPersistenceContext().setValue(DATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Match.group</code> attribute defined at extension <code>cuppy</code>. 
	 *  
	 * @param value the group - The group the match belongs to.
	 */
	@Accessor(qualifier = "group", type = Accessor.Type.SETTER)
	public void setGroup(final GroupModel value)
	{
		_group = getPersistenceContext().setValue(GROUP, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Match.guestGoals</code> attribute defined at extension <code>cuppy</code>. 
	 *  
	 * @param value the guestGoals - Amount of goals the guest team had at end of match.
	 */
	@Accessor(qualifier = "guestGoals", type = Accessor.Type.SETTER)
	public void setGuestGoals(final Integer value)
	{
		_guestGoals = getPersistenceContext().setValue(GUESTGOALS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Match.guestTeam</code> attribute defined at extension <code>cuppy</code>. 
	 *  
	 * @param value the guestTeam - The guest team of the match.
	 */
	@Accessor(qualifier = "guestTeam", type = Accessor.Type.SETTER)
	public void setGuestTeam(final TeamModel value)
	{
		_guestTeam = getPersistenceContext().setValue(GUESTTEAM, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Match.homeGoals</code> attribute defined at extension <code>cuppy</code>. 
	 *  
	 * @param value the homeGoals - Amount of goals the home team had at end of match.
	 */
	@Accessor(qualifier = "homeGoals", type = Accessor.Type.SETTER)
	public void setHomeGoals(final Integer value)
	{
		_homeGoals = getPersistenceContext().setValue(HOMEGOALS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Match.homeTeam</code> attribute defined at extension <code>cuppy</code>. 
	 *  
	 * @param value the homeTeam - The home team of the match.
	 */
	@Accessor(qualifier = "homeTeam", type = Accessor.Type.SETTER)
	public void setHomeTeam(final TeamModel value)
	{
		_homeTeam = getPersistenceContext().setValue(HOMETEAM, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Match.id</code> attribute defined at extension <code>cuppy</code>. 
	 *  
	 * @param value the id - Unique identifier of match within a competition.
	 */
	@Accessor(qualifier = "id", type = Accessor.Type.SETTER)
	public void setId(final int value)
	{
		_id = getPersistenceContext().setValue(ID, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Match.location</code> attribute defined at extension <code>cuppy</code>. 
	 *  
	 * @param value the location - Description of place where match take/took place.
	 */
	@Accessor(qualifier = "location", type = Accessor.Type.SETTER)
	public void setLocation(final String value)
	{
		_location = getPersistenceContext().setValue(LOCATION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Match.matchBets</code> attribute defined at extension <code>cuppy</code>. 
	 *  
	 * @param value the matchBets - The bets placed on this match.
	 */
	@Accessor(qualifier = "matchBets", type = Accessor.Type.SETTER)
	public void setMatchBets(final Collection<MatchBetModel> value)
	{
		_matchBets = getPersistenceContext().setValue(MATCHBETS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Match.matchday</code> attribute defined at extension <code>cuppy</code>. 
	 *  
	 * @param value the matchday - Absolute number of day of match within competition. Used to order matches by day.
	 * 					For league games it is similar to the matchday.
	 */
	@Accessor(qualifier = "matchday", type = Accessor.Type.SETTER)
	public void setMatchday(final int value)
	{
		_matchday = getPersistenceContext().setValue(MATCHDAY, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Match.stadium</code> attribute defined at extension <code>cuppytrail</code>. 
	 *  
	 * @param value the stadium
	 */
	@Accessor(qualifier = "stadium", type = Accessor.Type.SETTER)
	public void setStadium(final StadiumModel value)
	{
		_stadium = getPersistenceContext().setValue(STADIUM, value);
	}
	
}

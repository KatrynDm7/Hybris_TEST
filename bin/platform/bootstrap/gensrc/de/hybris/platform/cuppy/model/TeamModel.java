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
package de.hybris.platform.cuppy.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.cuppy.model.MatchModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

/**
 * Generated model class for type Team first defined at extension cuppy.
 * <p>
 * Represents a team playing in a match as home or guest.
 */
@SuppressWarnings("all")
public class TeamModel extends CountryModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "Team";
	
	/**<i>Generated relation code constant for relation <code>MatchGuestTeamRelation</code> defining source attribute <code>guestMatches</code> in extension <code>cuppy</code>.</i>*/
	public final static String _MATCHGUESTTEAMRELATION = "MatchGuestTeamRelation";
	
	/**<i>Generated relation code constant for relation <code>MatchHomeTeamRelation</code> defining source attribute <code>homeMatches</code> in extension <code>cuppy</code>.</i>*/
	public final static String _MATCHHOMETEAMRELATION = "MatchHomeTeamRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>Team.dummy</code> attribute defined at extension <code>cuppy</code>. */
	public static final String DUMMY = "dummy";
	
	/** <i>Generated constant</i> - Attribute key of <code>Team.guestMatches</code> attribute defined at extension <code>cuppy</code>. */
	public static final String GUESTMATCHES = "guestMatches";
	
	/** <i>Generated constant</i> - Attribute key of <code>Team.homeMatches</code> attribute defined at extension <code>cuppy</code>. */
	public static final String HOMEMATCHES = "homeMatches";
	
	
	/** <i>Generated variable</i> - Variable of <code>Team.dummy</code> attribute defined at extension <code>cuppy</code>. */
	private Boolean _dummy;
	
	/** <i>Generated variable</i> - Variable of <code>Team.guestMatches</code> attribute defined at extension <code>cuppy</code>. */
	private Collection<MatchModel> _guestMatches;
	
	/** <i>Generated variable</i> - Variable of <code>Team.homeMatches</code> attribute defined at extension <code>cuppy</code>. */
	private Collection<MatchModel> _homeMatches;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public TeamModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public TeamModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _dummy initial attribute declared by type <code>Team</code> at extension <code>cuppy</code>
	 * @param _isocode initial attribute declared by type <code>Country</code> at extension <code>core</code>
	 */
	@Deprecated
	public TeamModel(final boolean _dummy, final String _isocode)
	{
		super();
		setDummy(_dummy);
		setIsocode(_isocode);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _dummy initial attribute declared by type <code>Team</code> at extension <code>cuppy</code>
	 * @param _isocode initial attribute declared by type <code>Country</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public TeamModel(final boolean _dummy, final String _isocode, final ItemModel _owner)
	{
		super();
		setDummy(_dummy);
		setIsocode(_isocode);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Team.guestMatches</code> attribute defined at extension <code>cuppy</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the guestMatches - The matches the team is playing as guest team.
	 */
	@Accessor(qualifier = "guestMatches", type = Accessor.Type.GETTER)
	public Collection<MatchModel> getGuestMatches()
	{
		if (this._guestMatches!=null)
		{
			return _guestMatches;
		}
		return _guestMatches = getPersistenceContext().getValue(GUESTMATCHES, _guestMatches);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Team.homeMatches</code> attribute defined at extension <code>cuppy</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the homeMatches - The matches the team is playing as home team.
	 */
	@Accessor(qualifier = "homeMatches", type = Accessor.Type.GETTER)
	public Collection<MatchModel> getHomeMatches()
	{
		if (this._homeMatches!=null)
		{
			return _homeMatches;
		}
		return _homeMatches = getPersistenceContext().getValue(HOMEMATCHES, _homeMatches);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Team.dummy</code> attribute defined at extension <code>cuppy</code>. 
	 * @return the dummy - If true, the team will act as a placeholder
	 */
	@Accessor(qualifier = "dummy", type = Accessor.Type.GETTER)
	public boolean isDummy()
	{
		return toPrimitive( _dummy = getPersistenceContext().getValue(DUMMY, _dummy));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Team.dummy</code> attribute defined at extension <code>cuppy</code>. 
	 *  
	 * @param value the dummy - If true, the team will act as a placeholder
	 */
	@Accessor(qualifier = "dummy", type = Accessor.Type.SETTER)
	public void setDummy(final boolean value)
	{
		_dummy = getPersistenceContext().setValue(DUMMY, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Team.guestMatches</code> attribute defined at extension <code>cuppy</code>. 
	 *  
	 * @param value the guestMatches - The matches the team is playing as guest team.
	 */
	@Accessor(qualifier = "guestMatches", type = Accessor.Type.SETTER)
	public void setGuestMatches(final Collection<MatchModel> value)
	{
		_guestMatches = getPersistenceContext().setValue(GUESTMATCHES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Team.homeMatches</code> attribute defined at extension <code>cuppy</code>. 
	 *  
	 * @param value the homeMatches - The matches the team is playing as home team.
	 */
	@Accessor(qualifier = "homeMatches", type = Accessor.Type.SETTER)
	public void setHomeMatches(final Collection<MatchModel> value)
	{
		_homeMatches = getPersistenceContext().setValue(HOMEMATCHES, value);
	}
	
}

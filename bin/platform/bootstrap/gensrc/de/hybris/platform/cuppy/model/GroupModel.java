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
import de.hybris.platform.cuppy.model.CompetitionModel;
import de.hybris.platform.cuppy.model.MatchModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Locale;

/**
 * Generated model class for type Group first defined at extension cuppy.
 * <p>
 * Organizing matches by a group. For tournaments it is similar to grouping of preliminaries and K.O. rounds.
 * 			In a league there is not such a grouping needed, so it represents mainly first and second half of season.
 */
@SuppressWarnings("all")
public class GroupModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "Group";
	
	/**<i>Generated relation code constant for relation <code>MatchGroupRelation</code> defining source attribute <code>matches</code> in extension <code>cuppy</code>.</i>*/
	public final static String _MATCHGROUPRELATION = "MatchGroupRelation";
	
	/**<i>Generated relation code constant for relation <code>CompetitionGroupRelation</code> defining source attribute <code>competition</code> in extension <code>cuppy</code>.</i>*/
	public final static String _COMPETITIONGROUPRELATION = "CompetitionGroupRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>Group.code</code> attribute defined at extension <code>cuppy</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>Group.name</code> attribute defined at extension <code>cuppy</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>Group.multiplier</code> attribute defined at extension <code>cuppy</code>. */
	public static final String MULTIPLIER = "multiplier";
	
	/** <i>Generated constant</i> - Attribute key of <code>Group.sequenceNumber</code> attribute defined at extension <code>cuppy</code>. */
	public static final String SEQUENCENUMBER = "sequenceNumber";
	
	/** <i>Generated constant</i> - Attribute key of <code>Group.matches</code> attribute defined at extension <code>cuppy</code>. */
	public static final String MATCHES = "matches";
	
	/** <i>Generated constant</i> - Attribute key of <code>Group.competition</code> attribute defined at extension <code>cuppy</code>. */
	public static final String COMPETITION = "competition";
	
	
	/** <i>Generated variable</i> - Variable of <code>Group.code</code> attribute defined at extension <code>cuppy</code>. */
	private String _code;
	
	/** <i>Generated variable</i> - Variable of <code>Group.multiplier</code> attribute defined at extension <code>cuppy</code>. */
	private Float _multiplier;
	
	/** <i>Generated variable</i> - Variable of <code>Group.sequenceNumber</code> attribute defined at extension <code>cuppy</code>. */
	private Integer _sequenceNumber;
	
	/** <i>Generated variable</i> - Variable of <code>Group.matches</code> attribute defined at extension <code>cuppy</code>. */
	private Collection<MatchModel> _matches;
	
	/** <i>Generated variable</i> - Variable of <code>Group.competition</code> attribute defined at extension <code>cuppy</code>. */
	private CompetitionModel _competition;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public GroupModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public GroupModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>Group</code> at extension <code>cuppy</code>
	 * @param _competition initial attribute declared by type <code>Group</code> at extension <code>cuppy</code>
	 * @param _sequenceNumber initial attribute declared by type <code>Group</code> at extension <code>cuppy</code>
	 */
	@Deprecated
	public GroupModel(final String _code, final CompetitionModel _competition, final int _sequenceNumber)
	{
		super();
		setCode(_code);
		setCompetition(_competition);
		setSequenceNumber(_sequenceNumber);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>Group</code> at extension <code>cuppy</code>
	 * @param _competition initial attribute declared by type <code>Group</code> at extension <code>cuppy</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _sequenceNumber initial attribute declared by type <code>Group</code> at extension <code>cuppy</code>
	 */
	@Deprecated
	public GroupModel(final String _code, final CompetitionModel _competition, final ItemModel _owner, final int _sequenceNumber)
	{
		super();
		setCode(_code);
		setCompetition(_competition);
		setOwner(_owner);
		setSequenceNumber(_sequenceNumber);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Group.code</code> attribute defined at extension <code>cuppy</code>. 
	 * @return the code - Unique identifier of group within a competition.
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		if (this._code!=null)
		{
			return _code;
		}
		return _code = getPersistenceContext().getValue(CODE, _code);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Group.competition</code> attribute defined at extension <code>cuppy</code>. 
	 * @return the competition - The competition this group belongs to.
	 */
	@Accessor(qualifier = "competition", type = Accessor.Type.GETTER)
	public CompetitionModel getCompetition()
	{
		if (this._competition!=null)
		{
			return _competition;
		}
		return _competition = getPersistenceContext().getValue(COMPETITION, _competition);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Group.matches</code> attribute defined at extension <code>cuppy</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the matches - The matches of a group
	 */
	@Accessor(qualifier = "matches", type = Accessor.Type.GETTER)
	public Collection<MatchModel> getMatches()
	{
		if (this._matches!=null)
		{
			return _matches;
		}
		return _matches = getPersistenceContext().getValue(MATCHES, _matches);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Group.multiplier</code> attribute defined at extension <code>cuppy</code>. 
	 * @return the multiplier - Defines a weight for the scoring. The score a player gets on a bet for this match get multiplied by it.
	 */
	@Accessor(qualifier = "multiplier", type = Accessor.Type.GETTER)
	public float getMultiplier()
	{
		return toPrimitive( _multiplier = getPersistenceContext().getValue(MULTIPLIER, _multiplier));
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Group.name</code> attribute defined at extension <code>cuppy</code>. 
	 * @return the name - Name of group.
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getName(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>Group.name</code> attribute defined at extension <code>cuppy</code>. 
	 * @param loc the value localization key 
	 * @return the name - Name of group.
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(NAME, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Group.sequenceNumber</code> attribute defined at extension <code>cuppy</code>. 
	 * @return the sequenceNumber - Absolute number ordering the groups within a competition. So it is assured that preliminaries are
	 * 					displayed always as first group of a competition for example.
	 */
	@Accessor(qualifier = "sequenceNumber", type = Accessor.Type.GETTER)
	public int getSequenceNumber()
	{
		return toPrimitive( _sequenceNumber = getPersistenceContext().getValue(SEQUENCENUMBER, _sequenceNumber));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Group.code</code> attribute defined at extension <code>cuppy</code>. 
	 *  
	 * @param value the code - Unique identifier of group within a competition.
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		_code = getPersistenceContext().setValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Group.competition</code> attribute defined at extension <code>cuppy</code>. 
	 *  
	 * @param value the competition - The competition this group belongs to.
	 */
	@Accessor(qualifier = "competition", type = Accessor.Type.SETTER)
	public void setCompetition(final CompetitionModel value)
	{
		_competition = getPersistenceContext().setValue(COMPETITION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Group.matches</code> attribute defined at extension <code>cuppy</code>. 
	 *  
	 * @param value the matches - The matches of a group
	 */
	@Accessor(qualifier = "matches", type = Accessor.Type.SETTER)
	public void setMatches(final Collection<MatchModel> value)
	{
		_matches = getPersistenceContext().setValue(MATCHES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Group.multiplier</code> attribute defined at extension <code>cuppy</code>. 
	 *  
	 * @param value the multiplier - Defines a weight for the scoring. The score a player gets on a bet for this match get multiplied by it.
	 */
	@Accessor(qualifier = "multiplier", type = Accessor.Type.SETTER)
	public void setMultiplier(final float value)
	{
		_multiplier = getPersistenceContext().setValue(MULTIPLIER, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Group.name</code> attribute defined at extension <code>cuppy</code>. 
	 *  
	 * @param value the name - Name of group.
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		setName(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>Group.name</code> attribute defined at extension <code>cuppy</code>. 
	 *  
	 * @param value the name - Name of group.
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(NAME, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Group.sequenceNumber</code> attribute defined at extension <code>cuppy</code>. 
	 *  
	 * @param value the sequenceNumber - Absolute number ordering the groups within a competition. So it is assured that preliminaries are
	 * 					displayed always as first group of a competition for example.
	 */
	@Accessor(qualifier = "sequenceNumber", type = Accessor.Type.SETTER)
	public void setSequenceNumber(final int value)
	{
		_sequenceNumber = getPersistenceContext().setValue(SEQUENCENUMBER, toObject(value));
	}
	
}

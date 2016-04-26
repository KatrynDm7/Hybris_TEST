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
import de.hybris.platform.cuppy.enums.CompetitionType;
import de.hybris.platform.cuppy.model.GroupModel;
import de.hybris.platform.cuppy.model.NewsModel;
import de.hybris.platform.cuppy.model.PlayerModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Locale;
import java.util.Set;

/**
 * Generated model class for type Competition first defined at extension cuppy.
 * <p>
 * A competition managing matches structured by groups.
 */
@SuppressWarnings("all")
public class CompetitionModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "Competition";
	
	/** <i>Generated constant</i> - Attribute key of <code>Competition.code</code> attribute defined at extension <code>cuppy</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>Competition.name</code> attribute defined at extension <code>cuppy</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>Competition.type</code> attribute defined at extension <code>cuppy</code>. */
	public static final String TYPE = "type";
	
	/** <i>Generated constant</i> - Attribute key of <code>Competition.Finished</code> attribute defined at extension <code>cuppy</code>. */
	public static final String FINISHED = "Finished";
	
	/** <i>Generated constant</i> - Attribute key of <code>Competition.groups</code> attribute defined at extension <code>cuppy</code>. */
	public static final String GROUPS = "groups";
	
	/** <i>Generated constant</i> - Attribute key of <code>Competition.news</code> attribute defined at extension <code>cuppy</code>. */
	public static final String NEWS = "news";
	
	/** <i>Generated constant</i> - Attribute key of <code>Competition.players</code> attribute defined at extension <code>cuppy</code>. */
	public static final String PLAYERS = "players";
	
	
	/** <i>Generated variable</i> - Variable of <code>Competition.code</code> attribute defined at extension <code>cuppy</code>. */
	private String _code;
	
	/** <i>Generated variable</i> - Variable of <code>Competition.type</code> attribute defined at extension <code>cuppy</code>. */
	private CompetitionType _type;
	
	/** <i>Generated variable</i> - Variable of <code>Competition.Finished</code> attribute defined at extension <code>cuppy</code>. */
	private Boolean _Finished;
	
	/** <i>Generated variable</i> - Variable of <code>Competition.groups</code> attribute defined at extension <code>cuppy</code>. */
	private Collection<GroupModel> _groups;
	
	/** <i>Generated variable</i> - Variable of <code>Competition.news</code> attribute defined at extension <code>cuppy</code>. */
	private Collection<NewsModel> _news;
	
	/** <i>Generated variable</i> - Variable of <code>Competition.players</code> attribute defined at extension <code>cuppy</code>. */
	private Set<PlayerModel> _players;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CompetitionModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CompetitionModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>Competition</code> at extension <code>cuppy</code>
	 */
	@Deprecated
	public CompetitionModel(final String _code)
	{
		super();
		setCode(_code);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>Competition</code> at extension <code>cuppy</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public CompetitionModel(final String _code, final ItemModel _owner)
	{
		super();
		setCode(_code);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Competition.code</code> attribute defined at extension <code>cuppy</code>. 
	 * @return the code - Unique identifier of a competition.
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
	 * <i>Generated method</i> - Getter of the <code>Competition.groups</code> attribute defined at extension <code>cuppy</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the groups - The groups the competition consists of.
	 */
	@Accessor(qualifier = "groups", type = Accessor.Type.GETTER)
	public Collection<GroupModel> getGroups()
	{
		if (this._groups!=null)
		{
			return _groups;
		}
		return _groups = getPersistenceContext().getValue(GROUPS, _groups);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Competition.name</code> attribute defined at extension <code>cuppy</code>. 
	 * @return the name - Name of the competition.
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getName(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>Competition.name</code> attribute defined at extension <code>cuppy</code>. 
	 * @param loc the value localization key 
	 * @return the name - Name of the competition.
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(NAME, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Competition.news</code> attribute defined at extension <code>cuppy</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the news - All news of the competition (excluding news without explicitly assigned comeptition).
	 */
	@Accessor(qualifier = "news", type = Accessor.Type.GETTER)
	public Collection<NewsModel> getNews()
	{
		if (this._news!=null)
		{
			return _news;
		}
		return _news = getPersistenceContext().getValue(NEWS, _news);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Competition.players</code> attribute defined at extension <code>cuppy</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the players - Players having the competition activated. Competition will be selectable for this player at frontend.
	 */
	@Accessor(qualifier = "players", type = Accessor.Type.GETTER)
	public Set<PlayerModel> getPlayers()
	{
		if (this._players!=null)
		{
			return _players;
		}
		return _players = getPersistenceContext().getValue(PLAYERS, _players);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Competition.type</code> attribute defined at extension <code>cuppy</code>. 
	 * @return the type - Type of competition indicating how to present it at frontend.
	 */
	@Accessor(qualifier = "type", type = Accessor.Type.GETTER)
	public CompetitionType getType()
	{
		if (this._type!=null)
		{
			return _type;
		}
		return _type = getPersistenceContext().getValue(TYPE, _type);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Competition.Finished</code> attribute defined at extension <code>cuppy</code>. 
	 * @return the Finished - Is this competition finished and with that no bets can be placed
	 * 					 anymore and all results are calculated.
	 */
	@Accessor(qualifier = "Finished", type = Accessor.Type.GETTER)
	public boolean isFinished()
	{
		return toPrimitive( _Finished = getPersistenceContext().getValue(FINISHED, _Finished));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Competition.code</code> attribute defined at extension <code>cuppy</code>. 
	 *  
	 * @param value the code - Unique identifier of a competition.
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		_code = getPersistenceContext().setValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Competition.Finished</code> attribute defined at extension <code>cuppy</code>. 
	 *  
	 * @param value the Finished - Is this competition finished and with that no bets can be placed
	 * 					 anymore and all results are calculated.
	 */
	@Accessor(qualifier = "Finished", type = Accessor.Type.SETTER)
	public void setFinished(final boolean value)
	{
		_Finished = getPersistenceContext().setValue(FINISHED, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Competition.groups</code> attribute defined at extension <code>cuppy</code>. 
	 *  
	 * @param value the groups - The groups the competition consists of.
	 */
	@Accessor(qualifier = "groups", type = Accessor.Type.SETTER)
	public void setGroups(final Collection<GroupModel> value)
	{
		_groups = getPersistenceContext().setValue(GROUPS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Competition.name</code> attribute defined at extension <code>cuppy</code>. 
	 *  
	 * @param value the name - Name of the competition.
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		setName(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>Competition.name</code> attribute defined at extension <code>cuppy</code>. 
	 *  
	 * @param value the name - Name of the competition.
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(NAME, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Competition.news</code> attribute defined at extension <code>cuppy</code>. 
	 *  
	 * @param value the news - All news of the competition (excluding news without explicitly assigned comeptition).
	 */
	@Accessor(qualifier = "news", type = Accessor.Type.SETTER)
	public void setNews(final Collection<NewsModel> value)
	{
		_news = getPersistenceContext().setValue(NEWS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Competition.players</code> attribute defined at extension <code>cuppy</code>. 
	 *  
	 * @param value the players - Players having the competition activated. Competition will be selectable for this player at frontend.
	 */
	@Accessor(qualifier = "players", type = Accessor.Type.SETTER)
	public void setPlayers(final Set<PlayerModel> value)
	{
		_players = getPersistenceContext().setValue(PLAYERS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Competition.type</code> attribute defined at extension <code>cuppy</code>. 
	 *  
	 * @param value the type - Type of competition indicating how to present it at frontend.
	 */
	@Accessor(qualifier = "type", type = Accessor.Type.SETTER)
	public void setType(final CompetitionType value)
	{
		_type = getPersistenceContext().setValue(TYPE, value);
	}
	
}

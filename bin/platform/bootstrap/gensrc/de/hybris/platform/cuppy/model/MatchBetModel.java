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
import de.hybris.platform.cuppy.model.MatchModel;
import de.hybris.platform.cuppy.model.PlayerModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type MatchBet first defined at extension cuppy.
 * <p>
 * A bet of a player on a match.
 */
@SuppressWarnings("all")
public class MatchBetModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "MatchBet";
	
	/** <i>Generated constant</i> - Attribute key of <code>MatchBet.guestGoals</code> attribute defined at extension <code>cuppy</code>. */
	public static final String GUESTGOALS = "guestGoals";
	
	/** <i>Generated constant</i> - Attribute key of <code>MatchBet.homeGoals</code> attribute defined at extension <code>cuppy</code>. */
	public static final String HOMEGOALS = "homeGoals";
	
	/** <i>Generated constant</i> - Attribute key of <code>MatchBet.player</code> attribute defined at extension <code>cuppy</code>. */
	public static final String PLAYER = "player";
	
	/** <i>Generated constant</i> - Attribute key of <code>MatchBet.match</code> attribute defined at extension <code>cuppy</code>. */
	public static final String MATCH = "match";
	
	
	/** <i>Generated variable</i> - Variable of <code>MatchBet.guestGoals</code> attribute defined at extension <code>cuppy</code>. */
	private Integer _guestGoals;
	
	/** <i>Generated variable</i> - Variable of <code>MatchBet.homeGoals</code> attribute defined at extension <code>cuppy</code>. */
	private Integer _homeGoals;
	
	/** <i>Generated variable</i> - Variable of <code>MatchBet.player</code> attribute defined at extension <code>cuppy</code>. */
	private PlayerModel _player;
	
	/** <i>Generated variable</i> - Variable of <code>MatchBet.match</code> attribute defined at extension <code>cuppy</code>. */
	private MatchModel _match;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public MatchBetModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public MatchBetModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _guestGoals initial attribute declared by type <code>MatchBet</code> at extension <code>cuppy</code>
	 * @param _homeGoals initial attribute declared by type <code>MatchBet</code> at extension <code>cuppy</code>
	 * @param _match initial attribute declared by type <code>MatchBet</code> at extension <code>cuppy</code>
	 * @param _player initial attribute declared by type <code>MatchBet</code> at extension <code>cuppy</code>
	 */
	@Deprecated
	public MatchBetModel(final int _guestGoals, final int _homeGoals, final MatchModel _match, final PlayerModel _player)
	{
		super();
		setGuestGoals(_guestGoals);
		setHomeGoals(_homeGoals);
		setMatch(_match);
		setPlayer(_player);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _guestGoals initial attribute declared by type <code>MatchBet</code> at extension <code>cuppy</code>
	 * @param _homeGoals initial attribute declared by type <code>MatchBet</code> at extension <code>cuppy</code>
	 * @param _match initial attribute declared by type <code>MatchBet</code> at extension <code>cuppy</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _player initial attribute declared by type <code>MatchBet</code> at extension <code>cuppy</code>
	 */
	@Deprecated
	public MatchBetModel(final int _guestGoals, final int _homeGoals, final MatchModel _match, final ItemModel _owner, final PlayerModel _player)
	{
		super();
		setGuestGoals(_guestGoals);
		setHomeGoals(_homeGoals);
		setMatch(_match);
		setOwner(_owner);
		setPlayer(_player);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MatchBet.guestGoals</code> attribute defined at extension <code>cuppy</code>. 
	 * @return the guestGoals - Amount of goals for guest team at end of match.
	 */
	@Accessor(qualifier = "guestGoals", type = Accessor.Type.GETTER)
	public int getGuestGoals()
	{
		return toPrimitive( _guestGoals = getPersistenceContext().getValue(GUESTGOALS, _guestGoals));
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MatchBet.homeGoals</code> attribute defined at extension <code>cuppy</code>. 
	 * @return the homeGoals - Amount of goals for home team at end of match.
	 */
	@Accessor(qualifier = "homeGoals", type = Accessor.Type.GETTER)
	public int getHomeGoals()
	{
		return toPrimitive( _homeGoals = getPersistenceContext().getValue(HOMEGOALS, _homeGoals));
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MatchBet.match</code> attribute defined at extension <code>cuppy</code>. 
	 * @return the match - The match this bet belongs to.
	 */
	@Accessor(qualifier = "match", type = Accessor.Type.GETTER)
	public MatchModel getMatch()
	{
		if (this._match!=null)
		{
			return _match;
		}
		return _match = getPersistenceContext().getValue(MATCH, _match);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MatchBet.player</code> attribute defined at extension <code>cuppy</code>. 
	 * @return the player - The player this bet belongs to.
	 */
	@Accessor(qualifier = "player", type = Accessor.Type.GETTER)
	public PlayerModel getPlayer()
	{
		if (this._player!=null)
		{
			return _player;
		}
		return _player = getPersistenceContext().getValue(PLAYER, _player);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MatchBet.guestGoals</code> attribute defined at extension <code>cuppy</code>. 
	 *  
	 * @param value the guestGoals - Amount of goals for guest team at end of match.
	 */
	@Accessor(qualifier = "guestGoals", type = Accessor.Type.SETTER)
	public void setGuestGoals(final int value)
	{
		_guestGoals = getPersistenceContext().setValue(GUESTGOALS, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MatchBet.homeGoals</code> attribute defined at extension <code>cuppy</code>. 
	 *  
	 * @param value the homeGoals - Amount of goals for home team at end of match.
	 */
	@Accessor(qualifier = "homeGoals", type = Accessor.Type.SETTER)
	public void setHomeGoals(final int value)
	{
		_homeGoals = getPersistenceContext().setValue(HOMEGOALS, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MatchBet.match</code> attribute defined at extension <code>cuppy</code>. 
	 *  
	 * @param value the match - The match this bet belongs to.
	 */
	@Accessor(qualifier = "match", type = Accessor.Type.SETTER)
	public void setMatch(final MatchModel value)
	{
		_match = getPersistenceContext().setValue(MATCH, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MatchBet.player</code> attribute defined at extension <code>cuppy</code>. 
	 *  
	 * @param value the player - The player this bet belongs to.
	 */
	@Accessor(qualifier = "player", type = Accessor.Type.SETTER)
	public void setPlayer(final PlayerModel value)
	{
		_player = getPersistenceContext().setValue(PLAYER, value);
	}
	
}

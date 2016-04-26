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
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.cuppy.model.CompetitionModel;
import de.hybris.platform.cuppy.model.MatchBetModel;
import de.hybris.platform.cuppy.model.PlayerPreferencesModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Set;

/**
 * Generated model class for type Player first defined at extension cuppy.
 */
@SuppressWarnings("all")
public class PlayerModel extends UserModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "Player";
	
	/**<i>Generated relation code constant for relation <code>PlayerMatchBetRelation</code> defining source attribute <code>matchBets</code> in extension <code>cuppy</code>.</i>*/
	public final static String _PLAYERMATCHBETRELATION = "PlayerMatchBetRelation";
	
	/**<i>Generated relation code constant for relation <code>CompetitionPlayerRelation</code> defining source attribute <code>competitions</code> in extension <code>cuppy</code>.</i>*/
	public final static String _COMPETITIONPLAYERRELATION = "CompetitionPlayerRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>Player.eMail</code> attribute defined at extension <code>cuppy</code>. */
	public static final String EMAIL = "eMail";
	
	/** <i>Generated constant</i> - Attribute key of <code>Player.confirmed</code> attribute defined at extension <code>cuppy</code>. */
	public static final String CONFIRMED = "confirmed";
	
	/** <i>Generated constant</i> - Attribute key of <code>Player.preferences</code> attribute defined at extension <code>cuppy</code>. */
	public static final String PREFERENCES = "preferences";
	
	/** <i>Generated constant</i> - Attribute key of <code>Player.sendNewsletter</code> attribute defined at extension <code>cuppy</code>. */
	public static final String SENDNEWSLETTER = "sendNewsletter";
	
	/** <i>Generated constant</i> - Attribute key of <code>Player.country</code> attribute defined at extension <code>cuppy</code>. */
	public static final String COUNTRY = "country";
	
	/** <i>Generated constant</i> - Attribute key of <code>Player.matchBets</code> attribute defined at extension <code>cuppy</code>. */
	public static final String MATCHBETS = "matchBets";
	
	/** <i>Generated constant</i> - Attribute key of <code>Player.competitions</code> attribute defined at extension <code>cuppy</code>. */
	public static final String COMPETITIONS = "competitions";
	
	
	/** <i>Generated variable</i> - Variable of <code>Player.eMail</code> attribute defined at extension <code>cuppy</code>. */
	private String _eMail;
	
	/** <i>Generated variable</i> - Variable of <code>Player.confirmed</code> attribute defined at extension <code>cuppy</code>. */
	private Boolean _confirmed;
	
	/** <i>Generated variable</i> - Variable of <code>Player.preferences</code> attribute defined at extension <code>cuppy</code>. */
	private PlayerPreferencesModel _preferences;
	
	/** <i>Generated variable</i> - Variable of <code>Player.sendNewsletter</code> attribute defined at extension <code>cuppy</code>. */
	private Boolean _sendNewsletter;
	
	/** <i>Generated variable</i> - Variable of <code>Player.country</code> attribute defined at extension <code>cuppy</code>. */
	private CountryModel _country;
	
	/** <i>Generated variable</i> - Variable of <code>Player.matchBets</code> attribute defined at extension <code>cuppy</code>. */
	private Collection<MatchBetModel> _matchBets;
	
	/** <i>Generated variable</i> - Variable of <code>Player.competitions</code> attribute defined at extension <code>cuppy</code>. */
	private Set<CompetitionModel> _competitions;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public PlayerModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public PlayerModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _country initial attribute declared by type <code>Player</code> at extension <code>cuppy</code>
	 * @param _eMail initial attribute declared by type <code>Player</code> at extension <code>cuppy</code>
	 * @param _loginDisabled initial attribute declared by type <code>User</code> at extension <code>core</code>
	 * @param _uid initial attribute declared by type <code>Principal</code> at extension <code>core</code>
	 */
	@Deprecated
	public PlayerModel(final CountryModel _country, final String _eMail, final boolean _loginDisabled, final String _uid)
	{
		super();
		setCountry(_country);
		setEMail(_eMail);
		setLoginDisabled(_loginDisabled);
		setUid(_uid);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _country initial attribute declared by type <code>Player</code> at extension <code>cuppy</code>
	 * @param _eMail initial attribute declared by type <code>Player</code> at extension <code>cuppy</code>
	 * @param _loginDisabled initial attribute declared by type <code>User</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _uid initial attribute declared by type <code>Principal</code> at extension <code>core</code>
	 */
	@Deprecated
	public PlayerModel(final CountryModel _country, final String _eMail, final boolean _loginDisabled, final ItemModel _owner, final String _uid)
	{
		super();
		setCountry(_country);
		setEMail(_eMail);
		setLoginDisabled(_loginDisabled);
		setOwner(_owner);
		setUid(_uid);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Player.competitions</code> attribute defined at extension <code>cuppy</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the competitions - Competitions a player has activated. Competitions will be selectable for player at frontend
	 */
	@Accessor(qualifier = "competitions", type = Accessor.Type.GETTER)
	public Set<CompetitionModel> getCompetitions()
	{
		if (this._competitions!=null)
		{
			return _competitions;
		}
		return _competitions = getPersistenceContext().getValue(COMPETITIONS, _competitions);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Player.country</code> attribute defined at extension <code>cuppy</code>. 
	 * @return the country - The country (nation) a player belongs to.
	 */
	@Accessor(qualifier = "country", type = Accessor.Type.GETTER)
	public CountryModel getCountry()
	{
		if (this._country!=null)
		{
			return _country;
		}
		return _country = getPersistenceContext().getValue(COUNTRY, _country);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Player.eMail</code> attribute defined at extension <code>cuppy</code>. 
	 * @return the eMail - EMail address of the player where cuppy related mails will be send to.
	 */
	@Accessor(qualifier = "eMail", type = Accessor.Type.GETTER)
	public String getEMail()
	{
		if (this._eMail!=null)
		{
			return _eMail;
		}
		return _eMail = getPersistenceContext().getValue(EMAIL, _eMail);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Player.matchBets</code> attribute defined at extension <code>cuppy</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the matchBets - All bets of the player.
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
	 * <i>Generated method</i> - Getter of the <code>Player.preferences</code> attribute defined at extension <code>cuppy</code>. 
	 * @return the preferences - References the container for managing frontend related preferences of the player.
	 */
	@Accessor(qualifier = "preferences", type = Accessor.Type.GETTER)
	public PlayerPreferencesModel getPreferences()
	{
		if (this._preferences!=null)
		{
			return _preferences;
		}
		return _preferences = getPersistenceContext().getValue(PREFERENCES, _preferences);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Player.confirmed</code> attribute defined at extension <code>cuppy</code>. 
	 * @return the confirmed - If true the player will be able to login to frontend.
	 */
	@Accessor(qualifier = "confirmed", type = Accessor.Type.GETTER)
	public boolean isConfirmed()
	{
		return toPrimitive( _confirmed = getPersistenceContext().getValue(CONFIRMED, _confirmed));
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Player.sendNewsletter</code> attribute defined at extension <code>cuppy</code>. 
	 * @return the sendNewsletter - If true the player will get mails for news published by mail related to competitions the player has activated
	 */
	@Accessor(qualifier = "sendNewsletter", type = Accessor.Type.GETTER)
	public boolean isSendNewsletter()
	{
		return toPrimitive( _sendNewsletter = getPersistenceContext().getValue(SENDNEWSLETTER, _sendNewsletter));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Player.competitions</code> attribute defined at extension <code>cuppy</code>. 
	 *  
	 * @param value the competitions - Competitions a player has activated. Competitions will be selectable for player at frontend
	 */
	@Accessor(qualifier = "competitions", type = Accessor.Type.SETTER)
	public void setCompetitions(final Set<CompetitionModel> value)
	{
		_competitions = getPersistenceContext().setValue(COMPETITIONS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Player.confirmed</code> attribute defined at extension <code>cuppy</code>. 
	 *  
	 * @param value the confirmed - If true the player will be able to login to frontend.
	 */
	@Accessor(qualifier = "confirmed", type = Accessor.Type.SETTER)
	public void setConfirmed(final boolean value)
	{
		_confirmed = getPersistenceContext().setValue(CONFIRMED, toObject(value));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Player.country</code> attribute defined at extension <code>cuppy</code>. 
	 *  
	 * @param value the country - The country (nation) a player belongs to.
	 */
	@Accessor(qualifier = "country", type = Accessor.Type.SETTER)
	public void setCountry(final CountryModel value)
	{
		_country = getPersistenceContext().setValue(COUNTRY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Player.eMail</code> attribute defined at extension <code>cuppy</code>. 
	 *  
	 * @param value the eMail - EMail address of the player where cuppy related mails will be send to.
	 */
	@Accessor(qualifier = "eMail", type = Accessor.Type.SETTER)
	public void setEMail(final String value)
	{
		_eMail = getPersistenceContext().setValue(EMAIL, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Player.matchBets</code> attribute defined at extension <code>cuppy</code>. 
	 *  
	 * @param value the matchBets - All bets of the player.
	 */
	@Accessor(qualifier = "matchBets", type = Accessor.Type.SETTER)
	public void setMatchBets(final Collection<MatchBetModel> value)
	{
		_matchBets = getPersistenceContext().setValue(MATCHBETS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Player.preferences</code> attribute defined at extension <code>cuppy</code>. 
	 *  
	 * @param value the preferences - References the container for managing frontend related preferences of the player.
	 */
	@Accessor(qualifier = "preferences", type = Accessor.Type.SETTER)
	public void setPreferences(final PlayerPreferencesModel value)
	{
		_preferences = getPersistenceContext().setValue(PREFERENCES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Player.sendNewsletter</code> attribute defined at extension <code>cuppy</code>. 
	 *  
	 * @param value the sendNewsletter - If true the player will get mails for news published by mail related to competitions the player has activated
	 */
	@Accessor(qualifier = "sendNewsletter", type = Accessor.Type.SETTER)
	public void setSendNewsletter(final boolean value)
	{
		_sendNewsletter = getPersistenceContext().setValue(SENDNEWSLETTER, toObject(value));
	}
	
}

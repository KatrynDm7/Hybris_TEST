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
import de.hybris.platform.cuppy.jalo.Competition;
import de.hybris.platform.cuppy.jalo.MatchBet;
import de.hybris.platform.cuppy.jalo.PlayerPreferences;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Country;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.util.OneToManyHandler;
import de.hybris.platform.util.PartOfHandler;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Generated class for type {@link de.hybris.platform.cuppy.jalo.Player Player}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedPlayer extends User
{
	/** Qualifier of the <code>Player.eMail</code> attribute **/
	public static final String EMAIL = "eMail";
	/** Qualifier of the <code>Player.confirmed</code> attribute **/
	public static final String CONFIRMED = "confirmed";
	/** Qualifier of the <code>Player.preferences</code> attribute **/
	public static final String PREFERENCES = "preferences";
	/** Qualifier of the <code>Player.sendNewsletter</code> attribute **/
	public static final String SENDNEWSLETTER = "sendNewsletter";
	/** Qualifier of the <code>Player.country</code> attribute **/
	public static final String COUNTRY = "country";
	/** Qualifier of the <code>Player.matchBets</code> attribute **/
	public static final String MATCHBETS = "matchBets";
	/** Qualifier of the <code>Player.competitions</code> attribute **/
	public static final String COMPETITIONS = "competitions";
	/** Relation ordering override parameter constants for CompetitionPlayerRelation from ((cuppy))*/
	protected static String COMPETITIONPLAYERRELATION_SRC_ORDERED = "relation.CompetitionPlayerRelation.source.ordered";
	protected static String COMPETITIONPLAYERRELATION_TGT_ORDERED = "relation.CompetitionPlayerRelation.target.ordered";
	/** Relation disable markmodifed parameter constants for CompetitionPlayerRelation from ((cuppy))*/
	protected static String COMPETITIONPLAYERRELATION_MARKMODIFIED = "relation.CompetitionPlayerRelation.markmodified";
	/**
	* {@link BidirectionalOneToManyHandler} for handling 1:n COUNTRY's relation attributes from 'one' side.
	**/
	protected static final BidirectionalOneToManyHandler<GeneratedPlayer> COUNTRYHANDLER = new BidirectionalOneToManyHandler<GeneratedPlayer>(
	CuppyConstants.TC.PLAYER,
	false,
	"country",
	null,
	false,
	true,
	CollectionType.COLLECTION
	);
	/**
	* {@link OneToManyHandler} for handling 1:n MATCHBETS's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<MatchBet> MATCHBETSHANDLER = new OneToManyHandler<MatchBet>(
	CuppyConstants.TC.MATCHBET,
	true,
	"player",
	null,
	false,
	true,
	CollectionType.COLLECTION
	);
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(User.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(EMAIL, AttributeMode.INITIAL);
		tmp.put(CONFIRMED, AttributeMode.INITIAL);
		tmp.put(PREFERENCES, AttributeMode.INITIAL);
		tmp.put(SENDNEWSLETTER, AttributeMode.INITIAL);
		tmp.put(COUNTRY, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Player.competitions</code> attribute.
	 * @return the competitions - Competitions a player has activated. Competitions will be selectable for player at frontend
	 */
	public Set<Competition> getCompetitions(final SessionContext ctx)
	{
		final List<Competition> items = getLinkedItems( 
			ctx,
			false,
			CuppyConstants.Relations.COMPETITIONPLAYERRELATION,
			null,
			false,
			false
		);
		return new LinkedHashSet<Competition>(items);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Player.competitions</code> attribute.
	 * @return the competitions - Competitions a player has activated. Competitions will be selectable for player at frontend
	 */
	public Set<Competition> getCompetitions()
	{
		return getCompetitions( getSession().getSessionContext() );
	}
	
	public long getCompetitionsCount(final SessionContext ctx)
	{
		return getLinkedItemsCount(
			ctx,
			false,
			CuppyConstants.Relations.COMPETITIONPLAYERRELATION,
			null
		);
	}
	
	public long getCompetitionsCount()
	{
		return getCompetitionsCount( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Player.competitions</code> attribute. 
	 * @param value the competitions - Competitions a player has activated. Competitions will be selectable for player at frontend
	 */
	public void setCompetitions(final SessionContext ctx, final Set<Competition> value)
	{
		setLinkedItems( 
			ctx,
			false,
			CuppyConstants.Relations.COMPETITIONPLAYERRELATION,
			null,
			value,
			false,
			false,
			Utilities.getMarkModifiedOverride(COMPETITIONPLAYERRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Player.competitions</code> attribute. 
	 * @param value the competitions - Competitions a player has activated. Competitions will be selectable for player at frontend
	 */
	public void setCompetitions(final Set<Competition> value)
	{
		setCompetitions( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to competitions. 
	 * @param value the item to add to competitions - Competitions a player has activated. Competitions will be selectable for player at frontend
	 */
	public void addToCompetitions(final SessionContext ctx, final Competition value)
	{
		addLinkedItems( 
			ctx,
			false,
			CuppyConstants.Relations.COMPETITIONPLAYERRELATION,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(COMPETITIONPLAYERRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to competitions. 
	 * @param value the item to add to competitions - Competitions a player has activated. Competitions will be selectable for player at frontend
	 */
	public void addToCompetitions(final Competition value)
	{
		addToCompetitions( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from competitions. 
	 * @param value the item to remove from competitions - Competitions a player has activated. Competitions will be selectable for player at frontend
	 */
	public void removeFromCompetitions(final SessionContext ctx, final Competition value)
	{
		removeLinkedItems( 
			ctx,
			false,
			CuppyConstants.Relations.COMPETITIONPLAYERRELATION,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(COMPETITIONPLAYERRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from competitions. 
	 * @param value the item to remove from competitions - Competitions a player has activated. Competitions will be selectable for player at frontend
	 */
	public void removeFromCompetitions(final Competition value)
	{
		removeFromCompetitions( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Player.confirmed</code> attribute.
	 * @return the confirmed - If true the player will be able to login to frontend.
	 */
	public Boolean isConfirmed(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, CONFIRMED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Player.confirmed</code> attribute.
	 * @return the confirmed - If true the player will be able to login to frontend.
	 */
	public Boolean isConfirmed()
	{
		return isConfirmed( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Player.confirmed</code> attribute. 
	 * @return the confirmed - If true the player will be able to login to frontend.
	 */
	public boolean isConfirmedAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isConfirmed( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Player.confirmed</code> attribute. 
	 * @return the confirmed - If true the player will be able to login to frontend.
	 */
	public boolean isConfirmedAsPrimitive()
	{
		return isConfirmedAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Player.confirmed</code> attribute. 
	 * @param value the confirmed - If true the player will be able to login to frontend.
	 */
	public void setConfirmed(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, CONFIRMED,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Player.confirmed</code> attribute. 
	 * @param value the confirmed - If true the player will be able to login to frontend.
	 */
	public void setConfirmed(final Boolean value)
	{
		setConfirmed( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Player.confirmed</code> attribute. 
	 * @param value the confirmed - If true the player will be able to login to frontend.
	 */
	public void setConfirmed(final SessionContext ctx, final boolean value)
	{
		setConfirmed( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Player.confirmed</code> attribute. 
	 * @param value the confirmed - If true the player will be able to login to frontend.
	 */
	public void setConfirmed(final boolean value)
	{
		setConfirmed( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Player.country</code> attribute.
	 * @return the country - The country (nation) a player belongs to.
	 */
	public Country getCountry(final SessionContext ctx)
	{
		return (Country)getProperty( ctx, COUNTRY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Player.country</code> attribute.
	 * @return the country - The country (nation) a player belongs to.
	 */
	public Country getCountry()
	{
		return getCountry( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Player.country</code> attribute. 
	 * @param value the country - The country (nation) a player belongs to.
	 */
	public void setCountry(final SessionContext ctx, final Country value)
	{
		COUNTRYHANDLER.addValue( ctx, value, this  );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Player.country</code> attribute. 
	 * @param value the country - The country (nation) a player belongs to.
	 */
	public void setCountry(final Country value)
	{
		setCountry( getSession().getSessionContext(), value );
	}
	
	@Override
	protected Item createItem(final SessionContext ctx, final ComposedType type, final ItemAttributeMap allAttributes) throws JaloBusinessException
	{
		COUNTRYHANDLER.newInstance(ctx, allAttributes);
		return super.createItem( ctx, type, allAttributes );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Player.eMail</code> attribute.
	 * @return the eMail - EMail address of the player where cuppy related mails will be send to.
	 */
	public String getEMail(final SessionContext ctx)
	{
		return (String)getProperty( ctx, EMAIL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Player.eMail</code> attribute.
	 * @return the eMail - EMail address of the player where cuppy related mails will be send to.
	 */
	public String getEMail()
	{
		return getEMail( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Player.eMail</code> attribute. 
	 * @param value the eMail - EMail address of the player where cuppy related mails will be send to.
	 */
	public void setEMail(final SessionContext ctx, final String value)
	{
		setProperty(ctx, EMAIL,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Player.eMail</code> attribute. 
	 * @param value the eMail - EMail address of the player where cuppy related mails will be send to.
	 */
	public void setEMail(final String value)
	{
		setEMail( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Player.matchBets</code> attribute.
	 * @return the matchBets - All bets of the player.
	 */
	public Collection<MatchBet> getMatchBets(final SessionContext ctx)
	{
		return MATCHBETSHANDLER.getValues( ctx, this );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Player.matchBets</code> attribute.
	 * @return the matchBets - All bets of the player.
	 */
	public Collection<MatchBet> getMatchBets()
	{
		return getMatchBets( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Player.matchBets</code> attribute. 
	 * @param value the matchBets - All bets of the player.
	 */
	public void setMatchBets(final SessionContext ctx, final Collection<MatchBet> value)
	{
		MATCHBETSHANDLER.setValues( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Player.matchBets</code> attribute. 
	 * @param value the matchBets - All bets of the player.
	 */
	public void setMatchBets(final Collection<MatchBet> value)
	{
		setMatchBets( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to matchBets. 
	 * @param value the item to add to matchBets - All bets of the player.
	 */
	public void addToMatchBets(final SessionContext ctx, final MatchBet value)
	{
		MATCHBETSHANDLER.addValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to matchBets. 
	 * @param value the item to add to matchBets - All bets of the player.
	 */
	public void addToMatchBets(final MatchBet value)
	{
		addToMatchBets( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from matchBets. 
	 * @param value the item to remove from matchBets - All bets of the player.
	 */
	public void removeFromMatchBets(final SessionContext ctx, final MatchBet value)
	{
		MATCHBETSHANDLER.removeValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from matchBets. 
	 * @param value the item to remove from matchBets - All bets of the player.
	 */
	public void removeFromMatchBets(final MatchBet value)
	{
		removeFromMatchBets( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Player.preferences</code> attribute.
	 * @return the preferences - References the container for managing frontend related preferences of the player.
	 */
	public PlayerPreferences getPreferences(final SessionContext ctx)
	{
		return (PlayerPreferences)getProperty( ctx, PREFERENCES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Player.preferences</code> attribute.
	 * @return the preferences - References the container for managing frontend related preferences of the player.
	 */
	public PlayerPreferences getPreferences()
	{
		return getPreferences( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Player.preferences</code> attribute. 
	 * @param value the preferences - References the container for managing frontend related preferences of the player.
	 */
	public void setPreferences(final SessionContext ctx, final PlayerPreferences value)
	{
		new PartOfHandler<PlayerPreferences>()
		{
			@Override
			protected PlayerPreferences doGetValue(final SessionContext ctx)
			{
				return getPreferences( ctx );
			}
			@Override
			protected void doSetValue(final SessionContext ctx, final PlayerPreferences _value)
			{
				final PlayerPreferences value = _value;
				setProperty(ctx, PREFERENCES,value);
			}
		}.setValue( ctx, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Player.preferences</code> attribute. 
	 * @param value the preferences - References the container for managing frontend related preferences of the player.
	 */
	public void setPreferences(final PlayerPreferences value)
	{
		setPreferences( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Player.sendNewsletter</code> attribute.
	 * @return the sendNewsletter - If true the player will get mails for news published by mail related to competitions the player has activated
	 */
	public Boolean isSendNewsletter(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, SENDNEWSLETTER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Player.sendNewsletter</code> attribute.
	 * @return the sendNewsletter - If true the player will get mails for news published by mail related to competitions the player has activated
	 */
	public Boolean isSendNewsletter()
	{
		return isSendNewsletter( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Player.sendNewsletter</code> attribute. 
	 * @return the sendNewsletter - If true the player will get mails for news published by mail related to competitions the player has activated
	 */
	public boolean isSendNewsletterAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isSendNewsletter( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Player.sendNewsletter</code> attribute. 
	 * @return the sendNewsletter - If true the player will get mails for news published by mail related to competitions the player has activated
	 */
	public boolean isSendNewsletterAsPrimitive()
	{
		return isSendNewsletterAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Player.sendNewsletter</code> attribute. 
	 * @param value the sendNewsletter - If true the player will get mails for news published by mail related to competitions the player has activated
	 */
	public void setSendNewsletter(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, SENDNEWSLETTER,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Player.sendNewsletter</code> attribute. 
	 * @param value the sendNewsletter - If true the player will get mails for news published by mail related to competitions the player has activated
	 */
	public void setSendNewsletter(final Boolean value)
	{
		setSendNewsletter( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Player.sendNewsletter</code> attribute. 
	 * @param value the sendNewsletter - If true the player will get mails for news published by mail related to competitions the player has activated
	 */
	public void setSendNewsletter(final SessionContext ctx, final boolean value)
	{
		setSendNewsletter( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Player.sendNewsletter</code> attribute. 
	 * @param value the sendNewsletter - If true the player will get mails for news published by mail related to competitions the player has activated
	 */
	public void setSendNewsletter(final boolean value)
	{
		setSendNewsletter( getSession().getSessionContext(), value );
	}
	
}

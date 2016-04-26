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
import de.hybris.platform.cuppy.jalo.Group;
import de.hybris.platform.cuppy.jalo.News;
import de.hybris.platform.cuppy.jalo.Player;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.util.OneToManyHandler;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Generated class for type {@link de.hybris.platform.cuppy.jalo.Competition Competition}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedCompetition extends GenericItem
{
	/** Qualifier of the <code>Competition.code</code> attribute **/
	public static final String CODE = "code";
	/** Qualifier of the <code>Competition.name</code> attribute **/
	public static final String NAME = "name";
	/** Qualifier of the <code>Competition.type</code> attribute **/
	public static final String TYPE = "type";
	/** Qualifier of the <code>Competition.Finished</code> attribute **/
	public static final String FINISHED = "Finished";
	/** Qualifier of the <code>Competition.groups</code> attribute **/
	public static final String GROUPS = "groups";
	/** Qualifier of the <code>Competition.news</code> attribute **/
	public static final String NEWS = "news";
	/** Qualifier of the <code>Competition.players</code> attribute **/
	public static final String PLAYERS = "players";
	/** Relation ordering override parameter constants for CompetitionPlayerRelation from ((cuppy))*/
	protected static String COMPETITIONPLAYERRELATION_SRC_ORDERED = "relation.CompetitionPlayerRelation.source.ordered";
	protected static String COMPETITIONPLAYERRELATION_TGT_ORDERED = "relation.CompetitionPlayerRelation.target.ordered";
	/** Relation disable markmodifed parameter constants for CompetitionPlayerRelation from ((cuppy))*/
	protected static String COMPETITIONPLAYERRELATION_MARKMODIFIED = "relation.CompetitionPlayerRelation.markmodified";
	/**
	* {@link OneToManyHandler} for handling 1:n GROUPS's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<Group> GROUPSHANDLER = new OneToManyHandler<Group>(
	CuppyConstants.TC.GROUP,
	false,
	"competition",
	null,
	false,
	true,
	CollectionType.COLLECTION
	);
	/**
	* {@link OneToManyHandler} for handling 1:n NEWS's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<News> NEWSHANDLER = new OneToManyHandler<News>(
	CuppyConstants.TC.NEWS,
	false,
	"competition",
	null,
	false,
	true,
	CollectionType.COLLECTION
	);
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(CODE, AttributeMode.INITIAL);
		tmp.put(NAME, AttributeMode.INITIAL);
		tmp.put(TYPE, AttributeMode.INITIAL);
		tmp.put(FINISHED, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Competition.code</code> attribute.
	 * @return the code - Unique identifier of a competition.
	 */
	public String getCode(final SessionContext ctx)
	{
		return (String)getProperty( ctx, CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Competition.code</code> attribute.
	 * @return the code - Unique identifier of a competition.
	 */
	public String getCode()
	{
		return getCode( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Competition.code</code> attribute. 
	 * @param value the code - Unique identifier of a competition.
	 */
	public void setCode(final SessionContext ctx, final String value)
	{
		setProperty(ctx, CODE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Competition.code</code> attribute. 
	 * @param value the code - Unique identifier of a competition.
	 */
	public void setCode(final String value)
	{
		setCode( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Competition.Finished</code> attribute.
	 * @return the Finished - Is this competition finished and with that no bets can be placed
	 * 					 anymore and all results are calculated.
	 */
	public Boolean isFinished(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, FINISHED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Competition.Finished</code> attribute.
	 * @return the Finished - Is this competition finished and with that no bets can be placed
	 * 					 anymore and all results are calculated.
	 */
	public Boolean isFinished()
	{
		return isFinished( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Competition.Finished</code> attribute. 
	 * @return the Finished - Is this competition finished and with that no bets can be placed
	 * 					 anymore and all results are calculated.
	 */
	public boolean isFinishedAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isFinished( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Competition.Finished</code> attribute. 
	 * @return the Finished - Is this competition finished and with that no bets can be placed
	 * 					 anymore and all results are calculated.
	 */
	public boolean isFinishedAsPrimitive()
	{
		return isFinishedAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Competition.Finished</code> attribute. 
	 * @param value the Finished - Is this competition finished and with that no bets can be placed
	 * 					 anymore and all results are calculated.
	 */
	public void setFinished(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, FINISHED,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Competition.Finished</code> attribute. 
	 * @param value the Finished - Is this competition finished and with that no bets can be placed
	 * 					 anymore and all results are calculated.
	 */
	public void setFinished(final Boolean value)
	{
		setFinished( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Competition.Finished</code> attribute. 
	 * @param value the Finished - Is this competition finished and with that no bets can be placed
	 * 					 anymore and all results are calculated.
	 */
	public void setFinished(final SessionContext ctx, final boolean value)
	{
		setFinished( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Competition.Finished</code> attribute. 
	 * @param value the Finished - Is this competition finished and with that no bets can be placed
	 * 					 anymore and all results are calculated.
	 */
	public void setFinished(final boolean value)
	{
		setFinished( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Competition.groups</code> attribute.
	 * @return the groups - The groups the competition consists of.
	 */
	public Collection<Group> getGroups(final SessionContext ctx)
	{
		return GROUPSHANDLER.getValues( ctx, this );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Competition.groups</code> attribute.
	 * @return the groups - The groups the competition consists of.
	 */
	public Collection<Group> getGroups()
	{
		return getGroups( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Competition.groups</code> attribute. 
	 * @param value the groups - The groups the competition consists of.
	 */
	public void setGroups(final SessionContext ctx, final Collection<Group> value)
	{
		GROUPSHANDLER.setValues( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Competition.groups</code> attribute. 
	 * @param value the groups - The groups the competition consists of.
	 */
	public void setGroups(final Collection<Group> value)
	{
		setGroups( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to groups. 
	 * @param value the item to add to groups - The groups the competition consists of.
	 */
	public void addToGroups(final SessionContext ctx, final Group value)
	{
		GROUPSHANDLER.addValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to groups. 
	 * @param value the item to add to groups - The groups the competition consists of.
	 */
	public void addToGroups(final Group value)
	{
		addToGroups( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from groups. 
	 * @param value the item to remove from groups - The groups the competition consists of.
	 */
	public void removeFromGroups(final SessionContext ctx, final Group value)
	{
		GROUPSHANDLER.removeValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from groups. 
	 * @param value the item to remove from groups - The groups the competition consists of.
	 */
	public void removeFromGroups(final Group value)
	{
		removeFromGroups( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Competition.name</code> attribute.
	 * @return the name - Name of the competition.
	 */
	public String getName(final SessionContext ctx)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedCompetition.getName requires a session language", 0 );
		}
		return (String)getLocalizedProperty( ctx, NAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Competition.name</code> attribute.
	 * @return the name - Name of the competition.
	 */
	public String getName()
	{
		return getName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Competition.name</code> attribute. 
	 * @return the localized name - Name of the competition.
	 */
	public Map<Language,String> getAllName(final SessionContext ctx)
	{
		return (Map<Language,String>)getAllLocalizedProperties(ctx,NAME,C2LManager.getInstance().getAllLanguages());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Competition.name</code> attribute. 
	 * @return the localized name - Name of the competition.
	 */
	public Map<Language,String> getAllName()
	{
		return getAllName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Competition.name</code> attribute. 
	 * @param value the name - Name of the competition.
	 */
	public void setName(final SessionContext ctx, final String value)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedCompetition.setName requires a session language", 0 );
		}
		setLocalizedProperty(ctx, NAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Competition.name</code> attribute. 
	 * @param value the name - Name of the competition.
	 */
	public void setName(final String value)
	{
		setName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Competition.name</code> attribute. 
	 * @param value the name - Name of the competition.
	 */
	public void setAllName(final SessionContext ctx, final Map<Language,String> value)
	{
		setAllLocalizedProperties(ctx,NAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Competition.name</code> attribute. 
	 * @param value the name - Name of the competition.
	 */
	public void setAllName(final Map<Language,String> value)
	{
		setAllName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Competition.news</code> attribute.
	 * @return the news - All news of the competition (excluding news without explicitly assigned comeptition).
	 */
	public Collection<News> getNews(final SessionContext ctx)
	{
		return NEWSHANDLER.getValues( ctx, this );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Competition.news</code> attribute.
	 * @return the news - All news of the competition (excluding news without explicitly assigned comeptition).
	 */
	public Collection<News> getNews()
	{
		return getNews( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Competition.news</code> attribute. 
	 * @param value the news - All news of the competition (excluding news without explicitly assigned comeptition).
	 */
	public void setNews(final SessionContext ctx, final Collection<News> value)
	{
		NEWSHANDLER.setValues( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Competition.news</code> attribute. 
	 * @param value the news - All news of the competition (excluding news without explicitly assigned comeptition).
	 */
	public void setNews(final Collection<News> value)
	{
		setNews( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to news. 
	 * @param value the item to add to news - All news of the competition (excluding news without explicitly assigned comeptition).
	 */
	public void addToNews(final SessionContext ctx, final News value)
	{
		NEWSHANDLER.addValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to news. 
	 * @param value the item to add to news - All news of the competition (excluding news without explicitly assigned comeptition).
	 */
	public void addToNews(final News value)
	{
		addToNews( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from news. 
	 * @param value the item to remove from news - All news of the competition (excluding news without explicitly assigned comeptition).
	 */
	public void removeFromNews(final SessionContext ctx, final News value)
	{
		NEWSHANDLER.removeValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from news. 
	 * @param value the item to remove from news - All news of the competition (excluding news without explicitly assigned comeptition).
	 */
	public void removeFromNews(final News value)
	{
		removeFromNews( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Competition.players</code> attribute.
	 * @return the players - Players having the competition activated. Competition will be selectable for this player at frontend.
	 */
	public Set<Player> getPlayers(final SessionContext ctx)
	{
		final List<Player> items = getLinkedItems( 
			ctx,
			true,
			CuppyConstants.Relations.COMPETITIONPLAYERRELATION,
			null,
			false,
			false
		);
		return new LinkedHashSet<Player>(items);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Competition.players</code> attribute.
	 * @return the players - Players having the competition activated. Competition will be selectable for this player at frontend.
	 */
	public Set<Player> getPlayers()
	{
		return getPlayers( getSession().getSessionContext() );
	}
	
	public long getPlayersCount(final SessionContext ctx)
	{
		return getLinkedItemsCount(
			ctx,
			true,
			CuppyConstants.Relations.COMPETITIONPLAYERRELATION,
			null
		);
	}
	
	public long getPlayersCount()
	{
		return getPlayersCount( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Competition.players</code> attribute. 
	 * @param value the players - Players having the competition activated. Competition will be selectable for this player at frontend.
	 */
	public void setPlayers(final SessionContext ctx, final Set<Player> value)
	{
		setLinkedItems( 
			ctx,
			true,
			CuppyConstants.Relations.COMPETITIONPLAYERRELATION,
			null,
			value,
			false,
			false,
			Utilities.getMarkModifiedOverride(COMPETITIONPLAYERRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Competition.players</code> attribute. 
	 * @param value the players - Players having the competition activated. Competition will be selectable for this player at frontend.
	 */
	public void setPlayers(final Set<Player> value)
	{
		setPlayers( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to players. 
	 * @param value the item to add to players - Players having the competition activated. Competition will be selectable for this player at frontend.
	 */
	public void addToPlayers(final SessionContext ctx, final Player value)
	{
		addLinkedItems( 
			ctx,
			true,
			CuppyConstants.Relations.COMPETITIONPLAYERRELATION,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(COMPETITIONPLAYERRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to players. 
	 * @param value the item to add to players - Players having the competition activated. Competition will be selectable for this player at frontend.
	 */
	public void addToPlayers(final Player value)
	{
		addToPlayers( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from players. 
	 * @param value the item to remove from players - Players having the competition activated. Competition will be selectable for this player at frontend.
	 */
	public void removeFromPlayers(final SessionContext ctx, final Player value)
	{
		removeLinkedItems( 
			ctx,
			true,
			CuppyConstants.Relations.COMPETITIONPLAYERRELATION,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(COMPETITIONPLAYERRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from players. 
	 * @param value the item to remove from players - Players having the competition activated. Competition will be selectable for this player at frontend.
	 */
	public void removeFromPlayers(final Player value)
	{
		removeFromPlayers( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Competition.type</code> attribute.
	 * @return the type - Type of competition indicating how to present it at frontend.
	 */
	public EnumerationValue getType(final SessionContext ctx)
	{
		return (EnumerationValue)getProperty( ctx, TYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Competition.type</code> attribute.
	 * @return the type - Type of competition indicating how to present it at frontend.
	 */
	public EnumerationValue getType()
	{
		return getType( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Competition.type</code> attribute. 
	 * @param value the type - Type of competition indicating how to present it at frontend.
	 */
	public void setType(final SessionContext ctx, final EnumerationValue value)
	{
		setProperty(ctx, TYPE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Competition.type</code> attribute. 
	 * @param value the type - Type of competition indicating how to present it at frontend.
	 */
	public void setType(final EnumerationValue value)
	{
		setType( getSession().getSessionContext(), value );
	}
	
}

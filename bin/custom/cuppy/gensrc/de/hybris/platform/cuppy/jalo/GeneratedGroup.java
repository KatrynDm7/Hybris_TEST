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
import de.hybris.platform.cuppy.jalo.Competition;
import de.hybris.platform.cuppy.jalo.Match;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.cuppy.jalo.Group Group}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedGroup extends GenericItem
{
	/** Qualifier of the <code>Group.code</code> attribute **/
	public static final String CODE = "code";
	/** Qualifier of the <code>Group.name</code> attribute **/
	public static final String NAME = "name";
	/** Qualifier of the <code>Group.multiplier</code> attribute **/
	public static final String MULTIPLIER = "multiplier";
	/** Qualifier of the <code>Group.sequenceNumber</code> attribute **/
	public static final String SEQUENCENUMBER = "sequenceNumber";
	/** Qualifier of the <code>Group.matches</code> attribute **/
	public static final String MATCHES = "matches";
	/** Qualifier of the <code>Group.competition</code> attribute **/
	public static final String COMPETITION = "competition";
	/**
	* {@link OneToManyHandler} for handling 1:n MATCHES's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<Match> MATCHESHANDLER = new OneToManyHandler<Match>(
	CuppyConstants.TC.MATCH,
	false,
	"group",
	null,
	false,
	true,
	CollectionType.COLLECTION
	);
	/**
	* {@link BidirectionalOneToManyHandler} for handling 1:n COMPETITION's relation attributes from 'one' side.
	**/
	protected static final BidirectionalOneToManyHandler<GeneratedGroup> COMPETITIONHANDLER = new BidirectionalOneToManyHandler<GeneratedGroup>(
	CuppyConstants.TC.GROUP,
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
		tmp.put(MULTIPLIER, AttributeMode.INITIAL);
		tmp.put(SEQUENCENUMBER, AttributeMode.INITIAL);
		tmp.put(COMPETITION, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Group.code</code> attribute.
	 * @return the code - Unique identifier of group within a competition.
	 */
	public String getCode(final SessionContext ctx)
	{
		return (String)getProperty( ctx, CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Group.code</code> attribute.
	 * @return the code - Unique identifier of group within a competition.
	 */
	public String getCode()
	{
		return getCode( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Group.code</code> attribute. 
	 * @param value the code - Unique identifier of group within a competition.
	 */
	public void setCode(final SessionContext ctx, final String value)
	{
		setProperty(ctx, CODE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Group.code</code> attribute. 
	 * @param value the code - Unique identifier of group within a competition.
	 */
	public void setCode(final String value)
	{
		setCode( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Group.competition</code> attribute.
	 * @return the competition - The competition this group belongs to.
	 */
	public Competition getCompetition(final SessionContext ctx)
	{
		return (Competition)getProperty( ctx, COMPETITION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Group.competition</code> attribute.
	 * @return the competition - The competition this group belongs to.
	 */
	public Competition getCompetition()
	{
		return getCompetition( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Group.competition</code> attribute. 
	 * @param value the competition - The competition this group belongs to.
	 */
	public void setCompetition(final SessionContext ctx, final Competition value)
	{
		COMPETITIONHANDLER.addValue( ctx, value, this  );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Group.competition</code> attribute. 
	 * @param value the competition - The competition this group belongs to.
	 */
	public void setCompetition(final Competition value)
	{
		setCompetition( getSession().getSessionContext(), value );
	}
	
	@Override
	protected Item createItem(final SessionContext ctx, final ComposedType type, final ItemAttributeMap allAttributes) throws JaloBusinessException
	{
		COMPETITIONHANDLER.newInstance(ctx, allAttributes);
		return super.createItem( ctx, type, allAttributes );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Group.matches</code> attribute.
	 * @return the matches - The matches of a group
	 */
	public Collection<Match> getMatches(final SessionContext ctx)
	{
		return MATCHESHANDLER.getValues( ctx, this );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Group.matches</code> attribute.
	 * @return the matches - The matches of a group
	 */
	public Collection<Match> getMatches()
	{
		return getMatches( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Group.matches</code> attribute. 
	 * @param value the matches - The matches of a group
	 */
	public void setMatches(final SessionContext ctx, final Collection<Match> value)
	{
		MATCHESHANDLER.setValues( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Group.matches</code> attribute. 
	 * @param value the matches - The matches of a group
	 */
	public void setMatches(final Collection<Match> value)
	{
		setMatches( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to matches. 
	 * @param value the item to add to matches - The matches of a group
	 */
	public void addToMatches(final SessionContext ctx, final Match value)
	{
		MATCHESHANDLER.addValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to matches. 
	 * @param value the item to add to matches - The matches of a group
	 */
	public void addToMatches(final Match value)
	{
		addToMatches( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from matches. 
	 * @param value the item to remove from matches - The matches of a group
	 */
	public void removeFromMatches(final SessionContext ctx, final Match value)
	{
		MATCHESHANDLER.removeValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from matches. 
	 * @param value the item to remove from matches - The matches of a group
	 */
	public void removeFromMatches(final Match value)
	{
		removeFromMatches( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Group.multiplier</code> attribute.
	 * @return the multiplier - Defines a weight for the scoring. The score a player gets on a bet for this match get multiplied by it.
	 */
	public Float getMultiplier(final SessionContext ctx)
	{
		return (Float)getProperty( ctx, MULTIPLIER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Group.multiplier</code> attribute.
	 * @return the multiplier - Defines a weight for the scoring. The score a player gets on a bet for this match get multiplied by it.
	 */
	public Float getMultiplier()
	{
		return getMultiplier( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Group.multiplier</code> attribute. 
	 * @return the multiplier - Defines a weight for the scoring. The score a player gets on a bet for this match get multiplied by it.
	 */
	public float getMultiplierAsPrimitive(final SessionContext ctx)
	{
		Float value = getMultiplier( ctx );
		return value != null ? value.floatValue() : 0.0f;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Group.multiplier</code> attribute. 
	 * @return the multiplier - Defines a weight for the scoring. The score a player gets on a bet for this match get multiplied by it.
	 */
	public float getMultiplierAsPrimitive()
	{
		return getMultiplierAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Group.multiplier</code> attribute. 
	 * @param value the multiplier - Defines a weight for the scoring. The score a player gets on a bet for this match get multiplied by it.
	 */
	public void setMultiplier(final SessionContext ctx, final Float value)
	{
		setProperty(ctx, MULTIPLIER,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Group.multiplier</code> attribute. 
	 * @param value the multiplier - Defines a weight for the scoring. The score a player gets on a bet for this match get multiplied by it.
	 */
	public void setMultiplier(final Float value)
	{
		setMultiplier( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Group.multiplier</code> attribute. 
	 * @param value the multiplier - Defines a weight for the scoring. The score a player gets on a bet for this match get multiplied by it.
	 */
	public void setMultiplier(final SessionContext ctx, final float value)
	{
		setMultiplier( ctx,Float.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Group.multiplier</code> attribute. 
	 * @param value the multiplier - Defines a weight for the scoring. The score a player gets on a bet for this match get multiplied by it.
	 */
	public void setMultiplier(final float value)
	{
		setMultiplier( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Group.name</code> attribute.
	 * @return the name - Name of group.
	 */
	public String getName(final SessionContext ctx)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedGroup.getName requires a session language", 0 );
		}
		return (String)getLocalizedProperty( ctx, NAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Group.name</code> attribute.
	 * @return the name - Name of group.
	 */
	public String getName()
	{
		return getName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Group.name</code> attribute. 
	 * @return the localized name - Name of group.
	 */
	public Map<Language,String> getAllName(final SessionContext ctx)
	{
		return (Map<Language,String>)getAllLocalizedProperties(ctx,NAME,C2LManager.getInstance().getAllLanguages());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Group.name</code> attribute. 
	 * @return the localized name - Name of group.
	 */
	public Map<Language,String> getAllName()
	{
		return getAllName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Group.name</code> attribute. 
	 * @param value the name - Name of group.
	 */
	public void setName(final SessionContext ctx, final String value)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedGroup.setName requires a session language", 0 );
		}
		setLocalizedProperty(ctx, NAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Group.name</code> attribute. 
	 * @param value the name - Name of group.
	 */
	public void setName(final String value)
	{
		setName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Group.name</code> attribute. 
	 * @param value the name - Name of group.
	 */
	public void setAllName(final SessionContext ctx, final Map<Language,String> value)
	{
		setAllLocalizedProperties(ctx,NAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Group.name</code> attribute. 
	 * @param value the name - Name of group.
	 */
	public void setAllName(final Map<Language,String> value)
	{
		setAllName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Group.sequenceNumber</code> attribute.
	 * @return the sequenceNumber - Absolute number ordering the groups within a competition. So it is assured that preliminaries are
	 * 					displayed always as first group of a competition for example.
	 */
	public Integer getSequenceNumber(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, SEQUENCENUMBER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Group.sequenceNumber</code> attribute.
	 * @return the sequenceNumber - Absolute number ordering the groups within a competition. So it is assured that preliminaries are
	 * 					displayed always as first group of a competition for example.
	 */
	public Integer getSequenceNumber()
	{
		return getSequenceNumber( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Group.sequenceNumber</code> attribute. 
	 * @return the sequenceNumber - Absolute number ordering the groups within a competition. So it is assured that preliminaries are
	 * 					displayed always as first group of a competition for example.
	 */
	public int getSequenceNumberAsPrimitive(final SessionContext ctx)
	{
		Integer value = getSequenceNumber( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Group.sequenceNumber</code> attribute. 
	 * @return the sequenceNumber - Absolute number ordering the groups within a competition. So it is assured that preliminaries are
	 * 					displayed always as first group of a competition for example.
	 */
	public int getSequenceNumberAsPrimitive()
	{
		return getSequenceNumberAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Group.sequenceNumber</code> attribute. 
	 * @param value the sequenceNumber - Absolute number ordering the groups within a competition. So it is assured that preliminaries are
	 * 					displayed always as first group of a competition for example.
	 */
	public void setSequenceNumber(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, SEQUENCENUMBER,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Group.sequenceNumber</code> attribute. 
	 * @param value the sequenceNumber - Absolute number ordering the groups within a competition. So it is assured that preliminaries are
	 * 					displayed always as first group of a competition for example.
	 */
	public void setSequenceNumber(final Integer value)
	{
		setSequenceNumber( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Group.sequenceNumber</code> attribute. 
	 * @param value the sequenceNumber - Absolute number ordering the groups within a competition. So it is assured that preliminaries are
	 * 					displayed always as first group of a competition for example.
	 */
	public void setSequenceNumber(final SessionContext ctx, final int value)
	{
		setSequenceNumber( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Group.sequenceNumber</code> attribute. 
	 * @param value the sequenceNumber - Absolute number ordering the groups within a competition. So it is assured that preliminaries are
	 * 					displayed always as first group of a competition for example.
	 */
	public void setSequenceNumber(final int value)
	{
		setSequenceNumber( getSession().getSessionContext(), value );
	}
	
}

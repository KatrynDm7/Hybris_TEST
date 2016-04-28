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
import de.hybris.platform.cuppy.jalo.Match;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Country;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.cuppy.jalo.Team Team}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedTeam extends Country
{
	/** Qualifier of the <code>Team.dummy</code> attribute **/
	public static final String DUMMY = "dummy";
	/** Qualifier of the <code>Team.guestMatches</code> attribute **/
	public static final String GUESTMATCHES = "guestMatches";
	/** Qualifier of the <code>Team.homeMatches</code> attribute **/
	public static final String HOMEMATCHES = "homeMatches";
	/**
	* {@link OneToManyHandler} for handling 1:n GUESTMATCHES's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<Match> GUESTMATCHESHANDLER = new OneToManyHandler<Match>(
	CuppyConstants.TC.MATCH,
	false,
	"guestTeam",
	null,
	false,
	true,
	CollectionType.COLLECTION
	);
	/**
	* {@link OneToManyHandler} for handling 1:n HOMEMATCHES's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<Match> HOMEMATCHESHANDLER = new OneToManyHandler<Match>(
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
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(Country.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(DUMMY, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Team.dummy</code> attribute.
	 * @return the dummy - If true, the team will act as a placeholder
	 */
	public Boolean isDummy(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, DUMMY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Team.dummy</code> attribute.
	 * @return the dummy - If true, the team will act as a placeholder
	 */
	public Boolean isDummy()
	{
		return isDummy( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Team.dummy</code> attribute. 
	 * @return the dummy - If true, the team will act as a placeholder
	 */
	public boolean isDummyAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isDummy( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Team.dummy</code> attribute. 
	 * @return the dummy - If true, the team will act as a placeholder
	 */
	public boolean isDummyAsPrimitive()
	{
		return isDummyAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Team.dummy</code> attribute. 
	 * @param value the dummy - If true, the team will act as a placeholder
	 */
	public void setDummy(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, DUMMY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Team.dummy</code> attribute. 
	 * @param value the dummy - If true, the team will act as a placeholder
	 */
	public void setDummy(final Boolean value)
	{
		setDummy( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Team.dummy</code> attribute. 
	 * @param value the dummy - If true, the team will act as a placeholder
	 */
	public void setDummy(final SessionContext ctx, final boolean value)
	{
		setDummy( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Team.dummy</code> attribute. 
	 * @param value the dummy - If true, the team will act as a placeholder
	 */
	public void setDummy(final boolean value)
	{
		setDummy( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Team.guestMatches</code> attribute.
	 * @return the guestMatches - The matches the team is playing as guest team.
	 */
	public Collection<Match> getGuestMatches(final SessionContext ctx)
	{
		return GUESTMATCHESHANDLER.getValues( ctx, this );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Team.guestMatches</code> attribute.
	 * @return the guestMatches - The matches the team is playing as guest team.
	 */
	public Collection<Match> getGuestMatches()
	{
		return getGuestMatches( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Team.guestMatches</code> attribute. 
	 * @param value the guestMatches - The matches the team is playing as guest team.
	 */
	public void setGuestMatches(final SessionContext ctx, final Collection<Match> value)
	{
		GUESTMATCHESHANDLER.setValues( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Team.guestMatches</code> attribute. 
	 * @param value the guestMatches - The matches the team is playing as guest team.
	 */
	public void setGuestMatches(final Collection<Match> value)
	{
		setGuestMatches( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to guestMatches. 
	 * @param value the item to add to guestMatches - The matches the team is playing as guest team.
	 */
	public void addToGuestMatches(final SessionContext ctx, final Match value)
	{
		GUESTMATCHESHANDLER.addValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to guestMatches. 
	 * @param value the item to add to guestMatches - The matches the team is playing as guest team.
	 */
	public void addToGuestMatches(final Match value)
	{
		addToGuestMatches( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from guestMatches. 
	 * @param value the item to remove from guestMatches - The matches the team is playing as guest team.
	 */
	public void removeFromGuestMatches(final SessionContext ctx, final Match value)
	{
		GUESTMATCHESHANDLER.removeValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from guestMatches. 
	 * @param value the item to remove from guestMatches - The matches the team is playing as guest team.
	 */
	public void removeFromGuestMatches(final Match value)
	{
		removeFromGuestMatches( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Team.homeMatches</code> attribute.
	 * @return the homeMatches - The matches the team is playing as home team.
	 */
	public Collection<Match> getHomeMatches(final SessionContext ctx)
	{
		return HOMEMATCHESHANDLER.getValues( ctx, this );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Team.homeMatches</code> attribute.
	 * @return the homeMatches - The matches the team is playing as home team.
	 */
	public Collection<Match> getHomeMatches()
	{
		return getHomeMatches( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Team.homeMatches</code> attribute. 
	 * @param value the homeMatches - The matches the team is playing as home team.
	 */
	public void setHomeMatches(final SessionContext ctx, final Collection<Match> value)
	{
		HOMEMATCHESHANDLER.setValues( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Team.homeMatches</code> attribute. 
	 * @param value the homeMatches - The matches the team is playing as home team.
	 */
	public void setHomeMatches(final Collection<Match> value)
	{
		setHomeMatches( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to homeMatches. 
	 * @param value the item to add to homeMatches - The matches the team is playing as home team.
	 */
	public void addToHomeMatches(final SessionContext ctx, final Match value)
	{
		HOMEMATCHESHANDLER.addValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to homeMatches. 
	 * @param value the item to add to homeMatches - The matches the team is playing as home team.
	 */
	public void addToHomeMatches(final Match value)
	{
		addToHomeMatches( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from homeMatches. 
	 * @param value the item to remove from homeMatches - The matches the team is playing as home team.
	 */
	public void removeFromHomeMatches(final SessionContext ctx, final Match value)
	{
		HOMEMATCHESHANDLER.removeValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from homeMatches. 
	 * @param value the item to remove from homeMatches - The matches the team is playing as home team.
	 */
	public void removeFromHomeMatches(final Match value)
	{
		removeFromHomeMatches( getSession().getSessionContext(), value );
	}
	
}

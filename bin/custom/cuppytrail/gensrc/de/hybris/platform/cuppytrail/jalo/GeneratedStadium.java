/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 11.04.2016 18:36:08                         ---
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
package de.hybris.platform.cuppytrail.jalo;

import de.hybris.platform.cuppy.constants.CuppyConstants;
import de.hybris.platform.cuppy.jalo.Match;
import de.hybris.platform.cuppytrail.constants.CuppytrailConstants;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.jalo.GenericItem Stadium}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedStadium extends GenericItem
{
	/** Qualifier of the <code>Stadium.code</code> attribute **/
	public static final String CODE = "code";
	/** Qualifier of the <code>Stadium.capacity</code> attribute **/
	public static final String CAPACITY = "capacity";
	/** Qualifier of the <code>Stadium.StadiumType</code> attribute **/
	public static final String STADIUMTYPE = "StadiumType";
	/** Qualifier of the <code>Stadium.matches</code> attribute **/
	public static final String MATCHES = "matches";
	/**
	* {@link OneToManyHandler} for handling 1:n MATCHES's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<Match> MATCHESHANDLER = new OneToManyHandler<Match>(
	CuppyConstants.TC.MATCH,
	false,
	"stadium",
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
		tmp.put(CAPACITY, AttributeMode.INITIAL);
		tmp.put(STADIUMTYPE, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Stadium.capacity</code> attribute.
	 * @return the capacity - Capacity
	 */
	public Integer getCapacity(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, CAPACITY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Stadium.capacity</code> attribute.
	 * @return the capacity - Capacity
	 */
	public Integer getCapacity()
	{
		return getCapacity( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Stadium.capacity</code> attribute. 
	 * @return the capacity - Capacity
	 */
	public int getCapacityAsPrimitive(final SessionContext ctx)
	{
		Integer value = getCapacity( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Stadium.capacity</code> attribute. 
	 * @return the capacity - Capacity
	 */
	public int getCapacityAsPrimitive()
	{
		return getCapacityAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Stadium.capacity</code> attribute. 
	 * @param value the capacity - Capacity
	 */
	public void setCapacity(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, CAPACITY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Stadium.capacity</code> attribute. 
	 * @param value the capacity - Capacity
	 */
	public void setCapacity(final Integer value)
	{
		setCapacity( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Stadium.capacity</code> attribute. 
	 * @param value the capacity - Capacity
	 */
	public void setCapacity(final SessionContext ctx, final int value)
	{
		setCapacity( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Stadium.capacity</code> attribute. 
	 * @param value the capacity - Capacity
	 */
	public void setCapacity(final int value)
	{
		setCapacity( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Stadium.code</code> attribute.
	 * @return the code
	 */
	public String getCode(final SessionContext ctx)
	{
		return (String)getProperty( ctx, CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Stadium.code</code> attribute.
	 * @return the code
	 */
	public String getCode()
	{
		return getCode( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Stadium.code</code> attribute. 
	 * @param value the code
	 */
	public void setCode(final SessionContext ctx, final String value)
	{
		setProperty(ctx, CODE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Stadium.code</code> attribute. 
	 * @param value the code
	 */
	public void setCode(final String value)
	{
		setCode( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Stadium.matches</code> attribute.
	 * @return the matches
	 */
	public Collection<Match> getMatches(final SessionContext ctx)
	{
		return MATCHESHANDLER.getValues( ctx, this );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Stadium.matches</code> attribute.
	 * @return the matches
	 */
	public Collection<Match> getMatches()
	{
		return getMatches( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Stadium.matches</code> attribute. 
	 * @param value the matches
	 */
	public void setMatches(final SessionContext ctx, final Collection<Match> value)
	{
		MATCHESHANDLER.setValues( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Stadium.matches</code> attribute. 
	 * @param value the matches
	 */
	public void setMatches(final Collection<Match> value)
	{
		setMatches( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to matches. 
	 * @param value the item to add to matches
	 */
	public void addToMatches(final SessionContext ctx, final Match value)
	{
		MATCHESHANDLER.addValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to matches. 
	 * @param value the item to add to matches
	 */
	public void addToMatches(final Match value)
	{
		addToMatches( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from matches. 
	 * @param value the item to remove from matches
	 */
	public void removeFromMatches(final SessionContext ctx, final Match value)
	{
		MATCHESHANDLER.removeValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from matches. 
	 * @param value the item to remove from matches
	 */
	public void removeFromMatches(final Match value)
	{
		removeFromMatches( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Stadium.StadiumType</code> attribute.
	 * @return the StadiumType
	 */
	public EnumerationValue getStadiumType(final SessionContext ctx)
	{
		return (EnumerationValue)getProperty( ctx, STADIUMTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Stadium.StadiumType</code> attribute.
	 * @return the StadiumType
	 */
	public EnumerationValue getStadiumType()
	{
		return getStadiumType( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Stadium.StadiumType</code> attribute. 
	 * @param value the StadiumType
	 */
	public void setStadiumType(final SessionContext ctx, final EnumerationValue value)
	{
		setProperty(ctx, STADIUMTYPE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Stadium.StadiumType</code> attribute. 
	 * @param value the StadiumType
	 */
	public void setStadiumType(final EnumerationValue value)
	{
		setStadiumType( getSession().getSessionContext(), value );
	}
	
}

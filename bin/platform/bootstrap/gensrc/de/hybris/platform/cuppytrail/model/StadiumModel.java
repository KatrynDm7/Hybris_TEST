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
package de.hybris.platform.cuppytrail.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cuppy.model.MatchModel;
import de.hybris.platform.cuppytrail.enums.StadiumType;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

/**
 * Generated model class for type Stadium first defined at extension cuppytrail.
 */
@SuppressWarnings("all")
public class StadiumModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "Stadium";
	
	/** <i>Generated constant</i> - Attribute key of <code>Stadium.code</code> attribute defined at extension <code>cuppytrail</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>Stadium.capacity</code> attribute defined at extension <code>cuppytrail</code>. */
	public static final String CAPACITY = "capacity";
	
	/** <i>Generated constant</i> - Attribute key of <code>Stadium.StadiumType</code> attribute defined at extension <code>cuppytrail</code>. */
	public static final String STADIUMTYPE = "StadiumType";
	
	/** <i>Generated constant</i> - Attribute key of <code>Stadium.matches</code> attribute defined at extension <code>cuppytrail</code>. */
	public static final String MATCHES = "matches";
	
	
	/** <i>Generated variable</i> - Variable of <code>Stadium.code</code> attribute defined at extension <code>cuppytrail</code>. */
	private String _code;
	
	/** <i>Generated variable</i> - Variable of <code>Stadium.capacity</code> attribute defined at extension <code>cuppytrail</code>. */
	private Integer _capacity;
	
	/** <i>Generated variable</i> - Variable of <code>Stadium.StadiumType</code> attribute defined at extension <code>cuppytrail</code>. */
	private StadiumType _StadiumType;
	
	/** <i>Generated variable</i> - Variable of <code>Stadium.matches</code> attribute defined at extension <code>cuppytrail</code>. */
	private Collection<MatchModel> _matches;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public StadiumModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public StadiumModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>Stadium</code> at extension <code>cuppytrail</code>
	 */
	@Deprecated
	public StadiumModel(final String _code)
	{
		super();
		setCode(_code);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>Stadium</code> at extension <code>cuppytrail</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public StadiumModel(final String _code, final ItemModel _owner)
	{
		super();
		setCode(_code);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Stadium.capacity</code> attribute defined at extension <code>cuppytrail</code>. 
	 * @return the capacity - Capacity
	 */
	@Accessor(qualifier = "capacity", type = Accessor.Type.GETTER)
	public Integer getCapacity()
	{
		if (this._capacity!=null)
		{
			return _capacity;
		}
		return _capacity = getPersistenceContext().getValue(CAPACITY, _capacity);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Stadium.code</code> attribute defined at extension <code>cuppytrail</code>. 
	 * @return the code
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
	 * <i>Generated method</i> - Getter of the <code>Stadium.matches</code> attribute defined at extension <code>cuppytrail</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the matches
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
	 * <i>Generated method</i> - Getter of the <code>Stadium.StadiumType</code> attribute defined at extension <code>cuppytrail</code>. 
	 * @return the StadiumType
	 */
	@Accessor(qualifier = "StadiumType", type = Accessor.Type.GETTER)
	public StadiumType getStadiumType()
	{
		if (this._StadiumType!=null)
		{
			return _StadiumType;
		}
		return _StadiumType = getPersistenceContext().getValue(STADIUMTYPE, _StadiumType);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Stadium.capacity</code> attribute defined at extension <code>cuppytrail</code>. 
	 *  
	 * @param value the capacity - Capacity
	 */
	@Accessor(qualifier = "capacity", type = Accessor.Type.SETTER)
	public void setCapacity(final Integer value)
	{
		_capacity = getPersistenceContext().setValue(CAPACITY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Stadium.code</code> attribute defined at extension <code>cuppytrail</code>. 
	 *  
	 * @param value the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		_code = getPersistenceContext().setValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Stadium.matches</code> attribute defined at extension <code>cuppytrail</code>. 
	 *  
	 * @param value the matches
	 */
	@Accessor(qualifier = "matches", type = Accessor.Type.SETTER)
	public void setMatches(final Collection<MatchModel> value)
	{
		_matches = getPersistenceContext().setValue(MATCHES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Stadium.StadiumType</code> attribute defined at extension <code>cuppytrail</code>. 
	 *  
	 * @param value the StadiumType
	 */
	@Accessor(qualifier = "StadiumType", type = Accessor.Type.SETTER)
	public void setStadiumType(final StadiumType value)
	{
		_StadiumType = getPersistenceContext().setValue(STADIUMTYPE, value);
	}
	
}

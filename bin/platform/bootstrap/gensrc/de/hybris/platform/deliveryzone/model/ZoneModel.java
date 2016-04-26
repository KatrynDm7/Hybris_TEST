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
package de.hybris.platform.deliveryzone.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Set;

/**
 * Generated model class for type Zone first defined at extension deliveryzone.
 */
@SuppressWarnings("all")
public class ZoneModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "Zone";
	
	/** <i>Generated constant</i> - Attribute key of <code>Zone.code</code> attribute defined at extension <code>deliveryzone</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>Zone.countries</code> attribute defined at extension <code>deliveryzone</code>. */
	public static final String COUNTRIES = "countries";
	
	
	/** <i>Generated variable</i> - Variable of <code>Zone.code</code> attribute defined at extension <code>deliveryzone</code>. */
	private String _code;
	
	/** <i>Generated variable</i> - Variable of <code>Zone.countries</code> attribute defined at extension <code>deliveryzone</code>. */
	private Set<CountryModel> _countries;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ZoneModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ZoneModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>Zone</code> at extension <code>deliveryzone</code>
	 */
	@Deprecated
	public ZoneModel(final String _code)
	{
		super();
		setCode(_code);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>Zone</code> at extension <code>deliveryzone</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public ZoneModel(final String _code, final ItemModel _owner)
	{
		super();
		setCode(_code);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Zone.code</code> attribute defined at extension <code>deliveryzone</code>. 
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
	 * <i>Generated method</i> - Getter of the <code>Zone.countries</code> attribute defined at extension <code>deliveryzone</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the countries
	 */
	@Accessor(qualifier = "countries", type = Accessor.Type.GETTER)
	public Set<CountryModel> getCountries()
	{
		if (this._countries!=null)
		{
			return _countries;
		}
		return _countries = getPersistenceContext().getValue(COUNTRIES, _countries);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Zone.code</code> attribute defined at extension <code>deliveryzone</code>. 
	 *  
	 * @param value the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		_code = getPersistenceContext().setValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Zone.countries</code> attribute defined at extension <code>deliveryzone</code>. 
	 *  
	 * @param value the countries
	 */
	@Accessor(qualifier = "countries", type = Accessor.Type.SETTER)
	public void setCountries(final Set<CountryModel> value)
	{
		_countries = getPersistenceContext().setValue(COUNTRIES, value);
	}
	
}

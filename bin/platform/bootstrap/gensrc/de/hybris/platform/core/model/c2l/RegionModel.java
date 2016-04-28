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
package de.hybris.platform.core.model.c2l;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.C2LItemModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type Region first defined at extension core.
 */
@SuppressWarnings("all")
public class RegionModel extends C2LItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "Region";
	
	/**<i>Generated relation code constant for relation <code>Country2RegionRelation</code> defining source attribute <code>country</code> in extension <code>core</code>.</i>*/
	public final static String _COUNTRY2REGIONRELATION = "Country2RegionRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>Region.country</code> attribute defined at extension <code>core</code>. */
	public static final String COUNTRY = "country";
	
	
	/** <i>Generated variable</i> - Variable of <code>Region.country</code> attribute defined at extension <code>core</code>. */
	private CountryModel _country;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public RegionModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public RegionModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _country initial attribute declared by type <code>Region</code> at extension <code>core</code>
	 * @param _isocode initial attribute declared by type <code>Region</code> at extension <code>core</code>
	 */
	@Deprecated
	public RegionModel(final CountryModel _country, final String _isocode)
	{
		super();
		setCountry(_country);
		setIsocode(_isocode);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _country initial attribute declared by type <code>Region</code> at extension <code>core</code>
	 * @param _isocode initial attribute declared by type <code>Region</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public RegionModel(final CountryModel _country, final String _isocode, final ItemModel _owner)
	{
		super();
		setCountry(_country);
		setIsocode(_isocode);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Region.country</code> attribute defined at extension <code>core</code>. 
	 * @return the country
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
	 * <i>Generated method</i> - Setter of <code>Region.country</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the country
	 */
	@Accessor(qualifier = "country", type = Accessor.Type.SETTER)
	public void setCountry(final CountryModel value)
	{
		_country = getPersistenceContext().setValue(COUNTRY, value);
	}
	
}

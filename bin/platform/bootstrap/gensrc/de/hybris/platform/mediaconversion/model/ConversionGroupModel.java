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
package de.hybris.platform.mediaconversion.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.mediaconversion.model.ConversionMediaFormatModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;
import java.util.Set;

/**
 * Generated model class for type ConversionGroup first defined at extension mediaconversion.
 */
@SuppressWarnings("all")
public class ConversionGroupModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "ConversionGroup";
	
	/** <i>Generated constant</i> - Attribute key of <code>ConversionGroup.code</code> attribute defined at extension <code>mediaconversion</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>ConversionGroup.name</code> attribute defined at extension <code>mediaconversion</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>ConversionGroup.supportedFormats</code> attribute defined at extension <code>mediaconversion</code>. */
	public static final String SUPPORTEDFORMATS = "supportedFormats";
	
	
	/** <i>Generated variable</i> - Variable of <code>ConversionGroup.code</code> attribute defined at extension <code>mediaconversion</code>. */
	private String _code;
	
	/** <i>Generated variable</i> - Variable of <code>ConversionGroup.supportedFormats</code> attribute defined at extension <code>mediaconversion</code>. */
	private Set<ConversionMediaFormatModel> _supportedFormats;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ConversionGroupModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ConversionGroupModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public ConversionGroupModel(final ItemModel _owner)
	{
		super();
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ConversionGroup.code</code> attribute defined at extension <code>mediaconversion</code>. 
	 * @return the code - Unique identifier
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
	 * <i>Generated method</i> - Getter of the <code>ConversionGroup.name</code> attribute defined at extension <code>mediaconversion</code>. 
	 * @return the name - Display name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getName(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>ConversionGroup.name</code> attribute defined at extension <code>mediaconversion</code>. 
	 * @param loc the value localization key 
	 * @return the name - Display name
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(NAME, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ConversionGroup.supportedFormats</code> attribute defined at extension <code>mediaconversion</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the supportedFormats - All supported formats, i.e. all formats to convert the original media to.
	 */
	@Accessor(qualifier = "supportedFormats", type = Accessor.Type.GETTER)
	public Set<ConversionMediaFormatModel> getSupportedFormats()
	{
		if (this._supportedFormats!=null)
		{
			return _supportedFormats;
		}
		return _supportedFormats = getPersistenceContext().getValue(SUPPORTEDFORMATS, _supportedFormats);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ConversionGroup.code</code> attribute defined at extension <code>mediaconversion</code>. 
	 *  
	 * @param value the code - Unique identifier
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		_code = getPersistenceContext().setValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ConversionGroup.name</code> attribute defined at extension <code>mediaconversion</code>. 
	 *  
	 * @param value the name - Display name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		setName(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>ConversionGroup.name</code> attribute defined at extension <code>mediaconversion</code>. 
	 *  
	 * @param value the name - Display name
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(NAME, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ConversionGroup.supportedFormats</code> attribute defined at extension <code>mediaconversion</code>. 
	 *  
	 * @param value the supportedFormats - All supported formats, i.e. all formats to convert the original media to.
	 */
	@Accessor(qualifier = "supportedFormats", type = Accessor.Type.SETTER)
	public void setSupportedFormats(final Set<ConversionMediaFormatModel> value)
	{
		_supportedFormats = getPersistenceContext().setValue(SUPPORTEDFORMATS, value);
	}
	
}

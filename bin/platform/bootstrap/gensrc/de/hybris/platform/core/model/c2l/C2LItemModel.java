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
package de.hybris.platform.core.model.c2l;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;

/**
 * Generated model class for type C2LItem first defined at extension core.
 */
@SuppressWarnings("all")
public class C2LItemModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "C2LItem";
	
	/** <i>Generated constant</i> - Attribute key of <code>C2LItem.active</code> attribute defined at extension <code>core</code>. */
	public static final String ACTIVE = "active";
	
	/** <i>Generated constant</i> - Attribute key of <code>C2LItem.isocode</code> attribute defined at extension <code>core</code>. */
	public static final String ISOCODE = "isocode";
	
	/** <i>Generated constant</i> - Attribute key of <code>C2LItem.name</code> attribute defined at extension <code>core</code>. */
	public static final String NAME = "name";
	
	
	/** <i>Generated variable</i> - Variable of <code>C2LItem.active</code> attribute defined at extension <code>core</code>. */
	private Boolean _active;
	
	/** <i>Generated variable</i> - Variable of <code>C2LItem.isocode</code> attribute defined at extension <code>core</code>. */
	private String _isocode;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public C2LItemModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public C2LItemModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _isocode initial attribute declared by type <code>C2LItem</code> at extension <code>core</code>
	 */
	@Deprecated
	public C2LItemModel(final String _isocode)
	{
		super();
		setIsocode(_isocode);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _isocode initial attribute declared by type <code>C2LItem</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public C2LItemModel(final String _isocode, final ItemModel _owner)
	{
		super();
		setIsocode(_isocode);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>C2LItem.active</code> attribute defined at extension <code>core</code>. 
	 * @return the active
	 */
	@Accessor(qualifier = "active", type = Accessor.Type.GETTER)
	public Boolean getActive()
	{
		if (this._active!=null)
		{
			return _active;
		}
		return _active = getPersistenceContext().getValue(ACTIVE, _active);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>C2LItem.isocode</code> attribute defined at extension <code>core</code>. 
	 * @return the isocode
	 */
	@Accessor(qualifier = "isocode", type = Accessor.Type.GETTER)
	public String getIsocode()
	{
		if (this._isocode!=null)
		{
			return _isocode;
		}
		return _isocode = getPersistenceContext().getValue(ISOCODE, _isocode);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>C2LItem.name</code> attribute defined at extension <code>core</code>. 
	 * @return the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getName(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>C2LItem.name</code> attribute defined at extension <code>core</code>. 
	 * @param loc the value localization key 
	 * @return the name
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(NAME, loc);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>C2LItem.active</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the active
	 */
	@Accessor(qualifier = "active", type = Accessor.Type.SETTER)
	public void setActive(final Boolean value)
	{
		_active = getPersistenceContext().setValue(ACTIVE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>C2LItem.isocode</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the isocode
	 */
	@Accessor(qualifier = "isocode", type = Accessor.Type.SETTER)
	public void setIsocode(final String value)
	{
		_isocode = getPersistenceContext().setValue(ISOCODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>C2LItem.name</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		setName(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>C2LItem.name</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the name
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(NAME, loc, value);
	}
	
}

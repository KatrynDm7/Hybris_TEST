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
package de.hybris.platform.mcc.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.mcc.enums.CockpitLinkArea;
import de.hybris.platform.mcc.model.AbstractLinkEntryModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type Divider first defined at extension mcc.
 */
@SuppressWarnings("all")
public class DividerModel extends AbstractLinkEntryModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "Divider";
	
	/** <i>Generated constant</i> - Attribute key of <code>Divider.height</code> attribute defined at extension <code>mcc</code>. */
	public static final String HEIGHT = "height";
	
	/** <i>Generated constant</i> - Attribute key of <code>Divider.spacer</code> attribute defined at extension <code>mcc</code>. */
	public static final String SPACER = "spacer";
	
	
	/** <i>Generated variable</i> - Variable of <code>Divider.height</code> attribute defined at extension <code>mcc</code>. */
	private Integer _height;
	
	/** <i>Generated variable</i> - Variable of <code>Divider.spacer</code> attribute defined at extension <code>mcc</code>. */
	private Boolean _spacer;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public DividerModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public DividerModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _area initial attribute declared by type <code>AbstractLinkEntry</code> at extension <code>mcc</code>
	 * @param _code initial attribute declared by type <code>AbstractLinkEntry</code> at extension <code>mcc</code>
	 */
	@Deprecated
	public DividerModel(final CockpitLinkArea _area, final String _code)
	{
		super();
		setArea(_area);
		setCode(_code);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _area initial attribute declared by type <code>AbstractLinkEntry</code> at extension <code>mcc</code>
	 * @param _code initial attribute declared by type <code>AbstractLinkEntry</code> at extension <code>mcc</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public DividerModel(final CockpitLinkArea _area, final String _code, final ItemModel _owner)
	{
		super();
		setArea(_area);
		setCode(_code);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Divider.height</code> attribute defined at extension <code>mcc</code>. 
	 * @return the height
	 */
	@Accessor(qualifier = "height", type = Accessor.Type.GETTER)
	public Integer getHeight()
	{
		if (this._height!=null)
		{
			return _height;
		}
		return _height = getPersistenceContext().getValue(HEIGHT, _height);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Divider.spacer</code> attribute defined at extension <code>mcc</code>. 
	 * @return the spacer - Defines if item is a spacer only or should render horizontal bar as well
	 */
	@Accessor(qualifier = "spacer", type = Accessor.Type.GETTER)
	public boolean isSpacer()
	{
		return toPrimitive( _spacer = getPersistenceContext().getValue(SPACER, _spacer));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Divider.height</code> attribute defined at extension <code>mcc</code>. 
	 *  
	 * @param value the height
	 */
	@Accessor(qualifier = "height", type = Accessor.Type.SETTER)
	public void setHeight(final Integer value)
	{
		_height = getPersistenceContext().setValue(HEIGHT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Divider.spacer</code> attribute defined at extension <code>mcc</code>. 
	 *  
	 * @param value the spacer - Defines if item is a spacer only or should render horizontal bar as well
	 */
	@Accessor(qualifier = "spacer", type = Accessor.Type.SETTER)
	public void setSpacer(final boolean value)
	{
		_spacer = getPersistenceContext().setValue(SPACER, toObject(value));
	}
	
}

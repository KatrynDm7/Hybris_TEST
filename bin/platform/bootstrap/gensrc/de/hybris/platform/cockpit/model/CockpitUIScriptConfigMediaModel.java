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
package de.hybris.platform.cockpit.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cockpit.model.CockpitUIConfigurationMediaModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type CockpitUIScriptConfigMedia first defined at extension cockpit.
 */
@SuppressWarnings("all")
public class CockpitUIScriptConfigMediaModel extends CockpitUIConfigurationMediaModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "CockpitUIScriptConfigMedia";
	
	/** <i>Generated constant</i> - Attribute key of <code>CockpitUIScriptConfigMedia.allowScriptEvaluation</code> attribute defined at extension <code>cockpit</code>. */
	public static final String ALLOWSCRIPTEVALUATION = "allowScriptEvaluation";
	
	
	/** <i>Generated variable</i> - Variable of <code>CockpitUIScriptConfigMedia.allowScriptEvaluation</code> attribute defined at extension <code>cockpit</code>. */
	private Boolean _allowScriptEvaluation;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CockpitUIScriptConfigMediaModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CockpitUIScriptConfigMediaModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>CatalogUnawareMedia</code> at extension <code>catalog</code>
	 * @param _code initial attribute declared by type <code>Media</code> at extension <code>core</code>
	 */
	@Deprecated
	public CockpitUIScriptConfigMediaModel(final CatalogVersionModel _catalogVersion, final String _code)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setCode(_code);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>CatalogUnawareMedia</code> at extension <code>catalog</code>
	 * @param _code initial attribute declared by type <code>Media</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public CockpitUIScriptConfigMediaModel(final CatalogVersionModel _catalogVersion, final String _code, final ItemModel _owner)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setCode(_code);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CockpitUIScriptConfigMedia.allowScriptEvaluation</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the allowScriptEvaluation
	 */
	@Accessor(qualifier = "allowScriptEvaluation", type = Accessor.Type.GETTER)
	public Boolean getAllowScriptEvaluation()
	{
		if (this._allowScriptEvaluation!=null)
		{
			return _allowScriptEvaluation;
		}
		return _allowScriptEvaluation = getPersistenceContext().getValue(ALLOWSCRIPTEVALUATION, _allowScriptEvaluation);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CockpitUIScriptConfigMedia.allowScriptEvaluation</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the allowScriptEvaluation
	 */
	@Accessor(qualifier = "allowScriptEvaluation", type = Accessor.Type.SETTER)
	public void setAllowScriptEvaluation(final Boolean value)
	{
		_allowScriptEvaluation = getPersistenceContext().setValue(ALLOWSCRIPTEVALUATION, value);
	}
	
}

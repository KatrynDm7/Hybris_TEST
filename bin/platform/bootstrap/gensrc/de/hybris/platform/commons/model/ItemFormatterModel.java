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
package de.hybris.platform.commons.model;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.commons.model.FormatterModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type ItemFormatter first defined at extension commons.
 */
@SuppressWarnings("all")
public class ItemFormatterModel extends FormatterModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "ItemFormatter";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ItemFormatterModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ItemFormatterModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>Formatter</code> at extension <code>catalog</code>
	 * @param _code initial attribute declared by type <code>Media</code> at extension <code>core</code>
	 * @param _outputMimeType initial attribute declared by type <code>Formatter</code> at extension <code>commons</code>
	 */
	@Deprecated
	public ItemFormatterModel(final CatalogVersionModel _catalogVersion, final String _code, final String _outputMimeType)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setCode(_code);
		setOutputMimeType(_outputMimeType);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>Formatter</code> at extension <code>catalog</code>
	 * @param _code initial attribute declared by type <code>Media</code> at extension <code>core</code>
	 * @param _outputMimeType initial attribute declared by type <code>Formatter</code> at extension <code>commons</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public ItemFormatterModel(final CatalogVersionModel _catalogVersion, final String _code, final String _outputMimeType, final ItemModel _owner)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setCode(_code);
		setOutputMimeType(_outputMimeType);
		setOwner(_owner);
	}
	
	
}

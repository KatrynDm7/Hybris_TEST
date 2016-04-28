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
package de.hybris.platform.core.model.util;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type DuplicateCatalogItemCodesView first defined at extension catalog.
 */
@SuppressWarnings("all")
public class DuplicateCatalogItemCodesViewModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "DuplicateCatalogItemCodesView";
	
	/** <i>Generated constant</i> - Attribute key of <code>DuplicateCatalogItemCodesView.typeCode</code> attribute defined at extension <code>catalog</code>. */
	public static final String TYPECODE = "typeCode";
	
	/** <i>Generated constant</i> - Attribute key of <code>DuplicateCatalogItemCodesView.code</code> attribute defined at extension <code>catalog</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>DuplicateCatalogItemCodesView.count</code> attribute defined at extension <code>catalog</code>. */
	public static final String COUNT = "count";
	
	/** <i>Generated constant</i> - Attribute key of <code>DuplicateCatalogItemCodesView.catalogVersion</code> attribute defined at extension <code>catalog</code>. */
	public static final String CATALOGVERSION = "catalogVersion";
	
	/** <i>Generated constant</i> - Attribute key of <code>DuplicateCatalogItemCodesView.cv</code> attribute defined at extension <code>catalog</code>. */
	public static final String CV = "cv";
	
	
	/** <i>Generated variable</i> - Variable of <code>DuplicateCatalogItemCodesView.typeCode</code> attribute defined at extension <code>catalog</code>. */
	private ComposedTypeModel _typeCode;
	
	/** <i>Generated variable</i> - Variable of <code>DuplicateCatalogItemCodesView.code</code> attribute defined at extension <code>catalog</code>. */
	private String _code;
	
	/** <i>Generated variable</i> - Variable of <code>DuplicateCatalogItemCodesView.count</code> attribute defined at extension <code>catalog</code>. */
	private Integer _count;
	
	/** <i>Generated variable</i> - Variable of <code>DuplicateCatalogItemCodesView.catalogVersion</code> attribute defined at extension <code>catalog</code>. */
	private CatalogVersionModel _catalogVersion;
	
	/** <i>Generated variable</i> - Variable of <code>DuplicateCatalogItemCodesView.cv</code> attribute defined at extension <code>catalog</code>. */
	private CatalogVersionModel _cv;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public DuplicateCatalogItemCodesViewModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public DuplicateCatalogItemCodesViewModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public DuplicateCatalogItemCodesViewModel(final ItemModel _owner)
	{
		super();
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DuplicateCatalogItemCodesView.catalogVersion</code> attribute defined at extension <code>catalog</code>. 
	 * @return the catalogVersion
	 */
	@Accessor(qualifier = "catalogVersion", type = Accessor.Type.GETTER)
	public CatalogVersionModel getCatalogVersion()
	{
		if (this._catalogVersion!=null)
		{
			return _catalogVersion;
		}
		return _catalogVersion = getPersistenceContext().getValue(CATALOGVERSION, _catalogVersion);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DuplicateCatalogItemCodesView.code</code> attribute defined at extension <code>catalog</code>. 
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
	 * <i>Generated method</i> - Getter of the <code>DuplicateCatalogItemCodesView.count</code> attribute defined at extension <code>catalog</code>. 
	 * @return the count
	 */
	@Accessor(qualifier = "count", type = Accessor.Type.GETTER)
	public Integer getCount()
	{
		if (this._count!=null)
		{
			return _count;
		}
		return _count = getPersistenceContext().getValue(COUNT, _count);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DuplicateCatalogItemCodesView.cv</code> attribute defined at extension <code>catalog</code>. 
	 * @return the cv
	 */
	@Accessor(qualifier = "cv", type = Accessor.Type.GETTER)
	public CatalogVersionModel getCv()
	{
		if (this._cv!=null)
		{
			return _cv;
		}
		return _cv = getPersistenceContext().getValue(CV, _cv);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DuplicateCatalogItemCodesView.typeCode</code> attribute defined at extension <code>catalog</code>. 
	 * @return the typeCode
	 */
	@Accessor(qualifier = "typeCode", type = Accessor.Type.GETTER)
	public ComposedTypeModel getTypeCode()
	{
		if (this._typeCode!=null)
		{
			return _typeCode;
		}
		return _typeCode = getPersistenceContext().getValue(TYPECODE, _typeCode);
	}
	
}

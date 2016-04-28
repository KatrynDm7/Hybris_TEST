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
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type CatalogOverview first defined at extension catalog.
 */
@SuppressWarnings("all")
public class CatalogOverviewModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "CatalogOverview";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogOverview.cat</code> attribute defined at extension <code>catalog</code>. */
	public static final String CAT = "cat";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogOverview.typeCode</code> attribute defined at extension <code>catalog</code>. */
	public static final String TYPECODE = "typeCode";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogOverview.amount</code> attribute defined at extension <code>catalog</code>. */
	public static final String AMOUNT = "amount";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogOverview.catalogversion</code> attribute defined at extension <code>catalog</code>. */
	public static final String CATALOGVERSION = "catalogversion";
	
	
	/** <i>Generated variable</i> - Variable of <code>CatalogOverview.cat</code> attribute defined at extension <code>catalog</code>. */
	private CatalogModel _cat;
	
	/** <i>Generated variable</i> - Variable of <code>CatalogOverview.typeCode</code> attribute defined at extension <code>catalog</code>. */
	private ComposedTypeModel _typeCode;
	
	/** <i>Generated variable</i> - Variable of <code>CatalogOverview.amount</code> attribute defined at extension <code>catalog</code>. */
	private Integer _amount;
	
	/** <i>Generated variable</i> - Variable of <code>CatalogOverview.catalogversion</code> attribute defined at extension <code>catalog</code>. */
	private CatalogVersionModel _catalogversion;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CatalogOverviewModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CatalogOverviewModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public CatalogOverviewModel(final ItemModel _owner)
	{
		super();
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogOverview.amount</code> attribute defined at extension <code>catalog</code>. 
	 * @return the amount
	 */
	@Accessor(qualifier = "amount", type = Accessor.Type.GETTER)
	public Integer getAmount()
	{
		if (this._amount!=null)
		{
			return _amount;
		}
		return _amount = getPersistenceContext().getValue(AMOUNT, _amount);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogOverview.cat</code> attribute defined at extension <code>catalog</code>. 
	 * @return the cat
	 */
	@Accessor(qualifier = "cat", type = Accessor.Type.GETTER)
	public CatalogModel getCat()
	{
		if (this._cat!=null)
		{
			return _cat;
		}
		return _cat = getPersistenceContext().getValue(CAT, _cat);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogOverview.catalogversion</code> attribute defined at extension <code>catalog</code>. 
	 * @return the catalogversion
	 */
	@Accessor(qualifier = "catalogversion", type = Accessor.Type.GETTER)
	public CatalogVersionModel getCatalogversion()
	{
		if (this._catalogversion!=null)
		{
			return _catalogversion;
		}
		return _catalogversion = getPersistenceContext().getValue(CATALOGVERSION, _catalogversion);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogOverview.typeCode</code> attribute defined at extension <code>catalog</code>. 
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

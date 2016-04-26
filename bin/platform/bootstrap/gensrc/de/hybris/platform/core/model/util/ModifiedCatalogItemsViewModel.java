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
package de.hybris.platform.core.model.util;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;

/**
 * Generated model class for type ModifiedCatalogItemsView first defined at extension catalog.
 */
@SuppressWarnings("all")
public class ModifiedCatalogItemsViewModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "ModifiedCatalogItemsView";
	
	/** <i>Generated constant</i> - Attribute key of <code>ModifiedCatalogItemsView.job</code> attribute defined at extension <code>catalog</code>. */
	public static final String JOB = "job";
	
	/** <i>Generated constant</i> - Attribute key of <code>ModifiedCatalogItemsView.sourceItem</code> attribute defined at extension <code>catalog</code>. */
	public static final String SOURCEITEM = "sourceItem";
	
	/** <i>Generated constant</i> - Attribute key of <code>ModifiedCatalogItemsView.targetItem</code> attribute defined at extension <code>catalog</code>. */
	public static final String TARGETITEM = "targetItem";
	
	/** <i>Generated constant</i> - Attribute key of <code>ModifiedCatalogItemsView.sourceModifiedTime</code> attribute defined at extension <code>catalog</code>. */
	public static final String SOURCEMODIFIEDTIME = "sourceModifiedTime";
	
	/** <i>Generated constant</i> - Attribute key of <code>ModifiedCatalogItemsView.lastSyncTime</code> attribute defined at extension <code>catalog</code>. */
	public static final String LASTSYNCTIME = "lastSyncTime";
	
	/** <i>Generated constant</i> - Attribute key of <code>ModifiedCatalogItemsView.typeCode</code> attribute defined at extension <code>catalog</code>. */
	public static final String TYPECODE = "typeCode";
	
	
	/** <i>Generated variable</i> - Variable of <code>ModifiedCatalogItemsView.job</code> attribute defined at extension <code>catalog</code>. */
	private SyncItemJobModel _job;
	
	/** <i>Generated variable</i> - Variable of <code>ModifiedCatalogItemsView.sourceItem</code> attribute defined at extension <code>catalog</code>. */
	private ItemModel _sourceItem;
	
	/** <i>Generated variable</i> - Variable of <code>ModifiedCatalogItemsView.targetItem</code> attribute defined at extension <code>catalog</code>. */
	private ItemModel _targetItem;
	
	/** <i>Generated variable</i> - Variable of <code>ModifiedCatalogItemsView.sourceModifiedTime</code> attribute defined at extension <code>catalog</code>. */
	private Date _sourceModifiedTime;
	
	/** <i>Generated variable</i> - Variable of <code>ModifiedCatalogItemsView.lastSyncTime</code> attribute defined at extension <code>catalog</code>. */
	private Date _lastSyncTime;
	
	/** <i>Generated variable</i> - Variable of <code>ModifiedCatalogItemsView.typeCode</code> attribute defined at extension <code>catalog</code>. */
	private String _typeCode;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ModifiedCatalogItemsViewModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ModifiedCatalogItemsViewModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public ModifiedCatalogItemsViewModel(final ItemModel _owner)
	{
		super();
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ModifiedCatalogItemsView.job</code> attribute defined at extension <code>catalog</code>. 
	 * @return the job
	 */
	@Accessor(qualifier = "job", type = Accessor.Type.GETTER)
	public SyncItemJobModel getJob()
	{
		if (this._job!=null)
		{
			return _job;
		}
		return _job = getPersistenceContext().getValue(JOB, _job);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ModifiedCatalogItemsView.lastSyncTime</code> attribute defined at extension <code>catalog</code>. 
	 * @return the lastSyncTime
	 */
	@Accessor(qualifier = "lastSyncTime", type = Accessor.Type.GETTER)
	public Date getLastSyncTime()
	{
		if (this._lastSyncTime!=null)
		{
			return _lastSyncTime;
		}
		return _lastSyncTime = getPersistenceContext().getValue(LASTSYNCTIME, _lastSyncTime);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ModifiedCatalogItemsView.sourceItem</code> attribute defined at extension <code>catalog</code>. 
	 * @return the sourceItem
	 */
	@Accessor(qualifier = "sourceItem", type = Accessor.Type.GETTER)
	public ItemModel getSourceItem()
	{
		if (this._sourceItem!=null)
		{
			return _sourceItem;
		}
		return _sourceItem = getPersistenceContext().getValue(SOURCEITEM, _sourceItem);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ModifiedCatalogItemsView.sourceModifiedTime</code> attribute defined at extension <code>catalog</code>. 
	 * @return the sourceModifiedTime
	 */
	@Accessor(qualifier = "sourceModifiedTime", type = Accessor.Type.GETTER)
	public Date getSourceModifiedTime()
	{
		if (this._sourceModifiedTime!=null)
		{
			return _sourceModifiedTime;
		}
		return _sourceModifiedTime = getPersistenceContext().getValue(SOURCEMODIFIEDTIME, _sourceModifiedTime);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ModifiedCatalogItemsView.targetItem</code> attribute defined at extension <code>catalog</code>. 
	 * @return the targetItem
	 */
	@Accessor(qualifier = "targetItem", type = Accessor.Type.GETTER)
	public ItemModel getTargetItem()
	{
		if (this._targetItem!=null)
		{
			return _targetItem;
		}
		return _targetItem = getPersistenceContext().getValue(TARGETITEM, _targetItem);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ModifiedCatalogItemsView.typeCode</code> attribute defined at extension <code>catalog</code>. 
	 * @return the typeCode
	 */
	@Accessor(qualifier = "typeCode", type = Accessor.Type.GETTER)
	public String getTypeCode()
	{
		if (this._typeCode!=null)
		{
			return _typeCode;
		}
		return _typeCode = getPersistenceContext().getValue(TYPECODE, _typeCode);
	}
	
}

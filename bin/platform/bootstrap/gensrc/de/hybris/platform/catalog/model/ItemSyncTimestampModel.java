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
package de.hybris.platform.catalog.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.SyncItemCronJobModel;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Date;

/**
 * Generated model class for type ItemSyncTimestamp first defined at extension catalog.
 */
@SuppressWarnings("all")
public class ItemSyncTimestampModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "ItemSyncTimestamp";
	
	/** <i>Generated constant</i> - Attribute key of <code>ItemSyncTimestamp.syncJob</code> attribute defined at extension <code>catalog</code>. */
	public static final String SYNCJOB = "syncJob";
	
	/** <i>Generated constant</i> - Attribute key of <code>ItemSyncTimestamp.sourceItem</code> attribute defined at extension <code>catalog</code>. */
	public static final String SOURCEITEM = "sourceItem";
	
	/** <i>Generated constant</i> - Attribute key of <code>ItemSyncTimestamp.targetItem</code> attribute defined at extension <code>catalog</code>. */
	public static final String TARGETITEM = "targetItem";
	
	/** <i>Generated constant</i> - Attribute key of <code>ItemSyncTimestamp.sourceVersion</code> attribute defined at extension <code>catalog</code>. */
	public static final String SOURCEVERSION = "sourceVersion";
	
	/** <i>Generated constant</i> - Attribute key of <code>ItemSyncTimestamp.targetVersion</code> attribute defined at extension <code>catalog</code>. */
	public static final String TARGETVERSION = "targetVersion";
	
	/** <i>Generated constant</i> - Attribute key of <code>ItemSyncTimestamp.lastSyncSourceModifiedTime</code> attribute defined at extension <code>catalog</code>. */
	public static final String LASTSYNCSOURCEMODIFIEDTIME = "lastSyncSourceModifiedTime";
	
	/** <i>Generated constant</i> - Attribute key of <code>ItemSyncTimestamp.lastSyncTime</code> attribute defined at extension <code>catalog</code>. */
	public static final String LASTSYNCTIME = "lastSyncTime";
	
	/** <i>Generated constant</i> - Attribute key of <code>ItemSyncTimestamp.pendingAttributesOwnerJob</code> attribute defined at extension <code>catalog</code>. */
	public static final String PENDINGATTRIBUTESOWNERJOB = "pendingAttributesOwnerJob";
	
	/** <i>Generated constant</i> - Attribute key of <code>ItemSyncTimestamp.pendingAttributesScheduledTurn</code> attribute defined at extension <code>catalog</code>. */
	public static final String PENDINGATTRIBUTESSCHEDULEDTURN = "pendingAttributesScheduledTurn";
	
	/** <i>Generated constant</i> - Attribute key of <code>ItemSyncTimestamp.pendingAttributeQualifiers</code> attribute defined at extension <code>catalog</code>. */
	public static final String PENDINGATTRIBUTEQUALIFIERS = "pendingAttributeQualifiers";
	
	/** <i>Generated constant</i> - Attribute key of <code>ItemSyncTimestamp.outdated</code> attribute defined at extension <code>catalog</code>. */
	public static final String OUTDATED = "outdated";
	
	/** <i>Generated constant</i> - Attribute key of <code>ItemSyncTimestamp.pendingAttributes</code> attribute defined at extension <code>catalog</code>. */
	public static final String PENDINGATTRIBUTES = "pendingAttributes";
	
	
	/** <i>Generated variable</i> - Variable of <code>ItemSyncTimestamp.syncJob</code> attribute defined at extension <code>catalog</code>. */
	private SyncItemJobModel _syncJob;
	
	/** <i>Generated variable</i> - Variable of <code>ItemSyncTimestamp.sourceItem</code> attribute defined at extension <code>catalog</code>. */
	private ItemModel _sourceItem;
	
	/** <i>Generated variable</i> - Variable of <code>ItemSyncTimestamp.targetItem</code> attribute defined at extension <code>catalog</code>. */
	private ItemModel _targetItem;
	
	/** <i>Generated variable</i> - Variable of <code>ItemSyncTimestamp.sourceVersion</code> attribute defined at extension <code>catalog</code>. */
	private CatalogVersionModel _sourceVersion;
	
	/** <i>Generated variable</i> - Variable of <code>ItemSyncTimestamp.targetVersion</code> attribute defined at extension <code>catalog</code>. */
	private CatalogVersionModel _targetVersion;
	
	/** <i>Generated variable</i> - Variable of <code>ItemSyncTimestamp.lastSyncSourceModifiedTime</code> attribute defined at extension <code>catalog</code>. */
	private Date _lastSyncSourceModifiedTime;
	
	/** <i>Generated variable</i> - Variable of <code>ItemSyncTimestamp.lastSyncTime</code> attribute defined at extension <code>catalog</code>. */
	private Date _lastSyncTime;
	
	/** <i>Generated variable</i> - Variable of <code>ItemSyncTimestamp.pendingAttributesOwnerJob</code> attribute defined at extension <code>catalog</code>. */
	private SyncItemCronJobModel _pendingAttributesOwnerJob;
	
	/** <i>Generated variable</i> - Variable of <code>ItemSyncTimestamp.pendingAttributesScheduledTurn</code> attribute defined at extension <code>catalog</code>. */
	private Integer _pendingAttributesScheduledTurn;
	
	/** <i>Generated variable</i> - Variable of <code>ItemSyncTimestamp.pendingAttributeQualifiers</code> attribute defined at extension <code>catalog</code>. */
	private String _pendingAttributeQualifiers;
	
	/** <i>Generated variable</i> - Variable of <code>ItemSyncTimestamp.outdated</code> attribute defined at extension <code>catalog</code>. */
	private Boolean _outdated;
	
	/** <i>Generated variable</i> - Variable of <code>ItemSyncTimestamp.pendingAttributes</code> attribute defined at extension <code>catalog</code>. */
	private Collection<AttributeDescriptorModel> _pendingAttributes;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ItemSyncTimestampModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ItemSyncTimestampModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _sourceItem initial attribute declared by type <code>ItemSyncTimestamp</code> at extension <code>catalog</code>
	 * @param _sourceVersion initial attribute declared by type <code>ItemSyncTimestamp</code> at extension <code>catalog</code>
	 * @param _syncJob initial attribute declared by type <code>ItemSyncTimestamp</code> at extension <code>catalog</code>
	 * @param _targetItem initial attribute declared by type <code>ItemSyncTimestamp</code> at extension <code>catalog</code>
	 * @param _targetVersion initial attribute declared by type <code>ItemSyncTimestamp</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public ItemSyncTimestampModel(final ItemModel _sourceItem, final CatalogVersionModel _sourceVersion, final SyncItemJobModel _syncJob, final ItemModel _targetItem, final CatalogVersionModel _targetVersion)
	{
		super();
		setSourceItem(_sourceItem);
		setSourceVersion(_sourceVersion);
		setSyncJob(_syncJob);
		setTargetItem(_targetItem);
		setTargetVersion(_targetVersion);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _sourceItem initial attribute declared by type <code>ItemSyncTimestamp</code> at extension <code>catalog</code>
	 * @param _sourceVersion initial attribute declared by type <code>ItemSyncTimestamp</code> at extension <code>catalog</code>
	 * @param _syncJob initial attribute declared by type <code>ItemSyncTimestamp</code> at extension <code>catalog</code>
	 * @param _targetItem initial attribute declared by type <code>ItemSyncTimestamp</code> at extension <code>catalog</code>
	 * @param _targetVersion initial attribute declared by type <code>ItemSyncTimestamp</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public ItemSyncTimestampModel(final ItemModel _owner, final ItemModel _sourceItem, final CatalogVersionModel _sourceVersion, final SyncItemJobModel _syncJob, final ItemModel _targetItem, final CatalogVersionModel _targetVersion)
	{
		super();
		setOwner(_owner);
		setSourceItem(_sourceItem);
		setSourceVersion(_sourceVersion);
		setSyncJob(_syncJob);
		setTargetItem(_targetItem);
		setTargetVersion(_targetVersion);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ItemSyncTimestamp.lastSyncSourceModifiedTime</code> attribute defined at extension <code>catalog</code>. 
	 * @return the lastSyncSourceModifiedTime
	 */
	@Accessor(qualifier = "lastSyncSourceModifiedTime", type = Accessor.Type.GETTER)
	public Date getLastSyncSourceModifiedTime()
	{
		if (this._lastSyncSourceModifiedTime!=null)
		{
			return _lastSyncSourceModifiedTime;
		}
		return _lastSyncSourceModifiedTime = getPersistenceContext().getValue(LASTSYNCSOURCEMODIFIEDTIME, _lastSyncSourceModifiedTime);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ItemSyncTimestamp.lastSyncTime</code> attribute defined at extension <code>catalog</code>. 
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
	 * <i>Generated method</i> - Getter of the <code>ItemSyncTimestamp.outdated</code> attribute defined at extension <code>catalog</code>. 
	 * @return the outdated
	 */
	@Accessor(qualifier = "outdated", type = Accessor.Type.GETTER)
	public Boolean getOutdated()
	{
		if (this._outdated!=null)
		{
			return _outdated;
		}
		return _outdated = getPersistenceContext().getValue(OUTDATED, _outdated);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ItemSyncTimestamp.pendingAttributeQualifiers</code> attribute defined at extension <code>catalog</code>. 
	 * @return the pendingAttributeQualifiers
	 */
	@Accessor(qualifier = "pendingAttributeQualifiers", type = Accessor.Type.GETTER)
	public String getPendingAttributeQualifiers()
	{
		if (this._pendingAttributeQualifiers!=null)
		{
			return _pendingAttributeQualifiers;
		}
		return _pendingAttributeQualifiers = getPersistenceContext().getValue(PENDINGATTRIBUTEQUALIFIERS, _pendingAttributeQualifiers);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ItemSyncTimestamp.pendingAttributes</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the pendingAttributes
	 */
	@Accessor(qualifier = "pendingAttributes", type = Accessor.Type.GETTER)
	public Collection<AttributeDescriptorModel> getPendingAttributes()
	{
		if (this._pendingAttributes!=null)
		{
			return _pendingAttributes;
		}
		return _pendingAttributes = getPersistenceContext().getValue(PENDINGATTRIBUTES, _pendingAttributes);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ItemSyncTimestamp.pendingAttributesOwnerJob</code> attribute defined at extension <code>catalog</code>. 
	 * @return the pendingAttributesOwnerJob
	 */
	@Accessor(qualifier = "pendingAttributesOwnerJob", type = Accessor.Type.GETTER)
	public SyncItemCronJobModel getPendingAttributesOwnerJob()
	{
		if (this._pendingAttributesOwnerJob!=null)
		{
			return _pendingAttributesOwnerJob;
		}
		return _pendingAttributesOwnerJob = getPersistenceContext().getValue(PENDINGATTRIBUTESOWNERJOB, _pendingAttributesOwnerJob);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ItemSyncTimestamp.pendingAttributesScheduledTurn</code> attribute defined at extension <code>catalog</code>. 
	 * @return the pendingAttributesScheduledTurn
	 */
	@Accessor(qualifier = "pendingAttributesScheduledTurn", type = Accessor.Type.GETTER)
	public Integer getPendingAttributesScheduledTurn()
	{
		if (this._pendingAttributesScheduledTurn!=null)
		{
			return _pendingAttributesScheduledTurn;
		}
		return _pendingAttributesScheduledTurn = getPersistenceContext().getValue(PENDINGATTRIBUTESSCHEDULEDTURN, _pendingAttributesScheduledTurn);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ItemSyncTimestamp.sourceItem</code> attribute defined at extension <code>catalog</code>. 
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
	 * <i>Generated method</i> - Getter of the <code>ItemSyncTimestamp.sourceVersion</code> attribute defined at extension <code>catalog</code>. 
	 * @return the sourceVersion
	 */
	@Accessor(qualifier = "sourceVersion", type = Accessor.Type.GETTER)
	public CatalogVersionModel getSourceVersion()
	{
		if (this._sourceVersion!=null)
		{
			return _sourceVersion;
		}
		return _sourceVersion = getPersistenceContext().getValue(SOURCEVERSION, _sourceVersion);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ItemSyncTimestamp.syncJob</code> attribute defined at extension <code>catalog</code>. 
	 * @return the syncJob
	 */
	@Accessor(qualifier = "syncJob", type = Accessor.Type.GETTER)
	public SyncItemJobModel getSyncJob()
	{
		if (this._syncJob!=null)
		{
			return _syncJob;
		}
		return _syncJob = getPersistenceContext().getValue(SYNCJOB, _syncJob);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ItemSyncTimestamp.targetItem</code> attribute defined at extension <code>catalog</code>. 
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
	 * <i>Generated method</i> - Getter of the <code>ItemSyncTimestamp.targetVersion</code> attribute defined at extension <code>catalog</code>. 
	 * @return the targetVersion
	 */
	@Accessor(qualifier = "targetVersion", type = Accessor.Type.GETTER)
	public CatalogVersionModel getTargetVersion()
	{
		if (this._targetVersion!=null)
		{
			return _targetVersion;
		}
		return _targetVersion = getPersistenceContext().getValue(TARGETVERSION, _targetVersion);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ItemSyncTimestamp.lastSyncSourceModifiedTime</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the lastSyncSourceModifiedTime
	 */
	@Accessor(qualifier = "lastSyncSourceModifiedTime", type = Accessor.Type.SETTER)
	public void setLastSyncSourceModifiedTime(final Date value)
	{
		_lastSyncSourceModifiedTime = getPersistenceContext().setValue(LASTSYNCSOURCEMODIFIEDTIME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ItemSyncTimestamp.lastSyncTime</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the lastSyncTime
	 */
	@Accessor(qualifier = "lastSyncTime", type = Accessor.Type.SETTER)
	public void setLastSyncTime(final Date value)
	{
		_lastSyncTime = getPersistenceContext().setValue(LASTSYNCTIME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ItemSyncTimestamp.pendingAttributeQualifiers</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the pendingAttributeQualifiers
	 */
	@Accessor(qualifier = "pendingAttributeQualifiers", type = Accessor.Type.SETTER)
	public void setPendingAttributeQualifiers(final String value)
	{
		_pendingAttributeQualifiers = getPersistenceContext().setValue(PENDINGATTRIBUTEQUALIFIERS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ItemSyncTimestamp.pendingAttributesOwnerJob</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the pendingAttributesOwnerJob
	 */
	@Accessor(qualifier = "pendingAttributesOwnerJob", type = Accessor.Type.SETTER)
	public void setPendingAttributesOwnerJob(final SyncItemCronJobModel value)
	{
		_pendingAttributesOwnerJob = getPersistenceContext().setValue(PENDINGATTRIBUTESOWNERJOB, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ItemSyncTimestamp.pendingAttributesScheduledTurn</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the pendingAttributesScheduledTurn
	 */
	@Accessor(qualifier = "pendingAttributesScheduledTurn", type = Accessor.Type.SETTER)
	public void setPendingAttributesScheduledTurn(final Integer value)
	{
		_pendingAttributesScheduledTurn = getPersistenceContext().setValue(PENDINGATTRIBUTESSCHEDULEDTURN, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>ItemSyncTimestamp.sourceItem</code> attribute defined at extension <code>catalog</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the sourceItem
	 */
	@Accessor(qualifier = "sourceItem", type = Accessor.Type.SETTER)
	public void setSourceItem(final ItemModel value)
	{
		_sourceItem = getPersistenceContext().setValue(SOURCEITEM, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>ItemSyncTimestamp.sourceVersion</code> attribute defined at extension <code>catalog</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the sourceVersion
	 */
	@Accessor(qualifier = "sourceVersion", type = Accessor.Type.SETTER)
	public void setSourceVersion(final CatalogVersionModel value)
	{
		_sourceVersion = getPersistenceContext().setValue(SOURCEVERSION, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>ItemSyncTimestamp.syncJob</code> attribute defined at extension <code>catalog</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the syncJob
	 */
	@Accessor(qualifier = "syncJob", type = Accessor.Type.SETTER)
	public void setSyncJob(final SyncItemJobModel value)
	{
		_syncJob = getPersistenceContext().setValue(SYNCJOB, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>ItemSyncTimestamp.targetItem</code> attribute defined at extension <code>catalog</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the targetItem
	 */
	@Accessor(qualifier = "targetItem", type = Accessor.Type.SETTER)
	public void setTargetItem(final ItemModel value)
	{
		_targetItem = getPersistenceContext().setValue(TARGETITEM, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>ItemSyncTimestamp.targetVersion</code> attribute defined at extension <code>catalog</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the targetVersion
	 */
	@Accessor(qualifier = "targetVersion", type = Accessor.Type.SETTER)
	public void setTargetVersion(final CatalogVersionModel value)
	{
		_targetVersion = getPersistenceContext().setValue(TARGETVERSION, value);
	}
	
}

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
package de.hybris.platform.catalog.model.synchronization;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.synchronization.CatalogVersionSyncCronJobModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type CatalogVersionSyncScheduleMedia first defined at extension catalog.
 */
@SuppressWarnings("all")
public class CatalogVersionSyncScheduleMediaModel extends MediaModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "CatalogVersionSyncScheduleMedia";
	
	/**<i>Generated relation code constant for relation <code>SyncJobScheduleMediaRelation</code> defining source attribute <code>cronjob</code> in extension <code>catalog</code>.</i>*/
	public final static String _SYNCJOBSCHEDULEMEDIARELATION = "SyncJobScheduleMediaRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersionSyncScheduleMedia.scheduledCount</code> attribute defined at extension <code>catalog</code>. */
	public static final String SCHEDULEDCOUNT = "scheduledCount";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersionSyncScheduleMedia.cronjob</code> attribute defined at extension <code>catalog</code>. */
	public static final String CRONJOB = "cronjob";
	
	
	/** <i>Generated variable</i> - Variable of <code>CatalogVersionSyncScheduleMedia.scheduledCount</code> attribute defined at extension <code>catalog</code>. */
	private Integer _scheduledCount;
	
	/** <i>Generated variable</i> - Variable of <code>CatalogVersionSyncScheduleMedia.cronjob</code> attribute defined at extension <code>catalog</code>. */
	private CatalogVersionSyncCronJobModel _cronjob;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CatalogVersionSyncScheduleMediaModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CatalogVersionSyncScheduleMediaModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>Media</code> at extension <code>catalog</code>
	 * @param _code initial attribute declared by type <code>Media</code> at extension <code>core</code>
	 * @param _cronjob initial attribute declared by type <code>CatalogVersionSyncScheduleMedia</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public CatalogVersionSyncScheduleMediaModel(final CatalogVersionModel _catalogVersion, final String _code, final CatalogVersionSyncCronJobModel _cronjob)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setCode(_code);
		setCronjob(_cronjob);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>Media</code> at extension <code>catalog</code>
	 * @param _code initial attribute declared by type <code>Media</code> at extension <code>core</code>
	 * @param _cronjob initial attribute declared by type <code>CatalogVersionSyncScheduleMedia</code> at extension <code>catalog</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public CatalogVersionSyncScheduleMediaModel(final CatalogVersionModel _catalogVersion, final String _code, final CatalogVersionSyncCronJobModel _cronjob, final ItemModel _owner)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setCode(_code);
		setCronjob(_cronjob);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersionSyncScheduleMedia.cronjob</code> attribute defined at extension <code>catalog</code>. 
	 * @return the cronjob
	 */
	@Accessor(qualifier = "cronjob", type = Accessor.Type.GETTER)
	public CatalogVersionSyncCronJobModel getCronjob()
	{
		if (this._cronjob!=null)
		{
			return _cronjob;
		}
		return _cronjob = getPersistenceContext().getValue(CRONJOB, _cronjob);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersionSyncScheduleMedia.scheduledCount</code> attribute defined at extension <code>catalog</code>. 
	 * @return the scheduledCount
	 */
	@Accessor(qualifier = "scheduledCount", type = Accessor.Type.GETTER)
	public Integer getScheduledCount()
	{
		if (this._scheduledCount!=null)
		{
			return _scheduledCount;
		}
		return _scheduledCount = getPersistenceContext().getValue(SCHEDULEDCOUNT, _scheduledCount);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>CatalogVersionSyncScheduleMedia.cronjob</code> attribute defined at extension <code>catalog</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the cronjob
	 */
	@Accessor(qualifier = "cronjob", type = Accessor.Type.SETTER)
	public void setCronjob(final CatalogVersionSyncCronJobModel value)
	{
		_cronjob = getPersistenceContext().setValue(CRONJOB, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersionSyncScheduleMedia.scheduledCount</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the scheduledCount
	 */
	@Accessor(qualifier = "scheduledCount", type = Accessor.Type.SETTER)
	public void setScheduledCount(final Integer value)
	{
		_scheduledCount = getPersistenceContext().setValue(SCHEDULEDCOUNT, value);
	}
	
}

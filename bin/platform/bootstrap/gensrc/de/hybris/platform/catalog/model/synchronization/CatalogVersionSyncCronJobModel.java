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
import de.hybris.platform.catalog.model.SyncItemCronJobModel;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.catalog.model.synchronization.CatalogVersionSyncScheduleMediaModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;

/**
 * Generated model class for type CatalogVersionSyncCronJob first defined at extension catalog.
 */
@SuppressWarnings("all")
public class CatalogVersionSyncCronJobModel extends SyncItemCronJobModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "CatalogVersionSyncCronJob";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersionSyncCronJob.statusMessage</code> attribute defined at extension <code>catalog</code>. */
	public static final String STATUSMESSAGE = "statusMessage";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersionSyncCronJob.scheduleMedias</code> attribute defined at extension <code>catalog</code>. */
	public static final String SCHEDULEMEDIAS = "scheduleMedias";
	
	
	/** <i>Generated variable</i> - Variable of <code>CatalogVersionSyncCronJob.statusMessage</code> attribute defined at extension <code>catalog</code>. */
	private String _statusMessage;
	
	/** <i>Generated variable</i> - Variable of <code>CatalogVersionSyncCronJob.scheduleMedias</code> attribute defined at extension <code>catalog</code>. */
	private List<CatalogVersionSyncScheduleMediaModel> _scheduleMedias;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CatalogVersionSyncCronJobModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CatalogVersionSyncCronJobModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _job initial attribute declared by type <code>SyncItemCronJob</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public CatalogVersionSyncCronJobModel(final SyncItemJobModel _job)
	{
		super();
		setJob(_job);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _job initial attribute declared by type <code>SyncItemCronJob</code> at extension <code>catalog</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public CatalogVersionSyncCronJobModel(final SyncItemJobModel _job, final ItemModel _owner)
	{
		super();
		setJob(_job);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersionSyncCronJob.scheduleMedias</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the scheduleMedias
	 */
	@Accessor(qualifier = "scheduleMedias", type = Accessor.Type.GETTER)
	public List<CatalogVersionSyncScheduleMediaModel> getScheduleMedias()
	{
		if (this._scheduleMedias!=null)
		{
			return _scheduleMedias;
		}
		return _scheduleMedias = getPersistenceContext().getValue(SCHEDULEMEDIAS, _scheduleMedias);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersionSyncCronJob.statusMessage</code> attribute defined at extension <code>catalog</code>. 
	 * @return the statusMessage
	 */
	@Accessor(qualifier = "statusMessage", type = Accessor.Type.GETTER)
	public String getStatusMessage()
	{
		if (this._statusMessage!=null)
		{
			return _statusMessage;
		}
		return _statusMessage = getPersistenceContext().getValue(STATUSMESSAGE, _statusMessage);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersionSyncCronJob.scheduleMedias</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the scheduleMedias
	 */
	@Accessor(qualifier = "scheduleMedias", type = Accessor.Type.SETTER)
	public void setScheduleMedias(final List<CatalogVersionSyncScheduleMediaModel> value)
	{
		_scheduleMedias = getPersistenceContext().setValue(SCHEDULEMEDIAS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersionSyncCronJob.statusMessage</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the statusMessage
	 */
	@Accessor(qualifier = "statusMessage", type = Accessor.Type.SETTER)
	public void setStatusMessage(final String value)
	{
		_statusMessage = getPersistenceContext().setValue(STATUSMESSAGE, value);
	}
	
}

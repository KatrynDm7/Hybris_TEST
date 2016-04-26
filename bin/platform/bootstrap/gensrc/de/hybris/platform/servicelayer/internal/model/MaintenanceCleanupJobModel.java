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
package de.hybris.platform.servicelayer.internal.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.internal.model.ServicelayerJobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type MaintenanceCleanupJob first defined at extension processing.
 */
@SuppressWarnings("all")
public class MaintenanceCleanupJobModel extends ServicelayerJobModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "MaintenanceCleanupJob";
	
	/** <i>Generated constant</i> - Attribute key of <code>MaintenanceCleanupJob.threshold</code> attribute defined at extension <code>processing</code>. */
	public static final String THRESHOLD = "threshold";
	
	/** <i>Generated constant</i> - Attribute key of <code>MaintenanceCleanupJob.searchType</code> attribute defined at extension <code>processing</code>. */
	public static final String SEARCHTYPE = "searchType";
	
	
	/** <i>Generated variable</i> - Variable of <code>MaintenanceCleanupJob.threshold</code> attribute defined at extension <code>processing</code>. */
	private Integer _threshold;
	
	/** <i>Generated variable</i> - Variable of <code>MaintenanceCleanupJob.searchType</code> attribute defined at extension <code>processing</code>. */
	private ComposedTypeModel _searchType;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public MaintenanceCleanupJobModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public MaintenanceCleanupJobModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>Job</code> at extension <code>processing</code>
	 * @param _springId initial attribute declared by type <code>ServicelayerJob</code> at extension <code>processing</code>
	 */
	@Deprecated
	public MaintenanceCleanupJobModel(final String _code, final String _springId)
	{
		super();
		setCode(_code);
		setSpringId(_springId);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>Job</code> at extension <code>processing</code>
	 * @param _nodeID initial attribute declared by type <code>Job</code> at extension <code>processing</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _springId initial attribute declared by type <code>ServicelayerJob</code> at extension <code>processing</code>
	 */
	@Deprecated
	public MaintenanceCleanupJobModel(final String _code, final Integer _nodeID, final ItemModel _owner, final String _springId)
	{
		super();
		setCode(_code);
		setNodeID(_nodeID);
		setOwner(_owner);
		setSpringId(_springId);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MaintenanceCleanupJob.searchType</code> attribute defined at extension <code>processing</code>. 
	 * @return the searchType - can be used in the search part to search for this given type only
	 */
	@Accessor(qualifier = "searchType", type = Accessor.Type.GETTER)
	public ComposedTypeModel getSearchType()
	{
		if (this._searchType!=null)
		{
			return _searchType;
		}
		return _searchType = getPersistenceContext().getValue(SEARCHTYPE, _searchType);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MaintenanceCleanupJob.threshold</code> attribute defined at extension <code>processing</code>. 
	 * @return the threshold - Depending on the implemented Maintenance strategy this threeshold value can be uses as
	 *                         'days old' or as max/min value ...
	 */
	@Accessor(qualifier = "threshold", type = Accessor.Type.GETTER)
	public Integer getThreshold()
	{
		if (this._threshold!=null)
		{
			return _threshold;
		}
		return _threshold = getPersistenceContext().getValue(THRESHOLD, _threshold);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MaintenanceCleanupJob.searchType</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the searchType - can be used in the search part to search for this given type only
	 */
	@Accessor(qualifier = "searchType", type = Accessor.Type.SETTER)
	public void setSearchType(final ComposedTypeModel value)
	{
		_searchType = getPersistenceContext().setValue(SEARCHTYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MaintenanceCleanupJob.threshold</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the threshold - Depending on the implemented Maintenance strategy this threeshold value can be uses as
	 *                         'days old' or as max/min value ...
	 */
	@Accessor(qualifier = "threshold", type = Accessor.Type.SETTER)
	public void setThreshold(final Integer value)
	{
		_threshold = getPersistenceContext().setValue(THRESHOLD, value);
	}
	
}

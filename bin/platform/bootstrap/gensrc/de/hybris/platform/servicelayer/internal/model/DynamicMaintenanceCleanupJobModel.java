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
import de.hybris.platform.servicelayer.internal.model.MaintenanceCleanupJobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type DynamicMaintenanceCleanupJob first defined at extension processing.
 */
@SuppressWarnings("all")
public class DynamicMaintenanceCleanupJobModel extends MaintenanceCleanupJobModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "DynamicMaintenanceCleanupJob";
	
	/** <i>Generated constant</i> - Attribute key of <code>DynamicMaintenanceCleanupJob.searchScript</code> attribute defined at extension <code>processing</code>. */
	public static final String SEARCHSCRIPT = "searchScript";
	
	/** <i>Generated constant</i> - Attribute key of <code>DynamicMaintenanceCleanupJob.processScript</code> attribute defined at extension <code>processing</code>. */
	public static final String PROCESSSCRIPT = "processScript";
	
	
	/** <i>Generated variable</i> - Variable of <code>DynamicMaintenanceCleanupJob.searchScript</code> attribute defined at extension <code>processing</code>. */
	private String _searchScript;
	
	/** <i>Generated variable</i> - Variable of <code>DynamicMaintenanceCleanupJob.processScript</code> attribute defined at extension <code>processing</code>. */
	private String _processScript;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public DynamicMaintenanceCleanupJobModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public DynamicMaintenanceCleanupJobModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>Job</code> at extension <code>processing</code>
	 * @param _springId initial attribute declared by type <code>DynamicMaintenanceCleanupJob</code> at extension <code>processing</code>
	 */
	@Deprecated
	public DynamicMaintenanceCleanupJobModel(final String _code, final String _springId)
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
	 * @param _springId initial attribute declared by type <code>DynamicMaintenanceCleanupJob</code> at extension <code>processing</code>
	 */
	@Deprecated
	public DynamicMaintenanceCleanupJobModel(final String _code, final Integer _nodeID, final ItemModel _owner, final String _springId)
	{
		super();
		setCode(_code);
		setNodeID(_nodeID);
		setOwner(_owner);
		setSpringId(_springId);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DynamicMaintenanceCleanupJob.processScript</code> attribute defined at extension <code>processing</code>. 
	 * @return the processScript
	 */
	@Accessor(qualifier = "processScript", type = Accessor.Type.GETTER)
	public String getProcessScript()
	{
		if (this._processScript!=null)
		{
			return _processScript;
		}
		return _processScript = getPersistenceContext().getValue(PROCESSSCRIPT, _processScript);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DynamicMaintenanceCleanupJob.searchScript</code> attribute defined at extension <code>processing</code>. 
	 * @return the searchScript
	 */
	@Accessor(qualifier = "searchScript", type = Accessor.Type.GETTER)
	public String getSearchScript()
	{
		if (this._searchScript!=null)
		{
			return _searchScript;
		}
		return _searchScript = getPersistenceContext().getValue(SEARCHSCRIPT, _searchScript);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DynamicMaintenanceCleanupJob.processScript</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the processScript
	 */
	@Accessor(qualifier = "processScript", type = Accessor.Type.SETTER)
	public void setProcessScript(final String value)
	{
		_processScript = getPersistenceContext().setValue(PROCESSSCRIPT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DynamicMaintenanceCleanupJob.searchScript</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the searchScript
	 */
	@Accessor(qualifier = "searchScript", type = Accessor.Type.SETTER)
	public void setSearchScript(final String value)
	{
		_searchScript = getPersistenceContext().setValue(SEARCHSCRIPT, value);
	}
	
}

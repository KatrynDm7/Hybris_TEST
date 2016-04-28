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
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.catalog.model.synchronization.CatalogVersionSyncJobModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Set;

/**
 * Generated model class for type CatalogVersionSyncJob first defined at extension catalog.
 */
@SuppressWarnings("all")
public class CatalogVersionSyncJobModel extends SyncItemJobModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "CatalogVersionSyncJob";
	
	/**<i>Generated relation code constant for relation <code>DependentCatalogVersionSyncJobRelation</code> defining source attribute <code>dependentSyncJobs</code> in extension <code>catalog</code>.</i>*/
	public final static String _DEPENDENTCATALOGVERSIONSYNCJOBRELATION = "DependentCatalogVersionSyncJobRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersionSyncJob.copyCacheSize</code> attribute defined at extension <code>catalog</code>. */
	public static final String COPYCACHESIZE = "copyCacheSize";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersionSyncJob.enableTransactions</code> attribute defined at extension <code>catalog</code>. */
	public static final String ENABLETRANSACTIONS = "enableTransactions";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersionSyncJob.maxThreads</code> attribute defined at extension <code>catalog</code>. */
	public static final String MAXTHREADS = "maxThreads";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersionSyncJob.maxSchedulerThreads</code> attribute defined at extension <code>catalog</code>. */
	public static final String MAXSCHEDULERTHREADS = "maxSchedulerThreads";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersionSyncJob.dependentSyncJobs</code> attribute defined at extension <code>catalog</code>. */
	public static final String DEPENDENTSYNCJOBS = "dependentSyncJobs";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersionSyncJob.dependsOnSyncJobs</code> attribute defined at extension <code>catalog</code>. */
	public static final String DEPENDSONSYNCJOBS = "dependsOnSyncJobs";
	
	
	/** <i>Generated variable</i> - Variable of <code>CatalogVersionSyncJob.copyCacheSize</code> attribute defined at extension <code>catalog</code>. */
	private Integer _copyCacheSize;
	
	/** <i>Generated variable</i> - Variable of <code>CatalogVersionSyncJob.enableTransactions</code> attribute defined at extension <code>catalog</code>. */
	private Boolean _enableTransactions;
	
	/** <i>Generated variable</i> - Variable of <code>CatalogVersionSyncJob.maxThreads</code> attribute defined at extension <code>catalog</code>. */
	private Integer _maxThreads;
	
	/** <i>Generated variable</i> - Variable of <code>CatalogVersionSyncJob.maxSchedulerThreads</code> attribute defined at extension <code>catalog</code>. */
	private Integer _maxSchedulerThreads;
	
	/** <i>Generated variable</i> - Variable of <code>CatalogVersionSyncJob.dependentSyncJobs</code> attribute defined at extension <code>catalog</code>. */
	private Set<CatalogVersionSyncJobModel> _dependentSyncJobs;
	
	/** <i>Generated variable</i> - Variable of <code>CatalogVersionSyncJob.dependsOnSyncJobs</code> attribute defined at extension <code>catalog</code>. */
	private Set<CatalogVersionSyncJobModel> _dependsOnSyncJobs;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CatalogVersionSyncJobModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CatalogVersionSyncJobModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>SyncItemJob</code> at extension <code>catalog</code>
	 * @param _sourceVersion initial attribute declared by type <code>SyncItemJob</code> at extension <code>catalog</code>
	 * @param _targetVersion initial attribute declared by type <code>SyncItemJob</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public CatalogVersionSyncJobModel(final String _code, final CatalogVersionModel _sourceVersion, final CatalogVersionModel _targetVersion)
	{
		super();
		setCode(_code);
		setSourceVersion(_sourceVersion);
		setTargetVersion(_targetVersion);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>SyncItemJob</code> at extension <code>catalog</code>
	 * @param _nodeID initial attribute declared by type <code>Job</code> at extension <code>processing</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _sourceVersion initial attribute declared by type <code>SyncItemJob</code> at extension <code>catalog</code>
	 * @param _targetVersion initial attribute declared by type <code>SyncItemJob</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public CatalogVersionSyncJobModel(final String _code, final Integer _nodeID, final ItemModel _owner, final CatalogVersionModel _sourceVersion, final CatalogVersionModel _targetVersion)
	{
		super();
		setCode(_code);
		setNodeID(_nodeID);
		setOwner(_owner);
		setSourceVersion(_sourceVersion);
		setTargetVersion(_targetVersion);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersionSyncJob.copyCacheSize</code> attribute defined at extension <code>catalog</code>. 
	 * @return the copyCacheSize
	 */
	@Accessor(qualifier = "copyCacheSize", type = Accessor.Type.GETTER)
	public Integer getCopyCacheSize()
	{
		if (this._copyCacheSize!=null)
		{
			return _copyCacheSize;
		}
		return _copyCacheSize = getPersistenceContext().getValue(COPYCACHESIZE, _copyCacheSize);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersionSyncJob.dependentSyncJobs</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the dependentSyncJobs
	 */
	@Accessor(qualifier = "dependentSyncJobs", type = Accessor.Type.GETTER)
	public Set<CatalogVersionSyncJobModel> getDependentSyncJobs()
	{
		if (this._dependentSyncJobs!=null)
		{
			return _dependentSyncJobs;
		}
		return _dependentSyncJobs = getPersistenceContext().getValue(DEPENDENTSYNCJOBS, _dependentSyncJobs);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersionSyncJob.dependsOnSyncJobs</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the dependsOnSyncJobs
	 */
	@Accessor(qualifier = "dependsOnSyncJobs", type = Accessor.Type.GETTER)
	public Set<CatalogVersionSyncJobModel> getDependsOnSyncJobs()
	{
		if (this._dependsOnSyncJobs!=null)
		{
			return _dependsOnSyncJobs;
		}
		return _dependsOnSyncJobs = getPersistenceContext().getValue(DEPENDSONSYNCJOBS, _dependsOnSyncJobs);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersionSyncJob.enableTransactions</code> attribute defined at extension <code>catalog</code>. 
	 * @return the enableTransactions
	 */
	@Accessor(qualifier = "enableTransactions", type = Accessor.Type.GETTER)
	public Boolean getEnableTransactions()
	{
		if (this._enableTransactions!=null)
		{
			return _enableTransactions;
		}
		return _enableTransactions = getPersistenceContext().getValue(ENABLETRANSACTIONS, _enableTransactions);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersionSyncJob.maxSchedulerThreads</code> attribute defined at extension <code>catalog</code>. 
	 * @return the maxSchedulerThreads
	 */
	@Accessor(qualifier = "maxSchedulerThreads", type = Accessor.Type.GETTER)
	public Integer getMaxSchedulerThreads()
	{
		if (this._maxSchedulerThreads!=null)
		{
			return _maxSchedulerThreads;
		}
		return _maxSchedulerThreads = getPersistenceContext().getValue(MAXSCHEDULERTHREADS, _maxSchedulerThreads);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersionSyncJob.maxThreads</code> attribute defined at extension <code>catalog</code>. 
	 * @return the maxThreads
	 */
	@Accessor(qualifier = "maxThreads", type = Accessor.Type.GETTER)
	public Integer getMaxThreads()
	{
		if (this._maxThreads!=null)
		{
			return _maxThreads;
		}
		return _maxThreads = getPersistenceContext().getValue(MAXTHREADS, _maxThreads);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersionSyncJob.copyCacheSize</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the copyCacheSize
	 */
	@Accessor(qualifier = "copyCacheSize", type = Accessor.Type.SETTER)
	public void setCopyCacheSize(final Integer value)
	{
		_copyCacheSize = getPersistenceContext().setValue(COPYCACHESIZE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersionSyncJob.dependentSyncJobs</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the dependentSyncJobs
	 */
	@Accessor(qualifier = "dependentSyncJobs", type = Accessor.Type.SETTER)
	public void setDependentSyncJobs(final Set<CatalogVersionSyncJobModel> value)
	{
		_dependentSyncJobs = getPersistenceContext().setValue(DEPENDENTSYNCJOBS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersionSyncJob.dependsOnSyncJobs</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the dependsOnSyncJobs
	 */
	@Accessor(qualifier = "dependsOnSyncJobs", type = Accessor.Type.SETTER)
	public void setDependsOnSyncJobs(final Set<CatalogVersionSyncJobModel> value)
	{
		_dependsOnSyncJobs = getPersistenceContext().setValue(DEPENDSONSYNCJOBS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersionSyncJob.enableTransactions</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the enableTransactions
	 */
	@Accessor(qualifier = "enableTransactions", type = Accessor.Type.SETTER)
	public void setEnableTransactions(final Boolean value)
	{
		_enableTransactions = getPersistenceContext().setValue(ENABLETRANSACTIONS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersionSyncJob.maxSchedulerThreads</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the maxSchedulerThreads
	 */
	@Accessor(qualifier = "maxSchedulerThreads", type = Accessor.Type.SETTER)
	public void setMaxSchedulerThreads(final Integer value)
	{
		_maxSchedulerThreads = getPersistenceContext().setValue(MAXSCHEDULERTHREADS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersionSyncJob.maxThreads</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the maxThreads
	 */
	@Accessor(qualifier = "maxThreads", type = Accessor.Type.SETTER)
	public void setMaxThreads(final Integer value)
	{
		_maxThreads = getPersistenceContext().setValue(MAXTHREADS, value);
	}
	
}

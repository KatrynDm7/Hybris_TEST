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
package de.hybris.platform.mediaconversion.model.job;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type AbstractMediaCronJob first defined at extension mediaconversion.
 */
@SuppressWarnings("all")
public class AbstractMediaCronJobModel extends CronJobModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "AbstractMediaCronJob";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractMediaCronJob.maxThreads</code> attribute defined at extension <code>mediaconversion</code>. */
	public static final String MAXTHREADS = "maxThreads";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractMediaCronJob.catalogVersion</code> attribute defined at extension <code>mediaconversion</code>. */
	public static final String CATALOGVERSION = "catalogVersion";
	
	
	/** <i>Generated variable</i> - Variable of <code>AbstractMediaCronJob.maxThreads</code> attribute defined at extension <code>mediaconversion</code>. */
	private Integer _maxThreads;
	
	/** <i>Generated variable</i> - Variable of <code>AbstractMediaCronJob.catalogVersion</code> attribute defined at extension <code>mediaconversion</code>. */
	private CatalogVersionModel _catalogVersion;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public AbstractMediaCronJobModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public AbstractMediaCronJobModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _job initial attribute declared by type <code>CronJob</code> at extension <code>processing</code>
	 * @param _maxThreads initial attribute declared by type <code>AbstractMediaCronJob</code> at extension <code>mediaconversion</code>
	 */
	@Deprecated
	public AbstractMediaCronJobModel(final JobModel _job, final int _maxThreads)
	{
		super();
		setJob(_job);
		setMaxThreads(_maxThreads);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _job initial attribute declared by type <code>CronJob</code> at extension <code>processing</code>
	 * @param _maxThreads initial attribute declared by type <code>AbstractMediaCronJob</code> at extension <code>mediaconversion</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public AbstractMediaCronJobModel(final JobModel _job, final int _maxThreads, final ItemModel _owner)
	{
		super();
		setJob(_job);
		setMaxThreads(_maxThreads);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractMediaCronJob.catalogVersion</code> attribute defined at extension <code>mediaconversion</code>. 
	 * @return the catalogVersion - Optional catalog version to work on.
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
	 * <i>Generated method</i> - Getter of the <code>AbstractMediaCronJob.maxThreads</code> attribute defined at extension <code>mediaconversion</code>. 
	 * @return the maxThreads - Amount of threads to use.
	 */
	@Accessor(qualifier = "maxThreads", type = Accessor.Type.GETTER)
	public int getMaxThreads()
	{
		return toPrimitive( _maxThreads = getPersistenceContext().getValue(MAXTHREADS, _maxThreads));
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractMediaCronJob.catalogVersion</code> attribute defined at extension <code>mediaconversion</code>. 
	 *  
	 * @param value the catalogVersion - Optional catalog version to work on.
	 */
	@Accessor(qualifier = "catalogVersion", type = Accessor.Type.SETTER)
	public void setCatalogVersion(final CatalogVersionModel value)
	{
		_catalogVersion = getPersistenceContext().setValue(CATALOGVERSION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractMediaCronJob.maxThreads</code> attribute defined at extension <code>mediaconversion</code>. 
	 *  
	 * @param value the maxThreads - Amount of threads to use.
	 */
	@Accessor(qualifier = "maxThreads", type = Accessor.Type.SETTER)
	public void setMaxThreads(final int value)
	{
		_maxThreads = getPersistenceContext().setValue(MAXTHREADS, toObject(value));
	}
	
}

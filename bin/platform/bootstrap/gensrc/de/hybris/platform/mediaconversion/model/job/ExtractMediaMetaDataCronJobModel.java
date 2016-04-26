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
package de.hybris.platform.mediaconversion.model.job;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.mediaconversion.model.job.AbstractMediaCronJobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type ExtractMediaMetaDataCronJob first defined at extension mediaconversion.
 */
@SuppressWarnings("all")
public class ExtractMediaMetaDataCronJobModel extends AbstractMediaCronJobModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "ExtractMediaMetaDataCronJob";
	
	/** <i>Generated constant</i> - Attribute key of <code>ExtractMediaMetaDataCronJob.includeConverted</code> attribute defined at extension <code>mediaconversion</code>. */
	public static final String INCLUDECONVERTED = "includeConverted";
	
	/** <i>Generated constant</i> - Attribute key of <code>ExtractMediaMetaDataCronJob.containerMediasOnly</code> attribute defined at extension <code>mediaconversion</code>. */
	public static final String CONTAINERMEDIASONLY = "containerMediasOnly";
	
	
	/** <i>Generated variable</i> - Variable of <code>ExtractMediaMetaDataCronJob.includeConverted</code> attribute defined at extension <code>mediaconversion</code>. */
	private Boolean _includeConverted;
	
	/** <i>Generated variable</i> - Variable of <code>ExtractMediaMetaDataCronJob.containerMediasOnly</code> attribute defined at extension <code>mediaconversion</code>. */
	private Boolean _containerMediasOnly;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ExtractMediaMetaDataCronJobModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ExtractMediaMetaDataCronJobModel(final ItemModelContext ctx)
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
	public ExtractMediaMetaDataCronJobModel(final JobModel _job, final int _maxThreads)
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
	public ExtractMediaMetaDataCronJobModel(final JobModel _job, final int _maxThreads, final ItemModel _owner)
	{
		super();
		setJob(_job);
		setMaxThreads(_maxThreads);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ExtractMediaMetaDataCronJob.containerMediasOnly</code> attribute defined at extension <code>mediaconversion</code>. 
	 * @return the containerMediasOnly - Whether only media which reside in media container should be obeyed.
	 */
	@Accessor(qualifier = "containerMediasOnly", type = Accessor.Type.GETTER)
	public Boolean getContainerMediasOnly()
	{
		if (this._containerMediasOnly!=null)
		{
			return _containerMediasOnly;
		}
		return _containerMediasOnly = getPersistenceContext().getValue(CONTAINERMEDIASONLY, _containerMediasOnly);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ExtractMediaMetaDataCronJob.includeConverted</code> attribute defined at extension <code>mediaconversion</code>. 
	 * @return the includeConverted - Whether converted media should also be obeyed.
	 */
	@Accessor(qualifier = "includeConverted", type = Accessor.Type.GETTER)
	public Boolean getIncludeConverted()
	{
		if (this._includeConverted!=null)
		{
			return _includeConverted;
		}
		return _includeConverted = getPersistenceContext().getValue(INCLUDECONVERTED, _includeConverted);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ExtractMediaMetaDataCronJob.containerMediasOnly</code> attribute defined at extension <code>mediaconversion</code>. 
	 *  
	 * @param value the containerMediasOnly - Whether only media which reside in media container should be obeyed.
	 */
	@Accessor(qualifier = "containerMediasOnly", type = Accessor.Type.SETTER)
	public void setContainerMediasOnly(final Boolean value)
	{
		_containerMediasOnly = getPersistenceContext().setValue(CONTAINERMEDIASONLY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ExtractMediaMetaDataCronJob.includeConverted</code> attribute defined at extension <code>mediaconversion</code>. 
	 *  
	 * @param value the includeConverted - Whether converted media should also be obeyed.
	 */
	@Accessor(qualifier = "includeConverted", type = Accessor.Type.SETTER)
	public void setIncludeConverted(final Boolean value)
	{
		_includeConverted = getPersistenceContext().setValue(INCLUDECONVERTED, value);
	}
	
}

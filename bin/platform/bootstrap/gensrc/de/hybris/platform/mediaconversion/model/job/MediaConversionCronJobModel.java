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
import de.hybris.platform.mediaconversion.model.ConversionMediaFormatModel;
import de.hybris.platform.mediaconversion.model.job.AbstractMediaCronJobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

/**
 * Generated model class for type MediaConversionCronJob first defined at extension mediaconversion.
 */
@SuppressWarnings("all")
public class MediaConversionCronJobModel extends AbstractMediaCronJobModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "MediaConversionCronJob";
	
	/** <i>Generated constant</i> - Attribute key of <code>MediaConversionCronJob.includedFormats</code> attribute defined at extension <code>mediaconversion</code>. */
	public static final String INCLUDEDFORMATS = "includedFormats";
	
	/** <i>Generated constant</i> - Attribute key of <code>MediaConversionCronJob.asynchronous</code> attribute defined at extension <code>mediaconversion</code>. */
	public static final String ASYNCHRONOUS = "asynchronous";
	
	
	/** <i>Generated variable</i> - Variable of <code>MediaConversionCronJob.includedFormats</code> attribute defined at extension <code>mediaconversion</code>. */
	private Collection<ConversionMediaFormatModel> _includedFormats;
	
	/** <i>Generated variable</i> - Variable of <code>MediaConversionCronJob.asynchronous</code> attribute defined at extension <code>mediaconversion</code>. */
	private Boolean _asynchronous;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public MediaConversionCronJobModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public MediaConversionCronJobModel(final ItemModelContext ctx)
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
	public MediaConversionCronJobModel(final JobModel _job, final int _maxThreads)
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
	public MediaConversionCronJobModel(final JobModel _job, final int _maxThreads, final ItemModel _owner)
	{
		super();
		setJob(_job);
		setMaxThreads(_maxThreads);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MediaConversionCronJob.asynchronous</code> attribute defined at extension <code>mediaconversion</code>. 
	 * @return the asynchronous - Optional collection of ConversionMediaFormats to obey.
	 */
	@Accessor(qualifier = "asynchronous", type = Accessor.Type.GETTER)
	public Boolean getAsynchronous()
	{
		if (this._asynchronous!=null)
		{
			return _asynchronous;
		}
		return _asynchronous = getPersistenceContext().getValue(ASYNCHRONOUS, _asynchronous);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MediaConversionCronJob.includedFormats</code> attribute defined at extension <code>mediaconversion</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the includedFormats - Optional collection of ConversionMediaFormats to obey.
	 */
	@Accessor(qualifier = "includedFormats", type = Accessor.Type.GETTER)
	public Collection<ConversionMediaFormatModel> getIncludedFormats()
	{
		if (this._includedFormats!=null)
		{
			return _includedFormats;
		}
		return _includedFormats = getPersistenceContext().getValue(INCLUDEDFORMATS, _includedFormats);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MediaConversionCronJob.asynchronous</code> attribute defined at extension <code>mediaconversion</code>. 
	 *  
	 * @param value the asynchronous - Optional collection of ConversionMediaFormats to obey.
	 */
	@Accessor(qualifier = "asynchronous", type = Accessor.Type.SETTER)
	public void setAsynchronous(final Boolean value)
	{
		_asynchronous = getPersistenceContext().setValue(ASYNCHRONOUS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MediaConversionCronJob.includedFormats</code> attribute defined at extension <code>mediaconversion</code>. 
	 *  
	 * @param value the includedFormats - Optional collection of ConversionMediaFormats to obey.
	 */
	@Accessor(qualifier = "includedFormats", type = Accessor.Type.SETTER)
	public void setIncludedFormats(final Collection<ConversionMediaFormatModel> value)
	{
		_includedFormats = getPersistenceContext().setValue(INCLUDEDFORMATS, value);
	}
	
}

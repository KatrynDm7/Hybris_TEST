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
package de.hybris.platform.cronjob.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.CompositeCronJobModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type CompositeEntry first defined at extension processing.
 */
@SuppressWarnings("all")
public class CompositeEntryModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "CompositeEntry";
	
	/**<i>Generated relation code constant for relation <code>CompositeCronJobEntriesRelation</code> defining source attribute <code>compositeCronJob</code> in extension <code>processing</code>.</i>*/
	public final static String _COMPOSITECRONJOBENTRIESRELATION = "CompositeCronJobEntriesRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>CompositeEntry.code</code> attribute defined at extension <code>processing</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>CompositeEntry.executableCronJob</code> attribute defined at extension <code>processing</code>. */
	public static final String EXECUTABLECRONJOB = "executableCronJob";
	
	/** <i>Generated constant</i> - Attribute key of <code>CompositeEntry.triggerableJob</code> attribute defined at extension <code>processing</code>. */
	public static final String TRIGGERABLEJOB = "triggerableJob";
	
	/** <i>Generated constant</i> - Attribute key of <code>CompositeEntry.compositeCronJob</code> attribute defined at extension <code>processing</code>. */
	public static final String COMPOSITECRONJOB = "compositeCronJob";
	
	
	/** <i>Generated variable</i> - Variable of <code>CompositeEntry.code</code> attribute defined at extension <code>processing</code>. */
	private String _code;
	
	/** <i>Generated variable</i> - Variable of <code>CompositeEntry.executableCronJob</code> attribute defined at extension <code>processing</code>. */
	private CronJobModel _executableCronJob;
	
	/** <i>Generated variable</i> - Variable of <code>CompositeEntry.triggerableJob</code> attribute defined at extension <code>processing</code>. */
	private JobModel _triggerableJob;
	
	/** <i>Generated variable</i> - Variable of <code>CompositeEntry.compositeCronJob</code> attribute defined at extension <code>processing</code>. */
	private CompositeCronJobModel _compositeCronJob;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CompositeEntryModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CompositeEntryModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>CompositeEntry</code> at extension <code>processing</code>
	 */
	@Deprecated
	public CompositeEntryModel(final String _code)
	{
		super();
		setCode(_code);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>CompositeEntry</code> at extension <code>processing</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public CompositeEntryModel(final String _code, final ItemModel _owner)
	{
		super();
		setCode(_code);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CompositeEntry.code</code> attribute defined at extension <code>processing</code>. 
	 * @return the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.GETTER)
	public String getCode()
	{
		if (this._code!=null)
		{
			return _code;
		}
		return _code = getPersistenceContext().getValue(CODE, _code);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CompositeEntry.compositeCronJob</code> attribute defined at extension <code>processing</code>. 
	 * @return the compositeCronJob - assigned CronJob
	 */
	@Accessor(qualifier = "compositeCronJob", type = Accessor.Type.GETTER)
	public CompositeCronJobModel getCompositeCronJob()
	{
		if (this._compositeCronJob!=null)
		{
			return _compositeCronJob;
		}
		return _compositeCronJob = getPersistenceContext().getValue(COMPOSITECRONJOB, _compositeCronJob);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CompositeEntry.executableCronJob</code> attribute defined at extension <code>processing</code>. 
	 * @return the executableCronJob
	 */
	@Accessor(qualifier = "executableCronJob", type = Accessor.Type.GETTER)
	public CronJobModel getExecutableCronJob()
	{
		if (this._executableCronJob!=null)
		{
			return _executableCronJob;
		}
		return _executableCronJob = getPersistenceContext().getValue(EXECUTABLECRONJOB, _executableCronJob);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CompositeEntry.triggerableJob</code> attribute defined at extension <code>processing</code>. 
	 * @return the triggerableJob
	 */
	@Accessor(qualifier = "triggerableJob", type = Accessor.Type.GETTER)
	public JobModel getTriggerableJob()
	{
		if (this._triggerableJob!=null)
		{
			return _triggerableJob;
		}
		return _triggerableJob = getPersistenceContext().getValue(TRIGGERABLEJOB, _triggerableJob);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CompositeEntry.code</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		_code = getPersistenceContext().setValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CompositeEntry.compositeCronJob</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the compositeCronJob - assigned CronJob
	 */
	@Accessor(qualifier = "compositeCronJob", type = Accessor.Type.SETTER)
	public void setCompositeCronJob(final CompositeCronJobModel value)
	{
		_compositeCronJob = getPersistenceContext().setValue(COMPOSITECRONJOB, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CompositeEntry.executableCronJob</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the executableCronJob
	 */
	@Accessor(qualifier = "executableCronJob", type = Accessor.Type.SETTER)
	public void setExecutableCronJob(final CronJobModel value)
	{
		_executableCronJob = getPersistenceContext().setValue(EXECUTABLECRONJOB, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CompositeEntry.triggerableJob</code> attribute defined at extension <code>processing</code>. 
	 *  
	 * @param value the triggerableJob
	 */
	@Accessor(qualifier = "triggerableJob", type = Accessor.Type.SETTER)
	public void setTriggerableJob(final JobModel value)
	{
		_triggerableJob = getPersistenceContext().setValue(TRIGGERABLEJOB, value);
	}
	
}

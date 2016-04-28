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
package de.hybris.platform.impex.model.cronjob;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.enums.EncodingEnum;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.impex.enums.ImpExValidationModeEnum;
import de.hybris.platform.impex.model.ImpExMediaModel;
import de.hybris.platform.impex.model.cronjob.ImpExImportJobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

/**
 * Generated model class for type ImpExImportCronJob first defined at extension impex.
 */
@SuppressWarnings("all")
public class ImpExImportCronJobModel extends CronJobModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "ImpExImportCronJob";
	
	/**<i>Generated relation code constant for relation <code>JobCronJobRelation</code> defining source attribute <code>job</code> in extension <code>processing</code>.</i>*/
	public final static String _JOBCRONJOBRELATION = "JobCronJobRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExImportCronJob.jobMedia</code> attribute defined at extension <code>impex</code>. */
	public static final String JOBMEDIA = "jobMedia";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExImportCronJob.workMedia</code> attribute defined at extension <code>impex</code>. */
	public static final String WORKMEDIA = "workMedia";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExImportCronJob.lastSuccessfulLine</code> attribute defined at extension <code>impex</code>. */
	public static final String LASTSUCCESSFULLINE = "lastSuccessfulLine";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExImportCronJob.mediasMedia</code> attribute defined at extension <code>impex</code>. */
	public static final String MEDIASMEDIA = "mediasMedia";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExImportCronJob.externalDataCollection</code> attribute defined at extension <code>impex</code>. */
	public static final String EXTERNALDATACOLLECTION = "externalDataCollection";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExImportCronJob.locale</code> attribute defined at extension <code>impex</code>. */
	public static final String LOCALE = "locale";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExImportCronJob.dumpFileEncoding</code> attribute defined at extension <code>impex</code>. */
	public static final String DUMPFILEENCODING = "dumpFileEncoding";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExImportCronJob.enableCodeExecution</code> attribute defined at extension <code>impex</code>. */
	public static final String ENABLECODEEXECUTION = "enableCodeExecution";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExImportCronJob.enableExternalCodeExecution</code> attribute defined at extension <code>impex</code>. */
	public static final String ENABLEEXTERNALCODEEXECUTION = "enableExternalCodeExecution";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExImportCronJob.enableExternalSyntaxParsing</code> attribute defined at extension <code>impex</code>. */
	public static final String ENABLEEXTERNALSYNTAXPARSING = "enableExternalSyntaxParsing";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExImportCronJob.enableHmcSavedValues</code> attribute defined at extension <code>impex</code>. */
	public static final String ENABLEHMCSAVEDVALUES = "enableHmcSavedValues";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExImportCronJob.mediasTarget</code> attribute defined at extension <code>impex</code>. */
	public static final String MEDIASTARGET = "mediasTarget";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExImportCronJob.valueCount</code> attribute defined at extension <code>impex</code>. */
	public static final String VALUECOUNT = "valueCount";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExImportCronJob.unresolvedDataStore</code> attribute defined at extension <code>impex</code>. */
	public static final String UNRESOLVEDDATASTORE = "unresolvedDataStore";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExImportCronJob.zipentry</code> attribute defined at extension <code>impex</code>. */
	public static final String ZIPENTRY = "zipentry";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExImportCronJob.mode</code> attribute defined at extension <code>impex</code>. */
	public static final String MODE = "mode";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExImportCronJob.dumpingAllowed</code> attribute defined at extension <code>impex</code>. */
	public static final String DUMPINGALLOWED = "dumpingAllowed";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExImportCronJob.unzipMediasMedia</code> attribute defined at extension <code>impex</code>. */
	public static final String UNZIPMEDIASMEDIA = "unzipMediasMedia";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExImportCronJob.maxThreads</code> attribute defined at extension <code>impex</code>. */
	public static final String MAXTHREADS = "maxThreads";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExImportCronJob.legacyMode</code> attribute defined at extension <code>impex</code>. */
	public static final String LEGACYMODE = "legacyMode";
	
	
	/** <i>Generated variable</i> - Variable of <code>ImpExImportCronJob.jobMedia</code> attribute defined at extension <code>impex</code>. */
	private ImpExMediaModel _jobMedia;
	
	/** <i>Generated variable</i> - Variable of <code>ImpExImportCronJob.workMedia</code> attribute defined at extension <code>impex</code>. */
	private ImpExMediaModel _workMedia;
	
	/** <i>Generated variable</i> - Variable of <code>ImpExImportCronJob.lastSuccessfulLine</code> attribute defined at extension <code>impex</code>. */
	private Integer _lastSuccessfulLine;
	
	/** <i>Generated variable</i> - Variable of <code>ImpExImportCronJob.mediasMedia</code> attribute defined at extension <code>impex</code>. */
	private MediaModel _mediasMedia;
	
	/** <i>Generated variable</i> - Variable of <code>ImpExImportCronJob.externalDataCollection</code> attribute defined at extension <code>impex</code>. */
	private Collection<ImpExMediaModel> _externalDataCollection;
	
	/** <i>Generated variable</i> - Variable of <code>ImpExImportCronJob.locale</code> attribute defined at extension <code>impex</code>. */
	private String _locale;
	
	/** <i>Generated variable</i> - Variable of <code>ImpExImportCronJob.dumpFileEncoding</code> attribute defined at extension <code>impex</code>. */
	private EncodingEnum _dumpFileEncoding;
	
	/** <i>Generated variable</i> - Variable of <code>ImpExImportCronJob.enableCodeExecution</code> attribute defined at extension <code>impex</code>. */
	private Boolean _enableCodeExecution;
	
	/** <i>Generated variable</i> - Variable of <code>ImpExImportCronJob.enableExternalCodeExecution</code> attribute defined at extension <code>impex</code>. */
	private Boolean _enableExternalCodeExecution;
	
	/** <i>Generated variable</i> - Variable of <code>ImpExImportCronJob.enableExternalSyntaxParsing</code> attribute defined at extension <code>impex</code>. */
	private Boolean _enableExternalSyntaxParsing;
	
	/** <i>Generated variable</i> - Variable of <code>ImpExImportCronJob.enableHmcSavedValues</code> attribute defined at extension <code>impex</code>. */
	private Boolean _enableHmcSavedValues;
	
	/** <i>Generated variable</i> - Variable of <code>ImpExImportCronJob.mediasTarget</code> attribute defined at extension <code>impex</code>. */
	private String _mediasTarget;
	
	/** <i>Generated variable</i> - Variable of <code>ImpExImportCronJob.valueCount</code> attribute defined at extension <code>impex</code>. */
	private Integer _valueCount;
	
	/** <i>Generated variable</i> - Variable of <code>ImpExImportCronJob.unresolvedDataStore</code> attribute defined at extension <code>impex</code>. */
	private ImpExMediaModel _unresolvedDataStore;
	
	/** <i>Generated variable</i> - Variable of <code>ImpExImportCronJob.zipentry</code> attribute defined at extension <code>impex</code>. */
	private String _zipentry;
	
	/** <i>Generated variable</i> - Variable of <code>ImpExImportCronJob.mode</code> attribute defined at extension <code>impex</code>. */
	private ImpExValidationModeEnum _mode;
	
	/** <i>Generated variable</i> - Variable of <code>ImpExImportCronJob.dumpingAllowed</code> attribute defined at extension <code>impex</code>. */
	private Boolean _dumpingAllowed;
	
	/** <i>Generated variable</i> - Variable of <code>ImpExImportCronJob.unzipMediasMedia</code> attribute defined at extension <code>impex</code>. */
	private Boolean _unzipMediasMedia;
	
	/** <i>Generated variable</i> - Variable of <code>ImpExImportCronJob.maxThreads</code> attribute defined at extension <code>impex</code>. */
	private Integer _maxThreads;
	
	/** <i>Generated variable</i> - Variable of <code>ImpExImportCronJob.legacyMode</code> attribute defined at extension <code>impex</code>. */
	private Boolean _legacyMode;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ImpExImportCronJobModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ImpExImportCronJobModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _job initial attribute declared by type <code>ImpExImportCronJob</code> at extension <code>impex</code>
	 */
	@Deprecated
	public ImpExImportCronJobModel(final ImpExImportJobModel _job)
	{
		super();
		setJob(_job);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _job initial attribute declared by type <code>ImpExImportCronJob</code> at extension <code>impex</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public ImpExImportCronJobModel(final ImpExImportJobModel _job, final ItemModel _owner)
	{
		super();
		setJob(_job);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExImportCronJob.dumpFileEncoding</code> attribute defined at extension <code>impex</code>. 
	 * @return the dumpFileEncoding
	 */
	@Accessor(qualifier = "dumpFileEncoding", type = Accessor.Type.GETTER)
	public EncodingEnum getDumpFileEncoding()
	{
		if (this._dumpFileEncoding!=null)
		{
			return _dumpFileEncoding;
		}
		return _dumpFileEncoding = getPersistenceContext().getValue(DUMPFILEENCODING, _dumpFileEncoding);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExImportCronJob.dumpingAllowed</code> attribute defined at extension <code>impex</code>. 
	 * @return the dumpingAllowed
	 */
	@Accessor(qualifier = "dumpingAllowed", type = Accessor.Type.GETTER)
	public Boolean getDumpingAllowed()
	{
		if (this._dumpingAllowed!=null)
		{
			return _dumpingAllowed;
		}
		return _dumpingAllowed = getPersistenceContext().getValue(DUMPINGALLOWED, _dumpingAllowed);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExImportCronJob.enableCodeExecution</code> attribute defined at extension <code>impex</code>. 
	 * @return the enableCodeExecution
	 */
	@Accessor(qualifier = "enableCodeExecution", type = Accessor.Type.GETTER)
	public Boolean getEnableCodeExecution()
	{
		if (this._enableCodeExecution!=null)
		{
			return _enableCodeExecution;
		}
		return _enableCodeExecution = getPersistenceContext().getValue(ENABLECODEEXECUTION, _enableCodeExecution);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExImportCronJob.enableExternalCodeExecution</code> attribute defined at extension <code>impex</code>. 
	 * @return the enableExternalCodeExecution
	 */
	@Accessor(qualifier = "enableExternalCodeExecution", type = Accessor.Type.GETTER)
	public Boolean getEnableExternalCodeExecution()
	{
		if (this._enableExternalCodeExecution!=null)
		{
			return _enableExternalCodeExecution;
		}
		return _enableExternalCodeExecution = getPersistenceContext().getValue(ENABLEEXTERNALCODEEXECUTION, _enableExternalCodeExecution);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExImportCronJob.enableExternalSyntaxParsing</code> attribute defined at extension <code>impex</code>. 
	 * @return the enableExternalSyntaxParsing
	 */
	@Accessor(qualifier = "enableExternalSyntaxParsing", type = Accessor.Type.GETTER)
	public Boolean getEnableExternalSyntaxParsing()
	{
		if (this._enableExternalSyntaxParsing!=null)
		{
			return _enableExternalSyntaxParsing;
		}
		return _enableExternalSyntaxParsing = getPersistenceContext().getValue(ENABLEEXTERNALSYNTAXPARSING, _enableExternalSyntaxParsing);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExImportCronJob.enableHmcSavedValues</code> attribute defined at extension <code>impex</code>. 
	 * @return the enableHmcSavedValues
	 */
	@Accessor(qualifier = "enableHmcSavedValues", type = Accessor.Type.GETTER)
	public Boolean getEnableHmcSavedValues()
	{
		if (this._enableHmcSavedValues!=null)
		{
			return _enableHmcSavedValues;
		}
		return _enableHmcSavedValues = getPersistenceContext().getValue(ENABLEHMCSAVEDVALUES, _enableHmcSavedValues);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExImportCronJob.externalDataCollection</code> attribute defined at extension <code>impex</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the externalDataCollection
	 */
	@Accessor(qualifier = "externalDataCollection", type = Accessor.Type.GETTER)
	public Collection<ImpExMediaModel> getExternalDataCollection()
	{
		if (this._externalDataCollection!=null)
		{
			return _externalDataCollection;
		}
		return _externalDataCollection = getPersistenceContext().getValue(EXTERNALDATACOLLECTION, _externalDataCollection);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CronJob.job</code> attribute defined at extension <code>processing</code> and redeclared at extension <code>impex</code>. 
	 * @return the job
	 */
	@Override
	@Accessor(qualifier = "job", type = Accessor.Type.GETTER)
	public ImpExImportJobModel getJob()
	{
		return (ImpExImportJobModel) super.getJob();
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExImportCronJob.jobMedia</code> attribute defined at extension <code>impex</code>. 
	 * @return the jobMedia
	 */
	@Accessor(qualifier = "jobMedia", type = Accessor.Type.GETTER)
	public ImpExMediaModel getJobMedia()
	{
		if (this._jobMedia!=null)
		{
			return _jobMedia;
		}
		return _jobMedia = getPersistenceContext().getValue(JOBMEDIA, _jobMedia);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExImportCronJob.lastSuccessfulLine</code> attribute defined at extension <code>impex</code>. 
	 * @return the lastSuccessfulLine
	 */
	@Accessor(qualifier = "lastSuccessfulLine", type = Accessor.Type.GETTER)
	public Integer getLastSuccessfulLine()
	{
		if (this._lastSuccessfulLine!=null)
		{
			return _lastSuccessfulLine;
		}
		return _lastSuccessfulLine = getPersistenceContext().getValue(LASTSUCCESSFULLINE, _lastSuccessfulLine);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExImportCronJob.legacyMode</code> attribute defined at extension <code>impex</code>. 
	 * @return the legacyMode
	 */
	@Accessor(qualifier = "legacyMode", type = Accessor.Type.GETTER)
	public Boolean getLegacyMode()
	{
		if (this._legacyMode!=null)
		{
			return _legacyMode;
		}
		return _legacyMode = getPersistenceContext().getValue(LEGACYMODE, _legacyMode);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExImportCronJob.locale</code> attribute defined at extension <code>impex</code>. 
	 * @return the locale
	 */
	@Accessor(qualifier = "locale", type = Accessor.Type.GETTER)
	public String getLocale()
	{
		if (this._locale!=null)
		{
			return _locale;
		}
		return _locale = getPersistenceContext().getValue(LOCALE, _locale);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExImportCronJob.maxThreads</code> attribute defined at extension <code>impex</code>. 
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
	 * <i>Generated method</i> - Getter of the <code>ImpExImportCronJob.mediasMedia</code> attribute defined at extension <code>impex</code>. 
	 * @return the mediasMedia
	 */
	@Accessor(qualifier = "mediasMedia", type = Accessor.Type.GETTER)
	public MediaModel getMediasMedia()
	{
		if (this._mediasMedia!=null)
		{
			return _mediasMedia;
		}
		return _mediasMedia = getPersistenceContext().getValue(MEDIASMEDIA, _mediasMedia);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExImportCronJob.mediasTarget</code> attribute defined at extension <code>impex</code>. 
	 * @return the mediasTarget
	 */
	@Accessor(qualifier = "mediasTarget", type = Accessor.Type.GETTER)
	public String getMediasTarget()
	{
		if (this._mediasTarget!=null)
		{
			return _mediasTarget;
		}
		return _mediasTarget = getPersistenceContext().getValue(MEDIASTARGET, _mediasTarget);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExImportCronJob.mode</code> attribute defined at extension <code>impex</code>. 
	 * @return the mode
	 */
	@Accessor(qualifier = "mode", type = Accessor.Type.GETTER)
	public ImpExValidationModeEnum getMode()
	{
		if (this._mode!=null)
		{
			return _mode;
		}
		return _mode = getPersistenceContext().getValue(MODE, _mode);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExImportCronJob.unresolvedDataStore</code> attribute defined at extension <code>impex</code>. 
	 * @return the unresolvedDataStore
	 */
	@Accessor(qualifier = "unresolvedDataStore", type = Accessor.Type.GETTER)
	public ImpExMediaModel getUnresolvedDataStore()
	{
		if (this._unresolvedDataStore!=null)
		{
			return _unresolvedDataStore;
		}
		return _unresolvedDataStore = getPersistenceContext().getValue(UNRESOLVEDDATASTORE, _unresolvedDataStore);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExImportCronJob.unzipMediasMedia</code> attribute defined at extension <code>impex</code>. 
	 * @return the unzipMediasMedia
	 */
	@Accessor(qualifier = "unzipMediasMedia", type = Accessor.Type.GETTER)
	public Boolean getUnzipMediasMedia()
	{
		if (this._unzipMediasMedia!=null)
		{
			return _unzipMediasMedia;
		}
		return _unzipMediasMedia = getPersistenceContext().getValue(UNZIPMEDIASMEDIA, _unzipMediasMedia);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExImportCronJob.valueCount</code> attribute defined at extension <code>impex</code>. 
	 * @return the valueCount
	 */
	@Accessor(qualifier = "valueCount", type = Accessor.Type.GETTER)
	public Integer getValueCount()
	{
		if (this._valueCount!=null)
		{
			return _valueCount;
		}
		return _valueCount = getPersistenceContext().getValue(VALUECOUNT, _valueCount);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExImportCronJob.workMedia</code> attribute defined at extension <code>impex</code>. 
	 * @return the workMedia
	 */
	@Accessor(qualifier = "workMedia", type = Accessor.Type.GETTER)
	public ImpExMediaModel getWorkMedia()
	{
		if (this._workMedia!=null)
		{
			return _workMedia;
		}
		return _workMedia = getPersistenceContext().getValue(WORKMEDIA, _workMedia);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExImportCronJob.zipentry</code> attribute defined at extension <code>impex</code>. 
	 * @return the zipentry
	 */
	@Accessor(qualifier = "zipentry", type = Accessor.Type.GETTER)
	public String getZipentry()
	{
		if (this._zipentry!=null)
		{
			return _zipentry;
		}
		return _zipentry = getPersistenceContext().getValue(ZIPENTRY, _zipentry);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExImportCronJob.dumpFileEncoding</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the dumpFileEncoding
	 */
	@Accessor(qualifier = "dumpFileEncoding", type = Accessor.Type.SETTER)
	public void setDumpFileEncoding(final EncodingEnum value)
	{
		_dumpFileEncoding = getPersistenceContext().setValue(DUMPFILEENCODING, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExImportCronJob.dumpingAllowed</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the dumpingAllowed
	 */
	@Accessor(qualifier = "dumpingAllowed", type = Accessor.Type.SETTER)
	public void setDumpingAllowed(final Boolean value)
	{
		_dumpingAllowed = getPersistenceContext().setValue(DUMPINGALLOWED, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExImportCronJob.enableCodeExecution</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the enableCodeExecution
	 */
	@Accessor(qualifier = "enableCodeExecution", type = Accessor.Type.SETTER)
	public void setEnableCodeExecution(final Boolean value)
	{
		_enableCodeExecution = getPersistenceContext().setValue(ENABLECODEEXECUTION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExImportCronJob.enableExternalCodeExecution</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the enableExternalCodeExecution
	 */
	@Accessor(qualifier = "enableExternalCodeExecution", type = Accessor.Type.SETTER)
	public void setEnableExternalCodeExecution(final Boolean value)
	{
		_enableExternalCodeExecution = getPersistenceContext().setValue(ENABLEEXTERNALCODEEXECUTION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExImportCronJob.enableExternalSyntaxParsing</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the enableExternalSyntaxParsing
	 */
	@Accessor(qualifier = "enableExternalSyntaxParsing", type = Accessor.Type.SETTER)
	public void setEnableExternalSyntaxParsing(final Boolean value)
	{
		_enableExternalSyntaxParsing = getPersistenceContext().setValue(ENABLEEXTERNALSYNTAXPARSING, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExImportCronJob.enableHmcSavedValues</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the enableHmcSavedValues
	 */
	@Accessor(qualifier = "enableHmcSavedValues", type = Accessor.Type.SETTER)
	public void setEnableHmcSavedValues(final Boolean value)
	{
		_enableHmcSavedValues = getPersistenceContext().setValue(ENABLEHMCSAVEDVALUES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExImportCronJob.externalDataCollection</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the externalDataCollection
	 */
	@Accessor(qualifier = "externalDataCollection", type = Accessor.Type.SETTER)
	public void setExternalDataCollection(final Collection<ImpExMediaModel> value)
	{
		_externalDataCollection = getPersistenceContext().setValue(EXTERNALDATACOLLECTION, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>CronJob.job</code> attribute defined at extension <code>processing</code> and redeclared at extension <code>impex</code>. Can only be used at creation of model - before first save. Will only accept values of type {@link de.hybris.platform.impex.model.cronjob.ImpExImportJobModel}.  
	 *  
	 * @param value the job
	 */
	@Override
	@Accessor(qualifier = "job", type = Accessor.Type.SETTER)
	public void setJob(final JobModel value)
	{
		super.setJob(value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExImportCronJob.jobMedia</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the jobMedia
	 */
	@Accessor(qualifier = "jobMedia", type = Accessor.Type.SETTER)
	public void setJobMedia(final ImpExMediaModel value)
	{
		_jobMedia = getPersistenceContext().setValue(JOBMEDIA, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExImportCronJob.lastSuccessfulLine</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the lastSuccessfulLine
	 */
	@Accessor(qualifier = "lastSuccessfulLine", type = Accessor.Type.SETTER)
	public void setLastSuccessfulLine(final Integer value)
	{
		_lastSuccessfulLine = getPersistenceContext().setValue(LASTSUCCESSFULLINE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExImportCronJob.legacyMode</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the legacyMode
	 */
	@Accessor(qualifier = "legacyMode", type = Accessor.Type.SETTER)
	public void setLegacyMode(final Boolean value)
	{
		_legacyMode = getPersistenceContext().setValue(LEGACYMODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExImportCronJob.locale</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the locale
	 */
	@Accessor(qualifier = "locale", type = Accessor.Type.SETTER)
	public void setLocale(final String value)
	{
		_locale = getPersistenceContext().setValue(LOCALE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExImportCronJob.maxThreads</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the maxThreads
	 */
	@Accessor(qualifier = "maxThreads", type = Accessor.Type.SETTER)
	public void setMaxThreads(final Integer value)
	{
		_maxThreads = getPersistenceContext().setValue(MAXTHREADS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExImportCronJob.mediasMedia</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the mediasMedia
	 */
	@Accessor(qualifier = "mediasMedia", type = Accessor.Type.SETTER)
	public void setMediasMedia(final MediaModel value)
	{
		_mediasMedia = getPersistenceContext().setValue(MEDIASMEDIA, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExImportCronJob.mediasTarget</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the mediasTarget
	 */
	@Accessor(qualifier = "mediasTarget", type = Accessor.Type.SETTER)
	public void setMediasTarget(final String value)
	{
		_mediasTarget = getPersistenceContext().setValue(MEDIASTARGET, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExImportCronJob.mode</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the mode
	 */
	@Accessor(qualifier = "mode", type = Accessor.Type.SETTER)
	public void setMode(final ImpExValidationModeEnum value)
	{
		_mode = getPersistenceContext().setValue(MODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExImportCronJob.unresolvedDataStore</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the unresolvedDataStore
	 */
	@Accessor(qualifier = "unresolvedDataStore", type = Accessor.Type.SETTER)
	public void setUnresolvedDataStore(final ImpExMediaModel value)
	{
		_unresolvedDataStore = getPersistenceContext().setValue(UNRESOLVEDDATASTORE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExImportCronJob.unzipMediasMedia</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the unzipMediasMedia
	 */
	@Accessor(qualifier = "unzipMediasMedia", type = Accessor.Type.SETTER)
	public void setUnzipMediasMedia(final Boolean value)
	{
		_unzipMediasMedia = getPersistenceContext().setValue(UNZIPMEDIASMEDIA, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExImportCronJob.valueCount</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the valueCount
	 */
	@Accessor(qualifier = "valueCount", type = Accessor.Type.SETTER)
	public void setValueCount(final Integer value)
	{
		_valueCount = getPersistenceContext().setValue(VALUECOUNT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExImportCronJob.workMedia</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the workMedia
	 */
	@Accessor(qualifier = "workMedia", type = Accessor.Type.SETTER)
	public void setWorkMedia(final ImpExMediaModel value)
	{
		_workMedia = getPersistenceContext().setValue(WORKMEDIA, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExImportCronJob.zipentry</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the zipentry
	 */
	@Accessor(qualifier = "zipentry", type = Accessor.Type.SETTER)
	public void setZipentry(final String value)
	{
		_zipentry = getPersistenceContext().setValue(ZIPENTRY, value);
	}
	
}

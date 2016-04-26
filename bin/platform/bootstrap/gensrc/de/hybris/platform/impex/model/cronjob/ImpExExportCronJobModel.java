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
package de.hybris.platform.impex.model.cronjob;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.enums.EncodingEnum;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.impex.enums.ExportConverterEnum;
import de.hybris.platform.impex.enums.ImpExValidationModeEnum;
import de.hybris.platform.impex.model.ImpExMediaModel;
import de.hybris.platform.impex.model.exp.ExportModel;
import de.hybris.platform.impex.model.exp.HeaderLibraryModel;
import de.hybris.platform.impex.model.exp.ImpExExportMediaModel;
import de.hybris.platform.impex.model.exp.ReportModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type ImpExExportCronJob first defined at extension impex.
 */
@SuppressWarnings("all")
public class ImpExExportCronJobModel extends CronJobModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "ImpExExportCronJob";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExExportCronJob.encoding</code> attribute defined at extension <code>impex</code>. */
	public static final String ENCODING = "encoding";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExExportCronJob.mode</code> attribute defined at extension <code>impex</code>. */
	public static final String MODE = "mode";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExExportCronJob.dataExportTarget</code> attribute defined at extension <code>impex</code>. */
	public static final String DATAEXPORTTARGET = "dataExportTarget";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExExportCronJob.mediasExportTarget</code> attribute defined at extension <code>impex</code>. */
	public static final String MEDIASEXPORTTARGET = "mediasExportTarget";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExExportCronJob.exportTemplate</code> attribute defined at extension <code>impex</code>. */
	public static final String EXPORTTEMPLATE = "exportTemplate";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExExportCronJob.export</code> attribute defined at extension <code>impex</code>. */
	public static final String EXPORT = "export";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExExportCronJob.itemsExported</code> attribute defined at extension <code>impex</code>. */
	public static final String ITEMSEXPORTED = "itemsExported";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExExportCronJob.itemsMaxCount</code> attribute defined at extension <code>impex</code>. */
	public static final String ITEMSMAXCOUNT = "itemsMaxCount";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExExportCronJob.itemsSkipped</code> attribute defined at extension <code>impex</code>. */
	public static final String ITEMSSKIPPED = "itemsSkipped";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExExportCronJob.jobMedia</code> attribute defined at extension <code>impex</code>. */
	public static final String JOBMEDIA = "jobMedia";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExExportCronJob.fieldSeparator</code> attribute defined at extension <code>impex</code>. */
	public static final String FIELDSEPARATOR = "fieldSeparator";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExExportCronJob.quoteCharacter</code> attribute defined at extension <code>impex</code>. */
	public static final String QUOTECHARACTER = "quoteCharacter";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExExportCronJob.commentCharacter</code> attribute defined at extension <code>impex</code>. */
	public static final String COMMENTCHARACTER = "commentCharacter";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExExportCronJob.dataExportMediaCode</code> attribute defined at extension <code>impex</code>. */
	public static final String DATAEXPORTMEDIACODE = "dataExportMediaCode";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExExportCronJob.mediasExportMediaCode</code> attribute defined at extension <code>impex</code>. */
	public static final String MEDIASEXPORTMEDIACODE = "mediasExportMediaCode";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExExportCronJob.report</code> attribute defined at extension <code>impex</code>. */
	public static final String REPORT = "report";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExExportCronJob.converterClass</code> attribute defined at extension <code>impex</code>. */
	public static final String CONVERTERCLASS = "converterClass";
	
	/** <i>Generated constant</i> - Attribute key of <code>ImpExExportCronJob.singleFile</code> attribute defined at extension <code>impex</code>. */
	public static final String SINGLEFILE = "singleFile";
	
	
	/** <i>Generated variable</i> - Variable of <code>ImpExExportCronJob.encoding</code> attribute defined at extension <code>impex</code>. */
	private EncodingEnum _encoding;
	
	/** <i>Generated variable</i> - Variable of <code>ImpExExportCronJob.mode</code> attribute defined at extension <code>impex</code>. */
	private ImpExValidationModeEnum _mode;
	
	/** <i>Generated variable</i> - Variable of <code>ImpExExportCronJob.dataExportTarget</code> attribute defined at extension <code>impex</code>. */
	private ImpExExportMediaModel _dataExportTarget;
	
	/** <i>Generated variable</i> - Variable of <code>ImpExExportCronJob.mediasExportTarget</code> attribute defined at extension <code>impex</code>. */
	private ImpExExportMediaModel _mediasExportTarget;
	
	/** <i>Generated variable</i> - Variable of <code>ImpExExportCronJob.exportTemplate</code> attribute defined at extension <code>impex</code>. */
	private HeaderLibraryModel _exportTemplate;
	
	/** <i>Generated variable</i> - Variable of <code>ImpExExportCronJob.export</code> attribute defined at extension <code>impex</code>. */
	private ExportModel _export;
	
	/** <i>Generated variable</i> - Variable of <code>ImpExExportCronJob.itemsExported</code> attribute defined at extension <code>impex</code>. */
	private Integer _itemsExported;
	
	/** <i>Generated variable</i> - Variable of <code>ImpExExportCronJob.itemsMaxCount</code> attribute defined at extension <code>impex</code>. */
	private Integer _itemsMaxCount;
	
	/** <i>Generated variable</i> - Variable of <code>ImpExExportCronJob.itemsSkipped</code> attribute defined at extension <code>impex</code>. */
	private Integer _itemsSkipped;
	
	/** <i>Generated variable</i> - Variable of <code>ImpExExportCronJob.jobMedia</code> attribute defined at extension <code>impex</code>. */
	private ImpExMediaModel _jobMedia;
	
	/** <i>Generated variable</i> - Variable of <code>ImpExExportCronJob.fieldSeparator</code> attribute defined at extension <code>impex</code>. */
	private Character _fieldSeparator;
	
	/** <i>Generated variable</i> - Variable of <code>ImpExExportCronJob.quoteCharacter</code> attribute defined at extension <code>impex</code>. */
	private Character _quoteCharacter;
	
	/** <i>Generated variable</i> - Variable of <code>ImpExExportCronJob.commentCharacter</code> attribute defined at extension <code>impex</code>. */
	private Character _commentCharacter;
	
	/** <i>Generated variable</i> - Variable of <code>ImpExExportCronJob.dataExportMediaCode</code> attribute defined at extension <code>impex</code>. */
	private String _dataExportMediaCode;
	
	/** <i>Generated variable</i> - Variable of <code>ImpExExportCronJob.mediasExportMediaCode</code> attribute defined at extension <code>impex</code>. */
	private String _mediasExportMediaCode;
	
	/** <i>Generated variable</i> - Variable of <code>ImpExExportCronJob.report</code> attribute defined at extension <code>impex</code>. */
	private ReportModel _report;
	
	/** <i>Generated variable</i> - Variable of <code>ImpExExportCronJob.converterClass</code> attribute defined at extension <code>impex</code>. */
	private ExportConverterEnum _converterClass;
	
	/** <i>Generated variable</i> - Variable of <code>ImpExExportCronJob.singleFile</code> attribute defined at extension <code>impex</code>. */
	private Boolean _singleFile;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ImpExExportCronJobModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ImpExExportCronJobModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _job initial attribute declared by type <code>CronJob</code> at extension <code>processing</code>
	 */
	@Deprecated
	public ImpExExportCronJobModel(final JobModel _job)
	{
		super();
		setJob(_job);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _job initial attribute declared by type <code>CronJob</code> at extension <code>processing</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public ImpExExportCronJobModel(final JobModel _job, final ItemModel _owner)
	{
		super();
		setJob(_job);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExExportCronJob.commentCharacter</code> attribute defined at extension <code>impex</code>. 
	 * @return the commentCharacter - Character used for indicating a comment
	 */
	@Accessor(qualifier = "commentCharacter", type = Accessor.Type.GETTER)
	public Character getCommentCharacter()
	{
		if (this._commentCharacter!=null)
		{
			return _commentCharacter;
		}
		return _commentCharacter = getPersistenceContext().getValue(COMMENTCHARACTER, _commentCharacter);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExExportCronJob.converterClass</code> attribute defined at extension <code>impex</code>. 
	 * @return the converterClass - class, which will be used for report generation
	 */
	@Accessor(qualifier = "converterClass", type = Accessor.Type.GETTER)
	public ExportConverterEnum getConverterClass()
	{
		if (this._converterClass!=null)
		{
			return _converterClass;
		}
		return _converterClass = getPersistenceContext().getValue(CONVERTERCLASS, _converterClass);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExExportCronJob.dataExportMediaCode</code> attribute defined at extension <code>impex</code>. 
	 * @return the dataExportMediaCode - Code of the generated export media, containing the exported data
	 */
	@Accessor(qualifier = "dataExportMediaCode", type = Accessor.Type.GETTER)
	public String getDataExportMediaCode()
	{
		if (this._dataExportMediaCode!=null)
		{
			return _dataExportMediaCode;
		}
		return _dataExportMediaCode = getPersistenceContext().getValue(DATAEXPORTMEDIACODE, _dataExportMediaCode);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExExportCronJob.dataExportTarget</code> attribute defined at extension <code>impex</code>. 
	 * @return the dataExportTarget
	 */
	@Accessor(qualifier = "dataExportTarget", type = Accessor.Type.GETTER)
	public ImpExExportMediaModel getDataExportTarget()
	{
		if (this._dataExportTarget!=null)
		{
			return _dataExportTarget;
		}
		return _dataExportTarget = getPersistenceContext().getValue(DATAEXPORTTARGET, _dataExportTarget);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExExportCronJob.encoding</code> attribute defined at extension <code>impex</code>. 
	 * @return the encoding
	 */
	@Accessor(qualifier = "encoding", type = Accessor.Type.GETTER)
	public EncodingEnum getEncoding()
	{
		if (this._encoding!=null)
		{
			return _encoding;
		}
		return _encoding = getPersistenceContext().getValue(ENCODING, _encoding);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExExportCronJob.export</code> attribute defined at extension <code>impex</code>. 
	 * @return the export
	 */
	@Accessor(qualifier = "export", type = Accessor.Type.GETTER)
	public ExportModel getExport()
	{
		if (this._export!=null)
		{
			return _export;
		}
		return _export = getPersistenceContext().getValue(EXPORT, _export);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExExportCronJob.exportTemplate</code> attribute defined at extension <code>impex</code>. 
	 * @return the exportTemplate
	 */
	@Accessor(qualifier = "exportTemplate", type = Accessor.Type.GETTER)
	public HeaderLibraryModel getExportTemplate()
	{
		if (this._exportTemplate!=null)
		{
			return _exportTemplate;
		}
		return _exportTemplate = getPersistenceContext().getValue(EXPORTTEMPLATE, _exportTemplate);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExExportCronJob.fieldSeparator</code> attribute defined at extension <code>impex</code>. 
	 * @return the fieldSeparator - Character used for separating columns in CSV-lines
	 */
	@Accessor(qualifier = "fieldSeparator", type = Accessor.Type.GETTER)
	public Character getFieldSeparator()
	{
		if (this._fieldSeparator!=null)
		{
			return _fieldSeparator;
		}
		return _fieldSeparator = getPersistenceContext().getValue(FIELDSEPARATOR, _fieldSeparator);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExExportCronJob.itemsExported</code> attribute defined at extension <code>impex</code>. 
	 * @return the itemsExported
	 */
	@Accessor(qualifier = "itemsExported", type = Accessor.Type.GETTER)
	public Integer getItemsExported()
	{
		if (this._itemsExported!=null)
		{
			return _itemsExported;
		}
		return _itemsExported = getPersistenceContext().getValue(ITEMSEXPORTED, _itemsExported);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExExportCronJob.itemsMaxCount</code> attribute defined at extension <code>impex</code>. 
	 * @return the itemsMaxCount
	 */
	@Accessor(qualifier = "itemsMaxCount", type = Accessor.Type.GETTER)
	public Integer getItemsMaxCount()
	{
		if (this._itemsMaxCount!=null)
		{
			return _itemsMaxCount;
		}
		return _itemsMaxCount = getPersistenceContext().getValue(ITEMSMAXCOUNT, _itemsMaxCount);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExExportCronJob.itemsSkipped</code> attribute defined at extension <code>impex</code>. 
	 * @return the itemsSkipped
	 */
	@Accessor(qualifier = "itemsSkipped", type = Accessor.Type.GETTER)
	public Integer getItemsSkipped()
	{
		if (this._itemsSkipped!=null)
		{
			return _itemsSkipped;
		}
		return _itemsSkipped = getPersistenceContext().getValue(ITEMSSKIPPED, _itemsSkipped);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExExportCronJob.jobMedia</code> attribute defined at extension <code>impex</code>. 
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
	 * <i>Generated method</i> - Getter of the <code>ImpExExportCronJob.mediasExportMediaCode</code> attribute defined at extension <code>impex</code>. 
	 * @return the mediasExportMediaCode - Code of the generated export media, containing the exported medias
	 */
	@Accessor(qualifier = "mediasExportMediaCode", type = Accessor.Type.GETTER)
	public String getMediasExportMediaCode()
	{
		if (this._mediasExportMediaCode!=null)
		{
			return _mediasExportMediaCode;
		}
		return _mediasExportMediaCode = getPersistenceContext().getValue(MEDIASEXPORTMEDIACODE, _mediasExportMediaCode);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExExportCronJob.mediasExportTarget</code> attribute defined at extension <code>impex</code>. 
	 * @return the mediasExportTarget
	 */
	@Accessor(qualifier = "mediasExportTarget", type = Accessor.Type.GETTER)
	public ImpExExportMediaModel getMediasExportTarget()
	{
		if (this._mediasExportTarget!=null)
		{
			return _mediasExportTarget;
		}
		return _mediasExportTarget = getPersistenceContext().getValue(MEDIASEXPORTTARGET, _mediasExportTarget);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExExportCronJob.mode</code> attribute defined at extension <code>impex</code>. 
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
	 * <i>Generated method</i> - Getter of the <code>ImpExExportCronJob.quoteCharacter</code> attribute defined at extension <code>impex</code>. 
	 * @return the quoteCharacter - Character used for escaping columns in CSV-lines
	 */
	@Accessor(qualifier = "quoteCharacter", type = Accessor.Type.GETTER)
	public Character getQuoteCharacter()
	{
		if (this._quoteCharacter!=null)
		{
			return _quoteCharacter;
		}
		return _quoteCharacter = getPersistenceContext().getValue(QUOTECHARACTER, _quoteCharacter);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExExportCronJob.report</code> attribute defined at extension <code>impex</code>. 
	 * @return the report
	 */
	@Accessor(qualifier = "report", type = Accessor.Type.GETTER)
	public ReportModel getReport()
	{
		if (this._report!=null)
		{
			return _report;
		}
		return _report = getPersistenceContext().getValue(REPORT, _report);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ImpExExportCronJob.singleFile</code> attribute defined at extension <code>impex</code>. 
	 * @return the singleFile - Export result as a single file instead of Zip archive
	 */
	@Accessor(qualifier = "singleFile", type = Accessor.Type.GETTER)
	public Boolean getSingleFile()
	{
		if (this._singleFile!=null)
		{
			return _singleFile;
		}
		return _singleFile = getPersistenceContext().getValue(SINGLEFILE, _singleFile);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExExportCronJob.commentCharacter</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the commentCharacter - Character used for indicating a comment
	 */
	@Accessor(qualifier = "commentCharacter", type = Accessor.Type.SETTER)
	public void setCommentCharacter(final Character value)
	{
		_commentCharacter = getPersistenceContext().setValue(COMMENTCHARACTER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExExportCronJob.converterClass</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the converterClass - class, which will be used for report generation
	 */
	@Accessor(qualifier = "converterClass", type = Accessor.Type.SETTER)
	public void setConverterClass(final ExportConverterEnum value)
	{
		_converterClass = getPersistenceContext().setValue(CONVERTERCLASS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExExportCronJob.dataExportMediaCode</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the dataExportMediaCode - Code of the generated export media, containing the exported data
	 */
	@Accessor(qualifier = "dataExportMediaCode", type = Accessor.Type.SETTER)
	public void setDataExportMediaCode(final String value)
	{
		_dataExportMediaCode = getPersistenceContext().setValue(DATAEXPORTMEDIACODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExExportCronJob.dataExportTarget</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the dataExportTarget
	 */
	@Accessor(qualifier = "dataExportTarget", type = Accessor.Type.SETTER)
	public void setDataExportTarget(final ImpExExportMediaModel value)
	{
		_dataExportTarget = getPersistenceContext().setValue(DATAEXPORTTARGET, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExExportCronJob.encoding</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the encoding
	 */
	@Accessor(qualifier = "encoding", type = Accessor.Type.SETTER)
	public void setEncoding(final EncodingEnum value)
	{
		_encoding = getPersistenceContext().setValue(ENCODING, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExExportCronJob.export</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the export
	 */
	@Accessor(qualifier = "export", type = Accessor.Type.SETTER)
	public void setExport(final ExportModel value)
	{
		_export = getPersistenceContext().setValue(EXPORT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExExportCronJob.exportTemplate</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the exportTemplate
	 */
	@Accessor(qualifier = "exportTemplate", type = Accessor.Type.SETTER)
	public void setExportTemplate(final HeaderLibraryModel value)
	{
		_exportTemplate = getPersistenceContext().setValue(EXPORTTEMPLATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExExportCronJob.fieldSeparator</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the fieldSeparator - Character used for separating columns in CSV-lines
	 */
	@Accessor(qualifier = "fieldSeparator", type = Accessor.Type.SETTER)
	public void setFieldSeparator(final Character value)
	{
		_fieldSeparator = getPersistenceContext().setValue(FIELDSEPARATOR, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExExportCronJob.jobMedia</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the jobMedia
	 */
	@Accessor(qualifier = "jobMedia", type = Accessor.Type.SETTER)
	public void setJobMedia(final ImpExMediaModel value)
	{
		_jobMedia = getPersistenceContext().setValue(JOBMEDIA, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExExportCronJob.mediasExportMediaCode</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the mediasExportMediaCode - Code of the generated export media, containing the exported medias
	 */
	@Accessor(qualifier = "mediasExportMediaCode", type = Accessor.Type.SETTER)
	public void setMediasExportMediaCode(final String value)
	{
		_mediasExportMediaCode = getPersistenceContext().setValue(MEDIASEXPORTMEDIACODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExExportCronJob.mediasExportTarget</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the mediasExportTarget
	 */
	@Accessor(qualifier = "mediasExportTarget", type = Accessor.Type.SETTER)
	public void setMediasExportTarget(final ImpExExportMediaModel value)
	{
		_mediasExportTarget = getPersistenceContext().setValue(MEDIASEXPORTTARGET, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExExportCronJob.mode</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the mode
	 */
	@Accessor(qualifier = "mode", type = Accessor.Type.SETTER)
	public void setMode(final ImpExValidationModeEnum value)
	{
		_mode = getPersistenceContext().setValue(MODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExExportCronJob.quoteCharacter</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the quoteCharacter - Character used for escaping columns in CSV-lines
	 */
	@Accessor(qualifier = "quoteCharacter", type = Accessor.Type.SETTER)
	public void setQuoteCharacter(final Character value)
	{
		_quoteCharacter = getPersistenceContext().setValue(QUOTECHARACTER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExExportCronJob.report</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the report
	 */
	@Accessor(qualifier = "report", type = Accessor.Type.SETTER)
	public void setReport(final ReportModel value)
	{
		_report = getPersistenceContext().setValue(REPORT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ImpExExportCronJob.singleFile</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the singleFile - Export result as a single file instead of Zip archive
	 */
	@Accessor(qualifier = "singleFile", type = Accessor.Type.SETTER)
	public void setSingleFile(final Boolean value)
	{
		_singleFile = getPersistenceContext().setValue(SINGLEFILE, value);
	}
	
}

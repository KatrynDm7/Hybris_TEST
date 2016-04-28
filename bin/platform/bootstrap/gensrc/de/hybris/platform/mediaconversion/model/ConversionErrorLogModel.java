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
package de.hybris.platform.mediaconversion.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.mediaconversion.model.ConversionMediaFormatModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type ConversionErrorLog first defined at extension mediaconversion.
 */
@SuppressWarnings("all")
public class ConversionErrorLogModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "ConversionErrorLog";
	
	/**<i>Generated relation code constant for relation <code>ContainerToConversionErrorLogRel</code> defining source attribute <code>container</code> in extension <code>mediaconversion</code>.</i>*/
	public final static String _CONTAINERTOCONVERSIONERRORLOGREL = "ContainerToConversionErrorLogRel";
	
	/** <i>Generated constant</i> - Attribute key of <code>ConversionErrorLog.targetFormat</code> attribute defined at extension <code>mediaconversion</code>. */
	public static final String TARGETFORMAT = "targetFormat";
	
	/** <i>Generated constant</i> - Attribute key of <code>ConversionErrorLog.sourceMedia</code> attribute defined at extension <code>mediaconversion</code>. */
	public static final String SOURCEMEDIA = "sourceMedia";
	
	/** <i>Generated constant</i> - Attribute key of <code>ConversionErrorLog.errorMessage</code> attribute defined at extension <code>mediaconversion</code>. */
	public static final String ERRORMESSAGE = "errorMessage";
	
	/** <i>Generated constant</i> - Attribute key of <code>ConversionErrorLog.container</code> attribute defined at extension <code>mediaconversion</code>. */
	public static final String CONTAINER = "container";
	
	
	/** <i>Generated variable</i> - Variable of <code>ConversionErrorLog.targetFormat</code> attribute defined at extension <code>mediaconversion</code>. */
	private ConversionMediaFormatModel _targetFormat;
	
	/** <i>Generated variable</i> - Variable of <code>ConversionErrorLog.sourceMedia</code> attribute defined at extension <code>mediaconversion</code>. */
	private MediaModel _sourceMedia;
	
	/** <i>Generated variable</i> - Variable of <code>ConversionErrorLog.errorMessage</code> attribute defined at extension <code>mediaconversion</code>. */
	private String _errorMessage;
	
	/** <i>Generated variable</i> - Variable of <code>ConversionErrorLog.container</code> attribute defined at extension <code>mediaconversion</code>. */
	private MediaContainerModel _container;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ConversionErrorLogModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ConversionErrorLogModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _container initial attribute declared by type <code>ConversionErrorLog</code> at extension <code>mediaconversion</code>
	 * @param _targetFormat initial attribute declared by type <code>ConversionErrorLog</code> at extension <code>mediaconversion</code>
	 */
	@Deprecated
	public ConversionErrorLogModel(final MediaContainerModel _container, final ConversionMediaFormatModel _targetFormat)
	{
		super();
		setContainer(_container);
		setTargetFormat(_targetFormat);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _container initial attribute declared by type <code>ConversionErrorLog</code> at extension <code>mediaconversion</code>
	 * @param _errorMessage initial attribute declared by type <code>ConversionErrorLog</code> at extension <code>mediaconversion</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _sourceMedia initial attribute declared by type <code>ConversionErrorLog</code> at extension <code>mediaconversion</code>
	 * @param _targetFormat initial attribute declared by type <code>ConversionErrorLog</code> at extension <code>mediaconversion</code>
	 */
	@Deprecated
	public ConversionErrorLogModel(final MediaContainerModel _container, final String _errorMessage, final ItemModel _owner, final MediaModel _sourceMedia, final ConversionMediaFormatModel _targetFormat)
	{
		super();
		setContainer(_container);
		setErrorMessage(_errorMessage);
		setOwner(_owner);
		setSourceMedia(_sourceMedia);
		setTargetFormat(_targetFormat);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ConversionErrorLog.container</code> attribute defined at extension <code>mediaconversion</code>. 
	 * @return the container - The container for which the conversion failed.
	 */
	@Accessor(qualifier = "container", type = Accessor.Type.GETTER)
	public MediaContainerModel getContainer()
	{
		if (this._container!=null)
		{
			return _container;
		}
		return _container = getPersistenceContext().getValue(CONTAINER, _container);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ConversionErrorLog.errorMessage</code> attribute defined at extension <code>mediaconversion</code>. 
	 * @return the errorMessage - Technical description of the error.
	 */
	@Accessor(qualifier = "errorMessage", type = Accessor.Type.GETTER)
	public String getErrorMessage()
	{
		if (this._errorMessage!=null)
		{
			return _errorMessage;
		}
		return _errorMessage = getPersistenceContext().getValue(ERRORMESSAGE, _errorMessage);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ConversionErrorLog.sourceMedia</code> attribute defined at extension <code>mediaconversion</code>. 
	 * @return the sourceMedia - The source Media.
	 */
	@Accessor(qualifier = "sourceMedia", type = Accessor.Type.GETTER)
	public MediaModel getSourceMedia()
	{
		if (this._sourceMedia!=null)
		{
			return _sourceMedia;
		}
		return _sourceMedia = getPersistenceContext().getValue(SOURCEMEDIA, _sourceMedia);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ConversionErrorLog.targetFormat</code> attribute defined at extension <code>mediaconversion</code>. 
	 * @return the targetFormat - The targeted format, for which the conversion failed.
	 */
	@Accessor(qualifier = "targetFormat", type = Accessor.Type.GETTER)
	public ConversionMediaFormatModel getTargetFormat()
	{
		if (this._targetFormat!=null)
		{
			return _targetFormat;
		}
		return _targetFormat = getPersistenceContext().getValue(TARGETFORMAT, _targetFormat);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>ConversionErrorLog.container</code> attribute defined at extension <code>mediaconversion</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the container - The container for which the conversion failed.
	 */
	@Accessor(qualifier = "container", type = Accessor.Type.SETTER)
	public void setContainer(final MediaContainerModel value)
	{
		_container = getPersistenceContext().setValue(CONTAINER, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>ConversionErrorLog.errorMessage</code> attribute defined at extension <code>mediaconversion</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the errorMessage - Technical description of the error.
	 */
	@Accessor(qualifier = "errorMessage", type = Accessor.Type.SETTER)
	public void setErrorMessage(final String value)
	{
		_errorMessage = getPersistenceContext().setValue(ERRORMESSAGE, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>ConversionErrorLog.sourceMedia</code> attribute defined at extension <code>mediaconversion</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the sourceMedia - The source Media.
	 */
	@Accessor(qualifier = "sourceMedia", type = Accessor.Type.SETTER)
	public void setSourceMedia(final MediaModel value)
	{
		_sourceMedia = getPersistenceContext().setValue(SOURCEMEDIA, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>ConversionErrorLog.targetFormat</code> attribute defined at extension <code>mediaconversion</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the targetFormat - The targeted format, for which the conversion failed.
	 */
	@Accessor(qualifier = "targetFormat", type = Accessor.Type.SETTER)
	public void setTargetFormat(final ConversionMediaFormatModel value)
	{
		_targetFormat = getPersistenceContext().setValue(TARGETFORMAT, value);
	}
	
}

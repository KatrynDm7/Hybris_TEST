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
package de.hybris.platform.commons.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.commons.model.FormatModel;
import de.hybris.platform.commons.model.FormatterModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

/**
 * Generated model class for type MediaFormatter first defined at extension commons.
 */
@SuppressWarnings("all")
public class MediaFormatterModel extends FormatterModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "MediaFormatter";
	
	/**<i>Generated relation code constant for relation <code>Format2MedForRel</code> defining source attribute <code>formats</code> in extension <code>commons</code>.</i>*/
	public final static String _FORMAT2MEDFORREL = "Format2MedForRel";
	
	/** <i>Generated constant</i> - Attribute key of <code>MediaFormatter.inputMimeType</code> attribute defined at extension <code>commons</code>. */
	public static final String INPUTMIMETYPE = "inputMimeType";
	
	/** <i>Generated constant</i> - Attribute key of <code>MediaFormatter.formats</code> attribute defined at extension <code>commons</code>. */
	public static final String FORMATS = "formats";
	
	
	/** <i>Generated variable</i> - Variable of <code>MediaFormatter.inputMimeType</code> attribute defined at extension <code>commons</code>. */
	private String _inputMimeType;
	
	/** <i>Generated variable</i> - Variable of <code>MediaFormatter.formats</code> attribute defined at extension <code>commons</code>. */
	private Collection<FormatModel> _formats;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public MediaFormatterModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public MediaFormatterModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>Formatter</code> at extension <code>catalog</code>
	 * @param _code initial attribute declared by type <code>Media</code> at extension <code>core</code>
	 * @param _inputMimeType initial attribute declared by type <code>MediaFormatter</code> at extension <code>commons</code>
	 * @param _outputMimeType initial attribute declared by type <code>Formatter</code> at extension <code>commons</code>
	 */
	@Deprecated
	public MediaFormatterModel(final CatalogVersionModel _catalogVersion, final String _code, final String _inputMimeType, final String _outputMimeType)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setCode(_code);
		setInputMimeType(_inputMimeType);
		setOutputMimeType(_outputMimeType);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>Formatter</code> at extension <code>catalog</code>
	 * @param _code initial attribute declared by type <code>Media</code> at extension <code>core</code>
	 * @param _inputMimeType initial attribute declared by type <code>MediaFormatter</code> at extension <code>commons</code>
	 * @param _outputMimeType initial attribute declared by type <code>Formatter</code> at extension <code>commons</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public MediaFormatterModel(final CatalogVersionModel _catalogVersion, final String _code, final String _inputMimeType, final String _outputMimeType, final ItemModel _owner)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setCode(_code);
		setInputMimeType(_inputMimeType);
		setOutputMimeType(_outputMimeType);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MediaFormatter.formats</code> attribute defined at extension <code>commons</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the formats
	 */
	@Accessor(qualifier = "formats", type = Accessor.Type.GETTER)
	public Collection<FormatModel> getFormats()
	{
		if (this._formats!=null)
		{
			return _formats;
		}
		return _formats = getPersistenceContext().getValue(FORMATS, _formats);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MediaFormatter.inputMimeType</code> attribute defined at extension <code>commons</code>. 
	 * @return the inputMimeType - The mime type of the input.
	 */
	@Accessor(qualifier = "inputMimeType", type = Accessor.Type.GETTER)
	public String getInputMimeType()
	{
		if (this._inputMimeType!=null)
		{
			return _inputMimeType;
		}
		return _inputMimeType = getPersistenceContext().getValue(INPUTMIMETYPE, _inputMimeType);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MediaFormatter.formats</code> attribute defined at extension <code>commons</code>. 
	 *  
	 * @param value the formats
	 */
	@Accessor(qualifier = "formats", type = Accessor.Type.SETTER)
	public void setFormats(final Collection<FormatModel> value)
	{
		_formats = getPersistenceContext().setValue(FORMATS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>MediaFormatter.inputMimeType</code> attribute defined at extension <code>commons</code>. 
	 *  
	 * @param value the inputMimeType - The mime type of the input.
	 */
	@Accessor(qualifier = "inputMimeType", type = Accessor.Type.SETTER)
	public void setInputMimeType(final String value)
	{
		_inputMimeType = getPersistenceContext().setValue(INPUTMIMETYPE, value);
	}
	
}

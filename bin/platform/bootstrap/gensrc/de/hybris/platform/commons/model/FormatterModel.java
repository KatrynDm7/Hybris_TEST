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
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type Formatter first defined at extension commons.
 */
@SuppressWarnings("all")
public class FormatterModel extends MediaModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "Formatter";
	
	/** <i>Generated constant</i> - Attribute key of <code>Formatter.outputMimeType</code> attribute defined at extension <code>commons</code>. */
	public static final String OUTPUTMIMETYPE = "outputMimeType";
	
	/** <i>Generated constant</i> - Attribute key of <code>Formatter.script</code> attribute defined at extension <code>commons</code>. */
	public static final String SCRIPT = "script";
	
	
	/** <i>Generated variable</i> - Variable of <code>Formatter.outputMimeType</code> attribute defined at extension <code>commons</code>. */
	private String _outputMimeType;
	
	/** <i>Generated variable</i> - Variable of <code>Formatter.script</code> attribute defined at extension <code>commons</code>. */
	private String _script;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public FormatterModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public FormatterModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>Formatter</code> at extension <code>catalog</code>
	 * @param _code initial attribute declared by type <code>Media</code> at extension <code>core</code>
	 * @param _outputMimeType initial attribute declared by type <code>Formatter</code> at extension <code>commons</code>
	 */
	@Deprecated
	public FormatterModel(final CatalogVersionModel _catalogVersion, final String _code, final String _outputMimeType)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setCode(_code);
		setOutputMimeType(_outputMimeType);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>Formatter</code> at extension <code>catalog</code>
	 * @param _code initial attribute declared by type <code>Media</code> at extension <code>core</code>
	 * @param _outputMimeType initial attribute declared by type <code>Formatter</code> at extension <code>commons</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public FormatterModel(final CatalogVersionModel _catalogVersion, final String _code, final String _outputMimeType, final ItemModel _owner)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setCode(_code);
		setOutputMimeType(_outputMimeType);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Formatter.outputMimeType</code> attribute defined at extension <code>commons</code>. 
	 * @return the outputMimeType - The mime type of the output.
	 */
	@Accessor(qualifier = "outputMimeType", type = Accessor.Type.GETTER)
	public String getOutputMimeType()
	{
		if (this._outputMimeType!=null)
		{
			return _outputMimeType;
		}
		return _outputMimeType = getPersistenceContext().getValue(OUTPUTMIMETYPE, _outputMimeType);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Formatter.script</code> attribute defined at extension <code>commons</code>. 
	 * @return the script
	 */
	@Accessor(qualifier = "script", type = Accessor.Type.GETTER)
	public String getScript()
	{
		if (this._script!=null)
		{
			return _script;
		}
		return _script = getPersistenceContext().getValue(SCRIPT, _script);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Formatter.outputMimeType</code> attribute defined at extension <code>commons</code>. 
	 *  
	 * @param value the outputMimeType - The mime type of the output.
	 */
	@Accessor(qualifier = "outputMimeType", type = Accessor.Type.SETTER)
	public void setOutputMimeType(final String value)
	{
		_outputMimeType = getPersistenceContext().setValue(OUTPUTMIMETYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Formatter.script</code> attribute defined at extension <code>commons</code>. 
	 *  
	 * @param value the script
	 */
	@Accessor(qualifier = "script", type = Accessor.Type.SETTER)
	public void setScript(final String value)
	{
		_script = getPersistenceContext().setValue(SCRIPT, value);
	}
	
}

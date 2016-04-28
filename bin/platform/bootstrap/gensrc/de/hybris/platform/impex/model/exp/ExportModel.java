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
package de.hybris.platform.impex.model.exp;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.impex.model.ImpExMediaModel;
import de.hybris.platform.impex.model.exp.ImpExExportMediaModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;

/**
 * Generated model class for type Export first defined at extension impex.
 */
@SuppressWarnings("all")
public class ExportModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "Export";
	
	/** <i>Generated constant</i> - Attribute key of <code>Export.code</code> attribute defined at extension <code>impex</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>Export.exportedMedias</code> attribute defined at extension <code>impex</code>. */
	public static final String EXPORTEDMEDIAS = "exportedMedias";
	
	/** <i>Generated constant</i> - Attribute key of <code>Export.exportedData</code> attribute defined at extension <code>impex</code>. */
	public static final String EXPORTEDDATA = "exportedData";
	
	/** <i>Generated constant</i> - Attribute key of <code>Export.exportScript</code> attribute defined at extension <code>impex</code>. */
	public static final String EXPORTSCRIPT = "exportScript";
	
	/** <i>Generated constant</i> - Attribute key of <code>Export.description</code> attribute defined at extension <code>impex</code>. */
	public static final String DESCRIPTION = "description";
	
	
	/** <i>Generated variable</i> - Variable of <code>Export.code</code> attribute defined at extension <code>impex</code>. */
	private String _code;
	
	/** <i>Generated variable</i> - Variable of <code>Export.exportedMedias</code> attribute defined at extension <code>impex</code>. */
	private ImpExExportMediaModel _exportedMedias;
	
	/** <i>Generated variable</i> - Variable of <code>Export.exportedData</code> attribute defined at extension <code>impex</code>. */
	private ImpExExportMediaModel _exportedData;
	
	/** <i>Generated variable</i> - Variable of <code>Export.exportScript</code> attribute defined at extension <code>impex</code>. */
	private ImpExMediaModel _exportScript;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ExportModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ExportModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>Export</code> at extension <code>impex</code>
	 */
	@Deprecated
	public ExportModel(final String _code)
	{
		super();
		setCode(_code);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>Export</code> at extension <code>impex</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public ExportModel(final String _code, final ItemModel _owner)
	{
		super();
		setCode(_code);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Export.code</code> attribute defined at extension <code>impex</code>. 
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
	 * <i>Generated method</i> - Getter of the <code>Export.description</code> attribute defined at extension <code>impex</code>. 
	 * @return the description - description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription()
	{
		return getDescription(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>Export.description</code> attribute defined at extension <code>impex</code>. 
	 * @param loc the value localization key 
	 * @return the description - description
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(DESCRIPTION, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Export.exportedData</code> attribute defined at extension <code>impex</code>. 
	 * @return the exportedData - contains the exported data
	 */
	@Accessor(qualifier = "exportedData", type = Accessor.Type.GETTER)
	public ImpExExportMediaModel getExportedData()
	{
		if (this._exportedData!=null)
		{
			return _exportedData;
		}
		return _exportedData = getPersistenceContext().getValue(EXPORTEDDATA, _exportedData);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Export.exportedMedias</code> attribute defined at extension <code>impex</code>. 
	 * @return the exportedMedias - contains the exported medias
	 */
	@Accessor(qualifier = "exportedMedias", type = Accessor.Type.GETTER)
	public ImpExExportMediaModel getExportedMedias()
	{
		if (this._exportedMedias!=null)
		{
			return _exportedMedias;
		}
		return _exportedMedias = getPersistenceContext().getValue(EXPORTEDMEDIAS, _exportedMedias);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Export.exportScript</code> attribute defined at extension <code>impex</code>. 
	 * @return the exportScript - contains the export script
	 */
	@Accessor(qualifier = "exportScript", type = Accessor.Type.GETTER)
	public ImpExMediaModel getExportScript()
	{
		if (this._exportScript!=null)
		{
			return _exportScript;
		}
		return _exportScript = getPersistenceContext().getValue(EXPORTSCRIPT, _exportScript);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Export.code</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		_code = getPersistenceContext().setValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Export.description</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the description - description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value)
	{
		setDescription(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>Export.description</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the description - description
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(DESCRIPTION, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Export.exportedData</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the exportedData - contains the exported data
	 */
	@Accessor(qualifier = "exportedData", type = Accessor.Type.SETTER)
	public void setExportedData(final ImpExExportMediaModel value)
	{
		_exportedData = getPersistenceContext().setValue(EXPORTEDDATA, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Export.exportedMedias</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the exportedMedias - contains the exported medias
	 */
	@Accessor(qualifier = "exportedMedias", type = Accessor.Type.SETTER)
	public void setExportedMedias(final ImpExExportMediaModel value)
	{
		_exportedMedias = getPersistenceContext().setValue(EXPORTEDMEDIAS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Export.exportScript</code> attribute defined at extension <code>impex</code>. 
	 *  
	 * @param value the exportScript - contains the export script
	 */
	@Accessor(qualifier = "exportScript", type = Accessor.Type.SETTER)
	public void setExportScript(final ImpExMediaModel value)
	{
		_exportScript = getPersistenceContext().setValue(EXPORTSCRIPT, value);
	}
	
}

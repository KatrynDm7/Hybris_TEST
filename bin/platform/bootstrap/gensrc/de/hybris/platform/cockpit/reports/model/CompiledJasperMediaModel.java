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
package de.hybris.platform.cockpit.reports.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cockpit.reports.model.JasperMediaModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type CompiledJasperMedia first defined at extension cockpit.
 */
@SuppressWarnings("all")
public class CompiledJasperMediaModel extends JasperMediaModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "CompiledJasperMedia";
	
	/** <i>Generated constant</i> - Attribute key of <code>CompiledJasperMedia.compiledReport</code> attribute defined at extension <code>cockpit</code>. */
	public static final String COMPILEDREPORT = "compiledReport";
	
	
	/** <i>Generated variable</i> - Variable of <code>CompiledJasperMedia.compiledReport</code> attribute defined at extension <code>cockpit</code>. */
	private MediaModel _compiledReport;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CompiledJasperMediaModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CompiledJasperMediaModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>JasperMedia</code> at extension <code>cockpit</code>
	 * @param _code initial attribute declared by type <code>Media</code> at extension <code>core</code>
	 */
	@Deprecated
	public CompiledJasperMediaModel(final CatalogVersionModel _catalogVersion, final String _code)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setCode(_code);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogVersion initial attribute declared by type <code>JasperMedia</code> at extension <code>cockpit</code>
	 * @param _code initial attribute declared by type <code>Media</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public CompiledJasperMediaModel(final CatalogVersionModel _catalogVersion, final String _code, final ItemModel _owner)
	{
		super();
		setCatalogVersion(_catalogVersion);
		setCode(_code);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CompiledJasperMedia.compiledReport</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the compiledReport
	 */
	@Accessor(qualifier = "compiledReport", type = Accessor.Type.GETTER)
	public MediaModel getCompiledReport()
	{
		if (this._compiledReport!=null)
		{
			return _compiledReport;
		}
		return _compiledReport = getPersistenceContext().getValue(COMPILEDREPORT, _compiledReport);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CompiledJasperMedia.compiledReport</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the compiledReport
	 */
	@Accessor(qualifier = "compiledReport", type = Accessor.Type.SETTER)
	public void setCompiledReport(final MediaModel value)
	{
		_compiledReport = getPersistenceContext().setValue(COMPILEDREPORT, value);
	}
	
}

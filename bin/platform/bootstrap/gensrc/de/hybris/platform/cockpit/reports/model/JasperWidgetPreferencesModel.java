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
package de.hybris.platform.cockpit.reports.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.cockpit.model.DynamicWidgetPreferencesModel;
import de.hybris.platform.cockpit.reports.model.JasperMediaModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type JasperWidgetPreferences first defined at extension cockpit.
 */
@SuppressWarnings("all")
public class JasperWidgetPreferencesModel extends DynamicWidgetPreferencesModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "JasperWidgetPreferences";
	
	/** <i>Generated constant</i> - Attribute key of <code>JasperWidgetPreferences.report</code> attribute defined at extension <code>cockpit</code>. */
	public static final String REPORT = "report";
	
	
	/** <i>Generated variable</i> - Variable of <code>JasperWidgetPreferences.report</code> attribute defined at extension <code>cockpit</code>. */
	private JasperMediaModel _report;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public JasperWidgetPreferencesModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public JasperWidgetPreferencesModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _report initial attribute declared by type <code>JasperWidgetPreferences</code> at extension <code>cockpit</code>
	 */
	@Deprecated
	public JasperWidgetPreferencesModel(final JasperMediaModel _report)
	{
		super();
		setReport(_report);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _report initial attribute declared by type <code>JasperWidgetPreferences</code> at extension <code>cockpit</code>
	 */
	@Deprecated
	public JasperWidgetPreferencesModel(final ItemModel _owner, final JasperMediaModel _report)
	{
		super();
		setOwner(_owner);
		setReport(_report);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>JasperWidgetPreferences.report</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the report
	 */
	@Accessor(qualifier = "report", type = Accessor.Type.GETTER)
	public JasperMediaModel getReport()
	{
		if (this._report!=null)
		{
			return _report;
		}
		return _report = getPersistenceContext().getValue(REPORT, _report);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>JasperWidgetPreferences.report</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the report
	 */
	@Accessor(qualifier = "report", type = Accessor.Type.SETTER)
	public void setReport(final JasperMediaModel value)
	{
		_report = getPersistenceContext().setValue(REPORT, value);
	}
	
}

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
package de.hybris.platform.cockpit.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.cockpit.model.CockpitSavedQueryModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type CockpitSavedFacetValue first defined at extension cockpit.
 */
@SuppressWarnings("all")
public class CockpitSavedFacetValueModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "CockpitSavedFacetValue";
	
	/**<i>Generated relation code constant for relation <code>CockpitSavedQuery2CockpitSavedFacetValueRelation</code> defining source attribute <code>cockpitSavedQuery</code> in extension <code>cockpit</code>.</i>*/
	public final static String _COCKPITSAVEDQUERY2COCKPITSAVEDFACETVALUERELATION = "CockpitSavedQuery2CockpitSavedFacetValueRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>CockpitSavedFacetValue.facetQualifier</code> attribute defined at extension <code>cockpit</code>. */
	public static final String FACETQUALIFIER = "facetQualifier";
	
	/** <i>Generated constant</i> - Attribute key of <code>CockpitSavedFacetValue.valueQualifier</code> attribute defined at extension <code>cockpit</code>. */
	public static final String VALUEQUALIFIER = "valueQualifier";
	
	/** <i>Generated constant</i> - Attribute key of <code>CockpitSavedFacetValue.cockpitSavedQuery</code> attribute defined at extension <code>cockpit</code>. */
	public static final String COCKPITSAVEDQUERY = "cockpitSavedQuery";
	
	
	/** <i>Generated variable</i> - Variable of <code>CockpitSavedFacetValue.facetQualifier</code> attribute defined at extension <code>cockpit</code>. */
	private String _facetQualifier;
	
	/** <i>Generated variable</i> - Variable of <code>CockpitSavedFacetValue.valueQualifier</code> attribute defined at extension <code>cockpit</code>. */
	private String _valueQualifier;
	
	/** <i>Generated variable</i> - Variable of <code>CockpitSavedFacetValue.cockpitSavedQuery</code> attribute defined at extension <code>cockpit</code>. */
	private CockpitSavedQueryModel _cockpitSavedQuery;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CockpitSavedFacetValueModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CockpitSavedFacetValueModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _facetQualifier initial attribute declared by type <code>CockpitSavedFacetValue</code> at extension <code>cockpit</code>
	 * @param _valueQualifier initial attribute declared by type <code>CockpitSavedFacetValue</code> at extension <code>cockpit</code>
	 */
	@Deprecated
	public CockpitSavedFacetValueModel(final String _facetQualifier, final String _valueQualifier)
	{
		super();
		setFacetQualifier(_facetQualifier);
		setValueQualifier(_valueQualifier);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _facetQualifier initial attribute declared by type <code>CockpitSavedFacetValue</code> at extension <code>cockpit</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _valueQualifier initial attribute declared by type <code>CockpitSavedFacetValue</code> at extension <code>cockpit</code>
	 */
	@Deprecated
	public CockpitSavedFacetValueModel(final String _facetQualifier, final ItemModel _owner, final String _valueQualifier)
	{
		super();
		setFacetQualifier(_facetQualifier);
		setOwner(_owner);
		setValueQualifier(_valueQualifier);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CockpitSavedFacetValue.cockpitSavedQuery</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the cockpitSavedQuery
	 */
	@Accessor(qualifier = "cockpitSavedQuery", type = Accessor.Type.GETTER)
	public CockpitSavedQueryModel getCockpitSavedQuery()
	{
		if (this._cockpitSavedQuery!=null)
		{
			return _cockpitSavedQuery;
		}
		return _cockpitSavedQuery = getPersistenceContext().getValue(COCKPITSAVEDQUERY, _cockpitSavedQuery);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CockpitSavedFacetValue.facetQualifier</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the facetQualifier
	 */
	@Accessor(qualifier = "facetQualifier", type = Accessor.Type.GETTER)
	public String getFacetQualifier()
	{
		if (this._facetQualifier!=null)
		{
			return _facetQualifier;
		}
		return _facetQualifier = getPersistenceContext().getValue(FACETQUALIFIER, _facetQualifier);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CockpitSavedFacetValue.valueQualifier</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the valueQualifier
	 */
	@Accessor(qualifier = "valueQualifier", type = Accessor.Type.GETTER)
	public String getValueQualifier()
	{
		if (this._valueQualifier!=null)
		{
			return _valueQualifier;
		}
		return _valueQualifier = getPersistenceContext().getValue(VALUEQUALIFIER, _valueQualifier);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CockpitSavedFacetValue.cockpitSavedQuery</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the cockpitSavedQuery
	 */
	@Accessor(qualifier = "cockpitSavedQuery", type = Accessor.Type.SETTER)
	public void setCockpitSavedQuery(final CockpitSavedQueryModel value)
	{
		_cockpitSavedQuery = getPersistenceContext().setValue(COCKPITSAVEDQUERY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CockpitSavedFacetValue.facetQualifier</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the facetQualifier
	 */
	@Accessor(qualifier = "facetQualifier", type = Accessor.Type.SETTER)
	public void setFacetQualifier(final String value)
	{
		_facetQualifier = getPersistenceContext().setValue(FACETQUALIFIER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CockpitSavedFacetValue.valueQualifier</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the valueQualifier
	 */
	@Accessor(qualifier = "valueQualifier", type = Accessor.Type.SETTER)
	public void setValueQualifier(final String value)
	{
		_valueQualifier = getPersistenceContext().setValue(VALUEQUALIFIER, value);
	}
	
}

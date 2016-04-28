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
 * Generated model class for type CockpitSavedSortCriterion first defined at extension cockpit.
 */
@SuppressWarnings("all")
public class CockpitSavedSortCriterionModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "CockpitSavedSortCriterion";
	
	/**<i>Generated relation code constant for relation <code>CockpitSavedQuery2CockpitSavedSortCriterionRelation</code> defining source attribute <code>cockpitSavedQuery</code> in extension <code>cockpit</code>.</i>*/
	public final static String _COCKPITSAVEDQUERY2COCKPITSAVEDSORTCRITERIONRELATION = "CockpitSavedQuery2CockpitSavedSortCriterionRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>CockpitSavedSortCriterion.criterionQualifier</code> attribute defined at extension <code>cockpit</code>. */
	public static final String CRITERIONQUALIFIER = "criterionQualifier";
	
	/** <i>Generated constant</i> - Attribute key of <code>CockpitSavedSortCriterion.asc</code> attribute defined at extension <code>cockpit</code>. */
	public static final String ASC = "asc";
	
	/** <i>Generated constant</i> - Attribute key of <code>CockpitSavedSortCriterion.cockpitSavedQuery</code> attribute defined at extension <code>cockpit</code>. */
	public static final String COCKPITSAVEDQUERY = "cockpitSavedQuery";
	
	
	/** <i>Generated variable</i> - Variable of <code>CockpitSavedSortCriterion.criterionQualifier</code> attribute defined at extension <code>cockpit</code>. */
	private String _criterionQualifier;
	
	/** <i>Generated variable</i> - Variable of <code>CockpitSavedSortCriterion.asc</code> attribute defined at extension <code>cockpit</code>. */
	private Boolean _asc;
	
	/** <i>Generated variable</i> - Variable of <code>CockpitSavedSortCriterion.cockpitSavedQuery</code> attribute defined at extension <code>cockpit</code>. */
	private CockpitSavedQueryModel _cockpitSavedQuery;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CockpitSavedSortCriterionModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CockpitSavedSortCriterionModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _criterionQualifier initial attribute declared by type <code>CockpitSavedSortCriterion</code> at extension <code>cockpit</code>
	 */
	@Deprecated
	public CockpitSavedSortCriterionModel(final String _criterionQualifier)
	{
		super();
		setCriterionQualifier(_criterionQualifier);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _criterionQualifier initial attribute declared by type <code>CockpitSavedSortCriterion</code> at extension <code>cockpit</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public CockpitSavedSortCriterionModel(final String _criterionQualifier, final ItemModel _owner)
	{
		super();
		setCriterionQualifier(_criterionQualifier);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CockpitSavedSortCriterion.asc</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the asc
	 */
	@Accessor(qualifier = "asc", type = Accessor.Type.GETTER)
	public Boolean getAsc()
	{
		if (this._asc!=null)
		{
			return _asc;
		}
		return _asc = getPersistenceContext().getValue(ASC, _asc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CockpitSavedSortCriterion.cockpitSavedQuery</code> attribute defined at extension <code>cockpit</code>. 
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
	 * <i>Generated method</i> - Getter of the <code>CockpitSavedSortCriterion.criterionQualifier</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the criterionQualifier
	 */
	@Accessor(qualifier = "criterionQualifier", type = Accessor.Type.GETTER)
	public String getCriterionQualifier()
	{
		if (this._criterionQualifier!=null)
		{
			return _criterionQualifier;
		}
		return _criterionQualifier = getPersistenceContext().getValue(CRITERIONQUALIFIER, _criterionQualifier);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CockpitSavedSortCriterion.asc</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the asc
	 */
	@Accessor(qualifier = "asc", type = Accessor.Type.SETTER)
	public void setAsc(final Boolean value)
	{
		_asc = getPersistenceContext().setValue(ASC, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CockpitSavedSortCriterion.cockpitSavedQuery</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the cockpitSavedQuery
	 */
	@Accessor(qualifier = "cockpitSavedQuery", type = Accessor.Type.SETTER)
	public void setCockpitSavedQuery(final CockpitSavedQueryModel value)
	{
		_cockpitSavedQuery = getPersistenceContext().setValue(COCKPITSAVEDQUERY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CockpitSavedSortCriterion.criterionQualifier</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the criterionQualifier
	 */
	@Accessor(qualifier = "criterionQualifier", type = Accessor.Type.SETTER)
	public void setCriterionQualifier(final String value)
	{
		_criterionQualifier = getPersistenceContext().setValue(CRITERIONQUALIFIER, value);
	}
	
}

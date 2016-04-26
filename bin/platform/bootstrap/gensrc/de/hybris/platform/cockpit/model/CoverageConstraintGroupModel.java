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
package de.hybris.platform.cockpit.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.validation.model.constraints.ConstraintGroupModel;
import java.util.Set;

/**
 * Generated model class for type CoverageConstraintGroup first defined at extension validation.
 */
@SuppressWarnings("all")
public class CoverageConstraintGroupModel extends ConstraintGroupModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "CoverageConstraintGroup";
	
	/**<i>Generated relation code constant for relation <code>ComposedType2CoverageConstraintGroupRelation</code> defining source attribute <code>dedicatedTypes</code> in extension <code>validation</code>.</i>*/
	public final static String _COMPOSEDTYPE2COVERAGECONSTRAINTGROUPRELATION = "ComposedType2CoverageConstraintGroupRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>CoverageConstraintGroup.coverageDomainID</code> attribute defined at extension <code>validation</code>. */
	public static final String COVERAGEDOMAINID = "coverageDomainID";
	
	/** <i>Generated constant</i> - Attribute key of <code>CoverageConstraintGroup.dedicatedTypes</code> attribute defined at extension <code>validation</code>. */
	public static final String DEDICATEDTYPES = "dedicatedTypes";
	
	
	/** <i>Generated variable</i> - Variable of <code>CoverageConstraintGroup.coverageDomainID</code> attribute defined at extension <code>validation</code>. */
	private String _coverageDomainID;
	
	/** <i>Generated variable</i> - Variable of <code>CoverageConstraintGroup.dedicatedTypes</code> attribute defined at extension <code>validation</code>. */
	private Set<ComposedTypeModel> _dedicatedTypes;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CoverageConstraintGroupModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CoverageConstraintGroupModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _id initial attribute declared by type <code>ConstraintGroup</code> at extension <code>validation</code>
	 */
	@Deprecated
	public CoverageConstraintGroupModel(final String _id)
	{
		super();
		setId(_id);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _id initial attribute declared by type <code>ConstraintGroup</code> at extension <code>validation</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public CoverageConstraintGroupModel(final String _id, final ItemModel _owner)
	{
		super();
		setId(_id);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CoverageConstraintGroup.coverageDomainID</code> attribute defined at extension <code>validation</code>. 
	 * @return the coverageDomainID
	 */
	@Accessor(qualifier = "coverageDomainID", type = Accessor.Type.GETTER)
	public String getCoverageDomainID()
	{
		if (this._coverageDomainID!=null)
		{
			return _coverageDomainID;
		}
		return _coverageDomainID = getPersistenceContext().getValue(COVERAGEDOMAINID, _coverageDomainID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CoverageConstraintGroup.dedicatedTypes</code> attribute defined at extension <code>validation</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the dedicatedTypes
	 */
	@Accessor(qualifier = "dedicatedTypes", type = Accessor.Type.GETTER)
	public Set<ComposedTypeModel> getDedicatedTypes()
	{
		if (this._dedicatedTypes!=null)
		{
			return _dedicatedTypes;
		}
		return _dedicatedTypes = getPersistenceContext().getValue(DEDICATEDTYPES, _dedicatedTypes);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CoverageConstraintGroup.coverageDomainID</code> attribute defined at extension <code>validation</code>. 
	 *  
	 * @param value the coverageDomainID
	 */
	@Accessor(qualifier = "coverageDomainID", type = Accessor.Type.SETTER)
	public void setCoverageDomainID(final String value)
	{
		_coverageDomainID = getPersistenceContext().setValue(COVERAGEDOMAINID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CoverageConstraintGroup.dedicatedTypes</code> attribute defined at extension <code>validation</code>. 
	 *  
	 * @param value the dedicatedTypes
	 */
	@Accessor(qualifier = "dedicatedTypes", type = Accessor.Type.SETTER)
	public void setDedicatedTypes(final Set<ComposedTypeModel> value)
	{
		_dedicatedTypes = getPersistenceContext().setValue(DEDICATEDTYPES, value);
	}
	
}

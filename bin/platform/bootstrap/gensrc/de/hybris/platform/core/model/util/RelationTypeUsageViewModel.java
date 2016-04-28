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
package de.hybris.platform.core.model.util;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.RelationMetaTypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type RelationTypeUsageView first defined at extension core.
 */
@SuppressWarnings("all")
public class RelationTypeUsageViewModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "RelationTypeUsageView";
	
	/** <i>Generated constant</i> - Attribute key of <code>RelationTypeUsageView.relationType</code> attribute defined at extension <code>core</code>. */
	public static final String RELATIONTYPE = "relationType";
	
	/** <i>Generated constant</i> - Attribute key of <code>RelationTypeUsageView.composedType</code> attribute defined at extension <code>core</code>. */
	public static final String COMPOSEDTYPE = "composedType";
	
	
	/** <i>Generated variable</i> - Variable of <code>RelationTypeUsageView.relationType</code> attribute defined at extension <code>core</code>. */
	private RelationMetaTypeModel _relationType;
	
	/** <i>Generated variable</i> - Variable of <code>RelationTypeUsageView.composedType</code> attribute defined at extension <code>core</code>. */
	private ComposedTypeModel _composedType;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public RelationTypeUsageViewModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public RelationTypeUsageViewModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public RelationTypeUsageViewModel(final ItemModel _owner)
	{
		super();
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RelationTypeUsageView.composedType</code> attribute defined at extension <code>core</code>. 
	 * @return the composedType
	 */
	@Accessor(qualifier = "composedType", type = Accessor.Type.GETTER)
	public ComposedTypeModel getComposedType()
	{
		if (this._composedType!=null)
		{
			return _composedType;
		}
		return _composedType = getPersistenceContext().getValue(COMPOSEDTYPE, _composedType);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RelationTypeUsageView.relationType</code> attribute defined at extension <code>core</code>. 
	 * @return the relationType
	 */
	@Accessor(qualifier = "relationType", type = Accessor.Type.GETTER)
	public RelationMetaTypeModel getRelationType()
	{
		if (this._relationType!=null)
		{
			return _relationType;
		}
		return _relationType = getPersistenceContext().getValue(RELATIONTYPE, _relationType);
	}
	
}

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
package de.hybris.platform.advancedsavedquery.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.advancedsavedquery.model.AbstractAdvancedSavedQuerySearchParameterModel;
import de.hybris.platform.advancedsavedquery.model.WherePartModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

/**
 * Generated model class for type TypedAdvancedSavedQuerySearchParameter first defined at extension advancedsavedquery.
 */
@SuppressWarnings("all")
public class TypedAdvancedSavedQuerySearchParameterModel extends AbstractAdvancedSavedQuerySearchParameterModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "TypedAdvancedSavedQuerySearchParameter";
	
	/** <i>Generated constant</i> - Attribute key of <code>TypedAdvancedSavedQuerySearchParameter.typedSearchParameter</code> attribute defined at extension <code>advancedsavedquery</code>. */
	public static final String TYPEDSEARCHPARAMETER = "typedSearchParameter";
	
	/** <i>Generated constant</i> - Attribute key of <code>TypedAdvancedSavedQuerySearchParameter.typeAttributes</code> attribute defined at extension <code>advancedsavedquery</code>. */
	public static final String TYPEATTRIBUTES = "typeAttributes";
	
	/** <i>Generated constant</i> - Attribute key of <code>TypedAdvancedSavedQuerySearchParameter.enclosingType</code> attribute defined at extension <code>advancedsavedquery</code>. */
	public static final String ENCLOSINGTYPE = "enclosingType";
	
	
	/** <i>Generated variable</i> - Variable of <code>TypedAdvancedSavedQuerySearchParameter.typedSearchParameter</code> attribute defined at extension <code>advancedsavedquery</code>. */
	private AttributeDescriptorModel _typedSearchParameter;
	
	/** <i>Generated variable</i> - Variable of <code>TypedAdvancedSavedQuerySearchParameter.typeAttributes</code> attribute defined at extension <code>advancedsavedquery</code>. */
	private Collection<AttributeDescriptorModel> _typeAttributes;
	
	/** <i>Generated variable</i> - Variable of <code>TypedAdvancedSavedQuerySearchParameter.enclosingType</code> attribute defined at extension <code>advancedsavedquery</code>. */
	private ComposedTypeModel _enclosingType;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public TypedAdvancedSavedQuerySearchParameterModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public TypedAdvancedSavedQuerySearchParameterModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _enclosingType initial attribute declared by type <code>TypedAdvancedSavedQuerySearchParameter</code> at extension <code>advancedsavedquery</code>
	 * @param _searchParameterName initial attribute declared by type <code>TypedAdvancedSavedQuerySearchParameter</code> at extension <code>advancedsavedquery</code>
	 * @param _wherePart initial attribute declared by type <code>AbstractAdvancedSavedQuerySearchParameter</code> at extension <code>advancedsavedquery</code>
	 */
	@Deprecated
	public TypedAdvancedSavedQuerySearchParameterModel(final ComposedTypeModel _enclosingType, final String _searchParameterName, final WherePartModel _wherePart)
	{
		super();
		setEnclosingType(_enclosingType);
		setSearchParameterName(_searchParameterName);
		setWherePart(_wherePart);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _enclosingType initial attribute declared by type <code>TypedAdvancedSavedQuerySearchParameter</code> at extension <code>advancedsavedquery</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _searchParameterName initial attribute declared by type <code>TypedAdvancedSavedQuerySearchParameter</code> at extension <code>advancedsavedquery</code>
	 * @param _wherePart initial attribute declared by type <code>AbstractAdvancedSavedQuerySearchParameter</code> at extension <code>advancedsavedquery</code>
	 */
	@Deprecated
	public TypedAdvancedSavedQuerySearchParameterModel(final ComposedTypeModel _enclosingType, final ItemModel _owner, final String _searchParameterName, final WherePartModel _wherePart)
	{
		super();
		setEnclosingType(_enclosingType);
		setOwner(_owner);
		setSearchParameterName(_searchParameterName);
		setWherePart(_wherePart);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TypedAdvancedSavedQuerySearchParameter.enclosingType</code> attribute defined at extension <code>advancedsavedquery</code>. 
	 * @return the enclosingType
	 */
	@Accessor(qualifier = "enclosingType", type = Accessor.Type.GETTER)
	public ComposedTypeModel getEnclosingType()
	{
		if (this._enclosingType!=null)
		{
			return _enclosingType;
		}
		return _enclosingType = getPersistenceContext().getValue(ENCLOSINGTYPE, _enclosingType);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TypedAdvancedSavedQuerySearchParameter.typeAttributes</code> attribute defined at extension <code>advancedsavedquery</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the typeAttributes
	 */
	@Accessor(qualifier = "typeAttributes", type = Accessor.Type.GETTER)
	public Collection<AttributeDescriptorModel> getTypeAttributes()
	{
		if (this._typeAttributes!=null)
		{
			return _typeAttributes;
		}
		return _typeAttributes = getPersistenceContext().getValue(TYPEATTRIBUTES, _typeAttributes);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TypedAdvancedSavedQuerySearchParameter.typedSearchParameter</code> attribute defined at extension <code>advancedsavedquery</code>. 
	 * @return the typedSearchParameter
	 */
	@Accessor(qualifier = "typedSearchParameter", type = Accessor.Type.GETTER)
	public AttributeDescriptorModel getTypedSearchParameter()
	{
		if (this._typedSearchParameter!=null)
		{
			return _typedSearchParameter;
		}
		return _typedSearchParameter = getPersistenceContext().getValue(TYPEDSEARCHPARAMETER, _typedSearchParameter);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>TypedAdvancedSavedQuerySearchParameter.enclosingType</code> attribute defined at extension <code>advancedsavedquery</code>. 
	 *  
	 * @param value the enclosingType
	 */
	@Accessor(qualifier = "enclosingType", type = Accessor.Type.SETTER)
	public void setEnclosingType(final ComposedTypeModel value)
	{
		_enclosingType = getPersistenceContext().setValue(ENCLOSINGTYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>TypedAdvancedSavedQuerySearchParameter.typedSearchParameter</code> attribute defined at extension <code>advancedsavedquery</code>. 
	 *  
	 * @param value the typedSearchParameter
	 */
	@Accessor(qualifier = "typedSearchParameter", type = Accessor.Type.SETTER)
	public void setTypedSearchParameter(final AttributeDescriptorModel value)
	{
		_typedSearchParameter = getPersistenceContext().setValue(TYPEDSEARCHPARAMETER, value);
	}
	
}

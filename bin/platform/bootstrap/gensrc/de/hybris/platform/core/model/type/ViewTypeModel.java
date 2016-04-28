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
package de.hybris.platform.core.model.type;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.ViewAttributeDescriptorModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;
import java.util.Set;

/**
 * Generated model class for type ViewType first defined at extension core.
 */
@SuppressWarnings("all")
public class ViewTypeModel extends ComposedTypeModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "ViewType";
	
	/** <i>Generated constant</i> - Attribute key of <code>ViewType.columns</code> attribute defined at extension <code>core</code>. */
	public static final String COLUMNS = "columns";
	
	/** <i>Generated constant</i> - Attribute key of <code>ViewType.params</code> attribute defined at extension <code>core</code>. */
	public static final String PARAMS = "params";
	
	/** <i>Generated constant</i> - Attribute key of <code>ViewType.query</code> attribute defined at extension <code>core</code>. */
	public static final String QUERY = "query";
	
	
	/** <i>Generated variable</i> - Variable of <code>ViewType.columns</code> attribute defined at extension <code>core</code>. */
	private List<ViewAttributeDescriptorModel> _columns;
	
	/** <i>Generated variable</i> - Variable of <code>ViewType.params</code> attribute defined at extension <code>core</code>. */
	private Set<ViewAttributeDescriptorModel> _params;
	
	/** <i>Generated variable</i> - Variable of <code>ViewType.query</code> attribute defined at extension <code>core</code>. */
	private String _query;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ViewTypeModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ViewTypeModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogItemType initial attribute declared by type <code>ComposedType</code> at extension <code>catalog</code>
	 * @param _code initial attribute declared by type <code>Type</code> at extension <code>core</code>
	 * @param _generate initial attribute declared by type <code>TypeManagerManaged</code> at extension <code>core</code>
	 * @param _query initial attribute declared by type <code>ViewType</code> at extension <code>core</code>
	 * @param _singleton initial attribute declared by type <code>ComposedType</code> at extension <code>core</code>
	 * @param _superType initial attribute declared by type <code>ViewType</code> at extension <code>core</code>
	 */
	@Deprecated
	public ViewTypeModel(final Boolean _catalogItemType, final String _code, final Boolean _generate, final String _query, final Boolean _singleton, final ComposedTypeModel _superType)
	{
		super();
		setCatalogItemType(_catalogItemType);
		setCode(_code);
		setGenerate(_generate);
		setQuery(_query);
		setSingleton(_singleton);
		setSuperType(_superType);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _catalogItemType initial attribute declared by type <code>ComposedType</code> at extension <code>catalog</code>
	 * @param _code initial attribute declared by type <code>Type</code> at extension <code>core</code>
	 * @param _generate initial attribute declared by type <code>TypeManagerManaged</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _query initial attribute declared by type <code>ViewType</code> at extension <code>core</code>
	 * @param _singleton initial attribute declared by type <code>ComposedType</code> at extension <code>core</code>
	 * @param _superType initial attribute declared by type <code>ViewType</code> at extension <code>core</code>
	 */
	@Deprecated
	public ViewTypeModel(final Boolean _catalogItemType, final String _code, final Boolean _generate, final ItemModel _owner, final String _query, final Boolean _singleton, final ComposedTypeModel _superType)
	{
		super();
		setCatalogItemType(_catalogItemType);
		setCode(_code);
		setGenerate(_generate);
		setOwner(_owner);
		setQuery(_query);
		setSingleton(_singleton);
		setSuperType(_superType);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ViewType.columns</code> attribute defined at extension <code>core</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the columns
	 */
	@Accessor(qualifier = "columns", type = Accessor.Type.GETTER)
	public List<ViewAttributeDescriptorModel> getColumns()
	{
		if (this._columns!=null)
		{
			return _columns;
		}
		return _columns = getPersistenceContext().getValue(COLUMNS, _columns);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ViewType.params</code> attribute defined at extension <code>core</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the params
	 */
	@Accessor(qualifier = "params", type = Accessor.Type.GETTER)
	public Set<ViewAttributeDescriptorModel> getParams()
	{
		if (this._params!=null)
		{
			return _params;
		}
		return _params = getPersistenceContext().getValue(PARAMS, _params);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ViewType.query</code> attribute defined at extension <code>core</code>. 
	 * @return the query
	 */
	@Accessor(qualifier = "query", type = Accessor.Type.GETTER)
	public String getQuery()
	{
		if (this._query!=null)
		{
			return _query;
		}
		return _query = getPersistenceContext().getValue(QUERY, _query);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ViewType.columns</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the columns
	 */
	@Accessor(qualifier = "columns", type = Accessor.Type.SETTER)
	public void setColumns(final List<ViewAttributeDescriptorModel> value)
	{
		_columns = getPersistenceContext().setValue(COLUMNS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ViewType.params</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the params
	 */
	@Accessor(qualifier = "params", type = Accessor.Type.SETTER)
	public void setParams(final Set<ViewAttributeDescriptorModel> value)
	{
		_params = getPersistenceContext().setValue(PARAMS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ViewType.query</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the query
	 */
	@Accessor(qualifier = "query", type = Accessor.Type.SETTER)
	public void setQuery(final String value)
	{
		_query = getPersistenceContext().setValue(QUERY, value);
	}
	
}

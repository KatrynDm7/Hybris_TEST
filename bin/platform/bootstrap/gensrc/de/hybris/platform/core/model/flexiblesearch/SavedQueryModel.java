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
package de.hybris.platform.core.model.flexiblesearch;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;
import java.util.Map;

/**
 * Generated model class for type SavedQuery first defined at extension core.
 */
@SuppressWarnings("all")
public class SavedQueryModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "SavedQuery";
	
	/** <i>Generated constant</i> - Attribute key of <code>SavedQuery.code</code> attribute defined at extension <code>core</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>SavedQuery.name</code> attribute defined at extension <code>core</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>SavedQuery.description</code> attribute defined at extension <code>core</code>. */
	public static final String DESCRIPTION = "description";
	
	/** <i>Generated constant</i> - Attribute key of <code>SavedQuery.params</code> attribute defined at extension <code>core</code>. */
	public static final String PARAMS = "params";
	
	/** <i>Generated constant</i> - Attribute key of <code>SavedQuery.query</code> attribute defined at extension <code>core</code>. */
	public static final String QUERY = "query";
	
	/** <i>Generated constant</i> - Attribute key of <code>SavedQuery.resultType</code> attribute defined at extension <code>core</code>. */
	public static final String RESULTTYPE = "resultType";
	
	
	/** <i>Generated variable</i> - Variable of <code>SavedQuery.code</code> attribute defined at extension <code>core</code>. */
	private String _code;
	
	/** <i>Generated variable</i> - Variable of <code>SavedQuery.params</code> attribute defined at extension <code>core</code>. */
	private Map<String,TypeModel> _params;
	
	/** <i>Generated variable</i> - Variable of <code>SavedQuery.query</code> attribute defined at extension <code>core</code>. */
	private String _query;
	
	/** <i>Generated variable</i> - Variable of <code>SavedQuery.resultType</code> attribute defined at extension <code>core</code>. */
	private ComposedTypeModel _resultType;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public SavedQueryModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public SavedQueryModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>SavedQuery</code> at extension <code>core</code>
	 * @param _query initial attribute declared by type <code>SavedQuery</code> at extension <code>core</code>
	 * @param _resultType initial attribute declared by type <code>SavedQuery</code> at extension <code>core</code>
	 */
	@Deprecated
	public SavedQueryModel(final String _code, final String _query, final ComposedTypeModel _resultType)
	{
		super();
		setCode(_code);
		setQuery(_query);
		setResultType(_resultType);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>SavedQuery</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _query initial attribute declared by type <code>SavedQuery</code> at extension <code>core</code>
	 * @param _resultType initial attribute declared by type <code>SavedQuery</code> at extension <code>core</code>
	 */
	@Deprecated
	public SavedQueryModel(final String _code, final ItemModel _owner, final String _query, final ComposedTypeModel _resultType)
	{
		super();
		setCode(_code);
		setOwner(_owner);
		setQuery(_query);
		setResultType(_resultType);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SavedQuery.code</code> attribute defined at extension <code>core</code>. 
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
	 * <i>Generated method</i> - Getter of the <code>SavedQuery.description</code> attribute defined at extension <code>core</code>. 
	 * @return the description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription()
	{
		return getDescription(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>SavedQuery.description</code> attribute defined at extension <code>core</code>. 
	 * @param loc the value localization key 
	 * @return the description
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(DESCRIPTION, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SavedQuery.name</code> attribute defined at extension <code>core</code>. 
	 * @return the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getName(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>SavedQuery.name</code> attribute defined at extension <code>core</code>. 
	 * @param loc the value localization key 
	 * @return the name
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(NAME, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SavedQuery.params</code> attribute defined at extension <code>core</code>. 
	 * @return the params
	 */
	@Accessor(qualifier = "params", type = Accessor.Type.GETTER)
	public Map<String,TypeModel> getParams()
	{
		if (this._params!=null)
		{
			return _params;
		}
		return _params = getPersistenceContext().getValue(PARAMS, _params);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SavedQuery.query</code> attribute defined at extension <code>core</code>. 
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
	 * <i>Generated method</i> - Getter of the <code>SavedQuery.resultType</code> attribute defined at extension <code>core</code>. 
	 * @return the resultType
	 */
	@Accessor(qualifier = "resultType", type = Accessor.Type.GETTER)
	public ComposedTypeModel getResultType()
	{
		if (this._resultType!=null)
		{
			return _resultType;
		}
		return _resultType = getPersistenceContext().getValue(RESULTTYPE, _resultType);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SavedQuery.code</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		_code = getPersistenceContext().setValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SavedQuery.description</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value)
	{
		setDescription(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>SavedQuery.description</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the description
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(DESCRIPTION, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SavedQuery.name</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		setName(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>SavedQuery.name</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the name
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(NAME, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SavedQuery.params</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the params
	 */
	@Accessor(qualifier = "params", type = Accessor.Type.SETTER)
	public void setParams(final Map<String,TypeModel> value)
	{
		_params = getPersistenceContext().setValue(PARAMS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SavedQuery.query</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the query
	 */
	@Accessor(qualifier = "query", type = Accessor.Type.SETTER)
	public void setQuery(final String value)
	{
		_query = getPersistenceContext().setValue(QUERY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SavedQuery.resultType</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the resultType
	 */
	@Accessor(qualifier = "resultType", type = Accessor.Type.SETTER)
	public void setResultType(final ComposedTypeModel value)
	{
		_resultType = getPersistenceContext().setValue(RESULTTYPE, value);
	}
	
}

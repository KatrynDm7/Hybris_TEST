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
package de.hybris.platform.core.model.type;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.TypeManagerManagedModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type SearchRestriction first defined at extension core.
 */
@SuppressWarnings("all")
public class SearchRestrictionModel extends TypeManagerManagedModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "SearchRestriction";
	
	/**<i>Generated relation code constant for relation <code>Principal2SearchRestrictionRelation</code> defining source attribute <code>principal</code> in extension <code>core</code>.</i>*/
	public final static String _PRINCIPAL2SEARCHRESTRICTIONRELATION = "Principal2SearchRestrictionRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>SearchRestriction.code</code> attribute defined at extension <code>core</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>SearchRestriction.active</code> attribute defined at extension <code>core</code>. */
	public static final String ACTIVE = "active";
	
	/** <i>Generated constant</i> - Attribute key of <code>SearchRestriction.principal</code> attribute defined at extension <code>core</code>. */
	public static final String PRINCIPAL = "principal";
	
	/** <i>Generated constant</i> - Attribute key of <code>SearchRestriction.query</code> attribute defined at extension <code>core</code>. */
	public static final String QUERY = "query";
	
	/** <i>Generated constant</i> - Attribute key of <code>SearchRestriction.restrictedType</code> attribute defined at extension <code>core</code>. */
	public static final String RESTRICTEDTYPE = "restrictedType";
	
	
	/** <i>Generated variable</i> - Variable of <code>SearchRestriction.code</code> attribute defined at extension <code>core</code>. */
	private String _code;
	
	/** <i>Generated variable</i> - Variable of <code>SearchRestriction.active</code> attribute defined at extension <code>core</code>. */
	private Boolean _active;
	
	/** <i>Generated variable</i> - Variable of <code>SearchRestriction.principal</code> attribute defined at extension <code>core</code>. */
	private PrincipalModel _principal;
	
	/** <i>Generated variable</i> - Variable of <code>SearchRestriction.query</code> attribute defined at extension <code>core</code>. */
	private String _query;
	
	/** <i>Generated variable</i> - Variable of <code>SearchRestriction.restrictedType</code> attribute defined at extension <code>core</code>. */
	private ComposedTypeModel _restrictedType;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public SearchRestrictionModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public SearchRestrictionModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>SearchRestriction</code> at extension <code>core</code>
	 * @param _generate initial attribute declared by type <code>TypeManagerManaged</code> at extension <code>core</code>
	 * @param _principal initial attribute declared by type <code>SearchRestriction</code> at extension <code>core</code>
	 * @param _query initial attribute declared by type <code>SearchRestriction</code> at extension <code>core</code>
	 * @param _restrictedType initial attribute declared by type <code>SearchRestriction</code> at extension <code>core</code>
	 */
	@Deprecated
	public SearchRestrictionModel(final String _code, final Boolean _generate, final PrincipalModel _principal, final String _query, final ComposedTypeModel _restrictedType)
	{
		super();
		setCode(_code);
		setGenerate(_generate);
		setPrincipal(_principal);
		setQuery(_query);
		setRestrictedType(_restrictedType);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>SearchRestriction</code> at extension <code>core</code>
	 * @param _generate initial attribute declared by type <code>TypeManagerManaged</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _principal initial attribute declared by type <code>SearchRestriction</code> at extension <code>core</code>
	 * @param _query initial attribute declared by type <code>SearchRestriction</code> at extension <code>core</code>
	 * @param _restrictedType initial attribute declared by type <code>SearchRestriction</code> at extension <code>core</code>
	 */
	@Deprecated
	public SearchRestrictionModel(final String _code, final Boolean _generate, final ItemModel _owner, final PrincipalModel _principal, final String _query, final ComposedTypeModel _restrictedType)
	{
		super();
		setCode(_code);
		setGenerate(_generate);
		setOwner(_owner);
		setPrincipal(_principal);
		setQuery(_query);
		setRestrictedType(_restrictedType);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SearchRestriction.active</code> attribute defined at extension <code>core</code>. 
	 * @return the active
	 */
	@Accessor(qualifier = "active", type = Accessor.Type.GETTER)
	public Boolean getActive()
	{
		if (this._active!=null)
		{
			return _active;
		}
		return _active = getPersistenceContext().getValue(ACTIVE, _active);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SearchRestriction.code</code> attribute defined at extension <code>core</code>. 
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
	 * <i>Generated method</i> - Getter of the <code>SearchRestriction.principal</code> attribute defined at extension <code>core</code>. 
	 * @return the principal
	 */
	@Accessor(qualifier = "principal", type = Accessor.Type.GETTER)
	public PrincipalModel getPrincipal()
	{
		if (this._principal!=null)
		{
			return _principal;
		}
		return _principal = getPersistenceContext().getValue(PRINCIPAL, _principal);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SearchRestriction.query</code> attribute defined at extension <code>core</code>. 
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
	 * <i>Generated method</i> - Getter of the <code>SearchRestriction.restrictedType</code> attribute defined at extension <code>core</code>. 
	 * @return the restrictedType
	 */
	@Accessor(qualifier = "restrictedType", type = Accessor.Type.GETTER)
	public ComposedTypeModel getRestrictedType()
	{
		if (this._restrictedType!=null)
		{
			return _restrictedType;
		}
		return _restrictedType = getPersistenceContext().getValue(RESTRICTEDTYPE, _restrictedType);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SearchRestriction.active</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the active
	 */
	@Accessor(qualifier = "active", type = Accessor.Type.SETTER)
	public void setActive(final Boolean value)
	{
		_active = getPersistenceContext().setValue(ACTIVE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SearchRestriction.code</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		_code = getPersistenceContext().setValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SearchRestriction.principal</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the principal
	 */
	@Accessor(qualifier = "principal", type = Accessor.Type.SETTER)
	public void setPrincipal(final PrincipalModel value)
	{
		_principal = getPersistenceContext().setValue(PRINCIPAL, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SearchRestriction.query</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the query
	 */
	@Accessor(qualifier = "query", type = Accessor.Type.SETTER)
	public void setQuery(final String value)
	{
		_query = getPersistenceContext().setValue(QUERY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>SearchRestriction.restrictedType</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the restrictedType
	 */
	@Accessor(qualifier = "restrictedType", type = Accessor.Type.SETTER)
	public void setRestrictedType(final ComposedTypeModel value)
	{
		_restrictedType = getPersistenceContext().setValue(RESTRICTEDTYPE, value);
	}
	
}

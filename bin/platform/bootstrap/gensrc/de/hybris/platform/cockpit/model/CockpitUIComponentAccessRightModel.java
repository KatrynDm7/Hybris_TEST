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
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

/**
 * Generated model class for type CockpitUIComponentAccessRight first defined at extension cockpit.
 */
@SuppressWarnings("all")
public class CockpitUIComponentAccessRightModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "CockpitUIComponentAccessRight";
	
	/** <i>Generated constant</i> - Attribute key of <code>CockpitUIComponentAccessRight.code</code> attribute defined at extension <code>cockpit</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>CockpitUIComponentAccessRight.readPrincipals</code> attribute defined at extension <code>cockpit</code>. */
	public static final String READPRINCIPALS = "readPrincipals";
	
	/** <i>Generated constant</i> - Attribute key of <code>CockpitUIComponentAccessRight.writePrincipals</code> attribute defined at extension <code>cockpit</code>. */
	public static final String WRITEPRINCIPALS = "writePrincipals";
	
	
	/** <i>Generated variable</i> - Variable of <code>CockpitUIComponentAccessRight.code</code> attribute defined at extension <code>cockpit</code>. */
	private String _code;
	
	/** <i>Generated variable</i> - Variable of <code>CockpitUIComponentAccessRight.readPrincipals</code> attribute defined at extension <code>cockpit</code>. */
	private Collection<PrincipalModel> _readPrincipals;
	
	/** <i>Generated variable</i> - Variable of <code>CockpitUIComponentAccessRight.writePrincipals</code> attribute defined at extension <code>cockpit</code>. */
	private Collection<PrincipalModel> _writePrincipals;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CockpitUIComponentAccessRightModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CockpitUIComponentAccessRightModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>CockpitUIComponentAccessRight</code> at extension <code>cockpit</code>
	 */
	@Deprecated
	public CockpitUIComponentAccessRightModel(final String _code)
	{
		super();
		setCode(_code);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>CockpitUIComponentAccessRight</code> at extension <code>cockpit</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public CockpitUIComponentAccessRightModel(final String _code, final ItemModel _owner)
	{
		super();
		setCode(_code);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CockpitUIComponentAccessRight.code</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the code - Code of component
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
	 * <i>Generated method</i> - Getter of the <code>CockpitUIComponentAccessRight.readPrincipals</code> attribute defined at extension <code>cockpit</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the readPrincipals
	 */
	@Accessor(qualifier = "readPrincipals", type = Accessor.Type.GETTER)
	public Collection<PrincipalModel> getReadPrincipals()
	{
		if (this._readPrincipals!=null)
		{
			return _readPrincipals;
		}
		return _readPrincipals = getPersistenceContext().getValue(READPRINCIPALS, _readPrincipals);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CockpitUIComponentAccessRight.writePrincipals</code> attribute defined at extension <code>cockpit</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the writePrincipals
	 */
	@Accessor(qualifier = "writePrincipals", type = Accessor.Type.GETTER)
	public Collection<PrincipalModel> getWritePrincipals()
	{
		if (this._writePrincipals!=null)
		{
			return _writePrincipals;
		}
		return _writePrincipals = getPersistenceContext().getValue(WRITEPRINCIPALS, _writePrincipals);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CockpitUIComponentAccessRight.code</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the code - Code of component
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		_code = getPersistenceContext().setValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CockpitUIComponentAccessRight.readPrincipals</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the readPrincipals
	 */
	@Accessor(qualifier = "readPrincipals", type = Accessor.Type.SETTER)
	public void setReadPrincipals(final Collection<PrincipalModel> value)
	{
		_readPrincipals = getPersistenceContext().setValue(READPRINCIPALS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CockpitUIComponentAccessRight.writePrincipals</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the writePrincipals
	 */
	@Accessor(qualifier = "writePrincipals", type = Accessor.Type.SETTER)
	public void setWritePrincipals(final Collection<PrincipalModel> value)
	{
		_writePrincipals = getPersistenceContext().setValue(WRITEPRINCIPALS, value);
	}
	
}

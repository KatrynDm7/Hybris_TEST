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
package de.hybris.platform.core.model.security;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;
import java.util.Set;

/**
 * Generated model class for type PrincipalGroup first defined at extension core.
 */
@SuppressWarnings("all")
public class PrincipalGroupModel extends PrincipalModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "PrincipalGroup";
	
	/**<i>Generated relation code constant for relation <code>PrincipalGroupRelation</code> defining source attribute <code>members</code> in extension <code>core</code>.</i>*/
	public final static String _PRINCIPALGROUPRELATION = "PrincipalGroupRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>PrincipalGroup.locName</code> attribute defined at extension <code>core</code>. */
	public static final String LOCNAME = "locName";
	
	/** <i>Generated constant</i> - Attribute key of <code>PrincipalGroup.members</code> attribute defined at extension <code>core</code>. */
	public static final String MEMBERS = "members";
	
	
	/** <i>Generated variable</i> - Variable of <code>PrincipalGroup.members</code> attribute defined at extension <code>core</code>. */
	private Set<PrincipalModel> _members;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public PrincipalGroupModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public PrincipalGroupModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _uid initial attribute declared by type <code>Principal</code> at extension <code>core</code>
	 */
	@Deprecated
	public PrincipalGroupModel(final String _uid)
	{
		super();
		setUid(_uid);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _uid initial attribute declared by type <code>Principal</code> at extension <code>core</code>
	 */
	@Deprecated
	public PrincipalGroupModel(final ItemModel _owner, final String _uid)
	{
		super();
		setOwner(_owner);
		setUid(_uid);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PrincipalGroup.locName</code> attribute defined at extension <code>core</code>. 
	 * @return the locName
	 * @deprecated use {@link #getLocName()} instead
	 */
	@Deprecated
	public String getLocname()
	{
		return this.getLocName();
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>PrincipalGroup.locName</code> attribute defined at extension <code>core</code>. 
	 * @return the locName
	 * @deprecated use {@link #getLocName(Locale)} instead
	 */
	@Deprecated
	public String getLocname(final Locale loc)
	{
		return this.getLocName(loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PrincipalGroup.locName</code> attribute defined at extension <code>core</code>. 
	 * @return the locName
	 */
	@Accessor(qualifier = "locName", type = Accessor.Type.GETTER)
	public String getLocName()
	{
		return getLocName(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>PrincipalGroup.locName</code> attribute defined at extension <code>core</code>. 
	 * @param loc the value localization key 
	 * @return the locName
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "locName", type = Accessor.Type.GETTER)
	public String getLocName(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(LOCNAME, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PrincipalGroup.members</code> attribute defined at extension <code>core</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the members
	 */
	@Accessor(qualifier = "members", type = Accessor.Type.GETTER)
	public Set<PrincipalModel> getMembers()
	{
		if (this._members!=null)
		{
			return _members;
		}
		return _members = getPersistenceContext().getValue(MEMBERS, _members);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PrincipalGroup.locName</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the locName
	 * @deprecated use {@link #setLocName(java.lang.String)} instead
	 */
	@Deprecated
	public void setLocname(final String value)
	{
		this.setLocName(value);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>PrincipalGroup.locName</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the locName
	 * @deprecated use {@link #setLocName(java.lang.String,Locale)} instead
	 */
	@Deprecated
	public void setLocname(final String value, final Locale loc)
	{
		this.setLocName(value,loc);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PrincipalGroup.locName</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the locName
	 */
	@Accessor(qualifier = "locName", type = Accessor.Type.SETTER)
	public void setLocName(final String value)
	{
		setLocName(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>PrincipalGroup.locName</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the locName
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "locName", type = Accessor.Type.SETTER)
	public void setLocName(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(LOCNAME, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PrincipalGroup.members</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the members
	 */
	@Accessor(qualifier = "members", type = Accessor.Type.SETTER)
	public void setMembers(final Set<PrincipalModel> value)
	{
		_members = getPersistenceContext().setValue(MEMBERS, value);
	}
	
}

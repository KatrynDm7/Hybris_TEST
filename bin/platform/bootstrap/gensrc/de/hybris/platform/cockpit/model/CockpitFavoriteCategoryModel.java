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
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;

/**
 * Generated model class for type CockpitFavoriteCategory first defined at extension cockpit.
 */
@SuppressWarnings("all")
public class CockpitFavoriteCategoryModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "CockpitFavoriteCategory";
	
	/**<i>Generated relation code constant for relation <code>User2CockpitFavoriteCategoryRelation</code> defining source attribute <code>user</code> in extension <code>cockpit</code>.</i>*/
	public final static String _USER2COCKPITFAVORITECATEGORYRELATION = "User2CockpitFavoriteCategoryRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>CockpitFavoriteCategory.qualifier</code> attribute defined at extension <code>cockpit</code>. */
	public static final String QUALIFIER = "qualifier";
	
	/** <i>Generated constant</i> - Attribute key of <code>CockpitFavoriteCategory.label</code> attribute defined at extension <code>cockpit</code>. */
	public static final String LABEL = "label";
	
	/** <i>Generated constant</i> - Attribute key of <code>CockpitFavoriteCategory.description</code> attribute defined at extension <code>cockpit</code>. */
	public static final String DESCRIPTION = "description";
	
	/** <i>Generated constant</i> - Attribute key of <code>CockpitFavoriteCategory.user</code> attribute defined at extension <code>cockpit</code>. */
	public static final String USER = "user";
	
	
	/** <i>Generated variable</i> - Variable of <code>CockpitFavoriteCategory.qualifier</code> attribute defined at extension <code>cockpit</code>. */
	private String _qualifier;
	
	/** <i>Generated variable</i> - Variable of <code>CockpitFavoriteCategory.user</code> attribute defined at extension <code>cockpit</code>. */
	private UserModel _user;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CockpitFavoriteCategoryModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CockpitFavoriteCategoryModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _qualifier initial attribute declared by type <code>CockpitFavoriteCategory</code> at extension <code>cockpit</code>
	 */
	@Deprecated
	public CockpitFavoriteCategoryModel(final String _qualifier)
	{
		super();
		setQualifier(_qualifier);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _qualifier initial attribute declared by type <code>CockpitFavoriteCategory</code> at extension <code>cockpit</code>
	 */
	@Deprecated
	public CockpitFavoriteCategoryModel(final ItemModel _owner, final String _qualifier)
	{
		super();
		setOwner(_owner);
		setQualifier(_qualifier);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CockpitFavoriteCategory.description</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription()
	{
		return getDescription(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>CockpitFavoriteCategory.description</code> attribute defined at extension <code>cockpit</code>. 
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
	 * <i>Generated method</i> - Getter of the <code>CockpitFavoriteCategory.label</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the label
	 */
	@Accessor(qualifier = "label", type = Accessor.Type.GETTER)
	public String getLabel()
	{
		return getLabel(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>CockpitFavoriteCategory.label</code> attribute defined at extension <code>cockpit</code>. 
	 * @param loc the value localization key 
	 * @return the label
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "label", type = Accessor.Type.GETTER)
	public String getLabel(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(LABEL, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CockpitFavoriteCategory.qualifier</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the qualifier
	 */
	@Accessor(qualifier = "qualifier", type = Accessor.Type.GETTER)
	public String getQualifier()
	{
		if (this._qualifier!=null)
		{
			return _qualifier;
		}
		return _qualifier = getPersistenceContext().getValue(QUALIFIER, _qualifier);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CockpitFavoriteCategory.user</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the user
	 */
	@Accessor(qualifier = "user", type = Accessor.Type.GETTER)
	public UserModel getUser()
	{
		if (this._user!=null)
		{
			return _user;
		}
		return _user = getPersistenceContext().getValue(USER, _user);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CockpitFavoriteCategory.description</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value)
	{
		setDescription(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>CockpitFavoriteCategory.description</code> attribute defined at extension <code>cockpit</code>. 
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
	 * <i>Generated method</i> - Setter of <code>CockpitFavoriteCategory.label</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the label
	 */
	@Accessor(qualifier = "label", type = Accessor.Type.SETTER)
	public void setLabel(final String value)
	{
		setLabel(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>CockpitFavoriteCategory.label</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the label
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "label", type = Accessor.Type.SETTER)
	public void setLabel(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(LABEL, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CockpitFavoriteCategory.qualifier</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the qualifier
	 */
	@Accessor(qualifier = "qualifier", type = Accessor.Type.SETTER)
	public void setQualifier(final String value)
	{
		_qualifier = getPersistenceContext().setValue(QUALIFIER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CockpitFavoriteCategory.user</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the user
	 */
	@Accessor(qualifier = "user", type = Accessor.Type.SETTER)
	public void setUser(final UserModel value)
	{
		_user = getPersistenceContext().setValue(USER, value);
	}
	
}

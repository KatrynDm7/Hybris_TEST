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
import de.hybris.platform.cockpit.model.ObjectCollectionElementModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

/**
 * Generated model class for type CockpitObjectAbstractCollection first defined at extension cockpit.
 */
@SuppressWarnings("all")
public class CockpitObjectAbstractCollectionModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "CockpitObjectAbstractCollection";
	
	/**<i>Generated relation code constant for relation <code>User2CockpitObjectAbstractCollectionRelation</code> defining source attribute <code>user</code> in extension <code>cockpit</code>.</i>*/
	public final static String _USER2COCKPITOBJECTABSTRACTCOLLECTIONRELATION = "User2CockpitObjectAbstractCollectionRelation";
	
	/**<i>Generated relation code constant for relation <code>ReadPrincipal2CockpitObjectAbstractCollectionRelation</code> defining source attribute <code>readPrincipals</code> in extension <code>cockpit</code>.</i>*/
	public final static String _READPRINCIPAL2COCKPITOBJECTABSTRACTCOLLECTIONRELATION = "ReadPrincipal2CockpitObjectAbstractCollectionRelation";
	
	/**<i>Generated relation code constant for relation <code>WritePrincipal2CockpitObjectAbstractCollectionRelation</code> defining source attribute <code>writePrincipals</code> in extension <code>cockpit</code>.</i>*/
	public final static String _WRITEPRINCIPAL2COCKPITOBJECTABSTRACTCOLLECTIONRELATION = "WritePrincipal2CockpitObjectAbstractCollectionRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>CockpitObjectAbstractCollection.qualifier</code> attribute defined at extension <code>cockpit</code>. */
	public static final String QUALIFIER = "qualifier";
	
	/** <i>Generated constant</i> - Attribute key of <code>CockpitObjectAbstractCollection.label</code> attribute defined at extension <code>cockpit</code>. */
	public static final String LABEL = "label";
	
	/** <i>Generated constant</i> - Attribute key of <code>CockpitObjectAbstractCollection.description</code> attribute defined at extension <code>cockpit</code>. */
	public static final String DESCRIPTION = "description";
	
	/** <i>Generated constant</i> - Attribute key of <code>CockpitObjectAbstractCollection.user</code> attribute defined at extension <code>cockpit</code>. */
	public static final String USER = "user";
	
	/** <i>Generated constant</i> - Attribute key of <code>CockpitObjectAbstractCollection.readPrincipals</code> attribute defined at extension <code>cockpit</code>. */
	public static final String READPRINCIPALS = "readPrincipals";
	
	/** <i>Generated constant</i> - Attribute key of <code>CockpitObjectAbstractCollection.writePrincipals</code> attribute defined at extension <code>cockpit</code>. */
	public static final String WRITEPRINCIPALS = "writePrincipals";
	
	/** <i>Generated constant</i> - Attribute key of <code>CockpitObjectAbstractCollection.elements</code> attribute defined at extension <code>cockpit</code>. */
	public static final String ELEMENTS = "elements";
	
	
	/** <i>Generated variable</i> - Variable of <code>CockpitObjectAbstractCollection.qualifier</code> attribute defined at extension <code>cockpit</code>. */
	private String _qualifier;
	
	/** <i>Generated variable</i> - Variable of <code>CockpitObjectAbstractCollection.user</code> attribute defined at extension <code>cockpit</code>. */
	private UserModel _user;
	
	/** <i>Generated variable</i> - Variable of <code>CockpitObjectAbstractCollection.readPrincipals</code> attribute defined at extension <code>cockpit</code>. */
	private Collection<PrincipalModel> _readPrincipals;
	
	/** <i>Generated variable</i> - Variable of <code>CockpitObjectAbstractCollection.writePrincipals</code> attribute defined at extension <code>cockpit</code>. */
	private Collection<PrincipalModel> _writePrincipals;
	
	/** <i>Generated variable</i> - Variable of <code>CockpitObjectAbstractCollection.elements</code> attribute defined at extension <code>cockpit</code>. */
	private List<ObjectCollectionElementModel> _elements;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CockpitObjectAbstractCollectionModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CockpitObjectAbstractCollectionModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _qualifier initial attribute declared by type <code>CockpitObjectAbstractCollection</code> at extension <code>cockpit</code>
	 */
	@Deprecated
	public CockpitObjectAbstractCollectionModel(final String _qualifier)
	{
		super();
		setQualifier(_qualifier);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _qualifier initial attribute declared by type <code>CockpitObjectAbstractCollection</code> at extension <code>cockpit</code>
	 */
	@Deprecated
	public CockpitObjectAbstractCollectionModel(final ItemModel _owner, final String _qualifier)
	{
		super();
		setOwner(_owner);
		setQualifier(_qualifier);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CockpitObjectAbstractCollection.description</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription()
	{
		return getDescription(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>CockpitObjectAbstractCollection.description</code> attribute defined at extension <code>cockpit</code>. 
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
	 * <i>Generated method</i> - Getter of the <code>CockpitObjectAbstractCollection.elements</code> attribute defined at extension <code>cockpit</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the elements
	 */
	@Accessor(qualifier = "elements", type = Accessor.Type.GETTER)
	public List<ObjectCollectionElementModel> getElements()
	{
		if (this._elements!=null)
		{
			return _elements;
		}
		return _elements = getPersistenceContext().getValue(ELEMENTS, _elements);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CockpitObjectAbstractCollection.label</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the label
	 */
	@Accessor(qualifier = "label", type = Accessor.Type.GETTER)
	public String getLabel()
	{
		return getLabel(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>CockpitObjectAbstractCollection.label</code> attribute defined at extension <code>cockpit</code>. 
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
	 * <i>Generated method</i> - Getter of the <code>CockpitObjectAbstractCollection.qualifier</code> attribute defined at extension <code>cockpit</code>. 
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
	 * <i>Generated method</i> - Getter of the <code>CockpitObjectAbstractCollection.readPrincipals</code> attribute defined at extension <code>cockpit</code>. 
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
	 * <i>Generated method</i> - Getter of the <code>CockpitObjectAbstractCollection.user</code> attribute defined at extension <code>cockpit</code>. 
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
	 * <i>Generated method</i> - Getter of the <code>CockpitObjectAbstractCollection.writePrincipals</code> attribute defined at extension <code>cockpit</code>. 
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
	 * <i>Generated method</i> - Setter of <code>CockpitObjectAbstractCollection.description</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value)
	{
		setDescription(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>CockpitObjectAbstractCollection.description</code> attribute defined at extension <code>cockpit</code>. 
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
	 * <i>Generated method</i> - Setter of <code>CockpitObjectAbstractCollection.elements</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the elements
	 */
	@Accessor(qualifier = "elements", type = Accessor.Type.SETTER)
	public void setElements(final List<ObjectCollectionElementModel> value)
	{
		_elements = getPersistenceContext().setValue(ELEMENTS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CockpitObjectAbstractCollection.label</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the label
	 */
	@Accessor(qualifier = "label", type = Accessor.Type.SETTER)
	public void setLabel(final String value)
	{
		setLabel(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>CockpitObjectAbstractCollection.label</code> attribute defined at extension <code>cockpit</code>. 
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
	 * <i>Generated method</i> - Setter of <code>CockpitObjectAbstractCollection.qualifier</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the qualifier
	 */
	@Accessor(qualifier = "qualifier", type = Accessor.Type.SETTER)
	public void setQualifier(final String value)
	{
		_qualifier = getPersistenceContext().setValue(QUALIFIER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CockpitObjectAbstractCollection.readPrincipals</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the readPrincipals
	 */
	@Accessor(qualifier = "readPrincipals", type = Accessor.Type.SETTER)
	public void setReadPrincipals(final Collection<PrincipalModel> value)
	{
		_readPrincipals = getPersistenceContext().setValue(READPRINCIPALS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CockpitObjectAbstractCollection.user</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the user
	 */
	@Accessor(qualifier = "user", type = Accessor.Type.SETTER)
	public void setUser(final UserModel value)
	{
		_user = getPersistenceContext().setValue(USER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CockpitObjectAbstractCollection.writePrincipals</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the writePrincipals
	 */
	@Accessor(qualifier = "writePrincipals", type = Accessor.Type.SETTER)
	public void setWritePrincipals(final Collection<PrincipalModel> value)
	{
		_writePrincipals = getPersistenceContext().setValue(WRITEPRINCIPALS, value);
	}
	
}

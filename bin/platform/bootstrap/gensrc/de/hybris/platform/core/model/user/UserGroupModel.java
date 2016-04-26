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
package de.hybris.platform.core.model.user;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.europe1.enums.UserDiscountGroup;
import de.hybris.platform.europe1.enums.UserPriceGroup;
import de.hybris.platform.europe1.enums.UserTaxGroup;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

/**
 * Generated model class for type UserGroup first defined at extension core.
 */
@SuppressWarnings("all")
public class UserGroupModel extends PrincipalGroupModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "UserGroup";
	
	/** <i>Generated constant</i> - Attribute key of <code>UserGroup.writeableLanguages</code> attribute defined at extension <code>core</code>. */
	public static final String WRITEABLELANGUAGES = "writeableLanguages";
	
	/** <i>Generated constant</i> - Attribute key of <code>UserGroup.readableLanguages</code> attribute defined at extension <code>core</code>. */
	public static final String READABLELANGUAGES = "readableLanguages";
	
	/** <i>Generated constant</i> - Attribute key of <code>UserGroup.hmcXML</code> attribute defined at extension <code>core</code>. */
	public static final String HMCXML = "hmcXML";
	
	/** <i>Generated constant</i> - Attribute key of <code>UserGroup.userDiscountGroup</code> attribute defined at extension <code>europe1</code>. */
	public static final String USERDISCOUNTGROUP = "userDiscountGroup";
	
	/** <i>Generated constant</i> - Attribute key of <code>UserGroup.userPriceGroup</code> attribute defined at extension <code>europe1</code>. */
	public static final String USERPRICEGROUP = "userPriceGroup";
	
	/** <i>Generated constant</i> - Attribute key of <code>UserGroup.userTaxGroup</code> attribute defined at extension <code>europe1</code>. */
	public static final String USERTAXGROUP = "userTaxGroup";
	
	/** <i>Generated constant</i> - Attribute key of <code>UserGroup.hmcLoginDisabled</code> attribute defined at extension <code>hmc</code>. */
	public static final String HMCLOGINDISABLED = "hmcLoginDisabled";
	
	
	/** <i>Generated variable</i> - Variable of <code>UserGroup.writeableLanguages</code> attribute defined at extension <code>core</code>. */
	private Collection<LanguageModel> _writeableLanguages;
	
	/** <i>Generated variable</i> - Variable of <code>UserGroup.readableLanguages</code> attribute defined at extension <code>core</code>. */
	private Collection<LanguageModel> _readableLanguages;
	
	/** <i>Generated variable</i> - Variable of <code>UserGroup.hmcXML</code> attribute defined at extension <code>core</code>. */
	private String _hmcXML;
	
	/** <i>Generated variable</i> - Variable of <code>UserGroup.userDiscountGroup</code> attribute defined at extension <code>europe1</code>. */
	private UserDiscountGroup _userDiscountGroup;
	
	/** <i>Generated variable</i> - Variable of <code>UserGroup.userPriceGroup</code> attribute defined at extension <code>europe1</code>. */
	private UserPriceGroup _userPriceGroup;
	
	/** <i>Generated variable</i> - Variable of <code>UserGroup.userTaxGroup</code> attribute defined at extension <code>europe1</code>. */
	private UserTaxGroup _userTaxGroup;
	
	/** <i>Generated variable</i> - Variable of <code>UserGroup.hmcLoginDisabled</code> attribute defined at extension <code>hmc</code>. */
	private Boolean _hmcLoginDisabled;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public UserGroupModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public UserGroupModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _uid initial attribute declared by type <code>Principal</code> at extension <code>core</code>
	 */
	@Deprecated
	public UserGroupModel(final String _uid)
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
	public UserGroupModel(final ItemModel _owner, final String _uid)
	{
		super();
		setOwner(_owner);
		setUid(_uid);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UserGroup.hmcLoginDisabled</code> attribute defined at extension <code>hmc</code>. 
	 * @return the hmcLoginDisabled
	 */
	@Accessor(qualifier = "hmcLoginDisabled", type = Accessor.Type.GETTER)
	public Boolean getHmcLoginDisabled()
	{
		if (this._hmcLoginDisabled!=null)
		{
			return _hmcLoginDisabled;
		}
		return _hmcLoginDisabled = getPersistenceContext().getValue(HMCLOGINDISABLED, _hmcLoginDisabled);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UserGroup.hmcXML</code> attribute defined at extension <code>core</code>. 
	 * @return the hmcXML
	 */
	@Accessor(qualifier = "hmcXML", type = Accessor.Type.GETTER)
	public String getHmcXML()
	{
		if (this._hmcXML!=null)
		{
			return _hmcXML;
		}
		return _hmcXML = getPersistenceContext().getValue(HMCXML, _hmcXML);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UserGroup.readableLanguages</code> attribute defined at extension <code>core</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the readableLanguages
	 */
	@Accessor(qualifier = "readableLanguages", type = Accessor.Type.GETTER)
	public Collection<LanguageModel> getReadableLanguages()
	{
		if (this._readableLanguages!=null)
		{
			return _readableLanguages;
		}
		return _readableLanguages = getPersistenceContext().getValue(READABLELANGUAGES, _readableLanguages);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UserGroup.userDiscountGroup</code> attribute defined at extension <code>europe1</code>. 
	 * @return the userDiscountGroup
	 */
	@Accessor(qualifier = "userDiscountGroup", type = Accessor.Type.GETTER)
	public UserDiscountGroup getUserDiscountGroup()
	{
		if (this._userDiscountGroup!=null)
		{
			return _userDiscountGroup;
		}
		return _userDiscountGroup = getPersistenceContext().getValue(USERDISCOUNTGROUP, _userDiscountGroup);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UserGroup.userPriceGroup</code> attribute defined at extension <code>europe1</code>. 
	 * @return the userPriceGroup
	 */
	@Accessor(qualifier = "userPriceGroup", type = Accessor.Type.GETTER)
	public UserPriceGroup getUserPriceGroup()
	{
		if (this._userPriceGroup!=null)
		{
			return _userPriceGroup;
		}
		return _userPriceGroup = getPersistenceContext().getValue(USERPRICEGROUP, _userPriceGroup);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UserGroup.userTaxGroup</code> attribute defined at extension <code>europe1</code>. 
	 * @return the userTaxGroup
	 */
	@Accessor(qualifier = "userTaxGroup", type = Accessor.Type.GETTER)
	public UserTaxGroup getUserTaxGroup()
	{
		if (this._userTaxGroup!=null)
		{
			return _userTaxGroup;
		}
		return _userTaxGroup = getPersistenceContext().getValue(USERTAXGROUP, _userTaxGroup);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>UserGroup.writeableLanguages</code> attribute defined at extension <code>core</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the writeableLanguages
	 */
	@Accessor(qualifier = "writeableLanguages", type = Accessor.Type.GETTER)
	public Collection<LanguageModel> getWriteableLanguages()
	{
		if (this._writeableLanguages!=null)
		{
			return _writeableLanguages;
		}
		return _writeableLanguages = getPersistenceContext().getValue(WRITEABLELANGUAGES, _writeableLanguages);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>UserGroup.hmcLoginDisabled</code> attribute defined at extension <code>hmc</code>. 
	 *  
	 * @param value the hmcLoginDisabled
	 */
	@Accessor(qualifier = "hmcLoginDisabled", type = Accessor.Type.SETTER)
	public void setHmcLoginDisabled(final Boolean value)
	{
		_hmcLoginDisabled = getPersistenceContext().setValue(HMCLOGINDISABLED, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>UserGroup.hmcXML</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the hmcXML
	 */
	@Accessor(qualifier = "hmcXML", type = Accessor.Type.SETTER)
	public void setHmcXML(final String value)
	{
		_hmcXML = getPersistenceContext().setValue(HMCXML, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>UserGroup.readableLanguages</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the readableLanguages
	 */
	@Accessor(qualifier = "readableLanguages", type = Accessor.Type.SETTER)
	public void setReadableLanguages(final Collection<LanguageModel> value)
	{
		_readableLanguages = getPersistenceContext().setValue(READABLELANGUAGES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>UserGroup.userDiscountGroup</code> attribute defined at extension <code>europe1</code>. 
	 *  
	 * @param value the userDiscountGroup
	 */
	@Accessor(qualifier = "userDiscountGroup", type = Accessor.Type.SETTER)
	public void setUserDiscountGroup(final UserDiscountGroup value)
	{
		_userDiscountGroup = getPersistenceContext().setValue(USERDISCOUNTGROUP, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>UserGroup.userPriceGroup</code> attribute defined at extension <code>europe1</code>. 
	 *  
	 * @param value the userPriceGroup
	 */
	@Accessor(qualifier = "userPriceGroup", type = Accessor.Type.SETTER)
	public void setUserPriceGroup(final UserPriceGroup value)
	{
		_userPriceGroup = getPersistenceContext().setValue(USERPRICEGROUP, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>UserGroup.userTaxGroup</code> attribute defined at extension <code>europe1</code>. 
	 *  
	 * @param value the userTaxGroup
	 */
	@Accessor(qualifier = "userTaxGroup", type = Accessor.Type.SETTER)
	public void setUserTaxGroup(final UserTaxGroup value)
	{
		_userTaxGroup = getPersistenceContext().setValue(USERTAXGROUP, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>UserGroup.writeableLanguages</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the writeableLanguages
	 */
	@Accessor(qualifier = "writeableLanguages", type = Accessor.Type.SETTER)
	public void setWriteableLanguages(final Collection<LanguageModel> value)
	{
		_writeableLanguages = getPersistenceContext().setValue(WRITEABLELANGUAGES, value);
	}
	
}

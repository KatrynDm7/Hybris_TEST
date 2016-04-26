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
import de.hybris.platform.core.enums.PhoneContactInfoType;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.AbstractContactInfoModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type PhoneContactInfo first defined at extension core.
 * <p>
 * Phone contact info.
 */
@SuppressWarnings("all")
public class PhoneContactInfoModel extends AbstractContactInfoModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "PhoneContactInfo";
	
	/** <i>Generated constant</i> - Attribute key of <code>PhoneContactInfo.phoneNumber</code> attribute defined at extension <code>core</code>. */
	public static final String PHONENUMBER = "phoneNumber";
	
	/** <i>Generated constant</i> - Attribute key of <code>PhoneContactInfo.type</code> attribute defined at extension <code>core</code>. */
	public static final String TYPE = "type";
	
	
	/** <i>Generated variable</i> - Variable of <code>PhoneContactInfo.phoneNumber</code> attribute defined at extension <code>core</code>. */
	private String _phoneNumber;
	
	/** <i>Generated variable</i> - Variable of <code>PhoneContactInfo.type</code> attribute defined at extension <code>core</code>. */
	private PhoneContactInfoType _type;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public PhoneContactInfoModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public PhoneContactInfoModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>AbstractContactInfo</code> at extension <code>core</code>
	 * @param _phoneNumber initial attribute declared by type <code>PhoneContactInfo</code> at extension <code>core</code>
	 * @param _type initial attribute declared by type <code>PhoneContactInfo</code> at extension <code>core</code>
	 * @param _user initial attribute declared by type <code>AbstractContactInfo</code> at extension <code>core</code>
	 */
	@Deprecated
	public PhoneContactInfoModel(final String _code, final String _phoneNumber, final PhoneContactInfoType _type, final UserModel _user)
	{
		super();
		setCode(_code);
		setPhoneNumber(_phoneNumber);
		setType(_type);
		setUser(_user);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>AbstractContactInfo</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _phoneNumber initial attribute declared by type <code>PhoneContactInfo</code> at extension <code>core</code>
	 * @param _type initial attribute declared by type <code>PhoneContactInfo</code> at extension <code>core</code>
	 * @param _user initial attribute declared by type <code>AbstractContactInfo</code> at extension <code>core</code>
	 */
	@Deprecated
	public PhoneContactInfoModel(final String _code, final ItemModel _owner, final String _phoneNumber, final PhoneContactInfoType _type, final UserModel _user)
	{
		super();
		setCode(_code);
		setOwner(_owner);
		setPhoneNumber(_phoneNumber);
		setType(_type);
		setUser(_user);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PhoneContactInfo.phoneNumber</code> attribute defined at extension <code>core</code>. 
	 * @return the phoneNumber
	 */
	@Accessor(qualifier = "phoneNumber", type = Accessor.Type.GETTER)
	public String getPhoneNumber()
	{
		if (this._phoneNumber!=null)
		{
			return _phoneNumber;
		}
		return _phoneNumber = getPersistenceContext().getValue(PHONENUMBER, _phoneNumber);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PhoneContactInfo.type</code> attribute defined at extension <code>core</code>. 
	 * @return the type
	 */
	@Accessor(qualifier = "type", type = Accessor.Type.GETTER)
	public PhoneContactInfoType getType()
	{
		if (this._type!=null)
		{
			return _type;
		}
		return _type = getPersistenceContext().getValue(TYPE, _type);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PhoneContactInfo.phoneNumber</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the phoneNumber
	 */
	@Accessor(qualifier = "phoneNumber", type = Accessor.Type.SETTER)
	public void setPhoneNumber(final String value)
	{
		_phoneNumber = getPersistenceContext().setValue(PHONENUMBER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PhoneContactInfo.type</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the type
	 */
	@Accessor(qualifier = "type", type = Accessor.Type.SETTER)
	public void setType(final PhoneContactInfoType value)
	{
		_type = getPersistenceContext().setValue(TYPE, value);
	}
	
}

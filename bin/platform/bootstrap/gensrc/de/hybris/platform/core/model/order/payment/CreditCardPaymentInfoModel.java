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
package de.hybris.platform.core.model.order.payment;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.enums.CreditCardType;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type CreditCardPaymentInfo first defined at extension core.
 */
@SuppressWarnings("all")
public class CreditCardPaymentInfoModel extends PaymentInfoModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "CreditCardPaymentInfo";
	
	/** <i>Generated constant</i> - Attribute key of <code>CreditCardPaymentInfo.ccOwner</code> attribute defined at extension <code>core</code>. */
	public static final String CCOWNER = "ccOwner";
	
	/** <i>Generated constant</i> - Attribute key of <code>CreditCardPaymentInfo.number</code> attribute defined at extension <code>core</code>. */
	public static final String NUMBER = "number";
	
	/** <i>Generated constant</i> - Attribute key of <code>CreditCardPaymentInfo.type</code> attribute defined at extension <code>core</code>. */
	public static final String TYPE = "type";
	
	/** <i>Generated constant</i> - Attribute key of <code>CreditCardPaymentInfo.validToMonth</code> attribute defined at extension <code>core</code>. */
	public static final String VALIDTOMONTH = "validToMonth";
	
	/** <i>Generated constant</i> - Attribute key of <code>CreditCardPaymentInfo.validToYear</code> attribute defined at extension <code>core</code>. */
	public static final String VALIDTOYEAR = "validToYear";
	
	/** <i>Generated constant</i> - Attribute key of <code>CreditCardPaymentInfo.validFromMonth</code> attribute defined at extension <code>core</code>. */
	public static final String VALIDFROMMONTH = "validFromMonth";
	
	/** <i>Generated constant</i> - Attribute key of <code>CreditCardPaymentInfo.validFromYear</code> attribute defined at extension <code>core</code>. */
	public static final String VALIDFROMYEAR = "validFromYear";
	
	
	/** <i>Generated variable</i> - Variable of <code>CreditCardPaymentInfo.ccOwner</code> attribute defined at extension <code>core</code>. */
	private String _ccOwner;
	
	/** <i>Generated variable</i> - Variable of <code>CreditCardPaymentInfo.number</code> attribute defined at extension <code>core</code>. */
	private String _number;
	
	/** <i>Generated variable</i> - Variable of <code>CreditCardPaymentInfo.type</code> attribute defined at extension <code>core</code>. */
	private CreditCardType _type;
	
	/** <i>Generated variable</i> - Variable of <code>CreditCardPaymentInfo.validToMonth</code> attribute defined at extension <code>core</code>. */
	private String _validToMonth;
	
	/** <i>Generated variable</i> - Variable of <code>CreditCardPaymentInfo.validToYear</code> attribute defined at extension <code>core</code>. */
	private String _validToYear;
	
	/** <i>Generated variable</i> - Variable of <code>CreditCardPaymentInfo.validFromMonth</code> attribute defined at extension <code>core</code>. */
	private String _validFromMonth;
	
	/** <i>Generated variable</i> - Variable of <code>CreditCardPaymentInfo.validFromYear</code> attribute defined at extension <code>core</code>. */
	private String _validFromYear;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CreditCardPaymentInfoModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CreditCardPaymentInfoModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _ccOwner initial attribute declared by type <code>CreditCardPaymentInfo</code> at extension <code>core</code>
	 * @param _code initial attribute declared by type <code>PaymentInfo</code> at extension <code>core</code>
	 * @param _number initial attribute declared by type <code>CreditCardPaymentInfo</code> at extension <code>core</code>
	 * @param _type initial attribute declared by type <code>CreditCardPaymentInfo</code> at extension <code>core</code>
	 * @param _user initial attribute declared by type <code>PaymentInfo</code> at extension <code>core</code>
	 * @param _validToMonth initial attribute declared by type <code>CreditCardPaymentInfo</code> at extension <code>core</code>
	 * @param _validToYear initial attribute declared by type <code>CreditCardPaymentInfo</code> at extension <code>core</code>
	 */
	@Deprecated
	public CreditCardPaymentInfoModel(final String _ccOwner, final String _code, final String _number, final CreditCardType _type, final UserModel _user, final String _validToMonth, final String _validToYear)
	{
		super();
		setCcOwner(_ccOwner);
		setCode(_code);
		setNumber(_number);
		setType(_type);
		setUser(_user);
		setValidToMonth(_validToMonth);
		setValidToYear(_validToYear);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _ccOwner initial attribute declared by type <code>CreditCardPaymentInfo</code> at extension <code>core</code>
	 * @param _code initial attribute declared by type <code>PaymentInfo</code> at extension <code>core</code>
	 * @param _number initial attribute declared by type <code>CreditCardPaymentInfo</code> at extension <code>core</code>
	 * @param _original initial attribute declared by type <code>PaymentInfo</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _type initial attribute declared by type <code>CreditCardPaymentInfo</code> at extension <code>core</code>
	 * @param _user initial attribute declared by type <code>PaymentInfo</code> at extension <code>core</code>
	 * @param _validToMonth initial attribute declared by type <code>CreditCardPaymentInfo</code> at extension <code>core</code>
	 * @param _validToYear initial attribute declared by type <code>CreditCardPaymentInfo</code> at extension <code>core</code>
	 */
	@Deprecated
	public CreditCardPaymentInfoModel(final String _ccOwner, final String _code, final String _number, final ItemModel _original, final ItemModel _owner, final CreditCardType _type, final UserModel _user, final String _validToMonth, final String _validToYear)
	{
		super();
		setCcOwner(_ccOwner);
		setCode(_code);
		setNumber(_number);
		setOriginal(_original);
		setOwner(_owner);
		setType(_type);
		setUser(_user);
		setValidToMonth(_validToMonth);
		setValidToYear(_validToYear);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CreditCardPaymentInfo.ccOwner</code> attribute defined at extension <code>core</code>. 
	 * @return the ccOwner
	 */
	@Accessor(qualifier = "ccOwner", type = Accessor.Type.GETTER)
	public String getCcOwner()
	{
		if (this._ccOwner!=null)
		{
			return _ccOwner;
		}
		return _ccOwner = getPersistenceContext().getValue(CCOWNER, _ccOwner);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CreditCardPaymentInfo.number</code> attribute defined at extension <code>core</code>. 
	 * @return the number
	 */
	@Accessor(qualifier = "number", type = Accessor.Type.GETTER)
	public String getNumber()
	{
		if (this._number!=null)
		{
			return _number;
		}
		return _number = getPersistenceContext().getValue(NUMBER, _number);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CreditCardPaymentInfo.type</code> attribute defined at extension <code>core</code>. 
	 * @return the type
	 */
	@Accessor(qualifier = "type", type = Accessor.Type.GETTER)
	public CreditCardType getType()
	{
		if (this._type!=null)
		{
			return _type;
		}
		return _type = getPersistenceContext().getValue(TYPE, _type);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CreditCardPaymentInfo.validFromMonth</code> attribute defined at extension <code>core</code>. 
	 * @return the validFromMonth
	 */
	@Accessor(qualifier = "validFromMonth", type = Accessor.Type.GETTER)
	public String getValidFromMonth()
	{
		if (this._validFromMonth!=null)
		{
			return _validFromMonth;
		}
		return _validFromMonth = getPersistenceContext().getValue(VALIDFROMMONTH, _validFromMonth);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CreditCardPaymentInfo.validFromYear</code> attribute defined at extension <code>core</code>. 
	 * @return the validFromYear
	 */
	@Accessor(qualifier = "validFromYear", type = Accessor.Type.GETTER)
	public String getValidFromYear()
	{
		if (this._validFromYear!=null)
		{
			return _validFromYear;
		}
		return _validFromYear = getPersistenceContext().getValue(VALIDFROMYEAR, _validFromYear);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CreditCardPaymentInfo.validToMonth</code> attribute defined at extension <code>core</code>. 
	 * @return the validToMonth
	 */
	@Accessor(qualifier = "validToMonth", type = Accessor.Type.GETTER)
	public String getValidToMonth()
	{
		if (this._validToMonth!=null)
		{
			return _validToMonth;
		}
		return _validToMonth = getPersistenceContext().getValue(VALIDTOMONTH, _validToMonth);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CreditCardPaymentInfo.validToYear</code> attribute defined at extension <code>core</code>. 
	 * @return the validToYear
	 */
	@Accessor(qualifier = "validToYear", type = Accessor.Type.GETTER)
	public String getValidToYear()
	{
		if (this._validToYear!=null)
		{
			return _validToYear;
		}
		return _validToYear = getPersistenceContext().getValue(VALIDTOYEAR, _validToYear);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CreditCardPaymentInfo.ccOwner</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the ccOwner
	 */
	@Accessor(qualifier = "ccOwner", type = Accessor.Type.SETTER)
	public void setCcOwner(final String value)
	{
		_ccOwner = getPersistenceContext().setValue(CCOWNER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CreditCardPaymentInfo.number</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the number
	 */
	@Accessor(qualifier = "number", type = Accessor.Type.SETTER)
	public void setNumber(final String value)
	{
		_number = getPersistenceContext().setValue(NUMBER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CreditCardPaymentInfo.type</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the type
	 */
	@Accessor(qualifier = "type", type = Accessor.Type.SETTER)
	public void setType(final CreditCardType value)
	{
		_type = getPersistenceContext().setValue(TYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CreditCardPaymentInfo.validFromMonth</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the validFromMonth
	 */
	@Accessor(qualifier = "validFromMonth", type = Accessor.Type.SETTER)
	public void setValidFromMonth(final String value)
	{
		_validFromMonth = getPersistenceContext().setValue(VALIDFROMMONTH, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CreditCardPaymentInfo.validFromYear</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the validFromYear
	 */
	@Accessor(qualifier = "validFromYear", type = Accessor.Type.SETTER)
	public void setValidFromYear(final String value)
	{
		_validFromYear = getPersistenceContext().setValue(VALIDFROMYEAR, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CreditCardPaymentInfo.validToMonth</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the validToMonth
	 */
	@Accessor(qualifier = "validToMonth", type = Accessor.Type.SETTER)
	public void setValidToMonth(final String value)
	{
		_validToMonth = getPersistenceContext().setValue(VALIDTOMONTH, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CreditCardPaymentInfo.validToYear</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the validToYear
	 */
	@Accessor(qualifier = "validToYear", type = Accessor.Type.SETTER)
	public void setValidToYear(final String value)
	{
		_validToYear = getPersistenceContext().setValue(VALIDTOYEAR, value);
	}
	
}

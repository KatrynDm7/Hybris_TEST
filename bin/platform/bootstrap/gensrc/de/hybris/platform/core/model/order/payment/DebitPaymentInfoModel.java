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
package de.hybris.platform.core.model.order.payment;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type DebitPaymentInfo first defined at extension core.
 */
@SuppressWarnings("all")
public class DebitPaymentInfoModel extends PaymentInfoModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "DebitPaymentInfo";
	
	/** <i>Generated constant</i> - Attribute key of <code>DebitPaymentInfo.bankIDNumber</code> attribute defined at extension <code>core</code>. */
	public static final String BANKIDNUMBER = "bankIDNumber";
	
	/** <i>Generated constant</i> - Attribute key of <code>DebitPaymentInfo.bank</code> attribute defined at extension <code>core</code>. */
	public static final String BANK = "bank";
	
	/** <i>Generated constant</i> - Attribute key of <code>DebitPaymentInfo.accountNumber</code> attribute defined at extension <code>core</code>. */
	public static final String ACCOUNTNUMBER = "accountNumber";
	
	/** <i>Generated constant</i> - Attribute key of <code>DebitPaymentInfo.baOwner</code> attribute defined at extension <code>core</code>. */
	public static final String BAOWNER = "baOwner";
	
	
	/** <i>Generated variable</i> - Variable of <code>DebitPaymentInfo.bankIDNumber</code> attribute defined at extension <code>core</code>. */
	private String _bankIDNumber;
	
	/** <i>Generated variable</i> - Variable of <code>DebitPaymentInfo.bank</code> attribute defined at extension <code>core</code>. */
	private String _bank;
	
	/** <i>Generated variable</i> - Variable of <code>DebitPaymentInfo.accountNumber</code> attribute defined at extension <code>core</code>. */
	private String _accountNumber;
	
	/** <i>Generated variable</i> - Variable of <code>DebitPaymentInfo.baOwner</code> attribute defined at extension <code>core</code>. */
	private String _baOwner;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public DebitPaymentInfoModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public DebitPaymentInfoModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _accountNumber initial attribute declared by type <code>DebitPaymentInfo</code> at extension <code>core</code>
	 * @param _baOwner initial attribute declared by type <code>DebitPaymentInfo</code> at extension <code>core</code>
	 * @param _bank initial attribute declared by type <code>DebitPaymentInfo</code> at extension <code>core</code>
	 * @param _bankIDNumber initial attribute declared by type <code>DebitPaymentInfo</code> at extension <code>core</code>
	 * @param _code initial attribute declared by type <code>PaymentInfo</code> at extension <code>core</code>
	 * @param _user initial attribute declared by type <code>PaymentInfo</code> at extension <code>core</code>
	 */
	@Deprecated
	public DebitPaymentInfoModel(final String _accountNumber, final String _baOwner, final String _bank, final String _bankIDNumber, final String _code, final UserModel _user)
	{
		super();
		setAccountNumber(_accountNumber);
		setBaOwner(_baOwner);
		setBank(_bank);
		setBankIDNumber(_bankIDNumber);
		setCode(_code);
		setUser(_user);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _accountNumber initial attribute declared by type <code>DebitPaymentInfo</code> at extension <code>core</code>
	 * @param _baOwner initial attribute declared by type <code>DebitPaymentInfo</code> at extension <code>core</code>
	 * @param _bank initial attribute declared by type <code>DebitPaymentInfo</code> at extension <code>core</code>
	 * @param _bankIDNumber initial attribute declared by type <code>DebitPaymentInfo</code> at extension <code>core</code>
	 * @param _code initial attribute declared by type <code>PaymentInfo</code> at extension <code>core</code>
	 * @param _original initial attribute declared by type <code>PaymentInfo</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _user initial attribute declared by type <code>PaymentInfo</code> at extension <code>core</code>
	 */
	@Deprecated
	public DebitPaymentInfoModel(final String _accountNumber, final String _baOwner, final String _bank, final String _bankIDNumber, final String _code, final ItemModel _original, final ItemModel _owner, final UserModel _user)
	{
		super();
		setAccountNumber(_accountNumber);
		setBaOwner(_baOwner);
		setBank(_bank);
		setBankIDNumber(_bankIDNumber);
		setCode(_code);
		setOriginal(_original);
		setOwner(_owner);
		setUser(_user);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DebitPaymentInfo.accountNumber</code> attribute defined at extension <code>core</code>. 
	 * @return the accountNumber
	 */
	@Accessor(qualifier = "accountNumber", type = Accessor.Type.GETTER)
	public String getAccountNumber()
	{
		if (this._accountNumber!=null)
		{
			return _accountNumber;
		}
		return _accountNumber = getPersistenceContext().getValue(ACCOUNTNUMBER, _accountNumber);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DebitPaymentInfo.bank</code> attribute defined at extension <code>core</code>. 
	 * @return the bank
	 */
	@Accessor(qualifier = "bank", type = Accessor.Type.GETTER)
	public String getBank()
	{
		if (this._bank!=null)
		{
			return _bank;
		}
		return _bank = getPersistenceContext().getValue(BANK, _bank);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DebitPaymentInfo.bankIDNumber</code> attribute defined at extension <code>core</code>. 
	 * @return the bankIDNumber
	 */
	@Accessor(qualifier = "bankIDNumber", type = Accessor.Type.GETTER)
	public String getBankIDNumber()
	{
		if (this._bankIDNumber!=null)
		{
			return _bankIDNumber;
		}
		return _bankIDNumber = getPersistenceContext().getValue(BANKIDNUMBER, _bankIDNumber);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DebitPaymentInfo.baOwner</code> attribute defined at extension <code>core</code>. 
	 * @return the baOwner
	 */
	@Accessor(qualifier = "baOwner", type = Accessor.Type.GETTER)
	public String getBaOwner()
	{
		if (this._baOwner!=null)
		{
			return _baOwner;
		}
		return _baOwner = getPersistenceContext().getValue(BAOWNER, _baOwner);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DebitPaymentInfo.accountNumber</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the accountNumber
	 */
	@Accessor(qualifier = "accountNumber", type = Accessor.Type.SETTER)
	public void setAccountNumber(final String value)
	{
		_accountNumber = getPersistenceContext().setValue(ACCOUNTNUMBER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DebitPaymentInfo.bank</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the bank
	 */
	@Accessor(qualifier = "bank", type = Accessor.Type.SETTER)
	public void setBank(final String value)
	{
		_bank = getPersistenceContext().setValue(BANK, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DebitPaymentInfo.bankIDNumber</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the bankIDNumber
	 */
	@Accessor(qualifier = "bankIDNumber", type = Accessor.Type.SETTER)
	public void setBankIDNumber(final String value)
	{
		_bankIDNumber = getPersistenceContext().setValue(BANKIDNUMBER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>DebitPaymentInfo.baOwner</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the baOwner
	 */
	@Accessor(qualifier = "baOwner", type = Accessor.Type.SETTER)
	public void setBaOwner(final String value)
	{
		_baOwner = getPersistenceContext().setValue(BAOWNER, value);
	}
	
}

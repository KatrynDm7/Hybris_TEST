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
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Locale;

/**
 * Generated model class for type PaymentMode first defined at extension core.
 */
@SuppressWarnings("all")
public class PaymentModeModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "PaymentMode";
	
	/** <i>Generated constant</i> - Attribute key of <code>PaymentMode.active</code> attribute defined at extension <code>core</code>. */
	public static final String ACTIVE = "active";
	
	/** <i>Generated constant</i> - Attribute key of <code>PaymentMode.code</code> attribute defined at extension <code>core</code>. */
	public static final String CODE = "code";
	
	/** <i>Generated constant</i> - Attribute key of <code>PaymentMode.description</code> attribute defined at extension <code>core</code>. */
	public static final String DESCRIPTION = "description";
	
	/** <i>Generated constant</i> - Attribute key of <code>PaymentMode.name</code> attribute defined at extension <code>core</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>PaymentMode.paymentInfoType</code> attribute defined at extension <code>core</code>. */
	public static final String PAYMENTINFOTYPE = "paymentInfoType";
	
	/** <i>Generated constant</i> - Attribute key of <code>PaymentMode.supportedDeliveryModes</code> attribute defined at extension <code>core</code>. */
	public static final String SUPPORTEDDELIVERYMODES = "supportedDeliveryModes";
	
	
	/** <i>Generated variable</i> - Variable of <code>PaymentMode.active</code> attribute defined at extension <code>core</code>. */
	private Boolean _active;
	
	/** <i>Generated variable</i> - Variable of <code>PaymentMode.code</code> attribute defined at extension <code>core</code>. */
	private String _code;
	
	/** <i>Generated variable</i> - Variable of <code>PaymentMode.paymentInfoType</code> attribute defined at extension <code>core</code>. */
	private ComposedTypeModel _paymentInfoType;
	
	/** <i>Generated variable</i> - Variable of <code>PaymentMode.supportedDeliveryModes</code> attribute defined at extension <code>core</code>. */
	private Collection<DeliveryModeModel> _supportedDeliveryModes;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public PaymentModeModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public PaymentModeModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _active initial attribute declared by type <code>PaymentMode</code> at extension <code>core</code>
	 * @param _code initial attribute declared by type <code>PaymentMode</code> at extension <code>core</code>
	 * @param _paymentInfoType initial attribute declared by type <code>PaymentMode</code> at extension <code>core</code>
	 */
	@Deprecated
	public PaymentModeModel(final Boolean _active, final String _code, final ComposedTypeModel _paymentInfoType)
	{
		super();
		setActive(_active);
		setCode(_code);
		setPaymentInfoType(_paymentInfoType);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _active initial attribute declared by type <code>PaymentMode</code> at extension <code>core</code>
	 * @param _code initial attribute declared by type <code>PaymentMode</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _paymentInfoType initial attribute declared by type <code>PaymentMode</code> at extension <code>core</code>
	 */
	@Deprecated
	public PaymentModeModel(final Boolean _active, final String _code, final ItemModel _owner, final ComposedTypeModel _paymentInfoType)
	{
		super();
		setActive(_active);
		setCode(_code);
		setOwner(_owner);
		setPaymentInfoType(_paymentInfoType);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentMode.active</code> attribute defined at extension <code>core</code>. 
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
	 * <i>Generated method</i> - Getter of the <code>PaymentMode.code</code> attribute defined at extension <code>core</code>. 
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
	 * <i>Generated method</i> - Getter of the <code>PaymentMode.description</code> attribute defined at extension <code>core</code>. 
	 * @return the description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.GETTER)
	public String getDescription()
	{
		return getDescription(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentMode.description</code> attribute defined at extension <code>core</code>. 
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
	 * <i>Generated method</i> - Getter of the <code>PaymentMode.name</code> attribute defined at extension <code>core</code>. 
	 * @return the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getName(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentMode.name</code> attribute defined at extension <code>core</code>. 
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
	 * <i>Generated method</i> - Getter of the <code>PaymentMode.paymentInfoType</code> attribute defined at extension <code>core</code>. 
	 * @return the paymentInfoType
	 * @deprecated use {@link #getPaymentInfoType()} instead
	 */
	@Deprecated
	public ComposedTypeModel getPaymentinfotype()
	{
		return this.getPaymentInfoType();
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentMode.paymentInfoType</code> attribute defined at extension <code>core</code>. 
	 * @return the paymentInfoType
	 */
	@Accessor(qualifier = "paymentInfoType", type = Accessor.Type.GETTER)
	public ComposedTypeModel getPaymentInfoType()
	{
		if (this._paymentInfoType!=null)
		{
			return _paymentInfoType;
		}
		return _paymentInfoType = getPersistenceContext().getValue(PAYMENTINFOTYPE, _paymentInfoType);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentMode.supportedDeliveryModes</code> attribute defined at extension <code>core</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the supportedDeliveryModes
	 * @deprecated use {@link #getSupportedDeliveryModes()} instead
	 */
	@Deprecated
	public Collection<DeliveryModeModel> getSupporteddeliverymodes()
	{
		return this.getSupportedDeliveryModes();
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentMode.supportedDeliveryModes</code> attribute defined at extension <code>core</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the supportedDeliveryModes
	 */
	@Accessor(qualifier = "supportedDeliveryModes", type = Accessor.Type.GETTER)
	public Collection<DeliveryModeModel> getSupportedDeliveryModes()
	{
		if (this._supportedDeliveryModes!=null)
		{
			return _supportedDeliveryModes;
		}
		return _supportedDeliveryModes = getPersistenceContext().getValue(SUPPORTEDDELIVERYMODES, _supportedDeliveryModes);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PaymentMode.active</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the active
	 */
	@Accessor(qualifier = "active", type = Accessor.Type.SETTER)
	public void setActive(final Boolean value)
	{
		_active = getPersistenceContext().setValue(ACTIVE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PaymentMode.code</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the code
	 */
	@Accessor(qualifier = "code", type = Accessor.Type.SETTER)
	public void setCode(final String value)
	{
		_code = getPersistenceContext().setValue(CODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PaymentMode.description</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the description
	 */
	@Accessor(qualifier = "description", type = Accessor.Type.SETTER)
	public void setDescription(final String value)
	{
		setDescription(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>PaymentMode.description</code> attribute defined at extension <code>core</code>. 
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
	 * <i>Generated method</i> - Setter of <code>PaymentMode.name</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		setName(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>PaymentMode.name</code> attribute defined at extension <code>core</code>. 
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
	 * <i>Generated method</i> - Setter of <code>PaymentMode.paymentInfoType</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the paymentInfoType
	 * @deprecated use {@link #setPaymentInfoType(de.hybris.platform.core.model.type.ComposedTypeModel)} instead
	 */
	@Deprecated
	public void setPaymentinfotype(final ComposedTypeModel value)
	{
		this.setPaymentInfoType(value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>PaymentMode.paymentInfoType</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the paymentInfoType
	 */
	@Accessor(qualifier = "paymentInfoType", type = Accessor.Type.SETTER)
	public void setPaymentInfoType(final ComposedTypeModel value)
	{
		_paymentInfoType = getPersistenceContext().setValue(PAYMENTINFOTYPE, value);
	}
	
}

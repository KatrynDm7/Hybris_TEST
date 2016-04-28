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
package de.hybris.platform.core.model.c2l;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.C2LItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type Currency first defined at extension core.
 */
@SuppressWarnings("all")
public class CurrencyModel extends C2LItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "Currency";
	
	/** <i>Generated constant</i> - Attribute key of <code>Currency.base</code> attribute defined at extension <code>core</code>. */
	public static final String BASE = "base";
	
	/** <i>Generated constant</i> - Attribute key of <code>Currency.conversion</code> attribute defined at extension <code>core</code>. */
	public static final String CONVERSION = "conversion";
	
	/** <i>Generated constant</i> - Attribute key of <code>Currency.digits</code> attribute defined at extension <code>core</code>. */
	public static final String DIGITS = "digits";
	
	/** <i>Generated constant</i> - Attribute key of <code>Currency.symbol</code> attribute defined at extension <code>core</code>. */
	public static final String SYMBOL = "symbol";
	
	
	/** <i>Generated variable</i> - Variable of <code>Currency.base</code> attribute defined at extension <code>core</code>. */
	private Boolean _base;
	
	/** <i>Generated variable</i> - Variable of <code>Currency.conversion</code> attribute defined at extension <code>core</code>. */
	private Double _conversion;
	
	/** <i>Generated variable</i> - Variable of <code>Currency.digits</code> attribute defined at extension <code>core</code>. */
	private Integer _digits;
	
	/** <i>Generated variable</i> - Variable of <code>Currency.symbol</code> attribute defined at extension <code>core</code>. */
	private String _symbol;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CurrencyModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CurrencyModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _isocode initial attribute declared by type <code>Currency</code> at extension <code>core</code>
	 * @param _symbol initial attribute declared by type <code>Currency</code> at extension <code>core</code>
	 */
	@Deprecated
	public CurrencyModel(final String _isocode, final String _symbol)
	{
		super();
		setIsocode(_isocode);
		setSymbol(_symbol);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _isocode initial attribute declared by type <code>Currency</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _symbol initial attribute declared by type <code>Currency</code> at extension <code>core</code>
	 */
	@Deprecated
	public CurrencyModel(final String _isocode, final ItemModel _owner, final String _symbol)
	{
		super();
		setIsocode(_isocode);
		setOwner(_owner);
		setSymbol(_symbol);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Currency.base</code> attribute defined at extension <code>core</code>. 
	 * @return the base
	 */
	@Accessor(qualifier = "base", type = Accessor.Type.GETTER)
	public Boolean getBase()
	{
		if (this._base!=null)
		{
			return _base;
		}
		return _base = getPersistenceContext().getValue(BASE, _base);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Currency.conversion</code> attribute defined at extension <code>core</code>. 
	 * @return the conversion
	 */
	@Accessor(qualifier = "conversion", type = Accessor.Type.GETTER)
	public Double getConversion()
	{
		if (this._conversion!=null)
		{
			return _conversion;
		}
		return _conversion = getPersistenceContext().getValue(CONVERSION, _conversion);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Currency.digits</code> attribute defined at extension <code>core</code>. 
	 * @return the digits
	 */
	@Accessor(qualifier = "digits", type = Accessor.Type.GETTER)
	public Integer getDigits()
	{
		if (this._digits!=null)
		{
			return _digits;
		}
		return _digits = getPersistenceContext().getValue(DIGITS, _digits);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Currency.symbol</code> attribute defined at extension <code>core</code>. 
	 * @return the symbol
	 */
	@Accessor(qualifier = "symbol", type = Accessor.Type.GETTER)
	public String getSymbol()
	{
		if (this._symbol!=null)
		{
			return _symbol;
		}
		return _symbol = getPersistenceContext().getValue(SYMBOL, _symbol);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Currency.base</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the base
	 */
	@Accessor(qualifier = "base", type = Accessor.Type.SETTER)
	public void setBase(final Boolean value)
	{
		_base = getPersistenceContext().setValue(BASE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Currency.conversion</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the conversion
	 */
	@Accessor(qualifier = "conversion", type = Accessor.Type.SETTER)
	public void setConversion(final Double value)
	{
		_conversion = getPersistenceContext().setValue(CONVERSION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Currency.digits</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the digits
	 */
	@Accessor(qualifier = "digits", type = Accessor.Type.SETTER)
	public void setDigits(final Integer value)
	{
		_digits = getPersistenceContext().setValue(DIGITS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Currency.symbol</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the symbol
	 */
	@Accessor(qualifier = "symbol", type = Accessor.Type.SETTER)
	public void setSymbol(final String value)
	{
		_symbol = getPersistenceContext().setValue(SYMBOL, value);
	}
	
}

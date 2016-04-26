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
package de.hybris.platform.core.model.order;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.util.TaxValue;
import java.util.Collection;
import java.util.List;

/**
 * Generated model class for type AbstractOrderEntry first defined at extension core.
 */
@SuppressWarnings("all")
public class AbstractOrderEntryModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "AbstractOrderEntry";
	
	/**<i>Generated relation code constant for relation <code>AbstractOrder2AbstractOrderEntry</code> defining source attribute <code>order</code> in extension <code>core</code>.</i>*/
	public final static String _ABSTRACTORDER2ABSTRACTORDERENTRY = "AbstractOrder2AbstractOrderEntry";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrderEntry.basePrice</code> attribute defined at extension <code>core</code>. */
	public static final String BASEPRICE = "basePrice";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrderEntry.calculated</code> attribute defined at extension <code>core</code>. */
	public static final String CALCULATED = "calculated";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrderEntry.discountValuesInternal</code> attribute defined at extension <code>core</code>. */
	public static final String DISCOUNTVALUESINTERNAL = "discountValuesInternal";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrderEntry.discountValues</code> attribute defined at extension <code>core</code>. */
	public static final String DISCOUNTVALUES = "discountValues";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrderEntry.entryNumber</code> attribute defined at extension <code>core</code>. */
	public static final String ENTRYNUMBER = "entryNumber";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrderEntry.info</code> attribute defined at extension <code>core</code>. */
	public static final String INFO = "info";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrderEntry.product</code> attribute defined at extension <code>core</code>. */
	public static final String PRODUCT = "product";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrderEntry.quantity</code> attribute defined at extension <code>core</code>. */
	public static final String QUANTITY = "quantity";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrderEntry.taxValues</code> attribute defined at extension <code>core</code>. */
	public static final String TAXVALUES = "taxValues";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrderEntry.taxValuesInternal</code> attribute defined at extension <code>core</code>. */
	public static final String TAXVALUESINTERNAL = "taxValuesInternal";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrderEntry.totalPrice</code> attribute defined at extension <code>core</code>. */
	public static final String TOTALPRICE = "totalPrice";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrderEntry.unit</code> attribute defined at extension <code>core</code>. */
	public static final String UNIT = "unit";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrderEntry.giveAway</code> attribute defined at extension <code>core</code>. */
	public static final String GIVEAWAY = "giveAway";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrderEntry.rejected</code> attribute defined at extension <code>core</code>. */
	public static final String REJECTED = "rejected";
	
	/** <i>Generated constant</i> - Attribute key of <code>AbstractOrderEntry.order</code> attribute defined at extension <code>core</code>. */
	public static final String ORDER = "order";
	
	
	/** <i>Generated variable</i> - Variable of <code>AbstractOrderEntry.basePrice</code> attribute defined at extension <code>core</code>. */
	private Double _basePrice;
	
	/** <i>Generated variable</i> - Variable of <code>AbstractOrderEntry.calculated</code> attribute defined at extension <code>core</code>. */
	private Boolean _calculated;
	
	/** <i>Generated variable</i> - Variable of <code>AbstractOrderEntry.discountValuesInternal</code> attribute defined at extension <code>core</code>. */
	private String _discountValuesInternal;
	
	/** <i>Generated variable</i> - Variable of <code>AbstractOrderEntry.discountValues</code> attribute defined at extension <code>core</code>. */
	private List<DiscountValue> _discountValues;
	
	/** <i>Generated variable</i> - Variable of <code>AbstractOrderEntry.entryNumber</code> attribute defined at extension <code>core</code>. */
	private Integer _entryNumber;
	
	/** <i>Generated variable</i> - Variable of <code>AbstractOrderEntry.info</code> attribute defined at extension <code>core</code>. */
	private String _info;
	
	/** <i>Generated variable</i> - Variable of <code>AbstractOrderEntry.product</code> attribute defined at extension <code>core</code>. */
	private ProductModel _product;
	
	/** <i>Generated variable</i> - Variable of <code>AbstractOrderEntry.quantity</code> attribute defined at extension <code>core</code>. */
	private Long _quantity;
	
	/** <i>Generated variable</i> - Variable of <code>AbstractOrderEntry.taxValues</code> attribute defined at extension <code>core</code>. */
	private Collection<TaxValue> _taxValues;
	
	/** <i>Generated variable</i> - Variable of <code>AbstractOrderEntry.taxValuesInternal</code> attribute defined at extension <code>core</code>. */
	private String _taxValuesInternal;
	
	/** <i>Generated variable</i> - Variable of <code>AbstractOrderEntry.totalPrice</code> attribute defined at extension <code>core</code>. */
	private Double _totalPrice;
	
	/** <i>Generated variable</i> - Variable of <code>AbstractOrderEntry.unit</code> attribute defined at extension <code>core</code>. */
	private UnitModel _unit;
	
	/** <i>Generated variable</i> - Variable of <code>AbstractOrderEntry.giveAway</code> attribute defined at extension <code>core</code>. */
	private Boolean _giveAway;
	
	/** <i>Generated variable</i> - Variable of <code>AbstractOrderEntry.rejected</code> attribute defined at extension <code>core</code>. */
	private Boolean _rejected;
	
	/** <i>Generated variable</i> - Variable of <code>AbstractOrderEntry.order</code> attribute defined at extension <code>core</code>. */
	private AbstractOrderModel _order;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public AbstractOrderEntryModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public AbstractOrderEntryModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _product initial attribute declared by type <code>AbstractOrderEntry</code> at extension <code>core</code>
	 * @param _quantity initial attribute declared by type <code>AbstractOrderEntry</code> at extension <code>core</code>
	 * @param _unit initial attribute declared by type <code>AbstractOrderEntry</code> at extension <code>core</code>
	 */
	@Deprecated
	public AbstractOrderEntryModel(final ProductModel _product, final Long _quantity, final UnitModel _unit)
	{
		super();
		setProduct(_product);
		setQuantity(_quantity);
		setUnit(_unit);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _product initial attribute declared by type <code>AbstractOrderEntry</code> at extension <code>core</code>
	 * @param _quantity initial attribute declared by type <code>AbstractOrderEntry</code> at extension <code>core</code>
	 * @param _unit initial attribute declared by type <code>AbstractOrderEntry</code> at extension <code>core</code>
	 */
	@Deprecated
	public AbstractOrderEntryModel(final ItemModel _owner, final ProductModel _product, final Long _quantity, final UnitModel _unit)
	{
		super();
		setOwner(_owner);
		setProduct(_product);
		setQuantity(_quantity);
		setUnit(_unit);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.basePrice</code> attribute defined at extension <code>core</code>. 
	 * @return the basePrice
	 */
	@Accessor(qualifier = "basePrice", type = Accessor.Type.GETTER)
	public Double getBasePrice()
	{
		if (this._basePrice!=null)
		{
			return _basePrice;
		}
		return _basePrice = getPersistenceContext().getValue(BASEPRICE, _basePrice);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.calculated</code> attribute defined at extension <code>core</code>. 
	 * @return the calculated
	 */
	@Accessor(qualifier = "calculated", type = Accessor.Type.GETTER)
	public Boolean getCalculated()
	{
		if (this._calculated!=null)
		{
			return _calculated;
		}
		return _calculated = getPersistenceContext().getValue(CALCULATED, _calculated);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.discountValues</code> dynamic attribute defined at extension <code>core</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the discountValues
	 */
	@Accessor(qualifier = "discountValues", type = Accessor.Type.GETTER)
	public List<DiscountValue> getDiscountValues()
	{
		return getPersistenceContext().getDynamicValue(this,DISCOUNTVALUES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.discountValuesInternal</code> attribute defined at extension <code>core</code>. 
	 * @return the discountValuesInternal
	 */
	@Accessor(qualifier = "discountValuesInternal", type = Accessor.Type.GETTER)
	public String getDiscountValuesInternal()
	{
		if (this._discountValuesInternal!=null)
		{
			return _discountValuesInternal;
		}
		return _discountValuesInternal = getPersistenceContext().getValue(DISCOUNTVALUESINTERNAL, _discountValuesInternal);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.entryNumber</code> attribute defined at extension <code>core</code>. 
	 * @return the entryNumber
	 */
	@Accessor(qualifier = "entryNumber", type = Accessor.Type.GETTER)
	public Integer getEntryNumber()
	{
		if (this._entryNumber!=null)
		{
			return _entryNumber;
		}
		return _entryNumber = getPersistenceContext().getValue(ENTRYNUMBER, _entryNumber);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.giveAway</code> attribute defined at extension <code>core</code>. 
	 * @return the giveAway
	 */
	@Accessor(qualifier = "giveAway", type = Accessor.Type.GETTER)
	public Boolean getGiveAway()
	{
		if (this._giveAway!=null)
		{
			return _giveAway;
		}
		return _giveAway = getPersistenceContext().getValue(GIVEAWAY, _giveAway);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.info</code> attribute defined at extension <code>core</code>. 
	 * @return the info
	 */
	@Accessor(qualifier = "info", type = Accessor.Type.GETTER)
	public String getInfo()
	{
		if (this._info!=null)
		{
			return _info;
		}
		return _info = getPersistenceContext().getValue(INFO, _info);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.order</code> attribute defined at extension <code>core</code>. 
	 * @return the order
	 */
	@Accessor(qualifier = "order", type = Accessor.Type.GETTER)
	public AbstractOrderModel getOrder()
	{
		if (this._order!=null)
		{
			return _order;
		}
		return _order = getPersistenceContext().getValue(ORDER, _order);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.product</code> attribute defined at extension <code>core</code>. 
	 * @return the product
	 */
	@Accessor(qualifier = "product", type = Accessor.Type.GETTER)
	public ProductModel getProduct()
	{
		if (this._product!=null)
		{
			return _product;
		}
		return _product = getPersistenceContext().getValue(PRODUCT, _product);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.quantity</code> attribute defined at extension <code>core</code>. 
	 * @return the quantity
	 */
	@Accessor(qualifier = "quantity", type = Accessor.Type.GETTER)
	public Long getQuantity()
	{
		if (this._quantity!=null)
		{
			return _quantity;
		}
		return _quantity = getPersistenceContext().getValue(QUANTITY, _quantity);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.rejected</code> attribute defined at extension <code>core</code>. 
	 * @return the rejected
	 */
	@Accessor(qualifier = "rejected", type = Accessor.Type.GETTER)
	public Boolean getRejected()
	{
		if (this._rejected!=null)
		{
			return _rejected;
		}
		return _rejected = getPersistenceContext().getValue(REJECTED, _rejected);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.taxValues</code> dynamic attribute defined at extension <code>core</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the taxValues
	 */
	@Accessor(qualifier = "taxValues", type = Accessor.Type.GETTER)
	public Collection<TaxValue> getTaxValues()
	{
		return getPersistenceContext().getDynamicValue(this,TAXVALUES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.taxValuesInternal</code> attribute defined at extension <code>core</code>. 
	 * @return the taxValuesInternal
	 */
	@Accessor(qualifier = "taxValuesInternal", type = Accessor.Type.GETTER)
	public String getTaxValuesInternal()
	{
		if (this._taxValuesInternal!=null)
		{
			return _taxValuesInternal;
		}
		return _taxValuesInternal = getPersistenceContext().getValue(TAXVALUESINTERNAL, _taxValuesInternal);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.totalPrice</code> attribute defined at extension <code>core</code>. 
	 * @return the totalPrice
	 */
	@Accessor(qualifier = "totalPrice", type = Accessor.Type.GETTER)
	public Double getTotalPrice()
	{
		if (this._totalPrice!=null)
		{
			return _totalPrice;
		}
		return _totalPrice = getPersistenceContext().getValue(TOTALPRICE, _totalPrice);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.unit</code> attribute defined at extension <code>core</code>. 
	 * @return the unit
	 */
	@Accessor(qualifier = "unit", type = Accessor.Type.GETTER)
	public UnitModel getUnit()
	{
		if (this._unit!=null)
		{
			return _unit;
		}
		return _unit = getPersistenceContext().getValue(UNIT, _unit);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrderEntry.basePrice</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the basePrice
	 */
	@Accessor(qualifier = "basePrice", type = Accessor.Type.SETTER)
	public void setBasePrice(final Double value)
	{
		_basePrice = getPersistenceContext().setValue(BASEPRICE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrderEntry.calculated</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the calculated
	 */
	@Accessor(qualifier = "calculated", type = Accessor.Type.SETTER)
	public void setCalculated(final Boolean value)
	{
		_calculated = getPersistenceContext().setValue(CALCULATED, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrderEntry.discountValues</code> dynamic attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the discountValues
	 */
	@Accessor(qualifier = "discountValues", type = Accessor.Type.SETTER)
	public void setDiscountValues(final List<DiscountValue> value)
	{
		getPersistenceContext().setDynamicValue(this,DISCOUNTVALUES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrderEntry.discountValuesInternal</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the discountValuesInternal
	 */
	@Accessor(qualifier = "discountValuesInternal", type = Accessor.Type.SETTER)
	public void setDiscountValuesInternal(final String value)
	{
		_discountValuesInternal = getPersistenceContext().setValue(DISCOUNTVALUESINTERNAL, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrderEntry.entryNumber</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the entryNumber
	 */
	@Accessor(qualifier = "entryNumber", type = Accessor.Type.SETTER)
	public void setEntryNumber(final Integer value)
	{
		_entryNumber = getPersistenceContext().setValue(ENTRYNUMBER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrderEntry.giveAway</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the giveAway
	 */
	@Accessor(qualifier = "giveAway", type = Accessor.Type.SETTER)
	public void setGiveAway(final Boolean value)
	{
		_giveAway = getPersistenceContext().setValue(GIVEAWAY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrderEntry.info</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the info
	 */
	@Accessor(qualifier = "info", type = Accessor.Type.SETTER)
	public void setInfo(final String value)
	{
		_info = getPersistenceContext().setValue(INFO, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrderEntry.order</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the order
	 */
	@Accessor(qualifier = "order", type = Accessor.Type.SETTER)
	public void setOrder(final AbstractOrderModel value)
	{
		_order = getPersistenceContext().setValue(ORDER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrderEntry.product</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the product
	 */
	@Accessor(qualifier = "product", type = Accessor.Type.SETTER)
	public void setProduct(final ProductModel value)
	{
		_product = getPersistenceContext().setValue(PRODUCT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrderEntry.quantity</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the quantity
	 */
	@Accessor(qualifier = "quantity", type = Accessor.Type.SETTER)
	public void setQuantity(final Long value)
	{
		_quantity = getPersistenceContext().setValue(QUANTITY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrderEntry.rejected</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the rejected
	 */
	@Accessor(qualifier = "rejected", type = Accessor.Type.SETTER)
	public void setRejected(final Boolean value)
	{
		_rejected = getPersistenceContext().setValue(REJECTED, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrderEntry.taxValues</code> dynamic attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the taxValues
	 */
	@Accessor(qualifier = "taxValues", type = Accessor.Type.SETTER)
	public void setTaxValues(final Collection<TaxValue> value)
	{
		getPersistenceContext().setDynamicValue(this,TAXVALUES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrderEntry.taxValuesInternal</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the taxValuesInternal
	 */
	@Accessor(qualifier = "taxValuesInternal", type = Accessor.Type.SETTER)
	public void setTaxValuesInternal(final String value)
	{
		_taxValuesInternal = getPersistenceContext().setValue(TAXVALUESINTERNAL, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrderEntry.totalPrice</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the totalPrice
	 */
	@Accessor(qualifier = "totalPrice", type = Accessor.Type.SETTER)
	public void setTotalPrice(final Double value)
	{
		_totalPrice = getPersistenceContext().setValue(TOTALPRICE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>AbstractOrderEntry.unit</code> attribute defined at extension <code>core</code>. 
	 *  
	 * @param value the unit
	 */
	@Accessor(qualifier = "unit", type = Accessor.Type.SETTER)
	public void setUnit(final UnitModel value)
	{
		_unit = getPersistenceContext().setValue(UNIT, value);
	}
	
}

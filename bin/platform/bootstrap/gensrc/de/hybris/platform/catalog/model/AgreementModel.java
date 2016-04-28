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
package de.hybris.platform.catalog.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.CompanyModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;

/**
 * Generated model class for type Agreement first defined at extension catalog.
 */
@SuppressWarnings("all")
public class AgreementModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "Agreement";
	
	/** <i>Generated constant</i> - Attribute key of <code>Agreement.id</code> attribute defined at extension <code>catalog</code>. */
	public static final String ID = "id";
	
	/** <i>Generated constant</i> - Attribute key of <code>Agreement.startdate</code> attribute defined at extension <code>catalog</code>. */
	public static final String STARTDATE = "startdate";
	
	/** <i>Generated constant</i> - Attribute key of <code>Agreement.enddate</code> attribute defined at extension <code>catalog</code>. */
	public static final String ENDDATE = "enddate";
	
	/** <i>Generated constant</i> - Attribute key of <code>Agreement.CatalogVersion</code> attribute defined at extension <code>catalog</code>. */
	public static final String CATALOGVERSION = "CatalogVersion";
	
	/** <i>Generated constant</i> - Attribute key of <code>Agreement.buyer</code> attribute defined at extension <code>catalog</code>. */
	public static final String BUYER = "buyer";
	
	/** <i>Generated constant</i> - Attribute key of <code>Agreement.supplier</code> attribute defined at extension <code>catalog</code>. */
	public static final String SUPPLIER = "supplier";
	
	/** <i>Generated constant</i> - Attribute key of <code>Agreement.buyerContact</code> attribute defined at extension <code>catalog</code>. */
	public static final String BUYERCONTACT = "buyerContact";
	
	/** <i>Generated constant</i> - Attribute key of <code>Agreement.supplierContact</code> attribute defined at extension <code>catalog</code>. */
	public static final String SUPPLIERCONTACT = "supplierContact";
	
	/** <i>Generated constant</i> - Attribute key of <code>Agreement.currency</code> attribute defined at extension <code>catalog</code>. */
	public static final String CURRENCY = "currency";
	
	
	/** <i>Generated variable</i> - Variable of <code>Agreement.id</code> attribute defined at extension <code>catalog</code>. */
	private String _id;
	
	/** <i>Generated variable</i> - Variable of <code>Agreement.startdate</code> attribute defined at extension <code>catalog</code>. */
	private Date _startdate;
	
	/** <i>Generated variable</i> - Variable of <code>Agreement.enddate</code> attribute defined at extension <code>catalog</code>. */
	private Date _enddate;
	
	/** <i>Generated variable</i> - Variable of <code>Agreement.CatalogVersion</code> attribute defined at extension <code>catalog</code>. */
	private CatalogVersionModel _CatalogVersion;
	
	/** <i>Generated variable</i> - Variable of <code>Agreement.buyer</code> attribute defined at extension <code>catalog</code>. */
	private CompanyModel _buyer;
	
	/** <i>Generated variable</i> - Variable of <code>Agreement.supplier</code> attribute defined at extension <code>catalog</code>. */
	private CompanyModel _supplier;
	
	/** <i>Generated variable</i> - Variable of <code>Agreement.buyerContact</code> attribute defined at extension <code>catalog</code>. */
	private UserModel _buyerContact;
	
	/** <i>Generated variable</i> - Variable of <code>Agreement.supplierContact</code> attribute defined at extension <code>catalog</code>. */
	private UserModel _supplierContact;
	
	/** <i>Generated variable</i> - Variable of <code>Agreement.currency</code> attribute defined at extension <code>catalog</code>. */
	private CurrencyModel _currency;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public AgreementModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public AgreementModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _enddate initial attribute declared by type <code>Agreement</code> at extension <code>catalog</code>
	 * @param _id initial attribute declared by type <code>Agreement</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public AgreementModel(final Date _enddate, final String _id)
	{
		super();
		setEnddate(_enddate);
		setId(_id);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _enddate initial attribute declared by type <code>Agreement</code> at extension <code>catalog</code>
	 * @param _id initial attribute declared by type <code>Agreement</code> at extension <code>catalog</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public AgreementModel(final Date _enddate, final String _id, final ItemModel _owner)
	{
		super();
		setEnddate(_enddate);
		setId(_id);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Agreement.buyer</code> attribute defined at extension <code>catalog</code>. 
	 * @return the buyer - buyer
	 */
	@Accessor(qualifier = "buyer", type = Accessor.Type.GETTER)
	public CompanyModel getBuyer()
	{
		if (this._buyer!=null)
		{
			return _buyer;
		}
		return _buyer = getPersistenceContext().getValue(BUYER, _buyer);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Agreement.buyerContact</code> attribute defined at extension <code>catalog</code>. 
	 * @return the buyerContact - buyerContact
	 */
	@Accessor(qualifier = "buyerContact", type = Accessor.Type.GETTER)
	public UserModel getBuyerContact()
	{
		if (this._buyerContact!=null)
		{
			return _buyerContact;
		}
		return _buyerContact = getPersistenceContext().getValue(BUYERCONTACT, _buyerContact);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Agreement.CatalogVersion</code> attribute defined at extension <code>catalog</code>. 
	 * @return the CatalogVersion - CatalogVersion
	 */
	@Accessor(qualifier = "CatalogVersion", type = Accessor.Type.GETTER)
	public CatalogVersionModel getCatalogVersion()
	{
		if (this._CatalogVersion!=null)
		{
			return _CatalogVersion;
		}
		return _CatalogVersion = getPersistenceContext().getValue(CATALOGVERSION, _CatalogVersion);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Agreement.currency</code> attribute defined at extension <code>catalog</code>. 
	 * @return the currency - currency
	 */
	@Accessor(qualifier = "currency", type = Accessor.Type.GETTER)
	public CurrencyModel getCurrency()
	{
		if (this._currency!=null)
		{
			return _currency;
		}
		return _currency = getPersistenceContext().getValue(CURRENCY, _currency);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Agreement.enddate</code> attribute defined at extension <code>catalog</code>. 
	 * @return the enddate - Agreement ID
	 */
	@Accessor(qualifier = "enddate", type = Accessor.Type.GETTER)
	public Date getEnddate()
	{
		if (this._enddate!=null)
		{
			return _enddate;
		}
		return _enddate = getPersistenceContext().getValue(ENDDATE, _enddate);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Agreement.id</code> attribute defined at extension <code>catalog</code>. 
	 * @return the id - Agreement ID
	 */
	@Accessor(qualifier = "id", type = Accessor.Type.GETTER)
	public String getId()
	{
		if (this._id!=null)
		{
			return _id;
		}
		return _id = getPersistenceContext().getValue(ID, _id);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Agreement.startdate</code> attribute defined at extension <code>catalog</code>. 
	 * @return the startdate - Start Date
	 */
	@Accessor(qualifier = "startdate", type = Accessor.Type.GETTER)
	public Date getStartdate()
	{
		if (this._startdate!=null)
		{
			return _startdate;
		}
		return _startdate = getPersistenceContext().getValue(STARTDATE, _startdate);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Agreement.supplier</code> attribute defined at extension <code>catalog</code>. 
	 * @return the supplier - supplier
	 */
	@Accessor(qualifier = "supplier", type = Accessor.Type.GETTER)
	public CompanyModel getSupplier()
	{
		if (this._supplier!=null)
		{
			return _supplier;
		}
		return _supplier = getPersistenceContext().getValue(SUPPLIER, _supplier);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Agreement.supplierContact</code> attribute defined at extension <code>catalog</code>. 
	 * @return the supplierContact - supplierContact
	 */
	@Accessor(qualifier = "supplierContact", type = Accessor.Type.GETTER)
	public UserModel getSupplierContact()
	{
		if (this._supplierContact!=null)
		{
			return _supplierContact;
		}
		return _supplierContact = getPersistenceContext().getValue(SUPPLIERCONTACT, _supplierContact);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Agreement.buyer</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the buyer - buyer
	 */
	@Accessor(qualifier = "buyer", type = Accessor.Type.SETTER)
	public void setBuyer(final CompanyModel value)
	{
		_buyer = getPersistenceContext().setValue(BUYER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Agreement.buyerContact</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the buyerContact - buyerContact
	 */
	@Accessor(qualifier = "buyerContact", type = Accessor.Type.SETTER)
	public void setBuyerContact(final UserModel value)
	{
		_buyerContact = getPersistenceContext().setValue(BUYERCONTACT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Agreement.CatalogVersion</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the CatalogVersion - CatalogVersion
	 */
	@Accessor(qualifier = "CatalogVersion", type = Accessor.Type.SETTER)
	public void setCatalogVersion(final CatalogVersionModel value)
	{
		_CatalogVersion = getPersistenceContext().setValue(CATALOGVERSION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Agreement.currency</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the currency - currency
	 */
	@Accessor(qualifier = "currency", type = Accessor.Type.SETTER)
	public void setCurrency(final CurrencyModel value)
	{
		_currency = getPersistenceContext().setValue(CURRENCY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Agreement.enddate</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the enddate - Agreement ID
	 */
	@Accessor(qualifier = "enddate", type = Accessor.Type.SETTER)
	public void setEnddate(final Date value)
	{
		_enddate = getPersistenceContext().setValue(ENDDATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Agreement.id</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the id - Agreement ID
	 */
	@Accessor(qualifier = "id", type = Accessor.Type.SETTER)
	public void setId(final String value)
	{
		_id = getPersistenceContext().setValue(ID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Agreement.startdate</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the startdate - Start Date
	 */
	@Accessor(qualifier = "startdate", type = Accessor.Type.SETTER)
	public void setStartdate(final Date value)
	{
		_startdate = getPersistenceContext().setValue(STARTDATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Agreement.supplier</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the supplier - supplier
	 */
	@Accessor(qualifier = "supplier", type = Accessor.Type.SETTER)
	public void setSupplier(final CompanyModel value)
	{
		_supplier = getPersistenceContext().setValue(SUPPLIER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Agreement.supplierContact</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the supplierContact - supplierContact
	 */
	@Accessor(qualifier = "supplierContact", type = Accessor.Type.SETTER)
	public void setSupplierContact(final UserModel value)
	{
		_supplierContact = getPersistenceContext().setValue(SUPPLIERCONTACT, value);
	}
	
}

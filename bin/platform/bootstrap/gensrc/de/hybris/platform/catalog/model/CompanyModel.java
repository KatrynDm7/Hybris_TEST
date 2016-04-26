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
package de.hybris.platform.catalog.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.enums.LineOfBusiness;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CompanyModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

/**
 * Generated model class for type Company first defined at extension catalog.
 */
@SuppressWarnings("all")
public class CompanyModel extends UserGroupModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "Company";
	
	/** <i>Generated constant</i> - Attribute key of <code>Company.dunsID</code> attribute defined at extension <code>catalog</code>. */
	public static final String DUNSID = "dunsID";
	
	/** <i>Generated constant</i> - Attribute key of <code>Company.ilnID</code> attribute defined at extension <code>catalog</code>. */
	public static final String ILNID = "ilnID";
	
	/** <i>Generated constant</i> - Attribute key of <code>Company.buyerSpecificID</code> attribute defined at extension <code>catalog</code>. */
	public static final String BUYERSPECIFICID = "buyerSpecificID";
	
	/** <i>Generated constant</i> - Attribute key of <code>Company.Id</code> attribute defined at extension <code>catalog</code>. */
	public static final String ID = "Id";
	
	/** <i>Generated constant</i> - Attribute key of <code>Company.supplierSpecificID</code> attribute defined at extension <code>catalog</code>. */
	public static final String SUPPLIERSPECIFICID = "supplierSpecificID";
	
	/** <i>Generated constant</i> - Attribute key of <code>Company.addresses</code> attribute defined at extension <code>catalog</code>. */
	public static final String ADDRESSES = "addresses";
	
	/** <i>Generated constant</i> - Attribute key of <code>Company.medias</code> attribute defined at extension <code>catalog</code>. */
	public static final String MEDIAS = "medias";
	
	/** <i>Generated constant</i> - Attribute key of <code>Company.shippingAddresses</code> attribute defined at extension <code>catalog</code>. */
	public static final String SHIPPINGADDRESSES = "shippingAddresses";
	
	/** <i>Generated constant</i> - Attribute key of <code>Company.shippingAddress</code> attribute defined at extension <code>catalog</code>. */
	public static final String SHIPPINGADDRESS = "shippingAddress";
	
	/** <i>Generated constant</i> - Attribute key of <code>Company.unloadingAddresses</code> attribute defined at extension <code>catalog</code>. */
	public static final String UNLOADINGADDRESSES = "unloadingAddresses";
	
	/** <i>Generated constant</i> - Attribute key of <code>Company.unloadingAddress</code> attribute defined at extension <code>catalog</code>. */
	public static final String UNLOADINGADDRESS = "unloadingAddress";
	
	/** <i>Generated constant</i> - Attribute key of <code>Company.billingAddresses</code> attribute defined at extension <code>catalog</code>. */
	public static final String BILLINGADDRESSES = "billingAddresses";
	
	/** <i>Generated constant</i> - Attribute key of <code>Company.billingAddress</code> attribute defined at extension <code>catalog</code>. */
	public static final String BILLINGADDRESS = "billingAddress";
	
	/** <i>Generated constant</i> - Attribute key of <code>Company.contactAddresses</code> attribute defined at extension <code>catalog</code>. */
	public static final String CONTACTADDRESSES = "contactAddresses";
	
	/** <i>Generated constant</i> - Attribute key of <code>Company.contactAddress</code> attribute defined at extension <code>catalog</code>. */
	public static final String CONTACTADDRESS = "contactAddress";
	
	/** <i>Generated constant</i> - Attribute key of <code>Company.contact</code> attribute defined at extension <code>catalog</code>. */
	public static final String CONTACT = "contact";
	
	/** <i>Generated constant</i> - Attribute key of <code>Company.vatID</code> attribute defined at extension <code>catalog</code>. */
	public static final String VATID = "vatID";
	
	/** <i>Generated constant</i> - Attribute key of <code>Company.responsibleCompany</code> attribute defined at extension <code>catalog</code>. */
	public static final String RESPONSIBLECOMPANY = "responsibleCompany";
	
	/** <i>Generated constant</i> - Attribute key of <code>Company.country</code> attribute defined at extension <code>catalog</code>. */
	public static final String COUNTRY = "country";
	
	/** <i>Generated constant</i> - Attribute key of <code>Company.lineOfBuisness</code> attribute defined at extension <code>catalog</code>. */
	public static final String LINEOFBUISNESS = "lineOfBuisness";
	
	/** <i>Generated constant</i> - Attribute key of <code>Company.buyer</code> attribute defined at extension <code>catalog</code>. */
	public static final String BUYER = "buyer";
	
	/** <i>Generated constant</i> - Attribute key of <code>Company.supplier</code> attribute defined at extension <code>catalog</code>. */
	public static final String SUPPLIER = "supplier";
	
	/** <i>Generated constant</i> - Attribute key of <code>Company.manufacturer</code> attribute defined at extension <code>catalog</code>. */
	public static final String MANUFACTURER = "manufacturer";
	
	/** <i>Generated constant</i> - Attribute key of <code>Company.carrier</code> attribute defined at extension <code>catalog</code>. */
	public static final String CARRIER = "carrier";
	
	/** <i>Generated constant</i> - Attribute key of <code>Company.providedCatalogs</code> attribute defined at extension <code>catalog</code>. */
	public static final String PROVIDEDCATALOGS = "providedCatalogs";
	
	/** <i>Generated constant</i> - Attribute key of <code>Company.purchasedCatalogs</code> attribute defined at extension <code>catalog</code>. */
	public static final String PURCHASEDCATALOGS = "purchasedCatalogs";
	
	
	/** <i>Generated variable</i> - Variable of <code>Company.dunsID</code> attribute defined at extension <code>catalog</code>. */
	private String _dunsID;
	
	/** <i>Generated variable</i> - Variable of <code>Company.ilnID</code> attribute defined at extension <code>catalog</code>. */
	private String _ilnID;
	
	/** <i>Generated variable</i> - Variable of <code>Company.buyerSpecificID</code> attribute defined at extension <code>catalog</code>. */
	private String _buyerSpecificID;
	
	/** <i>Generated variable</i> - Variable of <code>Company.Id</code> attribute defined at extension <code>catalog</code>. */
	private String _Id;
	
	/** <i>Generated variable</i> - Variable of <code>Company.supplierSpecificID</code> attribute defined at extension <code>catalog</code>. */
	private String _supplierSpecificID;
	
	/** <i>Generated variable</i> - Variable of <code>Company.addresses</code> attribute defined at extension <code>catalog</code>. */
	private Collection<AddressModel> _addresses;
	
	/** <i>Generated variable</i> - Variable of <code>Company.medias</code> attribute defined at extension <code>catalog</code>. */
	private Collection<MediaModel> _medias;
	
	/** <i>Generated variable</i> - Variable of <code>Company.shippingAddresses</code> attribute defined at extension <code>catalog</code>. */
	private Collection<AddressModel> _shippingAddresses;
	
	/** <i>Generated variable</i> - Variable of <code>Company.shippingAddress</code> attribute defined at extension <code>catalog</code>. */
	private AddressModel _shippingAddress;
	
	/** <i>Generated variable</i> - Variable of <code>Company.unloadingAddresses</code> attribute defined at extension <code>catalog</code>. */
	private Collection<AddressModel> _unloadingAddresses;
	
	/** <i>Generated variable</i> - Variable of <code>Company.unloadingAddress</code> attribute defined at extension <code>catalog</code>. */
	private AddressModel _unloadingAddress;
	
	/** <i>Generated variable</i> - Variable of <code>Company.billingAddresses</code> attribute defined at extension <code>catalog</code>. */
	private Collection<AddressModel> _billingAddresses;
	
	/** <i>Generated variable</i> - Variable of <code>Company.billingAddress</code> attribute defined at extension <code>catalog</code>. */
	private AddressModel _billingAddress;
	
	/** <i>Generated variable</i> - Variable of <code>Company.contactAddresses</code> attribute defined at extension <code>catalog</code>. */
	private Collection<AddressModel> _contactAddresses;
	
	/** <i>Generated variable</i> - Variable of <code>Company.contactAddress</code> attribute defined at extension <code>catalog</code>. */
	private AddressModel _contactAddress;
	
	/** <i>Generated variable</i> - Variable of <code>Company.contact</code> attribute defined at extension <code>catalog</code>. */
	private UserModel _contact;
	
	/** <i>Generated variable</i> - Variable of <code>Company.vatID</code> attribute defined at extension <code>catalog</code>. */
	private String _vatID;
	
	/** <i>Generated variable</i> - Variable of <code>Company.responsibleCompany</code> attribute defined at extension <code>catalog</code>. */
	private CompanyModel _responsibleCompany;
	
	/** <i>Generated variable</i> - Variable of <code>Company.country</code> attribute defined at extension <code>catalog</code>. */
	private CountryModel _country;
	
	/** <i>Generated variable</i> - Variable of <code>Company.lineOfBuisness</code> attribute defined at extension <code>catalog</code>. */
	private LineOfBusiness _lineOfBuisness;
	
	/** <i>Generated variable</i> - Variable of <code>Company.buyer</code> attribute defined at extension <code>catalog</code>. */
	private Boolean _buyer;
	
	/** <i>Generated variable</i> - Variable of <code>Company.supplier</code> attribute defined at extension <code>catalog</code>. */
	private Boolean _supplier;
	
	/** <i>Generated variable</i> - Variable of <code>Company.manufacturer</code> attribute defined at extension <code>catalog</code>. */
	private Boolean _manufacturer;
	
	/** <i>Generated variable</i> - Variable of <code>Company.carrier</code> attribute defined at extension <code>catalog</code>. */
	private Boolean _carrier;
	
	/** <i>Generated variable</i> - Variable of <code>Company.providedCatalogs</code> attribute defined at extension <code>catalog</code>. */
	private Collection<CatalogModel> _providedCatalogs;
	
	/** <i>Generated variable</i> - Variable of <code>Company.purchasedCatalogs</code> attribute defined at extension <code>catalog</code>. */
	private Collection<CatalogModel> _purchasedCatalogs;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CompanyModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CompanyModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _uid initial attribute declared by type <code>Principal</code> at extension <code>core</code>
	 */
	@Deprecated
	public CompanyModel(final String _uid)
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
	public CompanyModel(final ItemModel _owner, final String _uid)
	{
		super();
		setOwner(_owner);
		setUid(_uid);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Company.addresses</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the addresses
	 */
	@Accessor(qualifier = "addresses", type = Accessor.Type.GETTER)
	public Collection<AddressModel> getAddresses()
	{
		if (this._addresses!=null)
		{
			return _addresses;
		}
		return _addresses = getPersistenceContext().getValue(ADDRESSES, _addresses);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Company.billingAddress</code> attribute defined at extension <code>catalog</code>. 
	 * @return the billingAddress - Billing address of this company
	 */
	@Accessor(qualifier = "billingAddress", type = Accessor.Type.GETTER)
	public AddressModel getBillingAddress()
	{
		if (this._billingAddress!=null)
		{
			return _billingAddress;
		}
		return _billingAddress = getPersistenceContext().getValue(BILLINGADDRESS, _billingAddress);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Company.billingAddresses</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the billingAddresses
	 */
	@Accessor(qualifier = "billingAddresses", type = Accessor.Type.GETTER)
	public Collection<AddressModel> getBillingAddresses()
	{
		if (this._billingAddresses!=null)
		{
			return _billingAddresses;
		}
		return _billingAddresses = getPersistenceContext().getValue(BILLINGADDRESSES, _billingAddresses);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Company.buyer</code> attribute defined at extension <code>catalog</code>. 
	 * @return the buyer - buyer
	 */
	@Accessor(qualifier = "buyer", type = Accessor.Type.GETTER)
	public Boolean getBuyer()
	{
		if (this._buyer!=null)
		{
			return _buyer;
		}
		return _buyer = getPersistenceContext().getValue(BUYER, _buyer);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Company.buyerSpecificID</code> attribute defined at extension <code>catalog</code>. 
	 * @return the buyerSpecificID - Buyer Specific ID
	 */
	@Accessor(qualifier = "buyerSpecificID", type = Accessor.Type.GETTER)
	public String getBuyerSpecificID()
	{
		if (this._buyerSpecificID!=null)
		{
			return _buyerSpecificID;
		}
		return _buyerSpecificID = getPersistenceContext().getValue(BUYERSPECIFICID, _buyerSpecificID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Company.carrier</code> attribute defined at extension <code>catalog</code>. 
	 * @return the carrier - carrier
	 */
	@Accessor(qualifier = "carrier", type = Accessor.Type.GETTER)
	public Boolean getCarrier()
	{
		if (this._carrier!=null)
		{
			return _carrier;
		}
		return _carrier = getPersistenceContext().getValue(CARRIER, _carrier);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Company.contact</code> attribute defined at extension <code>catalog</code>. 
	 * @return the contact - Contact for this company
	 */
	@Accessor(qualifier = "contact", type = Accessor.Type.GETTER)
	public UserModel getContact()
	{
		if (this._contact!=null)
		{
			return _contact;
		}
		return _contact = getPersistenceContext().getValue(CONTACT, _contact);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Company.contactAddress</code> attribute defined at extension <code>catalog</code>. 
	 * @return the contactAddress - Contact address of this company
	 */
	@Accessor(qualifier = "contactAddress", type = Accessor.Type.GETTER)
	public AddressModel getContactAddress()
	{
		if (this._contactAddress!=null)
		{
			return _contactAddress;
		}
		return _contactAddress = getPersistenceContext().getValue(CONTACTADDRESS, _contactAddress);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Company.contactAddresses</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the contactAddresses
	 */
	@Accessor(qualifier = "contactAddresses", type = Accessor.Type.GETTER)
	public Collection<AddressModel> getContactAddresses()
	{
		if (this._contactAddresses!=null)
		{
			return _contactAddresses;
		}
		return _contactAddresses = getPersistenceContext().getValue(CONTACTADDRESSES, _contactAddresses);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Company.country</code> attribute defined at extension <code>catalog</code>. 
	 * @return the country - country
	 */
	@Accessor(qualifier = "country", type = Accessor.Type.GETTER)
	public CountryModel getCountry()
	{
		if (this._country!=null)
		{
			return _country;
		}
		return _country = getPersistenceContext().getValue(COUNTRY, _country);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Company.dunsID</code> attribute defined at extension <code>catalog</code>. 
	 * @return the dunsID - DUNS
	 */
	@Accessor(qualifier = "dunsID", type = Accessor.Type.GETTER)
	public String getDunsID()
	{
		if (this._dunsID!=null)
		{
			return _dunsID;
		}
		return _dunsID = getPersistenceContext().getValue(DUNSID, _dunsID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Company.Id</code> attribute defined at extension <code>catalog</code>. 
	 * @return the Id - id
	 */
	@Accessor(qualifier = "Id", type = Accessor.Type.GETTER)
	public String getId()
	{
		if (this._Id!=null)
		{
			return _Id;
		}
		return _Id = getPersistenceContext().getValue(ID, _Id);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Company.ilnID</code> attribute defined at extension <code>catalog</code>. 
	 * @return the ilnID - ILN
	 */
	@Accessor(qualifier = "ilnID", type = Accessor.Type.GETTER)
	public String getIlnID()
	{
		if (this._ilnID!=null)
		{
			return _ilnID;
		}
		return _ilnID = getPersistenceContext().getValue(ILNID, _ilnID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Company.lineOfBuisness</code> attribute defined at extension <code>catalog</code>. 
	 * @return the lineOfBuisness - line of business
	 */
	@Accessor(qualifier = "lineOfBuisness", type = Accessor.Type.GETTER)
	public LineOfBusiness getLineOfBuisness()
	{
		if (this._lineOfBuisness!=null)
		{
			return _lineOfBuisness;
		}
		return _lineOfBuisness = getPersistenceContext().getValue(LINEOFBUISNESS, _lineOfBuisness);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Company.manufacturer</code> attribute defined at extension <code>catalog</code>. 
	 * @return the manufacturer - manufacturer
	 */
	@Accessor(qualifier = "manufacturer", type = Accessor.Type.GETTER)
	public Boolean getManufacturer()
	{
		if (this._manufacturer!=null)
		{
			return _manufacturer;
		}
		return _manufacturer = getPersistenceContext().getValue(MANUFACTURER, _manufacturer);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Company.medias</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the medias - medias
	 */
	@Accessor(qualifier = "medias", type = Accessor.Type.GETTER)
	public Collection<MediaModel> getMedias()
	{
		if (this._medias!=null)
		{
			return _medias;
		}
		return _medias = getPersistenceContext().getValue(MEDIAS, _medias);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Company.providedCatalogs</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the providedCatalogs
	 */
	@Accessor(qualifier = "providedCatalogs", type = Accessor.Type.GETTER)
	public Collection<CatalogModel> getProvidedCatalogs()
	{
		if (this._providedCatalogs!=null)
		{
			return _providedCatalogs;
		}
		return _providedCatalogs = getPersistenceContext().getValue(PROVIDEDCATALOGS, _providedCatalogs);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Company.purchasedCatalogs</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the purchasedCatalogs
	 */
	@Accessor(qualifier = "purchasedCatalogs", type = Accessor.Type.GETTER)
	public Collection<CatalogModel> getPurchasedCatalogs()
	{
		if (this._purchasedCatalogs!=null)
		{
			return _purchasedCatalogs;
		}
		return _purchasedCatalogs = getPersistenceContext().getValue(PURCHASEDCATALOGS, _purchasedCatalogs);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Company.responsibleCompany</code> attribute defined at extension <code>catalog</code>. 
	 * @return the responsibleCompany - responsible company
	 */
	@Accessor(qualifier = "responsibleCompany", type = Accessor.Type.GETTER)
	public CompanyModel getResponsibleCompany()
	{
		if (this._responsibleCompany!=null)
		{
			return _responsibleCompany;
		}
		return _responsibleCompany = getPersistenceContext().getValue(RESPONSIBLECOMPANY, _responsibleCompany);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Company.shippingAddress</code> attribute defined at extension <code>catalog</code>. 
	 * @return the shippingAddress - Shipping address of this company
	 */
	@Accessor(qualifier = "shippingAddress", type = Accessor.Type.GETTER)
	public AddressModel getShippingAddress()
	{
		if (this._shippingAddress!=null)
		{
			return _shippingAddress;
		}
		return _shippingAddress = getPersistenceContext().getValue(SHIPPINGADDRESS, _shippingAddress);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Company.shippingAddresses</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the shippingAddresses
	 */
	@Accessor(qualifier = "shippingAddresses", type = Accessor.Type.GETTER)
	public Collection<AddressModel> getShippingAddresses()
	{
		if (this._shippingAddresses!=null)
		{
			return _shippingAddresses;
		}
		return _shippingAddresses = getPersistenceContext().getValue(SHIPPINGADDRESSES, _shippingAddresses);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Company.supplier</code> attribute defined at extension <code>catalog</code>. 
	 * @return the supplier - supplier
	 */
	@Accessor(qualifier = "supplier", type = Accessor.Type.GETTER)
	public Boolean getSupplier()
	{
		if (this._supplier!=null)
		{
			return _supplier;
		}
		return _supplier = getPersistenceContext().getValue(SUPPLIER, _supplier);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Company.supplierSpecificID</code> attribute defined at extension <code>catalog</code>. 
	 * @return the supplierSpecificID - Supplier Specific ID
	 */
	@Accessor(qualifier = "supplierSpecificID", type = Accessor.Type.GETTER)
	public String getSupplierSpecificID()
	{
		if (this._supplierSpecificID!=null)
		{
			return _supplierSpecificID;
		}
		return _supplierSpecificID = getPersistenceContext().getValue(SUPPLIERSPECIFICID, _supplierSpecificID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Company.unloadingAddress</code> attribute defined at extension <code>catalog</code>. 
	 * @return the unloadingAddress - Unloading address of this company
	 */
	@Accessor(qualifier = "unloadingAddress", type = Accessor.Type.GETTER)
	public AddressModel getUnloadingAddress()
	{
		if (this._unloadingAddress!=null)
		{
			return _unloadingAddress;
		}
		return _unloadingAddress = getPersistenceContext().getValue(UNLOADINGADDRESS, _unloadingAddress);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Company.unloadingAddresses</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the unloadingAddresses
	 */
	@Accessor(qualifier = "unloadingAddresses", type = Accessor.Type.GETTER)
	public Collection<AddressModel> getUnloadingAddresses()
	{
		if (this._unloadingAddresses!=null)
		{
			return _unloadingAddresses;
		}
		return _unloadingAddresses = getPersistenceContext().getValue(UNLOADINGADDRESSES, _unloadingAddresses);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Company.vatID</code> attribute defined at extension <code>catalog</code>. 
	 * @return the vatID - vat id
	 */
	@Accessor(qualifier = "vatID", type = Accessor.Type.GETTER)
	public String getVatID()
	{
		if (this._vatID!=null)
		{
			return _vatID;
		}
		return _vatID = getPersistenceContext().getValue(VATID, _vatID);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Company.addresses</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the addresses
	 */
	@Accessor(qualifier = "addresses", type = Accessor.Type.SETTER)
	public void setAddresses(final Collection<AddressModel> value)
	{
		_addresses = getPersistenceContext().setValue(ADDRESSES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Company.billingAddress</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the billingAddress - Billing address of this company
	 */
	@Accessor(qualifier = "billingAddress", type = Accessor.Type.SETTER)
	public void setBillingAddress(final AddressModel value)
	{
		_billingAddress = getPersistenceContext().setValue(BILLINGADDRESS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Company.buyer</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the buyer - buyer
	 */
	@Accessor(qualifier = "buyer", type = Accessor.Type.SETTER)
	public void setBuyer(final Boolean value)
	{
		_buyer = getPersistenceContext().setValue(BUYER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Company.buyerSpecificID</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the buyerSpecificID - Buyer Specific ID
	 */
	@Accessor(qualifier = "buyerSpecificID", type = Accessor.Type.SETTER)
	public void setBuyerSpecificID(final String value)
	{
		_buyerSpecificID = getPersistenceContext().setValue(BUYERSPECIFICID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Company.carrier</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the carrier - carrier
	 */
	@Accessor(qualifier = "carrier", type = Accessor.Type.SETTER)
	public void setCarrier(final Boolean value)
	{
		_carrier = getPersistenceContext().setValue(CARRIER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Company.contact</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the contact - Contact for this company
	 */
	@Accessor(qualifier = "contact", type = Accessor.Type.SETTER)
	public void setContact(final UserModel value)
	{
		_contact = getPersistenceContext().setValue(CONTACT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Company.contactAddress</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the contactAddress - Contact address of this company
	 */
	@Accessor(qualifier = "contactAddress", type = Accessor.Type.SETTER)
	public void setContactAddress(final AddressModel value)
	{
		_contactAddress = getPersistenceContext().setValue(CONTACTADDRESS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Company.country</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the country - country
	 */
	@Accessor(qualifier = "country", type = Accessor.Type.SETTER)
	public void setCountry(final CountryModel value)
	{
		_country = getPersistenceContext().setValue(COUNTRY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Company.dunsID</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the dunsID - DUNS
	 */
	@Accessor(qualifier = "dunsID", type = Accessor.Type.SETTER)
	public void setDunsID(final String value)
	{
		_dunsID = getPersistenceContext().setValue(DUNSID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Company.Id</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the Id - id
	 */
	@Accessor(qualifier = "Id", type = Accessor.Type.SETTER)
	public void setId(final String value)
	{
		_Id = getPersistenceContext().setValue(ID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Company.ilnID</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the ilnID - ILN
	 */
	@Accessor(qualifier = "ilnID", type = Accessor.Type.SETTER)
	public void setIlnID(final String value)
	{
		_ilnID = getPersistenceContext().setValue(ILNID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Company.lineOfBuisness</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the lineOfBuisness - line of business
	 */
	@Accessor(qualifier = "lineOfBuisness", type = Accessor.Type.SETTER)
	public void setLineOfBuisness(final LineOfBusiness value)
	{
		_lineOfBuisness = getPersistenceContext().setValue(LINEOFBUISNESS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Company.manufacturer</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the manufacturer - manufacturer
	 */
	@Accessor(qualifier = "manufacturer", type = Accessor.Type.SETTER)
	public void setManufacturer(final Boolean value)
	{
		_manufacturer = getPersistenceContext().setValue(MANUFACTURER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Company.medias</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the medias - medias
	 */
	@Accessor(qualifier = "medias", type = Accessor.Type.SETTER)
	public void setMedias(final Collection<MediaModel> value)
	{
		_medias = getPersistenceContext().setValue(MEDIAS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Company.responsibleCompany</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the responsibleCompany - responsible company
	 */
	@Accessor(qualifier = "responsibleCompany", type = Accessor.Type.SETTER)
	public void setResponsibleCompany(final CompanyModel value)
	{
		_responsibleCompany = getPersistenceContext().setValue(RESPONSIBLECOMPANY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Company.shippingAddress</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the shippingAddress - Shipping address of this company
	 */
	@Accessor(qualifier = "shippingAddress", type = Accessor.Type.SETTER)
	public void setShippingAddress(final AddressModel value)
	{
		_shippingAddress = getPersistenceContext().setValue(SHIPPINGADDRESS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Company.supplier</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the supplier - supplier
	 */
	@Accessor(qualifier = "supplier", type = Accessor.Type.SETTER)
	public void setSupplier(final Boolean value)
	{
		_supplier = getPersistenceContext().setValue(SUPPLIER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Company.supplierSpecificID</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the supplierSpecificID - Supplier Specific ID
	 */
	@Accessor(qualifier = "supplierSpecificID", type = Accessor.Type.SETTER)
	public void setSupplierSpecificID(final String value)
	{
		_supplierSpecificID = getPersistenceContext().setValue(SUPPLIERSPECIFICID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Company.unloadingAddress</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the unloadingAddress - Unloading address of this company
	 */
	@Accessor(qualifier = "unloadingAddress", type = Accessor.Type.SETTER)
	public void setUnloadingAddress(final AddressModel value)
	{
		_unloadingAddress = getPersistenceContext().setValue(UNLOADINGADDRESS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Company.vatID</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the vatID - vat id
	 */
	@Accessor(qualifier = "vatID", type = Accessor.Type.SETTER)
	public void setVatID(final String value)
	{
		_vatID = getPersistenceContext().setValue(VATID, value);
	}
	
}

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
import de.hybris.platform.catalog.model.AgreementModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.CompanyModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Generated model class for type Catalog first defined at extension catalog.
 */
@SuppressWarnings("all")
public class CatalogModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "Catalog";
	
	/** <i>Generated constant</i> - Attribute key of <code>Catalog.id</code> attribute defined at extension <code>catalog</code>. */
	public static final String ID = "id";
	
	/** <i>Generated constant</i> - Attribute key of <code>Catalog.name</code> attribute defined at extension <code>catalog</code>. */
	public static final String NAME = "name";
	
	/** <i>Generated constant</i> - Attribute key of <code>Catalog.activeCatalogVersion</code> attribute defined at extension <code>catalog</code>. */
	public static final String ACTIVECATALOGVERSION = "activeCatalogVersion";
	
	/** <i>Generated constant</i> - Attribute key of <code>Catalog.rootCategories</code> attribute defined at extension <code>catalog</code>. */
	public static final String ROOTCATEGORIES = "rootCategories";
	
	/** <i>Generated constant</i> - Attribute key of <code>Catalog.defaultCatalog</code> attribute defined at extension <code>catalog</code>. */
	public static final String DEFAULTCATALOG = "defaultCatalog";
	
	/** <i>Generated constant</i> - Attribute key of <code>Catalog.supplier</code> attribute defined at extension <code>catalog</code>. */
	public static final String SUPPLIER = "supplier";
	
	/** <i>Generated constant</i> - Attribute key of <code>Catalog.buyer</code> attribute defined at extension <code>catalog</code>. */
	public static final String BUYER = "buyer";
	
	/** <i>Generated constant</i> - Attribute key of <code>Catalog.version</code> attribute defined at extension <code>catalog</code>. */
	public static final String VERSION = "version";
	
	/** <i>Generated constant</i> - Attribute key of <code>Catalog.mimeRootDirectory</code> attribute defined at extension <code>catalog</code>. */
	public static final String MIMEROOTDIRECTORY = "mimeRootDirectory";
	
	/** <i>Generated constant</i> - Attribute key of <code>Catalog.generationDate</code> attribute defined at extension <code>catalog</code>. */
	public static final String GENERATIONDATE = "generationDate";
	
	/** <i>Generated constant</i> - Attribute key of <code>Catalog.defaultCurrency</code> attribute defined at extension <code>catalog</code>. */
	public static final String DEFAULTCURRENCY = "defaultCurrency";
	
	/** <i>Generated constant</i> - Attribute key of <code>Catalog.inclFreight</code> attribute defined at extension <code>catalog</code>. */
	public static final String INCLFREIGHT = "inclFreight";
	
	/** <i>Generated constant</i> - Attribute key of <code>Catalog.inclPacking</code> attribute defined at extension <code>catalog</code>. */
	public static final String INCLPACKING = "inclPacking";
	
	/** <i>Generated constant</i> - Attribute key of <code>Catalog.inclAssurance</code> attribute defined at extension <code>catalog</code>. */
	public static final String INCLASSURANCE = "inclAssurance";
	
	/** <i>Generated constant</i> - Attribute key of <code>Catalog.inclDuty</code> attribute defined at extension <code>catalog</code>. */
	public static final String INCLDUTY = "inclDuty";
	
	/** <i>Generated constant</i> - Attribute key of <code>Catalog.territories</code> attribute defined at extension <code>catalog</code>. */
	public static final String TERRITORIES = "territories";
	
	/** <i>Generated constant</i> - Attribute key of <code>Catalog.languages</code> attribute defined at extension <code>catalog</code>. */
	public static final String LANGUAGES = "languages";
	
	/** <i>Generated constant</i> - Attribute key of <code>Catalog.generatorInfo</code> attribute defined at extension <code>catalog</code>. */
	public static final String GENERATORINFO = "generatorInfo";
	
	/** <i>Generated constant</i> - Attribute key of <code>Catalog.agreements</code> attribute defined at extension <code>catalog</code>. */
	public static final String AGREEMENTS = "agreements";
	
	/** <i>Generated constant</i> - Attribute key of <code>Catalog.previewURLTemplate</code> attribute defined at extension <code>catalog</code>. */
	public static final String PREVIEWURLTEMPLATE = "previewURLTemplate";
	
	/** <i>Generated constant</i> - Attribute key of <code>Catalog.urlPatterns</code> attribute defined at extension <code>catalog</code>. */
	public static final String URLPATTERNS = "urlPatterns";
	
	/** <i>Generated constant</i> - Attribute key of <code>Catalog.catalogVersions</code> attribute defined at extension <code>catalog</code>. */
	public static final String CATALOGVERSIONS = "catalogVersions";
	
	
	/** <i>Generated variable</i> - Variable of <code>Catalog.id</code> attribute defined at extension <code>catalog</code>. */
	private String _id;
	
	/** <i>Generated variable</i> - Variable of <code>Catalog.activeCatalogVersion</code> attribute defined at extension <code>catalog</code>. */
	private CatalogVersionModel _activeCatalogVersion;
	
	/** <i>Generated variable</i> - Variable of <code>Catalog.rootCategories</code> attribute defined at extension <code>catalog</code>. */
	private List<CategoryModel> _rootCategories;
	
	/** <i>Generated variable</i> - Variable of <code>Catalog.defaultCatalog</code> attribute defined at extension <code>catalog</code>. */
	private Boolean _defaultCatalog;
	
	/** <i>Generated variable</i> - Variable of <code>Catalog.supplier</code> attribute defined at extension <code>catalog</code>. */
	private CompanyModel _supplier;
	
	/** <i>Generated variable</i> - Variable of <code>Catalog.buyer</code> attribute defined at extension <code>catalog</code>. */
	private CompanyModel _buyer;
	
	/** <i>Generated variable</i> - Variable of <code>Catalog.version</code> attribute defined at extension <code>catalog</code>. */
	private String _version;
	
	/** <i>Generated variable</i> - Variable of <code>Catalog.mimeRootDirectory</code> attribute defined at extension <code>catalog</code>. */
	private String _mimeRootDirectory;
	
	/** <i>Generated variable</i> - Variable of <code>Catalog.generationDate</code> attribute defined at extension <code>catalog</code>. */
	private Date _generationDate;
	
	/** <i>Generated variable</i> - Variable of <code>Catalog.defaultCurrency</code> attribute defined at extension <code>catalog</code>. */
	private CurrencyModel _defaultCurrency;
	
	/** <i>Generated variable</i> - Variable of <code>Catalog.inclFreight</code> attribute defined at extension <code>catalog</code>. */
	private Boolean _inclFreight;
	
	/** <i>Generated variable</i> - Variable of <code>Catalog.inclPacking</code> attribute defined at extension <code>catalog</code>. */
	private Boolean _inclPacking;
	
	/** <i>Generated variable</i> - Variable of <code>Catalog.inclAssurance</code> attribute defined at extension <code>catalog</code>. */
	private Boolean _inclAssurance;
	
	/** <i>Generated variable</i> - Variable of <code>Catalog.inclDuty</code> attribute defined at extension <code>catalog</code>. */
	private Boolean _inclDuty;
	
	/** <i>Generated variable</i> - Variable of <code>Catalog.territories</code> attribute defined at extension <code>catalog</code>. */
	private Collection<CountryModel> _territories;
	
	/** <i>Generated variable</i> - Variable of <code>Catalog.languages</code> attribute defined at extension <code>catalog</code>. */
	private Collection<LanguageModel> _languages;
	
	/** <i>Generated variable</i> - Variable of <code>Catalog.generatorInfo</code> attribute defined at extension <code>catalog</code>. */
	private String _generatorInfo;
	
	/** <i>Generated variable</i> - Variable of <code>Catalog.agreements</code> attribute defined at extension <code>catalog</code>. */
	private Collection<AgreementModel> _agreements;
	
	/** <i>Generated variable</i> - Variable of <code>Catalog.previewURLTemplate</code> attribute defined at extension <code>catalog</code>. */
	private String _previewURLTemplate;
	
	/** <i>Generated variable</i> - Variable of <code>Catalog.urlPatterns</code> attribute defined at extension <code>catalog</code>. */
	private Collection<String> _urlPatterns;
	
	/** <i>Generated variable</i> - Variable of <code>Catalog.catalogVersions</code> attribute defined at extension <code>catalog</code>. */
	private Set<CatalogVersionModel> _catalogVersions;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CatalogModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CatalogModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _id initial attribute declared by type <code>Catalog</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public CatalogModel(final String _id)
	{
		super();
		setId(_id);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _id initial attribute declared by type <code>Catalog</code> at extension <code>catalog</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public CatalogModel(final String _id, final ItemModel _owner)
	{
		super();
		setId(_id);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Catalog.activeCatalogVersion</code> attribute defined at extension <code>catalog</code>. 
	 * @return the activeCatalogVersion - active CatalogVersion
	 */
	@Accessor(qualifier = "activeCatalogVersion", type = Accessor.Type.GETTER)
	public CatalogVersionModel getActiveCatalogVersion()
	{
		if (this._activeCatalogVersion!=null)
		{
			return _activeCatalogVersion;
		}
		return _activeCatalogVersion = getPersistenceContext().getValue(ACTIVECATALOGVERSION, _activeCatalogVersion);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Catalog.agreements</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the agreements - Agreement Collection
	 */
	@Accessor(qualifier = "agreements", type = Accessor.Type.GETTER)
	public Collection<AgreementModel> getAgreements()
	{
		if (this._agreements!=null)
		{
			return _agreements;
		}
		return _agreements = getPersistenceContext().getValue(AGREEMENTS, _agreements);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Catalog.buyer</code> attribute defined at extension <code>catalog</code>. 
	 * @return the buyer - Buyer
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
	 * <i>Generated method</i> - Getter of the <code>Catalog.catalogVersions</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the catalogVersions
	 */
	@Accessor(qualifier = "catalogVersions", type = Accessor.Type.GETTER)
	public Set<CatalogVersionModel> getCatalogVersions()
	{
		if (this._catalogVersions!=null)
		{
			return _catalogVersions;
		}
		return _catalogVersions = getPersistenceContext().getValue(CATALOGVERSIONS, _catalogVersions);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Catalog.defaultCatalog</code> attribute defined at extension <code>catalog</code>. 
	 * @return the defaultCatalog - Default catalog
	 */
	@Accessor(qualifier = "defaultCatalog", type = Accessor.Type.GETTER)
	public Boolean getDefaultCatalog()
	{
		if (this._defaultCatalog!=null)
		{
			return _defaultCatalog;
		}
		return _defaultCatalog = getPersistenceContext().getValue(DEFAULTCATALOG, _defaultCatalog);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Catalog.defaultCurrency</code> attribute defined at extension <code>catalog</code>. 
	 * @return the defaultCurrency - Default Currency
	 */
	@Accessor(qualifier = "defaultCurrency", type = Accessor.Type.GETTER)
	public CurrencyModel getDefaultCurrency()
	{
		if (this._defaultCurrency!=null)
		{
			return _defaultCurrency;
		}
		return _defaultCurrency = getPersistenceContext().getValue(DEFAULTCURRENCY, _defaultCurrency);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Catalog.generationDate</code> attribute defined at extension <code>catalog</code>. 
	 * @return the generationDate - Generation Date
	 */
	@Accessor(qualifier = "generationDate", type = Accessor.Type.GETTER)
	public Date getGenerationDate()
	{
		if (this._generationDate!=null)
		{
			return _generationDate;
		}
		return _generationDate = getPersistenceContext().getValue(GENERATIONDATE, _generationDate);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Catalog.generatorInfo</code> attribute defined at extension <code>catalog</code>. 
	 * @return the generatorInfo - Generator Info
	 */
	@Accessor(qualifier = "generatorInfo", type = Accessor.Type.GETTER)
	public String getGeneratorInfo()
	{
		if (this._generatorInfo!=null)
		{
			return _generatorInfo;
		}
		return _generatorInfo = getPersistenceContext().getValue(GENERATORINFO, _generatorInfo);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Catalog.id</code> attribute defined at extension <code>catalog</code>. 
	 * @return the id - ID
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
	 * <i>Generated method</i> - Getter of the <code>Catalog.inclAssurance</code> attribute defined at extension <code>catalog</code>. 
	 * @return the inclAssurance - incl.Assurance
	 */
	@Accessor(qualifier = "inclAssurance", type = Accessor.Type.GETTER)
	public Boolean getInclAssurance()
	{
		if (this._inclAssurance!=null)
		{
			return _inclAssurance;
		}
		return _inclAssurance = getPersistenceContext().getValue(INCLASSURANCE, _inclAssurance);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Catalog.inclDuty</code> attribute defined at extension <code>catalog</code>. 
	 * @return the inclDuty - incl.Duty
	 */
	@Accessor(qualifier = "inclDuty", type = Accessor.Type.GETTER)
	public Boolean getInclDuty()
	{
		if (this._inclDuty!=null)
		{
			return _inclDuty;
		}
		return _inclDuty = getPersistenceContext().getValue(INCLDUTY, _inclDuty);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Catalog.inclFreight</code> attribute defined at extension <code>catalog</code>. 
	 * @return the inclFreight - incl.Freight
	 */
	@Accessor(qualifier = "inclFreight", type = Accessor.Type.GETTER)
	public Boolean getInclFreight()
	{
		if (this._inclFreight!=null)
		{
			return _inclFreight;
		}
		return _inclFreight = getPersistenceContext().getValue(INCLFREIGHT, _inclFreight);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Catalog.inclPacking</code> attribute defined at extension <code>catalog</code>. 
	 * @return the inclPacking - incl.Packing
	 */
	@Accessor(qualifier = "inclPacking", type = Accessor.Type.GETTER)
	public Boolean getInclPacking()
	{
		if (this._inclPacking!=null)
		{
			return _inclPacking;
		}
		return _inclPacking = getPersistenceContext().getValue(INCLPACKING, _inclPacking);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Catalog.languages</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the languages - Language
	 */
	@Accessor(qualifier = "languages", type = Accessor.Type.GETTER)
	public Collection<LanguageModel> getLanguages()
	{
		if (this._languages!=null)
		{
			return _languages;
		}
		return _languages = getPersistenceContext().getValue(LANGUAGES, _languages);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Catalog.mimeRootDirectory</code> attribute defined at extension <code>catalog</code>. 
	 * @return the mimeRootDirectory - Mime Root Directory
	 */
	@Accessor(qualifier = "mimeRootDirectory", type = Accessor.Type.GETTER)
	public String getMimeRootDirectory()
	{
		if (this._mimeRootDirectory!=null)
		{
			return _mimeRootDirectory;
		}
		return _mimeRootDirectory = getPersistenceContext().getValue(MIMEROOTDIRECTORY, _mimeRootDirectory);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Catalog.name</code> attribute defined at extension <code>catalog</code>. 
	 * @return the name - Name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName()
	{
		return getName(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>Catalog.name</code> attribute defined at extension <code>catalog</code>. 
	 * @param loc the value localization key 
	 * @return the name - Name
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.GETTER)
	public String getName(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(NAME, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Catalog.previewURLTemplate</code> attribute defined at extension <code>catalog</code>. 
	 * @return the previewURLTemplate
	 */
	@Accessor(qualifier = "previewURLTemplate", type = Accessor.Type.GETTER)
	public String getPreviewURLTemplate()
	{
		if (this._previewURLTemplate!=null)
		{
			return _previewURLTemplate;
		}
		return _previewURLTemplate = getPersistenceContext().getValue(PREVIEWURLTEMPLATE, _previewURLTemplate);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Catalog.rootCategories</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the rootCategories - The categories of the active CatalogVersion
	 */
	@Accessor(qualifier = "rootCategories", type = Accessor.Type.GETTER)
	public List<CategoryModel> getRootCategories()
	{
		if (this._rootCategories!=null)
		{
			return _rootCategories;
		}
		return _rootCategories = getPersistenceContext().getValue(ROOTCATEGORIES, _rootCategories);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Catalog.supplier</code> attribute defined at extension <code>catalog</code>. 
	 * @return the supplier - Supplier
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
	 * <i>Generated method</i> - Getter of the <code>Catalog.territories</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the territories - Territory
	 */
	@Accessor(qualifier = "territories", type = Accessor.Type.GETTER)
	public Collection<CountryModel> getTerritories()
	{
		if (this._territories!=null)
		{
			return _territories;
		}
		return _territories = getPersistenceContext().getValue(TERRITORIES, _territories);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Catalog.urlPatterns</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the urlPatterns - Collection of URL patterns
	 */
	@Accessor(qualifier = "urlPatterns", type = Accessor.Type.GETTER)
	public Collection<String> getUrlPatterns()
	{
		if (this._urlPatterns!=null)
		{
			return _urlPatterns;
		}
		return _urlPatterns = getPersistenceContext().getValue(URLPATTERNS, _urlPatterns);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Catalog.version</code> attribute defined at extension <code>catalog</code>. 
	 * @return the version - version
	 */
	@Accessor(qualifier = "version", type = Accessor.Type.GETTER)
	public String getVersion()
	{
		if (this._version!=null)
		{
			return _version;
		}
		return _version = getPersistenceContext().getValue(VERSION, _version);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Catalog.activeCatalogVersion</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the activeCatalogVersion - active CatalogVersion
	 */
	@Accessor(qualifier = "activeCatalogVersion", type = Accessor.Type.SETTER)
	public void setActiveCatalogVersion(final CatalogVersionModel value)
	{
		_activeCatalogVersion = getPersistenceContext().setValue(ACTIVECATALOGVERSION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Catalog.buyer</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the buyer - Buyer
	 */
	@Accessor(qualifier = "buyer", type = Accessor.Type.SETTER)
	public void setBuyer(final CompanyModel value)
	{
		_buyer = getPersistenceContext().setValue(BUYER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Catalog.catalogVersions</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the catalogVersions
	 */
	@Accessor(qualifier = "catalogVersions", type = Accessor.Type.SETTER)
	public void setCatalogVersions(final Set<CatalogVersionModel> value)
	{
		_catalogVersions = getPersistenceContext().setValue(CATALOGVERSIONS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Catalog.defaultCatalog</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the defaultCatalog - Default catalog
	 */
	@Accessor(qualifier = "defaultCatalog", type = Accessor.Type.SETTER)
	public void setDefaultCatalog(final Boolean value)
	{
		_defaultCatalog = getPersistenceContext().setValue(DEFAULTCATALOG, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Catalog.id</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the id - ID
	 */
	@Accessor(qualifier = "id", type = Accessor.Type.SETTER)
	public void setId(final String value)
	{
		_id = getPersistenceContext().setValue(ID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Catalog.name</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the name - Name
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value)
	{
		setName(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>Catalog.name</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the name - Name
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "name", type = Accessor.Type.SETTER)
	public void setName(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(NAME, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Catalog.previewURLTemplate</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the previewURLTemplate
	 */
	@Accessor(qualifier = "previewURLTemplate", type = Accessor.Type.SETTER)
	public void setPreviewURLTemplate(final String value)
	{
		_previewURLTemplate = getPersistenceContext().setValue(PREVIEWURLTEMPLATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Catalog.supplier</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the supplier - Supplier
	 */
	@Accessor(qualifier = "supplier", type = Accessor.Type.SETTER)
	public void setSupplier(final CompanyModel value)
	{
		_supplier = getPersistenceContext().setValue(SUPPLIER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>Catalog.urlPatterns</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the urlPatterns - Collection of URL patterns
	 */
	@Accessor(qualifier = "urlPatterns", type = Accessor.Type.SETTER)
	public void setUrlPatterns(final Collection<String> value)
	{
		_urlPatterns = getPersistenceContext().setValue(URLPATTERNS, value);
	}
	
}

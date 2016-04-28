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
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Generated model class for type CatalogVersion first defined at extension catalog.
 */
@SuppressWarnings("all")
public class CatalogVersionModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "CatalogVersion";
	
	/**<i>Generated relation code constant for relation <code>Catalog2VersionsRelation</code> defining source attribute <code>catalog</code> in extension <code>catalog</code>.</i>*/
	public final static String _CATALOG2VERSIONSRELATION = "Catalog2VersionsRelation";
	
	/**<i>Generated relation code constant for relation <code>Principal2WriteableCatalogVersionRelation</code> defining source attribute <code>writePrincipals</code> in extension <code>catalog</code>.</i>*/
	public final static String _PRINCIPAL2WRITEABLECATALOGVERSIONRELATION = "Principal2WriteableCatalogVersionRelation";
	
	/**<i>Generated relation code constant for relation <code>Principal2ReadableCatalogVersionRelation</code> defining source attribute <code>readPrincipals</code> in extension <code>catalog</code>.</i>*/
	public final static String _PRINCIPAL2READABLECATALOGVERSIONRELATION = "Principal2ReadableCatalogVersionRelation";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersion.synchronizations</code> attribute defined at extension <code>catalog</code>. */
	public static final String SYNCHRONIZATIONS = "synchronizations";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersion.incomingSynchronizations</code> attribute defined at extension <code>catalog</code>. */
	public static final String INCOMINGSYNCHRONIZATIONS = "incomingSynchronizations";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersion.active</code> attribute defined at extension <code>catalog</code>. */
	public static final String ACTIVE = "active";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersion.version</code> attribute defined at extension <code>catalog</code>. */
	public static final String VERSION = "version";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersion.mimeRootDirectory</code> attribute defined at extension <code>catalog</code>. */
	public static final String MIMEROOTDIRECTORY = "mimeRootDirectory";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersion.generationDate</code> attribute defined at extension <code>catalog</code>. */
	public static final String GENERATIONDATE = "generationDate";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersion.defaultCurrency</code> attribute defined at extension <code>catalog</code>. */
	public static final String DEFAULTCURRENCY = "defaultCurrency";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersion.inclFreight</code> attribute defined at extension <code>catalog</code>. */
	public static final String INCLFREIGHT = "inclFreight";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersion.inclPacking</code> attribute defined at extension <code>catalog</code>. */
	public static final String INCLPACKING = "inclPacking";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersion.inclAssurance</code> attribute defined at extension <code>catalog</code>. */
	public static final String INCLASSURANCE = "inclAssurance";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersion.inclDuty</code> attribute defined at extension <code>catalog</code>. */
	public static final String INCLDUTY = "inclDuty";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersion.territories</code> attribute defined at extension <code>catalog</code>. */
	public static final String TERRITORIES = "territories";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersion.languages</code> attribute defined at extension <code>catalog</code>. */
	public static final String LANGUAGES = "languages";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersion.generatorInfo</code> attribute defined at extension <code>catalog</code>. */
	public static final String GENERATORINFO = "generatorInfo";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersion.agreements</code> attribute defined at extension <code>catalog</code>. */
	public static final String AGREEMENTS = "agreements";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersion.categorySystemID</code> attribute defined at extension <code>catalog</code>. */
	public static final String CATEGORYSYSTEMID = "categorySystemID";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersion.categorySystemName</code> attribute defined at extension <code>catalog</code>. */
	public static final String CATEGORYSYSTEMNAME = "categorySystemName";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersion.categorySystemDescription</code> attribute defined at extension <code>catalog</code>. */
	public static final String CATEGORYSYSTEMDESCRIPTION = "categorySystemDescription";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersion.rootCategories</code> attribute defined at extension <code>catalog</code>. */
	public static final String ROOTCATEGORIES = "rootCategories";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersion.previousUpdateVersion</code> attribute defined at extension <code>catalog</code>. */
	public static final String PREVIOUSUPDATEVERSION = "previousUpdateVersion";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersion.catalog</code> attribute defined at extension <code>catalog</code>. */
	public static final String CATALOG = "catalog";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersion.writePrincipals</code> attribute defined at extension <code>catalog</code>. */
	public static final String WRITEPRINCIPALS = "writePrincipals";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersion.readPrincipals</code> attribute defined at extension <code>catalog</code>. */
	public static final String READPRINCIPALS = "readPrincipals";
	
	/** <i>Generated constant</i> - Attribute key of <code>CatalogVersion.mnemonic</code> attribute defined at extension <code>cockpit</code>. */
	public static final String MNEMONIC = "mnemonic";
	
	
	/** <i>Generated variable</i> - Variable of <code>CatalogVersion.synchronizations</code> attribute defined at extension <code>catalog</code>. */
	private List<SyncItemJobModel> _synchronizations;
	
	/** <i>Generated variable</i> - Variable of <code>CatalogVersion.incomingSynchronizations</code> attribute defined at extension <code>catalog</code>. */
	private List<SyncItemJobModel> _incomingSynchronizations;
	
	/** <i>Generated variable</i> - Variable of <code>CatalogVersion.active</code> attribute defined at extension <code>catalog</code>. */
	private Boolean _active;
	
	/** <i>Generated variable</i> - Variable of <code>CatalogVersion.version</code> attribute defined at extension <code>catalog</code>. */
	private String _version;
	
	/** <i>Generated variable</i> - Variable of <code>CatalogVersion.mimeRootDirectory</code> attribute defined at extension <code>catalog</code>. */
	private String _mimeRootDirectory;
	
	/** <i>Generated variable</i> - Variable of <code>CatalogVersion.generationDate</code> attribute defined at extension <code>catalog</code>. */
	private Date _generationDate;
	
	/** <i>Generated variable</i> - Variable of <code>CatalogVersion.defaultCurrency</code> attribute defined at extension <code>catalog</code>. */
	private CurrencyModel _defaultCurrency;
	
	/** <i>Generated variable</i> - Variable of <code>CatalogVersion.inclFreight</code> attribute defined at extension <code>catalog</code>. */
	private Boolean _inclFreight;
	
	/** <i>Generated variable</i> - Variable of <code>CatalogVersion.inclPacking</code> attribute defined at extension <code>catalog</code>. */
	private Boolean _inclPacking;
	
	/** <i>Generated variable</i> - Variable of <code>CatalogVersion.inclAssurance</code> attribute defined at extension <code>catalog</code>. */
	private Boolean _inclAssurance;
	
	/** <i>Generated variable</i> - Variable of <code>CatalogVersion.inclDuty</code> attribute defined at extension <code>catalog</code>. */
	private Boolean _inclDuty;
	
	/** <i>Generated variable</i> - Variable of <code>CatalogVersion.territories</code> attribute defined at extension <code>catalog</code>. */
	private Collection<CountryModel> _territories;
	
	/** <i>Generated variable</i> - Variable of <code>CatalogVersion.languages</code> attribute defined at extension <code>catalog</code>. */
	private Collection<LanguageModel> _languages;
	
	/** <i>Generated variable</i> - Variable of <code>CatalogVersion.generatorInfo</code> attribute defined at extension <code>catalog</code>. */
	private String _generatorInfo;
	
	/** <i>Generated variable</i> - Variable of <code>CatalogVersion.agreements</code> attribute defined at extension <code>catalog</code>. */
	private Collection<AgreementModel> _agreements;
	
	/** <i>Generated variable</i> - Variable of <code>CatalogVersion.categorySystemID</code> attribute defined at extension <code>catalog</code>. */
	private String _categorySystemID;
	
	/** <i>Generated variable</i> - Variable of <code>CatalogVersion.rootCategories</code> attribute defined at extension <code>catalog</code>. */
	private List<CategoryModel> _rootCategories;
	
	/** <i>Generated variable</i> - Variable of <code>CatalogVersion.previousUpdateVersion</code> attribute defined at extension <code>catalog</code>. */
	private Integer _previousUpdateVersion;
	
	/** <i>Generated variable</i> - Variable of <code>CatalogVersion.catalog</code> attribute defined at extension <code>catalog</code>. */
	private CatalogModel _catalog;
	
	/** <i>Generated variable</i> - Variable of <code>CatalogVersion.writePrincipals</code> attribute defined at extension <code>catalog</code>. */
	private List<PrincipalModel> _writePrincipals;
	
	/** <i>Generated variable</i> - Variable of <code>CatalogVersion.readPrincipals</code> attribute defined at extension <code>catalog</code>. */
	private List<PrincipalModel> _readPrincipals;
	
	/** <i>Generated variable</i> - Variable of <code>CatalogVersion.mnemonic</code> attribute defined at extension <code>cockpit</code>. */
	private String _mnemonic;
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CatalogVersionModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CatalogVersionModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _catalog initial attribute declared by type <code>CatalogVersion</code> at extension <code>catalog</code>
	 * @param _version initial attribute declared by type <code>CatalogVersion</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public CatalogVersionModel(final CatalogModel _catalog, final String _version)
	{
		super();
		setCatalog(_catalog);
		setVersion(_version);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _catalog initial attribute declared by type <code>CatalogVersion</code> at extension <code>catalog</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _version initial attribute declared by type <code>CatalogVersion</code> at extension <code>catalog</code>
	 */
	@Deprecated
	public CatalogVersionModel(final CatalogModel _catalog, final ItemModel _owner, final String _version)
	{
		super();
		setCatalog(_catalog);
		setOwner(_owner);
		setVersion(_version);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersion.active</code> attribute defined at extension <code>catalog</code>. 
	 * @return the active - active flag
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
	 * <i>Generated method</i> - Getter of the <code>CatalogVersion.agreements</code> attribute defined at extension <code>catalog</code>. 
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
	 * <i>Generated method</i> - Getter of the <code>CatalogVersion.catalog</code> attribute defined at extension <code>catalog</code>. 
	 * @return the catalog
	 */
	@Accessor(qualifier = "catalog", type = Accessor.Type.GETTER)
	public CatalogModel getCatalog()
	{
		if (this._catalog!=null)
		{
			return _catalog;
		}
		return _catalog = getPersistenceContext().getValue(CATALOG, _catalog);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersion.categorySystemDescription</code> attribute defined at extension <code>catalog</code>. 
	 * @return the categorySystemDescription
	 */
	@Accessor(qualifier = "categorySystemDescription", type = Accessor.Type.GETTER)
	public String getCategorySystemDescription()
	{
		return getCategorySystemDescription(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersion.categorySystemDescription</code> attribute defined at extension <code>catalog</code>. 
	 * @param loc the value localization key 
	 * @return the categorySystemDescription
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "categorySystemDescription", type = Accessor.Type.GETTER)
	public String getCategorySystemDescription(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(CATEGORYSYSTEMDESCRIPTION, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersion.categorySystemID</code> attribute defined at extension <code>catalog</code>. 
	 * @return the categorySystemID
	 */
	@Accessor(qualifier = "categorySystemID", type = Accessor.Type.GETTER)
	public String getCategorySystemID()
	{
		if (this._categorySystemID!=null)
		{
			return _categorySystemID;
		}
		return _categorySystemID = getPersistenceContext().getValue(CATEGORYSYSTEMID, _categorySystemID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersion.categorySystemName</code> attribute defined at extension <code>catalog</code>. 
	 * @return the categorySystemName
	 */
	@Accessor(qualifier = "categorySystemName", type = Accessor.Type.GETTER)
	public String getCategorySystemName()
	{
		return getCategorySystemName(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersion.categorySystemName</code> attribute defined at extension <code>catalog</code>. 
	 * @param loc the value localization key 
	 * @return the categorySystemName
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "categorySystemName", type = Accessor.Type.GETTER)
	public String getCategorySystemName(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(CATEGORYSYSTEMNAME, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersion.defaultCurrency</code> attribute defined at extension <code>catalog</code>. 
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
	 * <i>Generated method</i> - Getter of the <code>CatalogVersion.generationDate</code> attribute defined at extension <code>catalog</code>. 
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
	 * <i>Generated method</i> - Getter of the <code>CatalogVersion.generatorInfo</code> attribute defined at extension <code>catalog</code>. 
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
	 * <i>Generated method</i> - Getter of the <code>CatalogVersion.inclAssurance</code> attribute defined at extension <code>catalog</code>. 
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
	 * <i>Generated method</i> - Getter of the <code>CatalogVersion.inclDuty</code> attribute defined at extension <code>catalog</code>. 
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
	 * <i>Generated method</i> - Getter of the <code>CatalogVersion.inclFreight</code> attribute defined at extension <code>catalog</code>. 
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
	 * <i>Generated method</i> - Getter of the <code>CatalogVersion.inclPacking</code> attribute defined at extension <code>catalog</code>. 
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
	 * <i>Generated method</i> - Getter of the <code>CatalogVersion.incomingSynchronizations</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the incomingSynchronizations
	 */
	@Accessor(qualifier = "incomingSynchronizations", type = Accessor.Type.GETTER)
	public List<SyncItemJobModel> getIncomingSynchronizations()
	{
		if (this._incomingSynchronizations!=null)
		{
			return _incomingSynchronizations;
		}
		return _incomingSynchronizations = getPersistenceContext().getValue(INCOMINGSYNCHRONIZATIONS, _incomingSynchronizations);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersion.languages</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the languages - languages
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
	 * <i>Generated method</i> - Getter of the <code>CatalogVersion.mimeRootDirectory</code> attribute defined at extension <code>catalog</code>. 
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
	 * <i>Generated method</i> - Getter of the <code>CatalogVersion.mnemonic</code> attribute defined at extension <code>cockpit</code>. 
	 * @return the mnemonic
	 */
	@Accessor(qualifier = "mnemonic", type = Accessor.Type.GETTER)
	public String getMnemonic()
	{
		if (this._mnemonic!=null)
		{
			return _mnemonic;
		}
		return _mnemonic = getPersistenceContext().getValue(MNEMONIC, _mnemonic);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersion.previousUpdateVersion</code> attribute defined at extension <code>catalog</code>. 
	 * @return the previousUpdateVersion
	 */
	@Accessor(qualifier = "previousUpdateVersion", type = Accessor.Type.GETTER)
	public Integer getPreviousUpdateVersion()
	{
		if (this._previousUpdateVersion!=null)
		{
			return _previousUpdateVersion;
		}
		return _previousUpdateVersion = getPersistenceContext().getValue(PREVIOUSUPDATEVERSION, _previousUpdateVersion);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersion.readPrincipals</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the readPrincipals
	 */
	@Accessor(qualifier = "readPrincipals", type = Accessor.Type.GETTER)
	public List<PrincipalModel> getReadPrincipals()
	{
		if (this._readPrincipals!=null)
		{
			return _readPrincipals;
		}
		return _readPrincipals = getPersistenceContext().getValue(READPRINCIPALS, _readPrincipals);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersion.rootCategories</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the rootCategories
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
	 * <i>Generated method</i> - Getter of the <code>CatalogVersion.synchronizations</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the synchronizations
	 */
	@Accessor(qualifier = "synchronizations", type = Accessor.Type.GETTER)
	public List<SyncItemJobModel> getSynchronizations()
	{
		if (this._synchronizations!=null)
		{
			return _synchronizations;
		}
		return _synchronizations = getPersistenceContext().getValue(SYNCHRONIZATIONS, _synchronizations);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CatalogVersion.territories</code> attribute defined at extension <code>catalog</code>. 
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
	 * <i>Generated method</i> - Getter of the <code>CatalogVersion.version</code> attribute defined at extension <code>catalog</code>. 
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
	 * <i>Generated method</i> - Getter of the <code>CatalogVersion.writePrincipals</code> attribute defined at extension <code>catalog</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the writePrincipals
	 */
	@Accessor(qualifier = "writePrincipals", type = Accessor.Type.GETTER)
	public List<PrincipalModel> getWritePrincipals()
	{
		if (this._writePrincipals!=null)
		{
			return _writePrincipals;
		}
		return _writePrincipals = getPersistenceContext().getValue(WRITEPRINCIPALS, _writePrincipals);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersion.active</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the active - active flag
	 */
	@Accessor(qualifier = "active", type = Accessor.Type.SETTER)
	public void setActive(final Boolean value)
	{
		_active = getPersistenceContext().setValue(ACTIVE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersion.agreements</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the agreements - Agreement Collection
	 */
	@Accessor(qualifier = "agreements", type = Accessor.Type.SETTER)
	public void setAgreements(final Collection<AgreementModel> value)
	{
		_agreements = getPersistenceContext().setValue(AGREEMENTS, value);
	}
	
	/**
	 * <i>Generated method</i> - Initial setter of <code>CatalogVersion.catalog</code> attribute defined at extension <code>catalog</code>. Can only be used at creation of model - before first save.  
	 *  
	 * @param value the catalog
	 */
	@Accessor(qualifier = "catalog", type = Accessor.Type.SETTER)
	public void setCatalog(final CatalogModel value)
	{
		_catalog = getPersistenceContext().setValue(CATALOG, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersion.categorySystemDescription</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the categorySystemDescription
	 */
	@Accessor(qualifier = "categorySystemDescription", type = Accessor.Type.SETTER)
	public void setCategorySystemDescription(final String value)
	{
		setCategorySystemDescription(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersion.categorySystemDescription</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the categorySystemDescription
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "categorySystemDescription", type = Accessor.Type.SETTER)
	public void setCategorySystemDescription(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(CATEGORYSYSTEMDESCRIPTION, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersion.categorySystemID</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the categorySystemID
	 */
	@Accessor(qualifier = "categorySystemID", type = Accessor.Type.SETTER)
	public void setCategorySystemID(final String value)
	{
		_categorySystemID = getPersistenceContext().setValue(CATEGORYSYSTEMID, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersion.categorySystemName</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the categorySystemName
	 */
	@Accessor(qualifier = "categorySystemName", type = Accessor.Type.SETTER)
	public void setCategorySystemName(final String value)
	{
		setCategorySystemName(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersion.categorySystemName</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the categorySystemName
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "categorySystemName", type = Accessor.Type.SETTER)
	public void setCategorySystemName(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(CATEGORYSYSTEMNAME, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersion.defaultCurrency</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the defaultCurrency - Default Currency
	 */
	@Accessor(qualifier = "defaultCurrency", type = Accessor.Type.SETTER)
	public void setDefaultCurrency(final CurrencyModel value)
	{
		_defaultCurrency = getPersistenceContext().setValue(DEFAULTCURRENCY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersion.generationDate</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the generationDate - Generation Date
	 */
	@Accessor(qualifier = "generationDate", type = Accessor.Type.SETTER)
	public void setGenerationDate(final Date value)
	{
		_generationDate = getPersistenceContext().setValue(GENERATIONDATE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersion.generatorInfo</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the generatorInfo - Generator Info
	 */
	@Accessor(qualifier = "generatorInfo", type = Accessor.Type.SETTER)
	public void setGeneratorInfo(final String value)
	{
		_generatorInfo = getPersistenceContext().setValue(GENERATORINFO, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersion.inclAssurance</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the inclAssurance - incl.Assurance
	 */
	@Accessor(qualifier = "inclAssurance", type = Accessor.Type.SETTER)
	public void setInclAssurance(final Boolean value)
	{
		_inclAssurance = getPersistenceContext().setValue(INCLASSURANCE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersion.inclDuty</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the inclDuty - incl.Duty
	 */
	@Accessor(qualifier = "inclDuty", type = Accessor.Type.SETTER)
	public void setInclDuty(final Boolean value)
	{
		_inclDuty = getPersistenceContext().setValue(INCLDUTY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersion.inclFreight</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the inclFreight - incl.Freight
	 */
	@Accessor(qualifier = "inclFreight", type = Accessor.Type.SETTER)
	public void setInclFreight(final Boolean value)
	{
		_inclFreight = getPersistenceContext().setValue(INCLFREIGHT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersion.inclPacking</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the inclPacking - incl.Packing
	 */
	@Accessor(qualifier = "inclPacking", type = Accessor.Type.SETTER)
	public void setInclPacking(final Boolean value)
	{
		_inclPacking = getPersistenceContext().setValue(INCLPACKING, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersion.languages</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the languages - languages
	 */
	@Accessor(qualifier = "languages", type = Accessor.Type.SETTER)
	public void setLanguages(final Collection<LanguageModel> value)
	{
		_languages = getPersistenceContext().setValue(LANGUAGES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersion.mimeRootDirectory</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the mimeRootDirectory - Mime Root Directory
	 */
	@Accessor(qualifier = "mimeRootDirectory", type = Accessor.Type.SETTER)
	public void setMimeRootDirectory(final String value)
	{
		_mimeRootDirectory = getPersistenceContext().setValue(MIMEROOTDIRECTORY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersion.mnemonic</code> attribute defined at extension <code>cockpit</code>. 
	 *  
	 * @param value the mnemonic
	 */
	@Accessor(qualifier = "mnemonic", type = Accessor.Type.SETTER)
	public void setMnemonic(final String value)
	{
		_mnemonic = getPersistenceContext().setValue(MNEMONIC, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersion.previousUpdateVersion</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the previousUpdateVersion
	 */
	@Accessor(qualifier = "previousUpdateVersion", type = Accessor.Type.SETTER)
	public void setPreviousUpdateVersion(final Integer value)
	{
		_previousUpdateVersion = getPersistenceContext().setValue(PREVIOUSUPDATEVERSION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersion.readPrincipals</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the readPrincipals
	 */
	@Accessor(qualifier = "readPrincipals", type = Accessor.Type.SETTER)
	public void setReadPrincipals(final List<PrincipalModel> value)
	{
		_readPrincipals = getPersistenceContext().setValue(READPRINCIPALS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersion.rootCategories</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the rootCategories
	 */
	@Accessor(qualifier = "rootCategories", type = Accessor.Type.SETTER)
	public void setRootCategories(final List<CategoryModel> value)
	{
		_rootCategories = getPersistenceContext().setValue(ROOTCATEGORIES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersion.synchronizations</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the synchronizations
	 */
	@Accessor(qualifier = "synchronizations", type = Accessor.Type.SETTER)
	public void setSynchronizations(final List<SyncItemJobModel> value)
	{
		_synchronizations = getPersistenceContext().setValue(SYNCHRONIZATIONS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersion.territories</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the territories - Territory
	 */
	@Accessor(qualifier = "territories", type = Accessor.Type.SETTER)
	public void setTerritories(final Collection<CountryModel> value)
	{
		_territories = getPersistenceContext().setValue(TERRITORIES, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersion.version</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the version - version
	 */
	@Accessor(qualifier = "version", type = Accessor.Type.SETTER)
	public void setVersion(final String value)
	{
		_version = getPersistenceContext().setValue(VERSION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CatalogVersion.writePrincipals</code> attribute defined at extension <code>catalog</code>. 
	 *  
	 * @param value the writePrincipals
	 */
	@Accessor(qualifier = "writePrincipals", type = Accessor.Type.SETTER)
	public void setWritePrincipals(final List<PrincipalModel> value)
	{
		_writePrincipals = getPersistenceContext().setValue(WRITEPRINCIPALS, value);
	}
	
}

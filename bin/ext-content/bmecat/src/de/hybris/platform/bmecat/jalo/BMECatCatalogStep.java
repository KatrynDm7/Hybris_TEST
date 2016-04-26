/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 * 
 *  
 */
package de.hybris.platform.bmecat.jalo;

import de.hybris.bootstrap.xml.AbstractValueObject;
import de.hybris.bootstrap.xml.ParseAbortException;
import de.hybris.bootstrap.xml.ParseFinishedException;
import de.hybris.platform.bmecat.constants.BMECatConstants;
import de.hybris.platform.bmecat.parser.Abort;
import de.hybris.platform.bmecat.parser.BMECatParser;
import de.hybris.platform.catalog.constants.CatalogConstants;
import de.hybris.platform.catalog.jalo.Agreement;
import de.hybris.platform.catalog.jalo.Catalog;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.catalog.jalo.Company;
import de.hybris.platform.cronjob.jalo.ChangeDescriptor;
import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Country;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.c2l.Region;
import de.hybris.platform.jalo.enumeration.EnumerationType;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloAbstractTypeException;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.user.Address;
import de.hybris.platform.util.Utilities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;


/**
 * This step import basic catalog tags. It creates the catalog, catalog version, buyer, supplier and agreement.
 * 
 * 
 */
public class BMECatCatalogStep extends GeneratedBMECatCatalogStep
{
	protected EnumerationValue CATALOG_ID;
	protected EnumerationValue CATALOG_NAME;
	protected EnumerationValue CATALOG_SUPPLIER;

	protected EnumerationValue CATALOGVERSION_AGREEMENT;
	protected EnumerationValue CATALOG_BUYER;
	protected EnumerationValue CATALOGVERSION_DEFAULTCURRENCY;
	protected EnumerationValue CATALOGVERSION_GENERATIONDATE;
	protected EnumerationValue CATALOGVERSION_GENERATORINFO;
	protected EnumerationValue CATALOGVERSION_INCLASSURANCE;
	protected EnumerationValue CATALOGVERSION_INCLDUTY;
	protected EnumerationValue CATALOGVERSION_INCLFREIGHT;
	protected EnumerationValue CATALOGVERSION_INCLPACKING;
	protected EnumerationValue CATALOGVERSION_LANGUAGE;
	protected EnumerationValue CATALOGVERSION_MIMEROOTDIRECTORY;
	protected EnumerationValue CATALOGVERSION_TERRITORIES;
	protected EnumerationValue CATALOGVERSION_FEATURESYSTEM;
	protected EnumerationValue CATALOGVERSION_VERSION;
	protected EnumerationValue CATALOGVERSION_CATALOG;

	protected EnumerationValue COMPANY_NAME;
	protected EnumerationValue COMPANY_ID;
	protected EnumerationValue COMPANY_BUYERSPECIFICID;
	protected EnumerationValue COMPANY_SUPPLIERSPECIFICID;
	protected EnumerationValue COMPANY_ILNID;
	protected EnumerationValue COMPANY_DUNSID;
	protected EnumerationValue COMPANY_ADDRESS;

	protected EnumerationValue ADDRESS_NAME;
	protected EnumerationValue ADDRESS_NAME2;
	protected EnumerationValue ADDRESS_NAME3;
	protected EnumerationValue ADDRESS_CONTACT;
	protected EnumerationValue ADDRESS_STREET;
	protected EnumerationValue ADDRESS_ZIP;
	protected EnumerationValue ADDRESS_BOXNO;
	protected EnumerationValue ADDRESS_ZIPBOX;
	protected EnumerationValue ADDRESS_CITY;
	protected EnumerationValue ADDRESS_STATE;
	protected EnumerationValue ADDRESS_COUNTRY;
	protected EnumerationValue ADDRESS_PHONE;
	protected EnumerationValue ADDRESS_FAX;
	protected EnumerationValue ADDRESS_EMAIL;
	protected EnumerationValue ADDRESS_PUBLICKEY;
	protected EnumerationValue ADDRESS_URL;
	protected EnumerationValue ADDRESS_REMARKS;

	protected EnumerationValue AGREEMENT_ID;
	protected EnumerationValue AGREEMENT_STARTDATE;
	protected EnumerationValue AGREEMENT_ENDDATE;
	protected EnumerationValue AGREEMENT_CATALOGVERSION;

	//********************************************************************************
	// Item logic
	//********************************************************************************

	/**
	 * @see Item#remove(SessionContext)
	 */
	@Override
	public void remove(final SessionContext ctx) throws ConsistencyCheckException
	{
		super.remove(ctx);
	}

	/**
	 * Implements default setting of category type and the attribute mapping if these attributes are not specified during
	 * step creation.
	 * 
	 * @param ctx
	 * @param item
	 * @param nonInitialAttributes
	 * @throws JaloBusinessException
	 */
	@Override
	public void setNonInitialAttributes(final SessionContext ctx, final Item item, final ItemAttributeMap nonInitialAttributes)
			throws JaloBusinessException
	{
		final BMECatCatalogStep newStep = (BMECatCatalogStep) item;

		newStep.initializeAttributeEnumValues();
		final ItemAttributeMap myMap = new ItemAttributeMap(nonInitialAttributes);
		ComposedType catalogType = (ComposedType) nonInitialAttributes.get(BMECatCatalogStep.CATALOGTYPE);
		Map catalogMapping = (Map) nonInitialAttributes.get(BMECatCatalogStep.CATALOGATTRIBUTEMAPPING);
		if (catalogType == null)
		{
			if (catalogMapping != null && !catalogMapping.isEmpty())
			{
				throw new JaloInvalidParameterException(
						"Cannot define catalog attribute mapping without specifying the catalog type", 0);
			}
			else
			{
				catalogType = getSession().getTypeManager().getComposedType(Catalog.class);
				myMap.put(BMECatCatalogStep.CATALOGTYPE, catalogType);
			}
		}
		if (catalogMapping == null || catalogMapping.isEmpty())
		{
			catalogMapping = newStep.createDefaultCatalogMapping(catalogType);
			myMap.put(BMECatCatalogStep.CATALOGATTRIBUTEMAPPING, catalogMapping);
		}
		MappingUtils.checkAttributeMapping(Catalog.class, catalogType, catalogMapping);

		ComposedType catalogVersionType = (ComposedType) nonInitialAttributes.get(BMECatCatalogStep.CATALOGVERSIONTYPE);
		Map catalogVersionMapping = (Map) nonInitialAttributes.get(BMECatCatalogStep.CATALOGVERSIONATTRIBUTEMAPPING);
		if (catalogVersionType == null)
		{
			if (catalogVersionMapping != null && !catalogVersionMapping.isEmpty())
			{
				throw new JaloInvalidParameterException(
						"Cannot define catalog version attribute mapping without specifying the catalog version type", 0);
			}
			else
			{
				catalogVersionType = getSession().getTypeManager().getComposedType(CatalogVersion.class);
				myMap.put(BMECatCatalogStep.CATALOGVERSIONTYPE, catalogVersionType);
			}
		}
		if (catalogVersionMapping == null || catalogVersionMapping.isEmpty())
		{
			catalogVersionMapping = newStep.createDefaultCatalogVersionMapping(catalogVersionType);
			myMap.put(BMECatCatalogStep.CATALOGVERSIONATTRIBUTEMAPPING, catalogVersionMapping);
		}
		MappingUtils.checkAttributeMapping(CatalogVersion.class, catalogVersionType, catalogVersionMapping);

		ComposedType buyerType = (ComposedType) nonInitialAttributes.get(BMECatCatalogStep.BUYERTYPE);
		Map buyerMapping = (Map) nonInitialAttributes.get(BMECatCatalogStep.BUYERATTRIBUTEMAPPING);
		if (buyerType == null)
		{
			if (buyerMapping != null && !buyerMapping.isEmpty())
			{
				throw new JaloInvalidParameterException("Cannot define buyer attribute mapping without specifying the buyer type", 0);
			}
			else
			{
				buyerType = getSession().getTypeManager().getComposedType(Company.class);
				myMap.put(BMECatCatalogStep.BUYERTYPE, buyerType);
			}
		}
		if (buyerMapping == null || buyerMapping.isEmpty())
		{
			buyerMapping = newStep.createDefaultCompanyMapping(buyerType);
			myMap.put(BMECatCatalogStep.BUYERATTRIBUTEMAPPING, buyerMapping);
		}
		MappingUtils.checkAttributeMapping(Company.class, buyerType, buyerMapping);
		ComposedType supplierType = (ComposedType) nonInitialAttributes.get(BMECatCatalogStep.SUPPLIERTYPE);
		Map supplierMapping = (Map) nonInitialAttributes.get(BMECatCatalogStep.SUPPLIERATTRIBUTEMAPPING);
		if (supplierType == null)
		{
			supplierType = getSession().getTypeManager().getComposedType(Company.class);
			myMap.put(BMECatCatalogStep.SUPPLIERTYPE, supplierType);
		}
		if (supplierMapping == null || supplierMapping.isEmpty())
		{
			supplierMapping = newStep.createDefaultCompanyMapping(supplierType);
			myMap.put(BMECatCatalogStep.SUPPLIERATTRIBUTEMAPPING, supplierMapping);
		}
		MappingUtils.checkAttributeMapping(Company.class, supplierType, supplierMapping);

		ComposedType agreementType = (ComposedType) nonInitialAttributes.get(BMECatCatalogStep.AGREEMENTTYPE);
		Map agreementMapping = (Map) nonInitialAttributes.get(BMECatCatalogStep.AGREEMENTATTRIBUTEMAPPING);
		if (agreementType == null)
		{
			if (agreementMapping != null && !agreementMapping.isEmpty())
			{
				throw new JaloInvalidParameterException(
						"Cannot define agreement attribute mapping without specifying the agreement type", 0);
			}
			else
			{
				agreementType = getSession().getTypeManager().getComposedType(Agreement.class);
				myMap.put(BMECatCatalogStep.AGREEMENTTYPE, agreementType);
			}
		}
		if (agreementMapping == null || agreementMapping.isEmpty())
		{
			agreementMapping = newStep.createDefaultAgreementMapping(agreementType);
			myMap.put(BMECatCatalogStep.AGREEMENTATTRIBUTEMAPPING, agreementMapping);
		}
		MappingUtils.checkAttributeMapping(Agreement.class, agreementType, agreementMapping);

		ComposedType addressType = (ComposedType) nonInitialAttributes.get(BMECatCatalogStep.ADDRESSTYPE);
		Map addressMapping = (Map) nonInitialAttributes.get(BMECatCatalogStep.ADDRESSATTRIBUTEMAPPING);
		if (addressType == null)
		{
			if (addressMapping != null && !addressMapping.isEmpty())
			{
				throw new JaloInvalidParameterException(
						"Cannot define address attribute mapping without specifying the address type", 0);
			}
			else
			{
				addressType = getSession().getTypeManager().getComposedType(Address.class);
				myMap.put(BMECatCatalogStep.ADDRESSTYPE, addressType);
			}
		}
		if (addressMapping == null || addressMapping.isEmpty())
		{
			addressMapping = newStep.createDefaultAddressMapping(addressType);
			myMap.put(BMECatCatalogStep.ADDRESSATTRIBUTEMAPPING, addressMapping);
		}
		MappingUtils.checkAttributeMapping(Address.class, addressType, addressMapping);

		super.setNonInitialAttributes(ctx, item, myMap);
	}

	//********************************************************************************
	// Import logic
	//********************************************************************************

	/**
	 * Declares that this step can be undone.
	 * 
	 * @return true since catalog step is able to undo its changes.
	 * @see de.hybris.platform.cronjob.jalo.Step#canUndo(de.hybris.platform.cronjob.jalo.CronJob)
	 */
	@Override
	protected boolean canUndo(final CronJob forSchedule)
	{
		return true;
	}

	/**
	 * Removes created catalog version and catalog.
	 * 
	 * @see de.hybris.platform.cronjob.jalo.Step#undoStep(de.hybris.platform.cronjob.jalo.CronJob)
	 */
	@Override
	protected void undoStep(final CronJob forSchedule)
	{
		super.undoStep(forSchedule);
		//
		final Collection changes = getChanges(forSchedule);
		if (changes == null)
		{
			return;
		}
		for (final Iterator it = changes.iterator(); it.hasNext();)
		{
			final ChangeDescriptor changeDescriptor = (ChangeDescriptor) it.next();
			final String type = changeDescriptor.getChangeType();
			/*
			 * remove catalog version
			 */
			if (BMECatConstants.ChangeTypes.CREATE_CATALOG_VERSION.equalsIgnoreCase(type))
			{
				final CatalogVersion ver = (CatalogVersion) changeDescriptor.getChangedItem();
				try
				{
					if (ver != null && ver.isAlive())
					{
						if (isDebugEnabled())
						{
							debug("undo cronjob >" + forSchedule.getCode() + "< removing catalog version " + ver.getVersion());
						}
						ver.remove();
					}
				}
				catch (final ConsistencyCheckException e)
				{
					throw new JaloSystemException(e);
				}
			}
			/*
			 * remove catalog
			 */
			else if (BMECatConstants.ChangeTypes.CREATE_CATALOG.equalsIgnoreCase(type))
			{
				final Catalog cat = (Catalog) changeDescriptor.getChangedItem();
				try
				{
					if (cat != null && cat.isAlive())
					{
						if (isInfoEnabled())
						{
							info("removing catalog " + cat);
						}
						cat.remove();
					}
				}
				catch (final ConsistencyCheckException e)
				{
					throw new JaloSystemException(e);
				}
			}
			/*
			 * remove agreements
			 */
			else if (BMECatConstants.ChangeTypes.CREATE_AGREEMENT.equalsIgnoreCase(type))
			{
				final Agreement agreement = (Agreement) changeDescriptor.getChangedItem();
				try
				{
					if (agreement != null && agreement.isAlive())
					{
						if (isInfoEnabled())
						{
							info("removing agreement " + agreement);
						}
						agreement.remove();
					}
				}
				catch (final ConsistencyCheckException e)
				{
					throw new JaloSystemException(e);
				}
			}
			/*
			 * remove company
			 */
			else if (BMECatConstants.ChangeTypes.CREATE_COMPANY.equalsIgnoreCase(type))
			{
				final Company comp = (Company) changeDescriptor.getChangedItem();
				try
				{
					if (comp != null && comp.isAlive())
					{
						if (isInfoEnabled())
						{
							info("removing company " + comp);
						}
						comp.remove();
					}
				}
				catch (final ConsistencyCheckException e)
				{
					throw new JaloSystemException(e);
				}
			}
			/*
			 * remove address
			 */
			else if (BMECatConstants.ChangeTypes.CREATE_ADDRESS.equalsIgnoreCase(type))
			{
				final Address adr = (Address) changeDescriptor.getChangedItem();
				try
				{
					if (adr != null && adr.isAlive())
					{
						if (isInfoEnabled())
						{
							info("removing address " + adr);
						}
						adr.remove();
					}
				}
				catch (final ConsistencyCheckException e)
				{
					throw new JaloSystemException(e);
				}
			}
			/*
			 * remove region
			 */
			else if (BMECatConstants.ChangeTypes.CREATE_REGION.equalsIgnoreCase(type))
			{
				final Region reg = (Region) changeDescriptor.getChangedItem();
				try
				{
					if (reg != null && reg.isAlive())
					{
						if (isInfoEnabled())
						{
							info("removing region " + reg);
						}
						reg.remove();
					}
				}
				catch (final ConsistencyCheckException e)
				{
					throw new JaloSystemException(e);
				}
			}
			try
			{
				// throw away change descriptor
				changeDescriptor.remove();
			}
			catch (final ConsistencyCheckException e)
			{
				throw new JaloSystemException(e);
			}
		}
	}

	/**
	 * Checks start constraints and creates the new catalog.
	 * 
	 * @param cat
	 * @param cronJob
	 * @see BMECatImportStep#initializeBMECatImport(de.hybris.platform.bmecat.parser.Catalog, BMECatImportCronJob)
	 */
	@Override
	protected void initializeBMECatImport(final de.hybris.platform.bmecat.parser.Catalog cat, final BMECatImportCronJob cronJob)
			throws ParseAbortException
	{
		initializeAttributeEnumValues();

		cronJob.setCataloValueObject(cat);

		if (isDebugEnabled())
		{
			debug("transaction mode>" + cat.getTransactionMode() + "<");
		}
		switch (cat.getTransactionMode())
		{
			case BMECatConstants.TRANSACTION.T_NEW_CATALOG:
			{
				checkConstraints(cat, cronJob);
				final Language language = cronJob.getOrCreateCatalogLanguage(cat.getLanguage());
				getSession().getSessionContext().setLanguage(language);
				importCatalog(cronJob, cat);
				break;
			}
			case BMECatConstants.TRANSACTION.T_UPDATE_PRODUCTS:
			{
				checkConstraints(cat, cronJob);
				final Language language = cronJob.getOrCreateCatalogLanguage(cat.getLanguage());
				this.getSession().getSessionContext().setLanguage(language);
				break;
			}
			case BMECatConstants.TRANSACTION.T_UPDATE_PRICES:
			{
				checkConstraints(cat, cronJob);
				break;
			}
		}
	}

	/**
	 * Throws a {@link ParseAbortException} in case of an (test-)abort otherwise does nothing.
	 * 
	 * @param catalog
	 * @param object
	 * @param cronJob
	 * @see BMECatImportStep#importBMECatObject(de.hybris.platform.bmecat.parser.Catalog, AbstractValueObject,
	 *      BMECatImportCronJob)
	 */
	@Override
	protected void importBMECatObject(final de.hybris.platform.bmecat.parser.Catalog catalog, final AbstractValueObject object,
			final BMECatImportCronJob cronJob) throws ParseAbortException
	{
		if (object instanceof Abort)
		{
			if (ABORTTYPE.equals(((Abort) object).getType()))
			{
				throw new BMECatParser.TestParseAbortException("");
			}
		}
		else
		{
			throw new ParseFinishedException("CatalogStep: done.");
		}
	}

	private final static String ABORTTYPE = "catalog";

	/**
	 * Checks which catalog objects exists and which information has already been set to the CronJob by the infoStep (if
	 * exists). Sets information to the cronJob
	 * 
	 * @param catalogValueObject
	 *           value object for catalog tag
	 * @param cronJob
	 *           context in which the step runs
	 */
	public void checkConstraints(final de.hybris.platform.bmecat.parser.Catalog catalogValueObject,
			final BMECatImportCronJob cronJob)
	{
		final boolean infoAvailable = cronJob.isCatalogInfoAvailableAsPrimitive();
		final int transactionMode = catalogValueObject.getTransactionMode();
		if (isDebugEnabled())
		{
			debug("Catalog step checkConstraints infoAvailable: " + Boolean.toString(infoAvailable));
		}
		if (!infoAvailable)
		{
			cronJob.setTransactionMode(getBMECatManager().getTransactionModeEnum(transactionMode));
		}
		switch (transactionMode)
		{
			case BMECatConstants.TRANSACTION.T_NEW_CATALOG:
			{
				// get supplier
				final Company supplier = null; //infoAvailable ? cronJob.getSupplier() : getCatalogManager().getCompanyByUID(catalogValueObject.getSupplier().getName());
				//if( !infoAvailable ) cronJob.setSupplier( supplier );
				// try to get preset catalog in case info is available
				final de.hybris.platform.catalog.jalo.Catalog catalog = infoAvailable ? cronJob.getCatalog() // we assume that the info step already tried to find the correct catalog
						: getCatalogManager().getCatalog(supplier, catalogValueObject.getID()); //( supplier != null ? getCatalogManager().getCatalog(supplier, catalogValueObject.getId()) : null );

				if (catalog == null)
				{
					//catalog does not exsit. import it!
					if (isInfoEnabled())
					{
						info("T_NEW_CATALOG Transaction valid. Importing new Catalog with ID '" + catalogValueObject.getID() + "'.");
					}
					if (!infoAvailable)
					{
						cronJob.setLocalizationUpdate(false);
					}
				}
				else
				{
					final CatalogVersion catalogVersion = infoAvailable ? cronJob.getCatalogVersion() : catalog
							.getCatalogVersion(catalogValueObject.getVersion());
					if (catalogVersion == null)
					{
						//new version. import it!
						if (isInfoEnabled())
						{
							info("T_NEW_CATALOG Transaction valid. Importing new version (" + catalogValueObject.getVersion()
									+ ") of Catalog with ID '" + catalogValueObject.getID() + "'.");
						}
						if (!infoAvailable)
						{
							cronJob.setLocalizationUpdate(false);
						}
					}
					else
					{
						if (!catalogVersion.isImportedLanguage(catalogValueObject.getLanguage()))
						{
							//new language. import it!
							if (isInfoEnabled())
							{
								info("T_NEW_CATALOG Transaction valid. Importing language (" + catalogValueObject.getLanguage()
										+ ") dependend data for Catalog with ID '" + catalogValueObject.getID() + "' and version '"
										+ catalogValueObject.getVersion() + "'.");
							}
							if (!infoAvailable)
							{
								cronJob.setLocalizationUpdate(true);
							}
						}
					}
				}
				break;
			}
			case BMECatConstants.TRANSACTION.T_UPDATE_PRODUCTS:
			case BMECatConstants.TRANSACTION.T_UPDATE_PRICES:
			{
				final Company supplier = null; //infoAvailable ? cronJob.getSupplier() : getCatalogManager().getCompanyByUID(catalogValueObject.getSupplier().getName());
				/*
				 * if(supplier==null) { throw new JaloSystemException(null, "no supplier", 0); }
				 */
				// catalog
				final Catalog catalog = infoAvailable ? cronJob.getCatalog() : getCatalogManager().getCatalog(supplier,
						catalogValueObject.getID());
				if (catalog == null)
				{
					throw new JaloSystemException(null, "no catalog", 0);
				}
				// catalog version
				final CatalogVersion catalogVersion = infoAvailable ? cronJob.getCatalogVersion() : catalog
						.getCatalogVersion(catalogValueObject.getVersion());
				if (catalogVersion == null)
				{
					throw new JaloSystemException(null, "no catalog version", 0);
				}

				//Integer prevVersion= catalogVersion.getPreviousUpdateVersion();
				final Map importedVersions = BMECatManager.getInstance().getImportedVersions(catalogVersion);
				final Integer prevVersion = (Integer) importedVersions.get(BMECatManager.getInstance().getImportedVersionsKey(
						catalogValueObject));

				// both versions must be null or equal
				// see BMECatSpec, page: 53
				if (prevVersion == null)
				{
					if (catalogValueObject.getPreviousVersion().intValue() != 0)
					{
						throw new JaloSystemException(null, "Update counters in xml prev_vers >"
								+ catalogValueObject.getPreviousVersion()
								+ "< does not fit to stored counter in catalog version >null<. prev_version in xml must be 0", 0);
					}
				}
				else
				{
					if (prevVersion.intValue() != catalogValueObject.getPreviousVersion().intValue() - 1)
					{
						throw new JaloSystemException(null, "Update counters in xml prev_vers >"
								+ catalogValueObject.getPreviousVersion() + "< does not fit to stored counter in catalog version >"
								+ prevVersion.intValue() + "<. prev_vers in xml must be one higher than stored counter.", 0);
					}
				}

				if (!infoAvailable)
				{
					//cronJob.setSupplier( supplier );
					cronJob.setCatalog(catalog);
					cronJob.setCatalogVersion(catalogVersion);
					cronJob.setPreviousUpdateVersion(catalogValueObject.getPreviousVersion());
				}
			}
		}
	}

	/**
	 * Imports catalog and catalog version
	 * 
	 * @param cronJob
	 * @param catalogValueObject
	 * @throws ParseAbortException
	 */
	protected void importCatalog(final BMECatImportCronJob cronJob,
			final de.hybris.platform.bmecat.parser.Catalog catalogValueObject) throws ParseAbortException
	{
		final boolean infoAvailable = cronJob.isCatalogInfoAvailableAsPrimitive();
		//CATALOG
		//is there an preset catalog?
		Catalog catalog = infoAvailable ? cronJob.getCatalog() : getCatalogManager().getCatalog(
				catalogValueObject.getSupplier().getName(), catalogValueObject.getID());
		/*
		 * a) create a new catalog is no catalog has been preset or found
		 */
		if (catalog == null)
		{
			if (isInfoEnabled())
			{
				info("Catalog with id '" + catalogValueObject.getID() + "' does not exsist. Creating new catalog!");
			}
			catalog = createCatalog(cronJob, getCatalogValues(cronJob, catalogValueObject, null));
			addChange(cronJob, BMECatConstants.ChangeTypes.CREATE_CATALOG, catalog, "Catalog with id '" + catalogValueObject.getID()
					+ "' does not exsist. Creating new catalog!");
			if (!infoAvailable)
			{
				cronJob.setCatalogID(catalog.getId());
			}
		}
		/*
		 * b) update catalog data
		 */
		else
		{
			if (isInfoEnabled())
			{
				info("Catalog with id '" + catalogValueObject.getID() + "' found - updating!");
			}
			//update values
			updateCatalog(cronJob, catalog, catalogValueObject);
		}
		cronJob.setCatalog(catalog);

		//CATALOG VERSION
		//
		CatalogVersion catalogVersion = infoAvailable ? cronJob.getCatalogVersion() : catalog.getCatalogVersion(catalogValueObject
				.getVersion());
		if (catalogVersion == null)
		{
			catalogVersion = createCatalogVersion(cronJob, getCatalogVersionValues(cronJob, catalogValueObject));
			addChange(cronJob, BMECatConstants.ChangeTypes.CREATE_CATALOG_VERSION, catalogVersion, null);
			if (!infoAvailable)
			{
				cronJob.setCatalogVersionName(catalogVersion != null ? catalogVersion.getVersion() : null);
			}
		}
		else
		{
			updateCatalogVersion(cronJob, catalogVersion, catalogValueObject);
		}
		cronJob.setCatalogVersion(catalogVersion);

		final HashMap importedVersions = new HashMap(BMECatManager.getInstance().getImportedVersions(catalogVersion));
		final String key = BMECatManager.getInstance().getImportedVersionsKey(catalogValueObject);

		//version already imported?
		if (!importedVersions.containsKey(key))
		{
			//add new entry
			importedVersions.put(key, null); // see BMECat Spec, pages 46, 50, 53
			BMECatManager.getInstance().setImportedVersions(catalogVersion, importedVersions);
		}

		// aborting parsing ...
		throw new ParseFinishedException("Catalog parsing done! (abort)");
	}

	/**
	 * @param cronJob
	 * @param catalogVersion
	 * @param catalogValueObject
	 */
	protected void updateCatalogVersion(final BMECatImportCronJob cronJob, final CatalogVersion catalogVersion,
			final de.hybris.platform.bmecat.parser.Catalog catalogValueObject)
	{
		Map values = Collections.EMPTY_MAP;
		try
		{
			values = getCatalogVersionValues(cronJob, catalogValueObject, catalogVersion);
			values.remove(CatalogVersion.CATALOG);
			catalogVersion.setAllAttributes(values);
		}
		catch (final Exception e)
		{
			if (isErrorEnabled())
			{
				error("Could not update catalog version attributes " + values.keySet() + " : " + e.getMessage());
			}
			throw new JaloSystemException(e);
		}
	}

	/**
	 * @param cronJob
	 * @param catalog
	 * @param catalogValueObject
	 */
	protected void updateCatalog(final BMECatImportCronJob cronJob, final Catalog catalog,
			final de.hybris.platform.bmecat.parser.Catalog catalogValueObject)
	{
		Map values = Collections.EMPTY_MAP;
		try
		{
			values = getCatalogValues(cronJob, catalogValueObject, catalog);
			catalog.setAllAttributes(values);
		}
		catch (final Exception e)
		{
			if (isErrorEnabled())
			{
				error("Could not update catalog attributes " + values.keySet() + " : " + e.getMessage());
			}
			throw new JaloSystemException(e);
		}
	}

	/**
	 * Returns an <code>ItemAttributeMap</code> containing all extracted values of the provided catalog value object. The
	 * keys of the map are the attribute qualifiers which are determined using the configured attribute mapping, the
	 * values are the actual attribute values.
	 * 
	 * @param cronJob
	 *           the currenct cronJob
	 * @param catalogValueObject
	 *           the catalog value object, the values will be extracted from
	 * @return the <code>ItemAttributeMap</code> containing all extracted values of the provided catalog value object
	 */
	protected ItemAttributeMap getCatalogVersionValues(final BMECatImportCronJob cronJob,
			final de.hybris.platform.bmecat.parser.Catalog catalogValueObject)
	{
		return getCatalogVersionValues(cronJob, catalogValueObject, null);
	}

	/**
	 * Get or creates the provided aggrements provided in the specified catalog value object.
	 * 
	 * @param cronJob
	 *           The current import cronJob.
	 * @param catalogValueObject
	 *           The catalog value object containing the actual data.
	 * @return A collection of {@link de.hybris.platform.catalog.jalo.Agreement}s according to the values of the catalog
	 *         value object.
	 */
	protected Collection getOrCreateAgreements(final BMECatImportCronJob cronJob,
			final de.hybris.platform.bmecat.parser.Catalog catalogValueObject)
	{
		if (catalogValueObject.getAgreements() == null)
		{
			return null;
		}
		if (cronJob.isImportAgreementAsPrimitive())
		{
			final Collection agreements = new ArrayList();
			for (final Iterator it = catalogValueObject.getAgreements().iterator(); it.hasNext();)
			{
				final de.hybris.platform.bmecat.parser.Agreement agreementValueObject = (de.hybris.platform.bmecat.parser.Agreement) it
						.next();

				final Agreement agreement = createAgreement(cronJob, getAgreementValues(cronJob, agreementValueObject));
				addChange(cronJob, BMECatConstants.ChangeTypes.CREATE_AGREEMENT, agreement, null);
				agreements.add(agreement);
			}
			return agreements;
		}
		else
		{
			if (isInfoEnabled())
			{
				info("skipping import of agreements (importAgreement is set to false in cronJob).");
			}
			return null;
		}
	}

	/**
	 * Creates agreement
	 * 
	 * @param values
	 *           paramter for agreement creation {@link de.hybris.platform.catalog.jalo.Agreement}
	 * @return created agreement
	 */
	public Agreement createAgreement(final BMECatImportCronJob cronJob, final ItemAttributeMap values)
	{
		try
		{
			return (Agreement) getAgreementType().newInstance(values);
		}
		catch (final Exception e)
		{
			if (isErrorEnabled())
			{
				error("Could create agreement ( values = " + values + " : " + e.getMessage() + " : "
						+ Utilities.getStackTraceAsString(e));
			}
			throw new JaloSystemException(e);
		}
	}

	/**
	 * Creates catalog
	 * 
	 * @param values
	 *           parameter for catalog creation {@link de.hybris.platform.catalog.jalo.Catalog}
	 * @return catalog
	 */
	protected de.hybris.platform.catalog.jalo.Catalog createCatalog(final BMECatImportCronJob cronJob,
			final ItemAttributeMap values)
	{
		try
		{
			return (Catalog) getCatalogType().newInstance(values);
		}
		catch (final JaloGenericCreationException e)
		{
			if (isErrorEnabled())
			{
				error("Could not create catalog ( values = " + values + " : " + e.getMessage() + " : "
						+ Utilities.getStackTraceAsString(e));
			}
			throw new JaloSystemException(e);
		}
		catch (final JaloAbstractTypeException e)
		{
			if (isErrorEnabled())
			{
				error("Could not create catalog with abstract type : " + Utilities.getStackTraceAsString(e));
			}
			throw new JaloSystemException(e);
		}
	}

	/**
	 * Creates catalog version
	 * 
	 * @param values
	 *           parameter for catalog creation
	 * @return catalog version
	 */
	protected CatalogVersion createCatalogVersion(final BMECatImportCronJob cronJob, final ItemAttributeMap values)
	{
		try
		{
			return (CatalogVersion) getCatalogVersionType().newInstance(values);
		}
		catch (final Exception e)
		{
			if (isErrorEnabled())
			{
				error("Could not create catalog version ( values = " + values + " : " + e.getMessage() + " : "
						+ Utilities.getStackTraceAsString(e));
			}
			throw new JaloSystemException(e);
		}
	}

	/**
	 * Gets an existing company by company name or creates a new with the given parameters (mapping)
	 * 
	 * @param cronJob
	 *           context of this step
	 * @param companyType
	 *           type of company to be created
	 * @param mapping
	 *           Maps company tags/attributes to company item attributes
	 * @param companyValueObject
	 *           value object containing information about parsed &lt;BUYER&gt; &lt;SUPPLIER&gt; tag
	 * @return company of specified type
	 */
	protected Company getOrCreateCompany(final BMECatImportCronJob cronJob, final ComposedType companyType, final Map mapping,
			final de.hybris.platform.bmecat.parser.Company companyValueObject)
	{
		if (companyValueObject == null || companyValueObject.getName() == null)
		{
			return null;
		}
		Company company = getCatalogManager().getCompanyByUID(companyValueObject.getName());
		if (company == null)
		{
			Map values = Collections.EMPTY_MAP;
			Map values2 = Collections.EMPTY_MAP;
			try
			{
				company = (Company) companyType.newInstance(values = getCompanyValues(cronJob, null, mapping, companyValueObject));

				company.setAllAttributes(values2 = getPartOfCompanyValues(cronJob, company, mapping, companyValueObject));
				addChange(cronJob, BMECatConstants.ChangeTypes.CREATE_COMPANY, company, "Created new company for "
						+ companyValueObject.getName());
			}
			catch (final Exception e)
			{
				if (isErrorEnabled())
				{
					error("Could not create company ( values = " + values + ", partOfValue = " + values2 + ") : " + e.getMessage()
							+ " : " + Utilities.getStackTraceAsString(e));
				}
				throw new JaloSystemException(e);
			}
		}
		else
		{
			Map values = Collections.EMPTY_MAP;
			try
			{
				company.setAllAttributes(values = getCompanyValues(cronJob, company, mapping, companyValueObject));
			}
			catch (final Exception e)
			{
				if (isErrorEnabled())
				{
					error("Could not update company ( values = " + values + " ) : " + e.getMessage() + " : "
							+ Utilities.getStackTraceAsString(e));
				}
				throw new JaloSystemException(e);
			}
		}
		return company;
	}

	/**
	 * Gets existing region by parameter "state" or creates new
	 * 
	 * @param jaloCountry
	 * @param state
	 * @return the Region
	 */
	protected Region getOrCreateRegion(final CronJob cronJob, final Country jaloCountry, final String state)
	{
		if (state == null)
		{
			return null;
		}

		try
		{
			return jaloCountry.getRegionByCode(state);
		}
		catch (final JaloItemNotFoundException exp)
		{
			try
			{
				final Region region = jaloCountry.addNewRegion(state);
				region.setActive(true);
				region.setName("Created automatically by BMECat Import on " + region.getCreationTime());
				addChange(cronJob, BMECatConstants.ChangeTypes.CREATE_REGION, region, "Created automatically by BMECat Import on "
						+ region.getCreationTime());
				return region;
			}
			catch (final ConsistencyCheckException exp2)
			{
				throw new JaloSystemException(exp2);
			}
		}
	}

	/**
	 * Gets existing region by parameter "state"
	 * 
	 * @param jaloCountry
	 * @param state
	 * @return the Region
	 */
	protected Region getRegion(final CronJob cronJob, final Country jaloCountry, final String state)
	{
		if (state == null)
		{
			return null;
		}

		try
		{
			return jaloCountry.getRegionByCode(state);
		}
		catch (final JaloItemNotFoundException exp)
		{
			if (isInfoEnabled())
			{
				info("region: '" + state + "' not found!");
			}
		}
		return null;
	}

	/**
	 * gets or create an address for parameters contained in addressValueObject
	 * 
	 * @param cronJob
	 * @param owner
	 * @param addressValueObject
	 * @return address
	 */
	protected Collection getOrCreateAddresses(final BMECatImportCronJob cronJob, final Company owner,
			final de.hybris.platform.bmecat.parser.Address addressValueObject)
	{
		final Collection addresses = new HashSet();
		if (addressValueObject != null && owner != null)
		{
			Map values = Collections.EMPTY_MAP;
			try
			{
				addresses.addAll(owner.getAddresses());
				final Address address = (Address) getAddressType().newInstance(
						values = getAddressValues(cronJob, owner, addressValueObject, true));
				addChange(cronJob, BMECatConstants.ChangeTypes.CREATE_ADDRESS, address, null);
				addresses.add(address);
			}
			catch (final Exception e)
			{
				if (isErrorEnabled())
				{
					error("Could not create company address ( company = " + owner + ", values = " + values + " ) : " + e.getMessage()
							+ " : " + Utilities.getStackTraceAsString(e));
				}
				throw new JaloSystemException(e);
			}
		}
		return addresses;
	}

	/**
	 * Translates atributes of value object {@link de.hybris.platform.bmecat.parser.Address} into attributes of the item
	 * {@link Address}
	 * 
	 * @param cronJob
	 * @param owner
	 * @param addressValueObject
	 * @param create
	 * @return a map, assigning {@link Address} attribute qualifiers to values extracted from the given BMECat value
	 *         object
	 */
	protected ItemAttributeMap getAddressValues(final BMECatImportCronJob cronJob, final Company owner,
			final de.hybris.platform.bmecat.parser.Address addressValueObject, final boolean create)
	{
		final ItemAttributeMap values = new ItemAttributeMap();
		final Map mapping = getAllAddressAttributeMapping();
		if (!cronJob.isLocalizationUpdateAsPrimitive())
		{
			if (mapping.containsKey(ADDRESS_BOXNO))
			{
				values.put(getAttributeQualifier(ADDRESS_BOXNO, mapping), addressValueObject.getBoxno());
			}
			if (mapping.containsKey(ADDRESS_CITY))
			{
				values.put(getAttributeQualifier(ADDRESS_CITY, mapping), addressValueObject.getCity());
			}
			if (mapping.containsKey(ADDRESS_CONTACT))
			{
				values.put(getAttributeQualifier(ADDRESS_CONTACT, mapping), addressValueObject.getContact());
			}
			if (mapping.containsKey(ADDRESS_COUNTRY))
			{
				final Country country = cronJob.getCountry(addressValueObject.getCountry());
				if (country == null)
				{
					if (isInfoEnabled())
					{
						info("country with isocode: '" + addressValueObject.getCountry() + "' not found!");
					}
				}
				values.put(getAttributeQualifier(ADDRESS_COUNTRY, mapping), country);
			}
			if (mapping.containsKey(ADDRESS_EMAIL))
			{
				values.put(getAttributeQualifier(ADDRESS_EMAIL, mapping), addressValueObject.getEmail());
			}
			if (mapping.containsKey(ADDRESS_FAX))
			{
				values.put(getAttributeQualifier(ADDRESS_FAX, mapping), addressValueObject.getFax());
			}
			if (mapping.containsKey(ADDRESS_NAME))
			{
				values.put(getAttributeQualifier(ADDRESS_NAME, mapping), addressValueObject.getName());
			}
			if (mapping.containsKey(ADDRESS_NAME2))
			{
				values.put(getAttributeQualifier(ADDRESS_NAME2, mapping), addressValueObject.getName2());
			}
			if (mapping.containsKey(ADDRESS_NAME3))
			{
				values.put(getAttributeQualifier(ADDRESS_NAME3, mapping), addressValueObject.getName3());
			}
			if (mapping.containsKey(ADDRESS_PHONE))
			{
				values.put(getAttributeQualifier(ADDRESS_PHONE, mapping), addressValueObject.getPhone());
			}
			if (mapping.containsKey(ADDRESS_PUBLICKEY))
			{
				values.put(getAttributeQualifier(ADDRESS_PUBLICKEY, mapping), addressValueObject.getPublicKey());
			}
			if (mapping.containsKey(ADDRESS_REMARKS))
			{
				values.put(getAttributeQualifier(ADDRESS_REMARKS, mapping), addressValueObject.getRemarks());
			}

			if (mapping.containsKey(ADDRESS_STATE))
			{
				final Object value = values.get(getAttributeQualifier(ADDRESS_COUNTRY, mapping));
				if (value instanceof Country)
				{
					values.put(getAttributeQualifier(ADDRESS_STATE, mapping),
							getRegion(cronJob, (Country) value, addressValueObject.getState()));
				}
			}
			if (mapping.containsKey(ADDRESS_STREET))
			{
				values.put(getAttributeQualifier(ADDRESS_STREET, mapping), addressValueObject.getStreet());
			}
			if (mapping.containsKey(ADDRESS_URL))
			{
				values.put(getAttributeQualifier(ADDRESS_URL, mapping), addressValueObject.getUrl());
			}
			if (mapping.containsKey(ADDRESS_ZIP))
			{
				values.put(getAttributeQualifier(ADDRESS_ZIP, mapping), addressValueObject.getZip());
			}
			if (mapping.containsKey(ADDRESS_ZIPBOX))
			{
				values.put(getAttributeQualifier(ADDRESS_ZIPBOX, mapping), addressValueObject.getZipbox());
			}

			if (create)
			{
				values.put(Address.OWNER, owner);
			}
		}

		return values;
	}

	/**
	 * Translates attributes of value object {@link de.hybris.platform.bmecat.parser.Agreement} into attributes of the
	 * item {@link Agreement}
	 * 
	 * @param cronJob
	 * @param agreementValueObject
	 * @return a map, assigning {@link Agreement} attribute qualifiers to values extracted from the given BMECat value
	 *         object
	 */
	protected ItemAttributeMap getAgreementValues(final BMECatImportCronJob cronJob,
			final de.hybris.platform.bmecat.parser.Agreement agreementValueObject)
	{
		final ItemAttributeMap values = new ItemAttributeMap();
		final Map mapping = getAllAgreementAttributeMapping();
		if (!cronJob.isLocalizationUpdateAsPrimitive())
		{
			if (mapping.containsKey(AGREEMENT_CATALOGVERSION))
			{
				values.put(getAttributeQualifier(AGREEMENT_CATALOGVERSION, mapping), cronJob.getCatalogVersion());
			}
			if (mapping.containsKey(AGREEMENT_ENDDATE))
			{
				values.put(getAttributeQualifier(AGREEMENT_ENDDATE, mapping), agreementValueObject.getEndDate());
			}
			if (mapping.containsKey(AGREEMENT_ID))
			{
				values.put(getAttributeQualifier(AGREEMENT_ID, mapping), agreementValueObject.getID());
			}
			if (mapping.containsKey(AGREEMENT_STARTDATE))
			{
				values.put(getAttributeQualifier(AGREEMENT_STARTDATE, mapping), agreementValueObject.getStartDate());
			}
		}
		return values;
	}

	/**
	 * Translates attributes of value object {@link de.hybris.platform.bmecat.parser.Company} into attributes of the item
	 * {@link Company}
	 * 
	 * @param cronJob
	 * @param companyValueObject
	 * @return a map, assigning {@link Company} attribute qualifiers to values extracted from the given BMECat value
	 *         object
	 */
	protected ItemAttributeMap getCompanyValues(final BMECatImportCronJob cronJob, final Company company, final Map mapping,
			final de.hybris.platform.bmecat.parser.Company companyValueObject)
	{
		final ItemAttributeMap values = new ItemAttributeMap();
		if (!cronJob.isLocalizationUpdateAsPrimitive())
		{
			if (mapping.containsKey(COMPANY_ID))
			{
				values.put(getAttributeQualifier(COMPANY_ID, mapping), companyValueObject.getId());
			}
			if (mapping.containsKey(COMPANY_BUYERSPECIFICID))
			{
				values.put(getAttributeQualifier(COMPANY_BUYERSPECIFICID, mapping), companyValueObject.getBuyerSpecificID());
			}
			if (mapping.containsKey(COMPANY_DUNSID))
			{
				values.put(getAttributeQualifier(COMPANY_DUNSID, mapping), companyValueObject.getDunsID());
			}
			if (mapping.containsKey(COMPANY_ILNID))
			{
				values.put(getAttributeQualifier(COMPANY_ILNID, mapping), companyValueObject.getIlnID());
			}
			if (mapping.containsKey(COMPANY_SUPPLIERSPECIFICID))
			{
				values.put(getAttributeQualifier(COMPANY_SUPPLIERSPECIFICID, mapping), companyValueObject.getSupplierSpecificID());
			}
			if (mapping.containsKey(COMPANY_NAME))
			{
				values.put(getAttributeQualifier(COMPANY_NAME, mapping), companyValueObject.getName());
			}
			if (mapping.containsKey(COMPANY_ADDRESS) && company != null)
			{
				values.put(getAttributeQualifier(COMPANY_ADDRESS, mapping),
						getOrCreateAddresses(cronJob, company, companyValueObject.getAddress()));
			}
		}
		return values;
	}

	/**
	 * Binds {@link Address} to {@link Company} according to the given bmecat value object
	 * {@link de.hybris.platform.bmecat.parser.Company}
	 * 
	 * @param cronJob
	 * @param company
	 * @param mapping
	 * @param companyValueObject
	 * @return a map, assigning {@link Company} attribute qualifiers to values extracted from the given BMECat value
	 *         object
	 */
	protected ItemAttributeMap getPartOfCompanyValues(final BMECatImportCronJob cronJob, final Company company, final Map mapping,
			final de.hybris.platform.bmecat.parser.Company companyValueObject)
	{
		final ItemAttributeMap values = new ItemAttributeMap();
		if (!cronJob.isLocalizationUpdateAsPrimitive())
		{
			if (mapping.containsKey(COMPANY_ADDRESS) && company != null)
			{
				values.put(getAttributeQualifier(COMPANY_ADDRESS, mapping),
						getOrCreateAddresses(cronJob, company, companyValueObject.getAddress()));
			}
		}
		return values;
	}

	/**
	 * Translates attributes of value object {@link de.hybris.platform.bmecat.parser.Catalog} into attributes of the item
	 * {@link Catalog}
	 * 
	 * @param cronJob
	 * @param catalogValueObject
	 * @return a map, assigning {@link Catalog} attribute qualifiers to values extracted from the given BMECat value
	 *         object
	 * @throws ParseAbortException
	 */
	protected Item.ItemAttributeMap getCatalogValues(final BMECatImportCronJob cronJob,
			final de.hybris.platform.bmecat.parser.Catalog catalogValueObject, final Catalog catalog) throws ParseAbortException
	{
		final boolean infoAvailable = cronJob.isCatalogInfoAvailableAsPrimitive();
		final Item.ItemAttributeMap values = new Item.ItemAttributeMap();
		final Map mapping = getAllCatalogAttributeMapping();

		/*
		 * Only store these values if the catalog is created by this step - otherwise dont change anything
		 */
		if (catalog == null && !cronJob.isLocalizationUpdateAsPrimitive())
		{
			if (mapping.containsKey(CATALOG_ID))
			{
				values.put(getAttributeQualifier(CATALOG_ID, mapping),
						infoAvailable && cronJob.getCatalogID() != null ? cronJob.getCatalogID() : catalogValueObject.getID());
			}

			if (mapping.containsKey(CATALOG_SUPPLIER))
			{
				Company supplier = null;
				if (catalogValueObject.getSupplier() != null)
				{
					if (catalogValueObject.getSupplier().getName() != null)
					{
						supplier = getCatalogManager().getCompanyByUID(catalogValueObject.getSupplier().getName());
					}
					if (supplier == null)
					{
						// supplier does not yet exist - to create or not to create that is the question
						if (cronJob.isImportSupplierAsPrimitive())
						{
							supplier = getOrCreateSupplier(cronJob, catalogValueObject.getSupplier());
						}
						else
						{
							if (isErrorEnabled())
							{
								error("Supplier with name >"
										+ catalogValueObject.getSupplier().getName()
										+ "< not found while cronjob has been ordered not to import new suppliers. (cronJob.importSupplier=false)");
							}
							throw new JaloSystemException(
									null,
									"Supplier with name >"
											+ catalogValueObject.getSupplier().getName()
											+ "< not found while cronjob has been ordered not to import new suppliers. (cronJob.importSupplier=false)",
									0);
						}
					}
					if (isDebugEnabled())
					{
						debug("getCatalogValues: supplier - " + (supplier == null ? "null" : supplier.getUID()));
					}
				}
				values.put(getAttributeQualifier(CATALOG_SUPPLIER, mapping), supplier);
			}
			if (mapping.containsKey(CATALOG_BUYER))
			{
				Company buyer = null;
				if (catalogValueObject.getBuyer() != null)
				{
					if (catalogValueObject.getBuyer().getName() != null)
					{
						buyer = getCatalogManager().getCompanyByUID(catalogValueObject.getBuyer().getName());
					}
					if (buyer == null)
					{
						if (cronJob.isImportBuyerAsPrimitive())
						{
							buyer = getOrCreateBuyer(cronJob, catalogValueObject.getBuyer());
						}
						else if (isInfoEnabled())
						{
							info("skipping import of buyers (importBuyer is set to false in cronJob).");
						}
					}
				}
				values.put(getAttributeQualifier(CATALOG_BUYER, mapping), buyer);
			}
			if (mapping.containsKey(CATALOG_NAME))
			{
				values.put(getAttributeQualifier(CATALOG_NAME, mapping), catalogValueObject.getName());
			}
		}
		/*
		 * Localization update of catalog name
		 */
		else if (catalog == null || cronJob.isLocalizationUpdateAsPrimitive())
		{
			if (mapping.containsKey(CATALOG_NAME))
			{
				values.put(getAttributeQualifier(CATALOG_NAME, mapping), catalogValueObject.getName());
			}
		}

		return values;
	}

	/**
	 * Translates attributes of value object {@link de.hybris.platform.bmecat.parser.Catalog} into attributes of the item
	 * {@link CatalogVersion}
	 * 
	 * @param cronJob
	 * @param catalogValueObject
	 * @param catalogVersion
	 * @return a map, assigning {@link CatalogVersion} attribute qualifiers to values extracted from the given BMECat
	 *         value object
	 */
	protected ItemAttributeMap getCatalogVersionValues(final BMECatImportCronJob cronJob,
			final de.hybris.platform.bmecat.parser.Catalog catalogValueObject, final CatalogVersion catalogVersion)
	{
		final ItemAttributeMap values = new ItemAttributeMap();
		final Map mapping = getAllCatalogVersionAttributeMapping();

		/*
		 * Only update these fields if the catalog version is created by this step - otherwise dont change anything !
		 */
		if (catalogVersion == null && !cronJob.isLocalizationUpdateAsPrimitive())
		{
			if (mapping.containsKey(CATALOGVERSION_AGREEMENT))
			{
				values.put(getAttributeQualifier(CATALOGVERSION_AGREEMENT, mapping),
						getOrCreateAgreements(cronJob, catalogValueObject));
			}
			if (mapping.containsKey(CATALOGVERSION_DEFAULTCURRENCY))
			{
				values.put(getAttributeQualifier(CATALOGVERSION_DEFAULTCURRENCY, mapping),
						cronJob.getOrCreateCurrency(catalogValueObject, null));
			}
			if (mapping.containsKey(CATALOGVERSION_GENERATIONDATE))
			{
				values.put(getAttributeQualifier(CATALOGVERSION_GENERATIONDATE, mapping), catalogValueObject.getGenerationDate());
			}
			if (mapping.containsKey(CATALOGVERSION_GENERATORINFO))
			{
				values.put(getAttributeQualifier(CATALOGVERSION_GENERATORINFO, mapping), catalogValueObject.getGeneratorInfo());
			}
			if (mapping.containsKey(CATALOGVERSION_INCLASSURANCE))
			{
				values.put(getAttributeQualifier(CATALOGVERSION_INCLASSURANCE, mapping), catalogValueObject.getInclAssurance());
			}
			if (mapping.containsKey(CATALOGVERSION_INCLDUTY))
			{
				values.put(getAttributeQualifier(CATALOGVERSION_INCLDUTY, mapping), catalogValueObject.getInclDuty());
			}
			if (mapping.containsKey(CATALOGVERSION_INCLFREIGHT))
			{
				values.put(getAttributeQualifier(CATALOGVERSION_INCLFREIGHT, mapping), catalogValueObject.getInclFreight());
			}
			if (mapping.containsKey(CATALOGVERSION_INCLPACKING))
			{
				values.put(getAttributeQualifier(CATALOGVERSION_INCLPACKING, mapping), catalogValueObject.getInclPacking());
			}
			if (mapping.containsKey(CATALOGVERSION_MIMEROOTDIRECTORY))
			{
				values.put(getAttributeQualifier(CATALOGVERSION_MIMEROOTDIRECTORY, mapping),
						catalogValueObject.getMimeRootDirectory());
			}
			if (mapping.containsKey(CATALOGVERSION_TERRITORIES))
			{
				values.put(getAttributeQualifier(CATALOGVERSION_TERRITORIES, mapping),
						cronJob.getOrCreateCountries(catalogValueObject.getTerritories()));
			}
			if (mapping.containsKey(CATALOGVERSION_CATALOG))
			{
				values.put(getAttributeQualifier(CATALOGVERSION_CATALOG, mapping), cronJob.getCatalog());
			}
			if (mapping.containsKey(CATALOGVERSION_VERSION))
			{
				values.put(getAttributeQualifier(CATALOGVERSION_VERSION, mapping), catalogValueObject.getVersion());
			}
		}
		if (mapping.containsKey(CATALOGVERSION_LANGUAGE))
		{
			if (catalogVersion != null)
			{
				if (!catalogVersion.isImportedLanguage(catalogValueObject.getLanguage()))
				{
					final Collection languages = (Collection) getAttributeValue(cronJob, CATALOGVERSION_LANGUAGE, mapping,
							catalogVersion);
					languages.add(cronJob.getOrCreateCatalogLanguage(catalogValueObject.getLanguage()));
					values.put(getAttributeQualifier(CATALOGVERSION_LANGUAGE, mapping), languages);
				}
			}
			else
			{
				values.put(getAttributeQualifier(CATALOGVERSION_LANGUAGE, mapping),
						Collections.singletonList(cronJob.getOrCreateCatalogLanguage(catalogValueObject.getLanguage())));
			}
		}
		return values;
	}

	protected Map createDefaultAddressMapping(final ComposedType addressType)
	{
		final Map mapping = new HashMap();
		MappingUtils.addMapping(mapping, addressType, ADDRESS_CITY, Address.TOWN, this);
		MappingUtils.addMapping(mapping, addressType, ADDRESS_CONTACT, Address.LASTNAME, this);
		MappingUtils.addMapping(mapping, addressType, ADDRESS_COUNTRY, Address.COUNTRY, this);
		MappingUtils.addMapping(mapping, addressType, ADDRESS_EMAIL, Address.EMAIL, this);
		MappingUtils.addMapping(mapping, addressType, ADDRESS_FAX, Address.FAX, this);
		MappingUtils.addMapping(mapping, addressType, ADDRESS_NAME, Address.COMPANY, this);
		MappingUtils.addMapping(mapping, addressType, ADDRESS_PHONE, Address.PHONE1, this);
		MappingUtils.addMapping(mapping, addressType, ADDRESS_PUBLICKEY, CatalogConstants.Attributes.Address.PUBLICKEY, this);
		MappingUtils.addMapping(mapping, addressType, ADDRESS_REMARKS, CatalogConstants.Attributes.Address.REMARKS, this);
		MappingUtils.addMapping(mapping, addressType, ADDRESS_STATE, Address.REGION, this);
		MappingUtils.addMapping(mapping, addressType, ADDRESS_STREET, Address.STREETNAME, this);
		MappingUtils.addMapping(mapping, addressType, ADDRESS_URL, CatalogConstants.Attributes.Address.URL, this);
		MappingUtils.addMapping(mapping, addressType, ADDRESS_ZIP, Address.POSTALCODE, this);
		return mapping;
	}

	protected Map createDefaultAgreementMapping(final ComposedType agreementType)
	{
		final Map mapping = new HashMap();
		MappingUtils.addMapping(mapping, agreementType, AGREEMENT_CATALOGVERSION, Agreement.CATALOGVERSION, this);
		MappingUtils.addMapping(mapping, agreementType, AGREEMENT_ENDDATE, Agreement.ENDDATE, this);
		MappingUtils.addMapping(mapping, agreementType, AGREEMENT_ID, Agreement.ID, this);
		MappingUtils.addMapping(mapping, agreementType, AGREEMENT_STARTDATE, Agreement.STARTDATE, this);
		return mapping;
	}

	protected Map createDefaultCatalogMapping(final ComposedType catalogType)
	{
		final Map mapping = new HashMap();
		MappingUtils.addMapping(mapping, catalogType, CATALOG_ID, Catalog.ID, this);
		MappingUtils.addMapping(mapping, catalogType, CATALOG_NAME, Catalog.NAME, this);
		MappingUtils.addMapping(mapping, catalogType, CATALOG_SUPPLIER, Catalog.SUPPLIER, this);
		MappingUtils.addMapping(mapping, catalogType, CATALOG_BUYER, Catalog.BUYER, this);

		return mapping;
	}

	protected Map createDefaultCatalogVersionMapping(final ComposedType catalogVersionType)
	{
		final Map mapping = new HashMap();
		MappingUtils.addMapping(mapping, catalogVersionType, CATALOGVERSION_VERSION, CatalogVersion.VERSION, this);
		MappingUtils.addMapping(mapping, catalogVersionType, CATALOGVERSION_AGREEMENT, CatalogVersion.AGREEMENTS, this);
		MappingUtils.addMapping(mapping, catalogVersionType, CATALOGVERSION_DEFAULTCURRENCY, CatalogVersion.DEFAULTCURRENCY, this);
		MappingUtils.addMapping(mapping, catalogVersionType, CATALOGVERSION_GENERATIONDATE, CatalogVersion.GENERATIONDATE, this);
		MappingUtils.addMapping(mapping, catalogVersionType, CATALOGVERSION_GENERATORINFO, CatalogVersion.GENERATORINFO, this);
		MappingUtils.addMapping(mapping, catalogVersionType, CATALOGVERSION_INCLASSURANCE, CatalogVersion.INCLASSURANCE, this);
		MappingUtils.addMapping(mapping, catalogVersionType, CATALOGVERSION_INCLDUTY, CatalogVersion.INCLDUTY, this);
		MappingUtils.addMapping(mapping, catalogVersionType, CATALOGVERSION_INCLFREIGHT, CatalogVersion.INCLFREIGHT, this);
		MappingUtils.addMapping(mapping, catalogVersionType, CATALOGVERSION_INCLPACKING, CatalogVersion.INCLPACKING, this);
		MappingUtils.addMapping(mapping, catalogVersionType, CATALOGVERSION_LANGUAGE, CatalogVersion.LANGUAGES, this);
		MappingUtils.addMapping(mapping, catalogVersionType, CATALOGVERSION_MIMEROOTDIRECTORY, CatalogVersion.MIMEROOTDIRECTORY,
				this);
		MappingUtils.addMapping(mapping, catalogVersionType, CATALOGVERSION_TERRITORIES, CatalogVersion.TERRITORIES, this);
		MappingUtils.addMapping(mapping, catalogVersionType, CATALOGVERSION_CATALOG, CatalogVersion.CATALOG, this);
		//categorysystemid
		//categorysystemname
		//categorysystemdescription
		return mapping;
	}

	protected Map createDefaultCompanyMapping(final ComposedType companyType)
	{
		final Map mapping = new HashMap();
		MappingUtils.addMapping(mapping, companyType, COMPANY_ADDRESS, Company.ADDRESSES, this);
		MappingUtils.addMapping(mapping, companyType, COMPANY_ID, Company.ID, this);
		MappingUtils.addMapping(mapping, companyType, COMPANY_BUYERSPECIFICID, Company.BUYERSPECIFICID, this);
		MappingUtils.addMapping(mapping, companyType, COMPANY_DUNSID, Company.DUNSID, this);
		MappingUtils.addMapping(mapping, companyType, COMPANY_ILNID, Company.ILNID, this);
		MappingUtils.addMapping(mapping, companyType, COMPANY_NAME, Principal.UID, this);
		MappingUtils.addMapping(mapping, companyType, COMPANY_SUPPLIERSPECIFICID, Company.SUPPLIERSPECIFICID, this);
		return mapping;
	}

	/**
	 * Creates enumeration values which represent BMECat elements or attributes. This enum values are later used to map
	 * bmecat elements to item attributes
	 */
	protected void initializeAttributeEnumValues()
	{
		EnumerationType eType = getSession().getEnumerationManager().getEnumerationType(BMECatConstants.TC.CATALOGATTRIBUTEENUM);
		CATALOG_ID = getSession().getEnumerationManager().getEnumerationValue(eType,
				BMECatConstants.Enumerations.CatalogAttributeEnum.ID);
		CATALOG_NAME = getSession().getEnumerationManager().getEnumerationValue(eType,
				BMECatConstants.Enumerations.CatalogAttributeEnum.NAME);
		CATALOG_SUPPLIER = getSession().getEnumerationManager().getEnumerationValue(eType,
				BMECatConstants.Enumerations.CatalogAttributeEnum.SUPPLIER);
		CATALOG_BUYER = getSession().getEnumerationManager().getEnumerationValue(eType,
				BMECatConstants.Enumerations.CatalogAttributeEnum.BUYER);

		eType = getSession().getEnumerationManager().getEnumerationType(BMECatConstants.TC.CATALOGVERSIONATTRIBUTEENUM);
		CATALOGVERSION_AGREEMENT = getSession().getEnumerationManager().getEnumerationValue(eType,
				BMECatConstants.Enumerations.CatalogVersionAttributeEnum.AGREEMENTS);
		CATALOGVERSION_DEFAULTCURRENCY = getSession().getEnumerationManager().getEnumerationValue(eType,
				BMECatConstants.Enumerations.CatalogVersionAttributeEnum.DEFAULTCURRENCY);
		CATALOGVERSION_GENERATIONDATE = getSession().getEnumerationManager().getEnumerationValue(eType,
				BMECatConstants.Enumerations.CatalogVersionAttributeEnum.GENERATIONDATE);
		CATALOGVERSION_GENERATORINFO = getSession().getEnumerationManager().getEnumerationValue(eType,
				BMECatConstants.Enumerations.CatalogVersionAttributeEnum.GENERATORINFO);
		CATALOGVERSION_INCLASSURANCE = getSession().getEnumerationManager().getEnumerationValue(eType,
				BMECatConstants.Enumerations.CatalogVersionAttributeEnum.INCLASSURANCE);
		CATALOGVERSION_INCLDUTY = getSession().getEnumerationManager().getEnumerationValue(eType,
				BMECatConstants.Enumerations.CatalogVersionAttributeEnum.INCLDUTY);
		CATALOGVERSION_INCLFREIGHT = getSession().getEnumerationManager().getEnumerationValue(eType,
				BMECatConstants.Enumerations.CatalogVersionAttributeEnum.INCLFREIGHT);
		CATALOGVERSION_INCLPACKING = getSession().getEnumerationManager().getEnumerationValue(eType,
				BMECatConstants.Enumerations.CatalogVersionAttributeEnum.INCLPACKING);
		CATALOGVERSION_LANGUAGE = getSession().getEnumerationManager().getEnumerationValue(eType,
				BMECatConstants.Enumerations.CatalogVersionAttributeEnum.LANGUAGE);
		CATALOGVERSION_MIMEROOTDIRECTORY = getSession().getEnumerationManager().getEnumerationValue(eType,
				BMECatConstants.Enumerations.CatalogVersionAttributeEnum.MIMEROOTDIRECTORY);
		CATALOGVERSION_TERRITORIES = getSession().getEnumerationManager().getEnumerationValue(eType,
				BMECatConstants.Enumerations.CatalogVersionAttributeEnum.TERRITORIES);
		//CATALOGVERSION_FEATURESYSTEM = getSession().getEnumerationManager().getEnumerationValue( eType, BMECatConstants.Enumerations.CatalogVersionAttributeEnum. );
		CATALOGVERSION_VERSION = getSession().getEnumerationManager().getEnumerationValue(eType,
				BMECatConstants.Enumerations.CatalogVersionAttributeEnum.VERSION);
		CATALOGVERSION_CATALOG = getSession().getEnumerationManager().getEnumerationValue(eType,
				BMECatConstants.Enumerations.CatalogVersionAttributeEnum.CATALOG);

		eType = getSession().getEnumerationManager().getEnumerationType(BMECatConstants.TC.COMPANYATTRIBUTEENUM);
		COMPANY_NAME = getSession().getEnumerationManager().getEnumerationValue(eType,
				BMECatConstants.Enumerations.CompanyAttributeEnum.NAME);
		COMPANY_ID = getSession().getEnumerationManager().getEnumerationValue(eType,
				BMECatConstants.Enumerations.CompanyAttributeEnum.ID);
		COMPANY_BUYERSPECIFICID = getSession().getEnumerationManager().getEnumerationValue(eType,
				BMECatConstants.Enumerations.CompanyAttributeEnum.BUYERSPECIFICID);
		COMPANY_SUPPLIERSPECIFICID = getSession().getEnumerationManager().getEnumerationValue(eType,
				BMECatConstants.Enumerations.CompanyAttributeEnum.SUPPLIERSPECIFICID);
		COMPANY_ILNID = getSession().getEnumerationManager().getEnumerationValue(eType,
				BMECatConstants.Enumerations.CompanyAttributeEnum.ILNID);
		COMPANY_DUNSID = getSession().getEnumerationManager().getEnumerationValue(eType,
				BMECatConstants.Enumerations.CompanyAttributeEnum.DUNSID);
		COMPANY_ADDRESS = getSession().getEnumerationManager().getEnumerationValue(eType,
				BMECatConstants.Enumerations.CompanyAttributeEnum.ADDRESS);

		eType = getSession().getEnumerationManager().getEnumerationType(BMECatConstants.TC.ADDRESSATTRIBUTEENUM);
		ADDRESS_NAME = getSession().getEnumerationManager().getEnumerationValue(eType,
				BMECatConstants.Enumerations.AddressAttributeEnum.NAME);
		ADDRESS_NAME2 = getSession().getEnumerationManager().getEnumerationValue(eType,
				BMECatConstants.Enumerations.AddressAttributeEnum.NAME2);
		ADDRESS_NAME3 = getSession().getEnumerationManager().getEnumerationValue(eType,
				BMECatConstants.Enumerations.AddressAttributeEnum.NAME3);
		ADDRESS_CONTACT = getSession().getEnumerationManager().getEnumerationValue(eType,
				BMECatConstants.Enumerations.AddressAttributeEnum.CONTACT);
		ADDRESS_STREET = getSession().getEnumerationManager().getEnumerationValue(eType,
				BMECatConstants.Enumerations.AddressAttributeEnum.STREET);
		ADDRESS_ZIP = getSession().getEnumerationManager().getEnumerationValue(eType,
				BMECatConstants.Enumerations.AddressAttributeEnum.ZIP);
		ADDRESS_BOXNO = getSession().getEnumerationManager().getEnumerationValue(eType,
				BMECatConstants.Enumerations.AddressAttributeEnum.BOXNO);
		ADDRESS_ZIPBOX = getSession().getEnumerationManager().getEnumerationValue(eType,
				BMECatConstants.Enumerations.AddressAttributeEnum.ZIPBOX);
		ADDRESS_CITY = getSession().getEnumerationManager().getEnumerationValue(eType,
				BMECatConstants.Enumerations.AddressAttributeEnum.CITY);
		ADDRESS_STATE = getSession().getEnumerationManager().getEnumerationValue(eType,
				BMECatConstants.Enumerations.AddressAttributeEnum.STATE);
		ADDRESS_COUNTRY = getSession().getEnumerationManager().getEnumerationValue(eType,
				BMECatConstants.Enumerations.AddressAttributeEnum.COUNTRY);
		ADDRESS_PHONE = getSession().getEnumerationManager().getEnumerationValue(eType,
				BMECatConstants.Enumerations.AddressAttributeEnum.PHONE);
		ADDRESS_FAX = getSession().getEnumerationManager().getEnumerationValue(eType,
				BMECatConstants.Enumerations.AddressAttributeEnum.FAX);
		ADDRESS_EMAIL = getSession().getEnumerationManager().getEnumerationValue(eType,
				BMECatConstants.Enumerations.AddressAttributeEnum.EMAIL);
		ADDRESS_PUBLICKEY = getSession().getEnumerationManager().getEnumerationValue(eType,
				BMECatConstants.Enumerations.AddressAttributeEnum.PUBLICKEY);
		ADDRESS_URL = getSession().getEnumerationManager().getEnumerationValue(eType,
				BMECatConstants.Enumerations.AddressAttributeEnum.URL);
		ADDRESS_REMARKS = getSession().getEnumerationManager().getEnumerationValue(eType,
				BMECatConstants.Enumerations.AddressAttributeEnum.REMARKS);

		eType = getSession().getEnumerationManager().getEnumerationType(BMECatConstants.TC.AGREEMENTATTRIBUTEENUM);
		AGREEMENT_ID = getSession().getEnumerationManager().getEnumerationValue(eType,
				BMECatConstants.Enumerations.AgreementAttributeEnum.ID);
		AGREEMENT_STARTDATE = getSession().getEnumerationManager().getEnumerationValue(eType,
				BMECatConstants.Enumerations.AgreementAttributeEnum.STARTDATE);
		AGREEMENT_ENDDATE = getSession().getEnumerationManager().getEnumerationValue(eType,
				BMECatConstants.Enumerations.AgreementAttributeEnum.ENDDATE);
		AGREEMENT_CATALOGVERSION = getSession().getEnumerationManager().getEnumerationValue(eType,
				BMECatConstants.Enumerations.AgreementAttributeEnum.CATALOGVERSION);
	}

	protected Company getOrCreateBuyer(final BMECatImportCronJob cronJob,
			final de.hybris.platform.bmecat.parser.Company companyValueObject)
	{
		return getOrCreateCompany(cronJob, getBuyerType(), getAllBuyerAttributeMapping(), companyValueObject);
	}

	protected Company getOrCreateSupplier(final BMECatImportCronJob cronJob,
			final de.hybris.platform.bmecat.parser.Company companyValueObject)
	{
		return getOrCreateCompany(cronJob, getSupplierType(), getAllSupplierAttributeMapping(), companyValueObject);
	}

	private String getAttributeQualifier(final EnumerationValue eValue, final Map mapping)
	{
		return ((AttributeDescriptor) mapping.get(eValue)).getQualifier();
	}

	private Object getAttributeValue(final BMECatImportCronJob cronJob, final EnumerationValue eValue, final Map mapping,
			final Item item)
	{
		final String qualifier = getAttributeQualifier(eValue, mapping);
		if (qualifier != null)
		{
			try
			{
				return item.getAttribute(qualifier);
			}
			catch (final Exception e)
			{
				if (isErrorEnabled())
				{
					error("Could not reat attribute '" + qualifier + "' mapped to bmecat field " + eValue.getCode() + " : "
							+ e.getMessage() + " : " + Utilities.getStackTraceAsString(e));
				}
				throw new JaloSystemException(e);
			}
		}
		return null;
	}
}

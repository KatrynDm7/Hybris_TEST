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
package de.hybris.platform.bmecat.parser;

import java.util.Collection;
import java.util.Date;


/**
 * Object which holds the value of a parsed &lt;CATALOG&gt; tag
 * 
 * 
 */
public class Catalog extends Node
{
	private Collection<Agreement> agreements;
	private Company buyer;
	private String defaultCurrency;
	private Date generationDate;
	private String generatorInfo;
	private String id;
	private Boolean inclAssurance;
	private Boolean inclDuty;
	private Boolean inclFreight;
	private Boolean inclPacking;
	private String language;
	private String mimeRootDirectory;
	private String name;
	private Company supplier;
	private Collection<String> territories;
	private Collection<FeatureSystem> featureSystems;
	private String version;
	private int transactionMode;
	private Integer previousVersion;


	/**
	 * BMECat: HEADER.AGREMENT
	 * 
	 * @return Returns the agreements.
	 */
	public Collection<Agreement> getAgreements()
	{
		return agreements;
	}

	/**
	 * @param agreements
	 *           The agreements to set.
	 */
	public void setAgreements(final Collection<Agreement> agreements)
	{
		this.agreements = agreements;
	}

	/**
	 * BMECat: HEADER.BUYER
	 * 
	 * @return Returns the buyer.
	 */
	public Company getBuyer()
	{
		return buyer;
	}

	/**
	 * @param buyer
	 *           The buyer to set.
	 */
	public void setBuyer(final Company buyer)
	{
		this.buyer = buyer;
	}

	/**
	 * BMECat: CATALOG.CURRENCY
	 * 
	 * @return Returns the defaultCurrency.
	 */
	public String getDefaultCurrency()
	{
		return defaultCurrency;
	}

	/**
	 * @param defaultCurrency
	 *           The defaultCurrency to set.
	 */
	public void setDefaultCurrency(final String defaultCurrency)
	{
		this.defaultCurrency = defaultCurrency;
	}

	/**
	 * BMECat: CATALOG.DATETIME type="generation_date"
	 * 
	 * @return Returns the generationDate.
	 */
	public Date getGenerationDate()
	{
		return generationDate;
	}

	/**
	 * @param generationDate
	 *           The generationDate to set.
	 */
	public void setGenerationDate(final Date generationDate)
	{
		this.generationDate = generationDate;
	}

	/**
	 * BMECat: HEADER.GENERATOR_INFO
	 * 
	 * @return Returns the generatorInfo.
	 */
	public String getGeneratorInfo()
	{
		return generatorInfo;
	}

	/**
	 * @param generatorInfo
	 *           The generatorInfo to set.
	 */
	public void setGeneratorInfo(final String generatorInfo)
	{
		this.generatorInfo = generatorInfo;
	}

	/**
	 * BMECat: CATALOG.CATALOG_ID
	 * 
	 * @return Returns the id.
	 */
	public String getID()
	{
		return id;
	}

	/**
	 * @param id
	 *           The id to set.
	 */
	public void setID(final String id)
	{
		this.id = id;
	}

	/**
	 * BMECat: CATALOG.PRICE_FLAG type="incl_assurance"
	 * 
	 * @return Returns the inclAssurance.
	 */
	public Boolean getInclAssurance()
	{
		return inclAssurance;
	}

	/**
	 * @param inclAssurance
	 *           The inclAssurance to set.
	 */
	public void setInclAssurance(final Boolean inclAssurance)
	{
		this.inclAssurance = inclAssurance;
	}

	/**
	 * BMECat: CATALOG.PRICE_FLAG type="incl_duty"
	 * 
	 * @return Returns the inclDuty.
	 */
	public Boolean getInclDuty()
	{
		return inclDuty;
	}

	/**
	 * @param inclDuty
	 *           The inclDuty to set.
	 */
	public void setInclDuty(final Boolean inclDuty)
	{
		this.inclDuty = inclDuty;
	}

	/**
	 * BMECat: CATALOG.PRICE_FLAG type="incl_freight"
	 * 
	 * @return Returns the inclFreight.
	 */
	public Boolean getInclFreight()
	{
		return inclFreight;
	}

	/**
	 * @param inclFreight
	 *           The inclFreight to set.
	 */
	public void setInclFreight(final Boolean inclFreight)
	{
		this.inclFreight = inclFreight;
	}

	/**
	 * BMECat: CATALOG.PRICE_FLAG type="incl_packing"
	 * 
	 * @return Returns the inclPacking.
	 */
	public Boolean getInclPacking()
	{
		return inclPacking;
	}

	/**
	 * @param inclPacking
	 *           The inclPacking to set.
	 */
	public void setInclPacking(final Boolean inclPacking)
	{
		this.inclPacking = inclPacking;
	}

	/**
	 * BMECat: CATALOG.LANGUAGE
	 * 
	 * @return Returns the language.
	 */
	public String getLanguage()
	{
		return language;
	}

	/**
	 * @param language
	 *           The languages to set.
	 */
	public void setLanguage(final String language)
	{
		this.language = language;
	}

	/**
	 * BMECat: CATALOG.MIME_ROOT
	 * 
	 * @return Returns the mimeRootDirectory.
	 */
	public String getMimeRootDirectory()
	{
		return mimeRootDirectory;
	}

	/**
	 * @param mimeRootDirectory
	 *           The mimeRootDirectory to set.
	 */
	public void setMimeRootDirectory(final String mimeRootDirectory)
	{
		this.mimeRootDirectory = mimeRootDirectory;
	}

	/**
	 * BMECat: CATALOG.CATALOG_NAME
	 * 
	 * @return Returns the name.
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name
	 *           The name to set.
	 */
	public void setName(final String name)
	{
		this.name = name;
	}

	/**
	 * BMECat: HEADER.SUPPLIER
	 * 
	 * @return Returns the supplier.
	 */
	public Company getSupplier()
	{
		return supplier;
	}

	/**
	 * @param supplier
	 *           The supplier to set.
	 */
	public void setSupplier(final Company supplier)
	{
		this.supplier = supplier;
	}

	/**
	 * BMECat: CATALOG.TERRITORY format: country-region Example: if the territory is "DE-BY", that means that the country
	 * code "DE" is for Germany, and the region code "BY" is for Bayern.
	 * 
	 * @return Returns the territories.
	 */
	public Collection<String> getTerritories()
	{
		return territories;
	}

	/**
	 * @param territories
	 *           The territories to set.
	 */
	public void setTerritories(final Collection<String> territories)
	{
		this.territories = territories;
	}

	/**
	 * @return Returns the featureSystems.
	 */
	public Collection<FeatureSystem> getFeatureSystems()
	{
		return featureSystems;
	}

	/**
	 * @param featureSystems
	 *           The featureSystems to set.
	 */
	public void setFeatureSystems(final Collection<FeatureSystem> featureSystems)
	{
		this.featureSystems = featureSystems;
	}

	/**
	 * BMECat: CATALOG.CATALOG_VERSION
	 * 
	 * @return Returns the version.
	 */
	public String getVersion()
	{
		return version;
	}

	/**
	 * @param version
	 *           The version to set.
	 */
	public void setVersion(final String version)
	{
		this.version = version;
	}

	/**
	 * @return Returns the transactionMode.
	 */
	public int getTransactionMode()
	{
		return transactionMode;
	}

	/**
	 * @param transactionMode
	 *           The transactionMode to set.
	 */
	public void setTransactionMode(final int transactionMode)
	{
		this.transactionMode = transactionMode;
	}

	/**
	 * @return Returns the previousVersion.
	 */
	public Integer getPreviousVersion()
	{
		return previousVersion;
	}

	/**
	 * @param previousVersion
	 *           The previousVersion to set.
	 */
	public void setPreviousVersion(final Integer previousVersion)
	{
		this.previousVersion = previousVersion;
	}
}

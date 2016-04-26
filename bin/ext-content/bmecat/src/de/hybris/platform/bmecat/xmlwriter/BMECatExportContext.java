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
package de.hybris.platform.bmecat.xmlwriter;

import de.hybris.platform.bmecat.xmlwriter.BMECatWriter.RangedItemProvider;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.catalog.jalo.Company;
import de.hybris.platform.catalog.jalo.classification.ClassificationSystemVersion;
import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.user.Customer;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * BMECatExportContext
 * 
 * 
 */
public class BMECatExportContext
{
	//readonly

	private final BMECatWriter parent;

	//writeable
	private Product product;
	private ClassificationSystemVersion classSysVersion;
	private Category cat;
	private RangedItemProvider productsProvider = null;
	private RangedItemProvider categoriesProvider = null;
	private Map stringPurpose2MediaCollectionMap;
	private boolean errorsOccuredFlag = false;

	private Company company;

	private final HashSet exportMedias = new HashSet();

	private Set skippedProductIDs;
	private Set skippedCategoryIDs;

	/**
	 * 
	 */
	public BMECatExportContext(final BMECatWriter writer)
	{
		this.parent = writer;
	}

	public BMECatWriter getWriter()
	{
		return this.parent;
	}

	public String getClassificationNumberForamt()
	{
		return parent.getClassificationNumberFormat();
	}

	public void addSkippedProduct(final Product product)
	{
		if (skippedProductIDs == null)
		{
			skippedProductIDs = new HashSet();
		}
		skippedProductIDs.add(product.getCode().intern());
	}

	public boolean isSkippedProduct(final Product product)
	{
		return skippedProductIDs != null && skippedProductIDs.contains(product.getCode());
	}

	public void addSkippedCategory(final Category category)
	{
		if (skippedCategoryIDs == null)
		{
			skippedCategoryIDs = new HashSet();
		}
		skippedCategoryIDs.add(category.getCode().intern());
	}

	public boolean isSkippedCategory(final Category category)
	{
		return skippedCategoryIDs != null && skippedCategoryIDs.contains(category.getCode());
	}

	public Currency getExportCurrency()
	{
		return this.parent.getExportCurrency();
	}

	public CatalogVersion getCatVersion()
	{
		return this.parent.getCatVersion();
	}

	public Collection getClassificationSystemVersions()
	{
		return this.parent.getClassificationSystemVersions();
	}

	public EnumerationValue getTransactionMode()
	{
		return this.parent.getTransactionMode();
	}

	public Product getProduct()
	{
		return product;
	}

	public void setProduct(final Product product)
	{
		this.product = product;
	}

	/**
	 * @param classSysVersion
	 */
	public void setClassificationSystemVersion(final ClassificationSystemVersion classSysVersion)
	{
		this.classSysVersion = classSysVersion;
	}

	public ClassificationSystemVersion getClassificationSystemVersion()
	{
		return classSysVersion;
	}

	public void setCategory(final Category cat)
	{
		this.cat = cat;
	}

	public Category getCategory()
	{
		return cat;
	}

	/**
	 * @return Returns the categoriesProvider.
	 */
	public RangedItemProvider getCategoriesProvider()
	{
		return categoriesProvider;
	}

	/**
	 * @param categoriesProvider
	 *           The categoriesProvider to set.
	 */
	public void setCategoriesProvider(final RangedItemProvider categoriesProvider)
	{
		this.categoriesProvider = categoriesProvider;
	}

	/**
	 * @return Returns the productsProvider.
	 */
	public RangedItemProvider getProductsProvider()
	{
		return productsProvider;
	}

	/**
	 * @param productsProvider
	 *           The productsProvider to set.
	 */
	public void setProductsProvider(final RangedItemProvider productsProvider)
	{
		this.productsProvider = productsProvider;
	}

	/**
	 * @return Returns the hybris2bmecatMediaMap.
	 */
	public Map getHybris2BmecatMediaMap()
	{
		return this.parent.getHybris2bmecatMediaMap();
	}

	/**
	 * @return Returns the exportMedias.
	 */
	public HashSet getExportMedias()
	{
		return exportMedias;
	}

	public void addToExportMedias(final Media media)
	{
		if (!exportMedias.contains(media))
		{
			exportMedias.add(media);
		}
	}

	/**
	 * @return Returns the stringPurpose2MediaCollectionMap.
	 */
	public Map getStringPurpose2MediaCollectionMap()
	{
		return stringPurpose2MediaCollectionMap;
	}


	/**
	 * @param stringPurpose2MediaCollectionMap
	 *           The stringPurpose2MediaCollectionMap to set.
	 */
	public void setStringPurpose2MediaCollectionMap(final Map stringPurpose2MediaCollectionMap)
	{
		this.stringPurpose2MediaCollectionMap = stringPurpose2MediaCollectionMap;
	}


	/**
	 * @return Returns the company.
	 */
	public Company getCompany()
	{
		return company;
	}


	/**
	 * @param company
	 *           The company to set.
	 */
	public void setCompany(final Company company)
	{
		this.company = company;
	}

	/**
	 * @return Returns the userPriceGroups2BMECatPriceTypesMap.
	 */
	public Map getPriceMapping()
	{
		return this.parent.getPriceMapping();
	}


	/**
	 * @return Returns the priceDate.
	 */
	public Date getReferenceDate()
	{
		return this.parent.getReferenceDate();
	}

	/**
	 * @return Returns the reference customer.
	 */
	public Customer getReferenceCustomer()
	{
		return this.parent.getReferenceCustomer();
	}


	/**
	 * @return Returns the udpNet.
	 */
	public boolean isUdpNet()
	{
		return this.parent.isUDPNet();
	}

	public boolean errorsOccured()
	{
		return this.errorsOccuredFlag;
	}

	public void notifyError()
	{
		this.errorsOccuredFlag = true;
	}

	public boolean ignoreErrors()
	{
		return this.parent.ignoreErrors();
	}

	public boolean suppressProductsWithoutPrices()
	{
		return this.parent.suppressProductsWithoutPrices();
	}

	public boolean suppressEmptyCategories()
	{
		return this.parent.suppressEmptyCategories();
	}

}

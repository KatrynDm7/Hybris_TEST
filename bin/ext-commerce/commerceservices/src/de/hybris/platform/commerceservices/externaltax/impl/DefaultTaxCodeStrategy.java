/*
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
 *  
 */
package de.hybris.platform.commerceservices.externaltax.impl;

import de.hybris.platform.basecommerce.model.externaltax.ProductTaxCodeModel;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.commerceservices.externaltax.TaxAreaLookupStrategy;
import de.hybris.platform.commerceservices.externaltax.TaxCodeStrategy;
import de.hybris.platform.commerceservices.util.CommerceCatalogUtils;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.externaltax.ProductTaxCodeService;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.variants.model.VariantProductModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


public class DefaultTaxCodeStrategy implements TaxCodeStrategy
{
	private static final Logger LOG = Logger.getLogger(DefaultTaxCodeStrategy.class);

	public static final String FALLBACK_TAX_CODE_PROPERTY = "externaltax.fallbacktaxcode";

	private ProductTaxCodeService productTaxCodeService;
	private TaxAreaLookupStrategy taxAreaLookupStrategy;
	private ProductService productService;
	private CatalogVersionService catalogVersionService;
	private BaseSiteService baseSiteService;
	private ConfigurationService configurationService;


	@Override
	public String getTaxCodeForCodeAndOrder(final String code, final AbstractOrderModel order)
	{
		if (isExternalTaxEnabled(order))
		{
			final String taxAreaForOrder = getTaxAreaLookupStrategy().getTaxAreaForOrder(order);
			final ProductModel product = findProduct(code);
			if (isVariantProduct(product))
			{
				return getTaxCodeForVariantProduct((VariantProductModel) product, taxAreaForOrder);
			}
			else
			{
				return getTaxCodeForBaseProduct(code, taxAreaForOrder);
			}
		}
		else
		{
			return "N/A";
		}
	}

	protected boolean isExternalTaxEnabled(final AbstractOrderModel order)
	{
		return (order != null && order.getStore() != null && Boolean.TRUE.equals(order.getStore().getExternalTaxEnabled()));
	}

	protected ProductModel findProduct(final String productCode)
	{
		ProductModel product = null;
		try
		{
			product = getProductService().getProductForCode(
					CommerceCatalogUtils.getActiveProductCatalogVersion(getCatalogVersionService().getSessionCatalogVersions()),
					productCode);
		}
		catch (final UnknownIdentifierException e)
		{
			//if we cannot find product we still can try to find tax base only on code in getTaxCodeForBaseProduct
		}
		return product;
	}

	protected boolean isVariantProduct(final ProductModel product)
	{
		return (product instanceof VariantProductModel);
	}

	protected String getTaxCodeForBaseProduct(final String productCode, final String taxAreaCode)
	{
		final ProductTaxCodeModel taxCodeModel = getProductTaxCodeService().getTaxCodeForProductAndArea(productCode, taxAreaCode);
		if (taxCodeModel == null)
		{
			return returnFallbackTaxCode(productCode, taxAreaCode);
		}

		return taxCodeModel.getTaxCode();
	}

	protected String getTaxCodeForVariantProduct(final VariantProductModel variantProduct, final String taxAreaCode)
	{
		final Collection<String> productCodes = getProductHierarchyCodes(variantProduct);
		final Map<String, String> taxMap = getProductTaxCodeService().lookupTaxCodes(productCodes, taxAreaCode);
		String taxCode = null;
		for (final String productCode : productCodes)
		{
			taxCode = taxMap.get(productCode);
			if (taxCode != null)
			{
				return taxCode;
			}
		}

		return returnFallbackTaxCode(variantProduct.getCode(), taxAreaCode);
	}

	protected Collection<String> getProductHierarchyCodes(final VariantProductModel variantProduct)
	{
		final Collection<String> productCodeList = new ArrayList<String>();
		productCodeList.add(variantProduct.getCode());
		ProductModel baseProduct = variantProduct.getBaseProduct();
		while (baseProduct != null)
		{
			productCodeList.add(baseProduct.getCode());
			if (baseProduct instanceof VariantProductModel)
			{
				baseProduct = ((VariantProductModel) baseProduct).getBaseProduct();
			}
			else
			{
				baseProduct = null;
			}
		}
		return productCodeList;
	}

	protected String returnFallbackTaxCode(final String productCode, final String taxAreaCode)
	{
		final String fallbackTaxCode = getPropertyForBaseSite(FALLBACK_TAX_CODE_PROPERTY);
		if (fallbackTaxCode != null && !fallbackTaxCode.isEmpty())
		{
			LOG.error("Could not find taxCode for code " + productCode + " and taxArea " + taxAreaCode
					+ " Fallback tax code will be used : " + fallbackTaxCode);
			return fallbackTaxCode;
		}

		throw new IllegalArgumentException(String.format("Could not find taxCode for code %s and taxArea %s ", productCode,
				taxAreaCode));
	}

	protected String getPropertyForBaseSite(final String property)
	{
		final Configuration configuration = getConfigurationService().getConfiguration();
		final BaseSiteModel currentBaseSite = getBaseSiteService().getCurrentBaseSite();

		if (configuration == null)
		{
			return null;
		}

		if (currentBaseSite != null)
		{
			final String currentBaseSiteUid = currentBaseSite.getUid();
			final String key = property + "." + currentBaseSiteUid;
			final String propertyValue = configuration.getString(key, null);
			if (propertyValue != null)
			{
				return propertyValue;
			}
		}

		return configuration.getString(property, null);
	}

	protected ProductTaxCodeService getProductTaxCodeService()
	{
		return productTaxCodeService;
	}

	@Required
	public void setProductTaxCodeService(final ProductTaxCodeService productTaxCodeService)
	{
		this.productTaxCodeService = productTaxCodeService;
	}

	protected TaxAreaLookupStrategy getTaxAreaLookupStrategy()
	{
		return taxAreaLookupStrategy;
	}

	@Required
	public void setTaxAreaLookupStrategy(final TaxAreaLookupStrategy taxAreaLookupStrategy)
	{
		this.taxAreaLookupStrategy = taxAreaLookupStrategy;
	}

	protected ProductService getProductService()
	{
		return productService;
	}

	@Required
	public void setProductService(final ProductService productService)
	{
		this.productService = productService;
	}

	protected CatalogVersionService getCatalogVersionService()
	{
		return catalogVersionService;
	}

	@Required
	public void setCatalogVersionService(final CatalogVersionService catalogVersionService)
	{
		this.catalogVersionService = catalogVersionService;
	}

	protected BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	@Required
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}

	protected ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	@Required
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}
}

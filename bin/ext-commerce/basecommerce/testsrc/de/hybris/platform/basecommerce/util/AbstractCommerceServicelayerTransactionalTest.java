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
package de.hybris.platform.basecommerce.util;

import de.hybris.platform.variants.model.GenericVariantProductModel;
import de.hybris.platform.variants.model.VariantCategoryModel;
import de.hybris.platform.variants.model.VariantValueCategoryModel;
import de.hybris.platform.catalog.CatalogService;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.product.UnitService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.variants.model.VariantProductModel;
import de.hybris.platform.variants.model.VariantTypeModel;

import java.util.Collection;

import javax.annotation.Resource;

import org.junit.Assert;

import com.google.common.collect.Lists;


/**
 *
 */
public abstract class AbstractCommerceServicelayerTransactionalTest extends ServicelayerTransactionalTest
{
	@Resource
	protected ModelService modelService;
	@Resource
	protected CatalogService catalogService;
	@Resource
	protected CatalogVersionService catalogVersionService;
	@Resource
	protected UnitService unitService;
	@Resource
	protected CategoryService categoryService;
	@Resource
	protected ProductService productService;
	@Resource
	protected TypeService typeService;

	private final String defaultCatalogId = "testCatalog";
	private final String defaultCatalogVersion = "Staged";


	public void save(final Object modelToSave)
	{
		modelService.save(modelToSave);
	}

	/**
	 * @param code
	 * @param catalogVersion
	 * @param variantTypeCode
	 *           The type code of the variant, usually it can be retrieved from the variant class it self. E.g. if your
	 *           variant type is VariantProduct its type code can be retrieved by calling
	 *           {@link de.hybris.platform.variants.model.VariantProductModel#_TYPECODE}
	 * @param unit
	 *           Unit that this product is measured in
	 * @param superCategories
	 * @return created product
	 */
	public ProductModel createProduct(final String code, final CatalogVersionModel catalogVersion, final String variantTypeCode,
			final UnitModel unit, final CategoryModel... superCategories)
	{
		ProductModel product = null;
		try
		{
			product = productService.getProductForCode(catalogVersion, code);
		}
		catch (final UnknownIdentifierException e)
		{
			// no product, we will create a new one
		}

		if (product == null)
		{
			product = new ProductModel();
			product.setCatalogVersion(catalogVersion);
			product.setCode(code);
		}

		product.setUnit(unit);
		product.setVariantType(createVariantType(variantTypeCode));
		product.setSupercategories(Lists.<CategoryModel> newArrayList(superCategories));

		modelService.save(product);
		return product;
	}

	public CatalogVersionModel createCatalogVersion(final String catalogId, final String catalogVersion)
	{
		CatalogVersionModel version = null;
		try
		{
			version = catalogVersionService.getCatalogVersion(catalogId, catalogVersion);
		}
		catch (final Exception e)
		{
			// nothing found new one will be created
		}
		if (version == null)
		{
			version = new CatalogVersionModel();
			version.setCatalog(createCatalog(catalogId));
			version.setVersion(catalogVersion);
			modelService.save(version);
		}

		return version;
	}

	public CatalogModel createCatalog(final String catalogId)
	{
		CatalogModel catalog = null;
		try
		{
			catalog = catalogService.getCatalogForId(catalogId);
		}
		catch (final Exception e)
		{
			// nothing found new one will be created
		}
		if (catalog == null)
		{
			catalog = new CatalogModel();
			catalog.setId(catalogId);
			modelService.save(catalog);
		}
		return catalog;
	}

	public String getDefaultCatalogId()
	{
		return defaultCatalogId;
	}

	public String getDefaultCatalogVersion()
	{
		return defaultCatalogVersion;
	}

	public CatalogVersionModel createDefaultCatalogVersion()
	{
		Assert.assertNotNull("Please setup a default catalog version, by calling the setter on the service",
				getDefaultCatalogVersion());
		Assert.assertNotNull("Please setup a default catalog id, by calling the setter on the service", getDefaultCatalogId());
		return createCatalogVersion(getDefaultCatalogId(), getDefaultCatalogVersion());
	}

	public CategoryModel createCategory(final String code, final CatalogVersionModel catalogVersion)
	{
		CategoryModel category = null;
		try
		{
			category = categoryService.getCategoryForCode(code);
		}
		catch (final Exception e)
		{
			// nothing found new one will be created
		}
		if (category == null)
		{
			category = new CategoryModel();
			category.setCode(code);
			category.setCatalogVersion(catalogVersion);
			modelService.save(category);
		}
		return category;
	}

	public UnitModel createUnit(final String code)
	{
		UnitModel unit = null;
		try
		{
			unit = unitService.getUnitForCode(code);
		}
		catch (final Exception e)
		{
			// nothing found create a new one
		}
		if (unit == null)
		{
			unit = new UnitModel();
			unit.setCode(code);
			unit.setUnitType("<autogenerated unit type>");
			unit.setConversion(new Double(1.0));
			modelService.save(unit);
		}
		return unit;
	}

	public VariantCategoryModel createVariantCategory(final String code, final CatalogVersionModel catalogVersion)
	{
		VariantCategoryModel category = null;
		try
		{
			category = (VariantCategoryModel) categoryService.getCategoryForCode(code);
		}
		catch (final Exception e)
		{
			// nothing found new one will be created
		}
		if (category == null)
		{
			category = new VariantCategoryModel();
			category.setCode(code);
			category.setCatalogVersion(catalogVersion);
			modelService.save(category);
		}
		return category;
	}

	public VariantValueCategoryModel createVariantValueCategory(final String code, final VariantCategoryModel parentCategory,
			final int sequenceNumber, final CatalogVersionModel catalogVersion)
	{
		VariantValueCategoryModel category = null;
		try
		{
			category = (VariantValueCategoryModel) categoryService.getCategoryForCode(code);
		}
		catch (final Exception e)
		{
			// nothing found new one will be created
		}
		if (category == null)
		{
			category = new VariantValueCategoryModel();
			category.setCode(code);
		}
		category.setCatalogVersion(catalogVersion);
		category.setSupercategories(Lists.<CategoryModel> newArrayList(parentCategory));
		category.setSequence(Integer.valueOf(sequenceNumber));
		save(category);
		save(parentCategory);
		return category;
	}

	public GenericVariantProductModel createGenericVariantProduct(final String code, final ProductModel baseProduct,
			final CatalogVersionModel catalogVersion, final VariantValueCategoryModel... variantSuperCategories)
	{
		final GenericVariantProductModel variant = new GenericVariantProductModel();
		variant.setCode(code);
		variant.setBaseProduct(baseProduct);
		variant.setCatalogVersion(catalogVersion);
		variant.setSupercategories((Collection) Lists.<VariantValueCategoryModel> newArrayList(variantSuperCategories));
		save(variant);
		return variant;
	}

	public VariantProductModel createVariantProduct(final String code, final ProductModel baseProduct,
			final CatalogVersionModel catalogVersion, final VariantTypeModel variantType)
	{
		final VariantProductModel product = new VariantProductModel();
		product.setCatalogVersion(createDefaultCatalogVersion());
		product.setCode(code);
		product.setVariantType(variantType);
		save(product);
		return product;
	}

	public VariantTypeModel createVariantType(final String variantTypeCode)
	{
		return (VariantTypeModel) typeService.getComposedTypeForCode(variantTypeCode);
	}

}

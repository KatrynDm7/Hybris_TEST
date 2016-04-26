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
package de.hybris.platform.bmecat.jalo.bmecat2csv;

import de.hybris.platform.catalog.constants.CatalogConstants;
import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.jalo.classification.ClassAttributeAssignment;
import de.hybris.platform.catalog.jalo.classification.ClassificationAttribute;
import de.hybris.platform.catalog.jalo.classification.ClassificationClass;
import de.hybris.platform.catalog.jalo.classification.ClassificationSystem;
import de.hybris.platform.catalog.jalo.classification.ClassificationSystemVersion;
import de.hybris.platform.catalog.jalo.classification.impex.ClassificationAttributeTranslator;
import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.category.jalo.CategoryManager;
import de.hybris.platform.impex.constants.ImpExConstants;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.header.HeaderValidationException;
import de.hybris.platform.impex.jalo.header.SpecialColumnDescriptor;
import de.hybris.platform.impex.jalo.header.StandardColumnDescriptor;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.TypeManager;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;


public class BMECatClassificationAttributeTranslator extends ClassificationAttributeTranslator
{
	private static final Logger LOG = Logger.getLogger(BMECatClassificationAttributeTranslator.class);
	protected Product product;
	private final String LANGUAGE_ISO = "lang_iso";
	private final String CLASS_SYSTEM_VERSIONS = "class_system_versions";
	private final String CLASSIFICATION_CLASS = "classificationClass";
	private String languageIso;
	private String classSystemVersions;
	private ClassificationClass classClass;
	private final Set<String> classSystemVersionPKs = new HashSet<String>();

	@Override
	public void init(final SpecialColumnDescriptor columnDescriptor) throws HeaderValidationException
	{
		// check if translator is used for product header at all
		if (!TypeManager.getInstance().getComposedType(Product.class)
				.isAssignableFrom(columnDescriptor.getHeader().getConfiguredComposedType()))
		{
			throw new HeaderValidationException(columnDescriptor.getHeader(), "invalid header type "
					+ columnDescriptor.getHeader().getConfiguredComposedType().getCode()
					+ " for ClassificationAttributeTranslator in column " + columnDescriptor.getValuePosition() + ":"
					+ columnDescriptor.getQualifier() + " - expected Product or any of its subtypes",
					HeaderValidationException.UNKNOWN);
		}
		if (columnDescriptor.getDescriptorData().getModifier(ImpExConstants.Syntax.Modifier.LANGUAGE) != null)
		{
			this.lang = StandardColumnDescriptor.findLanguage(columnDescriptor.getHeader(), columnDescriptor.getDescriptorData()
					.getModifier(ImpExConstants.Syntax.Modifier.LANGUAGE));
		}

		this.qualfier = columnDescriptor.getQualifier();
		// remove trailing @
		if (this.qualfier.startsWith(ImpExConstants.Syntax.SPECIAL_COLUMN_PREFIX))
		{
			this.qualfier = this.qualfier.substring(ImpExConstants.Syntax.SPECIAL_COLUMN_PREFIX.length()).trim();
		}

		this.languageIso = columnDescriptor.getDescriptorData().getModifier(this.LANGUAGE_ISO);
		this.classSystemVersions = columnDescriptor.getDescriptorData().getModifier(this.CLASS_SYSTEM_VERSIONS);
		//parsing classSystemVersions, separating its name and version, 
		//for example: "eclass-4.1.80c", its name is "eclass" and its version is "4.1.80c"
		final String[] systemVersions = this.classSystemVersions.split(",");
		final Iterator<String> iterator = Arrays.asList(systemVersions).iterator();
		while (iterator.hasNext())
		{
			final String version = iterator.next();
			final int minusPos = version.indexOf('-');
			if (minusPos == -1)
			{
				if ("$class_system_versions".equals(version))
				{
					continue;
				}
				else
				{
					this.systemName = version;
					this.versionName = null;
				}
			}
			else
			{
				this.systemName = version.substring(0, minusPos);
				this.versionName = version.substring(minusPos + 1);
			}

			final ClassificationSystem sys = CatalogManager.getInstance().getClassificationSystem(this.systemName);
			if (sys == null)
			{
				throw new HeaderValidationException(columnDescriptor.getHeader(), "unknown classification system '" + this.systemName
						+ "' in column " + columnDescriptor.getValuePosition() + ":" + columnDescriptor.getQualifier(),
						HeaderValidationException.UNKNOWN);
			}
			if (this.versionName == null)
			{
				this.classSystemVersion = (ClassificationSystemVersion) sys.getCatalogVersions().iterator().next();
				if (classSystemVersion != null)
				{
					this.versionName = this.classSystemVersion.getVersion();
				}
			}
			else
			{
				this.classSystemVersion = (ClassificationSystemVersion) sys.getCatalogVersion(this.versionName);
			}
			if (this.classSystemVersion == null)
			{
				throw new HeaderValidationException(columnDescriptor.getHeader(), "unknown classification system version '"
						+ this.systemName + "." + this.versionName + "' in column " + columnDescriptor.getValuePosition() + ":"
						+ columnDescriptor.getQualifier(), HeaderValidationException.UNKNOWN);
			}
			this.classSystemVersionPKs.add(this.classSystemVersion.getPK().toString());

			if (classSystemVersion != null && this.classClass == null)
			{
				final String classString = columnDescriptor.getDescriptorData().getModifier(this.CLASSIFICATION_CLASS);
				if (classString != null)
				{
					this.classClass = CatalogManager.getInstance().getClassificationClass(
							classSystemVersion.getClassificationSystem().getId(), classSystemVersion.getVersion(), classString);
					if (this.classClass == null)
					{
						LOG.warn("unknown classification class '" + this.systemName + "." + this.versionName + "." + classString
								+ "' in column " + columnDescriptor.getValuePosition() + ":" + columnDescriptor.getQualifier()
								+ " will try searching at all classes");
					}
				}
			}
		}

		final String customDelimiter = columnDescriptor.getDescriptorData().getModifier(
				ImpExConstants.Syntax.Modifier.COLLECTION_VALUE_DELIMITER);
		if (customDelimiter != null && customDelimiter.length() > 0)
		{
			this.collectionDelimiter = customDelimiter.charAt(0);
			this.TO_ESCAPE = new char[]
			{ this.collectionDelimiter };
		}
	}

	@Override
	public void performImport(final String cellValue, final Item processedItem) throws ImpExException
	{
		super.performImport(cellValue, processedItem);
		this.product = (Product) processedItem;
		this.classAttr = getClassificationAttribute(this.qualfier);
	}

	private ClassificationAttribute getClassificationAttribute(final String name) throws JaloItemNotFoundException
	{
		if (name == null)
		{
			throw new NullPointerException("feature name is null");
		}
		if (this.classSystemVersionPKs.size() == 0)
		{
			findNoClassVersionForFeature(name);
			return null;
		}
		final Iterator<Category> categoryIter = CategoryManager.getInstance().getSupercategories(this.product).iterator();
		final StringBuilder codes = new StringBuilder();
		while (categoryIter.hasNext())
		{
			final Category category = categoryIter.next();

			try
			{
				final Iterator<ClassificationAttribute> attributes = ((List<ClassificationAttribute>) category
						.getAttribute("classificationattributes")).iterator();
				while (attributes.hasNext())
				{
					codes.append("'" + attributes.next().getCode() + "',");
				}
			}
			catch (final JaloSystemException e)
			{
				//it is all right, the category is not defined in a classification system
			}
			catch (final Exception e)
			{
				e.printStackTrace();
			}
		}
		if (codes.length() > 0)
		{
			codes.deleteCharAt(codes.length() - 1);
		}
		else
		{
			LOG.error("No Classification Attribute found at categories of product " + this.product.getCode());
			return null;
		}

		final Map<String, String> values = new HashMap<String, String>();
		values.put("name", name);
		final StringBuilder pks = new StringBuilder();
		final Iterator<String> itPKs = this.classSystemVersionPKs.iterator();
		while (itPKs.hasNext())
		{
			pks.append("'" + itPKs.next() + "',");
		}
		final int length = pks.length();
		if (length == 0)
		{
			findNoClassVersionForFeature(name);
			return null;
		}
		else
		{
			pks.deleteCharAt(length - 1);
		}

		final String query = "SELECT {" + Item.PK + "} FROM {" + CatalogConstants.TC.CLASSIFICATIONATTRIBUTE + "} " + " WHERE {"
				+ ClassificationAttribute.NAME + "[" + this.languageIso + "]}=?name AND {" + ClassificationAttribute.SYSTEMVERSION
				+ "} in (" + pks.toString() + ") AND {" + ClassificationAttribute.CODE + "} in (" + codes.toString() + ")";

		final List res = FlexibleSearch.getInstance()
				.search(query, values, Collections.singletonList(ClassificationAttribute.class), true, // fail on unknown fields
						true, // don't need total
						0, -1 // range
				).getResult();

		switch (res.size())
		{
			case 0:
				LOG.error("no ClassificationAttribute [" + name + "] found in " + this.classSystemVersion.getFullVersionName()
						+ " using query " + query + " and values " + values);
				return null;
			case 1:
				return (ClassificationAttribute) res.get(0);
			default:
				LOG.error("more ClassificationAttributes for [" + name + "] found in " + this.classSystemVersion.getFullVersionName());
				return null;
		}
	}

	@Override
	protected ClassAttributeAssignment matchAssignment(final Collection<ClassificationClass> classes)
	{
		if (this.classClass != null)
		{
			return super.matchAssignment(Collections.singletonList(classClass));
		}
		else
		{
			return super.matchAssignment(classes);
		}
	}

	private void findNoClassVersionForFeature(final String featureName)
	{
		//no classification version is found for the feature, so the user should know it
		LOG.error("no Classification System version for feature [" + featureName + "] of product [" + this.product.getCode()
				+ "] found.");
	}
}

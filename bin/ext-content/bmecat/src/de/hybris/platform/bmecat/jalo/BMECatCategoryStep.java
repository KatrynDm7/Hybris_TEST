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
import de.hybris.platform.bmecat.parser.Article;
import de.hybris.platform.bmecat.parser.BMECatParser;
import de.hybris.platform.bmecat.parser.Catalog;
import de.hybris.platform.bmecat.parser.CatalogGroupSystem;
import de.hybris.platform.bmecat.parser.CatalogStructure;
import de.hybris.platform.catalog.constants.CatalogConstants;
import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.cronjob.jalo.AbortCronJobException;
import de.hybris.platform.cronjob.jalo.ChangeDescriptor;
import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationType;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloAbstractTypeException;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.util.Utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.LRUMap;


/**
 * Step imports catalog structure
 * 
 * 
 * 
 */
public class BMECatCategoryStep extends GeneratedBMECatCategoryStep
{
	//	enums for value mapping
	protected static EnumerationValue ID; //NOPMD
	protected static EnumerationValue ORDER;
	protected static EnumerationValue PARENT;
	protected static EnumerationValue DESCRIPTION;
	protected static EnumerationValue KEYWORDS;
	protected static EnumerationValue NAME;
	protected static EnumerationValue CATALOGVERSION;

	private Map attributeMapping;
	private Map categoryCache;

	private final static String PENDING_CATEGORY_MAP = "pendingCategoryMap";
	private final static String IMPORTED_CATEGORIES = "importedCategories";
	private final static String LAST_VALID_CATEGORY_ID = "lastValidCategoryID";
	private final static String NONE_CATEGORIES_IMPORTED = "noneCategoriesImported";

	//********************************************************************************
	// Item logic
	//********************************************************************************



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
		final BMECatCategoryStep newStep = (BMECatCategoryStep) item;

		newStep.initAttributeEnumValues();

		final ItemAttributeMap myMap = new ItemAttributeMap(nonInitialAttributes);
		ComposedType categoryType = (ComposedType) nonInitialAttributes.get(BMECatCategoryStep.CATEGORYTYPE);
		Map mapping = (Map) nonInitialAttributes.get(BMECatCategoryStep.CATEGORYATTRIBUTEMAPPING);
		if (categoryType == null)
		{
			if (mapping != null && !mapping.isEmpty())
			{
				throw new JaloInvalidParameterException(
						"Cannot define category attribute mapping without specifying the category type", 0);
			}
			else
			{
				categoryType = newStep.getSession().getTypeManager().getComposedType(Category.class);
				myMap.put(BMECatCategoryStep.CATEGORYTYPE, categoryType);
			}
		}
		if (mapping == null || mapping.isEmpty())
		{
			mapping = newStep.createDefaultMapping(categoryType);
			myMap.put(BMECatCategoryStep.CATEGORYATTRIBUTEMAPPING, mapping);
		}
		MappingUtils.checkAttributeMapping(Category.class, categoryType, mapping);
		super.setNonInitialAttributes(ctx, item, myMap);
	}

	//********************************************************************************
	// Import logic
	//********************************************************************************

	/**
	 * @see BMECatImportStep#initializeBMECatImport(Catalog, BMECatImportCronJob)
	 */
	@Override
	protected void initializeBMECatImport(final Catalog catalog, final BMECatImportCronJob cronJob)
	{
		initAttributeEnumValues();
		initAttributeMapping();
		categoryCache = new LRUMap();
		cronJob.resetCounter();
	}

	/**
	 * @see de.hybris.platform.cronjob.jalo.Step#performStep(de.hybris.platform.cronjob.jalo.CronJob)
	 */
	@Override
	protected void performStep(final CronJob forSchedule) throws AbortCronJobException
	{
		try
		{
			super.performStep(forSchedule);
		}
		finally
		{
			forSchedule.setTransientObject(IMPORTED_CATEGORIES, null);
			forSchedule.setTransientObject(PENDING_CATEGORY_MAP, null);
			forSchedule.setTransientObject(LAST_VALID_CATEGORY_ID, null);
		}
	}

	/**
	 * @see de.hybris.platform.bmecat.jalo.BMECatImportStep#importBMECatObject(Catalog, AbstractValueObject,
	 *      BMECatImportCronJob)
	 */
	@Override
	protected void importBMECatObject(final de.hybris.platform.bmecat.parser.Catalog catalogValueObject,
			final AbstractValueObject obj, final BMECatImportCronJob cronJob) throws ParseAbortException
	{
		final EnumerationValue taMode = cronJob.getTransactionMode();
		final String taCode = taMode != null ? taMode.getCode() : null;

		if (BMECatConstants.Enumerations.TransactionModeEnum.T_UPDATE_PRICES.equals(taCode)
				|| BMECatConstants.Enumerations.TransactionModeEnum.T_UPDATE_PRODUCTS.equals(taCode))
		{
			throw new ParseFinishedException("CategoryStep: skipped due to transaction mode " + taCode + " - nothing to do.");
		}

		if (obj instanceof CatalogGroupSystem && !(obj instanceof CatalogStructure))
		{
			final CatalogGroupSystem cgs = (CatalogGroupSystem) obj;
			//updates only the name / id of the catalog group system which are stored in CatalogVersion
			updateCatalogVersion(cronJob, cgs);
		}
		else if (obj instanceof CatalogStructure)
		{
			cronJob.addToCounter(1);
			final CatalogStructure catalogStructure = (CatalogStructure) obj;
			if (!cronJob.isLocalizationUpdateAsPrimitive())
			{

				//is is the root category or is the parent category already imported
				if ("0".equals(catalogStructure.getParentID())
						|| getImportedCategories(cronJob).containsKey(catalogStructure.getParentID()))
				{
					createCategories(cronJob, Collections.singletonList(catalogStructure));
				}
				else
				{
					getPendingCategoryMap(cronJob).put(catalogStructure.getParentID(), catalogStructure);
				}
			}
			else
			{
				final Category catalogCategory = getCatalogManager().getCatalogCategory(cronJob.getCatalogVersion(),
						catalogStructure.getID());
				updateCatalogCategory(cronJob, catalogCategory, catalogStructure);
			}
		}
		else if (obj instanceof Article)
		{
			throw new ParseFinishedException("CategoryStep: done.");
		}
		else if (obj instanceof Abort)
		{
			if (ABORTTYPE.equals(((Abort) obj).getType()))
			{
				throw new BMECatParser.TestParseAbortException("");
			}
		}
	}

	private final static String ABORTTYPE = "category";

	private boolean skip(final BMECatImportCronJob cronJob, final String catID)
	{
		final String lastValidCategoryCode = getLastValidCategoryCode(cronJob);
		final boolean skip;
		if (NONE_CATEGORIES_IMPORTED.equals(lastValidCategoryCode))
		{
			skip = false;
		}
		else if (catID.equals(lastValidCategoryCode))
		{
			skip = true;
			cronJob.setTransientObject(LAST_VALID_CATEGORY_ID, NONE_CATEGORIES_IMPORTED);
		}
		else
		{
			skip = true;
		}
		return skip;
	}

	private String getLastValidCategoryCode(final BMECatImportCronJob cronJob)
	{
		final String transientLastValidCategoryCode = (String) cronJob.getTransientObject(LAST_VALID_CATEGORY_ID);
		if (transientLastValidCategoryCode == null)
		{
			final ChangeDescriptor lastOne = getMostRecentChange(cronJob, BMECatConstants.ChangeTypes.CREATE_CATEGORY);
			final Category cat = lastOne != null ? (Category) lastOne.getChangedItem() : null;
			final String lastValidCategoryCode;
			if (cat != null)
			{
				lastValidCategoryCode = cat.getCode();
			}
			else
			{
				lastValidCategoryCode = NONE_CATEGORIES_IMPORTED;
			}
			cronJob.setTransientObject(LAST_VALID_CATEGORY_ID, lastValidCategoryCode);
			//   		log.info( "lastValidCategoryCode: " + lastValidCategoryCode );
			return lastValidCategoryCode;
		}
		return transientLastValidCategoryCode;
	}

	private void createCategories(final BMECatImportCronJob cronJob, final Collection categories)
	{
		if (categories == null || categories.isEmpty())
		{
			return;
		}

		for (final Iterator it = categories.iterator(); it.hasNext();)
		{
			final CatalogStructure catalogStructure = (CatalogStructure) it.next();
			final String id = catalogStructure.getID();

			if (!skip(cronJob, id))
			{
				final Category category = createCatalogCategory(getValues(cronJob, catalogStructure));
				addChange(cronJob, BMECatConstants.ChangeTypes.CREATE_CATEGORY, category, null);
				if (isInfoEnabled())
				{
					info("created category: " + category.getCode());
				}
				categoryCache.put(id, category);
			}
			else
			{
				if (isInfoEnabled())
				{
					info("skipped creation of category: " + id);
				}
			}

			getImportedCategories(cronJob).put(id, catalogStructure);

			//recursive call for pending categories
			createCategories(cronJob, (Collection) getPendingCategoryMap(cronJob).get(catalogStructure.getID()));
			getPendingCategoryMap(cronJob).remove(catalogStructure.getID());
		}
	}

	private PendingCategoryMap getPendingCategoryMap(final CronJob cronJob)
	{
		PendingCategoryMap map = (PendingCategoryMap) cronJob.getTransientObject(PENDING_CATEGORY_MAP);
		if (map == null)
		{
			map = new PendingCategoryMap();
			cronJob.setTransientObject(PENDING_CATEGORY_MAP, map);
		}
		return map;
	}

	private Map getImportedCategories(final CronJob cronJob)
	{
		Map categoryCache = (Map) cronJob.getTransientObject(IMPORTED_CATEGORIES);
		if (categoryCache == null)
		{
			categoryCache = new HashMap();
			cronJob.setTransientObject(IMPORTED_CATEGORIES, categoryCache);
		}
		return categoryCache;
	}

	@Override
	public int getCompletedCount(final BMECatImportCronJob cronJob)
	{
		return cronJob.getCounter();
	}

	@Override
	public int getTotalToComplete(final BMECatImportCronJob cronJob)
	{
		return cronJob.isCatalogInfoAvailableAsPrimitive() ? cronJob.getCategoryCountAsPrimitive() : -1;
	}


	/**
	 * @param forSchedule
	 * @return true since this step can remove its changes
	 * @see de.hybris.platform.cronjob.jalo.Step#canUndo(de.hybris.platform.cronjob.jalo.CronJob)
	 */
	@Override
	protected boolean canUndo(final CronJob forSchedule)
	{
		return true;
	}

	/**
	 * Removes all created categories.
	 * 
	 * @param forSchedule
	 *           the current running cronJob
	 * @see de.hybris.platform.cronjob.jalo.Step#undoStep(de.hybris.platform.cronjob.jalo.CronJob)
	 */
	@Override
	protected void undoStep(final CronJob forSchedule)
	{
		super.undoStep(forSchedule); // removes keywords

		int count = 0;

		/*
		 * fetch categories in ranges of 100 items since category amount may be large
		 */
		for (Collection changes = forSchedule.getChanges(this, BMECatConstants.ChangeTypes.CREATE_CATEGORY, 0, 100, false); changes != null
				&& !changes.isEmpty(); changes = forSchedule.getChanges(this, BMECatConstants.ChangeTypes.CREATE_CATEGORY, 0, 100,
				false))
		{
			count += changes.size();
			for (final Iterator it = changes.iterator(); it.hasNext();)
			{
				try
				{
					final ChangeDescriptor changeDescriptor = (ChangeDescriptor) it.next();
					final Category category = (Category) changeDescriptor.getChangedItem();
					if (category != null && category.isAlive())
					{
						category.remove();
					}
					changeDescriptor.remove();
				}
				catch (final ConsistencyCheckException e)
				{
					throw new JaloSystemException(e);
				}
			}
		}
		if (isInfoEnabled())
		{
			info("removed " + count + " categories");
		}
	}

	public void updateCatalogVersion(final BMECatImportCronJob cronJob, final CatalogGroupSystem cgsValueObject)
	{
		try
		{
			final Map values = new ItemAttributeMap();
			values.put(CatalogVersion.CATEGORYSYSTEMID, cgsValueObject.getID());
			values.put(CatalogVersion.CATEGORYSYSTEMNAME, cgsValueObject.getName());
			values.put(CatalogVersion.CATEGORYSYSTEMDESCRIPTION, cgsValueObject.getDescription());
			cronJob.getCatalogVersion().setAllAttributes(values);
		}
		catch (final Exception e)
		{
			if (isErrorEnabled())
			{
				error("Could not update CatalogVersion due to the following Exception: " + e.getMessage() + " : "
						+ Utilities.getStackTraceAsString(e));
			}
			throw new JaloSystemException(e);
		}
	}

	public void updateCatalogCategory(final BMECatImportCronJob cronJob, final Category category,
			final CatalogStructure csValueObject)
	{
		if (category == null)
		{
			if (isErrorEnabled())
			{
				error("Error updating CatalogCategory. Assigned category was <null>!");
			}
			return;
		}

		try
		{
			category.setAllAttributes(getValues(cronJob, csValueObject));
		}
		catch (final Exception e)
		{
			if (isErrorEnabled())
			{
				error("Error updating CatalogCategory. (" + e.getMessage() + ") " + Utilities.getStackTraceAsString(e));
			}
			throw new JaloSystemException(e);
		}
	}

	protected Category createCatalogCategory(final ItemAttributeMap values)
	{
		try
		{
			return (Category) getCategoryType().newInstance(values);
		}
		catch (final JaloGenericCreationException e)
		{
			throw new JaloSystemException(e);
		}
		catch (final JaloAbstractTypeException e)
		{
			throw new JaloSystemException(e);
		}
	}

	protected Category getCatalogCategory(final BMECatImportCronJob cronJob, final String code)
	{
		Category category = (Category) categoryCache.get(code);
		if (category == null)
		{
			category = getCatalogManager().getCatalogCategory(cronJob.getCatalogVersion(), code);
			if (category != null)
			{
				categoryCache.put(code, category);
			}
		}
		return category;
	}

	protected ItemAttributeMap getValues(final BMECatImportCronJob cronJob, final CatalogStructure csValueObject)
	{
		final ItemAttributeMap values = new ItemAttributeMap();
		if (!cronJob.isLocalizationUpdateAsPrimitive())
		{
			if (attributeMapping.containsKey(ID))
			{
				values.put(getAttributeQualifier(ID, attributeMapping), csValueObject.getID());
			}
			if (attributeMapping.containsKey(ORDER))
			{
				values.put(getAttributeQualifier(ORDER, attributeMapping), csValueObject.getOrder());
			}
			if (attributeMapping.containsKey(PARENT))
			{
				if (csValueObject.getParentID() != null && getCatalogCategory(cronJob, csValueObject.getParentID()) != null)
				{

					values.put(getAttributeQualifier(PARENT, attributeMapping),
							Collections.singletonList(getCatalogCategory(cronJob, csValueObject.getParentID())));
				}
			}
			if (attributeMapping.containsKey(CATALOGVERSION))
			{
				values.put(getAttributeQualifier(CATALOGVERSION, attributeMapping), cronJob.getCatalogVersion());
			}
		}

		if (attributeMapping.containsKey(NAME))
		{
			values.put(getAttributeQualifier(NAME, attributeMapping), csValueObject.getName());
		}
		if (attributeMapping.containsKey(DESCRIPTION))
		{
			values.put(getAttributeQualifier(DESCRIPTION, attributeMapping), csValueObject.getDescription());
		}
		if (attributeMapping.containsKey(KEYWORDS))
		{
			values.put(getAttributeQualifier(KEYWORDS, attributeMapping), getOrCreateKeywords(cronJob, csValueObject.getKeywords()));
		}
		return values;
	}

	//********************************************************************************
	// Attribute Mapping
	//********************************************************************************
	private synchronized void initAttributeEnumValues()
	{
		final EnumerationType categoryAttributesEnum = getSession().getEnumerationManager().getEnumerationType(
				BMECatConstants.TC.CATEGORYATTRIBUTEENUM);
		ID = getSession().getEnumerationManager().getEnumerationValue(categoryAttributesEnum, "ID");
		NAME = getSession().getEnumerationManager().getEnumerationValue(categoryAttributesEnum, "Name");
		DESCRIPTION = getSession().getEnumerationManager().getEnumerationValue(categoryAttributesEnum, "Description");
		ORDER = getSession().getEnumerationManager().getEnumerationValue(categoryAttributesEnum, "Order");
		PARENT = getSession().getEnumerationManager().getEnumerationValue(categoryAttributesEnum, "Parent");
		KEYWORDS = getSession().getEnumerationManager().getEnumerationValue(categoryAttributesEnum, "Keywords");
		CATALOGVERSION = getSession().getEnumerationManager().getEnumerationValue(categoryAttributesEnum, "CatalogVersion");
	}

	private synchronized void initAttributeMapping()
	{
		attributeMapping = getAllCategoryAttributeMapping();
	}

	/**
	 * Creates and returns the default category attribute mapping. The keys are EnumerationValues of the
	 * <code>CategoryAttributeEnum</code>-EnumerationType. Values are <code>AttributeDescriptor</code>s of the specified
	 * targetType.
	 * 
	 * @param targetType
	 *           the composed type which should be used for the category import
	 * @return the default category attribute mapping
	 */
	protected Map createDefaultMapping(final ComposedType targetType)
	{
		final Map attributeMapping = new HashMap();
		MappingUtils.addMapping(attributeMapping, targetType, ID, Category.CODE, this);
		MappingUtils.addMapping(attributeMapping, targetType, NAME, Category.NAME, this);
		MappingUtils.addMapping(attributeMapping, targetType, DESCRIPTION, CatalogConstants.Attributes.Category.DESCRIPTION, this);
		MappingUtils.addMapping(attributeMapping, targetType, KEYWORDS, CatalogConstants.Attributes.Category.KEYWORDS, this);
		MappingUtils.addMapping(attributeMapping, targetType, ORDER, CatalogConstants.Attributes.Category.ORDER, this);
		MappingUtils.addMapping(attributeMapping, targetType, PARENT, Category.SUPERCATEGORIES, this);
		MappingUtils.addMapping(attributeMapping, targetType, CATALOGVERSION, CatalogConstants.Attributes.Category.CATALOGVERSION,
				this);
		return attributeMapping;
	}

	private String getAttributeQualifier(final EnumerationValue eValue, final Map mapping)
	{
		return ((AttributeDescriptor) mapping.get(eValue)).getQualifier();
	}

	@Override
	protected void finalizeStep(final CronJob cronJob)
	{
		if (isInfoEnabled())
		{
			info("PROCESSING reordering of existing categories...");
		}

		final BMECatImportCronJob importCronJob = (BMECatImportCronJob) cronJob;
		final CatalogVersion version = importCronJob.getCatalogVersion();

		int start = 0;
		final int range = 100;
		Collection categories = null;

		do
		{
			categories = version.getAllCategories(start, range);
			start += range;
			for (final Iterator it = categories.iterator(); it.hasNext();)
			{
				final Category category = (Category) it.next();

				if (category.isRootAsPrimitive())
				{
					final List childs = Arrays.asList(category.getSubcategories().toArray());
					Collections.sort(childs, new CategoryComparator());
					category.setSubcategories(childs);
				}
			}
		}
		while (categories.size() == range);

		if (isInfoEnabled())
		{
			info("reordering of existing categories FINSIHED");
		}
	}

	//********************************************************************************
	// Utility map class
	//********************************************************************************
	class PendingCategoryMap extends HashMap
	{
		@Override
		public Object put(final Object key, final Object value)
		{
			if (!containsKey(key))
			{
				final Collection values = new ArrayList();
				values.add(value);
				return super.put(key, values);
			}
			else
			{
				final Collection col = (Collection) get(key);
				col.add(value);
				return super.put(key, col);
			}
		}
	}

	private class CategoryComparator implements Comparator
	{
		public int compare(final Object obj1, final Object obj2)
		{
			if (obj1 instanceof Category && obj2 instanceof Category)
			{
				final Category cat1 = (Category) obj1;
				final Category cat2 = (Category) obj2;

				final Integer orderOfCat1 = CatalogManager.getInstance().getOrder(cat1);
				final Integer orderOfCat2 = CatalogManager.getInstance().getOrder(cat2);

				if (orderOfCat1 != null && orderOfCat2 != null)
				{
					return orderOfCat1.compareTo(orderOfCat2);
				}
				else
				{
					if (orderOfCat1 != null && orderOfCat2 == null)
					{
						return -1;
					}
					else if (orderOfCat1 == null && orderOfCat2 != null)
					{
						return 1;
					}
					else
					{
						return 0;
					}
				}
			}
			else
			{
				throw new IllegalArgumentException();
			}
		}
	}
}

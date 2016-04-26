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

import de.hybris.platform.bmecat.constants.BMECatConstants;
import de.hybris.platform.catalog.constants.CatalogConstants;
import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.catalog.jalo.ProductReference;
import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.core.GenericCondition;
import de.hybris.platform.core.GenericConditionList;
import de.hybris.platform.core.GenericQuery;
import de.hybris.platform.core.GenericSearchField;
import de.hybris.platform.cronjob.constants.GeneratedCronJobConstants;
import de.hybris.platform.cronjob.jalo.ChangeDescriptor;
import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.cronjob.jalo.CronJobManager;
import de.hybris.platform.cronjob.jalo.Job;
import de.hybris.platform.cronjob.jalo.JobMedia;
import de.hybris.platform.cronjob.jalo.Step;
import de.hybris.platform.europe1.constants.Europe1Constants;
import de.hybris.platform.europe1.jalo.Europe1PriceFactory;
import de.hybris.platform.europe1.jalo.PDTRow;
import de.hybris.platform.europe1.jalo.PriceRow;
import de.hybris.platform.impex.jalo.ImpExManager;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInternalException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.enumeration.EnumerationType;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.link.Link;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.media.MediaManager;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloAbstractTypeException;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.CSVConstants;
import de.hybris.platform.util.JspContext;
import de.hybris.platform.util.StandardDateRange;
import de.hybris.platform.variants.jalo.VariantProduct;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;


/**
 * This is the extension manager of the BMECat extension.
 * 
 */
public class BMECatManager extends GeneratedBMECatManager
{
	// channel
	static final Logger LOG = Logger.getLogger(BMECatManager.class.getName());

	/**
	 * Overwritten to avoid calls to ejb layer.
	 */
	@Override
	protected void checkBeforeItemRemoval(final SessionContext ctx, final Item item) throws ConsistencyCheckException
	{
		// nope
	}

	static final String XML_MIME_TYPE = "text/xml";

	public final static String STANDARD_BMECAT_IMPORT_BATCH_JOB = "BMECat Import";

	public final static String BMECAT_IMPORT_WITH_CSV_JOB = "BMECat v1.2 Import";
	public final static String BMECAT_IMPORT_WITH_CSV_TRANSFORM_JOB = "BMECat v1.2 Import Transform Job";

	/**
	 * 
	 * @return instance of this manager
	 */
	public static BMECatManager getInstance()
	{
		final JaloSession jSession = JaloSession.getCurrentSession();
		return (BMECatManager) jSession.getExtensionManager().getExtension(BMECatConstants.EXTENSIONNAME);
	}


	public EnumerationValue getTransactionModeEnum(final int mode)
	{
		switch (mode)
		{
			case BMECatConstants.TRANSACTION.T_NEW_CATALOG:
				return getSession().getEnumerationManager().getEnumerationValue(BMECatConstants.TC.TRANSACTIONMODEENUM,
						BMECatConstants.Enumerations.TransactionModeEnum.T_NEW_CATALOG);
			case BMECatConstants.TRANSACTION.T_UPDATE_PRODUCTS:
				return getSession().getEnumerationManager().getEnumerationValue(BMECatConstants.TC.TRANSACTIONMODEENUM,
						BMECatConstants.Enumerations.TransactionModeEnum.T_UPDATE_PRODUCTS);
			case BMECatConstants.TRANSACTION.T_UPDATE_PRICES:
				return getSession().getEnumerationManager().getEnumerationValue(BMECatConstants.TC.TRANSACTIONMODEENUM,
						BMECatConstants.Enumerations.TransactionModeEnum.T_UPDATE_PRICES);
			default:
				throw new JaloInvalidParameterException("wrong transaction mode " + mode + " - expected "
						+ BMECatConstants.TRANSACTION.T_NEW_CATALOG + "," + BMECatConstants.TRANSACTION.T_UPDATE_PRODUCTS + " or "
						+ BMECatConstants.TRANSACTION.T_UPDATE_PRICES, 0);
		}
	}

	protected void removeCategoryToProductLink(final CronJob cronJob, final Link link)
	{
		final Category category = (Category) link.getSource();
		final Product product = (Product) link.getTarget();
		createCategory2ProductChangeDescriptor(cronJob, null, BMECatConstants.ChangeTypes.DELETE_CATEGORY2PRODUCT,
				category.getCode(), product.getCode(), Integer.valueOf(link.getSequenceNumber()),
				"removed category2product relation category >" + category.getCode() + "< product >" + product.getCode() + "<");
		try
		{
			link.remove();
			LOG.debug("Category2Product link " + category.getCode() + " -> " + product.getCode() + " deleted!");
		}
		catch (final JaloBusinessException x)
		{
			throw new JaloSystemException(x, null, 0);
		}
	}

	protected void removeProductReference(final CronJob cronJob, final ProductReference reference)
	{
		createProductReferenceChangeDescriptor(cronJob, reference, BMECatConstants.ChangeTypes.DELETE_PRODUCTREFERENCE,
				"removed product reference " + reference.toString());
		try
		{
			LOG.debug("Deleting ArticleReference: " + reference.toString());
			reference.remove();
		}
		catch (final ConsistencyCheckException x)
		{
			throw new JaloSystemException(x, null, 0);
		}
	}

	public ProductReferenceChangeDescriptor createProductReferenceChangeDescriptor(final CronJob cronJob,
			final ProductReference reference, final String changeType, final String description)
	{
		final Product source = reference.getSource();
		final Product target = reference.getTarget();
		final HashMap attributes = new HashMap();
		attributes.put(LinkChangeDescriptor.SOURCE, source.getCode());
		attributes.put(LinkChangeDescriptor.TARGET, target.getCode());
		attributes.put(ProductReferenceChangeDescriptor.REFERENCETYPE, reference.getQualifier());
		attributes.put(ProductReferenceChangeDescriptor.QUANTITY, reference.getQuantity());

		return (ProductReferenceChangeDescriptor) cronJob.addChangeDescriptor(
				getSession().getTypeManager().getComposedType(ProductReferenceChangeDescriptor.class), cronJob.getCurrentStep(),
				changeType, reference, description, attributes);
	}

	public Category2ProductChangeDescriptor createCategory2ProductChangeDescriptor(final CronJob cronJob,
			final Link category2productLink, final String changeType, final String categoryCode, final String productCode,
			final Integer position, final String description)
	{
		final HashMap attributes = new HashMap();
		attributes.put(LinkChangeDescriptor.SOURCE, categoryCode);
		attributes.put(LinkChangeDescriptor.TARGET, productCode);
		attributes.put(Category2ProductChangeDescriptor.POSITION, position);

		return (Category2ProductChangeDescriptor) cronJob.addChangeDescriptor(
				getSession().getTypeManager().getComposedType(Category2ProductChangeDescriptor.class), cronJob.getCurrentStep(),
				changeType, category2productLink, description, attributes);
	}

	/**
	 * Creates a changedescriptor from a category2product link
	 * 
	 * @param cronJob
	 * @param category2productLink
	 *           must not be null
	 * @param changeType
	 * @param description
	 * @return Category2ProductChangeDescriptor
	 */
	public Category2ProductChangeDescriptor createCategory2ProductChangeDescriptor(final CronJob cronJob,
			final Link category2productLink, final String changeType, final String description)
	{
		return createCategory2ProductChangeDescriptor(cronJob, category2productLink, changeType,
				((Category) category2productLink.getSource()).getCode(), ((Product) category2productLink.getTarget()).getCode(),
				Integer.valueOf(category2productLink.getSequenceNumber()), description);
	}

	public PriceChangeDescriptor createPriceChangeDescriptor(final CronJob cronJob, final String changeType,
			final PriceRow priceRow, final String description)
	{
		final HashMap attributes = new HashMap();
		attributes.put(PriceChangeDescriptor.PRICEROW, priceRow);
		attributes.put(PriceChangeDescriptor.PRICECOPY, createPriceCopy(priceRow));
		return (PriceChangeDescriptor) cronJob.addChangeDescriptor(
				getSession().getTypeManager().getComposedType(PriceChangeDescriptor.class), cronJob.getCurrentStep(), changeType,
				(Item) null, description, attributes);
	}

	/**
	 * public Collection getLinkChangeDescriptors( CronJob cronJob, Step step, String changeType, Item source, Item
	 * target ) { return getLinkChangeDescriptors( getSession().getSessionContext(), cronJob, step, changeType, source,
	 * target, 0, -1 ); }
	 */
	/**
	 * Returns LinkChangeDescriptor that match the given paramters
	 * 
	 * @param ctx
	 * @param cronJob
	 * @param step
	 * @param changeType
	 * @param source
	 * @param target
	 * @param start
	 * @param count
	 * @return Collection of LinkChangeDescriptors
	 */
	public Collection<LinkChangeDescriptor> getLinkChangeDescriptors(final SessionContext ctx, final CronJob cronJob,
			final Step step, final String changeType, final String source, final String target, final int start, final int count)
	{
		if (cronJob != null)
		{
			LOG.warn("getLinkChangeDescriptors - cron job is null");
			return Collections.EMPTY_LIST;
		}

		final Map<String, Object> values = new HashMap<String, Object>();
		values.put("cronJob", cronJob);
		if (changeType != null)
		{
			values.put("changeType", changeType);
		}
		if (step != null)
		{
			values.put("step", step);
		}
		if (source != null)
		{
			values.put("source", source);
		}
		if (target != null)
		{
			values.put("target", target);
		}
		//
		return getSession()
				.getFlexibleSearch()
				.search(
						ctx,
						"SELECT {" + Item.PK + "} FROM {"
								+ getSession().getTypeManager().getComposedType(LinkChangeDescriptor.class).getCode() + "} " + "WHERE {"
								+ ChangeDescriptor.CRONJOB + "} = ?cronJob "
								+ (changeType != null ? "AND {" + ChangeDescriptor.CHANGETYPE + "}=?changeType " : "")
								+ (step != null ? "AND {" + ChangeDescriptor.STEP + "}=?step " : "")
								+ (source != null ? "AND {" + LinkChangeDescriptor.SOURCE + "}=?source " : "")
								+ (target != null ? "AND {" + LinkChangeDescriptor.TARGET + "}=?target " : "") + "ORDER BY {"
								+ ChangeDescriptor.SEQUENCENUMBER + "} ASC", values,
						Collections.singletonList(LinkChangeDescriptor.class), true, true, start, count).getResult();
	}

	public Collection<LinkChangeDescriptor> getLinkChangeDescriptors(final CronJob cronJob, final Step step,
			final String changeType, final String source, final String target)
	{
		return getLinkChangeDescriptors(getSession().getSessionContext(), cronJob, step, changeType, source, target, 0, -1);
	}

	public Collection<PriceChangeDescriptor> getPriceChangeDescriptors(final SessionContext ctx, final CronJob cronJob,
			final Step step, final String changeType, final String productCode)
	{
		final Map values = new HashMap();

		if (changeType != null)
		{
			values.put("changeType", changeType);
		}
		if (cronJob != null)
		{
			values.put("cronJob", cronJob);
		}
		if (step != null)
		{
			values.put("step", step);
		}
		String select = "SELECT {1:" + Item.PK + "} FROM {"
				+ getSession().getTypeManager().getComposedType(PriceChangeDescriptor.class).getCode() + "} ";
		String where = "WHERE {1:" + ChangeDescriptor.CRONJOB + "} = ?cronJob ";
		if (productCode != null)
		{
			select += ",{" + getSession().getTypeManager().getComposedType(PriceCopy.class).getCode() + "} ";
			where += "AND {1:" + PriceChangeDescriptor.PRICECOPY + "} = {2:" + Item.PK + "} ";
			where += "AND {2:" + PriceCopy.PRODUCTCODE + "} = ?productCode ";
			values.put("productCode", productCode);
		}
		where += (changeType != null ? "AND {1:" + ChangeDescriptor.CHANGETYPE + "}=?changeType " : "")
				+ (step != null ? "AND {" + ChangeDescriptor.STEP + "}=?step " : "") + "ORDER BY {" + ChangeDescriptor.SEQUENCENUMBER
				+ "} ASC ";

		return getSession().getFlexibleSearch()
				.search(ctx, select + where, values, Collections.singletonList(PriceChangeDescriptor.class), true, true, 0, -1)
				.getResult();
	}

	public BMECatImportBatchJob getOrCreateBMECatImportJob()
	{
		final JaloSession jSession = JaloSession.getCurrentSession();
		final ComposedType bmeCatImportBatchJobType = jSession.getTypeManager().getComposedType(BMECatImportBatchJob.class);
		final EnumerationType eErrorMode = jSession.getEnumerationManager().getEnumerationType(
				GeneratedCronJobConstants.TC.ERRORMODE);
		final ComposedType tInfoStep = jSession.getTypeManager().getComposedType(BMECatInfoStep.class);
		final ComposedType tCatalogStep = jSession.getTypeManager().getComposedType(BMECatCatalogStep.class);
		final ComposedType tCategoryStep = jSession.getTypeManager().getComposedType(BMECatCategoryStep.class);
		final ComposedType tArticleStep = jSession.getTypeManager().getComposedType(BMECatArticleStep.class);
		final ComposedType tMediaStep = jSession.getTypeManager().getComposedType(BMECatMediaStep.class);
		final ComposedType priceStepType = jSession.getTypeManager().getComposedType(BMECatEurope1ArticlePriceStep.class);
		final ComposedType articleReferenceStepType = jSession.getTypeManager().getComposedType(BMECatArticleReferenceStep.class);
		final ComposedType tArticleToCategoryStep = jSession.getTypeManager().getComposedType(BMECatArticleToCategoryStep.class);
		final Map<String, Object> values = new HashMap<String, Object>();
		try
		{
			BMECatImportBatchJob completeJob = (BMECatImportBatchJob) CronJobManager.getInstance().getJob(
					STANDARD_BMECAT_IMPORT_BATCH_JOB);
			if (completeJob == null)
			{
				values.put(Job.CODE, STANDARD_BMECAT_IMPORT_BATCH_JOB);
				completeJob = (BMECatImportBatchJob) bmeCatImportBatchJobType.newInstance(values);

				int number = 0;

				values.clear();
				values.put(Step.BATCHJOB, completeJob);
				values.put(Step.CODE, "InfoStep");
				values.put(Step.SYNCHRONOUS, Boolean.TRUE);
				values.put(Step.ERRORMODE, eErrorMode.getValues().iterator().next());
				values.put(Step.SEQUENCENUMBER, Integer.valueOf(number++));
				tInfoStep.newInstance(values);

				values.clear();
				values.put(Step.CODE, "CatalogStep");
				values.put(Step.SYNCHRONOUS, Boolean.FALSE);
				values.put(Step.ERRORMODE, eErrorMode.getValues().iterator().next());
				values.put(Step.BATCHJOB, completeJob);
				values.put(Step.SEQUENCENUMBER, Integer.valueOf(number++));
				tCatalogStep.newInstance(values);

				values.clear();
				values.put(Step.CODE, "CategoryStep");
				values.put(Step.SYNCHRONOUS, Boolean.FALSE);
				values.put(Step.ERRORMODE, eErrorMode.getValues().iterator().next());
				values.put(Step.BATCHJOB, completeJob);
				values.put(Step.SEQUENCENUMBER, Integer.valueOf(number++));
				tCategoryStep.newInstance(values);

				values.clear();
				values.put(Step.CODE, "ArticleStep");
				values.put(Step.SYNCHRONOUS, Boolean.FALSE);
				values.put(Step.ERRORMODE, eErrorMode.getValues().iterator().next());
				values.put(Step.BATCHJOB, completeJob);
				values.put(Step.SEQUENCENUMBER, Integer.valueOf(number++));
				tArticleStep.newInstance(values);

				values.clear();
				values.put(Step.CODE, "MediaStep");
				values.put(Step.SYNCHRONOUS, Boolean.FALSE);
				values.put(Step.ERRORMODE, eErrorMode.getValues().iterator().next());
				values.put(Step.BATCHJOB, completeJob);
				values.put(Step.SEQUENCENUMBER, Integer.valueOf(number++));
				tMediaStep.newInstance(values);

				values.clear();
				values.put(Step.CODE, "ArticleToCategoryStep");
				values.put(Step.SYNCHRONOUS, Boolean.FALSE);
				values.put(Step.ERRORMODE, eErrorMode.getValues().iterator().next());
				values.put(Step.BATCHJOB, completeJob);
				values.put(Step.SEQUENCENUMBER, Integer.valueOf(number++));
				tArticleToCategoryStep.newInstance(values);

				values.clear();
				values.put(Step.CODE, "BMECatEurope1ArticlePriceStep");
				values.put(Step.SYNCHRONOUS, Boolean.FALSE);
				values.put(Step.ERRORMODE, eErrorMode.getValues().iterator().next());
				values.put(Step.BATCHJOB, completeJob);
				values.put(Step.SEQUENCENUMBER, Integer.valueOf(number++));
				priceStepType.newInstance(values);

				values.clear();
				values.put(Step.CODE, "BMECatArticleReferenceStep");
				values.put(Step.SYNCHRONOUS, Boolean.FALSE);
				values.put(Step.ERRORMODE, eErrorMode.getValues().iterator().next());
				values.put(Step.BATCHJOB, completeJob);
				values.put(Step.SEQUENCENUMBER, Integer.valueOf(number++));
				articleReferenceStepType.newInstance(values);
			}
			return completeJob;
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

	public BMECatImportWithCsvJob getOrCreateBMECatImportWithCsvJob()
	{
		final JaloSession jSession = JaloSession.getCurrentSession();
		final ComposedType bmeCatImportWithCsvJobType = jSession.getTypeManager().getComposedType(BMECatImportWithCsvJob.class);
		final Map<String, Object> values = new HashMap<String, Object>();
		try
		{
			BMECatImportWithCsvJob job = (BMECatImportWithCsvJob) CronJobManager.getInstance().getJob(BMECAT_IMPORT_WITH_CSV_JOB);
			if (job == null)
			{
				values.put(Job.CODE, BMECAT_IMPORT_WITH_CSV_JOB);
				//TODO some other values should also be added? if not, ignore this "TO-DO"
				job = (BMECatImportWithCsvJob) bmeCatImportWithCsvJobType.newInstance(values);
			}
			return job;
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

	public BMECatImportWithCsvTransformJob getOrCreateBMECatImportWithCsvTransformJob()
	{
		final JaloSession jSession = JaloSession.getCurrentSession();
		final ComposedType bmeCatImportWithCsvTransformJobType = jSession.getTypeManager().getComposedType(
				BMECatImportWithCsvTransformJob.class);
		final Map<String, Object> values = new HashMap<String, Object>();
		try
		{
			BMECatImportWithCsvTransformJob job = (BMECatImportWithCsvTransformJob) CronJobManager.getInstance().getJob(
					BMECAT_IMPORT_WITH_CSV_TRANSFORM_JOB);
			if (job == null)
			{
				values.put(Job.CODE, BMECAT_IMPORT_WITH_CSV_TRANSFORM_JOB);
				job = (BMECatImportWithCsvTransformJob) bmeCatImportWithCsvTransformJobType.newInstance(values);
			}
			return job;
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

	public BMECatImportCronJob createDefaultCompleteImportCronJob(final BMECatJobMedia catalogFile)
	{
		return createDefaultBMECatImportCronJob(catalogFile, null);
	}

	public BMECatImportCronJob createDefaultBMECatImportCronJob(final BMECatJobMedia catalogFile,
			final BMECatImportBatchJob bmeCatImportBatchJob)
	{
		final JaloSession jSession = JaloSession.getCurrentSession();
		final ComposedType tbmeCatCronJob = jSession.getTypeManager().getComposedType(BMECatImportCronJob.class);

		final Map<String, Object> values = new HashMap<String, Object>();

		// job for complete import, with InfoStep
		try
		{
			values.put(CronJob.SESSIONUSER, jSession.getSessionContext().getUser());
			values.put(CronJob.SESSIONCURRENCY, jSession.getSessionContext().getCurrency());
			values.put(CronJob.SESSIONLANGUAGE, jSession.getSessionContext().getLanguage());
			values.put(CronJob.ACTIVE, Boolean.TRUE);
			values.put(CronJob.SINGLEEXECUTABLE, Boolean.TRUE);
			values.put(CronJob.CHANGERECORDINGENABLED, Boolean.FALSE);
			values.put(BMECatMediaProcessCronJob.JOBMEDIA, catalogFile);
			final BMECatImportBatchJob job;
			if (bmeCatImportBatchJob == null)
			{
				job = getOrCreateBMECatImportJob();
			}
			else
			{
				job = bmeCatImportBatchJob;
			}
			values.put(CronJob.JOB, job);
			return (BMECatImportCronJob) tbmeCatCronJob.newInstance(values);
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

	public BMECatImportWithCsvCronJob createDefaultCompleteImportWithCsvCronJob(final BMECatImpExScriptMedia impexFile)
	{
		return createDefaultBMECatImportWithCsvCronJob(impexFile, null);
	}

	public BMECatImportWithCsvCronJob createDefaultBMECatImportWithCsvCronJob(final BMECatImpExScriptMedia impexFile,
			final BMECatImportWithCsvJob bmeCatImportWithCsvJob)
	{
		final JaloSession jSession = JaloSession.getCurrentSession();
		final ComposedType bmeCatWithCsvCronJob = jSession.getTypeManager().getComposedType(BMECatImportWithCsvCronJob.class);
		final Map<String, Object> values = new HashMap<String, Object>();

		try
		{
			values.put(CronJob.SESSIONUSER, jSession.getSessionContext().getUser());
			values.put(CronJob.SESSIONCURRENCY, jSession.getSessionContext().getCurrency());
			values.put(CronJob.SESSIONLANGUAGE, jSession.getSessionContext().getLanguage());
			values.put(CronJob.ACTIVE, Boolean.TRUE);
			values.put(CronJob.SINGLEEXECUTABLE, Boolean.TRUE);
			values.put(CronJob.CHANGERECORDINGENABLED, Boolean.FALSE);
			values.put(BMECatMediaProcessCronJob.JOBMEDIA, impexFile);
			final BMECatImportWithCsvJob job;
			if (bmeCatImportWithCsvJob == null)
			{
				job = getOrCreateBMECatImportWithCsvJob();
			}
			else
			{
				job = bmeCatImportWithCsvJob;
			}
			values.put(CronJob.JOB, job);
			return (BMECatImportWithCsvCronJob) bmeCatWithCsvCronJob.newInstance(values);
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

	public BMECatImportWithCsvTransformCronJob createDefaultBMECatImportWithCsvTransformCronJob(final BMECatJobMedia catalogFile,
			final BMECatImportWithCsvTransformJob bmeCatImportWithCsvTransformJob)
	{
		final JaloSession jSession = JaloSession.getCurrentSession();
		final ComposedType bmeCatWithCsvTransformCronJob = jSession.getTypeManager().getComposedType(
				BMECatImportWithCsvTransformCronJob.class);
		final Map<String, Object> values = new HashMap<String, Object>();
		try
		{
			values.put(CronJob.SESSIONUSER, jSession.getSessionContext().getUser());
			values.put(CronJob.SESSIONCURRENCY, jSession.getSessionContext().getCurrency());
			values.put(CronJob.SESSIONLANGUAGE, jSession.getSessionContext().getLanguage());
			values.put(CronJob.ACTIVE, Boolean.TRUE);
			values.put(CronJob.SINGLEEXECUTABLE, Boolean.TRUE);
			values.put(CronJob.CHANGERECORDINGENABLED, Boolean.FALSE);
			values.put(BMECatMediaProcessCronJob.JOBMEDIA, catalogFile);
			final BMECatImportWithCsvTransformJob tJob;
			if (bmeCatImportWithCsvTransformJob == null)
			{
				tJob = getOrCreateBMECatImportWithCsvTransformJob();
			}
			else
			{
				tJob = bmeCatImportWithCsvTransformJob;
			}
			values.put(CronJob.JOB, tJob);
			return (BMECatImportWithCsvTransformCronJob) bmeCatWithCsvTransformCronJob.newInstance(values);
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

	//	protected BMECatImportBatchJob getOrCreateDefaultLeanImportJob()
	//	{
	//		final JaloSession js = JaloSession.getCurrentSession();
	//		final ComposedType bmeCatImportBatchJobType = js.getTypeManager()
	//				.getComposedType(BMECatImportBatchJob.class);
	//		final EnumerationType eErrorMode = js.getEnumerationManager()
	//				.getEnumerationType(GeneratedCronJobConstants.TC.ERRORMODE);
	//		final ComposedType tInfoStep = js.getTypeManager().getComposedType(
	//				BMECatInfoStep.class);
	//		final ComposedType tCatalogStep = js.getTypeManager().getComposedType(
	//				BMECatCatalogStep.class);
	//		final ComposedType tCategoryStep = js.getTypeManager().getComposedType(
	//				BMECatCategoryStep.class);
	//		final ComposedType tArticleStep = js.getTypeManager().getComposedType(
	//				BMECatArticleStep.class);
	//		final ComposedType tMediaStep = js.getTypeManager().getComposedType(
	//				BMECatMediaStep.class);
	//		final ComposedType priceStepType = js.getTypeManager().getComposedType(
	//				BMECatEurope1ArticlePriceStep.class);
	//		final ComposedType articleReferenceStepType = js.getTypeManager()
	//				.getComposedType(BMECatArticleReferenceStep.class);
	//		final ComposedType tArticleToCategoryStep = js.getTypeManager()
	//				.getComposedType(BMECatArticleToCategoryStep.class);
	//		final Map values = new HashMap();
	//		try
	//		{
	//			BMECatImportBatchJob leanJob = (BMECatImportBatchJob) CronJobManager
	//					.getInstance().getJob(BMECatConstants.DEFAULT_JOBS.LEAN);
	//			if (leanJob == null)
	//			{
	//				values.put(Job.CODE, BMECatConstants.DEFAULT_JOBS.LEAN);
	//				leanJob = (BMECatImportBatchJob) bmeCatImportBatchJobType
	//						.newInstance(values);
	//
	//				values.clear();
	//				values.put(Step.BATCHJOB, leanJob);
	//				values.put(Step.CODE, "CatalogStep");
	//				values.put(Step.SYNCHRONOUS, Boolean.FALSE);
	//				values
	//						.put(Step.ERRORMODE, eErrorMode.getValues().iterator().next());
	//				values.put(Step.SEQUENCENUMBER, Integer.valueOf(2));
	//				tCatalogStep.newInstance(values);
	//
	//				values.clear();
	//				values.put(Step.BATCHJOB, leanJob);
	//				values.put(Step.CODE, "CategoryStep");
	//				values.put(Step.SYNCHRONOUS, Boolean.FALSE);
	//				values
	//						.put(Step.ERRORMODE, eErrorMode.getValues().iterator().next());
	//				values.put(Step.SEQUENCENUMBER, Integer.valueOf(4));
	//				tCategoryStep.newInstance(values);
	//
	//				// Article Step with default attribute mapping for product
	//				values.clear();
	//				values.put(Step.BATCHJOB, leanJob);
	//				values.put(Step.CODE, "ArticleStep");
	//				values.put(Step.SYNCHRONOUS, Boolean.FALSE);
	//				values
	//						.put(Step.ERRORMODE, eErrorMode.getValues().iterator().next());
	//				values.put(Step.SEQUENCENUMBER, Integer.valueOf(6));
	//				tArticleStep.newInstance(values);
	//
	//				values.clear();
	//				values.put(Step.BATCHJOB, leanJob);
	//				values.put(Step.CODE, "MediaStep");
	//				values.put(Step.SYNCHRONOUS, Boolean.FALSE);
	//				values
	//						.put(Step.ERRORMODE, eErrorMode.getValues().iterator().next());
	//				values.put(Step.SEQUENCENUMBER, Integer.valueOf(8));
	//				tMediaStep.newInstance(values);
	//
	//				values.clear();
	//				values.put(Step.BATCHJOB, leanJob);
	//				values.put(Step.CODE, "ArticleToCategoryStep");
	//				values.put(Step.SYNCHRONOUS, Boolean.FALSE);
	//				values
	//						.put(Step.ERRORMODE, eErrorMode.getValues().iterator().next());
	//				values.put(Step.SEQUENCENUMBER, Integer.valueOf(10));
	//				tArticleToCategoryStep.newInstance(values);
	//
	//				values.clear();
	//				values.put(Step.BATCHJOB, leanJob);
	//				values.put(Step.CODE, "BMECatEurope1ArticlePriceStep");
	//				values.put(Step.SYNCHRONOUS, Boolean.FALSE);
	//				values
	//						.put(Step.ERRORMODE, eErrorMode.getValues().iterator().next());
	//				values.put(Step.SEQUENCENUMBER, Integer.valueOf(12));
	//				priceStepType.newInstance(values);
	//
	//				values.clear();
	//				values.put(Step.BATCHJOB, leanJob);
	//				values.put(Step.CODE, "BMECatArticleReferenceStep");
	//				values.put(Step.SYNCHRONOUS, Boolean.FALSE);
	//				values
	//						.put(Step.ERRORMODE, eErrorMode.getValues().iterator().next());
	//				values.put(Step.SEQUENCENUMBER, Integer.valueOf(14));
	//				articleReferenceStepType.newInstance(values);
	//
	//			}
	//			return leanJob;
	//		}
	//		catch (JaloGenericCreationException e)
	//		{
	//			throw new JaloSystemException(e);
	//		}
	//		catch (JaloAbstractTypeException e)
	//		{
	//			throw new JaloSystemException(e);
	//		}
	//	}

	//	public BMECatImportCronJob createDefaultLeanImportJob(JobMedia catalogFile)
	//	{
	//		final JaloSession js = JaloSession.getCurrentSession();
	//		final ComposedType tbmeCatCronJob = js.getTypeManager().getComposedType(
	//				BMECatImportCronJob.class);
	//
	//		final Map values = new HashMap();
	//
	//		// job for complete import, with InfoStep
	//		try
	//		{
	//			values.put(CronJob.JOB, getOrCreateDefaultLeanImportJob());
	//			values.put(CronJob.SESSIONUSER, js.getSessionContext().getUser());
	//			values.put(CronJob.SESSIONCURRENCY, js.getSessionContext()
	//					.getCurrency());
	//			values.put(CronJob.SESSIONLANGUAGE, js.getSessionContext()
	//					.getLanguage());
	//			values.put(CronJob.ACTIVE, Boolean.TRUE);
	//			values.put(CronJob.SINGLEEXECUTABLE, Boolean.TRUE);
	//			values.put(MediaProcessCronJob.JOBMEDIA, catalogFile);
	//			tbmeCatCronJob.newInstance(values);
	//			return (BMECatImportCronJob) tbmeCatCronJob.newInstance(values);
	//		}
	//		catch (JaloGenericCreationException e)
	//		{
	//			throw new JaloSystemException(e);
	//		}
	//		catch (JaloAbstractTypeException e)
	//		{
	//			throw new JaloSystemException(e);
	//		}
	//	}

	/**
	 * Part of {@link de.hybris.platform.jalo.DataCreator} interface. Returns <code>false</code>
	 * 
	 * @return <code>false</code>
	 */
	@Override
	public boolean isCreatorDisabled()
	{
		return false;
	}

	/*
	 * @see de.hybris.platform.jalo.extension.Extension#createEssentialData(java.util.Map,
	 * de.hybris.platform.util.JspContext)
	 */
	@Override
	public void createEssentialData(final Map params, final JspContext jspc) throws Exception
	{
		createMedias();

		createImportItems();
		createExportItems();

		createDefaultPackageUnits();
		createDefaultSecurity();

	}


	@Override
	public void createProjectData(final Map params, final JspContext jspc)
	{
		// DOCTODO Document reason, why this block is empty
	}


	private void createImportItems()
	{
		getOrCreateBMECatImportJob();
	}


	private void createExportItems()
	{
		getOrCreateBMECatExportJob();


		/*
		 * we no longer create the Cronjob, because this is done by the wizard final Map params = new HashMap();
		 * params.put(CronJob.CODE, "BMECatExportCronJob"); params.put(CronJob.JOB, exportJob); params.put(CronJob.ACTIVE,
		 * Boolean.TRUE); params.put(CronJob.SINGLEEXECUTABLE, Boolean.FALSE); createBMECatExportCronJob(params);
		 */

	}

	public BMECatExportJob getOrCreateBMECatExportJob()
	{

		BMECatExportJob job = (BMECatExportJob) CronJobManager.getInstance().getJob("BMECat Export");
		if (job == null)
		{
			final Map map = new HashMap();
			map.put(Job.CODE, "BMECat Export");
			job = createBMECatExportJob(map);
		}
		return job;
	}


	private void createMedias() throws Exception
	{

		try
		{
			Map values;

			final ComposedType tJobMedia = TypeManager.getInstance().getComposedType(BMECatJobMedia.class);
			BMECatJobMedia otherBmeCatMedia;
			BMECatJobMedia bmeCatMedia;

			values = new HashMap();
			if (MediaManager.getInstance().getMediaByCode("BMECat Test T_NEW_CATLOG de").size() == 0)
			{
				values.put("code", "BMECat Test T_NEW_CATLOG de");
				values.put(JobMedia.LOCKED, Boolean.FALSE);
				bmeCatMedia = (BMECatJobMedia) tJobMedia.newInstance(values);
				assignFileToMedia(bmeCatMedia, "/bmecat/unittest_catalog_10_de.xml", XML_MIME_TYPE);
			}

			if (MediaManager.getInstance().getMediaByCode("BMECat Test T_NEW_CATLOG en").size() == 0)
			{
				values.clear();
				values.put("code", "BMECat Test T_NEW_CATLOG en");
				values.put(JobMedia.LOCKED, Boolean.FALSE);
				otherBmeCatMedia = (BMECatJobMedia) tJobMedia.newInstance(values);
				assignFileToMedia(otherBmeCatMedia, "/bmecat/unittest_catalog_10_en.xml", XML_MIME_TYPE);
			}

			if (MediaManager.getInstance().getMediaByCode("BMECat Test T_UPDATE_PRODUCTS").size() == 0)
			{
				values.clear();
				values.put("code", "BMECat Test T_UPDATE_PRODUCTS");
				values.put(JobMedia.LOCKED, Boolean.FALSE);
				otherBmeCatMedia = (BMECatJobMedia) tJobMedia.newInstance(values);
				assignFileToMedia(otherBmeCatMedia, "/bmecat/unittest_catalog_10_updateproducts.xml", XML_MIME_TYPE);
			}

			if (MediaManager.getInstance().getMediaByCode("BMECat Test T_UPDATE_PRICES").size() == 0)
			{
				values.clear();
				values.put("code", "BMECat Test T_UPDATE_PRICES");
				values.put(JobMedia.LOCKED, Boolean.FALSE);
				otherBmeCatMedia = (BMECatJobMedia) tJobMedia.newInstance(values);
				assignFileToMedia(otherBmeCatMedia, "/bmecat/unittest_catalog_10_updateprices.xml", XML_MIME_TYPE);
			}

			if (MediaManager.getInstance().getMediaByCode("BMECat Test Medias ZIP").size() == 0)
			{
				values.clear();
				values.put(JobMedia.CODE, "BMECat Test Medias ZIP");
				values.put(JobMedia.LOCKED, Boolean.FALSE);
				final JobMedia mediaMedia = (JobMedia) tJobMedia.newInstance(values);
				assignFileToMedia(mediaMedia, "/bmecat/images.zipped", "application/zip");
			}

			if (MediaManager.getInstance().getMediaByCode("BMECat v1.2 T_NEW_CATALOG").size() == 0)
			{
				values.clear();
				values.put(JobMedia.CODE, "BMECat v1.2 T_NEW_CATALOG");
				values.put(JobMedia.LOCKED, Boolean.FALSE);
				final JobMedia mediaMedia = (JobMedia) tJobMedia.newInstance(values);
				assignFileToMedia(mediaMedia, "/bmecat/bmecat_v1.2_example.xml", XML_MIME_TYPE);
			}

			if (MediaManager.getInstance().getMediaByCode("BMECat v1.2 T_UPDATE_PRODUCTS").size() == 0)
			{
				values.clear();
				values.put(JobMedia.CODE, "BMECat v1.2 T_UPDATE_PRODUCTS");
				values.put(JobMedia.LOCKED, Boolean.FALSE);
				final JobMedia mediaMedia = (JobMedia) tJobMedia.newInstance(values);
				assignFileToMedia(mediaMedia, "/bmecat/bmecat_v1.2_update_products.xml", XML_MIME_TYPE);
			}

			if (MediaManager.getInstance().getMediaByCode("BMECat v1.2 T_UPDATE_PRICES").size() == 0)
			{
				values.clear();
				values.put(JobMedia.CODE, "BMECat v1.2 T_UPDATE_PRICES");
				values.put(JobMedia.LOCKED, Boolean.FALSE);
				final JobMedia mediaMedia = (JobMedia) tJobMedia.newInstance(values);
				assignFileToMedia(mediaMedia, "/bmecat/bmecat_v1.2_update_prices.xml", XML_MIME_TYPE);
			}
		}
		catch (final JaloSystemException e)
		{
			LOG.warn("was not able to create bmecat medias " + e.getMessage());
		}
	}



	public void createDefaultSecurity() throws Exception
	{
		LOG.info("importing resource /bmecat/bmecatuserrights.csv");
		final InputStream inputStream = BMECatManager.class.getResourceAsStream("/bmecat/bmecatuserrights.csv");
		ImpExManager.getInstance().importData(inputStream, "windows-1252", CSVConstants.HYBRIS_FIELD_SEPARATOR,
				CSVConstants.HYBRIS_QUOTE_CHARACTER, true);
	}

	public void createDefaultPackageUnits()
	{
		final InputStream inputStream = BMECatManager.class.getResourceAsStream("/bmecat/rec21rev4_ecetrd309.txt");
		if (inputStream == null)
		{
			LOG.error("cannot read packaing unit file 'rec21rev4_ecetrd309.txt' - skipped unit creation");
			return;
		}
		final SessionContext enCtx = getSession().createSessionContext();
		enCtx.setLanguage(C2LManager.getInstance().getLanguageByIsoCode("en"));
		final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		try
		{
			for (String line = reader.readLine(); line != null; line = reader.readLine())
			{
				final int delimiter = line.indexOf(';');
				final String code = line.substring(0, delimiter).trim();
				final String name = line.substring(delimiter + 1).trim();
				final Unit unit = getOrCreatePackagingUnit(code);
				if (unit.getName(enCtx) == null)
				{
					unit.setName(enCtx, name);
				}
			}
		}
		catch (final IOException e)
		{
			throw new JaloSystemException(e);
		}
		finally
		{
			try
			{
				reader.close();
			}
			catch (final IOException e)
			{
				// DOCTODO Document reason, why this block is empty
			}
		}
	}


	private Unit getOrCreatePackagingUnit(final String code)
	{
		final ProductManager productManager = ProductManager.getInstance();
		final Collection coll = productManager.getUnitsByCode(code);
		if (coll.isEmpty())
		{
			return productManager.createUnit("packaging-" + code, code);
		}
		else
		{
			return (Unit) coll.iterator().next();
		}
	}




	protected PriceRow getPriceRowByCopy(final CatalogVersion catalogVersion, final PriceCopy priceCopy)
	{
		if (priceCopy == null)
		{
			return null;
		}
		Product product = getCatalogManager().getProductByCatalogVersion(catalogVersion, priceCopy.getProductCode());
		if (product == null)
		{
			product = getProductByPreviousCatalogVersion(catalogVersion, priceCopy.getProductCode());
		}
		if (product == null)
		{
			LOG.warn("No product of pricecopy found. product code >" + priceCopy.getProductCode() + "<");
			return null;
		}
		String priceRowTypeCode;
		try
		{
			priceRowTypeCode = getSession().getTypeManager().getComposedType(PriceRow.class).getCode();
		}
		catch (final JaloItemNotFoundException e)
		{
			throw new JaloInternalException(e);
		}

		final HashMap params = new HashMap();
		String query = "SELECT {" + Item.PK + "} FROM {" + priceRowTypeCode + "} " + "WHERE {" + PriceRow.PRODUCT + "} = ?product "
				+ "AND {" + PriceRow.NET + "} =?net " + "AND {" + PriceRow.CURRENCY + "} =?currency " + "AND {" + PDTRow.START_TIME
				+ "} ";
		if (priceCopy.getStartDate() == null)
		{
			query += "IS NULL ";
		}
		else
		{
			query += "=?starttime ";
			params.put("starttime", priceCopy.getStartDate());
		}
		query += "AND {" + PDTRow.END_TIME + "} ";
		if (priceCopy.getEndDate() == null)
		{
			query += "IS NULL ";
		}
		else
		{
			query += "=?endtime ";
			params.put("endtime", priceCopy.getEndDate());
		}
		query += "AND {" + PriceRow.UNIT + "} =?unit " + "AND {" + PriceRow.UNIT_FACTOR + "} =?unitfactor " + "AND {" + PriceRow.UG
				+ "} =?userpricegroup " + "AND {" + PriceRow.MIN_QUANTITY + "} =?minquantity " + "AND {" + PriceRow.PRICE
				+ "} =?price ";
		params.put("product", product);
		params.put("catalogVersion", catalogVersion);
		params.put("net", priceCopy.isNet());
		params.put("currency", priceCopy.getCurrency());
		params.put("unit", priceCopy.getUnit());
		params.put("unitfactor", priceCopy.getUnitFactor());
		params.put("userpricegroup", priceCopy.getUserPriceGroup());
		params.put("minquantity", priceCopy.getMinQuantity());
		params.put("price", priceCopy.getPriceValue());

		final Collection priceRows = getSession()
				.getFlexibleSearch()
				.search(getSession().getSessionContext(), query, params, Collections.singletonList(VariantProduct.class), true, true,
						0, -1).getResult();
		switch (priceRows.size())
		{
			case 0:
				return null;
			case 1:
				return (PriceRow) priceRows.iterator().next();
			default:
				LOG.warn("Mode than one PriceRow for pricecopy found >" + priceCopy.toString());
				return (PriceRow) priceRows.iterator().next();
		}

	}

	protected PriceCopy createPriceCopy(final PriceRow priceRow)
	{
		final Map params = new HashMap();
		params.put(PriceCopy.PRODUCTCODE, priceRow.getProduct().getCode());
		params.put(PriceCopy.UNIT, priceRow.getUnit());
		params.put(PriceCopy.UNITFACTOR, priceRow.getUnitFactor());
		params.put(PriceCopy.USERPRICEGROUP, priceRow.getUserGroup());
		params.put(PriceCopy.MINQUANTITY, Long.valueOf(priceRow.getMinQuantity()));
		params.put(PriceCopy.CURRENCY, priceRow.getCurrency());
		params.put(PriceCopy.NET, priceRow.isNet().booleanValue() ? Boolean.TRUE : Boolean.FALSE);
		final StandardDateRange dateRange = priceRow.getDateRange();
		params.put(PriceCopy.STARTDATE, dateRange == null ? null : dateRange.getStart());
		params.put(PriceCopy.ENDDATE, dateRange == null ? null : dateRange.getEnd());
		params.put(PriceCopy.PRICEVALUE, priceRow.getPrice());
		final PriceCopy priceCopy = createPriceCopy(params);
		return priceCopy;
	}

	protected PriceRow createPriceRow(final CatalogVersion catalogVersion, final PriceCopy priceCopy)
	{
		try
		{
			final Europe1PriceFactory europe1PriceFactory = (Europe1PriceFactory) getSession().getExtensionManager().getExtension(
					Europe1Constants.EXTENSIONNAME);
			Product product = getCatalogManager().getProductByCatalogVersion(catalogVersion, priceCopy.getProductCode());
			if (product == null)
			{
				product = getProductByPreviousCatalogVersion(catalogVersion, priceCopy.getProductCode());
			}
			if (product == null)
			{
				LOG.warn("could not create pricerow from pricecopy because product could not be found >" + priceCopy.getProductCode()
						+ "<");
				return null;
			}
			StandardDateRange dateRange = null;
			try
			{
				// if both are null no date range has been given
				if (priceCopy.getStartDate() != null || priceCopy.getEndDate() != null)
				{
					dateRange = new StandardDateRange(priceCopy.getStartDate(), priceCopy.getEndDate());
				}
			}
			catch (final IllegalArgumentException x)
			{
				LOG.warn("Exception creating DateRange from priceCopy >" + x.getMessage());
			}
			return europe1PriceFactory.createPriceRow(product, null, null, priceCopy.getUserPriceGroup(),
					priceCopy.getMinQuantityAsPrimitive(), priceCopy.getCurrency(), priceCopy.getUnit(),
					priceCopy.getUnitFactorAsPrimitive(), priceCopy.isNetAsPrimitive(), dateRange,
					priceCopy.getPriceValueAsPrimitive());
		}
		catch (final JaloPriceFactoryException x)
		{
			throw new JaloSystemException(x);
		}
	}

	public ChangeDescriptor getMostRecentChange(final CronJob cronJob, final Step step, final String changeType,
			final ComposedType changeDescriptorType)
	{
		if (cronJob == null || changeDescriptorType == null)
		{
			throw new JaloInvalidParameterException("cronjob and change descriptor type must not be null", 0);
		}
		final Map values = new HashMap();
		values.put("cronjob", cronJob);
		if (changeType != null)
		{
			values.put("changeType", changeType);
		}
		if (step != null)
		{
			values.put("step", step);
		}

		final List list = getSession()
				.getFlexibleSearch()
				.search(
						"SELECT {" + Item.PK + "} FROM {" + changeDescriptorType.getCode() + "} " + "WHERE {"
								+ ChangeDescriptor.CRONJOB + "} = ?cronjob "
								+ (changeType != null ? "AND {" + ChangeDescriptor.CHANGETYPE + "}=?changeType " : "")
								+ (step != null ? "AND {" + ChangeDescriptor.STEP + "}=?step " : "") + "ORDER BY {"
								+ ChangeDescriptor.SEQUENCENUMBER + "} DESC", values, Collections.singletonList(ChangeDescriptor.class),
						true, true, 0, 1).getResult();
		return list.isEmpty() ? null : (ChangeDescriptor) list.iterator().next();
	}

	protected CatalogManager getCatalogManager()
	{
		return (CatalogManager) getSession().getExtensionManager().getExtension(CatalogConstants.EXTENSIONNAME);
	}

	protected Product getProductByPreviousCatalogVersion(final CatalogVersion catalogVersion, final String articleID)
	{
		final GenericCondition codeCondition = GenericCondition.equals(new GenericSearchField("product", "code"), articleID);
		final GenericCondition catalogVersionCondition = GenericCondition.equals(new GenericSearchField("product",
				"previousCatalogVersion"), catalogVersion);
		final GenericConditionList genericConditionList = GenericCondition.createConditionList(codeCondition);
		genericConditionList.addToConditionList(catalogVersionCondition);

		final GenericQuery query = new GenericQuery("product", genericConditionList);
		final Collection result = getSession().search(query, getSession().createSearchContext()).getResult();
		if (result.isEmpty())
		{
			return null;
		}
		else if (result.size() == 1)
		{
			return (Product) result.iterator().next();
		}
		else
		{
			// TODO: How to handle inconsistency?!?
			LOG.warn("More than one product found!");
			return (Product) result.iterator().next();
		}
	}

	private void assignFileToMedia(final Media media, final String filename, final String mimetype)
	{
		final InputStream inputStream = BMECatManager.class.getResourceAsStream(filename);
		if (inputStream == null)
		{
			throw new IllegalArgumentException("cannot locate file " + filename + " via classloader "
					+ BMECatManager.class.getClassLoader());
		}

		final int index = filename.indexOf('/') != -1 ? 1 : 0;
		media.setData(new DataInputStream(inputStream), filename.substring(index, filename.length()), mimetype);

	}

	public Map<String, Integer> getImportedVersions(final SessionContext ctx, final CatalogVersion catalogVersion)
	{
		final Map<String, Integer> ret = getAllImportedVersions(ctx, catalogVersion);
		return ret != null ? ret : Collections.EMPTY_MAP;
	}

	public Map<String, Integer> getImportedVersions(final CatalogVersion catalogVersion)
	{
		return getImportedVersions(getSession().getSessionContext(), catalogVersion);
	}

	public void setImportedVersions(final SessionContext ctx, final CatalogVersion catalogVersion, final Map<String, Integer> map)
	{
		setAllImportedVersions(ctx, catalogVersion, map);
	}

	public void setImportedVersions(final CatalogVersion catalogVersion, final Map<String, Integer> map)
	{
		setImportedVersions(getSession().getSessionContext(), catalogVersion, map);
	}

	public String getImportedVersionsKey(final de.hybris.platform.bmecat.parser.Catalog catalogValueObject)
	{
		return catalogValueObject.getID() + "_" + catalogValueObject.getVersion();
	}

	public String getImportedVersionsKey(final CatalogVersion version)
	{
		return version != null ? version.getCatalog().getId() + "_" + version.getVersion() : null;
	}
}

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
package de.hybris.platform.solrfacetsearch.solr.impl;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.solrfacetsearch.enums.IndexerOperationStatus;
import de.hybris.platform.solrfacetsearch.integration.AbstractIntegrationTest;
import de.hybris.platform.solrfacetsearch.model.SolrIndexOperationRecordModel;
import de.hybris.platform.solrfacetsearch.model.indexer.SolrIndexedCoresRecordModel;
import de.hybris.platform.solrfacetsearch.solr.SolrCoresService;
import de.hybris.platform.solrfacetsearch.solr.SolrIndexerOperationsDao;
import de.hybris.platform.solrfacetsearch.solr.SolrIndexerOperationsService;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

import javax.annotation.Resource;

import org.junit.Test;



@IntegrationTest
public class DefaultSolrIndexerOperationsServiceTest extends AbstractIntegrationTest
{
	@Resource(name = "solrIndexerOperationsService")
	private SolrIndexerOperationsService indexOperationsService;

	@Resource
	private SolrIndexerOperationsDao solrIndexerOperationsDao;

	@Resource
	private SolrCoresService solrCoresService;

	private SolrIndexedCoresRecordModel indexCoreRecord;

	private SolrIndexOperationRecordModel operation1;
	private SolrIndexOperationRecordModel operation2;
	private SolrIndexOperationRecordModel operation3;
	private SolrIndexOperationRecordModel operation4;
	private SolrIndexOperationRecordModel operation5;
	private SolrIndexOperationRecordModel operation6;
	private SolrIndexOperationRecordModel operation7;
	private SolrIndexOperationRecordModel operation8;

	@Override
	protected void loadInitialData() throws Exception
	{
		final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

		importConfig("/test/integration/DefaultSolrIndexerOperationsServiceTest.csv",
				Collections.singletonMap("date", dateFormat.format(new Date())));

		indexCoreRecord = solrCoresService.getCoreRecordByCoreName("testCoreName");

		operation1 = solrIndexerOperationsDao.findLastSolrIndexOperationByStatus(indexCoreRecord, IndexerOperationStatus.ABORTED);
		operation2 = solrIndexerOperationsDao.findLastSolrIndexOperationByStatus(indexCoreRecord, IndexerOperationStatus.FAILED);
		operation3 = solrIndexerOperationsDao.findLastSolrIndexOperationByStatus(indexCoreRecord, IndexerOperationStatus.RUNNING);
		operation4 = solrIndexerOperationsDao.findLastSolrIndexOperationByStatus(indexCoreRecord, IndexerOperationStatus.SUCCESS);
		operation5 = solrIndexerOperationsDao.findLastSolrIndexOperationByStatus(indexCoreRecord, IndexerOperationStatus.FAILED);
		operation6 = solrIndexerOperationsDao.findLastSolrIndexOperationByStatus(indexCoreRecord, IndexerOperationStatus.SUCCESS);
		operation7 = solrIndexerOperationsDao.findLastSolrIndexOperationByStatus(indexCoreRecord, IndexerOperationStatus.ABORTED);
		operation8 = solrIndexerOperationsDao.findLastSolrIndexOperationByStatus(indexCoreRecord, IndexerOperationStatus.FAILED);
	}

	@Test
	public void testPurgeOldRecords()
	{
		indexOperationsService.purgePreviousOperations(operation1);
		assertThat(indexCoreRecord.getIndexOperations()).contains(operation1, operation2, operation3, operation4, operation5,
				operation6, operation7, operation8);
		indexOperationsService.purgePreviousOperations(operation3);
		assertThat(indexCoreRecord.getIndexOperations()).contains(operation3, operation4, operation5, operation6, operation7,
				operation8);
		indexOperationsService.purgePreviousOperations(operation5);
		assertThat(indexCoreRecord.getIndexOperations()).contains(operation5, operation6, operation7, operation8);
		indexOperationsService.purgePreviousOperations(operation8);
		assertThat(indexCoreRecord.getIndexOperations()).contains(operation8);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPurgeOldRecordsNull()
	{
		indexOperationsService.purgePreviousOperations(null);
	}
}

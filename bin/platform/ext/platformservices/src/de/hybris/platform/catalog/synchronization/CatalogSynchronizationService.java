/*
 *
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2014 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package de.hybris.platform.catalog.synchronization;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.SyncItemJobModel;


public interface CatalogSynchronizationService
{
	void synchronizeFully(CatalogVersionModel source, CatalogVersionModel target);

	void synchronizeFully(final CatalogVersionModel source, final CatalogVersionModel target, int numberOfThreads);

	void synchronizeFullyInBackground(CatalogVersionModel source, CatalogVersionModel target);

	/**
	 * Executes synchronization process using given syncJob and config object.
	 * 
	 * @param syncJob
	 *           the existing syncJob
	 * @param syncConfig
	 *           config object for preparing the SyncJob and executing synchronization.
	 * @return result of synchronization
	 */
	SyncResult synchronize(SyncItemJobModel syncJob, SyncConfig syncConfig);

}

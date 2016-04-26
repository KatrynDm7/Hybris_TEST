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
package de.hybris.platform.solr.rest;

import static org.apache.solr.core.CoreDescriptor.CORE_ABS_INSTDIR;
import static org.apache.solr.core.CoreDescriptor.SOLR_CORE_PROP_PREFIX;
import static org.apache.solr.rest.ManagedResourceStorage.STORAGE_DIR_INIT_ARG;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.SolrResourceLoader;
import org.apache.solr.rest.ManagedResourceStorage.FileStorageIO;
import org.apache.solr.rest.ManagedResourceStorage.StorageIO;


/**
 * {@link StorageIO} implementation that keeps resources in an index (core, collection) specific location.
 */
public class IndexAwareStorageIO extends FileStorageIO
{
	public static final String MANAGED_RESOURCES_DIR = "managed";

	@Override
	public void configure(final SolrResourceLoader loader, final NamedList<String> initArgs) throws SolrException
	{
		initArgs.remove(STORAGE_DIR_INIT_ARG);

		String coreInstanceDir = loader.getCoreProperties().getProperty(SOLR_CORE_PROP_PREFIX + CORE_ABS_INSTDIR);
		if (StringUtils.isBlank(coreInstanceDir))
		{
			// try without the prefix
			coreInstanceDir = loader.getCoreProperties().getProperty(CORE_ABS_INSTDIR);
		}

		final File storageDir = new File(coreInstanceDir, MANAGED_RESOURCES_DIR);

		initArgs.add(STORAGE_DIR_INIT_ARG, storageDir.getAbsolutePath());

		super.configure(loader, initArgs);
	}
}

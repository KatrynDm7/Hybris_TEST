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
package de.hybris.platform.commerceservices.setup;

import de.hybris.platform.commerceservices.constants.CommerceServicesConstants;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.impex.ImportConfig;
import de.hybris.platform.servicelayer.impex.ImportResult;
import de.hybris.platform.servicelayer.impex.ImportService;
import de.hybris.platform.servicelayer.impex.impl.StreamBasedImpExResource;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedPropertyModel;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


@SystemSetup(extension = CommerceServicesConstants.EXTENSIONNAME)
public class CommerceServicesSystemSetup
{
	private static final Logger LOG = Logger.getLogger(CommerceServicesSystemSetup.class);

	private CommonI18NService commonI18NService;
	private ImportService importService;

	private FlexibleSearchService flexibleSearchService;
	private ModelService modelService;

	private String fileEncoding = "UTF-8";

	/**
	 * Preferred File Extension for ImpEx files. Avoid using CSV as a file extension. *
	 */
	private String impexExt = ".impex";

	protected CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	@Required
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}

	protected ImportService getImportService()
	{
		return importService;
	}

	@Required
	public void setImportService(final ImportService importService)
	{
		this.importService = importService;
	}

	protected String getFileEncoding()
	{
		return fileEncoding;
	}

	public void setFileEncoding(final String fileEncoding)
	{
		this.fileEncoding = fileEncoding;
	}

	protected String getImpexExt()
	{
		return impexExt;
	}

	public void setImpexExt(final String impexExt)
	{
		this.impexExt = impexExt;
	}

	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}

	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	@SystemSetup(type = SystemSetup.Type.ESSENTIAL, process = SystemSetup.Process.UPDATE)
	public void updateSolrIndexedPropertyVisibleFlag(final SystemSetupContext context)
	{
		final SearchResult<SolrIndexedPropertyModel> searchResult = flexibleSearchService.search("SELECT {" +
				SolrIndexedPropertyModel.PK + "} FROM {SolrIndexedProperty} WHERE {" + SolrIndexedPropertyModel.VISIBLE + "} IS NULL");

		final List<SolrIndexedPropertyModel> solrIndexedPropertyModels = searchResult.getResult();

		for (SolrIndexedPropertyModel solrIndexedProperty : solrIndexedPropertyModels)
		{
			final int priority = solrIndexedProperty.getPriority();
			solrIndexedProperty.setVisible(priority <= 0 ? false : true);
		}
		modelService.saveAll(solrIndexedPropertyModels);
	}

	/**
	 * This method will be called by system creator during initialization and system update. Be sure that this method can
	 * be called repeatedly.
	 *
	 * @param context the context provides the selected parameters and values
	 */
	@SystemSetup(type = SystemSetup.Type.ESSENTIAL, process = SystemSetup.Process.ALL)
	public void createEssentialData(final SystemSetupContext context)
	{
		importImpexFile(context, "/commerceservices/import/constraints.impex", true);
	}

	/**
	 * Import impex file. The file is looked up from the classpath. If the file does not exist then an info message is
	 * logged. The file should used the ".impex" file extension. Any language specific files are found with the same root
	 * file name then they are also imported. Language specific files have the language iso code appended to the file
	 * name using an underscore as a separator. For example if the file <tt>/path/file.impex</tt> is imported and the
	 * language specific file <tt>/path/file_de.impex</tt> exists, then it will also be imported. Only files for
	 * languages that exist in the hybris system will be imported.
	 *
	 * @param context        the context provides the selected parameters and values
	 * @param file           the file path to import
	 * @param errorIfMissing flag, set to true to error if the file is not found
	 */
	protected void importImpexFile(final SystemSetupContext context, final String file, final boolean errorIfMissing)
	{
		try(final InputStream resourceAsStream = getClass().getResourceAsStream(file)) {
			if (resourceAsStream == null) {
				if (errorIfMissing) {
					LOG.error("Importing [" + file + "]... ERROR (MISSING FILE)", null);
				} else {
					LOG.info("Importing [" + file + "]... SKIPPED (Optional File Not Found)");
				}
			} else {
				importImpexFile(file, resourceAsStream);

				// Try to import language specific impex files
				if (file.endsWith(getImpexExt())) {
					final String filePath = file.substring(0, file.length() - getImpexExt().length());

					final List<LanguageModel> languages = getCommonI18NService().getAllLanguages();
					for (final LanguageModel language : languages) {
						final String languageFilePath = filePath + "_" + language.getIsocode() + getImpexExt();
						try(final InputStream languageResourceAsStream = getClass().getResourceAsStream(languageFilePath)) {
							if (languageResourceAsStream != null) {
								importImpexFile(languageFilePath, languageResourceAsStream);
							}
						}
					}
				}
			}
		} catch(IOException e){
			LOG.error("FAILED", e);
		}
	}

	protected void importImpexFile(final String file, final InputStream stream)
	{
		final String message = "Importing [" + file + "]...";

		try
		{
			LOG.info(message);

			final ImportConfig importConfig = new ImportConfig();
			importConfig.setScript(new StreamBasedImpExResource(stream, getFileEncoding()));
			importConfig.setLegacyMode(Boolean.TRUE); // Enable legacy mode

			final ImportResult importResult = getImportService().importData(importConfig);
			if (importResult.isError())
			{
				LOG.error(message + " FAILED");
			}
		}
		catch (final Exception e)
		{
			LOG.error(message + " FAILED", e);
		}
	}
}

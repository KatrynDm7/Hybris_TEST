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
package de.hybris.platform.commerceservices.setup.impl;

import de.hybris.platform.commerceservices.setup.SetupImpexService;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.impex.ImportConfig;
import de.hybris.platform.servicelayer.impex.ImportResult;
import de.hybris.platform.servicelayer.impex.ImportService;
import de.hybris.platform.servicelayer.impex.impl.StreamBasedImpExResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import de.hybris.platform.commerceservices.setup.data.ImpexMacroParameterData;


/**
 * Default implementation of {@link SetupImpexService}.
 */
public class DefaultSetupImpexService implements SetupImpexService
{
	private static final Logger LOG = Logger.getLogger(DefaultSetupImpexService.class);
	private ImportService importService;
	private CommonI18NService commonI18NService;

	private String fileEncoding = "UTF-8";

	/** Preferred File Extension for ImpEx files. Avoid using CSV as a file extension. **/
	private String impexExt = ".impex";


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


	@Override
	public void importImpexFile(final String file, final boolean errorIfMissing)
	{
		importImpexFile(file, errorIfMissing, false);
	}

	@Override
	public void importImpexFile(final String file, final boolean errorIfMissing, final boolean legacyMode)
	{
		try(final InputStream resourceAsStream = getClass().getResourceAsStream(file)) {
			if (resourceAsStream == null) {
				if (errorIfMissing) {
					LOG.error("Importing [" + file + "]... ERROR (MISSING FILE)", null);
				} else {
					LOG.info("Importing [" + file + "]... SKIPPED (Optional File Not Found)");
				}
			} else {
				importImpexFile(file, resourceAsStream, legacyMode);

				// Try to import language specific impex files
				if (file.endsWith(getImpexExt())) {
					final String filePath = file.substring(0, file.length() - getImpexExt().length());

					final List<LanguageModel> languages = getCommonI18NService().getAllLanguages();
					for (final LanguageModel language : languages) {
						final String languageFilePath = filePath + "_" + language.getIsocode() + getImpexExt();
						try(final InputStream languageResourceAsStream = getClass().getResourceAsStream(languageFilePath)) {
							if (languageResourceAsStream != null) {
								importImpexFile(languageFilePath, languageResourceAsStream, legacyMode);
							}
						}
					}
				}
			}
		} catch(IOException e){
			LOG.error("FAILED", e);
		}
	}

	protected void importImpexFile(final String file, final InputStream stream, final boolean legacyMode)
	{
		final String message = "Importing [" + file + "]...";

		try
		{
			LOG.info(message);

			final ImportConfig importConfig = new ImportConfig();
			importConfig.setScript(new StreamBasedImpExResource(stream, getFileEncoding()));
			importConfig.setLegacyMode(Boolean.valueOf(legacyMode));

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.acceleratorservices.setup.SetupImpexService#importImpexFile(java.lang.String,
	 * java.util.Map, boolean, boolean)
	 */
	@Override
	public boolean importImpexFile(final String file, final Map<String, Object> macroParameters, final boolean errorIfMissing,
			final boolean legacyMode)
	{
		// NOP
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.acceleratorservices.setup.SetupImpexService#importImpexFile(java.lang.String,
	 * java.util.Map, boolean)
	 */
	@Override
	public boolean importImpexFile(final String file, final Map<String, Object> macroParameters, final boolean errorIfMissing)
	{
		// NOP
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.acceleratorservices.setup.SetupImpexService#importImpexFile(java.lang.String,
	 * de.hybris.platform.commerceservices.setup.data.ImpexMacroParameterData, boolean, boolean)
	 */
	@Override
	public boolean importImpexFile(final String file, final ImpexMacroParameterData macroParameters, final boolean errorIfMissing,
			final boolean legacyMode)
	{
		// NOP
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.acceleratorservices.setup.SetupImpexService#importImpexFile(java.lang.String,
	 * de.hybris.platform.commerceservices.setup.data.ImpexMacroParameterData, boolean)
	 */
	@Override
	public boolean importImpexFile(final String file, final ImpexMacroParameterData macroParameters, final boolean errorIfMissing)
	{
		// NOP
		return false;
	}
}

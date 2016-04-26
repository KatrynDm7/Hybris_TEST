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
 */

package com.hybris.datahub.core.facades.impl.converter;

import de.hybris.bootstrap.config.ConfigUtil;
import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.impex.ImportResult;

import com.hybris.datahub.core.facades.ImportError;
import com.hybris.datahub.core.facades.ItemImportResult;
import com.hybris.datahub.core.facades.impl.ImportResultConverter;
import com.hybris.datahub.core.io.TextFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.Collection;


@SuppressWarnings("javadoc")
/**
 * Converts result reported by the <code>ImportService</code> to the result returned by the <code>ItemImportFacade</code>.
 * This converter combines error messages reported by the import service result with the corresponding lines from the source
 * impex script.
 */
public class SourceAndErrorCombiningConverter implements ImportResultConverter
{
	private static final String TENANT_MEDIA_FOLDER_PATTERN = "sys_{0}";

	// NOT FINAL to allow mocking
	private ErrorLogParser logParser = new ErrorLogParser();
	// NOT FINAL to allow mocking
	private PreviewParser previewParser = new PreviewParser();


	@Override
	public ItemImportResult convert(final ImportResult impRes)
	{
		return impRes == null ? null : toItemImportResult(impRes);
	}

	private ItemImportResult toItemImportResult(final ImportResult impRes)
	{
		final ItemImportResult result = new ItemImportResult();
		if (impRes.isError())
		{
			result.addErrors(extractErrorsFrom(impRes));
		}
		return result;
	}

	private Collection<ImportError> extractErrorsFrom(final ImportResult impRes)
	{
		try
		{
			return impRes.hasUnresolvedLines() ? extractPreviewErrors(impRes) : extractLogErrors(impRes);
		}
		catch (final Exception err)
		{
			throw new ErrorParsingException(err);
		}
	}

	private Collection<ImportError> extractLogErrors(final ImportResult ires) throws IOException
	{
		final String impexLoc = ires.getCronJob().getWorkMedia().getLocation();
		final String errorLog = ires.getCronJob().getLogText();
		final String tenantId = ires.getCronJob().getTenantId();
		final String impexPath = MessageFormat.format(TENANT_MEDIA_FOLDER_PATTERN, tenantId) + File.separator + impexLoc;

		final Collection<ImportError> errors = logParser.parse(errorLog, impexSourceFile(impexPath));
		if (errors.isEmpty() && !errorLog.isEmpty())
		{
			// adds whole log as an error, if errors were not extracted from it
			errors.add(ImportError.create(null, errorLog));
		}
		return errors;
	}

	private TextFile impexSourceFile(final String fname)
	{
		final Path dataDir = ConfigUtil.getPlatformConfig(Registry.class).getSystemConfig().getDataDir().toPath();
		final Path mediaDir = dataDir.resolve("media");
		return new TextFile(new File(mediaDir.toFile(), fname));
	}

	private Collection<ImportError> extractPreviewErrors(final ImportResult impRes)
	{
		final String preview = impRes.getUnresolvedLines().getPreview();
		return previewParser.parse(preview);
	}
}

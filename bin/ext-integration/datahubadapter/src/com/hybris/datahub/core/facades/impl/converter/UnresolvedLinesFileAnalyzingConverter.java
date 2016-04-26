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

import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.ImpExReader;
import de.hybris.platform.impex.jalo.imp.ValueLine;
import de.hybris.platform.servicelayer.impex.ImportResult;
import de.hybris.platform.servicelayer.media.MediaService;

import com.hybris.datahub.core.facades.ImportError;
import com.hybris.datahub.core.facades.ItemImportResult;
import com.hybris.datahub.core.facades.impl.ImportResultConverter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;


@SuppressWarnings("javadoc")
/**
 * Converts result reported by the <code>ImportService</code> to the result returned by the <code>ItemImportFacade</code>.
 * ImpEx service saves any unresolved lines into a separate file to be resolved
 * at the next pass. This converter uses the file to analyze whether any errors
 * remain in the file after the final pass.
 */
public class UnresolvedLinesFileAnalyzingConverter implements ImportResultConverter
{
	private MediaService mediaService;

	@Override
	public ItemImportResult convert(final ImportResult impRes)
	{
		return impRes == null ? null : toItemImportResult(impRes);
	}

	private ItemImportResult toItemImportResult(final ImportResult impRes)
	{
		final ItemImportResult result = new ItemImportResult();
		if (impRes.getCronJob() != null)
		{
			result.addErrors(extractErrorsFrom(impRes));
		}
		return result;
	}

	private Collection<ImportError> extractErrorsFrom(final ImportResult impRes)
	{
		try
		{
			final MediaModel media = impRes.getCronJob().getUnresolvedDataStore();
			return readUnresolved(media);
		}
		catch (final Exception err)
		{
			throw new ErrorParsingException(err);
		}
	}

	private Collection<ImportError> readUnresolved(final MediaModel media) throws IOException, ImpExException
	{
		final List<ImportError> lines = new LinkedList<>();
		try (final InputStream in = mediaService.getStreamFromMedia(media))
		{
			final ImpExReader ir = new ImpExReader(in, "utf-8");
			for (Object line = ir.readLine(); line != null; line = ir.readLine())
			{
				if (line instanceof ValueLine)
				{
					lines.add(toError((ValueLine) line));
				}
			}
		}
		return lines;
	}

	private ImportError toError(final ValueLine line)
	{
		return ImportError.create(line.getSource().toString(), line.getUnresolvedReason());
	}

	@Required
	public void setMediaService(final MediaService service)
	{
		mediaService = service;
	}
}

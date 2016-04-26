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

package de.hybris.datahub.y2ysync.rest.resources;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import com.google.common.base.Stopwatch;
import com.hybris.datahub.validation.ValidationException;

import de.hybris.datahub.y2ysync.facade.CsvImportFacade;


@Path("/data-feeds/y2ysync")
@Consumes(MediaType.APPLICATION_JSON)
public class Y2YSyncImportResource
{
	private static final Logger LOG = LoggerFactory.getLogger(Y2YSyncImportResource.class);

	private CsvImportFacade csvImportFacade;

	@POST
	public Response importData(final Y2YSyncRequest request) throws ValidationException
	{
		LOG.info("Received request for Y2YSync import, id: " + request.getSyncExecutionId());
		final Stopwatch requestStopwatch = Stopwatch.createStarted();
		
		final CompletableFuture<Void> requestProcessing = createProcessingForRequest(request);
		requestProcessing.join();
		
		LOG.info("Y2Y import (id: {}) processed in {}", request.getSyncExecutionId(), requestStopwatch);
		return Response.ok().build();
	}

	private CompletableFuture<Void> createProcessingForRequest(final Y2YSyncRequest request)
	{
		@SuppressWarnings("unchecked")
		final CompletableFuture<Void>[] chunksProcessing = //
				getChunkProcessingTasks(request).map(task -> CompletableFuture.runAsync(task)). //
				toArray(size -> new CompletableFuture[size]);
		return CompletableFuture.allOf(chunksProcessing);
	}

	@Required
	public void setCsvImportFacade(final CsvImportFacade csvImportFacade)
	{
		this.csvImportFacade = csvImportFacade;
	}
	
	private Stream<ChunkProcessingTask> getChunkProcessingTasks(final Y2YSyncRequest request)
	{
		return request.getDataStreams().stream(). //
				flatMap(ds -> ds.getUrls().stream().map(url -> new ChunkProcessingTask(ds, url, csvImportFacade)));
	}
	
	private static class ChunkProcessingTask implements Runnable {
		private static final Logger LOG = LoggerFactory.getLogger(ChunkProcessingTask.class);
		
		private final DataStream dataStream;
		private final String chunkUrl;
		private final CsvImportFacade csvImportFacade;
		
		public ChunkProcessingTask(final DataStream dataStream, final String chunkUrl, final CsvImportFacade csvImportFacade)
		{
			this.dataStream = Objects.requireNonNull(dataStream);
			this.chunkUrl = Objects.requireNonNull(chunkUrl);
			this.csvImportFacade = Objects.requireNonNull(csvImportFacade);
		}

		@Override
		public void run()
		{
			LOG.info("Processing chunk { url: {}, columns: {}, type: {}}", chunkUrl, dataStream.getColumns(), dataStream.getItemType());
			LOG.debug("Reading chunks for header: " + dataStream.getColumns());
			try (InputStream chunkInputStream = getInputStream(chunkUrl))
			{
				final String chunkContent = IOUtils.toString(chunkInputStream);
				LOG.debug("Chunk content: " + chunkContent);

				final String csvInput = dataStream.getColumns() + System.lineSeparator() + chunkContent;
				csvImportFacade.importCsv(dataStream.getItemType(), dataStream.isDelete(), csvInput);
			}
			catch (ValidationException | IOException e)
			{
				LOG.error("Error processing chunk: " + chunkUrl, e);
				throw new RuntimeException("Error processing chunk: " + chunkUrl, e);
			}			
		}
		
		private InputStream getInputStream(final String chunkUrl)
		{
			try
			{
				return new URL(chunkUrl).openStream();
			}
			catch (final IOException e)
			{
				throw new RuntimeException(e);
			}
		}
		
	}
}

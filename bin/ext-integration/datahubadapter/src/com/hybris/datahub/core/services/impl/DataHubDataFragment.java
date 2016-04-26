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

package com.hybris.datahub.core.services.impl;

import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.ScriptValidationReader;
import de.hybris.platform.util.CSVConstants;

import com.hybris.datahub.core.facades.ImportError;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.velocity.util.StringUtils;


/**
 * A fragment containing data being loaded from the Data Hub
 */
public class DataHubDataFragment implements ImpExFragment
{
	private final DataHubFacade dataHub;
	private final Map<String, String> headers;
	private String url;
	private StringBuilder impexHeader;
	private StringBuilder impexBody;

	/**
	 * Instantiates this fragment.
	 *
	 * @param daHub implementation of the facade for communicating to the Data Hub.
	 */
	public DataHubDataFragment(final DataHubFacade daHub)
	{
		assert daHub != null : "Expect Data Hub facade be always instantiated by the reader";
		dataHub = daHub;
		url = "";
		headers = new HashMap<>();
		impexHeader = new StringBuilder();
		impexBody = new StringBuilder();
	}

	@Override
	public boolean addLine(final String line)
	{
		return addLine(line, new Stack<ImpExFragment>());
	}

	@Override
	public boolean addLine(String line, List<ImpExFragment> fragments)
	{
		if (line != null)
		{
			if (impexHeader.length() > 0)
			{
				if (lineIsAUrlComment(line))
				{
					url = commentPayload(line, "URL");
					return true;
				}
				if (lineIsAHeaderComment(line))
				{
					addHeader(line);
					return true;
				}
				if (lineIsImpexBody(line))
				{
					impexBody.append(line).append(CSVConstants.HYBRIS_LINE_SEPARATOR);
					return true;
				}
				validateImpexHeader(fragments);
			}
			else
			{
				if (line.startsWith("INSERT") || line.startsWith("REMOVE"))
				{
					impexHeader.append(line).append(CSVConstants.HYBRIS_LINE_SEPARATOR);
					return true;
				}
			}
		}
		return false;
	}

	private void validateImpexHeader(final List<ImpExFragment> fragments) throws ImpexValidationException
	{
		try
		{
			final StringBuilder macros = new StringBuilder();
			if (CollectionUtils.isNotEmpty(fragments) && fragments.get(0) instanceof ImpexMacroFragment)
			{
				macros.append(fragments.get(0).getContent());
			}
			validateImpexHeader(impexHeader.toString(), macros.toString());
		}
		catch (final ImpExException | IOException ex)
		{
			final List<ImportError> errors = getErrorsForFragment(ex);
			throw new ImpexValidationException(errors);
		}
	}

	protected void validateImpexHeader(final String header, final String macros) throws ImpExException
	{
		final StringBuilder headerBuilder = new StringBuilder().append(macros).append(CSVConstants.HYBRIS_LINE_SEPARATOR).append(header);

		ScriptValidationReader.parseHeader(headerBuilder.toString());
	}

	private List<ImportError> getErrorsForFragment(final Exception ex)
	{
		final List<ImportError> errors = new ArrayList<>();
		try
		{
			final String impexBody = IOUtils.toString(getImpexBody());
			final String[] bodyLines = StringUtils.split(impexBody, CSVConstants.HYBRIS_LINE_SEPARATOR);
			for (final String bodyLine : bodyLines)
			{
				if (lineIsImpexBody(bodyLine))
				{
					final ImportError importError = ImportError.create(bodyLine, ex.getMessage());
					errors.add(importError);
				}
			}
		}
		catch (final IOException ioEx)
		{
			throw new ImpexValidationException(Arrays.asList(ImportError.create(null, ioEx.getMessage())));
		}
		return errors;
	}

	private void addHeader(final String line)
	{
		final String prop = commentPayload(line, "HEADER");
		final int idx = prop.indexOf('=');
		headers.put(prop.substring(0, idx), prop.substring(idx + 1));
	}

	private boolean lineIsAUrlComment(final String line)
	{
		return isSpecialComment(line, "URL");
	}

	private String commentPayload(final String line, final String type)
	{
		final String signature = "#$" + type + ":";
		return line.trim().substring(signature.length()).trim();
	}

	private boolean isSpecialComment(final String line, final String type)
	{
		return line.trim().startsWith("#$" + type + ":");
	}

	private boolean lineIsAHeaderComment(final String line)
	{
		return isSpecialComment(line, "HEADER") && commentPayload(line, "HEADER").contains("=");
	}

	private boolean lineIsImpexBody(final String line)
	{
		return line.trim().startsWith(";");
	}

	protected InputStream getImpexBody() throws IOException
	{
		try
		{
			return url.trim().isEmpty() ? new ByteArrayInputStream(impexBody.toString().getBytes()) : dataHub.readData(url, headers);
		}
		catch (final Exception ex)
		{
			throw new IOException(ex);
		}
	}

	@Override
	public String getContent() throws IOException
	{
		return IOUtils.toString(getContentAsInputStream());
	}

	@Override
	public InputStream getContentAsInputStream() throws IOException
	{
		return new SequenceInputStream(new ByteArrayInputStream(impexHeader.toString().getBytes()), getImpexBody());
	}

	/**
	 * Retrieves facade being used for communication with the Data Hub.
	 *
	 * @return facade implementation.
	 */
	protected DataHubFacade getDataHubFacade()
	{
		return dataHub;
	}

	/**
	 * Reads URL specified in this fragment.
	 *
	 * @return the URL to read data from.
	 */
	public String getUrl()
	{
		return url;
	}

	/**
	 * Reads headers present in this fragment.
	 *
	 * @return a map of the HTTP headers to pass to the Integration Layer; an empty map, if no headers defined in this
	 * fragment.
	 */
	public Map<String, String> getHeaders()
	{
		return headers;
	}


	/**
	 * Reads value of a specific header defined in this fragment.
	 *
	 * @param header name of the header to read.
	 * @return value of the specified header or <code>null</code>, if the specified header is not defined in this
	 * fragment.
	 */
	public String getHeader(final String header)
	{
		return headers.get(header);
	}
}

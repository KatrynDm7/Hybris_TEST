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

import com.hybris.datahub.core.facades.ImportError;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;


/**
 * Extracts errors from the preview field of the <code>ImportResult</code>, where the import result contains description
 * of the unresolved lines.
 */
public class PreviewParser
{

	/**
	 * Parses the specified preview content and returns the corresponding errors
	 *
	 * @param preview content of the <code>ImportResult.getCronJob().getWorkMedia().getPreview()</code>
	 * @return a collection of errors contained in the preview or an empty collection, if there are no errors described
	 * in the preview.
	 */
	public Collection<ImportError> parse(final String preview)
	{
		final String nullSafePreviewContent = preview != null ? preview : "";
		final List<ImportError> errors = new LinkedList<>();
		for (final String line : StringUtils.split(nullSafePreviewContent, "\n\r"))
		{
			final ErrorLine errLine = ErrorLine.parse(line);
			if (errLine != null)
			{
				errors.add(errLine.toError());
			}
		}
		return errors;
	}

	/**
	 * Captures parsed error line structure
	 */
	private static class ErrorLine
	{
		/**
		 * A pattern for extracting an error message and the script line from a line of text. The line of text may start
		 * with any number of ',' with optional numbers between them. The message should start after that and may include
		 * letters, digits, space, ',', ', '=', '!', '-', and '_' characters. The source part should start with ';'. The
		 * pattern trims leading and trailing spaces from the captured message. ?<message> and ?<source> give names for
		 * the substrings being extracted by the pattern, so that the code could use the group names for retrieving
		 * correct values.
		 */
		private static final Pattern errorLinePattern = Pattern
				.compile("\"?(?:[\\w: '=]*,)+\\s*(?<message>[\\w\\s:'\"#.,=!\\-\\[\\]@]+ *)\"?\\s*(?<source>;.*)\\s*");
		private final String message;
		private final String scriptLine;

		private ErrorLine(final String msg, final String line)
		{
			message = msg;
			scriptLine = line;
		}

		private static ErrorLine parse(final String line)
		{
			final Matcher matcher = errorLinePattern.matcher(line);
			if (matcher.matches())
			{
				final String errMsg = matcher.group("message");
				final String impexLn = removeSymbolsAddedByImportService(matcher.group("source"));
				return new ErrorLine(errMsg, impexLn);
			}
			return null;
		}

		private static String removeSymbolsAddedByImportService(final String in)
		{
			return in.replace("<ignore>", "");
		}

		private ImportError toError()
		{
			return ImportError.create(scriptLine, message);
		}
	}
}

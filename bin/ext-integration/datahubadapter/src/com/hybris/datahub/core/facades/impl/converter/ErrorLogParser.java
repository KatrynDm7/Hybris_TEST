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
import com.hybris.datahub.core.io.TextFile;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;


/**
 * Parses the impex job log.
 */
public class ErrorLogParser
{

	/**
	 * Parses the import log created by the ImpExCronJob and creates errors.
	 *
	 * @param log a log to parse
	 * @param script impex script that has been attempted to import
	 * @return a collection of structured and classified errors derived from the log or an empty collection, if there are
	 * no errors indicated.
	 * @throws IOException if failed to parse the log, IllegalArgumentException if null passed in.
	 */
	public Collection<ImportError> parse(final String log, final TextFile script) throws IOException
	{
		if (script == null)
		{
			throw new IllegalArgumentException("impex script is expected to present even, if there are no errors in the log");
		}

		final Set<ImportError> errors = new HashSet<>();
		if (log != null)
		{
			final LineLinkedErrorMessages messages = extractUniqueLogLines(log);
			for (final Map.Entry<Long, String> msg : messages.mappings())
			{
				errors.add(toError(msg, script));
			}
		}
		return errors;
	}

	private ImportError toError(final Entry<Long, String> msg, final TextFile script) throws IOException
	{
		final Long lineNum = msg.getKey();
		final String impexSrcLine = script.readLine(lineNum);
		return ImportError.create(impexSrcLine, msg.getValue());
	}

	private LineLinkedErrorMessages extractUniqueLogLines(final String log)
	{
		final LineLinkedErrorMessages messages = new LineLinkedErrorMessages();
		for (final String line : StringUtils.split(log, "\n\r"))
		{
			messages.add(line);
		}
		return messages;
	}

	/**
	 * Extracts error messages unique by their line number
	 */
	protected static class LineLinkedErrorMessages
	{
		/**
		 * A pattern for extracting line number from a line of text. The line number is expected to be between words
		 * 'line' and 'at <name_of_the_script> script:'
		 */
		private static final Pattern LINE_NUMBER_EXP = Pattern.compile(".*line (\\d+) at \\w+ script:.*");
		/**
		 * A pattern for extracting an error message from a line of text. The message should be a non-empty sequence of
		 * charecters starting after 'at <name_of_the_script> script:'. The pattern trims leading and trailing spaces from
		 * the message.
		 */
		private static final Pattern MESSAGE_EXP = Pattern.compile(".*at \\w+ script: *(.+) *");
		private final Map<Long, String> lines = new HashMap<>();

		/**
		 * Stores new error message or ignores it, if a message for the same line number already exists
		 *
		 * @param msgLine a message line to parse and to store
		 */
		public void add(final String msgLine)
		{
			final Long lineNum = extractLineNumber(msgLine);
			if (lineNum != null && !lines.containsKey(lineNum))
			{
				lines.put(lineNum, extractErrorMessage(msgLine));
			}
		}

		private String extractErrorMessage(final String msgLine)
		{
			return findMatch(MESSAGE_EXP, msgLine);
		}

		private Long extractLineNumber(final String msgLine)
		{
			final String ln = findMatch(LINE_NUMBER_EXP, msgLine);
			return ln != null ? Long.valueOf(ln) : null;
		}

		private String findMatch(final Pattern searchPattern, final String line)
		{
			final Matcher matcher = searchPattern.matcher(line);
			return matcher.matches() ? matcher.group(1) : null;
		}

		/**
		 * Returns a collection of messages added so far.
		 *
		 * @return a collection of all message mappings, in which the key is the line number and the value is the message
		 * text; or an empty collection, if there were messages added yet.
		 */
		public Collection<Map.Entry<Long, String>> mappings()
		{
			return lines.entrySet();
		}

		/**
		 * Retrieves error message for the specified line number.
		 *
		 * @param lnum line number to get the message for.
		 * @return a message for the line number specified or <code>null</code>, if there is no message for the specified
		 * line number.
		 */
		public String getMessageForLine(final long lnum)
		{
			return lines.get(lnum);
		}
	}
}

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
package de.hybris.platform.commercewebservicescommons.utils;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

/**
 *
 */
public class YSanitizer {
    public static String sanitize(final String input)
    {
        // clean input
        String output = StringUtils.defaultString(input).trim();
        // remove CRLF injection
        output = output.replaceAll("(\\r\\n|\\r|\\n)+", " ");
        // escape html
        output = StringEscapeUtils.escapeHtml(output);
        return output;
    }
}

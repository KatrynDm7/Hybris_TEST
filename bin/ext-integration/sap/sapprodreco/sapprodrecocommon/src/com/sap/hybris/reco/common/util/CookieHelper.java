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
package com.sap.hybris.reco.common.util;

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class CookieHelper {

    /**
     * For an anonymous user, get the Piwik cookie ID
     *
     * @return cookieId
     */
    public static String getPiwikID(HttpServletRequest request)
    {
        Cookie cookies[] = request.getCookies();

        if (cookies == null || cookies.length <= 0)
        {
            return StringUtils.EMPTY;
        }

        for (Cookie cookie : cookies)
        {
            if (cookie.getName().startsWith("_pk_id"))
            {
                String piwikId = cookie.getValue().substring(0,16);
                return piwikId;
            }
        }

        //return empty String if no cookies are matched
        return StringUtils.EMPTY;
    }

}

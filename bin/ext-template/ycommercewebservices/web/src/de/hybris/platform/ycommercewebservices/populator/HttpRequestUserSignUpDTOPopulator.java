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
package de.hybris.platform.ycommercewebservices.populator;

import de.hybris.platform.commercewebservicescommons.dto.user.UserSignUpWsDTO;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.converters.impl.AbstractPopulatingConverter;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;

/**
 * Populates {@link UserSignUpWsDTO} instance based on http request parameters:<br>
 * <ul>
 * <li>uid</li>
 * <li>password</li>
 * <li>titleCode</li>
 * <li>firstName</li>
 * <li>lastName</li>
 * </ul>
 *
 */
@Component("HttpRequestUserSignUpDTOPopulator")
public class HttpRequestUserSignUpDTOPopulator extends AbstractPopulatingConverter<HttpServletRequest, UserSignUpWsDTO>
{
    private static final String UID = "login";
    private static final String PASSWORD = "password";
    private static final String TITLECODE = "titleCode";
    private static final String FIRSTNAME = "firstName";
    private static final String LASTNAME = "lastName";

    @Override
    protected UserSignUpWsDTO createTarget()
    {
        return new UserSignUpWsDTO();
    }

    @Override
    public void populate(final HttpServletRequest source, final UserSignUpWsDTO target) throws ConversionException
    {
        Assert.notNull(source, "Parameter source cannot be null.");
        Assert.notNull(target, "Parameter target cannot be null.");

        target.setUid(StringUtils.defaultString(source.getParameter(UID), target.getUid()));
        target.setPassword(StringUtils.defaultString(source.getParameter(PASSWORD), target.getPassword()));
        target.setTitleCode(StringUtils.defaultString(source.getParameter(TITLECODE), target.getTitleCode()));
        target.setFirstName(StringUtils.defaultString(source.getParameter(FIRSTNAME), target.getFirstName()));
        target.setLastName(StringUtils.defaultString(source.getParameter(LASTNAME), target.getLastName()));
    }

}

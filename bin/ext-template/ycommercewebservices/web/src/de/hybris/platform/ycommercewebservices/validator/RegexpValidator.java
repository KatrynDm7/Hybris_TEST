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
package de.hybris.platform.ycommercewebservices.validator;

import org.apache.commons.validator.routines.RegexValidator;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Validates one specific string property specified by {@link #fieldPath} in any object
 * if it is valid against given regular expression
 */
public class RegexpValidator  implements Validator {

    private String fieldPath;
    private String regularExpression;
    private String errorMessageID;

    @Override
    public boolean supports(Class<?> aClass)
    {
        return true;
    }

    @Override
    public void validate(Object o, Errors errors) {
        Assert.notNull(errors, "Errors object must not be null");
        final String fieldValue = (String) errors.getFieldValue(getFieldPath());

        RegexValidator validator = new RegexValidator(getRegularExpression());

        if (!validator.isValid(fieldValue))
        {
            errors.rejectValue(getFieldPath(), getErrorMessageID(), new String[]
                    { getFieldPath() }, null);
        }
    }


    @Required
    public void setFieldPath(final String fieldPath)
    {
        this.fieldPath = fieldPath;
    }

    public String getFieldPath()
    {
        return this.fieldPath;
    }

    @Required
    public void setRegularExpression(final String regularExpression)
    {
        this.regularExpression = regularExpression;
    }

    public String getRegularExpression()
    {
        return this.regularExpression;
    }

    @Required
    public void setErrorMessageID(final String errorMessageID)
    {
        this.errorMessageID = errorMessageID;
    }

    public String getErrorMessageID()
    {
        return this.errorMessageID;
    }



}

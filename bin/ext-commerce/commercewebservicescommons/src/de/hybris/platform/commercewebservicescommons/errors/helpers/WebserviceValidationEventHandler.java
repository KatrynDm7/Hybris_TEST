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
package de.hybris.platform.commercewebservicescommons.errors.helpers;


import javax.xml.bind.ValidationEvent;

public class WebserviceValidationEventHandler implements javax.xml.bind.ValidationEventHandler {
    @Override
    public boolean handleEvent(final ValidationEvent event) {
        if( event == null ) {
            throw new IllegalArgumentException();
        }

        boolean retVal = false;
        switch ( event.getSeverity() ) {
            case ValidationEvent.WARNING:
                retVal = true; // continue after warnings
                break;
            case ValidationEvent.ERROR:
                retVal = false; // terminate after errors
                break;
            case ValidationEvent.FATAL_ERROR:
                retVal = false; // terminate after fatal errors
                break;
        }

        // fail on the first error or fatal error
        return retVal;
    }
}

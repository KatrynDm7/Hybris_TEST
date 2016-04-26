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
package de.hybris.platform.subscriptionfacades.converters.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.subscriptionfacades.data.UsageUnitData;
import de.hybris.platform.subscriptionservices.model.UsageUnitModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;


/**
 * Populate DTO {@link UsageUnitData} with data from {@link UsageUnitModel}
 */
public class UsageUnitPopulator<SOURCE extends UsageUnitModel, TARGET extends UsageUnitData> implements Populator<SOURCE, TARGET>
{
    @Override
    public void populate(final SOURCE source, final TARGET target) throws ConversionException
    {
    	if (source != null && target != null) {
            target.setName(source.getName());
            target.setNamePlural(source.getNamePlural());
            target.setId(source.getId());
            target.setAccumulative(source.getAccumulative() == null ? false : source.getAccumulative().booleanValue());    		
    	}
    }
}

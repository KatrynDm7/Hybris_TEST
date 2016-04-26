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
package de.hybris.platform.savedorderforms.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.savedorderforms.model.OrderFormEntryModel;
import de.hybris.platform.savedorderforms.model.OrderFormModel;
import de.hybris.platform.savedorderforms.orderform.data.OrderFormData;
import de.hybris.platform.savedorderforms.orderform.data.OrderFormEntryData;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.user.UserService;
import org.fest.util.Collections;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.List;


/**
 * Populates {@link de.hybris.platform.savedorderforms.orderform.data.OrderFormData} with {@link de.hybris.platform.savedorderforms.model.OrderFormModel}.
 */
public class OrderFormReversePopulator implements Populator<OrderFormData, OrderFormModel>
{
    private CommonI18NService commonI18NService;
    private UserService userService;

    @Override
    public void populate(final OrderFormData source, final OrderFormModel target) throws ConversionException
    {
        target.setCode(source.getCode());
        target.setDescription(source.getDescription());

        if (source.getCurrency() != null)
        {
            final String isocode = source.getCurrency().getIsocode();
            try
            {
                target.setCurrency(getCommonI18NService().getCurrency(isocode));
            }
            catch (final UnknownIdentifierException e)
            {
                throw new ConversionException("No currency with the code " + isocode + " found.", e);
            }

        }

        target.setUser(getUserService().getCurrentUser());

        if (!Collections.isEmpty(source.getEntries()))
        {
            final List<OrderFormEntryModel> orderFormEntryModelList = new ArrayList<OrderFormEntryModel>();

            for (OrderFormEntryData orderFormEntryData : source.getEntries())
            {
                OrderFormEntryModel orderFormEntryModel = new OrderFormEntryModel();
                orderFormEntryModel.setSku(orderFormEntryData.getSku());
                orderFormEntryModel.setQuantity(orderFormEntryData.getQuantity());

                orderFormEntryModelList.add(orderFormEntryModel);
            }

            target.setEntries(orderFormEntryModelList);
        }

    }

    protected CommonI18NService getCommonI18NService()
    {
        return commonI18NService;
    }

    @Required
    public void setCommonI18NService(final CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }

    protected UserService getUserService()
    {
        return userService;
    }

    @Required
    public void setUserService(final UserService userService)
    {
        this.userService = userService;
    }
}

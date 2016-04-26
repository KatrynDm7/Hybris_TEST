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

import de.hybris.platform.commercefacades.storesession.data.CurrencyData;
import de.hybris.platform.commercefacades.user.data.PrincipalData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.savedorderforms.model.OrderFormEntryModel;
import de.hybris.platform.savedorderforms.model.OrderFormModel;
import de.hybris.platform.savedorderforms.orderform.data.OrderFormData;
import de.hybris.platform.savedorderforms.orderform.data.OrderFormEntryData;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import org.fest.util.Collections;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.List;


/**
 * Populates {@link de.hybris.platform.savedorderforms.orderform.data.OrderFormData} with {@link de.hybris.platform.savedorderforms.model.OrderFormModel}.
 */
public class OrderFormPopulator implements Populator<OrderFormModel, OrderFormData>
{
    private Converter<CurrencyModel, CurrencyData> currencyConverter;
    private Converter<PrincipalModel, PrincipalData> principalConverter;

    @Override
    public void populate(final OrderFormModel source, final OrderFormData target) throws ConversionException
    {
        target.setCode(source.getCode());
        target.setDescription(source.getDescription());
        target.setCurrency(getCurrencyConverter().convert(source.getCurrency()));
        target.setUser(getPrincipalConverter().convert(source.getUser()));

        if (!Collections.isEmpty(source.getEntries()))
        {
            final List<OrderFormEntryData> orderFormEntryDataList = new ArrayList<OrderFormEntryData>();

            for (OrderFormEntryModel orderFormEntryModel : source.getEntries())
            {
                OrderFormEntryData orderFormEntryData = new OrderFormEntryData();
                orderFormEntryData.setSku(orderFormEntryModel.getSku());
                orderFormEntryData.setQuantity(orderFormEntryModel.getQuantity());

                orderFormEntryDataList.add(orderFormEntryData);
            }

            target.setEntries(orderFormEntryDataList);
        }

    }

    protected Converter<CurrencyModel, CurrencyData> getCurrencyConverter()
    {
        return currencyConverter;
    }

    @Required
    public void setCurrencyConverter( final Converter<CurrencyModel, CurrencyData> currencyConverter )
    {
        this.currencyConverter = currencyConverter;
    }

    protected Converter<PrincipalModel, PrincipalData> getPrincipalConverter()
    {
        return principalConverter;
    }

    @Required
    public void setPrincipalConverter(final Converter<PrincipalModel, PrincipalData> principalConverter)
    {
        this.principalConverter = principalConverter;
    }
}

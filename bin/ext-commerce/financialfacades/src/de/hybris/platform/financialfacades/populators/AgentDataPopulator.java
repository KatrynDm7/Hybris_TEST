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
package de.hybris.platform.financialfacades.populators;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.ImageDataType;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.financialfacades.findagent.data.AgentData;
import de.hybris.platform.financialservices.model.AgentModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

/**
 * AgentDataPopulator class.
 */
public class AgentDataPopulator<SOURCE extends AgentModel, TARGET extends AgentData> implements Populator<SOURCE, TARGET>
{

    private static final Logger LOG = Logger.getLogger(AgentDataPopulator.class);
    public static final String THUMBNAIL = "thumbnail";

    private Converter<CategoryModel, CategoryData> categoryConverter;
    private Converter<AddressModel, AddressData> addressConverter;

    /**
     * Populate the target instance with values from the source instance.
     *
     * @param agentModel
     * @param agentData
     * @throws de.hybris.platform.servicelayer.dto.converter.ConversionException if an error occurs
     */
    @Override
    public void populate(final SOURCE agentModel, final TARGET agentData) throws ConversionException
    {

        Assert.notNull(agentModel, "Parameter source cannot be null.");
        Assert.notNull(agentData, "Parameter target cannot be null.");


        final Collection<CategoryModel> categoryModels = agentModel.getCategories();
        if (categoryModels != null)
        {
            final List<CategoryData> categoryDatas = new ArrayList<>(categoryModels.size());
            for (CategoryModel category : categoryModels)
            {
                final CategoryData converted = categoryConverter.convert(category);
                categoryDatas.add(converted);
            }
            agentData.setCategories(categoryDatas);

        }
        if (agentModel.getEnquiry() != null)
        {
            final AddressData addressData = addressConverter.convert(agentModel.getEnquiry());
            agentData.setEnquiryData(addressData);
        }

        final ImageData image = createImageData(agentModel, THUMBNAIL, ImageDataType.PRIMARY);
        agentData.setThumbnail(image);

    }

    protected ImageData createImageData(final SOURCE source, final String imageFormat,
            final ImageDataType type)
    {
        ImageData result = null;
        final String imgValue = source.getThumbnail() != null? source.getThumbnail().getURL() : null;
        if (!StringUtils.isEmpty(imgValue))
        {
            final ImageData imageData = new ImageData();
            imageData.setImageType(type);
            imageData.setFormat(imageFormat);
            imageData.setUrl(imgValue);
            result = imageData;
        }
        return result;
    }

    @Required
    public void setCategoryConverter(final Converter<CategoryModel, CategoryData> categoryConverter)
    {
        this.categoryConverter = categoryConverter;
    }

    @Required
    public void setAddressConverter(final Converter<AddressModel, AddressData> addressConverter)
    {
        this.addressConverter = addressConverter;
    }

    public Converter<CategoryModel, CategoryData> getCategoryConverter()
    {
        return categoryConverter;
    }

    public Converter<AddressModel, AddressData> getAddressConverter()
    {
        return addressConverter;
    }
}

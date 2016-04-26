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
package de.hybris.platform.financialfacades.populators;

import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.ImageDataType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;


/**
 * The class of InsuranceProductThumbnailImagePopulator.
 */
public class InsuranceProductThumbnailImagePopulator implements Populator<ProductModel, ProductData>
{

	private Populator<MediaModel, ImageData> imagePopulator;

	/**
	 * Populate the target instance with values from the source instance.
	 *
	 * @param productModel
	 *           the source object
	 * @param productData
	 *           the target to fill
	 */
	@Override
	public void populate(final ProductModel productModel, final ProductData productData)
	{
		Assert.notNull(productModel, "productModel cannot be null.");
		Assert.notNull(productData, "productData cannot be null.");

		if (CollectionUtils.isNotEmpty(productModel.getThumbnails()))
		{
			final List<ImageData> imageList = new ArrayList<>();
			// fill our image list with the product's existing images
			if (productData.getImages() != null)
			{
				imageList.addAll(productData.getImages());
			}
			for (final MediaModel mediaModel : productModel.getThumbnails())
			{
				final ImageData imageData = new ImageData();
				imageData.setImageType(ImageDataType.PRIMARY);
				getImagePopulator().populate(mediaModel, imageData);

				imageList.add(imageData);
			}
			productData.setImages(imageList);
		}
	}

	protected Populator<MediaModel, ImageData> getImagePopulator()
	{
		return imagePopulator;
	}

	@Required
	public void setImagePopulator(final Populator<MediaModel, ImageData> imagePopulator)
	{
		this.imagePopulator = imagePopulator;
	}
}

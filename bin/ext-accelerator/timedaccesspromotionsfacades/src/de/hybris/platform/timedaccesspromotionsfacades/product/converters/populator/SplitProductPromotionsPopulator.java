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
package de.hybris.platform.timedaccesspromotionsfacades.product.converters.populator;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.product.converters.populator.ProductPromotionsPopulator;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.promotions.model.ProductPromotionModel;
import de.hybris.platform.promotions.model.PromotionGroupModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;


/**
 * Separate the product promotions into two group:<br>
 * Potential promotions and Special promotions. <br>
 * Potential promotions would be used by normal product detailed page, while special promotions are used for group buy
 * and flash buy product detailed pages.
 */
public class SplitProductPromotionsPopulator<SOURCE extends ProductModel, TARGET extends ProductData> extends
		ProductPromotionsPopulator<SOURCE, TARGET>
{

	@Override
	public void populate(final SOURCE productModel, final TARGET productData) throws ConversionException
	{
		final BaseSiteModel baseSiteModel = getBaseSiteService().getCurrentBaseSite();
		if (baseSiteModel != null)
		{
			final PromotionGroupModel defaultPromotionGroup = baseSiteModel.getDefaultPromotionGroup();
			defaultPromotionGroup.getPromotions();
			final Date currentTimeRoundedToMinute = DateUtils.round(getTimeService().getCurrentTime(), Calendar.MINUTE);

			if (defaultPromotionGroup != null)
			{
				final List<ProductPromotionModel> promotions = getPromotionsService().getProductPromotions(
						Collections.singletonList(defaultPromotionGroup), productModel, true, currentTimeRoundedToMinute);
				final List<ProductPromotionModel> normalPromotions = new ArrayList<ProductPromotionModel>();
				final List<ProductPromotionModel> specialPromotions = new ArrayList<ProductPromotionModel>();
				for (final ProductPromotionModel promotion : promotions)
				{
					if (promotion.getSpecialDiscount() == null || !promotion.getSpecialDiscount().equals('Y'))
					{
						normalPromotions.add(promotion);
					}
					else
					{
						specialPromotions.add(promotion);
					}

				}
				productData.setPotentialPromotions(Converters.convertAll(normalPromotions, getPromotionsConverter()));
				productData.setSpecialPromotions(Converters.convertAll(specialPromotions, getPromotionsConverter()));
			}
		}
	}
}

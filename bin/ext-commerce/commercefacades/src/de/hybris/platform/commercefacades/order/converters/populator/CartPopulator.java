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
package de.hybris.platform.commercefacades.order.converters.populator;

import de.hybris.platform.commercefacades.order.data.AbstractOrderData;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.user.data.PrincipalData;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.promotions.result.PromotionOrderResults;

import org.apache.commons.lang.StringUtils;


/**
 * Converter implementation for {@link de.hybris.platform.core.model.order.CartModel} as source and
 * {@link de.hybris.platform.commercefacades.order.data.CartData} as target type.
 */
public class CartPopulator<T extends CartData> extends AbstractOrderPopulator<CartModel, T>
{

	@Override
	public void populate(final CartModel source, final T target)
	{
		addCommon(source, target);
		addTotals(source, target);
		addEntries(source, target);
		addPromotions(source, target);
		addSavedCartData(source, target);
		target.setGuid(source.getGuid());
		target.setTotalUnitCount(calcTotalUnitCount(source));
	}

	@Override
	protected void addPromotions(final AbstractOrderModel source, final PromotionOrderResults promoOrderResults,
			final AbstractOrderData target)
	{
		super.addPromotions(source, promoOrderResults, target);

		if (promoOrderResults != null)
		{
			final CartData cartData = (CartData) target;
			cartData.setPotentialOrderPromotions(getPromotions(promoOrderResults.getPotentialOrderPromotions()));
			cartData.setPotentialProductPromotions(getPromotions(promoOrderResults.getPotentialProductPromotions()));
		}
	}

	protected void addSavedCartData(final CartModel source, final CartData target)
	{
		if (StringUtils.isNotEmpty(source.getName()))
		{
			target.setName(source.getName());
		}
		if (StringUtils.isNotEmpty(source.getDescription()))
		{
			target.setDescription(source.getDescription());
		}
		if (null != source.getSaveTime())
		{
			target.setSaveTime(source.getSaveTime());
		}
		if (null != source.getExpirationTime())
		{
			target.setExpirationTime(source.getExpirationTime());
		}

		if (null != source.getSavedBy())
		{
			final PrincipalData savedBy = new PrincipalData();
			if (StringUtils.isNotEmpty(source.getSavedBy().getName()))
			{
				savedBy.setName(source.getSavedBy().getName());
			}

			if (StringUtils.isNotEmpty(source.getSavedBy().getUid()))
			{
				savedBy.setUid(source.getSavedBy().getUid());

			}
			target.setSavedBy(savedBy);
		}
	}
}

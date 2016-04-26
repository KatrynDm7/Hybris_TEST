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
package de.hybris.platform.commerceservices.promotion.impl;


import de.hybris.platform.commerceservices.model.promotions.PromotionOrderRestrictionModel;
import de.hybris.platform.commerceservices.promotion.CommercePromotionRestrictionService;
import de.hybris.platform.commerceservices.promotion.dao.CommercePromotionRestrictionDao;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.promotions.model.AbstractPromotionRestrictionModel;
import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateIfSingleResult;
import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;
import static java.lang.String.format;


public class DefaultCommercePromotionRestrictionService extends AbstractBusinessService implements
		CommercePromotionRestrictionService
{
	private CommercePromotionRestrictionDao commercePromotionRestrictionDao;
	private ModelService modelService;

	@Override
	public List<AbstractPromotionRestrictionModel> getPromotionRestrictions(final AbstractPromotionModel promotion)
	{
		validateParameterNotNull(promotion, "Parameter promotion must not be null");
		return getCommercePromotionRestrictionDao().findPromotionRestriction(promotion);
	}

	@Override
	public PromotionOrderRestrictionModel getPromotionOrderRestriction(final AbstractPromotionModel promotion)
	{
		validateParameterNotNull(promotion, "Parameter promotion must not be null");
		final List<PromotionOrderRestrictionModel> restrictions = getCommercePromotionRestrictionDao()
				.findPromotionOrderRestriction(promotion);
		validateIfSingleResult(
				restrictions,
				format("PromotionOrderRestriction for promotion '%s' not found!", promotion),
				format("PromotionOrderRestriction is not unique for promotion %s, %d restrictions found!", promotion,
						Integer.valueOf(restrictions.size())));
		return restrictions.get(0);
	}

	@Override
	public void addOrderToRestriction(final PromotionOrderRestrictionModel restriction, final AbstractOrderModel order)
	{
		if (!isOrderInRestriction(restriction, order))
		{
			final Collection<AbstractOrderModel> orders = new HashSet<AbstractOrderModel>(restriction.getOrders());
			orders.add(order);
			restriction.setOrders(orders);
			getModelService().save(restriction);
		}
	}

	@Override
	public void removeOrderFromRestriction(final PromotionOrderRestrictionModel restriction, final AbstractOrderModel order)
	{
		if (isOrderInRestriction(restriction, order))
		{
			final Collection<AbstractOrderModel> orders = new HashSet<AbstractOrderModel>(restriction.getOrders());
			orders.remove(order);
			restriction.setOrders(orders);
			getModelService().save(restriction);
		}
	}

	protected boolean isOrderInRestriction(final PromotionOrderRestrictionModel restriction, final AbstractOrderModel order)
	{
		return restriction.getOrders().contains(order);
	}

	protected CommercePromotionRestrictionDao getCommercePromotionRestrictionDao()
	{
		return commercePromotionRestrictionDao;
	}

	@Required
	public void setCommercePromotionRestrictionDao(final CommercePromotionRestrictionDao commercePromotionRestrictionDao)
	{
		this.commercePromotionRestrictionDao = commercePromotionRestrictionDao;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}
}

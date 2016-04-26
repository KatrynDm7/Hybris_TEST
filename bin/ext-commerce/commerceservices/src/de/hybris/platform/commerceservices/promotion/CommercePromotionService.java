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
package de.hybris.platform.commerceservices.promotion;

import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.promotions.model.OrderPromotionModel;
import de.hybris.platform.promotions.model.ProductPromotionModel;
import de.hybris.platform.promotions.model.PromotionGroupModel;

import java.util.Collection;
import java.util.List;


/**
 * Service for getting information about promotions
 * 
 * @spring.bean commercePromotionService
 * 
 */
public interface CommercePromotionService
{
	/**
	 * Get the list of {@link ProductPromotionModel} instances
	 * 
	 * @return The list of {@link ProductPromotionModel}
	 */
	List<ProductPromotionModel> getProductPromotions();

	/**
	 * Get the list of {@link OrderPromotionModel} instances.
	 * 
	 * @return The list of {@link OrderPromotionModel}
	 */
	List<OrderPromotionModel> getOrderPromotions();

	/**
	 * Get list of {@link ProductPromotionModel} instances that belong to one of given {@link PromotionGroupModel}
	 * 
	 * @param promotionGroups
	 *           The promotion groups to evaluate
	 * @return The list of {@link ProductPromotionModel}
	 * 
	 * @throws IllegalArgumentException
	 *            if parameter promotionGroups is <code>null</code>
	 */
	List<ProductPromotionModel> getProductPromotions(Collection<PromotionGroupModel> promotionGroups);

	/**
	 * Get list of {@link OrderPromotionModel} instances instances that belong to one of given
	 * {@link PromotionGroupModel}
	 * 
	 * @param promotionGroups
	 *           The promotion groups to evaluate
	 * @return The list of {@link OrderPromotionModel}
	 * 
	 * @throws IllegalArgumentException
	 *            if parameter promotionGroups is <code>null</code>
	 */
	List<OrderPromotionModel> getOrderPromotions(Collection<PromotionGroupModel> promotionGroups);


	/**
	 * Get {@link AbstractPromotionModel} instance base on its code
	 * 
	 * @param code
	 *           - promotion identifier
	 * @return {@link AbstractPromotionModel} instance
	 * 
	 * @throws de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException
	 *            if no Promotion with the specified code is found
	 * @throws de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException
	 *            if more than one Promotion with the specified code is found
	 * @throws IllegalArgumentException
	 *            if parameter code is <code>null</code>
	 */
	AbstractPromotionModel getPromotion(String code);
}
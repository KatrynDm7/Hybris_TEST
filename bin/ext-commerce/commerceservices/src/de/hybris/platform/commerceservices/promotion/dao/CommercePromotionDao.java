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
package de.hybris.platform.commerceservices.promotion.dao;

import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.promotions.model.OrderPromotionModel;
import de.hybris.platform.promotions.model.ProductPromotionModel;
import de.hybris.platform.promotions.model.PromotionGroupModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;

import java.util.Collection;
import java.util.List;


/**
 * Dao for {@link AbstractPromotionModel} access.
 * 
 * @spring.bean commercePromotionDao
 * 
 */
public interface CommercePromotionDao extends Dao
{
	/**
	 * Find the promotion by given code.
	 * 
	 * @param code
	 *           - promotion identifier
	 * @return - found promotions list or empty list
	 * 
	 * @throws IllegalArgumentException
	 *            if the given <code>code</code> is <code>null</code>
	 */
	List<AbstractPromotionModel> findPromotionForCode(String code);

	/**
	 * Find all product promotions from promotion group
	 * 
	 * @param promotionGroups
	 *           The promotion groups to evaluate
	 * @return all product promotions
	 * 
	 * @throws IllegalArgumentException
	 *            if the given <code>promotionGroups</code> is <code>null</code> or empty
	 */
	List<ProductPromotionModel> findProductPromotions(Collection<PromotionGroupModel> promotionGroups);

	/**
	 * Find all order promotions from promotion group
	 * 
	 * @param promotionGroups
	 *           The promotion groups to evaluate
	 * @return all order promotions
	 * 
	 * @throws IllegalArgumentException
	 *            if the given <code>promotionGroups</code> is <code>null</code> or empty
	 */
	List<OrderPromotionModel> findOrderPromotions(Collection<PromotionGroupModel> promotionGroups);

	/**
	 * Find all product promotions
	 * 
	 * @return all product promotions
	 */
	List<ProductPromotionModel> findProductPromotions();

	/**
	 * Find all order promotions
	 * 
	 * @return all order promotions
	 */
	List<OrderPromotionModel> findOrderPromotions();

}

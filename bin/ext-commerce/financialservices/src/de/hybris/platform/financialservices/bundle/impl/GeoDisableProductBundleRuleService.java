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
package de.hybris.platform.financialservices.bundle.impl;

import de.hybris.platform.configurablebundleservices.bundle.impl.DefaultBundleRuleService;
import de.hybris.platform.configurablebundleservices.model.AbstractBundleRuleModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.financialservices.model.DisableRuleGeoAreaModel;
import de.hybris.platform.order.CartService;

import java.util.List;
import java.util.Set;


/**
 * This class extends the {@link DefaultBundleRuleService} so that the rules for disabling items in a bundle can
 * determined based upon their geographic location.
 *
 */
public class GeoDisableProductBundleRuleService extends DefaultBundleRuleService
{
	private CartService cartService;

	/**
	 * Checks to see if any bundle rule is applicable to the address that is specified on the cart.
	 *
	 * @param rule
	 *           the bundle rule
	 * @return true if the bundle rule applies to the geographical area which is determined by the cart's addresses post
	 *         code
	 */
	protected boolean checkBundleRuleForGeoCodes(final AbstractBundleRuleModel rule)
	{

		final CartModel cartModel = getCartService().getSessionCart();
		final AddressModel addressModel = cartModel.getDeliveryAddress();

		if (addressModel == null || addressModel.getPostalcode() == null)
		{
			return false;
		}

		final String addressCode = addressModel.getPostalcode().toLowerCase();

		for (final DisableRuleGeoAreaModel disableRuleGeoAreaModel : rule.getConditionalGeoAreas())
		{
			final String possibleCode = disableRuleGeoAreaModel.getAreaCode().toLowerCase();
			if (addressCode.equals(possibleCode) || addressCode.startsWith(possibleCode))
			{
				return true;
			}
		}
		return false;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	protected AbstractBundleRuleModel evaluateBundleRules(final List<AbstractBundleRuleModel> bundleRules,
			final ProductModel product, final Set<ProductModel> otherProductsInSameBundle)
	{

		for (final AbstractBundleRuleModel disableRule : bundleRules)
		{
			if (disableRule.getConditionalGeoAreas() != null && !disableRule.getConditionalGeoAreas().isEmpty())
			{
				final boolean isRuleApplicable = checkBundleRuleForGeoCodes(disableRule);
				if (isRuleApplicable)
				{
					return disableRule;
				}
			}
		}
		// If we get here, then none of the rules apply to geo regions, so pass back to the base class
		return super.evaluateBundleRules(bundleRules, product, otherProductsInSameBundle);
	}


	/**
	 * @return the cartService
	 */
	public CartService getCartService()
	{
		return cartService;
	}

	/**
	 * @param cartService
	 *           the cartService to set
	 */
	public void setCartService(final CartService cartService)
	{
		this.cartService = cartService;
	}

}

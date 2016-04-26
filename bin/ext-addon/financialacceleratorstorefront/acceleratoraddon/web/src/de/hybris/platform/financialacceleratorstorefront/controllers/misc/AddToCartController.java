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
package de.hybris.platform.financialacceleratorstorefront.controllers.misc;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.AbstractController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceSaveCartException;
import de.hybris.platform.financialacceleratorstorefront.constants.WebConstants;
import de.hybris.platform.financialacceleratorstorefront.controllers.ControllerConstants;
import de.hybris.platform.financialacceleratorstorefront.controllers.exceptions.CommerceCartModificationUpperLimitReachedException;
import de.hybris.platform.financialfacades.facades.InsuranceCartFacade;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.servicelayer.i18n.L10NService;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * Controller for Add to Cart functionality which is not specific to a certain page.
 */
@Controller("InsuranceAddToCartController")
public class AddToCartController extends AbstractController
{

	protected static final Logger LOG = Logger.getLogger(AddToCartController.class);

	/** This constant value dictates the quantity of the Insurance product to be added in the cart. */
	protected static final long PRODUCT_QUANTITY = 1;
	protected static final String ERROR_MSG_TYPE = "errorMsg";
	protected static final String DEFAULT_BUNDLE_NO = "-1";
	protected static final String REDIRECT_TO_CART_PAGE = REDIRECT_PREFIX + "/cart";
	protected static final boolean REMOVE_CURRENT_PRODUCT = false;
	protected static final int DEFAULT_FIRST_BUNDLE = 1;
	protected static final int MAX_MESSAGE_LENGTH = 30;

	@Resource(name = "cartFacade")
	private InsuranceCartFacade cartFacade;

	@Resource
	private L10NService l10NService;

	@RequestMapping(value = "/cart/addSingleProduct", method = RequestMethod.POST, produces = "application/json")
	public String addToCart(
			@RequestParam("productCodePost") final String code,
			final Model model,
			@RequestParam(value = "removeCurrentProducts", required = false, defaultValue = "false") final boolean removeCurrentProducts,
			@RequestParam(value = "bundleNo", required = false, defaultValue = DEFAULT_BUNDLE_NO) final int bundleNo,
			@RequestParam(value = "bundleTemplateId", required = true) final String bundleTemplateId)
	{

		try
		{
			addProduct(code, model, PRODUCT_QUANTITY, bundleNo, bundleTemplateId, removeCurrentProducts);
		}
		catch (final CommerceCartModificationUpperLimitReachedException e)
		{
			LOG.warn(e.getMessage());
			model.addAttribute("CART_UPPER_LIMIT_REACHED", true);
		}

		return REDIRECT_TO_CART_PAGE;
	}

	protected int addProduct(final String code, final Model model, final long qty, final int bundleNo,
			final String bundleTemplateId, final boolean removeCurrentProducts)
			throws CommerceCartModificationUpperLimitReachedException
	{
		int bundleNoToReturn = bundleNo;

		try
		{
			final List<CartModificationData> cartModifications = cartFacade.addToCart(code, qty, bundleNo, bundleTemplateId,
					removeCurrentProducts);
            cartFacade.saveCurrentUserCart();
			model.addAttribute("modifiedCartData", cartModifications);

			for (final CartModificationData cartModification : cartModifications)
			{

				if (cartModification.getQuantityAdded() == 0L)
				{
					GlobalMessages.addErrorMessage(model,
							"basket.information.quantity.noItemsAdded." + cartModification.getStatusCode());
					model.addAttribute(ERROR_MSG_TYPE, "basket.information.quantity.noItemsAdded." + cartModification.getStatusCode());
				}
				else if (cartModification.getQuantityAdded() < qty && !removeCurrentProducts)
				{
					GlobalMessages.addErrorMessage(model,
							"basket.information.quantity.reducedNumberOfItemsAdded." + cartModification.getStatusCode());
					model.addAttribute(ERROR_MSG_TYPE,
							"basket.information.quantity.reducedNumberOfItemsAdded." + cartModification.getStatusCode());
				}

				bundleNoToReturn = cartModification.getEntry().getBundleNo();
			}
		}
		catch (final CommerceCartModificationException ex)
		{
			final String message = l10NService.getLocalizedString("bundleservices.validation.selectioncriteriaexceeded");
			// we check the exception type by using exception message.
			// Currently, the facade layer only return general CommerceCartModificationException, we can only check the message content now.
			if (StringUtils.isNotEmpty(message) && message.length() >= MAX_MESSAGE_LENGTH
					&& ex.getMessage().contains(message.substring(0, MAX_MESSAGE_LENGTH)))
			{
				throw new CommerceCartModificationUpperLimitReachedException(ex.getMessage(), ex);
			}
			if (ex.getMessage().contains("cannot be added to bundle") && ex.getMessage().contains("with root bundleTemplate"))
			{
				throw new CommerceCartModificationUpperLimitReachedException(ex.getMessage(), ex);
			}
			if (ex.getMessage().contains("already in the cart for component"))
			{
				throw new CommerceCartModificationUpperLimitReachedException(ex.getMessage(), ex);
			}
			else
			{
				model.addAttribute(ERROR_MSG_TYPE, "basket.error.occurred");
				LOG.warn("Couldn't add product of code " + code + " to cart.", ex);
			}
		} catch (final CommerceSaveCartException e)
        {
            LOG.error("Save cart error.");
        }

		final CartData cartData = cartFacade.getSessionCart();
		model.addAttribute("cartData", cartData);

		return bundleNoToReturn;
	}

	@RequestMapping(value = "/cart/addBundle", method = RequestMethod.POST, produces = "application/json")
	public String addToCartBundle(@RequestParam("productCodes") final List<String> productCodes,
			@RequestParam("bundleTemplateIds") final List<String> bundleTemplateIds,
			@RequestParam(value = "bundleNo", required = false, defaultValue = DEFAULT_BUNDLE_NO) int bundleNo, final Model model)
	{

		final CartData sessionCart = cartFacade.getSessionCart();

		// This is temporary solution only for currently only allows 1 bundle in the cart.
		// Please extends the strategy in future to give the suitable bundleNo for adding product.
		if (CollectionUtils.isNotEmpty(sessionCart.getEntries()))
		{
			bundleNo = DEFAULT_FIRST_BUNDLE;
		}

		//This is temporary implementation to add multiple products to the cart in a single request. In future this controller
		// method should delegate the method call to facade by passing list of productids and corresponding bundleids.
		// Proper validation and Exception handling should be done based on the contract with facade.
		if (validateAddToCartRequestParams(productCodes, bundleTemplateIds))
		{
			final Iterator productIdIterator = productCodes.iterator();
			final Iterator bundleId = bundleTemplateIds.iterator();

			while (productIdIterator.hasNext() && bundleId.hasNext())
			{

				final String productId = productIdIterator.next().toString();
				try
				{
					bundleNo = addProduct(productId, model, PRODUCT_QUANTITY, bundleNo, bundleId.next().toString(),
							REMOVE_CURRENT_PRODUCT);
				}
				catch (final CommerceCartModificationUpperLimitReachedException ex)
				{
					LOG.debug(ex.getMessage());

					model.addAttribute(WebConstants.ADD_TO_CART_SUCCESS, false);
					model.addAttribute(WebConstants.CART_UPPER_LIMIT_REACHED, true);

					// If the adding product in the same default category as product already in the cart,
					// it should be added without giving warning.
					try
					{
						model.addAttribute(WebConstants.SAME_INSURANCE_TYPE, cartFacade.isSameInsuranceInSessionCart(productId));
					}
					catch (final InvalidCartException e)
					{
						model.addAttribute(WebConstants.ADD_TO_CART_SUCCESS, false);
						model.addAttribute(WebConstants.CART_UPPER_LIMIT_REACHED, false);
					}

					return ControllerConstants.Views.Fragments.Cart.AddToCartFragment;
				}
			}//while
		}

		model.addAttribute(WebConstants.ADD_TO_CART_SUCCESS, true);
		return ControllerConstants.Views.Fragments.Cart.AddToCartFragment;
	}

	// Temporary implementation to validate the lists of productCodes and bundleTemplateIds whether they
	//   - are not null or empty lists
	//   - have no null or "" elements
	//   - the product codes size must be equal or less than bundle templates id size.
	//
	//  Later on this should be:
	//	   - moved to the facade layer
	//	   - extended with additional validation that checks the elements of the two lists are consistent (Product belongs to BundleTample)
	protected boolean validateAddToCartRequestParams(final List<String> productCodes, final List<String> bundleTemplateIds)
	{

		if (validateAddToCartParameterList(productCodes) && validateAddToCartParameterList(bundleTemplateIds)
				&& productCodes.size() <= bundleTemplateIds.size())
		{
			return true;
		}

		return false;
	}

	protected boolean validateAddToCartParameterList(final List<String> params)
	{
		if (CollectionUtils.isNotEmpty(params) && !params.contains(null) && !params.contains(StringUtils.EMPTY))
		{
			return true;
		}
		return false;
	}

}

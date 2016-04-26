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
package de.hybris.platform.b2ctelcostorefront.controllers.misc;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.AbstractController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.acceleratorstorefrontcommons.forms.AddToCartForm;
import de.hybris.platform.b2ctelcostorefront.controllers.TelcoControllerConstants;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.configurablebundlefacades.order.BundleCartFacade;
import de.hybris.platform.jalo.JaloObjectNoLongerValidException;
import de.hybris.platform.order.InvalidCartException;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * Controller for Add to Cart functionality which is not specific to a certain page.
 */
@Controller("TelcoAddToCartController")
public class AddToCartController extends AbstractController
{
	private static final String TYPE_MISMATCH_ERROR_CODE = "typeMismatch";
	private static final String ERROR_MSG_TYPE = "errorMsg";
	private static final String QUANTITY_INVALID_BINDING_MESSAGE_KEY = "basket.error.quantity.invalid.binding";

	protected static final Logger LOG = Logger.getLogger(AddToCartController.class);

	@Resource(name = "cartFacade")
	private BundleCartFacade cartFacade;

	@Resource(name = "telcoProductFacade")
	private ProductFacade productFacade;

	/**
	 * @param form
	 *           the AddToCartForm from the page view. Not used, but included to allow overriding of this method. the
	 *           AddToCartForm from the page view. Not used, but included to allow overriding of this method.
	 */
	@RequestMapping(value = "/cart/add", method = RequestMethod.POST, produces = "application/json")
	public String addToCart(@RequestParam("productCodePost") final String code, final Model model,
			@RequestParam(value = "qty", required = false, defaultValue = "1") final long qty,
			@RequestParam(value = "bundleNo", required = false, defaultValue = "0") final Integer bundleNo,
			@RequestParam(value = "bundleTemplateId", required = false) final String bundleTemplateId,
			@Valid final AddToCartForm form, final BindingResult bindingErrors)
	{

		if (bindingErrors.hasErrors())
		{
			return getViewWithBindingErrorMessages(model, bindingErrors);
		}

		if (qty <= 0)
		{
			model.addAttribute(ERROR_MSG_TYPE, "basket.error.quantity.invalid");
			model.addAttribute("quantity", Long.valueOf(0L));
		}
		else
		{
			addProduct(code, model, qty, bundleNo, bundleTemplateId);
		}

		model.addAttribute("product", productFacade.getProductForCodeAndOptions(code, Arrays.asList(ProductOption.BASIC)));

		return TelcoControllerConstants.Views.Fragments.Cart.AddToCartPopup;
	}

	protected void addProduct(final String code, final Model model, final long qty, final Integer bundleNo,
			final String bundleTemplateId)
	{
		try
		{
			final List<CartModificationData> cartModifications = getCartFacade().addToCart(code, qty, bundleNo.intValue(),
					bundleTemplateId, false);
			model.addAttribute("modifiedCartData", cartModifications);

			long quantity = qty;

			for (final CartModificationData cartModification : cartModifications)
			{

				if (cartModification.getQuantityAdded() == 0L)
				{
					GlobalMessages.addErrorMessage(model,
							"basket.information.quantity.noItemsAdded." + cartModification.getStatusCode());
					model.addAttribute(ERROR_MSG_TYPE, "basket.information.quantity.noItemsAdded." + cartModification.getStatusCode());
					quantity = 0;
				}
				else if (cartModification.getQuantityAdded() < qty)
				{
					GlobalMessages.addErrorMessage(model,
							"basket.information.quantity.reducedNumberOfItemsAdded." + cartModification.getStatusCode());
					model.addAttribute(ERROR_MSG_TYPE,
							"basket.information.quantity.reducedNumberOfItemsAdded." + cartModification.getStatusCode());

					quantity = cartModification.getQuantityAdded();
				}

				model.addAttribute("entry", cartModification.getEntry());
				model.addAttribute("cartCode", cartModification.getCartCode());
			}

			model.addAttribute("quantity", Long.valueOf(quantity));


		}
		catch (final CommerceCartModificationException ex)
		{
			model.addAttribute("errorMsg", "basket.error.occurred");
			LOG.warn("Couldn't add product of code " + code + " to cart.", ex);
		}

		final CartData cartData = getCartFacade().getSessionCart();
		model.addAttribute("cartData", cartData);
	}


	@RequestMapping(value = "/cart/addBundle", method = RequestMethod.POST)
	public String addToCartBundle(@RequestParam("productCode1") final String productCode1,
			@RequestParam("productCode2") final String productCode2, final Model model,
			@RequestParam(value = "bundleTemplateId1", required = false) final String bundleTemplateId1,
			@RequestParam(value = "bundleTemplateId2", required = false) final String bundleTemplateId2)
	{
		String bundleId = null;
		try
		{
			final List<CartModificationData> cartModifications = getCartFacade().addToCart(productCode1, -1, bundleTemplateId1,
					productCode2, bundleTemplateId2);
			model.addAttribute("modifiedCartData", cartModifications);

			for (final CartModificationData cartModification : cartModifications)
			{
				if (cartModification.getEntry() == null)
				{
					GlobalMessages.addErrorMessage(model, "basket.information.quantity.noItemsAdded");
					model.addAttribute("errorMsg", "basket.information.quantity.noItemsAdded");
					throw new CommerceCartModificationException("Cart entry was not created. Reason: "
							+ cartModification.getStatusCode());
				}
				else
				{
					bundleId = String.valueOf(cartModification.getEntry().getBundleNo());
				}
			}
		}
		catch (final CommerceCartModificationException ex)
		{
			model.addAttribute("errorMsg", "basket.error.occurred");
			LOG.error("Couldn't add products of code " + productCode1 + " and " + productCode2 + " to cart.", ex);
			// we have no bundleId here, so we cannot redirect to extras-page; go to cart page instead.
			return REDIRECT_PREFIX + "/cart";
		}

		return REDIRECT_PREFIX + "/bundle/edit-component/" + bundleId + "/nextcomponent/" + bundleTemplateId2;
	}

	/**
	 * Add product to cart.
	 *
	 * @param code
	 * @return
	 * @throws CommerceCartModificationException
	 * @throws InvalidCartException
	 */
	@RequestMapping(value = "/sbgaddtocart", method = RequestMethod.POST, produces = "application/json")
	public String postAddToCart(@RequestParam("productCodePost") final String code, final Model model)
			throws CommerceCartModificationException, InvalidCartException
	{
		Long quantityAdded = null;
		try
		{
			final CartModificationData cartModification = getCartFacade().addToCart(code, 1);
			quantityAdded = Long.valueOf(cartModification.getQuantityAdded());
		}
		catch (final JaloObjectNoLongerValidException ex)
		{
			LOG.warn(String.format("Couldn't update product with the code: %s, quantity: %d.", code, Integer.valueOf(1)), ex);
			quantityAdded = new Long(0);
		}
		model.addAttribute("quantity", quantityAdded);
		model.addAttribute("product", productFacade.getProductForCodeAndOptions(code, Arrays.asList(ProductOption.BASIC)));
		return TelcoControllerConstants.Views.Fragments.Cart.AddToCartPopup;
	}

	protected String getViewWithBindingErrorMessages(final Model model, final BindingResult bindingErrors)
	{
		for (final ObjectError error : bindingErrors.getAllErrors())
		{
			if (isTypeMismatchError(error))
			{
				model.addAttribute(ERROR_MSG_TYPE, QUANTITY_INVALID_BINDING_MESSAGE_KEY);
			}
			else
			{
				model.addAttribute(ERROR_MSG_TYPE, error.getDefaultMessage());
			}
		}
		return TelcoControllerConstants.Views.Fragments.Cart.AddToCartPopup;
	}

	protected boolean isTypeMismatchError(final ObjectError error)
	{
		return error.getCode().equals(TYPE_MISMATCH_ERROR_CODE);
	}

	protected BundleCartFacade getCartFacade()
	{
		return cartFacade;
	}

	protected void setCartFacade(final BundleCartFacade cartFacade)
	{
		this.cartFacade = cartFacade;
	}
}

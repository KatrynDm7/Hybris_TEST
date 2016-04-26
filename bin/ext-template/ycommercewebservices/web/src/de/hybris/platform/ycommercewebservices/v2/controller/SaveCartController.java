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
package de.hybris.platform.ycommercewebservices.v2.controller;

import de.hybris.platform.commercefacades.order.SaveCartFacade;
import de.hybris.platform.commercefacades.order.data.CommerceSaveCartParameterData;
import de.hybris.platform.commercefacades.order.data.CommerceSaveCartResultData;
import de.hybris.platform.commerceservices.order.CommerceSaveCartException;
import de.hybris.platform.commercewebservicescommons.dto.order.SaveCartResultWsDTO;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Controller for saved cart related requests such as saving a cart or retrieving/restoring/... a saved cart
 *
 * @pathparam userId User identifier or one of the literals below :
 *            <ul>
 *            <li>'current' for currently authenticated user</li>
 *            <li>'anonymous' for anonymous user</li>
 *            </ul>
 * @pathparam cartId Cart identifier
 *            <ul>
 *            <li>cart code for logged in user</li>
 *            <li>cart guid for anonymous user</li>
 *            <li>'current' for the last modified cart</li>
 *            </ul>
 */
@Controller
@RequestMapping(value = "/{baseSiteId}/users/{userId}/carts")
public class SaveCartController extends BaseCommerceController
{
	@Resource(name = "saveCartFacade")
	private SaveCartFacade saveCartFacade;

	/**
	 * Explicitly saves a cart
	 *
	 * @formparam saveCartName the name that should be applied to the saved cart
	 * @formparam saveCartDescription the description that should be applied to the saved cart
	 * @queryparam fields Response configuration (list of fields, which should be returned in response)
	 * @return result of the save cart call (includes the id/code of the saved cart)
	 */
	@RequestMapping(value = "/{cartId}/save", method = RequestMethod.PATCH)
	@ResponseBody
	public SaveCartResultWsDTO saveCart(@PathVariable final String cartId,
			@RequestParam(value = "saveCartName", required = false) final String saveCartName,
			@RequestParam(value = "saveCartDescription", required = false) final String saveCartDescription,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields) throws CommerceSaveCartException
	{
		final CommerceSaveCartParameterData parameters = new CommerceSaveCartParameterData();
		parameters.setCartId(cartId);
		parameters.setName(saveCartName);
		parameters.setDescription(saveCartDescription);

		final CommerceSaveCartResultData result = saveCartFacade.saveCart(parameters);
		return dataMapper.map(result, SaveCartResultWsDTO.class, fields);
	}

	@RequestMapping(value = "/{cartId}/restoresavedcart", method = RequestMethod.PATCH)
	@ResponseBody
	public SaveCartResultWsDTO restoreSavedCart(@PathVariable final String cartId,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields) throws CommerceSaveCartException
	{
		final CommerceSaveCartParameterData parameters = new CommerceSaveCartParameterData();
		parameters.setCartId(cartId);
		saveCartFacade.restoreSavedCart(parameters);

		final CommerceSaveCartResultData result = new CommerceSaveCartResultData();
		result.setSavedCartData(getSessionCart());
		return dataMapper.map(result, SaveCartResultWsDTO.class, fields);
	}

	/**
	 * Flags a cart for deletion (the cart doesn't have corresponding save cart attributes anymore). The cart is not
	 * actually deleted from the database. But with the removal of the saved cart attributes, this cart will be taken
	 * care of by the cart removal job just like any other cart.
	 *
	 * @queryparam fields Response configuration (list of fields, which should be returned in response)
	 * @return result of the flag cart for deletion call
	 */
	@RequestMapping(value = "/{cartId}/flagForDeletion", method = RequestMethod.PATCH)
	@ResponseBody
	public SaveCartResultWsDTO flagForDeletion(@PathVariable final String cartId,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields) throws CommerceSaveCartException
	{
		final CommerceSaveCartResultData result = saveCartFacade.flagForDeletion(cartId);
		return dataMapper.map(result, SaveCartResultWsDTO.class, fields);
	}

	/**
	 * Returns saved cart by it id for authenticated user
	 *
	 * @queryparam fields Response configuration (list of fields, which should be returned in response)
	 * @return result of the get saved cart call
	 */
	@RequestMapping(value = "/{cartId}/savedcart", method = RequestMethod.GET)
	@ResponseBody
	public SaveCartResultWsDTO getSavedCart(@PathVariable final String cartId,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields) throws CommerceSaveCartException
	{
		final CommerceSaveCartParameterData parameters = new CommerceSaveCartParameterData();
		parameters.setCartId(cartId);

		final CommerceSaveCartResultData result = saveCartFacade.getCartForCodeAndCurrentUser(parameters);
		return dataMapper.map(result, SaveCartResultWsDTO.class, fields);
	}


	/**
	 * Explicitly clones a cart
	 *
	 * @formparam name the name that should be applied to the cloned cart
	 * @formparam description the description that should be applied to the cloned cart
	 * @queryparam fields Response configuration (list of fields, which should be returned in response)
	 * @return result of the clone cart call (includes the cloned cart)
	 */
	@RequestMapping(value = "/{cartId}/clonesavedcart", method = RequestMethod.POST)
	@ResponseBody
	public SaveCartResultWsDTO cloneSaveCart(@PathVariable final String cartId,
			@RequestParam(value = "name", required = false) final String name,
			@RequestParam(value = "description", required = false) final String description,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields) throws CommerceSaveCartException
	{
		final CommerceSaveCartParameterData parameters = new CommerceSaveCartParameterData();
		parameters.setCartId(cartId);
		parameters.setName(name);
		parameters.setDescription(description);

		final CommerceSaveCartResultData result = saveCartFacade.cloneSavedCart(parameters);
		return dataMapper.map(result, SaveCartResultWsDTO.class, fields);
	}
}

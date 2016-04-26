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
package de.hybris.platform.b2b.occ.v2.controllers;

import de.hybris.platform.b2b.occ.security.SecuredAccessConstants;
import de.hybris.platform.b2bacceleratorfacades.api.cart.CartFacade;
import de.hybris.platform.b2bacceleratorfacades.order.data.B2BCostCenterData;
import de.hybris.platform.b2bacceleratorfacades.order.data.B2BPaymentTypeData;
import de.hybris.platform.commercefacades.order.CheckoutFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.order.data.CartModificationDataList;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commerceservices.request.mapping.annotation.ApiVersion;
import de.hybris.platform.commerceservices.request.mapping.annotation.RequestMappingOverride;
import de.hybris.platform.commercewebservicescommons.dto.order.CartListWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.order.CartModificationListWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.order.CartModificationWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.order.CartWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.order.OrderEntryListWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.order.OrderEntryWsDTO;
import de.hybris.platform.commercewebservicescommons.mapping.DataMapper;
import de.hybris.platform.commercewebservicescommons.mapping.FieldSetLevelHelper;
import de.hybris.platform.ycommercewebservices.exceptions.NoCheckoutCartException;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


@Controller
@RequestMapping(value = "/{baseSiteId}/users/{userId}/carts")
@ApiVersion("v2")
public class B2BCartsController
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(B2BCartsController.class);

	@Resource(name = "cartFacade")
	private CartFacade cartFacade;

	@Resource(name = "userFacade")
	protected UserFacade userFacade;

	protected static final String DEFAULT_FIELD_SET = FieldSetLevelHelper.DEFAULT_LEVEL;

	@Resource(name = "dataMapper")
	protected DataMapper dataMapper;

	/**
	 * Adds more quantity to the cart for a specific entry based on it's product code, if the product is already in the
	 * cart the amount will be added to the existing quantity.
	 * 
	 * @param baseSiteId
	 *           the id of the site.
	 * @param product
	 *           code of the product to be added to the cart.
	 * @param quantity
	 *           amount to be added.
	 * @param fields
	 *           level mapping configuration.
	 * @return the results of the cart modification.
	 */
	@RequestMappingOverride(priorityProperty = "b2bocc.CartResource.addCartEntry.priority")
	@RequestMapping(value = "/{cartId}/entries", method = RequestMethod.POST)
	@ResponseBody
	public CartModificationWsDTO addCartEntry(@PathVariable final String baseSiteId,
			@RequestParam(required = true) final String product,
			@RequestParam(required = false, defaultValue = "1") final long quantity,
			@RequestParam(required = false, defaultValue = FieldSetLevelHelper.DEFAULT_LEVEL) final String fields)
	{

		final OrderEntryData orderEntry = getOrderEntryData(quantity, product, null);

		return dataMapper.map(cartFacade.addOrderEntry(orderEntry), CartModificationWsDTO.class, fields);
	}

	/**
	 * Updates the total amount of a specific product in the cart based on the entryNumber.
	 * 
	 * @param baseSiteId
	 *           the id of the site.
	 * @param entryNumber
	 *           the id of the entry in the cart.
	 * @param quantity
	 *           new quantity for this entry.
	 * @param fields
	 *           level mapping configuration.
	 * @return the results of the cart modification.
	 */
	@RequestMappingOverride(priorityProperty = "b2bocc.CartResource.updateCartEntry.priority")
	@RequestMapping(value = "/{cartId}/entries/{entryNumber}", method = RequestMethod.PUT)
	@ResponseBody
	public CartModificationWsDTO updateCartEntry(@PathVariable final String baseSiteId, @PathVariable final int entryNumber,
			@RequestParam(required = true) final Long quantity,
			@RequestParam(required = false, defaultValue = FieldSetLevelHelper.DEFAULT_LEVEL) final String fields)
	{

		return updateCartEntry(baseSiteId, null, entryNumber, quantity, fields);
	}

	/**
	 * Updates the total amount of a specific product in the cart based either in the product code or the entryNumber.
	 * 
	 * @param baseSiteId
	 *           the id of the site.
	 * @param product
	 *           code of the product to be added to the cart, this code is not considered if an entryNumber is passed.
	 * @param entryNumber
	 *           the id of the entry in the cart, this parameter takes precedence over the product code.
	 * @param quantity
	 *           new quantity for this product.
	 * @param fields
	 *           level mapping configuration.
	 * @return a list of containing the result for each intended cart modification.
	 */
	@RequestMappingOverride(priorityProperty = "b2bocc.CartResource.updateCartEntryByProduct.priority")
	@RequestMapping(value = "/{cartId}/entries/", method = RequestMethod.PUT)
	@ResponseBody
	public CartModificationWsDTO updateCartEntry(@PathVariable final String baseSiteId,
			@RequestParam(required = false) final String product, @RequestParam(required = false) final Integer entryNumber,
			@RequestParam(required = true) final Long quantity,
			@RequestParam(required = false, defaultValue = FieldSetLevelHelper.DEFAULT_LEVEL) final String fields)
	{

		final OrderEntryData orderEntry = getOrderEntryData(quantity, product, entryNumber);

		return dataMapper.map(cartFacade.updateOrderEntry(orderEntry), CartModificationWsDTO.class, fields);
	}

	/**
	 * Adds more quantity to the cart of specific products in the cart based either in the product code or the
	 * entryNumber.
	 * 
	 * @param baseSiteId
	 *           the id of the site.
	 * @param fields
	 *           level mapping configuration.
	 * @param entries
	 *           list of entries containing the amount to add and the product code or the entryNumber.
	 * @return a list of containing the result for each intended cart modification.
	 */
	@RequestMappingOverride(priorityProperty = "b2bocc.CartResource.addCartEntries.priority")
	@RequestMapping(value = "/{cartId}/entries/", method = RequestMethod.POST, consumes =
	{ MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public CartModificationListWsDTO addCartEntries(@PathVariable final String baseSiteId,
			@RequestParam(required = false, defaultValue = FieldSetLevelHelper.DEFAULT_LEVEL) final String fields,
			@RequestBody(required = true) final OrderEntryListWsDTO entries)
	{

		final List<OrderEntryData> cartEntriesData = convertToData(entries);
		final List<CartModificationData> resultList = cartFacade.addOrderEntryList(cartEntriesData);

		return dataMapper.map(getCartModificationDataList(resultList), CartModificationListWsDTO.class, fields);

	}

	/**
	 * Updates the quantity for specific products in the cart based either in the product code or the entryNumber.
	 * 
	 * @param baseSiteId
	 *           the id of the site.
	 * @param fields
	 *           level mapping configuration.
	 * @param entries
	 *           list of entries containing the amount to add and the product code or the entryNumber.
	 * @return a list of containing the result for each intended cart modification.
	 */
	@RequestMappingOverride(priorityProperty = "b2bocc.CartResource.updateCartEntries.priority")
	@RequestMapping(value = "/{cartId}/entries/", method = RequestMethod.PUT, consumes =
	{ MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public CartModificationListWsDTO updateCartEntries(@PathVariable final String baseSiteId,
			@RequestParam(required = false, defaultValue = FieldSetLevelHelper.DEFAULT_LEVEL) final String fields,
			@RequestBody(required = true) final OrderEntryListWsDTO entries)
	{

		final List<OrderEntryData> cartEntriesData = convertToData(entries);
		final List<CartModificationData> resultList = cartFacade.updateOrderEntryList(cartEntriesData);

		return dataMapper.map(getCartModificationDataList(resultList), CartModificationListWsDTO.class, fields);

	}

	/**
	 * Set the cost center for the checkout cart
	 * 
	 * @param costCenterId
	 * @param fields
	 * @return CartWsDTO
	 */
	@Secured(
	{ SecuredAccessConstants.ROLE_CUSTOMERGROUP, SecuredAccessConstants.ROLE_GUEST,
			SecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, SecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@RequestMapping(value = "/{cartId}/costcenter", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public CartWsDTO setCartCostCenter(@RequestParam(required = true) final String costCenterId,
			@RequestParam(required = false, defaultValue = "DEFAULT") final String fields)
	{
		final B2BCostCenterData costCenter = new B2BCostCenterData();
		costCenter.setCode(costCenterId);

		CartData cartData = new CartData();
		cartData.setCostCenter(costCenter);

		cartData = cartFacade.update(cartData);

		return dataMapper.map(cartData, CartWsDTO.class, fields);
	}

	/**
	 * Set the payment type for the checkout cart; if purchaseOrderNumber is not null, also set it to the cart
	 * 
	 * @param paymentType
	 * @param purchaseOrderNumber
	 * @param fields
	 * @return CartWsDTO
	 */
	@Secured(
	{ SecuredAccessConstants.ROLE_CUSTOMERGROUP, SecuredAccessConstants.ROLE_GUEST,
			SecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, SecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@RequestMapping(value = "/{cartId}/paymenttype", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public CartWsDTO setPaymentType(@RequestParam(required = true) final String paymentType,
			@RequestParam(required = false) final String purchaseOrderNumber,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final B2BPaymentTypeData paymentTypeData = new B2BPaymentTypeData();
		paymentTypeData.setCode(paymentType);

		CartData cartData = new CartData();
		cartData.setPaymentType(paymentTypeData);

		if (purchaseOrderNumber != null)
		{
			cartData.setPurchaseOrderNumber(purchaseOrderNumber);
		}

		cartData = cartFacade.update(cartData);

		return dataMapper.map(cartData, CartWsDTO.class, fields);
	}

	/**
	 * Set delivery address for the checkout cart
	 * 
	 * @param addressId
	 * @param fields
	 * @return CartWsDTO
	 * @throws NoCheckoutCartException
	 */
	@Secured(
	{ SecuredAccessConstants.ROLE_CUSTOMERGROUP, SecuredAccessConstants.ROLE_GUEST,
			SecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, SecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@RequestMapping(value = "/{cartId}/addresses/delivery", method = RequestMethod.PUT)
	@RequestMappingOverride(priorityProperty = "b2bocc.B2BOrdersController.setCartDeliveryAddress.priority")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public CartWsDTO setCartDeliveryAddress(@RequestParam(required = true) final String addressId,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields) throws NoCheckoutCartException
	{
		final AddressData address = new AddressData();
		address.setId(addressId);

		CartData cartData = new CartData();
		cartData.setDeliveryAddress(address);

		cartData = cartFacade.update(cartData);

		return dataMapper.map(cartData, CartWsDTO.class, fields);
	}


	@RequestMapping(value = "/current", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public CartWsDTO getCurrentCart(@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
	{

		if (userFacade.isAnonymousUser())
		{
			throw new AccessDeniedException("Access is denied");
		}


		return dataMapper.map(cartFacade.getCurrentCart(), CartWsDTO.class, fields);

	}


	protected CartModificationDataList getCartModificationDataList(final List<CartModificationData> result)
	{
		final CartModificationDataList cartModificationDataList = new CartModificationDataList();
		cartModificationDataList.setCartModificationList(result);
		return cartModificationDataList;
	}

	//TODO: Do this mapping automatically or put in a populator.
	private List<OrderEntryData> convertToData(final OrderEntryListWsDTO entriesWS)
	{
		final List<OrderEntryData> entriesData = new ArrayList<>();

		for (final OrderEntryWsDTO entryDto : entriesWS.getOrderEntries())
		{
			final OrderEntryData entryData = getOrderEntryData(entryDto.getQuantity(), entryDto.getProduct().getCode(),
					entryDto.getEntryNumber());
			entriesData.add(entryData);
		}

		return entriesData;
	}

	protected OrderEntryData getOrderEntryData(final long quantity, final String productCode, final Integer entryNumber)
	{

		final OrderEntryData orderEntry = new OrderEntryData();
		orderEntry.setQuantity(quantity);
		orderEntry.setProduct(new ProductData());
		orderEntry.getProduct().setCode(productCode);
		orderEntry.setEntryNumber(entryNumber);

		return orderEntry;
	}


}

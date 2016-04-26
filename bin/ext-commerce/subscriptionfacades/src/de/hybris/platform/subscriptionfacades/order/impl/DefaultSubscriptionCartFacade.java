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
package de.hybris.platform.subscriptionfacades.order.impl;

import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.order.impl.DefaultCartFacade;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.subscriptionfacades.SubscriptionFacade;
import de.hybris.platform.subscriptionfacades.converters.SubscriptionXStreamAliasConverter;
import de.hybris.platform.subscriptionfacades.order.SubscriptionCartFacade;
import de.hybris.platform.subscriptionservices.subscription.SubscriptionCommerceCartService;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import javax.annotation.Nonnull;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default subscription cart facade.
 */
public class DefaultSubscriptionCartFacade extends DefaultCartFacade implements SubscriptionCartFacade
{

	private ProductFacade productFacade;
	private SubscriptionCommerceCartService subscriptionCommerceCartService;
	private SubscriptionXStreamAliasConverter subscriptionXStreamAliasConverter;
	protected static final Collection<ProductOption> PRODUCT_XML_OPTIONS = Collections.unmodifiableList(Arrays.asList(
			ProductOption.BASIC, ProductOption.PRICE));
	private ModelService modelService;
	private SubscriptionFacade subscriptionFacade;

	@Override
	@Nonnull
	public CartModificationData addToCart(@Nonnull final String productCode, final long quantity)
			throws CommerceCartModificationException
	{
		final CartModel cartModel = getCartService().getSessionCart();
		final ProductModel product = getProductService().getProductForCode(productCode);

		final String xml = getProductAsXML(product);

		final CommerceCartModification modification = getSubscriptionCommerceCartService().addToCart(cartModel, product, quantity,
				product.getUnit(), false, xml);

		return getCartModificationConverter().convert(modification);
	}

	@Override
	@Nonnull
	public CartModificationData addToCart(@Nonnull final String productCode, @Nonnull final String originalSubscriptionId,
			@Nonnull final String originalOrderCode, final int originalEntryNumber) throws CommerceCartModificationException
	{
		final CartModel cartModel = getCartService().getSessionCart();
		final ProductModel product = getProductService().getProductForCode(productCode);

		final AbstractOrderEntryModel originalOrderEntry = getSubscriptionFacade().getOrderEntryForOrderCodeAndEntryNumber(
				originalOrderCode, originalEntryNumber);
		if (originalOrderEntry == null)
		{
			throw new CommerceCartModificationException(String.format("Cannot find order entry for code '%s' and entryNumber '%s'",
					originalOrderCode, String.valueOf(originalEntryNumber)));
		}

		final String xml = getProductAsXML(product);

		final CommerceCartModification modification = getSubscriptionCommerceCartService().addToCart(cartModel, product, 1,
				product.getUnit(), false, xml, originalSubscriptionId, originalOrderEntry);

		return getCartModificationConverter().convert(modification);
	}

	@Override
	public void refreshProductXMLs()
	{
		final CartModel cartModel = getCartService().getSessionCart();

		for (final AbstractOrderEntryModel orderEntryModel : cartModel.getEntries())
		{
			final ProductModel product = getProductService().getProductForCode(orderEntryModel.getProduct().getCode());

			final String xml = getProductAsXML(product);
			orderEntryModel.setXmlProduct(xml);
			modelService.save(orderEntryModel);
		}
	}

	/*
	 * Converts a product model into XML using the standard populators and stream aliases
	 */
	protected String getProductAsXML(final ProductModel product)
	{
		final ProductData productData = getProductFacade().getProductForOptions(product, PRODUCT_XML_OPTIONS);
		return getSubscriptionXStreamAliasConverter().getXStreamXmlFromSubscriptionProductData(productData);
	}



	@Required
	public void setSubscriptionCommerceCartService(final SubscriptionCommerceCartService subscriptionCommerceCartService)
	{
		this.subscriptionCommerceCartService = subscriptionCommerceCartService;
	}

	public SubscriptionCommerceCartService getSubscriptionCommerceCartService()
	{
		return subscriptionCommerceCartService;
	}

	protected ProductFacade getProductFacade()
	{
		return productFacade;
	}

	@Required
	public void setProductFacade(final ProductFacade productFacade)
	{
		this.productFacade = productFacade;
	}

	protected SubscriptionXStreamAliasConverter getSubscriptionXStreamAliasConverter()
	{
		return subscriptionXStreamAliasConverter;
	}

	@Required
	public void setSubscriptionXStreamAliasConverter(final SubscriptionXStreamAliasConverter subscriptionXStreamAliasConverter)
	{
		this.subscriptionXStreamAliasConverter = subscriptionXStreamAliasConverter;
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

	protected SubscriptionFacade getSubscriptionFacade()
	{
		return subscriptionFacade;
	}

	@Required
	public void setSubscriptionFacade(final SubscriptionFacade subscriptionFacade)
	{
		this.subscriptionFacade = subscriptionFacade;
	}



}

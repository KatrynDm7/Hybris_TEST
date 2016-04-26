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
package de.hybris.platform.financialfacades.strategies.impl;

import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.financialfacades.strategies.InsuranceAddToCartStrategy;
import de.hybris.platform.financialservices.model.InsuranceQuoteModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.xyformsservices.exception.YFormServiceException;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * The class of AbstractInsuranceAddToCartStrategy.
 */
public abstract class AbstractInsuranceAddToCartStrategy implements InsuranceAddToCartStrategy
{
	private static final Logger LOG = Logger.getLogger(AbstractInsuranceAddToCartStrategy.class);
	protected static final String PROPERTY_PRODUCT_CODE = "productCode";
	protected static final String PROPERTY_BUNDLE_NO = "bundleNo";

	protected String defaultCategoryCode;

	private ModelService modelService;

	private ProductService productService;

	private SessionService sessionService;

	private CartService cartService;

	@Override
	public void addToCart(final Map<String, Object> properties)
	{
		if (canExecuteStrategy(properties))
		{
			addToCartInternal(properties);
		}
	}

	protected boolean canExecuteStrategy(final Map<String, Object> properties)
	{

		if (properties.containsKey(PROPERTY_PRODUCT_CODE))
		{
			try
			{
				final ProductModel product = getProductService().getProductForCode((String) properties.get(PROPERTY_PRODUCT_CODE));

				if (product.getDefaultCategory() != null)
				{
					return getDefaultCategoryCode().equalsIgnoreCase(product.getDefaultCategory().getCode());
				}
			}
			catch (final Exception e)
			{
				LOG.error(e.getMessage(), e);
			}
		}

		return false;
	}

	protected void persistInsuranceInformation()
	{
		final CartModel cart = getCartService().hasSessionCart() ? getCartService().getSessionCart() : null;

		InsuranceQuoteModel quoteModel = null;

		if (cart != null && cart.getInsuranceQuote() != null)
		{
			quoteModel = cart.getInsuranceQuote();
		}
		else if (cart != null)
		{
			quoteModel = getModelService().create(InsuranceQuoteModel.class);
			cart.setInsuranceQuote(quoteModel);
		}

		if (quoteModel != null)
		{
			try
			{
				populateInsuranceDetailsInformation(quoteModel);

				getModelService().save(quoteModel);
				getModelService().save(cart);
			}
			catch (final YFormServiceException e)
			{
				LOG.error(e.getMessage(), e);
			}
		}

	}

	protected abstract void populateInsuranceDetailsInformation(final InsuranceQuoteModel quoteModel) throws YFormServiceException;

	protected abstract void addToCartInternal(final Map<String, Object> properties);

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	protected ProductService getProductService()
	{
		return productService;
	}

	@Required
	public void setProductService(final ProductService productService)
	{
		this.productService = productService;
	}

	protected SessionService getSessionService()
	{
		return sessionService;
	}

	@Required
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	protected CartService getCartService()
	{
		return cartService;
	}

	@Required
	public void setCartService(final CartService cartService)
	{
		this.cartService = cartService;
	}

	@Required
	public void setDefaultCategoryCode(final String defaultCategoryCode)
	{
		this.defaultCategoryCode = defaultCategoryCode;
	}

	public String getDefaultCategoryCode()
	{
		if (StringUtils.isEmpty(defaultCategoryCode))
		{
			LOG.warn("No default category code find in insurance add to cart strategy.");
		}
		return defaultCategoryCode;
	}
}

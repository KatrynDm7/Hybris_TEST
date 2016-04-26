/*
 *
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
 */
package de.hybris.platform.financialfacades.strategies.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.commercefacades.insurance.data.InsurancePolicyListingData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.financialfacades.strategies.CustomerInsurancePolicyStrategy;
import de.hybris.platform.financialservices.model.InsurancePolicyModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.user.UserService;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Required;


public class DefaultCustomerInsurancePolicyStrategy implements CustomerInsurancePolicyStrategy
{
	private UserService userService;

	private Converter<InsurancePolicyModel, InsurancePolicyListingData> policyListingConverter;
	private Converter<ProductModel, ProductData> productConverter;

	/**
	 * Retrieves the 'lightweight' policy DTO beans for a given customer ID. These beans only contain the bare minimum
	 * for rendering summary information on the UI (such as the 'Your Policies' on the homepage and the policy listing
	 * section on the account page) and is sorted in expiry date order.
	 *
	 * @param uid
	 *           the User ID to be used to determine the list of DTO beans
	 * @return a list of policy summary data beans sorted by expiry date
	 */
	@Override
	public List<InsurancePolicyListingData> getPolicyDataForUID(final String uid)
	{
		validateParameterNotNullStandardMessage("uid", uid);

		final UserModel userModel = getUserService().getUserForUID(uid);

		return getPolicyDataForUser(userModel);
	}

	/**
	 * Retrieves the 'lightweight' policy DTO beans for the current user. This method effectively wraps the
	 * <code>getPolicyDataForUser()</code> method by first looking up the user model for the current user then calling
	 * the main method
	 *
	 * @return a list of policy summary data beans sorted by expiry date
	 */
	@Override
	public List<InsurancePolicyListingData> getPolicyDataForCurrentCustomer()
	{
		final UserModel currentUser = getUserService().getCurrentUser();

		return getPolicyDataForUser(currentUser);
	}

	/**
	 * Retrieves the 'lightweight' policy DTO beans for a given customer ID. These beans only contain the bare minimum
	 * for rendering summary information on the UI (such as the 'Your Policies' on the homepage and the policy listing
	 * section on the account page) and is sorted in expiry date order.
	 *
	 * @param userModel
	 *           the User ID to be used to determine the list of DTO beans
	 * @return a list of policy summary data beans sorted by expiry date
	 */
	@Override
	public List<InsurancePolicyListingData> getPolicyDataForUser(final UserModel userModel)
	{
		validateParameterNotNullStandardMessage("userModel", userModel);

		final List<InsurancePolicyListingData> policies = retrievePolicyListingsFromOrders(userModel.getOrders());

		Collections.sort(policies, new Comparator<InsurancePolicyListingData>()
		{
			@Override
			public int compare(final InsurancePolicyListingData o1, final InsurancePolicyListingData o2)
			{
				final Date o1Date = o1.getPolicyRawExpiryDate();
				final Date o2Date = o2.getPolicyRawExpiryDate();

				return o1Date.compareTo(o2Date);
			}
		});
		return policies;
	}


	/**
	 * This method is used to generate a list (order being important for visual results) of <InsurancePolicyListingData>
	 * DTO beans from a collection of <OrderModel> beans.
	 *
	 * @param orders
	 *           the collection of orders to be used to determine the available policies
	 * @return the list of policies derived from the collection of orders.
	 */
	protected List<InsurancePolicyListingData> retrievePolicyListingsFromOrders(final Collection<OrderModel> orders)
	{
		final List<InsurancePolicyListingData> policies = new ArrayList<InsurancePolicyListingData>();

		for (final OrderModel orderModel : orders)
		{
			final Set<InsurancePolicyModel> policyModels = orderModel.getOrderPolicies();
			for (final InsurancePolicyModel policyModel : policyModels)
			{
				final InsurancePolicyListingData policyData = getPolicyListingConverter().convert(policyModel);

				if (orderModel.getEntries() != null && !orderModel.getEntries().isEmpty())
				{
					final ProductModel product = retrieveProductModelForOrder(orderModel);
					final ProductData productData = getProductConverter().convert(product);
					if (productData != null)
					{
						policyData.setPolicyImages(productData.getImages());
					}
					policyData.setPolicyProduct(productData);
				}

				final String priceStr = new DecimalFormat("#").format(orderModel.getTotalPrice());
				policyData.setPolicyPrice(priceStr);

				policies.add(policyData);
			}
		}

		return policies;
	}

	/**
	 * For a given <OrderModel> which contains an Insurance order, the first <OrderEntryModel> item is checked to extract
	 * the correct main policy product and this is used to determine the 'product data' of the policy object.
	 *
	 * @param orderModel
	 *           the <OrderModel> object to be used to determine the product name
	 * @return the model of the product associated with the policy
	 */
	protected ProductModel retrieveProductModelForOrder(final OrderModel orderModel)
	{
		final AbstractOrderEntryModel entryModel = orderModel.getEntries().get(0);
		final ProductModel productModel = entryModel.getProduct();
		return productModel;
	}

	protected UserService getUserService()
	{
		return userService;
	}

	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	public Converter<InsurancePolicyModel, InsurancePolicyListingData> getPolicyListingConverter()
	{
		return policyListingConverter;
	}

	@Required
	public void setPolicyListingConverter(final Converter<InsurancePolicyModel, InsurancePolicyListingData> policyListingConverter)
	{
		this.policyListingConverter = policyListingConverter;
	}

	protected Converter<ProductModel, ProductData> getProductConverter()
	{
		return productConverter;
	}

	@Required
	public void setProductConverter(final Converter<ProductModel, ProductData> productConverter)
	{
		this.productConverter = productConverter;
	}

}

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
package de.hybris.platform.financialfacades.order.strategies.impl;

import de.hybris.platform.commercefacades.insurance.data.PolicyItemRequestData;
import de.hybris.platform.commercefacades.insurance.data.PolicyItemResponseData;
import de.hybris.platform.commercefacades.insurance.data.PolicyRequestData;
import de.hybris.platform.commercefacades.insurance.data.PolicyResponseData;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.financialfacades.facades.PolicyServiceFacade;
import de.hybris.platform.financialservices.model.InsurancePolicyModel;
import de.hybris.platform.order.strategies.SubmitOrderStrategy;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Required;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;


/**
 * This is an example strategy designed to show that some action takes place which results in the creation of a policy
 * object on the order based upon the quotation object that has been assigned when the source cart was being
 * manipulated. This is here so that the policy creation is bound to the order creation, but in a more complex
 * implementation, it is expected that this kind of code would be implemented in the order-process flow and the order
 * have states that reflect the fact that a policy is in the process of being handled.
 * 
 */
public class PolicyCreationSubmitOrderStrategy implements SubmitOrderStrategy
{
	
	private ModelService modelService;
	
	private String dateFormat;
	
	private PolicyServiceFacade policyServiceFacade;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.order.strategies.SubmitOrderStrategy#submitOrder(de.hybris.platform.core.model.order.OrderModel
	 * )
	 */
	@Override
	public void submitOrder(final OrderModel order)
	{
		if (order.getInsuranceQuote() == null)
		{
		 	// If there are no insurance quote in order model, no need to run policy creation.
			return;
		}

		Set<InsurancePolicyModel> orderPolicies = order.getOrderPolicies();
		if (CollectionUtils.isEmpty(orderPolicies))
		{
			orderPolicies = new HashSet<>();
			order.setOrderPolicies(orderPolicies);
		}

		PolicyRequestData requestData = buildPolicyRequestData(order);
		PolicyResponseData responseData = getPolicyServiceFacade().requestPolicyCreation(requestData);

		if (responseData.getItems() != null)
		{
			for (PolicyItemResponseData policyItemResponseData : responseData.getItems())
			{
				final InsurancePolicyModel policyModel = getModelService().create(InsurancePolicyModel.class);
				processResponseData(policyModel, policyItemResponseData);
				policyModel.setPolicyOrder(order);
				modelService.save(policyModel);
				// associate with the policies on the order..
				orderPolicies.add(policyModel);
			}
		}

		modelService.save(order);
	}

	/**
	 * process the policy response data and set the relative value into policy model.
	 * 
	 * @param policyModel
	 *           the policy model
	 * @param itemResponseData
	 *           the policy item response data
	 */
	protected void processResponseData(final InsurancePolicyModel policyModel, final PolicyItemResponseData itemResponseData)
	{
		if (itemResponseData != null)
		{
			policyModel.setPolicyId(itemResponseData.getProperties().get("policyId"));

			final DateTimeFormatter formatter = DateTimeFormat.forPattern(getDateFormat());
			final Date startDate = formatter.parseDateTime(itemResponseData.getProperties().get("startDate")).toDate();
			final Date expiryDate = formatter.parseDateTime(itemResponseData.getProperties().get("expiryDate")).toDate();
			policyModel.setPolicyStartDate(startDate);
			policyModel.setPolicyExpiryDate(expiryDate);
			policyModel.setPolicyUrl(itemResponseData.getProperties().get("policyUrl"));
		}
	}

	/**
	 * Build te policy request data by order model.
	 * 
	 * @param order
	 *           the order model
	 * @return policy request data
	 */
	protected PolicyRequestData buildPolicyRequestData(final OrderModel order)
	{
		final PolicyRequestData requestData = new PolicyRequestData();
		requestData.setItems(Lists.<PolicyItemRequestData>newArrayList());
		final PolicyItemRequestData item = new PolicyItemRequestData();
		
		item.setId(order.getPk().toString());
		item.setProperties(Maps.<String, String>newHashMap());
		
		// As mock policy service facade, the default category need to pass to policy request data,
		// so it is looking for the main product which is the first entry. 
		if (!order.getEntries().isEmpty() && order.getEntries().get(0).getProduct().getDefaultCategory() != null)
		{
			item.getProperties().put("type", order.getEntries().get(0).getProduct().getDefaultCategory().getCode());
		}
		item.getProperties().put("orderId", order.getCode());
		
		requestData.getItems().add(item);
		
		return requestData;
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

	protected PolicyServiceFacade getPolicyServiceFacade()
	{
		return policyServiceFacade;
	}

	@Required
	public void setPolicyServiceFacade(final PolicyServiceFacade policyServiceFacade)
	{
		this.policyServiceFacade = policyServiceFacade;
	}

	protected String getDateFormat()
	{
		return dateFormat;
	}

	@Required
	public void setDateFormat(String dateFormat)
	{
		this.dateFormat = dateFormat;
	}
}

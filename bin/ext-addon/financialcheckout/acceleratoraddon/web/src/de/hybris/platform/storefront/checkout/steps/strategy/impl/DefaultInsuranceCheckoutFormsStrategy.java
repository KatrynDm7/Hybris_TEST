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
package de.hybris.platform.storefront.checkout.steps.strategy.impl;

import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.financialfacades.util.InsuranceCheckoutHelper;
import de.hybris.platform.storefront.checkout.steps.strategy.AbstractCheckoutFormsStrategy;
import de.hybris.platform.storefront.form.data.FormDetailData;
import de.hybris.platform.xyformsfacades.data.YFormDefinitionData;
import de.hybris.platform.xyformsfacades.strategy.GetYFormDefinitionsForProductStrategy;
import de.hybris.platform.xyformsservices.exception.YFormServiceException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * InsuranceCheckoutFormsStrategy to retrieve formPageIds and FormDetailData
 */
public class DefaultInsuranceCheckoutFormsStrategy extends AbstractCheckoutFormsStrategy
{

	private static final Logger LOG = Logger.getLogger(DefaultInsuranceCheckoutFormsStrategy.class);

	@Resource(name = "getYFormDefinitionsForProductStrategy")
	private GetYFormDefinitionsForProductStrategy getYFormDefinitionsForProductStrategy;

	private InsuranceCheckoutHelper insuranceCheckoutHelper;

	/**
	 * Simulation of different cases of dynamic checkout form pages depends on the entries in the cart.<br/>
	 * If one item then display no forms; Else display a number of form pages up to the defined Maximum.
	 *
	 * @param cartData
	 * @return Map<FormPageId, ProgressBarId> where the FormPageId defines the navigation of the FormPages, i.e. UniqueId
	 *         separating different FormPages, FormPageId will also be used for retrieve the forms. Whereas the
	 *         ProgressBarId is used as OOTB for linking the labels to display for each of the Checkout Pages, i.e. Form
	 *         Page and OOTB checkout pages.
	 */
	@Override
	public Map<String, String> getFormPageIdList(final CartData cartData)
	{
		final Map<String, String> formPageIds = new HashMap<String, String>();

		if (cartData != null && CollectionUtils.isNotEmpty(cartData.getEntries()))
		{
			final int steps = Math.min(cartData.getEntries().size(), getMaxCheckoutFormPages().intValue());

			for (final OrderEntryData orderEntryData : cartData.getEntries())
			{

				if (CollectionUtils.isNotEmpty(getFormDefinitionsForOrderEntry(orderEntryData)))
				{
					final String formPageId = getInsuranceCheckoutHelper().getFormPageIdByOrderEntryData(orderEntryData);

					formPageIds.put(formPageId, orderEntryData.getProduct().getCode());
				}

				if (formPageIds.size() >= steps)
				{
					break;
				}

			}
		}
		return formPageIds;
	}

	/**
	 * Get YFormDefinitions By FormPageId. <br/>
	 * FormPageId is the value defined earlier the CartData to represent the different form page from the cart entries.
	 *
	 * @param cartData
	 * @param formPageId
	 */
	@Override
	public List<FormDetailData> getFormDetailDataByFormPageId(final CartData cartData, final String formPageId)
	{
		final OrderEntryData orderEntryData = getInsuranceCheckoutHelper().getOrderEntryDataByFormPageId(cartData, formPageId);

		final List<YFormDefinitionData> yFormDefinitions = getFormDefinitionsForOrderEntry(orderEntryData);

		return getInsuranceCheckoutHelper().createFormDetailData(yFormDefinitions, cartData.getCode(),
				orderEntryData.getEntryNumber());
	}

	/**
	 * Get YFormDefinitionData by OrderEntryData. Returns a List of YFormDefinitionData that belongs to the product held
	 * by the OrderEntryData
	 *
	 * @param orderEntryData
	 * @return List<YFormDefinitionData>
	 */
	protected List<YFormDefinitionData> getFormDefinitionsForOrderEntry(final OrderEntryData orderEntryData)
	{
		List<YFormDefinitionData> yFormDefinitions = null;

		final String productCode = orderEntryData.getProduct().getCode();

		try
		{
			yFormDefinitions = getYFormDefinitionsForProductStrategy.execute(productCode);
		}
		catch (final YFormServiceException e)
		{
			LOG.error("Could not retrieve YFormDefinitions for Product[code=\"" + productCode + "\"]", e);
		}
		return yFormDefinitions;
	}


	protected InsuranceCheckoutHelper getInsuranceCheckoutHelper()
	{
		return insuranceCheckoutHelper;
	}

	@Required
	public void setInsuranceCheckoutHelper(final InsuranceCheckoutHelper insuranceCheckoutHelper)
	{
		this.insuranceCheckoutHelper = insuranceCheckoutHelper;
	}

}

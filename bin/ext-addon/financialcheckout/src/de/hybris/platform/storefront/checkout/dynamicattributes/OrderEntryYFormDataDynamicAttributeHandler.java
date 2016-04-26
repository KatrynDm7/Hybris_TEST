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
package de.hybris.platform.storefront.checkout.dynamicattributes;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.financialfacades.util.InsuranceCheckoutHelper;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;
import de.hybris.platform.xyformsservices.form.YFormService;
import de.hybris.platform.xyformsservices.model.YFormDataModel;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;


/**
 * OrderEntryYFormDataDynamicAttributeHandler class to retrieve the YFormData by refId
 * */
public class OrderEntryYFormDataDynamicAttributeHandler implements
		DynamicAttributeHandler<List<YFormDataModel>, AbstractOrderEntryModel>
{

	private YFormService yformService;

	private InsuranceCheckoutHelper insuranceCheckoutHelper;

	@Override
	public List<YFormDataModel> get(final AbstractOrderEntryModel orderEntry)
	{

		final String yFormDataRefId = getInsuranceCheckoutHelper().buildYFormDataRefId(orderEntry.getOrder().getCode(),
				orderEntry.getEntryNumber());

		return getYformService().getYFormDataByRefId(yFormDataRefId);
	}

	@Override
	public void set(final AbstractOrderEntryModel orderEntry, final List<YFormDataModel> yFormDataList)
	{
		throw new UnsupportedOperationException();
	}

	protected YFormService getYformService()
	{
		return yformService;
	}

	@Required
	public void setYformService(final YFormService yformService)
	{
		this.yformService = yformService;
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

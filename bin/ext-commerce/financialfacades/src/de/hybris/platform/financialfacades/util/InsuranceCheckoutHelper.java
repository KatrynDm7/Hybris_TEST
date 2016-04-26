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
package de.hybris.platform.financialfacades.util;

import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.storefront.form.data.FormDetailData;
import de.hybris.platform.xyformsfacades.data.YFormDefinitionData;

import java.util.List;


public interface InsuranceCheckoutHelper
{

	/**
	 * Builds up a refId for YFormData
	 *
	 * @param orderCode
	 * @param orderEntryNumber
	 * @return String YFormData refId
	 * */
	public String buildYFormDataRefId(final String orderCode, final Integer orderEntryNumber);

	/**
	 * Transforms the inputs into a FormDetailData object
	 *
	 * @param yFormDefinitionData
	 * @param orderCode
	 * @param orderEntryNumber
	 * @return FormDetailData
	 * */
	public FormDetailData createFormDetailData(final YFormDefinitionData yFormDefinitionData, final String orderCode,
			final Integer orderEntryNumber);

	/**
	 * Transforms the inputs into a FormDetailData objects
	 *
	 * @param yFormDefinitionDataList
	 * @param orderCode
	 * @param orderEntryNumber
	 * @return List<FormDetailData>
	 * */
	public List<FormDetailData> createFormDetailData(final List<YFormDefinitionData> yFormDefinitionDataList,
			final String orderCode, final Integer orderEntryNumber);

	/**
	 * Returns the OrderEntryData based on the formPageId
	 *
	 * @param cartData
	 * @param formPageId
	 * @return OrderEntryData
	 * */
	public OrderEntryData getOrderEntryDataByFormPageId(final CartData cartData, final String formPageId);

	/**
	 * Returns the FormPageId from the OrderEntryData
	 *
	 * @param orderEntryData
	 * @return String
	 * */
	public String getFormPageIdByOrderEntryData(final OrderEntryData orderEntryData);
}

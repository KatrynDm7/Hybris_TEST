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
package de.hybris.platform.storefront.checkout.steps.strategy;

import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.storefront.form.data.FormDetailData;

import java.util.List;
import java.util.Map;


/**
 * CheckoutFormsStrategy
 */
public interface CheckoutFormsStrategy
{
	/**
	 * Get maximum display checkout form page from the spring config if it's not set.
	 */
	Integer getMaxCheckoutFormPages();

	/**
	 * Get YFormDefinitions By FormPageId. <br/>
	 * FormPageId is the value defined earlier the CartData to represent the different form page from the cart entries.
	 *
	 * @param cartData
	 * @param formPageId
	 */
	List<FormDetailData> getFormDetailDataByFormPageId(final CartData cartData, final String formPageId);

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
	Map<String, String> getFormPageIdList(final CartData cartData);

}

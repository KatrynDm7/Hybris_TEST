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
package de.hybris.platform.commerceservices.constants;

import de.hybris.platform.commerceservices.order.CommerceSaveCartService;


/**
 * Global class for all CommerceServices constants. You can add global constants for your extension into this class.
 */
public final class CommerceServicesConstants extends GeneratedCommerceServicesConstants
{
	/**
	 * If true, configured hooks will be called before and after addToCart calls in commerce services.
	 */
	public static final String ADDTOCARTHOOK_ENABLED = "commerceservices.commerceaddtocartmethodhook.enabled";
	/**
	 * If true, configured hooks will be called before and after cart calculation calls in commerce services.
	 */
	public static final String CARTCALCULATIONHOOK_ENABLED = "commerceservices.commercecartcalculationmethodhook.enabled";
	/**
	 * If true, configured hooks will be called before and after authorize payment calls in commerce services.
	 */
	public static final String AUTHORIZEPAYMENTHOOK_ENABLED = "commerceservices.authorizepaymentmethodhook.enabled";
	/**
	 * If true, configured hooks will be called before and after place order calls in commerce services.
	 */
	public static final String PLACEORDERHOOK_ENABLED = "commerceservices.commerceplaceordermethodhook.enabled";
	/**
	 * If true, configured hooks will be called before and after update cart entries calls in commerce services.
	 */
	public static final String UPDATECARTENTRYHOOK_ENABLED = "commerceservices.commerceupdatecartentryhook.enabled";
	/**
	 * If true, configured hooks will be called before and after addToCart calls in commerce services.
	 */
	public static final String SAVECARTHOOK_ENABLED = "commerceservices.commercesavecartmethodhook.enabled";
	/**
	 * If true, configured hooks will be called before and after flag for deletion calls in commerce services.
	 */
	public static final String FLAGFORDELETIONHOOK_ENABLED = "commerceservices.commerceflagfordeletionmethodhook.enabled";
	/**
	 * If true, configured hooks will be called before and after
	 * {@link CommerceSaveCartService#cloneSavedCart(de.hybris.platform.commerceservices.service.data.CommerceSaveCartParameter)}
	 * calls in commerce services.
	 */
	public static final String CLONESAVEDCARTHOOK_ENABLED = "commerceservices.commerceclonesavedcartmethodhook.enabled";
	/**
	 * If true, configured hooks will be called before cart restoration in commerce services.
	 */
	public static final String SAVECARTRESTORATIONHOOK_ENABLED = "commerceservices.commercesavecartrestorationmethodhook.enabled";
}

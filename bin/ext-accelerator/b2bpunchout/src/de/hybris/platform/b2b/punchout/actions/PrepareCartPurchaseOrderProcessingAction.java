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
package de.hybris.platform.b2b.punchout.actions;

import de.hybris.platform.b2b.punchout.services.PunchOutConfigurationService;
import de.hybris.platform.b2bacceleratorfacades.api.cart.CheckoutFacade;
import de.hybris.platform.b2bacceleratorfacades.order.data.B2BPaymentTypeData;
import de.hybris.platform.b2bacceleratorservices.enums.CheckoutPaymentType;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.core.model.order.CartModel;

import org.cxml.CXML;
import org.springframework.beans.factory.annotation.Required;


/**
 * Prepares a cart for processing/populating by setting the required details.
 */
public class PrepareCartPurchaseOrderProcessingAction implements PunchOutProcessingAction<CXML, CartModel>
{

	private CheckoutFacade b2bCheckoutFacade;
	private PunchOutConfigurationService punchOutConfigurationService;

	@Override
	public void process(final CXML input, final CartModel output)
	{
		output.setPunchOutOrder(Boolean.TRUE);

		final String paymentType = CheckoutPaymentType.ACCOUNT.getCode();
		final String costCenterCode = getPunchOutConfigurationService().getDefaultCostCenter();

		final B2BPaymentTypeData paymentTypeData = new B2BPaymentTypeData();
		paymentTypeData.setCode(paymentType);

		//final B2BCostCenterData costCenter = new B2BCostCenterData();
		//costCenter.setCode(costCenterCode);

		final CartData cartData = new CartData();
		cartData.setPaymentType(paymentTypeData);
		//cartData.setCostCenter(costCenter);

		b2bCheckoutFacade.updateCheckoutCart(cartData);
		b2bCheckoutFacade.setCostCenterForCart(costCenterCode, output.getCode());
	}

	public CheckoutFacade getB2bCheckoutFacade()
	{
		return b2bCheckoutFacade;
	}

	@Required
	public void setB2bCheckoutFacade(final CheckoutFacade b2bCheckoutFacade)
	{
		this.b2bCheckoutFacade = b2bCheckoutFacade;
	}


	public PunchOutConfigurationService getPunchOutConfigurationService()
	{
		return punchOutConfigurationService;
	}

	@Required
	public void setPunchOutConfigurationService(final PunchOutConfigurationService punchOutConfigurationService)
	{
		this.punchOutConfigurationService = punchOutConfigurationService;
	}

}

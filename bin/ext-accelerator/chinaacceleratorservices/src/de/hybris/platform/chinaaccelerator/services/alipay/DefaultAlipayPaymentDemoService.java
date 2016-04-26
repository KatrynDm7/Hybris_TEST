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
package de.hybris.platform.chinaaccelerator.services.alipay;

import de.hybris.platform.chinaaccelerator.services.alipay.AlipayEnums.AlipayTradeStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.payment.impl.DefaultPaymentServiceImpl;

import java.util.Map;


//import de.hybris.platform.acceleratorservices.enums.UiExperienceLevel;


public class DefaultAlipayPaymentDemoService extends DefaultPaymentServiceImpl implements AlipayPaymentService
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.chinaaccelerator.services.alipay.AlipayPaymentService#initiate(de.hybris.platform.core.model
	 * .order.OrderModel)
	 */
	@Override
	public boolean initiate(final OrderModel order)
	{
		// YTODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.chinaaccelerator.services.alipay.AlipayPaymentService#handleResponse(de.hybris.platform.core
	 * .model.order.OrderModel, de.hybris.platform.chinaaccelerator.services.alipay.AlipayNotifyInfoData, java.util.Map,
	 * boolean)
	 */
	@Override
	public boolean handleResponse(final OrderModel order, final AlipayNotifyInfoData notifyData,
			final Map<String, String> notifyDataMa, final boolean isMobile)
	{
		// YTODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.chinaaccelerator.services.alipay.AlipayPaymentService#handleResponse(de.hybris.platform.core
	 * .model.order.OrderModel, de.hybris.platform.chinaaccelerator.services.alipay.AlipayReturnData)
	 */
	@Override
	public boolean handleResponse(final OrderModel orderModel, final AlipayReturnData returnData)
	{
		// YTODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.chinaaccelerator.services.alipay.AlipayPaymentService#closeTrade(de.hybris.platform.core.model
	 * .order.OrderModel)
	 */
	@Override
	public boolean closeTrade(final OrderModel orderModel)
	{
		// YTODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.chinaaccelerator.services.alipay.AlipayPaymentService#checkTrade(de.hybris.platform.core.model
	 * .order.OrderModel)
	 */
	@Override
	public AlipayTradeStatus checkTrade(final OrderModel orderModel)
	{
		// YTODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.chinaaccelerator.services.alipay.AlipayPaymentService#getRequestUrl(de.hybris.platform.core
	 * .model.order.OrderModel)
	 */
	@Override
	public String getRequestUrl(final OrderModel orderModel)
	{
		// YTODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.chinaaccelerator.services.alipay.AlipayPaymentService#saveErrorCallback(de.hybris.platform.
	 * core.model.order.OrderModel, java.lang.String)
	 */
	@Override
	public void saveErrorCallback(final OrderModel orderModel, final String errorCode)
	{
		// YTODO Auto-generated method stub

	}
}

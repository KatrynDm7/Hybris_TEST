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
package de.hybris.platform.chinaaccelerator.alipay.data;

public class AlipayTradeResponseData {
	public static final String RESPONSE_ROOT = "alipay";
	private String is_success;
	private String error;
	/**
	 * @return the is_success
	 */
	public String getIs_success() {
		return is_success;
	}
	/**
	 * @param is_success the is_success to set
	 */
	public void setIs_success(String is_success) {
		this.is_success = is_success;
	}
	/**
	 * @return the error
	 */
	public String getError() {
		return error;
	}
	/**
	 * @param error the error to set
	 */
	public void setError(String error) {
		this.error = error;
	}
	
}

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

public class BaseRequestData {
	private String service;
	private String partner;
	private String _input_charset;
	
	/**
	 * @return the service
	 */
	public String getService() {
		return service;
	}

	/**
	 * @param service the service to set
	 */
	public void setService(String service) {
		this.service = service;
	}

	/**
	 * @return the partner
	 */
	public String getPartner() {
		return partner;
	}

	/**
	 * @param partner the partner to set
	 */
	public void setPartner(String partner) {
		this.partner = partner;
	}

	/**
	 * @return the _input_charset
	 */
	public String get_input_charset() {
		return _input_charset;
	}

	/**
	 * @param _input_charset the _input_charset to set
	 */
	public void set_input_charset(String _input_charset) {
		this._input_charset = _input_charset;
	}

	
}

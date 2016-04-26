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
package de.hybris.platform.sap.sappricingbol.businessobject.impl;

import de.hybris.platform.sap.sappricingbol.businessobject.interf.SapPricingPartnerFunction;

public class SapPricingPartnerFunctionImpl implements SapPricingPartnerFunction {
	
	private String language;
	private String currency;
	private String soldTo;
	public String getLanguage() {
		return language;
	}
	
	public void setLanguage(String language) {
		this.language = language;
	}
	
	public String getCurrency() {
		return currency;
	}
	
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	public String getSoldTo() {
		return soldTo;
	}

	public void setSoldTo(String soldTo) {
		this.soldTo = soldTo;
	}
	
	public SapPricingPartnerFunctionImpl() {
		super();
	}
	
	public SapPricingPartnerFunctionImpl(String language, String currency,
			String soldTo) {
		super();
		this.language = language;
		this.currency = currency;
		this.soldTo = soldTo;
	}
	
	@Override
	public String toString() {
		return "DefaultSapPricingPartnerFunction [language=" + language
				+ ", currency=" + currency + ", soldTo=" + soldTo + "]";
	}
	
	
	
	
	

}

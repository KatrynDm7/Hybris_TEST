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
package de.hybris.platform.sap.sappricingbol.businessobject.interf;
public interface SapPricingPartnerFunction {

	public abstract void setSoldTo(String soldTo);

	public abstract String getSoldTo();

	public abstract void setCurrency(String currency);

	public abstract String getCurrency();

	public abstract void setLanguage(String language);

	public abstract String getLanguage();
	
	

}
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
package de.hybris.platform.integration.cis.subscription.forms;

public class PaymentForm
{
	private String sessionToken;
	private String cardTypeCode;
	private String nameOnCard;
	private String cardNumber;
	private String startMonth;
	private String startYear;
	private String expiryMonth;
	private String expiryYear;
	private String cvn;
	private String issueNumber;
	private AddressForm billingAddress;

	public String getSessionToken()
	{
		return sessionToken;
	}

	public void setSessionToken(final String sessionToken)
	{
		this.sessionToken = sessionToken;
	}

	public String getStartMonth()
	{
		return startMonth;
	}

	public void setStartMonth(final String startMonth)
	{
		this.startMonth = startMonth;
	}

	public String getStartYear()
	{
		return startYear;
	}

	public void setStartYear(final String startYear)
	{
		this.startYear = startYear;
	}

	public String getCardTypeCode()
	{
		return cardTypeCode;
	}

	public void setCardTypeCode(final String cardTypeCode)
	{
		this.cardTypeCode = cardTypeCode;
	}

	public String getNameOnCard()
	{
		return nameOnCard;
	}

	public void setNameOnCard(final String nameOnCard)
	{
		this.nameOnCard = nameOnCard;
	}

	public String getCardNumber()
	{
		return cardNumber;
	}

	public void setCardNumber(final String cardNumber)
	{
		this.cardNumber = cardNumber;
	}

	public String getExpiryMonth()
	{
		return expiryMonth;
	}

	public void setExpiryMonth(final String expiryMonth)
	{
		this.expiryMonth = expiryMonth;
	}

	public String getExpiryYear()
	{
		return expiryYear;
	}

	public void setExpiryYear(final String expiryYear)
	{
		this.expiryYear = expiryYear;
	}

	public String getCvn()
	{
		return cvn;
	}

	public void setCvn(final String cvn)
	{
		this.cvn = cvn;
	}

	public String getIssueNumber()
	{
		return issueNumber;
	}

	public void setIssueNumber(final String issueNumber)
	{
		this.issueNumber = issueNumber;
	}

	public AddressForm getBillingAddress()
	{
		return billingAddress;
	}

	public void setBillingAddress(final AddressForm billingAddress)
	{
		this.billingAddress = billingAddress;
	}

}

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
package de.hybris.platform.storefront.forms;

import de.hybris.platform.acceleratorstorefrontcommons.forms.AddressForm;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


/**
 */
public class SopPaymentDetailsForm
{
	private String sessionToken;
	private String cardTypeCode;
	private String nameOnCard;
	private String cardNumber;
	private String startMonth;
	private String startYear;
	private String expiryMonth;
	private String expiryYear;
	private String issueNumber;
	private Boolean makeAsDefault = Boolean.FALSE;

	private Boolean saveInAccount;
	private Boolean newBillingAddress;
	private AddressForm billingAddress;


	public String getSessionToken()
	{
		return sessionToken;
	}

	public void setSessionToken(final String sessionToken)
	{
		this.sessionToken = sessionToken;
	}

	@NotNull(message = "{payment.cardType.invalid}")
	@Size(min = 1, max = 255, message = "{payment.cardType.invalid}")
	public String getCardTypeCode()
	{
		return cardTypeCode;
	}

	public void setCardTypeCode(final String cardTypeCode)
	{
		this.cardTypeCode = cardTypeCode;
	}

	@NotNull(message = "{payment.nameOnCard.invalid}")
	@Size(min = 1, max = 255, message = "{payment.nameOnCard.invalid}")
	public String getNameOnCard()
	{
		return nameOnCard;
	}

	public void setNameOnCard(final String nameOnCard)
	{
		this.nameOnCard = nameOnCard;
	}

	@NotNull(message = "{payment.cardNumber.invalid}")
	@Size(min = 16, max = 16, message = "{payment.cardNumber.invalid}")
	public String getCardNumber()
	{
		return cardNumber;
	}

	public void setCardNumber(final String cardNumber)
	{
		this.cardNumber = cardNumber;
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

	@NotNull(message = "{payment.expiryMonth.invalid}")
	@Size(min = 1, max = 2, message = "{payment.expiryMonth.invalid}")
	public String getExpiryMonth()
	{
		return expiryMonth;
	}

	public void setExpiryMonth(final String expiryMonth)
	{
		this.expiryMonth = expiryMonth;
	}

	@NotNull(message = "{payment.expiryYear.invalid}")
	@Size(min = 2, max = 4, message = "{payment.expiryYear.invalid}")
	public String getExpiryYear()
	{
		return expiryYear;
	}

	public void setExpiryYear(final String expiryYear)
	{
		this.expiryYear = expiryYear;
	}

	@Pattern(regexp = "(^$|^?\\d*$)", message = "{payment.issueNumber.invalid}")
	public String getIssueNumber()
	{
		return issueNumber;
	}

	public void setIssueNumber(final String issueNumber)
	{
		this.issueNumber = issueNumber;
	}

	public Boolean getSaveInAccount()
	{
		return saveInAccount;
	}

	public void setSaveInAccount(final Boolean saveInAccount)
	{
		this.saveInAccount = saveInAccount;
	}

	public Boolean getNewBillingAddress()
	{
		return newBillingAddress;
	}

	public void setNewBillingAddress(final Boolean newBillingAddress)
	{
		this.newBillingAddress = newBillingAddress;
	}

	//	@Valid
	public AddressForm getBillingAddress()
	{
		return billingAddress;
	}

	public void setBillingAddress(final AddressForm billingAddress)
	{
		this.billingAddress = billingAddress;
	}

	public Boolean getMakeAsDefault()
	{
		return makeAsDefault;
	}

	public void setMakeAsDefault(final Boolean makeAsDefault)
	{
		this.makeAsDefault = makeAsDefault;
	}
}

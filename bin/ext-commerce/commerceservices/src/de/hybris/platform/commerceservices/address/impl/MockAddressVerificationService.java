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
package de.hybris.platform.commerceservices.address.impl;

import de.hybris.platform.commerceservices.address.AddressErrorCode;
import de.hybris.platform.commerceservices.address.AddressFieldType;
import de.hybris.platform.commerceservices.address.AddressVerificationDecision;
import de.hybris.platform.commerceservices.address.AddressVerificationService;
import de.hybris.platform.commerceservices.address.data.AddressFieldErrorData;
import de.hybris.platform.commerceservices.address.data.AddressVerificationResultData;
import de.hybris.platform.commerceservices.address.util.AddressVerificationResultUtils;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.List;


/**
 * The mock implementation of AddressVerificationService to be used in the absence of an external service.
 */
public class MockAddressVerificationService implements
		AddressVerificationService<AddressVerificationDecision, AddressFieldErrorData<AddressFieldType, AddressErrorCode>>
{
	private BaseStoreService baseStoreService;

	public static final String ACCEPT = "accept";
	public static final String REJECT = "reject";

	public static final String TITLE_CODE = "titleCode";
	public static final String FIRST_NAME = "firstName";
	public static final String LAST_NAME = "lastName";
	public static final String ADDRESS_LINE_1 = "addressline1";
	public static final String ADDRESS_LINE_2 = "addressline2";
	public static final String REGION = "region";
	public static final String ZIP_CODE = "zipcode";
	public static final String CITY = "city";
	public static final String COUNTRY = "country";

	public static final String MISSING = "missing";
	public static final String INVALID = "invalid";

	@Override
	public AddressVerificationResultData<AddressVerificationDecision, AddressFieldErrorData<AddressFieldType, AddressErrorCode>> verifyAddress(
			final AddressModel addressModel)
	{
		final AddressVerificationResultData<AddressVerificationDecision, AddressFieldErrorData<AddressFieldType, AddressErrorCode>> acceptedResult = createVerificationResult();

		validateAddressFields(acceptedResult, addressModel);
		if (AddressVerificationResultUtils.requiresErrorHandling(acceptedResult))
		{
			acceptedResult.setDecision(AddressVerificationDecision.lookup(REJECT));
		}
		else
		{
            if(addressModel.getTown().equals("review"))
            {
                acceptedResult.setDecision(AddressVerificationDecision.REVIEW);
                final List<AddressModel> suggestedAddresses = new ArrayList<AddressModel>();
                addressModel.setLine1(String.format("%s corrected", addressModel.getLine1()));
                suggestedAddresses.add(addressModel);
                acceptedResult.setSuggestedAddresses(suggestedAddresses);
                return acceptedResult;
            }
			acceptedResult.setDecision(AddressVerificationDecision.lookup(ACCEPT));
		}
		return acceptedResult;
	}

	@Override
	public boolean isCustomerAllowedToIgnoreSuggestions()
	{
		final BaseStoreModel baseStore = getBaseStoreService().getCurrentBaseStore();
		return baseStore != null && baseStore.isCustomerAllowedToIgnoreSuggestions();
	}

	/**
	 * Validates each field input in an AddressForm. Field validation is usually left up to the external address
	 * verification service so the mock must perform this function.
	 */
	protected void validateAddressFields(
			final AddressVerificationResultData<AddressVerificationDecision, AddressFieldErrorData<AddressFieldType, AddressErrorCode>> result,
			final AddressModel address)
	{

		final List<AddressFieldErrorData<AddressFieldType, AddressErrorCode>> errorList = new ArrayList<AddressFieldErrorData<AddressFieldType, AddressErrorCode>>();
		if (address.getTitle() == null || (address.getTitle() != null && address.getTitle().getCode() == null))
		{
			addErrorToVerificationResult(TITLE_CODE, MISSING, errorList);
		}
		else if (StringUtils.length(address.getTitle().getCode()) > 255)
		{
			addErrorToVerificationResult(TITLE_CODE, INVALID, errorList);
		}

		if (StringUtils.isEmpty(address.getFirstname()))
		{
			addErrorToVerificationResult(FIRST_NAME, MISSING, errorList);
		}
		else if (StringUtils.length(address.getFirstname()) > 255)
		{
			addErrorToVerificationResult(FIRST_NAME, INVALID, errorList);
		}

		if (StringUtils.isEmpty(address.getLastname()))
		{
			addErrorToVerificationResult(LAST_NAME, MISSING, errorList);
		}
		else if (StringUtils.length(address.getLastname()) > 255)
		{
			addErrorToVerificationResult(LAST_NAME, INVALID, errorList);
		}

		if (StringUtils.isEmpty(address.getLine1()))
		{
			addErrorToVerificationResult(ADDRESS_LINE_1, MISSING, errorList);
		}
		else if (StringUtils.length(address.getLine1()) > 255)
		{
			addErrorToVerificationResult(ADDRESS_LINE_1, INVALID, errorList);
		}

		if (StringUtils.isNotEmpty(address.getLine2()) && StringUtils.length(address.getLine2()) > 255)
		{
			addErrorToVerificationResult(ADDRESS_LINE_2, INVALID, errorList);
		}

		if (StringUtils.isEmpty(address.getTown()))
		{
			addErrorToVerificationResult(CITY, MISSING, errorList);
		}
		else if (StringUtils.length(address.getTown()) > 255)
		{
			addErrorToVerificationResult(CITY, INVALID, errorList);
		}

		if (address.getRegion() != null && address.getRegion().getIsocode() == null)
		{
			addErrorToVerificationResult(REGION, MISSING, errorList);
		}
		else if (address.getRegion() != null && StringUtils.length(address.getRegion().getIsocode()) > 255)
		{
			addErrorToVerificationResult(REGION, INVALID, errorList);
		}

		if (StringUtils.isEmpty(address.getPostalcode()))
		{
			addErrorToVerificationResult(ZIP_CODE, MISSING, errorList);
		}
		else if (StringUtils.length(address.getPostalcode()) > 10)
		{
			addErrorToVerificationResult(ZIP_CODE, INVALID, errorList);
		}

		if (address.getCountry() == null || (address.getCountry() != null && address.getCountry().getIsocode() == null))
		{
			addErrorToVerificationResult(COUNTRY, MISSING, errorList);
		}
		else if (StringUtils.length(address.getCountry().getIsocode()) > 255)
		{
			addErrorToVerificationResult(COUNTRY, INVALID, errorList);
		}

		result.setFieldErrors(errorList);
	}

	protected void addErrorToVerificationResult(final String titleCode, final String missing,
			final List<AddressFieldErrorData<AddressFieldType, AddressErrorCode>> errors)
	{
		final AddressFieldErrorData<AddressFieldType, AddressErrorCode> errorData = createFieldError();
		errorData.setFieldType(AddressFieldType.lookup(titleCode));
		errorData.setErrorCode(AddressErrorCode.lookup(missing));
		errors.add(errorData);
	}

	protected AddressFieldErrorData<AddressFieldType, AddressErrorCode> createFieldError()
	{
		return new AddressFieldErrorData<AddressFieldType, AddressErrorCode>();
	}

	protected AddressVerificationResultData<AddressVerificationDecision, AddressFieldErrorData<AddressFieldType, AddressErrorCode>> createVerificationResult()
	{
		return new AddressVerificationResultData<AddressVerificationDecision, AddressFieldErrorData<AddressFieldType, AddressErrorCode>>();
	}

	protected BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	@Required
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}
}

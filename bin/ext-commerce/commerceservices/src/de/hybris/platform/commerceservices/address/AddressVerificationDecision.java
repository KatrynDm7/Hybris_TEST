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
package de.hybris.platform.commerceservices.address;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the set of response codes that can be returned by an external address verification service.
 */
public enum AddressVerificationDecision
{
	/** The address was verified. No major differences between the address entered and suggested address(es) */
	ACCEPT("accept"),
	/** There are major differences between the address entered and the corrected standardized address(es) */
	REVIEW("review"),
	/** The address data is insufficient or incorrect, and no corrections/standardization could be applied */
	REJECT("reject"),
	/** A valid decision was not provided by the service */
	UNKNOWN("unknown");

	private final String decisionString;
	private static final Map<String, AddressVerificationDecision> LOOKUPMAP =
			new HashMap<String, AddressVerificationDecision>();

	static
	{
		for (final AddressVerificationDecision dec : AddressVerificationDecision.values())
		{
			LOOKUPMAP.put(dec.getDecisionString(), dec);
		}
	}

	private AddressVerificationDecision(final String decision)
	{
		this.decisionString = decision;
	}

	public String getDecisionString()
	{
		return decisionString;
	}

	public static AddressVerificationDecision lookup(final String decisionKey)
	{
		AddressVerificationDecision decision = LOOKUPMAP.get(decisionKey);
		if (decision == null)
		{
			decision = UNKNOWN;
		}
		return decision;
	}
}

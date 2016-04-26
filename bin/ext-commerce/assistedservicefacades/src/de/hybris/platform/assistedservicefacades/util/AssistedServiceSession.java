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
package de.hybris.platform.assistedservicefacades.util;

import de.hybris.platform.core.model.user.UserModel;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * Class represents Assisted Service emulation parameters.
 */
public class AssistedServiceSession
{
	private final Map asmSessionParams = new HashMap<String, String>();
	private String flashErrorMessage = null;

	public Map<String, Object> getAsmSessionParametersMap()
	{
		return Collections.unmodifiableMap(asmSessionParams);
	}

	public UserModel getAgent()
	{
		return (UserModel) asmSessionParams.get(AGENT);
	}

	public void setAgent(final UserModel agent)
	{
		asmSessionParams.put(AGENT, agent);
	}

	public UserModel getEmulatedCustomer()
	{
		return (UserModel) asmSessionParams.get(EMULATED_CUSTOMER);
	}

	public void setEmulatedCustomer(final UserModel emulatedCustomer)
	{
		asmSessionParams.put(EMULATED_CUSTOMER, emulatedCustomer);
	}

	public void setSavedEmulationData(final CustomerEmulationParams emulationParams)
	{
		asmSessionParams.put(EMULATION_PARAM, emulationParams);
		if (emulationParams == null)
		{
			asmSessionParams.remove("customerId");
			asmSessionParams.remove("customerName");
			asmSessionParams.remove("cartId");
		}
		else
		{
			asmSessionParams.put("customerId", emulationParams.getUserId());
			asmSessionParams.put("customerName", emulationParams.getUserId());
			asmSessionParams.put("cartId", emulationParams.getCartId());
		}
	}

	public CustomerEmulationParams getSavedEmulationData()
	{
		return (CustomerEmulationParams) asmSessionParams.get(EMULATION_PARAM);
	}

	public void setFlashErrorMessage(final String flashErrorMessage)
	{
		this.flashErrorMessage = flashErrorMessage;
	}

	public String getFlashErrorMessage()
	{
		final String flashErrorMessage = this.flashErrorMessage == null ? null : new String(this.flashErrorMessage);
		this.flashErrorMessage = null;
		return flashErrorMessage;
	}

	public static final String AGENT = "agent";
	public static final String EMULATED_CUSTOMER = "emulatedUser";
	public static final String EMULATION_PARAM = "emulationParams";
}
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
package de.hybris.platform.acceleratorservices.fraud.symptom.impl;

import de.hybris.platform.commerceservices.enums.CustomerType;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.fraud.impl.FraudServiceResponse;
import de.hybris.platform.fraud.impl.FraudSymptom;
import de.hybris.platform.fraud.symptom.impl.FirstTimeOrderSymptom;


public class AcceleratorFirstTimeOrderSymptom extends FirstTimeOrderSymptom
{
	@Override
	public FraudServiceResponse recognizeSymptom(final FraudServiceResponse fraudResponse, final AbstractOrderModel order)
	{
		final boolean firstOrder = ((order.getUser() instanceof CustomerModel && CustomerType.GUEST.equals(((CustomerModel) order
				.getUser()).getType())) || (null != order.getUser().getOrders() && order.getUser().getOrders().size() == 1));
		fraudResponse.addSymptom(new FraudSymptom(getSymptomName(), firstOrder ? getIncrement() : 0));

		return fraudResponse;
	}
}

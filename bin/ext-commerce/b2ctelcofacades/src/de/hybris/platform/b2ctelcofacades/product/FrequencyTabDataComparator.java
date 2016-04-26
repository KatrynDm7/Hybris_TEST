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

package de.hybris.platform.b2ctelcofacades.product;

import de.hybris.platform.b2ctelcofacades.data.FrequencyTabData;
import de.hybris.platform.commerceservices.util.AbstractComparator;
import de.hybris.platform.subscriptionservices.enums.TermOfServiceFrequency;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;


/**
 * Comparator of {@link FrequencyTadData}.
 */
public class FrequencyTabDataComparator extends AbstractComparator<FrequencyTabData> implements Serializable
{
	@Override
	protected int compareInstances(final FrequencyTabData thisFrequencyData, final FrequencyTabData thatFrequencyData)
	{
		final TermOfServiceFrequency thisToSFrequency = TermOfServiceFrequency.valueOf(StringUtils.upperCase(thisFrequencyData
				.getTermOfServiceFrequency().getCode()));
		final TermOfServiceFrequency thatToSFrequency = TermOfServiceFrequency.valueOf(StringUtils.upperCase(thatFrequencyData
				.getTermOfServiceFrequency().getCode()));
		if (thisToSFrequency.equals(thatToSFrequency) && !thisToSFrequency.equals(TermOfServiceFrequency.NONE))
		{
			return thisFrequencyData.getTermOfServiceNumber() - thatFrequencyData.getTermOfServiceNumber();
		}
		else
		{
			return thisToSFrequency.compareTo(thatToSFrequency);
		}
	}
}

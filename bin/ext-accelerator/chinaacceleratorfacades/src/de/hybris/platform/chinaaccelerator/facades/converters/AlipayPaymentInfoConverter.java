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
package de.hybris.platform.chinaaccelerator.facades.converters;

import de.hybris.platform.chinaaccelerator.facades.order.data.EnhancedPayentInfoData;
import de.hybris.platform.converters.impl.AbstractPopulatingConverter;
import de.hybris.platform.core.model.order.payment.AlipayPaymentInfoModel;



public class AlipayPaymentInfoConverter extends AbstractPopulatingConverter<AlipayPaymentInfoModel, EnhancedPayentInfoData>
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.converters.impl.AbstractConverter#createTarget()
	 */
	@Override
	protected EnhancedPayentInfoData createTarget()
	{
		return new EnhancedPayentInfoData();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.converters.impl.AbstractPopulatingConverter#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final AlipayPaymentInfoModel source, final EnhancedPayentInfoData target)
	{
		super.populate(source, target);
	}

}

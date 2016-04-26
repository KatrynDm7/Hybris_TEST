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
package com.sap.wec.adtreco.b2b.bo.impl;

import org.apache.commons.lang.StringUtils;
import com.sap.wec.adtreco.bo.impl.SAPInitiativeReaderImpl;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.services.B2BCustomerService;
import de.hybris.platform.b2b.services.B2BUnitService;
import de.hybris.platform.sap.core.bol.backend.BackendType;


/**
 *
 */
@BackendType("CEI")
public class SAPInitiativeReaderB2BImpl extends SAPInitiativeReaderImpl
{

	public B2BCustomerService b2bCustomerService;
	public B2BUnitService b2bUnitService;

	protected String getBusinessPartnerFilter(final String businessPartner)
	{
		if (StringUtils.isEmpty(businessPartner))
		{
			return StringUtils.EMPTY;
		}

		boolean isB2CCustomer = true;
		String unitId = StringUtils.EMPTY;

		final B2BCustomerModel b2bCustomer = (B2BCustomerModel) b2bCustomerService.getCurrentB2BCustomer();
		if (b2bCustomer != null)
		{
			unitId = b2bUnitService.getParent(b2bCustomer).getUid();
			if (unitId != null)
			{
				isB2CCustomer = false;
			}
		}

		if (isB2CCustomer)
		{
			return super.getBusinessPartnerFilter(businessPartner, false); //TODO
		}
		else
		{
			unitId = StringUtils.leftPad(unitId, 10, '0'); //B2B Unit Id is a 10 char string
			return "Filter/CustomerId" + EQ_UTF8 + unitId + QUOT_UTF8;
		}
	}

	public B2BCustomerService getB2bCustomerService()
	{
		return b2bCustomerService;
	}

	public void setB2bCustomerService(final B2BCustomerService b2bCustomerService)
	{
		this.b2bCustomerService = b2bCustomerService;
	}

	public B2BUnitService getB2bUnitService()
	{
		return b2bUnitService;
	}

	public void setB2bUnitService(final B2BUnitService b2bUnitService)
	{
		this.b2bUnitService = b2bUnitService;
	}

}

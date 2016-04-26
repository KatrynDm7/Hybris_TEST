/*
 *
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
 */
package de.hybris.platform.chinaaccelerator.alipay.setup;

import java.util.ArrayList;
import java.util.List;


//import de.hybris.platform.acceleratorservices.setup.AbstractSystemSetup;
import de.hybris.platform.chinaaccelerator.alipay.constants.AlipayConstants;
import de.hybris.platform.commerceservices.setup.AbstractSystemSetup;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetup.Process;
import de.hybris.platform.core.initialization.SystemSetup.Type;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.core.initialization.SystemSetupParameter;


@SystemSetup(extension = AlipayConstants.EXTENSIONNAME)
public class PaymentSystemSetup extends AbstractSystemSetup
{
	@SystemSetup(type = Type.ESSENTIAL, process = Process.ALL)
	public void createEssentialData(final SystemSetupContext context)
	{
		importImpexFile(context, "/alipay/import/essential/payments.impex");
	}

	@Override
	public List<SystemSetupParameter> getInitializationOptions() {
		return new ArrayList<SystemSetupParameter>();
	}
}

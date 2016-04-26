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
 */

package de.hybris.platform.chinaaccelerator.cmscockpit.systemsetup;

import de.hybris.platform.chinaaccelerator.services.constants.ChinaacceleratorServicesConstants;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetup.Type;
import de.hybris.platform.servicelayer.impex.ImportService;
import de.hybris.platform.servicelayer.impex.impl.StreamBasedImpExResource;
import de.hybris.platform.util.CSVConstants;

import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


@SystemSetup(extension = ChinaacceleratorServicesConstants.EXTENSIONNAME)
public class CmscockpitSystemSetup
{
	private static final Logger LOG = Logger.getLogger(CmscockpitSystemSetup.class);
	private ImportService importService;

	@SystemSetup(extension = "admincockpit", type = Type.PROJECT)
	public void importConfigurationForCmsCockpitByImpexImport()
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Configuring searchboxcomponent for cmscockpit....");
		}

		InputStream is = null;
		try
		{
			is = CmscockpitSystemSetup.class.getResourceAsStream("/cmscockpitimpex/searchboxcomponent_projectdata_cmscockpit.impex");
			importService.importData(new StreamBasedImpExResource(is, CSVConstants.HYBRIS_ENCODING, Character.valueOf(';')));
		}
		finally
		{
			if (is != null)
			{
				try
				{
					is.close();
				}
				catch (final IOException e)
				{
					// do nothing
				}
			}
		}
	}

	@Required
	public void setImportService(final ImportService importService)
	{
		this.importService = importService;
	}
}
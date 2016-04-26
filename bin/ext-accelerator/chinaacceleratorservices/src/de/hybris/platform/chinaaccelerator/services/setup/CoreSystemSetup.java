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
package de.hybris.platform.chinaaccelerator.services.setup;

//import de.hybris.platform.acceleratorservices.setup.AbstractSystemSetup;
import de.hybris.platform.chinaaccelerator.services.constants.ChinaacceleratorServicesConstants;
import de.hybris.platform.commerceservices.setup.AbstractSystemSetup;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetup.Process;
import de.hybris.platform.core.initialization.SystemSetup.Type;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.core.initialization.SystemSetupParameter;
import de.hybris.platform.core.initialization.SystemSetupParameterMethod;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;


/**
 * This class provides hooks into the system's initialization and update processes.
 *
 * @see "https://wiki.hybris.com/display/release4/Hooks+for+Initialization+and+Update+Process"
 */
@SystemSetup(extension = ChinaacceleratorServicesConstants.EXTENSIONNAME)
public class CoreSystemSetup extends AbstractSystemSetup
{
	private static final Logger LOG = Logger.getLogger(CoreSystemSetup.class);


	public static final String IMPORT_ACCESS_RIGHTS = "accessRights";

	/**
	 * This method will be called by system creator during initialization and system update. Be sure that this method can
	 * be called repeatedly.
	 *
	 * @param context
	 *           the context provides the selected parameters and values
	 */
	@SystemSetup(type = Type.ESSENTIAL, process = Process.ALL)
	public void createEssentialData(final SystemSetupContext context)
	{
		importImpexFile(context, "/chinaacceleratorservices/import/common/essential-data.impex");
		importImpexFile(context, "/chinaacceleratorservices/import/common/countries.impex");
		// CHINAACC_START
		importImpexFile(context, "/chinaacceleratorservices/import/common/cities.impex");
		importImpexFile(context, "/chinaacceleratorservices/import/common/districts.impex");
		// CHINAACC_END
		importImpexFile(context, "/chinaacceleratorservices/import/common/delivery-modes.impex");

		importImpexFile(context, "/chinaacceleratorservices/import/common/themes.impex");
		importImpexFile(context, "/chinaacceleratorservices/import/common/user-groups.impex");

		// CHINAACC_START
		// CHINAACC: TODO: verify w/ Tony about loading of payments.impex
		importImpexFile(context, "/chinaacceleratorservices/import/common/payments.impex");
		// CHINAACC_END
	}

	/**
	 * Generates the Dropdown and Multi-select boxes for the project data import
	 */
	@Override
	@SystemSetupParameterMethod
	public List<SystemSetupParameter> getInitializationOptions()
	{
		final List<SystemSetupParameter> params = new ArrayList<SystemSetupParameter>();

		params.add(createBooleanSystemSetupParameter(IMPORT_ACCESS_RIGHTS, "Import Users & Groups", true));

		return params;
	}

	/**
	 * This method will be called during the system initialization.
	 *
	 * @param context
	 *           the context provides the selected parameters and values
	 */
	/*
	 * @SystemSetup(type = Type.PROJECT, process = Process.ALL) public void createProjectData(final SystemSetupContext
	 * context) { final boolean importAccessRights = getBooleanSystemSetupParameter(context, IMPORT_ACCESS_RIGHTS);
	 *
	 * final List<String> extensionNames = getExtensionNames();
	 *
	 * if (importAccessRights && extensionNames.contains("cmscockpit")) { importImpexFile(context,
	 * "/chinaacceleratorservices/import/cockpits/cmscockpit/cmscockpit-users.impex"); importImpexFile(context,
	 * "/chinaacceleratorservices/import/cockpits/cmscockpit/cmscockpit-access-rights.impex"); }
	 *
	 * if (importAccessRights && extensionNames.contains("btgcockpit")) { importImpexFile(context,
	 * "/chinaacceleratorservices/import/cockpits/cmscockpit/btgcockpit-users.impex"); importImpexFile(context,
	 * "/chinaacceleratorservices/import/cockpits/cmscockpit/btgcockpit-access-rights.impex"); }
	 *
	 * if (importAccessRights && extensionNames.contains("productcockpit")) { importImpexFile(context,
	 * "/chinaacceleratorservices/import/cockpits/productcockpit/productcockpit-users.impex"); importImpexFile(context,
	 * "/chinaacceleratorservices/import/cockpits/productcockpit/productcockpit-access-rights.impex");
	 * importImpexFile(context,
	 * "/chinaacceleratorservices/import/cockpits/productcockpit/productcockpit-constraints.impex"); }
	 *
	 * if (importAccessRights && extensionNames.contains("cscockpit")) { importImpexFile(context,
	 * "/chinaacceleratorservices/import/cockpits/cscockpit/cscockpit-users.impex"); importImpexFile(context,
	 * "/chinaacceleratorservices/import/cockpits/cscockpit/cscockpit-access-rights.impex"); }
	 *
	 * if (importAccessRights && extensionNames.contains("reportcockpit")) { importImpexFile(context,
	 * "/chinaacceleratorservices/import/cockpits/reportcockpit/reportcockpit-users.impex"); importImpexFile(context,
	 * "/chinaacceleratorservices/import/cockpits/reportcockpit/reportcockpit-access-rights.impex"); }
	 *
	 * if (extensionNames.contains("mcc")) { importImpexFile(context,
	 * "/chinaacceleratorservices/import/common/mcc-sites-links.impex"); } }
	 */


	protected List<String> getExtensionNames()
	{
		return Registry.getCurrentTenant().getTenantSpecificExtensionNames();
	}

	protected <T> T getBeanForName(final String name)
	{
		return (T) Registry.getApplicationContext().getBean(name);
	}
}

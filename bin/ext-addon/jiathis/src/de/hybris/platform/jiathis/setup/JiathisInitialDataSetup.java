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
package de.hybris.platform.jiathis.setup;

import de.hybris.platform.commerceservices.setup.AbstractSystemSetup;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetup.Process;
import de.hybris.platform.core.initialization.SystemSetup.Type;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.core.initialization.SystemSetupParameter;
import de.hybris.platform.core.initialization.SystemSetupParameterMethod;
import de.hybris.platform.jiathis.constants.JiathisConstants;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;


/**
 * This class provides hooks into the system's initialization and update processes.
 *
 * @see "https://wiki.hybris.com/display/release4/Hooks+for+Initialization+and+Update+Process"
 */
@SystemSetup(extension = JiathisConstants.EXTENSIONNAME)
public class JiathisInitialDataSetup extends AbstractSystemSetup
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(JiathisInitialDataSetup.class);

	public static final String SITE_ELECTRONICS_CN = "electronics-cn";
	public static final String SITE_ELECTRONICS = "electronics";

	private static final String JIATHIS_IMPEX_CNACC = "/jiathis/import/contentCatalogs/electronics-cnContentCatalog/jiathisPage.impex";

	private static final String JIATHIS_IMPEX_ELECTRONICS = "/jiathis/import/contentCatalogs/electronicsContentCatalog/jiathisPage.impex";

	private static final String IMPORT_CNACC_DATA = "importCNACCData";
	private static final String IMPORT_B2C_DATA = "importB2CData";

	/**
	 * Generates the Dropdown and Multi-select boxes for the project data import
	 */
	@Override
	@SystemSetupParameterMethod
	public List<SystemSetupParameter> getInitializationOptions()
	{

		final List<SystemSetupParameter> params = new ArrayList<SystemSetupParameter>();

		params.add(createBooleanSystemSetupParameter(IMPORT_CNACC_DATA, "Import China Accelerator Data", true));
		params.add(createBooleanSystemSetupParameter(IMPORT_B2C_DATA, "Import B2C Accelerator Data", false));

		return params;
	}


	/**
	 * Implement this method to create data that is used in your project. This method will be called during the system
	 * initialization.
	 *
	 * @param context
	 *           the context provides the selected parameters and values
	 * @param systemSetup
	 */
	@SystemSetup(type = Type.PROJECT, process = Process.ALL)
	public void createProjectData(final SystemSetupContext context)
	{


		// improt cnacc data
		if (this.getBooleanSystemSetupParameter(context, IMPORT_CNACC_DATA))
		{

			importExectronicsCN(context);

		}

		// improt b2c data
		if (this.getBooleanSystemSetupParameter(context, IMPORT_B2C_DATA))
		{

			importExectronics(context);

		}
	}

	/**
	 * improt data for store site ececltronics-cn
	 *
	 * @param context
	 */
	void importExectronicsCN(final SystemSetupContext context)
	{
		// the impex of jiathis page

		importJiaThisData(context, JIATHIS_IMPEX_CNACC);

	}

	/**
	 * improt data for store site ececltronics
	 *
	 * @param context
	 */
	void importExectronics(final SystemSetupContext context)
	{
		// the impex of jiathis page

		importJiaThisData(context, JIATHIS_IMPEX_ELECTRONICS);

	}


	void importJiaThisData(final SystemSetupContext context, final String dataLocation)
	{
		this.logInfo(context, String.format("Begin importing data for [%s]", context.getExtensionName()));
		getSetupImpexService().importImpexFile(String.format(dataLocation, context.getExtensionName()), false);
	}

}

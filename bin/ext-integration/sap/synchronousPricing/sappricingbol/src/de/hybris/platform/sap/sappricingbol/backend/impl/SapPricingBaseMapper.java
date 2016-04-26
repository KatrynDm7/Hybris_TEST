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
package de.hybris.platform.sap.sappricingbol.backend.impl;

import de.hybris.platform.sap.core.bol.logging.Log4JWrapper;
import de.hybris.platform.sap.core.module.ModuleConfigurationAccess;
import de.hybris.platform.sap.sappricingbol.constants.SappricingbolConstants;
import de.hybris.platform.sap.sappricingbol.enums.PricingProceduresSubtotal;

import org.springframework.beans.factory.annotation.Required;

import sap.hybris.integration.models.constants.SapmodelConstants;

import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoStructure;


/**
 * SapPricingBaseMapper
 */
public class SapPricingBaseMapper
{
	
	static final private Log4JWrapper sapLogger = Log4JWrapper.getInstance(SapPricingBaseMapper.class.getName());
	private ModuleConfigurationAccess moduleConfigurationAccess = null;

	/**
	 * @return ModuleConfigurationAccess
	 */
	public ModuleConfigurationAccess getModuleConfigurationAccess()
	{
		return moduleConfigurationAccess;
	}

	/**
	 * @param moduleConfigurationAccess
	 */
	@Required
	public void setModuleConfigurationAccess(final ModuleConfigurationAccess moduleConfigurationAccess)
	{
		this.moduleConfigurationAccess = moduleConfigurationAccess;
	}

	/**
	 * write import parameters to JCo structures
	 * 
	 * @param importParameters
	 */
	public void fillImportParameters(final JCoParameterList importParameters , final boolean catalog)
	{

		// Set control attributes
		final JCoStructure isControl = importParameters.getStructure("IS_CONTROL");
		isControl.setValue("EXTERNAL_FORMAT", SappricingbolConstants.NO);
		isControl.setValue("GROUP_PROCESSING", catalog ? SappricingbolConstants.NO : SappricingbolConstants.YES ); 
		isControl.setValue("PRICE_DETAILS", SappricingbolConstants.YES);
		isControl.setValue("KALSM_VARIANT", SappricingbolConstants.NO);

		// Set pricing control attributes
		final JCoStructure isPricingControl = isControl.getStructure("PRICING_CONTROL");
		isPricingControl.setValue("GET_SCALE_LEVELS", catalog ? SappricingbolConstants.SCALE_LEVELS :SappricingbolConstants.NO);
		isPricingControl.setValue("MAX_SCALE_LEVELS", SappricingbolConstants.NO);
		isPricingControl.setValue("PRIC_DETAIL_VAR", SappricingbolConstants.NO);

		isControl.setValue("PRICING_CONTROL", isPricingControl);
		importParameters.setValue("IS_CONTROL", isControl);


		// Set global attributes
		final JCoStructure isGlobal = importParameters.getStructure("IS_GLOBAL");
		isGlobal.setValue("PRSDT", SappricingbolConstants.NO);
		isGlobal.setValue("AUART", getProperty(SapmodelConstants.CONFIGURATION_PROPERTY_TRANSACTION_TYPE));
		isGlobal.setValue("VKORG", getProperty(SapmodelConstants.CONFIGURATION_PROPERTY_SALES_ORG));
		isGlobal.setValue("VTWEG", getProperty(SapmodelConstants.CONFIGURATION_PROPERTY_DISTRIBUTION_CHANNEL));
		isGlobal.setValue("SPART", getProperty(SapmodelConstants.CONFIGURATION_PROPERTY_DIVISION));
		importParameters.setValue("IS_GLOBAL", isGlobal);
		

		isGlobal.setValue("CALLER_DATA", SappricingbolConstants.NO);

		importParameters.setValue("IV_CALLER_ID", SappricingbolConstants.CALLER_ID);

		if (sapLogger.isDebugEnabled())
		{
			sapLogger.debug("sappricingbol: RFC Call Global Parameters");
			sapLogger.debug(isGlobal.toString());
			
			sapLogger.debug("sappricingbol: RFC Call Control Parameters");
			sapLogger.debug(isControl.toString());
		}
				
	}

	protected String getProperty(final String name)
	{
		final Object propertyValue = getModuleConfigurationAccess().getProperty(name);

		// Some configuration attributes are read as enumeration types, we need to convert them to Strings
		if (propertyValue instanceof PricingProceduresSubtotal)
		{
			return ((PricingProceduresSubtotal) propertyValue).getCode();
		}

		return (String) propertyValue;
	}

}

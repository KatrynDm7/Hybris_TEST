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


import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.jalo.order.price.PriceInformation;
import de.hybris.platform.sap.core.jco.exceptions.BackendException;
import de.hybris.platform.sap.core.bol.backend.BackendType;
import de.hybris.platform.sap.core.bol.backend.jco.BackendBusinessObjectBaseJCo;
import de.hybris.platform.sap.core.jco.connection.JCoConnection;
import de.hybris.platform.sap.core.jco.connection.JCoManagedConnectionFactory;
import de.hybris.platform.sap.core.jco.connection.JCoStateful;
import de.hybris.platform.sap.core.bol.businessobject.CommunicationException;
import de.hybris.platform.sap.core.bol.logging.Log4JWrapper;
import de.hybris.platform.sap.core.bol.logging.LogCategories;
import de.hybris.platform.sap.core.bol.logging.LogSeverity;
import de.hybris.platform.sap.sappricingbol.backend.interf.SapPricingBackend;
import de.hybris.platform.sap.sappricingbol.businessobject.interf.SapPricingPartnerFunction;
import de.hybris.platform.sap.sappricingbol.constants.SappricingbolConstants;
import de.hybris.platform.sap.sappricingbol.converter.ConversionService;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Required;

import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoTable;


/**
 * 
 */
@BackendType("ERP")
public class SapPricingBackendERP extends BackendBusinessObjectBaseJCo implements SapPricingBackend
{
	static final private Log4JWrapper sapLogger = Log4JWrapper.getInstance(SapPricingBackendERP.class.getName());

	@Resource(name = "sapCoreJCoManagedConnectionFactory")
	protected JCoManagedConnectionFactory managedConnectionFactory; //NOPMD

	private SapPricingMapper pricingMapper;
	private SapPricingCachedBackendERP cacheAccess;

	@Override
	public List<PriceInformation> readPriceInformationForProducts(List<ProductModel> productModels,
			SapPricingPartnerFunction partnerFunction, ConversionService conversionService) throws BackendException
	{

		sapLogger.entering("readPriceInformationForProducts(...)");

		List<PriceInformation> priceInformationList = cacheAccess.readCachedPriceInformationForProducts(productModels,
				partnerFunction);
		JCoConnection connection = null;

		// Check if the catalog price is cached
		if (priceInformationList != null)
		{
			return priceInformationList;
		}
		try
		{
			// Get JCo connection
			connection = managedConnectionFactory.getManagedConnection(getDefaultConnectionName(), this.getClass().getName());

			// Get function module
			final JCoFunction function = connection.getFunction(SappricingbolConstants.FM_PIQ_CALCULATE);

			// Get import parameters
			final JCoParameterList importParameters = function.getImportParameterList();
            
			// Fill import parameters
			pricingMapper.fillImportParameters(importParameters, productModels, partnerFunction, conversionService);
	
			// Execute 
			connection.execute(function);

			// Read parameter list
			final JCoParameterList exportParameterList = function.getExportParameterList();

			// Read back-end messages
			final JCoTable etMessage = function.getExportParameterList().getTable("ET_MESSAGE");
			logMesages(etMessage);

			// Read catalog price
			final JCoTable resultTable = exportParameterList.getTable("ET_RESULT");

			priceInformationList = pricingMapper.readResultExport(resultTable);
			
			// Cache the price
			cacheAccess.cachePriceInformationForProducts(productModels, partnerFunction, priceInformationList);
		}
		finally
		{
			closeConnection(connection);
		}
		sapLogger.exiting();

		return priceInformationList;
	}

	@Override
	public void readPricesForCart(final AbstractOrderModel order, final SapPricingPartnerFunction partnerFunction,
			ConversionService conversionService) throws BackendException, CommunicationException
	{
		sapLogger.entering("readPriceInformationForProduct(...)");

		JCoConnection connection = null;

		try
		{
			// Get JCo connection
			connection = managedConnectionFactory.getManagedConnection(getDefaultConnectionName(), this.getClass().getName());

			// Get function module
			final JCoFunction function = connection.getFunction(SappricingbolConstants.FM_PIQ_CALCULATE);

			// Get import parameters
			final JCoParameterList importParameters = function.getImportParameterList();
			
			// Fill import parameters
			pricingMapper.fillImportParameters(importParameters, order, partnerFunction, conversionService);
			
			// Execute 
			connection.execute(function);

			// Read parameter list
			final JCoParameterList exportParameterList = function.getExportParameterList();

			// Read back-end messages
			final JCoTable etMessage = function.getExportParameterList().getTable("ET_MESSAGE");
			logMesages(etMessage);

			// Read cart prices 
			final JCoTable resultTable = exportParameterList.getTable("ET_RESULT");
	
			pricingMapper.readResultExport(order, resultTable, conversionService);
		}
		finally
		{
			closeConnection(connection);	
		}
		sapLogger.exiting();
	}

	protected void closeConnection(final JCoConnection connection)
	{
		try{
			
			if (connection != null)
			{
				((JCoStateful) connection).destroy();
			}
		}
		catch(BackendException ex){	
			
		sapLogger.log(LogSeverity.ERROR, LogCategories.APPS_COMMON_RESOURCES ,"Error during JCoStateful connection close! " + ex.getMessage());
		
		}
	}
	
	protected void logMesages(final JCoTable etMessage)
	{
		if (!etMessage.isEmpty())
		{
			for (int i = 0; i < etMessage.getNumRows(); i++)
			{
				etMessage.setRow(i);
				if (etMessage.getString("TYPE").contentEquals("E"))
				{
					sapLogger.log(LogSeverity.ERROR, LogCategories.APPLICATIONS, etMessage.getString("MESSAGE"));
				}
			}
		}
	}

	/**
	 * @return CacheAccess
	 */
	public SapPricingCachedBackendERP getCacheAccess()
	{
		return cacheAccess;
	}

	/**
	 * @param cacheAccess
	 */
	@Required
	public void setCacheAccess(final SapPricingCachedBackendERP cacheAccess)
	{
		this.cacheAccess = cacheAccess;
	}

	public void setManagedConnectionFactory(JCoManagedConnectionFactory managedConnectionFactory)
	{
		this.managedConnectionFactory = managedConnectionFactory;
	}

	/**
	 * @return SapPricingMapper
	 */
	public SapPricingMapper getPricingMapper() {
		return pricingMapper;
	}

	/**
	 * @param pricingMapper
	 */
	@Required
	public void setPricingMapper(SapPricingMapper pricingMapper) {
		this.pricingMapper = pricingMapper;
	}

	

}

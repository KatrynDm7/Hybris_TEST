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

import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoTable;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.jalo.order.price.PriceInformation;
import de.hybris.platform.sap.sappricingbol.businessobject.interf.SapPricingPartnerFunction;
import de.hybris.platform.sap.sappricingbol.converter.ConversionService;

public class SapPricingMapper {
	
	private SapPricingBaseMapper   baseMapper;
	private SapPricingItemMapper   itemMapper;
	private SapPricingHeaderMapper headerMapper;
	
	/**
	 * Fill catalog import parameters
	 * @param importParameters
	 * @param productModels
	 * @param partnerFunction
	 * @param conversionService
	 */
	public void fillImportParameters(final JCoParameterList importParameters , 
                                     final List<ProductModel> productModels,
			                         final SapPricingPartnerFunction partnerFunction,
			             			 final ConversionService conversionService ){
		
		fillCatalogControlAndGlobalImports(importParameters);
		fillHeaderImports(importParameters, partnerFunction);
		fillItemImports(importParameters, productModels, conversionService);						
	}
	
	 /**
	 * Fill cart import parameters
	 * @param importParameters
	 * @param order
	 * @param partnerFunction
	 * @param conversionService
	 */
	public void fillImportParameters(final JCoParameterList importParameters , 
	          final AbstractOrderModel order,
	          final SapPricingPartnerFunction partnerFunction,
			  final ConversionService conversionService ){
		
		fillCartControlAndGlobalImports(importParameters);
		fillHeaderImports(importParameters, partnerFunction, order);
		fillItemImports(order, importParameters, conversionService);			 
	 }

	/**
	 * Read catalog result prices
	 * @param resultTable
	 * @return
	 */
	public List<PriceInformation> readResultExport(final JCoTable resultTable){
		return getItemMapper().readPrices(resultTable);
	}
	/**
	 * Read cart result table 
	 * @param order
	 * @param resultTable
	 * @param conversionService
	 */
	public void readResultExport(AbstractOrderModel order, final JCoTable resultTable, ConversionService conversionService)
	{
		getItemMapper().readPrices(order, resultTable, conversionService);			
	}
	/**
	 * Fill catalog items imports
	 * @param importParameters
	 * @param productModels
	 * @param conversionService
	 */
	private void fillItemImports(JCoParameterList importParameters,
			List<ProductModel> productModels,
			ConversionService conversionService) {
		getItemMapper().fillImportParameters(importParameters, productModels, conversionService);	
	}
	
	/**
	 * Fill cart items imports
	 * @param order
	 * @param importParameters
	 * @param conversionService
	 */
	private void fillItemImports(final AbstractOrderModel order, 
            final JCoParameterList importParameters,
            final ConversionService conversionService) {
		getItemMapper().fillImportParameters(order, importParameters, conversionService);	
	}
	
	/**
	 *  Fill catalog header imports
	 * @param importParameters
	 * @param partnerFunction
	 */
	private void fillHeaderImports(JCoParameterList importParameters,
			SapPricingPartnerFunction partnerFunction) {
		getHeaderMapper().fillImportParameters(importParameters, partnerFunction);		
	}
	
	/**
	 *  Fill cart header imports
	 * @param importParameters
	 * @param partnerFunction
	 * @param order
	 */
	private void fillHeaderImports(final JCoParameterList importParameters, 
								   final SapPricingPartnerFunction partnerFunction,
								   final AbstractOrderModel order) {
		
		getHeaderMapper().fillImportParameters(importParameters, partnerFunction, order);

	}
	
	/**
	 *  Fill cart control and global imports
	 * @param importParameters
	 */
	private void fillCartControlAndGlobalImports(final JCoParameterList importParameters) {
			
			getBaseMapper().fillImportParameters(importParameters, false);
	}

	/**
	 * Fill catalog control and global imports
	 * @param importParameters
	 */
	private void fillCatalogControlAndGlobalImports(final JCoParameterList importParameters) {
		
		getBaseMapper().fillImportParameters(importParameters, true);	
	}
		
	/**
	 * @return SapPricingBaseMapper
	 */
	public SapPricingBaseMapper getBaseMapper() {
		return baseMapper;
	}

	 /**
	 * @param baseMapper
	 */
	@Required
	public void setBaseMapper(SapPricingBaseMapper baseMapper) {
		this.baseMapper = baseMapper;
	}


	 /**
	 * @return SapPricingItemMapper
	 */
	public SapPricingItemMapper getItemMapper() {
		return itemMapper;
	}

	/**
	 * @param itemMapper
	 */
	@Required
	public void setItemMapper(SapPricingItemMapper itemMapper) {
		this.itemMapper = itemMapper;
	}


	/**
	 * @return SapPricingHeaderMapper
	 */
	public SapPricingHeaderMapper getHeaderMapper() {
		return headerMapper;
	}

    /**
     * @param headerMapper
     */
    @Required
    public void setHeaderMapper(SapPricingHeaderMapper headerMapper) {
		this.headerMapper = headerMapper;
	}
			
}

/**
 * 
 */
package de.hybris.platform.sap.sapproductavailability.service.impl;

import de.hybris.platform.acceleratorservices.futurestock.impl.DefaultFutureStockService;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.sap.sapproductavailability.service.SapProductAvailabilityService;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Required;


/**
 * @author Administrator
 * 
 */
public class DefaultSapFutureStockService extends DefaultFutureStockService
{

	private SapProductAvailabilityService sapProductAvailabilityService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.acceleratorservices.futurestock.FutureStockService#getFutureAvailability(java.util.List)
	 */

	@Override
	public Map<String, Map<Date, Integer>> getFutureAvailability(final List<ProductModel> products)
	{
		if (getSapProductAvailabilityService().isSynchronousATPCheckActive())
		{
			return getSapProductAvailabilityService().readProductFutureAvailability(products.get(0));
		}
		else
		{
			return super.getFutureAvailability(products);
		}
	}

	/**
	 * @return the sapProductAvailabilityService
	 */
	public SapProductAvailabilityService getSapProductAvailabilityService()
	{
		return sapProductAvailabilityService;
	}

	/**
	 * @param sapProductAvailabilityService
	 *           the sapProductAvailabilityService to set
	 */
	@Required
	public void setSapProductAvailabilityService(final SapProductAvailabilityService sapProductAvailabilityService)
	{
		this.sapProductAvailabilityService = sapProductAvailabilityService;
	}


}

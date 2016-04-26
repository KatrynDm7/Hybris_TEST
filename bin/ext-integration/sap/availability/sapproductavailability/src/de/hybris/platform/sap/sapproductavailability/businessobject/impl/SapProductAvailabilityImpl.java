package de.hybris.platform.sap.sapproductavailability.businessobject.impl;

import de.hybris.platform.sap.sapproductavailability.businessobject.SapProductAvailability;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * Immutable Object
 * 
 * @author Administrator
 * 
 */
public class SapProductAvailabilityImpl implements SapProductAvailability
{

	private final Long currentStockLevel;

	private final Map<String, Map<Date, Long>> futureAvailability;

	/**
	 * @param currentStockLevel
	 * @param futureAvailability
	 */
	public SapProductAvailabilityImpl(final Long currentStockLevel, final Map<String, Map<Date, Long>> futureAvailability)
	{
		this.currentStockLevel = Long.valueOf(currentStockLevel.longValue());
		this.futureAvailability = new HashMap<String, Map<Date, Long>>(futureAvailability);
	}

	@Override
	public Long getCurrentStockLevel()
	{
		return Long.valueOf(this.currentStockLevel.longValue());
	}

	@Override
	public Map<String, Map<Date, Long>> getFutureAvailability()
	{
		return new HashMap<String, Map<Date, Long>>(this.futureAvailability);
	}

}

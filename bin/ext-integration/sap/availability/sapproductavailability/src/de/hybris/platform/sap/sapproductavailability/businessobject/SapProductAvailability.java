package de.hybris.platform.sap.sapproductavailability.businessobject;

import java.util.Date;
import java.util.Map;


public interface SapProductAvailability
{

	Long getCurrentStockLevel();

	Map<String, Map<Date, Long>> getFutureAvailability();

}

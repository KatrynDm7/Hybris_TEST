/**
 * 
 */
package de.hybris.platform.sap.sapproductavailability.businessobject;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.sap.core.jco.exceptions.BackendException;


/**
 * @author Administrator
 * 
 */
public interface SapProductAvailabilityBO
{

	/**
	 * reads the current stock level for a product + future available quantities
	 * @param product
	 * @param customerId
	 * @param plant
	 * @param requestedQuantity
	 * @return @SapProductAvailability  
	 */
	SapProductAvailability readProductAvailability(final ProductModel product, final String customerId, String plant, final Long requestedQuantity);
	
	/**
	 * Perform Determination Based on Material This checks which plant is 
	 * assigned to the material for the selected product.
	 * @param material
	 * @return String plant
	 * @throws BackendException
	 */
	String readPlantForMaterial(String material);


	/**
	 * Gets the plant from the customer material record. Uses RFC BAPI_CUSTMATINFO_GETDETAILM.
	 * @param material
	 * @param customerId
	 * @return Plant 
	 */
	String readPlantForCustomerMaterial(String material, String customerId);


	
	/**
	 * Returns the plant name given information about a product.
	 * @param product
	 * @param customerId
	 * @return String plant
	 */
	String readPlant(ProductModel product, String customerId);
	
}

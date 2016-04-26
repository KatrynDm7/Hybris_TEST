/**
 * 
 */
package de.hybris.platform.sap.sapproductavailability.backend;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.sap.core.jco.exceptions.BackendException;
import de.hybris.platform.sap.sapproductavailability.businessobject.SapProductAvailability;


/**
 * @author Administrator
 * 
 */
public interface SapProductAvailabilityBackend extends de.hybris.platform.sap.core.bol.backend.BackendBusinessObject
{

	/**
	 * Perform Determination Based on Material This checks which plant is 
	 * assigned to the material for the selected product.
	 * @param material
	 * @return String plant
	 * @throws BackendException
	 */
	String readPlantForMaterial(String material) throws BackendException;


	/**
	 * Gets the plant from the customer material record. Uses RFC BAPI_CUSTMATINFO_GETDETAILM.
	 * @param material
	 * @param customerId
	 * @return Plant 
	 * @throws BackendException
	 */
	String readPlantForCustomerMaterial(String material, String customerId)
			throws BackendException;


	/**
	 * Reads availability information given the search criteria.
     * (Might return '0 for current date available, 1 for tomorrow').
	 * @param product
	 * @param customerId
	 * @param plant
	 * @param requestedQuantity
	 * @return
	 * @throws BackendException
	 */
	SapProductAvailability readProductAvailability(ProductModel product, String customerId,
			String plant, Long requestedQuantity) throws BackendException;


	/**
	 * Returns the plant name given information about a product.
	 * @param product
	 * @param customerId
	 * @return String plant
	 * @throws BackendException
	 */
	String readPlant(ProductModel product, String customerId)
			throws BackendException;







	



	
}

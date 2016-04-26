/**
 *
 */
package de.hybris.platform.sap.sapcreditcheck.backend;

import de.hybris.platform.commercefacades.order.data.AbstractOrderData;
import de.hybris.platform.sap.core.bol.backend.BackendBusinessObject;
import de.hybris.platform.sap.core.jco.exceptions.BackendException;


/**
 * @author Administrator
 *
 */
public interface SapCreditCheckBackend extends BackendBusinessObject
{
	/**
	 * checks if status of order is credit blocked
	 * @param orderCode
	 * @return a boolean flag indicating if the order is credit blocked
	 * @throws BackendException
	 */
	public boolean checkOrderCreditBlocked(final String orderCode) throws BackendException;

	/**
	 * 
	 * @param orderData
	 * @param soldTo
	 * @return
	 * @throws BackendException
	 */
	boolean checkCreditLimitExceeded(AbstractOrderData orderData, String soldTo)
			throws BackendException;
}

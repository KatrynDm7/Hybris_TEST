/**
 *
 */
package de.hybris.platform.sap.sapcreditcheck.businessobject;

import de.hybris.platform.commercefacades.order.data.AbstractOrderData;



/**
 * @author Administrator
 *
 */
public interface SapCreditCheckBO
{

	/**
	 * checks if status of order is credit blocked
	 * @param orderCode
	 * @return a boolean flag indicating if the order is credit blocked
	 */
	abstract boolean checkOrderCreditBlocked(final String orderCode);

	/**
	 * 
	 * @param orderData
	 * @param soldTo
	 * @return
	 */
	abstract boolean checkCreditLimitExceeded(AbstractOrderData orderData, String soldTo);
}

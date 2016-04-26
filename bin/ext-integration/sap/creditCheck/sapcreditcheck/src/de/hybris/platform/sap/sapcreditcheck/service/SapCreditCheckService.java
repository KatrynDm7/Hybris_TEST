/**
 * 
 */
package de.hybris.platform.sap.sapcreditcheck.service;

import de.hybris.platform.commercefacades.order.data.AbstractOrderData; 


/**
 * @author Administrator
 * 
 */
public interface SapCreditCheckService 
{
	/**
	 * 
	 * @return true if the credit limit has been exceeded
	 */
	abstract boolean checkCreditLimitExceeded(AbstractOrderData order);
	
	
	/**
	 * Check if the order is blocked in ERP due to exceeding credit limit
	 * @param orderCode
	 * @return true if order is credit blocked
	 */
	abstract boolean checkOrderCreditBlocked(String orderCode);
	
}

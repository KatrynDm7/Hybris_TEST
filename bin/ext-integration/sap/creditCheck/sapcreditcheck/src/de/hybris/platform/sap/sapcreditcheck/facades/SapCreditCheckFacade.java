/**
 * 
 */
package de.hybris.platform.sap.sapcreditcheck.facades;

/**
 * @author FirstName LastName
 *
 */
public interface SapCreditCheckFacade
{
	
	/**
	 * Check if the credit limit has been exceeded 
	 * @return true if credit check exceeded
	 */
	abstract boolean checkCreditLimitExceeded(String orderCode);
	 
	
	/**
	 * checks if order ir credit bocked
	 * @param orderCode
	 * @return true if order is credit blocked
	 */
	abstract boolean checkOrderCreditBlocked(String orderCode);
	
	
	
	
}

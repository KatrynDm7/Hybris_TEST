/**
 *
 */
package de.hybris.platform.sap.sapcreditcheck.businessobject.impl;

import de.hybris.platform.commercefacades.order.data.AbstractOrderData;
import de.hybris.platform.sap.core.bol.businessobject.BackendInterface;
import de.hybris.platform.sap.core.bol.businessobject.BusinessObjectBase;
import de.hybris.platform.sap.core.jco.exceptions.BackendException;
import de.hybris.platform.sap.sapcreditcheck.backend.SapCreditCheckBackend;
import de.hybris.platform.sap.sapcreditcheck.businessobject.SapCreditCheckBO;
import de.hybris.platform.sap.sapcreditcheck.exceptions.SapCreditCheckException;


/**
 * @author SAP
 *
 */
@BackendInterface(SapCreditCheckBackend.class)
public class SapCreditCheckBOImpl extends BusinessObjectBase implements SapCreditCheckBO
{
	
	/**
	 * @return the sapCreditCheckBackend
	 * @throws BackendException
	 */
	public SapCreditCheckBackend getSapCreditCheckBackend() throws BackendException
	{
	  return  (SapCreditCheckBackend) getBackendBusinessObject();
	}

	@Override
	public boolean checkOrderCreditBlocked(final String orderCode)
	{
		try
		{
			return getSapCreditCheckBackend().checkOrderCreditBlocked(orderCode);
		}
		catch (final BackendException e)
		{
			throw new SapCreditCheckException(e);
		}
	}
	
	@Override
	public boolean checkCreditLimitExceeded(final AbstractOrderData orderData, final String soldTo)
	{
		try
		{
			return getSapCreditCheckBackend().checkCreditLimitExceeded(orderData, soldTo);
		}
		catch (final BackendException e)
		{
			throw new SapCreditCheckException(e);
		}
	}

}

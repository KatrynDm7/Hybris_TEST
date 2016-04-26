package de.hybris.platform.sap.sapcreditcheck.facades.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import de.hybris.platform.b2bacceleratorfacades.order.impl.DefaultB2BOrderFacade;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.sap.sapcreditcheck.service.SapCreditCheckService;

public class DefaultSapB2BOrderFacade extends DefaultB2BOrderFacade{ 

	static final private Logger sapLogger = Logger.getLogger(DefaultSapB2BOrderFacade.class.getName());
	private SapCreditCheckService sapCreditCheckService;// NOPMD
	
	public SapCreditCheckService getSapCreditCheckService() {
		return sapCreditCheckService;
	}
	
	@Required
	public void setSapCreditCheckService(SapCreditCheckService sapCreditCheckService) {
		this.sapCreditCheckService = sapCreditCheckService;
	}

	@Override
	public OrderData getOrderDetailsForCode(String code) {
		
		OrderData orderData = super.getOrderDetailsForCode(code);
	    Boolean creditBlockStatus = true; 
	    
       try {
			
    	   creditBlockStatus = getSapCreditCheckService().checkOrderCreditBlocked(code); 
						
		} catch (Exception ex) {
			
			sapLogger.error(String.format("An exception was thrown while checking the credit status for the order %s from the ERP backend! ", code) + ex.getMessage());				
			
		}
	
		if (creditBlockStatus) {
			orderData.setStatus(OrderStatus.PENDING_APPROVAL_FROM_MERCHANT);
			orderData.setStatusDisplay("pending.merchant.approval");
		}
		
		return orderData;
	}
		
}

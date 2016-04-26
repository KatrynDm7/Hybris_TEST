/**
 *
 */
package de.hybris.platform.sap.sapcreditcheck.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BCustomerService;
import de.hybris.platform.b2b.services.B2BUnitService;
import de.hybris.platform.commercefacades.order.data.AbstractOrderData;
import de.hybris.platform.sap.core.module.ModuleConfigurationAccess;
import de.hybris.platform.sap.sapcreditcheck.constants.SapcreditcheckConstants;
import de.hybris.platform.sap.sapcreditcheck.service.SapCreditCheckBOFactory;
import de.hybris.platform.sap.sapcreditcheck.service.SapCreditCheckService;
import de.hybris.platform.store.services.BaseStoreService;


/**
 * @author Administrator
 *
 */
public class DefaultSapCreditCheckService implements SapCreditCheckService 
{

	private static final Logger sapLogger = Logger.getLogger(DefaultSapCreditCheckService.class);
	  
	private ModuleConfigurationAccess moduleConfigurationAccess; // NOPMD
	private SapCreditCheckBOFactory sapCreditCheckBOFactory; // NOPMD
	private B2BCustomerService<B2BCustomerModel, B2BUnitModel> b2bCustomerService;// NOPMD
	private B2BUnitService<B2BUnitModel, B2BCustomerModel> b2bUnitService;// NOPMD
	private BaseStoreService baseStoreService;// NOPMD
	
	/**
	 * @return B2BCustomerService<B2BCustomerModel, B2BUnitModel>
	 */
	public B2BCustomerService<B2BCustomerModel, B2BUnitModel> getB2bCustomerService()
	{
		return b2bCustomerService;
	}

	/**
	 * @param b2bCustomerService
	 */
	@Required
	public void setB2bCustomerService(final B2BCustomerService<B2BCustomerModel, B2BUnitModel> b2bCustomerService)
	{
		this.b2bCustomerService = b2bCustomerService;
	}

	/**
	 * @return B2BUnitService<B2BUnitModel, B2BCustomerModel>
	 */
	public B2BUnitService<B2BUnitModel, B2BCustomerModel> getB2bUnitService()
	{
		return b2bUnitService;
	}

	
	/**
	 * @return
	 */
	public BaseStoreService getBaseStoreService() {
		return baseStoreService;
	}

	/**
	 * @param baseStoreService
	 */
	@Required
	public void setBaseStoreService(BaseStoreService baseStoreService) {
		this.baseStoreService = baseStoreService;
	}

	/**
	 * @param b2bUnitService
	 */
	@Required
	public void setB2bUnitService(final B2BUnitService<B2BUnitModel, B2BCustomerModel> b2bUnitService)
	{
		this.b2bUnitService = b2bUnitService;
	}

	public ModuleConfigurationAccess getModuleConfigurationAccess() {
		return moduleConfigurationAccess;
	}

	@Required
	public void setModuleConfigurationAccess(
			ModuleConfigurationAccess moduleConfigurationAccess) {
		this.moduleConfigurationAccess = moduleConfigurationAccess;
	}

	public SapCreditCheckBOFactory getSapCreditCheckBOFactory() {
		return sapCreditCheckBOFactory;
	}

	@Required
	public void setSapCreditCheckBOFactory(
			SapCreditCheckBOFactory sapCreditCheckBOFactory) {
		this.sapCreditCheckBOFactory = sapCreditCheckBOFactory;
	}

	@Override
	public boolean checkCreditLimitExceeded(AbstractOrderData orderData) {
		
		// Call ERP to check the credit limit of the customer
		if (isCreditCheckActive()) {
			return getSapCreditCheckBOFactory().getSapCreditCheckBO().checkCreditLimitExceeded(orderData, getCurrentSapCustomerId());
			
		}
		
		return false;
		
	}

	@Override
	public boolean checkOrderCreditBlocked(String orderCode) {
			// Call ERP to check the status of the order
		if (isCreditCheckActive()) {
			return getSapCreditCheckBOFactory().getSapCreditCheckBO().checkOrderCreditBlocked(orderCode);
			
		}
		
		return false;
	}
	
	protected String getCurrentSapCustomerId()
	{
		final B2BUnitModel root = determineB2BUnitOfCurrentB2BCustomer();

		if (root != null)
		{
			return root.getUid();
		}
		
		String customer = b2bCustomerService.getCurrentB2BCustomer() !=  null ? b2bCustomerService.getCurrentB2BCustomer().getUid():"Missing customer Id!";
		sapLogger.warn(String.format("The current B2B customer %s is not assigned to a parent B2B unit!", customer ));
		return "";

	}

	/**
	 * @return the root B2B unit of the current B2B customer
	 */
	protected B2BUnitModel determineB2BUnitOfCurrentB2BCustomer()
	{
		final B2BCustomerModel b2bCustomer = b2bCustomerService.getCurrentB2BCustomer();
		final B2BUnitModel parent = b2bUnitService.getParent(b2bCustomer);
		final B2BUnitModel root = b2bUnitService.getRootUnit(parent);
		return root;

	}
	
	/**
	 * @return true if SAP credit check is active
	 */
	protected boolean isCreditCheckActive()
	{	
		return (getBaseStoreService().getCurrentBaseStore().getSAPConfiguration() != null)
				&& (getBaseStoreService().getCurrentBaseStore().getSAPConfiguration().isSapcreditcheckactive());
	}


}

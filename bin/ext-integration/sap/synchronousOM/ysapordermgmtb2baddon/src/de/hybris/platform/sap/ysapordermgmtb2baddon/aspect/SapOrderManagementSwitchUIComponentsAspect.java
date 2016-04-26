/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package de.hybris.platform.sap.ysapordermgmtb2baddon.aspect;

import de.hybris.platform.store.services.BaseStoreService;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Required;


/**
 *
 * This aspect is used to apply the UI changes when switching between synchronous order management SOM and asynchronous
 * order management AOM.
 *
 */
public class SapOrderManagementSwitchUIComponentsAspect
{

	public static final Logger LOG = Logger.getLogger(SapOrderManagementSwitchUIComponentsAspect.class);
	public static final String SOM_ADDON_PREFIX = "addon:/ysapordermgmtb2baddon/";
	public static final String REDIRECT_PREFIX = "redirect:";
	public static final String FORWARD_PREFIX = "forward:";
	public static final String ADDON_PREFIX = "addon:";

	private BaseStoreService baseStoreService;


	/**
	 * @return the baseStoreService
	 */
	public BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}


	/**
	 * @param baseStoreService
	 *           the baseStoreService to set
	 */
	@Required
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}

	/**
	 * Apply the UI changes when switching between synchronous order management SOM and asynchronous order management
	 * AOM. For the SOM scenario add the add-on prefix to the UI component.
	 *
	 * @param pjp
	 * @return the UI component name
	 * @throws Throwable
	 */
	public Object applyUIChanges(final ProceedingJoinPoint pjp) throws Throwable
	{

		String uiComponent = pjp.proceed().toString();

		if (isUIChangeRequired(uiComponent))
		{
			final StringBuilder prefix = new StringBuilder(SOM_ADDON_PREFIX);
			prefix.append(uiComponent);
			uiComponent = prefix.toString();

			logInfoMessage(pjp.getSignature().toString(), uiComponent, true);
		}
		else
		{
			logInfoMessage(pjp.getSignature().toString(), uiComponent, false);
		}

		return uiComponent;

	}

	/**
	 * Switch order page
	 *
	 * @param pjp
	 * @return UI Component
	 * @throws Throwable
	 */
	public Object switchOrderPage(final ProceedingJoinPoint pjp) throws Throwable
	{
		final String uiComponent = applyUIChanges(pjp).toString();
		return isUIChangeRequired(uiComponent) ? uiComponent.replace("accountLayoutPage", "accountOrderLayoutPage") : uiComponent;
	}

	/**
	 * Switch order history page
	 *
	 * @param pjp
	 * @return UI Component
	 * @throws Throwable
	 */
	public Object switchOrdersPage(final ProceedingJoinPoint pjp) throws Throwable
	{
		final String uiComponent = applyUIChanges(pjp).toString();
		return isUIChangeRequired(uiComponent) ? uiComponent.replace("accountLayoutPage", "accountOrderHistoryLayoutPage")
				: uiComponent;
	}

	/**
	 * Switch the UI component definition from the b2bacceleratoraddon add-on to the ysapordermgmtb2baddon add-on
	 *
	 * @param pjp
	 * @return the UI component name
	 * @throws Throwable
	 */
	public Object switchAddonUIComponent(final ProceedingJoinPoint pjp) throws Throwable
	{

		String uiComponent = pjp.proceed().toString();

		if (isUIChangeRequired(uiComponent) && uiComponent.contains(ADDON_PREFIX))
		{
			uiComponent = uiComponent.replace("b2bacceleratoraddon", "ysapordermgmtb2baddon");

			logInfoMessage(pjp.getSignature().toString(), uiComponent, true);
		}
		else
		{
			logInfoMessage(pjp.getSignature().toString(), uiComponent, false);
		}

		return uiComponent;

	}


	/**
	 * Log an information message
	 *
	 * @param methodSignature
	 * @param uiComponent
	 * @param somEnabled
	 */
	private void logInfoMessage(final String methodSignature, final String uiComponent, final boolean somEnabled)
	{
		if (LOG.isInfoEnabled())
		{
			if (somEnabled)
			{
				LOG.info("The synchronous order scenario SOM is active and the intercepted method is [" + methodSignature
						+ "]. The synchronous order UI component is [" + uiComponent + "]");
			}
			else
			{
				LOG.info("The asynchronous order scenario AOM is active and the intercepted method is [" + methodSignature
						+ "]. The asynchronous order UI component is [" + uiComponent + "]");
			}
		}
	}

	/**
	 * Check if synchronous order management SOM is active
	 *
	 * @return true is SOM is active
	 */
	protected boolean isSapOrderMgmtEnabled()
	{
		return getBaseStoreService().getCurrentBaseStore().getSAPConfiguration() != null
				&& getBaseStoreService().getCurrentBaseStore().getSAPConfiguration().isSapordermgmt_enabled();
	}

	/**
	 * @param uiComponent
	 * @return true if the UI component switch is required
	 */
	protected boolean isUIChangeRequired(final String uiComponent)
	{
		return isSapOrderMgmtEnabled() && !uiComponent.contains(REDIRECT_PREFIX) && !uiComponent.contains(FORWARD_PREFIX);

	}

}

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
package de.hybris.platform.integration.cis.subscription.cscockpit.widgets.controllers.impl;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.services.values.ValueHandlerException;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.widgets.controllers.impl.DefaultCustomerCreateController;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.subscriptionfacades.SubscriptionFacade;
import de.hybris.platform.subscriptionfacades.exceptions.SubscriptionFacadeException;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zul.Messagebox;


/**
 * Default controller implementation for creating new subscription customers in the hybris customer service cockpit. In
 * addition to a hybris account an account at a third party subscription billing provider is created via the
 * Subscription Billing Gateway.
 */
public class DefaultSubscriptionCustomerCreateController extends DefaultCustomerCreateController
{
	protected static final Logger LOG = Logger.getLogger(DefaultSubscriptionCustomerCreateController.class);

	private SubscriptionFacade subscriptionFacade;
	private SessionService sessionService;

	@Override
	public TypedObject createNewCustomer(final ObjectValueContainer customerObjectValueContainer, final String customerTypeCode)
			throws DuplicateUidException, ValueHandlerException
	{
		final TypedObject customerObject = super.createNewCustomer(customerObjectValueContainer, customerTypeCode);

		if (customerObject != null && customerObject.getObject() instanceof CustomerModel)
		{
			getSessionService().setAttribute("newCsCustomer", customerObject);

			try
			{
				getSubscriptionFacade().updateProfile(new HashMap<String, String>());
			}
			catch (final SubscriptionFacadeException e)
			{
				LOG.error("Creating a new subscription billing profile failed", e);
				try
				{
					Messagebox.show(e.getMessage(),
							LabelUtils.getLabel("cscockpit.widget.customer.create", "unableToCreateBillingProfile", new Object[0]), 1,
							"z-msgbox z-msgbox-error");
				}
				catch (final InterruptedException ie)
				{
					LOG.error("Failed to display error message box", ie);
				}
			}
			finally
			{
				getSessionService().removeAttribute("newCsCustomer");
			}
		}

		return customerObject;
	}

	protected SubscriptionFacade getSubscriptionFacade()
	{
		return subscriptionFacade;
	}

	@Required
	public void setSubscriptionFacade(final SubscriptionFacade subscriptionFacade)
	{
		this.subscriptionFacade = subscriptionFacade;
	}

	public SessionService getSessionService()
	{
		return sessionService;
	}

	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}
}

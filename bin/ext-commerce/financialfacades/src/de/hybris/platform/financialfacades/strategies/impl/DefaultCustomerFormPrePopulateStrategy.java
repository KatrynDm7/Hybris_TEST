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
package de.hybris.platform.financialfacades.strategies.impl;

import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.insurance.data.CustomerFormSessionData;
import de.hybris.platform.commercefacades.insurance.data.FormSessionData;
import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.financialfacades.constants.FinancialfacadesConstants;
import de.hybris.platform.financialfacades.strategies.CustomerFormPrePopulateStrategy;
import de.hybris.platform.servicelayer.session.Session;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.xyformsfacades.data.YFormDataData;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;


/**
 * The class of DefaultCustomerFormPrePopulateStrategy.
 */
public class DefaultCustomerFormPrePopulateStrategy implements CustomerFormPrePopulateStrategy
{
	private static final Logger LOG = Logger.getLogger(DefaultCustomerFormPrePopulateStrategy.class);

	private SessionService sessionService;

	private CustomerFacade customerFacade;

	private CartFacade cartFacade;

	@Override
	public void storeCustomerFormData()
	{
		if (hasCustomerFormDataStored())
		{
			return;
		}

		final Session session = getSessionService().getCurrentSession();

		final CustomerFormSessionData customerFormSessionData = new CustomerFormSessionData();

		final CustomerData currentCustomer = getCustomerFacade().getCurrentCustomer();
		if (currentCustomer == null || currentCustomer.getUid() == null)
		{
			return;
		}

		final CartData cartData = getCartFacade().getSessionCart();
		if (cartData == null)
		{
			return;
		}

		customerFormSessionData.setCustomerUid(currentCustomer.getUid());
		final List<FormSessionData> formSessionDataList = Lists.newArrayList();

		for (final OrderEntryData orderEntryData : cartData.getEntries())
		{
			if (CollectionUtils.isNotEmpty(orderEntryData.getFormDataData()))
			{
				for (final YFormDataData yFormDataData : orderEntryData.getFormDataData())
				{
					final FormSessionData formSessionData = new FormSessionData();
					formSessionData.setYFormDataId(yFormDataData.getId());
					formSessionData.setYFormDefinition(yFormDataData.getFormDefinition());
					formSessionData.setYFormDataRefId(yFormDataData.getRefId());
					formSessionDataList.add(formSessionData);
				}
			}
		}

		customerFormSessionData.setFormSessionData(normalizeFormSessionData(formSessionDataList));

		if (CollectionUtils.isNotEmpty(customerFormSessionData.getFormSessionData()))
		{
			session.setAttribute(FinancialfacadesConstants.INSURANCE_STORED_CUSTOMER_FORM, customerFormSessionData);
		}
	}

	protected List<FormSessionData> normalizeFormSessionData(final List<FormSessionData> source)
	{
		final List<FormSessionData> res = Lists.newArrayList();
		final Set<String> formIdSet = Sets.newHashSet();
		for (final FormSessionData formSessionData : source)
		{
			if (!formIdSet.contains(formSessionData.getYFormDataId()))
			{
				res.add(formSessionData);
				formIdSet.add(formSessionData.getYFormDataId());
			}
		}

		return res;
	}

	@Override
	public <T extends Serializable> T getCustomerFormData()
	{
		if (hasCustomerFormDataStored())
		{
			final Object obj = getSessionService().getCurrentSession().getAttribute(
					FinancialfacadesConstants.INSURANCE_STORED_CUSTOMER_FORM);

			CustomerFormSessionData customerFormSessionData = null;
			if (obj instanceof Serializable && obj instanceof CustomerFormSessionData)
			{
				customerFormSessionData = (CustomerFormSessionData) obj;
			}

			if (customerFormSessionData == null)
			{
				return null;
			}

			final CustomerData currentCustomer = getCustomerFacade().getCurrentCustomer();
			if (currentCustomer == null || currentCustomer.getUid() == null)
			{
				removeStoredCustomerFormData();
				return null;
			}

			if (currentCustomer.getUid().equals(customerFormSessionData.getCustomerUid()))
			{
				return getSessionService().getCurrentSession().getAttribute(FinancialfacadesConstants.INSURANCE_STORED_CUSTOMER_FORM);
			}
			else
			{
				removeStoredCustomerFormData();
				return null;
			}
		}

		return null;
	}

	@Override
	public void removeStoredCustomerFormData()
	{
		if (hasCustomerFormDataStored())
		{
			getSessionService().getCurrentSession().removeAttribute(FinancialfacadesConstants.INSURANCE_STORED_CUSTOMER_FORM);
		}
	}

	@Override
	public boolean hasCustomerFormDataStored()
	{
		final Session session = getSessionService().getCurrentSession();
		return session != null && session.getAttribute(FinancialfacadesConstants.INSURANCE_STORED_CUSTOMER_FORM) != null;
	}

	protected SessionService getSessionService()
	{
		return sessionService;
	}

	@Required
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	protected CustomerFacade getCustomerFacade()
	{
		return customerFacade;
	}

	@Required
	public void setCustomerFacade(final CustomerFacade customerFacade)
	{
		this.customerFacade = customerFacade;
	}

	protected CartFacade getCartFacade()
	{
		return cartFacade;
	}

	@Required
	public void setCartFacade(final CartFacade cartFacade)
	{
		this.cartFacade = cartFacade;
	}
}

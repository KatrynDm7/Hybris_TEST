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
package de.hybris.platform.integration.cis.subscription.cscockpit.widgets.renderers.impl;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.cockpit.widgets.impl.DefaultListboxWidget;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.widgets.controllers.CheckoutController;
import de.hybris.platform.cscockpit.widgets.models.impl.CheckoutPaymentWidgetModel;
import de.hybris.platform.subscriptioncockpits.cscockpit.widgets.renderers.impl.SubscriptionCheckoutPaymentWidgetRenderer;
import de.hybris.platform.subscriptionfacades.SubscriptionFacade;
import de.hybris.platform.subscriptionfacades.data.SubscriptionPaymentData;
import de.hybris.platform.util.Config;

import java.math.BigDecimal;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Button;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;


/**
 * CIS specific extension of {@link SubscriptionCheckoutPaymentWidgetRenderer}
 */
public class CisSubscriptionCheckoutPaymentWidgetRenderer extends SubscriptionCheckoutPaymentWidgetRenderer
{
	private static final Logger LOG = Logger.getLogger(CisSubscriptionCheckoutPaymentWidgetRenderer.class);
	private SubscriptionFacade subscriptionFacade;

	@Override
	protected void handleOpenNewPaymentOptionClickEvent(
			final DefaultListboxWidget<CheckoutPaymentWidgetModel, CheckoutController> widget, final Event event, final Div container)
			throws Exception
	{
		final SubscriptionPaymentData initResult = getSubscriptionFacade().initializeTransaction(getClientIpAddress(),
				getPaymentFormReturnUrl(), getPaymentFormErrorUrl(), new HashMap<String, String>());
		final String sessionToken = initResult.getParameters().get("sessionTransactionToken");

		if (sessionToken == null)
		{
			try
			{
				Messagebox
						.show(LabelUtils.getLabel(widget, "noSessionToken", new Object[0]),
								LabelUtils.getLabel(widget, "unableToInitTransaction", new Object[0]), Messagebox.OK,
								"z-msgbox z-msgbox-error");
			}
			catch (final InterruptedException ie)
			{
				LOG.error("Failed to display error message box", ie);
			}
			return;
		}
		UISessionUtils.getCurrentSession().setSessionAttribute("authorizationRequestId", getClientIpAddress());
		UISessionUtils.getCurrentSession().setSessionAttribute("authorizationRequestToken", sessionToken);
		UISessionUtils.getCurrentSession().setSessionAttribute("widget", widget);

		final PK billingAddressPk = getSelectedPaymentAddress(widget) == null ? PK.NULL_PK : getSelectedPaymentAddress(widget)
				.getPk();
		Executions.sendRedirect(getSecureServerUrl() + "/cissubscription/addPaymentMethod?sessionToken=" + sessionToken
				+ "&billingAddress=" + billingAddressPk + "&lang=" + getLanguageIso());
	}

	@Override
	protected void populateMasterRow(final DefaultListboxWidget<CheckoutPaymentWidgetModel, CheckoutController> widget,
			final Listitem row, final Object context, final TypedObject item)
	{
		super.populateDataRow(widget, row, getColumnConfigurations(), item);

		final Listcell amountCell = new Listcell();
		row.appendChild(amountCell);
		final Div amountContainer = new Div();
		amountContainer.setParent(amountCell);
		final Decimalbox amountInput = new Decimalbox();
		amountInput.setScale(2);
		amountInput.setValue(BigDecimal.valueOf(widget.getWidgetController().getSuggestedAmountForPaymentOption()));
		amountInput.setParent(amountContainer);
		amountInput.setWidth("75px");

		// set an arbitrary cv2 code
		final Textbox cv2Input = new Textbox();
		cv2Input.setValue("000");

		final Listcell actionCell = new Listcell();
		row.appendChild(actionCell);
		final Div actionContainer = new Div();
		actionContainer.setParent(actionCell);
		final Button payBtn = new Button(LabelUtils.getLabel(widget, "payBtn", new Object[0]));
		payBtn.setParent(actionContainer);
		if (UISessionUtils.getCurrentSession().isUsingTestIDs())
		{
			UITools.applyTestID(amountInput, "Checkout_Payment_StoredCardAmmount_input");
			UITools.applyTestID(payBtn, "Checkout_Payment_UseStoredCard_button");
		}
		payBtn.addEventListener("onClick", new PayEventListener(widget, item, amountInput, cv2Input));
	}

	@Override
	protected Object populateHeaderRow(final DefaultListboxWidget<CheckoutPaymentWidgetModel, CheckoutController> widget,
			final Listhead row)
	{
		super.populateHeaderRow(widget, row, getColumnConfigurations());
		// create all additional columns except for the cv2 column
		final Listheader amountHeader = new Listheader(LabelUtils.getLabel(widget, "amountHeader", new Object[0]));
		amountHeader.setWidth("90px");
		row.appendChild(amountHeader);
		final Listheader actionHeader = new Listheader(LabelUtils.getLabel(widget, "actionHeader", new Object[0]));
		actionHeader.setWidth("100px");
		row.appendChild(actionHeader);
		return null;
	}


	/**
	 * Returns the ISO code of the current session language.
	 * 
	 * @return {@link String} session language ISO code
	 */
	protected String getLanguageIso()
	{
		return UISessionUtils.getCurrentSession().getLanguageIso();
	}

	/**
	 * Returns the selected payment address.
	 * 
	 * @param widget
	 *           the widget
	 * @return the selected payment address
	 */
	protected AddressModel getSelectedPaymentAddress(
			final DefaultListboxWidget<CheckoutPaymentWidgetModel, CheckoutController> widget)
	{
		final TypedObject cart = widget.getWidgetController().getBasketController().getCart();
		if (cart != null && cart.getObject() instanceof CartModel)
		{
			return ((CartModel) cart.getObject()).getPaymentAddress();
		}
		throw new IllegalStateException("Unable to retrieve cart");
	}

	/**
	 * Returns the hybris server URL
	 * 
	 * @return {@link String} hybris server URL
	 */
	protected String getServerUrl()
	{
		final Execution exec = Executions.getCurrent();
		return exec.getScheme() + "://" + exec.getServerName() + ":" + exec.getServerPort();
	}

	/**
	 * Returns the secure hybris server URL using SSL
	 * 
	 * @return {@link String} secure hybris server URL
	 */
	protected String getSecureServerUrl()
	{
		if (Config.getParameter("tomcat.ssl.port") != null)
		{
			final Execution exec = Executions.getCurrent();
			return "https://" + exec.getServerName() + ":" + Config.getParameter("tomcat.ssl.port");
		}
		throw new IllegalStateException("Unable to determine SSL port");
	}

	/**
	 * Returns the redirect URL to use in case of errors for PCI compliant payment form post.
	 * 
	 * @return {@link String} error redirect URL
	 */
	protected String getPaymentFormErrorUrl()
	{
		return getBaseReturnUrl() + "&sop-error=true";
	}

	/**
	 * Returns the redirect URL for PCI compliant payment form post.
	 * 
	 * @return {@link String} redirect URL
	 */
	protected String getPaymentFormReturnUrl()
	{
		return getBaseReturnUrl() + "&sop-error=false";
	}

	private String getBaseReturnUrl()
	{
		return getServerUrl() + Executions.getCurrent().getContextPath() + "/index.zul?events=sop";
	}

	/**
	 * Returns the client IP address
	 * 
	 * @return the client IP address
	 */
	protected String getClientIpAddress()
	{
		return Executions.getCurrent().getLocalAddr();
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
}

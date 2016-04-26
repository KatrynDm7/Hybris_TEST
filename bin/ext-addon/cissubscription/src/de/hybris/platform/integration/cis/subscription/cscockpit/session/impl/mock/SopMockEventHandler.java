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
package de.hybris.platform.integration.cis.subscription.cscockpit.session.impl.mock;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.UICockpitPerspective;
import de.hybris.platform.cockpit.session.impl.AbstractRequestEventHandler;
import de.hybris.platform.cockpit.widgets.impl.DefaultListboxWidget;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commerceservices.customer.CustomerEmailResolutionService;
import de.hybris.platform.commerceservices.order.CommerceCardTypeService;
import de.hybris.platform.commerceservices.strategies.CustomerNameStrategy;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.cscockpit.services.checkout.CsCheckoutService;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.widgets.controllers.BasketController;
import de.hybris.platform.cscockpit.widgets.controllers.CallContextController;
import de.hybris.platform.cscockpit.widgets.controllers.CheckoutController;
import de.hybris.platform.cscockpit.widgets.models.impl.CheckoutPaymentWidgetModel;
import de.hybris.platform.payment.dto.CardType;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.subscriptionfacades.SubscriptionFacade;
import de.hybris.platform.subscriptionfacades.data.SubscriptionPaymentData;
import de.hybris.platform.subscriptionfacades.exceptions.SubscriptionFacadeException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Messagebox;


/**
 * Handler for events of type "sop-mock". Mocks the payment details form handling of a third party billing provider. Do
 * not use in production!
 */
public class SopMockEventHandler extends AbstractRequestEventHandler
{
	private static final Logger LOG = Logger.getLogger(SopMockEventHandler.class);
	private static final String ERROR_MSG = "Unable to create Payment method";
	private static final String ERROR_KEY = "error";

	private SubscriptionFacade subscriptionFacade;
	private BasketController basketController;
	private CallContextController callContextController;
	private CsCheckoutService csCheckoutService;
	private CommerceCardTypeService commerceCardTypeService;
	private SessionService sessionService;
	private CommonI18NService commonI18NService;
	private I18NService i18NService;
	private ModelService modelService;
	private CustomerEmailResolutionService customerEmailResolutionService;
	private CustomerNameStrategy customerNameStrategy;
	private FlexibleSearchService flexibleSearchService;
	private Converter<CCPaymentInfoData, CreditCardPaymentInfoModel> cardPaymentInfoReverseConverter;

	@Override
	public void handleEvent(final UICockpitPerspective perspective, final Map<String, String[]> paramMap)
	{
		final boolean sopError = Boolean.parseBoolean(getParameter(paramMap, ERROR_KEY));

		final String sessionTransactionToken = getSessionService().getAttribute("authorizationRequestToken");
		final String sessionTransactionId = getSessionService().getAttribute("authorizationRequestId");
		final DefaultListboxWidget<CheckoutPaymentWidgetModel, CheckoutController> widget = getSessionService().getAttribute(
				"widget");

		if (sessionTransactionToken == null || sessionTransactionId == null || widget == null)
		{
			LOG.error("Session attributes authorizationRequestToken, authorizationRequestId and widget may not be null");
		}
		else if (sopError)
		{
			LOG.error(ERROR_MSG);
			showErrorMessagebox(widget, ERROR_MSG, "unableToCreatePaymentMethod");
		}
		else
		{
			getSessionService().removeAttribute("authorizationRequestToken");
			getSessionService().removeAttribute("authorizationRequestId");
			getSessionService().removeAttribute("widget");
			getSessionService().removeAttribute("amount");

			SubscriptionPaymentData finalizeResult;
			try
			{
				finalizeResult = getSubscriptionFacade().finalizeTransaction(sessionTransactionId, sessionTransactionToken,
						getParameters(paramMap));
				persistPaymentMethod(finalizeResult.getParameters());
			}
			catch (final SubscriptionFacadeException e)
			{
				LOG.error(ERROR_MSG, e);
				showErrorMessagebox(widget, e.getMessage(), "unableToCreatePaymentMethod");
			}
			finally
			{
				widget.getWidgetModel().notifyListeners();
				widget.getWidgetController().dispatchEvent(null, widget, null);
				widget.getWidgetController().getBasketController().dispatchEvent(null, widget, null);
			}
		}
		Executions.sendRedirect("/");
	}


	private Map<String, String> getParameters(final Map<String, String[]> paramMap)
	{
		final Map<String, String> map = new HashMap<>();

		// Mask the card number
		String maskedCardNumber = "************";
		final String cardNumber = getRawParam(paramMap, "cardNumber");
		if (cardNumber.length() >= 4)
		{
			final String endPortion = cardNumber.trim().substring(cardNumber.length() - 4);
			maskedCardNumber = maskedCardNumber + endPortion;
		}

		map.put("cardNumber", maskedCardNumber);
		map.put("cardType", getRawParam(paramMap, "cardTypeCode"));
		map.put("expiryMonth", getRawParam(paramMap, "expiryMonth"));
		map.put("expiryYear", getRawParam(paramMap, "expiryYear"));
		map.put("issueNumber", getRawParam(paramMap, "issueNumber"));
		map.put("nameOnCard", getRawParam(paramMap, "nameOnCard"));
		map.put("startMonth", getRawParam(paramMap, "startMonth"));
		map.put("startYear", getRawParam(paramMap, "startYear"));

		map.put("billingAddress_countryIso", getRawParam(paramMap, "billingAddress.countryIso"));
		map.put("billingAddress_firstName", getRawParam(paramMap, "billingAddress.firstName"));
		map.put("billingAddress_lastName", getRawParam(paramMap, "billingAddress.lastName"));
		map.put("billingAddress_titleCode", getRawParam(paramMap, "billingAddress.titleCode"));
		map.put("billingAddress_line1", getRawParam(paramMap, "billingAddress.line1"));
		map.put("billingAddress_line2", getRawParam(paramMap, "billingAddress.line2"));
		map.put("billingAddress_postcode", getRawParam(paramMap, "billingAddress.postcode"));
		map.put("billingAddress_townCity", getRawParam(paramMap, "billingAddress.townCity"));

		return map;
	}


	private String getRawParam(final Map<String, String[]> paramMap, final String key)
	{
		return paramMap.get(key) == null || paramMap.get(key).length < 1 ? StringUtils.EMPTY : paramMap.get(key)[0];
	}


	private void showErrorMessagebox(final DefaultListboxWidget<CheckoutPaymentWidgetModel, CheckoutController> widget,
			final String message, final String labelProperty)
	{
		try
		{
			Messagebox.show(message, LabelUtils.getLabel(widget, labelProperty, new Object[0]), Messagebox.OK,
					"z-msgbox z-msgbox-error");
		}
		catch (final InterruptedException ie)
		{
			LOG.error("Failed to display error message box", ie);
		}
	}

	/**
	 * Persist a new credit card payment information based on the result of the finalize transaction call to the
	 * subscription billing provider.
	 *
	 * @param resultParams
	 *           the results of the finalize call
	 */
	private void persistPaymentMethod(final Map<String, String> resultParams)
	{
		Assert.notNull(getCallContextController().getCurrentCustomer(), "Current customer may not be null");

		final CustomerModel customerModel = (CustomerModel) getCallContextController().getCurrentCustomer().getObject();

		Assert.notNull(customerModel, "Customer may not be null");
		Assert.notNull(resultParams, "CardInfo may not be null");
		Assert.notNull(resultParams.get("merchantPaymentMethodId"), "Result parameter 'merchantPaymentMethodId' may not be null");

		final AddressModel billingAddress = createBillingAddress(resultParams);

		final CurrencyModel currencyModel = customerModel.getSessionCurrency() == null ? getCommonI18NService()
				.getCurrentCurrency() : customerModel.getSessionCurrency();
		ServicesUtil.validateParameterNotNull(currencyModel, "Customer session currency cannot be null");

		final CreditCardPaymentInfoModel cardPaymentInfoModel = createCreditCardPaymentInfo(resultParams, customerModel);

		billingAddress.setOwner(cardPaymentInfoModel);
		cardPaymentInfoModel.setBillingAddress(billingAddress);

		getModelService().saveAll(new Object[]
		{ billingAddress, cardPaymentInfoModel });
		getModelService().refresh(customerModel);

		final List<PaymentInfoModel> paymentInfoModels = new ArrayList<PaymentInfoModel>(customerModel.getPaymentInfos());
		if (!paymentInfoModels.contains(cardPaymentInfoModel))
		{
			paymentInfoModels.add(cardPaymentInfoModel);
			customerModel.setPaymentInfos(paymentInfoModels);

			getModelService().saveAll(new Object[]
			{ customerModel, cardPaymentInfoModel });
			getModelService().refresh(customerModel);
		}
	}

	protected CreditCardPaymentInfoModel createCreditCardPaymentInfo(final Map<String, String> resultParams,
			final CustomerModel customerModel)
	{
		final CreditCardPaymentInfoModel cardPaymentInfoModel = (CreditCardPaymentInfoModel) getModelService().create(
				CreditCardPaymentInfoModel.class);
		cardPaymentInfoModel.setCode(customerModel.getUid() + "_" + UUID.randomUUID());
		cardPaymentInfoModel.setUser(customerModel);
		cardPaymentInfoModel.setSubscriptionId(resultParams.get("merchantPaymentMethodId"));
		cardPaymentInfoModel.setNumber(getMaskedCardNumber(resultParams.get("cardNumber")));
		cardPaymentInfoModel.setCcOwner(resultParams.get("accountHolderName"));

		// card type
		final CardType cardType = getCommerceCardTypeService().getCardTypeForCode(resultParams.get("cardType"));
		if (cardType != null)
		{
			cardPaymentInfoModel.setType(cardType.getCode());
		}

		// expiration date
		final String expirationDate = resultParams.get("expiryDate");
		if (StringUtils.isNotEmpty(expirationDate))
		{
			// assuming date of format MM/yyyy
			cardPaymentInfoModel.setValidToMonth(expirationDate.substring(0, 2));
			cardPaymentInfoModel.setValidToYear(expirationDate.substring(3, 7));
		}

		// issue date + number
		final String issueDate = resultParams.get("startDate");
		if (StringUtils.isNotEmpty(issueDate))
		{
			// assuming date of format MM/yyyy
			cardPaymentInfoModel.setValidFromMonth(issueDate.substring(0, 2));
			cardPaymentInfoModel.setValidFromYear(issueDate.substring(3, 7));
		}
		if (!(StringUtils.isEmpty(resultParams.get("issueNumber"))))
		{
			cardPaymentInfoModel.setIssueNumber(Integer.valueOf(resultParams.get("issueNumber")));
		}

		cardPaymentInfoModel.setSaved(true);
		return cardPaymentInfoModel;
	}

	protected AddressModel createBillingAddress(final Map<String, String> resultParams)
	{
		final AddressModel billingAddress = (AddressModel) getModelService().create(AddressModel.class);
		billingAddress.setFirstname(resultParams.get("firstName"));
		billingAddress.setLastname(resultParams.get("lastName"));
		billingAddress.setLine1(resultParams.get("addr1"));
		billingAddress.setLine2(resultParams.get("addr2"));
		billingAddress.setTown(resultParams.get("city"));
		billingAddress.setPostalcode(resultParams.get("postalCode"));
		billingAddress.setCountry(getCommonI18NService().getCountry(resultParams.get("country")));
		billingAddress.setEmail(resultParams.get("emailAddress"));
		if (StringUtils.isNotEmpty(resultParams.get("titleCode")))
		{
			final TitleModel title = new TitleModel();
			title.setCode(resultParams.get("titleCode"));
			billingAddress.setTitle(getFlexibleSearchService().getModelByExample(title));
		}
		return billingAddress;
	}

	protected CartModel getCartModel()
	{
		final TypedObject cart = getBasketController().getCart();
		if ((cart != null) && (cart.getObject() instanceof CartModel))
		{
			return ((CartModel) cart.getObject());
		}
		throw new IllegalStateException("No session cart available");
	}

	protected String getMaskedCardNumber(final String cardNumber)
	{
		Assert.notNull(cardNumber, "Method param cardNumber may not be null");
		if (cardNumber.trim().length() > 4)
		{
			final String endPortion = cardNumber.trim().substring(cardNumber.length() - 4);
			return "************" + endPortion;
		}
		throw new IllegalArgumentException("Given card number is too short");
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

	protected CallContextController getCallContextController()
	{
		return callContextController;
	}

	@Required
	public void setCallContextController(final CallContextController callContextController)
	{
		this.callContextController = callContextController;
	}

	protected Converter<CCPaymentInfoData, CreditCardPaymentInfoModel> getCardPaymentInfoReverseConverter()
	{
		return cardPaymentInfoReverseConverter;
	}

	@Required
	public void setCardPaymentInfoReverseConverter(
			final Converter<CCPaymentInfoData, CreditCardPaymentInfoModel> cardPaymentInfoReverseConverter)
	{
		this.cardPaymentInfoReverseConverter = cardPaymentInfoReverseConverter;
	}

	protected BasketController getBasketController()
	{
		return basketController;
	}

	@Required
	public void setBasketController(final BasketController basketController)
	{
		this.basketController = basketController;
	}

	protected CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	@Required
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}

	protected I18NService getI18NService()
	{
		return i18NService;
	}

	@Required
	public void setI18NService(final I18NService i18NService)
	{
		this.i18NService = i18NService;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	protected CustomerEmailResolutionService getCustomerEmailResolutionService()
	{
		return customerEmailResolutionService;
	}

	@Required
	public void setCustomerEmailResolutionService(final CustomerEmailResolutionService customerEmailResolutionService)
	{
		this.customerEmailResolutionService = customerEmailResolutionService;
	}

	protected FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}

	@Required
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
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

	protected CustomerNameStrategy getCustomerNameStrategy()
	{
		return customerNameStrategy;
	}

	@Required
	public void setCustomerNameStrategy(final CustomerNameStrategy customerNameStrategy)
	{
		this.customerNameStrategy = customerNameStrategy;
	}

	protected CommerceCardTypeService getCommerceCardTypeService()
	{
		return commerceCardTypeService;
	}

	@Required
	public void setCommerceCardTypeService(final CommerceCardTypeService commerceCardTypeService)
	{
		this.commerceCardTypeService = commerceCardTypeService;
	}

	protected CsCheckoutService getCsCheckoutService()
	{
		return csCheckoutService;
	}

	@Required
	public void setCsCheckoutService(final CsCheckoutService csCheckoutService)
	{
		this.csCheckoutService = csCheckoutService;
	}

}

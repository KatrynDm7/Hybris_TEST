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
package de.hybris.platform.b2bacceleratorfacades.order.impl;

import static de.hybris.platform.util.localization.Localization.getLocalizedString;

import de.hybris.platform.b2b.model.B2BCommentModel;
import de.hybris.platform.b2b.model.B2BCostCenterModel;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BPermissionResultModel;
import de.hybris.platform.b2b.services.B2BCartService;
import de.hybris.platform.b2b.services.B2BCommentService;
import de.hybris.platform.b2b.services.B2BCostCenterService;
import de.hybris.platform.b2b.services.B2BOrderService;
import de.hybris.platform.b2bacceleratorfacades.api.cart.CheckoutFacade;
import de.hybris.platform.b2bacceleratorfacades.checkout.data.PlaceOrderData;
import de.hybris.platform.b2bacceleratorfacades.exception.EntityValidationException;
import de.hybris.platform.b2bacceleratorfacades.order.data.B2BCommentData;
import de.hybris.platform.b2bacceleratorfacades.order.data.B2BCostCenterData;
import de.hybris.platform.b2bacceleratorfacades.order.data.B2BDaysOfWeekData;
import de.hybris.platform.b2bacceleratorfacades.order.data.B2BPaymentTypeData;
import de.hybris.platform.b2bacceleratorfacades.order.data.B2BReplenishmentRecurrenceEnum;
import de.hybris.platform.b2bacceleratorfacades.order.data.ScheduledCartData;
import de.hybris.platform.b2bacceleratorfacades.order.data.TriggerData;
import de.hybris.platform.b2bacceleratorservices.enums.CheckoutPaymentType;
import de.hybris.platform.b2bacceleratorservices.event.ReplenishmentOrderPlacedEvent;
import de.hybris.platform.b2bacceleratorservices.order.B2BCommerceCartService;
import de.hybris.platform.commercefacades.order.data.AbstractOrderData;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.InvoicePaymentInfoModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.cronjob.enums.DayOfWeek;
import de.hybris.platform.cronjob.model.TriggerModel;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.orderscheduling.ScheduleOrderService;
import de.hybris.platform.orderscheduling.model.CartToOrderCronJobModel;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.servicelayer.cronjob.TriggerService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.internal.dao.GenericDao;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * A default implementation of a checkout facade for a b2b accelerator store.
 */
public class DefaultB2BCheckoutFacade extends DefaultCheckoutFacade implements CheckoutFacade
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(DefaultB2BCheckoutFacade.class);

	private B2BCostCenterService<B2BCostCenterModel, B2BCustomerModel> b2bCostCenterService;
	private TriggerService triggerService;
	private Converter<B2BCostCenterModel, B2BCostCenterData> b2bCostCenterConverter;

	private Converter<CheckoutPaymentType, B2BPaymentTypeData> b2bPaymentTypeDataConverter;
	private Converter<DayOfWeek, B2BDaysOfWeekData> b2bDaysOfWeekConverter;
	private B2BOrderService b2BOrderService;
	private GenericDao<AbstractOrderModel> abstractOrderGenericDao;
	private Converter<CartToOrderCronJobModel, ScheduledCartData> scheduledCartConverter;
	private Populator<TriggerData, TriggerModel> triggerPopulator;
	private ScheduleOrderService scheduleOrderService;
	private B2BCommentService<AbstractOrderModel> b2bCommentService;
	private BaseSiteService baseSiteService;
	private BaseStoreService baseStoreService;
	private EventService eventService;
	private B2BCommerceCartService commerceCartService;
	private I18NService i18NService;

	private static final String CART_CHECKOUT_COSTCENTER_PAYMENTTYPE_INVALID = "cart.costcenter.paymenttypeInvalid";
	private static final String CART_CHECKOUT_PAYMENTTYPE_INVALID = "cart.paymenttype.invalid";
	private static final String CART_CHECKOUT_DELIVERYADDRESS_INVALID = "cart.deliveryAddress.invalid";
	private static final String CART_CHECKOUT_DELIVERYMODE_INVALID = "cart.deliveryMode.invalid";
	private static final String CART_CHECKOUT_TRANSACTION_NOT_AUTHORIZED = "cart.transation.notAuthorized";
	private static final String CART_CHECKOUT_PAYMENTINFO_EMPTY = "cart.paymentInfo.empty";
	private static final String CART_CHECKOUT_NOT_CALCULATED = "cart.not.calculated";
	private static final String CART_CHECKOUT_TERM_UNCHECKED = "cart.term.unchecked";
	private static final String CART_CHECKOUT_NO_QUOTE_DESCRIPTION = "cart.no.quote.description";
	private static final String CART_CHECKOUT_REPLENISHMENT_NO_STARTDATE = "cart.replenishment.no.startdate";
	private static final String CART_CHECKOUT_REPLENISHMENT_NO_FREQUENCY = "cart.replenishment.no.frequency";

	@Override
	public CartData updateCheckoutCart(final CartData cartData)
	{
		final CartModel cartModel = getCart();
		if (cartModel == null)
		{
			return null;
		}
		// set payment type
		if (cartData.getPaymentType() != null)
		{
			final String newPaymentTypeCode = cartData.getPaymentType().getCode();

			// clear delivery address, delivery mode and payment details when changing payment type
			if (cartModel.getPaymentType() == null || !newPaymentTypeCode.equalsIgnoreCase(cartModel.getPaymentType().getCode()))
			{
				cartModel.setDeliveryAddress(null);
				cartModel.setDeliveryMode(null);
				cartModel.setPaymentInfo(null);
			}

			setPaymentTypeForCart(newPaymentTypeCode, cartModel);
			// cost center need to be be cleared if using card
			if (CheckoutPaymentType.CARD.getCode().equals(newPaymentTypeCode))
			{
				setCostCenterForCart(null, cartModel);
			}
		}

		// set cost center
		if (cartData.getCostCenter() != null)
		{
			setCostCenterForCart(cartData.getCostCenter().getCode(), cartModel);
		}

		// set purchase order number
		if (cartData.getPurchaseOrderNumber() != null)
		{
			cartModel.setPurchaseOrderNumber(cartData.getPurchaseOrderNumber());
		}

		// set delivery address
		if (cartData.getDeliveryAddress() != null)
		{
			setDeliveryAddress(cartData.getDeliveryAddress());
		}

		// set quote request description
		if (cartData.getB2BComment() != null)
		{
			final B2BCommentModel b2bComment = getModelService().create(B2BCommentModel.class);
			b2bComment.setComment(cartData.getB2BComment().getComment());
			getB2bCommentService().addComment(cartModel, b2bComment);
		}

		getModelService().save(cartModel);
		return getCheckoutCart();

	}

	protected void setPaymentTypeForCart(final String paymentType, final CartModel cartModel)
	{
		final List<CheckoutPaymentType> checkoutPaymentTypes = getEnumerationService().getEnumerationValues(
				CheckoutPaymentType._TYPECODE);
		if (!checkoutPaymentTypes.contains(CheckoutPaymentType.valueOf(paymentType)))
		{
			throw new EntityValidationException(getLocalizedString(CART_CHECKOUT_PAYMENTTYPE_INVALID));
		}

		cartModel.setPaymentType(CheckoutPaymentType.valueOf(paymentType));
		if (CheckoutPaymentType.ACCOUNT.getCode().equals(paymentType))
		{
			cartModel.setPaymentInfo(getCommerceCartService().createInvoicePaymentInfo(cartModel));
		}

		getCommerceCartService().calculateCartForPaymentTypeChange(cartModel);
	}

	protected void setCostCenterForCart(final String costCenterCode, final CartModel cartModel)
	{
		final B2BPaymentTypeData paymentType = getCheckoutCart().getPaymentType();
		if (paymentType.getCode().equals("CARD") && costCenterCode != null)
		{
			throw new EntityValidationException(getLocalizedString(CART_CHECKOUT_COSTCENTER_PAYMENTTYPE_INVALID));
		}

		B2BCostCenterModel costCenterModel = null;
		if (costCenterCode != null)
		{
			costCenterModel = getB2bCostCenterService().getCostCenterForCode(costCenterCode);
		}

		for (final AbstractOrderEntryModel abstractOrderEntry : cartModel.getEntries())
		{
			if (abstractOrderEntry.getCostCenter() != costCenterModel)
			{
				abstractOrderEntry.setCostCenter(costCenterModel);
				getModelService().save(abstractOrderEntry);
			}
		}

		// if set cost center, delivery address and mode need to be cleared
		removeDeliveryAddress();
		removeDeliveryMode();
	}

	protected void setDeliveryAddressForCart(final AddressData addressData)
	{
		boolean supportedAddress = false;
		final List<? extends AddressData> deliveryAddresses = getSupportedDeliveryAddresses(false);
		for (final AddressData deliveryAddress : deliveryAddresses)
		{
			if (deliveryAddress.getId().equals(addressData.getId()))
			{
				supportedAddress = true;
				break;
			}
		}

		if (supportedAddress)
		{
			setDeliveryAddress(addressData);
		}
		else
		{
			throw new EntityValidationException(getLocalizedString(CART_CHECKOUT_DELIVERYADDRESS_INVALID));
		}
	}

	//------------------------------------------------------------------------------------

	@Override
	public boolean authorizePayment(final String securityCode)
	{
		if (getCart() == null)
		{
			return false;
		}
		return getCart().getPaymentInfo() instanceof CreditCardPaymentInfoModel ? super.authorizePayment(securityCode) : true;
	}

	@Override
	public List<B2BPaymentTypeData> getPaymentTypes()
	{
		final List<CheckoutPaymentType> checkoutPaymentTypes = getEnumerationService().getEnumerationValues(
				CheckoutPaymentType._TYPECODE);

		return Converters.convertAll(checkoutPaymentTypes, getB2bPaymentTypeDataConverter());
	}

	@Override
	public List<B2BDaysOfWeekData> getDaysOfWeekForReplenishmentCheckoutSummary()
	{
		final List<DayOfWeek> daysOfWeek = getEnumerationService().getEnumerationValues(DayOfWeek._TYPECODE);

		return Converters.convertAll(daysOfWeek, getB2bDaysOfWeekConverter());
	}

	@Override
	public CartData setCostCenterForCart(final String costCenterCode, final String orderCode)
	{
		final List<AbstractOrderModel> orders = getAbstractOrderGenericDao().find(
				Collections.singletonMap(AbstractOrderModel.CODE, orderCode));
		final CartModel cartModel = (CartModel) (orders.iterator().hasNext() ? orders.iterator().next() : null);

		setCostCenterForCart(costCenterCode, cartModel);

		getModelService().save(cartModel);
		return getCheckoutCart();
	}

	@Override
	public <T extends AbstractOrderData> T placeOrder(final PlaceOrderData placeOrderData) throws InvalidCartException
	{
		// term must be checked
		if (!placeOrderData.getTermsCheck().equals(Boolean.TRUE))
		{
			throw new EntityValidationException(getLocalizedString(CART_CHECKOUT_TERM_UNCHECKED));
		}

		// for CARD type, transaction must be authorized before placing order
		final boolean isCardtPaymentType = CheckoutPaymentType.CARD.getCode().equals(getCart().getPaymentType().getCode());
		if (isCardtPaymentType)
		{
			final List<PaymentTransactionModel> transactions = getCart().getPaymentTransactions();
			boolean authorized = false;
			for (final PaymentTransactionModel transaction : transactions)
			{
				for (final PaymentTransactionEntryModel entry : transaction.getEntries())
				{
					if (entry.getType().equals(PaymentTransactionType.AUTHORIZATION)
							&& TransactionStatus.ACCEPTED.name().equals(entry.getTransactionStatus()))
					{
						authorized = true;
						break;
					}
				}
			}
			if (!authorized)
			{
				// FIXME - change error message
				throw new EntityValidationException(getLocalizedString(CART_CHECKOUT_TRANSACTION_NOT_AUTHORIZED));
			}
		}

		if (isValidCheckoutCart())
		{
			// validate quote negotiation
			if (placeOrderData.getNegotiateQuote() != null && placeOrderData.getNegotiateQuote().equals(Boolean.TRUE))
			{
				if (StringUtils.isBlank(placeOrderData.getQuoteRequestDescription()))
				{
					throw new EntityValidationException(getLocalizedString(CART_CHECKOUT_NO_QUOTE_DESCRIPTION));
				}
				else
				{
					final B2BCommentData b2BComment = new B2BCommentData();
					b2BComment.setComment(placeOrderData.getQuoteRequestDescription());

					final CartData cartData = new CartData();
					cartData.setB2BComment(b2BComment);

					updateCheckoutCart(cartData);
				}
			}

			// validate replenishment
			if (placeOrderData.getReplenishmentOrder() != null && placeOrderData.getReplenishmentOrder().equals(Boolean.TRUE))
			{
				if (placeOrderData.getReplenishmentStartDate() == null)
				{
					throw new EntityValidationException(getLocalizedString(CART_CHECKOUT_REPLENISHMENT_NO_STARTDATE));
				}

				if (placeOrderData.getReplenishmentRecurrence().equals(B2BReplenishmentRecurrenceEnum.WEEKLY)
						&& CollectionUtils.isEmpty(placeOrderData.getNDaysOfWeek()))
				{
					throw new EntityValidationException(getLocalizedString(CART_CHECKOUT_REPLENISHMENT_NO_FREQUENCY));
				}

				final TriggerData triggerData = new TriggerData();
				populateTriggerDataFromPlaceOrderData(placeOrderData, triggerData);

				return (T) scheduleOrder(triggerData);
			}

			return (T) super.placeOrder();
		}

		return null;
	}

	protected void populateTriggerDataFromPlaceOrderData(final PlaceOrderData placeOrderData, final TriggerData triggerData)
	{
		final Date replenishmentStartDate = placeOrderData.getReplenishmentStartDate();
		final Calendar calendar = Calendar.getInstance(i18NService.getCurrentTimeZone(), i18NService.getCurrentLocale());
		triggerData.setActivationTime((replenishmentStartDate.before(calendar.getTime()) ? calendar.getTime()
				: replenishmentStartDate));

		final B2BReplenishmentRecurrenceEnum recurrenceValue = placeOrderData.getReplenishmentRecurrence();

		if (B2BReplenishmentRecurrenceEnum.DAILY.equals(recurrenceValue))
		{
			triggerData.setDay(Integer.valueOf(placeOrderData.getNDays()));
			triggerData.setRelative(Boolean.TRUE);
		}
		else if (B2BReplenishmentRecurrenceEnum.WEEKLY.equals(recurrenceValue))
		{
			triggerData.setDaysOfWeek(placeOrderData.getNDaysOfWeek());
			triggerData.setWeekInterval(Integer.valueOf(placeOrderData.getNWeeks()));
			triggerData.setHour(Integer.valueOf(0));
			triggerData.setMinute(Integer.valueOf(0));
		}
		else if (B2BReplenishmentRecurrenceEnum.MONTHLY.equals(recurrenceValue))
		{
			triggerData.setDay(Integer.valueOf(placeOrderData.getNthDayOfMonth()));
			triggerData.setRelative(Boolean.FALSE);
		}
	}

	@Override
	protected void beforePlaceOrder(final CartModel cartModel)
	{
		super.beforePlaceOrder(cartModel);

		final boolean isQuoteOrder = !cartModel.getB2bcomments().isEmpty();
		if (isQuoteOrder)
		{
			cartModel.setStatus(OrderStatus.PENDING_QUOTE);
		}
		else
		{
			cartModel.setStatus(OrderStatus.CREATED);
		}
	}

	protected boolean isValidCheckoutCart()
	{
		final CartData cartData = getCheckoutCart();
		final boolean valid = true;

		if (!cartData.isCalculated())
		{
			throw new EntityValidationException(getLocalizedString(CART_CHECKOUT_NOT_CALCULATED));
		}

		if (cartData.getDeliveryAddress() == null)
		{
			throw new EntityValidationException(getLocalizedString(CART_CHECKOUT_DELIVERYADDRESS_INVALID));
		}

		if (cartData.getDeliveryMode() == null)
		{
			throw new EntityValidationException(getLocalizedString(CART_CHECKOUT_DELIVERYMODE_INVALID));
		}

		final boolean accountPaymentType = CheckoutPaymentType.ACCOUNT.getCode().equals(cartData.getPaymentType().getCode());
		if (!accountPaymentType && cartData.getPaymentInfo() == null)
		{
			throw new EntityValidationException(getLocalizedString(CART_CHECKOUT_PAYMENTINFO_EMPTY));
		}

		return valid;
	}

	@Override
	public ScheduledCartData scheduleOrder(final TriggerData trigger)
	{
		final CartModel cartModel = getCart();
		cartModel.setSite(getBaseSiteService().getCurrentBaseSite());
		cartModel.setStore(getBaseStoreService().getCurrentBaseStore());
		getModelService().save(cartModel);

		final AddressModel deliveryAddress = cartModel.getDeliveryAddress();
		final AddressModel paymentAddress = cartModel.getPaymentAddress();
		final PaymentInfoModel paymentInfo = cartModel.getPaymentInfo();
		final TriggerModel triggerModel = getModelService().create(TriggerModel.class);
		getTriggerPopulator().populate(trigger, triggerModel);

		// If Trigger is not relative, reset activeDate to next expected runtime
		if (BooleanUtils.isFalse(triggerModel.getRelative()))
		{
			// getNextTime(relavtiveDate) will skip the date, to avoid skipping the activation date, go back 1 day to test.
			final Calendar priorDayCalendar = Calendar.getInstance();
			priorDayCalendar.setTime(DateUtils.addDays(triggerModel.getActivationTime(), -1));

			final Date nextPotentialFire = triggerService.getNextTime(triggerModel, priorDayCalendar).getTime();

			if (!DateUtils.isSameDay(nextPotentialFire, triggerModel.getActivationTime()))
			{
				// Adjust activation time to next scheduled vis a vis the cron expression
				triggerModel.setActivationTime(nextPotentialFire);
			}
		}
		// schedule cart
		final CartToOrderCronJobModel scheduledCart = getScheduleOrderService().createOrderFromCartCronJob(cartModel,
				deliveryAddress, paymentAddress, paymentInfo, Collections.singletonList(triggerModel));

		ScheduledCartData scheduledCartData = null;
		if (scheduledCart != null)
		{
			scheduledCartData = getScheduledCartConverter().convert(scheduledCart);
			getCartService().removeSessionCart();
			getCartService().getSessionCart();
			// trigger an email.
			getEventService().publishEvent(initializeReplenishmentPlacedEvent(scheduledCart));
		}

		return scheduledCartData;
	}

	@Override
	public void createCartFromOrder(final String orderCode)
	{
		final OrderModel order = getB2BOrderService().getOrderForCode(orderCode);
		AddressModel originalDeliveryAddress = order.getDeliveryAddress();
		if (originalDeliveryAddress != null)
		{
			originalDeliveryAddress = originalDeliveryAddress.getOriginal();
		}

		AddressModel originalPaymentAddress = order.getPaymentAddress();
		if (originalPaymentAddress != null)
		{
			originalPaymentAddress = originalPaymentAddress.getOriginal();
		}

		final PaymentInfoModel paymentInfoModel = getPaymentInfoModelForClonedCart(order);

		// detach the order and null the attribute that is not available on the cart to avoid cloning errors.
		getModelService().detach(order);
		order.setSchedulingCronJob(null);
		order.setOriginalVersion(null);
		order.setStatus(OrderStatus.CREATED);
		order.setPaymentAddress(null);
		order.setDeliveryAddress(null);
		order.setHistoryEntries(null);
		order.setPaymentInfo(null);
		order.setB2bcomments(Collections.<B2BCommentModel> emptyList());
		order.setWorkflow(null);
		order.setPermissionResults(Collections.<B2BPermissionResultModel> emptyList());
		order.setExhaustedApprovers(Collections.<B2BCustomerModel> emptySet());

		// create cart from the order object.
		final CartModel cart = this.<B2BCartService> getCartService().createCartFromAbstractOrder(order);
		if (cart != null)
		{
			cart.setDeliveryAddress(originalDeliveryAddress);
			cart.setPaymentAddress(originalPaymentAddress);
			cart.setPaymentInfo(paymentInfoModel);
			getModelService().save(cart);
			getCartService().removeSessionCart();
			getCommerceCartService().calculateCart(cart);
			getModelService().refresh(cart);
			getCartService().setSessionCart(cart);
		}
	}

	protected PaymentInfoModel getPaymentInfoModelForClonedCart(final OrderModel order)
	{
		final PaymentInfoModel paymentInfoModel = order.getPaymentInfo();
		return (paymentInfoModel != null && paymentInfoModel instanceof InvoicePaymentInfoModel)
				? getModelService().clone(order.getPaymentInfo()):null;
	}

	@Override
	protected ReplenishmentOrderPlacedEvent initializeReplenishmentPlacedEvent(final CartToOrderCronJobModel scheduledCart)
	{
		final ReplenishmentOrderPlacedEvent replenishmentOrderPlacedEvent = new ReplenishmentOrderPlacedEvent(scheduledCart);
		replenishmentOrderPlacedEvent.setCurrency(getCommonI18NService().getCurrentCurrency());
		replenishmentOrderPlacedEvent.setLanguage(getCommonI18NService().getCurrentLanguage());
		replenishmentOrderPlacedEvent.setCustomer((CustomerModel) scheduledCart.getCart().getUser());
		replenishmentOrderPlacedEvent.setBaseStore(getBaseStoreService().getCurrentBaseStore());

		return replenishmentOrderPlacedEvent;
	}

	@Override
	protected B2BCostCenterService<B2BCostCenterModel, B2BCustomerModel> getB2bCostCenterService()
	{
		return b2bCostCenterService;
	}

	@Override
	@Required
	public void setB2bCostCenterService(final B2BCostCenterService<B2BCostCenterModel, B2BCustomerModel> b2bCostCenterService)
	{
		this.b2bCostCenterService = b2bCostCenterService;
	}

	@Override
	protected Converter<B2BCostCenterModel, B2BCostCenterData> getB2bCostCenterConverter()
	{
		return b2bCostCenterConverter;
	}

	@Override
	@Required
	public void setB2bCostCenterConverter(final Converter<B2BCostCenterModel, B2BCostCenterData> b2bCostCenterConverter)
	{
		this.b2bCostCenterConverter = b2bCostCenterConverter;
	}

	@Override
	protected B2BOrderService getB2BOrderService()
	{
		return b2BOrderService;
	}

	@Override
	@Required
	public void setB2BOrderService(final B2BOrderService b2BOrderService)
	{
		this.b2BOrderService = b2BOrderService;
	}

	@Override
	protected GenericDao<AbstractOrderModel> getAbstractOrderGenericDao()
	{
		return abstractOrderGenericDao;
	}

	@Override
	@Required
	public void setAbstractOrderGenericDao(final GenericDao<AbstractOrderModel> abstractOrderGenericDao)
	{
		this.abstractOrderGenericDao = abstractOrderGenericDao;
	}

	@Override
	protected Converter<CheckoutPaymentType, B2BPaymentTypeData> getB2bPaymentTypeDataConverter()
	{
		return b2bPaymentTypeDataConverter;
	}

	@Override
	@Required
	public void setB2bPaymentTypeDataConverter(final Converter<CheckoutPaymentType, B2BPaymentTypeData> b2bPaymentTypeDataConverter)
	{
		this.b2bPaymentTypeDataConverter = b2bPaymentTypeDataConverter;
	}

	@Override
	protected B2BCommentService<AbstractOrderModel> getB2bCommentService()
	{
		return b2bCommentService;
	}

	@Override
	@Required
	public void setB2bCommentService(final B2BCommentService<AbstractOrderModel> b2bCommentService)
	{
		this.b2bCommentService = b2bCommentService;
	}

	@Override
	protected Converter<DayOfWeek, B2BDaysOfWeekData> getB2bDaysOfWeekConverter()
	{
		return b2bDaysOfWeekConverter;
	}

	@Override
	@Required
	public void setB2bDaysOfWeekConverter(final Converter<DayOfWeek, B2BDaysOfWeekData> b2bDaysOfWeekConverter)
	{
		this.b2bDaysOfWeekConverter = b2bDaysOfWeekConverter;
	}

	@Override
	protected Populator<TriggerData, TriggerModel> getTriggerPopulator()
	{
		return triggerPopulator;
	}

	@Override
	@Required
	public void setTriggerPopulator(final Populator<TriggerData, TriggerModel> triggerPopulator)
	{
		this.triggerPopulator = triggerPopulator;
	}

	@Override
	protected ScheduleOrderService getScheduleOrderService()
	{
		return scheduleOrderService;
	}

	@Override
	@Required
	public void setScheduleOrderService(final ScheduleOrderService scheduleOrderService)
	{
		this.scheduleOrderService = scheduleOrderService;
	}

	@Override
	protected Converter<CartToOrderCronJobModel, ScheduledCartData> getScheduledCartConverter()
	{
		return scheduledCartConverter;
	}

	@Override
	@Required
	public void setScheduledCartConverter(final Converter<CartToOrderCronJobModel, ScheduledCartData> scheduledCartConverter)
	{
		this.scheduledCartConverter = scheduledCartConverter;
	}

	@Override
	protected BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	@Override
	@Required
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}

	@Override
	protected BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	@Override
	@Required
	public void setBaseStoreService(final BaseStoreService service)
	{
		this.baseStoreService = service;
	}

	@Override
	protected EventService getEventService()
	{
		return eventService;
	}

	@Override
	@Required
	public void setEventService(final EventService eventService)
	{
		this.eventService = eventService;
	}

	@Override
	protected <T extends B2BCommerceCartService> T getCommerceCartService()
	{
		return (T) commerceCartService;
	}

	@Override
	@Required
	public <T extends B2BCommerceCartService> void setCommerceCartService(final T _commerceCartService)
	{
		this.commerceCartService = _commerceCartService;
	}

	@Override
	protected TriggerService getTriggerService()
	{
		return triggerService;
	}

	@Override
	@Required
	public void setTriggerService(final TriggerService triggerService)
	{
		this.triggerService = triggerService;
	}

	protected I18NService getI18NService()
	{
		return i18NService;
	}

	@Required
	public void setI18NService(final I18NService i18nService)
	{
		i18NService = i18nService;
	}
}

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
package de.hybris.platform.commerceservices.customer.impl;

import de.hybris.platform.commerceservices.customer.*;
import de.hybris.platform.commerceservices.customer.dao.CustomerAccountDao;
import de.hybris.platform.commerceservices.enums.CustomerType;
import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;
import de.hybris.platform.commerceservices.event.ForgottenPwdEvent;
import de.hybris.platform.commerceservices.event.RegisterEvent;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.commerceservices.security.SecureToken;
import de.hybris.platform.commerceservices.security.SecureTokenService;
import de.hybris.platform.commerceservices.strategies.CustomerNameStrategy;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.payment.AdapterException;
import de.hybris.platform.payment.PaymentService;
import de.hybris.platform.payment.dto.BillingInfo;
import de.hybris.platform.payment.dto.CardInfo;
import de.hybris.platform.payment.dto.NewSubscription;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.impl.UniqueAttributesInterceptor;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.user.PasswordEncoderService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import java.util.*;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;
import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;


/**
 * Default implementation of {@link CustomerAccountService}
 */
public class DefaultCustomerAccountService implements CustomerAccountService
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(DefaultCustomerAccountService.class);
	private static final String DEFAULT_PASSWORD_ENCODING = "md5";
	private UserService userService;
	private PaymentService paymentService;
	private ModelService modelService;
	private FlexibleSearchService flexibleSearchService;
	private I18NService i18nService;
	private PasswordEncoderService passwordEncoderService;
	private SecureTokenService secureTokenService;
	private long tokenValiditySeconds;
	private CustomerAccountDao customerAccountDao;
	private BaseStoreService baseStoreService;
	private BaseSiteService baseSiteService;
	private EventService eventService;
	private CommonI18NService commonI18NService;
	private TypeService typeService;
	private CustomerEmailResolutionService customerEmailResolutionService;
	private String passwordEncoding = DEFAULT_PASSWORD_ENCODING;
	private CustomerNameStrategy customerNameStrategy;
	private String monthsForOrderExpiry;
	private TimeService timeService;

	protected void addPaymentInfo(final CustomerModel customerModel, final PaymentInfoModel paymentInfoModel)
	{
		validateParameterNotNull(customerModel, "Customer model cannot be null");
		validateParameterNotNull(paymentInfoModel, "Payment info model cannot be null");
		final List<PaymentInfoModel> paymentInfoModels = new ArrayList<PaymentInfoModel>(customerModel.getPaymentInfos());
		if (!paymentInfoModels.contains(paymentInfoModel))
		{
			paymentInfoModels.add(paymentInfoModel);
			customerModel.setPaymentInfos(paymentInfoModels);

			getModelService().saveAll(customerModel, paymentInfoModel);
			getModelService().refresh(customerModel);
		}
	}

	@Override
	public CreditCardPaymentInfoModel createPaymentSubscription(final CustomerModel customerModel, final CardInfo cardInfo,
			final BillingInfo billingInfo, final String titleCode, final String paymentProvider, final boolean saveInAccount)
	{
		validateParameterNotNull(customerModel, "Customer cannot be null");
		validateParameterNotNull(cardInfo, "CardInfo cannot be null");
		validateParameterNotNull(billingInfo, "billingInfo cannot be null");
		validateParameterNotNull(paymentProvider, "PaymentProvider cannot be null");
		final CurrencyModel currencyModel = getCurrency(customerModel);
		validateParameterNotNull(currencyModel, "Customer session currency cannot be null");

		final Currency currency = getI18nService().getBestMatchingJavaCurrency(currencyModel.getIsocode());

		final AddressModel billingAddress = getModelService().create(AddressModel.class);
		if (StringUtils.isNotBlank(titleCode))
		{
			final TitleModel title = new TitleModel();
			title.setCode(titleCode);
			billingAddress.setTitle(getFlexibleSearchService().getModelByExample(title));
		}
		billingAddress.setFirstname(billingInfo.getFirstName());
		billingAddress.setLastname(billingInfo.getLastName());
		billingAddress.setLine1(billingInfo.getStreet1());
		billingAddress.setLine2(billingInfo.getStreet2());
		billingAddress.setTown(billingInfo.getCity());
		billingAddress.setPostalcode(billingInfo.getPostalCode());
		billingAddress.setCountry(getCommonI18NService().getCountry(billingInfo.getCountry()));
		final String email = getCustomerEmailResolutionService().getEmailForCustomer(customerModel);
		billingAddress.setEmail(email);

		final String merchantTransactionCode = customerModel.getUid() + "-" + UUID.randomUUID();
		try
		{
			final NewSubscription subscription = getPaymentService().createSubscription(merchantTransactionCode, paymentProvider,
					currency, billingAddress, cardInfo);

			if (StringUtils.isNotBlank(subscription.getSubscriptionID()))
			{
				final CreditCardPaymentInfoModel cardPaymentInfoModel = getModelService().create(CreditCardPaymentInfoModel.class);
				cardPaymentInfoModel.setCode(customerModel.getUid() + "_" + UUID.randomUUID());
				cardPaymentInfoModel.setUser(customerModel);
				cardPaymentInfoModel.setSubscriptionId(subscription.getSubscriptionID());
				cardPaymentInfoModel.setNumber(getMaskedCardNumber(cardInfo.getCardNumber()));
				cardPaymentInfoModel.setType(cardInfo.getCardType());
				cardPaymentInfoModel.setCcOwner(cardInfo.getCardHolderFullName());
				cardPaymentInfoModel.setValidToMonth(String.format("%02d", cardInfo.getExpirationMonth()));
				cardPaymentInfoModel.setValidToYear(String.valueOf(cardInfo.getExpirationYear()));
				if (cardInfo.getIssueMonth() != null)
				{
					cardPaymentInfoModel.setValidFromMonth(String.valueOf(cardInfo.getIssueMonth()));
				}
				if (cardInfo.getIssueYear() != null)
				{
					cardPaymentInfoModel.setValidFromYear(String.valueOf(cardInfo.getIssueYear()));
				}

				cardPaymentInfoModel.setSubscriptionId(subscription.getSubscriptionID());
				cardPaymentInfoModel.setSaved(saveInAccount);
				if (!StringUtils.isEmpty(cardInfo.getIssueNumber()))
				{
					cardPaymentInfoModel.setIssueNumber(Integer.valueOf(cardInfo.getIssueNumber()));
				}

				billingAddress.setOwner(cardPaymentInfoModel);
				cardPaymentInfoModel.setBillingAddress(billingAddress);

				getModelService().saveAll(billingAddress, cardPaymentInfoModel);
				getModelService().refresh(customerModel);

				addPaymentInfo(customerModel, cardPaymentInfoModel);

				return cardPaymentInfoModel;
			}
		}
		catch (final AdapterException ae)
		{
			LOG.error(String.format("Failed to create subscription for customer %s due to error of [%s]", customerModel.getUid(),
					ae.getMessage()));

			return null;
		}

		return null;
	}

	protected CurrencyModel getCurrency(final CustomerModel customerModel)
	{
		if (customerModel != null && customerModel.getSessionCurrency() != null)
		{
			return customerModel.getSessionCurrency();
		}
		return getCommonI18NService().getCurrentCurrency();
	}

	/**
	 * This is a default implementation to get masked card number give the card number
	 * 
	 * @param cardNumber
	 *           the card number
	 * @return the masked number
	 */
	protected String getMaskedCardNumber(final String cardNumber)
	{
		if (cardNumber != null && cardNumber.trim().length() > 4)
		{
			final String endPortion = cardNumber.trim().substring(cardNumber.length() - 4);
			return "************" + endPortion;
		}
		return null;
	}

	@Override
	public void setDefaultPaymentInfo(final CustomerModel customerModel, final PaymentInfoModel paymentInfoModel)
	{
		validateParameterNotNull(customerModel, "Customer model cannot be null");
		validateParameterNotNull(paymentInfoModel, "Payment info model cannot be null");
		if (customerModel.getPaymentInfos().contains(paymentInfoModel))
		{
			customerModel.setDefaultPaymentInfo(paymentInfoModel);
			getModelService().save(customerModel);
			getModelService().refresh(customerModel);
		}
	}

	@Override
	public List<CreditCardPaymentInfoModel> getCreditCardPaymentInfos(final CustomerModel customerModel, final boolean saved)
	{
		validateParameterNotNull(customerModel, "Customer model cannot be null");
		return getCustomerAccountDao().findCreditCardPaymentInfosByCustomer(customerModel, saved);
	}

	@Override
	public CreditCardPaymentInfoModel getCreditCardPaymentInfoForCode(final CustomerModel customerModel, final String code)
	{
		validateParameterNotNull(customerModel, "Customer model cannot be null");
		return getCustomerAccountDao().findCreditCardPaymentInfoByCustomer(customerModel, code);
	}

	@Override
	public List<AddressModel> getAllAddressEntries(final CustomerModel customerModel)
	{
		validateParameterNotNull(customerModel, "Customer model cannot be null");
		final List<AddressModel> addressModels = new ArrayList<AddressModel>();
		addressModels.addAll(customerModel.getAddresses());
		return addressModels;
	}

	@Override
	public List<AddressModel> getAddressBookEntries(final CustomerModel customerModel)
	{
		validateParameterNotNull(customerModel, "Customer model cannot be null");
		final List<AddressModel> addressModels = new ArrayList<AddressModel>();

		for (final AddressModel address : customerModel.getAddresses())
		{
			if (Boolean.TRUE.equals(address.getVisibleInAddressBook()))
			{
				addressModels.add(address);
			}
		}

		return addressModels;
	}

	@Override
	public List<AddressModel> getAddressBookDeliveryEntries(final CustomerModel customerModel)
	{
		validateParameterNotNull(customerModel, "Customer model cannot be null");
		final List<AddressModel> addressModels = new ArrayList<AddressModel>();

		for (final AddressModel address : customerModel.getAddresses())
		{
			if (Boolean.TRUE.equals(address.getShippingAddress()) && Boolean.TRUE.equals(address.getVisibleInAddressBook()))
			{
				addressModels.add(address);
			}
		}

		return addressModels;
	}

	@Override
	public AddressModel getAddressForCode(final CustomerModel customerModel, final String code)
	{
		validateParameterNotNull(customerModel, "Customer model cannot be null");

		for (final AddressModel addressModel : getAllAddressEntries(customerModel))
		{
			if (addressModel.getPk().getLongValueAsString().equals(code))
			{
				return addressModel;
			}
		}
		return null;
	}

	@Override
	public AddressModel getDefaultAddress(final CustomerModel customerModel)
	{
		return getUserService().getCurrentUser().getDefaultShipmentAddress();
	}

	@Override
	public void saveAddressEntry(final CustomerModel customerModel, final AddressModel addressModel)
	{
		validateParameterNotNull(customerModel, "Customer model cannot be null");
		validateParameterNotNull(addressModel, "Address model cannot be null");
		final List<AddressModel> customerAddresses = new ArrayList<AddressModel>();
		customerAddresses.addAll(customerModel.getAddresses());
		if (customerModel.getAddresses().contains(addressModel))
		{
			getModelService().save(addressModel);
		}
		else
		{
			addressModel.setOwner(customerModel);
			getModelService().save(addressModel);
			getModelService().refresh(addressModel);
			customerAddresses.add(addressModel);
		}
		customerModel.setAddresses(customerAddresses);

		getModelService().save(customerModel);
		getModelService().refresh(customerModel);
	}

	@Override
	public void deleteAddressEntry(final CustomerModel customerModel, final AddressModel addressModel)
	{
		validateParameterNotNull(customerModel, "Customer model cannot be null");
		validateParameterNotNull(addressModel, "Address model cannot be null");
		if (customerModel.getAddresses().contains(addressModel))
		{
			getModelService().remove(addressModel);
			getModelService().refresh(customerModel);
		}
		else
		{
			throw new IllegalArgumentException("Address " + addressModel + " does not belong to the customer " + customerModel
					+ " and will not be removed.");
		}
	}

	@Override
	public void deleteCCPaymentInfo(final CustomerModel customerModel, final CreditCardPaymentInfoModel creditCardPaymentInfo)
	{
		validateParameterNotNull(customerModel, "Customer model cannot be null");
		validateParameterNotNull(creditCardPaymentInfo, "CreditCardPaymentInfo model cannot be null");
		if (customerModel.getPaymentInfos().contains(creditCardPaymentInfo))
		{
			getModelService().remove(creditCardPaymentInfo);
			getModelService().refresh(customerModel);
		}
		else
		{
			throw new IllegalArgumentException("Credit Card Payment Info " + creditCardPaymentInfo
					+ " does not belong to the customer " + customerModel + " and will not be removed.");
		}
	}

	@Override
	public void unlinkCCPaymentInfo(final CustomerModel customerModel, final CreditCardPaymentInfoModel creditCardPaymentInfo)
	{
		validateParameterNotNull(customerModel, "Customer model cannot be null");
		validateParameterNotNull(creditCardPaymentInfo, "CreditCardPaymentInfo model cannot be null");
		if (customerModel.getPaymentInfos().contains(creditCardPaymentInfo))
		{
			final Collection<PaymentInfoModel> paymentInfoList = new ArrayList(customerModel.getPaymentInfos());
			paymentInfoList.remove(creditCardPaymentInfo);
			customerModel.setPaymentInfos(paymentInfoList);
			getModelService().save(customerModel);
			getModelService().refresh(customerModel);
		}
		else
		{
			throw new IllegalArgumentException("Credit Card Payment Info " + creditCardPaymentInfo
					+ " does not belong to the customer " + customerModel + " and will not be removed.");
		}
	}

	@Override
	public void setDefaultAddressEntry(final CustomerModel customerModel, final AddressModel addressModel)
	{
		validateParameterNotNull(customerModel, "Customer model cannot be null");
		validateParameterNotNull(addressModel, "Address model cannot be null");
		if (customerModel.getAddresses().contains(addressModel))
		{
			customerModel.setDefaultPaymentAddress(addressModel);
			customerModel.setDefaultShipmentAddress(addressModel);
		}
		else
		{
			final AddressModel clone = getModelService().clone(addressModel);
			clone.setOwner(customerModel);
			getModelService().save(clone);
			final List<AddressModel> customerAddresses = new ArrayList<AddressModel>();
			customerAddresses.addAll(customerModel.getAddresses());
			customerAddresses.add(clone);
			customerModel.setAddresses(customerAddresses);
			customerModel.setDefaultPaymentAddress(clone);
			customerModel.setDefaultShipmentAddress(clone);
		}
		getModelService().save(customerModel);
		getModelService().refresh(customerModel);
	}

	@Override
	public void clearDefaultAddressEntry(final CustomerModel customerModel)
	{
		validateParameterNotNull(customerModel, "Customer model cannot be null");
		customerModel.setDefaultPaymentAddress(null);
		customerModel.setDefaultShipmentAddress(null);
		getModelService().save(customerModel);
		getModelService().refresh(customerModel);
	}

	@Override
	public Collection<TitleModel> getTitles()
	{
		return getUserService().getAllTitles();
	}

	@Override
	public void register(final CustomerModel customerModel, final String password) throws DuplicateUidException
	{
		registerCustomer(customerModel, password);
		getEventService().publishEvent(initializeEvent(new RegisterEvent(), customerModel));
	}

	@Override
	public void registerGuestForAnonymousCheckout(final CustomerModel customerModel, final String password)
			throws DuplicateUidException
	{
		registerCustomer(customerModel, password);
	}

	@Deprecated
	@Override
	public void updateProfile(final CustomerModel customerModel, final String titleCode, final String name, final String login)
			throws DuplicateUidException
	{
		validateParameterNotNullStandardMessage("customerModel", customerModel);

		customerModel.setUid(login);
		customerModel.setName(name);
		if (StringUtils.isNotBlank(titleCode))
		{
			customerModel.setTitle(getUserService().getTitleForCode(titleCode));
		}
		else
		{
			customerModel.setTitle(null);
		}
		internalSaveCustomer(customerModel);
	}

	@Override
	public void changePassword(final UserModel userModel, final String oldPassword, final String newPassword)
			throws PasswordMismatchException
	{
		validateParameterNotNullStandardMessage("customerModel", userModel);
		if (!getUserService().isAnonymousUser(userModel))
		{
			final String encodedCurrentPassword = getPasswordEncoderService().encode(userModel, oldPassword,
					userModel.getPasswordEncoding());
			if (encodedCurrentPassword.equals(userModel.getEncodedPassword()))
			{
				getUserService().setPassword(userModel, newPassword, userModel.getPasswordEncoding());
				getModelService().save(userModel);
			}
			else
			{
				throw new PasswordMismatchException(userModel.getUid());
			}
		}
	}

	@Override
	public void forgottenPassword(final CustomerModel customerModel)
	{
		validateParameterNotNullStandardMessage("customerModel", customerModel);
		final long timeStamp = getTokenValiditySeconds() > 0L ? new Date().getTime() : 0L;
		final SecureToken data = new SecureToken(customerModel.getUid(), timeStamp);
		final String token = getSecureTokenService().encryptData(data);
		customerModel.setToken(token);
		getModelService().save(customerModel);
		getEventService().publishEvent(initializeEvent(new ForgottenPwdEvent(token), customerModel));
	}

	@Override
	public void updatePassword(final String token, final String newPassword) throws TokenInvalidatedException
	{
		Assert.hasText(token, "The field [token] cannot be empty");
		Assert.hasText(newPassword, "The field [newPassword] cannot be empty");

		final SecureToken data = getSecureTokenService().decryptData(token);
		if (getTokenValiditySeconds() > 0L)
		{
			final long delta = new Date().getTime() - data.getTimeStamp();
			if (delta / 1000 > getTokenValiditySeconds())
			{
				throw new IllegalArgumentException("token expired");
			}
		}

		final CustomerModel customer = getUserService().getUserForUID(data.getData(), CustomerModel.class);
		if (customer == null)
		{
			throw new IllegalArgumentException("user for token not found");
		}
		if (!token.equals(customer.getToken()))
		{
			throw new TokenInvalidatedException();
		}
		customer.setToken(null);
		customer.setLoginDisabled(false);
		getModelService().save(customer);

		getUserService().setPassword(data.getData(), newPassword, getPasswordEncoding());
	}

	@Override
	@Deprecated
	public OrderModel getOrderForCode(final CustomerModel customerModel, final String code, final BaseStoreModel store)
	{
		validateParameterNotNull(customerModel, "Customer model cannot be null");
		validateParameterNotNull(code, "Order code cannot be null");
		validateParameterNotNull(store, "Store must not be null");
		return getCustomerAccountDao().findOrderByCustomerAndCodeAndStore(customerModel, code, store);
	}

	@Override
	public OrderModel getGuestOrderForGUID(final String guid, final BaseStoreModel store)
	{
		validateParameterNotNull(guid, "Order guid cannot be null");
		validateParameterNotNull(store, "Store must not be null");

		try
		{

			final OrderModel orderModel = getCustomerAccountDao().findOrderByGUIDAndStore(guid, store, null);

			if (new DateTime(getTimeService().getCurrentTime()).minusMonths(Integer.parseInt(getMonthsForOrderExpiry())).toDate()
					.after(orderModel.getDate()))
			{
				throw new IllegalArgumentException("OrderModel with guid " + guid + " is not visible due to being older than "
						+ getMonthsForOrderExpiry() + " months");
			}

			return orderModel;

		}
		catch (final ModelNotFoundException ex)
		{
			return null;
		}
	}

	@Override
	public OrderModel getOrderDetailsForGUID(final String guid, final BaseStoreModel store)
	{
		validateParameterNotNull(guid, "Order guid cannot be null");
		validateParameterNotNull(store, "Store must not be null");

		try
		{
			return getCustomerAccountDao().findOrderByGUIDAndStore(guid, store, null);
		}
		catch (final ModelNotFoundException ex)
		{
			return null;
		}
	}

	@Override
	public List<OrderModel> getOrderList(final CustomerModel customerModel, final BaseStoreModel store, final OrderStatus[] status)
	{
		validateParameterNotNull(customerModel, "Customer model cannot be null");
		validateParameterNotNull(store, "Store must not be null");
		return getCustomerAccountDao().findOrdersByCustomerAndStore(customerModel, store, status);
	}

	@Override
	public SearchPageData<OrderModel> getOrderList(final CustomerModel customerModel, final BaseStoreModel store,
			final OrderStatus[] status, final PageableData pageableData)
	{
		validateParameterNotNull(customerModel, "Customer model cannot be null");
		validateParameterNotNull(store, "Store must not be null");
		validateParameterNotNull(pageableData, "PageableData must not be null");
		return getCustomerAccountDao().findOrdersByCustomerAndStore(customerModel, store, status, pageableData);
	}

	@Override
	public void convertGuestToCustomer(final String pwd, final String orderGUID) throws DuplicateUidException
	{
		final OrderModel orderModel = getOrderDetailsForGUID(orderGUID, getBaseStoreService().getCurrentBaseStore());
		if (orderModel == null)
		{
			throw new UnknownIdentifierException("Order with guid " + orderGUID + " not found in current BaseStore");
		}

		final CustomerModel customer = (CustomerModel) orderModel.getUser();
		if (!CustomerType.GUEST.equals(customer.getType()))
		{
			throw new IllegalArgumentException("Order with guid " + orderGUID + " does not belong to guest user");
		}

		fillValuesForCustomerInfo(customer);
		register(customer, pwd);
		getUserService().setCurrentUser(customer);
	}

	@Override
	public OrderModel getOrderForCode(final String code, final BaseStoreModel store)
	{
		validateParameterNotNull(code, "Order code cannot be null");
		validateParameterNotNull(store, "Store must not be null");
		return getCustomerAccountDao().findOrderByCodeAndStore(code, store);
	}

	protected void fillValuesForCustomerInfo(final CustomerModel customer) throws DuplicateUidException
	{
		// Pull the name from the guest's billing info
		if (customer.getDefaultPaymentAddress() != null)
		{
			customer.setName(getCustomerNameStrategy().getName(customer.getDefaultPaymentAddress().getFirstname(),
					customer.getDefaultPaymentAddress().getLastname()));
			customer.setTitle(customer.getDefaultPaymentAddress().getTitle());
		}
		customer.setUid(StringUtils.substringAfter(customer.getUid(), "|"));
		customer.setType(null);
	}

	/**
	 * initializes an {@link AbstractCommerceUserEvent}
	 * 
	 * @param event
	 * @param customerModel
	 * @return the event
	 */
	protected AbstractCommerceUserEvent initializeEvent(final AbstractCommerceUserEvent event, final CustomerModel customerModel)
	{
		event.setBaseStore(getBaseStoreService().getCurrentBaseStore());
		event.setSite(getBaseSiteService().getCurrentBaseSite());
		event.setCustomer(customerModel);
		event.setLanguage(getCommonI18NService().getCurrentLanguage());
		event.setCurrency(getCommonI18NService().getCurrentCurrency());
		return event;
	}

	/**
	 * Generates a customer ID during registration
	 * 
	 * @param customerModel
	 */
	protected void generateCustomerId(final CustomerModel customerModel)
	{
		customerModel.setCustomerID(UUID.randomUUID().toString());
	}

	/**
	 * Saves the customer translating model layer exceptions regarding duplicate identifiers
	 * 
	 * @param customerModel
	 * @throws DuplicateUidException
	 *            if the uid is not unique
	 */
	protected void internalSaveCustomer(final CustomerModel customerModel) throws DuplicateUidException
	{
		try
		{
			getModelService().save(customerModel);
		}
		catch (final ModelSavingException e)
		{
			if (e.getCause() instanceof InterceptorException
					&& ((InterceptorException) e.getCause()).getInterceptor().getClass().equals(UniqueAttributesInterceptor.class))
			{
				throw new DuplicateUidException(customerModel.getUid(), e);
			}
			else
			{
				throw e;
			}
		}
		catch (final AmbiguousIdentifierException e)
		{
			throw new DuplicateUidException(customerModel.getUid(), e);
		}
	}

	@Override
	public void changeUid(final String newUid, final String currentPassword) throws DuplicateUidException,
			PasswordMismatchException
	{
		Assert.hasText(newUid, "The field [newEmail] cannot be empty");
		Assert.hasText(currentPassword, "The field [currentPassword] cannot be empty");

		final String newUidLower = newUid.toLowerCase();
		final CustomerModel currentUser = (CustomerModel) getUserService().getCurrentUser();
		currentUser.setOriginalUid(newUid);

		// check uniqueness only if the uids are case insensitive different
		if (!currentUser.getUid().equalsIgnoreCase(newUid))
		{
			checkUidUniqueness(newUidLower);
		}
		adjustPassword(currentUser, newUidLower, currentPassword);
	}

	/**
	 * Adjusts a given current user {@link UserModel#UID} with a newUid value unless the user with newUid does not exists
	 * and password for current user matches given currentPassword.
	 */
	protected void adjustPassword(final UserModel currentUser, final String newUid, final String currentPassword)
			throws PasswordMismatchException
	{

		// Validate that the current password is correct
		final String encodedCurrentPassword = getPasswordEncoderService().encode(currentUser, currentPassword,
				currentUser.getPasswordEncoding());
		if (!encodedCurrentPassword.equals(currentUser.getEncodedPassword()))
		{
			throw new PasswordMismatchException(currentUser.getUid());
		}

		// Save the newUid
		currentUser.setUid(newUid);
		getModelService().save(currentUser);

		// Update the password
		getUserService().setPassword(currentUser, currentPassword, currentUser.getPasswordEncoding());
		getModelService().save(currentUser);
	}

	protected void checkUidUniqueness(final String newUid) throws DuplicateUidException
	{
		// Check if the newUid is available
		try
		{
			if (getUserService().getUserForUID(newUid) != null)
			{
				throw new DuplicateUidException("User with email " + newUid + " already exists.");
			}
		}
		catch (final UnknownIdentifierException unknownIdentifierException)
		{
			// That's ok - user for new uid was not found
		}
	}

	protected void registerCustomer(final CustomerModel customerModel, final String password) throws DuplicateUidException
	{
		validateParameterNotNullStandardMessage("customerModel", customerModel);

		generateCustomerId(customerModel);
		if (password != null)
		{
			getUserService().setPassword(customerModel, password, getPasswordEncoding());
		}
		internalSaveCustomer(customerModel);
	}

	protected PaymentService getPaymentService()
	{
		return paymentService;
	}

	@Required
	public void setPaymentService(final PaymentService paymentService)
	{
		this.paymentService = paymentService;
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

	protected I18NService getI18nService()
	{
		return i18nService;
	}

	@Required
	public void setI18nService(final I18NService i18nService)
	{
		this.i18nService = i18nService;
	}

	protected UserService getUserService()
	{
		return userService;
	}

	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	/**
	 * @return the passwordEncoderService
	 */
	protected PasswordEncoderService getPasswordEncoderService()
	{
		return passwordEncoderService;
	}

	/**
	 * @param passwordEncoderService
	 *           the passwordEncoderService to set
	 */
	@Required
	public void setPasswordEncoderService(final PasswordEncoderService passwordEncoderService)
	{
		this.passwordEncoderService = passwordEncoderService;
	}

	/**
	 * @return the secureTokenService
	 */
	protected SecureTokenService getSecureTokenService()
	{
		return secureTokenService;
	}

	/**
	 * @param secureTokenService
	 *           the secureTokenService to set
	 */
	@Required
	public void setSecureTokenService(final SecureTokenService secureTokenService)
	{
		this.secureTokenService = secureTokenService;
	}

	/**
	 * @return the tokenValiditySeconds
	 */
	protected long getTokenValiditySeconds()
	{
		return tokenValiditySeconds;
	}

	/**
	 * @param tokenValiditySeconds
	 *           the tokenValiditySeconds to set
	 */
	@Required
	public void setTokenValiditySeconds(final long tokenValiditySeconds)
	{
		if (tokenValiditySeconds < 0)
		{
			throw new IllegalArgumentException("tokenValiditySeconds has to be >= 0");
		}
		this.tokenValiditySeconds = tokenValiditySeconds;
	}

	protected CustomerAccountDao getCustomerAccountDao()
	{
		return customerAccountDao;
	}

	@Required
	public void setCustomerAccountDao(final CustomerAccountDao customerAccountDao)
	{
		this.customerAccountDao = customerAccountDao;
	}

	/**
	 * @return the baseStoreService
	 */
	@Required
	protected BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	/**
	 * @param service
	 *           the baseStoreService to set
	 */
	@Required
	public void setBaseStoreService(final BaseStoreService service)
	{
		this.baseStoreService = service;
	}

	/**
	 * @return the {@link BaseSiteService}
	 */
	protected BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	/**
	 * @param siteService
	 *           the {@link BaseSiteService} to set
	 */
	@Required
	public void setBaseSiteService(final BaseSiteService siteService)
	{
		this.baseSiteService = siteService;
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

	/**
	 * @return the eventService
	 */
	protected EventService getEventService()
	{
		return eventService;
	}

	/**
	 * @param eventService
	 *           the eventService to set
	 */
	@Required
	public void setEventService(final EventService eventService)
	{
		this.eventService = eventService;
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

	protected String getPasswordEncoding()
	{
		return passwordEncoding;
	}

	// Optional: Defaults to md5
	public void setPasswordEncoding(final String passwordEncoding)
	{
		Assert.hasText(passwordEncoding);
		this.passwordEncoding = passwordEncoding;
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

	public TypeService getTypeService()
	{
		return typeService;
	}

	@Required
	public void setTypeService(final TypeService typeService)
	{
		this.typeService = typeService;
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

	protected String getMonthsForOrderExpiry()
	{
		return monthsForOrderExpiry;
	}

	@Required
	public void setMonthsForOrderExpiry(final String monthsForOrderExpiry)
	{
		this.monthsForOrderExpiry = monthsForOrderExpiry;
	}

	protected TimeService getTimeService()
	{
		return timeService;
	}

	@Required
	public void setTimeService(final TimeService timeService)
	{
		this.timeService = timeService;
	}

}

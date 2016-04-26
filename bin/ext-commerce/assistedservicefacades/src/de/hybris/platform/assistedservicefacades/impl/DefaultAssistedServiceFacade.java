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
package de.hybris.platform.assistedservicefacades.impl;

import de.hybris.platform.assistedservicefacades.AssistedServiceFacade;
import de.hybris.platform.assistedservicefacades.constants.AssistedservicefacadesConstants;
import de.hybris.platform.assistedservicefacades.exception.AssistedServiceAgentBadCredentialsException;
import de.hybris.platform.assistedservicefacades.exception.AssistedServiceAgentBlockedException;
import de.hybris.platform.assistedservicefacades.exception.AssistedServiceAgentNoCustomerAndCartIdException;
import de.hybris.platform.assistedservicefacades.exception.AssistedServiceAgentNotLoggedInException;
import de.hybris.platform.assistedservicefacades.exception.AssistedServiceCartBindException;
import de.hybris.platform.assistedservicefacades.exception.AssistedServiceFacadeException;
import de.hybris.platform.assistedservicefacades.exception.AssistedServiceWrongCartIdException;
import de.hybris.platform.assistedservicefacades.exception.AssistedServiceWrongCustomerIdException;
import de.hybris.platform.assistedservicefacades.util.AssistedServiceSession;
import de.hybris.platform.assistedservicefacades.util.AssistedServiceUtils;
import de.hybris.platform.assistedservicefacades.util.CustomerEmulationParams;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commerceservices.customer.CustomerAccountService;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.user.CookieBasedLoginToken;
import de.hybris.platform.jalo.user.LoginToken;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.impl.DefaultCartService;
import de.hybris.platform.servicelayer.exceptions.ClassMismatchException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.PasswordEncoderService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.localization.Localization;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of the {@link AssistedServiceFacade} interface.
 */
public class DefaultAssistedServiceFacade implements AssistedServiceFacade
{

	// Abbreviation 'AS' in this class means Assisted Service
	private static final Logger LOG = Logger.getLogger(AssistedServiceFacade.class);

	// Hashmap for storing number of bad password attempts for each agent
	private final ConcurrentHashMap<String, Integer> bruteForceAttackCache = new ConcurrentHashMap();

	// Session key for storing customer which was logged in before AS agent
	private static final String AS_SAVED_ON_AGENTLOGIN_CUSTOMER = AssistedServiceFacade.class.getName() + "_saved_customer";

	/* Default parameters values */
	private static final int DEFAULT_SESSION_TIMEOUT = 660;
	private static final int DEFAULT_SESSION_TIMER = 600;
	private static final int DEFAULT_MAX_BAD_ATTEMPTS = 3;

	private CartService cartService;
	private SessionService sessionService;
	private UserService userService;
	private CustomerFacade customerFacade;
	private PasswordEncoderService passwordEncoderService;
	private FlexibleSearchService flexibleSearchService;
	private ModelService modelService;
	private CustomerAccountService customerAccountService;
	private CommerceCartService commerceCartService;
	private BaseSiteService baseSiteService;
	private CommonI18NService commonI18NService;

	@Override
	public Map<String, Object> getAssistedServiceSessionAttributes()
	{
		// Populate model with params to be displayed
		final Map<String, Object> attributes = new HashMap();
		attributes.putAll(getAsmSession().getAsmSessionParametersMap());
		attributes
				.put("cart", getSessionService().getCurrentSession().getAttribute(DefaultCartService.SESSION_CART_PARAMETER_NAME));
		attributes.put("agentTimer", String.valueOf(getAssistedServiceSessionTimerValue()));
		attributes.put("createDisabled", Config.getParameter(AssistedservicefacadesConstants.CREATE_DISABLED_PROPERTY));
		final String errorMessage = getAsmSession().getFlashErrorMessage();
		if (errorMessage != null)
		{
			attributes.put("asm_message", errorMessage);
		}
		return attributes;
	}

	@Override
	public boolean isAssistedServiceModeLaunched()
	{
		return getAsmSession() != null;
	}

	@Override
	public void emulateCustomer(final String customerId, final String cartId) throws AssistedServiceFacadeException
	{
		try
		{
			// validate session at first
			validateSession();
		}
		catch (final AssistedServiceAgentNotLoggedInException e)
		{
			// in case AS agent isn't logged in yet - save parameters in session
			getAsmSession().setSavedEmulationData(new CustomerEmulationParams(customerId, cartId));
			throw e;
		}

		// Both parameters can't be blank at the same time
		if (StringUtils.isBlank(customerId) && StringUtils.isBlank(cartId))
		{
			throw new AssistedServiceAgentNoCustomerAndCartIdException(
					Localization.getLocalizedString("asm.emulate.error.no_customer_or_cart_id_provided"));
		}

		try
		{
			final UserModel customer = getCustomer(customerId);
			// emulate only customers and not agents
			if ((customer instanceof CustomerModel) && !isAssistedServiceAgent(customer))
			{
				// attach cart to the session in case it's been provided
				if (StringUtils.isNotBlank(cartId))
				{
					final CartModel cart = getCartByCode(cartId, customer);
					if (cart == null)
					{
						throw new AssistedServiceWrongCartIdException("Cart ID not found");
					}
					getCartService().setSessionCart(cart);
				}
				else
				{
					// in case no cartId has been provided get the latest modified one
					final CartModel cart = getLatestModifiedCart(customer);
					if (cart != null)
					{
						getCartService().setSessionCart(cart);
					}
					else
					{
						detachSessionCart();
					}
				}

				getUserService().setCurrentUser(customer);
				getAsmSession().setEmulatedCustomer(customer);
			}
			else
			{
				throw new AssistedServiceWrongCustomerIdException(
						"This id belongs to non-customer person. Will not add customer and/or cart to the session.");
			}

		}
		catch (final UnknownIdentifierException e)
		{
			throw new AssistedServiceWrongCustomerIdException(
					"Unknown customer id. Will not add customer and/or cart to the session.", e);
		}
	}

	@Override
	public void stopEmulateCustomer()
	{
		getAsmSession().setEmulatedCustomer(null);
		detachSessionCart();
		getUserService().setCurrentUser(getUserService().getAnonymousUser());
	}

	/**
	 * Detaches current cart from session and attaches newly created empty one.
	 */
	private void detachSessionCart()
	{
		getSessionService().removeAttribute(DefaultCartService.SESSION_CART_PARAMETER_NAME);
		// At first look this method doesn't make any sense, but that's the simplest way
		//    to create new empty cart
		getCartService().setSessionCart(getCartService().getSessionCart());
	}

	private UserModel getCustomer(final String customerId)
	{
		if (StringUtils.isBlank(customerId))
		{
			return getUserService().getAnonymousUser();
		}
		else
		{
			final StringBuffer buf = new StringBuffer();

			// select the chosen customer using his UID or CustomerId
			buf.append("SELECT DISTINCT {p:" + CustomerModel.PK + "} ");
			buf.append("FROM {" + CustomerModel._TYPECODE + " as p } ");
			buf.append("WHERE {p:" + CustomerModel.UID + "} = ?customerId ");
			buf.append("OR {p:" + CustomerModel.CUSTOMERID + "} = ?customerId ");

			final FlexibleSearchQuery query = new FlexibleSearchQuery(buf.toString());
			query.addQueryParameter("customerId", customerId);
			final List<CustomerModel> matchCustomers = getFlexibleSearchService().<CustomerModel> search(query).getResult();
			if (CollectionUtils.isEmpty(matchCustomers))
			{
				throw new UnknownIdentifierException((new StringBuilder("Cannot find user with id '")).append(customerId).append("'")
						.toString());
			}
			if (matchCustomers.size() > 1)
			{
				LOG.warn("More than two customers were found with id=[" + customerId + "]");
			}
			return matchCustomers.iterator().next();
		}
	}

	private CartModel getCartByCode(final String cartCode, final UserModel customer)
	{
		final CartModel cartModel = getCommerceCartService().getCartForCodeAndUser(cartCode, customer);
		if (cartModel != null)
		{
			return isCartMatchBaseSite(cartModel) ? cartModel : null;
		}
		return null;
	}

	private CartModel getLatestModifiedCart(final UserModel customer)
	{
		return CollectionUtils.isNotEmpty(customer.getCarts()) ? customer.getCarts().stream()
				.filter(cart -> isCartMatchBaseSite(cart)).max(Comparator.comparing(CartModel::getModifiedtime)).get() : null;
	}

	@Override
	public void launchAssistedServiceMode()
	{
		cleanAsmSession();
	}

	@Override
	public void quitAssistedServiceMode()
	{
		try
		{
			logoutAssistedServiceAgent();
		}
		catch (final AssistedServiceFacadeException e)
		{
			// AssistedServiceFacadeException here means that AS agent isn't logged in.
			// That's ok. Just do nothing after that
		}
		quitAsmSession();
	}

	@Override
	public void loginAssistedServiceAgent(final String username, final String password) throws AssistedServiceFacadeException
	{
		verifyAssistedServiceMode();
		verifyFormLoginAbility();
		try
		{
			final UserModel agent = getUserService().getUserForUID(username, EmployeeModel.class);

			if (agent.isLoginDisabled())
			{
				throw new AssistedServiceAgentBlockedException("Account was blocked.");
			}

			if (!getPasswordEncoderService().isValid(agent, password))
			{
				// check for a possible brute force
				if (isBruteForce(agent))
				{
					agent.setLoginDisabled(true);
					getModelService().save(agent);
					resetBruteForceCounter(agent);
					throw new AssistedServiceAgentBlockedException("Account has been blocked.");
				}
				throw new AssistedServiceAgentBadCredentialsException("User Name and Password doesn't match.");
			}
			// reset brute force counter after successful login
			resetBruteForceCounter(agent);
			loginAssistedServiceAgent(agent);
			LOG.info(String.format("Agent [%s] has been loged in using login form", agent.getUid()));
		}
		catch (final UnknownIdentifierException | ClassMismatchException e)
		{
			LOG.debug(e.getMessage());
			throw new AssistedServiceAgentBadCredentialsException("Unknown user id.");
		}
	}

	@Override
	public void loginAssistedServiceAgent(final HttpServletRequest request) throws AssistedServiceFacadeException
	{
		// get token from request (cookie)
		final Cookie samlTokenCookie = AssistedServiceUtils.getSamlCookie(request);
		if (null != samlTokenCookie)
		{
			launchAssistedServiceMode();
			final LoginToken token = new CookieBasedLoginToken(samlTokenCookie);
			try
			{
				// grab user model
				final UserModel agent = getUserService().getUserForUID(token.getUser().getUid());

				// ensure credentials (password didn't change)
				if (agent.getEncodedPassword().equals(token.getPassword()))
				{
					// success --> do 'soft login'
					// optional: remove cookie ?
					loginAssistedServiceAgent(agent);
					LOG.info(String.format("Agent [%s] has been loged in using saml", agent.getUid()));
				}
			}
			catch (final UnknownIdentifierException | ClassMismatchException e)
			{
				LOG.debug(e.getMessage());
				throw new AssistedServiceAgentBadCredentialsException("Unknown user id.");
			}
		}
	}

	private void loginAssistedServiceAgent(final UserModel agent) throws AssistedServiceFacadeException
	{
		verifyAssistedServiceAgent(agent);

		// there is a little hack over there - we change current user to 'anonymous', so it's not going to be removed on authentication step
		getCartService().changeCurrentCartUser(getUserService().getAnonymousUser());

		getAsmSession().setAgent(agent);

		// save previously logged in customer
		if (!isAssistedServiceAgent(getUserService().getCurrentUser()))
		{
			getSessionService().getCurrentSession().setAttribute(AS_SAVED_ON_AGENTLOGIN_CUSTOMER, getUserService().getCurrentUser());
		}
	}

	@Override
	public void logoutAssistedServiceAgent() throws AssistedServiceFacadeException
	{
		verifyAssistedServiceMode();
		if (getAsmSession().getAgent() == null)
		{
			throw new AssistedServiceFacadeException(Localization.getLocalizedString("asm.logout.error.no_agent"));
		}
		cleanAsmSession();

		if (isAssistedServiceAgent(getUserService().getCurrentUser()))
		{
			getUserService().setCurrentUser(getUserService().getAnonymousUser());
			getCartService().getSessionCart().setUser(getUserService().getAnonymousUser());
		}
	}

	@Override
	public int getAssistedServiceSessionTimeout()
	{
		final String timeout = Config.getParameter(AssistedservicefacadesConstants.ASM_AGENT_SESSION_TIMEOUT);
		if (StringUtils.isNotBlank(timeout))
		{
			try
			{
				final int timeoutInt = Integer.parseInt(timeout);
				if (timeoutInt > 0)
				{
					return timeoutInt;
				}
			}
			catch (final NumberFormatException e)
			{
				LOG.warn(String.format("Timeout value from config file - [%s] can not be cast to integer.", timeout));
			}
		}
		LOG.warn(String.format("Bad or missing property [%s]. Using [%s] as default value.",
				AssistedservicefacadesConstants.ASM_AGENT_SESSION_TIMEOUT, String.valueOf(DEFAULT_SESSION_TIMEOUT)));
		return DEFAULT_SESSION_TIMEOUT;
	}

	@Override
	public int getAssistedServiceSessionTimerValue()
	{
		final String timer = Config.getParameter(AssistedservicefacadesConstants.ASM_AGENT_SESSION_TIMER);
		if (StringUtils.isNotBlank(timer))
		{
			try
			{
				final int timerInt = Integer.valueOf(timer).intValue();
				if (timerInt > 0)
				{
					return timerInt;
				}
			}
			catch (final NumberFormatException e)
			{
				LOG.warn(String.format("Timer value from config file - [%s] can not be cast to integer.", timer));
			}
		}
		LOG.warn(String.format("Bad or missing property [%s]. Using [%s] as default value.",
				AssistedservicefacadesConstants.ASM_AGENT_SESSION_TIMER, String.valueOf(DEFAULT_SESSION_TIMER)));
		return DEFAULT_SESSION_TIMER;
	}

	@Override
	public List<CustomerModel> getSuggestedCustomers(final String username) throws AssistedServiceFacadeException
	{
		validateSession();

		final StringBuffer buf = new StringBuffer();

		// get suggested customers using wildcard search by uid and name
		buf.append("SELECT DISTINCT {p:" + CustomerModel.PK + "} ");
		buf.append("FROM {" + CustomerModel._TYPECODE + " AS p ");
		buf.append("JOIN " + PrincipalGroupModel._PRINCIPALGROUPRELATION + " AS pgr ");
		buf.append("ON {p:pk} = {pgr:source} ");
		buf.append("JOIN " + PrincipalGroupModel._TYPECODE + " AS pg ");
		buf.append("ON {pgr:target} = {pg:pk} } ");
		buf.append("WHERE NOT {" + CustomerModel.UID + "}='anonymous' ");
		buf.append("AND (LOWER({p:" + CustomerModel.UID + "}) LIKE CONCAT(?username, '%') ");
		buf.append("OR LOWER({p:name}) LIKE CONCAT('%', CONCAT(?username, '%'))) ");
		buf.append("AND {pg.uid} <> '" + AssistedservicefacadesConstants.AS_AGENT_GROUP_UID + "'");

		final FlexibleSearchQuery query = new FlexibleSearchQuery(buf.toString());
		query.addQueryParameter("username", username.toLowerCase());
		final List<CustomerModel> suggestedUsers = new ArrayList<CustomerModel>(getFlexibleSearchService().<CustomerModel> search(
				query).getResult());
		Collections.sort(suggestedUsers, Comparator.comparing(CustomerModel::getName).thenComparing(CustomerModel::getUid));
		return suggestedUsers;
	}

	@Override
	public void emulateAfterLogin() throws AssistedServiceFacadeException
	{
		// try to emulate using previous emulation call parameters
		final CustomerEmulationParams savedEmulationParams = getAsmSession().getSavedEmulationData();

		final CartModel cart = getCartService().getSessionCart();
		final UserModel savedUser = getSessionService().getCurrentSession().getAttribute(AS_SAVED_ON_AGENTLOGIN_CUSTOMER);

		// in case we saved params after Deep Link were called without logging in first
		if (savedEmulationParams != null)
		{
			restoreCartToUser(cart, savedUser);
			emulateCustomer(savedEmulationParams.getUserId(), savedEmulationParams.getCartId());
			getAsmSession().setSavedEmulationData(null);
			return;
		}

		// in case there were a user before we signed in
		if (savedUser != null)
		{
			getSessionService().getCurrentSession().removeAttribute(AS_SAVED_ON_AGENTLOGIN_CUSTOMER);
			if (CollectionUtils.isNotEmpty(cart.getEntries()))
			{
				restoreCartToUser(cart, savedUser);
				if (!getUserService().isAnonymousUser(savedUser))
				{
					emulateCustomer(savedUser.getUid(), cart.getCode());
				}
				else
				{
					emulateCustomer(null, cart.getCode());
				}
			}
			else
			{
				if (!getUserService().isAnonymousUser(savedUser))
				{
					emulateCustomer(savedUser.getUid(), null);
				}
			}

			if (isAssistedServiceAgent(getUserService().getCurrentUser()))
			{
				getUserService().setCurrentUser(getUserService().getAnonymousUser());
			}
		}
		else
		{
			getUserService().setCurrentUser(getUserService().getAnonymousUser());
		}
	}

	@Override
	public void bindCustomerToCart(final String customerId, final String cartId) throws AssistedServiceFacadeException
	{
		validateSession();
		try
		{
			final UserModel customer = customerId == null ? getUserService().getCurrentUser() : getUserService().getUserForUID(
					customerId);
			final CartModel cart = cartId == null ? getCartService().getSessionCart() : getCartByCode(cartId, getUserService()
					.getAnonymousUser());
			if (cart == null || !getUserService().isAnonymousUser(cart.getUser()))
			{
				throw new AssistedServiceCartBindException(Localization.getLocalizedString("asm.bindCart.error.not_anonymous_cart"));
			}
			getUserService().setCurrentUser(customer);
			getCartService().setSessionCart(cart);
			getCartService().changeCurrentCartUser(customer);
			getAsmSession().setEmulatedCustomer(customer);
		}
		catch (final UnknownIdentifierException e)
		{
			throw new AssistedServiceCartBindException(Localization.getLocalizedString("asm.bindCart.error.unknown_cart_id"), e);
		}
	}

	/**
	 * Checks current session for ASM requirements
	 *
	 * @throws AssistedServiceFacadeException
	 *            in case ASM not launched or AS agent isn't logged in
	 */
	protected void validateSession() throws AssistedServiceFacadeException
	{
		verifyAssistedServiceMode();
		if (!isAssistedServiceAgentLoggedIn())
		{
			throw new AssistedServiceAgentNotLoggedInException("Assisted Service Agent is not logged in.");
		}
	}

	/**
	 * Checks current session for ASM mode launched.
	 *
	 * @throws AssistedServiceFacadeException
	 *            in case it's not
	 */
	protected void verifyAssistedServiceMode() throws AssistedServiceFacadeException
	{
		if (!isAssistedServiceModeLaunched())
		{
			throw new AssistedServiceFacadeException("Assisted Service mode inactive.");
		}
	}

	/**
	 * Verify whether AS agent can be logged in via https login form.
	 *
	 * @throws AssistedServiceFacadeException
	 *            in case it can't
	 */
	protected void verifyFormLoginAbility() throws AssistedServiceFacadeException
	{
		if (!Config.getBoolean(AssistedservicefacadesConstants.AS_ENABLE_FORM_LOGIN, true))
		{
			throw new AssistedServiceFacadeException("asm.login.disabled");
		}
	}

	/**
	 * Verify that provided user participate in a parent AS agent group.
	 *
	 * @param asmAgent
	 * @throws AssistedServiceFacadeException
	 *            in case he's not
	 */
	protected void verifyAssistedServiceAgent(final UserModel asmAgent) throws AssistedServiceFacadeException
	{
		if (!isAssistedServiceAgent(asmAgent))
		{
			throw new AssistedServiceFacadeException(String.format("User does not belong to the correct user group - [%s].",
					AssistedservicefacadesConstants.AS_AGENT_GROUP_UID));
		}
	}

	/**
	 * Verify that provided user participate in a parent AS agent group.
	 *
	 * @param asmAgent
	 * @return true when he is
	 */
	protected boolean isAssistedServiceAgent(final UserModel asmAgent)
	{
		final Set<UserGroupModel> userGroups = getUserService().getAllUserGroupsForUser(asmAgent);
		for (final UserGroupModel userGroup : userGroups)
		{
			if (AssistedservicefacadesConstants.AS_AGENT_GROUP_UID.equals(userGroup.getUid()))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isAssistedServiceAgentLoggedIn()
	{
		return getAsmSession() != null && getAsmSession().getAgent() != null;
	}

	@Override
	public CustomerModel createNewCustomer(final String customerId, final String customerName)
			throws AssistedServiceFacadeException
	{
		final boolean createDisabled = BooleanUtils.toBoolean(Config
				.getParameter(AssistedservicefacadesConstants.CREATE_DISABLED_PROPERTY));
		if (createDisabled)
		{
			throw new AssistedServiceFacadeException("Customer creation not enabled.");
		}
		final CustomerModel customerModel = getModelService().create(CustomerModel.class);
		try
		{

			customerModel.setName(customerName.trim());
			customerModel.setUid(customerId);
			customerModel.setLoginDisabled(false);
			customerModel.setLdapaccount(Boolean.FALSE);
			customerModel.setSessionCurrency(getCommonI18NService().getCurrentCurrency());
			customerModel.setSessionLanguage(getCommonI18NService().getCurrentLanguage());
			getCustomerAccountService().register(customerModel, null);
			LOG.info(String.format("New customer has been created via ASM: uid [%s]", customerId));
		}
		catch (final DuplicateUidException ex)
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug(String.format("Trying to create user with already existed uid [%s]", customerId));
			}
			throw new AssistedServiceFacadeException("Duplicate User id", ex);
		}
		return customerModel;
	}

	@Override
	public AssistedServiceSession getAsmSession()
	{
		return getSessionService().getAttribute(AssistedservicefacadesConstants.ASM_SESSION_PARAMETER);
	}

	/**
	 * Restore cart to provided user
	 *
	 * @param cart
	 * @param user
	 */
	protected void restoreCartToUser(final CartModel cart, final UserModel user)
	{
		if (user != null)
		{
			if (CollectionUtils.isNotEmpty(cart.getEntries()))
			{
				getCartService().changeCurrentCartUser(user);
				// refresh persistence context after cart user manipulations
				// without this step - customer will not have modified cart
				getModelService().refresh(user);
			}
		}
	}

	protected void cleanAsmSession()
	{
		getSessionService().setAttribute(AssistedservicefacadesConstants.ASM_SESSION_PARAMETER, new AssistedServiceSession());
	}

	protected void quitAsmSession()
	{
		getSessionService().removeAttribute(AssistedservicefacadesConstants.ASM_SESSION_PARAMETER);
	}

	private boolean isBruteForce(final UserModel asmAgent)
	{
		final int badAttemptNumber = bruteForceAttackCache.get(asmAgent.getUid()) != null ? bruteForceAttackCache.get(
				asmAgent.getUid()).intValue() + 1 : 1;
		int maxBadAttempts;
		try
		{
			maxBadAttempts = Config.getInt(AssistedservicefacadesConstants.AS_BAD_ATTEMPTS_BEFORE_DISABLE, DEFAULT_MAX_BAD_ATTEMPTS);
		}
		catch (final NumberFormatException e)
		{
			LOG.warn(String.format("Bad or missing property [%s] value, using [" + DEFAULT_MAX_BAD_ATTEMPTS + "]",
					AssistedservicefacadesConstants.AS_BAD_ATTEMPTS_BEFORE_DISABLE));
			maxBadAttempts = DEFAULT_MAX_BAD_ATTEMPTS;
		}

		if (badAttemptNumber >= maxBadAttempts)
		{
			LOG.info(String.format("Wrong password has been provided for agent [%s]. Attempt #%d. Agent has been blocked.",
					asmAgent.getUid(), Integer.valueOf(badAttemptNumber)));
			return true;
		}
		else
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug(String.format("Wrong password has been provided for agent [%s]. Attempt #%d", asmAgent.getUid(),
						Integer.valueOf(badAttemptNumber)));
			}
			bruteForceAttackCache.put(asmAgent.getUid(), Integer.valueOf(badAttemptNumber));
		}
		return false;
	}

	private void resetBruteForceCounter(final UserModel asmAgent)
	{
		bruteForceAttackCache.remove(asmAgent.getUid());
	}

	/**
	 * @return the customerAccountService
	 */
	public CustomerAccountService getCustomerAccountService()
	{
		return customerAccountService;
	}

	/**
	 * @param customerAccountService
	 *           the customerAccountService to set
	 */
	public void setCustomerAccountService(final CustomerAccountService customerAccountService)
	{
		this.customerAccountService = customerAccountService;
	}

	@Override
	public Collection<CartModel> getCartsForCustomer(final CustomerModel customer)
	{
		final BaseSiteModel paramBaseSiteModel = getBaseSiteService().getCurrentBaseSite();
		return getCommerceCartService().getCartsForSiteAndUser(paramBaseSiteModel, customer);
	}

	protected CommerceCartService getCommerceCartService()
	{
		return commerceCartService;
	}

	@Required
	public void setCommerceCartService(final CommerceCartService commerceCartService)
	{
		this.commerceCartService = commerceCartService;
	}

	protected BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	@Required
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
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
	 * Returns true when provided cart relates to current base site
	 *
	 * @param cart
	 */
	protected boolean isCartMatchBaseSite(final CartModel cart)
	{
		return cart.getSite() != null && getBaseSiteService().getCurrentBaseSite().getUid().equals(cart.getSite().getUid());
	}

	protected CartService getCartService()
	{
		return cartService;
	}

	@Required
	public void setCartService(final CartService cartService)
	{
		this.cartService = cartService;
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

	protected UserService getUserService()
	{
		return userService;
	}

	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
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

	protected PasswordEncoderService getPasswordEncoderService()
	{
		return passwordEncoderService;
	}

	@Required
	public void setPasswordEncoderService(final PasswordEncoderService passwordEncoderService)
	{
		this.passwordEncoderService = passwordEncoderService;
	}

	/**
	 * @return the flexibleSearchService
	 */
	protected FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}


	/**
	 * @param flexibleSearchService
	 *           the flexibleSearchService to set
	 */
	@Required
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}

	/**
	 * @return the modelService
	 */
	protected ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 */
	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}
}
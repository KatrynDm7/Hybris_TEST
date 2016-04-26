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
package de.hybris.platform.assistedservicestorefront.controllers.cms;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.AbstractController;
import de.hybris.platform.acceleratorstorefrontcommons.util.XSSFilterUtil;
import de.hybris.platform.assistedservicefacades.AssistedServiceFacade;
import de.hybris.platform.assistedservicefacades.constants.AssistedservicefacadesConstants;
import de.hybris.platform.assistedservicefacades.exception.AssistedServiceFacadeException;
import de.hybris.platform.assistedservicestorefront.redirect.AssistedServiceRedirectStrategy;
import de.hybris.platform.assistedservicestorefront.security.impl.AssistedServiceAgentLoginStrategy;
import de.hybris.platform.assistedservicestorefront.security.impl.AssistedServiceAuthenticationToken;
import de.hybris.platform.assistedservicestorefront.util.SubscriptionFacadeReflectionWrapper;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping(value = "/assisted-service")
public class AssistedServiceComponentController extends AbstractController
{
	private static final String ASSISTED_SERVICE_COMPONENT = "addon:/assistedservicestorefront/cms/asm/assistedServiceComponent";

	private static final String ASM_MESSAGE_ATTRIBUTE = "asm_message";
	private static final String ASM_REDIRECT_URL_ATTRIBUTE = "redirect_url";
	private static final String ASM_ALERT_CLASS = "asm_alert_class";

	private static final Logger LOG = Logger.getLogger(AssistedServiceComponentController.class);

	@Resource(name = "assistedServiceFacade")
	private AssistedServiceFacade assistedServiceFacade;

	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "cartService")
	private CartService cartService;

	@Resource(name = "assistedServiceAgentLoginStrategy")
	private AssistedServiceAgentLoginStrategy assistedServiceAgentLoginStrategy;

	@Resource(name = "sessionService")
	private SessionService sessionService;

	@Resource(name = "subscriptionFacadeWrapper")
	private SubscriptionFacadeReflectionWrapper subscriptionFacadeWrapper;

	@Resource(name = "assistedServiceRedirectStrategy")
	private AssistedServiceRedirectStrategy assistedServiceRedirectStrategy;

	@RequestMapping(value = "/quit", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void quitAssistedServiceMode()
	{
		assistedServiceFacade.quitAssistedServiceMode();
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String loginAssistedServiceAgent(final Model model, final HttpServletRequest request,
			final HttpServletResponse response, @RequestParam("username") final String username,
			@RequestParam("password") final String password)
	{
		try
		{
			assistedServiceFacade.loginAssistedServiceAgent(username, password);
			assistedServiceAgentLoginStrategy.login(username, request, response);
			assistedServiceFacade.emulateAfterLogin();
			refreshSpringSecurityToken();
			setSessionTimeout();
			model.addAttribute(ASM_REDIRECT_URL_ATTRIBUTE, assistedServiceRedirectStrategy.getRedirectPath());
		}
		catch (final AssistedServiceFacadeException e)
		{
			model.addAttribute(ASM_MESSAGE_ATTRIBUTE, e.getMessageCode());
			model.addAttribute(ASM_ALERT_CLASS, e.getAlertClass());
			model.addAttribute("username", XSSFilterUtil.filter(username));
		}
		model.addAllAttributes(assistedServiceFacade.getAssistedServiceSessionAttributes());
		return ASSISTED_SERVICE_COMPONENT;
	}

	@RequestMapping(value = "/logoutasm", method = RequestMethod.POST)
	public String logoutAssistedServiceAgent(final Model model)
	{
		try
		{
			assistedServiceFacade.logoutAssistedServiceAgent();
		}
		catch (final AssistedServiceFacadeException e)
		{
			model.addAttribute(ASM_MESSAGE_ATTRIBUTE, e.getMessage());
		}
		model.addAllAttributes(assistedServiceFacade.getAssistedServiceSessionAttributes());
		model.addAttribute("customerReload", "reload");
		return ASSISTED_SERVICE_COMPONENT;
	}

	@RequestMapping(value = "/personify-customer", method = RequestMethod.POST)
	public String emulateCustomer(final Model model, @RequestParam("customerId") final String customerId,
			@RequestParam("customerName") final String customerName, @RequestParam("cartId") final String cartId)
	{
		try
		{
			assistedServiceFacade.emulateCustomer(customerId, cartId);
			refreshSpringSecurityToken();
			model.addAttribute(ASM_REDIRECT_URL_ATTRIBUTE, assistedServiceRedirectStrategy.getRedirectPath());
		}
		catch (final AssistedServiceFacadeException e)
		{
			model.addAttribute(ASM_MESSAGE_ATTRIBUTE, e.getMessageCode());
			model.addAttribute(ASM_ALERT_CLASS, e.getAlertClass());
			model.addAttribute("customerId", XSSFilterUtil.filter(customerId));
			model.addAttribute("cartId", XSSFilterUtil.filter(cartId));
			model.addAttribute("customerName", XSSFilterUtil.filter(customerName));
		}
		model.addAllAttributes(assistedServiceFacade.getAssistedServiceSessionAttributes());
		return ASSISTED_SERVICE_COMPONENT;
	}

	@RequestMapping(value = "/emulate", method = RequestMethod.GET)
	public String emulateCustomerByLink(final RedirectAttributes redirectAttrs,
			@RequestParam(value = "customerId", required = false) final String customerId,
			@RequestParam(value = "cartId", required = false) final String cartId)
	{
		try
		{
			// launch AS mode in case it has not been launched yet
			if (!assistedServiceFacade.isAssistedServiceModeLaunched())
			{
				LOG.debug("ASM launched after link-emulate request");
				assistedServiceFacade.launchAssistedServiceMode();
			}

			if (assistedServiceFacade.isAssistedServiceAgentLoggedIn())
			{
				assistedServiceFacade.stopEmulateCustomer();
				refreshSpringSecurityToken();
				LOG.debug(String.format("Previous emulation for customerId:[%s] has been stopped.", userService.getCurrentUser()
						.getUid()));
			}

			assistedServiceFacade.emulateCustomer(customerId, cartId);
			LOG.debug(String.format(
					"Link-emulate request successfuly started an emulation with parameters: customerId:[%s], cartId:[%s]", customerId,
					cartId));
			refreshSpringSecurityToken();
			return REDIRECT_PREFIX + assistedServiceRedirectStrategy.getRedirectPath();
		}
		catch (final AssistedServiceFacadeException e)
		{
			LOG.debug(e.getMessage(), e);
			redirectAttrs.addFlashAttribute(ASM_MESSAGE_ATTRIBUTE, e.getMessageCode());
			redirectAttrs.addFlashAttribute(ASM_ALERT_CLASS, e.getAlertClass());
			redirectAttrs.addFlashAttribute("customerId", XSSFilterUtil.filter(customerId));
			redirectAttrs.addFlashAttribute("customerName", XSSFilterUtil.filter(customerId));
			redirectAttrs.addFlashAttribute("cartId", XSSFilterUtil.filter(cartId));
		}
		return REDIRECT_PREFIX + assistedServiceRedirectStrategy.getErrorRedirectPath();
	}

	@RequestMapping(value = "/create-account", method = RequestMethod.POST)
	public String createCustomer(final Model model, @RequestParam("customerId") final String customerId,
			@RequestParam("customerName") final String customerName)
	{
		String redirectTo = ASSISTED_SERVICE_COMPONENT;
		try
		{
			assistedServiceFacade.createNewCustomer(customerId, customerName);
			// customerId is lower cased when user registered using customer account service
			final String customerIdLowerCased = customerId.toLowerCase();
			final CartModel sessionCart = cartService.getSessionCart();
			if (sessionCart != null && userService.isAnonymousUser(sessionCart.getUser())
					&& CollectionUtils.isNotEmpty(sessionCart.getEntries()))
			{
				// if there's already an anonymous cart with entries - bind it to customer
				bindCart(customerIdLowerCased, null, model);
				redirectTo = emulateCustomer(model, customerIdLowerCased, null, cartService.getSessionCart().getCode());
			}
			else
			{
				redirectTo = emulateCustomer(model, customerIdLowerCased, null, null);
			}
			try
			{
				subscriptionFacadeWrapper.updateProfile(new HashMap<String, String>());
			}
			catch (final Exception ex)
			{
				LOG.error("Subscription profile updating failed", ex);
				throw new AssistedServiceFacadeException("Subscription profile updating failed", ex);
			}
		}
		catch (final AssistedServiceFacadeException e)
		{
			if (e.getMessage() != null && e.getMessage().toLowerCase().contains("duplicate"))
			{
				model.addAttribute(ASM_MESSAGE_ATTRIBUTE, "asm.createCustomer.duplicate.error");
				model.addAttribute(ASM_ALERT_CLASS, "ASM_alert_customer ASM_alert_create_new");
			}
			else
			{
				model.addAttribute(ASM_MESSAGE_ATTRIBUTE, "asm.createCustomer.error");
			}
			model.addAttribute("customerId", XSSFilterUtil.filter(customerId));
			model.addAttribute("customerName", XSSFilterUtil.filter(customerName + ", " + customerId));
		}
		model.addAllAttributes(assistedServiceFacade.getAssistedServiceSessionAttributes());
		return redirectTo;
	}

	@RequestMapping(value = "/personify-stop", method = RequestMethod.POST)
	public String endEmulateCustomer(final Model model)
	{
		assistedServiceFacade.stopEmulateCustomer();
		refreshSpringSecurityToken();
		model.addAllAttributes(assistedServiceFacade.getAssistedServiceSessionAttributes());
		model.addAttribute(ASM_REDIRECT_URL_ATTRIBUTE, "/");
		return ASSISTED_SERVICE_COMPONENT;
	}

	@RequestMapping(value = "/resetSession", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void resetSession()
	{
		return;
	}

	@RequestMapping(value = "/autocomplete", method = RequestMethod.GET)
	@ResponseBody
	public String autocomplete(@RequestParam("customerId") final String customerId, @RequestParam("callback") final String callback)
	{
		final StringBuilder autocompleteResult = new StringBuilder();
		try
		{
			final List<CustomerModel> customers = assistedServiceFacade.getSuggestedCustomers(customerId);

			// because of jsonp callback parameter - I have to construct JSON manually (
			autocompleteResult.append(XSSFilterUtil.filter(callback)).append("([");
			if (CollectionUtils.isNotEmpty(customers))
			{
				for (final CustomerModel customer : customers)
				{
					autocompleteResult.append(getCustomerJSON(customer));
					if (CollectionUtils.isNotEmpty(customer.getCarts()))
					{
						autocompleteResult.append(", carts:[");
						final Collection<CartModel> cartsForCustomer = assistedServiceFacade.getCartsForCustomer(customer);
						for (final CartModel cart : cartsForCustomer)
						{
							cart.getCreationtime();
							autocompleteResult.append("\"").append(cart.getCode()).append("\",");
						}
						autocompleteResult.deleteCharAt(autocompleteResult.length() - 1);
						autocompleteResult.append("]");
					}
					autocompleteResult.append("},");
				}
				autocompleteResult.deleteCharAt(autocompleteResult.length() - 1);
			}
			else
			{
				autocompleteResult.append("{label:\"No results found\", value: \"\" }");
			}
			autocompleteResult.append("])");
		}
		catch (final AssistedServiceFacadeException e)
		{
			// just do nothing and return empty string
		}
		return autocompleteResult.toString();
	}

	private String getCustomerJSON(final CustomerModel customer)
	{
		final String cardNumber = customer.getDefaultPaymentInfo() instanceof CreditCardPaymentInfoModel ? ((CreditCardPaymentInfoModel) customer
				.getDefaultPaymentInfo()).getNumber() : null;
		final String last4Digits = cardNumber == null ? "----" : cardNumber.subSequence(
				cardNumber.length() >= 4 ? cardNumber.length() - 4 : 0, cardNumber.length()).toString();
		final String formattedCreationDate = customer.getCreationtime() != null ? new SimpleDateFormat("dd/MM/YYYY")
				.format(customer.getCreationtime()) : "--/--/----";
		return String.format("{email:'%s',value:'%s',date:'%s',card:'%s'", XSSFilterUtil.filter(customer.getUid()),
				XSSFilterUtil.filter(customer.getName()), formattedCreationDate, last4Digits);
	}

	@RequestMapping(value = "/bind-cart", method = RequestMethod.POST)
	public String bindCart(@RequestParam(value = "customerId", required = false) final String customerId,
			@RequestParam(value = "cartId", required = false) final String cartId, final Model model)
	{
		try
		{
			assistedServiceFacade.bindCustomerToCart(customerId, cartId);
			refreshSpringSecurityToken();
			model.addAttribute(ASM_REDIRECT_URL_ATTRIBUTE, "/");
		}
		catch (final AssistedServiceFacadeException e)
		{
			model.addAttribute(ASM_MESSAGE_ATTRIBUTE, e.getMessage());
			model.addAttribute("customerId", XSSFilterUtil.filter(customerId));
		}
		model.addAllAttributes(assistedServiceFacade.getAssistedServiceSessionAttributes());
		return ASSISTED_SERVICE_COMPONENT;
	}

	@RequestMapping(value = "/add-to-cart", method = RequestMethod.POST)
	public String addToCartEventHandler(final Model model)
	{
		try
		{
			// since cart isn't empty anymore - emulate mode should be on
			assistedServiceFacade.emulateCustomer(userService.getCurrentUser().getUid(), cartService.getSessionCart().getCode());
		}
		catch (final AssistedServiceFacadeException e)
		{
			return null; // there will be 'page not found' response in case of exception
		}
		return refresh(model);
	}

	@RequestMapping(value = "/refresh", method = RequestMethod.POST)
	public String refresh(final Model model)
	{
		model.addAllAttributes(assistedServiceFacade.getAssistedServiceSessionAttributes());
		return ASSISTED_SERVICE_COMPONENT;
	}


	private void setSessionTimeout()
	{
		JaloSession.getCurrentSession().setTimeout(assistedServiceFacade.getAssistedServiceSessionTimeout());
	}

	/**
	 * This method should be called after any facade method where user substitution may occur
	 */
	private void refreshSpringSecurityToken()
	{
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AssistedServiceAuthenticationToken)
		{
			final UserModel currentUser = userService.getCurrentUser();
			if (currentUser == null || userService.isAnonymousUser(currentUser) || isASAgent(currentUser))
			{
				((AssistedServiceAuthenticationToken) authentication).setEmulating(false);
			}
			else
			{
				((AssistedServiceAuthenticationToken) authentication).setEmulating(true);
			}
		}
	}

	private boolean isASAgent(final UserModel currentUser)
	{
		final Set<UserGroupModel> userGroups = userService.getAllUserGroupsForUser(currentUser);
		for (final UserGroupModel userGroup : userGroups)
		{
			if (AssistedservicefacadesConstants.AS_AGENT_GROUP_UID.equals(userGroup.getUid()))
			{
				return true;
			}
		}
		return false;
	}
}
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
package de.hybris.platform.storefront.controllers.pages.checkout.steps;

import de.hybris.platform.acceleratorstorefrontcommons.annotations.PreValidateCheckoutStep;
import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.checkout.steps.CheckoutStep;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessage;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commerceservices.order.CommerceSaveCartException;
import de.hybris.platform.financialacceleratorstorefront.checkout.step.InsuranceCheckoutStep;
import de.hybris.platform.financialfacades.facades.InsuranceCartFacade;
import de.hybris.platform.storefront.checkout.steps.FormCheckoutStep;
import de.hybris.platform.storefront.controllers.ControllerConstants;
import de.hybris.platform.storefront.yforms.EmbeddedFormXml;
import de.hybris.platform.xyformsfacades.form.YFormFacade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


/**
 * * FormCheckoutStepController for capture the information required by xform.
 */
@Controller
@RequestMapping(value = "/checkout/multi/form")
public class FormCheckoutStepController extends AbstractInsuranceCheckoutStepController
{
	/**
	 *
	 */
	protected static final String EMBEDDED_FORM_HTMLS = "embeddedFormHtmls";

	protected static final String FORM = "form";

	@Resource
	private YFormFacade yFormFacade;

	@Resource(name = "cartFacade")
	private InsuranceCartFacade cartFacade;


	@Override
	@RequestMapping(method = RequestMethod.GET, value = "/b2c")
	@RequireHardLogIn
	@PreValidateCheckoutStep(checkoutStep = FORM)
	public String enterStep(final Model model, final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException
	{

		getCheckoutFacade().setDeliveryModeIfAvailable();
		setupDeliveryAddress();

		initialSetup(model);

		return ControllerConstants.Views.Pages.MultiStepCheckout.GetFormPage;
	}

	@RequestMapping(method = RequestMethod.GET)
	@RequireHardLogIn
	@PreValidateCheckoutStep(checkoutStep = FORM)
	public String enterStep(final Model model, final RedirectAttributes redirectAttributes, final HttpServletRequest request)
			throws CMSItemNotFoundException
	{
		populateGlobalMessages(model, redirectAttributes);

		// Setup sample delivery mode and delivery address.
		getCheckoutFacade().setDeliveryModeIfAvailable();
		setupDeliveryAddress();

		initialSetup(model);

		final CartData cartData = getCheckoutFacade().getCheckoutCart();
		model.addAttribute("cartData", cartData);

		try
		{
			getCartFacade().saveCurrentUserCart();
		}
		catch (final CommerceSaveCartException e)
		{
			LOG.error("Save cart error.");
			e.printStackTrace();
		}

		return setupNavigation(request);
	}

	protected void populateGlobalMessages(final Model model, final RedirectAttributes redirectAttributes)
	{
		populateGlobalMessage(model, redirectAttributes, GlobalMessages.INFO_MESSAGES_HOLDER);
		populateGlobalMessage(model, redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER);
		populateGlobalMessage(model, redirectAttributes, GlobalMessages.CONF_MESSAGES_HOLDER);
	}

	protected void populateGlobalMessage(final Model model, final RedirectAttributes redirectAttributes,
			final String messageHolderType)
	{
		if (model != null && redirectAttributes != null && model.containsAttribute(messageHolderType))
		{
			final Object obj = model.asMap().get(messageHolderType);
			if (obj instanceof List && CollectionUtils.isNotEmpty((List) obj))
			{
				final Object o = ((List) obj).get(0);
				if (o instanceof GlobalMessage)
				{
					if (((GlobalMessage) o).getCode() != null)
					{
						final Collection attributes = ((GlobalMessage) o).getAttributes();
						GlobalMessages.addFlashMessage(redirectAttributes, messageHolderType, ((GlobalMessage) o).getCode(),
								attributes != null ? attributes.toArray() : Collections.emptyList().toArray());
					}
				}
			}
		}
	}


	/**
	 * Enter step of the form with defined form title.
	 */
	@RequestMapping(value = "/{formPageId}", method = RequestMethod.GET)
	@RequireHardLogIn
	public String enterStepForForm(final Model model, @PathVariable final String formPageId,
			final RedirectAttributes redirectAttributes, final HttpServletRequest request) throws CMSItemNotFoundException
	{
		if (!getCheckoutFlowFacade().hasValidCart())
		{
			LOG.info("Missing, empty or unsupported cart");
			return REDIRECT_URL_CART;
		}

		initialSetup(model);

		final List<InsuranceCheckoutStep> formCheckoutSteps = getDynamicFormCheckoutStrategy().createDynamicFormSteps(
				getFormPlaceholder());
		CheckoutStep currentStep = null;
		for (final CheckoutStep step : formCheckoutSteps)
		{
			if (step instanceof FormCheckoutStep)
			{
				if (((FormCheckoutStep) step).getFormPageId().equals(formPageId))
				{
					currentStep = step;
					break;
				}
			}
		}
		if (currentStep != null)
		{
			model.addAttribute(WebConstants.BREADCRUMBS_KEY,
					getResourceBreadcrumbBuilder().getBreadcrumbs("checkout.multi." + currentStep.getProgressBarId() + ".breadcrumb"));
			setupFormsToModel(model, formPageId);
			setCheckoutStepLinksForModel(model, currentStep);

			// The cartData must be added into model after the formCheckout steps setup.
			// This is important the formData for the cart entry data populated correctly.
			final CartData cartData = getCheckoutFacade().getCheckoutCart();
			model.addAttribute("cartData", cartData);
		}

		final CategoryData categoryData = cartFacade.getSelectedInsuranceCategory();
		if (categoryData != null)
		{
			model.addAttribute("categoryName", categoryData.getName());
		}

		return ControllerConstants.Views.Pages.MultiStepCheckout.GetFormPage;
	}

	/**
	 * The original FormCheckoutStep is the placeholder for placing in dynamic form steps. <br/>
	 * If there are no forms in the current cart content, the form step will be skipped by redirect to defined PREVIOUS
	 * step if it is accessed from the NEXT step; and it will skipped to NEXT step if it is access from any other pages.
	 * If there are forms, the first and the last form will be setup with correct navigation checkout steps; And it will
	 * navigate to the first step or the last step of the form depends on the entering page.
	 */
	protected String setupNavigation(final HttpServletRequest request)
	{
		List<InsuranceCheckoutStep> dynamicFormProgressSteps = new ArrayList<InsuranceCheckoutStep>();
		dynamicFormProgressSteps = getDynamicFormCheckoutStrategy().createDynamicFormSteps(getFormPlaceholder());

		final String refererURL = request.getHeader("referer");
		if (refererURL.contains(StringUtils.remove(getCheckoutStep().nextStep(), "redirect:")))
		{
			// If the entrance of the page is from the NEXT checkout step - Display the last page or redirect to PREVIOUS
			if (dynamicFormProgressSteps.size() > 0)
			{
				final CheckoutStep lastStep = dynamicFormProgressSteps.get(dynamicFormProgressSteps.size() - 1);
				return lastStep.currentStep();
			}
			return getCheckoutStep().previousStep();
		}
		else
		{
			// If the entrance of the page is from the Other step - Display the first page or redirect to NEXT
			if (dynamicFormProgressSteps.size() > 0)
			{
				final CheckoutStep firstStep = dynamicFormProgressSteps.get(0);
				firstStep.getTransitions().put(CheckoutStep.PREVIOUS, getCheckoutStep().previousStep());
				return firstStep.currentStep();
			}
			else
			{
				return getCheckoutStep().nextStep();
			}
		}
	}

	@Override
	protected void setVisitedToStep(final CheckoutStep checkoutStep)
	{
		super.setVisitedToStep(checkoutStep);
		if (getFormPlaceholder() instanceof InsuranceCheckoutStep)
		{
			((InsuranceCheckoutStep) getFormPlaceholder()).setVisited(true);
		}
	}

	protected void initialSetup(final Model model) throws CMSItemNotFoundException
	{
		this.prepareDataForPage(model);
		storeCmsPageInModel(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
		model.addAttribute("metaRobots", "noindex,nofollow");
	}

	/**
	 * Setup form onto the view model from the information extracted from the product.
	 */
	protected void setupFormsToModel(final Model model, final String formPageId)
	{
		final List<String> formsHtml = getDynamicFormCheckoutStrategy().getFormsInlineHtmlByFormPageId(formPageId);
		model.addAttribute(EMBEDDED_FORM_HTMLS, formsHtml);

		/*
		 * This should really be moved into a separate handler for the yForms post-form handling..
		 */
		final List<EmbeddedFormXml> xmls = (List<EmbeddedFormXml>) getSessionService().getAttribute(
				EmbeddedFormXml.EMBEDDED_FORM_XMLS);
		model.addAttribute(EmbeddedFormXml.EMBEDDED_FORM_XMLS, xmls);
		getSessionService().removeAttribute(EmbeddedFormXml.EMBEDDED_FORM_XMLS);
	}

	@RequestMapping(value = "/back", method = RequestMethod.GET)
	@RequireHardLogIn
	@Override
	public String back(final RedirectAttributes redirectAttributes)
	{
		return getCheckoutStep().previousStep();
	}

	@RequestMapping(value = "/next", method = RequestMethod.GET)
	@RequireHardLogIn
	@Override
	public String next(final RedirectAttributes redirectAttributes)
	{
		return getCheckoutStep().nextStep();
	}

	@Override
	protected CheckoutStep getCheckoutStep()
	{
		return getCheckoutStep(FORM);
	}

	protected YFormFacade getYFormFacade()
	{
		return yFormFacade;
	}

	public void setYFormFacade(final YFormFacade yFormFacade)
	{
		this.yFormFacade = yFormFacade;
	}

}

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
package de.hybris.platform.storefront.checkout.steps.strategy.impl;

import de.hybris.platform.acceleratorfacades.order.AcceleratorCheckoutFacade;
import de.hybris.platform.acceleratorstorefrontcommons.checkout.steps.CheckoutStep;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.financialacceleratorstorefront.checkout.step.InsuranceCheckoutStep;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.storefront.checkout.steps.FormCheckoutStep;
import de.hybris.platform.storefront.checkout.steps.strategy.CheckoutFormsStrategy;
import de.hybris.platform.storefront.checkout.steps.strategy.DynamicFormCheckoutStrategy;
import de.hybris.platform.storefront.checkout.strategy.impl.InsuranceYFormDataPreprocessorStrategy;
import de.hybris.platform.storefront.controllers.pages.checkout.steps.AbstractCheckoutStepController;
import de.hybris.platform.storefront.controllers.pages.checkout.steps.AbstractInsuranceCheckoutStepController;
import de.hybris.platform.storefront.form.data.FormDetailData;
import de.hybris.platform.storefront.yforms.EmbeddedFormXml;
import de.hybris.platform.storefront.yforms.EmbeddedFormXmlParser;
import de.hybris.platform.xyformsfacades.data.YFormDataData;
import de.hybris.platform.xyformsfacades.form.YFormFacade;
import de.hybris.platform.xyformsfacades.strategy.preprocessor.EmptyYFormPreprocessorStrategy;
import de.hybris.platform.xyformsfacades.strategy.preprocessor.ReferenceIdTransformerYFormPreprocessorStrategy;
import de.hybris.platform.xyformsfacades.strategy.preprocessor.YFormPreprocessorStrategy;
import de.hybris.platform.xyformsservices.enums.YFormDataActionEnum;
import de.hybris.platform.xyformsservices.exception.YFormServiceException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


/**
 * DefaultDynamicFormCheckoutStrategy. <br/>
 * This is use to define the checkout progress bar for all checkout steps, forms for display for the form page and also
 * the dynamic form pages for the given cart.
 */
public class DefaultDynamicFormCheckoutStrategy implements DynamicFormCheckoutStrategy
{
	private static final Logger LOG = Logger.getLogger(DefaultDynamicFormCheckoutStrategy.class);

	private String baseFormUrl;

	@Resource(name = "acceleratorCheckoutFacade")
	private AcceleratorCheckoutFacade checkoutFacade;

	@Resource(name = "checkoutFormsStrategy")
	private CheckoutFormsStrategy checkoutFormsStrategy;

	@Resource(name = "form2PreprocessorStrategyMap")
	private Map<String, InsuranceYFormDataPreprocessorStrategy> form2PreprocessorStrategyMap;

	@Resource
	private YFormFacade yFormFacade;

	@Resource
	private SessionService sessionService;

	@Resource
	private EmbeddedFormXmlParser embeddedFormXmlParser;

	@Resource
	private EmptyYFormPreprocessorStrategy emptyYFormPreprocessorStrategy;

	@Resource
	private InsuranceYFormDataPreprocessorStrategy insuranceReferenceIdTransformerYFormPreprocessorStrategy;


	/**
	 * Create dynamic form progress checkout steps based on the cart entries. <br/>
	 * Depends on the content of the cart, dynamically create the form steps and defined the navigation between the
	 * forms. <br/>
	 * Number of the form pages to display for checkout is defined by the CheckoutFormsStrategy, where the formPageId is
	 * also representing the progressBarId. The corresponding labels should be created base on them.
	 *
	 * @param formCheckoutStepPlaceholder
	 */
	@Override
	public List<InsuranceCheckoutStep> createDynamicFormSteps(final CheckoutStep formCheckoutStepPlaceholder)
	{
		final CartData cartData = getCheckoutFacade().getCheckoutCart();
		final List<InsuranceCheckoutStep> newSteps = new ArrayList<InsuranceCheckoutStep>();
		CheckoutStep previous = null;
		final Map<String, String> formPageIds = getCheckoutFormsStrategy().getFormPageIdList(cartData);
		for (final String formPageId : formPageIds.keySet())
		{
			final FormCheckoutStep newStep = createNewCheckoutStep(formCheckoutStepPlaceholder, previous, formPageId,
					formPageIds.get(formPageId));
			previous = newStep;
			newSteps.add(newStep);
		}
		if (!newSteps.isEmpty())
		{
			final CheckoutStep firstStep = newSteps.get(0);
			final CheckoutStep lastStep = newSteps.get(newSteps.size() - 1);
			firstStep.getTransitions().put(CheckoutStep.PREVIOUS, formCheckoutStepPlaceholder.previousStep());
			lastStep.getTransitions().put(CheckoutStep.NEXT, formCheckoutStepPlaceholder.nextStep());
		}
		return newSteps;
	}

	protected FormCheckoutStep createNewCheckoutStep(final CheckoutStep formCheckoutStepPlaceholder, final CheckoutStep previous,
			final String formPageId, final String progressBarId)
	{
		final FormCheckoutStep newStep = new FormCheckoutStep();
		newStep.setProgressBarId(progressBarId);
		newStep.setFormPageId(formPageId);
		newStep.setCheckoutGroup(formCheckoutStepPlaceholder.getCheckoutGroup());
		if (formCheckoutStepPlaceholder instanceof InsuranceCheckoutStep)
		{
			newStep.setStepVisitedAnalysisStrategyList(((InsuranceCheckoutStep) formCheckoutStepPlaceholder)
					.getStepVisitedAnalysisStrategyList());
			newStep.setCheckoutStepValidatorList(((InsuranceCheckoutStep) formCheckoutStepPlaceholder)
					.getCheckoutStepValidatorList());
			newStep.setStepEnabledAnalysisStrategyList(((InsuranceCheckoutStep) formCheckoutStepPlaceholder)
					.getStepEnabledAnalysisStrategyList());
		}
		newStep.setCheckoutStepValidator(formCheckoutStepPlaceholder.getCheckoutStepValidator());
		final Map<String, String> transactions = new HashMap<String, String>();

		transactions.put(CheckoutStep.CURRENT, getBaseFormUrl() + "/" + formPageId);
		newStep.setTransitions(transactions);
		if (previous != null)
		{
			transactions.put(CheckoutStep.PREVIOUS, previous.currentStep());
			previous.getTransitions().put(CheckoutStep.NEXT, newStep.currentStep());
		}
		return newStep;
	}

	/**
	 * Combine the Form Checkout Steps to the original Checkout steps for display the at the progress bar.
	 *
	 * @param formCheckoutStepPlaceholder
	 * @param originalCheckoutSteps
	 */
	@Override
	public List<AbstractCheckoutStepController.CheckoutSteps> combineFormCheckoutStepProgressBar(
			final CheckoutStep formCheckoutStepPlaceholder,
			final List<AbstractCheckoutStepController.CheckoutSteps> originalCheckoutSteps)
	{
		final List<AbstractCheckoutStepController.CheckoutSteps> combinedCheckoutSteps = new ArrayList<AbstractCheckoutStepController.CheckoutSteps>();

		final List<InsuranceCheckoutStep> formCheckoutSteps = createDynamicFormSteps(formCheckoutStepPlaceholder);
		int progressKey = 0;
		for (final AbstractCheckoutStepController.CheckoutSteps checkoutStep : originalCheckoutSteps)
		{
			if (formCheckoutStepPlaceholder.getProgressBarId().equals(checkoutStep.getProgressBarId()))
			{
				for (final InsuranceCheckoutStep formCheckoutStep : formCheckoutSteps)
				{
					combinedCheckoutSteps.add(new AbstractInsuranceCheckoutStepController.InsuranceCheckoutSteps(formCheckoutStep
							.getProgressBarId(), StringUtils.remove(formCheckoutStep.currentStep(), "redirect:"), progressKey,
							formCheckoutStep.getCurrentStatus(), formCheckoutStep.getIsEnabled()));
					progressKey++;
				}
			}
			else
			{
				if (checkoutStep instanceof AbstractInsuranceCheckoutStepController.InsuranceCheckoutSteps)
				{
					final AbstractInsuranceCheckoutStepController.InsuranceCheckoutSteps step = (AbstractInsuranceCheckoutStepController.InsuranceCheckoutSteps) checkoutStep;
					combinedCheckoutSteps.add(new AbstractInsuranceCheckoutStepController.InsuranceCheckoutSteps(checkoutStep
							.getProgressBarId(), step.getUrl(), progressKey, step.getStatus(), step.getIsEnabled()));
				}
				else
				{
					combinedCheckoutSteps.add(new AbstractInsuranceCheckoutStepController.InsuranceCheckoutSteps(checkoutStep
							.getProgressBarId(), checkoutStep.getUrl(), progressKey));
				}
				progressKey++;
			}
		}
		return combinedCheckoutSteps;
	}

	/**
	 * Get all the Form HTMLs to display on the given formPageId
	 *
	 * @param formPageId
	 */
	@Override
	public List<String> getFormsInlineHtmlByFormPageId(final String formPageId)
	{
		final List<String> formsHtml = new ArrayList<String>();
		final CartData cartData = getCheckoutFacade().getCheckoutCart();
		final List<FormDetailData> forms = getCheckoutFormsStrategy().getFormDetailDataByFormPageId(cartData, formPageId);
		for (final FormDetailData data : forms)
		{
			String embeddedFormHtml = "";

			// This strategy does not apply any execute any extra code.
			YFormPreprocessorStrategy strategy = emptyYFormPreprocessorStrategy;
			YFormDataActionEnum action = YFormDataActionEnum.EDIT;
			String formDataId = null;
			final Map<String, Object> params = new HashMap<String, Object>();

			YFormDataData yFormData = null;
			try
			{
				yFormData = getYFormFacade().getYFormData(data.getApplicationId(), data.getFormId(), data.getRefId());
				formDataId = yFormData.getId();
			}
			catch (final YFormServiceException e)
			{
				// This Strategy will create a new DRAFT and DATA record for the given formDataId
				strategy = insuranceReferenceIdTransformerYFormPreprocessorStrategy;
				params.put(ReferenceIdTransformerYFormPreprocessorStrategy.REFERENCE_ID, data.getRefId());
				action = YFormDataActionEnum.NEW;
				formDataId = getYFormFacade().getNewFormDataId();
			}

			try
			{
				if (form2PreprocessorStrategyMap.containsKey(data.getFormId()))
				{
					strategy = form2PreprocessorStrategyMap.get(data.getFormId());
					params.put(InsuranceYFormDataPreprocessorStrategy.FORM_DETAIL_DATA, data);
					params.put(ReferenceIdTransformerYFormPreprocessorStrategy.REFERENCE_ID, data.getRefId());
				}
				embeddedFormHtml = getYFormFacade().getInlineFormHtml(data.getApplicationId(), data.getFormId(), action, formDataId,
						strategy, params);
				yFormData = getYFormFacade().getYFormData(formDataId);
				addToEmbeddedFormXml(yFormData);
			}
			catch (final YFormServiceException e)
			{
				embeddedFormHtml = "";
				LOG.error(e.getMessage(), e);
			}
			formsHtml.add(embeddedFormHtml);
		}
		return formsHtml;
	}

	/**
	 * Note : this method is here as the 'post processing' of xforms - implmented as a read operation here. It should be
	 * moved to the generic post-proxy implementation in the YForms module asap.
	 *
	 * @param yFormData
	 */
	protected void addToEmbeddedFormXml(final YFormDataData yFormData)
	{
		List<EmbeddedFormXml> datas = (List<EmbeddedFormXml>) sessionService.getAttribute(EmbeddedFormXml.EMBEDDED_FORM_XMLS);

		if (datas == null)
		{
			datas = new ArrayList<EmbeddedFormXml>();
			sessionService.setAttribute(EmbeddedFormXml.EMBEDDED_FORM_XMLS, datas);
		}

		final EmbeddedFormXml formXml = new EmbeddedFormXml();

		formXml.setApplicationId(yFormData.getFormDefinition().getApplicationId());
		formXml.setFormId(yFormData.getFormDefinition().getFormId());
		formXml.setDataId(yFormData.getRefId());
		formXml.setDocument(embeddedFormXmlParser.parseContent(yFormData.getContent()));
		formXml.setDataType(yFormData.getType());

		datas.add(formXml);
	}

	protected AcceleratorCheckoutFacade getCheckoutFacade()
	{
		return checkoutFacade;
	}

	public void setCheckoutFacade(final AcceleratorCheckoutFacade checkoutFacade)
	{
		this.checkoutFacade = checkoutFacade;
	}

	protected String getBaseFormUrl()
	{
		return baseFormUrl;
	}

	public void setBaseFormUrl(final String baseFormUrl)
	{
		this.baseFormUrl = baseFormUrl;
	}

	protected CheckoutFormsStrategy getCheckoutFormsStrategy()
	{
		return checkoutFormsStrategy;
	}

	public void setCheckoutFormsStrategy(final CheckoutFormsStrategy checkoutFormsStrategy)
	{
		this.checkoutFormsStrategy = checkoutFormsStrategy;
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

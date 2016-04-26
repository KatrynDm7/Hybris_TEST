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
package de.hybris.platform.timedaccesspromotionsstorefront.controllers.pages;

import de.hybris.platform.acceleratorservices.config.SiteConfigService;
import de.hybris.platform.acceleratorservices.controllers.page.PageType;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.impl.ProductBreadcrumbBuilder;
import de.hybris.platform.acceleratorstorefrontcommons.forms.ReviewForm;
import de.hybris.platform.acceleratorstorefrontcommons.util.MetaSanitizerUtil;
import de.hybris.platform.catalog.enums.ProductReferenceTypeEnum;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.servicelayer.services.CMSPageService;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.ProductReferenceData;
import de.hybris.platform.commercefacades.product.data.PromotionData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.timedaccesspromotionsfacades.facades.FlashbuyPromotionFacade;
import de.hybris.platform.timedaccesspromotionsservices.constants.TimedaccesspromotionsservicesConstants;
import de.hybris.platform.timedaccesspromotionsservices.exception.MultipleEnqueueException;
import de.hybris.platform.timedaccesspromotionsservices.model.FlashbuyPromotionModel;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.context.Theme;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.support.RequestContextUtils;

import atg.taglib.json.util.JSONException;
import atg.taglib.json.util.JSONObject;


/**
 * Controller for flash buy product details page
 */
@Controller
@Scope("tenant")
@RequestMapping(value = "/**/ta")
public class FlashbuyController extends ProductPageController
{

	protected static final Logger LOG = Logger.getLogger(FlashbuyController.class);

	private static final String FLASHBUY_CMS_PAGE = "flashbuyDetail";
	private static final String PROMOTION_CODE = "PROMOTIONCODE";
	private static final String RETRIEVERESULT_INTERVAL = "flashbuygroupbuypromotions.flashbuyshowresultintervalinMills";
	private static final String RETRIEVERESULT_WAIT = "wait";
	private static final String RETRIEVERESULT_SUCCESS = "success";
	private static final String RETRIEVERESULT_REJECT = "reject";
	protected static final String PRODUCT_CODE_PATH_VARIABLE_PATTERN = "/{productCode:.*}";
	protected static final String PRODUCT_ENQUEUE = "/enqueue";
	protected static final String PRODUCT_RESULT = "/result";
	protected static final String FLASHBUY_URL = "ta";

	@Resource(name = "accProductFacade")
	private ProductFacade productFacade;

	@Resource(name = "productService")
	private ProductService productService;

	@Resource(name = "customerFacade")
	private CustomerFacade customerFacade;

	@Resource(name = "productBreadcrumbBuilder")
	private ProductBreadcrumbBuilder productBreadcrumbBuilder;

	@Resource(name = "cmsPageService")
	private CMSPageService cmsPageService;

	@Resource(name = "flashbuyPromotionFacade")
	private FlashbuyPromotionFacade flashbuyPromotionFacade;

	@Resource
	private SiteConfigService siteConfigService;

	private static final String ERROR_MSG_TYPE = "errorMsg";

	/**
	 * Data preparation and necessary check before showing up the flash buy product details page. Check wether flashbuy
	 * promotion existing for the current product, if not, redirect to normal product details page.
	 */
	@Override
	@RequestMapping(value = PRODUCT_CODE_PATH_VARIABLE_PATTERN, method = RequestMethod.GET)
	public String productDetail(@PathVariable("productCode") final String productCode, final Model model,
			final HttpServletRequest request, final HttpServletResponse response) throws CMSItemNotFoundException,
			UnsupportedEncodingException
	{

		final ProductModel productModel = productService.getProductForCode(productCode);

		updatePageTitle(productModel, model);
		populateProductDetailForDisplay(productModel, model, request);
		model.addAttribute(new ReviewForm());
		final List<ProductReferenceData> productReferences = productFacade.getProductReferencesForCode(productCode,
				Arrays.asList(ProductReferenceTypeEnum.SIMILAR, ProductReferenceTypeEnum.ACCESSORIES),
				Arrays.asList(ProductOption.BASIC, ProductOption.PRICE), null);
		model.addAttribute("productReferences", productReferences);
		model.addAttribute("pageType", PageType.PRODUCT.name());

		final ProductData productData = (ProductData) model.asMap().get("product");

		PromotionData flashBuyPromotion = null;
		for (final PromotionData promotionData : productData.getSpecialPromotions())
		{
			if (promotionData.getItemType().equals(FlashbuyPromotionModel._TYPECODE))
			{
				flashBuyPromotion = promotionData;
				break;
			}
		}

		//Redirect to Product Details page in case no valid FlashBuyPromotion
		if (null == flashBuyPromotion)
		{
			final String redirectURL = request
					.getRequestURL()
					.replace(request.getRequestURL().lastIndexOf(FLASHBUY_URL),
							request.getRequestURL().lastIndexOf(FLASHBUY_URL) + FLASHBUY_URL.length(), "p").toString();

			if (LOG.isDebugEnabled())
			{
				LOG.debug("No Flashbuy Promotion existing, redirect to product page");
				LOG.debug("Original URL:" + request.getRequestURL());
				LOG.debug("Redirect URL:" + redirectURL);
			}

			return REDIRECT_PREFIX + redirectURL;
		}

		final boolean isRemaining = flashbuyPromotionFacade.hasProductAvailable(productCode, flashBuyPromotion.getCode());

		//show run out directly since no available quantity left.
		if (!isRemaining)
		{
			model.addAttribute("soldout", true);
		}

		request.getSession().setAttribute(PROMOTION_CODE, flashBuyPromotion.getCode());

		final String metaKeywords = MetaSanitizerUtil.sanitizeKeywords(productModel.getKeywords());
		final String metaDescription = MetaSanitizerUtil.sanitizeDescription(productModel.getDescription());
		setUpMetaData(model, metaKeywords, metaDescription);

		model.addAttribute("orderCompleteStrategy",
				siteConfigService.getProperty(TimedaccesspromotionsservicesConstants.ORDER_COMPLETE_STRATEGY_KEY));
		return getViewForPage(model);
	}

	/**
	 * Replace the product page of super class with flash buy product page.
	 */
	@Override
	protected AbstractPageModel getPageForProduct(final ProductModel product) throws CMSItemNotFoundException
	{
		return cmsPageService.getPageForId(FLASHBUY_CMS_PAGE);
	}


	/**
	 * Extend the super class methed, and populate flash buy related data.
	 */
	@Override
	protected void populateProductData(final ProductData productData, final Model model)
	{

		final List<PromotionData> potentialPromotions = new ArrayList<PromotionData>();
		for (final PromotionData promotionData : productData.getSpecialPromotions())
		{
			if (promotionData.getItemType().equals(FlashbuyPromotionModel._TYPECODE))
			{
				//both specialPromotions and potentialPromotions would only contain one flash buy entry
				potentialPromotions.add(promotionData);
				productData.setSpecialPromotions(potentialPromotions);
				productData.setPotentialPromotions(potentialPromotions);
				break;
			}
		}

		super.populateProductData(productData, model);
	}


	/**
	 * Enqueue the flash buy request
	 */
	@RequestMapping(value = PRODUCT_ENQUEUE + PRODUCT_CODE_PATH_VARIABLE_PATTERN, method = RequestMethod.POST)
	@ResponseBody
	public String enqueue(@PathVariable("productCode") final String productCode, @RequestBody final String requestBody,
			final HttpServletRequest request, final HttpServletResponse response) throws JSONException
	{

		final String buyNumber = request.getParameter("count");
		final String redirectURL = request.getRequestURL().toString();
		if (null == request.getSession().getAttribute(PROMOTION_CODE))
		{
			LOG.error("The promotion code can not be null to enqueue flashbuy promotion." + "\n" + "Redirect URL:" + redirectURL);
			return REDIRECT_PREFIX + redirectURL;
		}
		if (buyNumber.isEmpty())
		{
			LOG.error("The request quantity can not be null to enqueue flashbuy promotion." + "\n" + "Redirect URL:" + redirectURL);
			return REDIRECT_PREFIX + redirectURL;
		}
		final Pattern buyNumberPattern = Pattern.compile("[1-9]+[0-9]*");
		final Matcher buyNumberMatcher = buyNumberPattern.matcher(buyNumber);

		if (!buyNumberMatcher.matches())
		{
			LOG.error("The request quantity is not vaild for the flash buy promotion." + "\n" + "Redirect URL:" + redirectURL);
			return REDIRECT_PREFIX + redirectURL;
		}

		final long buyQuantity = Integer.parseInt(buyNumber);
		final CustomerData currentCustomer = customerFacade.getCurrentCustomer();
		final String userId = currentCustomer.getUid();
		boolean enqueueSuccess = false;
		final String product_promotion_key = productCode + request.getSession().getAttribute(PROMOTION_CODE);

		// Enqueue fail because of double enqueue try
		if (request.getSession().getAttribute(product_promotion_key) != null)
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Customer '" + userId + "' was rejected for the current flash buy promotion");
			}
			return enqueueFailedHandled(productCode, request, "flashbuy.label.btn.enqueued");
		}

		if (LOG.isDebugEnabled())
		{
			LOG.debug("Customer '" + userId + "'" + " brought " + buyQuantity + " '" + productCode + "' product(s)"
					+ " with promotion " + request.getSession().getAttribute(PROMOTION_CODE).toString());
		}
		try
		{
			enqueueSuccess = flashbuyPromotionFacade.enqueue(productCode, request.getSession().getAttribute(PROMOTION_CODE)
					.toString(), userId, buyQuantity);
			request.getSession().setAttribute(product_promotion_key, "Enequed");
		}
		catch (final CommerceCartModificationException e)
		{
			LOG.error("Couldn't add flash buy product of code " + productCode + " to cart. , e/n Redirect URL: " + redirectURL);
			return REDIRECT_PREFIX + redirectURL;
		}
		catch (final MultipleEnqueueException e)
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Customer '" + userId + "' was rejected for the current flash buy promotion");
			}
			return enqueueFailedHandled(productCode, request, "flashbuy.label.btn.enqueued");
		}

		//Enqueue success
		if (enqueueSuccess)
		{
			return RETRIEVERESULT_SUCCESS;
		}
		else
		{
			// Enqueue fail because of limited total quantity
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Customer '" + userId + "' was rejected for the current flash buy promotion");
			}
			return enqueueFailedHandled(productCode, request, "flashbuy.label.btn.soldout");
		}


	}


	@ModelAttribute("showResultInterval")
	public String getShowResultInterval(final HttpServletRequest request)
	{
		String showResultInterval = getHostConfigService().getProperty(RETRIEVERESULT_INTERVAL, request.getServerName());
		if (StringUtils.isEmpty(showResultInterval))
		{
			LOG.warn("No flash buy retrieve result interval configured for server: " + request.getServerName()
					+ ". Default value is 5 seconds.");
			showResultInterval = "5000";
		}
		return showResultInterval;
	}

	protected String enqueueFailedHandled(final String productCode, final HttpServletRequest request,
			final String flashbuyButtonLabel) throws JSONException
	{
		final Theme theme = RequestContextUtils.getTheme(request);
		final String message = theme.getMessageSource().getMessage(flashbuyButtonLabel, null,
				RequestContextUtils.getLocale(request));
		final JSONObject jsonObj = new JSONObject();
		jsonObj.put("status", RETRIEVERESULT_REJECT);
		jsonObj.put("label", message);
		return jsonObj.toString();
	}


}

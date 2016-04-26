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
package com.sap.hybris.reco.addon.facade.impl;

import com.google.common.collect.Lists;
import com.sap.hybris.reco.addon.constants.SapprodrecoaddonConstants;
import com.sap.hybris.reco.dao.ProductRecommendationData;
import com.sap.hybris.reco.common.util.HMCConfigurationReader;
import com.sap.hybris.reco.common.util.CookieHelper;
import com.sap.hybris.reco.model.CMSSAPRecommendationComponentModel;

import de.hybris.platform.catalog.enums.ProductReferenceTypeEnum;
import de.hybris.platform.commercefacades.converter.ConfigurablePopulator;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.ProductReferenceData;
import de.hybris.platform.commerceservices.product.data.ReferenceData;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.sap.core.common.util.GenericFactory;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sap.hybris.reco.addon.facade.ProductRecommendationManagerFacade;
import com.sap.hybris.reco.bo.ProductRecommendationManagerBO;
import com.sap.hybris.reco.common.util.UserIdProvider;
import com.sap.hybris.reco.dao.InteractionContext;
import com.sap.hybris.reco.dao.ProductRecommendation;
import com.sap.hybris.reco.dao.RecommendationContext;

import org.apache.commons.lang.BooleanUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @param <REF_TARGET> 
 * 
 */
public class DefaultProductRecommendationManagerFacade<REF_TARGET> implements ProductRecommendationManagerFacade
{
	private Converter<ReferenceData<ProductReferenceTypeEnum, REF_TARGET>, ProductReferenceData> referenceDataProductReferenceConverter;
	private ConfigurablePopulator<REF_TARGET, ProductData, ProductOption> referenceProductConfiguredPopulator;

	private GenericFactory genericFactory;
	private UserService userService;
	private CartService cartService;
	private UserIdProvider userIDProvider;
	private ProductService productService;
	private String anonOriginOfContactId;
	
	@Resource(name="hmcConfigurationReader")
	private HMCConfigurationReader configuration;
	
	protected static final List<ProductOption> PRODUCT_OPTIONS = Arrays.asList(ProductOption.BASIC, ProductOption.PRICE);


	/**
	 * Get product recommendations based on current context
	 * 
	 */
	public List<ProductReferenceData> getProductRecommendation(final RecommendationContext context)
	{
		final List<ReferenceData<ProductReferenceTypeEnum, ProductModel>> references = createReferenzList(context);

		final List<ProductReferenceData> result = new ArrayList<ProductReferenceData>();

		for (final ReferenceData<ProductReferenceTypeEnum, ProductModel> reference : references)
		{
			final ProductReferenceData productReferenceData = getReferenceDataProductReferenceConverter().convert(
					(ReferenceData<ProductReferenceTypeEnum, REF_TARGET>) reference);
			getReferenceProductConfiguredPopulator().populate((REF_TARGET) reference.getTarget(), productReferenceData.getTarget(),
					PRODUCT_OPTIONS);
			result.add(productReferenceData);
		}

		return result;
	}
	
	/**
	 * Post clickthrough action to backend
	 * @param context 
	 * 
	 */
	public void postInteraction(InteractionContext context)
	{
		getRecommendationManager().postInteraction(context);		
	}

	/**
	 * @param context
	 * @return
	 */
	private List<ReferenceData<ProductReferenceTypeEnum, ProductModel>> createReferenzList(
			final RecommendationContext context)
	{
		final List<ProductRecommendationData> productRecommendations = getRecommendationManager().getProductRecommendation(context);

		if(productRecommendations != null) {
			return convertToProductReference(productRecommendations);
		}

		return Lists.newArrayList();
	}

	/**
	 * @param productRecommendations
	 * @return
	 */
	private List<ReferenceData<ProductReferenceTypeEnum, ProductModel>> convertToProductReference(
			final List<ProductRecommendationData> productRecommendations)
	{
		final List<ReferenceData<ProductReferenceTypeEnum, ProductModel>> references = new ArrayList<ReferenceData<ProductReferenceTypeEnum, ProductModel>>();

		for (final ProductRecommendationData productRecommendation : productRecommendations)
		{
			addReferenceData(references, productRecommendation);
		}

		return references;
	}
	
	private void addReferenceData(List<ReferenceData<ProductReferenceTypeEnum, ProductModel>> references, ProductRecommendationData productRecommendation) {
		try {
			ProductModel product = getProductService().getProductForCode(productRecommendation.getProductCode());
			references.add(createReferenceData(product));

		} catch (IllegalArgumentException | UnknownIdentifierException | AmbiguousIdentifierException e){

		}
	}

	private ReferenceData<ProductReferenceTypeEnum, ProductModel> createReferenceData(ProductModel product) {
		final ReferenceData<ProductReferenceTypeEnum, ProductModel> referenceData = new ReferenceData<ProductReferenceTypeEnum, ProductModel>();
		referenceData.setQuantity(new Integer(1));
		referenceData.setReferenceType(ProductReferenceTypeEnum.OTHERS);
		referenceData.setTarget(product);
		return referenceData;
	}

	public RecommendationContext createRecommendationContextProvider()
	{
		final RecommendationContext contextProvider = genericFactory.getBean("sapRecommendationContextProvider");
		final UserModel currentUser = userService.getCurrentUser();
		contextProvider.setUserId(userIDProvider.getUserId(currentUser));
		contextProvider.setUserType("");
		final CartModel cartModel = cartService.getSessionCart();
		for (final AbstractOrderEntryModel cartEntry : cartModel.getEntries())
		{
			contextProvider.addCartItem(cartEntry.getProduct().getCode());
		}

		return contextProvider;
	}
	
	public String getSessionUserId(){
		final UserModel currentUser = userService.getCurrentUser();
		return userIDProvider.getUserId(currentUser);
	}
	
	@Override
	public void populateContext(HttpServletRequest request, RecommendationContext context, CMSSAPRecommendationComponentModel component, String productCode){

		context.setProductId(productCode);
		context.setRecotype(component.getRecotype());
		context.setIncludeCart(BooleanUtils.isTrue(component.isIncludecart()));
		context.setItemDataSourceType(component.getDatasourcetype());
		context.setComponentModel(component);
		if (getSessionUserId().equals("anonymous")) {
			context.setUserId(CookieHelper.getPiwikID(request));
			context.setUserType(this.getAnonOriginOfContactId());
		} else {
			context.setUserId(getSessionUserId());
			context.setUserType(configuration.getUserType());
		}
		context.setUsage(configuration.getUsage());
	}

	@Override
	public void prefetchRecommendations(HttpServletRequest request, CMSSAPRecommendationComponentModel component, String productCode) {
		final RecommendationContext context = createRecommendationContextProvider();
		populateContext(request, context, component, productCode);
		getRecommendationManager().prefetchRecommendations(context);
	}
	
	protected ProductRecommendationManagerBO getRecommendationManager()
	{
		return genericFactory.getBean("sapProductRecommendationManagerBO");
	}


	/**
	 * @return the referenceDataProductReferenceConverter
	 */
	public Converter<ReferenceData<ProductReferenceTypeEnum, REF_TARGET>, ProductReferenceData> getReferenceDataProductReferenceConverter()
	{
		return referenceDataProductReferenceConverter;
	}


	/**
	 * @param referenceDataProductReferenceConverter
	 *           the referenceDataProductReferenceConverter to set
	 */
	public void setReferenceDataProductReferenceConverter(
			final Converter<ReferenceData<ProductReferenceTypeEnum, REF_TARGET>, ProductReferenceData> referenceDataProductReferenceConverter)
	{
		this.referenceDataProductReferenceConverter = referenceDataProductReferenceConverter;
	}


	/**
	 * @return the referenceProductConfiguredPopulator
	 */
	public ConfigurablePopulator<REF_TARGET, ProductData, ProductOption> getReferenceProductConfiguredPopulator()
	{
		return referenceProductConfiguredPopulator;
	}


	/**
	 * @param referenceProductConfiguredPopulator
	 *           the referenceProductConfiguredPopulator to set
	 */
	public void setReferenceProductConfiguredPopulator(
			final ConfigurablePopulator<REF_TARGET, ProductData, ProductOption> referenceProductConfiguredPopulator)
	{
		this.referenceProductConfiguredPopulator = referenceProductConfiguredPopulator;
	}


	/**
	 * @return the genericFactory
	 */
	public GenericFactory getGenericFactory()
	{
		return genericFactory;
	}


	/**
	 * @param genericFactory
	 *           the genericFactory to set
	 */
	public void setGenericFactory(final GenericFactory genericFactory)
	{
		this.genericFactory = genericFactory;
	}

	/**
	 * @return the userService
	 */
	public UserService getUserService()
	{
		return userService;
	}

	/**
	 * @param userService
	 *           the userService to set
	 */
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	/**
	 * @return the cartService
	 */
	public CartService getCartService()
	{
		return cartService;
	}

	/**
	 * @param cartService
	 *           the cartService to set
	 */
	public void setCartService(final CartService cartService)
	{
		this.cartService = cartService;
	}

	/**
	 * @return the userIDProvider
	 */
	public UserIdProvider getUserIDProvider()
	{
		return userIDProvider;
	}

	/**
	 * @param userIDProvider
	 *           the userIDProvider to set
	 */
	public void setUserIDProvider(final UserIdProvider userIDProvider)
	{
		this.userIDProvider = userIDProvider;
	}
	

	/**
	 * @return the productService
	 */
	public ProductService getProductService() {
		return productService;
	}

	/**
	 * @param productService
	 *           the productService to set
	 */
	public void setProductService(ProductService productService) {
		this.productService = productService;
	}
	
	public String getAnonOriginOfContactId() {
		return anonOriginOfContactId;
	}

	public void setAnonOriginOfContactId(String anonOriginOfContactId) {
		this.anonOriginOfContactId = anonOriginOfContactId;
	}



}

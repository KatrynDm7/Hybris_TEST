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
package com.sap.hybris.reco.be.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sap.hybris.reco.bo.ProductRecommendationPrefetcher;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.TenantAwareThreadFactory;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

import de.hybris.platform.servicelayer.session.SessionService;

import org.apache.log4j.Logger;

import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoTable;

import de.hybris.platform.sap.core.bol.backend.BackendType;
import de.hybris.platform.sap.core.bol.backend.jco.BackendBusinessObjectBaseJCo;
import de.hybris.platform.sap.core.jco.connection.JCoConnection;
import de.hybris.platform.sap.core.jco.exceptions.BackendException;

import com.sap.hybris.reco.dao.ProductRecommendationData;
import com.sap.hybris.reco.dao.RecommendationContext;
import com.sap.hybris.reco.be.ProductRecommendationManagerBackend;
import com.sap.hybris.reco.be.ProductRecommendationsValueProvider;
import com.sap.hybris.reco.common.util.HMCConfigurationReader;
import com.sap.hybris.reco.constants.SapproductrecommendationConstants;
import com.sap.hybris.reco.dao.InteractionContext;
import com.sap.hybris.reco.dao.ProductRecommendation;

import org.springframework.beans.factory.annotation.Required;

import javax.annotation.PostConstruct;

/**
 * 
 */
@BackendType("CEI")
public class ProductRecommendationManagerCEI extends BackendBusinessObjectBaseJCo implements ProductRecommendationManagerBackend
{
	private final static Logger LOG = Logger.getLogger( ProductRecommendationManagerCEI.class.getName() );
	private static String JCO_STATELESS = "JCoStateless";
	protected HMCConfigurationReader configuration;
	private SessionService sessionService;

	private boolean prefetchEnabled;
	private int maxThreads;

	private ExecutorService threadPool;

	private int retrieveWaitTimeoutMs;

	private static final TimeUnit MS = TimeUnit.MILLISECONDS;
	
	private final static String ERROR = "E";
	private final static String INFO = "I";
	private ProductRecommendationsValueProvider defaultResultProvider;


	@PostConstruct
	public void init(){
		this.threadPool = Executors.newFixedThreadPool(getMaxThreads(), new TenantAwareThreadFactory(Registry.getCurrentTenant()));
	}
		
	/**
	 * @param context
	 */
	public void postInteraction(InteractionContext context)
	{
		JCoConnection jCoConnection = getJcoConnection();
		
		try
		{
			String functionName = "";
			functionName = "PROD_RECO_POST_IA_FOR_SCENARIO";
			
			final JCoFunction function = jCoConnection.getFunction(functionName);
			final JCoParameterList importParameterList = function.getImportParameterList();
			final JCoTable interactions = importParameterList.getTable("IT_INTERACTIONS");
			interactions.appendRow();
			interactions.setValue("SCENARIO_ID", context.getScenarioId());
			interactions.setValue("USER_ID", context.getUserId());
			interactions.setValue("USER_TYPE", context.getUserType());
			interactions.setValue("IA_TYPE", SapproductrecommendationConstants.CLICKTHROUGH);
			interactions.setValue("TIMESTAMP", context.getTimestamp());
			interactions.setValue("SOURCE_OBJECT_ID", context.getSourceObjectId());
			
			final JCoTable leadingObjects = interactions.getTable("IPRODUCTS");
			leadingObjects.appendRow();
			leadingObjects.setValue("OBJECT_TYPE", context.getProductType());
			leadingObjects.setValue("OBJECT_ID", context.getProductId());
			leadingObjects.setValue("PRODUCT_NAV_URL", context.getProductNavURL());
			leadingObjects.setValue("PRODUCT_IMAGE_URL", context.getProductImageURL());
												
			jCoConnection.execute(function);

			final JCoParameterList exportParameterList = function.getExportParameterList();

			final JCoTable messages = exportParameterList.getTable("ET_MESSAGES");
						
			if (!messages.isEmpty())
			{
				logJCoMessages(messages, INFO);
			}
		}
		catch (final BackendException e)
		{
			LOG.error("", e);
		}
		
	}

	/**
	 * @param context
	 * @return list of recommended products
	 */
	public List<ProductRecommendationData> getProductRecommendation(final RecommendationContext context)
	{

		if(prefetchEnabled) {
			return getPrefetchedResult(context);
		}

		if(getConfiguration().getUsage().equals(SapproductrecommendationConstants.SCENARIO))
		{
			return getProductRecommendationByScenario(context);
		}else if(getConfiguration().getUsage().equals(SapproductrecommendationConstants.MODELTYPE)){
			return getProductRecommendationByModelType(context);
		}
		return null;
	}
	
	/**
	 * @param context
	 * @return list of recommended products
	 */
	private List<ProductRecommendationData> getPrefetchedResult(RecommendationContext context) {
		List<ProductRecommendationData> result = Lists.newArrayList();
		try {
			Future<JCoParameterList> future = getAndRemoveFutureFromSession(context);
			if(future == null){
				result = getDefaultResultProvider().getDefaultResult();
			}else {
				final JCoParameterList exportParameterList = future.get(getRetrieveWaitTimeoutMs(), MS);
				final JCoTable results = exportParameterList.getTable("ET_RESULTS");
				addRecommendations(results, result);
				final JCoTable messages = exportParameterList.getTable("ET_MESSAGES");

				if (!messages.isEmpty()) {
					logJCoMessages(messages, INFO);
				}
			}

		} catch (InterruptedException  | ExecutionException | TimeoutException e) {
			LOG.error("Cannot retrieve product recommendations from yMarketing", e);
			result = getDefaultResultProvider().getDefaultResult();
		}
		return result;
	}
	
	/**
	 * @param context
	 * @return the future
	 */
	private Future<JCoParameterList> getAndRemoveFutureFromSession(RecommendationContext context) {
		Object recommendations = getSessionService().getAttribute(SapproductrecommendationConstants.PRODUCTRECOMMENDATIONS);
		Future<JCoParameterList> future = null;
		if(recommendations != null){
			Map<String, Future<JCoParameterList>> recommendationsMap = (Map<String, Future<JCoParameterList>>) recommendations;
			if(recommendationsMap.containsKey(context.getSessionKey())){
				future = recommendationsMap.get(context.getSessionKey());
				removeFromSession(context.getSessionKey(), recommendationsMap);

			}
		}
		return future;
	}
	
	private void removeFromSession(String key,  Map<String, Future<JCoParameterList>> recommendations) {
		Map<String, Future<JCoParameterList>> modifiableMap = Maps.newHashMap(recommendations);
		modifiableMap.remove(key);
		getSessionService().setAttribute(SapproductrecommendationConstants.PRODUCTRECOMMENDATIONS, modifiableMap);
	}
	
	/**
	 * @param context
	 */
	public void prefetchRecommendation(final RecommendationContext context)
	{
		if(prefetchEnabled) {
			if (getConfiguration().getUsage().equals(SapproductrecommendationConstants.SCENARIO)) {
				prefetchProductRecommendationByScenario(context);
			} else if (getConfiguration().getUsage().equals(SapproductrecommendationConstants.MODELTYPE)) {
				prefetchRecommendationByModelType(context);
			}
		}
	}
	
	/**
	 * @param context
	 * @return list of recommended products
	 */
	public void prefetchRecommendationByModelType(final RecommendationContext context){
		//build JCoConneciton
		JCoConnection jCoConnection = getJcoConnection();

		try
		{
			final JCoFunction function = jCoConnection.getFunction("PROD_RECO_GET_RECOMMENDATIONS");
			handleImportParameterList(function, context, "MODEL_TYPE");
			prefetchRecommendations(jCoConnection, function, context);

		}
		catch (final BackendException e)
		{
			LOG.error("", e);
		}

	}
	
	/**
	 * @param jCoConnection
	 * @param function
	 * @param context
	 */
	private void prefetchRecommendations(JCoConnection jCoConnection, JCoFunction function, RecommendationContext context) {
		final Future<JCoParameterList> future = threadPool.submit(new ProductRecommendationPrefetcher(jCoConnection, function));
		storeFutureInSession(context.getSessionKey(), future);
	}

	/**
	 * @param uid
	 * @param future
	 */
	private void storeFutureInSession(String uid, Future<JCoParameterList> future) {
		Object recommendations = getSessionService().getAttribute(SapproductrecommendationConstants.PRODUCTRECOMMENDATIONS);
		Map<String, Future<JCoParameterList>> recommendationsMap;
		if(recommendations != null){
			recommendationsMap = Maps.newHashMap((Map<String, Future<JCoParameterList>>) recommendations);

		}
		else {
			recommendationsMap = Maps.newHashMap();
		}
		recommendationsMap.put(uid, future);
		getSessionService().setAttribute(SapproductrecommendationConstants.PRODUCTRECOMMENDATIONS, recommendationsMap);
	}

	/**
	 * @param results
	 * @param result
	 */
	private void addRecommendations(JCoTable results, List<ProductRecommendationData> result) {
		if (!results.isEmpty()) {
			final int len = results.getNumRows();
			for (int i = 0; i < len; i++) {
				results.setRow(i);
				final String recommendationId = results.getString("ITEM_ID");
				final ProductRecommendationData productRecommendation = createProductRecommedation(recommendationId);
				if (productRecommendation != null) {
					result.add(productRecommendation);
				}
			}
		}
	}
	
	/**
	 * @param context
	 * @return list of recommended products
	 */
	public List<ProductRecommendationData> getProductRecommendationByModelType(final RecommendationContext context)
	{		
		final List<ProductRecommendationData> result = new ArrayList<>();
      
		//build JCoConneciton
		JCoConnection jCoConnection = getJcoConnection();
		try
		{
			final JCoFunction function = jCoConnection.getFunction("PROD_RECO_GET_RECOMMENDATIONS");
			handleImportParameterList(function, context, "MODEL_TYPE");

			jCoConnection.execute(function);

			final JCoParameterList exportParameterList = function.getExportParameterList();

			final JCoTable results = exportParameterList.getTable("ET_RESULTS");

			addRecommendations(results, result);

			final JCoTable messages = exportParameterList.getTable("ET_MESSAGES");
			
			if (!messages.isEmpty())
			{
				logJCoMessages(messages, INFO);
			}
		}
		catch (final BackendException e)
		{
			LOG.error("", e);
		}
		
		return result;	
	}

	
	/**
	 * @param context
	 * @return list of recommended products
	 */
	public List<ProductRecommendationData> prefetchProductRecommendationByScenario(final RecommendationContext context)
	{
		final List<ProductRecommendationData> result = new ArrayList<>();

		JCoConnection jCoConnection = getJcoConnection();

		try
		{
			final JCoFunction function = jCoConnection.getFunction("PROD_RECO_GET_RECO_BY_SCENARIO");
			handleImportParameterList(function, context, "SCENARIO_ID");
			prefetchRecommendations(jCoConnection,function, context);

		}
		catch (final BackendException e)
		{
			LOG.error("", e);
		}

		return result;
	}

	
	/**
	 * @param context
	 * @return list of recommended products
	 */
	public List<ProductRecommendationData> getProductRecommendationByScenario(final RecommendationContext context)
	{		
		final List<ProductRecommendationData> result = new ArrayList<>();
      
		JCoConnection jCoConnection = getJcoConnection();

		try
		{
			final JCoFunction function = jCoConnection.getFunction("PROD_RECO_GET_RECO_BY_SCENARIO");
			handleImportParameterList(function, context, "SCENARIO_ID");

			jCoConnection.execute(function);

			final JCoParameterList exportParameterList = function.getExportParameterList();

			final JCoTable results = exportParameterList.getTable("ET_RESULTS");

			if (!results.isEmpty())
			{
				final int len = results.getNumRows();
				for (int i = 0; i < len; i++)
				{
					results.setRow(i);
					final String recommendationId = results.getString("ITEM_ID");

					final ProductRecommendationData productRecommendation = createProductRecommedation(recommendationId);
					if (productRecommendation != null)
					{
						result.add(productRecommendation);
					}
				}
			}
			final JCoTable messages = exportParameterList.getTable("ET_MESSAGES");
			
			if (!messages.isEmpty())
			{
				logJCoMessages(messages, INFO);
			}
		}
		catch (final BackendException e)
		{
			LOG.error("", e);
		}

		return result;	
	}
	
	/**
	 * @param function
	 * @param context
	 * @param valueKey
	 */
	private void handleImportParameterList(JCoFunction function, RecommendationContext context, String valueKey) {

		final String recoType = context.getRecotype();
		final String itemType= context.getItemDataSourceType();
		final String productId = context.getProductId();


		final JCoParameterList importParameterList = function.getImportParameterList();
		final JCoTable recommenders = importParameterList.getTable("IT_RECOMMENDERS");
		recommenders.appendRow();
		recommenders.setValue(valueKey, recoType);

		final JCoTable leadingObjects = recommenders.getTable("LEADING_OBJECTS");
		if ( productId != null && !productId.equals("") && !productId.equalsIgnoreCase("null"))
		{
			leadingObjects.appendRow();
			leadingObjects.setValue("ITEM_ID", productId);
			leadingObjects.setValue("ITEM_TYPE", itemType);
		}

		final JCoTable cartEntries = recommenders.getTable("BASKET_OBJECTS");
		for (final String cartItem : context.getCartItems())
		{
			cartEntries.appendRow();
			cartEntries.setValue("ITEM_ID", cartItem);
			cartEntries.setValue("ITEM_TYPE", itemType);
			if (context.getIncludeCart())
			{
				leadingObjects.appendRow();
				leadingObjects.setValue("ITEM_ID", cartItem);
				leadingObjects.setValue("ITEM_TYPE", itemType);
			}
		}

		importParameterList.setValue("IV_USER_ID", context.getUserId());
		importParameterList.setValue("IV_USER_TYPE", context.getUserType());
	}
	
	private JCoConnection getJcoConnection()
	{
		JCoConnection jCoConnection;
		if (getConfiguration().getRfcDestinationId() == null)
		{
			jCoConnection = getDefaultJCoConnection();
		}
		else
		{
			jCoConnection = getJCoConnection(JCO_STATELESS, getConfiguration().getRfcDestinationId());
		}

		try
		{
			if (jCoConnection.isBackendAvailable() == false)
			{
				LOG.error("RFC - " + getConfiguration().getRfcDestinationId() + " backend is not available");
			}
		}
		catch (final BackendException e)
		{
			LOG.error("", e);
		}
		return jCoConnection;
	}
	
	/**
	 * @param productId
	 */
	private ProductRecommendationData createProductRecommedation(final String productId)
	{
		try
		{
			final ProductRecommendationData productRecommendationData = new ProductRecommendationData();
			productRecommendationData.setProductCode(productId);
			return productRecommendationData;
		}
		catch (final UnknownIdentifierException exception)
		{
			return null;
		}
	}

	/**
	 * @return hmc configuration reader  
	 */
	public HMCConfigurationReader getConfiguration()
	{
		return configuration;
	}
	
	
	/**
	 * @return sessionService
	 */
	public SessionService getSessionService() {
		return sessionService;
	}

	/**
	 * @param sessionService
	 *           the sessionService to set
	 */
	public void setSessionService(SessionService sessionService) {
		this.sessionService = sessionService;
	}

	/**
	 * @param configuration
	 */
	public void setConfiguration(final HMCConfigurationReader configuration)
	{
		this.configuration = configuration;
	}
	
	/**
	 * @return retrieveWaitTimeoutMs
	 */
	public int getRetrieveWaitTimeoutMs() {
		return retrieveWaitTimeoutMs;
	}

	/**
	 * @param retrieveWaitTimeoutMs
	 *           the retrieveWaitTimeoutMs to set
	 */
	@Required
	public void setRetrieveWaitTimeoutMs(int retrieveWaitTimeoutMs) {
		this.retrieveWaitTimeoutMs = retrieveWaitTimeoutMs;
	}
	
	private void logJCoMessages(JCoTable table, String level){
		final int len = table.getNumRows();
		for (int i = 0; i < len; i++)
		{
			table.setRow(i);
			String msgId = table.getString("ID");
			String msg = table.getString("MESSAGE");
			if(level.equals(ERROR)){
				LOG.error(msgId + " " + msg);
			}
			else if(level.equals(INFO)){
				LOG.info(msgId + " " + msg);
			}
		}
	}
	
	/**
	 * @return prefetchEnabled
	 */
	public boolean isPrefetchEnabled() {
		return prefetchEnabled;
	}

	/**
	 * @param prefetchEnabled
	 *           the prefetchEnabled to set
	 */
	@Required
	public void setPrefetchEnabled(boolean prefetchEnabled) {
		this.prefetchEnabled = prefetchEnabled;
	}

	/**
	 * @return maxThreads
	 */
	public int getMaxThreads() {
		return maxThreads;
	}

	/**
	 * @param maxThreads
	 *           the maxThreads to set
	 */
	@Required
	public void setMaxThreads(int maxThreads) {
		this.maxThreads = maxThreads;
	}

	@Required
	public void setDefaultResultProvider(ProductRecommendationsValueProvider defaultResultProvider) {
		this.defaultResultProvider = defaultResultProvider;
	}

	protected ProductRecommendationsValueProvider getDefaultResultProvider() {
		return defaultResultProvider;
	}
}

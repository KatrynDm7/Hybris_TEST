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
package de.hybris.platform.timedaccesspromotionsservices.dao.impl;

import de.hybris.platform.acceleratorservices.promotions.dao.impl.DefaultPromotionsDao;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.timedaccesspromotionsservices.dao.FlashbuyPromotionDao;
import de.hybris.platform.timedaccesspromotionsservices.model.FlashbuyPromotionModel;
import de.hybris.platform.timedaccesspromotionsservices.model.PromotionAllocationModel;
import de.hybris.platform.timedaccesspromotionsservices.model.PromotionEnqueueModel;
import de.hybris.platform.timedaccesspromotionsservices.model.PromotionReservationModel;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Default Data Access for Flashbuy Promotion remaining quantity and reserved quantity.
 */
public class DefaultFlashbuyPromotionDao extends DefaultPromotionsDao implements FlashbuyPromotionDao
{

	final String enqueueSql = "SELECT COUNT( {" + PromotionEnqueueModel.PK + "} ) FROM {" + PromotionEnqueueModel._TYPECODE + "} "
			+ "WHERE {" + PromotionEnqueueModel.PROMOTIONCODE + "}=?promotionCode and {" + PromotionEnqueueModel.PRODUCTCODE
			+ "}=?productCode";

	final String reservationSql = "SELECT COUNT( {" + PromotionReservationModel.PK + "} ) FROM {"
			+ PromotionReservationModel._TYPECODE + "} " + "WHERE {" + PromotionReservationModel.PROMOTIONCODE
			+ "}=?promotionCode and {" + PromotionReservationModel.PRODUCTCODE + "}=?productCode";

	final String allocationSql = "SELECT COUNT( {" + PromotionAllocationModel.PK + "} ) FROM {"
			+ PromotionAllocationModel._TYPECODE + "} " + "WHERE {" + PromotionAllocationModel.PROMOTIONCODE
			+ "}=?promotionCode and {" + PromotionAllocationModel.PRODUCTCODE + "}=?productCode";

	final String reservationPerCartSql = reservationSql + " and {" + PromotionReservationModel.PROMOTIONMATCHER
			+ "}=?promotionMatcher";

	final String promotionEnqueueRemoveSql = "SELECT {" + PromotionEnqueueModel.PK + "} FROM {" + PromotionEnqueueModel._TYPECODE
			+ "} " + "WHERE {" + PromotionEnqueueModel.PROMOTIONCODE + "}=?promotionCode and {" + PromotionEnqueueModel.PRODUCTCODE
			+ "}=?productCode and {" + PromotionEnqueueModel.CUSTOMERUID + "}=?customerUID";

	final String promotionReservationRemoveSql = "SELECT {" + PromotionReservationModel.PK + "} FROM {"
			+ PromotionReservationModel._TYPECODE + "} " + "WHERE {" + PromotionReservationModel.PROMOTIONCODE
			+ "}=?promotionCode and {" + PromotionReservationModel.PRODUCTCODE + "}=?productCode and {"
			+ PromotionReservationModel.PROMOTIONMATCHER + "}=?promotionMatcher";

	final String promotionAllocationRemoveSql = "SELECT {" + PromotionAllocationModel.PK + "} FROM {"
			+ PromotionAllocationModel._TYPECODE + "} " + "WHERE {" + PromotionAllocationModel.PROMOTIONCODE
			+ "}=?promotionCode and {" + PromotionAllocationModel.PRODUCTCODE + "}=?productCode and {"
			+ PromotionAllocationModel.PROMOTIONMATCHER + "}=?promotionMatcher";

	final String getReservationPromotionMatcherSql = "SELECT DISTINCT {" + PromotionReservationModel.PROMOTIONMATCHER + "} FROM {"
			+ PromotionReservationModel._TYPECODE + "} " + "WHERE {" + PromotionReservationModel.PROMOTIONCODE
			+ "}=?promotionCode and {" + PromotionReservationModel.PRODUCTCODE + "}=?productCode";

	final String getAllocationPromotionMatcherSql = "SELECT DISTINCT {" + PromotionAllocationModel.PROMOTIONMATCHER + "} FROM {"
			+ PromotionAllocationModel._TYPECODE + "} " + "WHERE {" + PromotionAllocationModel.PROMOTIONCODE
			+ "}=?promotionCode and {" + PromotionAllocationModel.PRODUCTCODE + "}=?productCode";

	final String getAbstractOrderEntrySql = "SELECT {" + AbstractOrderEntryModel.PK + "} FROM {"
			+ AbstractOrderEntryModel._TYPECODE + "} " + "WHERE {" + AbstractOrderEntryModel.PROMOTIONMATCHER
			+ "}=?promotionMatcher";

	@Override
	public long getRemainingQuantity(final String promotionCode, final String productCode)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("promotionCode", promotionCode);
		ServicesUtil.validateParameterNotNullStandardMessage("productCode", productCode);

		final AbstractPromotionModel promotionModel = getPromotionForCode(promotionCode);
		if (null == promotionModel)
		{
			return 0;
		}
		final FlashbuyPromotionModel flashbuyPromotion = (FlashbuyPromotionModel) promotionModel;
		//Deduct the enqueue quantity
		long remainingQty = flashbuyPromotion.getAvailableUnitsPerProduct().longValue()
				- getQuantityOfDifferentTypes(promotionCode, productCode, enqueueSql);
		if (remainingQty <= 0)
		{
			return 0;
		}

		//Deduct the reservation quantity
		remainingQty = remainingQty - getQuantityOfDifferentTypes(promotionCode, productCode, reservationSql);
		if (remainingQty <= 0)
		{
			return 0;
		}

		//Deduct the allocation quantity
		remainingQty = remainingQty - getQuantityOfDifferentTypes(promotionCode, productCode, allocationSql);

		if (remainingQty <= 0)
		{
			return 0;
		}

		return remainingQty;
	}

	@Override
	public long getReserverdQuantity(final String promotionCode, final String productCode, final String promotionMatcher)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("promotionCode", promotionCode);
		ServicesUtil.validateParameterNotNullStandardMessage("productCode", productCode);
		ServicesUtil.validateParameterNotNullStandardMessage("promotionMatcher", promotionMatcher);


		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("promotionCode", promotionCode);
		params.put("productCode", productCode);
		params.put("promotionMatcher", promotionMatcher);

		final FlexibleSearchQuery query = new FlexibleSearchQuery(reservationPerCartSql);
		query.addQueryParameters(params);
		query.setResultClassList(Arrays.asList(Long.class));

		final SearchResult<Long> result = getFlexibleSearchService().search(query);
		return result.getResult().get(0);
	}

	/**
	 * Get the quantity of enqueue, reservation, allocation for specific promotion code and specific product code.
	 *
	 * @param sql
	 *           SQL to query the quantity of different types
	 */
	protected long getQuantityOfDifferentTypes(final String promotionCode, final String productCode, final String sql)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("promotionCode", promotionCode);
		ServicesUtil.validateParameterNotNullStandardMessage("productCode", productCode);

		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("promotionCode", promotionCode);
		params.put("productCode", productCode);

		final FlexibleSearchQuery query = new FlexibleSearchQuery(sql);
		query.addQueryParameters(params);
		query.setResultClassList(Arrays.asList(Long.class));

		final SearchResult<Long> result = getFlexibleSearchService().search(query);
		return result.getResult().get(0);

	}

	@Override
	public List<PromotionEnqueueModel> getEnqueueforRemoval(final String productID, final String promotionID,
			final String customerUID)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("productCode", productID);
		ServicesUtil.validateParameterNotNullStandardMessage("promotionCode", promotionID);
		ServicesUtil.validateParameterNotNullStandardMessage("customerUID", customerUID);

		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("productCode", productID);
		params.put("promotionCode", promotionID);
		params.put("customerUID", customerUID);

		final FlexibleSearchQuery query = new FlexibleSearchQuery(promotionEnqueueRemoveSql);
		query.addQueryParameters(params);
		query.setResultClassList(Arrays.asList(PromotionEnqueueModel.class));

		final SearchResult<PromotionEnqueueModel> result = getFlexibleSearchService().search(query);
		return result.getResult();
	}

	@Override
	public List<PromotionReservationModel> getReservationforRemoval(final String productID, final String promotionID,
			final String promotionMatcher)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("productCode", productID);
		ServicesUtil.validateParameterNotNullStandardMessage("promotionCode", promotionID);
		ServicesUtil.validateParameterNotNullStandardMessage("promotionMatcher", promotionMatcher);

		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("productCode", productID);
		params.put("promotionCode", promotionID);
		params.put("promotionMatcher", promotionMatcher);

		final FlexibleSearchQuery query = new FlexibleSearchQuery(promotionReservationRemoveSql);
		query.addQueryParameters(params);
		query.setResultClassList(Arrays.asList(PromotionReservationModel.class));

		final SearchResult<PromotionReservationModel> result = getFlexibleSearchService().search(query);
		return result.getResult();
	}

	@Override
	public List<PromotionAllocationModel> getAllocationforRemoval(final String productID, final String promotionID,
			final String promotionMatcher)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("productCode", productID);
		ServicesUtil.validateParameterNotNullStandardMessage("promotionCode", promotionID);
		ServicesUtil.validateParameterNotNullStandardMessage("promotionMatcher", promotionMatcher);

		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("productCode", productID);
		params.put("promotionCode", promotionID);
		params.put("promotionMatcher", promotionMatcher);

		final FlexibleSearchQuery query = new FlexibleSearchQuery(promotionAllocationRemoveSql);
		query.addQueryParameters(params);
		query.setResultClassList(Arrays.asList(PromotionAllocationModel.class));

		final SearchResult<PromotionAllocationModel> result = getFlexibleSearchService().search(query);
		return result.getResult();
	}

	@Override
	public List<String> getReservePromotionMatcherByPromotionAndProduct(final String promotionCode, final String productCode)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("productCode", productCode);
		ServicesUtil.validateParameterNotNullStandardMessage("promotionCode", promotionCode);

		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("productCode", productCode);
		params.put("promotionCode", promotionCode);

		final FlexibleSearchQuery query = new FlexibleSearchQuery(getReservationPromotionMatcherSql);
		query.addQueryParameters(params);
		query.setResultClassList(Arrays.asList(String.class));

		final SearchResult<String> result = getFlexibleSearchService().search(query);
		return result.getResult();
	}

	@Override
	public List<String> getAllocatePromotionMatcherByPromotionAndProduct(final String promotionCode, final String productCode)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("productCode", productCode);
		ServicesUtil.validateParameterNotNullStandardMessage("promotionCode", promotionCode);

		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("productCode", productCode);
		params.put("promotionCode", promotionCode);

		final FlexibleSearchQuery query = new FlexibleSearchQuery(getAllocationPromotionMatcherSql);
		query.addQueryParameters(params);
		query.setResultClassList(Arrays.asList(String.class));

		final SearchResult<String> result = getFlexibleSearchService().search(query);
		return result.getResult();
	}

	@Override
	public AbstractOrderEntryModel getAbstractOrderEntryByPromotionMatcher(final String promotionMatcher)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("promotionMatcher", promotionMatcher);

		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("promotionMatcher", promotionMatcher);

		final FlexibleSearchQuery query = new FlexibleSearchQuery(getAbstractOrderEntrySql);
		query.addQueryParameters(params);
		query.setResultClassList(Arrays.asList(AbstractOrderEntryModel.class));

		final SearchResult<AbstractOrderEntryModel> result = getFlexibleSearchService().search(query);

		return result.getCount() > 0 ? result.getResult().get(0) : null;
	}



}

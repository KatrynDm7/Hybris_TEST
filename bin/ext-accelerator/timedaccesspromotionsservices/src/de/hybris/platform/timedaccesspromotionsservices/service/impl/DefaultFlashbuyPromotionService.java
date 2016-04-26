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
package de.hybris.platform.timedaccesspromotionsservices.service.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.timedaccesspromotionsservices.dao.FlashbuyPromotionDao;
import de.hybris.platform.timedaccesspromotionsservices.exception.MultipleEnqueueException;
import de.hybris.platform.timedaccesspromotionsservices.model.FlashbuyPromotionModel;
import de.hybris.platform.timedaccesspromotionsservices.model.PromotionAllocationModel;
import de.hybris.platform.timedaccesspromotionsservices.model.PromotionEnqueueModel;
import de.hybris.platform.timedaccesspromotionsservices.model.PromotionReservationModel;
import de.hybris.platform.timedaccesspromotionsservices.service.FlashbuyPromotionService;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation for {@link FlashbuyPromotionService}
 */
public class DefaultFlashbuyPromotionService implements FlashbuyPromotionService
{
	protected static final Logger LOG = Logger.getLogger(DefaultFlashbuyPromotionService.class);

	private FlashbuyPromotionDao flashbuyPromotionDao;
	private ModelService modelService;

	@Override
	public long getRemainingQuantity(final String promotionCode, final String productCode)
	{
		validateParameterNotNull(promotionCode, "Promotion Code can not be null");
		validateParameterNotNull(productCode, "Product Code can not be null");

		return getFlashbuyPromotionDao().getRemainingQuantity(promotionCode, productCode);
	}

	@Override
	public long getReserverdQuantity(final String promotionCode, final String productCode, final String promotionMatcher)
	{
		validateParameterNotNull(promotionCode, "Promotion Code can not be null");
		validateParameterNotNull(productCode, "Product Code can not be null");
		validateParameterNotNull(promotionMatcher, "promotion Matcher can not be null");

		return getFlashbuyPromotionDao().getReserverdQuantity(promotionCode, productCode, promotionMatcher);
	}

	@Override
	public boolean enqueue(final String promotionCode, final String productCode, final String customerUID, final long quantity)
			throws MultipleEnqueueException
	{
		validateParameterNotNull(promotionCode, "Promotion Code can not be null");
		validateParameterNotNull(productCode, "Product Code can not be null");
		validateParameterNotNull(customerUID, "Customer Uid can not be null");

		final FlashbuyPromotionModel flashbuyPromotion = (FlashbuyPromotionModel) getFlashbuyPromotionDao().getPromotionForCode(
				promotionCode);
		if (flashbuyPromotion != null)
		{
			//request quantity should be no more than availableUnitsPerUserAndProduct limitation
			final long requestQuantity = Math.min(quantity, flashbuyPromotion.getAvailableUnitsPerUserAndProduct());

			// check whether the customer has already have enqueue record for this flashbuy promotion
			if (!getFlashbuyPromotionDao().getEnqueueforRemoval(productCode, promotionCode, customerUID).isEmpty()
					|| hasEnqueuedBefore(promotionCode, productCode, customerUID))
			{
				if (LOG.isDebugEnabled())
				{
					LOG.debug("Customer '" + customerUID + "' enqueue failed for trying to enqueue multiple times.");
				}
				throw new MultipleEnqueueException("Customer '" + customerUID
						+ "' enqueue failed for trying to enqueue multiple times.");
			}
			if (requestQuantity > 0)
			{
				for (int i = 0; i < requestQuantity; i++)
				{
					final PromotionEnqueueModel promotionEnqueueModel = getModelService().create(PromotionEnqueueModel.class);
					promotionEnqueueModel.setCustomerUID(customerUID);
					promotionEnqueueModel.setProductCode(productCode);
					promotionEnqueueModel.setPromotionCode(promotionCode);

					getModelService().save(promotionEnqueueModel);
				}
				if (LOG.isDebugEnabled())
				{
					LOG.debug("Request quantity is " + quantity + " ; approved quantity is " + requestQuantity + " .");
				}
				return true;

			}
		}
		return false;
	}

	@Override
	public boolean reserve(final String promotionCode, final String productCode, final String customerUID,
			final String promotionMatcher)
	{
		validateParameterNotNull(promotionCode, "Promotion Code can not be null");
		validateParameterNotNull(productCode, "Product Code can not be null");
		validateParameterNotNull(customerUID, "Customer Uid can not be null");
		validateParameterNotNull(promotionMatcher, "Promotion Matcher can not be null");

		final List<PromotionEnqueueModel> enqueueModels = getFlashbuyPromotionDao().getEnqueueforRemoval(productCode,
				promotionCode, customerUID);
		if (enqueueModels == null || enqueueModels.isEmpty())
		{
			LOG.error("Reserve failed. No record for enqueue with ");
			LOG.error("promotion: " + promotionCode);
			LOG.error("product: " + productCode);
			LOG.error("customer: " + customerUID);
			return false;
		}

		for (final PromotionEnqueueModel enqueueForRemoval : enqueueModels)
		{
			final PromotionReservationModel promotionReservationModel = getModelService().create(PromotionReservationModel.class);
			promotionReservationModel.setPromotionMatcher(promotionMatcher);
			promotionReservationModel.setProductCode(productCode);
			promotionReservationModel.setPromotionCode(promotionCode);

			getModelService().remove(enqueueForRemoval);
			getModelService().save(promotionReservationModel);
		}

		return true;
	}

	@Override
	public boolean allocate(final String promotionCode, final String productCode, final String promotionMatcher)
	{
		validateParameterNotNull(promotionCode, "Promotion Code can not be null");
		validateParameterNotNull(productCode, "Product Code can not be null");
		validateParameterNotNull(promotionMatcher, "Promotion Matcher can not be null");

		final List<PromotionReservationModel> reservationModels = getFlashbuyPromotionDao().getReservationforRemoval(productCode,
				promotionCode, promotionMatcher);
		if (reservationModels == null || reservationModels.isEmpty())
		{
			LOG.error("Allocate failed. No record for reservation with ");
			LOG.error("promotion: " + promotionCode);
			LOG.error("product: " + productCode);
			LOG.error("promotionMatcher: " + promotionMatcher);
			return false;
		}

		for (final PromotionReservationModel reserveForRemoval : reservationModels)
		{
			final PromotionAllocationModel promotionAllocationModel = getModelService().create(PromotionAllocationModel.class);
			promotionAllocationModel.setPromotionMatcher(promotionMatcher);
			promotionAllocationModel.setProductCode(productCode);
			promotionAllocationModel.setPromotionCode(promotionCode);

			getModelService().remove(reserveForRemoval);
			getModelService().save(promotionAllocationModel);
		}

		return true;
	}

	protected boolean hasEnqueuedBefore(final String promotionCode, final String productCode, final String customerUID)
	{

		final List<String> promotionMatcherList = new ArrayList<String>();
		final List<String> reservePromotionMacherList = getFlashbuyPromotionDao().getReservePromotionMatcherByPromotionAndProduct(
				promotionCode, productCode);
		final List<String> allocatePromotionMatcherList = getFlashbuyPromotionDao()
				.getAllocatePromotionMatcherByPromotionAndProduct(promotionCode, productCode);

		promotionMatcherList.addAll(reservePromotionMacherList);
		promotionMatcherList.addAll(allocatePromotionMatcherList);

		for (final String promotionMatcher : promotionMatcherList)
		{
			final AbstractOrderEntryModel abstractEntryModel = getFlashbuyPromotionDao().getAbstractOrderEntryByPromotionMatcher(
					promotionMatcher);
			if (abstractEntryModel != null && customerUID.equals(abstractEntryModel.getOrder().getUser().getUid()))
			{
				return true;
			}
		}
		return false;
	}

	protected FlashbuyPromotionDao getFlashbuyPromotionDao()
	{
		return flashbuyPromotionDao;
	}

	@Required
	public void setFlashbuyPromotionDao(final FlashbuyPromotionDao flashbuyPromotionDao)
	{
		this.flashbuyPromotionDao = flashbuyPromotionDao;
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

}

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
package de.hybris.platform.financialfacades.util;

import de.hybris.platform.commercefacades.insurance.data.CustomerFormSessionData;
import de.hybris.platform.commercefacades.insurance.data.FormSessionData;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.financialfacades.strategies.CustomerFormPrePopulateStrategy;
import de.hybris.platform.storefront.form.data.FormDetailData;
import de.hybris.platform.xyformsfacades.data.YFormDataData;
import de.hybris.platform.xyformsfacades.data.YFormDefinitionData;
import de.hybris.platform.xyformsfacades.form.YFormFacade;
import de.hybris.platform.xyformsservices.enums.YFormDataTypeEnum;
import de.hybris.platform.xyformsservices.exception.YFormServiceException;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;


public class DefaultInsuranceCheckoutHelper implements InsuranceCheckoutHelper
{

	private static final Logger LOG = Logger.getLogger(DefaultInsuranceCheckoutHelper.class);
	private YFormFacade yFormFacade;
	private CustomerFormPrePopulateStrategy customerFormPrePopulateStrategy;

	/**
	 * Builds up a refId for YFormData
	 *
	 * @param orderCode
	 * @param orderEntryNumber
	 * @return String YFormData refId
	 * */
	@Override
	public String buildYFormDataRefId(final String orderCode, final Integer orderEntryNumber)
	{
		final StringBuilder refIdBuilder = new StringBuilder();

		refIdBuilder.append(orderCode);
		refIdBuilder.append('_');
		refIdBuilder.append(orderEntryNumber);

		return refIdBuilder.toString();
	}

	/**
	 * Transforms the inputs into a FormDetailData object
	 *
	 * @param yFormDefinitionData
	 * @param orderCode
	 * @param orderEntryNumber
	 * @return FormDetailData
	 * */
	@Override
	public FormDetailData createFormDetailData(final YFormDefinitionData yFormDefinitionData, final String orderCode,
			final Integer orderEntryNumber)
	{
		final FormDetailData formDetailData = new FormDetailData();
		formDetailData.setApplicationId(yFormDefinitionData.getApplicationId());
		formDetailData.setFormId(yFormDefinitionData.getFormId());
		formDetailData.setRefId(buildYFormDataRefId(orderCode, orderEntryNumber));
		formDetailData.setOrderEntryNumber(orderEntryNumber);
		return formDetailData;
	}

	/**
	 * Transforms the inputs into a FormDetailData objects
	 *
	 * @param yFormDefinitionDataList
	 * @param orderCode
	 * @param orderEntryNumber
	 * @return List<FormDetailData>
	 * */
	@Override
	public List<FormDetailData> createFormDetailData(final List<YFormDefinitionData> yFormDefinitionDataList,
			final String orderCode, final Integer orderEntryNumber)
	{
		final List<FormDetailData> formDetailDataList = Lists.newArrayList();

		if (CollectionUtils.isNotEmpty(yFormDefinitionDataList))
		{
			CustomerFormSessionData customerFormSessionData = null;
			if (Boolean.TRUE.equals(getCustomerFormPrePopulateStrategy().hasCustomerFormDataStored()))
			{
				customerFormSessionData = getCustomerFormPrePopulateStrategy().getCustomerFormData();
				getCustomerFormPrePopulateStrategy().removeStoredCustomerFormData();
			}

			for (final YFormDefinitionData yFormDefinitionData : yFormDefinitionDataList)
			{
				if (customerFormSessionData != null && customerFormSessionData.getFormSessionData() != null)
				{
					for (final FormSessionData formSessionData : customerFormSessionData.getFormSessionData())
					{
						if (canCloneYFormDataModel(orderCode, orderEntryNumber, formSessionData))
						{
							cloneYFormDataModel(yFormDefinitionData, orderCode, orderEntryNumber, formSessionData);
						}
					}
				}
				formDetailDataList.add(createFormDetailData(yFormDefinitionData, orderCode, orderEntryNumber));
			}
		}

		return formDetailDataList;
	}

	/**
	 * Returns the OrderEntryData based on the formPageId
	 *
	 * @param cartData
	 * @param formPageId
	 * @return OrderEntryData
	 * */
	@Override
	public OrderEntryData getOrderEntryDataByFormPageId(final CartData cartData, final String formPageId)
	{
		for (final OrderEntryData orderEntryData : cartData.getEntries())
		{
			final int bundleNo = orderEntryData.getBundleNo();
			if (bundleNo == Integer.parseInt(formPageId))
			{
				return orderEntryData;
			}
		}
		return null;
	}

	/**
	 * Returns the FormPageId from the OrderEntryData
	 *
	 * @param orderEntryData
	 * @return String
	 * */
	@Override
	public String getFormPageIdByOrderEntryData(final OrderEntryData orderEntryData)
	{
		return String.valueOf(orderEntryData.getBundleNo());
	}

	/**
	 * The helper method to check whehter the session YFormData can be cloned.
	 *
	 * @param orderCode
	 *           the order code
	 * @param orderEntryNumber
	 *           the order entry number
	 * @param formSessionData
	 *           the formSessionData
	 * @return boolean
	 */
	protected boolean canCloneYFormDataModel(final String orderCode, final Integer orderEntryNumber,
			final FormSessionData formSessionData)
	{
		return !buildYFormDataRefId(orderCode, orderEntryNumber).equals(formSessionData.getYFormDataRefId());
	}

	/**
	 * Clone the yform data by given form session data.
	 *
	 * @param yFormDefinitionData
	 * @param orderCode
	 * @param orderEntryNumber
	 * @param formSessionData
	 */
	protected void cloneYFormDataModel(final YFormDefinitionData yFormDefinitionData, final String orderCode,
			final Integer orderEntryNumber, final FormSessionData formSessionData)
	{
		Assert.notNull(yFormDefinitionData, "yFormDefinitionData cannot be null.");
		Assert.notNull(orderCode, "orderCode cannot be null");
		Assert.notNull(orderEntryNumber, "orderEntryNumber cannot be null");
		Assert.notNull(formSessionData, "formSessionData cannot be null");

		try
		{
			YFormDataData yFormData = null;
			try
			{
				yFormData = getYFormFacade().getYFormData(formSessionData.getYFormDataId());
			}
			catch (final YFormServiceException e)
			{
				LOG.info("There is no DRAFT/DATA version for the requested form data");
			}

			if (yFormData != null && yFormData.getFormDefinition().getApplicationId().equals(yFormDefinitionData.getApplicationId())
					&& yFormData.getFormDefinition().getFormId().equals(yFormDefinitionData.getFormId())
					&& yFormData.getFormDefinition().getVersion() == yFormDefinitionData.getVersion())
			{
				final String formDataId = getYFormFacade().getNewFormDataId();

				// Clone a DRAFT version with the pre-populated data content.
				// The refId have to be empty for DRAFT version
				getYFormFacade().createYFormData(yFormDefinitionData.getApplicationId(), yFormDefinitionData.getFormId(), formDataId,
						YFormDataTypeEnum.DRAFT, StringUtils.EMPTY, yFormData.getContent());

				// Create a DATA version without the pre-populated data content.
				// The refId have to be the orderCode_orderEntryNumber for DATA version.
				getYFormFacade().createYFormData(yFormDefinitionData.getApplicationId(), yFormDefinitionData.getFormId(), formDataId,
						YFormDataTypeEnum.DATA, buildYFormDataRefId(orderCode, orderEntryNumber), StringUtils.EMPTY);
			}
		}
		catch (final YFormServiceException e)
		{
			LOG.warn(e.getMessage(), e);
		}
	}

	protected YFormFacade getYFormFacade()
	{
		return yFormFacade;
	}

	@Required
	public void setyFormFacade(final YFormFacade yFormFacade)
	{
		this.yFormFacade = yFormFacade;
	}

	protected CustomerFormPrePopulateStrategy getCustomerFormPrePopulateStrategy()
	{
		return customerFormPrePopulateStrategy;
	}

	@Required
	public void setCustomerFormPrePopulateStrategy(final CustomerFormPrePopulateStrategy customerFormPrePopulateStrategy)
	{
		this.customerFormPrePopulateStrategy = customerFormPrePopulateStrategy;
	}
}

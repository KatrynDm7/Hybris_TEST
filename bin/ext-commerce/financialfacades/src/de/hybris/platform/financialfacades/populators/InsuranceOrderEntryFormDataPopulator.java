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
package de.hybris.platform.financialfacades.populators;

import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.financialfacades.util.InsuranceCheckoutHelper;
import de.hybris.platform.xyformsfacades.data.YFormDataData;
import de.hybris.platform.xyformsfacades.data.YFormDefinitionData;
import de.hybris.platform.xyformsfacades.form.YFormFacade;
import de.hybris.platform.xyformsfacades.strategy.GetYFormDefinitionsForProductStrategy;
import de.hybris.platform.xyformsservices.enums.YFormDataTypeEnum;
import de.hybris.platform.xyformsservices.exception.YFormServiceException;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;


/**
 * The class of InsuranceFormDataPopulator.
 */
public class InsuranceOrderEntryFormDataPopulator implements Populator<AbstractOrderEntryModel, OrderEntryData>
{
	private static final Logger LOG = Logger.getLogger(InsuranceOrderEntryFormDataPopulator.class);

	private GetYFormDefinitionsForProductStrategy getYFormDefinitionsForProductStrategy;

	private InsuranceCheckoutHelper insuranceCheckoutHelper;

	private YFormFacade yFormFacade;

	@Override
	public void populate(final AbstractOrderEntryModel orderEntryModel, final OrderEntryData orderEntryData)
	{
		Assert.notNull(orderEntryModel, "orderEntryModel cannot be null");
		Assert.notNull(orderEntryData, "orderEntryData cannot be null");

		final List<YFormDataData> yFormDataDatas = Lists.newArrayList();

		if (orderEntryModel.getOrder() != null && orderEntryModel.getOrder().getCode() != null
				&& orderEntryData.getEntryNumber() != null)
		{
			try
			{
				final List<YFormDefinitionData> yFormDefinitions = getGetYFormDefinitionsForProductStrategy().execute(
						orderEntryData.getProduct().getCode());

				if (CollectionUtils.isNotEmpty(yFormDefinitions))
				{

					for (final YFormDefinitionData yFormDefinition : yFormDefinitions)
					{

						final YFormDataData yFormData = getyFormFacade().getYFormData(
								yFormDefinition.getApplicationId(),
								yFormDefinition.getFormId(),
								getInsuranceCheckoutHelper().buildYFormDataRefId(orderEntryModel.getOrder().getCode(),
										orderEntryData.getEntryNumber()), YFormDataTypeEnum.DATA);

						if (yFormData != null)
						{
							yFormDataDatas.add(yFormData);

							try
							{
								// if there is form DATA exists, we need to check if there is DRAFT exists,
								// and add the DRAFT version of form into list.
								final YFormDataData yFormDraftData = getyFormFacade().getYFormData(yFormData.getId(),
										YFormDataTypeEnum.DRAFT);
								if (yFormDraftData != null)
								{
									// We only add the yFormDraftData into the formDataData list if it is exists.
									yFormDataDatas.add(yFormDraftData);
								}
							}
							catch (final YFormServiceException e)
							{
								LOG.info("If there is no form draft data found, we just continue the loop");
							}
						}
					}
				}
			}
			catch (final YFormServiceException e)
			{
				LOG.error(e.getMessage(), e);
			}
		}

		if (CollectionUtils.isNotEmpty(yFormDataDatas))
		{
			orderEntryData.setFormDataData(yFormDataDatas);
		}
	}

	protected GetYFormDefinitionsForProductStrategy getGetYFormDefinitionsForProductStrategy()
	{
		return getYFormDefinitionsForProductStrategy;
	}

	@Required
	public void setGetYFormDefinitionsForProductStrategy(
			final GetYFormDefinitionsForProductStrategy getYFormDefinitionsForProductStrategy)
	{
		this.getYFormDefinitionsForProductStrategy = getYFormDefinitionsForProductStrategy;
	}

	protected InsuranceCheckoutHelper getInsuranceCheckoutHelper()
	{
		return insuranceCheckoutHelper;
	}

	@Required
	public void setInsuranceCheckoutHelper(final InsuranceCheckoutHelper insuranceCheckoutHelper)
	{
		this.insuranceCheckoutHelper = insuranceCheckoutHelper;
	}

	protected YFormFacade getyFormFacade()
	{
		return yFormFacade;
	}

	@Required
	public void setyFormFacade(final YFormFacade yFormFacade)
	{
		this.yFormFacade = yFormFacade;
	}
}

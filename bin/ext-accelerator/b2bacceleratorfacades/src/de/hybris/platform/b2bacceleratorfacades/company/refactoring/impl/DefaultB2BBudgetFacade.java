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
package de.hybris.platform.b2bacceleratorfacades.company.refactoring.impl;

import de.hybris.platform.b2b.model.B2BBudgetModel;
import de.hybris.platform.b2b.model.B2BCostCenterModel;
import de.hybris.platform.b2bacceleratorfacades.api.company.BudgetFacade;
import de.hybris.platform.b2bacceleratorfacades.api.company.CostCenterFacade;
import de.hybris.platform.b2bacceleratorfacades.exception.EntityValidationException;
import de.hybris.platform.b2bacceleratorfacades.order.data.B2BBudgetData;
import de.hybris.platform.b2bacceleratorfacades.order.data.B2BCostCenterData;
import de.hybris.platform.b2bacceleratorfacades.order.populators.B2BBudgetReversePopulator;
import de.hybris.platform.b2bacceleratorfacades.search.data.BudgetSearchStateData;
import de.hybris.platform.b2bacceleratorservices.company.B2BCommerceBudgetService;
import de.hybris.platform.commercefacades.search.data.AutocompleteSuggestionData;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.List;

import org.apache.commons.beanutils.BeanPropertyValueEqualsPredicate;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;


public class DefaultB2BBudgetFacade implements BudgetFacade
{
	private Converter<B2BBudgetModel, B2BBudgetData> b2bBudgetConverter;
	private B2BCommerceBudgetService b2bCommerceBudgetService;
	private Converter<B2BCostCenterModel, B2BCostCenterData> b2bCostCenterConverter;
	private B2BBudgetReversePopulator b2bBudgetReversePopulator;
	private ModelService modelService;
	private CostCenterFacade costCenterFacade;

	@Override
	public B2BBudgetData getBudgetDataForCode(final String budgetCode)
	{
		B2BBudgetData b2BBudgetData = null;
		final B2BBudgetModel budgetModel = b2bCommerceBudgetService.getBudgetModelForCode(budgetCode);
		if (budgetModel != null)
		{
			b2BBudgetData = b2bBudgetConverter.convert(budgetModel);
			if (CollectionUtils.isNotEmpty(budgetModel.getCostCenters()))
			{
				b2BBudgetData.setCostCenters(Converters.convertAll(budgetModel.getCostCenters(), b2bCostCenterConverter));
			}
		}
		return b2BBudgetData;
	}

	@Override
	public void updateBudget(final B2BBudgetData b2BBudgetData)
	{
		final B2BBudgetModel b2BBudgetModel = b2bCommerceBudgetService.getBudgetModelForCode(b2BBudgetData.getOriginalCode());
		if (b2BBudgetModel != null)
		{
			b2bBudgetReversePopulator.populate(b2BBudgetData, b2BBudgetModel);
			modelService.save(b2BBudgetModel);
		}
	}

	@Override
	public void addBudget(final B2BBudgetData b2BBudgetData)
	{
		final B2BBudgetModel b2BBudgetModel = modelService.create(B2BBudgetModel.class);
		b2bBudgetReversePopulator.populate(b2BBudgetData, b2BBudgetModel);
		modelService.save(b2BBudgetModel);
	}

	@Override
	public void enableDisableBudget(final String b2BudgetCode, final boolean active)
	{
		final B2BBudgetModel b2BBudgetModel = b2bCommerceBudgetService.getBudgetModelForCode(b2BudgetCode);
		if (b2BBudgetModel != null)
		{
			b2BBudgetModel.setActive(Boolean.valueOf(active));
			modelService.save(b2BBudgetModel);
		}
	}

	@Override
	public SearchPageData<B2BBudgetData> search(final BudgetSearchStateData searchState, final PageableData pageableData)
	{
		SearchPageData<B2BBudgetData> searchPageData = null;

		final SearchPageData<B2BBudgetModel> b2BBudgets = b2bCommerceBudgetService.findPagedBudgets(pageableData);
		searchPageData = convertPageData(b2BBudgets, b2bBudgetConverter);

		if (searchState != null && searchState.getCostCenterCode() != null)
		{
			final B2BCostCenterData costCenter = costCenterFacade.getCostCenterDataForCode(searchState.getCostCenterCode());
			for (final B2BBudgetData budgetData : searchPageData.getResults())
			{
				budgetData.setSelected(CollectionUtils.find(costCenter.getB2bBudgetData(), new BeanPropertyValueEqualsPredicate(
						B2BBudgetModel.CODE, budgetData.getCode())) != null);
			}
		}

		return searchPageData;
	}

	protected <S, T> SearchPageData<T> convertPageData(final SearchPageData<S> source, final Converter<S, T> converter)
	{
		final SearchPageData<T> result = new SearchPageData<T>();
		result.setPagination(source.getPagination());
		result.setSorts(source.getSorts());
		result.setResults(Converters.convertAll(source.getResults(), converter));
		return result;
	}

	@Override
	public List<AutocompleteSuggestionData> autocomplete(final BudgetSearchStateData searchState)
	{
		// YTODO Auto-generated method stub
		return null;
	}

	protected Converter<B2BBudgetModel, B2BBudgetData> getB2bBudgetConverter()
	{
		return b2bBudgetConverter;
	}

	@Required
	public void setB2bBudgetConverter(final Converter<B2BBudgetModel, B2BBudgetData> b2bBudgetConverter)
	{
		this.b2bBudgetConverter = b2bBudgetConverter;
	}

	protected B2BCommerceBudgetService getB2bCommerceBudgetService()
	{
		return b2bCommerceBudgetService;
	}

	@Required
	public void setB2bCommerceBudgetService(final B2BCommerceBudgetService b2bCommerceBudgetService)
	{
		this.b2bCommerceBudgetService = b2bCommerceBudgetService;
	}

	protected Converter<B2BCostCenterModel, B2BCostCenterData> getB2bCostCenterConverter()
	{
		return b2bCostCenterConverter;
	}

	@Required
	public void setB2bCostCenterConverter(final Converter<B2BCostCenterModel, B2BCostCenterData> b2bCostCenterConverter)
	{
		this.b2bCostCenterConverter = b2bCostCenterConverter;
	}

	protected B2BBudgetReversePopulator getB2bBudgetReversePopulator()
	{
		return b2bBudgetReversePopulator;
	}

	@Required
	public void setB2bBudgetReversePopulator(final B2BBudgetReversePopulator b2bBudgetReversePopulator)
	{
		this.b2bBudgetReversePopulator = b2bBudgetReversePopulator;
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

	protected CostCenterFacade getCostCenterFacade()
	{
		return costCenterFacade;
	}

	@Required
	public void setCostCenterFacade(final CostCenterFacade costCenterFacade)
	{
		this.costCenterFacade = costCenterFacade;
	}
}

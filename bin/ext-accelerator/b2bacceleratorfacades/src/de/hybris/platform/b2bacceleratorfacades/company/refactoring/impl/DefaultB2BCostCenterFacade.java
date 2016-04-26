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

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.b2b.model.B2BBudgetModel;
import de.hybris.platform.b2b.model.B2BCostCenterModel;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.services.B2BCostCenterService;
import de.hybris.platform.b2bacceleratorfacades.api.company.CostCenterFacade;
import de.hybris.platform.b2bacceleratorfacades.order.data.B2BCostCenterData;
import de.hybris.platform.b2bacceleratorfacades.order.data.B2BSelectionData;
import de.hybris.platform.b2bacceleratorfacades.order.populators.B2BCostCenterReversePopulator;
import de.hybris.platform.b2bacceleratorservices.company.B2BCommerceBudgetService;
import de.hybris.platform.b2bacceleratorservices.company.B2BCommerceCostCenterService;
import de.hybris.platform.commercefacades.search.data.AutocompleteSuggestionData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.springframework.beans.factory.annotation.Required;


public class DefaultB2BCostCenterFacade implements CostCenterFacade
{
	private B2BCommerceCostCenterService b2BCommerceCostCenterService;
	private B2BCostCenterService<B2BCostCenterModel, B2BCustomerModel> b2BCostCenterService;
	private Converter<B2BCostCenterModel, B2BCostCenterData> b2BCostCenterConverter;
	private B2BCostCenterReversePopulator b2BCostCenterReversePopulator;
	private B2BCommerceBudgetService b2BCommerceBudgetService;
	private ModelService modelService;

	@Override
	public List<? extends B2BCostCenterData> getCostCenters()
	{
		return Converters.convertAll(b2BCostCenterService.getAllCostCenters(), b2BCostCenterConverter);
	}

	@Override
	public List<? extends B2BCostCenterData> getActiveCostCenters()
	{
		final Collection costCenters = CollectionUtils.select(b2BCostCenterService.getAllCostCenters(), new Predicate()
		{
			@Override
			public boolean evaluate(final Object object)
			{
				return ((B2BCostCenterModel) object).getActive().booleanValue();
			}
		});
		return Converters.convertAll(costCenters, b2BCostCenterConverter);
	}

	@Override
	public B2BCostCenterData getCostCenterDataForCode(final String costCenterCode)
	{
		validateParameterNotNullStandardMessage("costCenterCode", costCenterCode);
		final B2BCostCenterModel b2bCostCenterModel = b2BCommerceCostCenterService.getCostCenterForCode(costCenterCode);
		return b2BCostCenterConverter.convert(b2bCostCenterModel);
	}

	@Override
	public SearchPageData<B2BCostCenterData> search(final SearchStateData searchState, final PageableData pageableData)
	{
		final SearchPageData<B2BCostCenterModel> costCenters = getB2BCommerceCostCenterService().getPagedCostCenters(pageableData);
		final SearchPageData<B2BCostCenterData> costCentersPageData = convertPageData(costCenters, b2BCostCenterConverter);

		return costCentersPageData;
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
	public List<AutocompleteSuggestionData> autocomplete(final SearchStateData searchState)
	{
		// YTODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateCostCenter(final B2BCostCenterData b2BCostCenterData)
	{
		final B2BCostCenterModel b2BCostCenterModel = b2BCommerceCostCenterService.getCostCenterForCode(b2BCostCenterData
				.getOriginalCode());
		if (b2BCostCenterModel != null)
		{
			b2BCostCenterReversePopulator.populate(b2BCostCenterData, b2BCostCenterModel);
			modelService.save(b2BCostCenterModel);
		}
	}

	@Override
	public void addCostCenter(final B2BCostCenterData b2BCostCenterData)
	{
		final B2BCostCenterModel b2BCostCenterModel = modelService.create(B2BCostCenterModel.class);
		b2BCostCenterReversePopulator.populate(b2BCostCenterData, b2BCostCenterModel);
		modelService.save(b2BCostCenterModel);

	}

	@Override
	public void enableDisableCostCenter(final String costCenterCode, final boolean active)
	{
		final B2BCostCenterModel b2BCostCenterModel = b2BCommerceCostCenterService.getCostCenterForCode(costCenterCode);
		if (b2BCostCenterModel != null && b2BCostCenterModel.getUnit() != null
				&& Boolean.TRUE.equals(b2BCostCenterModel.getUnit().getActive()))
		{
			b2BCostCenterModel.setActive(Boolean.valueOf(active));
			modelService.save(b2BCostCenterModel);
		}
	}


	@Override
	public B2BSelectionData selectBudgetForCostCenter(final String costCenterCode, final String budgetCode)

	{
		final B2BCostCenterModel b2BCostCenterModel = b2BCommerceCostCenterService.getCostCenterForCode(costCenterCode);
		final Set<B2BBudgetModel> budgetModelSet = new HashSet<B2BBudgetModel>(b2BCostCenterModel.getBudgets());
		final B2BBudgetModel b2BBudgetModel = b2BCommerceBudgetService.getBudgetModelForCode(budgetCode);
		budgetModelSet.add(b2BBudgetModel);
		b2BCostCenterModel.setBudgets(budgetModelSet);
		modelService.save(b2BCostCenterModel);

		return B2BCompanyUtils.createB2BSelectionData(b2BBudgetModel.getCode(), true, b2BBudgetModel.getActive().booleanValue());

	}

	@Override
	public B2BSelectionData deSelectBudgetForCostCenter(final String costCenterCode, final String budgetCode)

	{
		final B2BCostCenterModel b2BCostCenterModel = b2BCommerceCostCenterService.getCostCenterForCode(costCenterCode);
		final Set<B2BBudgetModel> budgetModelSet = new HashSet<B2BBudgetModel>(b2BCostCenterModel.getBudgets());
		final B2BBudgetModel b2BBudgetModel = b2BCommerceBudgetService.getBudgetModelForCode(budgetCode);
		if (b2BBudgetModel != null)
		{
			budgetModelSet.remove(b2BBudgetModel);
		}

		b2BCostCenterModel.setBudgets(budgetModelSet);
		modelService.save(b2BCostCenterModel);

		if (b2BBudgetModel != null)
		{
			return B2BCompanyUtils
					.createB2BSelectionData(b2BBudgetModel.getCode(), false, b2BBudgetModel.getActive().booleanValue());
		}

		throw new IllegalStateException("The b2BBudgetModel must be found in the system");
	}

	protected B2BCommerceCostCenterService getB2BCommerceCostCenterService()
	{
		return b2BCommerceCostCenterService;
	}

	@Required
	public void setB2BCommerceCostCenterService(final B2BCommerceCostCenterService b2bCommerceCostCenterService)
	{
		b2BCommerceCostCenterService = b2bCommerceCostCenterService;
	}

	protected B2BCostCenterService<B2BCostCenterModel, B2BCustomerModel> getB2bCostCenterService()
	{
		return b2BCostCenterService;
	}

	@Required
	public void setB2bCostCenterService(final B2BCostCenterService<B2BCostCenterModel, B2BCustomerModel> b2BCostCenterService)
	{
		this.b2BCostCenterService = b2BCostCenterService;
	}

	protected Converter<B2BCostCenterModel, B2BCostCenterData> getB2bCostCenterConverter()
	{
		return b2BCostCenterConverter;
	}

	@Required
	public void setB2bCostCenterConverter(final Converter<B2BCostCenterModel, B2BCostCenterData> b2BCostCenterConverter)
	{
		this.b2BCostCenterConverter = b2BCostCenterConverter;
	}

	protected B2BCostCenterReversePopulator getB2BCostCenterReversePopulator()
	{
		return b2BCostCenterReversePopulator;
	}

	@Required
	public void setB2BCostCenterReversePopulator(final B2BCostCenterReversePopulator b2bCostCenterReversePopulator)
	{
		b2BCostCenterReversePopulator = b2bCostCenterReversePopulator;
	}

	protected B2BCommerceBudgetService getB2BCommerceBudgetService()
	{
		return b2BCommerceBudgetService;
	}

	@Required
	public void setB2BCommerceBudgetService(final B2BCommerceBudgetService b2bCommerceBudgetService)
	{
		b2BCommerceBudgetService = b2bCommerceBudgetService;
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

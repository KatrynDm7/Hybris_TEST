package de.hybris.platform.warehousing.util.models;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.Collection;
import java.util.function.Supplier;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;


public abstract class AbstractItems<T extends ItemModel>
{
	private ModelService modelService;
	private FlexibleSearchService flexibleSearchService;

	protected T getFromCollectionOrSaveAndReturn(final Supplier<Collection<T>> getter, final Supplier<T> creator)
	{
		T model = null;
		if (!CollectionUtils.isEmpty(getter.get()))
		{
			model = getter.get().iterator().next();
		}
		else
		{
			model = creator.get();
			getModelService().save(model);
		}
		return model;
	}

	protected T getOrSaveAndReturn(final Supplier<T> getter, final Supplier<T> creator)
	{
		T model = null;
		try
		{
			model = getter.get();
			if (model == null)
			{
				throw new ModelNotFoundException("DAO returned null.");
			}
		}
		catch (final ModelNotFoundException | UnknownIdentifierException e)
		{
			model = creator.get();
			getModelService().save(model);
		}
		return model;
	}

	public ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	public FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}

	@Required
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}
}

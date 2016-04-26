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
package de.hybris.platform.configurablebundlefacades.order.converters.populator;

import de.hybris.platform.commercefacades.order.data.AbstractOrderData;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.configurablebundlefacades.converters.populator.ProductBundlePopulator;
import de.hybris.platform.configurablebundlefacades.data.BundleTemplateData;
import de.hybris.platform.configurablebundlefacades.order.converters.comparator.AbstractBundleOrderEntryComparator;
import de.hybris.platform.configurablebundleservices.bundle.BundleCommerceCartService;
import de.hybris.platform.configurablebundleservices.bundle.BundleTemplateService;
import de.hybris.platform.configurablebundleservices.bundle.impl.DefaultBundleCommerceCartService;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;


public class BundleCartPotentialProductsPopulator<S extends CartModel, T extends CartData> extends
		AbstractBundleOrderPopulator<S, T>
{
	private BundleCommerceCartService bundleCommerceCartService;
	private BundleTemplateService bundleTemplateService;

	private static final String POTENTIAL_PRODUCT_COUNT_KEY = "configurablebundlefacades.bundlecartpopulator.potentialproduct.count";

	private static final int DEFAULT_PRODUCT_COUNT = 5;

	private Collection<AbstractBundleOrderEntryComparator<OrderEntryData>> orderEntryDataComparators;
    
    private ProductBundlePopulator productBundlePopulator;


	/**
	 * This template method which coordinates the data population
	 */
	@Override
	public void populate(final S source, final T target)
	{
        Assert.notNull(source, "source cannot be null");
        Assert.notNull(target, "target cannot be null");
        final List<OrderEntryData> entriesList = getPotentialProducts(source, target);

		mergeEntries(entriesList, target);

		final List<OrderEntryData> sortedEntries = sortingOrderEntries(target.getEntries());
		target.setEntries(sortedEntries);

	}

	/**
	 * Method to populate the potential products as OrderEntryData for the each Bundle component that have been added in
	 * the cart.
	 * 
	 * @param source
	 * @param target
	 * @return list of existing order entry and the potential products
	 */
	protected List<OrderEntryData> getPotentialProducts(final AbstractOrderModel source, final AbstractOrderData target)
	{
		// List which holds the potential products from all the component.
		final List<OrderEntryData> potentialEntriesList = new ArrayList<>();

		//local variable which holds the bundle component used to eliminate the hunt for the potential product
		//if it is already done.
		final List<String> visitedTemplate = new ArrayList<>();

		if (CollectionUtils.isNotEmpty(target.getEntries()))
		{
			for (final OrderEntryData orderEntry : target.getEntries())
			{
				
                if (orderEntry.getProduct() != null && orderEntry.getProduct().getBundleTemplates() == null)
                {
                    final CartEntryModel entryModel = getCartService().getEntryForNumber((CartModel) source, orderEntry.getEntryNumber());
                    getProductBundlePopulator().populate(entryModel.getProduct(), orderEntry.getProduct());
                }
                
                //from the product get the component Ids in which the product is part of
				if (orderEntry.getBundleNo() > DefaultBundleCommerceCartService.NO_BUNDLE && orderEntry.getProduct() != null
						&& orderEntry.getProduct().getBundleTemplates() != null)
				{
					for (final BundleTemplateData template : orderEntry.getProduct().getBundleTemplates())
					{
						// get the potential product only if it is not done for any of the products that belongs to same component
						if (template.getId() != null && !visitedTemplate.contains(template.getId()))
						{
							visitedTemplate.add(template.getId());

							final List<CartEntryModel> bundleEntries = bundleCommerceCartService.getCartEntriesForComponentInBundle(
									(CartModel) source, bundleTemplateService.getBundleTemplateForCode(template.getId()),
									orderEntry.getBundleNo());

							if (bundleEntries != null)
							{
								final int maxPotentialProducts = maxPotentialProducts(template, bundleEntries.size());

								if (maxPotentialProducts > 0 && getCartMaxPotentialProductCount(target, template) < maxPotentialProducts)
								{
									// call to create new OrderEntryData for each bundle component
									potentialEntriesList.addAll(populatePotentialOrderentry(target, template, maxPotentialProducts,
											orderEntry.getProduct().getBundleTemplates()));
								}
							}
						}
					}
				}
			}
		}

		return potentialEntriesList;
	}

    /**
	 * Get the count number for current cart DTO's potential product.
	 * 
	 * @param orderData
	 * @param template
	 * @return
	 */
	private int getCartMaxPotentialProductCount(final AbstractOrderData orderData, final BundleTemplateData template)
	{
		int cnt = 0;
		for (final OrderEntryData entryData : orderData.getEntries())
		{
			if (entryData.getComponent() != null && template.getId() != null && template.getVersion() != null)
			{
				if (entryData.getBundleNo() == DefaultBundleCommerceCartService.NO_BUNDLE
						&& template.getId().equals(entryData.getComponent().getId())
						&& template.getVersion().equals(entryData.getComponent().getVersion()))
				{
					cnt++;
				}
			}
		}

		return cnt;
	}

	/**
	 * Method to get the possible potential product count for the each Bundle component that have been added in the cart.
	 * 
	 * @param bundleTemplate
	 * @param productsInCart
	 * @return returns the maximum possible potential product count
	 */
	protected int maxPotentialProducts(final BundleTemplateData bundleTemplate, final int productsInCart)
	{
		//* When number of selected products in a bundle cart component matches upper limit of selection criteria, then the potential product should be zero for that Bundle component.
		//* When number of selected products in a bundle cart component is lower then upper limit of selection criteria, 
		//	  then show available potential products up to configured maximum.
		final int configuredNumberofProducts = Config.getInt(POTENTIAL_PRODUCT_COUNT_KEY, DEFAULT_PRODUCT_COUNT);

		final int maxItemsAllowed = (bundleTemplate.getMaxItemsAllowed() - productsInCart) > 0 ? configuredNumberofProducts : 0;

		return maxItemsAllowed;
	}


	/**
	 * Helper method used to populate the potential products as OrderEntryData
	 * 
	 * @param target
	 * @param template
	 * @param numberOfPotentialProduct
	 * @return List of potential products as OrderEntryData
	 */
	protected List<OrderEntryData> populatePotentialOrderentry(final AbstractOrderData target, final BundleTemplateData template,
			final int numberOfPotentialProduct, final List<BundleTemplateData> allBundleTemplates)
	{
		final List<OrderEntryData> potentialEntriesList = new ArrayList<>();

		if (target != null && template != null && CollectionUtils.isNotEmpty(template.getProducts()))
		{
			for (final ProductData productData : template.getProducts())
			{
				if (productData != null)
				{
					// create the OrderEntry only for the product which is not exist in the cart.
					if (!isDuplicateProduct(target, productData, template) && potentialEntriesList.size() < numberOfPotentialProduct)
					{
						potentialEntriesList.add(createOrderEntryData(productData, template));
						productData.setBundleTemplates(allBundleTemplates);
					}
				}
			}
		}

		return potentialEntriesList;

	}

	/**
	 * Helper method used to create OrderEntryData for the given product.
	 * 
	 * @param product
	 * @return OrderEntryData
	 */
	protected OrderEntryData createOrderEntryData(final ProductData product, final BundleTemplateData bundleTemplate)
	{
		final OrderEntryData orderEntry = new OrderEntryData();

		orderEntry.setAddable(true);
		orderEntry.setProduct(product);
		orderEntry.setComponent(bundleTemplate);
		return orderEntry;
	}

	/**
	 * Helper method used to find whether the given product that belongs to the Bundle component is already been part of
	 * cart entry.
	 * 
	 * @param target
	 * @param productData
	 * @param template
	 * @return true if the product already exists in the cart
	 */
	protected boolean isDuplicateProduct(final AbstractOrderData target, final ProductData productData,
			final BundleTemplateData template)
	{
		boolean isDuplicate = false;

		if (target != null && CollectionUtils.isNotEmpty(target.getEntries()) && productData != null)
		{
			for (final OrderEntryData orderEntry : target.getEntries())
			{
				if (orderEntry != null && template != null && orderEntry.getComponent() != null && orderEntry.getProduct() != null
						&& template.getId() != null && productData.getCode().equals(orderEntry.getProduct().getCode())
						&& template.getId().equals(orderEntry.getComponent().getId()))
				{
					isDuplicate = true;
					break;
				}
			}
		}

		return isDuplicate;
	}

	/**
	 * Helper method to merge the potential product list to the existing OrderEntry.
	 * 
	 * @param potentialProduct
	 * @param target
	 */
	protected void mergeEntries(final List<OrderEntryData> potentialProduct, final AbstractOrderData target)
	{
		final List<OrderEntryData> consolidatedEntries = target.getEntries();

		consolidatedEntries.addAll(potentialProduct);

		target.setEntries(consolidatedEntries);
	}

	/**
	 * Method which delegates the method call to AbstractBundleOrderEntryComparator which sorts the OrderDataEntry based
	 * on product position that have been added to the component.
	 * 
	 * @param orderEntries
	 * @return sorted list of OrderEntryData
	 */
	protected List<OrderEntryData> sortingOrderEntries(final List<OrderEntryData> orderEntries)
	{
		for (final AbstractBundleOrderEntryComparator<OrderEntryData> comparator : getOrderEntryDataComparators())
		{
			Collections.sort(orderEntries, comparator);
		}
		return orderEntries;
	}

	@Required
	public void setBundleCommerceCartService(final BundleCommerceCartService bundleCommerceCartService)
	{
		this.bundleCommerceCartService = bundleCommerceCartService;
	}

	protected BundleCommerceCartService getBundleCommerceCartService()
	{
		return bundleCommerceCartService;
	}

	@Override
	@Required
	public void setBundleTemplateService(final BundleTemplateService bundleTemplateService)
	{
		this.bundleTemplateService = bundleTemplateService;
	}

	@Override
	protected BundleTemplateService getBundleTemplateService()
	{
		return bundleTemplateService;
	}

	protected Collection<AbstractBundleOrderEntryComparator<OrderEntryData>> getOrderEntryDataComparators()
	{
		return orderEntryDataComparators;
	}

	@Required
	public void setOrderEntryDataComparators(
			final Collection<AbstractBundleOrderEntryComparator<OrderEntryData>> orderEntryDataComparators)
	{
		this.orderEntryDataComparators = orderEntryDataComparators;
	}

    protected ProductBundlePopulator getProductBundlePopulator()
    {
        return productBundlePopulator;
    }

    @Required
    public void setProductBundlePopulator(ProductBundlePopulator productBundlePopulator)
    {
        this.productBundlePopulator = productBundlePopulator;
    }
}

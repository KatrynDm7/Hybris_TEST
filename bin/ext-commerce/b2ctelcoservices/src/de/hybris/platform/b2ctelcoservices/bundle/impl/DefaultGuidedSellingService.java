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
package de.hybris.platform.b2ctelcoservices.bundle.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.b2ctelcoservices.bundle.GuidedSellingService;
import de.hybris.platform.b2ctelcoservices.model.DeviceModel;
import de.hybris.platform.b2ctelcoservices.model.ServiceAddOnModel;
import de.hybris.platform.configurablebundleservices.bundle.AbstractBundleComponentEditableChecker;
import de.hybris.platform.configurablebundleservices.bundle.BundleTemplateService;
import de.hybris.platform.configurablebundleservices.model.BundleTemplateModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;


/**
 * Default implementation for {@link GuidedSellingService}.
 */
public class DefaultGuidedSellingService implements GuidedSellingService
{
	private BundleTemplateService bundleTemplateService;
	private AbstractBundleComponentEditableChecker<CartModel> bundleComponentEditableChecker;

	@Override
	public BundleTemplateModel getRelativeSelectionComponent(final AbstractOrderModel masterAbstractOrder,
			final BundleTemplateModel bundleTemplate, final int bundleNo, final int relativeposition)
	{
		validateParameterNotNullStandardMessage("masterAbstractOrder", masterAbstractOrder);
		validateParameterNotNullStandardMessage("bundleTemplate", bundleTemplate);

		BundleTemplateModel subsequentBundleModel = getBundleTemplateService().getRelativeBundleTemplate(bundleTemplate,
				relativeposition);

		// if there is no previous one, choose the next existing one
		if (subsequentBundleModel == null && relativeposition < 0)
		{
			int currentPos = relativeposition;
			do
			{
				subsequentBundleModel = getBundleTemplateService().getRelativeBundleTemplate(bundleTemplate, ++currentPos);
			}
			while (subsequentBundleModel == null && currentPos < 0);
		}

		// now check that if is a valid one, otherwise go to the next one
		while (subsequentBundleModel != null
				&& !isComponentToBeDisplayedOnGuidedSellingSelectPage(masterAbstractOrder, subsequentBundleModel, bundleNo,
						DeviceModel.class, ServiceAddOnModel.class))
		{
			subsequentBundleModel = getBundleTemplateService().getSubsequentBundleTemplate(subsequentBundleModel);
		}
		return subsequentBundleModel;
	}

	@Override
	public boolean isComponentToBeDisplayedOnGuidedSellingSelectPage(final AbstractOrderModel masterAbstractOrder,
			final BundleTemplateModel bundleTemplate, final int bundleNo, final Class<? extends ProductModel>... clazzes)
	{
		validateParameterNotNullStandardMessage("masterAbstractOrder", masterAbstractOrder);
		validateParameterNotNullStandardMessage("bundleTemplate", bundleTemplate);
		Assert.isInstanceOf(CartModel.class, masterAbstractOrder, "The masterAbstractOrder must be type of CartModel");

		if (!getBundleTemplateService().isAutoPickComponent(bundleTemplate)
				&& getBundleTemplateService().containsComponenentProductsOfType(bundleTemplate, clazzes)
				&& getBundleComponentEditableChecker().canEdit((CartModel) masterAbstractOrder, bundleTemplate, bundleNo))
		{
			return true;
		}
		return false;
	}

	protected BundleTemplateService getBundleTemplateService()
	{
		return bundleTemplateService;
	}

	@Required
	public void setBundleTemplateService(final BundleTemplateService bundleTemplateService)
	{
		this.bundleTemplateService = bundleTemplateService;
	}


	protected AbstractBundleComponentEditableChecker<CartModel> getBundleComponentEditableChecker()
	{
		return bundleComponentEditableChecker;
	}

	@Required
	public void setBundleComponentEditableChecker(
			final AbstractBundleComponentEditableChecker<CartModel> bundleComponentEditableChecker)
	{
		this.bundleComponentEditableChecker = bundleComponentEditableChecker;
	}
}

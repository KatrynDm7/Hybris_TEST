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
package de.hybris.platform.b2ctelcostorefront.controllers.cms;

import de.hybris.platform.addonsupport.controllers.cms.AbstractCMSAddOnComponentController;
import de.hybris.platform.b2ctelcofacades.product.TelcoProductFacade;
import de.hybris.platform.b2ctelcostorefront.model.ProductReferencesAndClassificationsComponentModel;
import de.hybris.platform.b2ctelcostorefront.controllers.TelcoControllerConstants;
import de.hybris.platform.b2ctelcostorefront.controllers.util.ProductDataHelper;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;


/**
 * Component controller that displays controller for the product references and feature compatible products.
 */
@Controller("ProductReferencesAndClassificationsComponentController")
@RequestMapping(value = TelcoControllerConstants.Actions.Cms.ProductReferencesAndClassificationsComponent)
public class ProductReferencesAndClassificationsComponentController extends
        AbstractCMSAddOnComponentController<ProductReferencesAndClassificationsComponentModel>
{
    protected static final List<ProductOption> PRODUCT_OPTIONS = Arrays.asList(ProductOption.BASIC, ProductOption.PRICE,
            ProductOption.SUMMARY);

    @Resource(name = "telcoProductFacade")
    private TelcoProductFacade telcoProductFacade;

	@Override
	protected void fillModel(final HttpServletRequest request, final Model model,
                             final ProductReferencesAndClassificationsComponentModel component)
	{
        ServicesUtil.validateParameterNotNull(component, "CMSComponent must not be null");

		final List<ProductData> referenceAndClassificationsProducts = telcoProductFacade
				.getProductReferencesAndFeatureCompatibleProductsForCode(ProductDataHelper.getCurrentProduct(request),
                        component.getProductReferenceTypes(),
                        PRODUCT_OPTIONS,
                        component.getMaximumNumberProducts(),
                        component.getClassAttributeAssignment(),
                        component.getTargetItemType());

		model.addAttribute("title", component.getTitle());

		model.addAttribute("productAccessories", referenceAndClassificationsProducts);
	}

    public void setTelcoProductFacade(final TelcoProductFacade telcoProductFacade)
    {
        this.telcoProductFacade = telcoProductFacade;
    }

    public TelcoProductFacade getTelcoProductFacade()
    {
        return telcoProductFacade;
    }
}

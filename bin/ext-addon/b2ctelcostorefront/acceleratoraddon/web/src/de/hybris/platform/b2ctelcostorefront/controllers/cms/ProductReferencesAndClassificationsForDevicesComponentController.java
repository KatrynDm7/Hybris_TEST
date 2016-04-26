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

import de.hybris.platform.b2ctelcostorefront.model.ProductReferencesAndClassificationsComponentModel;
import de.hybris.platform.b2ctelcostorefront.model.ProductReferencesAndClassificationsForDevicesComponentModel;
import de.hybris.platform.b2ctelcostorefront.controllers.TelcoControllerConstants;
import de.hybris.platform.b2ctelcostorefront.controllers.util.ProductDataHelper;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * Component controller that handles product references, feature compatible and vendor compatible products.
 */
@Controller("ProductReferencesAndClassificationsForDevicesComponentController")
@RequestMapping(value = TelcoControllerConstants.Actions.Cms.ProductReferencesAndClassificationsForDevicesComponent)
public class ProductReferencesAndClassificationsForDevicesComponentController extends
        ProductReferencesAndClassificationsComponentController
{
	@Override
	protected void fillModel(final HttpServletRequest request, final Model model,
                             final ProductReferencesAndClassificationsComponentModel component)
	{
        ServicesUtil.validateParameterNotNull(component, "CMSComponent must not be null");

        if(component instanceof ProductReferencesAndClassificationsForDevicesComponentModel)
        {
            final ProductReferencesAndClassificationsForDevicesComponentModel prComponent
                    = (ProductReferencesAndClassificationsForDevicesComponentModel) component;

            final List<ProductData> referenceAndClassificationsProducts = getTelcoProductFacade()
                    .getProductReferencesAndFeatureCompatibleAndVendorCompatibleProductsForCode(
                            ProductDataHelper.getCurrentProduct(request), prComponent.getProductReferenceTypes(),
                            ProductReferencesAndClassificationsComponentController.PRODUCT_OPTIONS,
                            prComponent.getMaximumNumberProducts(),
                            prComponent.getClassAttributeAssignment(), prComponent.getTargetItemType());

            model.addAttribute("title", prComponent.getTitle());

            model.addAttribute("productAccessories", referenceAndClassificationsProducts);
        }
        else
        {
            LOG.info("Unsupported CMSComponent type for item [" + component
                    + "]: expected 'ProductReferencesAndClassificationsForDevicesComponentModel', but was '"
                    + component.getClass().getSimpleName() + "'");
        }
    }
}

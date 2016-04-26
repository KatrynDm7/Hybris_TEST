package de.hybris.platform.configurablebundlefacades.order.converters.populator;

import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.configurablebundleservices.bundle.BundleCommerceCartService;
import de.hybris.platform.configurablebundleservices.bundle.impl.DefaultBundleCommerceCartService;
import de.hybris.platform.configurablebundleservices.model.BundleTemplateModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;


/**
 * Bundling specific converter implementation for {@link CartModel} as source and {@link CartData} as target type.
 */
public class BundleCartPotentialProductDisableRulePopulator<S extends CartModel, T extends CartData> extends
		AbstractBundleOrderPopulator<S, T>
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(BundleCartPotentialProductDisableRulePopulator.class);

	private ProductService productService;

	private BundleCommerceCartService bundleCommerceCartService;

	@Override
	public void populate(final S cartModel, final T cartData)
	{
		Assert.notNull(cartModel, "parameter cartModel cannot be null.");
		Assert.notNull(cartData, "parameter cartData cannot be null.");

		if (cartModel.getEntries() != null && cartData.getEntries() != null)
		{
			for (final AbstractOrderEntryModel orderEntryModel : cartModel.getEntries())
			{
				for (final OrderEntryData orderEntry : cartData.getEntries())
				{
					if (orderEntry.getBundleNo() == DefaultBundleCommerceCartService.NO_BUNDLE)
					{
						// The loop only take place when the order entry bundle No is 0, this mean it is a potential product.
						if (orderEntry.getProduct() != null && orderEntry.getProduct().getCode() != null)
						{
							if (orderEntry.isAddable() && !orderEntry.getProduct().isDisabled())
							{
								// We only take the loop when the order entry is addable, and product is enabled.
								final ProductModel productModel = getProductService().getProductForCode(
										orderEntry.getProduct().getCode());
								if (productModel != null && orderEntry.getComponent() != null)
								{
									final BundleTemplateModel productBundle = getBundleTemplateService().getBundleTemplateForCode(
											orderEntry.getComponent().getId(), orderEntry.getComponent().getVersion());
									final String disableMessage = getBundleCommerceCartService()
											.checkAndGetReasonForDisabledProductInComponent(cartModel, productModel, productBundle,
													orderEntryModel.getBundleNo(), false);
									if (disableMessage != null)
									{
										orderEntry.getProduct().setDisabled(true);
										orderEntry.setAddable(false);
										orderEntry.setRemoveable(false);
									}
								}
							}
						}
					}

				}
			}
		}
	}

	protected ProductService getProductService()
	{
		return productService;
	}

	@Required
	public void setProductService(final ProductService productService)
	{
		this.productService = productService;
	}

	protected BundleCommerceCartService getBundleCommerceCartService()
	{
		return bundleCommerceCartService;
	}

	@Required
	public void setBundleCommerceCartService(final BundleCommerceCartService bundleCommerceCartService)
	{
		this.bundleCommerceCartService = bundleCommerceCartService;
	}
}

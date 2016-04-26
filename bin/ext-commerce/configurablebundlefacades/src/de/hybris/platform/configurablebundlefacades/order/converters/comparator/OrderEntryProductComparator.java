package de.hybris.platform.configurablebundlefacades.order.converters.comparator;

import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.configurablebundleservices.model.BundleTemplateModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import org.springframework.beans.factory.annotation.Required;


/**
 * The class of OrderEntryProductComparator.
 */
public class OrderEntryProductComparator extends AbstractBundleOrderEntryComparator<OrderEntryData>
{

	private ProductService productService;

	@Override
	protected int doCompare(OrderEntryData o1, OrderEntryData o2)
	{

		Integer o1ProductPOS = null;
		Integer o2ProductPOS = null;

		if (o1 != null && o1.getComponent() != null && o1.getProduct() != null && o1.getProduct().getCode() != null &&
				o1.getComponent().getId() != null && o1.getComponent().getVersion() != null)
		{
			final ProductModel product = getProductService().getProductForCode(o1.getProduct().getCode());

			if (product != null)
			{
				final BundleTemplateModel bundleTemplate1 = getBundleTemplateService().getBundleTemplateForCode(
						o1.getComponent().getId(), o1.getComponent().getVersion());

				if (bundleTemplate1.getProducts() != null)
				{
					o1ProductPOS = bundleTemplate1.getProducts().indexOf(product);
				}
			}

		}

		if (o2 != null && o2.getComponent() != null && o2.getProduct() != null && o2.getProduct().getCode() != null &&
				o2.getComponent().getId() != null && o2.getComponent().getVersion() != null)
		{

			final ProductModel product = getProductService().getProductForCode(o2.getProduct().getCode());

			if (product != null)
			{
				final BundleTemplateModel bundleTemplate2 = getBundleTemplateService().getBundleTemplateForCode(
						o2.getComponent().getId(), o2.getComponent().getVersion());

				if (bundleTemplate2.getProducts() != null)
				{
					o2ProductPOS = bundleTemplate2.getProducts().indexOf(product);
				}

			}
		}

		if (o1ProductPOS != null && o2ProductPOS != null)
		{
			return o1ProductPOS.compareTo(o2ProductPOS);
		}

		return 0;
	}

	@Override
	protected boolean comparable(OrderEntryData o1, OrderEntryData o2)
	{
		if (o1 != null && o2 != null)
		{
			boolean comparable = o1.getBundleNo() == o2.getBundleNo();

			if (o1.getComponent() != null && o2.getComponent() != null)
			{
				if (o1.getComponent().getId() != null && o2.getComponent().getId() != null && o1.getComponent().getVersion() != null
						&& o2.getComponent().getVersion() != null)
				{
					comparable &= (o1.getComponent().getId().equals(o2.getComponent().getId()) && o1.getComponent().getVersion()
							.equals(o2.getComponent().getVersion()));
				}
				else
				{
					return o1.getComponent().getId() == null && o2.getComponent().getId() == null
							&& o1.getComponent().getVersion() == null && o2.getComponent().getVersion() == null;
				}
			}
			else
			{
				return o1.getComponent() == null && o2.getComponent() == null;
			}

			return comparable;
		}
		else
		{
			return false;
		}
	}

	protected ProductService getProductService()
	{
		return productService;
	}

	@Required
	public void setProductService(ProductService productService)
	{
		this.productService = productService;
	}
}

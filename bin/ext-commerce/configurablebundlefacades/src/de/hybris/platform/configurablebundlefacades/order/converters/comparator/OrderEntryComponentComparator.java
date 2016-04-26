package de.hybris.platform.configurablebundlefacades.order.converters.comparator;

import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.configurablebundlefacades.data.BundleTemplateData;
import de.hybris.platform.configurablebundleservices.model.BundleTemplateModel;


/**
 * The class of OrderEntryComponentComparator.
 */
public class OrderEntryComponentComparator extends AbstractBundleOrderEntryComparator<OrderEntryData>
{

	@Override
	protected int doCompare(OrderEntryData o1, OrderEntryData o2)
	{
		Integer o1Pos = null;
		Integer o2Pos = null;
		if (o1 != null && o1.getComponent() != null)
		{
			o1Pos = getPosition(o1.getComponent());
		}

		if (o2 != null && o2.getComponent() != null)
		{
			o2Pos = getPosition(o2.getComponent());
		}

		if (o1Pos != null && o2Pos != null)
		{
			return o1Pos.compareTo(o2Pos);
		}

		return 0;
	}

	protected Integer getPosition(final BundleTemplateData bundleTemplate)
	{
		if (bundleTemplate != null && bundleTemplate.getId() != null && bundleTemplate.getVersion() != null)
		{
			final BundleTemplateModel component = getBundleTemplateService().getBundleTemplateForCode(bundleTemplate.getId(),
					bundleTemplate.getVersion());
			if (component != null)
			{
				return getBundleTemplateService().getPositionInParent(component);
			}
		}

		return null;
	}

	@Override
	public boolean comparable(OrderEntryData o1, OrderEntryData o2)
	{
		return o1 != null && o2 != null && o1.getBundleNo() == o2.getBundleNo();
	}
}

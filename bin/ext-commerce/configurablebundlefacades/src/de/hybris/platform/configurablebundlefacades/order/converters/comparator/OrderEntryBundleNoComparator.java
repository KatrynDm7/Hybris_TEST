package de.hybris.platform.configurablebundlefacades.order.converters.comparator;

import de.hybris.platform.commercefacades.order.data.OrderEntryData;


/**
 * The class of OrderEntryBundleComparator.
 */
public class OrderEntryBundleNoComparator extends AbstractBundleOrderEntryComparator<OrderEntryData>
{
	@Override
	protected int doCompare(OrderEntryData o1, OrderEntryData o2)
	{
		// sort standalone items as last items
		if (o1 != null && o1.getBundleNo() == 0)
		{
			return 1;
		}
		if (o2 != null && o2.getBundleNo() == 0)
		{
			return -1;
		}

		// first comparing based on the bundleNo
		if (o1 != null && o2 != null)
		{
			final int compare = Integer.valueOf(o1.getBundleNo()).compareTo(o2.getBundleNo());
			if (compare != 0)
			{
				return compare;
			}
		}
        
        return 0;
	}

	@Override
	public boolean comparable(OrderEntryData o1, OrderEntryData o2)
	{
		return !(o1 == null && o2 == null);
	}
}

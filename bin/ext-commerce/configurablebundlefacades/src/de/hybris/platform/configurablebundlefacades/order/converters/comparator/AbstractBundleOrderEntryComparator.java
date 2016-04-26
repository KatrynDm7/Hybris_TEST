package de.hybris.platform.configurablebundlefacades.order.converters.comparator;

import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.configurablebundleservices.bundle.BundleTemplateService;
import org.springframework.beans.factory.annotation.Required;

import java.io.Serializable;
import java.util.Comparator;


/**
 * The class of BundleOrderEntryComparator.
 */
public abstract class AbstractBundleOrderEntryComparator<T extends OrderEntryData> implements Comparator<T>, Serializable
{

	private static final long serialVersionUID = -2497165242462930140L;

    private BundleTemplateService bundleTemplateService;
    
	@Override
	public int compare(T o1, T o2)
	{
		if (o1 != null && o2 != null && comparable(o1, o2))
		{
			return doCompare(o1, o2);
		}
		else
		{
			return 0;
		}
	}

	protected abstract int doCompare(T o1, T o2);

	protected abstract boolean comparable(T o1, T o2);

    protected BundleTemplateService getBundleTemplateService()
    {
        return bundleTemplateService;
    }
    
    @Required
    public void setBundleTemplateService(BundleTemplateService bundleTemplateService)
    {
        this.bundleTemplateService = bundleTemplateService;
    }
}

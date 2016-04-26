package de.hybris.platform.warehousing.util;

import de.hybris.platform.basecommerce.util.SpringCustomContextLoader;
import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import javax.annotation.Resource;

import org.junit.Ignore;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.test.context.ContextConfiguration;


@Ignore("Just a testing base class.")
@ContextConfiguration(locations =
{ "classpath:/warehousing-spring-test.xml" })
public class BaseWarehousingIntegrationTest extends ServicelayerTransactionalTest
{
	protected static SpringCustomContextLoader springCustomContextLoader = null;

	@Resource
	protected FlexibleSearchService flexibleSearchService;

	public BaseWarehousingIntegrationTest()
	{
		if (springCustomContextLoader == null)
		{
			try
			{
				springCustomContextLoader = new SpringCustomContextLoader(getClass());
				springCustomContextLoader.loadApplicationContexts((GenericApplicationContext) Registry.getGlobalApplicationContext());
				springCustomContextLoader.loadApplicationContextByConvention((GenericApplicationContext) Registry
						.getGlobalApplicationContext());
			}
			catch (final Exception e)
			{
				throw new RuntimeException(e.getMessage(), e);
			}
		}
	}
}
